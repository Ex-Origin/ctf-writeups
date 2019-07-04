// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

public class LongSparseArray<E> implements Cloneable
{
    private static final Object DELETED;
    private boolean mGarbage;
    private long[] mKeys;
    private int mSize;
    private Object[] mValues;
    
    static {
        DELETED = new Object();
    }
    
    public LongSparseArray() {
        this(10);
    }
    
    public LongSparseArray(int idealLongArraySize) {
        this.mGarbage = false;
        if (idealLongArraySize == 0) {
            this.mKeys = ContainerHelpers.EMPTY_LONGS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        }
        else {
            idealLongArraySize = ContainerHelpers.idealLongArraySize(idealLongArraySize);
            this.mKeys = new long[idealLongArraySize];
            this.mValues = new Object[idealLongArraySize];
        }
        this.mSize = 0;
    }
    
    private void gc() {
        final int mSize = this.mSize;
        int mSize2 = 0;
        final long[] mKeys = this.mKeys;
        final Object[] mValues = this.mValues;
        int n;
        for (int i = 0; i < mSize; ++i, mSize2 = n) {
            final Object o = mValues[i];
            n = mSize2;
            if (o != LongSparseArray.DELETED) {
                if (i != mSize2) {
                    mKeys[mSize2] = mKeys[i];
                    mValues[mSize2] = o;
                    mValues[i] = null;
                }
                n = mSize2 + 1;
            }
        }
        this.mGarbage = false;
        this.mSize = mSize2;
    }
    
    public void append(final long n, final E e) {
        if (this.mSize != 0 && n <= this.mKeys[this.mSize - 1]) {
            this.put(n, e);
            return;
        }
        if (this.mGarbage && this.mSize >= this.mKeys.length) {
            this.gc();
        }
        final int mSize = this.mSize;
        if (mSize >= this.mKeys.length) {
            final int idealLongArraySize = ContainerHelpers.idealLongArraySize(mSize + 1);
            final long[] mKeys = new long[idealLongArraySize];
            final Object[] mValues = new Object[idealLongArraySize];
            System.arraycopy(this.mKeys, 0, mKeys, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, mValues, 0, this.mValues.length);
            this.mKeys = mKeys;
            this.mValues = mValues;
        }
        this.mKeys[mSize] = n;
        this.mValues[mSize] = e;
        this.mSize = mSize + 1;
    }
    
    public void clear() {
        final int mSize = this.mSize;
        final Object[] mValues = this.mValues;
        for (int i = 0; i < mSize; ++i) {
            mValues[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }
    
    public LongSparseArray<E> clone() {
        LongSparseArray<E> longSparseArray = null;
        try {
            final LongSparseArray longSparseArray2 = longSparseArray = (LongSparseArray)super.clone();
            longSparseArray2.mKeys = this.mKeys.clone();
            longSparseArray = longSparseArray2;
            longSparseArray2.mValues = this.mValues.clone();
            return longSparseArray2;
        }
        catch (CloneNotSupportedException ex) {
            return longSparseArray;
        }
    }
    
    public void delete(final long n) {
        final int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (binarySearch >= 0 && this.mValues[binarySearch] != LongSparseArray.DELETED) {
            this.mValues[binarySearch] = LongSparseArray.DELETED;
            this.mGarbage = true;
        }
    }
    
    public E get(final long n) {
        return this.get(n, null);
    }
    
    public E get(final long n, final E e) {
        final int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (binarySearch < 0 || this.mValues[binarySearch] == LongSparseArray.DELETED) {
            return e;
        }
        return (E)this.mValues[binarySearch];
    }
    
    public int indexOfKey(final long n) {
        if (this.mGarbage) {
            this.gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
    }
    
    public int indexOfValue(final E e) {
        if (this.mGarbage) {
            this.gc();
        }
        for (int i = 0; i < this.mSize; ++i) {
            if (this.mValues[i] == e) {
                return i;
            }
        }
        return -1;
    }
    
    public long keyAt(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mKeys[n];
    }
    
    public void put(final long n, final E e) {
        final int binarySearch = ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
        if (binarySearch >= 0) {
            this.mValues[binarySearch] = e;
            return;
        }
        final int n2 = ~binarySearch;
        if (n2 < this.mSize && this.mValues[n2] == LongSparseArray.DELETED) {
            this.mKeys[n2] = n;
            this.mValues[n2] = e;
            return;
        }
        int n3 = n2;
        if (this.mGarbage) {
            n3 = n2;
            if (this.mSize >= this.mKeys.length) {
                this.gc();
                n3 = ~ContainerHelpers.binarySearch(this.mKeys, this.mSize, n);
            }
        }
        if (this.mSize >= this.mKeys.length) {
            final int idealLongArraySize = ContainerHelpers.idealLongArraySize(this.mSize + 1);
            final long[] mKeys = new long[idealLongArraySize];
            final Object[] mValues = new Object[idealLongArraySize];
            System.arraycopy(this.mKeys, 0, mKeys, 0, this.mKeys.length);
            System.arraycopy(this.mValues, 0, mValues, 0, this.mValues.length);
            this.mKeys = mKeys;
            this.mValues = mValues;
        }
        if (this.mSize - n3 != 0) {
            System.arraycopy(this.mKeys, n3, this.mKeys, n3 + 1, this.mSize - n3);
            System.arraycopy(this.mValues, n3, this.mValues, n3 + 1, this.mSize - n3);
        }
        this.mKeys[n3] = n;
        this.mValues[n3] = e;
        ++this.mSize;
    }
    
    public void remove(final long n) {
        this.delete(n);
    }
    
    public void removeAt(final int n) {
        if (this.mValues[n] != LongSparseArray.DELETED) {
            this.mValues[n] = LongSparseArray.DELETED;
            this.mGarbage = true;
        }
    }
    
    public void setValueAt(final int n, final E e) {
        if (this.mGarbage) {
            this.gc();
        }
        this.mValues[n] = e;
    }
    
    public int size() {
        if (this.mGarbage) {
            this.gc();
        }
        return this.mSize;
    }
    
    @Override
    public String toString() {
        if (this.size() <= 0) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(this.mSize * 28);
        sb.append('{');
        for (int i = 0; i < this.mSize; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(this.keyAt(i));
            sb.append('=');
            final E value = this.valueAt(i);
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
    
    public E valueAt(final int n) {
        if (this.mGarbage) {
            this.gc();
        }
        return (E)this.mValues[n];
    }
}
