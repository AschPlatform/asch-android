package so.asch.sdk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import so.asch.sdk.AschInterface;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.AschSDKConfig;
import so.asch.sdk.dbc.Argument;
import so.asch.sdk.security.SecurityStrategy;
import so.asch.sdk.transaction.TransactionBuilder;
import so.asch.sdk.transaction.TransactionInfo;
import so.asch.sdk.transaction.asset.InTransferAssetInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Asch服务基类
 * @author eagle
 */
public abstract class AschRESTService implements AschInterface{

    //protected static final  Logger logger = LoggerFactory.getLogger(AschRESTService.class);

    protected static final AschSDKConfig config = AschSDKConfig.getInstance();
    private static final Map<String, String> customeHeaders =  new HashMap<>();

    static {
        customeHeaders.put("magic", config.getMagic());
        customeHeaders.put("version", "");
    }
    
    private static boolean isAndroid() {
    	return (AschSDK.Config.getPlatform()==1);
    }

    protected TransactionBuilder getTransactionBuilder(){
        return new TransactionBuilder();
    }

    protected SecurityStrategy getSecurity(){
        return AschFactory.getInstance().getSecurity();
    }

    protected Map<String, String> getCustomeHeaders(){
        if (! config.getMagic().equals(customeHeaders.get("magic"))){
            customeHeaders.put("magic", config.getMagic());
        }
        return customeHeaders;
    }

    protected String getFullUrl(String relativeUrl){
        return config.getAschServer() + relativeUrl;
    }

    protected AschResult get(String relativeUrl){
        try{
            String jsonString = RESTOkHttp.get(getFullUrl(relativeUrl), null);
            return AschResult.FromJsonString(jsonString);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    protected AschResult get(String relativeUrl, ParameterMap parameters){
        try{
            String jsonString =  RESTOkHttp.get(getFullUrl(relativeUrl), parameters);
            return AschResult.FromJsonString(jsonString);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    protected AschResult post(String relativeUrl, ParameterMap parameters){
        try{
            String jsonString =  RESTOkHttp.post(getFullUrl(relativeUrl), parameters);
            return AschResult.FromJsonString(jsonString);
        }
        catch (Exception ex){
        	   ex.printStackTrace();
            return fail(ex);
        }
    }

    protected AschResult post(String relativeUrl, String parameters){
        try{
            String jsonString =  RESTOkHttp.post(getFullUrl(relativeUrl), parameters);
            return AschResult.FromJsonString(jsonString);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    protected AschResult postMagic(String relativeUrl, ParameterMap parameters){
        try{
            String jsonString =  RESTOkHttp.post(getFullUrl(relativeUrl), parameters, getCustomeHeaders(), null);
            return AschResult.FromJsonString(jsonString);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    protected AschResult putMagic(String relativeUrl, ParameterMap parameters){
        try{
            String jsonString =  RESTOkHttp.put(getFullUrl(relativeUrl), parameters, getCustomeHeaders(), null);
            return AschResult.FromJsonString(jsonString);
        }
        catch (Exception ex){
            return fail(ex);

        }
    }

    protected AschResult postMagic(String relativeUrl, String parameters){
        try{
            String jsonString =  RESTOkHttp.post(getFullUrl(relativeUrl), parameters, getCustomeHeaders(), null);
            return AschResult.FromJsonString(jsonString);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    protected AschResult broadcastTransaction(TransactionInfo transaction){
        JSONObject transactionJson = (JSONObject) JSONObject.toJSON(transaction);
        //transactionJson.remove("args");
        transactionJson.remove("option");
        transactionJson.remove("contractType");
        transactionJson.remove("transactionType");
        transactionJson.put("type",transaction.getTransactionType().getCode());
        String json=transactionJson.toJSONString();
        System.out.println("json:"+json+"\n\n");
        ParameterMap transactionParameter = new ParameterMap()
                .put("transaction", transactionJson);
//        if (transaction.getAsset() instanceof InTransferAssetInfo){
//            InTransferAssetInfo assetInfo =(InTransferAssetInfo) transaction.getAsset();
//            String dappId = assetInfo.getInTransfer().getDappId();
//
//        }else {
            return postMagic(AschServiceUrls.Peer.BROADCAST_TRANSACTION, transactionParameter );
        //}

    }

    protected AschResult broadcastArgsTransaction(TransactionInfo transaction){
        JSONObject transactionJson = (JSONObject) JSONObject.toJSON(transaction);
        transactionJson.remove("args");
        transactionJson.remove("option");
        transactionJson.remove("contractType");
       transactionJson.put("type",transaction.getTransactionType().getCode());
        transactionJson.put("args",transaction.getOption().getArgs());
        ParameterMap transactionParameter = new ParameterMap()
                .put("transaction", transactionJson);
        return postMagic(AschServiceUrls.Peer.BROADCAST_TRANSACTION, transactionParameter );
    }

    protected AschResult broadcastDAppTransaction(String dappID, TransactionInfo transaction){

        JSONObject transactionJson = (JSONObject) JSONObject.toJSON(transaction);
        transactionJson.put("fee",String.valueOf(transaction.getFee()));
        transactionJson.put("type",transaction.getContractType().getCode());
        transactionJson.remove("option");
        transactionJson.remove("contractType");
        ParameterMap transactionParameter = new ParameterMap()
                .put("transaction", transactionJson);
//        if (transaction.getAsset() instanceof InTransferAssetInfo){
//            InTransferAssetInfo assetInfo =(InTransferAssetInfo) transaction.getAsset();
//            String dappId = assetInfo.getInTransfer().getDappId();
//
//        }else {
        return putMagic(String.format(AschServiceUrls.Dapp.TRANSCATIONS_SIGNED_FORMAT,dappID), transactionParameter );
        //}
    }

    protected AschResult fail(Exception ex){
        //logger.error("rest call failed", ex);
        return AschResult.Failed(ex);
    }

    protected ParameterMap parametersWithPublicKeyField(String publicKey){
        return new ParameterMap().put("publicKey", publicKey);
    }

    protected AschResult getByPublicKey(String relativeUrl, String publicKey){
        try {
            Argument.require(Validation.isValidPublicKey(publicKey), "invalid public key");

            ParameterMap parameters = parametersWithPublicKeyField(publicKey);
            return get(relativeUrl, parameters);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    protected AschResult getByAddress(String relativeUrl, String address){
        try {
            Argument.require(Validation.isValidAddress(address), "invalid public address");

            ParameterMap parameters = new ParameterMap().put("address", address);
            return get(relativeUrl, parameters);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    protected AschResult getReplaceByAddress(String relativeUrl, String address){
        try {
            Argument.require(Validation.isValidAddress(address), "invalid public address");

           // ParameterMap parameters = new ParameterMap().put("address", address);
           String url=  AschServiceUrls.Account.GET_ACCOUNT_V2.replace(":address",address);
            return get(url);
        }
        catch (Exception ex){
            return fail(ex);
        }
    }

    protected ParameterMap parametersFromObject(Object object){
        ParameterMap map = new ParameterMap();
        map.putAll(JSONObject.parseObject( JSONObject.toJSONString(object) ));

        return map;
    }
}
