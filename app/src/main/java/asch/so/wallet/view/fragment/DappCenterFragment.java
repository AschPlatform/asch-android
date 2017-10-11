package asch.so.wallet.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import asch.so.wallet.TestData;
import asch.so.wallet.activity.DappActivity;
import asch.so.wallet.contract.DappCenterContract;
import asch.so.wallet.model.entity.Balance;
import asch.so.wallet.model.entity.Dapp;
import asch.so.wallet.view.adapter.AssetsAdapter;
import asch.so.wallet.view.adapter.DappCenterAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/10/11.
 */

public class DappCenterFragment extends BaseFragment implements DappCenterContract.View{
    private static final String TAG = DappCenterFragment.class.getSimpleName();
    @BindView(R.id.dapp_list_rv)
    RecyclerView dappListRv;

    DappCenterContract.Presenter presenter;

    private List<Dapp> dappList=new ArrayList<>();
    private DappCenterAdapter adapter=new DappCenterAdapter(dappList);


    public static DappCenterFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DappCenterFragment fragment = new DappCenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_dapp_center,container,false);
        ButterKnife.bind(this,rootView);
        dappListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        dappListRv.setItemAnimator(new DefaultItemAnimator());
        dappListRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
//                Intent intent=new Intent(getActivity(), DappActivity.class);
//                startActivity(intent);
                deposit(TestData.dappID,"XAS",100,null,TestData.secret,null);
            }
        });

        return rootView;
    }

    public void deposit(String dappID,String currency,long amount, String message,String secret, String secondSecret) {

        Observable.create(new Observable.OnSubscribe<AschResult>(){

            @Override
            public void call(Subscriber<? super AschResult> subscriber) {
                AschResult result=null;
//                if (AppConstants.XAS_NAME.equals(currency)){
//                    result = AschSDK.Account.transfer(targetAddress,amount,message,secret,secondSecret);
//                }else {
//                    result = AschSDK.UIA.transfer(currency,targetAddress,amount,message,secret,secondSecret);
//                }
                result= AschSDK.Dapp.deposit(dappID,currency,amount,message,secret,secondSecret);
                if (result!=null && result.isSuccessful()){
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                }else {
                    subscriber.onError(result!=null?result.getException():new Exception("result is null"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AschResult>() {
                    @Override
                    public void call(AschResult aschResult) {
                        Log.i(TAG, "+++++++"+aschResult.getRawJson());
                        Toast.makeText(getContext(),"充值成功", Toast.LENGTH_SHORT).show();
                        //view.displayToast("转账成功");
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setPresenter(DappCenterContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void displayDappList(List<Dapp> dapps) {
        this.dappList.clear();
        this.dappList.addAll(dapps);
        adapter.notifyDataSetChanged();
    }
}
