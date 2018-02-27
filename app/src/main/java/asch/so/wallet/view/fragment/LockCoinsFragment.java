package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.Calendar;
import java.util.Date;

import asch.so.base.fragment.BaseFragment;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class LockCoinsFragment extends BaseFragment implements LockCoinsContract.View, View.OnClickListener,MyDateRecyclerViewAdapter.OnDateSelectListener {

    KProgressHUD hud=null;
    @BindView(R.id.second_passwd_ll)
    LinearLayout second_passwd_ll;
    @BindView(R.id.ok_btn)
    Button okBtn;
    @BindView(R.id.block_height_et)
    EditText blockHeightEt;
    @BindView(R.id.account_passwd_et)
    EditText accountPasswordEt;
    @BindView(R.id.second_passwd_et)
    EditText secondSecretEt;
    @BindView(R.id.ll_year)
    LinearLayout ll_year;
    @BindView(R.id.tv_year)
    TextView tv_year;
    @BindView(R.id.ll_month)
    LinearLayout ll_month;
    @BindView(R.id.tv_month)
    TextView tv_month;
    @BindView(R.id.ll_day)
    LinearLayout ll_day;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.block_height)
    TextView block_height;

    private LockCoinsContract.Presenter presenter;
    private EasyPopup esayPopup;
    private MyDateRecyclerViewAdapter yearAdapter,monthAdapter,dayAdapter;
    private long lastHeight = 0 ;
    private long haveLockHeight = 0;

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
        okBtn.setOnClickListener(this);
        blockHeightEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if("".equals(charSequence.toString())){
                    return;
                }
                long lockHeight = Long.valueOf(charSequence.toString());
                if(lockHeight>lastHeight){
                    initDate(lockHeight-lastHeight);
                }else{
                    initDate(0);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        presenter=new LockCoinsPresenter(getContext(), this);
        presenter.loadBlockInfo();
        if(!getAccount().hasSecondSecret()){
            second_passwd_ll.setVisibility(View.GONE);
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v==okBtn){
            String blockHeightStr = blockHeightEt.getText().toString();
            if(null==blockHeightStr||"".equals(blockHeightStr)){
                AppUtil.toastError(getContext(),getString(R.string.input_locked_Height));
                return;
            }

            if(Integer.valueOf(blockHeightStr)-lastHeight>=10000000){
                AppUtil.toastWarning(getActivity(),getString(R.string.lock_warning));
                return;
            }

            if(Integer.valueOf(blockHeightStr)-haveLockHeight<=0){
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
            presenter.lockCoins(Long.valueOf(blockHeightStr),account_pwd.trim(),getAccount().hasSecondSecret()?second_secret.trim():null);
        }
    }

    @OnClick(R.id.ll_year) void onClickYear() {
        initPopup(MyDateRecyclerViewAdapter.YEAR);
        esayPopup.showAtAnchorView(ll_year,VerticalGravity.BELOW,HorizontalGravity.CENTER);
    }

    @OnClick(R.id.ll_month) void onClickMonth() {
        initPopup(MyDateRecyclerViewAdapter.MONTH);
        esayPopup.showAtAnchorView(ll_month,VerticalGravity.BELOW,HorizontalGravity.CENTER);
    }

    @OnClick(R.id.ll_day) void onClickDay() {
        initPopup(MyDateRecyclerViewAdapter.DAY);
        esayPopup.showAtAnchorView(ll_day,VerticalGravity.BELOW,HorizontalGravity.CENTER);
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
        if (account!=null){
            FullAccount.BlockInfo blockInfo =account.getFullAccount().getLatestBlock();
            lastHeight = blockInfo.getHeight();
            block_height.setText(String.valueOf(lastHeight));
            haveLockHeight = account.getFullAccount().getAccount().getLockHeight();
            if(haveLockHeight>lastHeight){
                blockHeightEt.setText(haveLockHeight+"");
                initDate(haveLockHeight-lastHeight);
            }else{
                initDate(0);
            }
        }
    }

    private void initPopup(int type) {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.date_recyclerview,null);
        handleListView(type,contentView);
        esayPopup = new EasyPopup(getContext());
        if(type!=MyDateRecyclerViewAdapter.YEAR){
            esayPopup.setHeight(310);
        }
        esayPopup.setContentView(contentView)
                .setAnimationStyle(R.style.DatePopAnim)
                .setFocusAndOutsideEnable(true)
                .createPopup();

    }

    private void handleListView(int type,View contentView){
        if (contentView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) contentView;
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            if(null==yearAdapter){
                yearAdapter = new MyDateRecyclerViewAdapter(getActivity(),MyDateRecyclerViewAdapter.YEAR,this);
                yearAdapter.notifyDataSetChanged();
            }
            if(null==monthAdapter){
                monthAdapter = new MyDateRecyclerViewAdapter(getActivity(),MyDateRecyclerViewAdapter.MONTH,this);
                monthAdapter.notifyDataSetChanged();
            }
            if(null==dayAdapter){
                dayAdapter = new MyDateRecyclerViewAdapter(getActivity(),MyDateRecyclerViewAdapter.DAY,this);
                dayAdapter.notifyDataSetChanged();
            }
            switch (type){
                case MyDateRecyclerViewAdapter.YEAR:
                    recyclerView.setAdapter(yearAdapter);
                    break;
                case MyDateRecyclerViewAdapter.MONTH:
                    recyclerView.setAdapter(monthAdapter);
                    break;
                case MyDateRecyclerViewAdapter.DAY:
                    dayAdapter.onYearOrMonthChange(tv_year.getText().toString(),tv_month.getText().toString());
                    recyclerView.setAdapter(dayAdapter);
                    break;
            }
        }
    }

    public void onDateSelect(int type,String str) {
        esayPopup.dismiss();
        switch (type){
            case MyDateRecyclerViewAdapter.YEAR:
                tv_year.setText(str);
                reSetDay();
                break;
            case MyDateRecyclerViewAdapter.MONTH:
                tv_month.setText(str);
                reSetDay();
                break;
            case MyDateRecyclerViewAdapter.DAY:
                tv_day.setText(str);
                break;
        }
        setLockHeightByDate();
    }

    public void initDate(long differHeight){
        Calendar calendar = AppUtil.getDateByHeight(differHeight);
        tv_year.setText(calendar.get(Calendar.YEAR)+ getString(R.string.year));
        tv_month.setText(calendar.get(Calendar.MONTH)+1+getString(R.string.month));
        tv_day.setText(calendar.get(Calendar.DAY_OF_MONTH)+getString(R.string.day));
    }

    private void setLockHeightByDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,StrUtil.getNumbers(tv_year.getText().toString()));
        calendar.set(Calendar.MONTH,StrUtil.getNumbers(tv_month.getText().toString())-1);
        calendar.set(Calendar.DAY_OF_MONTH,StrUtil.getNumbers(tv_day.getText().toString()));
        long ms = calendar.getTimeInMillis()-System.currentTimeMillis();
        if(ms>0){
            blockHeightEt.setText(ms/10000 + lastHeight+"");
        }else{
            blockHeightEt.setText("");
        }
    }

    private void reSetDay(){
        int maxDay = dayAdapter.getMaxDay(tv_year.getText().toString(),tv_month.getText().toString());
        int day = StrUtil.getNumbers(tv_day.getText().toString());
        if(day>maxDay){
            tv_day.setText("1"+getString(R.string.day));
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
