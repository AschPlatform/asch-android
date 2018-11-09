package asch.io.wallet.cordova;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import asch.io.wallet.view.widget.DAppPasswordDialog;

/**
 * Created by kimziv on 2018/1/17.
 */

public class CCTimePlugin extends BasePlugin {

    private static final String TAG=CCTimePlugin.class.getSimpleName();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if(this.cordova.getActivity().isFinishing()) return true;

        if (DAppContract.PostArticle.getName().equals(action)){
            String dappID=args.getString(0);
            long fee=args.getLong(1);
            String title=args.getString(2);
            String url=args.getString(3);
            String text=args.getString(4);
            String tags =args.getString(5);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new DAppPasswordDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(DAppPasswordDialog dialog, String secret, String errMsg) {
                                postArticle(dappID, fee, title, url, text, tags, secret, callbackContext);
                        }
                    });
                }
            });
        }else if (DAppContract.PostComment.getName().equals(action)){
            String dappID=args.getString(0);
            //long fee=args.getLong(1);
            long fee=Long.parseLong(args.getString(1));
            String aid=args.getString(2);
            String pid=args.getString(3);
            String content=args.getString(4);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new DAppPasswordDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(DAppPasswordDialog dialog, String secret, String errMsg) {
                            postComment(dappID,fee, aid,pid,content, secret, callbackContext);
                        }
                    });
                }
            });
        }else if (DAppContract.VoteArticle.getName().equals(action)){
            String dappID=args.getString(0);
            //long fee=args.getLong(1);
            long fee=Long.parseLong(args.getString(1));
            String aid=args.getString(2);
            long amount=args.getLong(3);
            //long amount=Long.parseLong(args.getString(3));
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new DAppPasswordDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(DAppPasswordDialog dialog, String secret, String errMsg) {
                            voteArticle(dappID,fee, aid,amount,secret,callbackContext);
                        }
                    });
                }
            });
        }else if (DAppContract.LikeComment.getName().equals(action)){
            String dappID=args.getString(0);
            long fee=args.getLong(1);
            String cid=args.getString(2);
            long amount=args.getLong(3);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new DAppPasswordDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(DAppPasswordDialog dialog, String secret, String errMsg) {
                            likeComment(dappID,fee, cid,amount,secret,callbackContext);
                        }
                    });
                }
            });
        }else if (DAppContract.Report.getName().equals(action)){
            String dappID=args.getString(0);
            long fee=args.getLong(1);
            int topic=args.getInt(2);
            String value=args.getString(3);
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showPasswordDialog(new DAppPasswordDialog.OnConfirmationListenner() {
                        @Override
                        public void callback(DAppPasswordDialog dialog, String secret, String errMsg) {
                            report(dappID,fee, topic,value,secret,callbackContext);
                        }
                    });
                }
            });
        }
        //callbackContext.success();
        return true;
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
        invokeContract(dappID, DAppContract.PostArticle.getType(),fee, args,secret, callbackContext);
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
        invokeContract(dappID, DAppContract.PostComment.getType(),fee, args,secret, callbackContext);
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
        invokeContract(dappID, DAppContract.VoteArticle.getType(),fee, args,secret, callbackContext);
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
        invokeContract(dappID, DAppContract.LikeComment.getType(),fee, args,secret, callbackContext);
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
        invokeContract(dappID, DAppContract.Report.getType(),fee, args,secret, callbackContext);
    }



}
