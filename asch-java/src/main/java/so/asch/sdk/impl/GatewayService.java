package so.asch.sdk.impl;

import so.asch.sdk.AschResult;
import so.asch.sdk.Gateway;
import so.asch.sdk.Transaction;
import so.asch.sdk.dbc.Argument;
import so.asch.sdk.dto.query.DepositQueryParameters;
import so.asch.sdk.dto.query.TransactionQueryParameters;
import so.asch.sdk.dto.query.WithdrawQueryParameters;

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
}
