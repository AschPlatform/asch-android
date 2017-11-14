package asch.so.wallet.accounts;

import android.content.Context;
import android.graphics.Color;

import org.bitcoinj.crypto.MnemonicCode;

import java.io.IOException;
import java.io.InputStream;

import so.asch.sdk.security.Bip39;

/**
 * Created by kimziv on 2017/10/17.
 */

public class Wallet {

    private static Wallet instance=null;
    private Context context;
    private  MnemonicCode mnemonicCode;

    public static Wallet getInstance(){
        return instance;
    }

    public static void init(Context ctx){
        instance=new Wallet(ctx);
    }

    public Wallet(Context ctx) {
        this.context=ctx;
        try {
            InputStream wis = ctx.getResources().getAssets().open(Bip39.BIP39_WORDLIST_FILENAME);
            if(wis != null) {
                this.mnemonicCode = new MnemonicCode(wis, Bip39.BIP39_ENGLISH_SHA256);
                wis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MnemonicCode getMnemonicCode() {
        return mnemonicCode;
    }
}
