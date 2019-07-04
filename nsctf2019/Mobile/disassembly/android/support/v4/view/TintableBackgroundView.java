// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.graphics.PorterDuff$Mode;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;

public interface TintableBackgroundView
{
    @Nullable
    ColorStateList getSupportBackgroundTintList();
    
    @Nullable
    PorterDuff$Mode getSupportBackgroundTintMode();
    
    void setSupportBackgroundTintList(@Nullable final ColorStateList p0);
    
    void setSupportBackgroundTintMode(@Nullable final PorterDuff$Mode p0);
}
