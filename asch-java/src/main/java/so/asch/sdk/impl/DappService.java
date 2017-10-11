package so.asch.sdk.impl;

import so.asch.sdk.AschResult;
import so.asch.sdk.Dapp;
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

        return null;
    }

    @Override
    public AschResult withdraw(String dappID, long fee,  String[] args, String secret, String secondSecret) {

        try {
//            Argument.notNullOrEmpty(currency, "invalid currency");
//            Argument.require(Validation.isValidAddress(recipientId), "invalid recipientId");
//            Argument.require(Validation.isValidSecret(secret), "invalid secret");
//            Argument.optional(secondSecret, Validation::isValidSecret, "invalid second secret");

            TransactionInfo transaction = getTransactionBuilder()
                    .buildInnerTransaction(fee, TransactionType.InTransfer,args,secret);
            System.out.println("====== transaction:"+transaction.toString());
            return broadcastTransaction(transaction);
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
