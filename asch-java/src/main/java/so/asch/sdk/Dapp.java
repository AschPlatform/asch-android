package so.asch.sdk;

import so.asch.sdk.dto.query.BlockQueryParameters;
import so.asch.sdk.dto.query.TransactionQueryParameters;

/**
 * Created by eagle on 17-7-7.
 */
public interface Dapp extends AschInterface{

//    接口地址：/api/dapps/dappID/blocks/height
//    请求方式：GET
//    支持格式：urlencode
//
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    height	integer	dapp区块高度
    AschResult getBlockHeight(String dappID);

//    接口地址：/api/dapps/dappID/blocks
//    请求方式：GET
//    支持格式：urlencode
//    接口说明：不加参数则获取全网区块详情
//    请求参数说明：
//
//    名称	类型	必填	说明
//    limit	integer	N	限制结果集个数，最小值：0,最大值：100
//    orderBy	string	N	根据表中字段排序，如height:desc
//    offset	integer	N	偏移量，最小值0
//    generatorPublicKey	string	N	区块生成者公钥
//    totalAmount	integer	N	交易总额，最小值：0，最大值：10000000000000000
//    totalFee	integer	N	手续费总额，最小值：0，最大值：10000000000000000
//    previousBlock	string	N	上一个区块
//    height	integer	N	区块高度
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    count	integer	符合条件的总结果数目
//    blocks	Array	每个元素是一个block对象，对象里面包含block的id、height、产块受托人公钥等信息
     AschResult queryBlocks(BlockQueryParameters parameters);

//    2.1 根据地址获取dapp内账户信息
//
//    接口地址：/api/dapps/dappID/accounts/:address
//    请求方式：GET
//    支持格式：urlencode
//    请求参数说明：
//
//    名称	类型	必填	说明
//    address	string	Y	asch地址
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    account	字典	账户详情，包含dapp内该账户拥有的所有资产及余额，是否受托人，额外信息
     AschResult getAccount(String address);

    //    3.1.1.1 dapp充值
//
//    接口地址：/peer/transactions
//    请求方式：POST
//    支持格式：json
//    备注：充值时在主链发生type=6的交易（intransfer），dapp内部会自动调用编号为1的智能合约进行dapp内部充值 请求参数说明：
//
//    名称	类型	必填	说明
//    transaction	json	Y	aschJS.transfer.createInTransfer生成的交易数据
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    transactionId	string	交易id
    AschResult deposit(String dappID,String currency,long amount, String message,String secret, String secondSecret);

    //    3.1.1.2 dapp提现,type=2
//
//    接口地址：/api/dapps/dappID/transactions/signed
//    请求方式：PUT
//    支持格式：json
//    请求参数说明：
//
//    名称	类型	必填	说明
//    dappID	string	Y	dapp的id
//    transaction	json	Y	aschJS.dapp.createInnerTransaction生成的交易数据
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    transactionId	string	提币交易id
    public AschResult withdraw(String dappID, long fee,  String[] args, String secret, String secondSecret);

    //    3.1.1.2 dapp内部转账,type=3
//
//    接口地址：/api/dapps/dappID/transactions/signed
//    请求方式：PUT
//    支持格式：json
//    请求参数说明：
//
//    名称	类型	必填	说明
//    dappID	string	Y	dapp的id
//    transaction	json	Y	aschJS.dapp.createInnerTransaction生成的交易数据
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    transactionId	string	内部转账交易id
    AschResult innerTransfer(String dappID, String currency, String recipientId, long amount, String message, String secret, String secondSecret);

    //    3.1.1.3 dapp设置昵称,type=4
//
//    接口地址：/api/dapps/dappID/transactions/signed
//    请求方式：PUT
//    支持格式：json
//    请求参数说明：
//
//    名称	类型	必填	说明
//    dappID	string	Y	dapp的id
//    transaction	json	Y	aschJS.dapp.createInnerTransaction生成的交易数据
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    transactionId	string	设置昵称的交易id
    AschResult setNickname(String dappID, String nickname);

//    3.2 获取未确认的交易
//
//    接口地址：/api/dapps/dappID/transactions/unconfirmed
//    请求方式：GET
//    支持格式：urlencode
//
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    transactions	array	未确认交易列表
    AschResult queryUnconfirmedTransactions(String dappID);

//    3.3 获取已确认的交易
//
//    接口地址：/api/dapps/dappID/transactions
//    请求方式：GET
//    支持格式：urlencode
//    请求参数说明：
//
//    名称	类型	必填	说明
//    limit	interger	N	限制返回的条数,默认值是100
//    offset	interger	N	偏移量
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    transactions	array	交易列表
//    count	integer	符合查询条件的总交易条数

    AschResult queryTransactions(TransactionQueryParameters parameters);

//    3.4 根据交易id获取交易详情
//
//    接口地址：/api/dapps/dappID/transactions/:id
//    请求方式：GET
//    支持格式：urlencode
//    请求参数说明：
//
//    名称	类型	必填	说明
//    id	string	Y	交易id
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    transaction	dict	该交易id对应的交易详情

    AschResult getTransaction(String transactionId);

//    3.5 根据查询条件获取交易
//
//    接口地址：/api/dapps/dappID/transfers
//    请求方式：GET
//    支持格式：urlencode
//    请求参数说明：
//
//    名称	类型	必填	说明
//    ownerId	string	N	发送者地址，ownerId和currency必须有一个或者两个都存在
//    currency	string	N	代币名称，ownerId和currency必须有一个或者两个都存在
//    limit	interger	N	限制返回的条数,默认值是10
//    offset	interger	N	偏移量，默认0
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    transfers	array	符合查询条件的交易列表
//    count	integer	符合查询条件的条数
    AschResult getTransactions(String ownerId, String currency, int limit, int offset);

//    4.1 获取dapp内的所有智能合约
//
//    接口地址：/api/dapps/dappID/contracts
//    请求方式：GET
//    支持格式：urlencode
//
//    返回参数说明：
//
//    名称	类型	说明
//    success	boole	是否成功获得response数据
//    contracts	array	每个元素都是一个字典，由合约编号、合约名字组成，其中core开头的合约为每个dapp通用的内置合约
    AschResult getContracts();


}
