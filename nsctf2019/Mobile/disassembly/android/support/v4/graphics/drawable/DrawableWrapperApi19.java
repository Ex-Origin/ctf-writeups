// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics.drawable;

import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class DrawableWrapperApi19 extends DrawableWrapperApi14
{
    DrawableWrapperApi19(final Drawable drawable) {
        super(drawable);
    }
    
    DrawableWrapperApi19(final DrawableWrapperState drawableWrapperState, final Resources resources) {
        super(drawableWrapperState, resources);
    }
    
    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }
    
    @NonNull
    @Override
    DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateKitKat(this.mState, null);
    }
    
    public void setAutoMirrored(final boolean autoMirrored) {
        this.mDrawable.setAutoMirrored(autoMirrored);
    }
    
    private static class DrawableWrapperStateKitKat extends DrawableWrapperState
    {
        DrawableWrapperStateKitKat(@Nullable final DrawableWrapperState drawableWrapperState, @Nullable final Resources resources) {
            super(drawableWrapperState, resources);
        }
        
        @Override
        public Drawable newDrawable(@Nullable final Resources resources) {
            return new DrawableWrapperApi19(this, resources);
        }
    }
}
