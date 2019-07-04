// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.os.Build$VERSION;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.annotation.StyleableRes;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.content.res.ColorStateList;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class TintTypedArray
{
    private final Context mContext;
    private TypedValue mTypedValue;
    private final TypedArray mWrapped;
    
    private TintTypedArray(final Context mContext, final TypedArray mWrapped) {
        this.mContext = mContext;
        this.mWrapped = mWrapped;
    }
    
    public static TintTypedArray obtainStyledAttributes(final Context context, final int n, final int[] array) {
        return new TintTypedArray(context, context.obtainStyledAttributes(n, array));
    }
    
    public static TintTypedArray obtainStyledAttributes(final Context context, final AttributeSet set, final int[] array) {
        return new TintTypedArray(context, context.obtainStyledAttributes(set, array));
    }
    
    public static TintTypedArray obtainStyledAttributes(final Context context, final AttributeSet set, final int[] array, final int n, final int n2) {
        return new TintTypedArray(context, context.obtainStyledAttributes(set, array, n, n2));
    }
    
    public boolean getBoolean(final int n, final boolean b) {
        return this.mWrapped.getBoolean(n, b);
    }
    
    @RequiresApi(21)
    public int getChangingConfigurations() {
        return this.mWrapped.getChangingConfigurations();
    }
    
    public int getColor(final int n, final int n2) {
        return this.mWrapped.getColor(n, n2);
    }
    
    public ColorStateList getColorStateList(final int n) {
        if (this.mWrapped.hasValue(n)) {
            final int resourceId = this.mWrapped.getResourceId(n, 0);
            if (resourceId != 0) {
                final ColorStateList colorStateList = AppCompatResources.getColorStateList(this.mContext, resourceId);
                if (colorStateList != null) {
                    return colorStateList;
                }
            }
        }
        return this.mWrapped.getColorStateList(n);
    }
    
    public float getDimension(final int n, final float n2) {
        return this.mWrapped.getDimension(n, n2);
    }
    
    public int getDimensionPixelOffset(final int n, final int n2) {
        return this.mWrapped.getDimensionPixelOffset(n, n2);
    }
    
    public int getDimensionPixelSize(final int n, final int n2) {
        return this.mWrapped.getDimensionPixelSize(n, n2);
    }
    
    public Drawable getDrawable(final int n) {
        if (this.mWrapped.hasValue(n)) {
            final int resourceId = this.mWrapped.getResourceId(n, 0);
            if (resourceId != 0) {
                return AppCompatResources.getDrawable(this.mContext, resourceId);
            }
        }
        return this.mWrapped.getDrawable(n);
    }
    
    public Drawable getDrawableIfKnown(int resourceId) {
        if (this.mWrapped.hasValue(resourceId)) {
            resourceId = this.mWrapped.getResourceId(resourceId, 0);
            if (resourceId != 0) {
                return AppCompatDrawableManager.get().getDrawable(this.mContext, resourceId, true);
            }
        }
        return null;
    }
    
    public float getFloat(final int n, final float n2) {
        return this.mWrapped.getFloat(n, n2);
    }
    
    @Nullable
    public TypefaceCompat.TypefaceHolder getFont(@StyleableRes int resourceId, final int n) {
        if (this.mWrapped.hasValue(resourceId)) {
            resourceId = this.mWrapped.getResourceId(resourceId, 0);
            if (resourceId != 0) {
                return ResourcesCompat.getFont(this.mContext, resourceId, n);
            }
        }
        return null;
    }
    
    public float getFraction(final int n, final int n2, final int n3, final float n4) {
        return this.mWrapped.getFraction(n, n2, n3, n4);
    }
    
    public int getIndex(final int n) {
        return this.mWrapped.getIndex(n);
    }
    
    public int getIndexCount() {
        return this.mWrapped.getIndexCount();
    }
    
    public int getInt(final int n, final int n2) {
        return this.mWrapped.getInt(n, n2);
    }
    
    public int getInteger(final int n, final int n2) {
        return this.mWrapped.getInteger(n, n2);
    }
    
    public int getLayoutDimension(final int n, final int n2) {
        return this.mWrapped.getLayoutDimension(n, n2);
    }
    
    public int getLayoutDimension(final int n, final String s) {
        return this.mWrapped.getLayoutDimension(n, s);
    }
    
    public String getNonResourceString(final int n) {
        return this.mWrapped.getNonResourceString(n);
    }
    
    public String getPositionDescription() {
        return this.mWrapped.getPositionDescription();
    }
    
    public int getResourceId(final int n, final int n2) {
        return this.mWrapped.getResourceId(n, n2);
    }
    
    public Resources getResources() {
        return this.mWrapped.getResources();
    }
    
    public String getString(final int n) {
        return this.mWrapped.getString(n);
    }
    
    public CharSequence getText(final int n) {
        return this.mWrapped.getText(n);
    }
    
    public CharSequence[] getTextArray(final int n) {
        return this.mWrapped.getTextArray(n);
    }
    
    public int getType(final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            return this.mWrapped.getType(n);
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        this.mWrapped.getValue(n, this.mTypedValue);
        return this.mTypedValue.type;
    }
    
    public boolean getValue(final int n, final TypedValue typedValue) {
        return this.mWrapped.getValue(n, typedValue);
    }
    
    public boolean hasValue(final int n) {
        return this.mWrapped.hasValue(n);
    }
    
    public int length() {
        return this.mWrapped.length();
    }
    
    public TypedValue peekValue(final int n) {
        return this.mWrapped.peekValue(n);
    }
    
    public void recycle() {
        this.mWrapped.recycle();
    }
}
