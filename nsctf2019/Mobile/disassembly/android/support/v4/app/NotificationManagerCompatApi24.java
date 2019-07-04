// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.app.NotificationManager;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
class NotificationManagerCompatApi24
{
    public static boolean areNotificationsEnabled(final NotificationManager notificationManager) {
        return notificationManager.areNotificationsEnabled();
    }
    
    public static int getImportance(final NotificationManager notificationManager) {
        return notificationManager.getImportance();
    }
}
