// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.annotation.TargetApi;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.v4.os.BuildCompat;

public class TooltipCompat
{
    private static final ViewCompatImpl IMPL;
    
    static {
        if (BuildCompat.isAtLeastO()) {
            IMPL = (ViewCompatImpl)new Api26ViewCompatImpl();
            return;
        }
        IMPL = (ViewCompatImpl)new BaseViewCompatImpl();
    }
    
    public static void setTooltipText(@NonNull final View view, @Nullable final CharSequence charSequence) {
        TooltipCompat.IMPL.setTooltipText(view, charSequence);
    }
    
    @TargetApi(26)
    private static class Api26ViewCompatImpl implements ViewCompatImpl
    {
        @Override
        public void setTooltipText(@NonNull final View view, @Nullable final CharSequence tooltipText) {
            view.setTooltipText(tooltipText);
        }
    }
    
    private static class BaseViewCompatImpl implements ViewCompatImpl
    {
        @Override
        public void setTooltipText(@NonNull final View view, @Nullable final CharSequence charSequence) {
            TooltipCompatHandler.setTooltipText(view, charSequence);
        }
    }
    
    private interface ViewCompatImpl
    {
        void setTooltipText(@NonNull final View p0, @Nullable final CharSequence p1);
    }
}
