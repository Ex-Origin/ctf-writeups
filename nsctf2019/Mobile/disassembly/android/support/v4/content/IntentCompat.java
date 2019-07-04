// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content;

import android.support.annotation.RequiresApi;
import android.content.Intent;
import android.content.ComponentName;
import android.os.Build$VERSION;

public final class IntentCompat
{
    @Deprecated
    public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
    @Deprecated
    public static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
    public static final String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
    @Deprecated
    public static final String EXTRA_CHANGED_PACKAGE_LIST = "android.intent.extra.changed_package_list";
    @Deprecated
    public static final String EXTRA_CHANGED_UID_LIST = "android.intent.extra.changed_uid_list";
    public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
    public static final String EXTRA_START_PLAYBACK = "android.intent.extra.START_PLAYBACK";
    @Deprecated
    public static final int FLAG_ACTIVITY_CLEAR_TASK = 32768;
    @Deprecated
    public static final int FLAG_ACTIVITY_TASK_ON_HOME = 16384;
    private static final IntentCompatBaseImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 15) {
            IMPL = (IntentCompatBaseImpl)new IntentCompatApi15Impl();
            return;
        }
        IMPL = new IntentCompatBaseImpl();
    }
    
    @Deprecated
    public static Intent makeMainActivity(final ComponentName componentName) {
        return Intent.makeMainActivity(componentName);
    }
    
    public static Intent makeMainSelectorActivity(final String s, final String s2) {
        return IntentCompat.IMPL.makeMainSelectorActivity(s, s2);
    }
    
    @Deprecated
    public static Intent makeRestartActivityTask(final ComponentName componentName) {
        return Intent.makeRestartActivityTask(componentName);
    }
    
    @RequiresApi(15)
    static class IntentCompatApi15Impl extends IntentCompatBaseImpl
    {
        @Override
        public Intent makeMainSelectorActivity(final String s, final String s2) {
            return Intent.makeMainSelectorActivity(s, s2);
        }
    }
    
    static class IntentCompatBaseImpl
    {
        public Intent makeMainSelectorActivity(final String s, final String s2) {
            final Intent intent = new Intent(s);
            intent.addCategory(s2);
            return intent;
        }
    }
}
