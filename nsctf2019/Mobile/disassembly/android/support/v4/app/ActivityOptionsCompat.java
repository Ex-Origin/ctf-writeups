// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.os.Bundle;
import android.app.PendingIntent;
import android.support.annotation.Nullable;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.support.v4.util.Pair;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.support.annotation.RequiresApi;
import android.os.Build$VERSION;
import android.app.ActivityOptions;

public class ActivityOptionsCompat
{
    public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
    public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";
    
    @RequiresApi(16)
    private static ActivityOptionsCompat createImpl(final ActivityOptions activityOptions) {
        if (Build$VERSION.SDK_INT >= 24) {
            return new ActivityOptionsCompatApi24Impl(activityOptions);
        }
        if (Build$VERSION.SDK_INT >= 23) {
            return new ActivityOptionsCompatApi23Impl(activityOptions);
        }
        return new ActivityOptionsCompatApi16Impl(activityOptions);
    }
    
    public static ActivityOptionsCompat makeBasic() {
        if (Build$VERSION.SDK_INT >= 23) {
            return createImpl(ActivityOptions.makeBasic());
        }
        return new ActivityOptionsCompat();
    }
    
    public static ActivityOptionsCompat makeClipRevealAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 23) {
            return createImpl(ActivityOptions.makeClipRevealAnimation(view, n, n2, n3, n4));
        }
        return new ActivityOptionsCompat();
    }
    
    public static ActivityOptionsCompat makeCustomAnimation(final Context context, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 16) {
            return createImpl(ActivityOptions.makeCustomAnimation(context, n, n2));
        }
        return new ActivityOptionsCompat();
    }
    
    public static ActivityOptionsCompat makeScaleUpAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
        if (Build$VERSION.SDK_INT >= 16) {
            return createImpl(ActivityOptions.makeScaleUpAnimation(view, n, n2, n3, n4));
        }
        return new ActivityOptionsCompat();
    }
    
    public static ActivityOptionsCompat makeSceneTransitionAnimation(final Activity activity, final View view, final String s) {
        if (Build$VERSION.SDK_INT >= 21) {
            return createImpl(ActivityOptions.makeSceneTransitionAnimation(activity, view, s));
        }
        return new ActivityOptionsCompat();
    }
    
    public static ActivityOptionsCompat makeSceneTransitionAnimation(final Activity activity, final Pair<View, String>... array) {
        if (Build$VERSION.SDK_INT >= 21) {
            android.util.Pair[] array2 = null;
            if (array != null) {
                final android.util.Pair[] array3 = new android.util.Pair[array.length];
                int n = 0;
                while (true) {
                    array2 = array3;
                    if (n >= array.length) {
                        break;
                    }
                    array3[n] = android.util.Pair.create((Object)array[n].first, (Object)array[n].second);
                    ++n;
                }
            }
            return createImpl(ActivityOptions.makeSceneTransitionAnimation(activity, array2));
        }
        return new ActivityOptionsCompat();
    }
    
    public static ActivityOptionsCompat makeTaskLaunchBehind() {
        if (Build$VERSION.SDK_INT >= 21) {
            return createImpl(ActivityOptions.makeTaskLaunchBehind());
        }
        return new ActivityOptionsCompat();
    }
    
    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(final View view, final Bitmap bitmap, final int n, final int n2) {
        if (Build$VERSION.SDK_INT >= 16) {
            return createImpl(ActivityOptions.makeThumbnailScaleUpAnimation(view, bitmap, n, n2));
        }
        return new ActivityOptionsCompat();
    }
    
    @Nullable
    public Rect getLaunchBounds() {
        return null;
    }
    
    public void requestUsageTimeReport(final PendingIntent pendingIntent) {
    }
    
    public ActivityOptionsCompat setLaunchBounds(@Nullable final Rect rect) {
        return null;
    }
    
    public Bundle toBundle() {
        return null;
    }
    
    public void update(final ActivityOptionsCompat activityOptionsCompat) {
    }
    
    @RequiresApi(16)
    private static class ActivityOptionsCompatApi16Impl extends ActivityOptionsCompat
    {
        protected final ActivityOptions mActivityOptions;
        
        ActivityOptionsCompatApi16Impl(final ActivityOptions mActivityOptions) {
            this.mActivityOptions = mActivityOptions;
        }
        
        @Override
        public Bundle toBundle() {
            return this.mActivityOptions.toBundle();
        }
        
        @Override
        public void update(final ActivityOptionsCompat activityOptionsCompat) {
            if (activityOptionsCompat instanceof ActivityOptionsCompatApi16Impl) {
                this.mActivityOptions.update(((ActivityOptionsCompatApi16Impl)activityOptionsCompat).mActivityOptions);
            }
        }
    }
    
    @RequiresApi(23)
    private static class ActivityOptionsCompatApi23Impl extends ActivityOptionsCompatApi16Impl
    {
        ActivityOptionsCompatApi23Impl(final ActivityOptions activityOptions) {
            super(activityOptions);
        }
        
        @Override
        public void requestUsageTimeReport(final PendingIntent pendingIntent) {
            this.mActivityOptions.requestUsageTimeReport(pendingIntent);
        }
    }
    
    @RequiresApi(24)
    private static class ActivityOptionsCompatApi24Impl extends ActivityOptionsCompatApi23Impl
    {
        ActivityOptionsCompatApi24Impl(final ActivityOptions activityOptions) {
            super(activityOptions);
        }
        
        @Override
        public Rect getLaunchBounds() {
            return this.mActivityOptions.getLaunchBounds();
        }
        
        @Override
        public ActivityOptionsCompat setLaunchBounds(@Nullable final Rect launchBounds) {
            return new ActivityOptionsCompatApi24Impl(this.mActivityOptions.setLaunchBounds(launchBounds));
        }
    }
}
