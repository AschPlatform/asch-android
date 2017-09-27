package so.asch.sdk.security;


import org.bitcoinj.core.Context;
import org.bitcoinj.crypto.MnemonicCode;

import java.io.InputStream;
import java.lang.SecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

public class Bip39 {

    public static String generateMnemonicCode(byte[] entropy) throws SecurityException{
        try {
            String[] words = MnemonicCode.INSTANCE.toMnemonic(entropy).toArray(new String[0]);

            return String.join(" ", words);
        }
        catch (Exception ex){
            throw new SecurityException("generate mnemonic code failed", ex);
        }
    }

    private static String stringJoin(CharSequence delimiter, CharSequence... elements){
        StringBuilder builder=new StringBuilder();
        for (CharSequence element :
                elements) {
           if (element!=elements[0]){
               builder.append(delimiter);
               builder.append(element);
           }else{
               builder.append(element);
           }
        }
        return builder.toString();
    }

    public static final String BIP39_ENGLISH_SHA256 = "ad90bf3beb7b0eb7e5acd74727dc0da96e0a280a258354e7293fb7e211ac03db";
    public static final String BIP39_WORDLIST_FILENAME = "bip39-wordlist.txt";
    /**
     * 生成助记词
     * @param wordsNum
     * @return
     * @throws SecurityException
     */
    public static String generateMnemonic(InputStream wordstream, int wordsNum) throws  SecurityException{
        try {
            if((wordsNum % 3 != 0) || (wordsNum < 12 || wordsNum > 24)) {
                wordsNum = 12;
            }
            // len == 16 (12 words), len == 24 (18 words), len == 32 (24 words)
            int len = (wordsNum / 3) * 4;
            SecureRandom random = new SecureRandom();
            byte ent[] = new byte[len];
            random.nextBytes(ent);
           // InputStream wis = context.getResources().getAssets().open(BIP39_WORDLIST_FILENAME);
            if(wordstream != null) {
                MnemonicCode mc = new MnemonicCode(wordstream, BIP39_ENGLISH_SHA256);
                wordstream.close();
                List<String> words = mc.toMnemonic(ent);

                return stringJoin(" ",words.toArray(new String[0]));
            }
            return null;
        }catch (Exception ex){
            throw new SecurityException("generate mnemonic code failed", ex);
        }
    }

    /**
     * 生成长度为12的助记词
     * @param wordstream
     * @return
     * @throws SecurityException
     */
    public static String generateMnemonic12Words(InputStream wordstream) throws  SecurityException{
        try {
            int wordsNum = 12;
            // len == 16 (12 words), len == 24 (18 words), len == 32 (24 words)
            int len = (wordsNum / 3) * 4;
            SecureRandom random = new SecureRandom();
            byte ent[] = new byte[len];
            random.nextBytes(ent);
            if(wordstream != null) {
                MnemonicCode mc = new MnemonicCode(wordstream, BIP39_ENGLISH_SHA256);
                wordstream.close();
                List<String> words = mc.toMnemonic(ent);

                return stringJoin(" ",words.toArray(new String[0]));
            }
            return null;
        }catch (Exception ex){
            throw new SecurityException("generate mnemonic code failed", ex);
        }
    }

    public static boolean isValidMnemonicCode(String mnemonicCode){

        if (mnemonicCode == null || mnemonicCode.equals(""))
            return false;

        List<String> words = Arrays.asList(mnemonicCode.split(" "));
        try{
            //MnemonicCode.INSTANCE.check(words);
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }
}
