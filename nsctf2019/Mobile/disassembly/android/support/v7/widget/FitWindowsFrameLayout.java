// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.Rect;
import android.util.AttributeSet;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.widget.FrameLayout;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class FitWindowsFrameLayout extends FrameLayout implements FitWindowsViewGroup
{
    private OnFitSystemWindowsListener mListener;
    
    public FitWindowsFrameLayout(final Context context) {
        super(context);
    }
    
    public FitWindowsFrameLayout(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    protected boolean fitSystemWindows(final Rect rect) {
        if (this.mListener != null) {
            this.mListener.onFitSystemWindows(rect);
        }
        return super.fitSystemWindows(rect);
    }
    
    public void setOnFitSystemWindowsListener(final OnFitSystemWindowsListener mListener) {
        this.mListener = mListener;
    }
}
