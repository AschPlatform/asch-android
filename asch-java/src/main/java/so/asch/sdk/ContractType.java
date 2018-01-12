package so.asch.sdk;

import java.util.HashMap;
import java.util.Map;

import so.asch.sdk.dbc.Argument;
import so.asch.sdk.dbc.ContractException;

/**
 * Created by kimziv on 2018/1/12.
 */
/*
{
	contracts: [{
		type: "1",
		name: "core.deposit" // 系统内置合约，充值(从主链往dapp内进行资产充值)，普通用户不能直接调用（受托人可以调用但不能通过其它节点的校验），当主链有type=9（intransfer）的交易类型发生时会自动调用该智能合约进行dapp充值
	},
	{
		type: "2",
		name: "core.withdrawal" // 系统内置合约，提现(将资产从dapp内转出到主链上)
	},
	{
		type: "3",
		name: "core.transfer" // 系统内置合约，dapp内部转账，包括XAS和UIA
	},
	{
		type: "4",
		name: "core.setNickname" // 系统内置合约，dapp内给地址设置昵称
	},
	{
		type: "1000",
		name: "cctime.postArticle" // dapp自定义合约，发布文章
	},
	{
		type: "1001",
		name: "cctime.postComment" // dapp自定义合约，发布评论
	},
	{
		type: "1002",
		name: "cctime.voteArticle" // dapp自定义合约，给文章进行投票
	},
	{
		type: "1003",
		name: "cctime.likeComment" // dapp自定义合约，对评论进行打赏
	},
	{
		type: "1004",
		name: "cctime.report" // dapp自定义合约，举报文章
	}],
	success: true
}
 */
public enum ContractType {
    //core contracts
    CoreDeposit(1, "core.deposit"),
    CoreWithdrawal(2, "core.withdrawal"),
    CoreTransfer(3, "core.transfer"),
    CoreSetNickname(4, "core.setNickname"),
    //cctime contracts
    CCTimePostArticle(1000, "cctime.postArticle"),
    CCTimePostComment(1001,"cctime.postComment"),
    CCTimeVoteArticle(1002,"cctime.voteArticle"),
    CCTimeLikeComment(1003, "cctime.likeComment"),
    CCTimeReport(1004, "cctime.report");

    private int code;
    private String name;

    public int getCode(){
        return this.code;
    }
    public String getName(){
        return this.name;
    }

    private static final Map<Integer, ContractType> allTransactionTypes = new HashMap<>();
    static{
        for( ContractType type : ContractType.values()){
            allTransactionTypes.put(type.getCode(), type);
        }
    }

    ContractType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ContractType fromCode(int code) throws ContractException {
        Argument.require(allTransactionTypes.containsKey(code), String.format("invalid contact type code '%d'", code));

        return allTransactionTypes.get(code);
    }

}