// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.annotation.DrawableRes;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.support.annotation.RestrictTo;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.widget.AutoSizeableTextView;
import android.support.v4.view.TintableBackgroundView;
import android.widget.TextView;

public class AppCompatTextView extends TextView implements TintableBackgroundView, AutoSizeableTextView
{
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private final AppCompatTextHelper mTextHelper;
    
    public AppCompatTextView(final Context context) {
        this(context, null);
    }
    
    public AppCompatTextView(final Context context, final AttributeSet set) {
        this(context, set, 16842884);
    }
    
    public AppCompatTextView(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this)).loadFromAttributes(set, n);
        (this.mTextHelper = AppCompatTextHelper.create(this)).loadFromAttributes(set, n);
        this.mTextHelper.applyCompoundDrawablesTints();
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.applySupportBackgroundTint();
        }
        if (this.mTextHelper != null) {
            this.mTextHelper.applyCompoundDrawablesTints();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getAutoSizeMaxTextSize() {
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeMaxTextSize();
        }
        return -1;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getAutoSizeMinTextSize() {
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeMinTextSize();
        }
        return -1;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getAutoSizeStepGranularity() {
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeStepGranularity();
        }
        return -1;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int[] getAutoSizeTextAvailableSizes() {
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeTextAvailableSizes();
        }
        return new int[0];
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getAutoSizeTextType() {
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeTextType();
        }
        return 0;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ColorStateList getSupportBackgroundTintList() {
        if (this.mBackgroundTintHelper != null) {
            return this.mBackgroundTintHelper.getSupportBackgroundTintList();
        }
        return null;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        if (this.mBackgroundTintHelper != null) {
            return this.mBackgroundTintHelper.getSupportBackgroundTintMode();
        }
        return null;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (this.mTextHelper != null) {
            this.mTextHelper.onLayout(b, n, n2, n3, n4);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final int[] array, final int n) throws IllegalArgumentException {
        if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setAutoSizeTextTypeWithDefaults(final int autoSizeTextTypeWithDefaults) {
        if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeWithDefaults(autoSizeTextTypeWithDefaults);
        }
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundDrawable(backgroundDrawable);
        }
    }
    
    public void setBackgroundResource(@DrawableRes final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundResource(backgroundResource);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintList(@Nullable final ColorStateList supportBackgroundTintList) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintMode(@Nullable final PorterDuff$Mode supportBackgroundTintMode) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
    
    public void setTextAppearance(final Context context, final int n) {
        super.setTextAppearance(context, n);
        if (this.mTextHelper != null) {
            this.mTextHelper.onSetTextAppearance(context, n);
        }
    }
    
    public void setTextSize(final int n, final float n2) {
        if (this.mTextHelper != null) {
            this.mTextHelper.setTextSize(n, n2);
            return;
        }
        super.setTextSize(n, n2);
    }
}
