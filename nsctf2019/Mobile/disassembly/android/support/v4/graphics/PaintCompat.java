// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics;

import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.graphics.Paint;

public final class PaintCompat
{
    public static boolean hasGlyph(@NonNull final Paint paint, @NonNull final String s) {
        if (Build$VERSION.SDK_INT >= 23) {
            return paint.hasGlyph(s);
        }
        return PaintCompatApi14.hasGlyph(paint, s);
    }
}
