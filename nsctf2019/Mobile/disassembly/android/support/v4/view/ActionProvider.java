// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.util.Log;
import android.support.annotation.RestrictTo;
import android.view.SubMenu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;

public abstract class ActionProvider
{
    private static final String TAG = "ActionProvider(support)";
    private final Context mContext;
    private SubUiVisibilityListener mSubUiVisibilityListener;
    private VisibilityListener mVisibilityListener;
    
    public ActionProvider(final Context mContext) {
        this.mContext = mContext;
    }
    
    public Context getContext() {
        return this.mContext;
    }
    
    public boolean hasSubMenu() {
        return false;
    }
    
    public boolean isVisible() {
        return true;
    }
    
    public abstract View onCreateActionView();
    
    public View onCreateActionView(final MenuItem menuItem) {
        return this.onCreateActionView();
    }
    
    public boolean onPerformDefaultAction() {
        return false;
    }
    
    public void onPrepareSubMenu(final SubMenu subMenu) {
    }
    
    public boolean overridesItemVisibility() {
        return false;
    }
    
    public void refreshVisibility() {
        if (this.mVisibilityListener != null && this.overridesItemVisibility()) {
            this.mVisibilityListener.onActionProviderVisibilityChanged(this.isVisible());
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void reset() {
        this.mVisibilityListener = null;
        this.mSubUiVisibilityListener = null;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSubUiVisibilityListener(final SubUiVisibilityListener mSubUiVisibilityListener) {
        this.mSubUiVisibilityListener = mSubUiVisibilityListener;
    }
    
    public void setVisibilityListener(final VisibilityListener mVisibilityListener) {
        if (this.mVisibilityListener != null && mVisibilityListener != null) {
            Log.w("ActionProvider(support)", "setVisibilityListener: Setting a new ActionProvider.VisibilityListener when one is already set. Are you reusing this " + this.getClass().getSimpleName() + " instance while it is still in use somewhere else?");
        }
        this.mVisibilityListener = mVisibilityListener;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void subUiVisibilityChanged(final boolean b) {
        if (this.mSubUiVisibilityListener != null) {
            this.mSubUiVisibilityListener.onSubUiVisibilityChanged(b);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public interface SubUiVisibilityListener
    {
        void onSubUiVisibilityChanged(final boolean p0);
    }
    
    public interface VisibilityListener
    {
        void onActionProviderVisibilityChanged(final boolean p0);
    }
}
