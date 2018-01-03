package asch.so.wallet;

//import so.asch.sdk.AschSDK;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;


import java.io.InputStream;
import java.security.PrivateKey;
import java.util.Random;

import asch.so.wallet.model.entity.Account;
import io.realm.Realm;
import io.realm.annotations.PrimaryKey;
import so.asch.sdk.AschHelper;
import so.asch.sdk.AschResult;
import so.asch.sdk.AschSDK;
import so.asch.sdk.impl.AschFactory;
import so.asch.sdk.security.Bip39;
import so.asch.sdk.security.DefaultSecurityStrategy;
import so.asch.sdk.security.SecurityException;

/**
 * Created by eagle on 17-7-16.
 */
public class TestData {

   public static  final  String TAG =TestData.class.getSimpleName();

   // public static final String root = "http://127.0.0.1:4096";
    public static final String root = "http://192.168.1.102:4096";//"http://172.16.100.239:4096";//http://testnet.asch.so:4096";//"http://101.200.84.232:4097";//http://106.14.156.245:4096"; "http://wallet.asch.mobi:9999";
    public static final String secret = "spatial slush action typical emerge feature confirm edge game desk orphan burst";
    public static final String address = "AHcGmYnCyr6jufT5AGbpmRUv55ebwMLCym";
    public static final String secondSecret = null;//"asch111111";
    public static final String userName = "asch_g11";
    public static final String magic="594fe0f3";

    //asch_g101 11705168753296944226 00dd0e70fe22e9b580ba0ccf696f1d108a6475bb303b8689947ba346b0b02ad9
    public static final String targetAddress ="AAmTPHrenFQdZHnUGqhE3qV9nr8BP5SzWx";// "11705168753296944226";

    //asch_g101	11705168753296944226 00dd0e70fe22e9b580ba0ccf696f1d108a6475bb303b8689947ba346b0b02ad9
    //asch_g96	16093201530526106149 0225c340addf24211f4b51ea24cb0565c67f09600f57bad89af350b38b08a366
    //asch_g32	4354832300657989346 0ae2e3bcd8c959bccc34445a9473eab1bece60300f3aa00d89612923470dee75
    //public static final String[] voted = "00dd0e70fe22e9b580ba0ccf696f1d108a6475bb303b8689947ba346b0b02ad9,0225c340addf24211f4b51ea24cb0565c67f09600f57bad89af350b38b08a366,0ae2e3bcd8c959bccc34445a9473eab1bece60300f3aa00d89612923470dee75".split(",");
    
    //asch_g84 17c92caada699222de561bf8994ea5057b2c698b9704db2d925b9c16cc2f8f24
    //asch_g93 0f77baea7e7b3e0147ca856dbfa58e556e72499562888cd725397bb6ea780938
    public static final String[] voted = "17c92caada699222de561bf8994ea5057b2c698b9704db2d925b9c16cc2f8f24,0f77baea7e7b3e0147ca856dbfa58e556e72499562888cd725397bb6ea780938".split(",");

    //asch_g23	4752137788839516181 486179424b4bcfaf7960a4a121277d73e4a7c9e0c27b254edf978762d5a6dfe6
    //asch_g40	16494449392359591122 49b369ff2635e2ac083d62a6c59baf872fbdef6990297f296c95e1830118aba1
    //public static final String[] canceled = "486179424b4bcfaf7960a4a121277d73e4a7c9e0c27b254edf978762d5a6dfe6,49b369ff2635e2ac083d62a6c59baf872fbdef6990297f296c95e1830118aba1".split(",");
    public static final String[] canceled = {"253fef39a05c0025033a6b7a3cf8a37913751b7a7ec791b26feef5986c110946"};

    public static final String blockId = "a9a4d8263b68a238bd934943624a2a59f13525b32b0957ea2328d4ff346de174";

    public static final long blockHeight = 6900;

    public static final String blockHash = "979fa5571901256bb10e42d5ae88e5f5108b6f684938beaf4281bf9cfa2041850409eee45cc5f2a37aa93717f6079da9a549496c53ee9ff510c97c2e0c0b6c0a";

    public static final String transactionId = "12936058387774240556";

    public static final String senderPublicKey = "fd6df6dc35852ac7edcc081eb5195718e0c77a6ad4f8157eeb78c865fa83efc4";

    public  static final String dappID="f8da1167eabc89111004e6a3e5165855458ef4ad1600a0ea98a13c77d23f719f";




    public static void createTestAccountsData(){
     //final Random random =new Random(1234);
     Realm realm=Realm.getDefaultInstance();
     realm.executeTransaction(new Realm.Transaction(){
      @Override
      public void execute(Realm realm) {
       int i;
       for (i = 0;i<20;i++){
        Account account=createAccount(i);
         realm.insertOrUpdate(account);
       }
      }
     });
    }

    private static Account createAccount(int i){

     Account account= new Account();//realm.createObject(Account.class);
     account.setAddress("address"+i);
     account.setName("name"+i);
    // account.address="address"+i;
     account.setPublicKey("publicKey"+i);
     return account;
    }



 public static  String testGenerateSeeds(Context context){
  try {
   InputStream wis = context.getResources().getAssets().open(Bip39.BIP39_WORDLIST_FILENAME);
   String words = Bip39.generateMnemonic(wis,12);

   LogUtils.iTag(TAG, "words:"+words);
   return words;
  }catch (Exception ex){
   ex.printStackTrace();
  }
  return null;
 }

 public static void configAschSDK(){

  String url= AppConfig.getNodeURL();
  AschSDK.Config.setAschServer(TextUtils.isEmpty(url)?AppConstants.DEFAULT_NODE_URL:url);
  AschSDK.Config.setMagic(magic);
  //String.join(" ", new String[]{"aaa", "bbb"});
 }

 public static   void testSDK(){
  LogUtils.dTag(TAG, "publicKey:testSDK begin");
  String publicKey = AschSDK.Helper.getPublicKey(secret);
  LogUtils.dTag(TAG, "publicKey:"+publicKey);

  String  address = null;
  try {
   address = AschFactory.getInstance().getSecurity().getAddress(publicKey);
   LogUtils.iTag(TAG, "address:"+address);
  } catch (SecurityException e) {
   e.printStackTrace();
  }

//  new Thread(new Runnable() {
//   @Override
//   public void run() {
////    AschSDK.Config.setAschServer(root);
////    AschResult result= AschSDK.Account.login(secret);
////    String rawJson=result.getRawJson();
////    Log.d("++++++++"+TAG, rawJson+" ");
//
//    //Assert.assertTrue(result.isSuccessful());
//
////    result= AschSDK.Account.secureLogin(TestData.secret);
////    Log.d("++++++++"+TAG+"-secureLogin:", rawJson+" ");
//    //Assert.assertTrue(result.isSuccessful());
////    String publicKey = AschSDK.Helper.getPublicKey(secret);
////    Log.d(TAG, "publicKey:"+publicKey);
//
//
//    String  address = null;
//    try {
//     address = AschFactory.getInstance().getSecurity().getAddress(publicKey);
//    } catch (SecurityException e) {
//     e.printStackTrace();
//    }
//
//   // Log.d(TAG, "address:"+address);
//
//    //AschHelper helper =new AschHelper();
//    //String secret= helper.generateSecret();
//    // Log.d(mTAG,"++++++++ "+secret);
//   }
//  }).start();

 }

}
