// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View$OnTouchListener;
import android.util.AttributeSet;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.transition.ActionBarTransition;
import android.support.v7.view.menu.SubMenuBuilder;
import android.os.Parcelable;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.view.ActionBarPolicy;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.view.ViewGroup$LayoutParams;
import java.util.ArrayList;
import android.view.View$MeasureSpec;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuItemImpl;
import android.view.ViewGroup;
import android.view.MenuItem;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.appcompat.R;
import android.content.Context;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.support.v4.view.ActionProvider;
import android.support.v7.view.menu.BaseMenuPresenter;

class ActionMenuPresenter extends BaseMenuPresenter implements SubUiVisibilityListener
{
    private static final String TAG = "ActionMenuPresenter";
    private final SparseBooleanArray mActionButtonGroups;
    ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private boolean mExpandedActionViewsExclusive;
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    OverflowMenuButton mOverflowButton;
    OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    final PopupPresenterCallback mPopupPresenterCallback;
    OpenOverflowRunnable mPostedOpenRunnable;
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private View mScrapActionButtonView;
    private boolean mStrictWidthLimit;
    private int mWidthLimit;
    private boolean mWidthLimitSet;
    
    public ActionMenuPresenter(final Context context) {
        super(context, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
        this.mActionButtonGroups = new SparseBooleanArray();
        this.mPopupPresenterCallback = new PopupPresenterCallback();
    }
    
    private View findViewForItem(final MenuItem menuItem) {
        final ViewGroup viewGroup = (ViewGroup)this.mMenuView;
        if (viewGroup != null) {
            for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = viewGroup.getChildAt(i);
                if (child instanceof MenuView.ItemView) {
                    final View view = child;
                    if (((MenuView.ItemView)child).getItemData() == menuItem) {
                        return view;
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    @Override
    public void bindItemView(final MenuItemImpl menuItemImpl, final MenuView.ItemView itemView) {
        itemView.initialize(menuItemImpl, 0);
        final ActionMenuView itemInvoker = (ActionMenuView)this.mMenuView;
        final ActionMenuItemView actionMenuItemView = (ActionMenuItemView)itemView;
        actionMenuItemView.setItemInvoker(itemInvoker);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback();
        }
        actionMenuItemView.setPopupCallback((ActionMenuItemView.PopupCallback)this.mPopupCallback);
    }
    
    public boolean dismissPopupMenus() {
        return this.hideOverflowMenu() | this.hideSubMenus();
    }
    
    public boolean filterLeftoverView(final ViewGroup viewGroup, final int n) {
        return viewGroup.getChildAt(n) != this.mOverflowButton && super.filterLeftoverView(viewGroup, n);
    }
    
    @Override
    public boolean flagActionItems() {
        ArrayList<MenuItemImpl> visibleItems;
        int size;
        if (this.mMenu != null) {
            visibleItems = this.mMenu.getVisibleItems();
            size = visibleItems.size();
        }
        else {
            visibleItems = null;
            size = 0;
        }
        int mMaxItems = this.mMaxItems;
        final int mActionItemWidthLimit = this.mActionItemWidthLimit;
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
        final ViewGroup viewGroup = (ViewGroup)this.mMenuView;
        int n = 0;
        int n2 = 0;
        final boolean b = false;
        boolean b2 = false;
        int n3;
        for (int i = 0; i < size; ++i, mMaxItems = n3) {
            final MenuItemImpl menuItemImpl = visibleItems.get(i);
            if (menuItemImpl.requiresActionButton()) {
                ++n;
            }
            else if (menuItemImpl.requestsActionButton()) {
                ++n2;
            }
            else {
                b2 = true;
            }
            n3 = mMaxItems;
            if (this.mExpandedActionViewsExclusive) {
                n3 = mMaxItems;
                if (menuItemImpl.isActionViewExpanded()) {
                    n3 = 0;
                }
            }
        }
        int n4 = mMaxItems;
        if (this.mReserveOverflow && (b2 || n + n2 > (n4 = mMaxItems))) {
            n4 = mMaxItems - 1;
        }
        int n5 = n4 - n;
        final SparseBooleanArray mActionButtonGroups = this.mActionButtonGroups;
        mActionButtonGroups.clear();
        int n6 = 0;
        int n7 = 0;
        if (this.mStrictWidthLimit) {
            n7 = mActionItemWidthLimit / this.mMinCellSize;
            n6 = this.mMinCellSize + mActionItemWidthLimit % this.mMinCellSize / n7;
        }
        final int n8 = 0;
        int n9 = mActionItemWidthLimit;
        int j = n8;
        int n10 = b ? 1 : 0;
        while (j < size) {
            final MenuItemImpl menuItemImpl2 = visibleItems.get(j);
            int n11;
            if (menuItemImpl2.requiresActionButton()) {
                final View itemView = this.getItemView(menuItemImpl2, this.mScrapActionButtonView, viewGroup);
                if (this.mScrapActionButtonView == null) {
                    this.mScrapActionButtonView = itemView;
                }
                if (this.mStrictWidthLimit) {
                    n7 -= ActionMenuView.measureChildForCells(itemView, n6, n7, measureSpec, 0);
                }
                else {
                    itemView.measure(measureSpec, measureSpec);
                }
                final int measuredWidth = itemView.getMeasuredWidth();
                n11 = n9 - measuredWidth;
                int n12 = n10;
                if (n10 == 0) {
                    n12 = measuredWidth;
                }
                final int groupId = menuItemImpl2.getGroupId();
                if (groupId != 0) {
                    mActionButtonGroups.put(groupId, true);
                }
                menuItemImpl2.setIsActionButton(true);
                n10 = n12;
            }
            else if (menuItemImpl2.requestsActionButton()) {
                final int groupId2 = menuItemImpl2.getGroupId();
                final boolean value = mActionButtonGroups.get(groupId2);
                boolean b3 = (n5 > 0 || value) && n9 > 0 && (!this.mStrictWidthLimit || n7 > 0);
                int n13 = n7;
                int n14 = n10;
                boolean isActionButton = b3;
                n11 = n9;
                if (b3) {
                    final View itemView2 = this.getItemView(menuItemImpl2, this.mScrapActionButtonView, viewGroup);
                    if (this.mScrapActionButtonView == null) {
                        this.mScrapActionButtonView = itemView2;
                    }
                    if (this.mStrictWidthLimit) {
                        final int measureChildForCells = ActionMenuView.measureChildForCells(itemView2, n6, n7, measureSpec, 0);
                        final int n15 = n7 -= measureChildForCells;
                        if (measureChildForCells == 0) {
                            b3 = false;
                            n7 = n15;
                        }
                    }
                    else {
                        itemView2.measure(measureSpec, measureSpec);
                    }
                    final int measuredWidth2 = itemView2.getMeasuredWidth();
                    n11 = n9 - measuredWidth2;
                    if ((n14 = n10) == 0) {
                        n14 = measuredWidth2;
                    }
                    if (this.mStrictWidthLimit) {
                        isActionButton = (b3 & n11 >= 0);
                        n13 = n7;
                    }
                    else {
                        isActionButton = (b3 & n11 + n14 > 0);
                        n13 = n7;
                    }
                }
                int n16;
                if (isActionButton && groupId2 != 0) {
                    mActionButtonGroups.put(groupId2, true);
                    n16 = n5;
                }
                else {
                    n16 = n5;
                    if (value) {
                        mActionButtonGroups.put(groupId2, false);
                        int n17 = 0;
                        while (true) {
                            n16 = n5;
                            if (n17 >= j) {
                                break;
                            }
                            final MenuItemImpl menuItemImpl3 = visibleItems.get(n17);
                            int n18 = n5;
                            if (menuItemImpl3.getGroupId() == groupId2) {
                                n18 = n5;
                                if (menuItemImpl3.isActionButton()) {
                                    n18 = n5 + 1;
                                }
                                menuItemImpl3.setIsActionButton(false);
                            }
                            ++n17;
                            n5 = n18;
                        }
                    }
                }
                n5 = n16;
                if (isActionButton) {
                    n5 = n16 - 1;
                }
                menuItemImpl2.setIsActionButton(isActionButton);
                n7 = n13;
                n10 = n14;
            }
            else {
                menuItemImpl2.setIsActionButton(false);
                n11 = n9;
            }
            ++j;
            n9 = n11;
        }
        return true;
    }
    
    @Override
    public View getItemView(final MenuItemImpl menuItemImpl, final View view, final ViewGroup viewGroup) {
        View view2 = menuItemImpl.getActionView();
        if (view2 == null || menuItemImpl.hasCollapsibleActionView()) {
            view2 = super.getItemView(menuItemImpl, view, viewGroup);
        }
        int visibility;
        if (menuItemImpl.isActionViewExpanded()) {
            visibility = 8;
        }
        else {
            visibility = 0;
        }
        view2.setVisibility(visibility);
        final ActionMenuView actionMenuView = (ActionMenuView)viewGroup;
        final ViewGroup$LayoutParams layoutParams = view2.getLayoutParams();
        if (!actionMenuView.checkLayoutParams(layoutParams)) {
            view2.setLayoutParams((ViewGroup$LayoutParams)actionMenuView.generateLayoutParams(layoutParams));
        }
        return view2;
    }
    
    @Override
    public MenuView getMenuView(final ViewGroup viewGroup) {
        final MenuView mMenuView = this.mMenuView;
        final MenuView menuView = super.getMenuView(viewGroup);
        if (mMenuView != menuView) {
            ((ActionMenuView)menuView).setPresenter(this);
        }
        return menuView;
    }
    
    public Drawable getOverflowIcon() {
        if (this.mOverflowButton != null) {
            return this.mOverflowButton.getDrawable();
        }
        if (this.mPendingOverflowIconSet) {
            return this.mPendingOverflowIcon;
        }
        return null;
    }
    
    public boolean hideOverflowMenu() {
        if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
            ((View)this.mMenuView).removeCallbacks((Runnable)this.mPostedOpenRunnable);
            this.mPostedOpenRunnable = null;
            return true;
        }
        final OverflowPopup mOverflowPopup = this.mOverflowPopup;
        if (mOverflowPopup != null) {
            mOverflowPopup.dismiss();
            return true;
        }
        return false;
    }
    
    public boolean hideSubMenus() {
        if (this.mActionButtonPopup != null) {
            this.mActionButtonPopup.dismiss();
            return true;
        }
        return false;
    }
    
    @Override
    public void initForMenu(@NonNull final Context context, @Nullable final MenuBuilder menuBuilder) {
        super.initForMenu(context, menuBuilder);
        final Resources resources = context.getResources();
        final ActionBarPolicy value = ActionBarPolicy.get(context);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = value.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = value.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = value.getMaxActionButtons();
        }
        int mWidthLimit = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                if (this.mPendingOverflowIconSet) {
                    this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
                    this.mPendingOverflowIcon = null;
                    this.mPendingOverflowIconSet = false;
                }
                final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
                this.mOverflowButton.measure(measureSpec, measureSpec);
            }
            mWidthLimit -= this.mOverflowButton.getMeasuredWidth();
        }
        else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = mWidthLimit;
        this.mMinCellSize = (int)(56.0f * resources.getDisplayMetrics().density);
        this.mScrapActionButtonView = null;
    }
    
    public boolean isOverflowMenuShowPending() {
        return this.mPostedOpenRunnable != null || this.isOverflowMenuShowing();
    }
    
    public boolean isOverflowMenuShowing() {
        return this.mOverflowPopup != null && this.mOverflowPopup.isShowing();
    }
    
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }
    
    @Override
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        this.dismissPopupMenus();
        super.onCloseMenu(menuBuilder, b);
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = ActionBarPolicy.get(this.mContext).getMaxActionButtons();
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
        }
    }
    
    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            final SavedState savedState = (SavedState)parcelable;
            if (savedState.openSubMenuId > 0) {
                final MenuItem item = this.mMenu.findItem(savedState.openSubMenuId);
                if (item != null) {
                    this.onSubMenuSelected((SubMenuBuilder)item.getSubMenu());
                }
            }
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState();
        savedState.openSubMenuId = this.mOpenSubMenuId;
        return (Parcelable)savedState;
    }
    
    @Override
    public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
        if (subMenuBuilder.hasVisibleItems()) {
            SubMenuBuilder subMenuBuilder2;
            for (subMenuBuilder2 = subMenuBuilder; subMenuBuilder2.getParentMenu() != this.mMenu; subMenuBuilder2 = (SubMenuBuilder)subMenuBuilder2.getParentMenu()) {}
            final View viewForItem = this.findViewForItem(subMenuBuilder2.getItem());
            if (viewForItem != null) {
                this.mOpenSubMenuId = subMenuBuilder.getItem().getItemId();
                final boolean b = false;
                final int size = subMenuBuilder.size();
                int n = 0;
                boolean forceShowIcon;
                while (true) {
                    forceShowIcon = b;
                    if (n >= size) {
                        break;
                    }
                    final MenuItem item = subMenuBuilder.getItem(n);
                    if (item.isVisible() && item.getIcon() != null) {
                        forceShowIcon = true;
                        break;
                    }
                    ++n;
                }
                (this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, subMenuBuilder, viewForItem)).setForceShowIcon(forceShowIcon);
                this.mActionButtonPopup.show();
                super.onSubMenuSelected(subMenuBuilder);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onSubUiVisibilityChanged(final boolean b) {
        if (b) {
            super.onSubMenuSelected(null);
        }
        else if (this.mMenu != null) {
            this.mMenu.close(false);
        }
    }
    
    public void setExpandedActionViewsExclusive(final boolean mExpandedActionViewsExclusive) {
        this.mExpandedActionViewsExclusive = mExpandedActionViewsExclusive;
    }
    
    public void setItemLimit(final int mMaxItems) {
        this.mMaxItems = mMaxItems;
        this.mMaxItemsSet = true;
    }
    
    public void setMenuView(final ActionMenuView mMenuView) {
        ((ActionMenuView)(this.mMenuView = mMenuView)).initialize(this.mMenu);
    }
    
    public void setOverflowIcon(final Drawable drawable) {
        if (this.mOverflowButton != null) {
            this.mOverflowButton.setImageDrawable(drawable);
            return;
        }
        this.mPendingOverflowIconSet = true;
        this.mPendingOverflowIcon = drawable;
    }
    
    public void setReserveOverflow(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
        this.mReserveOverflowSet = true;
    }
    
    public void setWidthLimit(final int mWidthLimit, final boolean mStrictWidthLimit) {
        this.mWidthLimit = mWidthLimit;
        this.mStrictWidthLimit = mStrictWidthLimit;
        this.mWidthLimitSet = true;
    }
    
    @Override
    public boolean shouldIncludeItem(final int n, final MenuItemImpl menuItemImpl) {
        return menuItemImpl.isActionButton();
    }
    
    public boolean showOverflowMenu() {
        if (this.mReserveOverflow && !this.isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
            this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
            ((View)this.mMenuView).post((Runnable)this.mPostedOpenRunnable);
            super.onSubMenuSelected(null);
            return true;
        }
        return false;
    }
    
    @Override
    public void updateMenuView(final boolean b) {
        final ViewGroup viewGroup = (ViewGroup)((View)this.mMenuView).getParent();
        if (viewGroup != null) {
            ActionBarTransition.beginDelayedTransition(viewGroup);
        }
        super.updateMenuView(b);
        ((View)this.mMenuView).requestLayout();
        if (this.mMenu != null) {
            final ArrayList<MenuItemImpl> actionItems = this.mMenu.getActionItems();
            for (int size = actionItems.size(), i = 0; i < size; ++i) {
                final ActionProvider supportActionProvider = actionItems.get(i).getSupportActionProvider();
                if (supportActionProvider != null) {
                    supportActionProvider.setSubUiVisibilityListener((ActionProvider.SubUiVisibilityListener)this);
                }
            }
        }
        ArrayList<MenuItemImpl> nonActionItems;
        if (this.mMenu != null) {
            nonActionItems = this.mMenu.getNonActionItems();
        }
        else {
            nonActionItems = null;
        }
        int n2;
        final int n = n2 = 0;
        if (this.mReserveOverflow) {
            n2 = n;
            if (nonActionItems != null) {
                final int size2 = nonActionItems.size();
                if (size2 == 1) {
                    if (!nonActionItems.get(0).isActionViewExpanded()) {
                        n2 = 1;
                    }
                    else {
                        n2 = 0;
                    }
                }
                else if (size2 > 0) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
            }
        }
        if (n2 != 0) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
            }
            final ViewGroup viewGroup2 = (ViewGroup)this.mOverflowButton.getParent();
            if (viewGroup2 != this.mMenuView) {
                if (viewGroup2 != null) {
                    viewGroup2.removeView((View)this.mOverflowButton);
                }
                final ActionMenuView actionMenuView = (ActionMenuView)this.mMenuView;
                actionMenuView.addView((View)this.mOverflowButton, (ViewGroup$LayoutParams)actionMenuView.generateOverflowButtonLayoutParams());
            }
        }
        else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
            ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton);
        }
        ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }
    
    private class ActionButtonSubmenu extends MenuPopupHelper
    {
        public ActionButtonSubmenu(final Context context, final SubMenuBuilder subMenuBuilder, final View view) {
            super(context, subMenuBuilder, view, false, R.attr.actionOverflowMenuStyle);
            if (!((MenuItemImpl)subMenuBuilder.getItem()).isActionButton()) {
                Object mOverflowButton;
                if (ActionMenuPresenter.this.mOverflowButton == null) {
                    mOverflowButton = ActionMenuPresenter.this.mMenuView;
                }
                else {
                    mOverflowButton = ActionMenuPresenter.this.mOverflowButton;
                }
                this.setAnchorView((View)mOverflowButton);
            }
            this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }
        
        @Override
        protected void onDismiss() {
            ActionMenuPresenter.this.mActionButtonPopup = null;
            ActionMenuPresenter.this.mOpenSubMenuId = 0;
            super.onDismiss();
        }
    }
    
    private class ActionMenuPopupCallback extends PopupCallback
    {
        @Override
        public ShowableListMenu getPopup() {
            if (ActionMenuPresenter.this.mActionButtonPopup != null) {
                return ActionMenuPresenter.this.mActionButtonPopup.getPopup();
            }
            return null;
        }
    }
    
    private class OpenOverflowRunnable implements Runnable
    {
        private OverflowPopup mPopup;
        
        public OpenOverflowRunnable(final OverflowPopup mPopup) {
            this.mPopup = mPopup;
        }
        
        @Override
        public void run() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.changeMenuMode();
            }
            final View view = (View)ActionMenuPresenter.this.mMenuView;
            if (view != null && view.getWindowToken() != null && this.mPopup.tryShow()) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }
    
    private class OverflowMenuButton extends AppCompatImageView implements ActionMenuChildView
    {
        private final float[] mTempPts;
        
        public OverflowMenuButton(final Context context) {
            super(context, null, R.attr.actionOverflowButtonStyle);
            this.mTempPts = new float[2];
            this.setClickable(true);
            this.setFocusable(true);
            this.setVisibility(0);
            this.setEnabled(true);
            TooltipCompat.setTooltipText((View)this, this.getContentDescription());
            this.setOnTouchListener((View$OnTouchListener)new ForwardingListener(this) {
                @Override
                public ShowableListMenu getPopup() {
                    if (ActionMenuPresenter.this.mOverflowPopup == null) {
                        return null;
                    }
                    return ActionMenuPresenter.this.mOverflowPopup.getPopup();
                }
                
                public boolean onForwardingStarted() {
                    ActionMenuPresenter.this.showOverflowMenu();
                    return true;
                }
                
                public boolean onForwardingStopped() {
                    if (ActionMenuPresenter.this.mPostedOpenRunnable != null) {
                        return false;
                    }
                    ActionMenuPresenter.this.hideOverflowMenu();
                    return true;
                }
            });
        }
        
        @Override
        public boolean needsDividerAfter() {
            return false;
        }
        
        @Override
        public boolean needsDividerBefore() {
            return false;
        }
        
        public boolean performClick() {
            if (super.performClick()) {
                return true;
            }
            this.playSoundEffect(0);
            ActionMenuPresenter.this.showOverflowMenu();
            return true;
        }
        
        protected boolean setFrame(int n, int height, int paddingTop, int paddingBottom) {
            final boolean setFrame = super.setFrame(n, height, paddingTop, paddingBottom);
            final Drawable drawable = this.getDrawable();
            final Drawable background = this.getBackground();
            if (drawable != null && background != null) {
                final int width = this.getWidth();
                height = this.getHeight();
                n = Math.max(width, height) / 2;
                final int paddingLeft = this.getPaddingLeft();
                final int paddingRight = this.getPaddingRight();
                paddingTop = this.getPaddingTop();
                paddingBottom = this.getPaddingBottom();
                final int n2 = (width + (paddingLeft - paddingRight)) / 2;
                height = (height + (paddingTop - paddingBottom)) / 2;
                DrawableCompat.setHotspotBounds(background, n2 - n, height - n, n2 + n, height + n);
            }
            return setFrame;
        }
    }
    
    private class OverflowPopup extends MenuPopupHelper
    {
        public OverflowPopup(final Context context, final MenuBuilder menuBuilder, final View view, final boolean b) {
            super(context, menuBuilder, view, b, R.attr.actionOverflowMenuStyle);
            this.setGravity(8388613);
            this.setPresenterCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }
        
        @Override
        protected void onDismiss() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.close();
            }
            ActionMenuPresenter.this.mOverflowPopup = null;
            super.onDismiss();
        }
    }
    
    private class PopupPresenterCallback implements Callback
    {
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
            if (menuBuilder instanceof SubMenuBuilder) {
                menuBuilder.getRootMenu().close(false);
            }
            final Callback callback = ActionMenuPresenter.this.getCallback();
            if (callback != null) {
                callback.onCloseMenu(menuBuilder, b);
            }
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            if (menuBuilder == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)menuBuilder).getItem().getItemId();
            final Callback callback = ActionMenuPresenter.this.getCallback();
            return callback != null && callback.onOpenSubMenu(menuBuilder);
        }
    }
    
    private static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        public int openSubMenuId;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState() {
        }
        
        SavedState(final Parcel parcel) {
            this.openSubMenuId = parcel.readInt();
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.openSubMenuId);
        }
    }
}
