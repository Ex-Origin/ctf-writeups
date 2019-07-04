// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

public interface NestedScrollingChild
{
    boolean dispatchNestedFling(final float p0, final float p1, final boolean p2);
    
    boolean dispatchNestedPreFling(final float p0, final float p1);
    
    boolean dispatchNestedPreScroll(final int p0, final int p1, final int[] p2, final int[] p3);
    
    boolean dispatchNestedScroll(final int p0, final int p1, final int p2, final int p3, final int[] p4);
    
    boolean hasNestedScrollingParent();
    
    boolean isNestedScrollingEnabled();
    
    void setNestedScrollingEnabled(final boolean p0);
    
    boolean startNestedScroll(final int p0);
    
    void stopNestedScroll();
}
