// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver;

final class Pools
{
    private static final boolean DEBUG = false;
    
    interface Pool<T>
    {
        T acquire();
        
        boolean release(final T p0);
        
        void releaseAll(final T[] p0, final int p1);
    }
    
    static class SimplePool<T> implements Pool<T>
    {
        private final Object[] mPool;
        private int mPoolSize;
        
        SimplePool(final int n) {
            if (n <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[n];
        }
        
        private boolean isInPool(final T t) {
            for (int i = 0; i < this.mPoolSize; ++i) {
                if (this.mPool[i] == t) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public T acquire() {
            if (this.mPoolSize > 0) {
                final int n = this.mPoolSize - 1;
                final Object o = this.mPool[n];
                this.mPool[n] = null;
                --this.mPoolSize;
                return (T)o;
            }
            return null;
        }
        
        @Override
        public boolean release(final T t) {
            if (this.mPoolSize < this.mPool.length) {
                this.mPool[this.mPoolSize] = t;
                ++this.mPoolSize;
                return true;
            }
            return false;
        }
        
        @Override
        public void releaseAll(final T[] array, int i) {
            int length = i;
            if (i > array.length) {
                length = array.length;
            }
            T t;
            for (i = 0; i < length; ++i) {
                t = array[i];
                if (this.mPoolSize < this.mPool.length) {
                    this.mPool[this.mPoolSize] = t;
                    ++this.mPoolSize;
                }
            }
        }
    }
}
