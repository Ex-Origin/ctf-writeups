// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.content.res.Resources$NotFoundException;
import android.graphics.drawable.Drawable;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.content.Context;
import java.lang.ref.WeakReference;

class TintResources extends ResourcesWrapper
{
    private final WeakReference<Context> mContextRef;
    
    public TintResources(@NonNull final Context context, @NonNull final Resources resources) {
        super(resources);
        this.mContextRef = new WeakReference<Context>(context);
    }
    
    @Override
    public Drawable getDrawable(final int n) throws Resources$NotFoundException {
        final Drawable drawable = super.getDrawable(n);
        final Context context = this.mContextRef.get();
        if (drawable != null && context != null) {
            AppCompatDrawableManager.get();
            AppCompatDrawableManager.tintDrawableUsingColorFilter(context, n, drawable);
        }
        return drawable;
    }
}
