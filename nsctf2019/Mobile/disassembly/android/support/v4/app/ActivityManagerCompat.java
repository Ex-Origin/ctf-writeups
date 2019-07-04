// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.app.ActivityManager;

public final class ActivityManagerCompat
{
    public static boolean isLowRamDevice(@NonNull final ActivityManager activityManager) {
        return Build$VERSION.SDK_INT >= 19 && activityManager.isLowRamDevice();
    }
}
