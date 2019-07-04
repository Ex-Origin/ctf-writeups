// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.graphics.Bitmap;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;
import android.os.Parcelable;
import java.util.ArrayList;
import android.app.Notification$Action;
import android.app.Notification;
import android.app.RemoteInput;
import android.os.Bundle;
import android.app.Notification$Action$Builder;
import android.app.Notification$Builder;
import android.support.annotation.RequiresApi;

@RequiresApi(20)
class NotificationCompatApi20
{
    public static void addAction(final Notification$Builder notification$Builder, final NotificationCompatBase.Action action) {
        final Notification$Action$Builder notification$Action$Builder = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
        if (action.getRemoteInputs() != null) {
            final RemoteInput[] fromCompat = RemoteInputCompatApi20.fromCompat(action.getRemoteInputs());
            for (int length = fromCompat.length, i = 0; i < length; ++i) {
                notification$Action$Builder.addRemoteInput(fromCompat[i]);
            }
        }
        Bundle bundle;
        if (action.getExtras() != null) {
            bundle = new Bundle(action.getExtras());
        }
        else {
            bundle = new Bundle();
        }
        bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        notification$Action$Builder.addExtras(bundle);
        notification$Builder.addAction(notification$Action$Builder.build());
    }
    
    public static NotificationCompatBase.Action getAction(final Notification notification, final int n, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        return getActionCompatFromAction(notification.actions[n], factory, factory2);
    }
    
    private static NotificationCompatBase.Action getActionCompatFromAction(final Notification$Action notification$Action, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        return factory.build(notification$Action.icon, notification$Action.title, notification$Action.actionIntent, notification$Action.getExtras(), RemoteInputCompatApi20.toCompat(notification$Action.getRemoteInputs(), factory2), null, notification$Action.getExtras().getBoolean("android.support.allowGeneratedReplies"));
    }
    
    private static Notification$Action getActionFromActionCompat(final NotificationCompatBase.Action action) {
        final Notification$Action$Builder notification$Action$Builder = new Notification$Action$Builder(action.getIcon(), action.getTitle(), action.getActionIntent());
        Bundle bundle;
        if (action.getExtras() != null) {
            bundle = new Bundle(action.getExtras());
        }
        else {
            bundle = new Bundle();
        }
        bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        notification$Action$Builder.addExtras(bundle);
        final RemoteInputCompatBase.RemoteInput[] remoteInputs = action.getRemoteInputs();
        if (remoteInputs != null) {
            final RemoteInput[] fromCompat = RemoteInputCompatApi20.fromCompat(remoteInputs);
            for (int length = fromCompat.length, i = 0; i < length; ++i) {
                notification$Action$Builder.addRemoteInput(fromCompat[i]);
            }
        }
        return notification$Action$Builder.build();
    }
    
    public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> list, final NotificationCompatBase.Action.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        NotificationCompatBase.Action[] array;
        if (list == null) {
            array = null;
        }
        else {
            final NotificationCompatBase.Action[] array2 = factory.newArray(list.size());
            int n = 0;
            while (true) {
                array = array2;
                if (n >= array2.length) {
                    break;
                }
                array2[n] = getActionCompatFromAction((Notification$Action)list.get(n), factory, factory2);
                ++n;
            }
        }
        return array;
    }
    
    public static String getGroup(final Notification notification) {
        return notification.getGroup();
    }
    
    public static boolean getLocalOnly(final Notification notification) {
        return (notification.flags & 0x100) != 0x0;
    }
    
    public static ArrayList<Parcelable> getParcelableArrayListForActions(final NotificationCompatBase.Action[] array) {
        ArrayList<Parcelable> list;
        if (array == null) {
            list = null;
        }
        else {
            final ArrayList<Parcelable> list2 = new ArrayList<Parcelable>(array.length);
            final int length = array.length;
            int n = 0;
            while (true) {
                list = list2;
                if (n >= length) {
                    break;
                }
                list2.add((Parcelable)getActionFromActionCompat(array[n]));
                ++n;
            }
        }
        return list;
    }
    
    public static String getSortKey(final Notification notification) {
        return notification.getSortKey();
    }
    
    public static boolean isGroupSummary(final Notification notification) {
        return (notification.flags & 0x200) != 0x0;
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder b;
        private RemoteViews mBigContentView;
        private RemoteViews mContentView;
        private Bundle mExtras;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean showWhen, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean localOnly, final ArrayList<String> list, final Bundle bundle, final String group, final boolean groupSummary, final String sortKey, final RemoteViews mContentView, final RemoteViews mBigContentView) {
            this.b = new Notification$Builder(context).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b).setLocalOnly(localOnly).setGroup(group).setGroupSummary(groupSummary).setSortKey(sortKey);
            this.mExtras = new Bundle();
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            if (list != null && !list.isEmpty()) {
                this.mExtras.putStringArray("android.people", (String[])list.toArray(new String[list.size()]));
            }
            this.mContentView = mContentView;
            this.mBigContentView = mBigContentView;
        }
        
        @Override
        public void addAction(final NotificationCompatBase.Action action) {
            NotificationCompatApi20.addAction(this.b, action);
        }
        
        @Override
        public Notification build() {
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
