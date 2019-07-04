// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.annotation.DrawableRes;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.RestrictTo;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.widget.CompoundButton;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.widget.TintableCompoundButton;
import android.widget.CheckBox;

public class AppCompatCheckBox extends CheckBox implements TintableCompoundButton
{
    private final AppCompatCompoundButtonHelper mCompoundButtonHelper;
    
    public AppCompatCheckBox(final Context context) {
        this(context, null);
    }
    
    public AppCompatCheckBox(final Context context, final AttributeSet set) {
        this(context, set, R.attr.checkboxStyle);
    }
    
    public AppCompatCheckBox(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper((CompoundButton)this)).loadFromAttributes(set, n);
    }
    
    public int getCompoundPaddingLeft() {
        int n = super.getCompoundPaddingLeft();
        if (this.mCompoundButtonHelper != null) {
            n = this.mCompoundButtonHelper.getCompoundPaddingLeft(n);
        }
        return n;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ColorStateList getSupportButtonTintList() {
        if (this.mCompoundButtonHelper != null) {
            return this.mCompoundButtonHelper.getSupportButtonTintList();
        }
        return null;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PorterDuff$Mode getSupportButtonTintMode() {
        if (this.mCompoundButtonHelper != null) {
            return this.mCompoundButtonHelper.getSupportButtonTintMode();
        }
        return null;
    }
    
    public void setButtonDrawable(@DrawableRes final int n) {
        this.setButtonDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setButtonDrawable(final Drawable buttonDrawable) {
        super.setButtonDrawable(buttonDrawable);
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.onSetButtonDrawable();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportButtonTintList(@Nullable final ColorStateList supportButtonTintList) {
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.setSupportButtonTintList(supportButtonTintList);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportButtonTintMode(@Nullable final PorterDuff$Mode supportButtonTintMode) {
        if (this.mCompoundButtonHelper != null) {
            this.mCompoundButtonHelper.setSupportButtonTintMode(supportButtonTintMode);
        }
    }
}
