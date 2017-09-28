package so.asch.sdk;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * Created by kimziv on 2017/9/28.
 */

public class Utils {

    public static String join(CharSequence delimiter, CharSequence... elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        // Number of elements not likely worth Arrays.stream overhead.
        StringBuilder builder = new StringBuilder();
        for (CharSequence cs: elements) {
            if (cs == elements[0]){
                builder.append(cs);
            }else {
                builder.append(delimiter);
                builder.append(cs);
            }
        }
        return builder.toString();
    }

    public static String join(CharSequence delimiter,
                              Iterable<? extends CharSequence> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringBuilder builder = new StringBuilder();
        for (CharSequence cs: elements) {
            if (elements.iterator().hasNext()){
                builder.append(cs);
                builder.append(delimiter);
            }else {
                builder.append(cs);
            }
        }
        return builder.toString();
    }
}
