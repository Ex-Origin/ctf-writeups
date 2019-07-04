// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content;

import java.util.ArrayList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class MimeTypeFilter
{
    public static String matches(@Nullable String s, @NonNull final String[] array) {
        if (s != null) {
            final String[] split = s.split("/");
            for (int length = array.length, i = 0; i < length; ++i) {
                if (mimeTypeAgainstFilter(split, (s = array[i]).split("/"))) {
                    return s;
                }
            }
            return null;
        }
        s = null;
        return s;
    }
    
    public static String matches(@Nullable final String[] array, @NonNull String s) {
        if (array != null) {
            final String[] split = s.split("/");
            for (int length = array.length, i = 0; i < length; ++i) {
                if (mimeTypeAgainstFilter((s = array[i]).split("/"), split)) {
                    return s;
                }
            }
            return null;
        }
        s = null;
        return s;
    }
    
    public static boolean matches(@Nullable final String s, @NonNull final String s2) {
        return s != null && mimeTypeAgainstFilter(s.split("/"), s2.split("/"));
    }
    
    public static String[] matchesMany(@Nullable final String[] array, @NonNull final String s) {
        int i = 0;
        if (array == null) {
            return new String[0];
        }
        final ArrayList<String> list = new ArrayList<String>();
        final String[] split = s.split("/");
        while (i < array.length) {
            final String s2 = array[i];
            if (mimeTypeAgainstFilter(s2.split("/"), split)) {
                list.add(s2);
            }
            ++i;
        }
        return list.toArray(new String[list.size()]);
    }
    
    private static boolean mimeTypeAgainstFilter(@NonNull final String[] array, @NonNull final String[] array2) {
        if (array2.length != 2) {
            throw new IllegalArgumentException("Ill-formatted MIME type filter. Must be type/subtype.");
        }
        if (array2[0].isEmpty() || array2[1].isEmpty()) {
            throw new IllegalArgumentException("Ill-formatted MIME type filter. Type or subtype empty.");
        }
        return array.length == 2 && ("*".equals(array2[0]) || array2[0].equals(array[0])) && ("*".equals(array2[1]) || array2[1].equals(array[1]));
    }
}
