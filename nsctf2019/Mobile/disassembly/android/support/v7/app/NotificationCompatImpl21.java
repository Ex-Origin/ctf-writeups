// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.media.session.MediaSession$Token;
import android.app.Notification$MediaStyle;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class NotificationCompatImpl21
{
    public static void addMediaStyle(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final int[] showActionsInCompactView, final Object o) {
        final Notification$MediaStyle notification$MediaStyle = new Notification$MediaStyle(notificationBuilderWithBuilderAccessor.getBuilder());
        if (showActionsInCompactView != null) {
            notification$MediaStyle.setShowActionsInCompactView(showActionsInCompactView);
        }
        if (o != null) {
            notification$MediaStyle.setMediaSession((MediaSession$Token)o);
        }
    }
}
