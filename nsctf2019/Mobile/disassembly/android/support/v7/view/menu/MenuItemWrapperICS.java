// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.widget.FrameLayout;
import android.view.MenuItem$OnMenuItemClickListener;
import android.view.MenuItem$OnActionExpandListener;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.SubMenu;
import android.view.ContextMenu$ContextMenuInfo;
import android.content.Intent;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ActionProvider;
import android.content.Context;
import java.lang.reflect.Method;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;
import android.view.MenuItem;
import android.support.v4.internal.view.SupportMenuItem;

@RequiresApi(14)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class MenuItemWrapperICS extends BaseMenuWrapper<SupportMenuItem> implements MenuItem
{
    static final String LOG_TAG = "MenuItemWrapper";
    private Method mSetExclusiveCheckableMethod;
    
    MenuItemWrapperICS(final Context context, final SupportMenuItem supportMenuItem) {
        super(context, supportMenuItem);
    }
    
    public boolean collapseActionView() {
        return ((SupportMenuItem)this.mWrappedObject).collapseActionView();
    }
    
    ActionProviderWrapper createActionProviderWrapper(final ActionProvider actionProvider) {
        return new ActionProviderWrapper(this.mContext, actionProvider);
    }
    
    public boolean expandActionView() {
        return ((SupportMenuItem)this.mWrappedObject).expandActionView();
    }
    
    public ActionProvider getActionProvider() {
        final android.support.v4.view.ActionProvider supportActionProvider = ((SupportMenuItem)this.mWrappedObject).getSupportActionProvider();
        if (supportActionProvider instanceof ActionProviderWrapper) {
            return ((ActionProviderWrapper)supportActionProvider).mInner;
        }
        return null;
    }
    
    public View getActionView() {
        View view2;
        final View view = view2 = ((SupportMenuItem)this.mWrappedObject).getActionView();
        if (view instanceof CollapsibleActionViewWrapper) {
            view2 = ((CollapsibleActionViewWrapper)view).getWrappedView();
        }
        return view2;
    }
    
    public int getAlphabeticModifiers() {
        return ((SupportMenuItem)this.mWrappedObject).getAlphabeticModifiers();
    }
    
    public char getAlphabeticShortcut() {
        return ((SupportMenuItem)this.mWrappedObject).getAlphabeticShortcut();
    }
    
    public CharSequence getContentDescription() {
        return ((SupportMenuItem)this.mWrappedObject).getContentDescription();
    }
    
    public int getGroupId() {
        return ((SupportMenuItem)this.mWrappedObject).getGroupId();
    }
    
    public Drawable getIcon() {
        return ((SupportMenuItem)this.mWrappedObject).getIcon();
    }
    
    public ColorStateList getIconTintList() {
        return ((SupportMenuItem)this.mWrappedObject).getIconTintList();
    }
    
    public PorterDuff$Mode getIconTintMode() {
        return ((SupportMenuItem)this.mWrappedObject).getIconTintMode();
    }
    
    public Intent getIntent() {
        return ((SupportMenuItem)this.mWrappedObject).getIntent();
    }
    
    public int getItemId() {
        return ((SupportMenuItem)this.mWrappedObject).getItemId();
    }
    
    public ContextMenu$ContextMenuInfo getMenuInfo() {
        return ((SupportMenuItem)this.mWrappedObject).getMenuInfo();
    }
    
    public int getNumericModifiers() {
        return ((SupportMenuItem)this.mWrappedObject).getNumericModifiers();
    }
    
    public char getNumericShortcut() {
        return ((SupportMenuItem)this.mWrappedObject).getNumericShortcut();
    }
    
    public int getOrder() {
        return ((SupportMenuItem)this.mWrappedObject).getOrder();
    }
    
    public SubMenu getSubMenu() {
        return this.getSubMenuWrapper(((SupportMenuItem)this.mWrappedObject).getSubMenu());
    }
    
    public CharSequence getTitle() {
        return ((SupportMenuItem)this.mWrappedObject).getTitle();
    }
    
    public CharSequence getTitleCondensed() {
        return ((SupportMenuItem)this.mWrappedObject).getTitleCondensed();
    }
    
    public CharSequence getTooltipText() {
        return ((SupportMenuItem)this.mWrappedObject).getTooltipText();
    }
    
    public boolean hasSubMenu() {
        return ((SupportMenuItem)this.mWrappedObject).hasSubMenu();
    }
    
    public boolean isActionViewExpanded() {
        return ((SupportMenuItem)this.mWrappedObject).isActionViewExpanded();
    }
    
    public boolean isCheckable() {
        return ((SupportMenuItem)this.mWrappedObject).isCheckable();
    }
    
    public boolean isChecked() {
        return ((SupportMenuItem)this.mWrappedObject).isChecked();
    }
    
    public boolean isEnabled() {
        return ((SupportMenuItem)this.mWrappedObject).isEnabled();
    }
    
    public boolean isVisible() {
        return ((SupportMenuItem)this.mWrappedObject).isVisible();
    }
    
    public MenuItem setActionProvider(final ActionProvider actionProvider) {
        final SupportMenuItem supportMenuItem = (SupportMenuItem)this.mWrappedObject;
        ActionProviderWrapper actionProviderWrapper;
        if (actionProvider != null) {
            actionProviderWrapper = this.createActionProviderWrapper(actionProvider);
        }
        else {
            actionProviderWrapper = null;
        }
        supportMenuItem.setSupportActionProvider(actionProviderWrapper);
        return (MenuItem)this;
    }
    
    public MenuItem setActionView(final int actionView) {
        ((SupportMenuItem)this.mWrappedObject).setActionView(actionView);
        final View actionView2 = ((SupportMenuItem)this.mWrappedObject).getActionView();
        if (actionView2 instanceof CollapsibleActionView) {
            ((SupportMenuItem)this.mWrappedObject).setActionView((View)new CollapsibleActionViewWrapper(actionView2));
        }
        return (MenuItem)this;
    }
    
    public MenuItem setActionView(final View view) {
        Object actionView = view;
        if (view instanceof CollapsibleActionView) {
            actionView = new CollapsibleActionViewWrapper(view);
        }
        ((SupportMenuItem)this.mWrappedObject).setActionView((View)actionView);
        return (MenuItem)this;
    }
    
    public MenuItem setAlphabeticShortcut(final char alphabeticShortcut) {
        ((SupportMenuItem)this.mWrappedObject).setAlphabeticShortcut(alphabeticShortcut);
        return (MenuItem)this;
    }
    
    public MenuItem setAlphabeticShortcut(final char c, final int n) {
        ((SupportMenuItem)this.mWrappedObject).setAlphabeticShortcut(c, n);
        return (MenuItem)this;
    }
    
    public MenuItem setCheckable(final boolean checkable) {
        ((SupportMenuItem)this.mWrappedObject).setCheckable(checkable);
        return (MenuItem)this;
    }
    
    public MenuItem setChecked(final boolean checked) {
        ((SupportMenuItem)this.mWrappedObject).setChecked(checked);
        return (MenuItem)this;
    }
    
    public MenuItem setContentDescription(final CharSequence contentDescription) {
        ((SupportMenuItem)this.mWrappedObject).setContentDescription(contentDescription);
        return (MenuItem)this;
    }
    
    public MenuItem setEnabled(final boolean enabled) {
        ((SupportMenuItem)this.mWrappedObject).setEnabled(enabled);
        return (MenuItem)this;
    }
    
    public void setExclusiveCheckable(final boolean b) {
        try {
            if (this.mSetExclusiveCheckableMethod == null) {
                this.mSetExclusiveCheckableMethod = ((SupportMenuItem)this.mWrappedObject).getClass().getDeclaredMethod("setExclusiveCheckable", Boolean.TYPE);
            }
            this.mSetExclusiveCheckableMethod.invoke(this.mWrappedObject, b);
        }
        catch (Exception ex) {
            Log.w("MenuItemWrapper", "Error while calling setExclusiveCheckable", (Throwable)ex);
        }
    }
    
    public MenuItem setIcon(final int icon) {
        ((SupportMenuItem)this.mWrappedObject).setIcon(icon);
        return (MenuItem)this;
    }
    
    public MenuItem setIcon(final Drawable icon) {
        ((SupportMenuItem)this.mWrappedObject).setIcon(icon);
        return (MenuItem)this;
    }
    
    public MenuItem setIconTintList(final ColorStateList iconTintList) {
        ((SupportMenuItem)this.mWrappedObject).setIconTintList(iconTintList);
        return (MenuItem)this;
    }
    
    public MenuItem setIconTintMode(final PorterDuff$Mode iconTintMode) {
        ((SupportMenuItem)this.mWrappedObject).setIconTintMode(iconTintMode);
        return (MenuItem)this;
    }
    
    public MenuItem setIntent(final Intent intent) {
        ((SupportMenuItem)this.mWrappedObject).setIntent(intent);
        return (MenuItem)this;
    }
    
    public MenuItem setNumericShortcut(final char numericShortcut) {
        ((SupportMenuItem)this.mWrappedObject).setNumericShortcut(numericShortcut);
        return (MenuItem)this;
    }
    
    public MenuItem setNumericShortcut(final char c, final int n) {
        ((SupportMenuItem)this.mWrappedObject).setNumericShortcut(c, n);
        return (MenuItem)this;
    }
    
    public MenuItem setOnActionExpandListener(final MenuItem$OnActionExpandListener menuItem$OnActionExpandListener) {
        final SupportMenuItem supportMenuItem = (SupportMenuItem)this.mWrappedObject;
        Object onActionExpandListener;
        if (menuItem$OnActionExpandListener != null) {
            onActionExpandListener = new OnActionExpandListenerWrapper(menuItem$OnActionExpandListener);
        }
        else {
            onActionExpandListener = null;
        }
        supportMenuItem.setOnActionExpandListener((MenuItem$OnActionExpandListener)onActionExpandListener);
        return (MenuItem)this;
    }
    
    public MenuItem setOnMenuItemClickListener(final MenuItem$OnMenuItemClickListener menuItem$OnMenuItemClickListener) {
        final SupportMenuItem supportMenuItem = (SupportMenuItem)this.mWrappedObject;
        Object onMenuItemClickListener;
        if (menuItem$OnMenuItemClickListener != null) {
            onMenuItemClickListener = new OnMenuItemClickListenerWrapper(menuItem$OnMenuItemClickListener);
        }
        else {
            onMenuItemClickListener = null;
        }
        supportMenuItem.setOnMenuItemClickListener((MenuItem$OnMenuItemClickListener)onMenuItemClickListener);
        return (MenuItem)this;
    }
    
    public MenuItem setShortcut(final char c, final char c2) {
        ((SupportMenuItem)this.mWrappedObject).setShortcut(c, c2);
        return (MenuItem)this;
    }
    
    public MenuItem setShortcut(final char c, final char c2, final int n, final int n2) {
        ((SupportMenuItem)this.mWrappedObject).setShortcut(c, c2, n, n2);
        return (MenuItem)this;
    }
    
    public void setShowAsAction(final int showAsAction) {
        ((SupportMenuItem)this.mWrappedObject).setShowAsAction(showAsAction);
    }
    
    public MenuItem setShowAsActionFlags(final int showAsActionFlags) {
        ((SupportMenuItem)this.mWrappedObject).setShowAsActionFlags(showAsActionFlags);
        return (MenuItem)this;
    }
    
    public MenuItem setTitle(final int title) {
        ((SupportMenuItem)this.mWrappedObject).setTitle(title);
        return (MenuItem)this;
    }
    
    public MenuItem setTitle(final CharSequence title) {
        ((SupportMenuItem)this.mWrappedObject).setTitle(title);
        return (MenuItem)this;
    }
    
    public MenuItem setTitleCondensed(final CharSequence titleCondensed) {
        ((SupportMenuItem)this.mWrappedObject).setTitleCondensed(titleCondensed);
        return (MenuItem)this;
    }
    
    public MenuItem setTooltipText(final CharSequence tooltipText) {
        ((SupportMenuItem)this.mWrappedObject).setTooltipText(tooltipText);
        return (MenuItem)this;
    }
    
    public MenuItem setVisible(final boolean visible) {
        return ((SupportMenuItem)this.mWrappedObject).setVisible(visible);
    }
    
    class ActionProviderWrapper extends ActionProvider
    {
        final android.view.ActionProvider mInner;
        
        public ActionProviderWrapper(final Context context, final android.view.ActionProvider mInner) {
            super(context);
            this.mInner = mInner;
        }
        
        @Override
        public boolean hasSubMenu() {
            return this.mInner.hasSubMenu();
        }
        
        @Override
        public View onCreateActionView() {
            return this.mInner.onCreateActionView();
        }
        
        @Override
        public boolean onPerformDefaultAction() {
            return this.mInner.onPerformDefaultAction();
        }
        
        @Override
        public void onPrepareSubMenu(final SubMenu subMenu) {
            this.mInner.onPrepareSubMenu(MenuItemWrapperICS.this.getSubMenuWrapper(subMenu));
        }
    }
    
    static class CollapsibleActionViewWrapper extends FrameLayout implements CollapsibleActionView
    {
        final android.view.CollapsibleActionView mWrappedView;
        
        CollapsibleActionViewWrapper(final View view) {
            super(view.getContext());
            this.mWrappedView = (android.view.CollapsibleActionView)view;
            this.addView(view);
        }
        
        View getWrappedView() {
            return (View)this.mWrappedView;
        }
        
        public void onActionViewCollapsed() {
            this.mWrappedView.onActionViewCollapsed();
        }
        
        public void onActionViewExpanded() {
            this.mWrappedView.onActionViewExpanded();
        }
    }
    
    private class OnActionExpandListenerWrapper extends BaseWrapper<MenuItem$OnActionExpandListener> implements MenuItem$OnActionExpandListener
    {
        OnActionExpandListenerWrapper(final MenuItem$OnActionExpandListener menuItem$OnActionExpandListener) {
            super(menuItem$OnActionExpandListener);
        }
        
        public boolean onMenuItemActionCollapse(final MenuItem menuItem) {
            return ((MenuItem$OnActionExpandListener)this.mWrappedObject).onMenuItemActionCollapse(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
        
        public boolean onMenuItemActionExpand(final MenuItem menuItem) {
            return ((MenuItem$OnActionExpandListener)this.mWrappedObject).onMenuItemActionExpand(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }
    
    private class OnMenuItemClickListenerWrapper extends BaseWrapper<MenuItem$OnMenuItemClickListener> implements MenuItem$OnMenuItemClickListener
    {
        OnMenuItemClickListenerWrapper(final MenuItem$OnMenuItemClickListener menuItem$OnMenuItemClickListener) {
            super(menuItem$OnMenuItemClickListener);
        }
        
        public boolean onMenuItemClick(final MenuItem menuItem) {
            return ((MenuItem$OnMenuItemClickListener)this.mWrappedObject).onMenuItemClick(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }
}
