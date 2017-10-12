package so.asch.sdk.transaction;

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

import static so.asch.sdk.TransactionType.Transfer;

public class TransactionBuilder {

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
        KeyPair keyPair = getSecurity().generateKeyPair(secret);
        KeyPair secondKeyPair = getSecurity().generateKeyPair(secondSecret);

        TransactionInfo transaction = newTransaction(
                TransactionType.Delegate,
                0L,
                AschConst.Fees.DELEGATE,
                keyPair.getPublic());

        transaction.setAsset(new DelegateAssetInfo(userName, transaction.getSenderPublicKey()));

        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(),secondSecret);
    }

    public TransactionInfo buildSignature(String secret, String secondSecret) throws  SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);
        KeyPair secondKeyPair = getSecurity().generateKeyPair(secondSecret);

        TransactionInfo transaction =  newTransaction(
                TransactionType.Signature,
                0L,
                AschConst.Fees.SECOND_SIGNATURE,
                keyPair.getPublic())
                .setAsset(new SignatureAssetInfo(getSecurity().encodePublicKey(secondKeyPair.getPublic())));

        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), null);

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

    public TransactionInfo buildTransfer(String targetAddress, long amount, String message,
                                         String secret, String secondSecret) throws  SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);

        TransactionInfo transaction =  newTransaction(
                Transfer,
                amount,
                AschConst.Fees.TRANSFER,
                keyPair.getPublic())
                .setMessage(message)
                .setRecipientId(targetAddress)
                .setAsset(new TransferAssetInfo());

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

    public TransactionInfo buildUIATransfer(String currency, long amount, String targetAddress, String message,
                                            String secret, String secondSecret) throws SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);

        TransactionInfo transaction =  newTransaction(
                TransactionType.UIATransfer,
                0,
                AschConst.Fees.UIA_TRANSFER,
                keyPair.getPublic())
                .setMessage(message)
                .setRecipientId(targetAddress)
                .setAsset(new UIATransferAssetInfo(amount, currency));

        return signatureAndGenerateTransactionId(transaction, keyPair.getPrivate(), secondSecret);
    }

    /**
     * Dapp inner transaction
     * @param fee
     * @param type
     * @param args
     * @param secret
     * @return
     * @throws SecurityException
     */
    public TransactionInfo buildInnerTransaction(long fee, TransactionType type, String [] args, String secret) throws SecurityException{
        KeyPair keyPair = getSecurity().generateKeyPair(secret);

        TransactionInfo transaction= newTransaction(TransactionType.InTransfer,0,fee,keyPair.getPublic())
                .setOption(new OptionInfo(fee,type,args));
        return signatureAndGenerateTransactionId(transaction,keyPair.getPrivate(),null);
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


        protected TransactionInfo newTransaction(TransactionType type, long amount, long fee, PublicKey publicKey, String dappID, OptionInfo optionInfo) throws SecurityException{
        switch (type){
            case Transfer:
                return new TransactionInfo()
                        .setTransactionType(type)
                        .setAmount(amount)
                        .setFee(fee)
                        .setTimestamp(getSecurity().getTransactionTimestamp())
                        .setSenderPublicKey(getSecurity().encodePublicKey(publicKey));
            case Signature:
                break;
            case Delegate:
                break;
            case Vote:
                break;
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
                break;
            case Lock:
                break;
        }
        return null;
    }


    protected TransactionInfo signatureAndGenerateTransactionId(TransactionInfo transaction,
                                                                PrivateKey privateKey, String secondSecret) throws SecurityException{
        transaction.setSignature(getSecurity().Signature(transaction, privateKey));

        if (null != secondSecret) {
            KeyPair secondKeyPair = getSecurity().generateKeyPair(secondSecret);
            transaction.setSignSignature(getSecurity().SignSignature(transaction, secondKeyPair.getPrivate()));
        }

        transaction.setTransactionId(getSecurity().generateTransactionId(transaction));
        return transaction;
    }

    protected SecurityStrategy getSecurity(){
        return AschFactory.getInstance().getSecurity();
    }

}
