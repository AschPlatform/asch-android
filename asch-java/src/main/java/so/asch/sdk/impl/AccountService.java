package so.asch.sdk.impl;

import so.asch.sdk.Account;
import so.asch.sdk.AschResult;
import so.asch.sdk.dbc.Argument;
import so.asch.sdk.dto.query.QueryParameters;
import so.asch.sdk.transaction.TransactionInfo;

import java.security.KeyPair;

/**
 * {@link Account}服务实现
 * @author eagle
 */
public class AccountService extends so.asch.sdk.impl.AschRESTService implements Account  {

    @Override
    public AschResult login(String secret){
        ParameterMap parameters = new ParameterMap().put("secret", secret);
        return post(AschServiceUrls.Account.LOGIN, parameters);
    }

    @Override
    public AschResult secureLogin(String secretOrPublicKey){
        try{
            boolean isSecret = Validation.isValidSecret(secretOrPublicKey);
            boolean isPublicKey= Validation.isValidPublicKey(secretOrPublicKey);

            Argument.require(isSecret||isPublicKey, "invalid secret or PublicKey");
            String publicKey=null;
            if (isSecret){
                KeyPair keyPair = getSecurity().generateKeyPair(secretOrPublicKey);
                publicKey = getSecurity().encodePublicKey(keyPair.getPublic());
            }else {
                publicKey=secretOrPublicKey;
            }
            ParameterMap parameters = parametersWithPublicKeyField(publicKey);

            return post(AschServiceUrls.Account.SECURE_LOGIN, parameters);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult getAccount(String address){

            return getByAddress(AschServiceUrls.Account.GET_ACCOUNT, address);
    }

    @Override
    public AschResult getAccountV2(String address) {
        return getReplaceByAddress(AschServiceUrls.Account.GET_ACCOUNT_V2, address);
    }

    @Override
    public AschResult getBalance(String address){
        return getByAddress(AschServiceUrls.Account.GET_BALANCE, address);
    }

    /**
     * {
     "success": true,
     "count": 2,
     "balances": [{
     "address": "AHcGmYnCyr6jufT5AGbpmRUv55ebwMLCym",
     "currency": "ddddd.HEL",
     "balance": "10000000000",
     "flag": 2,
     "_version_": 1,
     "asset": {
     "name": "ddddd.HEL",
     "tid": "0548907ec2be9ee2fdea8a7077aae05b1885887ef697c917b87741147e570a34",
     "timestamp": 62924027,
     "maximum": "100000000000000",
     "precision": 6,
     "quantity": "200000000000",
     "desc": "JADLSJFLAJDLF放假啦大家福利卡的就是浪费就到了房间辣贷款时间浪费敬爱的老师看风景大家是否将拉动手机发垃圾点时空裂缝的空间发垃圾的立法接待来访肯定积分垃圾的斯洛伐克静安里看到就舒服啦卡的减肥啦快捷快递累计罚款了的纠纷房间爱来的快解放啦大家说开了房间爱打瞌睡了房间啊扩大房间数量卡进来的房间里卡剪短发垃圾地方卡就到了房间爱劳动时间发垃圾点开了房间开打了四姐夫拉克绝地反击阿杜里斯积分垃圾堆了房间爱打瞌睡了几分拉的屎ADLSJFLAJDLF放假啦大家福利卡的就是浪费就到了房间辣贷款时间浪费敬爱的老师看风景大家是否将拉动手机发垃圾点时空裂缝的空间发垃圾的立法接待来访肯定积分垃圾的斯洛伐克静安里看到就舒服啦卡的减肥啦快捷快递累计罚款了的纠纷房间爱来的快解放啦大家说开了房间爱打瞌睡了房间啊扩大房间数量卡进来的房间里卡剪短发垃圾地方卡就到了房间爱劳动时间发垃圾点开了房间开打了四姐夫拉克绝地反击阿杜里斯积分垃圾堆了房间爱打瞌睡了几分拉的屎",
     "issuerId": "A7jbxyRqsaVQRbY69rzKbVruc8tnamdAsf",
     "_version_": 2
     }
     }, {
     "address": "AHcGmYnCyr6jufT5AGbpmRUv55ebwMLCym",
     "currency": "hhh.OOPP",
     "balance": "1000000000",
     "flag": 2,
     "_version_": 1,
     "asset": {
     "name": "hhh.OOPP",
     "tid": "fb5a25985dd30b15a9ad9b7addc957f942b2332a3792aa3be6da39e099cfcf06",
     "timestamp": 62518008,
     "maximum": "2000000000000",
     "precision": 6,
     "quantity": "2000000000000",
     "desc": "黄金季节",
     "issuerId": "A6MGhEcA8xgnUiGbszKrbtVjtxQ22ySx4m",
     "_version_": 2
     }
     }]
     }
     */
    @Override
    public AschResult getBalanceV2(String address, int limit, int offset) {
       // return
        try {
            Argument.require(Validation.isValidLimit(limit), "invalid limit");
            Argument.require(Validation.isValidOffset(offset), "invalid offset");
            Argument.require(Validation.isValidAddress(address), "invalid address");

            ParameterMap parameters = createLimitAndOffsetParameters(limit, offset);

            String url=  AschServiceUrls.Account.GET_BALANCE_V2.replace(":address",address);

            return get(url, parameters);
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
    public AschResult getPublicKey(String address){
        return  getByAddress(AschServiceUrls.Account.GET_PUBLIC_KEY, address);
    }

    @Override
    public AschResult generatePublicKey(String secret){
        try {
            Argument.require(Validation.isValidSecret(secret), "invalid secret");

            ParameterMap parameters = new ParameterMap().put("secret", secret);
            return post(AschServiceUrls.Account.GENERATE_PUBLIC_KEY, parameters);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult getVotedDelegates(String address){
        return getByAddress(AschServiceUrls.Account.GET_VOTED_DELEGATES, address);
    }

    @Override
    public AschResult getDelegatesFee(){
        return get(AschServiceUrls.Account.GET_DELEGATE_FEE);
    }

    //todo:验证投票和取消投票的数组都符合规则
    @Override
    public AschResult vote(String[] upvotePublicKeys, String[] downvotePublicKeys, String secret, String secondSecret){
        try {
            Argument.require(Validation.isValidSecret(secret), "invalid secret");
            Argument.optional(secondSecret, Validation.isValidSecondSecret(secondSecret), "invalid secondSecret");
            Argument.require(Validation.isValidVoteKeys(upvotePublicKeys, downvotePublicKeys), "invalid upvoteKeys or downvoteKeys");

            TransactionInfo transaction = getTransactionBuilder()
                    .buildVote( upvotePublicKeys, downvotePublicKeys,secret, secondSecret);
            return broadcastTransaction(transaction);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult transfer(String targetAddress, long amount, String message, String secret, String secondSecret){
        try {
            Argument.require(Validation.isValidAddress(targetAddress), "invalid target address");
            Argument.require(Validation.isValidSecret(secret), "invalid secret");
           // Argument.optional(secondSecret, Validation::isValidSecondSecret, "invalid second secret");
            Argument.optional(secondSecret,Validation.isValidSecondSecret(secondSecret),"invalid second secret");
            TransactionInfo transaction = getTransactionBuilder()
                    .buildTransfer(targetAddress, amount, message, secret, secondSecret);
            return broadcastTransaction(transaction);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult getTopAccounts(QueryParameters parameters){
        try {
            Argument.require(Validation.isValidAccountQueryParameters(parameters), "invalid parameters");

            ParameterMap getParameters = parametersFromObject(parameters);
            return get(AschServiceUrls.Account.GET_TOP_ACCOUNTS, getParameters);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    @Override
    public AschResult lockCoins(long height, String secret, String secondSecret) {
        try {
            //Argument.require(Validation.isv);
            Argument.require(Validation.isValidSecret(secret), "invalid secret");
            Argument.optional(secondSecret,Validation.isValidSecondSecret(secondSecret), "invalid secondSecret");

            TransactionInfo transaction  = getTransactionBuilder().buildLock(height,secret,secondSecret);
            return broadcastArgsTransaction(transaction);
        } catch (Exception ex) {
            return fail(ex);
        }
    }
}
