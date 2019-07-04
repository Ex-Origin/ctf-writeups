// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.support.v4.app.AppOpsManagerCompat;
import android.os.Process;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.content.Context;

public final class PermissionChecker
{
    public static final int PERMISSION_DENIED = -1;
    public static final int PERMISSION_DENIED_APP_OP = -2;
    public static final int PERMISSION_GRANTED = 0;
    
    public static int checkCallingOrSelfPermission(@NonNull final Context context, @NonNull final String s) {
        String packageName;
        if (Binder.getCallingPid() == Process.myPid()) {
            packageName = context.getPackageName();
        }
        else {
            packageName = null;
        }
        return checkPermission(context, s, Binder.getCallingPid(), Binder.getCallingUid(), packageName);
    }
    
    public static int checkCallingPermission(@NonNull final Context context, @NonNull final String s, final String s2) {
        if (Binder.getCallingPid() == Process.myPid()) {
            return -1;
        }
        return checkPermission(context, s, Binder.getCallingPid(), Binder.getCallingUid(), s2);
    }
    
    public static int checkPermission(@NonNull final Context context, @NonNull String s, final int n, final int n2, final String s2) {
        if (context.checkPermission(s, n, n2) != -1) {
            final String permissionToOp = AppOpsManagerCompat.permissionToOp(s);
            if (permissionToOp == null) {
                return 0;
            }
            if ((s = s2) == null) {
                final String[] packagesForUid = context.getPackageManager().getPackagesForUid(n2);
                if (packagesForUid == null || packagesForUid.length <= 0) {
                    return -1;
                }
                s = packagesForUid[0];
            }
            if (AppOpsManagerCompat.noteProxyOp(context, permissionToOp, s) != 0) {
                return -2;
            }
            return 0;
        }
        return -1;
    }
    
    public static int checkSelfPermission(@NonNull final Context context, @NonNull final String s) {
        return checkPermission(context, s, Process.myPid(), Process.myUid(), context.getPackageName());
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface PermissionResult {
    }
}
