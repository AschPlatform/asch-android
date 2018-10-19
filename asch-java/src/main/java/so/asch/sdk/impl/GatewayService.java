package so.asch.sdk.impl;

import so.asch.sdk.AschResult;
import so.asch.sdk.Gateway;
import so.asch.sdk.Transaction;
import so.asch.sdk.TransactionType;
import so.asch.sdk.dbc.Argument;
import so.asch.sdk.dto.query.DepositQueryParameters;
import so.asch.sdk.dto.query.TransactionQueryParameters;
import so.asch.sdk.dto.query.WithdrawQueryParameters;
import so.asch.sdk.security.SecurityException;
import so.asch.sdk.transaction.TransactionBuilder;
import so.asch.sdk.transaction.TransactionInfo;

public class GatewayService extends AschRESTService implements Gateway {


    @Override
    public AschResult getDepositRecord(DepositQueryParameters parameters) {
        try {
            Argument.require(Validation.isValidAddress(parameters.getAddress()), "invalid address");
            Argument.notNull(parameters.getCurrency(), "invalid currency");
            return get(String.format(AschServiceUrls.Gateway.GET_GATEWAY_DEPOSITS,parameters.getAddress(),parameters.getCurrency()));
        }
        catch (Exception ex){
            return fail(ex);
        }

    }

    @Override
    public AschResult getWithdrawRecord(WithdrawQueryParameters parameters) {
        try {
            Argument.require(Validation.isValidAddress(parameters.getAddress()), "invalid address");
            Argument.notNull(parameters.getCurrency(), "invalid currency");
            ParameterMap query = parametersFromObject(parameters);
            return get(String.format(AschServiceUrls.Gateway.GET_GATEWAY_WITHDRAWALS,parameters.getAddress(),parameters.getCurrency()));
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult getGatewayAssets(int limit, int offset) {
        try {
            Argument.require(Validation.isValidLimit(limit), "invalid limit");
            Argument.require(Validation.isValidOffset(offset), "invalid offset");

            return get(AschServiceUrls.Gateway.GET_GATEWAY_CURRENCIES, createLimitAndOffsetParameters(limit, offset));
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    private ParameterMap createLimitAndOffsetParameters(int limit, int offset){
        return new ParameterMap()
                .put("limit", limit)
                .put("offset", offset);
    }

    @Override
    public AschResult withdraw(String address,String gateway,String currency,
                               Long amount,String fee,String message,String secret,String secondSecret)  {
        try {
            TransactionInfo info = new TransactionBuilder().buildWithdrawTransaction(address,gateway,
                    currency,amount,fee,message,secret,secondSecret);

            return broadcastTransaction(info);
        }
        catch (Exception ex){
            return fail(ex);
        }

    }

    @Override
    public AschResult getGatewayAccount(String name, String address) {
        try {
            Argument.require(Validation.isValidAddress(address), "invalid limit");

            return get(AschServiceUrls.Gateway.GET_GATEWAY_GETACCOUNT.replace(":name",name).replace(":address",address),
                    new ParameterMap());
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult openGatewayAccount(String gateway, String message, String secret, String secondSecret) {
        try {
            TransactionInfo info = new TransactionBuilder().buildOpenGatewayAccountTransaction(gateway,
                    message,secret,secondSecret);

            return broadcastTransaction(info);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }
}
