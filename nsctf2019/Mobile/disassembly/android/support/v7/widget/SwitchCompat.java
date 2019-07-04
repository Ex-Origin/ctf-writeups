// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.v7.content.res.AppCompatResources;
import android.support.v7.text.AllCapsTransformationMethod;
import android.support.v4.view.ViewCompat;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;
import android.graphics.Region$Op;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.Layout$Alignment;
import android.view.View;
import android.view.MotionEvent;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.os.Build$VERSION;
import android.view.ViewConfiguration;
import android.graphics.drawable.Drawable$Callback;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.view.VelocityTracker;
import android.graphics.PorterDuff$Mode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.animation.ObjectAnimator;
import android.text.Layout;
import android.util.Property;
import android.support.annotation.RequiresApi;
import android.widget.CompoundButton;

@RequiresApi(14)
public class SwitchCompat extends CompoundButton
{
    private static final String ACCESSIBILITY_EVENT_CLASS_NAME = "android.widget.Switch";
    private static final int[] CHECKED_STATE_SET;
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int THUMB_ANIMATION_DURATION = 250;
    private static final Property<SwitchCompat, Float> THUMB_POS;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final int TOUCH_MODE_IDLE = 0;
    private boolean mHasThumbTint;
    private boolean mHasThumbTintMode;
    private boolean mHasTrackTint;
    private boolean mHasTrackTintMode;
    private int mMinFlingVelocity;
    private Layout mOffLayout;
    private Layout mOnLayout;
    ObjectAnimator mPositionAnimator;
    private boolean mShowText;
    private boolean mSplitTrack;
    private int mSwitchBottom;
    private int mSwitchHeight;
    private int mSwitchLeft;
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private int mSwitchRight;
    private int mSwitchTop;
    private TransformationMethod mSwitchTransformationMethod;
    private int mSwitchWidth;
    private final Rect mTempRect;
    private ColorStateList mTextColors;
    private CharSequence mTextOff;
    private CharSequence mTextOn;
    private TextPaint mTextPaint;
    private Drawable mThumbDrawable;
    private float mThumbPosition;
    private int mThumbTextPadding;
    private ColorStateList mThumbTintList;
    private PorterDuff$Mode mThumbTintMode;
    private int mThumbWidth;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private Drawable mTrackDrawable;
    private ColorStateList mTrackTintList;
    private PorterDuff$Mode mTrackTintMode;
    private VelocityTracker mVelocityTracker;
    
    static {
        THUMB_POS = new Property<SwitchCompat, Float>(Float.class, "thumbPos") {
            public Float get(final SwitchCompat switchCompat) {
                return switchCompat.mThumbPosition;
            }
            
            public void set(final SwitchCompat switchCompat, final Float n) {
                switchCompat.setThumbPosition(n);
            }
        };
        CHECKED_STATE_SET = new int[] { 16842912 };
    }
    
    public SwitchCompat(final Context context) {
        this(context, null);
    }
    
    public SwitchCompat(final Context context, final AttributeSet set) {
        this(context, set, R.attr.switchStyle);
    }
    
    public SwitchCompat(final Context context, final AttributeSet set, int resourceId) {
        super(context, set, resourceId);
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTrackTintList = null;
        this.mTrackTintMode = null;
        this.mHasTrackTint = false;
        this.mHasTrackTintMode = false;
        this.mVelocityTracker = VelocityTracker.obtain();
        this.mTempRect = new Rect();
        this.mTextPaint = new TextPaint(1);
        this.mTextPaint.density = this.getResources().getDisplayMetrics().density;
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.SwitchCompat, resourceId, 0);
        this.mThumbDrawable = obtainStyledAttributes.getDrawable(R.styleable.SwitchCompat_android_thumb);
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setCallback((Drawable$Callback)this);
        }
        this.mTrackDrawable = obtainStyledAttributes.getDrawable(R.styleable.SwitchCompat_track);
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setCallback((Drawable$Callback)this);
        }
        this.mTextOn = obtainStyledAttributes.getText(R.styleable.SwitchCompat_android_textOn);
        this.mTextOff = obtainStyledAttributes.getText(R.styleable.SwitchCompat_android_textOff);
        this.mShowText = obtainStyledAttributes.getBoolean(R.styleable.SwitchCompat_showText, true);
        this.mThumbTextPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SwitchCompat_thumbTextPadding, 0);
        this.mSwitchMinWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SwitchCompat_switchMinWidth, 0);
        this.mSwitchPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SwitchCompat_switchPadding, 0);
        this.mSplitTrack = obtainStyledAttributes.getBoolean(R.styleable.SwitchCompat_splitTrack, false);
        final ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.SwitchCompat_thumbTint);
        if (colorStateList != null) {
            this.mThumbTintList = colorStateList;
            this.mHasThumbTint = true;
        }
        final PorterDuff$Mode tintMode = DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.SwitchCompat_thumbTintMode, -1), null);
        if (this.mThumbTintMode != tintMode) {
            this.mThumbTintMode = tintMode;
            this.mHasThumbTintMode = true;
        }
        if (this.mHasThumbTint || this.mHasThumbTintMode) {
            this.applyThumbTint();
        }
        final ColorStateList colorStateList2 = obtainStyledAttributes.getColorStateList(R.styleable.SwitchCompat_trackTint);
        if (colorStateList2 != null) {
            this.mTrackTintList = colorStateList2;
            this.mHasTrackTint = true;
        }
        final PorterDuff$Mode tintMode2 = DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.SwitchCompat_trackTintMode, -1), null);
        if (this.mTrackTintMode != tintMode2) {
            this.mTrackTintMode = tintMode2;
            this.mHasTrackTintMode = true;
        }
        if (this.mHasTrackTint || this.mHasTrackTintMode) {
            this.applyTrackTint();
        }
        resourceId = obtainStyledAttributes.getResourceId(R.styleable.SwitchCompat_switchTextAppearance, 0);
        if (resourceId != 0) {
            this.setSwitchTextAppearance(context, resourceId);
        }
        obtainStyledAttributes.recycle();
        final ViewConfiguration value = ViewConfiguration.get(context);
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mMinFlingVelocity = value.getScaledMinimumFlingVelocity();
        this.refreshDrawableState();
        this.setChecked(this.isChecked());
    }
    
    private void animateThumbToCheckedState(final boolean b) {
        float n;
        if (b) {
            n = 1.0f;
        }
        else {
            n = 0.0f;
        }
        (this.mPositionAnimator = ObjectAnimator.ofFloat((Object)this, (Property)SwitchCompat.THUMB_POS, new float[] { n })).setDuration(250L);
        if (Build$VERSION.SDK_INT >= 18) {
            this.mPositionAnimator.setAutoCancel(true);
        }
        this.mPositionAnimator.start();
    }
    
    private void applyThumbTint() {
        if (this.mThumbDrawable != null && (this.mHasThumbTint || this.mHasThumbTintMode)) {
            this.mThumbDrawable = this.mThumbDrawable.mutate();
            if (this.mHasThumbTint) {
                DrawableCompat.setTintList(this.mThumbDrawable, this.mThumbTintList);
            }
            if (this.mHasThumbTintMode) {
                DrawableCompat.setTintMode(this.mThumbDrawable, this.mThumbTintMode);
            }
            if (this.mThumbDrawable.isStateful()) {
                this.mThumbDrawable.setState(this.getDrawableState());
            }
        }
    }
    
    private void applyTrackTint() {
        if (this.mTrackDrawable != null && (this.mHasTrackTint || this.mHasTrackTintMode)) {
            this.mTrackDrawable = this.mTrackDrawable.mutate();
            if (this.mHasTrackTint) {
                DrawableCompat.setTintList(this.mTrackDrawable, this.mTrackTintList);
            }
            if (this.mHasTrackTintMode) {
                DrawableCompat.setTintMode(this.mTrackDrawable, this.mTrackTintMode);
            }
            if (this.mTrackDrawable.isStateful()) {
                this.mTrackDrawable.setState(this.getDrawableState());
            }
        }
    }
    
    private void cancelPositionAnimator() {
        if (this.mPositionAnimator != null) {
            this.mPositionAnimator.cancel();
        }
    }
    
    private void cancelSuperTouch(MotionEvent obtain) {
        obtain = MotionEvent.obtain(obtain);
        obtain.setAction(3);
        super.onTouchEvent(obtain);
        obtain.recycle();
    }
    
    private static float constrain(final float n, final float n2, final float n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }
    
    private boolean getTargetCheckedState() {
        return this.mThumbPosition > 0.5f;
    }
    
    private int getThumbOffset() {
        float mThumbPosition;
        if (ViewUtils.isLayoutRtl((View)this)) {
            mThumbPosition = 1.0f - this.mThumbPosition;
        }
        else {
            mThumbPosition = this.mThumbPosition;
        }
        return (int)(this.getThumbScrollRange() * mThumbPosition + 0.5f);
    }
    
    private int getThumbScrollRange() {
        if (this.mTrackDrawable != null) {
            final Rect mTempRect = this.mTempRect;
            this.mTrackDrawable.getPadding(mTempRect);
            Rect rect;
            if (this.mThumbDrawable != null) {
                rect = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
            }
            else {
                rect = DrawableUtils.INSETS_NONE;
            }
            return this.mSwitchWidth - this.mThumbWidth - mTempRect.left - mTempRect.right - rect.left - rect.right;
        }
        return 0;
    }
    
    private boolean hitThumb(final float n, final float n2) {
        if (this.mThumbDrawable != null) {
            final int thumbOffset = this.getThumbOffset();
            this.mThumbDrawable.getPadding(this.mTempRect);
            final int mSwitchTop = this.mSwitchTop;
            final int mTouchSlop = this.mTouchSlop;
            final int n3 = this.mSwitchLeft + thumbOffset - this.mTouchSlop;
            final int mThumbWidth = this.mThumbWidth;
            final int left = this.mTempRect.left;
            final int right = this.mTempRect.right;
            final int mTouchSlop2 = this.mTouchSlop;
            final int mSwitchBottom = this.mSwitchBottom;
            final int mTouchSlop3 = this.mTouchSlop;
            if (n > n3 && n < mThumbWidth + n3 + left + right + mTouchSlop2 && n2 > mSwitchTop - mTouchSlop && n2 < mSwitchBottom + mTouchSlop3) {
                return true;
            }
        }
        return false;
    }
    
    private Layout makeLayout(CharSequence transformation) {
        if (this.mSwitchTransformationMethod != null) {
            transformation = this.mSwitchTransformationMethod.getTransformation(transformation, (View)this);
        }
        final TextPaint mTextPaint = this.mTextPaint;
        int n;
        if (transformation != null) {
            n = (int)Math.ceil(Layout.getDesiredWidth(transformation, this.mTextPaint));
        }
        else {
            n = 0;
        }
        return (Layout)new StaticLayout(transformation, mTextPaint, n, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
    }
    
    private void setSwitchTypefaceByIndex(final int n, final int n2) {
        Typeface typeface = null;
        switch (n) {
            case 1: {
                typeface = Typeface.SANS_SERIF;
                break;
            }
            case 2: {
                typeface = Typeface.SERIF;
                break;
            }
            case 3: {
                typeface = Typeface.MONOSPACE;
                break;
            }
        }
        this.setSwitchTypeface(typeface, n2);
    }
    
    private void stopDrag(final MotionEvent motionEvent) {
        this.mTouchMode = 0;
        int n;
        if (motionEvent.getAction() == 1 && this.isEnabled()) {
            n = 1;
        }
        else {
            n = 0;
        }
        final boolean checked = this.isChecked();
        boolean targetCheckedState;
        if (n != 0) {
            this.mVelocityTracker.computeCurrentVelocity(1000);
            final float xVelocity = this.mVelocityTracker.getXVelocity();
            if (Math.abs(xVelocity) > this.mMinFlingVelocity) {
                if (ViewUtils.isLayoutRtl((View)this)) {
                    targetCheckedState = (xVelocity < 0.0f);
                }
                else {
                    targetCheckedState = (xVelocity > 0.0f);
                }
            }
            else {
                targetCheckedState = this.getTargetCheckedState();
            }
        }
        else {
            targetCheckedState = checked;
        }
        if (targetCheckedState != checked) {
            this.playSoundEffect(0);
        }
        this.setChecked(targetCheckedState);
        this.cancelSuperTouch(motionEvent);
    }
    
    public void draw(final Canvas canvas) {
        final Rect mTempRect = this.mTempRect;
        final int mSwitchLeft = this.mSwitchLeft;
        final int mSwitchTop = this.mSwitchTop;
        final int mSwitchRight = this.mSwitchRight;
        final int mSwitchBottom = this.mSwitchBottom;
        final int n = mSwitchLeft + this.getThumbOffset();
        Rect rect;
        if (this.mThumbDrawable != null) {
            rect = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
        }
        else {
            rect = DrawableUtils.INSETS_NONE;
        }
        int n2 = n;
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.getPadding(mTempRect);
            final int n3 = n + mTempRect.left;
            final int n4 = mSwitchTop;
            int n6;
            final int n5 = n6 = mSwitchBottom;
            int n7 = mSwitchLeft;
            int n8 = mSwitchRight;
            int n9 = n4;
            if (rect != null) {
                int n10 = mSwitchLeft;
                if (rect.left > mTempRect.left) {
                    n10 = mSwitchLeft + (rect.left - mTempRect.left);
                }
                int n11 = n4;
                if (rect.top > mTempRect.top) {
                    n11 = n4 + (rect.top - mTempRect.top);
                }
                int n12 = mSwitchRight;
                if (rect.right > mTempRect.right) {
                    n12 = mSwitchRight - (rect.right - mTempRect.right);
                }
                n6 = n5;
                n7 = n10;
                n8 = n12;
                n9 = n11;
                if (rect.bottom > mTempRect.bottom) {
                    n6 = n5 - (rect.bottom - mTempRect.bottom);
                    n9 = n11;
                    n8 = n12;
                    n7 = n10;
                }
            }
            this.mTrackDrawable.setBounds(n7, n9, n8, n6);
            n2 = n3;
        }
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.getPadding(mTempRect);
            final int n13 = n2 - mTempRect.left;
            final int n14 = this.mThumbWidth + n2 + mTempRect.right;
            this.mThumbDrawable.setBounds(n13, mSwitchTop, n14, mSwitchBottom);
            final Drawable background = this.getBackground();
            if (background != null) {
                DrawableCompat.setHotspotBounds(background, n13, mSwitchTop, n14, mSwitchBottom);
            }
        }
        super.draw(canvas);
    }
    
    public void drawableHotspotChanged(final float n, final float n2) {
        if (Build$VERSION.SDK_INT >= 21) {
            super.drawableHotspotChanged(n, n2);
        }
        if (this.mThumbDrawable != null) {
            DrawableCompat.setHotspot(this.mThumbDrawable, n, n2);
        }
        if (this.mTrackDrawable != null) {
            DrawableCompat.setHotspot(this.mTrackDrawable, n, n2);
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final int[] drawableState = this.getDrawableState();
        final boolean b = false;
        final Drawable mThumbDrawable = this.mThumbDrawable;
        boolean b2 = b;
        if (mThumbDrawable != null) {
            b2 = b;
            if (mThumbDrawable.isStateful()) {
                b2 = (false | mThumbDrawable.setState(drawableState));
            }
        }
        final Drawable mTrackDrawable = this.mTrackDrawable;
        boolean b3 = b2;
        if (mTrackDrawable != null) {
            b3 = b2;
            if (mTrackDrawable.isStateful()) {
                b3 = (b2 | mTrackDrawable.setState(drawableState));
            }
        }
        if (b3) {
            this.invalidate();
        }
    }
    
    public int getCompoundPaddingLeft() {
        int compoundPaddingLeft;
        if (!ViewUtils.isLayoutRtl((View)this)) {
            compoundPaddingLeft = super.getCompoundPaddingLeft();
        }
        else {
            compoundPaddingLeft = super.getCompoundPaddingLeft() + this.mSwitchWidth;
            if (!TextUtils.isEmpty(this.getText())) {
                return compoundPaddingLeft + this.mSwitchPadding;
            }
        }
        return compoundPaddingLeft;
    }
    
    public int getCompoundPaddingRight() {
        int compoundPaddingRight;
        if (ViewUtils.isLayoutRtl((View)this)) {
            compoundPaddingRight = super.getCompoundPaddingRight();
        }
        else {
            compoundPaddingRight = super.getCompoundPaddingRight() + this.mSwitchWidth;
            if (!TextUtils.isEmpty(this.getText())) {
                return compoundPaddingRight + this.mSwitchPadding;
            }
        }
        return compoundPaddingRight;
    }
    
    public boolean getShowText() {
        return this.mShowText;
    }
    
    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }
    
    public int getSwitchMinWidth() {
        return this.mSwitchMinWidth;
    }
    
    public int getSwitchPadding() {
        return this.mSwitchPadding;
    }
    
    public CharSequence getTextOff() {
        return this.mTextOff;
    }
    
    public CharSequence getTextOn() {
        return this.mTextOn;
    }
    
    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }
    
    public int getThumbTextPadding() {
        return this.mThumbTextPadding;
    }
    
    @Nullable
    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }
    
    @Nullable
    public PorterDuff$Mode getThumbTintMode() {
        return this.mThumbTintMode;
    }
    
    public Drawable getTrackDrawable() {
        return this.mTrackDrawable;
    }
    
    @Nullable
    public ColorStateList getTrackTintList() {
        return this.mTrackTintList;
    }
    
    @Nullable
    public PorterDuff$Mode getTrackTintMode() {
        return this.mTrackTintMode;
    }
    
    public void jumpDrawablesToCurrentState() {
        if (Build$VERSION.SDK_INT >= 14) {
            super.jumpDrawablesToCurrentState();
            if (this.mThumbDrawable != null) {
                this.mThumbDrawable.jumpToCurrentState();
            }
            if (this.mTrackDrawable != null) {
                this.mTrackDrawable.jumpToCurrentState();
            }
            if (this.mPositionAnimator != null && this.mPositionAnimator.isStarted()) {
                this.mPositionAnimator.end();
                this.mPositionAnimator = null;
            }
        }
    }
    
    protected int[] onCreateDrawableState(final int n) {
        final int[] onCreateDrawableState = super.onCreateDrawableState(n + 1);
        if (this.isChecked()) {
            mergeDrawableStates(onCreateDrawableState, SwitchCompat.CHECKED_STATE_SET);
        }
        return onCreateDrawableState;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final Rect mTempRect = this.mTempRect;
        final Drawable mTrackDrawable = this.mTrackDrawable;
        if (mTrackDrawable != null) {
            mTrackDrawable.getPadding(mTempRect);
        }
        else {
            mTempRect.setEmpty();
        }
        final int mSwitchTop = this.mSwitchTop;
        final int mSwitchBottom = this.mSwitchBottom;
        final int top = mTempRect.top;
        final int bottom = mTempRect.bottom;
        final Drawable mThumbDrawable = this.mThumbDrawable;
        if (mTrackDrawable != null) {
            if (this.mSplitTrack && mThumbDrawable != null) {
                final Rect opticalBounds = DrawableUtils.getOpticalBounds(mThumbDrawable);
                mThumbDrawable.copyBounds(mTempRect);
                mTempRect.left += opticalBounds.left;
                mTempRect.right -= opticalBounds.right;
                final int save = canvas.save();
                canvas.clipRect(mTempRect, Region$Op.DIFFERENCE);
                mTrackDrawable.draw(canvas);
                canvas.restoreToCount(save);
            }
            else {
                mTrackDrawable.draw(canvas);
            }
        }
        final int save2 = canvas.save();
        if (mThumbDrawable != null) {
            mThumbDrawable.draw(canvas);
        }
        Layout layout;
        if (this.getTargetCheckedState()) {
            layout = this.mOnLayout;
        }
        else {
            layout = this.mOffLayout;
        }
        if (layout != null) {
            final int[] drawableState = this.getDrawableState();
            if (this.mTextColors != null) {
                this.mTextPaint.setColor(this.mTextColors.getColorForState(drawableState, 0));
            }
            this.mTextPaint.drawableState = drawableState;
            int width;
            if (mThumbDrawable != null) {
                final Rect bounds = mThumbDrawable.getBounds();
                width = bounds.left + bounds.right;
            }
            else {
                width = this.getWidth();
            }
            canvas.translate((float)(width / 2 - layout.getWidth() / 2), (float)((mSwitchTop + top + (mSwitchBottom - bottom)) / 2 - layout.getHeight() / 2));
            layout.draw(canvas);
        }
        canvas.restoreToCount(save2);
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)"android.widget.Switch");
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        if (Build$VERSION.SDK_INT >= 14) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)"android.widget.Switch");
            CharSequence text;
            if (this.isChecked()) {
                text = this.mTextOn;
            }
            else {
                text = this.mTextOff;
            }
            if (!TextUtils.isEmpty(text)) {
                final CharSequence text2 = accessibilityNodeInfo.getText();
                if (!TextUtils.isEmpty(text2)) {
                    final StringBuilder text3 = new StringBuilder();
                    text3.append(text2).append(' ').append(text);
                    accessibilityNodeInfo.setText((CharSequence)text3);
                    return;
                }
                accessibilityNodeInfo.setText(text);
            }
        }
    }
    
    protected void onLayout(final boolean b, int max, int mSwitchTop, int mSwitchLeft, int mSwitchRight) {
        super.onLayout(b, max, mSwitchTop, mSwitchLeft, mSwitchRight);
        max = 0;
        mSwitchTop = 0;
        if (this.mThumbDrawable != null) {
            final Rect mTempRect = this.mTempRect;
            if (this.mTrackDrawable != null) {
                this.mTrackDrawable.getPadding(mTempRect);
            }
            else {
                mTempRect.setEmpty();
            }
            final Rect opticalBounds = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
            max = Math.max(0, opticalBounds.left - mTempRect.left);
            mSwitchTop = Math.max(0, opticalBounds.right - mTempRect.right);
        }
        if (ViewUtils.isLayoutRtl((View)this)) {
            mSwitchLeft = this.getPaddingLeft() + max;
            mSwitchRight = this.mSwitchWidth + mSwitchLeft - max - mSwitchTop;
        }
        else {
            mSwitchRight = this.getWidth() - this.getPaddingRight() - mSwitchTop;
            mSwitchLeft = mSwitchRight - this.mSwitchWidth + max + mSwitchTop;
        }
        switch (this.getGravity() & 0x70) {
            default: {
                mSwitchTop = this.getPaddingTop();
                max = mSwitchTop + this.mSwitchHeight;
                break;
            }
            case 16: {
                mSwitchTop = (this.getPaddingTop() + this.getHeight() - this.getPaddingBottom()) / 2 - this.mSwitchHeight / 2;
                max = mSwitchTop + this.mSwitchHeight;
                break;
            }
            case 80: {
                max = this.getHeight() - this.getPaddingBottom();
                mSwitchTop = max - this.mSwitchHeight;
                break;
            }
        }
        this.mSwitchLeft = mSwitchLeft;
        this.mSwitchTop = mSwitchTop;
        this.mSwitchBottom = max;
        this.mSwitchRight = mSwitchRight;
    }
    
    public void onMeasure(final int n, final int n2) {
        if (this.mShowText) {
            if (this.mOnLayout == null) {
                this.mOnLayout = this.makeLayout(this.mTextOn);
            }
            if (this.mOffLayout == null) {
                this.mOffLayout = this.makeLayout(this.mTextOff);
            }
        }
        final Rect mTempRect = this.mTempRect;
        int n3;
        int intrinsicHeight;
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.getPadding(mTempRect);
            n3 = this.mThumbDrawable.getIntrinsicWidth() - mTempRect.left - mTempRect.right;
            intrinsicHeight = this.mThumbDrawable.getIntrinsicHeight();
        }
        else {
            n3 = 0;
            intrinsicHeight = 0;
        }
        int n4;
        if (this.mShowText) {
            n4 = Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + this.mThumbTextPadding * 2;
        }
        else {
            n4 = 0;
        }
        this.mThumbWidth = Math.max(n4, n3);
        int intrinsicHeight2;
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.getPadding(mTempRect);
            intrinsicHeight2 = this.mTrackDrawable.getIntrinsicHeight();
        }
        else {
            mTempRect.setEmpty();
            intrinsicHeight2 = 0;
        }
        final int left = mTempRect.left;
        final int right = mTempRect.right;
        int max = left;
        int max2 = right;
        if (this.mThumbDrawable != null) {
            final Rect opticalBounds = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
            max = Math.max(left, opticalBounds.left);
            max2 = Math.max(right, opticalBounds.right);
        }
        final int max3 = Math.max(this.mSwitchMinWidth, this.mThumbWidth * 2 + max + max2);
        final int max4 = Math.max(intrinsicHeight2, intrinsicHeight);
        this.mSwitchWidth = max3;
        this.mSwitchHeight = max4;
        super.onMeasure(n, n2);
        if (this.getMeasuredHeight() < max4) {
            this.setMeasuredDimension(this.getMeasuredWidthAndState(), max4);
        }
    }
    
    public void onPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        CharSequence charSequence;
        if (this.isChecked()) {
            charSequence = this.mTextOn;
        }
        else {
            charSequence = this.mTextOff;
        }
        if (charSequence != null) {
            accessibilityEvent.getText().add(charSequence);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.mVelocityTracker.addMovement(motionEvent);
        Label_0044: {
            switch (motionEvent.getActionMasked()) {
                case 0: {
                    final float x = motionEvent.getX();
                    final float y = motionEvent.getY();
                    if (this.isEnabled() && this.hitThumb(x, y)) {
                        this.mTouchMode = 1;
                        this.mTouchX = x;
                        this.mTouchY = y;
                        break;
                    }
                    break;
                }
                case 2: {
                    switch (this.mTouchMode) {
                        case 0: {
                            break Label_0044;
                        }
                        default: {
                            break Label_0044;
                        }
                        case 1: {
                            final float x2 = motionEvent.getX();
                            final float y2 = motionEvent.getY();
                            if (Math.abs(x2 - this.mTouchX) > this.mTouchSlop || Math.abs(y2 - this.mTouchY) > this.mTouchSlop) {
                                this.mTouchMode = 2;
                                this.getParent().requestDisallowInterceptTouchEvent(true);
                                this.mTouchX = x2;
                                this.mTouchY = y2;
                                return true;
                            }
                            break Label_0044;
                        }
                        case 2: {
                            final float x3 = motionEvent.getX();
                            final int thumbScrollRange = this.getThumbScrollRange();
                            final float n = x3 - this.mTouchX;
                            float n2;
                            if (thumbScrollRange != 0) {
                                n2 = n / thumbScrollRange;
                            }
                            else if (n > 0.0f) {
                                n2 = 1.0f;
                            }
                            else {
                                n2 = -1.0f;
                            }
                            float n3 = n2;
                            if (ViewUtils.isLayoutRtl((View)this)) {
                                n3 = -n2;
                            }
                            final float constrain = constrain(this.mThumbPosition + n3, 0.0f, 1.0f);
                            if (constrain != this.mThumbPosition) {
                                this.mTouchX = x3;
                                this.setThumbPosition(constrain);
                            }
                            return true;
                        }
                    }
                    break;
                }
                case 1:
                case 3: {
                    if (this.mTouchMode == 2) {
                        this.stopDrag(motionEvent);
                        super.onTouchEvent(motionEvent);
                        return true;
                    }
                    this.mTouchMode = 0;
                    this.mVelocityTracker.clear();
                    break;
                }
            }
        }
        return super.onTouchEvent(motionEvent);
    }
    
    public void setChecked(final boolean checked) {
        super.setChecked(checked);
        final boolean checked2 = this.isChecked();
        if (this.getWindowToken() != null && ViewCompat.isLaidOut((View)this)) {
            this.animateThumbToCheckedState(checked2);
            return;
        }
        this.cancelPositionAnimator();
        float thumbPosition;
        if (checked2) {
            thumbPosition = 1.0f;
        }
        else {
            thumbPosition = 0.0f;
        }
        this.setThumbPosition(thumbPosition);
    }
    
    public void setShowText(final boolean mShowText) {
        if (this.mShowText != mShowText) {
            this.mShowText = mShowText;
            this.requestLayout();
        }
    }
    
    public void setSplitTrack(final boolean mSplitTrack) {
        this.mSplitTrack = mSplitTrack;
        this.invalidate();
    }
    
    public void setSwitchMinWidth(final int mSwitchMinWidth) {
        this.mSwitchMinWidth = mSwitchMinWidth;
        this.requestLayout();
    }
    
    public void setSwitchPadding(final int mSwitchPadding) {
        this.mSwitchPadding = mSwitchPadding;
        this.requestLayout();
    }
    
    public void setSwitchTextAppearance(final Context context, int dimensionPixelSize) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, dimensionPixelSize, R.styleable.TextAppearance);
        final ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor);
        if (colorStateList != null) {
            this.mTextColors = colorStateList;
        }
        else {
            this.mTextColors = this.getTextColors();
        }
        dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
        if (dimensionPixelSize != 0 && dimensionPixelSize != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize((float)dimensionPixelSize);
            this.requestLayout();
        }
        this.setSwitchTypefaceByIndex(obtainStyledAttributes.getInt(R.styleable.TextAppearance_android_typeface, -1), obtainStyledAttributes.getInt(R.styleable.TextAppearance_android_textStyle, -1));
        if (obtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false)) {
            this.mSwitchTransformationMethod = (TransformationMethod)new AllCapsTransformationMethod(this.getContext());
        }
        else {
            this.mSwitchTransformationMethod = null;
        }
        obtainStyledAttributes.recycle();
    }
    
    public void setSwitchTypeface(final Typeface typeface) {
        if (this.mTextPaint.getTypeface() != typeface) {
            this.mTextPaint.setTypeface(typeface);
            this.requestLayout();
            this.invalidate();
        }
    }
    
    public void setSwitchTypeface(Typeface typeface, int n) {
        boolean fakeBoldText = false;
        if (n > 0) {
            if (typeface == null) {
                typeface = Typeface.defaultFromStyle(n);
            }
            else {
                typeface = Typeface.create(typeface, n);
            }
            this.setSwitchTypeface(typeface);
            int style;
            if (typeface != null) {
                style = typeface.getStyle();
            }
            else {
                style = 0;
            }
            n &= ~style;
            final TextPaint mTextPaint = this.mTextPaint;
            if ((n & 0x1) != 0x0) {
                fakeBoldText = true;
            }
            mTextPaint.setFakeBoldText(fakeBoldText);
            final TextPaint mTextPaint2 = this.mTextPaint;
            float textSkewX;
            if ((n & 0x2) != 0x0) {
                textSkewX = -0.25f;
            }
            else {
                textSkewX = 0.0f;
            }
            mTextPaint2.setTextSkewX(textSkewX);
            return;
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        this.setSwitchTypeface(typeface);
    }
    
    public void setTextOff(final CharSequence mTextOff) {
        this.mTextOff = mTextOff;
        this.requestLayout();
    }
    
    public void setTextOn(final CharSequence mTextOn) {
        this.mTextOn = mTextOn;
        this.requestLayout();
    }
    
    public void setThumbDrawable(final Drawable mThumbDrawable) {
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setCallback((Drawable$Callback)null);
        }
        if ((this.mThumbDrawable = mThumbDrawable) != null) {
            mThumbDrawable.setCallback((Drawable$Callback)this);
        }
        this.requestLayout();
    }
    
    void setThumbPosition(final float mThumbPosition) {
        this.mThumbPosition = mThumbPosition;
        this.invalidate();
    }
    
    public void setThumbResource(final int n) {
        this.setThumbDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setThumbTextPadding(final int mThumbTextPadding) {
        this.mThumbTextPadding = mThumbTextPadding;
        this.requestLayout();
    }
    
    public void setThumbTintList(@Nullable final ColorStateList mThumbTintList) {
        this.mThumbTintList = mThumbTintList;
        this.mHasThumbTint = true;
        this.applyThumbTint();
    }
    
    public void setThumbTintMode(@Nullable final PorterDuff$Mode mThumbTintMode) {
        this.mThumbTintMode = mThumbTintMode;
        this.mHasThumbTintMode = true;
        this.applyThumbTint();
    }
    
    public void setTrackDrawable(final Drawable mTrackDrawable) {
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setCallback((Drawable$Callback)null);
        }
        if ((this.mTrackDrawable = mTrackDrawable) != null) {
            mTrackDrawable.setCallback((Drawable$Callback)this);
        }
        this.requestLayout();
    }
    
    public void setTrackResource(final int n) {
        this.setTrackDrawable(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setTrackTintList(@Nullable final ColorStateList mTrackTintList) {
        this.mTrackTintList = mTrackTintList;
        this.mHasTrackTint = true;
        this.applyTrackTint();
    }
    
    public void setTrackTintMode(@Nullable final PorterDuff$Mode mTrackTintMode) {
        this.mTrackTintMode = mTrackTintMode;
        this.mHasTrackTintMode = true;
        this.applyTrackTint();
    }
    
    public void toggle() {
        this.setChecked(!this.isChecked());
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mThumbDrawable || drawable == this.mTrackDrawable;
    }
}
