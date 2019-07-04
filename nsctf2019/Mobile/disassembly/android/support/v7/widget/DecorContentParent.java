// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.view.Window$Callback;
import android.support.v7.view.menu.MenuPresenter;
import android.view.Menu;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.SparseArray;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface DecorContentParent
{
    boolean canShowOverflowMenu();
    
    void dismissPopups();
    
    CharSequence getTitle();
    
    boolean hasIcon();
    
    boolean hasLogo();
    
    boolean hideOverflowMenu();
    
    void initFeature(final int p0);
    
    boolean isOverflowMenuShowPending();
    
    boolean isOverflowMenuShowing();
    
    void restoreToolbarHierarchyState(final SparseArray<Parcelable> p0);
    
    void saveToolbarHierarchyState(final SparseArray<Parcelable> p0);
    
    void setIcon(final int p0);
    
    void setIcon(final Drawable p0);
    
    void setLogo(final int p0);
    
    void setMenu(final Menu p0, final MenuPresenter.Callback p1);
    
    void setMenuPrepared();
    
    void setUiOptions(final int p0);
    
    void setWindowCallback(final Window$Callback p0);
    
    void setWindowTitle(final CharSequence p0);
    
    boolean showOverflowMenu();
}
