// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

public final class CircularArray<E>
{
    private int mCapacityBitmask;
    private E[] mElements;
    private int mHead;
    private int mTail;
    
    public CircularArray() {
        this(8);
    }
    
    public CircularArray(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("capacity must be >= 1");
        }
        if (n > 1073741824) {
            throw new IllegalArgumentException("capacity must be <= 2^30");
        }
        if (Integer.bitCount(n) != 1) {
            n = Integer.highestOneBit(n - 1) << 1;
        }
        this.mCapacityBitmask = n - 1;
        this.mElements = new Object[n];
    }
    
    private void doubleCapacity() {
        final int length = this.mElements.length;
        final int n = length - this.mHead;
        final int n2 = length << 1;
        if (n2 < 0) {
            throw new RuntimeException("Max array capacity exceeded");
        }
        final Object[] array = new Object[n2];
        System.arraycopy(this.mElements, this.mHead, array, 0, n);
        System.arraycopy(this.mElements, 0, array, n, this.mHead);
        this.mElements = array;
        this.mHead = 0;
        this.mTail = length;
        this.mCapacityBitmask = n2 - 1;
    }
    
    public void addFirst(final E e) {
        this.mHead = (this.mHead - 1 & this.mCapacityBitmask);
        this.mElements[this.mHead] = e;
        if (this.mHead == this.mTail) {
            this.doubleCapacity();
        }
    }
    
    public void addLast(final E e) {
        this.mElements[this.mTail] = e;
        this.mTail = (this.mTail + 1 & this.mCapacityBitmask);
        if (this.mTail == this.mHead) {
            this.doubleCapacity();
        }
    }
    
    public void clear() {
        this.removeFromStart(this.size());
    }
    
    public E get(final int n) {
        if (n < 0 || n >= this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (E)this.mElements[this.mHead + n & this.mCapacityBitmask];
    }
    
    public E getFirst() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (E)this.mElements[this.mHead];
    }
    
    public E getLast() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return (E)this.mElements[this.mTail - 1 & this.mCapacityBitmask];
    }
    
    public boolean isEmpty() {
        return this.mHead == this.mTail;
    }
    
    public E popFirst() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        final Object o = this.mElements[this.mHead];
        this.mElements[this.mHead] = null;
        this.mHead = (this.mHead + 1 & this.mCapacityBitmask);
        return (E)o;
    }
    
    public E popLast() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        final int mTail = this.mTail - 1 & this.mCapacityBitmask;
        final Object o = this.mElements[mTail];
        this.mElements[mTail] = null;
        this.mTail = mTail;
        return (E)o;
    }
    
    public void removeFromEnd(int i) {
        if (i > 0) {
            if (i > this.size()) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int n = 0;
            if (i < this.mTail) {
                n = this.mTail - i;
            }
            for (int j = n; j < this.mTail; ++j) {
                this.mElements[j] = null;
            }
            final int n2 = this.mTail - n;
            i -= n2;
            this.mTail -= n2;
            if (i > 0) {
                this.mTail = this.mElements.length;
                int mTail;
                for (mTail = (i = this.mTail - i); i < this.mTail; ++i) {
                    this.mElements[i] = null;
                }
                this.mTail = mTail;
            }
        }
    }
    
    public void removeFromStart(int i) {
        if (i > 0) {
            if (i > this.size()) {
                throw new ArrayIndexOutOfBoundsException();
            }
            int length;
            if (i < (length = this.mElements.length) - this.mHead) {
                length = this.mHead + i;
            }
            for (int j = this.mHead; j < length; ++j) {
                this.mElements[j] = null;
            }
            final int n = length - this.mHead;
            final int mHead = i - n;
            this.mHead = (this.mHead + n & this.mCapacityBitmask);
            if (mHead > 0) {
                for (i = 0; i < mHead; ++i) {
                    this.mElements[i] = null;
                }
                this.mHead = mHead;
            }
        }
    }
    
    public int size() {
        return this.mTail - this.mHead & this.mCapacityBitmask;
    }
}
