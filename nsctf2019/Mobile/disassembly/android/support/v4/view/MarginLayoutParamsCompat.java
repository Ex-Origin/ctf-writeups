// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.os.Build$VERSION;
import android.view.ViewGroup$MarginLayoutParams;

public final class MarginLayoutParamsCompat
{
    public static int getLayoutDirection(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
        int layoutDirection;
        if (Build$VERSION.SDK_INT >= 17) {
            layoutDirection = viewGroup$MarginLayoutParams.getLayoutDirection();
        }
        else {
            layoutDirection = 0;
        }
        int n = layoutDirection;
        if (layoutDirection != 0 && (n = layoutDirection) != 1) {
            n = 0;
        }
        return n;
    }
    
    public static int getMarginEnd(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
        if (Build$VERSION.SDK_INT >= 17) {
            return viewGroup$MarginLayoutParams.getMarginEnd();
        }
        return viewGroup$MarginLayoutParams.rightMargin;
    }
    
    public static int getMarginStart(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
        if (Build$VERSION.SDK_INT >= 17) {
            return viewGroup$MarginLayoutParams.getMarginStart();
        }
        return viewGroup$MarginLayoutParams.leftMargin;
    }
    
    public static boolean isMarginRelative(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
        return Build$VERSION.SDK_INT >= 17 && viewGroup$MarginLayoutParams.isMarginRelative();
    }
    
    public static void resolveLayoutDirection(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams, final int n) {
        if (Build$VERSION.SDK_INT >= 17) {
            viewGroup$MarginLayoutParams.resolveLayoutDirection(n);
        }
    }
    
    public static void setLayoutDirection(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams, final int layoutDirection) {
        if (Build$VERSION.SDK_INT >= 17) {
            viewGroup$MarginLayoutParams.setLayoutDirection(layoutDirection);
        }
    }
    
    public static void setMarginEnd(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams, final int n) {
        if (Build$VERSION.SDK_INT >= 17) {
            viewGroup$MarginLayoutParams.setMarginEnd(n);
            return;
        }
        viewGroup$MarginLayoutParams.rightMargin = n;
    }
    
    public static void setMarginStart(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams, final int n) {
        if (Build$VERSION.SDK_INT >= 17) {
            viewGroup$MarginLayoutParams.setMarginStart(n);
            return;
        }
        viewGroup$MarginLayoutParams.leftMargin = n;
    }
}
