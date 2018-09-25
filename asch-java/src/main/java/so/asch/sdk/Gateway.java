package so.asch.sdk;

import so.asch.sdk.dto.query.DepositQueryParameters;
import so.asch.sdk.dto.query.WithdrawQueryParameters;

/**
 * Asch UIA接口
 * @author eagle
 */
public interface Gateway extends AschInterface {

//    网关 充值记录
//    接口地址：/api/v2/gateways/deposits/:address/:currency
//    请求方式：GET
//    支持格式：urlencoded
//
//            参数说明
//
//    名称	类型	说明
//    address	string	账户地址
//    currency	string	币种

    AschResult getDepositRecord(DepositQueryParameters parameters);

//    网关 提现记录
//    接口地址：/api/v2/gateways/withdrawals/:address/:currency
//    请求方式：GET
//    支持格式：urlencoded
//
//            请求参数说明
//    名称	类型	说明
//    address	string	账户地址
//    currency	string	币种
    AschResult getWithdrawRecord(WithdrawQueryParameters parameters);

}
