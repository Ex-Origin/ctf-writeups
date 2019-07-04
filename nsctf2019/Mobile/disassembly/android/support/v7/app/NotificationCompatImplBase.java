// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.app.Notification;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompatBase;
import android.app.PendingIntent;
import java.util.List;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import android.graphics.Bitmap$Config;
import android.support.v4.app.NotificationCompat;
import java.util.ArrayList;
import android.content.res.Resources;
import android.os.SystemClock;
import java.text.NumberFormat;
import android.support.v7.appcompat.R;
import android.os.Build$VERSION;
import android.widget.RemoteViews;
import android.graphics.Bitmap;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(9)
class NotificationCompatImplBase
{
    private static final int MAX_ACTION_BUTTONS = 3;
    static final int MAX_MEDIA_BUTTONS = 5;
    static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
    
    public static RemoteViews applyStandardTemplate(final Context context, final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3, int n, int n2, final Bitmap bitmap, final CharSequence charSequence4, final boolean b, final long n3, int n4, final int n5, int n6, final boolean b2) {
        final Resources resources = context.getResources();
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), n6);
        final int n7 = 0;
        n6 = 0;
        if (n4 < -1) {
            n4 = 1;
        }
        else {
            n4 = 0;
        }
        if (Build$VERSION.SDK_INT >= 16 && Build$VERSION.SDK_INT < 21) {
            if (n4 != 0) {
                remoteViews.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg_low);
                remoteViews.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_low_bg);
            }
            else {
                remoteViews.setInt(R.id.notification_background, "setBackgroundResource", R.drawable.notification_bg);
                remoteViews.setInt(R.id.icon, "setBackgroundResource", R.drawable.notification_template_icon_bg);
            }
        }
        if (bitmap != null) {
            if (Build$VERSION.SDK_INT >= 16) {
                remoteViews.setViewVisibility(R.id.icon, 0);
                remoteViews.setImageViewBitmap(R.id.icon, bitmap);
            }
            else {
                remoteViews.setViewVisibility(R.id.icon, 8);
            }
            if (n2 != 0) {
                n4 = resources.getDimensionPixelSize(R.dimen.notification_right_icon_size);
                final int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.notification_small_icon_background_padding);
                if (Build$VERSION.SDK_INT >= 21) {
                    remoteViews.setImageViewBitmap(R.id.right_icon, createIconWithBackground(context, n2, n4, n4 - dimensionPixelSize * 2, n5));
                }
                else {
                    remoteViews.setImageViewBitmap(R.id.right_icon, createColoredBitmap(context, n2, -1));
                }
                remoteViews.setViewVisibility(R.id.right_icon, 0);
            }
        }
        else if (n2 != 0) {
            remoteViews.setViewVisibility(R.id.icon, 0);
            if (Build$VERSION.SDK_INT >= 21) {
                remoteViews.setImageViewBitmap(R.id.icon, createIconWithBackground(context, n2, resources.getDimensionPixelSize(R.dimen.notification_large_icon_width) - resources.getDimensionPixelSize(R.dimen.notification_big_circle_margin), resources.getDimensionPixelSize(R.dimen.notification_small_icon_size_as_large), n5));
            }
            else {
                remoteViews.setImageViewBitmap(R.id.icon, createColoredBitmap(context, n2, -1));
            }
        }
        if (charSequence != null) {
            remoteViews.setTextViewText(R.id.title, charSequence);
        }
        n2 = n7;
        if (charSequence2 != null) {
            remoteViews.setTextViewText(R.id.text, charSequence2);
            n2 = 1;
        }
        if (Build$VERSION.SDK_INT < 21 && bitmap != null) {
            n4 = 1;
        }
        else {
            n4 = 0;
        }
        if (charSequence3 != null) {
            remoteViews.setTextViewText(R.id.info, charSequence3);
            remoteViews.setViewVisibility(R.id.info, 0);
            n2 = 1;
            n = 1;
        }
        else if (n > 0) {
            if (n > resources.getInteger(R.integer.status_bar_notification_info_maxnum)) {
                remoteViews.setTextViewText(R.id.info, (CharSequence)resources.getString(R.string.status_bar_notification_info_overflow));
            }
            else {
                remoteViews.setTextViewText(R.id.info, (CharSequence)NumberFormat.getIntegerInstance().format(n));
            }
            remoteViews.setViewVisibility(R.id.info, 0);
            n2 = 1;
            n = 1;
        }
        else {
            remoteViews.setViewVisibility(R.id.info, 8);
            n = n4;
        }
        n4 = n6;
        if (charSequence4 != null) {
            n4 = n6;
            if (Build$VERSION.SDK_INT >= 16) {
                remoteViews.setTextViewText(R.id.text, charSequence4);
                if (charSequence2 != null) {
                    remoteViews.setTextViewText(R.id.text2, charSequence2);
                    remoteViews.setViewVisibility(R.id.text2, 0);
                    n4 = 1;
                }
                else {
                    remoteViews.setViewVisibility(R.id.text2, 8);
                    n4 = n6;
                }
            }
        }
        if (n4 != 0 && Build$VERSION.SDK_INT >= 16) {
            if (b2) {
                remoteViews.setTextViewTextSize(R.id.text, 0, (float)resources.getDimensionPixelSize(R.dimen.notification_subtext_size));
            }
            remoteViews.setViewPadding(R.id.line1, 0, 0, 0, 0);
        }
        if (n3 != 0L) {
            if (b && Build$VERSION.SDK_INT >= 16) {
                remoteViews.setViewVisibility(R.id.chronometer, 0);
                remoteViews.setLong(R.id.chronometer, "setBase", SystemClock.elapsedRealtime() - System.currentTimeMillis() + n3);
                remoteViews.setBoolean(R.id.chronometer, "setStarted", true);
            }
            else {
                remoteViews.setViewVisibility(R.id.time, 0);
                remoteViews.setLong(R.id.time, "setTime", n3);
            }
            n = 1;
        }
        n4 = R.id.right_side;
        if (n != 0) {
            n = 0;
        }
        else {
            n = 8;
        }
        remoteViews.setViewVisibility(n4, n);
        n4 = R.id.line3;
        if (n2 != 0) {
            n = 0;
        }
        else {
            n = 8;
        }
        remoteViews.setViewVisibility(n4, n);
        return remoteViews;
    }
    
    public static RemoteViews applyStandardTemplateWithActions(final Context context, final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3, int n, int n2, final Bitmap bitmap, final CharSequence charSequence4, final boolean b, final long n3, int size, int n4, final int n5, final boolean b2, final ArrayList<NotificationCompat.Action> list) {
        final RemoteViews applyStandardTemplate = applyStandardTemplate(context, charSequence, charSequence2, charSequence3, n, n2, bitmap, charSequence4, b, n3, size, n4, n5, b2);
        applyStandardTemplate.removeAllViews(R.id.actions);
        n = (n2 = 0);
        if (list != null) {
            size = list.size();
            n2 = n;
            if (size > 0) {
                n4 = 1;
                if ((n = size) > 3) {
                    n = 3;
                }
                size = 0;
                while (true) {
                    n2 = n4;
                    if (size >= n) {
                        break;
                    }
                    applyStandardTemplate.addView(R.id.actions, generateActionButton(context, list.get(size)));
                    ++size;
                }
            }
        }
        if (n2 != 0) {
            n = 0;
        }
        else {
            n = 8;
        }
        applyStandardTemplate.setViewVisibility(R.id.actions, n);
        applyStandardTemplate.setViewVisibility(R.id.action_divider, n);
        return applyStandardTemplate;
    }
    
    public static void buildIntoRemoteViews(final Context context, final RemoteViews remoteViews, final RemoteViews remoteViews2) {
        hideNormalContent(remoteViews);
        remoteViews.removeAllViews(R.id.notification_main_column);
        remoteViews.addView(R.id.notification_main_column, remoteViews2.clone());
        remoteViews.setViewVisibility(R.id.notification_main_column, 0);
        if (Build$VERSION.SDK_INT >= 21) {
            remoteViews.setViewPadding(R.id.notification_main_column_container, 0, calculateTopPadding(context), 0, 0);
        }
    }
    
    public static int calculateTopPadding(final Context context) {
        final int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.notification_top_pad);
        final int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.notification_top_pad_large_text);
        final float n = (constrain(context.getResources().getConfiguration().fontScale, 1.0f, 1.3f) - 1.0f) / 0.29999995f;
        return Math.round((1.0f - n) * dimensionPixelSize + dimensionPixelSize2 * n);
    }
    
    public static float constrain(final float n, final float n2, final float n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }
    
    private static Bitmap createColoredBitmap(final Context context, final int n, final int n2) {
        return createColoredBitmap(context, n, n2, 0);
    }
    
    private static Bitmap createColoredBitmap(final Context context, int intrinsicWidth, final int n, int intrinsicHeight) {
        final Drawable drawable = context.getResources().getDrawable(intrinsicWidth);
        if (intrinsicHeight == 0) {
            intrinsicWidth = drawable.getIntrinsicWidth();
        }
        else {
            intrinsicWidth = intrinsicHeight;
        }
        if (intrinsicHeight == 0) {
            intrinsicHeight = drawable.getIntrinsicHeight();
        }
        final Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap$Config.ARGB_8888);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        if (n != 0) {
            drawable.mutate().setColorFilter((ColorFilter)new PorterDuffColorFilter(n, PorterDuff$Mode.SRC_IN));
        }
        drawable.draw(new Canvas(bitmap));
        return bitmap;
    }
    
    public static Bitmap createIconWithBackground(final Context context, int n, final int n2, final int n3, final int n4) {
        final int notification_icon_background = R.drawable.notification_icon_background;
        int n5 = n4;
        if (n4 == 0) {
            n5 = 0;
        }
        final Bitmap coloredBitmap = createColoredBitmap(context, notification_icon_background, n5, n2);
        final Canvas canvas = new Canvas(coloredBitmap);
        final Drawable mutate = context.getResources().getDrawable(n).mutate();
        mutate.setFilterBitmap(true);
        n = (n2 - n3) / 2;
        mutate.setBounds(n, n, n3 + n, n3 + n);
        mutate.setColorFilter((ColorFilter)new PorterDuffColorFilter(-1, PorterDuff$Mode.SRC_ATOP));
        mutate.draw(canvas);
        return coloredBitmap;
    }
    
    private static RemoteViews generateActionButton(final Context context, final NotificationCompat.Action action) {
        boolean b;
        if (action.actionIntent == null) {
            b = true;
        }
        else {
            b = false;
        }
        final String packageName = context.getPackageName();
        int n;
        if (b) {
            n = getActionTombstoneLayoutResource();
        }
        else {
            n = getActionLayoutResource();
        }
        final RemoteViews remoteViews = new RemoteViews(packageName, n);
        remoteViews.setImageViewBitmap(R.id.action_image, createColoredBitmap(context, action.getIcon(), context.getResources().getColor(R.color.notification_action_color_filter)));
        remoteViews.setTextViewText(R.id.action_text, action.title);
        if (!b) {
            remoteViews.setOnClickPendingIntent(R.id.action_container, action.actionIntent);
        }
        if (Build$VERSION.SDK_INT >= 15) {
            remoteViews.setContentDescription(R.id.action_container, action.title);
        }
        return remoteViews;
    }
    
    @RequiresApi(11)
    private static <T extends NotificationCompatBase.Action> RemoteViews generateContentViewMedia(final Context context, final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3, int min, final Bitmap bitmap, final CharSequence charSequence4, final boolean b, final long n, int i, final List<T> list, final int[] array, final boolean b2, final PendingIntent pendingIntent, final boolean b3) {
        int n2;
        if (b3) {
            n2 = R.layout.notification_template_media_custom;
        }
        else {
            n2 = R.layout.notification_template_media;
        }
        final RemoteViews applyStandardTemplate = applyStandardTemplate(context, charSequence, charSequence2, charSequence3, min, 0, bitmap, charSequence4, b, n, i, 0, n2, true);
        final int size = list.size();
        if (array == null) {
            min = 0;
        }
        else {
            min = Math.min(array.length, 3);
        }
        applyStandardTemplate.removeAllViews(R.id.media_actions);
        if (min > 0) {
            for (i = 0; i < min; ++i) {
                if (i >= size) {
                    throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", i, size - 1));
                }
                applyStandardTemplate.addView(R.id.media_actions, generateMediaActionButton(context, list.get(array[i])));
            }
        }
        if (b2) {
            applyStandardTemplate.setViewVisibility(R.id.end_padder, 8);
            applyStandardTemplate.setViewVisibility(R.id.cancel_action, 0);
            applyStandardTemplate.setOnClickPendingIntent(R.id.cancel_action, pendingIntent);
            applyStandardTemplate.setInt(R.id.cancel_action, "setAlpha", context.getResources().getInteger(R.integer.cancel_button_image_alpha));
            return applyStandardTemplate;
        }
        applyStandardTemplate.setViewVisibility(R.id.end_padder, 0);
        applyStandardTemplate.setViewVisibility(R.id.cancel_action, 8);
        return applyStandardTemplate;
    }
    
    @RequiresApi(11)
    private static RemoteViews generateMediaActionButton(final Context context, final NotificationCompatBase.Action action) {
        int n;
        if (action.getActionIntent() == null) {
            n = 1;
        }
        else {
            n = 0;
        }
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_media_action);
        remoteViews.setImageViewResource(R.id.action0, action.getIcon());
        if (n == 0) {
            remoteViews.setOnClickPendingIntent(R.id.action0, action.getActionIntent());
        }
        if (Build$VERSION.SDK_INT >= 15) {
            remoteViews.setContentDescription(R.id.action0, action.getTitle());
        }
        return remoteViews;
    }
    
    @RequiresApi(11)
    public static <T extends NotificationCompatBase.Action> RemoteViews generateMediaBigView(final Context context, final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3, int i, final Bitmap bitmap, final CharSequence charSequence4, final boolean b, final long n, final int n2, final int n3, final List<T> list, final boolean b2, final PendingIntent pendingIntent, final boolean b3) {
        final int min = Math.min(list.size(), 5);
        final RemoteViews applyStandardTemplate = applyStandardTemplate(context, charSequence, charSequence2, charSequence3, i, 0, bitmap, charSequence4, b, n, n2, n3, getBigMediaLayoutResource(b3, min), false);
        applyStandardTemplate.removeAllViews(R.id.media_actions);
        if (min > 0) {
            for (i = 0; i < min; ++i) {
                applyStandardTemplate.addView(R.id.media_actions, generateMediaActionButton(context, list.get(i)));
            }
        }
        if (b2) {
            applyStandardTemplate.setViewVisibility(R.id.cancel_action, 0);
            applyStandardTemplate.setInt(R.id.cancel_action, "setAlpha", context.getResources().getInteger(R.integer.cancel_button_image_alpha));
            applyStandardTemplate.setOnClickPendingIntent(R.id.cancel_action, pendingIntent);
            return applyStandardTemplate;
        }
        applyStandardTemplate.setViewVisibility(R.id.cancel_action, 8);
        return applyStandardTemplate;
    }
    
    private static int getActionLayoutResource() {
        return R.layout.notification_action;
    }
    
    private static int getActionTombstoneLayoutResource() {
        return R.layout.notification_action_tombstone;
    }
    
    @RequiresApi(11)
    private static int getBigMediaLayoutResource(final boolean b, final int n) {
        if (n <= 3) {
            if (b) {
                return R.layout.notification_template_big_media_narrow_custom;
            }
            return R.layout.notification_template_big_media_narrow;
        }
        else {
            if (b) {
                return R.layout.notification_template_big_media_custom;
            }
            return R.layout.notification_template_big_media;
        }
    }
    
    private static void hideNormalContent(final RemoteViews remoteViews) {
        remoteViews.setViewVisibility(R.id.title, 8);
        remoteViews.setViewVisibility(R.id.text2, 8);
        remoteViews.setViewVisibility(R.id.text, 8);
    }
    
    @RequiresApi(11)
    public static <T extends NotificationCompatBase.Action> RemoteViews overrideContentViewMedia(final NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, final Context context, final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3, final int n, final Bitmap bitmap, final CharSequence charSequence4, final boolean b, final long n2, final int n3, final List<T> list, final int[] array, final boolean b2, final PendingIntent pendingIntent, final boolean b3) {
        final RemoteViews generateContentViewMedia = generateContentViewMedia(context, charSequence, charSequence2, charSequence3, n, bitmap, charSequence4, b, n2, n3, list, array, b2, pendingIntent, b3);
        notificationBuilderWithBuilderAccessor.getBuilder().setContent(generateContentViewMedia);
        if (b2) {
            notificationBuilderWithBuilderAccessor.getBuilder().setOngoing(true);
        }
        return generateContentViewMedia;
    }
    
    @RequiresApi(16)
    public static <T extends NotificationCompatBase.Action> void overrideMediaBigContentView(final Notification notification, final Context context, final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3, final int n, final Bitmap bitmap, final CharSequence charSequence4, final boolean b, final long n2, final int n3, final int n4, final List<T> list, final boolean b2, final PendingIntent pendingIntent, final boolean b3) {
        notification.bigContentView = generateMediaBigView(context, charSequence, charSequence2, charSequence3, n, bitmap, charSequence4, b, n2, n3, n4, list, b2, pendingIntent, b3);
        if (b2) {
            notification.flags |= 0x2;
        }
    }
}
