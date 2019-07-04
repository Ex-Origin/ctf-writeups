// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.net.Uri;
import android.graphics.drawable.Icon;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.RestrictTo;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.widget.ImageView;
import android.view.View;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v4.view.TintableBackgroundView;
import android.widget.ImageButton;

public class AppCompatImageButton extends ImageButton implements TintableBackgroundView, TintableImageSourceView
{
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private final AppCompatImageHelper mImageHelper;
    
    public AppCompatImageButton(final Context context) {
        this(context, null);
    }
    
    public AppCompatImageButton(final Context context, final AttributeSet set) {
        this(context, set, R.attr.imageButtonStyle);
    }
    
    public AppCompatImageButton(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this)).loadFromAttributes(set, n);
        (this.mImageHelper = new AppCompatImageHelper((ImageView)this)).loadFromAttributes(set, n);
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.applySupportBackgroundTint();
        }
        if (this.mImageHelper != null) {
            this.mImageHelper.applySupportImageTint();
        }
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
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ColorStateList getSupportImageTintList() {
        if (this.mImageHelper != null) {
            return this.mImageHelper.getSupportImageTintList();
        }
        return null;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PorterDuff$Mode getSupportImageTintMode() {
        if (this.mImageHelper != null) {
            return this.mImageHelper.getSupportImageTintMode();
        }
        return null;
    }
    
    public boolean hasOverlappingRendering() {
        return this.mImageHelper.hasOverlappingRendering() && super.hasOverlappingRendering();
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
    
    public void setImageBitmap(final Bitmap imageBitmap) {
        super.setImageBitmap(imageBitmap);
        if (this.mImageHelper != null) {
            this.mImageHelper.applySupportImageTint();
        }
    }
    
    public void setImageDrawable(@Nullable final Drawable imageDrawable) {
        super.setImageDrawable(imageDrawable);
        if (this.mImageHelper != null) {
            this.mImageHelper.applySupportImageTint();
        }
    }
    
    public void setImageIcon(@Nullable final Icon imageIcon) {
        super.setImageIcon(imageIcon);
        if (this.mImageHelper != null) {
            this.mImageHelper.applySupportImageTint();
        }
    }
    
    public void setImageResource(@DrawableRes final int imageResource) {
        this.mImageHelper.setImageResource(imageResource);
    }
    
    public void setImageURI(@Nullable final Uri imageURI) {
        super.setImageURI(imageURI);
        if (this.mImageHelper != null) {
            this.mImageHelper.applySupportImageTint();
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
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportImageTintList(@Nullable final ColorStateList supportImageTintList) {
        if (this.mImageHelper != null) {
            this.mImageHelper.setSupportImageTintList(supportImageTintList);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportImageTintMode(@Nullable final PorterDuff$Mode supportImageTintMode) {
        if (this.mImageHelper != null) {
            this.mImageHelper.setSupportImageTintMode(supportImageTintMode);
        }
    }
}
