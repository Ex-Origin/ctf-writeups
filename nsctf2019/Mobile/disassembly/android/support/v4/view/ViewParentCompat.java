// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.util.Log;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import android.view.ViewParent;
import android.os.Build$VERSION;

public final class ViewParentCompat
{
    static final ViewParentCompatBaseImpl IMPL;
    private static final String TAG = "ViewParentCompat";
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (ViewParentCompatBaseImpl)new ViewParentCompatApi21Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (ViewParentCompatBaseImpl)new ViewParentCompatApi19Impl();
            return;
        }
        IMPL = new ViewParentCompatBaseImpl();
    }
    
    public static void notifySubtreeAccessibilityStateChanged(final ViewParent viewParent, final View view, final View view2, final int n) {
        ViewParentCompat.IMPL.notifySubtreeAccessibilityStateChanged(viewParent, view, view2, n);
    }
    
    public static boolean onNestedFling(final ViewParent viewParent, final View view, final float n, final float n2, final boolean b) {
        return ViewParentCompat.IMPL.onNestedFling(viewParent, view, n, n2, b);
    }
    
    public static boolean onNestedPreFling(final ViewParent viewParent, final View view, final float n, final float n2) {
        return ViewParentCompat.IMPL.onNestedPreFling(viewParent, view, n, n2);
    }
    
    public static void onNestedPreScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int[] array) {
        ViewParentCompat.IMPL.onNestedPreScroll(viewParent, view, n, n2, array);
    }
    
    public static void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4) {
        ViewParentCompat.IMPL.onNestedScroll(viewParent, view, n, n2, n3, n4);
    }
    
    public static void onNestedScrollAccepted(final ViewParent viewParent, final View view, final View view2, final int n) {
        ViewParentCompat.IMPL.onNestedScrollAccepted(viewParent, view, view2, n);
    }
    
    public static boolean onStartNestedScroll(final ViewParent viewParent, final View view, final View view2, final int n) {
        return ViewParentCompat.IMPL.onStartNestedScroll(viewParent, view, view2, n);
    }
    
    public static void onStopNestedScroll(final ViewParent viewParent, final View view) {
        ViewParentCompat.IMPL.onStopNestedScroll(viewParent, view);
    }
    
    @Deprecated
    public static boolean requestSendAccessibilityEvent(final ViewParent viewParent, final View view, final AccessibilityEvent accessibilityEvent) {
        return viewParent.requestSendAccessibilityEvent(view, accessibilityEvent);
    }
    
    @RequiresApi(19)
    static class ViewParentCompatApi19Impl extends ViewParentCompatBaseImpl
    {
        @Override
        public void notifySubtreeAccessibilityStateChanged(final ViewParent viewParent, final View view, final View view2, final int n) {
            viewParent.notifySubtreeAccessibilityStateChanged(view, view2, n);
        }
    }
    
    @RequiresApi(21)
    static class ViewParentCompatApi21Impl extends ViewParentCompatApi19Impl
    {
        @Override
        public boolean onNestedFling(final ViewParent viewParent, final View view, final float n, final float n2, final boolean b) {
            try {
                return viewParent.onNestedFling(view, n, n2, b);
            }
            catch (AbstractMethodError abstractMethodError) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface " + "method onNestedFling", (Throwable)abstractMethodError);
                return false;
            }
        }
        
        @Override
        public boolean onNestedPreFling(final ViewParent viewParent, final View view, final float n, final float n2) {
            try {
                return viewParent.onNestedPreFling(view, n, n2);
            }
            catch (AbstractMethodError abstractMethodError) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface " + "method onNestedPreFling", (Throwable)abstractMethodError);
                return false;
            }
        }
        
        @Override
        public void onNestedPreScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int[] array) {
            try {
                viewParent.onNestedPreScroll(view, n, n2, array);
            }
            catch (AbstractMethodError abstractMethodError) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface " + "method onNestedPreScroll", (Throwable)abstractMethodError);
            }
        }
        
        @Override
        public void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4) {
            try {
                viewParent.onNestedScroll(view, n, n2, n3, n4);
            }
            catch (AbstractMethodError abstractMethodError) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface " + "method onNestedScroll", (Throwable)abstractMethodError);
            }
        }
        
        @Override
        public void onNestedScrollAccepted(final ViewParent viewParent, final View view, final View view2, final int n) {
            try {
                viewParent.onNestedScrollAccepted(view, view2, n);
            }
            catch (AbstractMethodError abstractMethodError) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface " + "method onNestedScrollAccepted", (Throwable)abstractMethodError);
            }
        }
        
        @Override
        public boolean onStartNestedScroll(final ViewParent viewParent, final View view, final View view2, final int n) {
            try {
                return viewParent.onStartNestedScroll(view, view2, n);
            }
            catch (AbstractMethodError abstractMethodError) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface " + "method onStartNestedScroll", (Throwable)abstractMethodError);
                return false;
            }
        }
        
        @Override
        public void onStopNestedScroll(final ViewParent viewParent, final View view) {
            try {
                viewParent.onStopNestedScroll(view);
            }
            catch (AbstractMethodError abstractMethodError) {
                Log.e("ViewParentCompat", "ViewParent " + viewParent + " does not implement interface " + "method onStopNestedScroll", (Throwable)abstractMethodError);
            }
        }
    }
    
    static class ViewParentCompatBaseImpl
    {
        public void notifySubtreeAccessibilityStateChanged(final ViewParent viewParent, final View view, final View view2, final int n) {
        }
        
        public boolean onNestedFling(final ViewParent viewParent, final View view, final float n, final float n2, final boolean b) {
            return viewParent instanceof NestedScrollingParent && ((NestedScrollingParent)viewParent).onNestedFling(view, n, n2, b);
        }
        
        public boolean onNestedPreFling(final ViewParent viewParent, final View view, final float n, final float n2) {
            return viewParent instanceof NestedScrollingParent && ((NestedScrollingParent)viewParent).onNestedPreFling(view, n, n2);
        }
        
        public void onNestedPreScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int[] array) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedPreScroll(view, n, n2, array);
            }
        }
        
        public void onNestedScroll(final ViewParent viewParent, final View view, final int n, final int n2, final int n3, final int n4) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScroll(view, n, n2, n3, n4);
            }
        }
        
        public void onNestedScrollAccepted(final ViewParent viewParent, final View view, final View view2, final int n) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScrollAccepted(view, view2, n);
            }
        }
        
        public boolean onStartNestedScroll(final ViewParent viewParent, final View view, final View view2, final int n) {
            return viewParent instanceof NestedScrollingParent && ((NestedScrollingParent)viewParent).onStartNestedScroll(view, view2, n);
        }
        
        public void onStopNestedScroll(final ViewParent viewParent, final View view) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onStopNestedScroll(view);
            }
        }
    }
}
