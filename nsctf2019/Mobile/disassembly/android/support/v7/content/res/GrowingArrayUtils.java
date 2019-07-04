// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.content.res;

import java.lang.reflect.Array;

final class GrowingArrayUtils
{
    public static int[] append(final int[] array, final int n, final int n2) {
        assert n <= array.length;
        int[] array2 = array;
        if (n + 1 > array.length) {
            array2 = new int[growSize(n)];
            System.arraycopy(array, 0, array2, 0, n);
        }
        array2[n] = n2;
        return array2;
    }
    
    public static long[] append(final long[] array, final int n, final long n2) {
        assert n <= array.length;
        long[] array2 = array;
        if (n + 1 > array.length) {
            array2 = new long[growSize(n)];
            System.arraycopy(array, 0, array2, 0, n);
        }
        array2[n] = n2;
        return array2;
    }
    
    public static <T> T[] append(final T[] array, final int n, final T t) {
        assert n <= array.length;
        Object[] array2 = array;
        if (n + 1 > array.length) {
            array2 = (Object[])Array.newInstance(array.getClass().getComponentType(), growSize(n));
            System.arraycopy(array, 0, array2, 0, n);
        }
        array2[n] = t;
        return (T[])array2;
    }
    
    public static boolean[] append(final boolean[] array, final int n, final boolean b) {
        assert n <= array.length;
        boolean[] array2 = array;
        if (n + 1 > array.length) {
            array2 = new boolean[growSize(n)];
            System.arraycopy(array, 0, array2, 0, n);
        }
        array2[n] = b;
        return array2;
    }
    
    public static int growSize(final int n) {
        if (n <= 4) {
            return 8;
        }
        return n * 2;
    }
    
    public static int[] insert(final int[] array, final int n, final int n2, final int n3) {
        assert n <= array.length;
        if (n + 1 <= array.length) {
            System.arraycopy(array, n2, array, n2 + 1, n - n2);
            array[n2] = n3;
            return array;
        }
        final int[] array2 = new int[growSize(n)];
        System.arraycopy(array, 0, array2, 0, n2);
        array2[n2] = n3;
        System.arraycopy(array, n2, array2, n2 + 1, array.length - n2);
        return array2;
    }
    
    public static long[] insert(final long[] array, final int n, final int n2, final long n3) {
        assert n <= array.length;
        if (n + 1 <= array.length) {
            System.arraycopy(array, n2, array, n2 + 1, n - n2);
            array[n2] = n3;
            return array;
        }
        final long[] array2 = new long[growSize(n)];
        System.arraycopy(array, 0, array2, 0, n2);
        array2[n2] = n3;
        System.arraycopy(array, n2, array2, n2 + 1, array.length - n2);
        return array2;
    }
    
    public static <T> T[] insert(final T[] array, final int n, final int n2, final T t) {
        assert n <= array.length;
        if (n + 1 <= array.length) {
            System.arraycopy(array, n2, array, n2 + 1, n - n2);
            array[n2] = t;
            return array;
        }
        final Object[] array2 = (Object[])Array.newInstance(array.getClass().getComponentType(), growSize(n));
        System.arraycopy(array, 0, array2, 0, n2);
        array2[n2] = t;
        System.arraycopy(array, n2, array2, n2 + 1, array.length - n2);
        return (T[])array2;
    }
    
    public static boolean[] insert(final boolean[] array, final int n, final int n2, final boolean b) {
        assert n <= array.length;
        if (n + 1 <= array.length) {
            System.arraycopy(array, n2, array, n2 + 1, n - n2);
            array[n2] = b;
            return array;
        }
        final boolean[] array2 = new boolean[growSize(n)];
        System.arraycopy(array, 0, array2, 0, n2);
        array2[n2] = b;
        System.arraycopy(array, n2, array2, n2 + 1, array.length - n2);
        return array2;
    }
}
