// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics.drawable;

import android.graphics.drawable.DrawableContainer$DrawableContainerState;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.InsetDrawable;
import android.util.Log;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.content.res.Resources$Theme;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;

public final class DrawableCompat
{
    static final DrawableCompatBaseImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 23) {
            IMPL = (DrawableCompatBaseImpl)new DrawableCompatApi23Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (DrawableCompatBaseImpl)new DrawableCompatApi21Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (DrawableCompatBaseImpl)new DrawableCompatApi19Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 17) {
            IMPL = (DrawableCompatBaseImpl)new DrawableCompatApi17Impl();
            return;
        }
        IMPL = new DrawableCompatBaseImpl();
    }
    
    public static void applyTheme(@NonNull final Drawable drawable, @NonNull final Resources$Theme resources$Theme) {
        DrawableCompat.IMPL.applyTheme(drawable, resources$Theme);
    }
    
    public static boolean canApplyTheme(@NonNull final Drawable drawable) {
        return DrawableCompat.IMPL.canApplyTheme(drawable);
    }
    
    public static void clearColorFilter(@NonNull final Drawable drawable) {
        DrawableCompat.IMPL.clearColorFilter(drawable);
    }
    
    public static int getAlpha(@NonNull final Drawable drawable) {
        return DrawableCompat.IMPL.getAlpha(drawable);
    }
    
    public static ColorFilter getColorFilter(@NonNull final Drawable drawable) {
        return DrawableCompat.IMPL.getColorFilter(drawable);
    }
    
    public static int getLayoutDirection(@NonNull final Drawable drawable) {
        return DrawableCompat.IMPL.getLayoutDirection(drawable);
    }
    
    public static void inflate(@NonNull final Drawable drawable, @NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        DrawableCompat.IMPL.inflate(drawable, resources, xmlPullParser, set, resources$Theme);
    }
    
    public static boolean isAutoMirrored(@NonNull final Drawable drawable) {
        return DrawableCompat.IMPL.isAutoMirrored(drawable);
    }
    
    public static void jumpToCurrentState(@NonNull final Drawable drawable) {
        DrawableCompat.IMPL.jumpToCurrentState(drawable);
    }
    
    public static void setAutoMirrored(@NonNull final Drawable drawable, final boolean b) {
        DrawableCompat.IMPL.setAutoMirrored(drawable, b);
    }
    
    public static void setHotspot(@NonNull final Drawable drawable, final float n, final float n2) {
        DrawableCompat.IMPL.setHotspot(drawable, n, n2);
    }
    
    public static void setHotspotBounds(@NonNull final Drawable drawable, final int n, final int n2, final int n3, final int n4) {
        DrawableCompat.IMPL.setHotspotBounds(drawable, n, n2, n3, n4);
    }
    
    public static boolean setLayoutDirection(@NonNull final Drawable drawable, final int n) {
        return DrawableCompat.IMPL.setLayoutDirection(drawable, n);
    }
    
    public static void setTint(@NonNull final Drawable drawable, @ColorInt final int n) {
        DrawableCompat.IMPL.setTint(drawable, n);
    }
    
    public static void setTintList(@NonNull final Drawable drawable, @Nullable final ColorStateList list) {
        DrawableCompat.IMPL.setTintList(drawable, list);
    }
    
    public static void setTintMode(@NonNull final Drawable drawable, @Nullable final PorterDuff$Mode porterDuff$Mode) {
        DrawableCompat.IMPL.setTintMode(drawable, porterDuff$Mode);
    }
    
    public static <T extends Drawable> T unwrap(@NonNull final Drawable drawable) {
        Drawable wrappedDrawable = drawable;
        if (drawable instanceof DrawableWrapper) {
            wrappedDrawable = ((DrawableWrapper)drawable).getWrappedDrawable();
        }
        return (T)wrappedDrawable;
    }
    
    public static Drawable wrap(@NonNull final Drawable drawable) {
        return DrawableCompat.IMPL.wrap(drawable);
    }
    
    @RequiresApi(17)
    static class DrawableCompatApi17Impl extends DrawableCompatBaseImpl
    {
        private static final String TAG = "DrawableCompatApi17";
        private static Method sGetLayoutDirectionMethod;
        private static boolean sGetLayoutDirectionMethodFetched;
        private static Method sSetLayoutDirectionMethod;
        private static boolean sSetLayoutDirectionMethodFetched;
        
        @Override
        public int getLayoutDirection(final Drawable drawable) {
            while (true) {
                if (!DrawableCompatApi17Impl.sGetLayoutDirectionMethodFetched) {
                    while (true) {
                        try {
                            (DrawableCompatApi17Impl.sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection", (Class<?>[])new Class[0])).setAccessible(true);
                            DrawableCompatApi17Impl.sGetLayoutDirectionMethodFetched = true;
                            if (DrawableCompatApi17Impl.sGetLayoutDirectionMethod != null) {
                                final Method method = DrawableCompatApi17Impl.sGetLayoutDirectionMethod;
                                final Drawable drawable2 = drawable;
                                final int n = 0;
                                final Object[] array = new Object[n];
                                final Object o = method.invoke(drawable2, array);
                                final Integer n2 = (Integer)o;
                                final int intValue = n2;
                                return intValue;
                            }
                            return 0;
                        }
                        catch (NoSuchMethodException ex) {
                            Log.i("DrawableCompatApi17", "Failed to retrieve getLayoutDirection() method", (Throwable)ex);
                            continue;
                        }
                        break;
                    }
                    try {
                        final Method method = DrawableCompatApi17Impl.sGetLayoutDirectionMethod;
                        final Drawable drawable2 = drawable;
                        final int n = 0;
                        final Object[] array = new Object[n];
                        final Object o = method.invoke(drawable2, array);
                        final Integer n2 = (Integer)o;
                        final int intValue2;
                        final int intValue = intValue2 = n2;
                        return intValue2;
                    }
                    catch (Exception ex2) {
                        Log.i("DrawableCompatApi17", "Failed to invoke getLayoutDirection() via reflection", (Throwable)ex2);
                        DrawableCompatApi17Impl.sGetLayoutDirectionMethod = null;
                    }
                    return 0;
                }
                continue;
            }
        }
        
        @Override
        public boolean setLayoutDirection(final Drawable drawable, final int n) {
            while (true) {
                if (!DrawableCompatApi17Impl.sSetLayoutDirectionMethodFetched) {
                    while (true) {
                        try {
                            (DrawableCompatApi17Impl.sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE)).setAccessible(true);
                            DrawableCompatApi17Impl.sSetLayoutDirectionMethodFetched = true;
                            if (DrawableCompatApi17Impl.sSetLayoutDirectionMethod != null) {
                                final Method method = DrawableCompatApi17Impl.sSetLayoutDirectionMethod;
                                final Drawable drawable2 = drawable;
                                final int n2 = 1;
                                final Object[] array = new Object[n2];
                                final int n3 = 0;
                                final int n4 = n;
                                final Integer n5 = n4;
                                array[n3] = n5;
                                method.invoke(drawable2, array);
                                return true;
                            }
                            return false;
                        }
                        catch (NoSuchMethodException ex) {
                            Log.i("DrawableCompatApi17", "Failed to retrieve setLayoutDirection(int) method", (Throwable)ex);
                            continue;
                        }
                        break;
                    }
                    try {
                        final Method method = DrawableCompatApi17Impl.sSetLayoutDirectionMethod;
                        final Drawable drawable2 = drawable;
                        final int n2 = 1;
                        final Object[] array = new Object[n2];
                        final int n3 = 0;
                        final int n4 = n;
                        final Integer n5 = n4;
                        array[n3] = n5;
                        method.invoke(drawable2, array);
                        return true;
                    }
                    catch (Exception ex2) {
                        Log.i("DrawableCompatApi17", "Failed to invoke setLayoutDirection(int) via reflection", (Throwable)ex2);
                        DrawableCompatApi17Impl.sSetLayoutDirectionMethod = null;
                    }
                    return false;
                }
                continue;
            }
        }
    }
    
    @RequiresApi(19)
    static class DrawableCompatApi19Impl extends DrawableCompatApi17Impl
    {
        @Override
        public int getAlpha(final Drawable drawable) {
            return drawable.getAlpha();
        }
        
        @Override
        public boolean isAutoMirrored(final Drawable drawable) {
            return drawable.isAutoMirrored();
        }
        
        @Override
        public void setAutoMirrored(final Drawable drawable, final boolean autoMirrored) {
            drawable.setAutoMirrored(autoMirrored);
        }
        
        @Override
        public Drawable wrap(final Drawable drawable) {
            Drawable drawable2 = drawable;
            if (!(drawable instanceof TintAwareDrawable)) {
                drawable2 = new DrawableWrapperApi19(drawable);
            }
            return drawable2;
        }
    }
    
    @RequiresApi(21)
    static class DrawableCompatApi21Impl extends DrawableCompatApi19Impl
    {
        @Override
        public void applyTheme(final Drawable drawable, final Resources$Theme resources$Theme) {
            drawable.applyTheme(resources$Theme);
        }
        
        @Override
        public boolean canApplyTheme(final Drawable drawable) {
            return drawable.canApplyTheme();
        }
        
        @Override
        public void clearColorFilter(final Drawable drawable) {
            drawable.clearColorFilter();
            if (drawable instanceof InsetDrawable) {
                this.clearColorFilter(((InsetDrawable)drawable).getDrawable());
            }
            else {
                if (drawable instanceof DrawableWrapper) {
                    this.clearColorFilter(((DrawableWrapper)drawable).getWrappedDrawable());
                    return;
                }
                if (drawable instanceof DrawableContainer) {
                    final DrawableContainer$DrawableContainerState drawableContainer$DrawableContainerState = (DrawableContainer$DrawableContainerState)((DrawableContainer)drawable).getConstantState();
                    if (drawableContainer$DrawableContainerState != null) {
                        for (int i = 0; i < drawableContainer$DrawableContainerState.getChildCount(); ++i) {
                            final Drawable child = drawableContainer$DrawableContainerState.getChild(i);
                            if (child != null) {
                                this.clearColorFilter(child);
                            }
                        }
                    }
                }
            }
        }
        
        @Override
        public ColorFilter getColorFilter(final Drawable drawable) {
            return drawable.getColorFilter();
        }
        
        @Override
        public void inflate(final Drawable drawable, final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws IOException, XmlPullParserException {
            drawable.inflate(resources, xmlPullParser, set, resources$Theme);
        }
        
        @Override
        public void setHotspot(final Drawable drawable, final float n, final float n2) {
            drawable.setHotspot(n, n2);
        }
        
        @Override
        public void setHotspotBounds(final Drawable drawable, final int n, final int n2, final int n3, final int n4) {
            drawable.setHotspotBounds(n, n2, n3, n4);
        }
        
        @Override
        public void setTint(final Drawable drawable, final int tint) {
            drawable.setTint(tint);
        }
        
        @Override
        public void setTintList(final Drawable drawable, final ColorStateList tintList) {
            drawable.setTintList(tintList);
        }
        
        @Override
        public void setTintMode(final Drawable drawable, final PorterDuff$Mode tintMode) {
            drawable.setTintMode(tintMode);
        }
        
        @Override
        public Drawable wrap(final Drawable drawable) {
            Drawable drawable2 = drawable;
            if (!(drawable instanceof TintAwareDrawable)) {
                drawable2 = new DrawableWrapperApi21(drawable);
            }
            return drawable2;
        }
    }
    
    @RequiresApi(23)
    static class DrawableCompatApi23Impl extends DrawableCompatApi21Impl
    {
        @Override
        public void clearColorFilter(final Drawable drawable) {
            drawable.clearColorFilter();
        }
        
        @Override
        public int getLayoutDirection(final Drawable drawable) {
            return drawable.getLayoutDirection();
        }
        
        @Override
        public boolean setLayoutDirection(final Drawable drawable, final int layoutDirection) {
            return drawable.setLayoutDirection(layoutDirection);
        }
        
        @Override
        public Drawable wrap(final Drawable drawable) {
            return drawable;
        }
    }
    
    static class DrawableCompatBaseImpl
    {
        public void applyTheme(final Drawable drawable, final Resources$Theme resources$Theme) {
        }
        
        public boolean canApplyTheme(final Drawable drawable) {
            return false;
        }
        
        public void clearColorFilter(final Drawable drawable) {
            drawable.clearColorFilter();
        }
        
        public int getAlpha(final Drawable drawable) {
            return 0;
        }
        
        public ColorFilter getColorFilter(final Drawable drawable) {
            return null;
        }
        
        public int getLayoutDirection(final Drawable drawable) {
            return 0;
        }
        
        public void inflate(final Drawable drawable, final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws IOException, XmlPullParserException {
            drawable.inflate(resources, xmlPullParser, set);
        }
        
        public boolean isAutoMirrored(final Drawable drawable) {
            return false;
        }
        
        public void jumpToCurrentState(final Drawable drawable) {
            drawable.jumpToCurrentState();
        }
        
        public void setAutoMirrored(final Drawable drawable, final boolean b) {
        }
        
        public void setHotspot(final Drawable drawable, final float n, final float n2) {
        }
        
        public void setHotspotBounds(final Drawable drawable, final int n, final int n2, final int n3, final int n4) {
        }
        
        public boolean setLayoutDirection(final Drawable drawable, final int n) {
            return false;
        }
        
        public void setTint(final Drawable drawable, final int tint) {
            if (drawable instanceof TintAwareDrawable) {
                ((TintAwareDrawable)drawable).setTint(tint);
            }
        }
        
        public void setTintList(final Drawable drawable, final ColorStateList tintList) {
            if (drawable instanceof TintAwareDrawable) {
                ((TintAwareDrawable)drawable).setTintList(tintList);
            }
        }
        
        public void setTintMode(final Drawable drawable, final PorterDuff$Mode tintMode) {
            if (drawable instanceof TintAwareDrawable) {
                ((TintAwareDrawable)drawable).setTintMode(tintMode);
            }
        }
        
        public Drawable wrap(final Drawable drawable) {
            Drawable drawable2 = drawable;
            if (!(drawable instanceof TintAwareDrawable)) {
                drawable2 = new DrawableWrapperApi14(drawable);
            }
            return drawable2;
        }
    }
}
