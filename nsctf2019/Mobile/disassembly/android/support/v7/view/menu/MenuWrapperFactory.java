// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.view.SubMenu;
import android.support.v4.internal.view.SupportSubMenu;
import android.os.Build$VERSION;
import android.view.MenuItem;
import android.support.v4.internal.view.SupportMenuItem;
import android.view.Menu;
import android.support.v4.internal.view.SupportMenu;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class MenuWrapperFactory
{
    public static Menu wrapSupportMenu(final Context context, final SupportMenu supportMenu) {
        return (Menu)new MenuWrapperICS(context, supportMenu);
    }
    
    public static MenuItem wrapSupportMenuItem(final Context context, final SupportMenuItem supportMenuItem) {
        if (Build$VERSION.SDK_INT >= 16) {
            return (MenuItem)new MenuItemWrapperJB(context, supportMenuItem);
        }
        return (MenuItem)new MenuItemWrapperICS(context, supportMenuItem);
    }
    
    public static SubMenu wrapSupportSubMenu(final Context context, final SupportSubMenu supportSubMenu) {
        return (SubMenu)new SubMenuWrapperICS(context, supportSubMenu);
    }
}
