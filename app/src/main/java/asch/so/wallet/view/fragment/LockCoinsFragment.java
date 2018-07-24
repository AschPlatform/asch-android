package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import asch.so.base.fragment.BaseFragment;
import asch.so.base.util.DateConvertUtils;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.contract.LockCoinsContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.FullAccount;
import asch.so.wallet.presenter.LockCoinsPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.util.StrUtil;
import asch.so.wallet.view.adapter.MyDateRecyclerViewAdapter;
import asch.so.wallet.view.validator.Validator;
import asch.so.wallet.view.widget.DateTimePickerDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import so.asch.sdk.AschHelper;
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
//    @BindView(R.id.block_height_et)
//    EditText blockHeightEt;
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
    private  long lockHeight;


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
        ButterKnife.bind(this, rootView);
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

//            String blockHeightStr = blockHeightEt.getText().toString();
//            if(null==blockHeightStr||"".equals(blockHeightStr)){
//                AppUtil.toastError(getContext(),getString(R.string.input_locked_Height));
//                return;
//            }

//            if(Integer.valueOf(blockHeightStr)-lastHeight>=10000000){
//                AppUtil.toastWarning(getActivity(),getString(R.string.lock_warning));
//                return;
//            }
//
//            if(Integer.valueOf(blockHeightStr)-haveLockHeight<=0){
//                AppUtil.toastWarning(getActivity(),getString(R.string.lock_warning_2));
//                return;
//            }
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
        DateTimePickerDialog dialog = new DateTimePickerDialog(getContext(), getString(R.string.date_time_picker_title));
        dialog.setClicklistener(this);
        dialog.show();
    }

    @Override
    public void doOk(Date time) {
        this.dateTime=time;
    //reSetDay();
    tv_date_time.setText(TimeUtils.date2String(time));
    setLockHeightByDate();
    }

    //    @OnClick(R.id.ll_year) void onClickYear() {
//        initPopup(MyDateRecyclerViewAdapter.YEAR);
//        esayPopup.showAtAnchorView(ll_year,VerticalGravity.BELOW,HorizontalGravity.CENTER);
//    }
//
//    @OnClick(R.id.ll_month) void onClickMonth() {
//        initPopup(MyDateRecyclerViewAdapter.MONTH);
//        esayPopup.showAtAnchorView(ll_month,VerticalGravity.BELOW,HorizontalGravity.CENTER);
//    }
//
//    @OnClick(R.id.ll_day) void onClickDay() {
//        initPopup(MyDateRecyclerViewAdapter.DAY);
//        esayPopup.showAtAnchorView(ll_day,VerticalGravity.BELOW,HorizontalGravity.CENTER);
//    }

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
            if(haveLockHeight>lastHeight){
                //blockHeightEt.setText(haveLockHeight+"");
                lockHeight=haveLockHeight;
                initDate(haveLockHeight-lastHeight);
            }else{
                initDate(0);
            }
        }
    }


    @Override
    public void displayLockFee(String fee) {
        lockFeeTv.setText(fee);
    }

    public void initDate(long differHeight){
        Calendar calendar = AppUtil.getDateByHeight(differHeight);
        tv_date_time.setText(TimeUtils.date2String(calendar.getTime()));
//        tv_year.setText(calendar.get(Calendar.YEAR)+ getString(R.string.year));
//        tv_month.setText(calendar.get(Calendar.MONTH)+1+getString(R.string.month));
//        tv_day.setText(calendar.get(Calendar.DAY_OF_MONTH)+getString(R.string.day));
    }

    private void setLockHeightByDate(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR,StrUtil.getNumbers(tv_year.getText().toString()));
//        calendar.set(Calendar.MONTH,StrUtil.getNumbers(tv_month.getText().toString())-1);
//        calendar.set(Calendar.DAY_OF_MONTH,StrUtil.getNumbers(tv_day.getText().toString()));

//        long ms = calendar.getTimeInMillis()-System.currentTimeMillis();
        long ms = this.dateTime.getTime()-System.currentTimeMillis();
        if(ms>0){
            lockHeight=ms/10000 + lastHeight;
            //blockHeightEt.setText(ms/10000 + lastHeight+"");
        }else{
            lastHeight=0;
//            blockHeightEt.setText("");
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

}
