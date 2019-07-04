// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.content.pm.ApplicationInfo;
import java.lang.reflect.InvocationTargetException;
import android.app.AppOpsManager;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class NotificationManagerCompatKitKat
{
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    
    public static boolean areNotificationsEnabled(final Context context) {
        final AppOpsManager appOpsManager = (AppOpsManager)context.getSystemService("appops");
        final ApplicationInfo applicationInfo = context.getApplicationInfo();
        final String packageName = context.getApplicationContext().getPackageName();
        final int uid = applicationInfo.uid;
        try {
            final Class<?> forName = Class.forName(AppOpsManager.class.getName());
            return (int)forName.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(appOpsManager, (int)forName.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class), uid, packageName) == 0;
        }
        catch (ClassNotFoundException ex) {}
        catch (RuntimeException ex2) {
            goto Label_0122;
        }
        catch (NoSuchFieldException ex3) {
            goto Label_0122;
        }
        catch (IllegalAccessException ex4) {
            goto Label_0122;
        }
        catch (NoSuchMethodException ex5) {
            goto Label_0122;
        }
        catch (InvocationTargetException ex6) {
            goto Label_0122;
        }
    }
}
