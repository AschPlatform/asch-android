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

//    获取支持的所有跨链币种
//    接口地址：/api/v2/gateways/currencies
//    请求方式：GET
//    支持格式：urlencoded
    AschResult getGatewayAssets(int limit, int offset);


//    从网关提现
//    type：403
//
//    fee： 0*XAS
//
//    args： [address,gateway,currency,amount,fee]
//
//    名称	类型	说明
//    address	string	提现地址
//    gateway	string	网关名字
//    currency	string	货币
//    amount	string	数量
//    fee	string	手续费
    AschResult withdraw(String address,String gateway,String currency,
                        Long amount,String fee,String message,String secret,String secondSecret);



//    2.8.5 获取指定网关的指定账户
//    接口地址：/api/v2/gateways/:name/accounts/:address 请求方式：GET
//    支持格式：urlencoded
//
//            请求参数说明
//
//    名称	类型	说明
//    name	string	网关名字
//    address	string	账户地址
    AschResult getGatewayAccount(String name,String address);


//    开通网关账户
//    type：400
//
//    fee： 0.1*XAS
//
//    args： [gateway]
    AschResult openGatewayAccount(String gateway,String message,String secret,String secondSecret);

}
