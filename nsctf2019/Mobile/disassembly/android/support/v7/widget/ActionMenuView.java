// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.view.ViewDebug$ExportedProperty;
import android.view.ContextThemeWrapper;
import android.support.annotation.StyleRes;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.annotation.Nullable;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.support.annotation.RestrictTo;
import android.view.accessibility.AccessibilityEvent;
import android.view.ViewGroup$LayoutParams;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View$MeasureSpec;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.MenuBuilder;

public class ActionMenuView extends LinearLayoutCompat implements ItemInvoker, MenuView
{
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    Callback mMenuBuilderCallback;
    private int mMinCellSize;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;
    
    public ActionMenuView(final Context context) {
        this(context, null);
    }
    
    public ActionMenuView(final Context mPopupContext, final AttributeSet set) {
        super(mPopupContext, set);
        this.setBaselineAligned(false);
        final float density = mPopupContext.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int)(56.0f * density);
        this.mGeneratedItemPadding = (int)(4.0f * density);
        this.mPopupContext = mPopupContext;
        this.mPopupTheme = 0;
    }
    
    static int measureChildForCells(final View view, final int n, int n2, int cellsUsed, int n3) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(cellsUsed) - n3, View$MeasureSpec.getMode(cellsUsed));
        ActionMenuItemView actionMenuItemView;
        if (view instanceof ActionMenuItemView) {
            actionMenuItemView = (ActionMenuItemView)view;
        }
        else {
            actionMenuItemView = null;
        }
        if (actionMenuItemView != null && actionMenuItemView.hasText()) {
            n3 = 1;
        }
        else {
            n3 = 0;
        }
        final int n4 = cellsUsed = 0;
        Label_0131: {
            if (n2 > 0) {
                if (n3 != 0) {
                    cellsUsed = n4;
                    if (n2 < 2) {
                        break Label_0131;
                    }
                }
                view.measure(View$MeasureSpec.makeMeasureSpec(n * n2, Integer.MIN_VALUE), measureSpec);
                final int measuredWidth = view.getMeasuredWidth();
                cellsUsed = (n2 = measuredWidth / n);
                if (measuredWidth % n != 0) {
                    n2 = cellsUsed + 1;
                }
                cellsUsed = n2;
                if (n3 != 0 && (cellsUsed = n2) < 2) {
                    cellsUsed = 2;
                }
            }
        }
        layoutParams.expandable = (!layoutParams.isOverflowButton && n3 != 0);
        layoutParams.cellsUsed = cellsUsed;
        view.measure(View$MeasureSpec.makeMeasureSpec(cellsUsed * n, 1073741824), measureSpec);
        return cellsUsed;
    }
    
    private void onMeasureExactFormat(int i, int mMinCellSize) {
        final int mode = View$MeasureSpec.getMode(mMinCellSize);
        i = View$MeasureSpec.getSize(i);
        final int size = View$MeasureSpec.getSize(mMinCellSize);
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int n = this.getPaddingTop() + this.getPaddingBottom();
        final int childMeasureSpec = getChildMeasureSpec(mMinCellSize, n, -2);
        final int n2 = i - (paddingLeft + paddingRight);
        i = n2 / this.mMinCellSize;
        mMinCellSize = this.mMinCellSize;
        if (i == 0) {
            this.setMeasuredDimension(n2, 0);
            return;
        }
        final int n3 = this.mMinCellSize + n2 % mMinCellSize / i;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        long n9 = 0L;
        final int childCount = this.getChildCount();
        long n10;
        int n11;
        for (int j = 0; j < childCount; ++j, n8 = n11, n9 = n10) {
            final View child = this.getChildAt(j);
            if (child.getVisibility() == 8) {
                n10 = n9;
                n11 = n8;
            }
            else {
                final boolean b = child instanceof ActionMenuItemView;
                final int n12 = n7 + 1;
                if (b) {
                    child.setPadding(this.mGeneratedItemPadding, 0, this.mGeneratedItemPadding, 0);
                }
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                layoutParams.expanded = false;
                layoutParams.extraPixels = 0;
                layoutParams.cellsUsed = 0;
                layoutParams.expandable = false;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.preventEdgeOffset = (b && ((ActionMenuItemView)child).hasText());
                if (layoutParams.isOverflowButton) {
                    mMinCellSize = 1;
                }
                else {
                    mMinCellSize = i;
                }
                final int measureChildForCells = measureChildForCells(child, n3, mMinCellSize, childMeasureSpec, n);
                final int max = Math.max(n5, measureChildForCells);
                mMinCellSize = n6;
                if (layoutParams.expandable) {
                    mMinCellSize = n6 + 1;
                }
                if (layoutParams.isOverflowButton) {
                    n8 = 1;
                }
                final int n13 = i - measureChildForCells;
                final int max2 = Math.max(n4, child.getMeasuredHeight());
                i = n13;
                n6 = mMinCellSize;
                n11 = n8;
                n5 = max;
                n4 = max2;
                n10 = n9;
                n7 = n12;
                if (measureChildForCells == 1) {
                    n10 = (n9 | 1 << j);
                    i = n13;
                    n6 = mMinCellSize;
                    n11 = n8;
                    n5 = max;
                    n4 = max2;
                    n7 = n12;
                }
            }
        }
        final boolean b2 = n8 != 0 && n7 == 2;
        mMinCellSize = 0;
        int n14 = i;
        long n15;
        while (true) {
            n15 = n9;
            if (n6 <= 0) {
                break;
            }
            n15 = n9;
            if (n14 <= 0) {
                break;
            }
            int n16 = Integer.MAX_VALUE;
            long n17 = 0L;
            int n18 = 0;
            long n19;
            int cellsUsed;
            for (int k = 0; k < childCount; ++k, n16 = cellsUsed, n18 = i, n17 = n19) {
                final LayoutParams layoutParams2 = (LayoutParams)this.getChildAt(k).getLayoutParams();
                if (!layoutParams2.expandable) {
                    n19 = n17;
                    i = n18;
                    cellsUsed = n16;
                }
                else if (layoutParams2.cellsUsed < n16) {
                    cellsUsed = layoutParams2.cellsUsed;
                    n19 = 1 << k;
                    i = 1;
                }
                else {
                    cellsUsed = n16;
                    i = n18;
                    n19 = n17;
                    if (layoutParams2.cellsUsed == n16) {
                        n19 = (n17 | 1 << k);
                        i = n18 + 1;
                        cellsUsed = n16;
                    }
                }
            }
            n9 |= n17;
            if (n18 > n14) {
                n15 = n9;
                break;
            }
            View child2;
            LayoutParams layoutParams3;
            long n20;
            for (i = 0; i < childCount; ++i, n14 = mMinCellSize, n9 = n20) {
                child2 = this.getChildAt(i);
                layoutParams3 = (LayoutParams)child2.getLayoutParams();
                if ((1 << i & n17) == 0x0L) {
                    mMinCellSize = n14;
                    n20 = n9;
                    if (layoutParams3.cellsUsed == n16 + 1) {
                        n20 = (n9 | 1 << i);
                        mMinCellSize = n14;
                    }
                }
                else {
                    if (b2 && layoutParams3.preventEdgeOffset && n14 == 1) {
                        child2.setPadding(this.mGeneratedItemPadding + n3, 0, this.mGeneratedItemPadding, 0);
                    }
                    ++layoutParams3.cellsUsed;
                    layoutParams3.expanded = true;
                    mMinCellSize = n14 - 1;
                    n20 = n9;
                }
            }
            mMinCellSize = 1;
        }
        if (n8 == 0 && n7 == 1) {
            i = 1;
        }
        else {
            i = 0;
        }
        int n21 = mMinCellSize;
        Label_1160: {
            if (n14 > 0) {
                n21 = mMinCellSize;
                if (n15 != 0L) {
                    if (n14 >= n7 - 1 && i == 0) {
                        n21 = mMinCellSize;
                        if (n5 <= 1) {
                            break Label_1160;
                        }
                    }
                    float n23;
                    final float n22 = n23 = Long.bitCount(n15);
                    if (i == 0) {
                        float n24 = n22;
                        if ((0x1L & n15) != 0x0L) {
                            n24 = n22;
                            if (!((LayoutParams)this.getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                                n24 = n22 - 0.5f;
                            }
                        }
                        n23 = n24;
                        if ((1 << childCount - 1 & n15) != 0x0L) {
                            n23 = n24;
                            if (!((LayoutParams)this.getChildAt(childCount - 1).getLayoutParams()).preventEdgeOffset) {
                                n23 = n24 - 0.5f;
                            }
                        }
                    }
                    int n25;
                    if (n23 > 0.0f) {
                        n25 = (int)(n14 * n3 / n23);
                    }
                    else {
                        n25 = 0;
                    }
                    for (int l = 0; l < childCount; ++l, mMinCellSize = i) {
                        if ((1 << l & n15) == 0x0L) {
                            i = mMinCellSize;
                        }
                        else {
                            final View child3 = this.getChildAt(l);
                            final LayoutParams layoutParams4 = (LayoutParams)child3.getLayoutParams();
                            if (child3 instanceof ActionMenuItemView) {
                                layoutParams4.extraPixels = n25;
                                layoutParams4.expanded = true;
                                if (l == 0 && !layoutParams4.preventEdgeOffset) {
                                    layoutParams4.leftMargin = -n25 / 2;
                                }
                                i = 1;
                            }
                            else if (layoutParams4.isOverflowButton) {
                                layoutParams4.extraPixels = n25;
                                layoutParams4.expanded = true;
                                layoutParams4.rightMargin = -n25 / 2;
                                i = 1;
                            }
                            else {
                                if (l != 0) {
                                    layoutParams4.leftMargin = n25 / 2;
                                }
                                i = mMinCellSize;
                                if (l != childCount - 1) {
                                    layoutParams4.rightMargin = n25 / 2;
                                    i = mMinCellSize;
                                }
                            }
                        }
                    }
                    n21 = mMinCellSize;
                }
            }
        }
        if (n21 != 0) {
            View child4;
            LayoutParams layoutParams5;
            for (i = 0; i < childCount; ++i) {
                child4 = this.getChildAt(i);
                layoutParams5 = (LayoutParams)child4.getLayoutParams();
                if (layoutParams5.expanded) {
                    child4.measure(View$MeasureSpec.makeMeasureSpec(layoutParams5.cellsUsed * n3 + layoutParams5.extraPixels, 1073741824), childMeasureSpec);
                }
            }
        }
        i = size;
        if (mode != 1073741824) {
            i = n4;
        }
        this.setMeasuredDimension(n2, i);
    }
    
    @Override
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams != null && viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void dismissPopupMenus() {
        if (this.mPresenter != null) {
            this.mPresenter.dismissPopupMenus();
        }
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        return false;
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        final LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.gravity = 16;
        return layoutParams;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams != null) {
            LayoutParams layoutParams;
            if (viewGroup$LayoutParams instanceof LayoutParams) {
                layoutParams = new LayoutParams((LayoutParams)viewGroup$LayoutParams);
            }
            else {
                layoutParams = new LayoutParams(viewGroup$LayoutParams);
            }
            if (layoutParams.gravity <= 0) {
                layoutParams.gravity = 16;
            }
            return layoutParams;
        }
        return this.generateDefaultLayoutParams();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public LayoutParams generateOverflowButtonLayoutParams() {
        final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
        generateDefaultLayoutParams.isOverflowButton = true;
        return generateDefaultLayoutParams;
    }
    
    public Menu getMenu() {
        if (this.mMenu == null) {
            final Context context = this.getContext();
            (this.mMenu = new MenuBuilder(context)).setCallback((MenuBuilder.Callback)new MenuBuilderCallback());
            (this.mPresenter = new ActionMenuPresenter(context)).setReserveOverflow(true);
            final ActionMenuPresenter mPresenter = this.mPresenter;
            MenuPresenter.Callback mActionMenuPresenterCallback;
            if (this.mActionMenuPresenterCallback != null) {
                mActionMenuPresenterCallback = this.mActionMenuPresenterCallback;
            }
            else {
                mActionMenuPresenterCallback = new ActionMenuPresenterCallback();
            }
            mPresenter.setCallback(mActionMenuPresenterCallback);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return (Menu)this.mMenu;
    }
    
    @Nullable
    public Drawable getOverflowIcon() {
        this.getMenu();
        return this.mPresenter.getOverflowIcon();
    }
    
    public int getPopupTheme() {
        return this.mPopupTheme;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public int getWindowAnimations() {
        return 0;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    protected boolean hasSupportDividerBeforeChildAt(final int n) {
        boolean b;
        if (n == 0) {
            b = false;
        }
        else {
            final View child = this.getChildAt(n - 1);
            final View child2 = this.getChildAt(n);
            boolean b3;
            final boolean b2 = b3 = false;
            if (n < this.getChildCount()) {
                b3 = b2;
                if (child instanceof ActionMenuChildView) {
                    b3 = (false | ((ActionMenuChildView)child).needsDividerAfter());
                }
            }
            b = b3;
            if (n > 0) {
                b = b3;
                if (child2 instanceof ActionMenuChildView) {
                    return b3 | ((ActionMenuChildView)child2).needsDividerBefore();
                }
            }
        }
        return b;
    }
    
    public boolean hideOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.hideOverflowMenu();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public void initialize(final MenuBuilder mMenu) {
        this.mMenu = mMenu;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Override
    public boolean invokeItem(final MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction((MenuItem)menuItemImpl, 0);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isOverflowMenuShowPending() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        return this.mPresenter != null && this.mPresenter.isOverflowMenuShowing();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mPresenter != null) {
            this.mPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.dismissPopupMenus();
    }
    
    @Override
    protected void onLayout(final boolean b, int i, int n, int n2, int n3) {
        if (!this.mFormatItems) {
            super.onLayout(b, i, n, n2, n3);
        }
        else {
            final int childCount = this.getChildCount();
            final int n4 = (n3 - n) / 2;
            final int dividerWidth = this.getDividerWidth();
            int n5 = 0;
            n3 = 0;
            n = n2 - i - this.getPaddingRight() - this.getPaddingLeft();
            boolean b2 = false;
            final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
            for (int j = 0; j < childCount; ++j) {
                final View child = this.getChildAt(j);
                if (child.getVisibility() != 8) {
                    final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                    if (layoutParams.isOverflowButton) {
                        int measuredWidth;
                        final int n6 = measuredWidth = child.getMeasuredWidth();
                        if (this.hasSupportDividerBeforeChildAt(j)) {
                            measuredWidth = n6 + dividerWidth;
                        }
                        final int measuredHeight = child.getMeasuredHeight();
                        int n7;
                        int n8;
                        if (layoutRtl) {
                            n7 = this.getPaddingLeft() + layoutParams.leftMargin;
                            n8 = n7 + measuredWidth;
                        }
                        else {
                            n8 = this.getWidth() - this.getPaddingRight() - layoutParams.rightMargin;
                            n7 = n8 - measuredWidth;
                        }
                        final int n9 = n4 - measuredHeight / 2;
                        child.layout(n7, n9, n8, n9 + measuredHeight);
                        n -= measuredWidth;
                        b2 = true;
                    }
                    else {
                        final int n10 = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                        final int n11 = n5 + n10;
                        final int n12 = n - n10;
                        n = n11;
                        if (this.hasSupportDividerBeforeChildAt(j)) {
                            n = n11 + dividerWidth;
                        }
                        ++n3;
                        n5 = n;
                        n = n12;
                    }
                }
            }
            if (childCount == 1 && !b2) {
                final View child2 = this.getChildAt(0);
                n = child2.getMeasuredWidth();
                n3 = child2.getMeasuredHeight();
                i = (n2 - i) / 2 - n / 2;
                n2 = n4 - n3 / 2;
                child2.layout(i, n2, i + n, n2 + n3);
                return;
            }
            if (b2) {
                i = 0;
            }
            else {
                i = 1;
            }
            i = n3 - i;
            if (i > 0) {
                i = n / i;
            }
            else {
                i = 0;
            }
            n3 = Math.max(0, i);
            if (layoutRtl) {
                n = this.getWidth() - this.getPaddingRight();
                View child3;
                LayoutParams layoutParams2;
                int measuredHeight2;
                int n13;
                for (i = 0; i < childCount; ++i, n = n2) {
                    child3 = this.getChildAt(i);
                    layoutParams2 = (LayoutParams)child3.getLayoutParams();
                    n2 = n;
                    if (child3.getVisibility() != 8) {
                        if (layoutParams2.isOverflowButton) {
                            n2 = n;
                        }
                        else {
                            n -= layoutParams2.rightMargin;
                            n2 = child3.getMeasuredWidth();
                            measuredHeight2 = child3.getMeasuredHeight();
                            n13 = n4 - measuredHeight2 / 2;
                            child3.layout(n - n2, n13, n, n13 + measuredHeight2);
                            n2 = n - (layoutParams2.leftMargin + n2 + n3);
                        }
                    }
                }
            }
            else {
                n = this.getPaddingLeft();
                View child4;
                LayoutParams layoutParams3;
                int measuredHeight3;
                int n14;
                for (i = 0; i < childCount; ++i, n = n2) {
                    child4 = this.getChildAt(i);
                    layoutParams3 = (LayoutParams)child4.getLayoutParams();
                    n2 = n;
                    if (child4.getVisibility() != 8) {
                        if (layoutParams3.isOverflowButton) {
                            n2 = n;
                        }
                        else {
                            n += layoutParams3.leftMargin;
                            n2 = child4.getMeasuredWidth();
                            measuredHeight3 = child4.getMeasuredHeight();
                            n14 = n4 - measuredHeight3 / 2;
                            child4.layout(n, n14, n + n2, n14 + measuredHeight3);
                            n2 = n + (layoutParams3.rightMargin + n2 + n3);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        final boolean mFormatItems = this.mFormatItems;
        this.mFormatItems = (View$MeasureSpec.getMode(n) == 1073741824);
        if (mFormatItems != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        final int size = View$MeasureSpec.getSize(n);
        if (this.mFormatItems && this.mMenu != null && size != this.mFormatItemsWidth) {
            this.mFormatItemsWidth = size;
            this.mMenu.onItemsChanged(true);
        }
        final int childCount = this.getChildCount();
        if (this.mFormatItems && childCount > 0) {
            this.onMeasureExactFormat(n, n2);
            return;
        }
        for (int i = 0; i < childCount; ++i) {
            final LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
            layoutParams.rightMargin = 0;
            layoutParams.leftMargin = 0;
        }
        super.onMeasure(n, n2);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setExpandedActionViewsExclusive(final boolean expandedActionViewsExclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(expandedActionViewsExclusive);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setMenuCallbacks(final MenuPresenter.Callback mActionMenuPresenterCallback, final Callback mMenuBuilderCallback) {
        this.mActionMenuPresenterCallback = mActionMenuPresenterCallback;
        this.mMenuBuilderCallback = mMenuBuilderCallback;
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    
    public void setOverflowIcon(@Nullable final Drawable overflowIcon) {
        this.getMenu();
        this.mPresenter.setOverflowIcon(overflowIcon);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setOverflowReserved(final boolean mReserveOverflow) {
        this.mReserveOverflow = mReserveOverflow;
    }
    
    public void setPopupTheme(@StyleRes final int mPopupTheme) {
        if (this.mPopupTheme != mPopupTheme) {
            if ((this.mPopupTheme = mPopupTheme) != 0) {
                this.mPopupContext = (Context)new ContextThemeWrapper(this.getContext(), mPopupTheme);
                return;
            }
            this.mPopupContext = this.getContext();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setPresenter(final ActionMenuPresenter mPresenter) {
        (this.mPresenter = mPresenter).setMenuView(this);
    }
    
    public boolean showOverflowMenu() {
        return this.mPresenter != null && this.mPresenter.showOverflowMenu();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public interface ActionMenuChildView
    {
        boolean needsDividerAfter();
        
        boolean needsDividerBefore();
    }
    
    private class ActionMenuPresenterCallback implements MenuPresenter.Callback
    {
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        }
        
        @Override
        public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
            return false;
        }
    }
    
    public static class LayoutParams extends LinearLayoutCompat.LayoutParams
    {
        @ViewDebug$ExportedProperty
        public int cellsUsed;
        @ViewDebug$ExportedProperty
        public boolean expandable;
        boolean expanded;
        @ViewDebug$ExportedProperty
        public int extraPixels;
        @ViewDebug$ExportedProperty
        public boolean isOverflowButton;
        @ViewDebug$ExportedProperty
        public boolean preventEdgeOffset;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.isOverflowButton = false;
        }
        
        LayoutParams(final int n, final int n2, final boolean isOverflowButton) {
            super(n, n2);
            this.isOverflowButton = isOverflowButton;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$LayoutParams)layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
    }
    
    private class MenuBuilderCallback implements Callback
    {
        @Override
        public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
        }
        
        @Override
        public void onMenuModeChange(final MenuBuilder menuBuilder) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menuBuilder);
            }
        }
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
}
