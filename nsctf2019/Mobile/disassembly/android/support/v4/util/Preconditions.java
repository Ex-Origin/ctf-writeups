// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

import android.text.TextUtils;
import android.support.annotation.NonNull;
import java.util.Iterator;
import java.util.Collection;
import android.support.annotation.IntRange;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class Preconditions
{
    public static void checkArgument(final boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
    }
    
    public static void checkArgument(final boolean b, final Object o) {
        if (!b) {
            throw new IllegalArgumentException(String.valueOf(o));
        }
    }
    
    public static float checkArgumentFinite(final float n, final String s) {
        if (Float.isNaN(n)) {
            throw new IllegalArgumentException(s + " must not be NaN");
        }
        if (Float.isInfinite(n)) {
            throw new IllegalArgumentException(s + " must not be infinite");
        }
        return n;
    }
    
    public static float checkArgumentInRange(final float n, final float n2, final float n3, final String s) {
        if (Float.isNaN(n)) {
            throw new IllegalArgumentException(s + " must not be NaN");
        }
        if (n < n2) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%f, %f] (too low)", s, n2, n3));
        }
        if (n > n3) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%f, %f] (too high)", s, n2, n3));
        }
        return n;
    }
    
    public static int checkArgumentInRange(final int n, final int n2, final int n3, final String s) {
        if (n < n2) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%d, %d] (too low)", s, n2, n3));
        }
        if (n > n3) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%d, %d] (too high)", s, n2, n3));
        }
        return n;
    }
    
    public static long checkArgumentInRange(final long n, final long n2, final long n3, final String s) {
        if (n < n2) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%d, %d] (too low)", s, n2, n3));
        }
        if (n > n3) {
            throw new IllegalArgumentException(String.format("%s is out of range of [%d, %d] (too high)", s, n2, n3));
        }
        return n;
    }
    
    @IntRange(from = 0L)
    public static int checkArgumentNonnegative(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        return n;
    }
    
    @IntRange(from = 0L)
    public static int checkArgumentNonnegative(final int n, final String s) {
        if (n < 0) {
            throw new IllegalArgumentException(s);
        }
        return n;
    }
    
    public static long checkArgumentNonnegative(final long n) {
        if (n < 0L) {
            throw new IllegalArgumentException();
        }
        return n;
    }
    
    public static long checkArgumentNonnegative(final long n, final String s) {
        if (n < 0L) {
            throw new IllegalArgumentException(s);
        }
        return n;
    }
    
    public static int checkArgumentPositive(final int n, final String s) {
        if (n <= 0) {
            throw new IllegalArgumentException(s);
        }
        return n;
    }
    
    public static float[] checkArrayElementsInRange(final float[] array, final float n, final float n2, final String s) {
        checkNotNull(array, s + " must not be null");
        for (int i = 0; i < array.length; ++i) {
            final float n3 = array[i];
            if (Float.isNaN(n3)) {
                throw new IllegalArgumentException(s + "[" + i + "] must not be NaN");
            }
            if (n3 < n) {
                throw new IllegalArgumentException(String.format("%s[%d] is out of range of [%f, %f] (too low)", s, i, n, n2));
            }
            if (n3 > n2) {
                throw new IllegalArgumentException(String.format("%s[%d] is out of range of [%f, %f] (too high)", s, i, n, n2));
            }
        }
        return array;
    }
    
    public static <T> T[] checkArrayElementsNotNull(final T[] array, final String s) {
        if (array == null) {
            throw new NullPointerException(s + " must not be null");
        }
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == null) {
                throw new NullPointerException(String.format("%s[%d] must not be null", s, i));
            }
        }
        return array;
    }
    
    @NonNull
    public static <C extends Collection<T>, T> C checkCollectionElementsNotNull(final C c, final String s) {
        if (c == null) {
            throw new NullPointerException(s + " must not be null");
        }
        long n = 0L;
        final Iterator<T> iterator = c.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) {
                throw new NullPointerException(String.format("%s[%d] must not be null", s, n));
            }
            ++n;
        }
        return c;
    }
    
    public static <T> Collection<T> checkCollectionNotEmpty(final Collection<T> collection, final String s) {
        if (collection == null) {
            throw new NullPointerException(s + " must not be null");
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(s + " is empty");
        }
        return collection;
    }
    
    public static int checkFlagsArgument(final int n, final int n2) {
        if ((n & n2) != n) {
            throw new IllegalArgumentException("Requested flags 0x" + Integer.toHexString(n) + ", but only 0x" + Integer.toHexString(n2) + " are allowed");
        }
        return n;
    }
    
    @NonNull
    public static <T> T checkNotNull(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }
    
    @NonNull
    public static <T> T checkNotNull(final T t, final Object o) {
        if (t == null) {
            throw new NullPointerException(String.valueOf(o));
        }
        return t;
    }
    
    public static void checkState(final boolean b) {
        checkState(b, null);
    }
    
    public static void checkState(final boolean b, final String s) {
        if (!b) {
            throw new IllegalStateException(s);
        }
    }
    
    @NonNull
    public static <T extends CharSequence> T checkStringNotEmpty(final T t) {
        if (TextUtils.isEmpty((CharSequence)t)) {
            throw new IllegalArgumentException();
        }
        return t;
    }
    
    @NonNull
    public static <T extends CharSequence> T checkStringNotEmpty(final T t, final Object o) {
        if (TextUtils.isEmpty((CharSequence)t)) {
            throw new IllegalArgumentException(String.valueOf(o));
        }
        return t;
    }
}
