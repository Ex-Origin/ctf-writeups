// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.graphics.drawable;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.Region;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.support.annotation.RestrictTo;
import android.graphics.drawable.Drawable$Callback;
import android.graphics.drawable.Drawable;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class DrawableWrapper extends Drawable implements Drawable$Callback
{
    private Drawable mDrawable;
    
    public DrawableWrapper(final Drawable wrappedDrawable) {
        this.setWrappedDrawable(wrappedDrawable);
    }
    
    public void draw(final Canvas canvas) {
        this.mDrawable.draw(canvas);
    }
    
    public int getChangingConfigurations() {
        return this.mDrawable.getChangingConfigurations();
    }
    
    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }
    
    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }
    
    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }
    
    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }
    
    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }
    
    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }
    
    public boolean getPadding(final Rect rect) {
        return this.mDrawable.getPadding(rect);
    }
    
    public int[] getState() {
        return this.mDrawable.getState();
    }
    
    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }
    
    public Drawable getWrappedDrawable() {
        return this.mDrawable;
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        this.invalidateSelf();
    }
    
    public boolean isAutoMirrored() {
        return DrawableCompat.isAutoMirrored(this.mDrawable);
    }
    
    public boolean isStateful() {
        return this.mDrawable.isStateful();
    }
    
    public void jumpToCurrentState() {
        DrawableCompat.jumpToCurrentState(this.mDrawable);
    }
    
    protected void onBoundsChange(final Rect bounds) {
        this.mDrawable.setBounds(bounds);
    }
    
    protected boolean onLevelChange(final int level) {
        return this.mDrawable.setLevel(level);
    }
    
    public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
        this.scheduleSelf(runnable, n);
    }
    
    public void setAlpha(final int alpha) {
        this.mDrawable.setAlpha(alpha);
    }
    
    public void setAutoMirrored(final boolean b) {
        DrawableCompat.setAutoMirrored(this.mDrawable, b);
    }
    
    public void setChangingConfigurations(final int changingConfigurations) {
        this.mDrawable.setChangingConfigurations(changingConfigurations);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
    }
    
    public void setDither(final boolean dither) {
        this.mDrawable.setDither(dither);
    }
    
    public void setFilterBitmap(final boolean filterBitmap) {
        this.mDrawable.setFilterBitmap(filterBitmap);
    }
    
    public void setHotspot(final float n, final float n2) {
        DrawableCompat.setHotspot(this.mDrawable, n, n2);
    }
    
    public void setHotspotBounds(final int n, final int n2, final int n3, final int n4) {
        DrawableCompat.setHotspotBounds(this.mDrawable, n, n2, n3, n4);
    }
    
    public boolean setState(final int[] state) {
        return this.mDrawable.setState(state);
    }
    
    public void setTint(final int n) {
        DrawableCompat.setTint(this.mDrawable, n);
    }
    
    public void setTintList(final ColorStateList list) {
        DrawableCompat.setTintList(this.mDrawable, list);
    }
    
    public void setTintMode(final PorterDuff$Mode porterDuff$Mode) {
        DrawableCompat.setTintMode(this.mDrawable, porterDuff$Mode);
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        return super.setVisible(b, b2) || this.mDrawable.setVisible(b, b2);
    }
    
    public void setWrappedDrawable(final Drawable mDrawable) {
        if (this.mDrawable != null) {
            this.mDrawable.setCallback((Drawable$Callback)null);
        }
        if ((this.mDrawable = mDrawable) != null) {
            mDrawable.setCallback((Drawable$Callback)this);
        }
    }
    
    public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
        this.unscheduleSelf(runnable);
    }
}
