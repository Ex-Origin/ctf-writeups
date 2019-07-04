// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.content.res.TypedArray;
import android.view.ViewGroup$MarginLayoutParams;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityEvent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.annotation.RestrictTo;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.view.View$MeasureSpec;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;

public class LinearLayoutCompat extends ViewGroup
{
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;
    
    public LinearLayoutCompat(final Context context) {
        this(context, null);
    }
    
    public LinearLayoutCompat(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public LinearLayoutCompat(final Context context, final AttributeSet set, int n) {
        super(context, set, n);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.LinearLayoutCompat, n, 0);
        n = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (n >= 0) {
            this.setOrientation(n);
        }
        n = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (n >= 0) {
            this.setGravity(n);
        }
        final boolean boolean1 = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!boolean1) {
            this.setBaselineAligned(boolean1);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        this.setDividerDrawable(obtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }
    
    private void forceUniformHeight(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                if (layoutParams.height == -1) {
                    final int width = layoutParams.width;
                    layoutParams.width = virtualChild.getMeasuredWidth();
                    this.measureChildWithMargins(virtualChild, n2, 0, measureSpec, 0);
                    layoutParams.width = width;
                }
            }
        }
    }
    
    private void forceUniformWidth(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                if (layoutParams.width == -1) {
                    final int height = layoutParams.height;
                    layoutParams.height = virtualChild.getMeasuredHeight();
                    this.measureChildWithMargins(virtualChild, measureSpec, 0, n2, 0);
                    layoutParams.height = height;
                }
            }
        }
    }
    
    private void setChildFrame(final View view, final int n, final int n2, final int n3, final int n4) {
        view.layout(n, n2, n + n3, n2 + n4);
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    void drawDividersHorizontal(final Canvas canvas) {
        final int virtualChildCount = this.getVirtualChildCount();
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        for (int i = 0; i < virtualChildCount; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild != null && virtualChild.getVisibility() != 8 && this.hasDividerBeforeChildAt(i)) {
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                int n;
                if (layoutRtl) {
                    n = virtualChild.getRight() + layoutParams.rightMargin;
                }
                else {
                    n = virtualChild.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
                }
                this.drawVerticalDivider(canvas, n);
            }
        }
        if (this.hasDividerBeforeChildAt(virtualChildCount)) {
            final View virtualChild2 = this.getVirtualChildAt(virtualChildCount - 1);
            int paddingLeft;
            if (virtualChild2 == null) {
                if (layoutRtl) {
                    paddingLeft = this.getPaddingLeft();
                }
                else {
                    paddingLeft = this.getWidth() - this.getPaddingRight() - this.mDividerWidth;
                }
            }
            else {
                final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                if (layoutRtl) {
                    paddingLeft = virtualChild2.getLeft() - layoutParams2.leftMargin - this.mDividerWidth;
                }
                else {
                    paddingLeft = virtualChild2.getRight() + layoutParams2.rightMargin;
                }
            }
            this.drawVerticalDivider(canvas, paddingLeft);
        }
    }
    
    void drawDividersVertical(final Canvas canvas) {
        final int virtualChildCount = this.getVirtualChildCount();
        for (int i = 0; i < virtualChildCount; ++i) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild != null && virtualChild.getVisibility() != 8 && this.hasDividerBeforeChildAt(i)) {
                this.drawHorizontalDivider(canvas, virtualChild.getTop() - ((LayoutParams)virtualChild.getLayoutParams()).topMargin - this.mDividerHeight);
            }
        }
        if (this.hasDividerBeforeChildAt(virtualChildCount)) {
            final View virtualChild2 = this.getVirtualChildAt(virtualChildCount - 1);
            int n;
            if (virtualChild2 == null) {
                n = this.getHeight() - this.getPaddingBottom() - this.mDividerHeight;
            }
            else {
                n = virtualChild2.getBottom() + ((LayoutParams)virtualChild2.getLayoutParams()).bottomMargin;
            }
            this.drawHorizontalDivider(canvas, n);
        }
    }
    
    void drawHorizontalDivider(final Canvas canvas, final int n) {
        this.mDivider.setBounds(this.getPaddingLeft() + this.mDividerPadding, n, this.getWidth() - this.getPaddingRight() - this.mDividerPadding, this.mDividerHeight + n);
        this.mDivider.draw(canvas);
    }
    
    void drawVerticalDivider(final Canvas canvas, final int n) {
        this.mDivider.setBounds(n, this.getPaddingTop() + this.mDividerPadding, this.mDividerWidth + n, this.getHeight() - this.getPaddingBottom() - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -2);
        }
        if (this.mOrientation == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    public int getBaseline() {
        int baseline = -1;
        if (this.mBaselineAlignedChildIndex < 0) {
            baseline = super.getBaseline();
        }
        else {
            if (this.getChildCount() <= this.mBaselineAlignedChildIndex) {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
            }
            final View child = this.getChildAt(this.mBaselineAlignedChildIndex);
            final int baseline2 = child.getBaseline();
            if (baseline2 != -1) {
                int mBaselineChildTop;
                final int n = mBaselineChildTop = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    final int n2 = this.mGravity & 0x70;
                    mBaselineChildTop = n;
                    if (n2 != 48) {
                        switch (n2) {
                            default: {
                                mBaselineChildTop = n;
                                break;
                            }
                            case 80: {
                                mBaselineChildTop = this.getBottom() - this.getTop() - this.getPaddingBottom() - this.mTotalLength;
                                break;
                            }
                            case 16: {
                                mBaselineChildTop = n + (this.getBottom() - this.getTop() - this.getPaddingTop() - this.getPaddingBottom() - this.mTotalLength) / 2;
                                break;
                            }
                        }
                    }
                }
                return ((LayoutParams)child.getLayoutParams()).topMargin + mBaselineChildTop + baseline2;
            }
            if (this.mBaselineAlignedChildIndex != 0) {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        }
        return baseline;
    }
    
    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }
    
    int getChildrenSkipCount(final View view, final int n) {
        return 0;
    }
    
    public Drawable getDividerDrawable() {
        return this.mDivider;
    }
    
    public int getDividerPadding() {
        return this.mDividerPadding;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getDividerWidth() {
        return this.mDividerWidth;
    }
    
    public int getGravity() {
        return this.mGravity;
    }
    
    int getLocationOffset(final View view) {
        return 0;
    }
    
    int getNextLocationOffset(final View view) {
        return 0;
    }
    
    public int getOrientation() {
        return this.mOrientation;
    }
    
    public int getShowDividers() {
        return this.mShowDividers;
    }
    
    View getVirtualChildAt(final int n) {
        return this.getChildAt(n);
    }
    
    int getVirtualChildCount() {
        return this.getChildCount();
    }
    
    public float getWeightSum() {
        return this.mWeightSum;
    }
    
    protected boolean hasDividerBeforeChildAt(int n) {
        if (n == 0) {
            if ((this.mShowDividers & 0x1) == 0x0) {
                return false;
            }
        }
        else if (n == this.getChildCount()) {
            if ((this.mShowDividers & 0x4) == 0x0) {
                return false;
            }
        }
        else {
            if ((this.mShowDividers & 0x2) != 0x0) {
                final boolean b = false;
                --n;
                boolean b2;
                while (true) {
                    b2 = b;
                    if (n < 0) {
                        break;
                    }
                    if (this.getChildAt(n).getVisibility() != 8) {
                        b2 = true;
                        break;
                    }
                    --n;
                }
                return b2;
            }
            return false;
        }
        return true;
    }
    
    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }
    
    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }
    
    void layoutHorizontal(int n, int i, int n2, int n3) {
        final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
        final int paddingTop = this.getPaddingTop();
        final int n4 = n3 - i;
        final int paddingBottom = this.getPaddingBottom();
        final int paddingBottom2 = this.getPaddingBottom();
        final int virtualChildCount = this.getVirtualChildCount();
        i = this.mGravity;
        final int mGravity = this.mGravity;
        final boolean mBaselineAligned = this.mBaselineAligned;
        final int[] mMaxAscent = this.mMaxAscent;
        final int[] mMaxDescent = this.mMaxDescent;
        switch (GravityCompat.getAbsoluteGravity(i & 0x800007, ViewCompat.getLayoutDirection((View)this))) {
            default: {
                n = this.getPaddingLeft();
                break;
            }
            case 5: {
                n = this.getPaddingLeft() + n2 - n - this.mTotalLength;
                break;
            }
            case 1: {
                n = this.getPaddingLeft() + (n2 - n - this.mTotalLength) / 2;
                break;
            }
        }
        int n5 = 0;
        n3 = 1;
        if (layoutRtl) {
            n5 = virtualChildCount - 1;
            n3 = -1;
        }
        i = 0;
        n2 = n;
        while (i < virtualChildCount) {
            final int n6 = n5 + n3 * i;
            final View virtualChild = this.getVirtualChildAt(n6);
            int n7;
            if (virtualChild == null) {
                n = n2 + this.measureNullChild(n6);
                n7 = i;
            }
            else {
                n = n2;
                n7 = i;
                if (virtualChild.getVisibility() != 8) {
                    final int measuredWidth = virtualChild.getMeasuredWidth();
                    final int measuredHeight = virtualChild.getMeasuredHeight();
                    n = -1;
                    final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                    int baseline = n;
                    if (mBaselineAligned) {
                        baseline = n;
                        if (layoutParams.height != -1) {
                            baseline = virtualChild.getBaseline();
                        }
                    }
                    if ((n = layoutParams.gravity) < 0) {
                        n = (mGravity & 0x70);
                    }
                    switch (n & 0x70) {
                        default: {
                            n = paddingTop;
                            break;
                        }
                        case 48: {
                            final int n8 = n = paddingTop + layoutParams.topMargin;
                            if (baseline != -1) {
                                n = n8 + (mMaxAscent[1] - baseline);
                                break;
                            }
                            break;
                        }
                        case 16: {
                            n = (n4 - paddingTop - paddingBottom2 - measuredHeight) / 2 + paddingTop + layoutParams.topMargin - layoutParams.bottomMargin;
                            break;
                        }
                        case 80: {
                            final int n9 = n = n4 - paddingBottom - measuredHeight - layoutParams.bottomMargin;
                            if (baseline != -1) {
                                n = virtualChild.getMeasuredHeight();
                                n = n9 - (mMaxDescent[2] - (n - baseline));
                                break;
                            }
                            break;
                        }
                    }
                    int n10 = n2;
                    if (this.hasDividerBeforeChildAt(n6)) {
                        n10 = n2 + this.mDividerWidth;
                    }
                    n2 = n10 + layoutParams.leftMargin;
                    this.setChildFrame(virtualChild, n2 + this.getLocationOffset(virtualChild), n, measuredWidth, measuredHeight);
                    n = n2 + (layoutParams.rightMargin + measuredWidth + this.getNextLocationOffset(virtualChild));
                    n7 = i + this.getChildrenSkipCount(virtualChild, n6);
                }
            }
            i = n7 + 1;
            n2 = n;
        }
    }
    
    void layoutVertical(int n, int i, int n2, int gravity) {
        final int paddingLeft = this.getPaddingLeft();
        final int n3 = n2 - n;
        final int paddingRight = this.getPaddingRight();
        final int paddingRight2 = this.getPaddingRight();
        final int virtualChildCount = this.getVirtualChildCount();
        n = this.mGravity;
        final int mGravity = this.mGravity;
        switch (n & 0x70) {
            default: {
                n = this.getPaddingTop();
                break;
            }
            case 80: {
                n = this.getPaddingTop() + gravity - i - this.mTotalLength;
                break;
            }
            case 16: {
                n = this.getPaddingTop() + (gravity - i - this.mTotalLength) / 2;
                break;
            }
        }
        View virtualChild;
        int measuredWidth;
        int measuredHeight;
        LayoutParams layoutParams;
        for (i = 0; i < virtualChildCount; i = gravity + 1, n = n2) {
            virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                n2 = n + this.measureNullChild(i);
                gravity = i;
            }
            else {
                n2 = n;
                gravity = i;
                if (virtualChild.getVisibility() != 8) {
                    measuredWidth = virtualChild.getMeasuredWidth();
                    measuredHeight = virtualChild.getMeasuredHeight();
                    layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                    gravity = layoutParams.gravity;
                    if ((n2 = gravity) < 0) {
                        n2 = (mGravity & 0x800007);
                    }
                    switch (GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this)) & 0x7) {
                        default: {
                            n2 = paddingLeft + layoutParams.leftMargin;
                            break;
                        }
                        case 1: {
                            n2 = (n3 - paddingLeft - paddingRight2 - measuredWidth) / 2 + paddingLeft + layoutParams.leftMargin - layoutParams.rightMargin;
                            break;
                        }
                        case 5: {
                            n2 = n3 - paddingRight - measuredWidth - layoutParams.rightMargin;
                            break;
                        }
                    }
                    gravity = n;
                    if (this.hasDividerBeforeChildAt(i)) {
                        gravity = n + this.mDividerHeight;
                    }
                    n = gravity + layoutParams.topMargin;
                    this.setChildFrame(virtualChild, n2, n + this.getLocationOffset(virtualChild), measuredWidth, measuredHeight);
                    n2 = n + (layoutParams.bottomMargin + measuredHeight + this.getNextLocationOffset(virtualChild));
                    gravity = i + this.getChildrenSkipCount(virtualChild, i);
                }
            }
        }
    }
    
    void measureChildBeforeLayout(final View view, final int n, final int n2, final int n3, final int n4, final int n5) {
        this.measureChildWithMargins(view, n2, n3, n4, n5);
    }
    
    void measureHorizontal(final int n, final int n2) {
        this.mTotalLength = 0;
        int max = 0;
        int n3 = 0;
        int max2 = 0;
        int max3 = 0;
        int n4 = 1;
        float mWeightSum = 0.0f;
        final int virtualChildCount = this.getVirtualChildCount();
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        int n5 = 0;
        int n6 = 0;
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        final int[] mMaxAscent = this.mMaxAscent;
        final int[] mMaxDescent = this.mMaxDescent;
        mMaxAscent[2] = (mMaxAscent[3] = -1);
        mMaxAscent[0] = (mMaxAscent[1] = -1);
        mMaxDescent[2] = (mMaxDescent[3] = -1);
        mMaxDescent[0] = (mMaxDescent[1] = -1);
        final boolean mBaselineAligned = this.mBaselineAligned;
        final boolean mUseLargestChild = this.mUseLargestChild;
        final boolean b = mode == 1073741824;
        int n7 = Integer.MIN_VALUE;
        int max4;
        for (int i = 0; i < virtualChildCount; ++i, n7 = max4) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                this.mTotalLength += this.measureNullChild(i);
                max4 = n7;
            }
            else if (virtualChild.getVisibility() == 8) {
                i += this.getChildrenSkipCount(virtualChild, i);
                max4 = n7;
            }
            else {
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                mWeightSum += layoutParams.weight;
                int n8;
                if (mode == 1073741824 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                    if (b) {
                        this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                    }
                    else {
                        final int mTotalLength = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength, layoutParams.leftMargin + mTotalLength + layoutParams.rightMargin);
                    }
                    if (mBaselineAligned) {
                        final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChild.measure(measureSpec, measureSpec);
                        n8 = n6;
                        max4 = n7;
                    }
                    else {
                        n8 = 1;
                        max4 = n7;
                    }
                }
                else {
                    int width;
                    final int n9 = width = Integer.MIN_VALUE;
                    if (layoutParams.width == 0) {
                        width = n9;
                        if (layoutParams.weight > 0.0f) {
                            width = 0;
                            layoutParams.width = -2;
                        }
                    }
                    int mTotalLength2;
                    if (mWeightSum == 0.0f) {
                        mTotalLength2 = this.mTotalLength;
                    }
                    else {
                        mTotalLength2 = 0;
                    }
                    this.measureChildBeforeLayout(virtualChild, i, n, mTotalLength2, n2, 0);
                    if (width != Integer.MIN_VALUE) {
                        layoutParams.width = width;
                    }
                    final int measuredWidth = virtualChild.getMeasuredWidth();
                    if (b) {
                        this.mTotalLength += layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin + this.getNextLocationOffset(virtualChild);
                    }
                    else {
                        final int mTotalLength3 = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + this.getNextLocationOffset(virtualChild));
                    }
                    max4 = n7;
                    n8 = n6;
                    if (mUseLargestChild) {
                        max4 = Math.max(measuredWidth, n7);
                        n8 = n6;
                    }
                }
                final boolean b2 = false;
                int n10 = n5;
                boolean b3 = b2;
                if (mode2 != 1073741824) {
                    n10 = n5;
                    b3 = b2;
                    if (layoutParams.height == -1) {
                        n10 = 1;
                        b3 = true;
                    }
                }
                int n11 = layoutParams.topMargin + layoutParams.bottomMargin;
                final int n12 = virtualChild.getMeasuredHeight() + n11;
                final int combineMeasuredStates = ViewUtils.combineMeasuredStates(n3, virtualChild.getMeasuredState());
                if (mBaselineAligned) {
                    final int baseline = virtualChild.getBaseline();
                    if (baseline != -1) {
                        int n13;
                        if (layoutParams.gravity < 0) {
                            n13 = this.mGravity;
                        }
                        else {
                            n13 = layoutParams.gravity;
                        }
                        final int n14 = ((n13 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                        mMaxAscent[n14] = Math.max(mMaxAscent[n14], baseline);
                        mMaxDescent[n14] = Math.max(mMaxDescent[n14], n12 - baseline);
                    }
                }
                max = Math.max(max, n12);
                if (n4 != 0 && layoutParams.height == -1) {
                    n4 = 1;
                }
                else {
                    n4 = 0;
                }
                if (layoutParams.weight > 0.0f) {
                    if (!b3) {
                        n11 = n12;
                    }
                    max3 = Math.max(max3, n11);
                }
                else {
                    if (!b3) {
                        n11 = n12;
                    }
                    max2 = Math.max(max2, n11);
                }
                i += this.getChildrenSkipCount(virtualChild, i);
                n3 = combineMeasuredStates;
                n5 = n10;
                n6 = n8;
            }
        }
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        int max5 = 0;
        Label_1008: {
            if (mMaxAscent[1] == -1 && mMaxAscent[0] == -1 && mMaxAscent[2] == -1) {
                max5 = max;
                if (mMaxAscent[3] == -1) {
                    break Label_1008;
                }
            }
            max5 = Math.max(max, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
        }
        if (mUseLargestChild && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            for (int j = 0; j < virtualChildCount; ++j) {
                final View virtualChild2 = this.getVirtualChildAt(j);
                if (virtualChild2 == null) {
                    this.mTotalLength += this.measureNullChild(j);
                }
                else if (virtualChild2.getVisibility() == 8) {
                    j += this.getChildrenSkipCount(virtualChild2, j);
                }
                else {
                    final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                    if (b) {
                        this.mTotalLength += layoutParams2.leftMargin + n7 + layoutParams2.rightMargin + this.getNextLocationOffset(virtualChild2);
                    }
                    else {
                        final int mTotalLength4 = this.mTotalLength;
                        this.mTotalLength = Math.max(mTotalLength4, mTotalLength4 + n7 + layoutParams2.leftMargin + layoutParams2.rightMargin + this.getNextLocationOffset(virtualChild2));
                    }
                }
            }
        }
        this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
        final int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumWidth()), n, 0);
        int n15 = (resolveSizeAndState & 0xFFFFFF) - this.mTotalLength;
        int n36 = 0;
        int n37 = 0;
        int n38 = 0;
        int max8 = 0;
        Label_2121: {
            if (n6 != 0 || (n15 != 0 && mWeightSum > 0.0f)) {
                if (this.mWeightSum > 0.0f) {
                    mWeightSum = this.mWeightSum;
                }
                mMaxAscent[2] = (mMaxAscent[3] = -1);
                mMaxAscent[0] = (mMaxAscent[1] = -1);
                mMaxDescent[2] = (mMaxDescent[3] = -1);
                mMaxDescent[0] = (mMaxDescent[1] = -1);
                int n16 = -1;
                this.mTotalLength = 0;
                int k = 0;
                int n17 = max2;
                while (k < virtualChildCount) {
                    final View virtualChild3 = this.getVirtualChildAt(k);
                    int n18 = n4;
                    int n19 = n17;
                    int n20 = n3;
                    int n21 = n15;
                    int n22 = n16;
                    float n23 = mWeightSum;
                    if (virtualChild3 != null) {
                        if (virtualChild3.getVisibility() == 8) {
                            n23 = mWeightSum;
                            n22 = n16;
                            n21 = n15;
                            n20 = n3;
                            n19 = n17;
                            n18 = n4;
                        }
                        else {
                            final LayoutParams layoutParams3 = (LayoutParams)virtualChild3.getLayoutParams();
                            final float weight = layoutParams3.weight;
                            int n24 = n3;
                            int n25 = n15;
                            float n26 = mWeightSum;
                            if (weight > 0.0f) {
                                int n27 = (int)(n15 * weight / mWeightSum);
                                n26 = mWeightSum - weight;
                                final int n28 = n15 - n27;
                                final int childMeasureSpec = getChildMeasureSpec(n2, this.getPaddingTop() + this.getPaddingBottom() + layoutParams3.topMargin + layoutParams3.bottomMargin, layoutParams3.height);
                                if (layoutParams3.width != 0 || mode != 1073741824) {
                                    int n29;
                                    if ((n29 = virtualChild3.getMeasuredWidth() + n27) < 0) {
                                        n29 = 0;
                                    }
                                    virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(n29, 1073741824), childMeasureSpec);
                                }
                                else {
                                    if (n27 <= 0) {
                                        n27 = 0;
                                    }
                                    virtualChild3.measure(View$MeasureSpec.makeMeasureSpec(n27, 1073741824), childMeasureSpec);
                                }
                                final int combineMeasuredStates2 = View.combineMeasuredStates(n3, virtualChild3.getMeasuredState() & 0xFF000000);
                                n25 = n28;
                                n24 = combineMeasuredStates2;
                            }
                            if (b) {
                                this.mTotalLength += virtualChild3.getMeasuredWidth() + layoutParams3.leftMargin + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild3);
                            }
                            else {
                                final int mTotalLength5 = this.mTotalLength;
                                this.mTotalLength = Math.max(mTotalLength5, virtualChild3.getMeasuredWidth() + mTotalLength5 + layoutParams3.leftMargin + layoutParams3.rightMargin + this.getNextLocationOffset(virtualChild3));
                            }
                            int n30;
                            if (mode2 != 1073741824 && layoutParams3.height == -1) {
                                n30 = 1;
                            }
                            else {
                                n30 = 0;
                            }
                            final int n31 = layoutParams3.topMargin + layoutParams3.bottomMargin;
                            final int n32 = virtualChild3.getMeasuredHeight() + n31;
                            final int max6 = Math.max(n16, n32);
                            int n33;
                            if (n30 != 0) {
                                n33 = n31;
                            }
                            else {
                                n33 = n32;
                            }
                            final int max7 = Math.max(n17, n33);
                            boolean b4;
                            if (n4 != 0 && layoutParams3.height == -1) {
                                b4 = true;
                            }
                            else {
                                b4 = false;
                            }
                            n18 = (b4 ? 1 : 0);
                            n19 = max7;
                            n20 = n24;
                            n21 = n25;
                            n22 = max6;
                            n23 = n26;
                            if (mBaselineAligned) {
                                final int baseline2 = virtualChild3.getBaseline();
                                n18 = (b4 ? 1 : 0);
                                n19 = max7;
                                n20 = n24;
                                n21 = n25;
                                n22 = max6;
                                n23 = n26;
                                if (baseline2 != -1) {
                                    int n34;
                                    if (layoutParams3.gravity < 0) {
                                        n34 = this.mGravity;
                                    }
                                    else {
                                        n34 = layoutParams3.gravity;
                                    }
                                    final int n35 = ((n34 & 0x70) >> 4 & 0xFFFFFFFE) >> 1;
                                    mMaxAscent[n35] = Math.max(mMaxAscent[n35], baseline2);
                                    mMaxDescent[n35] = Math.max(mMaxDescent[n35], n32 - baseline2);
                                    n18 = (b4 ? 1 : 0);
                                    n19 = max7;
                                    n20 = n24;
                                    n21 = n25;
                                    n22 = max6;
                                    n23 = n26;
                                }
                            }
                        }
                    }
                    ++k;
                    n4 = n18;
                    n17 = n19;
                    n3 = n20;
                    n15 = n21;
                    n16 = n22;
                    mWeightSum = n23;
                }
                this.mTotalLength += this.getPaddingLeft() + this.getPaddingRight();
                if (mMaxAscent[1] == -1 && mMaxAscent[0] == -1 && mMaxAscent[2] == -1) {
                    n36 = n4;
                    n37 = n17;
                    n38 = n3;
                    max8 = n16;
                    if (mMaxAscent[3] == -1) {
                        break Label_2121;
                    }
                }
                max8 = Math.max(n16, Math.max(mMaxAscent[3], Math.max(mMaxAscent[0], Math.max(mMaxAscent[1], mMaxAscent[2]))) + Math.max(mMaxDescent[3], Math.max(mMaxDescent[0], Math.max(mMaxDescent[1], mMaxDescent[2]))));
                n38 = n3;
                n37 = n17;
                n36 = n4;
            }
            else {
                final int max9 = Math.max(max2, max3);
                n36 = n4;
                n37 = max9;
                n38 = n3;
                max8 = max5;
                if (mUseLargestChild) {
                    n36 = n4;
                    n37 = max9;
                    n38 = n3;
                    max8 = max5;
                    if (mode != 1073741824) {
                        int n39 = 0;
                        while (true) {
                            n36 = n4;
                            n37 = max9;
                            n38 = n3;
                            max8 = max5;
                            if (n39 >= virtualChildCount) {
                                break;
                            }
                            final View virtualChild4 = this.getVirtualChildAt(n39);
                            if (virtualChild4 != null && virtualChild4.getVisibility() != 8 && ((LayoutParams)virtualChild4.getLayoutParams()).weight > 0.0f) {
                                virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(n7, 1073741824), View$MeasureSpec.makeMeasureSpec(virtualChild4.getMeasuredHeight(), 1073741824));
                            }
                            ++n39;
                        }
                    }
                }
            }
        }
        int n40 = max8;
        if (n36 == 0) {
            n40 = max8;
            if (mode2 != 1073741824) {
                n40 = n37;
            }
        }
        this.setMeasuredDimension((0xFF000000 & n38) | resolveSizeAndState, View.resolveSizeAndState(Math.max(n40 + (this.getPaddingTop() + this.getPaddingBottom()), this.getSuggestedMinimumHeight()), n2, n38 << 16));
        if (n5 != 0) {
            this.forceUniformHeight(virtualChildCount, n);
        }
    }
    
    int measureNullChild(final int n) {
        return 0;
    }
    
    void measureVertical(final int n, final int n2) {
        this.mTotalLength = 0;
        int max = 0;
        int combineMeasuredStates = 0;
        int max2 = 0;
        int max3 = 0;
        int n3 = 1;
        float mWeightSum = 0.0f;
        final int virtualChildCount = this.getVirtualChildCount();
        final int mode = View$MeasureSpec.getMode(n);
        final int mode2 = View$MeasureSpec.getMode(n2);
        int n4 = 0;
        int n5 = 0;
        final int mBaselineAlignedChildIndex = this.mBaselineAlignedChildIndex;
        final boolean mUseLargestChild = this.mUseLargestChild;
        int n6 = Integer.MIN_VALUE;
        int max4;
        for (int i = 0; i < virtualChildCount; ++i, n6 = max4) {
            final View virtualChild = this.getVirtualChildAt(i);
            if (virtualChild == null) {
                this.mTotalLength += this.measureNullChild(i);
                max4 = n6;
            }
            else if (virtualChild.getVisibility() == 8) {
                i += this.getChildrenSkipCount(virtualChild, i);
                max4 = n6;
            }
            else {
                if (this.hasDividerBeforeChildAt(i)) {
                    this.mTotalLength += this.mDividerHeight;
                }
                final LayoutParams layoutParams = (LayoutParams)virtualChild.getLayoutParams();
                mWeightSum += layoutParams.weight;
                int n7;
                if (mode2 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                    final int mTotalLength = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength, layoutParams.topMargin + mTotalLength + layoutParams.bottomMargin);
                    n7 = 1;
                    max4 = n6;
                }
                else {
                    int height;
                    final int n8 = height = Integer.MIN_VALUE;
                    if (layoutParams.height == 0) {
                        height = n8;
                        if (layoutParams.weight > 0.0f) {
                            height = 0;
                            layoutParams.height = -2;
                        }
                    }
                    int mTotalLength2;
                    if (mWeightSum == 0.0f) {
                        mTotalLength2 = this.mTotalLength;
                    }
                    else {
                        mTotalLength2 = 0;
                    }
                    this.measureChildBeforeLayout(virtualChild, i, n, 0, n2, mTotalLength2);
                    if (height != Integer.MIN_VALUE) {
                        layoutParams.height = height;
                    }
                    final int measuredHeight = virtualChild.getMeasuredHeight();
                    final int mTotalLength3 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength3, mTotalLength3 + measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin + this.getNextLocationOffset(virtualChild));
                    max4 = n6;
                    n7 = n5;
                    if (mUseLargestChild) {
                        max4 = Math.max(measuredHeight, n6);
                        n7 = n5;
                    }
                }
                if (mBaselineAlignedChildIndex >= 0 && mBaselineAlignedChildIndex == i + 1) {
                    this.mBaselineChildTop = this.mTotalLength;
                }
                if (i < mBaselineAlignedChildIndex && layoutParams.weight > 0.0f) {
                    throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                }
                final boolean b = false;
                int n9 = n4;
                boolean b2 = b;
                if (mode != 1073741824) {
                    n9 = n4;
                    b2 = b;
                    if (layoutParams.width == -1) {
                        n9 = 1;
                        b2 = true;
                    }
                }
                int n10 = layoutParams.leftMargin + layoutParams.rightMargin;
                final int n11 = virtualChild.getMeasuredWidth() + n10;
                max = Math.max(max, n11);
                combineMeasuredStates = View.combineMeasuredStates(combineMeasuredStates, virtualChild.getMeasuredState());
                if (n3 != 0 && layoutParams.width == -1) {
                    n3 = 1;
                }
                else {
                    n3 = 0;
                }
                if (layoutParams.weight > 0.0f) {
                    if (!b2) {
                        n10 = n11;
                    }
                    max3 = Math.max(max3, n10);
                }
                else {
                    if (!b2) {
                        n10 = n11;
                    }
                    max2 = Math.max(max2, n10);
                }
                i += this.getChildrenSkipCount(virtualChild, i);
                n4 = n9;
                n5 = n7;
            }
        }
        if (this.mTotalLength > 0 && this.hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerHeight;
        }
        if (mUseLargestChild && (mode2 == Integer.MIN_VALUE || mode2 == 0)) {
            this.mTotalLength = 0;
            for (int j = 0; j < virtualChildCount; ++j) {
                final View virtualChild2 = this.getVirtualChildAt(j);
                if (virtualChild2 == null) {
                    this.mTotalLength += this.measureNullChild(j);
                }
                else if (virtualChild2.getVisibility() == 8) {
                    j += this.getChildrenSkipCount(virtualChild2, j);
                }
                else {
                    final LayoutParams layoutParams2 = (LayoutParams)virtualChild2.getLayoutParams();
                    final int mTotalLength4 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength4, mTotalLength4 + n6 + layoutParams2.topMargin + layoutParams2.bottomMargin + this.getNextLocationOffset(virtualChild2));
                }
            }
        }
        this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
        final int resolveSizeAndState = View.resolveSizeAndState(Math.max(this.mTotalLength, this.getSuggestedMinimumHeight()), n2, 0);
        final int n12 = (resolveSizeAndState & 0xFFFFFF) - this.mTotalLength;
        int max5;
        int n25;
        int n26;
        if (n5 != 0 || (n12 != 0 && mWeightSum > 0.0f)) {
            if (this.mWeightSum > 0.0f) {
                mWeightSum = this.mWeightSum;
            }
            this.mTotalLength = 0;
            int k = 0;
            max5 = max;
            int n13 = n12;
            while (k < virtualChildCount) {
                final View virtualChild3 = this.getVirtualChildAt(k);
                int n14;
                int n15;
                int max6;
                int n16;
                if (virtualChild3.getVisibility() == 8) {
                    n14 = n13;
                    n15 = combineMeasuredStates;
                    max6 = max2;
                    n16 = n3;
                }
                else {
                    final LayoutParams layoutParams3 = (LayoutParams)virtualChild3.getLayoutParams();
                    final float weight = layoutParams3.weight;
                    n15 = combineMeasuredStates;
                    n14 = n13;
                    float n17 = mWeightSum;
                    if (weight > 0.0f) {
                        int n18 = (int)(n13 * weight / mWeightSum);
                        n17 = mWeightSum - weight;
                        final int n19 = n13 - n18;
                        final int childMeasureSpec = getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + layoutParams3.leftMargin + layoutParams3.rightMargin, layoutParams3.width);
                        if (layoutParams3.height != 0 || mode2 != 1073741824) {
                            int n20;
                            if ((n20 = virtualChild3.getMeasuredHeight() + n18) < 0) {
                                n20 = 0;
                            }
                            virtualChild3.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n20, 1073741824));
                        }
                        else {
                            if (n18 <= 0) {
                                n18 = 0;
                            }
                            virtualChild3.measure(childMeasureSpec, View$MeasureSpec.makeMeasureSpec(n18, 1073741824));
                        }
                        final int combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates, virtualChild3.getMeasuredState() & 0xFFFFFF00);
                        n14 = n19;
                        n15 = combineMeasuredStates2;
                    }
                    final int n21 = layoutParams3.leftMargin + layoutParams3.rightMargin;
                    final int n22 = virtualChild3.getMeasuredWidth() + n21;
                    max5 = Math.max(max5, n22);
                    int n23;
                    if (mode != 1073741824 && layoutParams3.width == -1) {
                        n23 = 1;
                    }
                    else {
                        n23 = 0;
                    }
                    int n24;
                    if (n23 != 0) {
                        n24 = n21;
                    }
                    else {
                        n24 = n22;
                    }
                    max6 = Math.max(max2, n24);
                    if (n3 != 0 && layoutParams3.width == -1) {
                        n16 = 1;
                    }
                    else {
                        n16 = 0;
                    }
                    final int mTotalLength5 = this.mTotalLength;
                    this.mTotalLength = Math.max(mTotalLength5, virtualChild3.getMeasuredHeight() + mTotalLength5 + layoutParams3.topMargin + layoutParams3.bottomMargin + this.getNextLocationOffset(virtualChild3));
                    mWeightSum = n17;
                }
                ++k;
                n3 = n16;
                max2 = max6;
                combineMeasuredStates = n15;
                n13 = n14;
            }
            this.mTotalLength += this.getPaddingTop() + this.getPaddingBottom();
            n25 = combineMeasuredStates;
            n26 = n3;
        }
        else {
            final int max7 = Math.max(max2, max3);
            n26 = n3;
            max2 = max7;
            n25 = combineMeasuredStates;
            max5 = max;
            if (mUseLargestChild) {
                n26 = n3;
                max2 = max7;
                n25 = combineMeasuredStates;
                max5 = max;
                if (mode2 != 1073741824) {
                    int n27 = 0;
                    while (true) {
                        n26 = n3;
                        max2 = max7;
                        n25 = combineMeasuredStates;
                        max5 = max;
                        if (n27 >= virtualChildCount) {
                            break;
                        }
                        final View virtualChild4 = this.getVirtualChildAt(n27);
                        if (virtualChild4 != null && virtualChild4.getVisibility() != 8 && ((LayoutParams)virtualChild4.getLayoutParams()).weight > 0.0f) {
                            virtualChild4.measure(View$MeasureSpec.makeMeasureSpec(virtualChild4.getMeasuredWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(n6, 1073741824));
                        }
                        ++n27;
                    }
                }
            }
        }
        int n28 = max5;
        if (n26 == 0) {
            n28 = max5;
            if (mode != 1073741824) {
                n28 = max2;
            }
        }
        this.setMeasuredDimension(View.resolveSizeAndState(Math.max(n28 + (this.getPaddingLeft() + this.getPaddingRight()), this.getSuggestedMinimumWidth()), n, n25), resolveSizeAndState);
        if (n4 != 0) {
            this.forceUniformWidth(virtualChildCount, n2);
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            this.drawDividersVertical(canvas);
            return;
        }
        this.drawDividersHorizontal(canvas);
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)LinearLayoutCompat.class.getName());
        }
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)LinearLayoutCompat.class.getName());
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (this.mOrientation == 1) {
            this.layoutVertical(n, n2, n3, n4);
            return;
        }
        this.layoutHorizontal(n, n2, n3, n4);
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mOrientation == 1) {
            this.measureVertical(n, n2);
            return;
        }
        this.measureHorizontal(n, n2);
    }
    
    public void setBaselineAligned(final boolean mBaselineAligned) {
        this.mBaselineAligned = mBaselineAligned;
    }
    
    public void setBaselineAlignedChildIndex(final int mBaselineAlignedChildIndex) {
        if (mBaselineAlignedChildIndex < 0 || mBaselineAlignedChildIndex >= this.getChildCount()) {
            throw new IllegalArgumentException("base aligned child index out of range (0, " + this.getChildCount() + ")");
        }
        this.mBaselineAlignedChildIndex = mBaselineAlignedChildIndex;
    }
    
    public void setDividerDrawable(final Drawable mDivider) {
        boolean willNotDraw = false;
        if (mDivider == this.mDivider) {
            return;
        }
        if ((this.mDivider = mDivider) != null) {
            this.mDividerWidth = mDivider.getIntrinsicWidth();
            this.mDividerHeight = mDivider.getIntrinsicHeight();
        }
        else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        if (mDivider == null) {
            willNotDraw = true;
        }
        this.setWillNotDraw(willNotDraw);
        this.requestLayout();
    }
    
    public void setDividerPadding(final int mDividerPadding) {
        this.mDividerPadding = mDividerPadding;
    }
    
    public void setGravity(int mGravity) {
        if (this.mGravity != mGravity) {
            int n = mGravity;
            if ((0x800007 & mGravity) == 0x0) {
                n = (mGravity | 0x800003);
            }
            mGravity = n;
            if ((n & 0x70) == 0x0) {
                mGravity = (n | 0x30);
            }
            this.mGravity = mGravity;
            this.requestLayout();
        }
    }
    
    public void setHorizontalGravity(int n) {
        n &= 0x800007;
        if ((this.mGravity & 0x800007) != n) {
            this.mGravity = ((this.mGravity & 0xFF7FFFF8) | n);
            this.requestLayout();
        }
    }
    
    public void setMeasureWithLargestChildEnabled(final boolean mUseLargestChild) {
        this.mUseLargestChild = mUseLargestChild;
    }
    
    public void setOrientation(final int mOrientation) {
        if (this.mOrientation != mOrientation) {
            this.mOrientation = mOrientation;
            this.requestLayout();
        }
    }
    
    public void setShowDividers(final int mShowDividers) {
        if (mShowDividers != this.mShowDividers) {
            this.requestLayout();
        }
        this.mShowDividers = mShowDividers;
    }
    
    public void setVerticalGravity(int n) {
        n &= 0x70;
        if ((this.mGravity & 0x70) != n) {
            this.mGravity = ((this.mGravity & 0xFFFFFF8F) | n);
            this.requestLayout();
        }
    }
    
    public void setWeightSum(final float n) {
        this.mWeightSum = Math.max(0.0f, n);
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface DividerMode {
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public int gravity;
        public float weight;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.gravity = -1;
            this.weight = 0.0f;
        }
        
        public LayoutParams(final int n, final int n2, final float weight) {
            super(n, n2);
            this.gravity = -1;
            this.weight = weight;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.gravity = -1;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.gravity = -1;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.gravity = -1;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface OrientationMode {
    }
}
