// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.Gravity;
import android.os.Build$VERSION;
import android.graphics.Rect;

public final class GravityCompat
{
    public static final int END = 8388613;
    public static final int RELATIVE_HORIZONTAL_GRAVITY_MASK = 8388615;
    public static final int RELATIVE_LAYOUT_DIRECTION = 8388608;
    public static final int START = 8388611;
    
    public static void apply(final int n, final int n2, final int n3, final Rect rect, final int n4, final int n5, final Rect rect2, final int n6) {
        if (Build$VERSION.SDK_INT >= 17) {
            Gravity.apply(n, n2, n3, rect, n4, n5, rect2, n6);
            return;
        }
        Gravity.apply(n, n2, n3, rect, n4, n5, rect2);
    }
    
    public static void apply(final int n, final int n2, final int n3, final Rect rect, final Rect rect2, final int n4) {
        if (Build$VERSION.SDK_INT >= 17) {
            Gravity.apply(n, n2, n3, rect, rect2, n4);
            return;
        }
        Gravity.apply(n, n2, n3, rect, rect2);
    }
    
    public static void applyDisplay(final int n, final Rect rect, final Rect rect2, final int n2) {
        if (Build$VERSION.SDK_INT >= 17) {
            Gravity.applyDisplay(n, rect, rect2, n2);
            return;
        }
        Gravity.applyDisplay(n, rect, rect2);
    }
    
    public static int getAbsoluteGravity(final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 17) {
            return Gravity.getAbsoluteGravity(n, n2);
        }
        return 0xFF7FFFFF & n;
    }
}
