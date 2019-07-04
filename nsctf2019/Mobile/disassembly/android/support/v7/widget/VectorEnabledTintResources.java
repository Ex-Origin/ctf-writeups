// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.content.res.Resources$NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.support.v7.app.AppCompatDelegate;
import android.support.annotation.NonNull;
import android.content.Context;
import java.lang.ref.WeakReference;
import android.support.annotation.RestrictTo;
import android.content.res.Resources;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class VectorEnabledTintResources extends Resources
{
    public static final int MAX_SDK_WHERE_REQUIRED = 20;
    private final WeakReference<Context> mContextRef;
    
    public VectorEnabledTintResources(@NonNull final Context context, @NonNull final Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.mContextRef = new WeakReference<Context>(context);
    }
    
    public static boolean shouldBeUsed() {
        return AppCompatDelegate.isCompatVectorFromResourcesEnabled() && Build$VERSION.SDK_INT <= 20;
    }
    
    public Drawable getDrawable(final int n) throws Resources$NotFoundException {
        final Context context = this.mContextRef.get();
        if (context != null) {
            return AppCompatDrawableManager.get().onDrawableLoadedFromResources(context, this, n);
        }
        return super.getDrawable(n);
    }
    
    final Drawable superGetDrawable(final int n) {
        return super.getDrawable(n);
    }
}
