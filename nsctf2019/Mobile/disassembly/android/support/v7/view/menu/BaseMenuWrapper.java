// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import java.util.Iterator;
import android.support.v4.util.ArrayMap;
import android.view.SubMenu;
import android.support.v4.internal.view.SupportSubMenu;
import android.view.MenuItem;
import android.support.v4.internal.view.SupportMenuItem;
import java.util.Map;
import android.content.Context;

abstract class BaseMenuWrapper<T> extends BaseWrapper<T>
{
    final Context mContext;
    private Map<SupportMenuItem, MenuItem> mMenuItems;
    private Map<SupportSubMenu, SubMenu> mSubMenus;
    
    BaseMenuWrapper(final Context mContext, final T t) {
        super(t);
        this.mContext = mContext;
    }
    
    final MenuItem getMenuItemWrapper(MenuItem wrapSupportMenuItem) {
        if (wrapSupportMenuItem instanceof SupportMenuItem) {
            final SupportMenuItem supportMenuItem = (SupportMenuItem)wrapSupportMenuItem;
            if (this.mMenuItems == null) {
                this.mMenuItems = new ArrayMap<SupportMenuItem, MenuItem>();
            }
            if ((wrapSupportMenuItem = this.mMenuItems.get(wrapSupportMenuItem)) == null) {
                wrapSupportMenuItem = MenuWrapperFactory.wrapSupportMenuItem(this.mContext, supportMenuItem);
                this.mMenuItems.put(supportMenuItem, wrapSupportMenuItem);
            }
            return wrapSupportMenuItem;
        }
        return wrapSupportMenuItem;
    }
    
    final SubMenu getSubMenuWrapper(SubMenu wrapSupportSubMenu) {
        if (wrapSupportSubMenu instanceof SupportSubMenu) {
            final SupportSubMenu supportSubMenu = (SupportSubMenu)wrapSupportSubMenu;
            if (this.mSubMenus == null) {
                this.mSubMenus = new ArrayMap<SupportSubMenu, SubMenu>();
            }
            if ((wrapSupportSubMenu = this.mSubMenus.get(supportSubMenu)) == null) {
                wrapSupportSubMenu = MenuWrapperFactory.wrapSupportSubMenu(this.mContext, supportSubMenu);
                this.mSubMenus.put(supportSubMenu, wrapSupportSubMenu);
            }
            return wrapSupportSubMenu;
        }
        return wrapSupportSubMenu;
    }
    
    final void internalClear() {
        if (this.mMenuItems != null) {
            this.mMenuItems.clear();
        }
        if (this.mSubMenus != null) {
            this.mSubMenus.clear();
        }
    }
    
    final void internalRemoveGroup(final int n) {
        if (this.mMenuItems != null) {
            final Iterator<SupportMenuItem> iterator = this.mMenuItems.keySet().iterator();
            while (iterator.hasNext()) {
                if (n == ((MenuItem)iterator.next()).getGroupId()) {
                    iterator.remove();
                }
            }
        }
    }
    
    final void internalRemoveItem(final int n) {
        if (this.mMenuItems != null) {
            final Iterator<SupportMenuItem> iterator = this.mMenuItems.keySet().iterator();
            while (iterator.hasNext()) {
                if (n == ((MenuItem)iterator.next()).getItemId()) {
                    iterator.remove();
                }
            }
        }
    }
}
