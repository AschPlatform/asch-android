package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.AppConstants;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.contract.LockCoinsContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.model.entity.FullAccount;
import asch.io.wallet.presenter.LockCoinsPresenter;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.validator.Validator;
import asch.io.wallet.view.widget.DateTimePickerDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import so.asch.sdk.AschSDK;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.FeeCalculater;

/**
 */
public class LockCoinsFragment extends BaseFragment implements LockCoinsContract.View, View.OnClickListener,DateTimePickerDialog.ClickListenerInterface {

    KProgressHUD hud=null;
    @BindView(R.id.second_passwd_ll)
    LinearLayout second_passwd_ll;
    @BindView(R.id.ok_btn)
    Button okBtn;
    @BindView(R.id.lock_amount_et)
    EditText lockAmountEt;
    @BindView(R.id.account_passwd_et)
    EditText accountPasswordEt;
    @BindView(R.id.second_passwd_et)
    EditText secondSecretEt;
    @BindView(R.id.tv_date_time)
    TextView tv_date_time;
    @BindView(R.id.block_height)
    TextView block_height;
    @BindView(R.id.lock_fee_tv)
    TextView lockFeeTv;
    Unbinder unbinder;
    private  long lockHeight;
    private  long initMills;
    private Calendar minLockCalaendar;


    private LockCoinsContract.Presenter presenter;
   // private EasyPopup esayPopup;
   // private MyDateRecyclerViewAdapter yearAdapter,monthAdapter,dayAdapter;
    private long lastHeight = 0 ;
    private long haveLockHeight = 0;
    private Date dateTime;

    public LockCoinsFragment() {
    }

    public static LockCoinsFragment newInstance(String param1, String param2) {
        LockCoinsFragment fragment = new LockCoinsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lock_coins, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        //
        okBtn.setOnClickListener(this);
        presenter=new LockCoinsPresenter(getContext(), this);
        presenter.loadBlockInfo();
        presenter.queryLockFee();
        if(!getAccount().hasSecondSecret()){
            second_passwd_ll.setVisibility(View.GONE);
        }
        return rootView;
    }



    @Override
    public void onClick(View v) {
        if (v==okBtn){
            String lockAmountStr=lockAmountEt.getText().toString().trim();
            if (TextUtils.isEmpty(lockAmountStr)){
                AppUtil.toastError(getContext(),getString(R.string.input_locked_amount));
                return;
            }

            long lockAmount = AschSDK.Helper.amountForXAS(BigDecimal.valueOf(Long.valueOf(lockAmountStr)));
            if (lockAmount<=0){
                AppUtil.toastError(getContext(),getString(R.string.locked_low_limit));
                return;
            }


            long balance= getAccount().getXASLongBalance();
            if (lockAmount>balance){
                AppUtil.toastError(getContext(),getString(R.string.error_balance_insufficient));
                return;
            }
            long fee=FeeCalculater.calcFee(TransactionType.basic_lock);
            long lowLimit=AschSDK.Helper.amountForCoins(1);
            long maxLock=balance-fee-lowLimit;


            if ((balance-fee-lockAmount)<lowLimit){
                BigDecimal decimal= AppUtil.decimalFromBigint(maxLock,AppConstants.PRECISION);
                int maxLockInt= decimal.intValue();
                String errText=String.format(getString(R.string.locked_remain_balance_limit),maxLockInt);
                AppUtil.toastError(getContext(),errText);
                return;
            }

            if(lockHeight-lastHeight>=10000000){
                AppUtil.toastWarning(getActivity(),getString(R.string.lock_warning));
                return;
            }

            if(lockHeight-haveLockHeight<=0){
                AppUtil.toastWarning(getActivity(),getString(R.string.lock_warning_2));
                return;
            }

            String account_pwd = accountPasswordEt.getText().toString();
            if (!Validator.check(getContext(), Validator.Type.Password,account_pwd,getString(R.string.account_password_error))){
                return;
            }

            String second_secret = "";
            if(getAccount().hasSecondSecret()){
                second_secret = secondSecretEt.getText().toString();
                if (!Validator.check(getContext(), Validator.Type.SecondSecret,second_secret,getString(R.string.secondary_password_error))){
                    return;
                }
            }
            showHUD();

            presenter.lockCoins(lockAmount, lockHeight,account_pwd.trim(),getAccount().hasSecondSecret()?second_secret.trim():null);
        }
    }

    @OnClick(R.id.ll_date_time) void onClickDateTime(){
        DateTimePickerDialog dialog = new DateTimePickerDialog(getContext(),
                getString(R.string.date_time_picker_title),
                true,
                true,
                this.minLockCalaendar,
                this.minLockCalaendar,
                null

        );
        dialog.setClicklistener(this);
        dialog.show();
    }

    @Override
    public void doOk(Date time) {
        this.dateTime=time;
        tv_date_time.setText(TimeUtils.date2String(time));
        setLockHeightByDate();
    }

    @Override
    public void setPresenter(LockCoinsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayError(Throwable exception) {
        AppUtil.toastError(getContext(),AppUtil.extractInfoFromError(getContext(),exception));
        dismissHUD();
    }

    @Override
    public void displayLockCoinsResult(boolean success, String msg) {
        if (getActivity()==null)
            return;
        dismissHUD();
        if (success) {
            AppUtil.toastSuccess(getContext(), msg);
            getActivity().finish();
        }else {
            AppUtil.toastError(getContext(),msg);
        }
    }



    @Override
    public void displayBlockInfo(Account account) {
        if (account!=null && account.getFullAccount()!=null){
            FullAccount.BlockInfo blockInfo =account.getFullAccount().getLatestBlock();
            lastHeight = blockInfo.getHeight();
            block_height.setText(String.valueOf(lastHeight));
            haveLockHeight = account.getFullAccount().getAccount().getLockHeight();
//            long oneMonthHeight=30*24*60*60/10;
//            long nextLockHeight = haveLockHeight+oneMonthHeight;
//            long diffHeight=0;
//            if (nextLockHeight>lastHeight){
//                diffHeight=nextLockHeight-lastHeight;
//            }else {
//                diffHeight=oneMonthHeight;
//            }
//            initDate(diffHeight);
            initDate(lastHeight,haveLockHeight);
        }
//        else {
//            long oneMonthHeight=30*24*60*60/10;
//            initDate(oneMonthHeight);
//        }
    }


    @Override
    public void displayLockFee(String fee) {
        lockFeeTv.setText(fee);
    }

    public void initDate(long lastHeight, long lockedBlock){
        this.initMills=System.currentTimeMillis();
        long nextLockHeight = haveLockHeight+AppConstants.ONE_MONTH_BLOCKS;
        long diffHeight=0;
        if (nextLockHeight>lastHeight){
            diffHeight=nextLockHeight-lastHeight;
        }else {
            diffHeight=AppConstants.ONE_MONTH_BLOCKS;
        }
        this.minLockCalaendar=AppUtil.getDateByHeight(this.initMills, diffHeight+30);

        long lockedMillis;
        if (lockedBlock==0) {
            lockedMillis=this.initMills;
        }else
            if (lockedBlock<lastHeight){
            lockedMillis=this.initMills-(lastHeight-lockedBlock)*10*1000;
        }else {
            lockedMillis=this.initMills+(lockedBlock-lastHeight)*10*1000;
        }
        tv_date_time.setText(TimeUtils.millis2String(lockedMillis));
//
//
//        Calendar calendar = AppUtil.getDateByHeight(this.initMills, differHeight);
//        this.minLockCalaendar=calendar;
//
//        tv_date_time.setText(TimeUtils.date2String(calendar.getTime()));
    }

    private void setLockHeightByDate(){
//        long ms = this.dateTime.getTime()-System.currentTimeMillis();
        long ms = this.dateTime.getTime()-this.initMills+(System.currentTimeMillis()-this.initMills);
        if(ms>0){
            lockHeight=ms/10000 + lastHeight;
        }else{
            lastHeight=0;
        }
    }


    private  void  showHUD(){
        if (hud==null){
            hud = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(true)
                    .show();
        }
    }

    private  void  dismissHUD(){
        if (hud!=null){
            hud.dismiss();
        }
    }

    private Account getAccount() {
        return AccountsManager.getInstance().getCurrentAccount();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
    }
}
