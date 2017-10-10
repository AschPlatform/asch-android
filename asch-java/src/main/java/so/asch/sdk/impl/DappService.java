package so.asch.sdk.impl;

import so.asch.sdk.AschResult;
import so.asch.sdk.Dapp;
import so.asch.sdk.dto.query.BlockQueryParameters;
import so.asch.sdk.dto.query.TransactionQueryParameters;

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
    public AschResult withdraw(String dappID, String currency, long amount, String message, String secret, String secondSecret) {
        return null;
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
