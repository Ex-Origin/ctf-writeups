// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.view.View;
import android.graphics.drawable.Drawable;
import android.view.MenuItem;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportSubMenu;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;
import android.view.SubMenu;

@RequiresApi(14)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class SubMenuWrapperICS extends MenuWrapperICS implements SubMenu
{
    SubMenuWrapperICS(final Context context, final SupportSubMenu supportSubMenu) {
        super(context, supportSubMenu);
    }
    
    public void clearHeader() {
        this.getWrappedObject().clearHeader();
    }
    
    public MenuItem getItem() {
        return this.getMenuItemWrapper(this.getWrappedObject().getItem());
    }
    
    public SupportSubMenu getWrappedObject() {
        return (SupportSubMenu)this.mWrappedObject;
    }
    
    public SubMenu setHeaderIcon(final int headerIcon) {
        this.getWrappedObject().setHeaderIcon(headerIcon);
        return (SubMenu)this;
    }
    
    public SubMenu setHeaderIcon(final Drawable headerIcon) {
        this.getWrappedObject().setHeaderIcon(headerIcon);
        return (SubMenu)this;
    }
    
    public SubMenu setHeaderTitle(final int headerTitle) {
        this.getWrappedObject().setHeaderTitle(headerTitle);
        return (SubMenu)this;
    }
    
    public SubMenu setHeaderTitle(final CharSequence headerTitle) {
        this.getWrappedObject().setHeaderTitle(headerTitle);
        return (SubMenu)this;
    }
    
    public SubMenu setHeaderView(final View headerView) {
        this.getWrappedObject().setHeaderView(headerView);
        return (SubMenu)this;
    }
    
    public SubMenu setIcon(final int icon) {
        this.getWrappedObject().setIcon(icon);
        return (SubMenu)this;
    }
    
    public SubMenu setIcon(final Drawable icon) {
        this.getWrappedObject().setIcon(icon);
        return (SubMenu)this;
    }
}
