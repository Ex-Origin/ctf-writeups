// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.view.animation.Interpolator;
import android.content.Context;
import android.widget.OverScroller;

@Deprecated
public final class ScrollerCompat
{
    OverScroller mScroller;
    
    ScrollerCompat(final Context context, final Interpolator interpolator) {
        OverScroller mScroller;
        if (interpolator != null) {
            mScroller = new OverScroller(context, interpolator);
        }
        else {
            mScroller = new OverScroller(context);
        }
        this.mScroller = mScroller;
    }
    
    @Deprecated
    public static ScrollerCompat create(final Context context) {
        return create(context, null);
    }
    
    @Deprecated
    public static ScrollerCompat create(final Context context, final Interpolator interpolator) {
        return new ScrollerCompat(context, interpolator);
    }
    
    @Deprecated
    public void abortAnimation() {
        this.mScroller.abortAnimation();
    }
    
    @Deprecated
    public boolean computeScrollOffset() {
        return this.mScroller.computeScrollOffset();
    }
    
    @Deprecated
    public void fling(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        this.mScroller.fling(n, n2, n3, n4, n5, n6, n7, n8);
    }
    
    @Deprecated
    public void fling(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final int n9, final int n10) {
        this.mScroller.fling(n, n2, n3, n4, n5, n6, n7, n8, n9, n10);
    }
    
    @Deprecated
    public float getCurrVelocity() {
        return this.mScroller.getCurrVelocity();
    }
    
    @Deprecated
    public int getCurrX() {
        return this.mScroller.getCurrX();
    }
    
    @Deprecated
    public int getCurrY() {
        return this.mScroller.getCurrY();
    }
    
    @Deprecated
    public int getFinalX() {
        return this.mScroller.getFinalX();
    }
    
    @Deprecated
    public int getFinalY() {
        return this.mScroller.getFinalY();
    }
    
    @Deprecated
    public boolean isFinished() {
        return this.mScroller.isFinished();
    }
    
    @Deprecated
    public boolean isOverScrolled() {
        return this.mScroller.isOverScrolled();
    }
    
    @Deprecated
    public void notifyHorizontalEdgeReached(final int n, final int n2, final int n3) {
        this.mScroller.notifyHorizontalEdgeReached(n, n2, n3);
    }
    
    @Deprecated
    public void notifyVerticalEdgeReached(final int n, final int n2, final int n3) {
        this.mScroller.notifyVerticalEdgeReached(n, n2, n3);
    }
    
    @Deprecated
    public boolean springBack(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return this.mScroller.springBack(n, n2, n3, n4, n5, n6);
    }
    
    @Deprecated
    public void startScroll(final int n, final int n2, final int n3, final int n4) {
        this.mScroller.startScroll(n, n2, n3, n4);
    }
    
    @Deprecated
    public void startScroll(final int n, final int n2, final int n3, final int n4, final int n5) {
        this.mScroller.startScroll(n, n2, n3, n4, n5);
    }
}
