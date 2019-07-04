// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.v7.content.res.AppCompatResources;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.graphics.drawable.RippleDrawable;
import android.os.Build$VERSION;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class AppCompatImageHelper
{
    private TintInfo mImageTint;
    private TintInfo mInternalImageTint;
    private TintInfo mTmpInfo;
    private final ImageView mView;
    
    public AppCompatImageHelper(final ImageView mView) {
        this.mView = mView;
    }
    
    private boolean applyFrameworkTintUsingColorFilter(@NonNull final Drawable drawable) {
        if (this.mTmpInfo == null) {
            this.mTmpInfo = new TintInfo();
        }
        final TintInfo mTmpInfo = this.mTmpInfo;
        mTmpInfo.clear();
        final ColorStateList imageTintList = ImageViewCompat.getImageTintList(this.mView);
        if (imageTintList != null) {
            mTmpInfo.mHasTintList = true;
            mTmpInfo.mTintList = imageTintList;
        }
        final PorterDuff$Mode imageTintMode = ImageViewCompat.getImageTintMode(this.mView);
        if (imageTintMode != null) {
            mTmpInfo.mHasTintMode = true;
            mTmpInfo.mTintMode = imageTintMode;
        }
        if (mTmpInfo.mHasTintList || mTmpInfo.mHasTintMode) {
            AppCompatDrawableManager.tintDrawable(drawable, mTmpInfo, this.mView.getDrawableState());
            return true;
        }
        return false;
    }
    
    private boolean shouldApplyFrameworkTintUsingColorFilter() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT > 21) {
            if (this.mInternalImageTint == null) {
                return false;
            }
        }
        else if (sdk_INT != 21) {
            return false;
        }
        return true;
    }
    
    void applySupportImageTint() {
        final Drawable drawable = this.mView.getDrawable();
        if (drawable != null) {
            DrawableUtils.fixDrawable(drawable);
        }
        if (drawable != null && (!this.shouldApplyFrameworkTintUsingColorFilter() || !this.applyFrameworkTintUsingColorFilter(drawable))) {
            if (this.mImageTint != null) {
                AppCompatDrawableManager.tintDrawable(drawable, this.mImageTint, this.mView.getDrawableState());
                return;
            }
            if (this.mInternalImageTint != null) {
                AppCompatDrawableManager.tintDrawable(drawable, this.mInternalImageTint, this.mView.getDrawableState());
            }
        }
    }
    
    ColorStateList getSupportImageTintList() {
        if (this.mImageTint != null) {
            return this.mImageTint.mTintList;
        }
        return null;
    }
    
    PorterDuff$Mode getSupportImageTintMode() {
        if (this.mImageTint != null) {
            return this.mImageTint.mTintMode;
        }
        return null;
    }
    
    boolean hasOverlappingRendering() {
        final Drawable background = this.mView.getBackground();
        return Build$VERSION.SDK_INT < 21 || !(background instanceof RippleDrawable);
    }
    
    public void loadFromAttributes(final AttributeSet set, int resourceId) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), set, R.styleable.AppCompatImageView, resourceId, 0);
        try {
            Drawable drawable2;
            final Drawable drawable = drawable2 = this.mView.getDrawable();
            if (drawable == null) {
                resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatImageView_srcCompat, -1);
                drawable2 = drawable;
                if (resourceId != -1) {
                    final Drawable drawable3 = AppCompatResources.getDrawable(this.mView.getContext(), resourceId);
                    if ((drawable2 = drawable3) != null) {
                        this.mView.setImageDrawable(drawable3);
                        drawable2 = drawable3;
                    }
                }
            }
            if (drawable2 != null) {
                DrawableUtils.fixDrawable(drawable2);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.AppCompatImageView_tint)) {
                ImageViewCompat.setImageTintList(this.mView, obtainStyledAttributes.getColorStateList(R.styleable.AppCompatImageView_tint));
            }
            if (obtainStyledAttributes.hasValue(R.styleable.AppCompatImageView_tintMode)) {
                ImageViewCompat.setImageTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.AppCompatImageView_tintMode, -1), null));
            }
        }
        finally {
            obtainStyledAttributes.recycle();
        }
    }
    
    public void setImageResource(final int n) {
        if (n != 0) {
            final Drawable drawable = AppCompatResources.getDrawable(this.mView.getContext(), n);
            if (drawable != null) {
                DrawableUtils.fixDrawable(drawable);
            }
            this.mView.setImageDrawable(drawable);
        }
        else {
            this.mView.setImageDrawable((Drawable)null);
        }
        this.applySupportImageTint();
    }
    
    void setInternalImageTint(final ColorStateList mTintList) {
        if (mTintList != null) {
            if (this.mInternalImageTint == null) {
                this.mInternalImageTint = new TintInfo();
            }
            this.mInternalImageTint.mTintList = mTintList;
            this.mInternalImageTint.mHasTintList = true;
        }
        else {
            this.mInternalImageTint = null;
        }
        this.applySupportImageTint();
    }
    
    void setSupportImageTintList(final ColorStateList mTintList) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        this.mImageTint.mTintList = mTintList;
        this.mImageTint.mHasTintList = true;
        this.applySupportImageTint();
    }
    
    void setSupportImageTintMode(final PorterDuff$Mode mTintMode) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        this.mImageTint.mTintMode = mTintMode;
        this.mImageTint.mHasTintMode = true;
        this.applySupportImageTint();
    }
}
