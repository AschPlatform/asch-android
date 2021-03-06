package asch.io.base.util;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @author kimziv (kimziv@qq.com)
 *         2017/2/17
 */
public final class RxSchedulerUtils {

    /**
     * 在RxJava的使用过程中我们会频繁的调用subscribeOn()和observeOn(),通过Transformer结合
     * Observable.compose()我们可以复用这些代码
     *
     * @return Transformer
     */
    public static <T> Observable.Transformer<T, T> normalSchedulersTransformer() {

        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
