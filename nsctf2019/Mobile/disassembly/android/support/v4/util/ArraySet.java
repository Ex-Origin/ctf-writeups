// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

import java.lang.reflect.Array;
import android.support.annotation.RestrictTo;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

public final class ArraySet<E> implements Collection<E>, Set<E>
{
    private static final int BASE_SIZE = 4;
    private static final int CACHE_SIZE = 10;
    private static final boolean DEBUG = false;
    private static final int[] INT;
    private static final Object[] OBJECT;
    private static final String TAG = "ArraySet";
    static Object[] sBaseCache;
    static int sBaseCacheSize;
    static Object[] sTwiceBaseCache;
    static int sTwiceBaseCacheSize;
    Object[] mArray;
    MapCollections<E, E> mCollections;
    int[] mHashes;
    final boolean mIdentityHashCode;
    int mSize;
    
    static {
        INT = new int[0];
        OBJECT = new Object[0];
    }
    
    public ArraySet() {
        this(0, false);
    }
    
    public ArraySet(final int n) {
        this(n, false);
    }
    
    public ArraySet(final int n, final boolean mIdentityHashCode) {
        this.mIdentityHashCode = mIdentityHashCode;
        if (n == 0) {
            this.mHashes = ArraySet.INT;
            this.mArray = ArraySet.OBJECT;
        }
        else {
            this.allocArrays(n);
        }
        this.mSize = 0;
    }
    
    public ArraySet(final ArraySet<E> set) {
        this();
        if (set != null) {
            this.addAll((ArraySet<? extends E>)set);
        }
    }
    
    public ArraySet(final Collection<E> collection) {
        this();
        if (collection != null) {
            this.addAll((Collection<? extends E>)collection);
        }
    }
    
    private void allocArrays(final int n) {
        while (true) {
            Label_0094: {
                if (n != 8) {
                    break Label_0094;
                }
                synchronized (ArraySet.class) {
                    if (ArraySet.sTwiceBaseCache != null) {
                        final Object[] sTwiceBaseCache = ArraySet.sTwiceBaseCache;
                        this.mArray = sTwiceBaseCache;
                        ArraySet.sTwiceBaseCache = (Object[])sTwiceBaseCache[0];
                        this.mHashes = (int[])sTwiceBaseCache[1];
                        sTwiceBaseCache[0] = (sTwiceBaseCache[1] = null);
                        --ArraySet.sTwiceBaseCacheSize;
                        return;
                    }
                    // monitorexit(ArraySet.class)
                    this.mHashes = new int[n];
                    this.mArray = new Object[n];
                    return;
                }
            }
            if (n == 4) {
                synchronized (ArraySet.class) {
                    if (ArraySet.sBaseCache != null) {
                        final Object[] sBaseCache = ArraySet.sBaseCache;
                        this.mArray = sBaseCache;
                        ArraySet.sBaseCache = (Object[])sBaseCache[0];
                        this.mHashes = (int[])sBaseCache[1];
                        sBaseCache[0] = (sBaseCache[1] = null);
                        --ArraySet.sBaseCacheSize;
                        return;
                    }
                }
                // monitorexit(ArraySet.class)
                continue;
            }
            continue;
        }
    }
    
    private static void freeArrays(final int[] array, final Object[] array2, int i) {
    Label_0113:
        while (true) {
            Label_0035: {
                Block_1: {
                    if (array.length == 8) {
                        break Block_1;
                    }
                    break Label_0035;
                    while (i >= 2) {
                        array2[i] = null;
                        --i;
                    }
                    break Label_0035;
                }
                synchronized (ArraySet.class) {
                    if (ArraySet.sTwiceBaseCacheSize < 10) {
                        array2[0] = ArraySet.sTwiceBaseCache;
                        array2[1] = array;
                        --i;
                        continue Label_0113;
                    }
                    return;
                    ArraySet.sTwiceBaseCache = array2;
                    ++ArraySet.sTwiceBaseCacheSize;
                    return;
                }
            }
            if (array.length != 4) {
                return;
            }
            while (true) {
                while (true) {
                    Label_0130: {
                        synchronized (ArraySet.class) {
                            if (ArraySet.sBaseCacheSize < 10) {
                                array2[0] = ArraySet.sBaseCache;
                                array2[1] = array;
                                --i;
                                break Label_0130;
                            }
                            return;
                            ArraySet.sBaseCache = array2;
                            ++ArraySet.sBaseCacheSize;
                            return;
                        }
                        continue Label_0113;
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
    
    private MapCollections<E, E> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<E, E>() {
                @Override
                protected void colClear() {
                    ArraySet.this.clear();
                }
                
                @Override
                protected Object colGetEntry(final int n, final int n2) {
                    return ArraySet.this.mArray[n];
                }
                
                @Override
                protected Map<E, E> colGetMap() {
                    throw new UnsupportedOperationException("not a map");
                }
                
                @Override
                protected int colGetSize() {
                    return ArraySet.this.mSize;
                }
                
                @Override
                protected int colIndexOfKey(final Object o) {
                    return ArraySet.this.indexOf(o);
                }
                
                @Override
                protected int colIndexOfValue(final Object o) {
                    return ArraySet.this.indexOf(o);
                }
                
                @Override
                protected void colPut(final E e, final E e2) {
                    ArraySet.this.add(e);
                }
                
                @Override
                protected void colRemoveAt(final int n) {
                    ArraySet.this.removeAt(n);
                }
                
                @Override
                protected E colSetValue(final int n, final E e) {
                    throw new UnsupportedOperationException("not a map");
                }
            };
        }
        return this.mCollections;
    }
    
    private int indexOf(final Object o, final int n) {
        final int mSize = this.mSize;
        int n2;
        if (mSize == 0) {
            n2 = -1;
        }
        else {
            final int binarySearch = ContainerHelpers.binarySearch(this.mHashes, mSize, n);
            if ((n2 = binarySearch) >= 0) {
                n2 = binarySearch;
                if (!o.equals(this.mArray[binarySearch])) {
                    int n3;
                    for (n3 = binarySearch + 1; n3 < mSize && this.mHashes[n3] == n; ++n3) {
                        if (o.equals(this.mArray[n3])) {
                            return n3;
                        }
                    }
                    for (int n4 = binarySearch - 1; n4 >= 0 && this.mHashes[n4] == n; --n4) {
                        if (o.equals(this.mArray[n4])) {
                            return n4;
                        }
                    }
                    return ~n3;
                }
            }
        }
        return n2;
    }
    
    private int indexOfNull() {
        final int mSize = this.mSize;
        int n;
        if (mSize == 0) {
            n = -1;
        }
        else {
            final int binarySearch = ContainerHelpers.binarySearch(this.mHashes, mSize, 0);
            if ((n = binarySearch) >= 0) {
                n = binarySearch;
                if (this.mArray[binarySearch] != null) {
                    int n2;
                    for (n2 = binarySearch + 1; n2 < mSize && this.mHashes[n2] == 0; ++n2) {
                        if (this.mArray[n2] == null) {
                            return n2;
                        }
                    }
                    for (int n3 = binarySearch - 1; n3 >= 0 && this.mHashes[n3] == 0; --n3) {
                        if (this.mArray[n3] == null) {
                            return n3;
                        }
                    }
                    return ~n2;
                }
            }
        }
        return n;
    }
    
    @Override
    public boolean add(final E e) {
        final int n = 8;
        int n2;
        int indexOfNull;
        if (e == null) {
            n2 = 0;
            indexOfNull = this.indexOfNull();
        }
        else {
            int n3;
            if (this.mIdentityHashCode) {
                n3 = System.identityHashCode(e);
            }
            else {
                n3 = e.hashCode();
            }
            final int index = this.indexOf(e, n3);
            n2 = n3;
            indexOfNull = index;
        }
        if (indexOfNull >= 0) {
            return false;
        }
        final int n4 = ~indexOfNull;
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
            System.arraycopy(this.mArray, n4, this.mArray, n4 + 1, this.mSize - n4);
        }
        this.mHashes[n4] = n2;
        this.mArray[n4] = e;
        ++this.mSize;
        return true;
    }
    
    public void addAll(final ArraySet<? extends E> set) {
        final int mSize = set.mSize;
        this.ensureCapacity(this.mSize + mSize);
        if (this.mSize == 0) {
            if (mSize > 0) {
                System.arraycopy(set.mHashes, 0, this.mHashes, 0, mSize);
                System.arraycopy(set.mArray, 0, this.mArray, 0, mSize);
                this.mSize = mSize;
            }
        }
        else {
            for (int i = 0; i < mSize; ++i) {
                this.add(set.valueAt(i));
            }
        }
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        this.ensureCapacity(this.mSize + collection.size());
        boolean b = false;
        final Iterator<? extends E> iterator = collection.iterator();
        while (iterator.hasNext()) {
            b |= this.add(iterator.next());
        }
        return b;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void append(final E e) {
        final int mSize = this.mSize;
        int n;
        if (e == null) {
            n = 0;
        }
        else if (this.mIdentityHashCode) {
            n = System.identityHashCode(e);
        }
        else {
            n = e.hashCode();
        }
        if (mSize >= this.mHashes.length) {
            throw new IllegalStateException("Array is full");
        }
        if (mSize > 0 && this.mHashes[mSize - 1] > n) {
            this.add(e);
            return;
        }
        this.mSize = mSize + 1;
        this.mHashes[mSize] = n;
        this.mArray[mSize] = e;
    }
    
    @Override
    public void clear() {
        if (this.mSize != 0) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ArraySet.INT;
            this.mArray = ArraySet.OBJECT;
            this.mSize = 0;
        }
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.indexOf(o) >= 0;
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!this.contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    public void ensureCapacity(final int n) {
        if (this.mHashes.length < n) {
            final int[] mHashes = this.mHashes;
            final Object[] mArray = this.mArray;
            this.allocArrays(n);
            if (this.mSize > 0) {
                System.arraycopy(mHashes, 0, this.mHashes, 0, this.mSize);
                System.arraycopy(mArray, 0, this.mArray, 0, this.mSize);
            }
            freeArrays(mHashes, mArray, this.mSize);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o instanceof Set) {
                final Set set = (Set)o;
                if (this.size() != set.size()) {
                    return false;
                }
                int i = 0;
                try {
                    while (i < this.mSize) {
                        if (!set.contains(this.valueAt(i))) {
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
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int[] mHashes = this.mHashes;
        int n = 0;
        for (int i = 0; i < this.mSize; ++i) {
            n += mHashes[i];
        }
        return n;
    }
    
    public int indexOf(final Object o) {
        if (o == null) {
            return this.indexOfNull();
        }
        int n;
        if (this.mIdentityHashCode) {
            n = System.identityHashCode(o);
        }
        else {
            n = o.hashCode();
        }
        return this.indexOf(o, n);
    }
    
    @Override
    public boolean isEmpty() {
        return this.mSize <= 0;
    }
    
    @Override
    public Iterator<E> iterator() {
        return this.getCollection().getKeySet().iterator();
    }
    
    @Override
    public boolean remove(final Object o) {
        final int index = this.indexOf(o);
        if (index >= 0) {
            this.removeAt(index);
            return true;
        }
        return false;
    }
    
    public boolean removeAll(final ArraySet<? extends E> set) {
        final int mSize = set.mSize;
        final int mSize2 = this.mSize;
        for (int i = 0; i < mSize; ++i) {
            this.remove(set.valueAt(i));
        }
        return mSize2 != this.mSize;
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        boolean b = false;
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            b |= this.remove(iterator.next());
        }
        return b;
    }
    
    public E removeAt(final int n) {
        int n2 = 8;
        final Object o = this.mArray[n];
        if (this.mSize <= 1) {
            freeArrays(this.mHashes, this.mArray, this.mSize);
            this.mHashes = ArraySet.INT;
            this.mArray = ArraySet.OBJECT;
            this.mSize = 0;
        }
        else {
            if (this.mHashes.length <= 8 || this.mSize >= this.mHashes.length / 3) {
                --this.mSize;
                if (n < this.mSize) {
                    System.arraycopy(this.mHashes, n + 1, this.mHashes, n, this.mSize - n);
                    System.arraycopy(this.mArray, n + 1, this.mArray, n, this.mSize - n);
                }
                this.mArray[this.mSize] = null;
                return (E)o;
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
                System.arraycopy(mArray, 0, this.mArray, 0, n);
            }
            if (n < this.mSize) {
                System.arraycopy(mHashes, n + 1, this.mHashes, n, this.mSize - n);
                System.arraycopy(mArray, n + 1, this.mArray, n, this.mSize - n);
                return (E)o;
            }
        }
        return (E)o;
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        boolean b = false;
        for (int i = this.mSize - 1; i >= 0; --i) {
            if (!collection.contains(this.mArray[i])) {
                this.removeAt(i);
                b = true;
            }
        }
        return b;
    }
    
    @Override
    public int size() {
        return this.mSize;
    }
    
    @Override
    public Object[] toArray() {
        final Object[] array = new Object[this.mSize];
        System.arraycopy(this.mArray, 0, array, 0, this.mSize);
        return array;
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        Object[] array2 = array;
        if (array.length < this.mSize) {
            array2 = (Object[])Array.newInstance(array.getClass().getComponentType(), this.mSize);
        }
        System.arraycopy(this.mArray, 0, array2, 0, this.mSize);
        if (array2.length > this.mSize) {
            array2[this.mSize] = null;
        }
        return (T[])array2;
    }
    
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(this.mSize * 14);
        sb.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            final E value = this.valueAt(i);
            if (value != this) {
                sb.append(value);
            }
            else {
                sb.append("(this Set)");
            }
        }
        sb.append('}');
        return sb.toString();
    }
    
    public E valueAt(final int n) {
        return (E)this.mArray[n];
    }
}
