// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

import java.util.Map;

public class SimpleArrayMap<K, V>
{
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final String TAG = "ArrayMap";
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    int[] mHashes;
    int mSize;
    
    public SimpleArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }
    
    public SimpleArrayMap(final int n) {
        if (n == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        }
        else {
            this.allocArrays(n);
        }
        this.mSize = 0;
    }
    
    public SimpleArrayMap(final SimpleArrayMap<K, V> simpleArrayMap) {
        this();
        if (simpleArrayMap != null) {
            this.putAll((SimpleArrayMap<? extends K, ? extends V>)simpleArrayMap);
        }
    }
    
    private void allocArrays(final int n) {
        while (true) {
            Label_0096: {
                if (n != 8) {
                    break Label_0096;
                }
                synchronized (ArrayMap.class) {
                    if (SimpleArrayMap.mTwiceBaseCache != null) {
                        final Object[] mTwiceBaseCache = SimpleArrayMap.mTwiceBaseCache;
                        this.mArray = mTwiceBaseCache;
                        SimpleArrayMap.mTwiceBaseCache = (Object[])mTwiceBaseCache[0];
                        this.mHashes = (int[])mTwiceBaseCache[1];
                        mTwiceBaseCache[0] = (mTwiceBaseCache[1] = null);
                        --SimpleArrayMap.mTwiceBaseCacheSize;
                        return;
                    }
                    // monitorexit(ArrayMap.class)
                    this.mHashes = new int[n];
                    this.mArray = new Object[n << 1];
                    return;
                }
            }
            if (n == 4) {
                synchronized (ArrayMap.class) {
                    if (SimpleArrayMap.mBaseCache != null) {
                        final Object[] mBaseCache = SimpleArrayMap.mBaseCache;
                        this.mArray = mBaseCache;
                        SimpleArrayMap.mBaseCache = (Object[])mBaseCache[0];
                        this.mHashes = (int[])mBaseCache[1];
                        mBaseCache[0] = (mBaseCache[1] = null);
                        --SimpleArrayMap.mBaseCacheSize;
                        return;
                    }
                }
                // monitorexit(ArrayMap.class)
                continue;
            }
            continue;
        }
    }
    
    private static void freeArrays(final int[] array, final Object[] array2, int i) {
    Label_0117:
        while (true) {
            Label_0037: {
                Block_1: {
                    if (array.length == 8) {
                        break Block_1;
                    }
                    break Label_0037;
                    while (i >= 2) {
                        array2[i] = null;
                        --i;
                    }
                    break Label_0037;
                }
                synchronized (ArrayMap.class) {
                    if (SimpleArrayMap.mTwiceBaseCacheSize < 10) {
                        array2[0] = SimpleArrayMap.mTwiceBaseCache;
                        array2[1] = array;
                        i = (i << 1) - 1;
                        continue Label_0117;
                    }
                    return;
                    SimpleArrayMap.mTwiceBaseCache = array2;
                    ++SimpleArrayMap.mTwiceBaseCacheSize;
                    return;
                }
            }
            if (array.length != 4) {
                return;
            }
            while (true) {
                while (true) {
                    Label_0134: {
                        synchronized (ArrayMap.class) {
                            if (SimpleArrayMap.mBaseCacheSize < 10) {
                                array2[0] = SimpleArrayMap.mBaseCache;
                                array2[1] = array;
                                i = (i << 1) - 1;
                                break Label_0134;
                            }
                            return;
                            SimpleArrayMap.mBaseCache = array2;
                            ++SimpleArrayMap.mBaseCacheSize;
                            return;
                        }
                        continue Label_0117;
                    }
                    while (i >= 2) {
                        array2[i] = null;
                        --i;
                    }
                    continue;
                }
            }
            break;
        }
    }
    
    public void clear() {
        if (this.mSize != 0) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        }
    }
    
    public boolean containsKey(final Object o) {
        return this.indexOfKey(o) >= 0;
    }
    
    public boolean containsValue(final Object o) {
        return this.indexOfValue(o) >= 0;
    }
    
    public void ensureCapacity(final int n) {
        if (this.mHashes.length < n) {
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            this.allocArrays(n);
            if (this.mSize > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, this.mSize);
                System.arraycopy(mArray, 0, this.mArray, 0, this.mSize << 1);
            }
            freeArrays(mHashes, mArray, this.mSize);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof SimpleArrayMap) {
                final SimpleArrayMap simpleArrayMap = (SimpleArrayMap)o;
                if (this.size() != simpleArrayMap.size()) {
                    return false;
                }
                int i = 0;
                try {
                    while (i < this.mSize) {
                        final K key = this.keyAt(i);
                        final V value = this.valueAt(i);
                        final Object value2 = simpleArrayMap.get(key);
                        if (value == null) {
                            if (value2 != null || !simpleArrayMap.containsKey(key)) {
                                return false;
                            }
                        }
                        else if (!value.equals(value2)) {
                            return false;
                        }
                        ++i;
                    }
                    return true;
                }
                catch (NullPointerException ex) {
                    return false;
                }
                catch (ClassCastException ex2) {
                    return false;
                }
            }
            if (o instanceof Map) {
                final Map map = (Map)o;
                if (this.size() != map.size()) {
                    return false;
                }
                int j = 0;
                try {
                    while (j < this.mSize) {
                        final K key2 = this.keyAt(j);
                        final V value3 = this.valueAt(j);
                        final Object value4 = map.get(key2);
                        if (value3 == null) {
                            if (value4 != null || !map.containsKey(key2)) {
                                return false;
                            }
                        }
                        else if (!value3.equals(value4)) {
                            return false;
                        }
                        ++j;
                    }
                    return true;
                }
                catch (NullPointerException ex3) {
                    return false;
                }
                catch (ClassCastException ex4) {
                    return false;
                }
                return false;
            }
            return false;
        }
        return true;
    }
    
    public V get(final Object o) {
        final int indexOfKey = this.indexOfKey(o);
        if (indexOfKey >= 0) {
            return (V)this.mArray[(indexOfKey << 1) + 1];
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        final int[] mHashes = this.mHashes;
        final Object[] mArray = this.mArray;
        int n = 0;
        for (int i = 0, n2 = 1; i < this.mSize; ++i, n2 += 2) {
            final Object o = mArray[n2];
            final int n3 = mHashes[i];
            int hashCode;
            if (o == null) {
                hashCode = 0;
            }
            else {
                hashCode = o.hashCode();
            }
            n += (hashCode ^ n3);
        }
        return n;
    }
    
    int indexOf(final Object o, final int n) {
        final int mSize = this.mSize;
        int n2;
        if (mSize == 0) {
            n2 = -1;
        }
        else {
            final int binarySearch = ContainerHelpers.binarySearch(this.mHashes, mSize, n);
            if ((n2 = binarySearch) >= 0) {
                n2 = binarySearch;
                if (!o.equals(this.mArray[binarySearch << 1])) {
                    int n3;
                    for (n3 = binarySearch + 1; n3 < mSize && this.mHashes[n3] == n; ++n3) {
                        if (o.equals(this.mArray[n3 << 1])) {
                            return n3;
                        }
                    }
                    for (int n4 = binarySearch - 1; n4 >= 0 && this.mHashes[n4] == n; --n4) {
                        if (o.equals(this.mArray[n4 << 1])) {
                            return n4;
                        }
                    }
                    return ~n3;
                }
            }
        }
        return n2;
    }
    
    public int indexOfKey(final Object o) {
        if (o == null) {
            return this.indexOfNull();
        }
        return this.indexOf(o, o.hashCode());
    }
    
    int indexOfNull() {
        final int mSize = this.mSize;
        int n;
        if (mSize == 0) {
            n = -1;
        }
        else {
            final int binarySearch = ContainerHelpers.binarySearch(this.mHashes, mSize, 0);
            if ((n = binarySearch) >= 0) {
                n = binarySearch;
                if (this.mArray[binarySearch << 1] != null) {
                    int n2;
                    for (n2 = binarySearch + 1; n2 < mSize && this.mHashes[n2] == 0; ++n2) {
                        if (this.mArray[n2 << 1] == null) {
                            return n2;
                        }
                    }
                    for (int n3 = binarySearch - 1; n3 >= 0 && this.mHashes[n3] == 0; --n3) {
                        if (this.mArray[n3 << 1] == null) {
                            return n3;
                        }
                    }
                    return ~n2;
                }
            }
        }
        return n;
    }
    
    int indexOfValue(final Object o) {
        final int n = this.mSize * 2;
        final Object[] mArray = this.mArray;
        if (o == null) {
            for (int i = 1; i < n; i += 2) {
                if (mArray[i] == null) {
                    return i >> 1;
                }
            }
        }
        else {
            for (int j = 1; j < n; j += 2) {
                if (o.equals(mArray[j])) {
                    return j >> 1;
                }
            }
        }
        return -1;
    }
    
    public boolean isEmpty() {
        return this.mSize <= 0;
    }
    
    public K keyAt(final int n) {
        return (K)this.mArray[n << 1];
    }
    
    public V put(final K k, final V v) {
        final int n = 8;
        int hashCode;
        int n2;
        if (k == null) {
            hashCode = 0;
            n2 = this.indexOfNull();
        }
        else {
            hashCode = k.hashCode();
            n2 = this.indexOf(k, hashCode);
        }
        if (n2 >= 0) {
            final int n3 = (n2 << 1) + 1;
            final Object o = this.mArray[n3];
            this.mArray[n3] = v;
            return (V)o;
        }
        final int n4 = ~n2;
        if (this.mSize >= this.mHashes.length) {
            int n5;
            if (this.mSize >= 8) {
                n5 = this.mSize + (this.mSize >> 1);
            }
            else {
                n5 = n;
                if (this.mSize < 4) {
                    n5 = 4;
                }
            }
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            this.allocArrays(n5);
            if (this.mHashes.length > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, mHashes.length);
                System.arraycopy(mArray, 0, this.mArray, 0, mArray.length);
            }
            freeArrays(mHashes, mArray, this.mSize);
        }
        if (n4 < this.mSize) {
            System.arraycopy(this.mHashes, n4, this.mHashes, n4 + 1, this.mSize - n4);
            System.arraycopy(this.mArray, n4 << 1, this.mArray, n4 + 1 << 1, this.mSize - n4 << 1);
        }
        this.mHashes[n4] = hashCode;
        this.mArray[n4 << 1] = k;
        this.mArray[(n4 << 1) + 1] = v;
        ++this.mSize;
        return null;
    }
    
    public void putAll(final SimpleArrayMap<? extends K, ? extends V> simpleArrayMap) {
        final int mSize = simpleArrayMap.mSize;
        this.ensureCapacity(this.mSize + mSize);
        if (this.mSize == 0) {
            if (mSize > 0) {
                System.arraycopy(simpleArrayMap.mHashes, 0, this.mHashes, 0, mSize);
                System.arraycopy(simpleArrayMap.mArray, 0, this.mArray, 0, mSize << 1);
                this.mSize = mSize;
            }
        }
        else {
            for (int i = 0; i < mSize; ++i) {
                this.put(simpleArrayMap.keyAt(i), simpleArrayMap.valueAt(i));
            }
        }
    }
    
    public V remove(final Object o) {
        final int indexOfKey = this.indexOfKey(o);
        if (indexOfKey >= 0) {
            return this.removeAt(indexOfKey);
        }
        return null;
    }
    
    public V removeAt(final int n) {
        int n2 = 8;
        final Object o = this.mArray[(n << 1) + 1];
        if (this.mSize <= 1) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
        }
        else {
            if (this.mHashes.length <= 8 || this.mSize >= this.mHashes.length / 3) {
                --this.mSize;
                if (n < this.mSize) {
                    System.arraycopy(this.mHashes, n + 1, this.mHashes, n, this.mSize - n);
                    System.arraycopy(this.mArray, n + 1 << 1, this.mArray, n << 1, this.mSize - n << 1);
                }
                this.mArray[this.mSize << 1] = null;
                this.mArray[(this.mSize << 1) + 1] = null;
                return (V)o;
            }
            if (this.mSize > 8) {
                n2 = this.mSize + (this.mSize >> 1);
            }
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            this.allocArrays(n2);
            --this.mSize;
            if (n > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, n);
                System.arraycopy(mArray, 0, this.mArray, 0, n << 1);
            }
            if (n < this.mSize) {
                System.arraycopy(mHashes, n + 1, this.mHashes, n, this.mSize - n);
                System.arraycopy(mArray, n + 1 << 1, this.mArray, n << 1, this.mSize - n << 1);
                return (V)o;
            }
        }
        return (V)o;
    }
    
    public V setValueAt(int n, final V v) {
        n = (n << 1) + 1;
        final Object o = this.mArray[n];
        this.mArray[n] = v;
        return (V)o;
    }
    
    public int size() {
        return this.mSize;
    }
    
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            final K key = this.keyAt(i);
            if (key != this) {
                sb.append(key);
            }
            else {
                sb.append("(this Map)");
            }
            sb.append('=');
            final V value = this.valueAt(i);
            if (value != this) {
                sb.append(value);
            }
            else {
                sb.append("(this Map)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    public V valueAt(final int n) {
        return (V)this.mArray[(n << 1) + 1];
    }
}
