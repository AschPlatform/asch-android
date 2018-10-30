package asch.so.wallet;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by kimziv on 2018/8/2.
 */

public class DBMigration implements RealmMigration {
    private static String TAG=DBMigration.class.getName();

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();

        // Migrate to version 1: Add a new class.
        // Example:
        // public Person extends RealmObject {
        //     private String name;
        //     private int age;
        //     // getters and setters left out for brevity
        // }
        Log.i(TAG,"oldVersion:"+oldVersion);
        if (oldVersion == 2) {
            if ( schema.contains("DApp")){
                return;
            }
            schema.create("DApp")
                    .addField("category", int.class)
                    .addField("name", String.class)
                    .addField("description", String.class)
                    .addField("tags", String.class)
                    .addField("type", int.class)
                    .addField("link", String.class)
                    .addField("icon", String.class)
                    .addField("unlockDelegates", int.class)
                    .addField("transactionId", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("downloadId", int.class)
                    .addField("downloadUrl", String.class)
                    .addField("downloadPath", String.class)
                    .addField("installedPath", String.class)
                    .addField("status", int.class)
                    .addField("sofarBytes", long.class)
                    .addField("totalBytes", long.class)
                    .addField("verion", String.class)
                    .addField("build", int.class)
                    .addField("publishTimestamp", int.class)
                    .addField("updateTimestamp", int.class);

            oldVersion++;
        }

        if (oldVersion == 3) {
            schema.create("AschAsset")
                    .addField("showState",int.class)
                    .addField("address",String.class)
                    .addField("aid",String.class,FieldAttribute.PRIMARY_KEY)
                    .addField("type",int.class)
                    .addField("name",String.class)
                    .addField("desc",String.class)
                    .addField("precision",int.class)
                    .addField("balance",String.class)
                    .addField("trueBalance",float.class)
                    .addField("tid",String.class)
                    .addField("maximum",String.class)
                    .addField("quantity",String.class)
                    .addField("issuerId",String.class)
                    .addField("timestamp",long.class)
                    .addField("version",int.class)
                    .addField("gateway",String.class)
                    .addField("symbol",String.class)
                    .addField("revoked",int.class)
                    .addField("flag",int.class)
                    .addField("createTimestamp",int.class)
                    .addField("updateTimestamp",int.class)
                    .addField("strategy",String.class)
                    .addField("height",long.class)
                    .addField("acl",int.class)
                    .addField("writeoff",int.class)
                    .addField("allowWriteoff",int.class)
                    .addField("allowWhitelist",int.class)
                    .addField("allowBlacklist",int.class)
                    .addField("maximumShow",String.class)
                    .addField("quantityShow",String.class)
                    .addField("xasTotal",String.class);

            schema.get("Account")
                    .addField("saveSecondPwdState", int.class)
                    .removeField("enryptSecondSecret")
                    .removeField("encryptPasswd")
                    .removeField("hint")
                    .removeField("passwd");

            oldVersion++;
        }
    }
}
