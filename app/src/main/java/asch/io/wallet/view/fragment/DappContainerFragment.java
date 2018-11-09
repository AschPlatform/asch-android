package asch.io.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.contract.DappContainerContract;

/**
 * Created by kimziv on 2017/10/11.
 */

public class DappContainerFragment extends BaseFragment implements DappContainerContract.View{

    DappContainerContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setPresenter(DappContainerContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayError(java.lang.Throwable exception) {

    }


    @Override
    public void displayDapp() {

    }
}
