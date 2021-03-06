package asch.io.wallet.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.contract.AccountDetailContract;
import asch.io.wallet.model.entity.Account;
import asch.io.wallet.presenter.AccountDetailPresenter;
import asch.io.wallet.util.IdenticonGenerator;
import asch.io.widget.toolbar.TitleToolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/9/21.
 */

public class AccountDetailFragment extends BaseFragment implements AccountDetailContract.View{

    @BindView(R.id.toolbar)
    TitleToolbar toolbar;
    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.balance_tv)
    TextView balanceTv;
    @BindView(R.id.ident_icon)
    ImageView identiconIv;
    AccountDetailContract.Presenter presenter;
    Unbinder unbinder;
    public static AccountDetailFragment newInstance() {
        
        Bundle args = new Bundle();
        
        AccountDetailFragment fragment = new AccountDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        presenter=new AccountDetailPresenter(getContext(),this);
        presenter.loadAccount(null);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribe();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }

    @Override
    public void setPresenter(AccountDetailContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {

    }

    @Override
    public void displayAccount(Account account) {
        this.addressTv.setText(account.getAddress());
        this.nameEt.setText(account.getName());
        this.balanceTv.setText(" XAS");
        IdenticonGenerator.getInstance().generateBitmap(account.getPublicKey(), new IdenticonGenerator.OnIdenticonGeneratorListener() {
            @Override
            public void onIdenticonGenerated(Bitmap bmp) {
                identiconIv.setImageBitmap(bmp);
            }
        });
    }
}
