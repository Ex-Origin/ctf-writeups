// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.v7.view.ContextThemeWrapper;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.content.Context;
import android.support.annotation.Nullable;
import android.content.res.Resources$Theme;
import android.widget.SpinnerAdapter;

public interface ThemedSpinnerAdapter extends SpinnerAdapter
{
    @Nullable
    Resources$Theme getDropDownViewTheme();
    
    void setDropDownViewTheme(@Nullable final Resources$Theme p0);
    
    public static final class Helper
    {
        private final Context mContext;
        private LayoutInflater mDropDownInflater;
        private final LayoutInflater mInflater;
        
        public Helper(@NonNull final Context mContext) {
            this.mContext = mContext;
            this.mInflater = LayoutInflater.from(mContext);
        }
        
        @NonNull
        public LayoutInflater getDropDownViewInflater() {
            if (this.mDropDownInflater != null) {
                return this.mDropDownInflater;
            }
            return this.mInflater;
        }
        
        @Nullable
        public Resources$Theme getDropDownViewTheme() {
            if (this.mDropDownInflater == null) {
                return null;
            }
            return this.mDropDownInflater.getContext().getTheme();
        }
        
        public void setDropDownViewTheme(@Nullable final Resources$Theme resources$Theme) {
            if (resources$Theme == null) {
                this.mDropDownInflater = null;
                return;
            }
            if (resources$Theme == this.mContext.getTheme()) {
                this.mDropDownInflater = this.mInflater;
                return;
            }
            this.mDropDownInflater = LayoutInflater.from((Context)new ContextThemeWrapper(this.mContext, resources$Theme));
        }
    }
}
