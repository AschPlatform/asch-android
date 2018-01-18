package so.asch.sdk.impl;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.security.KeyPair;

import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.ContractType;
import so.asch.sdk.Dapp;
import so.asch.sdk.Transaction;
import so.asch.sdk.TransactionType;
import so.asch.sdk.dbc.Argument;
import so.asch.sdk.dto.query.BlockQueryParameters;
import so.asch.sdk.dto.query.TransactionQueryParameters;
import so.asch.sdk.transaction.TransactionInfo;

public class DappService extends AschRESTService implements Dapp {
    @Override
    public AschResult getBlockHeight(String dappID) {
        return null;
    }

    @Override
    public AschResult queryBlocks(BlockQueryParameters parameters) {
        return null;
    }

    @Override
    public AschResult getAccount(String address) {
        return null;
    }

    @Override
    public AschResult deposit(String dappID, String currency, long amount, String message, String secret, String secondSecret) {
        try {
           Argument.notNull(dappID,"invalid dappID");
           Argument.notNull(currency, "invalid currency");
           Argument.require(Validation.isValidInTransferAmount(currency, amount), "invalid ammount");
           Argument.require(Validation.isValidSecret(secret),"invalid secret");
           Argument.optional(secondSecret, Validation.isValidSecondSecret(secondSecret),"invalid secondSecret");

            TransactionInfo transaction=getTransactionBuilder()
                    .buildInTransfer(dappID,currency,amount,secret,secondSecret);
            System.out.println("====== transaction:"+transaction.toString());

            return broadcastTransaction(transaction);
        }catch (Exception e){
            return fail(e);
        }
    }

    @Override
    public AschResult withdraw(String dappID,  String currency, long amount, long fee, String secret, String secondSecret) {

        try {
            Argument.notNullOrEmpty(dappID, "invalid dappID");
            Argument.require(Validation.isValidFee(fee), "invalid fee");
            Argument.require(Validation.isValidSecret(secret), "invalid secret");
             String[] args={currency, String.valueOf(amount)};
            TransactionInfo transaction = getTransactionBuilder()
                    .buildDAppTransaction(fee, ContractType.CoreWithdrawal,args,secret);
            System.out.println("====== transaction:"+ JSON.toJSONString(transaction));
            return broadcastDAppTransaction(dappID, transaction);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult innerTransfer(String dappID, String currency, String targetAddress, long amount, long fee, String message, String secret, String secondSecret) {
        try {
            Argument.notNullOrEmpty(dappID, "invalid dappID");
            Argument.notNullOrEmpty(currency, "invalid currency");
            Argument.notNullOrEmpty(targetAddress, "invalid address");
            Argument.require(Validation.isValidFee(fee), "invalid fee");
            Argument.require(Validation.isValidSecret(secret), "invalid secret");
            //Argument.optional(secondSecret, Validation.isValidSecondSecret(secondSecret), "invalid second secret");

            String[] args={currency, String.valueOf(amount), targetAddress};
            TransactionInfo transaction = getTransactionBuilder()
                    .buildDAppTransaction(fee, ContractType.CoreTransfer,args,secret);
            System.out.println("====== transaction:"+transaction.toString());
            return broadcastDAppTransaction(dappID, transaction);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult setNickname(String dappID, String nickname, long fee, String secret, String secondSecret) {
        try {
            Argument.notNullOrEmpty(dappID, "invalid dappID");
            Argument.notNullOrEmpty(nickname, "invalid currency");
            Argument.require(Validation.isValidSecret(secret), "invalid secret");
            Argument.optional(secondSecret, Validation.isValidSecondSecret(secondSecret), "invalid second secret");

            String[] args={ nickname };
            TransactionInfo transaction = getTransactionBuilder()
                    .buildDAppTransaction(fee, ContractType.CoreSetNickname, args, secret);
            System.out.println("====== transaction:"+transaction.toString());
            return broadcastDAppTransaction(dappID, transaction);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult invokeContract(String dappID, int type, long fee, String[] args, String secret) {
        try {
            Argument.notNullOrEmpty(dappID, "invalid dappID");
            Argument.notNullOrEmpty(args,"args can not be null or empty");
            Argument.require(Validation.isValidSecret(secret), "invalid secret");

            TransactionInfo transaction = getTransactionBuilder()
                    .buildDAppTransaction(fee, ContractType.fromCode(type), args, secret);
            System.out.println("====== transaction:"+transaction.toString());
            return broadcastDAppTransaction(dappID, transaction);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult queryUnconfirmedTransactions(String dappID) {
        return null;
    }

    @Override
    public AschResult queryTransactions(TransactionQueryParameters parameters) {
        return null;
    }

    @Override
    public AschResult getTransaction(String transactionId) {
        return null;
    }

    @Override
    public AschResult getTransactions(String ownerId, String currency, int limit, int offset) {
        return null;
    }

    @Override
    public AschResult getContracts() {
        return null;
    }
}
