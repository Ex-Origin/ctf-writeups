// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.v4.view.ViewCompat;
import android.widget.LinearLayout$LayoutParams;
import android.view.View$MeasureSpec;
import android.view.View;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.widget.LinearLayout;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ButtonBarLayout extends LinearLayout
{
    private static final int ALLOW_STACKING_MIN_HEIGHT_DP = 320;
    private static final int PEEK_BUTTON_DP = 16;
    private boolean mAllowStacking;
    private int mLastWidthSize;
    private int mMinimumHeight;
    
    public ButtonBarLayout(final Context context, final AttributeSet set) {
        boolean b = false;
        super(context, set);
        this.mLastWidthSize = -1;
        this.mMinimumHeight = 0;
        if (this.getResources().getConfiguration().screenHeightDp >= 320) {
            b = true;
        }
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ButtonBarLayout);
        this.mAllowStacking = obtainStyledAttributes.getBoolean(R.styleable.ButtonBarLayout_allowStacking, b);
        obtainStyledAttributes.recycle();
    }
    
    private int getNextVisibleChildIndex(int i) {
        while (i < this.getChildCount()) {
            if (this.getChildAt(i).getVisibility() == 0) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    private boolean isStacked() {
        return this.getOrientation() == 1;
    }
    
    private void setStacked(final boolean b) {
        int orientation;
        if (b) {
            orientation = 1;
        }
        else {
            orientation = 0;
        }
        this.setOrientation(orientation);
        int gravity;
        if (b) {
            gravity = 5;
        }
        else {
            gravity = 80;
        }
        this.setGravity(gravity);
        final View viewById = this.findViewById(R.id.spacer);
        if (viewById != null) {
            int visibility;
            if (b) {
                visibility = 8;
            }
            else {
                visibility = 4;
            }
            viewById.setVisibility(visibility);
        }
        for (int i = this.getChildCount() - 2; i >= 0; --i) {
            this.bringChildToFront(this.getChildAt(i));
        }
    }
    
    public int getMinimumHeight() {
        return Math.max(this.mMinimumHeight, super.getMinimumHeight());
    }
    
    protected void onMeasure(int minimumHeight, int n) {
        final int size = View$MeasureSpec.getSize(minimumHeight);
        if (this.mAllowStacking) {
            if (size > this.mLastWidthSize && this.isStacked()) {
                this.setStacked(false);
            }
            this.mLastWidthSize = size;
        }
        boolean b = false;
        int measureSpec;
        if (!this.isStacked() && View$MeasureSpec.getMode(minimumHeight) == 1073741824) {
            measureSpec = View$MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
            b = true;
        }
        else {
            measureSpec = minimumHeight;
        }
        super.onMeasure(measureSpec, n);
        boolean b2 = b;
        if (this.mAllowStacking) {
            b2 = b;
            if (!this.isStacked()) {
                int n2;
                if ((this.getMeasuredWidthAndState() & 0xFF000000) == 0x1000000) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                b2 = b;
                if (n2 != 0) {
                    this.setStacked(true);
                    b2 = true;
                }
            }
        }
        if (b2) {
            super.onMeasure(minimumHeight, n);
        }
        minimumHeight = 0;
        final int nextVisibleChildIndex = this.getNextVisibleChildIndex(0);
        if (nextVisibleChildIndex >= 0) {
            final View child = this.getChildAt(nextVisibleChildIndex);
            final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)child.getLayoutParams();
            n = 0 + (this.getPaddingTop() + child.getMeasuredHeight() + linearLayout$LayoutParams.topMargin + linearLayout$LayoutParams.bottomMargin);
            if (this.isStacked()) {
                final int nextVisibleChildIndex2 = this.getNextVisibleChildIndex(nextVisibleChildIndex + 1);
                minimumHeight = n;
                if (nextVisibleChildIndex2 >= 0) {
                    minimumHeight = (int)(n + (this.getChildAt(nextVisibleChildIndex2).getPaddingTop() + 16.0f * this.getResources().getDisplayMetrics().density));
                }
            }
            else {
                minimumHeight = n + this.getPaddingBottom();
            }
        }
        if (ViewCompat.getMinimumHeight((View)this) != minimumHeight) {
            this.setMinimumHeight(minimumHeight);
        }
    }
    
    public void setAllowStacking(final boolean mAllowStacking) {
        if (this.mAllowStacking != mAllowStacking) {
            this.mAllowStacking = mAllowStacking;
            if (!this.mAllowStacking && this.getOrientation() == 1) {
                this.setStacked(false);
            }
            this.requestLayout();
        }
    }
}
