// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.os.Build$VERSION;
import android.view.ScaleGestureDetector;

public final class ScaleGestureDetectorCompat
{
    public static boolean isQuickScaleEnabled(final ScaleGestureDetector scaleGestureDetector) {
        return Build$VERSION.SDK_INT >= 19 && scaleGestureDetector.isQuickScaleEnabled();
    }
    
    @Deprecated
    public static boolean isQuickScaleEnabled(final Object o) {
        return isQuickScaleEnabled((ScaleGestureDetector)o);
    }
    
    public static void setQuickScaleEnabled(final ScaleGestureDetector scaleGestureDetector, final boolean quickScaleEnabled) {
        if (Build$VERSION.SDK_INT >= 19) {
            scaleGestureDetector.setQuickScaleEnabled(quickScaleEnabled);
        }
    }
    
    @Deprecated
    public static void setQuickScaleEnabled(final Object o, final boolean b) {
        setQuickScaleEnabled((ScaleGestureDetector)o, b);
    }
}
