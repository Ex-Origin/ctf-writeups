// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v7.appcompat.R;
import android.view.ViewGroup;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.support.annotation.Nullable;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class AlertDialogLayout extends LinearLayoutCompat
{
    public AlertDialogLayout(@Nullable final Context context) {
        super(context);
    }
    
    public AlertDialogLayout(@Nullable final Context context, @Nullable final AttributeSet set) {
        super(context, set);
    }
    
    private void forceUniformWidth(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.width == -1) {
                    final int height = layoutParams.height;
                    layoutParams.height = child.getMeasuredHeight();
                    this.measureChildWithMargins(child, measureSpec, 0, n2, 0);
                    layoutParams.height = height;
                }
            }
        }
    }
    
    private static int resolveMinimumHeight(final View view) {
        final int minimumHeight = ViewCompat.getMinimumHeight(view);
        if (minimumHeight > 0) {
            return minimumHeight;
        }
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            if (viewGroup.getChildCount() == 1) {
                return resolveMinimumHeight(viewGroup.getChildAt(0));
            }
        }
        return 0;
    }
    
    private void setChildFrame(final View view, final int n, final int n2, final int n3, final int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }
    
    private boolean tryOnMeasure(final int n, final int n2) {
        View view = null;
        View view2 = null;
        View view3 = null;
        final int childCount = this.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final int id = child.getId();
                if (id == R.id.topPanel) {
                    view = child;
                }
                else if (id == R.id.buttonPanel) {
                    view2 = child;
                }
                else {
                    if (id != R.id.contentPanel && id != R.id.customPanel) {
                        return false;
                    }
                    if (view3 != null) {
                        return false;
                    }
                    view3 = child;
                }
            }
        }
        final int mode = View$MeasureSpec.getMode(n2);
        final int size = View$MeasureSpec.getSize(n2);
        final int mode2 = View$MeasureSpec.getMode(n);
        int combineMeasuredStates = 0;
        int n4;
        final int n3 = n4 = this.getPaddingTop() + this.getPaddingBottom();
        if (view != null) {
            view.measure(n, 0);
            n4 = n3 + view.getMeasuredHeight();
            combineMeasuredStates = View.combineMeasuredStates(0, view.getMeasuredState());
        }
        int resolveMinimumHeight = 0;
        int n5 = 0;
        int combineMeasuredStates2 = combineMeasuredStates;
        int n6 = n4;
        if (view2 != null) {
            view2.measure(n, 0);
            resolveMinimumHeight = resolveMinimumHeight(view2);
            n5 = view2.getMeasuredHeight() - resolveMinimumHeight;
            n6 = n4 + resolveMinimumHeight;
            combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates, view2.getMeasuredState());
        }
        int measuredHeight = 0;
        int combineMeasuredStates3 = combineMeasuredStates2;
        int n7 = n6;
        if (view3 != null) {
            int measureSpec;
            if (mode == 0) {
                measureSpec = 0;
            }
            else {
                measureSpec = View$MeasureSpec.makeMeasureSpec(Math.max(0, size - n6), mode);
            }
            view3.measure(n, measureSpec);
            measuredHeight = view3.getMeasuredHeight();
            n7 = n6 + measuredHeight;
            combineMeasuredStates3 = View.combineMeasuredStates(combineMeasuredStates2, view3.getMeasuredState());
        }
        final int n8 = size - n7;
        int n9 = combineMeasuredStates3;
        int n10 = n8;
        int n11 = n7;
        if (view2 != null) {
            final int min = Math.min(n8, n5);
            int n12 = resolveMinimumHeight;
            int n13 = n8;
            if (min > 0) {
                n13 = n8 - min;
                n12 = resolveMinimumHeight + min;
            }
            view2.measure(n, View$MeasureSpec.makeMeasureSpec(n12, 1073741824));
            n11 = n7 - resolveMinimumHeight + view2.getMeasuredHeight();
            final int combineMeasuredStates4 = View.combineMeasuredStates(combineMeasuredStates3, view2.getMeasuredState());
            n10 = n13;
            n9 = combineMeasuredStates4;
        }
        int combineMeasuredStates5 = n9;
        int n14 = n11;
        if (view3 != null) {
            combineMeasuredStates5 = n9;
            n14 = n11;
            if (n10 > 0) {
                view3.measure(n, View$MeasureSpec.makeMeasureSpec(measuredHeight + n10, mode));
                n14 = n11 - measuredHeight + view3.getMeasuredHeight();
                combineMeasuredStates5 = View.combineMeasuredStates(n9, view3.getMeasuredState());
            }
        }
        int n15 = 0;
        int max;
        for (int j = 0; j < childCount; ++j, n15 = max) {
            final View child2 = this.getChildAt(j);
            max = n15;
            if (child2.getVisibility() != 8) {
                max = Math.max(n15, child2.getMeasuredWidth());
            }
        }
        this.setMeasuredDimension(View.resolveSizeAndState(n15 + (this.getPaddingLeft() + this.getPaddingRight()), n, combineMeasuredStates5), View.resolveSizeAndState(n14, n2, 0));
        if (mode2 != 1073741824) {
            this.forceUniformWidth(childCount, n2);
        }
        return true;
    }
    
    @Override
    protected void onLayout(final boolean b, int n, int gravity, int intrinsicHeight, int i) {
        final int paddingLeft = this.getPaddingLeft();
        final int n2 = intrinsicHeight - n;
        final int paddingRight = this.getPaddingRight();
        final int paddingRight2 = this.getPaddingRight();
        n = this.getMeasuredHeight();
        final int childCount = this.getChildCount();
        final int gravity2 = this.getGravity();
        switch (gravity2 & 0x70) {
            default: {
                n = this.getPaddingTop();
                break;
            }
            case 80: {
                n = this.getPaddingTop() + i - gravity - n;
                break;
            }
            case 16: {
                n = this.getPaddingTop() + (i - gravity - n) / 2;
                break;
            }
        }
        final Drawable dividerDrawable = this.getDividerDrawable();
        if (dividerDrawable == null) {
            intrinsicHeight = 0;
        }
        else {
            intrinsicHeight = dividerDrawable.getIntrinsicHeight();
        }
        View child;
        int measuredWidth;
        int measuredHeight;
        LayoutParams layoutParams;
        int n3;
        for (i = 0; i < childCount; ++i, n = gravity) {
            child = this.getChildAt(i);
            gravity = n;
            if (child != null) {
                gravity = n;
                if (child.getVisibility() != 8) {
                    measuredWidth = child.getMeasuredWidth();
                    measuredHeight = child.getMeasuredHeight();
                    layoutParams = (LayoutParams)child.getLayoutParams();
                    if ((gravity = layoutParams.gravity) < 0) {
                        gravity = (gravity2 & 0x800007);
                    }
                    switch (GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection((View)this)) & 0x7) {
                        default: {
                            gravity = paddingLeft + layoutParams.leftMargin;
                            break;
                        }
                        case 1: {
                            gravity = (n2 - paddingLeft - paddingRight2 - measuredWidth) / 2 + paddingLeft + layoutParams.leftMargin - layoutParams.rightMargin;
                            break;
                        }
                        case 5: {
                            gravity = n2 - paddingRight - measuredWidth - layoutParams.rightMargin;
                            break;
                        }
                    }
                    n3 = n;
                    if (this.hasDividerBeforeChildAt(i)) {
                        n3 = n + intrinsicHeight;
                    }
                    n = n3 + layoutParams.topMargin;
                    this.setChildFrame(child, gravity, n, measuredWidth, measuredHeight);
                    gravity = n + (layoutParams.bottomMargin + measuredHeight);
                }
            }
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        if (!this.tryOnMeasure(n, n2)) {
            super.onMeasure(n, n2);
        }
    }
}
