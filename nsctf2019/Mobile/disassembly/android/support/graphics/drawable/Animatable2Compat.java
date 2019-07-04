// 
// Decompiled by Procyon v0.5.30
// 

package android.support.graphics.drawable;

import android.support.annotation.RequiresApi;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Animatable2$AnimationCallback;
import android.support.annotation.NonNull;
import android.graphics.drawable.Animatable;

public interface Animatable2Compat extends Animatable
{
    void clearAnimationCallbacks();
    
    void registerAnimationCallback(@NonNull final AnimationCallback p0);
    
    boolean unregisterAnimationCallback(@NonNull final AnimationCallback p0);
    
    public abstract static class AnimationCallback
    {
        Animatable2$AnimationCallback mPlatformCallback;
        
        @RequiresApi(23)
        Animatable2$AnimationCallback getPlatformCallback() {
            if (this.mPlatformCallback == null) {
                this.mPlatformCallback = new Animatable2$AnimationCallback() {
                    public void onAnimationEnd(final Drawable drawable) {
                        AnimationCallback.this.onAnimationEnd(drawable);
                    }
                    
                    public void onAnimationStart(final Drawable drawable) {
                        AnimationCallback.this.onAnimationStart(drawable);
                    }
                };
            }
            return this.mPlatformCallback;
        }
        
        public void onAnimationEnd(final Drawable drawable) {
        }
        
        public void onAnimationStart(final Drawable drawable) {
        }
    }
}
