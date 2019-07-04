// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.view.View;
import android.widget.AdapterView;
import android.view.MenuItem;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListView;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class ExpandedMenuView extends ListView implements ItemInvoker, MenuView, AdapterView$OnItemClickListener
{
    private static final int[] TINT_ATTRS;
    private int mAnimations;
    private MenuBuilder mMenu;
    
    static {
        TINT_ATTRS = new int[] { 16842964, 16843049 };
    }
    
    public ExpandedMenuView(final Context context, final AttributeSet set) {
        this(context, set, 16842868);
    }
    
    public ExpandedMenuView(final Context context, final AttributeSet set, final int n) {
        super(context, set);
        this.setOnItemClickListener((AdapterView$OnItemClickListener)this);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, ExpandedMenuView.TINT_ATTRS, n, 0);
        if (obtainStyledAttributes.hasValue(0)) {
            this.setBackgroundDrawable(obtainStyledAttributes.getDrawable(0));
        }
        if (obtainStyledAttributes.hasValue(1)) {
            this.setDivider(obtainStyledAttributes.getDrawable(1));
        }
        obtainStyledAttributes.recycle();
    }
    
    public int getWindowAnimations() {
        return this.mAnimations;
    }
    
    public void initialize(final MenuBuilder mMenu) {
        this.mMenu = mMenu;
    }
    
    public boolean invokeItem(final MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction((MenuItem)menuItemImpl, 0);
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.setChildrenDrawingCacheEnabled(false);
    }
    
    public void onItemClick(final AdapterView adapterView, final View view, final int n, final long n2) {
        this.invokeItem((MenuItemImpl)this.getAdapter().getItem(n));
    }
}
