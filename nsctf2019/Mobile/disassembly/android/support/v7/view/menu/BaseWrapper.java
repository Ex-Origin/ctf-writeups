// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

class BaseWrapper<T>
{
    final T mWrappedObject;
    
    BaseWrapper(final T mWrappedObject) {
        if (mWrappedObject == null) {
            throw new IllegalArgumentException("Wrapped Object can not be null.");
        }
        this.mWrappedObject = mWrappedObject;
    }
    
    public T getWrappedObject() {
        return this.mWrappedObject;
    }
}
