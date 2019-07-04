// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.app.AppOpsManager;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.Context;

public final class AppOpsManagerCompat
{
    public static final int MODE_ALLOWED = 0;
    public static final int MODE_DEFAULT = 3;
    public static final int MODE_IGNORED = 1;
    
    public static int noteOp(@NonNull final Context context, @NonNull final String s, final int n, @NonNull final String s2) {
        if (Build$VERSION.SDK_INT >= 23) {
            return ((AppOpsManager)context.getSystemService((Class)AppOpsManager.class)).noteOp(s, n, s2);
        }
        return 1;
    }
    
    public static int noteProxyOp(@NonNull final Context context, @NonNull final String s, @NonNull final String s2) {
        if (Build$VERSION.SDK_INT >= 23) {
            return ((AppOpsManager)context.getSystemService((Class)AppOpsManager.class)).noteProxyOp(s, s2);
        }
        return 1;
    }
    
    public static String permissionToOp(@NonNull final String s) {
        if (Build$VERSION.SDK_INT >= 23) {
            return AppOpsManager.permissionToOp(s);
        }
        return null;
    }
}
