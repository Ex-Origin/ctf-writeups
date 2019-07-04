// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.view.View$OnApplyWindowInsetsListener;
import android.view.View;
import android.view.WindowInsets;
import android.view.ViewGroup$MarginLayoutParams;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class DrawerLayoutCompatApi21
{
    private static final int[] THEME_ATTRS;
    
    static {
        THEME_ATTRS = new int[] { 16843828 };
    }
    
    public static void applyMarginInsets(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams, final Object o, final int n) {
        final WindowInsets windowInsets = (WindowInsets)o;
        WindowInsets windowInsets2;
        if (n == 3) {
            windowInsets2 = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
        }
        else {
            windowInsets2 = windowInsets;
            if (n == 5) {
                windowInsets2 = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
            }
        }
        viewGroup$MarginLayoutParams.leftMargin = windowInsets2.getSystemWindowInsetLeft();
        viewGroup$MarginLayoutParams.topMargin = windowInsets2.getSystemWindowInsetTop();
        viewGroup$MarginLayoutParams.rightMargin = windowInsets2.getSystemWindowInsetRight();
        viewGroup$MarginLayoutParams.bottomMargin = windowInsets2.getSystemWindowInsetBottom();
    }
    
    public static void configureApplyInsets(final View view) {
        if (view instanceof DrawerLayoutImpl) {
            view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new InsetsListener());
            view.setSystemUiVisibility(1280);
        }
    }
    
    public static void dispatchChildInsets(final View view, final Object o, final int n) {
        final WindowInsets windowInsets = (WindowInsets)o;
        WindowInsets windowInsets2;
        if (n == 3) {
            windowInsets2 = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
        }
        else {
            windowInsets2 = windowInsets;
            if (n == 5) {
                windowInsets2 = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
            }
        }
        view.dispatchApplyWindowInsets(windowInsets2);
    }
    
    public static Drawable getDefaultStatusBarBackground(Context obtainStyledAttributes) {
        obtainStyledAttributes = (Context)obtainStyledAttributes.obtainStyledAttributes(DrawerLayoutCompatApi21.THEME_ATTRS);
        try {
            return ((TypedArray)obtainStyledAttributes).getDrawable(0);
        }
        finally {
            ((TypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    public static int getTopInset(final Object o) {
        if (o != null) {
            return ((WindowInsets)o).getSystemWindowInsetTop();
        }
        return 0;
    }
    
    static class InsetsListener implements View$OnApplyWindowInsetsListener
    {
        public WindowInsets onApplyWindowInsets(final View view, final WindowInsets windowInsets) {
            ((DrawerLayoutImpl)view).setChildInsets(windowInsets, windowInsets.getSystemWindowInsetTop() > 0);
            return windowInsets.consumeSystemWindowInsets();
        }
    }
}
