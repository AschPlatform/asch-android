package asch.so.wallet.cordova;

import android.text.TextUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import asch.so.wallet.R;
import asch.so.wallet.accounts.AccountsManager;
import asch.so.wallet.model.entity.Account;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.util.AppUtil;
import asch.so.wallet.view.widget.AllPasswdsDialog;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschConst;

/**
 * Created by kimziv on 2018/1/17.
 */

public class CCTimePlugin extends CordovaPlugin {

    private static final String TAG=CCTimePlugin.class.getSimpleName();
    private AllPasswdsDialog dialog;

    public enum Action{
        PostArticle(1000,"cctime.postArticle"),
        PostComment(1001,"cctime.postComment"),
        VoteArticle(1002,"cctime.voteArticle"),
        LikeComment(1003,"cctime.likeComment"),
        Report(1004,"cctime.report")
        ;

        private static final Map<Integer,Action> allActions = new HashMap<>();
        static{
            for( Action action : Action.values()){
                allActions.put(action.getType(), action);
            }
        }

        private int type;
        private String name;

        Action(int type, String name) {
            this.type=type;
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static Action fromType(int type){
            return allActions.get(type);
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if(this.cordova.getActivity().isFinishing()) return true;

        if (Action.PostArticle.getName().equals(action)){
            String dappID=args.getString(0);
            long fee=args.getLong(1);
            String title=args.getString(2);
            String url=args.getString(3);
            String text=args.getString(4);
            String tags =args.getString(5);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new AllPasswdsDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                                postArticle(dappID, fee, title, url, text, tags, secret, callbackContext);
                        }
                    });
                }
            });
        }else if (Action.PostComment.getName().equals(action)){
            String dappID=args.getString(0);
            long fee=args.getLong(1);
            String aid=args.getString(2);
            String pid=args.getString(3);
            String content=args.getString(4);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new AllPasswdsDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                            postComment(dappID,fee, aid,pid,content, secret, callbackContext);
                        }
                    });
                }
            });
        }else if (Action.VoteArticle.getName().equals(action)){
            String dappID=args.getString(0);
            long fee=args.getLong(1);
            String aid=args.getString(2);
            long amount=args.getLong(3);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new AllPasswdsDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                            voteArticle(dappID,fee, aid,amount,secret,callbackContext);
                        }
                    });
                }
            });
        }else if (Action.LikeComment.getName().equals(action)){
            String dappID=args.getString(0);
            long fee=args.getLong(1);
            String cid=args.getString(2);
            long amount=args.getLong(3);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new AllPasswdsDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                            likeComment(dappID,fee, cid,amount,secret,callbackContext);
                        }
                    });
                }
            });
        }else if (Action.Report.getName().equals(action)){
            String dappID=args.getString(0);
            long fee=args.getLong(1);
            int topic=args.getInt(2);
            String value=args.getString(3);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new AllPasswdsDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(AllPasswdsDialog dialog, String secret, String secondSecret, String errMsg) {
                            report(dappID,fee, topic,value,secret,callbackContext);
                        }
                    });
                }
            });
        }
        //callbackContext.success();
        return true;
    }


    private Account getAccount(){
        return AccountsManager.getInstance().getCurrentAccount();
    }


    private void showPasswordDialog(AllPasswdsDialog.OnConfirmationListenner confirmationListenner){
        boolean hasSecondSecret=getAccount().hasSecondSecret();
        dialog = new AllPasswdsDialog(this.cordova.getContext(),hasSecondSecret);
        dialog.setTitle(this.cordova.getContext().getString(R.string.account_input_title));
        dialog.show(confirmationListenner);
    }

    private void dismissDialog(){
        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }

    /**
     1.1 发布文章
     合约编号：1000
     合约参数：
     title 文章标题，非空，最长256字符
     url 文章url，最长256字符
     text 发表内容，最长4096字符
     tags 文章标签，非空，最长20字符，多个词语用逗号分隔
     注意：url与text必须有一个为空
     *
     * @param title
     * @param url
     * @param text
     * @param tags
     */
    public void postArticle(String dappID, long fee, String title, String url, String text, String tags, String secret, CallbackContext callbackContext){
        String[] args={title,url,text,tags};
        invokeContract(dappID, Action.PostArticle.getType(),fee, args,secret, callbackContext);
    }

    /**
     *
     1.2 发表评论
     合约编号：1001

     合约参数：

     aid 文章编号
     pid 回复评论编号，可选
     content 回复内容，非空，最长4096字符

     * @param aid
     * @param pid
     * @param content
     */
    public void postComment(String dappID, long fee, String aid, String pid, String content, String secret, CallbackContext callbackContext){
        String[] args={aid,pid,content};
        invokeContract(dappID, Action.PostComment.getType(),fee, args,secret, callbackContext);
    }

    /**
     1.3 给文章投票
     合约编号：1002

     合约参数：

     aid 文章编号
     amount 投票数额‘，大于等于100000
     * @param aid
     * @param amount
     */
    public void voteArticle(String dappID, long fee, String aid, long amount, String secret, CallbackContext callbackContext){
        String[] args={aid,String.valueOf(amount)};
        invokeContract(dappID, Action.VoteArticle.getType(),fee, args,secret, callbackContext);
    }

    /**
     1.4 给评论打赏
     合约编号：1003

     合约参数：

     cid 评论编号
     amount 打赏数额，大于等于100000

     * @param cid
     * @param amount
     */
    public void likeComment(String dappID, long fee, String cid, long amount, String secret, CallbackContext callbackContext){
        String[] args={cid,String.valueOf(amount)};
        invokeContract(dappID, Action.LikeComment.getType(),fee, args,secret, callbackContext);
    }

    /**
     1.5 举报文章或评论
     合约编号：1004

     合约参数：

     topic 只能为1或2. 1表示举报文章，2表示举报评论
     value 举报的文章或评论id
     * @param topic
     * @param value
     */
    public void report(String dappID, long fee, int topic, String value, String secret, CallbackContext callbackContext){
        String[] args={String.valueOf(topic), value};
        invokeContract(dappID, Action.Report.getType(),fee, args,secret, callbackContext);
    }


    private void invokeContract(String dappID, int type, long fee, String[] args, String secret, CallbackContext callbackContext){
        Observable.create(new Observable.OnSubscribe<AschResult>() {

            @Override
            public void call(Subscriber<? super AschResult> subscriber) {
                AschResult result = AschSDK.Dapp.invokeContract(dappID, type, fee,args, secret);
                if (result != null && result.isSuccessful()) {
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Throwable(result!=null?result.getError():"result is null"));
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
                AppUtil.toastError(cordova.getContext(), e.getMessage());
            }

            @Override
            public void onNext(AschResult result) {

                AppUtil.toastSuccess(cordova.getContext(), "操作成功");
                dismissDialog();
            }
        });
    }

}
