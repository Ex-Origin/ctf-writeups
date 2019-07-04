// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.util.Iterator;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.content.Context;
import android.widget.RemoteViews;
import android.app.Notification$Builder;
import java.util.Set;
import android.app.PendingIntent;
import android.app.Notification;
import android.os.Parcelable;
import android.os.Bundle;
import android.app.RemoteInput$Builder;
import android.app.RemoteInput;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class NotificationCompatApi21
{
    public static final String CATEGORY_ALARM = "alarm";
    public static final String CATEGORY_CALL = "call";
    public static final String CATEGORY_EMAIL = "email";
    public static final String CATEGORY_ERROR = "err";
    public static final String CATEGORY_EVENT = "event";
    public static final String CATEGORY_MESSAGE = "msg";
    public static final String CATEGORY_PROGRESS = "progress";
    public static final String CATEGORY_PROMO = "promo";
    public static final String CATEGORY_RECOMMENDATION = "recommendation";
    public static final String CATEGORY_SERVICE = "service";
    public static final String CATEGORY_SOCIAL = "social";
    public static final String CATEGORY_STATUS = "status";
    public static final String CATEGORY_SYSTEM = "sys";
    public static final String CATEGORY_TRANSPORT = "transport";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_MESSAGES = "messages";
    private static final String KEY_ON_READ = "on_read";
    private static final String KEY_ON_REPLY = "on_reply";
    private static final String KEY_PARTICIPANTS = "participants";
    private static final String KEY_REMOTE_INPUT = "remote_input";
    private static final String KEY_TEXT = "text";
    private static final String KEY_TIMESTAMP = "timestamp";
    
    private static RemoteInput fromCompatRemoteInput(final RemoteInputCompatBase.RemoteInput remoteInput) {
        return new RemoteInput$Builder(remoteInput.getResultKey()).setLabel(remoteInput.getLabel()).setChoices(remoteInput.getChoices()).setAllowFreeFormInput(remoteInput.getAllowFreeFormInput()).addExtras(remoteInput.getExtras()).build();
    }
    
    static Bundle getBundleForUnreadConversation(final NotificationCompatBase.UnreadConversation unreadConversation) {
        if (unreadConversation == null) {
            return null;
        }
        final Bundle bundle = new Bundle();
        String s = null;
        if (unreadConversation.getParticipants() != null) {
            s = s;
            if (unreadConversation.getParticipants().length > 1) {
                s = unreadConversation.getParticipants()[0];
            }
        }
        final Parcelable[] array = new Parcelable[unreadConversation.getMessages().length];
        for (int i = 0; i < array.length; ++i) {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("text", unreadConversation.getMessages()[i]);
            bundle2.putString("author", s);
            array[i] = (Parcelable)bundle2;
        }
        bundle.putParcelableArray("messages", array);
        final RemoteInputCompatBase.RemoteInput remoteInput = unreadConversation.getRemoteInput();
        if (remoteInput != null) {
            bundle.putParcelable("remote_input", (Parcelable)fromCompatRemoteInput(remoteInput));
        }
        bundle.putParcelable("on_reply", (Parcelable)unreadConversation.getReplyPendingIntent());
        bundle.putParcelable("on_read", (Parcelable)unreadConversation.getReadPendingIntent());
        bundle.putStringArray("participants", unreadConversation.getParticipants());
        bundle.putLong("timestamp", unreadConversation.getLatestTimestamp());
        return bundle;
    }
    
    public static String getCategory(final Notification notification) {
        return notification.category;
    }
    
    static NotificationCompatBase.UnreadConversation getUnreadConversationFromBundle(final Bundle bundle, final NotificationCompatBase.UnreadConversation.Factory factory, final RemoteInputCompatBase.RemoteInput.Factory factory2) {
        RemoteInputCompatBase.RemoteInput compatRemoteInput = null;
        if (bundle != null) {
            final Parcelable[] parcelableArray = bundle.getParcelableArray("messages");
            String[] array = null;
            Label_0067: {
                if (parcelableArray != null) {
                    array = new String[parcelableArray.length];
                    final boolean b = true;
                    int n = 0;
                    while (true) {
                        Block_7: {
                            int n2;
                            while (true) {
                                n2 = (b ? 1 : 0);
                                if (n >= array.length) {
                                    break;
                                }
                                if (!(parcelableArray[n] instanceof Bundle)) {
                                    n2 = 0;
                                    break;
                                }
                                array[n] = ((Bundle)parcelableArray[n]).getString("text");
                                if (array[n] == null) {
                                    break Block_7;
                                }
                                ++n;
                            }
                            if (n2 != 0) {
                                break Label_0067;
                            }
                            return null;
                        }
                        int n2 = 0;
                        continue;
                    }
                }
            }
            final PendingIntent pendingIntent = (PendingIntent)bundle.getParcelable("on_read");
            final PendingIntent pendingIntent2 = (PendingIntent)bundle.getParcelable("on_reply");
            final RemoteInput remoteInput = (RemoteInput)bundle.getParcelable("remote_input");
            final String[] stringArray = bundle.getStringArray("participants");
            if (stringArray != null && stringArray.length == 1) {
                if (remoteInput != null) {
                    compatRemoteInput = toCompatRemoteInput(remoteInput, factory2);
                }
                return factory.build(array, compatRemoteInput, pendingIntent2, pendingIntent, stringArray, bundle.getLong("timestamp"));
            }
        }
        return null;
    }
    
    private static RemoteInputCompatBase.RemoteInput toCompatRemoteInput(final RemoteInput remoteInput, final RemoteInputCompatBase.RemoteInput.Factory factory) {
        return factory.build(remoteInput.getResultKey(), remoteInput.getLabel(), remoteInput.getChoices(), remoteInput.getAllowFreeFormInput(), remoteInput.getExtras(), null);
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder b;
        private RemoteViews mBigContentView;
        private RemoteViews mContentView;
        private Bundle mExtras;
        private RemoteViews mHeadsUpContentView;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean showWhen, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean localOnly, final String category, final ArrayList<String> list, final Bundle bundle, final int color, final int visibility, final Notification publicVersion, final String group, final boolean groupSummary, final String sortKey, final RemoteViews mContentView, final RemoteViews mBigContentView, final RemoteViews mHeadsUpContentView) {
            this.b = new Notification$Builder(context).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b).setLocalOnly(localOnly).setGroup(group).setGroupSummary(groupSummary).setSortKey(sortKey).setCategory(category).setColor(color).setVisibility(visibility).setPublicVersion(publicVersion);
            this.mExtras = new Bundle();
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.b.addPerson((String)iterator.next());
            }
            this.mContentView = mContentView;
            this.mBigContentView = mBigContentView;
            this.mHeadsUpContentView = mHeadsUpContentView;
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
            if (this.mHeadsUpContentView != null) {
                build.headsUpContentView = this.mHeadsUpContentView;
            }
            return build;
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.b;
        }
    }
}
