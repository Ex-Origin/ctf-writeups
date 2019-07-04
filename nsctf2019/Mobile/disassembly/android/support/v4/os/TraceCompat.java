// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import android.os.Trace;
import android.os.Build$VERSION;

public final class TraceCompat
{
    public static void beginSection(final String s) {
        if (Build$VERSION.SDK_INT >= 18) {
            Trace.beginSection(s);
        }
    }
    
    public static void endSection() {
        if (Build$VERSION.SDK_INT >= 18) {
            Trace.endSection();
        }
    }
}
