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


//        // Migrate to version 2: Add a primary key + object references
//        // Example:
//        // public Person extends RealmObject {
//        //     private String name;
//        //     @PrimaryKey
//        //     private int age;
//        //     private Dog favoriteDog;
//        //     private RealmList<Dog> dogs;
//        //     // getters and setters left out for brevity
//        // }
//        if (oldVersion == 1) {
//            schema.get("Person")
//                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
//                    .addRealmObjectField("favoriteDog", schema.get("Dog"))
//                    .addRealmListField("dogs", schema.get("Dog"));
//            oldVersion++;
//        }
    }
}
