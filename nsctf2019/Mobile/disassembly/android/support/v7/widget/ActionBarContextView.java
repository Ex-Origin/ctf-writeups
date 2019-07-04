// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.View$MeasureSpec;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityEvent;
import android.view.MotionEvent;
import android.graphics.drawable.Drawable;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.view.View$OnClickListener;
import android.support.v7.view.ActionMode;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ActionBarContextView extends AbsActionBarView
{
    private static final String TAG = "ActionBarContextView";
    private View mClose;
    private int mCloseItemLayout;
    private View mCustomView;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private boolean mTitleOptional;
    private int mTitleStyleRes;
    private TextView mTitleView;
    
    public ActionBarContextView(final Context context) {
        this(context, null);
    }
    
    public ActionBarContextView(final Context context, final AttributeSet set) {
        this(context, set, R.attr.actionModeStyle);
    }
    
    public ActionBarContextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.ActionMode, n, 0);
        ViewCompat.setBackground((View)this, obtainStyledAttributes.getDrawable(R.styleable.ActionMode_background));
        this.mTitleStyleRes = obtainStyledAttributes.getResourceId(R.styleable.ActionMode_titleTextStyle, 0);
        this.mSubtitleStyleRes = obtainStyledAttributes.getResourceId(R.styleable.ActionMode_subtitleTextStyle, 0);
        this.mContentHeight = obtainStyledAttributes.getLayoutDimension(R.styleable.ActionMode_height, 0);
        this.mCloseItemLayout = obtainStyledAttributes.getResourceId(R.styleable.ActionMode_closeItemLayout, R.layout.abc_action_mode_close_item_material);
        obtainStyledAttributes.recycle();
    }
    
    private void initTitle() {
        final int n = 8;
        if (this.mTitleLayout == null) {
            LayoutInflater.from(this.getContext()).inflate(R.layout.abc_action_bar_title_item, (ViewGroup)this);
            this.mTitleLayout = (LinearLayout)this.getChildAt(this.getChildCount() - 1);
            this.mTitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_title);
            this.mSubtitleView = (TextView)this.mTitleLayout.findViewById(R.id.action_bar_subtitle);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(this.getContext(), this.mTitleStyleRes);
            }
            if (this.mSubtitleStyleRes != 0) {
                this.mSubtitleView.setTextAppearance(this.getContext(), this.mSubtitleStyleRes);
            }
        }
        this.mTitleView.setText(this.mTitle);
        this.mSubtitleView.setText(this.mSubtitle);
        int n2;
        if (!TextUtils.isEmpty(this.mTitle)) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        boolean b;
        if (!TextUtils.isEmpty(this.mSubtitle)) {
            b = true;
        }
        else {
            b = false;
        }
        final TextView mSubtitleView = this.mSubtitleView;
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        mSubtitleView.setVisibility(visibility);
        final LinearLayout mTitleLayout = this.mTitleLayout;
        int visibility2 = 0;
        Label_0204: {
            if (n2 == 0) {
                visibility2 = n;
                if (!b) {
                    break Label_0204;
                }
            }
            visibility2 = 0;
        }
        mTitleLayout.setVisibility(visibility2);
        if (this.mTitleLayout.getParent() == null) {
            this.addView((View)this.mTitleLayout);
        }
    }
    
    public void closeMode() {
        if (this.mClose == null) {
            this.killMode();
        }
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return (ViewGroup$LayoutParams)new ViewGroup$MarginLayoutParams(-1, -2);
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return (ViewGroup$LayoutParams)new ViewGroup$MarginLayoutParams(this.getContext(), set);
    }
    
    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }
    
    public CharSequence getTitle() {
        return this.mTitle;
    }
    
    @Override
    public boolean hideOverflowMenu() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.hideOverflowMenu();
    }
    
    public void initForMode(final ActionMode actionMode) {
        if (this.mClose == null) {
            this.addView(this.mClose = LayoutInflater.from(this.getContext()).inflate(this.mCloseItemLayout, (ViewGroup)this, false));
        }
        else if (this.mClose.getParent() == null) {
            this.addView(this.mClose);
        }
        this.mClose.findViewById(R.id.action_mode_close_button).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                actionMode.finish();
            }
        });
        final MenuBuilder menuBuilder = (MenuBuilder)actionMode.getMenu();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.dismissPopupMenus();
        }
        (this.mActionMenuPresenter = new ActionMenuPresenter(this.getContext())).setReserveOverflow(true);
        final ViewGroup$LayoutParams viewGroup$LayoutParams = new ViewGroup$LayoutParams(-2, -1);
        menuBuilder.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
        ViewCompat.setBackground((View)(this.mMenuView = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this)), null);
        this.addView((View)this.mMenuView, viewGroup$LayoutParams);
    }
    
    @Override
    public boolean isOverflowMenuShowing() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.isOverflowMenuShowing();
    }
    
    public boolean isTitleOptional() {
        return this.mTitleOptional;
    }
    
    public void killMode() {
        this.removeAllViews();
        this.mCustomView = null;
        this.mMenuView = null;
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (Build$VERSION.SDK_INT >= 14) {
            if (accessibilityEvent.getEventType() != 32) {
                super.onInitializeAccessibilityEvent(accessibilityEvent);
                return;
            }
            accessibilityEvent.setSource((View)this);
            accessibilityEvent.setClassName((CharSequence)this.getClass().getName());
            accessibilityEvent.setPackageName((CharSequence)this.getContext().getPackageName());
            accessibilityEvent.setContentDescription(this.mTitle);
        }
    }
    
    protected void onLayout(final boolean b, int paddingLeft, int n, final int n2, int n3) {
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        int paddingLeft2;
        if (layoutRtl) {
            paddingLeft2 = n2 - paddingLeft - this.getPaddingRight();
        }
        else {
            paddingLeft2 = this.getPaddingLeft();
        }
        final int paddingTop = this.getPaddingTop();
        final int n4 = n3 - n - this.getPaddingTop() - this.getPaddingBottom();
        n = paddingLeft2;
        if (this.mClose != null) {
            n = paddingLeft2;
            if (this.mClose.getVisibility() != 8) {
                final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)this.mClose.getLayoutParams();
                if (layoutRtl) {
                    n = viewGroup$MarginLayoutParams.rightMargin;
                }
                else {
                    n = viewGroup$MarginLayoutParams.leftMargin;
                }
                if (layoutRtl) {
                    n3 = viewGroup$MarginLayoutParams.leftMargin;
                }
                else {
                    n3 = viewGroup$MarginLayoutParams.rightMargin;
                }
                n = AbsActionBarView.next(paddingLeft2, n, layoutRtl);
                n = AbsActionBarView.next(n + this.positionChild(this.mClose, n, paddingTop, n4, layoutRtl), n3, layoutRtl);
            }
        }
        n3 = n;
        if (this.mTitleLayout != null) {
            n3 = n;
            if (this.mCustomView == null) {
                n3 = n;
                if (this.mTitleLayout.getVisibility() != 8) {
                    n3 = n + this.positionChild((View)this.mTitleLayout, n, paddingTop, n4, layoutRtl);
                }
            }
        }
        if (this.mCustomView != null) {
            this.positionChild(this.mCustomView, n3, paddingTop, n4, layoutRtl);
        }
        if (layoutRtl) {
            paddingLeft = this.getPaddingLeft();
        }
        else {
            paddingLeft = n2 - paddingLeft - this.getPaddingRight();
        }
        if (this.mMenuView != null) {
            this.positionChild((View)this.mMenuView, paddingLeft, paddingTop, n4, !layoutRtl);
        }
    }
    
    protected void onMeasure(int i, int n) {
        if (View$MeasureSpec.getMode(i) != 1073741824) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"match_parent\" (or fill_parent)");
        }
        if (View$MeasureSpec.getMode(n) == 0) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\"");
        }
        final int size = View$MeasureSpec.getSize(i);
        int n2;
        if (this.mContentHeight > 0) {
            n2 = this.mContentHeight;
        }
        else {
            n2 = View$MeasureSpec.getSize(n);
        }
        final int n3 = this.getPaddingTop() + this.getPaddingBottom();
        i = size - this.getPaddingLeft() - this.getPaddingRight();
        int min = n2 - n3;
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(min, Integer.MIN_VALUE);
        n = i;
        if (this.mClose != null) {
            i = this.measureChildView(this.mClose, i, measureSpec, 0);
            final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)this.mClose.getLayoutParams();
            n = i - (viewGroup$MarginLayoutParams.leftMargin + viewGroup$MarginLayoutParams.rightMargin);
        }
        i = n;
        if (this.mMenuView != null) {
            i = n;
            if (this.mMenuView.getParent() == this) {
                i = this.measureChildView((View)this.mMenuView, n, measureSpec, 0);
            }
        }
        n = i;
        if (this.mTitleLayout != null) {
            n = i;
            if (this.mCustomView == null) {
                if (this.mTitleOptional) {
                    n = View$MeasureSpec.makeMeasureSpec(0, 0);
                    this.mTitleLayout.measure(n, measureSpec);
                    final int measuredWidth = this.mTitleLayout.getMeasuredWidth();
                    boolean b;
                    if (measuredWidth <= i) {
                        b = true;
                    }
                    else {
                        b = false;
                    }
                    n = i;
                    if (b) {
                        n = i - measuredWidth;
                    }
                    final LinearLayout mTitleLayout = this.mTitleLayout;
                    if (b) {
                        i = 0;
                    }
                    else {
                        i = 8;
                    }
                    mTitleLayout.setVisibility(i);
                }
                else {
                    n = this.measureChildView((View)this.mTitleLayout, i, measureSpec, 0);
                }
            }
        }
        if (this.mCustomView != null) {
            final ViewGroup$LayoutParams layoutParams = this.mCustomView.getLayoutParams();
            if (layoutParams.width != -2) {
                i = 1073741824;
            }
            else {
                i = Integer.MIN_VALUE;
            }
            if (layoutParams.width >= 0) {
                n = Math.min(layoutParams.width, n);
            }
            int n4;
            if (layoutParams.height != -2) {
                n4 = 1073741824;
            }
            else {
                n4 = Integer.MIN_VALUE;
            }
            if (layoutParams.height >= 0) {
                min = Math.min(layoutParams.height, min);
            }
            this.mCustomView.measure(View$MeasureSpec.makeMeasureSpec(n, i), View$MeasureSpec.makeMeasureSpec(min, n4));
        }
        if (this.mContentHeight <= 0) {
            n = 0;
            int childCount;
            int n5;
            int n6;
            for (childCount = this.getChildCount(), i = 0; i < childCount; ++i, n = n6) {
                n5 = this.getChildAt(i).getMeasuredHeight() + n3;
                if (n5 > (n6 = n)) {
                    n6 = n5;
                }
            }
            this.setMeasuredDimension(size, n);
            return;
        }
        this.setMeasuredDimension(size, n2);
    }
    
    @Override
    public void setContentHeight(final int mContentHeight) {
        this.mContentHeight = mContentHeight;
    }
    
    public void setCustomView(final View mCustomView) {
        if (this.mCustomView != null) {
            this.removeView(this.mCustomView);
        }
        this.mCustomView = mCustomView;
        if (mCustomView != null && this.mTitleLayout != null) {
            this.removeView((View)this.mTitleLayout);
            this.mTitleLayout = null;
        }
        if (mCustomView != null) {
            this.addView(mCustomView);
        }
        this.requestLayout();
    }
    
    public void setSubtitle(final CharSequence mSubtitle) {
        this.mSubtitle = mSubtitle;
        this.initTitle();
    }
    
    public void setTitle(final CharSequence mTitle) {
        this.mTitle = mTitle;
        this.initTitle();
    }
    
    public void setTitleOptional(final boolean mTitleOptional) {
        if (mTitleOptional != this.mTitleOptional) {
            this.requestLayout();
        }
        this.mTitleOptional = mTitleOptional;
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    @Override
    public boolean showOverflowMenu() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.showOverflowMenu();
    }
}
