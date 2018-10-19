package so.asch.sdk.transaction;

import android.content.Intent;
import android.util.DebugUtils;
import android.util.Log;

import so.asch.sdk.ContractType;
import so.asch.sdk.Transaction;
import so.asch.sdk.TransactionType;
import so.asch.sdk.impl.AschConst;
import so.asch.sdk.impl.AschFactory;
import so.asch.sdk.security.SecurityException;
import so.asch.sdk.security.SecurityStrategy;
import so.asch.sdk.transaction.asset.*;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;


public class TransactionBuilder {

    private static final String TAG=TransactionBuilder.class.getSimpleName();



    public TransactionInfo buildOpenGatewayAccountTransaction(String gateway, String message, String secret, String secondSecret) throws SecurityException {
        KeyPair keyPair = getSecurity().generateKeyPair(secret);
        TransactionInfo transaction =  newTransaction(
                TransactionType.gateway_openAccount,
                keyPair.getPublic())
                .setTransactionType(TransactionType.gateway_openAccount)
                .setArgs(new String[]{gateway})
                .setMessage(message)
                .calcFee()
                ;

        return signatureAndGenerateTransactionId(transaction,keyPair.getPrivate(),secondSecret);

    }


    public TransactionInfo buildWithdrawTransaction(String address,String gateway,String currency,
                                                    Long amount,String fee,String message,String secret,String secondSecret) throws SecurityException {
        String strAmount  = amount.longValue()+"";
        KeyPair keyPair = getSecurity().generateKeyPair(secret);
        TransactionInfo transaction =  newTransaction(
                TransactionType.gateway_withdrawal,
                keyPair.getPublic())
                .setTransactionType(TransactionType.gateway_withdrawal)
                .setArgs(new String[]{address,gateway,currency,strAmount,fee})
                .setMessage(message)
                .calcFee()
                ;

        return signatureAndGenerateTransactionId(transaction,keyPair.getPrivate(),secondSecret);

    }

    public TransactionInfo buildTransfer(String targetAddress, long amount, String message,
                                         String secret, String secondSecret) throws  SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);

        TransactionInfo transaction =  newTransaction(
                TransactionType.basic_transfer,
                keyPair.getPublic())
//                .setSenderPublicKey(getSecurity().encodePublicKey(keyPair.getPublic()))
                .setMessage(message)
                .setArgs(new String[]{ amount+"", targetAddress })
                .calcFee();

        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), secondSecret);
    }

    public TransactionInfo buildVote(String[] upvotePublicKeys, String[] downvotePublicKeys,
                                     String secret, String secondSecret ) throws SecurityException {
        KeyPair keyPair = getSecurity().generateKeyPair(secret);

        TransactionInfo transaction = newTransaction(
                TransactionType.Vote,
                0L,
                AschConst.Fees.VOTE,
                keyPair.getPublic())
                .setAsset(new VoteAssetInfo(upvotePublicKeys, downvotePublicKeys));

        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), secondSecret);
    }

    public TransactionInfo buildDelegate(String userName, String secret, String secondSecret)  throws SecurityException  {
//        KeyPair keyPair = getSecurity().generateKeyPair(secret);
//        KeyPair secondKeyPair = getSecurity().generateKeyPair(secondSecret);
//
//        TransactionInfo transaction = newTransaction(
//                TransactionType.Delegate,
//                0L,
//                AschConst.Fees.DELEGATE,
//                keyPair.getPublic());
//
//        transaction.setAsset(new DelegateAssetInfo(userName, transaction.getSenderPublicKey()));
//
//        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(),secondSecret);
        return null;
    }

    public TransactionInfo buildSignature(String secret, String secondSecret) throws  SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);
        KeyPair secondKeyPair = getSecurity().generateKeyPair(secondSecret);

        TransactionInfo transaction=newTransaction(TransactionType.basic_setPassword,keyPair.getPublic())
                .setArgs(new Object[]{getSecurity().encodePublicKey(secondKeyPair.getPublic())})
                .calcFee();

        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), null);

    }

    public TransactionInfo buildLock(long amount, long height,String secret, String secondSecret) throws  SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);
        //long[] args={height,amount};

        TransactionInfo transaction = newTransaction(TransactionType.basic_lock,keyPair.getPublic())
                //.setSenderPublicKey(getSecurity().encodePublicKey(keyPair.getPublic()))
                .setArgs(new Object[]{height,amount})
                .calcFee();
        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), secondSecret);

    }

    public TransactionInfo buildMultiSignature(int minAccount, int lifetime, String[] addKeys, String[] removeKeys,
                                               String secret, String secondSecret) throws SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);

        TransactionInfo transaction =  newTransaction(
                TransactionType.MultiSignature,
                0L,
                AschConst.Fees.MULTI_SIGNATURE,
                keyPair.getPublic())
                .setAsset(new MultiSignatureAssetInfo(minAccount, lifetime, addKeys, removeKeys));

        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), secondSecret);

    }



    public TransactionInfo buildStore(byte[] contentBuffer, int wait,
                                      String secret, String secondSecret)throws SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);

        long fee = (int) Math.floor(contentBuffer.length / (200) + 1) * 10000000;
        TransactionInfo transaction =  newTransaction(
                TransactionType.Store,
                0L,
                fee,
                keyPair.getPublic())
                .setAsset(new StorageAssetInfo(contentBuffer));

        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), secondSecret);
    }

//    public TransactionInfo buildUIATransfer(String currency, long amount, String targetAddress, String message,
//                                            String secret, String secondSecret) throws SecurityException{
//        KeyPair keyPair = getSecurity().generateKeyPair(secret);
//
//        TransactionInfo transaction =  newTransaction(
//                TransactionType.UIATransfer,
//                0,
//                AschConst.Fees.UIA_TRANSFER,
//                keyPair.getPublic())
//                .setMessage(message)
//                .setRecipientId(targetAddress)
//                .setAsset(new UIATransferAssetInfo(amount, currency));
//
//        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), secondSecret);
//    }


    public TransactionInfo buildUIATransfer(String currency, long amount, String targetAddress, String message,
                                            String secret, String secondSecret) throws SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);

        TransactionInfo transaction =  newTransaction(
                TransactionType.uia_transfer,
                keyPair.getPublic())
                .setMessage(message)
                .setArgs(new Object[]{ currency, amount, targetAddress })
                .calcFee();

        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), secondSecret);
    }


    public TransactionInfo buildDAppTransaction(long fee, ContractType type, String [] args, String secret) throws SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);

        TransactionInfo transaction= newDAppTransaction(fee,type, args , keyPair.getPublic());
        return signatureDAppTransaction(transaction, keyPair.getPrivate());
    }

    public TransactionInfo buildInTransfer(String dappID, String currency, long amount,String  secret, String secondSecret) throws SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);
        TransactionInfo transaction=newTransaction(TransactionType.InTransfer,AschConst.CORE_COIN_NAME.equals(currency)?amount:0,AschConst.Fees.TRANSFER,keyPair.getPublic())
                .setAsset(new InTransferAssetInfo(dappID,currency,amount));
        return signatureAndGenerateTransactionId(transaction,keyPair.getPrivate(),secondSecret);
    }

    protected TransactionInfo newTransaction(TransactionType type, long amount, long fee, PublicKey publicKey) throws SecurityException{
       return newTransaction(type,amount,fee,publicKey,null,null);
    }


//    protected TransactionInfo newTransaction(TransactionType type, PublicKey publicKey) throws SecurityException{
//        return new TransactionInfo()
//                .setTransactionType(type)
//                .setTimestamp(getSecurity().getTransactionTimestamp())
//                .setSenderId(getSecurity().encodePublicKey(publicKey));
//    }

    protected TransactionInfo newTransaction(TransactionType type, PublicKey publicKey) throws SecurityException{
        return new TransactionInfo()
                .setTransactionType(type)
                .setTimestamp(getSecurity().getTransactionTimestamp())
                .setSenderPublicKey(getSecurity().encodePublicKey(publicKey))
                .setSenderId(getSecurity().getBase58Address(getSecurity().encodePublicKey(publicKey)));
    }





    protected TransactionInfo newTransaction(TransactionType type, long amount, long fee, PublicKey publicKey, String dappID, OptionInfo optionInfo) throws SecurityException{
        switch (type){

            case basic_transfer:
                return new TransactionInfo()
                        .setTransactionType(type)
                        .setAmount(amount)
                        .setFee(fee)
                        .setTimestamp(getSecurity().getTransactionTimestamp())
                        .setSenderPublicKey(getSecurity().encodePublicKey(publicKey));
            case basic_setPassword:
                return new TransactionInfo()
                        .setTransactionType(type)
                        .setAmount(amount)
                        .setFee(fee)
                        .setTimestamp(getSecurity().getTransactionTimestamp())
                        .setSenderPublicKey(getSecurity().encodePublicKey(publicKey));
//            case Delegate:
//                break;
            case Vote:
                return new TransactionInfo()
                        .setTransactionType(type)
                        .setAmount(amount)
                        .setFee(fee)
                        .setTimestamp(getSecurity().getTransactionTimestamp())
                        .setSenderPublicKey(getSecurity().encodePublicKey(publicKey));
            case MultiSignature:
                break;
            case Dapp:
                break;
            case InTransfer:
                return new TransactionInfo()
                        .setTransactionType(type)
                        .setAmount(amount)
                        .setFee(fee)
                        .setTimestamp(getSecurity().getTransactionTimestamp())
                        .setSenderPublicKey(getSecurity().encodePublicKey(publicKey));
            case OutTransfer:
                break;
            case Store:
                break;
            case UIAIssuer:
                break;
            case UIAAsset:
                break;
            case UIAFlags:
                break;
            case UIA_ACL:
                break;
            case UIAIssue:
                break;
            case UIATransfer:
                return new TransactionInfo()
                        .setTransactionType(type)
                        .setAmount(amount)
                        .setFee(fee)
                        .setTimestamp(getSecurity().getTransactionTimestamp())
                        .setSenderPublicKey(getSecurity().encodePublicKey(publicKey));
            case Lock:
                return new TransactionInfo()
                        .setTransactionType(type)
                        .setFee(fee)
                        .setTimestamp(getSecurity().getTransactionTimestamp())
                        .setSenderPublicKey(getSecurity().encodePublicKey(publicKey));
        }
        return null;
    }


    protected TransactionInfo newDAppTransaction(long fee, ContractType type, String [] args, PublicKey publicKey) throws SecurityException{

        OptionInfo optionInfo =  new OptionInfo(fee, type, args);
        return new TransactionInfo()
                .setFee(fee)
                .setTimestamp(getSecurity().getTransactionTimestamp())
                .setSenderPublicKey(getSecurity().encodePublicKey(publicKey))
                .setContractType(type)
                //.setArgs(optionInfo.getArgsJson())
                .setOption(optionInfo);
    }

    protected TransactionInfo newArgsTransaction(TransactionType type, long amount ,long fee, String recipientId, String[] args,  PublicKey publicKey) throws SecurityException{

        OptionInfo optionInfo =  new OptionInfo(fee, ContractType.None, args);
        return new TransactionInfo()
                .setTransactionType(type)
                .setAmount(amount)
                .setFee(fee)
                .setTimestamp(getSecurity().getTransactionTimestamp())
                .setSenderPublicKey(getSecurity().encodePublicKey(publicKey))
                .setOption(optionInfo);
    }



    protected TransactionInfo signatureAndGenerateTransactionId(TransactionInfo transaction,
                                                                PrivateKey privateKey, String secondSecret) throws SecurityException{
        transaction.setSignature(getSecurity().Signature(transaction, privateKey));

        if (null != secondSecret) {
            KeyPair secondKeyPair = getSecurity().generateKeyPair(secondSecret);
            transaction.setSecondSignature(getSecurity().SignSignature(transaction, secondKeyPair.getPrivate()));
        }

        transaction.setTransactionId(getSecurity().generateTransactionId(transaction));
        return transaction;
    }

    protected TransactionInfo signatureDAppTransaction(TransactionInfo transaction,
                                                                PrivateKey privateKey) throws SecurityException{
        byte[] bytes = transaction.getBytes(true,true);
        String signature = getSecurity().signBytes(bytes,privateKey);
        Log.d(TAG,"signatureDAppTransaction:"+signature);
        transaction.setSignature(signature);
        return transaction;
    }

    protected TransactionInfo signatureArgsTransaction(TransactionInfo transaction,
                                                       PrivateKey privateKey) throws SecurityException{
        byte[] bytes = transaction.getBytes(true,true);
        String signature = getSecurity().signBytes(bytes,privateKey);
        Log.d(TAG,"signatureDAppTransaction:"+signature);
        transaction.setSignature(signature);
        return transaction;
    }

    protected SecurityStrategy getSecurity(){
        return AschFactory.getInstance().getSecurity();
    }

}
