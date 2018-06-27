package asch.so.wallet.cordova;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kimziv on 2018/2/27.
 */

public enum DAppContract {
    CoreDeposit(1,"core.deposit"),
    CoreWithdrawal(2,"core.withdrawal"),
    CoreTransfer(3,"core.transfer"),
    CoreSetNickname(4,"core.setNickname"),
    PostArticle(1000,"cctime.postArticle"),
    PostComment(1001,"cctime.postComment"),
    VoteArticle(1002,"cctime.voteArticle"),
    LikeComment(1003,"cctime.likeComment"),
    Report(1004,"cctime.report")
    ;

    private static final Map<Integer,DAppContract> allActions = new HashMap<>();
    static{
        for( DAppContract action : DAppContract.values()){
            allActions.put(action.getType(), action);
        }
    }

    private int type;
    private String name;

    DAppContract(int type, String name) {
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

    public static DAppContract fromType(int type){
        return allActions.get(type);
    }
}
