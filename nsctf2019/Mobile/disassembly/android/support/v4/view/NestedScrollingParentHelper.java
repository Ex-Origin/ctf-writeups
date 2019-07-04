// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.View;
import android.view.ViewGroup;

public class NestedScrollingParentHelper
{
    private int mNestedScrollAxes;
    private final ViewGroup mViewGroup;
    
    public NestedScrollingParentHelper(final ViewGroup mViewGroup) {
        this.mViewGroup = mViewGroup;
    }
    
    public int getNestedScrollAxes() {
        return this.mNestedScrollAxes;
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int mNestedScrollAxes) {
        this.mNestedScrollAxes = mNestedScrollAxes;
    }
    
    public void onStopNestedScroll(final View view) {
        this.mNestedScrollAxes = 0;
    }
}
