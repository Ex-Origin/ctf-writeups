// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.support.annotation.RestrictTo;
import android.content.Context;
import android.support.v4.text.BidiFormatter;
import android.content.res.ColorStateList;
import android.text.style.TextAppearanceSpan;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.BundleCompat;
import android.support.v4.media.session.MediaSessionCompat;
import java.util.ArrayList;
import android.text.TextUtils;
import android.os.Build$VERSION;
import android.text.SpannableStringBuilder;
import android.support.v7.appcompat.R;
import android.app.PendingIntent;
import android.support.annotation.RequiresApi;
import java.util.List;
import android.app.Notification;
import android.widget.RemoteViews;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;

public class NotificationCompat extends android.support.v4.app.NotificationCompat
{
    @RequiresApi(16)
    private static void addBigStyleToBuilderJellybean(final Notification notification, final android.support.v4.app.NotificationCompat.Builder builder) {
        if (builder.mStyle instanceof MediaStyle) {
            final MediaStyle mediaStyle = (MediaStyle)builder.mStyle;
            RemoteViews remoteViews;
            if (builder.getBigContentView() != null) {
                remoteViews = builder.getBigContentView();
            }
            else {
                remoteViews = builder.getContentView();
            }
            final boolean b = builder.mStyle instanceof DecoratedMediaCustomViewStyle && remoteViews != null;
            NotificationCompatImplBase.overrideMediaBigContentView(notification, builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mNumber, builder.mLargeIcon, builder.mSubText, builder.mUseChronometer, builder.getWhenIfShowing(), builder.getPriority(), 0, builder.mActions, mediaStyle.mShowCancelButton, mediaStyle.mCancelButtonIntent, b);
            if (b) {
                NotificationCompatImplBase.buildIntoRemoteViews(builder.mContext, notification.bigContentView, remoteViews);
            }
        }
        else if (builder.mStyle instanceof DecoratedCustomViewStyle) {
            addDecoratedBigStyleToBuilderJellybean(notification, builder);
        }
    }
    
    @RequiresApi(21)
    private static void addBigStyleToBuilderLollipop(final Notification notification, final android.support.v4.app.NotificationCompat.Builder builder) {
        RemoteViews remoteViews;
        if (builder.getBigContentView() != null) {
            remoteViews = builder.getBigContentView();
        }
        else {
            remoteViews = builder.getContentView();
        }
        if (builder.mStyle instanceof DecoratedMediaCustomViewStyle && remoteViews != null) {
            NotificationCompatImplBase.overrideMediaBigContentView(notification, builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mNumber, builder.mLargeIcon, builder.mSubText, builder.mUseChronometer, builder.getWhenIfShowing(), builder.getPriority(), 0, builder.mActions, false, null, true);
            NotificationCompatImplBase.buildIntoRemoteViews(builder.mContext, notification.bigContentView, remoteViews);
            setBackgroundColor(builder.mContext, notification.bigContentView, builder.getColor());
        }
        else if (builder.mStyle instanceof DecoratedCustomViewStyle) {
            addDecoratedBigStyleToBuilderJellybean(notification, builder);
        }
    }
    
    @RequiresApi(16)
    private static void addDecoratedBigStyleToBuilderJellybean(final Notification notification, final android.support.v4.app.NotificationCompat.Builder builder) {
        RemoteViews remoteViews = builder.getBigContentView();
        if (remoteViews == null) {
            remoteViews = builder.getContentView();
        }
        if (remoteViews == null) {
            return;
        }
        final RemoteViews applyStandardTemplateWithActions = NotificationCompatImplBase.applyStandardTemplateWithActions(builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mNumber, notification.icon, builder.mLargeIcon, builder.mSubText, builder.mUseChronometer, builder.getWhenIfShowing(), builder.getPriority(), builder.getColor(), R.layout.notification_template_custom_big, false, builder.mActions);
        NotificationCompatImplBase.buildIntoRemoteViews(builder.mContext, applyStandardTemplateWithActions, remoteViews);
        notification.bigContentView = applyStandardTemplateWithActions;
    }
    
    @RequiresApi(21)
    private static void addDecoratedHeadsUpToBuilderLollipop(final Notification notification, final android.support.v4.app.NotificationCompat.Builder builder) {
        final RemoteViews headsUpContentView = builder.getHeadsUpContentView();
        RemoteViews contentView;
        if (headsUpContentView != null) {
            contentView = headsUpContentView;
        }
        else {
            contentView = builder.getContentView();
        }
        if (headsUpContentView == null) {
            return;
        }
        final RemoteViews applyStandardTemplateWithActions = NotificationCompatImplBase.applyStandardTemplateWithActions(builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mNumber, notification.icon, builder.mLargeIcon, builder.mSubText, builder.mUseChronometer, builder.getWhenIfShowing(), builder.getPriority(), builder.getColor(), R.layout.notification_template_custom_big, false, builder.mActions);
        NotificationCompatImplBase.buildIntoRemoteViews(builder.mContext, applyStandardTemplateWithActions, contentView);
        notification.headsUpContentView = applyStandardTemplateWithActions;
    }
    
    @RequiresApi(21)
    private static void addHeadsUpToBuilderLollipop(final Notification notification, final android.support.v4.app.NotificationCompat.Builder builder) {
        RemoteViews remoteViews;
        if (builder.getHeadsUpContentView() != null) {
            remoteViews = builder.getHeadsUpContentView();
        }
        else {
            remoteViews = builder.getContentView();
        }
        if (builder.mStyle instanceof DecoratedMediaCustomViewStyle && remoteViews != null) {
            notification.headsUpContentView = NotificationCompatImplBase.generateMediaBigView(builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mNumber, builder.mLargeIcon, builder.mSubText, builder.mUseChronometer, builder.getWhenIfShowing(), builder.getPriority(), 0, builder.mActions, false, null, true);
            NotificationCompatImplBase.buildIntoRemoteViews(builder.mContext, notification.headsUpContentView, remoteViews);
            setBackgroundColor(builder.mContext, notification.headsUpContentView, builder.getColor());
        }
        else if (builder.mStyle instanceof DecoratedCustomViewStyle) {
            addDecoratedHeadsUpToBuilderLollipop(notification, builder);
        }
    }
    
    @RequiresApi(16)
    private static void addMessagingFallBackStyle(final MessagingStyle messagingStyle, final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final android.support.v4.app.NotificationCompat.Builder builder) {
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        final List<MessagingStyle.Message> messages = messagingStyle.getMessages();
        int n;
        if (messagingStyle.getConversationTitle() != null || hasMessagesWithoutSender(messagingStyle.getMessages())) {
            n = 1;
        }
        else {
            n = 0;
        }
        for (int i = messages.size() - 1; i >= 0; --i) {
            final Message message = messages.get(i);
            CharSequence charSequence;
            if (n != 0) {
                charSequence = makeMessageLine(builder, messagingStyle, message);
            }
            else {
                charSequence = message.getText();
            }
            if (i != messages.size() - 1) {
                spannableStringBuilder.insert(0, (CharSequence)"\n");
            }
            spannableStringBuilder.insert(0, charSequence);
        }
        NotificationCompatImplJellybean.addBigTextStyle(notificationBuilderWithBuilderAccessor, (CharSequence)spannableStringBuilder);
    }
    
    @RequiresApi(14)
    private static RemoteViews addStyleGetContentViewIcs(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final android.support.v4.app.NotificationCompat.Builder builder) {
        if (builder.mStyle instanceof MediaStyle) {
            final MediaStyle mediaStyle = (MediaStyle)builder.mStyle;
            final boolean b = builder.mStyle instanceof DecoratedMediaCustomViewStyle && builder.getContentView() != null;
            final RemoteViews overrideContentViewMedia = NotificationCompatImplBase.overrideContentViewMedia(notificationBuilderWithBuilderAccessor, builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mNumber, builder.mLargeIcon, builder.mSubText, builder.mUseChronometer, builder.getWhenIfShowing(), builder.getPriority(), builder.mActions, mediaStyle.mActionsToShowInCompact, mediaStyle.mShowCancelButton, mediaStyle.mCancelButtonIntent, b);
            if (b) {
                NotificationCompatImplBase.buildIntoRemoteViews(builder.mContext, overrideContentViewMedia, builder.getContentView());
                return overrideContentViewMedia;
            }
        }
        else if (builder.mStyle instanceof DecoratedCustomViewStyle) {
            return getDecoratedContentView(builder);
        }
        return null;
    }
    
    @RequiresApi(16)
    private static RemoteViews addStyleGetContentViewJellybean(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final android.support.v4.app.NotificationCompat.Builder builder) {
        if (builder.mStyle instanceof MessagingStyle) {
            addMessagingFallBackStyle((MessagingStyle)builder.mStyle, notificationBuilderWithBuilderAccessor, builder);
        }
        return addStyleGetContentViewIcs(notificationBuilderWithBuilderAccessor, builder);
    }
    
    @RequiresApi(21)
    private static RemoteViews addStyleGetContentViewLollipop(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final android.support.v4.app.NotificationCompat.Builder builder) {
        if (builder.mStyle instanceof MediaStyle) {
            final MediaStyle mediaStyle = (MediaStyle)builder.mStyle;
            final int[] mActionsToShowInCompact = mediaStyle.mActionsToShowInCompact;
            Object token;
            if (mediaStyle.mToken != null) {
                token = mediaStyle.mToken.getToken();
            }
            else {
                token = null;
            }
            NotificationCompatImpl21.addMediaStyle(notificationBuilderWithBuilderAccessor, mActionsToShowInCompact, token);
            final boolean b = builder.getContentView() != null;
            boolean b2;
            if (Build$VERSION.SDK_INT >= 21 && Build$VERSION.SDK_INT <= 23) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            boolean b3;
            if (b || (b2 && builder.getBigContentView() != null)) {
                b3 = true;
            }
            else {
                b3 = false;
            }
            if (builder.mStyle instanceof DecoratedMediaCustomViewStyle && b3) {
                final RemoteViews overrideContentViewMedia = NotificationCompatImplBase.overrideContentViewMedia(notificationBuilderWithBuilderAccessor, builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mNumber, builder.mLargeIcon, builder.mSubText, builder.mUseChronometer, builder.getWhenIfShowing(), builder.getPriority(), builder.mActions, mediaStyle.mActionsToShowInCompact, false, null, b);
                if (b) {
                    NotificationCompatImplBase.buildIntoRemoteViews(builder.mContext, overrideContentViewMedia, builder.getContentView());
                }
                setBackgroundColor(builder.mContext, overrideContentViewMedia, builder.getColor());
                return overrideContentViewMedia;
            }
            return null;
        }
        else {
            if (builder.mStyle instanceof DecoratedCustomViewStyle) {
                return getDecoratedContentView(builder);
            }
            return addStyleGetContentViewJellybean(notificationBuilderWithBuilderAccessor, builder);
        }
    }
    
    @RequiresApi(24)
    private static void addStyleToBuilderApi24(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final android.support.v4.app.NotificationCompat.Builder builder) {
        if (builder.mStyle instanceof DecoratedCustomViewStyle) {
            NotificationCompatImpl24.addDecoratedCustomViewStyle(notificationBuilderWithBuilderAccessor);
        }
        else {
            if (builder.mStyle instanceof DecoratedMediaCustomViewStyle) {
                NotificationCompatImpl24.addDecoratedMediaCustomViewStyle(notificationBuilderWithBuilderAccessor);
                return;
            }
            if (!(builder.mStyle instanceof MessagingStyle)) {
                addStyleGetContentViewLollipop(notificationBuilderWithBuilderAccessor, builder);
            }
        }
    }
    
    private static Message findLatestIncomingMessage(final MessagingStyle messagingStyle) {
        final List<MessagingStyle.Message> messages = messagingStyle.getMessages();
        for (int i = messages.size() - 1; i >= 0; --i) {
            final Message message = messages.get(i);
            if (!TextUtils.isEmpty(message.getSender())) {
                return message;
            }
        }
        if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1);
        }
        return null;
    }
    
    private static RemoteViews getDecoratedContentView(final android.support.v4.app.NotificationCompat.Builder builder) {
        if (builder.getContentView() == null) {
            return null;
        }
        final RemoteViews applyStandardTemplateWithActions = NotificationCompatImplBase.applyStandardTemplateWithActions(builder.mContext, builder.mContentTitle, builder.mContentText, builder.mContentInfo, builder.mNumber, builder.mNotification.icon, builder.mLargeIcon, builder.mSubText, builder.mUseChronometer, builder.getWhenIfShowing(), builder.getPriority(), builder.getColor(), R.layout.notification_template_custom_big, false, null);
        NotificationCompatImplBase.buildIntoRemoteViews(builder.mContext, applyStandardTemplateWithActions, builder.getContentView());
        return applyStandardTemplateWithActions;
    }
    
    public static MediaSessionCompat.Token getMediaSession(final Notification notification) {
        final Bundle extras = android.support.v4.app.NotificationCompat.getExtras(notification);
        if (extras != null) {
            if (Build$VERSION.SDK_INT >= 21) {
                final Parcelable parcelable = extras.getParcelable("android.mediaSession");
                if (parcelable != null) {
                    return MediaSessionCompat.Token.fromToken(parcelable);
                }
            }
            else {
                final IBinder binder = BundleCompat.getBinder(extras, "android.mediaSession");
                if (binder != null) {
                    final Parcel obtain = Parcel.obtain();
                    obtain.writeStrongBinder(binder);
                    obtain.setDataPosition(0);
                    final MediaSessionCompat.Token token = (MediaSessionCompat.Token)MediaSessionCompat.Token.CREATOR.createFromParcel(obtain);
                    obtain.recycle();
                    return token;
                }
            }
        }
        return null;
    }
    
    private static boolean hasMessagesWithoutSender(final List<Message> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            if (list.get(i).getSender() == null) {
                return true;
            }
        }
        return false;
    }
    
    private static TextAppearanceSpan makeFontColorSpan(final int n) {
        return new TextAppearanceSpan((String)null, 0, 0, ColorStateList.valueOf(n), (ColorStateList)null);
    }
    
    private static CharSequence makeMessageLine(final android.support.v4.app.NotificationCompat.Builder builder, final MessagingStyle messagingStyle, final Message message) {
        final BidiFormatter instance = BidiFormatter.getInstance();
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        boolean b;
        if (Build$VERSION.SDK_INT >= 21) {
            b = true;
        }
        else {
            b = false;
        }
        int n;
        if (b || Build$VERSION.SDK_INT <= 10) {
            n = -16777216;
        }
        else {
            n = -1;
        }
        CharSequence sender = message.getSender();
        int color = n;
        if (TextUtils.isEmpty(message.getSender())) {
            CharSequence userDisplayName;
            if (messagingStyle.getUserDisplayName() == null) {
                userDisplayName = "";
            }
            else {
                userDisplayName = messagingStyle.getUserDisplayName();
            }
            color = n;
            sender = userDisplayName;
            if (b) {
                color = n;
                sender = userDisplayName;
                if (builder.getColor() != 0) {
                    color = builder.getColor();
                    sender = userDisplayName;
                }
            }
        }
        final CharSequence unicodeWrap = instance.unicodeWrap(sender);
        spannableStringBuilder.append(unicodeWrap);
        spannableStringBuilder.setSpan((Object)makeFontColorSpan(color), spannableStringBuilder.length() - unicodeWrap.length(), spannableStringBuilder.length(), 33);
        CharSequence text;
        if (message.getText() == null) {
            text = "";
        }
        else {
            text = message.getText();
        }
        spannableStringBuilder.append((CharSequence)"  ").append(instance.unicodeWrap(text));
        return (CharSequence)spannableStringBuilder;
    }
    
    private static void setBackgroundColor(final Context context, final RemoteViews remoteViews, final int n) {
        int color = n;
        if (n == 0) {
            color = context.getResources().getColor(R.color.notification_material_background_media_default_color);
        }
        remoteViews.setInt(R.id.status_bar_latest_event_content, "setBackgroundColor", color);
    }
    
    @RequiresApi(24)
    private static class Api24Extender extends BuilderExtender
    {
        @Override
        public Notification build(final Builder builder, final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            addStyleToBuilderApi24(notificationBuilderWithBuilderAccessor, builder);
            return notificationBuilderWithBuilderAccessor.build();
        }
    }
    
    public static class Builder extends android.support.v4.app.NotificationCompat.Builder
    {
        public Builder(final Context context) {
            super(context);
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        protected BuilderExtender getExtender() {
            if (Build$VERSION.SDK_INT >= 24) {
                return new Api24Extender();
            }
            if (Build$VERSION.SDK_INT >= 21) {
                return new LollipopExtender();
            }
            if (Build$VERSION.SDK_INT >= 16) {
                return new JellybeanExtender();
            }
            if (Build$VERSION.SDK_INT >= 14) {
                return new IceCreamSandwichExtender();
            }
            return super.getExtender();
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        protected CharSequence resolveText() {
            if (this.mStyle instanceof MessagingStyle) {
                final MessagingStyle messagingStyle = (MessagingStyle)this.mStyle;
                final Message access$000 = findLatestIncomingMessage(messagingStyle);
                final CharSequence conversationTitle = messagingStyle.getConversationTitle();
                if (access$000 != null) {
                    if (conversationTitle != null) {
                        return makeMessageLine(this, messagingStyle, access$000);
                    }
                    return access$000.getText();
                }
            }
            return super.resolveText();
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        @Override
        protected CharSequence resolveTitle() {
            if (this.mStyle instanceof MessagingStyle) {
                final MessagingStyle messagingStyle = (MessagingStyle)this.mStyle;
                final Message access$000 = findLatestIncomingMessage(messagingStyle);
                final CharSequence conversationTitle = messagingStyle.getConversationTitle();
                if (conversationTitle != null || access$000 != null) {
                    if (conversationTitle != null) {
                        return conversationTitle;
                    }
                    return access$000.getSender();
                }
            }
            return super.resolveTitle();
        }
    }
    
    public static class DecoratedCustomViewStyle extends Style
    {
    }
    
    public static class DecoratedMediaCustomViewStyle extends MediaStyle
    {
    }
    
    @RequiresApi(14)
    private static class IceCreamSandwichExtender extends BuilderExtender
    {
        @Override
        public Notification build(final Builder builder, final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            final RemoteViews access$300 = addStyleGetContentViewIcs(notificationBuilderWithBuilderAccessor, builder);
            final Notification build = notificationBuilderWithBuilderAccessor.build();
            if (access$300 != null) {
                build.contentView = access$300;
            }
            else if (builder.getContentView() != null) {
                build.contentView = builder.getContentView();
                return build;
            }
            return build;
        }
    }
    
    @RequiresApi(16)
    private static class JellybeanExtender extends BuilderExtender
    {
        @Override
        public Notification build(final Builder builder, final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            final RemoteViews access$400 = addStyleGetContentViewJellybean(notificationBuilderWithBuilderAccessor, builder);
            final Notification build = notificationBuilderWithBuilderAccessor.build();
            if (access$400 != null) {
                build.contentView = access$400;
            }
            addBigStyleToBuilderJellybean(build, builder);
            return build;
        }
    }
    
    @RequiresApi(21)
    private static class LollipopExtender extends BuilderExtender
    {
        @Override
        public Notification build(final Builder builder, final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            final RemoteViews access$600 = addStyleGetContentViewLollipop(notificationBuilderWithBuilderAccessor, builder);
            final Notification build = notificationBuilderWithBuilderAccessor.build();
            if (access$600 != null) {
                build.contentView = access$600;
            }
            addBigStyleToBuilderLollipop(build, builder);
            addHeadsUpToBuilderLollipop(build, builder);
            return build;
        }
    }
    
    public static class MediaStyle extends Style
    {
        int[] mActionsToShowInCompact;
        PendingIntent mCancelButtonIntent;
        boolean mShowCancelButton;
        MediaSessionCompat.Token mToken;
        
        public MediaStyle() {
            this.mActionsToShowInCompact = null;
        }
        
        public MediaStyle(final Builder builder) {
            this.mActionsToShowInCompact = null;
            ((Style)this).setBuilder(builder);
        }
        
        public MediaStyle setCancelButtonIntent(final PendingIntent mCancelButtonIntent) {
            this.mCancelButtonIntent = mCancelButtonIntent;
            return this;
        }
        
        public MediaStyle setMediaSession(final MediaSessionCompat.Token mToken) {
            this.mToken = mToken;
            return this;
        }
        
        public MediaStyle setShowActionsInCompactView(final int... mActionsToShowInCompact) {
            this.mActionsToShowInCompact = mActionsToShowInCompact;
            return this;
        }
        
        public MediaStyle setShowCancelButton(final boolean mShowCancelButton) {
            this.mShowCancelButton = mShowCancelButton;
            return this;
        }
    }
}
