// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build$VERSION;

public final class ViewGroupCompat
{
    static final ViewGroupCompatBaseImpl IMPL;
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (ViewGroupCompatBaseImpl)new ViewGroupCompatApi21Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            IMPL = (ViewGroupCompatBaseImpl)new ViewGroupCompatApi18Impl();
            return;
        }
        IMPL = new ViewGroupCompatBaseImpl();
    }
    
    public static int getLayoutMode(final ViewGroup viewGroup) {
        return ViewGroupCompat.IMPL.getLayoutMode(viewGroup);
    }
    
    public static int getNestedScrollAxes(final ViewGroup viewGroup) {
        return ViewGroupCompat.IMPL.getNestedScrollAxes(viewGroup);
    }
    
    public static boolean isTransitionGroup(final ViewGroup viewGroup) {
        return ViewGroupCompat.IMPL.isTransitionGroup(viewGroup);
    }
    
    @Deprecated
    public static boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
        return viewGroup.onRequestSendAccessibilityEvent(view, accessibilityEvent);
    }
    
    public static void setLayoutMode(final ViewGroup viewGroup, final int n) {
        ViewGroupCompat.IMPL.setLayoutMode(viewGroup, n);
    }
    
    @Deprecated
    public static void setMotionEventSplittingEnabled(final ViewGroup viewGroup, final boolean motionEventSplittingEnabled) {
        viewGroup.setMotionEventSplittingEnabled(motionEventSplittingEnabled);
    }
    
    public static void setTransitionGroup(final ViewGroup viewGroup, final boolean b) {
        ViewGroupCompat.IMPL.setTransitionGroup(viewGroup, b);
    }
    
    @RequiresApi(18)
    static class ViewGroupCompatApi18Impl extends ViewGroupCompatBaseImpl
    {
        @Override
        public int getLayoutMode(final ViewGroup viewGroup) {
            return viewGroup.getLayoutMode();
        }
        
        @Override
        public void setLayoutMode(final ViewGroup viewGroup, final int layoutMode) {
            viewGroup.setLayoutMode(layoutMode);
        }
    }
    
    @RequiresApi(21)
    static class ViewGroupCompatApi21Impl extends ViewGroupCompatApi18Impl
    {
        @Override
        public int getNestedScrollAxes(final ViewGroup viewGroup) {
            return viewGroup.getNestedScrollAxes();
        }
        
        @Override
        public boolean isTransitionGroup(final ViewGroup viewGroup) {
            return viewGroup.isTransitionGroup();
        }
        
        @Override
        public void setTransitionGroup(final ViewGroup viewGroup, final boolean transitionGroup) {
            viewGroup.setTransitionGroup(transitionGroup);
        }
    }
    
    static class ViewGroupCompatBaseImpl
    {
        public int getLayoutMode(final ViewGroup viewGroup) {
            return 0;
        }
        
        public int getNestedScrollAxes(final ViewGroup viewGroup) {
            if (viewGroup instanceof NestedScrollingParent) {
                return ((NestedScrollingParent)viewGroup).getNestedScrollAxes();
            }
            return 0;
        }
        
        public boolean isTransitionGroup(final ViewGroup viewGroup) {
            return false;
        }
        
        public void setLayoutMode(final ViewGroup viewGroup, final int n) {
        }
        
        public void setTransitionGroup(final ViewGroup viewGroup, final boolean b) {
        }
    }
}
