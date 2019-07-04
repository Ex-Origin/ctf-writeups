// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.content.res;

import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.content.res.Resources;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import android.support.v7.widget.AppCompatDrawableManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.content.Context;
import java.util.WeakHashMap;
import android.util.TypedValue;

public final class AppCompatResources
{
    private static final String LOG_TAG = "AppCompatResources";
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE;
    private static final Object sColorStateCacheLock;
    private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches;
    
    static {
        TL_TYPED_VALUE = new ThreadLocal<TypedValue>();
        sColorStateCaches = new WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>>(0);
        sColorStateCacheLock = new Object();
    }
    
    private static void addColorStateListToCache(@NonNull final Context context, @ColorRes final int n, @NonNull final ColorStateList list) {
        synchronized (AppCompatResources.sColorStateCacheLock) {
            SparseArray sparseArray;
            if ((sparseArray = AppCompatResources.sColorStateCaches.get(context)) == null) {
                sparseArray = new SparseArray();
                AppCompatResources.sColorStateCaches.put(context, (SparseArray<ColorStateListCacheEntry>)sparseArray);
            }
            sparseArray.append(n, (Object)new ColorStateListCacheEntry(list, context.getResources().getConfiguration()));
        }
    }
    
    @Nullable
    private static ColorStateList getCachedColorStateList(@NonNull final Context context, @ColorRes final int n) {
        synchronized (AppCompatResources.sColorStateCacheLock) {
            final SparseArray<ColorStateListCacheEntry> sparseArray = AppCompatResources.sColorStateCaches.get(context);
            if (sparseArray != null && sparseArray.size() > 0) {
                final ColorStateListCacheEntry colorStateListCacheEntry = (ColorStateListCacheEntry)sparseArray.get(n);
                if (colorStateListCacheEntry != null) {
                    if (colorStateListCacheEntry.configuration.equals(context.getResources().getConfiguration())) {
                        return colorStateListCacheEntry.value;
                    }
                    sparseArray.remove(n);
                }
            }
            return null;
        }
    }
    
    public static ColorStateList getColorStateList(@NonNull final Context context, @ColorRes final int n) {
        ColorStateList list;
        if (Build$VERSION.SDK_INT >= 23) {
            list = context.getColorStateList(n);
        }
        else if ((list = getCachedColorStateList(context, n)) == null) {
            final ColorStateList inflateColorStateList = inflateColorStateList(context, n);
            if (inflateColorStateList != null) {
                addColorStateListToCache(context, n, inflateColorStateList);
                return inflateColorStateList;
            }
            return ContextCompat.getColorStateList(context, n);
        }
        return list;
    }
    
    @Nullable
    public static Drawable getDrawable(@NonNull final Context context, @DrawableRes final int n) {
        return AppCompatDrawableManager.get().getDrawable(context, n);
    }
    
    @NonNull
    private static TypedValue getTypedValue() {
        TypedValue typedValue;
        if ((typedValue = AppCompatResources.TL_TYPED_VALUE.get()) == null) {
            typedValue = new TypedValue();
            AppCompatResources.TL_TYPED_VALUE.set(typedValue);
        }
        return typedValue;
    }
    
    @Nullable
    private static ColorStateList inflateColorStateList(final Context context, final int n) {
        if (isColorInt(context, n)) {
            return null;
        }
        final Resources resources = context.getResources();
        final XmlResourceParser xml = resources.getXml(n);
        try {
            return AppCompatColorStateListInflater.createFromXml(resources, (XmlPullParser)xml, context.getTheme());
        }
        catch (Exception ex) {
            Log.e("AppCompatResources", "Failed to inflate ColorStateList, leaving it to the framework", (Throwable)ex);
            return null;
        }
    }
    
    private static boolean isColorInt(@NonNull final Context context, @ColorRes final int n) {
        final Resources resources = context.getResources();
        final TypedValue typedValue = getTypedValue();
        resources.getValue(n, typedValue, true);
        return typedValue.type >= 28 && typedValue.type <= 31;
    }
    
    private static class ColorStateListCacheEntry
    {
        final Configuration configuration;
        final ColorStateList value;
        
        ColorStateListCacheEntry(@NonNull final ColorStateList value, @NonNull final Configuration configuration) {
            this.value = value;
            this.configuration = configuration;
        }
    }
}
