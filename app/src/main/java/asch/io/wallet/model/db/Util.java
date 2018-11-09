package asch.io.wallet.model.db;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by kimziv on 2017/9/21.
 */

public class Util {
    @SuppressWarnings("unchecked")
    protected static void collectRelatedRealmClass(Set<String> set, Class<? extends RealmObject> start) {
        if (set.add(start.getCanonicalName())) {
            List<Field> fields = ReflectionUtil.getPrivateFields(start);
            for (Field field: fields) {
                if (ReflectionUtil.isSubclass(field, RealmObject.class)) {
                    final Class fieldType = field.getType();
                    collectRelatedRealmClass(set, (Class<? extends RealmObject>) fieldType);
                } else if (ReflectionUtil.isSubclass(field, RealmList.class)) {
                    final ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                    final Class aClass = (Class<?>) genericType.getActualTypeArguments()[0];
                    collectRelatedRealmClass(set, (Class<? extends RealmObject>) aClass);
                }
            }
        }

    }

    public static Set<String> collectRelatedRealmClass(Class<? extends RealmObject> start)  {
        Set<String> set = new HashSet<>();
        collectRelatedRealmClass(set, start);
        return set;
    }
}
