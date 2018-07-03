package so.asch.sdk;

import so.asch.sdk.dbc.Argument;
import so.asch.sdk.dbc.ContractException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by eagle on 17-7-14.
 */
public enum TransactionType {
    //named SEND in asch
    //Transfer(0,"transfer"),
    //Signature(1, "setSignature"),
    //Delegate(2, "delegate"),
    Vote(3, "vote"),
    MultiSignature(4, "setMultiSignature"),
    Dapp(5, "dapp"),
    InTransfer(6,"inTransfer"),
    OutTransfer(7,"outTransfer"),
    Store(8, "store"),

    UIAIssuer(9, "UIA_ISSUER"),
    UIAAsset(10, "UIA_ASSET"),
    UIAFlags(11, "UIA_FLAGS"),
    UIA_ACL(12, "UIA_ACL"),
    UIAIssue(13, "UIA_ISSUE"),
    UIATransfer(14, "UIA_TRANSFER"),

    //new in V1.3
    Lock(100, "Lock"),

    //TransferV2(1,"transfer"),
    SignatureV2(3, "setSignature"),
    DelegateV2(10, "delegate"),
    VoteV2(3, "vote"),
    //MultiSignatureV2(4, "setMultiSignature"),
    //DappV2(200, "dapp"),
    InTransferV2(204, "inTransfer"),
    OutTransferV2(205,"outTransfer"),
    //StoreV2(8, "store"),

    UIAIssuerV2(100, "UIA_ISSUER"),
    UIAAssetV2(101, "UIA_ASSET"),
    //UIAFlagsV2(11, "UIA_FLAGS"),
    //UIA_ACLV2(12, "UIA_ACL"),
    UIAIssueV2(102, "UIA_ISSUE"),
    UIATransferV2(103, "UIA_TRANSFER"),

    //new in V1.3
    LockV2(4, "Lock"),

    unknow (-1, "unknow"),

    basic_transfer(1, "basic.transfer"),
    basic_setName(2, "basic.setName"),
    basic_setPassword(3, "basic.setPassword"),
    basic_lock(4, "basic.lock"),
    basic_unlock(5, "basic.unlock"),
    basic_registerGroup(6,"basic.registerGroup"),
    basic_registerAgent(7,"basic.registerAgent"),
    basic_setAgent(8, "basic.setAgent"),
    basic_cancelAgent(9, "basic.cancelAgent"),
    basic_registerDelegate(10, "basic.registerDelegate"),
    basic_vote(11, "basic.vote"),
    basic_unvote(12, "basic.unvote"),

    uia_registerIssuer(100, "uia.registerIssuer"),
    uia_registerAsset(101, "uia.registerAsset"),
    uia_issue(102, "uia.issue"),
    uia_transfer(103, "uia.transfer"),

    chain_register(200, "chain.register"),
    chain_replaceDelegate(201, "chain.replaceDelegate"),
    chain_addDelegate(202,"chain.addDelegate"),
    chain_removeDelegate(203,"chain.removeDelegate"),
    chain_deposit(204, "chain.deposit"),
    chain_withdrawal(205, "chain.withdrawal"),

    proposal_propose(300, "proposal.propose"),
    proposal_vote(301, "proposal.vote"),
    proposal_activate(302,"proposal.activate"),

    gateway_openAccount(400, "gateway.openAccount"),
    gateway_registerMember(401, "gateway.registerMember"),
    gateway_deposit(402,"gateway.deposit"),
    gateway_withdrawal(403,"gateway.withdrawal"),
    gateway_submitWithdrawalTransaction(404, "gateway.submitWithdrawalTransaction"),
    gateway_submitWithdrawalSignature(405, "gateway.submitWithdrawalSignature"),
    gateway_submitOutTransactionId(406, "gateway.submitOutTransactionId"),

    group_vote(500, "group.vote"),
    group_activate(501, "group.activate"),
    group_addMember(502,"group.addMember"),
    group_removeMember(503,"group.removeMember"),
    group_replaceMember(504, "group.replaceMember");

    private int code;
    private String name;

    public int getCode(){
        return this.code;
    }
    public String getName(){
        return this.name;
    }

    private static final Map<Integer, TransactionType> allTransactionTypes = new HashMap<>();
    static{
        for( TransactionType type : TransactionType.values()){
            allTransactionTypes.put(type.getCode(), type);
        }
    }

    TransactionType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static TransactionType fromCode(int code) throws ContractException {
        Argument.require(allTransactionTypes.containsKey(code), String.format("invalid transaction type code '%d'", code));

        return allTransactionTypes.get(code);
    }

}
