package asch.so.wallet.cordova;

import com.github.lzyzsd.jsbridge.CallBackFunction;

import rx.Observable;
import rx.Subscriber;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;

/**
 * Created by kimziv on 2017/12/27.
 */

public class DappApiBridge {


    public Observable createDepositObservable(String dappID, String currency, long amount, String message, String secret, String secondSecret, CallBackFunction callBack) {

       return Observable.create(new Observable.OnSubscribe<AschResult>() {

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
        });
    }

    public Observable createWitdrawObservable(String dappID, String currency, long amount, String message, String secret, String secondSecret, CallBackFunction callBack) {

        return Observable.create(new Observable.OnSubscribe<AschResult>() {

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
        });
    }

    public Observable createInnertransferObservable(String dappID, String currency, long amount, String message, String secret, String secondSecret, CallBackFunction callBack) {

        return Observable.create(new Observable.OnSubscribe<AschResult>() {

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
        });
    }

    public Observable createSetNicknameObservable(String dappID, String currency, long amount, String message, String secret, String secondSecret, CallBackFunction callBack) {

        return Observable.create(new Observable.OnSubscribe<AschResult>() {

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
        });
    }
}
