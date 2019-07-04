// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.app.Notification$DecoratedMediaCustomViewStyle;
import android.app.Notification$Style;
import android.app.Notification$DecoratedCustomViewStyle;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
class NotificationCompatImpl24
{
    public static void addDecoratedCustomViewStyle(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
        notificationBuilderWithBuilderAccessor.getBuilder().setStyle((Notification$Style)new Notification$DecoratedCustomViewStyle());
    }
    
    public static void addDecoratedMediaCustomViewStyle(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
        notificationBuilderWithBuilderAccessor.getBuilder().setStyle((Notification$Style)new Notification$DecoratedMediaCustomViewStyle());
    }
}
