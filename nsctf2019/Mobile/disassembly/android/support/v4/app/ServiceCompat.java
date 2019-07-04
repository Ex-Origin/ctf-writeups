// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.os.Build$VERSION;
import android.app.Service;

public final class ServiceCompat
{
    public static final int START_STICKY = 1;
    public static final int STOP_FOREGROUND_DETACH = 2;
    public static final int STOP_FOREGROUND_REMOVE = 1;
    
    public static void stopForeground(final Service service, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            service.stopForeground(n);
            return;
        }
        service.stopForeground((n & 0x1) != 0x0);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface StopForegroundFlags {
    }
}
