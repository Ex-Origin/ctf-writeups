// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content.res;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import android.util.TypedValue;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.os.BuildCompat;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.content.res.Resources$NotFoundException;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import android.content.res.Resources$Theme;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.content.res.Resources;

public final class ResourcesCompat
{
    private static final String TAG = "ResourcesCompat";
    
    @ColorInt
    public static int getColor(@NonNull final Resources resources, @ColorRes final int n, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 23) {
            return resources.getColor(n, resources$Theme);
        }
        return resources.getColor(n);
    }
    
    @Nullable
    public static ColorStateList getColorStateList(@NonNull final Resources resources, @ColorRes final int n, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(n, resources$Theme);
        }
        return resources.getColorStateList(n);
    }
    
    @Nullable
    public static Drawable getDrawable(@NonNull final Resources resources, @DrawableRes final int n, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return resources.getDrawable(n, resources$Theme);
        }
        return resources.getDrawable(n);
    }
    
    @Nullable
    public static Drawable getDrawableForDensity(@NonNull final Resources resources, @DrawableRes final int n, final int n2, @Nullable final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(n, n2, resources$Theme);
        }
        if (Build$VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(n, n2);
        }
        return resources.getDrawable(n);
    }
    
    @Nullable
    public static Typeface getFont(@NonNull final Context context, @FontRes final int n) throws Resources$NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        if (BuildCompat.isAtLeastO()) {
            return context.getResources().getFont(n);
        }
        return loadFont(context, n, 0).getTypeface();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static TypefaceCompat.TypefaceHolder getFont(@NonNull final Context context, @FontRes int n, final int n2) throws Resources$NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        if (BuildCompat.isAtLeastO()) {
            final Typeface create = Typeface.create(context.getResources().getFont(n), n2);
            if ((n2 & 0x1) == 0x0) {
                n = 400;
            }
            else {
                n = 700;
            }
            return new TypefaceCompat.TypefaceHolder(create, n, (n2 & 0x2) != 0x0);
        }
        return loadFont(context, n, n2);
    }
    
    private static TypefaceCompat.TypefaceHolder loadFont(@NonNull final Context context, final int n, final int n2) {
        final TypedValue typedValue = new TypedValue();
        final Resources resources = context.getResources();
        resources.getValue(n, typedValue, true);
        final TypefaceCompat.TypefaceHolder loadFont = loadFont(context, resources, typedValue, n, n2);
        if (loadFont != null) {
            return loadFont;
        }
        throw new Resources$NotFoundException("Font resource ID #0x" + Integer.toHexString(n));
    }
    
    private static TypefaceCompat.TypefaceHolder loadFont(@NonNull final Context context, final Resources resources, TypedValue string, final int n, final int n2) {
        if (string.string == null) {
            throw new Resources$NotFoundException("Resource \"" + resources.getResourceName(n) + "\" (" + Integer.toHexString(n) + ") is not a Font: " + string);
        }
        final TypefaceCompat.TypefaceHolder fromCache = TypefaceCompat.findFromCache(resources, n, n2);
        if (fromCache != null) {
            return fromCache;
        }
        string = (TypedValue)string.string.toString();
        try {
            if (!((String)string).toLowerCase().endsWith(".xml")) {
                return TypefaceCompat.createFromResourcesFontFile(context, resources, n, n2);
            }
            final FontResourcesParserCompat.FamilyResourceEntry parse = FontResourcesParserCompat.parse((XmlPullParser)resources.getXml(n), resources);
            if (parse == null) {
                Log.e("ResourcesCompat", "Failed to find font-family tag");
                return null;
            }
            return TypefaceCompat.createFromResourcesFamilyXml(context, parse, resources, n, n2);
        }
        catch (XmlPullParserException ex) {
            Log.e("ResourcesCompat", "Failed to parse xml resource " + (String)string, (Throwable)ex);
        }
        catch (IOException ex2) {
            Log.e("ResourcesCompat", "Failed to read xml resource " + (String)string, (Throwable)ex2);
            goto Label_0173;
        }
    }
}
