// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import java.lang.reflect.InvocationTargetException;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.graphics.Rect;
import android.view.View;
import android.os.Build$VERSION;
import java.lang.reflect.Method;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ViewUtils
{
    private static final String TAG = "ViewUtils";
    private static Method sComputeFitSystemWindowsMethod;
    
    static {
        if (Build$VERSION.SDK_INT < 18) {
            return;
        }
        try {
            ViewUtils.sComputeFitSystemWindowsMethod = View.class.getDeclaredMethod("computeFitSystemWindows", Rect.class, Rect.class);
            if (!ViewUtils.sComputeFitSystemWindowsMethod.isAccessible()) {
                ViewUtils.sComputeFitSystemWindowsMethod.setAccessible(true);
            }
        }
        catch (NoSuchMethodException ex) {
            Log.d("ViewUtils", "Could not find method computeFitSystemWindows. Oh well.");
        }
    }
    
    @Deprecated
    public static int combineMeasuredStates(final int n, final int n2) {
        return View.combineMeasuredStates(n, n2);
    }
    
    public static void computeFitSystemWindows(final View view, final Rect rect, final Rect rect2) {
        if (ViewUtils.sComputeFitSystemWindowsMethod == null) {
            return;
        }
        try {
            ViewUtils.sComputeFitSystemWindowsMethod.invoke(view, rect, rect2);
        }
        catch (Exception ex) {
            Log.d("ViewUtils", "Could not invoke computeFitSystemWindows", (Throwable)ex);
        }
    }
    
    public static boolean isLayoutRtl(final View view) {
        return ViewCompat.getLayoutDirection(view) == 1;
    }
    
    public static void makeOptionalFitsSystemWindows(final View view) {
        if (Build$VERSION.SDK_INT < 16) {
            return;
        }
        try {
            final Method method = view.getClass().getMethod("makeOptionalFitsSystemWindows", (Class<?>[])new Class[0]);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            method.invoke(view, new Object[0]);
        }
        catch (NoSuchMethodException ex3) {
            Log.d("ViewUtils", "Could not find method makeOptionalFitsSystemWindows. Oh well...");
        }
        catch (InvocationTargetException ex) {
            Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", (Throwable)ex);
        }
        catch (IllegalAccessException ex2) {
            Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", (Throwable)ex2);
        }
    }
}
