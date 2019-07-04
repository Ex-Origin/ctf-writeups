// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

public final class CircularIntArray
{
    private int mCapacityBitmask;
    private int[] mElements;
    private int mHead;
    private int mTail;
    
    public CircularIntArray() {
        this(8);
    }
    
    public CircularIntArray(int n) {
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
        this.mElements = new int[n];
    }
    
    private void doubleCapacity() {
        final int length = this.mElements.length;
        final int n = length - this.mHead;
        final int n2 = length << 1;
        if (n2 < 0) {
            throw new RuntimeException("Max array capacity exceeded");
        }
        final int[] mElements = new int[n2];
        System.arraycopy(this.mElements, this.mHead, mElements, 0, n);
        System.arraycopy(this.mElements, 0, mElements, n, this.mHead);
        this.mElements = mElements;
        this.mHead = 0;
        this.mTail = length;
        this.mCapacityBitmask = n2 - 1;
    }
    
    public void addFirst(final int n) {
        this.mHead = (this.mHead - 1 & this.mCapacityBitmask);
        this.mElements[this.mHead] = n;
        if (this.mHead == this.mTail) {
            this.doubleCapacity();
        }
    }
    
    public void addLast(final int n) {
        this.mElements[this.mTail] = n;
        this.mTail = (this.mTail + 1 & this.mCapacityBitmask);
        if (this.mTail == this.mHead) {
            this.doubleCapacity();
        }
    }
    
    public void clear() {
        this.mTail = this.mHead;
    }
    
    public int get(final int n) {
        if (n < 0 || n >= this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mElements[this.mHead + n & this.mCapacityBitmask];
    }
    
    public int getFirst() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mElements[this.mHead];
    }
    
    public int getLast() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mElements[this.mTail - 1 & this.mCapacityBitmask];
    }
    
    public boolean isEmpty() {
        return this.mHead == this.mTail;
    }
    
    public int popFirst() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        final int n = this.mElements[this.mHead];
        this.mHead = (this.mHead + 1 & this.mCapacityBitmask);
        return n;
    }
    
    public int popLast() {
        if (this.mHead == this.mTail) {
            throw new ArrayIndexOutOfBoundsException();
        }
        final int mTail = this.mTail - 1 & this.mCapacityBitmask;
        final int n = this.mElements[mTail];
        this.mTail = mTail;
        return n;
    }
    
    public void removeFromEnd(final int n) {
        if (n <= 0) {
            return;
        }
        if (n > this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.mTail = (this.mTail - n & this.mCapacityBitmask);
    }
    
    public void removeFromStart(final int n) {
        if (n <= 0) {
            return;
        }
        if (n > this.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.mHead = (this.mHead + n & this.mCapacityBitmask);
    }
    
    public int size() {
        return this.mTail - this.mHead & this.mCapacityBitmask;
    }
}
