// 
// Decompiled by Procyon v0.5.30
// 

package android.support.graphics.drawable;

import android.graphics.PorterDuff$Mode;
import android.graphics.Region;
import android.graphics.Rect;
import android.graphics.ColorFilter;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.content.res.Resources$Theme;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.graphics.drawable.Drawable;

abstract class VectorDrawableCommon extends Drawable implements TintAwareDrawable
{
    Drawable mDelegateDrawable;
    
    public void applyTheme(final Resources$Theme resources$Theme) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.applyTheme(this.mDelegateDrawable, resources$Theme);
        }
    }
    
    public void clearColorFilter() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.clearColorFilter();
            return;
        }
        super.clearColorFilter();
    }
    
    public ColorFilter getColorFilter() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.getColorFilter(this.mDelegateDrawable);
        }
        return null;
    }
    
    public Drawable getCurrent() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getCurrent();
        }
        return super.getCurrent();
    }
    
    public int getMinimumHeight() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getMinimumHeight();
        }
        return super.getMinimumHeight();
    }
    
    public int getMinimumWidth() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getMinimumWidth();
        }
        return super.getMinimumWidth();
    }
    
    public boolean getPadding(final Rect rect) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getPadding(rect);
        }
        return super.getPadding(rect);
    }
    
    public int[] getState() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getState();
        }
        return super.getState();
    }
    
    public Region getTransparentRegion() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getTransparentRegion();
        }
        return super.getTransparentRegion();
    }
    
    public void jumpToCurrentState() {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.jumpToCurrentState(this.mDelegateDrawable);
        }
    }
    
    protected void onBoundsChange(final Rect bounds) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setBounds(bounds);
            return;
        }
        super.onBoundsChange(bounds);
    }
    
    protected boolean onLevelChange(final int level) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setLevel(level);
        }
        return super.onLevelChange(level);
    }
    
    public void setChangingConfigurations(final int n) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setChangingConfigurations(n);
            return;
        }
        super.setChangingConfigurations(n);
    }
    
    public void setColorFilter(final int n, final PorterDuff$Mode porterDuff$Mode) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setColorFilter(n, porterDuff$Mode);
            return;
        }
        super.setColorFilter(n, porterDuff$Mode);
    }
    
    public void setFilterBitmap(final boolean filterBitmap) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setFilterBitmap(filterBitmap);
        }
    }
    
    public void setHotspot(final float n, final float n2) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setHotspot(this.mDelegateDrawable, n, n2);
        }
    }
    
    public void setHotspotBounds(final int n, final int n2, final int n3, final int n4) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setHotspotBounds(this.mDelegateDrawable, n, n2, n3, n4);
        }
    }
    
    public boolean setState(final int[] array) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setState(array);
        }
        return super.setState(array);
    }
}
