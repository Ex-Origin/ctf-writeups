// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.v4.graphics.ColorUtils;
import android.graphics.Color;
import android.util.AttributeSet;
import android.content.Context;
import android.content.res.ColorStateList;
import android.util.TypedValue;

class ThemeUtils
{
    static final int[] ACTIVATED_STATE_SET;
    static final int[] CHECKED_STATE_SET;
    static final int[] DISABLED_STATE_SET;
    static final int[] EMPTY_STATE_SET;
    static final int[] FOCUSED_STATE_SET;
    static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET;
    static final int[] PRESSED_STATE_SET;
    static final int[] SELECTED_STATE_SET;
    private static final int[] TEMP_ARRAY;
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE;
    
    static {
        TL_TYPED_VALUE = new ThreadLocal<TypedValue>();
        DISABLED_STATE_SET = new int[] { -16842910 };
        FOCUSED_STATE_SET = new int[] { 16842908 };
        ACTIVATED_STATE_SET = new int[] { 16843518 };
        PRESSED_STATE_SET = new int[] { 16842919 };
        CHECKED_STATE_SET = new int[] { 16842912 };
        SELECTED_STATE_SET = new int[] { 16842913 };
        NOT_PRESSED_OR_FOCUSED_STATE_SET = new int[] { -16842919, -16842908 };
        EMPTY_STATE_SET = new int[0];
        TEMP_ARRAY = new int[1];
    }
    
    public static ColorStateList createDisabledStateList(final int n, int n2) {
        final int[][] array = new int[2][];
        final int[] array2 = new int[2];
        array[0] = ThemeUtils.DISABLED_STATE_SET;
        array2[0] = n2;
        n2 = 0 + 1;
        array[n2] = ThemeUtils.EMPTY_STATE_SET;
        array2[n2] = n;
        return new ColorStateList(array, array2);
    }
    
    public static int getDisabledThemeAttrColor(final Context context, final int n) {
        final ColorStateList themeAttrColorStateList = getThemeAttrColorStateList(context, n);
        if (themeAttrColorStateList != null && themeAttrColorStateList.isStateful()) {
            return themeAttrColorStateList.getColorForState(ThemeUtils.DISABLED_STATE_SET, themeAttrColorStateList.getDefaultColor());
        }
        final TypedValue typedValue = getTypedValue();
        context.getTheme().resolveAttribute(16842803, typedValue, true);
        return getThemeAttrColor(context, n, typedValue.getFloat());
    }
    
    public static int getThemeAttrColor(Context obtainStyledAttributes, int color) {
        ThemeUtils.TEMP_ARRAY[0] = color;
        obtainStyledAttributes = (Context)TintTypedArray.obtainStyledAttributes(obtainStyledAttributes, null, ThemeUtils.TEMP_ARRAY);
        try {
            color = ((TintTypedArray)obtainStyledAttributes).getColor(0, 0);
            return color;
        }
        finally {
            ((TintTypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    static int getThemeAttrColor(final Context context, int themeAttrColor, final float n) {
        themeAttrColor = getThemeAttrColor(context, themeAttrColor);
        return ColorUtils.setAlphaComponent(themeAttrColor, Math.round(Color.alpha(themeAttrColor) * n));
    }
    
    public static ColorStateList getThemeAttrColorStateList(Context obtainStyledAttributes, final int n) {
        ThemeUtils.TEMP_ARRAY[0] = n;
        obtainStyledAttributes = (Context)TintTypedArray.obtainStyledAttributes(obtainStyledAttributes, null, ThemeUtils.TEMP_ARRAY);
        try {
            return ((TintTypedArray)obtainStyledAttributes).getColorStateList(0);
        }
        finally {
            ((TintTypedArray)obtainStyledAttributes).recycle();
        }
    }
    
    private static TypedValue getTypedValue() {
        TypedValue typedValue;
        if ((typedValue = ThemeUtils.TL_TYPED_VALUE.get()) == null) {
            typedValue = new TypedValue();
            ThemeUtils.TL_TYPED_VALUE.set(typedValue);
        }
        return typedValue;
    }
}
