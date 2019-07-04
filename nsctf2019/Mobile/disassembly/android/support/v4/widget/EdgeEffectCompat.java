// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.support.annotation.RequiresApi;
import android.graphics.Canvas;
import android.content.Context;
import android.os.Build$VERSION;
import android.widget.EdgeEffect;

public final class EdgeEffectCompat
{
    private static final EdgeEffectBaseImpl IMPL;
    private EdgeEffect mEdgeEffect;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (EdgeEffectBaseImpl)new EdgeEffectApi21Impl();
            return;
        }
        IMPL = new EdgeEffectBaseImpl();
    }
    
    public EdgeEffectCompat(final Context context) {
        this.mEdgeEffect = new EdgeEffect(context);
    }
    
    public static void onPull(final EdgeEffect edgeEffect, final float n, final float n2) {
        EdgeEffectCompat.IMPL.onPull(edgeEffect, n, n2);
    }
    
    @Deprecated
    public boolean draw(final Canvas canvas) {
        return this.mEdgeEffect.draw(canvas);
    }
    
    @Deprecated
    public void finish() {
        this.mEdgeEffect.finish();
    }
    
    @Deprecated
    public boolean isFinished() {
        return this.mEdgeEffect.isFinished();
    }
    
    @Deprecated
    public boolean onAbsorb(final int n) {
        this.mEdgeEffect.onAbsorb(n);
        return true;
    }
    
    @Deprecated
    public boolean onPull(final float n) {
        this.mEdgeEffect.onPull(n);
        return true;
    }
    
    @Deprecated
    public boolean onPull(final float n, final float n2) {
        EdgeEffectCompat.IMPL.onPull(this.mEdgeEffect, n, n2);
        return true;
    }
    
    @Deprecated
    public boolean onRelease() {
        this.mEdgeEffect.onRelease();
        return this.mEdgeEffect.isFinished();
    }
    
    @Deprecated
    public void setSize(final int n, final int n2) {
        this.mEdgeEffect.setSize(n, n2);
    }
    
    @RequiresApi(21)
    static class EdgeEffectApi21Impl extends EdgeEffectBaseImpl
    {
        @Override
        public void onPull(final EdgeEffect edgeEffect, final float n, final float n2) {
            edgeEffect.onPull(n, n2);
        }
    }
    
    static class EdgeEffectBaseImpl
    {
        public void onPull(final EdgeEffect edgeEffect, final float n, final float n2) {
            edgeEffect.onPull(n);
        }
    }
}
