// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;
import java.util.List;
import android.app.Notification$Builder;
import android.util.SparseArray;
import android.app.Notification$Action;
import android.os.Bundle;
import android.app.Notification;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class NotificationCompatKitKat
{
    public static NotificationCompatBase.Action getAction(final Notification notification, final int n, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        final Notification$Action notification$Action = notification.actions[n];
        final Bundle bundle = null;
        final SparseArray sparseParcelableArray = notification.extras.getSparseParcelableArray("android.support.actionExtras");
        Bundle bundle2 = bundle;
        if (sparseParcelableArray != null) {
            bundle2 = (Bundle)sparseParcelableArray.get(n);
        }
        return NotificationCompatJellybean.readAction(factory, factory2, notification$Action.icon, notification$Action.title, notification$Action.actionIntent, bundle2);
    }
    
    public static int getActionCount(final Notification notification) {
        if (notification.actions != null) {
            return notification.actions.length;
        }
        return 0;
    }
    
    public static Bundle getExtras(final Notification notification) {
        return notification.extras;
    }
    
    public static String getGroup(final Notification notification) {
        return notification.extras.getString("android.support.groupKey");
    }
    
    public static boolean getLocalOnly(final Notification notification) {
        return notification.extras.getBoolean("android.support.localOnly");
    }
    
    public static String getSortKey(final Notification notification) {
        return notification.extras.getString("android.support.sortKey");
    }
    
    public static boolean isGroupSummary(final Notification notification) {
        return notification.extras.getBoolean("android.support.isGroupSummary");
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder b;
        private List<Bundle> mActionExtrasList;
        private RemoteViews mBigContentView;
        private RemoteViews mContentView;
        private Bundle mExtras;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean showWhen, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean b2, final ArrayList<String> list, final Bundle bundle, final String s, final boolean b3, final String s2, final RemoteViews mContentView, final RemoteViews mBigContentView) {
            this.mActionExtrasList = new ArrayList<Bundle>();
            this.b = new Notification$Builder(context).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b);
            this.mExtras = new Bundle();
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            if (list != null && !list.isEmpty()) {
                this.mExtras.putStringArray("android.people", (String[])list.toArray(new String[list.size()]));
            }
            if (b2) {
                this.mExtras.putBoolean("android.support.localOnly", true);
            }
            if (s != null) {
                this.mExtras.putString("android.support.groupKey", s);
                if (b3) {
                    this.mExtras.putBoolean("android.support.isGroupSummary", true);
                }
                else {
                    this.mExtras.putBoolean("android.support.useSideChannel", true);
                }
            }
            if (s2 != null) {
                this.mExtras.putString("android.support.sortKey", s2);
            }
            this.mContentView = mContentView;
            this.mBigContentView = mBigContentView;
        }
        
        @Override
        public void addAction(final NotificationCompatBase.Action action) {
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.b, action));
        }
        
        @Override
        public Notification build() {
            final SparseArray<Bundle> buildActionExtrasMap = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (buildActionExtrasMap != null) {
                this.mExtras.putSparseParcelableArray("android.support.actionExtras", (SparseArray)buildActionExtrasMap);
            }
            this.b.setExtras(this.mExtras);
            final Notification build = this.b.build();
            if (this.mContentView != null) {
                build.contentView = this.mContentView;
            }
            if (this.mBigContentView != null) {
                build.bigContentView = this.mBigContentView;
            }
            return build;
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.b;
        }
    }
}
