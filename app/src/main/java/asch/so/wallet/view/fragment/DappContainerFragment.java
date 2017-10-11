package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.contract.DappCenterContract;
import asch.so.wallet.contract.DappContainerContract;
import asch.so.wallet.model.entity.Dapp;

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
    public void displayDapp() {

    }
}
