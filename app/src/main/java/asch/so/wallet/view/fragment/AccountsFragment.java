package asch.so.wallet.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import java.util.List;

import asch.so.base.activity.BaseActivity;
import asch.so.base.fragment.BaseDialogFragment;
import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConfig;
import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.accounts.Wallet;
import asch.so.wallet.activity.AccountCreateActivity;
import asch.so.wallet.activity.AccountImportActivity;
import asch.so.wallet.activity.AccountsActivity;
import asch.so.wallet.activity.CheckPasswordActivity;
import asch.so.wallet.activity.QRCodeScanActivity;
import asch.so.wallet.contract.AccountsContract;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.presenter.AccountsPresenter;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.adapter.AccountsAdapter;
import asch.so.wallet.view.widget.InputPasswdDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 * 所有账户
 */

public class AccountsFragment extends BaseFragment implements AccountsContract.View,View.OnClickListener{
    private static final String TAG=AccountsFragment.class.getSimpleName();
    private final int FLAG_ADD_ACCOUNT = 0;
    private final int FLAG_IMPORT_ACCOUNT = 1;
    public final int FLAG_DEL_ACCOUNT = 2;
    View addBtn;
    EasyPopup moreEasyPopup;
    @BindView(R.id.accounts_rcv)
    SwipeMenuRecyclerView accountsRecycleView;
    private AccountsAdapter accountsAdapter;
    private Unbinder unbinder;
    private AccountsContract.Presenter presenter;

    public static AccountsFragment newInstance() {
        Bundle args = new Bundle();
        AccountsFragment fragment = new AccountsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_accounts, container, false);
        unbinder= ButterKnife.bind(this,rootView);
        Context ctx=rootView.getContext();
        setHasOptionsMenu(true);
        initPopupMenu();
        accountsRecycleView.setLayoutManager(new LinearLayoutManager(ctx));
        accountsRecycleView.setItemAnimator(new DefaultItemAnimator());
        accountsRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        accountsRecycleView.setSwipeMenuCreator(swipeMenuCreator);
        accountsRecycleView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        accountsAdapter=new AccountsAdapter();
        accountsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {

                Account account = (Account) baseQuickAdapter.getItem(position);
                presenter.setCurrentAccount(account);
                accountsAdapter.notifyDataSetChanged();
                getActivity().finish();
            }
        });

        accountsRecycleView.setAdapter(accountsAdapter);
        if (presenter!=null) {
            presenter.subscribe();
        }


        presenter=new AccountsPresenter(getContext(),this);
        presenter.loadSavedAccounts();

        return rootView;
    }


    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_72);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem del = new SwipeMenuItem(getActivity())
                    .setBackground(R.color.list_del)
                    .setText(R.string.delete)
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(del);
        }
    };


    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int adapterPosition = menuBridge.getAdapterPosition();
            delAccount( (Account) accountsAdapter.getItem(adapterPosition));
        }
    };

    public void addAccount(){
        checkPwd(FLAG_ADD_ACCOUNT,new Account());
    }

    public void impAccount(){
        checkPwd(FLAG_IMPORT_ACCOUNT,new Account());
    }

    public void delAccount(Account account){
        if (AccountsManager.getInstance().getAccountsCount()<2){
            AppUtil.toastError(getActivity(),getString(R.string.only_one_account));
            return;
        }
        checkPwd(FLAG_DEL_ACCOUNT,account);
    }

    private void checkPwd(int Flag,Account account){
        Bundle bundle = new Bundle();
        String title = null;
        if (Flag==FLAG_DEL_ACCOUNT){
            title = getString(R.string.delete_account);
            AccountsManager.getInstance().setDelAccount(account);
        }else if(Flag == FLAG_IMPORT_ACCOUNT){
            title = AccountsActivity.class.getSimpleName();
        }else if(Flag == FLAG_ADD_ACCOUNT){
            title = getString(R.string.add_account);
        }
        bundle.putString("title",title);
        BaseActivity.start(getActivity(),CheckPasswordActivity.class,bundle);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_btn:
            {
                if (addBtn==null)
                    addBtn = getActivity().getWindow().getDecorView().findViewById(R.id.menu_add_btn);
                showPopupMenu(addBtn, SizeUtils.dp2px(30), SizeUtils.dp2px(-12));
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showPopupMenu(View view, int offsetX, int offsetY) {
        moreEasyPopup.showAtAnchorView(view, VerticalGravity.BELOW, HorizontalGravity.LEFT, offsetX,offsetY);
    }

    private void initPopupMenu() {
        moreEasyPopup = new EasyPopup(getActivity())
                .setContentView(R.layout.menu_account)
                .setAnimationStyle(R.style.PopupMenuAnimation)
                .setFocusAndOutsideEnable(true)
                .createPopup();
        View contentView = moreEasyPopup.getContentView();
        View addItem = contentView.findViewById(R.id.add_ll);
        View importItem = contentView.findViewById(R.id.import_ll);

        addItem.setOnClickListener(this);
        importItem.setOnClickListener(this);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unSubscribe();
    }

    @Override
    public void setPresenter(AccountsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {

    }

    @Override
    public void displaySavedAccounts(List<Account> accountList) {
        this.accountsAdapter.replaceData(accountList);
    }

    @Override
    public void gotoCreateAccount() {

    }

    @Override
    public void gotoImportAccount() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.subscribe();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadSavedAccounts();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_ll) {
            moreEasyPopup.dismiss();
            addAccount();
        } else if (v.getId() == R.id.import_ll) {
            moreEasyPopup.dismiss();
            impAccount();
        }
    }
}
