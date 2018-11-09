package asch.io.wallet.model.db;

import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by kimziv on 2017/9/21.
 */

public class RealmResultsObservable<T extends RealmObject> extends Observable <RealmResults<T>>{

    /**
     * Creates an Observable with a Function to execute when it is subscribed to.
     * <p>
     * <em>Note:</em> Use {@link #create(OnSubscribe)} to create an Observable, instead of this constructor,
     * unless you specifically have a need for inheritance.
     *
     * @param o {@link OnSubscribe} to be executed when {@link #subscribe(Subscriber)} is called
     */
    protected RealmResultsObservable(final Observable<RealmResults<T>> o) {
        super(new OnSubscribe<RealmResults<T>>() {
            @Override
            public void call(Subscriber<? super RealmResults<T>> subscriber) {
                o.unsafeSubscribe(subscriber);
            }
        });
    }
}
