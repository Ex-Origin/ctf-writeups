// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

class ContainerHelpers
{
    static final int[] EMPTY_INTS;
    static final long[] EMPTY_LONGS;
    static final Object[] EMPTY_OBJECTS;
    
    static {
        EMPTY_INTS = new int[0];
        EMPTY_LONGS = new long[0];
        EMPTY_OBJECTS = new Object[0];
    }
    
    static int binarySearch(final int[] array, int i, final int n) {
        final int n2 = 0;
        final int n3 = i - 1;
        i = n2;
        int n4 = n3;
        while (i <= n4) {
            final int n5 = i + n4 >>> 1;
            final int n6 = array[n5];
            if (n6 < n) {
                i = n5 + 1;
            }
            else {
                final int n7 = n5;
                if (n6 <= n) {
                    return n7;
                }
                n4 = n5 - 1;
            }
        }
        return ~i;
    }
    
    static int binarySearch(final long[] array, int i, final long n) {
        final int n2 = 0;
        final int n3 = i - 1;
        i = n2;
        int n4 = n3;
        while (i <= n4) {
            final int n5 = i + n4 >>> 1;
            final long n6 = array[n5];
            if (n6 < n) {
                i = n5 + 1;
            }
            else {
                final int n7 = n5;
                if (n6 <= n) {
                    return n7;
                }
                n4 = n5 - 1;
            }
        }
        return ~i;
    }
    
    public static boolean equal(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static int idealByteArraySize(final int n) {
        int n2 = 4;
        int n3;
        while (true) {
            n3 = n;
            if (n2 >= 32) {
                break;
            }
            if (n <= (1 << n2) - 12) {
                n3 = (1 << n2) - 12;
                break;
            }
            ++n2;
        }
        return n3;
    }
    
    public static int idealIntArraySize(final int n) {
        return idealByteArraySize(n * 4) / 4;
    }
    
    public static int idealLongArraySize(final int n) {
        return idealByteArraySize(n * 8) / 8;
    }
}
