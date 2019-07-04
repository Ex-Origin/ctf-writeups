// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.util.Iterator;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.app.PendingIntent;
import android.widget.RemoteViews;
import android.app.Notification;
import android.content.Context;
import android.app.Notification$MessagingStyle$Message;
import android.app.Notification$MessagingStyle;
import android.net.Uri;
import java.util.List;
import android.app.RemoteInput;
import android.os.Bundle;
import android.app.Notification$Action$Builder;
import android.app.Notification$Builder;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
class NotificationCompatApi24
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
        notification$Action$Builder.setAllowGeneratedReplies(action.getAllowGeneratedReplies());
        notification$Builder.addAction(notification$Action$Builder.build());
    }
    
    public static void addMessagingStyle(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final CharSequence charSequence, final CharSequence conversationTitle, final List<CharSequence> list, final List<Long> list2, final List<CharSequence> list3, final List<String> list4, final List<Uri> list5) {
        final Notification$MessagingStyle setConversationTitle = new Notification$MessagingStyle(charSequence).setConversationTitle(conversationTitle);
        for (int i = 0; i < list.size(); ++i) {
            final Notification$MessagingStyle$Message notification$MessagingStyle$Message = new Notification$MessagingStyle$Message((CharSequence)list.get(i), (long)list2.get(i), (CharSequence)list3.get(i));
            if (list4.get(i) != null) {
                notification$MessagingStyle$Message.setData((String)list4.get(i), (Uri)list5.get(i));
            }
            setConversationTitle.addMessage(notification$MessagingStyle$Message);
        }
        setConversationTitle.setBuilder(notificationBuilderWithBuilderAccessor.getBuilder());
    }
    
    public static class Builder implements NotificationBuilderWithBuilderAccessor, NotificationBuilderWithActions
    {
        private Notification$Builder b;
        
        public Builder(final Context context, final Notification notification, final CharSequence contentTitle, final CharSequence contentText, final CharSequence contentInfo, final RemoteViews remoteViews, final int number, final PendingIntent contentIntent, final PendingIntent pendingIntent, final Bitmap largeIcon, final int n, final int n2, final boolean b, final boolean showWhen, final boolean usesChronometer, final int priority, final CharSequence subText, final boolean localOnly, final String category, final ArrayList<String> list, final Bundle extras, final int color, final int visibility, final Notification publicVersion, final String group, final boolean groupSummary, final String sortKey, final CharSequence[] remoteInputHistory, final RemoteViews customContentView, final RemoteViews customBigContentView, final RemoteViews customHeadsUpContentView) {
            this.b = new Notification$Builder(context).setWhen(notification.when).setShowWhen(showWhen).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS).setOngoing((notification.flags & 0x2) != 0x0).setOnlyAlertOnce((notification.flags & 0x8) != 0x0).setAutoCancel((notification.flags & 0x10) != 0x0).setDefaults(notification.defaults).setContentTitle(contentTitle).setContentText(contentText).setSubText(subText).setContentInfo(contentInfo).setContentIntent(contentIntent).setDeleteIntent(notification.deleteIntent).setFullScreenIntent(pendingIntent, (notification.flags & 0x80) != 0x0).setLargeIcon(largeIcon).setNumber(number).setUsesChronometer(usesChronometer).setPriority(priority).setProgress(n, n2, b).setLocalOnly(localOnly).setExtras(extras).setGroup(group).setGroupSummary(groupSummary).setSortKey(sortKey).setCategory(category).setColor(color).setVisibility(visibility).setPublicVersion(publicVersion).setRemoteInputHistory(remoteInputHistory);
            if (customContentView != null) {
                this.b.setCustomContentView(customContentView);
            }
            if (customBigContentView != null) {
                this.b.setCustomBigContentView(customBigContentView);
            }
            if (customHeadsUpContentView != null) {
                this.b.setCustomHeadsUpContentView(customHeadsUpContentView);
            }
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                this.b.addPerson((String)iterator.next());
            }
        }
        
        @Override
        public void addAction(final NotificationCompatBase.Action action) {
            NotificationCompatApi24.addAction(this.b, action);
        }
        
        @Override
        public Notification build() {
            return this.b.build();
        }
        
        @Override
        public Notification$Builder getBuilder() {
            return this.b;
        }
    }
}
