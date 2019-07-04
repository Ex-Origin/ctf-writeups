// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content.pm;

import android.os.Bundle;
import android.content.IntentSender$SendIntentException;
import android.os.Handler;
import android.content.IntentSender$OnFinished;
import android.content.BroadcastReceiver;
import android.support.annotation.Nullable;
import android.content.IntentSender;
import java.util.Iterator;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.support.v4.content.ContextCompat;
import android.content.pm.ShortcutManager;
import android.support.v4.os.BuildCompat;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

public class ShortcutManagerCompat
{
    @VisibleForTesting
    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    @VisibleForTesting
    static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";
    
    @NonNull
    public static Intent createShortcutResultIntent(@NonNull final Context context, @NonNull final ShortcutInfoCompat shortcutInfoCompat) {
        Intent shortcutResultIntent = null;
        if (BuildCompat.isAtLeastO()) {
            shortcutResultIntent = ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).createShortcutResultIntent(shortcutInfoCompat.toShortcutInfo());
        }
        Intent intent;
        if ((intent = shortcutResultIntent) == null) {
            intent = new Intent();
        }
        return shortcutInfoCompat.addToIntent(intent);
    }
    
    public static boolean isRequestPinShortcutSupported(@NonNull final Context context) {
        final boolean b = false;
        boolean requestPinShortcutSupported;
        if (BuildCompat.isAtLeastO()) {
            requestPinShortcutSupported = ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).isRequestPinShortcutSupported();
        }
        else {
            requestPinShortcutSupported = b;
            if (ContextCompat.checkSelfPermission(context, "com.android.launcher.permission.INSTALL_SHORTCUT") == 0) {
                final Iterator<ResolveInfo> iterator = (Iterator<ResolveInfo>)context.getPackageManager().queryBroadcastReceivers(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"), 0).iterator();
                String permission;
                do {
                    requestPinShortcutSupported = b;
                    if (!iterator.hasNext()) {
                        return requestPinShortcutSupported;
                    }
                    permission = iterator.next().activityInfo.permission;
                } while (!TextUtils.isEmpty((CharSequence)permission) && !"com.android.launcher.permission.INSTALL_SHORTCUT".equals(permission));
                return true;
            }
        }
        return requestPinShortcutSupported;
    }
    
    public static boolean requestPinShortcut(@NonNull final Context context, @NonNull final ShortcutInfoCompat shortcutInfoCompat, @Nullable final IntentSender intentSender) {
        if (BuildCompat.isAtLeastO()) {
            return ((ShortcutManager)context.getSystemService((Class)ShortcutManager.class)).requestPinShortcut(shortcutInfoCompat.toShortcutInfo(), intentSender);
        }
        if (!isRequestPinShortcutSupported(context)) {
            return false;
        }
        final Intent addToIntent = shortcutInfoCompat.addToIntent(new Intent("com.android.launcher.action.INSTALL_SHORTCUT"));
        if (intentSender == null) {
            context.sendBroadcast(addToIntent);
            return true;
        }
        context.sendOrderedBroadcast(addToIntent, (String)null, (BroadcastReceiver)new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                try {
                    intentSender.sendIntent(context, 0, (Intent)null, (IntentSender$OnFinished)null, (Handler)null);
                }
                catch (IntentSender$SendIntentException ex) {}
            }
        }, (Handler)null, -1, (String)null, (Bundle)null);
        return true;
    }
}
