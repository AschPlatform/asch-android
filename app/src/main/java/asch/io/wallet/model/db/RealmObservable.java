package asch.io.wallet.model.db;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;



public class RealmObservable {
    private RealmObservable() {

    }

    public static <T extends Object> Observable<T> createObservable(final Func1<Realm, T> function) {
        return Observable.create(new OnSubscribeRealm<T>() {
            @Override
            public T get(Realm realm) {
                T t = function.call(realm);
                if(t!=null){
                    if (t instanceof RealmObject) {
                        return (T) realm.copyFromRealm((RealmObject)t);
                    } else if (t instanceof RealmList) {
                        return (T) realm.copyFromRealm((List<RealmObject>) t);
                    } else if(t instanceof RealmResults){
                        return (T) realm.copyFromRealm((List<RealmObject>) t);
                    }
                    return t;
                }
                return t;
            }
        });
    }

}
