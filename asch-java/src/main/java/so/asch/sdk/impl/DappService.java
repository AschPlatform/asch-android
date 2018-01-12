package so.asch.sdk.impl;

import so.asch.sdk.AschResult;
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
            //Argument.optional(secondSecret, Validation.isValidSecondSecret(secondSecret), "invalid second secret");

             String[] args={currency, String.valueOf(amount)};
            TransactionInfo transaction = getTransactionBuilder()
                    .buildDAppTransaction(fee, ContractType.CoreWithdrawal,args,secret);
            System.out.println("====== transaction:"+transaction.toString());
            return broadcastDAppTransaction(dappID, transaction);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult innerTransfer(String dappID, String currency, String recipientId, long amount, String message, String secret, String secondSecret) {
        return null;
    }

    @Override
    public AschResult setNickname(String dappID, String nickname) {
        return null;
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
