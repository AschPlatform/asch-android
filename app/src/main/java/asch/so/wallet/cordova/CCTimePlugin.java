package asch.so.wallet.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import asch.so.wallet.model.entity.Transaction;

/**
 * Created by kimziv on 2018/1/17.
 */

public class CCTimePlugin extends CordovaPlugin {

    private static final String TAG=CCTimePlugin.class.getSimpleName();

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

        if (Action.PostArticle.getName().equals(action)){

        }else if (Action.PostComment.getName().equals(action)){

        }else if (Action.VoteArticle.getName().equals(action)){

        }else if (Action.LikeComment.getName().equals(action)){

        }else if (Action.Report.getName().equals(action)){

        }

        return super.execute(action, args, callbackContext);
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
    public void postArticle(String title, String url, String text, String tags){

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
    public void postComment(String aid, String pid, String content){

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
    public void voteArticle(String aid, long amount){

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
    public void likeComment(String cid, long amount){

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
    public void report(int topic, String value){

    }

}
