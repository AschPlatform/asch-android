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
        return super.execute(action, args, callbackContext);
    }

}
