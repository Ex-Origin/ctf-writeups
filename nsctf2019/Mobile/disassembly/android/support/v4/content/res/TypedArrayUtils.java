// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content.res;

import android.util.AttributeSet;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.support.annotation.AnyRes;
import android.support.annotation.ColorInt;
import org.xmlpull.v1.XmlPullParser;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.support.annotation.StyleableRes;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class TypedArrayUtils
{
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    
    public static int getAttr(final Context context, final int n, final int n2) {
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(n, typedValue, true);
        if (typedValue.resourceId != 0) {
            return n;
        }
        return n2;
    }
    
    public static boolean getBoolean(final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2, final boolean b) {
        return typedArray.getBoolean(n, typedArray.getBoolean(n2, b));
    }
    
    public static Drawable getDrawable(final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2) {
        Drawable drawable;
        if ((drawable = typedArray.getDrawable(n)) == null) {
            drawable = typedArray.getDrawable(n2);
        }
        return drawable;
    }
    
    public static int getInt(final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2, final int n3) {
        return typedArray.getInt(n, typedArray.getInt(n2, n3));
    }
    
    public static boolean getNamedBoolean(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, final String s, @StyleableRes final int n, final boolean b) {
        if (!hasAttribute(xmlPullParser, s)) {
            return b;
        }
        return typedArray.getBoolean(n, b);
    }
    
    @ColorInt
    public static int getNamedColor(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, final String s, @StyleableRes final int n, @ColorInt final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getColor(n, n2);
    }
    
    public static float getNamedFloat(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @NonNull final String s, @StyleableRes final int n, final float n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getFloat(n, n2);
    }
    
    public static int getNamedInt(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, final String s, @StyleableRes final int n, final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getInt(n, n2);
    }
    
    @AnyRes
    public static int getNamedResourceId(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, final String s, @StyleableRes final int n, @AnyRes final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getResourceId(n, n2);
    }
    
    public static String getNamedString(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, final String s, @StyleableRes final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        return typedArray.getString(n);
    }
    
    @AnyRes
    public static int getResourceId(final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2, @AnyRes final int n3) {
        return typedArray.getResourceId(n, typedArray.getResourceId(n2, n3));
    }
    
    public static String getString(final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2) {
        String s;
        if ((s = typedArray.getString(n)) == null) {
            s = typedArray.getString(n2);
        }
        return s;
    }
    
    public static CharSequence getText(final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2) {
        CharSequence charSequence;
        if ((charSequence = typedArray.getText(n)) == null) {
            charSequence = typedArray.getText(n2);
        }
        return charSequence;
    }
    
    public static CharSequence[] getTextArray(final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2) {
        CharSequence[] array;
        if ((array = typedArray.getTextArray(n)) == null) {
            array = typedArray.getTextArray(n2);
        }
        return array;
    }
    
    public static boolean hasAttribute(@NonNull final XmlPullParser xmlPullParser, @NonNull final String s) {
        return xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", s) != null;
    }
    
    public static TypedArray obtainAttributes(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final int[] array) {
        if (resources$Theme == null) {
            return resources.obtainAttributes(set, array);
        }
        return resources$Theme.obtainStyledAttributes(set, array, 0, 0);
    }
    
    public static TypedValue peekNamedValue(final TypedArray typedArray, final XmlPullParser xmlPullParser, final String s, final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        return typedArray.peekValue(n);
    }
}
