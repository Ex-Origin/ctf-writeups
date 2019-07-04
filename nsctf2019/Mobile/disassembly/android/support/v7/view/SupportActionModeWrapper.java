// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view;

import android.support.v4.internal.view.SupportMenuItem;
import android.view.MenuItem;
import android.view.ActionMode$Callback;
import android.support.v4.util.SimpleArrayMap;
import java.util.ArrayList;
import android.view.MenuInflater;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.support.v4.internal.view.SupportMenu;
import android.view.Menu;
import android.view.View;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.view.ActionMode;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class SupportActionModeWrapper extends ActionMode
{
    final Context mContext;
    final android.support.v7.view.ActionMode mWrappedObject;
    
    public SupportActionModeWrapper(final Context mContext, final android.support.v7.view.ActionMode mWrappedObject) {
        this.mContext = mContext;
        this.mWrappedObject = mWrappedObject;
    }
    
    public void finish() {
        this.mWrappedObject.finish();
    }
    
    public View getCustomView() {
        return this.mWrappedObject.getCustomView();
    }
    
    public Menu getMenu() {
        return MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu)this.mWrappedObject.getMenu());
    }
    
    public MenuInflater getMenuInflater() {
        return this.mWrappedObject.getMenuInflater();
    }
    
    public CharSequence getSubtitle() {
        return this.mWrappedObject.getSubtitle();
    }
    
    public Object getTag() {
        return this.mWrappedObject.getTag();
    }
    
    public CharSequence getTitle() {
        return this.mWrappedObject.getTitle();
    }
    
    public boolean getTitleOptionalHint() {
        return this.mWrappedObject.getTitleOptionalHint();
    }
    
    public void invalidate() {
        this.mWrappedObject.invalidate();
    }
    
    public boolean isTitleOptional() {
        return this.mWrappedObject.isTitleOptional();
    }
    
    public void setCustomView(final View customView) {
        this.mWrappedObject.setCustomView(customView);
    }
    
    public void setSubtitle(final int subtitle) {
        this.mWrappedObject.setSubtitle(subtitle);
    }
    
    public void setSubtitle(final CharSequence subtitle) {
        this.mWrappedObject.setSubtitle(subtitle);
    }
    
    public void setTag(final Object tag) {
        this.mWrappedObject.setTag(tag);
    }
    
    public void setTitle(final int title) {
        this.mWrappedObject.setTitle(title);
    }
    
    public void setTitle(final CharSequence title) {
        this.mWrappedObject.setTitle(title);
    }
    
    public void setTitleOptionalHint(final boolean titleOptionalHint) {
        this.mWrappedObject.setTitleOptionalHint(titleOptionalHint);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class CallbackWrapper implements Callback
    {
        final ArrayList<SupportActionModeWrapper> mActionModes;
        final Context mContext;
        final SimpleArrayMap<Menu, Menu> mMenus;
        final ActionMode$Callback mWrappedCallback;
        
        public CallbackWrapper(final Context mContext, final ActionMode$Callback mWrappedCallback) {
            this.mContext = mContext;
            this.mWrappedCallback = mWrappedCallback;
            this.mActionModes = new ArrayList<SupportActionModeWrapper>();
            this.mMenus = new SimpleArrayMap<Menu, Menu>();
        }
        
        private Menu getMenuWrapper(final Menu menu) {
            Menu wrapSupportMenu;
            if ((wrapSupportMenu = this.mMenus.get(menu)) == null) {
                wrapSupportMenu = MenuWrapperFactory.wrapSupportMenu(this.mContext, (SupportMenu)menu);
                this.mMenus.put(menu, wrapSupportMenu);
            }
            return wrapSupportMenu;
        }
        
        public android.view.ActionMode getActionModeWrapper(final ActionMode actionMode) {
            for (int i = 0; i < this.mActionModes.size(); ++i) {
                final SupportActionModeWrapper supportActionModeWrapper = this.mActionModes.get(i);
                if (supportActionModeWrapper != null && supportActionModeWrapper.mWrappedObject == actionMode) {
                    return supportActionModeWrapper;
                }
            }
            final SupportActionModeWrapper supportActionModeWrapper2 = new SupportActionModeWrapper(this.mContext, actionMode);
            this.mActionModes.add(supportActionModeWrapper2);
            return supportActionModeWrapper2;
        }
        
        @Override
        public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
            return this.mWrappedCallback.onActionItemClicked(this.getActionModeWrapper(actionMode), MenuWrapperFactory.wrapSupportMenuItem(this.mContext, (SupportMenuItem)menuItem));
        }
        
        @Override
        public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrappedCallback.onCreateActionMode(this.getActionModeWrapper(actionMode), this.getMenuWrapper(menu));
        }
        
        @Override
        public void onDestroyActionMode(final ActionMode actionMode) {
            this.mWrappedCallback.onDestroyActionMode(this.getActionModeWrapper(actionMode));
        }
        
        @Override
        public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
            return this.mWrappedCallback.onPrepareActionMode(this.getActionModeWrapper(actionMode), this.getMenuWrapper(menu));
        }
    }
}
