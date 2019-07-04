// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.content.ActivityNotFoundException;
import android.util.Log;
import android.os.Build$VERSION;
import android.view.SubMenu;
import android.view.ViewDebug$CapturedViewProperty;
import android.support.v7.content.res.AppCompatResources;
import android.view.MenuItem;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem$OnActionExpandListener;
import android.view.ContextMenu$ContextMenuInfo;
import android.content.Intent;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.MenuItem$OnMenuItemClickListener;
import android.view.View;
import android.support.v4.view.ActionProvider;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenuItem;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class MenuItemImpl implements SupportMenuItem
{
    private static final int CHECKABLE = 1;
    private static final int CHECKED = 2;
    private static final int ENABLED = 16;
    private static final int EXCLUSIVE = 4;
    private static final int HIDDEN = 8;
    private static final int IS_ACTION = 32;
    static final int NO_ICON = 0;
    private static final int SHOW_AS_ACTION_MASK = 3;
    private static final String TAG = "MenuItemImpl";
    private static String sDeleteShortcutLabel;
    private static String sEnterShortcutLabel;
    private static String sPrependShortcutLabel;
    private static String sSpaceShortcutLabel;
    private ActionProvider mActionProvider;
    private View mActionView;
    private final int mCategoryOrder;
    private MenuItem$OnMenuItemClickListener mClickListener;
    private CharSequence mContentDescription;
    private int mFlags;
    private final int mGroup;
    private boolean mHasIconTint;
    private boolean mHasIconTintMode;
    private Drawable mIconDrawable;
    private int mIconResId;
    private ColorStateList mIconTintList;
    private PorterDuff$Mode mIconTintMode;
    private final int mId;
    private Intent mIntent;
    private boolean mIsActionViewExpanded;
    private Runnable mItemCallback;
    MenuBuilder mMenu;
    private ContextMenu$ContextMenuInfo mMenuInfo;
    private boolean mNeedToApplyIconTint;
    private MenuItem$OnActionExpandListener mOnActionExpandListener;
    private final int mOrdering;
    private char mShortcutAlphabeticChar;
    private int mShortcutAlphabeticModifiers;
    private char mShortcutNumericChar;
    private int mShortcutNumericModifiers;
    private int mShowAsAction;
    private SubMenuBuilder mSubMenu;
    private CharSequence mTitle;
    private CharSequence mTitleCondensed;
    private CharSequence mTooltipText;
    
    MenuItemImpl(final MenuBuilder mMenu, final int mGroup, final int mId, final int mCategoryOrder, final int mOrdering, final CharSequence mTitle, final int mShowAsAction) {
        this.mShortcutNumericModifiers = 4096;
        this.mShortcutAlphabeticModifiers = 4096;
        this.mIconResId = 0;
        this.mIconTintList = null;
        this.mIconTintMode = null;
        this.mHasIconTint = false;
        this.mHasIconTintMode = false;
        this.mNeedToApplyIconTint = false;
        this.mFlags = 16;
        this.mShowAsAction = 0;
        this.mIsActionViewExpanded = false;
        this.mMenu = mMenu;
        this.mId = mId;
        this.mGroup = mGroup;
        this.mCategoryOrder = mCategoryOrder;
        this.mOrdering = mOrdering;
        this.mTitle = mTitle;
        this.mShowAsAction = mShowAsAction;
    }
    
    private Drawable applyIconTintIfNecessary(final Drawable drawable) {
        Drawable mutate = drawable;
        if (drawable != null) {
            mutate = drawable;
            if (this.mNeedToApplyIconTint) {
                if (!this.mHasIconTint) {
                    mutate = drawable;
                    if (!this.mHasIconTintMode) {
                        return mutate;
                    }
                }
                mutate = DrawableCompat.wrap(drawable).mutate();
                if (this.mHasIconTint) {
                    DrawableCompat.setTintList(mutate, this.mIconTintList);
                }
                if (this.mHasIconTintMode) {
                    DrawableCompat.setTintMode(mutate, this.mIconTintMode);
                }
                this.mNeedToApplyIconTint = false;
            }
        }
        return mutate;
    }
    
    public void actionFormatChanged() {
        this.mMenu.onItemActionRequestChanged(this);
    }
    
    @Override
    public boolean collapseActionView() {
        if ((this.mShowAsAction & 0x8) != 0x0) {
            if (this.mActionView == null) {
                return true;
            }
            if (this.mOnActionExpandListener == null || this.mOnActionExpandListener.onMenuItemActionCollapse((MenuItem)this)) {
                return this.mMenu.collapseItemActionView(this);
            }
        }
        return false;
    }
    
    @Override
    public boolean expandActionView() {
        return this.hasCollapsibleActionView() && (this.mOnActionExpandListener == null || this.mOnActionExpandListener.onMenuItemActionExpand((MenuItem)this)) && this.mMenu.expandItemActionView(this);
    }
    
    public android.view.ActionProvider getActionProvider() {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.getActionProvider()");
    }
    
    @Override
    public View getActionView() {
        if (this.mActionView != null) {
            return this.mActionView;
        }
        if (this.mActionProvider != null) {
            return this.mActionView = this.mActionProvider.onCreateActionView((MenuItem)this);
        }
        return null;
    }
    
    @Override
    public int getAlphabeticModifiers() {
        return this.mShortcutAlphabeticModifiers;
    }
    
    public char getAlphabeticShortcut() {
        return this.mShortcutAlphabeticChar;
    }
    
    Runnable getCallback() {
        return this.mItemCallback;
    }
    
    @Override
    public CharSequence getContentDescription() {
        return this.mContentDescription;
    }
    
    public int getGroupId() {
        return this.mGroup;
    }
    
    public Drawable getIcon() {
        if (this.mIconDrawable != null) {
            return this.applyIconTintIfNecessary(this.mIconDrawable);
        }
        if (this.mIconResId != 0) {
            final Drawable drawable = AppCompatResources.getDrawable(this.mMenu.getContext(), this.mIconResId);
            this.mIconResId = 0;
            this.mIconDrawable = drawable;
            return this.applyIconTintIfNecessary(drawable);
        }
        return null;
    }
    
    @Override
    public ColorStateList getIconTintList() {
        return this.mIconTintList;
    }
    
    @Override
    public PorterDuff$Mode getIconTintMode() {
        return this.mIconTintMode;
    }
    
    public Intent getIntent() {
        return this.mIntent;
    }
    
    @ViewDebug$CapturedViewProperty
    public int getItemId() {
        return this.mId;
    }
    
    public ContextMenu$ContextMenuInfo getMenuInfo() {
        return this.mMenuInfo;
    }
    
    @Override
    public int getNumericModifiers() {
        return this.mShortcutNumericModifiers;
    }
    
    public char getNumericShortcut() {
        return this.mShortcutNumericChar;
    }
    
    public int getOrder() {
        return this.mCategoryOrder;
    }
    
    public int getOrdering() {
        return this.mOrdering;
    }
    
    char getShortcut() {
        if (this.mMenu.isQwertyMode()) {
            return this.mShortcutAlphabeticChar;
        }
        return this.mShortcutNumericChar;
    }
    
    String getShortcutLabel() {
        final char shortcut = this.getShortcut();
        if (shortcut == '\0') {
            return "";
        }
        final StringBuilder sb = new StringBuilder(MenuItemImpl.sPrependShortcutLabel);
        switch (shortcut) {
            default: {
                sb.append(shortcut);
                break;
            }
            case 10: {
                sb.append(MenuItemImpl.sEnterShortcutLabel);
                break;
            }
            case 8: {
                sb.append(MenuItemImpl.sDeleteShortcutLabel);
                break;
            }
            case 32: {
                sb.append(MenuItemImpl.sSpaceShortcutLabel);
                break;
            }
        }
        return sb.toString();
    }
    
    public SubMenu getSubMenu() {
        return (SubMenu)this.mSubMenu;
    }
    
    @Override
    public ActionProvider getSupportActionProvider() {
        return this.mActionProvider;
    }
    
    @ViewDebug$CapturedViewProperty
    public CharSequence getTitle() {
        return this.mTitle;
    }
    
    public CharSequence getTitleCondensed() {
        CharSequence charSequence;
        if (this.mTitleCondensed != null) {
            charSequence = this.mTitleCondensed;
        }
        else {
            charSequence = this.mTitle;
        }
        CharSequence string = charSequence;
        if (Build$VERSION.SDK_INT < 18 && (string = charSequence) != null) {
            string = charSequence;
            if (!(charSequence instanceof String)) {
                string = charSequence.toString();
            }
        }
        return string;
    }
    
    CharSequence getTitleForItemView(final MenuView.ItemView itemView) {
        if (itemView != null && itemView.prefersCondensedTitle()) {
            return this.getTitleCondensed();
        }
        return this.getTitle();
    }
    
    @Override
    public CharSequence getTooltipText() {
        return this.mTooltipText;
    }
    
    public boolean hasCollapsibleActionView() {
        boolean b = false;
        if ((this.mShowAsAction & 0x8) != 0x0) {
            if (this.mActionView == null && this.mActionProvider != null) {
                this.mActionView = this.mActionProvider.onCreateActionView((MenuItem)this);
            }
            b = b;
            if (this.mActionView != null) {
                b = true;
            }
        }
        return b;
    }
    
    public boolean hasSubMenu() {
        return this.mSubMenu != null;
    }
    
    public boolean invoke() {
        if ((this.mClickListener == null || !this.mClickListener.onMenuItemClick((MenuItem)this)) && !this.mMenu.dispatchMenuItemSelected(this.mMenu.getRootMenu(), (MenuItem)this)) {
            if (this.mItemCallback != null) {
                this.mItemCallback.run();
                return true;
            }
            if (this.mIntent != null) {
                try {
                    this.mMenu.getContext().startActivity(this.mIntent);
                    return true;
                }
                catch (ActivityNotFoundException ex) {
                    Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", (Throwable)ex);
                }
            }
            if (this.mActionProvider == null || !this.mActionProvider.onPerformDefaultAction()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isActionButton() {
        return (this.mFlags & 0x20) == 0x20;
    }
    
    @Override
    public boolean isActionViewExpanded() {
        return this.mIsActionViewExpanded;
    }
    
    public boolean isCheckable() {
        return (this.mFlags & 0x1) == 0x1;
    }
    
    public boolean isChecked() {
        return (this.mFlags & 0x2) == 0x2;
    }
    
    public boolean isEnabled() {
        return (this.mFlags & 0x10) != 0x0;
    }
    
    public boolean isExclusiveCheckable() {
        return (this.mFlags & 0x4) != 0x0;
    }
    
    public boolean isVisible() {
        if (this.mActionProvider != null && this.mActionProvider.overridesItemVisibility()) {
            if ((this.mFlags & 0x8) != 0x0 || !this.mActionProvider.isVisible()) {
                return false;
            }
        }
        else if ((this.mFlags & 0x8) != 0x0) {
            return false;
        }
        return true;
    }
    
    public boolean requestsActionButton() {
        return (this.mShowAsAction & 0x1) == 0x1;
    }
    
    public boolean requiresActionButton() {
        return (this.mShowAsAction & 0x2) == 0x2;
    }
    
    public MenuItem setActionProvider(final android.view.ActionProvider actionProvider) {
        throw new UnsupportedOperationException("This is not supported, use MenuItemCompat.setActionProvider()");
    }
    
    public SupportMenuItem setActionView(final int n) {
        final Context context = this.mMenu.getContext();
        this.setActionView(LayoutInflater.from(context).inflate(n, (ViewGroup)new LinearLayout(context), false));
        return this;
    }
    
    public SupportMenuItem setActionView(final View mActionView) {
        this.mActionView = mActionView;
        this.mActionProvider = null;
        if (mActionView != null && mActionView.getId() == -1 && this.mId > 0) {
            mActionView.setId(this.mId);
        }
        this.mMenu.onItemActionRequestChanged(this);
        return this;
    }
    
    public void setActionViewExpanded(final boolean mIsActionViewExpanded) {
        this.mIsActionViewExpanded = mIsActionViewExpanded;
        this.mMenu.onItemsChanged(false);
    }
    
    public MenuItem setAlphabeticShortcut(final char c) {
        if (this.mShortcutAlphabeticChar == c) {
            return (MenuItem)this;
        }
        this.mShortcutAlphabeticChar = Character.toLowerCase(c);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setAlphabeticShortcut(final char c, final int n) {
        if (this.mShortcutAlphabeticChar == c && this.mShortcutAlphabeticModifiers == n) {
            return (MenuItem)this;
        }
        this.mShortcutAlphabeticChar = Character.toLowerCase(c);
        this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(n);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setCallback(final Runnable mItemCallback) {
        this.mItemCallback = mItemCallback;
        return (MenuItem)this;
    }
    
    public MenuItem setCheckable(final boolean b) {
        final int mFlags = this.mFlags;
        final int mFlags2 = this.mFlags;
        boolean b2;
        if (b) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        this.mFlags = ((b2 ? 1 : 0) | (mFlags2 & 0xFFFFFFFE));
        if (mFlags != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
        return (MenuItem)this;
    }
    
    public MenuItem setChecked(final boolean checkedInt) {
        if ((this.mFlags & 0x4) != 0x0) {
            this.mMenu.setExclusiveItemChecked((MenuItem)this);
            return (MenuItem)this;
        }
        this.setCheckedInt(checkedInt);
        return (MenuItem)this;
    }
    
    void setCheckedInt(final boolean b) {
        final int mFlags = this.mFlags;
        final int mFlags2 = this.mFlags;
        int n;
        if (b) {
            n = 2;
        }
        else {
            n = 0;
        }
        this.mFlags = (n | (mFlags2 & 0xFFFFFFFD));
        if (mFlags != this.mFlags) {
            this.mMenu.onItemsChanged(false);
        }
    }
    
    @Override
    public SupportMenuItem setContentDescription(final CharSequence mContentDescription) {
        this.mContentDescription = mContentDescription;
        this.mMenu.onItemsChanged(false);
        return this;
    }
    
    public MenuItem setEnabled(final boolean b) {
        if (b) {
            this.mFlags |= 0x10;
        }
        else {
            this.mFlags &= 0xFFFFFFEF;
        }
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public void setExclusiveCheckable(final boolean b) {
        final int mFlags = this.mFlags;
        int n;
        if (b) {
            n = 4;
        }
        else {
            n = 0;
        }
        this.mFlags = (n | (mFlags & 0xFFFFFFFB));
    }
    
    public MenuItem setIcon(final int mIconResId) {
        this.mIconDrawable = null;
        this.mIconResId = mIconResId;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setIcon(final Drawable mIconDrawable) {
        this.mIconResId = 0;
        this.mIconDrawable = mIconDrawable;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setIconTintList(@Nullable final ColorStateList mIconTintList) {
        this.mIconTintList = mIconTintList;
        this.mHasIconTint = true;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setIconTintMode(final PorterDuff$Mode mIconTintMode) {
        this.mIconTintMode = mIconTintMode;
        this.mHasIconTintMode = true;
        this.mNeedToApplyIconTint = true;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setIntent(final Intent mIntent) {
        this.mIntent = mIntent;
        return (MenuItem)this;
    }
    
    public void setIsActionButton(final boolean b) {
        if (b) {
            this.mFlags |= 0x20;
            return;
        }
        this.mFlags &= 0xFFFFFFDF;
    }
    
    void setMenuInfo(final ContextMenu$ContextMenuInfo mMenuInfo) {
        this.mMenuInfo = mMenuInfo;
    }
    
    public MenuItem setNumericShortcut(final char mShortcutNumericChar) {
        if (this.mShortcutNumericChar == mShortcutNumericChar) {
            return (MenuItem)this;
        }
        this.mShortcutNumericChar = mShortcutNumericChar;
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setNumericShortcut(final char mShortcutNumericChar, final int n) {
        if (this.mShortcutNumericChar == mShortcutNumericChar && this.mShortcutNumericModifiers == n) {
            return (MenuItem)this;
        }
        this.mShortcutNumericChar = mShortcutNumericChar;
        this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(n);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    public MenuItem setOnActionExpandListener(final MenuItem$OnActionExpandListener mOnActionExpandListener) {
        this.mOnActionExpandListener = mOnActionExpandListener;
        return (MenuItem)this;
    }
    
    public MenuItem setOnMenuItemClickListener(final MenuItem$OnMenuItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
        return (MenuItem)this;
    }
    
    public MenuItem setShortcut(final char mShortcutNumericChar, final char c) {
        this.mShortcutNumericChar = mShortcutNumericChar;
        this.mShortcutAlphabeticChar = Character.toLowerCase(c);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public MenuItem setShortcut(final char mShortcutNumericChar, final char c, final int n, final int n2) {
        this.mShortcutNumericChar = mShortcutNumericChar;
        this.mShortcutNumericModifiers = KeyEvent.normalizeMetaState(n);
        this.mShortcutAlphabeticChar = Character.toLowerCase(c);
        this.mShortcutAlphabeticModifiers = KeyEvent.normalizeMetaState(n2);
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public void setShowAsAction(final int mShowAsAction) {
        switch (mShowAsAction & 0x3) {
            default: {
                throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
            }
            case 0:
            case 1:
            case 2: {
                this.mShowAsAction = mShowAsAction;
                this.mMenu.onItemActionRequestChanged(this);
            }
        }
    }
    
    public SupportMenuItem setShowAsActionFlags(final int showAsAction) {
        this.setShowAsAction(showAsAction);
        return this;
    }
    
    public void setSubMenu(final SubMenuBuilder mSubMenu) {
        (this.mSubMenu = mSubMenu).setHeaderTitle(this.getTitle());
    }
    
    @Override
    public SupportMenuItem setSupportActionProvider(final ActionProvider mActionProvider) {
        if (this.mActionProvider != null) {
            this.mActionProvider.reset();
        }
        this.mActionView = null;
        this.mActionProvider = mActionProvider;
        this.mMenu.onItemsChanged(true);
        if (this.mActionProvider != null) {
            this.mActionProvider.setVisibilityListener((ActionProvider.VisibilityListener)new ActionProvider.VisibilityListener() {
                @Override
                public void onActionProviderVisibilityChanged(final boolean b) {
                    MenuItemImpl.this.mMenu.onItemVisibleChanged(MenuItemImpl.this);
                }
            });
        }
        return this;
    }
    
    public MenuItem setTitle(final int n) {
        return this.setTitle(this.mMenu.getContext().getString(n));
    }
    
    public MenuItem setTitle(final CharSequence charSequence) {
        this.mTitle = charSequence;
        this.mMenu.onItemsChanged(false);
        if (this.mSubMenu != null) {
            this.mSubMenu.setHeaderTitle(charSequence);
        }
        return (MenuItem)this;
    }
    
    public MenuItem setTitleCondensed(CharSequence mTitle) {
        this.mTitleCondensed = mTitle;
        if (mTitle == null) {
            mTitle = this.mTitle;
        }
        this.mMenu.onItemsChanged(false);
        return (MenuItem)this;
    }
    
    @Override
    public SupportMenuItem setTooltipText(final CharSequence mTooltipText) {
        this.mTooltipText = mTooltipText;
        this.mMenu.onItemsChanged(false);
        return this;
    }
    
    public MenuItem setVisible(final boolean visibleInt) {
        if (this.setVisibleInt(visibleInt)) {
            this.mMenu.onItemVisibleChanged(this);
        }
        return (MenuItem)this;
    }
    
    boolean setVisibleInt(final boolean b) {
        final boolean b2 = false;
        final int mFlags = this.mFlags;
        final int mFlags2 = this.mFlags;
        int n;
        if (b) {
            n = 0;
        }
        else {
            n = 8;
        }
        this.mFlags = (n | (mFlags2 & 0xFFFFFFF7));
        boolean b3 = b2;
        if (mFlags != this.mFlags) {
            b3 = true;
        }
        return b3;
    }
    
    public boolean shouldShowIcon() {
        return this.mMenu.getOptionalIconsVisible();
    }
    
    boolean shouldShowShortcut() {
        return this.mMenu.isShortcutsVisible() && this.getShortcut() != '\0';
    }
    
    public boolean showsTextAsAction() {
        return (this.mShowAsAction & 0x4) == 0x4;
    }
    
    @Override
    public String toString() {
        if (this.mTitle != null) {
            return this.mTitle.toString();
        }
        return null;
    }
}
