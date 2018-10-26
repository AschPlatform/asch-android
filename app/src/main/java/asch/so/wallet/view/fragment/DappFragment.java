package asch.so.wallet.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.TestData;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.util.IdenticonGenerator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/9/21.
 */

public class DappFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = DappFragment.class.getSimpleName();
    @BindView(R.id.dapp_webview)
    BridgeWebView webView;

    @BindView(R.id.test_btn)
    Button testBtn;
    @BindView(R.id.identicon)
    ImageView identiconIv;
    Unbinder unbinder;
    //Bitmap bitmap

    ValueCallback<Uri> mUploadMessage;
    int RESULT_CODE = 0;

    public static DappFragment newInstance() {

        Bundle args = new Bundle();

        DappFragment fragment = new DappFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dapp, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        webView.setDefaultHandler(new DefaultHandler());
        webView.setWebChromeClient(new WebChromeClient() {
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                //return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
                pickFile();
                return true;
            }
        });

        webView.loadUrl("file:///android_asset/jdenticontest.html");
        webView.registerHandler("jsbridgeDeposit", new BridgeHandler() {
            @Override
            public void handler(String input, CallBackFunction callBackFunction) {
                Log.i(TAG, "handler = submitFromWeb, data from web = " + input);
                JSONObject params = JSON.parseObject(input);
                String dappId = params.getString("dappId");
                String currency = params.getString("currency");
                long amount = (long) (Float.parseFloat(params.getString("amount")) * 100000000);
                deposit(dappId, currency, amount, null, TestData.secret, null, callBackFunction);
                //callBackFunction.onCallBack("haha"+data);
            }

        });

        testBtn.setOnClickListener(this);
        onClick(testBtn);
        return rootView;
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    public void deposit(String dappID, String currency, long amount, String message, String secret, String secondSecret, CallBackFunction callBack) {

        Observable.create(new Observable.OnSubscribe<AschResult>() {

            @Override
            public void call(Subscriber<? super AschResult> subscriber) {
                AschResult result = AschSDK.Dapp.deposit(dappID, currency, amount, message, secret, secondSecret);
                if (result != null && result.isSuccessful()) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(result != null ? result.getException() : new Exception("result is null"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AschResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AschResult aschResult) {
                        String rawJson = aschResult.getRawJson();
                        Log.i(TAG, "+++++++" + rawJson);
                        callBack.onCallBack(rawJson);
                        AppUtil.toastError(getContext(), "充值成功");
                    }
                });
//                .subscribe(new Action1<AschResult>() {
//                    @Override
//                    public void call(AschResult aschResult) {
//                        String rawJson=aschResult.getRawJson();
//                        Log.i(TAG, "+++++++"+rawJson);
//                        callBack.onCallBack(rawJson);
//                        Toast.makeText(getContext(),"充值成功", Toast.LENGTH_SHORT).show();
//                        //view.displayToast("转账成功");
//                    }
//                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    public void onClick(View view) {
        if (testBtn.equals(view)) {
            String value= "kimziv"+ SystemClock.currentThreadTimeMillis();
            IdenticonGenerator.getInstance().generateBitmap(value, new IdenticonGenerator.OnIdenticonGeneratorListener() {
                @Override
                public void onIdenticonGenerated(Bitmap bmp) {
                    identiconIv.setImageBitmap(bmp);
                }
            });
//            webView.callHandler("functionInJs", value, new CallBackFunction() {
//                @Override
//                public void onCallBack(String data) {
//                    // TODO Auto-generated method stub
////                    Log.i(TAG, "reponse data from js " + data);
//                    final String pureBase64Encoded = data.substring(data.indexOf(",")  + 1);
//                    byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
//                    Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//                    identiconIv.setImageBitmap(bmp);
//                    Log.i(TAG, "reponse data from js bitmap: " + bmp.getWidth()+", "+bmp.getHeight());
//                }
//            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
    }
}
