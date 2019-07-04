// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.View;

public interface NestedScrollingParent
{
    int getNestedScrollAxes();
    
    boolean onNestedFling(final View p0, final float p1, final float p2, final boolean p3);
    
    boolean onNestedPreFling(final View p0, final float p1, final float p2);
    
    void onNestedPreScroll(final View p0, final int p1, final int p2, final int[] p3);
    
    void onNestedScroll(final View p0, final int p1, final int p2, final int p3, final int p4);
    
    void onNestedScrollAccepted(final View p0, final View p1, final int p2);
    
    boolean onStartNestedScroll(final View p0, final View p1, final int p2);
    
    void onStopNestedScroll(final View p0);
}
