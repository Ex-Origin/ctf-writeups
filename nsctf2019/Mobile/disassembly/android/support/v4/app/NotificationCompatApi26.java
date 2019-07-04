// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.util.Iterator;
import android.os.Bundle;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.app.PendingIntent;
import android.widget.RemoteViews;
import android.content.Context;
import android.app.Notification$Builder;
import android.app.Notification;
import android.support.annotation.RequiresApi;

@RequiresApi(26)
class NotificationCompatApi26
{
    public static int getBadgeIcon(final Notification notification) {
        return notification.getBadgeIconType();
    }
    
    public static String getChannel(final Notification notification) {
        return notification.getChannel();
    }
    
    public static String getShortcutId(final Notification notification) {
        return notification.getShortcutId();
    }
    
    public static long getTimeout(final Notification notification) {
        return notification.getTimeout();
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder mB;
        
        Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean showWhen, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean localOnly, final String category, final ArrayList<String> list, final Bundle extras, final int color, final int visibility, final Notification publicVersion, final String group, final boolean groupSummary, final String sortKey, final CharSequence[] remoteInputHistory, final RemoteViews customContentView, final RemoteViews customBigContentView, final RemoteViews customHeadsUpContentView, final String channel, final int badgeIconType, final String shortcutId, final long timeout, final boolean colorized, final boolean b2) {
            this.mB = new Notification$Builder(context, channel).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b).setLocalOnly(localOnly).setExtras(extras).setGroup(group).setGroupSummary(groupSummary).setSortKey(sortKey).setCategory(category).setColor(color).setVisibility(visibility).setPublicVersion(publicVersion).setRemoteInputHistory(remoteInputHistory).setChannel(channel).setBadgeIconType(badgeIconType).setShortcutId(shortcutId).setTimeout(timeout);
            if (b2) {
                this.mB.setColorized(colorized);
            }
            if (customContentView != null) {
                this.mB.setCustomContentView(customContentView);
            }
            if (customBigContentView != null) {
                this.mB.setCustomBigContentView(customBigContentView);
            }
            if (customHeadsUpContentView != null) {
                this.mB.setCustomHeadsUpContentView(customHeadsUpContentView);
            }
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.mB.addPerson((String)iterator.next());
            }
        }
        
        @Override
        public void addAction(final NotificationCompatBase.Action action) {
            NotificationCompatApi24.addAction(this.mB, action);
        }
        
        @Override
        public Notification build() {
            return this.mB.build();
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.mB;
        }
    }
}
