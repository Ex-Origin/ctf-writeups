// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.text.method.TransformationMethod;
import android.support.v7.text.AllCapsTransformationMethod;
import android.support.annotation.RestrictTo;
import android.text.TextPaint;
import android.support.v4.graphics.TypefaceCompat;
import android.content.res.Resources$NotFoundException;
import android.text.method.PasswordTransformationMethod;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.graphics.drawable.Drawable;
import android.content.res.ColorStateList;
import android.content.Context;
import android.os.Build$VERSION;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@RequiresApi(9)
class AppCompatTextHelper
{
    @NonNull
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    final TextView mView;
    
    AppCompatTextHelper(final TextView mView) {
        this.mView = mView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
    }
    
    private void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }
    
    static AppCompatTextHelper create(final TextView textView) {
        if (Build$VERSION.SDK_INT >= 17) {
            return new AppCompatTextHelperV17(textView);
        }
        return new AppCompatTextHelper(textView);
    }
    
    protected static TintInfo createTintInfo(final Context context, final AppCompatDrawableManager appCompatDrawableManager, final int n) {
        final ColorStateList tintList = appCompatDrawableManager.getTintList(context, n);
        if (tintList != null) {
            final TintInfo tintInfo = new TintInfo();
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = tintList;
            return tintInfo;
        }
        return null;
    }
    
    private boolean getNeedsAutoSizeText() {
        return this.mAutoSizeTextHelper.getNeedsAutoSizeText();
    }
    
    private boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }
    
    private void setNeedsAutoSizeText(final boolean needsAutoSizeText) {
        this.mAutoSizeTextHelper.setNeedsAutoSizeText(needsAutoSizeText);
    }
    
    private void setTextSizeInternal(final int n, final float n2) {
        this.mAutoSizeTextHelper.setTextSizeInternal(n, n2);
    }
    
    private boolean shouldLoadFontResources(final Context context) {
        return !context.isRestricted();
    }
    
    final void applyCompoundDrawableTint(final Drawable drawable, final TintInfo tintInfo) {
        if (drawable != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
        }
    }
    
    void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            final Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            this.applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            this.applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            this.applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            this.applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
    }
    
    int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }
    
    int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }
    
    int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }
    
    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }
    
    int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }
    
    void loadFromAttributes(AttributeSet autoSizeTextAvailableSizes, final int n) {
        final Context context = this.mView.getContext();
        final AppCompatDrawableManager value = AppCompatDrawableManager.get();
        final boolean shouldLoadFontResources = this.shouldLoadFontResources(context);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, autoSizeTextAvailableSizes, R.styleable.AppCompatTextHelper, n, 0);
        final int resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        obtainStyledAttributes.recycle();
        final boolean b = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        boolean boolean1 = false;
        final boolean b2 = false;
        int int1 = 0;
        final int n2 = 0;
        final ColorStateList list = null;
        Object colorStateList = null;
        Object obtainStyledAttributes2 = null;
        Object textColor = null;
        Object colorStateList2 = null;
        Object colorStateList3 = null;
        ColorStateList colorStateList4 = null;
        ColorStateList colorStateList5 = null;
        Object font = null;
        final TypefaceCompat.TypefaceHolder typefaceHolder = null;
        int n3 = 0;
        int int2 = 0;
        Label_0470: {
            if (resourceId == -1) {
                break Label_0470;
            }
            final TintTypedArray obtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(context, resourceId, R.styleable.TextAppearance);
            boolean1 = b2;
            int1 = n2;
            if (!b) {
                boolean1 = b2;
                int1 = n2;
                if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                    int1 = 1;
                    boolean1 = obtainStyledAttributes3.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                }
            }
            font = typefaceHolder;
            n3 = int2;
            while (true) {
                if (!shouldLoadFontResources) {
                    break Label_0358;
                }
                int2 = obtainStyledAttributes3.getInt(R.styleable.TextAppearance_android_textStyle, 0);
                font = typefaceHolder;
                n3 = int2;
                if (!obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_fontFamily)) {
                    break Label_0358;
                }
                try {
                    font = obtainStyledAttributes3.getFont(R.styleable.TextAppearance_android_fontFamily, int2);
                    n3 = int2;
                    colorStateList = list;
                    colorStateList4 = colorStateList5;
                    if (Build$VERSION.SDK_INT < 23) {
                        Object colorStateList6 = obtainStyledAttributes2;
                        if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColor)) {
                            colorStateList6 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColor);
                        }
                        if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                            colorStateList3 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                        }
                        colorStateList = colorStateList6;
                        textColor = colorStateList3;
                        colorStateList4 = colorStateList5;
                        if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                            colorStateList4 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                            textColor = colorStateList3;
                            colorStateList = colorStateList6;
                        }
                    }
                    obtainStyledAttributes3.recycle();
                    colorStateList2 = textColor;
                    obtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(context, autoSizeTextAvailableSizes, R.styleable.TextAppearance, n, 0);
                    boolean boolean2 = boolean1;
                    int2 = int1;
                    if (!b) {
                        boolean2 = boolean1;
                        int2 = int1;
                        if (((TintTypedArray)obtainStyledAttributes2).hasValue(R.styleable.TextAppearance_textAllCaps)) {
                            int2 = 1;
                            boolean2 = ((TintTypedArray)obtainStyledAttributes2).getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                        }
                    }
                    textColor = colorStateList;
                    colorStateList3 = colorStateList2;
                    colorStateList5 = colorStateList4;
                    if (Build$VERSION.SDK_INT < 23) {
                        if (((TintTypedArray)obtainStyledAttributes2).hasValue(R.styleable.TextAppearance_android_textColor)) {
                            colorStateList = ((TintTypedArray)obtainStyledAttributes2).getColorStateList(R.styleable.TextAppearance_android_textColor);
                        }
                        if (((TintTypedArray)obtainStyledAttributes2).hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                            colorStateList2 = ((TintTypedArray)obtainStyledAttributes2).getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                        }
                        textColor = colorStateList;
                        colorStateList3 = colorStateList2;
                        colorStateList5 = colorStateList4;
                        if (((TintTypedArray)obtainStyledAttributes2).hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                            colorStateList5 = ((TintTypedArray)obtainStyledAttributes2).getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                            colorStateList3 = colorStateList2;
                            textColor = colorStateList;
                        }
                    }
                    Object font2 = font;
                    int1 = n3;
                    Label_0687: {
                        if (!shouldLoadFontResources) {
                            break Label_0687;
                        }
                        font2 = font;
                        int1 = n3;
                        if (!((TintTypedArray)obtainStyledAttributes2).hasValue(R.styleable.TextAppearance_android_fontFamily)) {
                            break Label_0687;
                        }
                        int1 = ((TintTypedArray)obtainStyledAttributes2).getInt(R.styleable.TextAppearance_android_textStyle, 0);
                        try {
                            font2 = ((TintTypedArray)obtainStyledAttributes2).getFont(R.styleable.TextAppearance_android_fontFamily, int1);
                            ((TintTypedArray)obtainStyledAttributes2).recycle();
                            if (textColor != null) {
                                this.mView.setTextColor((ColorStateList)textColor);
                            }
                            if (colorStateList3 != null) {
                                this.mView.setHintTextColor((ColorStateList)colorStateList3);
                            }
                            if (colorStateList5 != null) {
                                this.mView.setLinkTextColor(colorStateList5);
                            }
                            if (!b && int2 != 0) {
                                this.setAllCaps(boolean2);
                            }
                            if (font2 != null) {
                                this.mView.setTypeface(((TypefaceCompat.TypefaceHolder)font2).getTypeface());
                                final TextPaint paint = this.mView.getPaint();
                                boolean1 = ((int1 & 0x1) != 0x0 && ((TypefaceCompat.TypefaceHolder)font2).getWeight() < 600);
                                paint.setFakeBoldText(boolean1);
                                int n4;
                                if ((int1 & 0x2) != 0x0 && !((TypefaceCompat.TypefaceHolder)font2).isItalic()) {
                                    n4 = 1;
                                }
                                else {
                                    n4 = 0;
                                }
                                float textSkewX;
                                if (n4 != 0) {
                                    textSkewX = -0.25f;
                                }
                                else {
                                    textSkewX = 0.0f;
                                }
                                paint.setTextSkewX(textSkewX);
                            }
                            this.mAutoSizeTextHelper.loadFromAttributes(autoSizeTextAvailableSizes, n);
                            if (Build$VERSION.SDK_INT >= 26 && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
                                autoSizeTextAvailableSizes = (AttributeSet)(Object)this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
                                if (autoSizeTextAvailableSizes.length > 0) {
                                    if (this.mView.getAutoSizeStepGranularity() == -1) {
                                        this.mView.setAutoSizeTextTypeUniformWithPresetSizes((int[])(Object)autoSizeTextAvailableSizes, 0);
                                        return;
                                    }
                                    this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
                                }
                            }
                        }
                        catch (UnsupportedOperationException ex) {
                            font2 = font;
                        }
                        catch (Resources$NotFoundException ex2) {
                            font2 = font;
                        }
                    }
                }
                catch (UnsupportedOperationException ex3) {
                    font = typefaceHolder;
                    n3 = int2;
                    continue;
                }
                catch (Resources$NotFoundException ex4) {
                    font = typefaceHolder;
                    n3 = int2;
                    continue;
                }
                break;
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.isAutoSizeEnabled()) {
            if (this.getNeedsAutoSizeText()) {
                this.autoSizeText();
            }
            this.setNeedsAutoSizeText(true);
        }
    }
    
    void onSetTextAppearance(final Context context, final int n) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(obtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if (Build$VERSION.SDK_INT < 23 && obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor)) {
            final ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor);
            if (colorStateList != null) {
                this.mView.setTextColor(colorStateList);
            }
        }
        obtainStyledAttributes.recycle();
    }
    
    void setAllCaps(final boolean b) {
        final TextView mView = this.mView;
        Object transformationMethod;
        if (b) {
            transformationMethod = new AllCapsTransformationMethod(this.mView.getContext());
        }
        else {
            transformationMethod = null;
        }
        mView.setTransformationMethod((TransformationMethod)transformationMethod);
    }
    
    void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
    }
    
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final int[] array, final int n) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
    }
    
    void setAutoSizeTextTypeWithDefaults(final int autoSizeTextTypeWithDefaults) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(autoSizeTextTypeWithDefaults);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setTextSize(final int n, final float n2) {
        if (!this.isAutoSizeEnabled()) {
            this.setTextSizeInternal(n, n2);
        }
    }
}
