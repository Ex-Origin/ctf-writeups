// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.Rect;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface FitWindowsViewGroup
{
    void setOnFitSystemWindowsListener(final OnFitSystemWindowsListener p0);
    
    public interface OnFitSystemWindowsListener
    {
        void onFitSystemWindows(final Rect p0);
    }
}
