// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.View$AccessibilityDelegate;
import android.animation.ValueAnimator;
import android.view.WindowManager;
import android.util.Log;
import java.util.WeakHashMap;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import android.view.PointerIcon;
import android.view.View$OnApplyWindowInsetsListener;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityNodeProvider;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.View$DragShadowBuilder;
import android.content.ClipData;
import android.graphics.Paint;
import android.support.annotation.IdRes;
import android.view.ViewGroup;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.view.ViewParent;
import android.support.annotation.Nullable;
import android.graphics.Matrix;
import android.view.Display;
import android.graphics.Rect;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import java.util.Collection;
import android.support.annotation.NonNull;
import android.view.View;
import android.os.Build$VERSION;
import android.support.v4.os.BuildCompat;

public class ViewCompat
{
    public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
    public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
    public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
    static final ViewCompatBaseImpl IMPL;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
    public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
    @Deprecated
    public static final int LAYER_TYPE_HARDWARE = 2;
    @Deprecated
    public static final int LAYER_TYPE_NONE = 0;
    @Deprecated
    public static final int LAYER_TYPE_SOFTWARE = 1;
    public static final int LAYOUT_DIRECTION_INHERIT = 2;
    public static final int LAYOUT_DIRECTION_LOCALE = 3;
    public static final int LAYOUT_DIRECTION_LTR = 0;
    public static final int LAYOUT_DIRECTION_RTL = 1;
    @Deprecated
    public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
    @Deprecated
    public static final int MEASURED_SIZE_MASK = 16777215;
    @Deprecated
    public static final int MEASURED_STATE_MASK = -16777216;
    @Deprecated
    public static final int MEASURED_STATE_TOO_SMALL = 16777216;
    @Deprecated
    public static final int OVER_SCROLL_ALWAYS = 0;
    @Deprecated
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    @Deprecated
    public static final int OVER_SCROLL_NEVER = 2;
    public static final int SCROLL_AXIS_HORIZONTAL = 1;
    public static final int SCROLL_AXIS_NONE = 0;
    public static final int SCROLL_AXIS_VERTICAL = 2;
    public static final int SCROLL_INDICATOR_BOTTOM = 2;
    public static final int SCROLL_INDICATOR_END = 32;
    public static final int SCROLL_INDICATOR_LEFT = 4;
    public static final int SCROLL_INDICATOR_RIGHT = 8;
    public static final int SCROLL_INDICATOR_START = 16;
    public static final int SCROLL_INDICATOR_TOP = 1;
    private static final String TAG = "ViewCompat";
    
    static {
        if (BuildCompat.isAtLeastO()) {
            IMPL = (ViewCompatBaseImpl)new ViewCompatApi26Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 24) {
            IMPL = (ViewCompatBaseImpl)new ViewCompatApi24Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            IMPL = (ViewCompatBaseImpl)new ViewCompatApi23Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (ViewCompatBaseImpl)new ViewCompatApi21Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (ViewCompatBaseImpl)new ViewCompatApi19Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            IMPL = (ViewCompatBaseImpl)new ViewCompatApi18Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 17) {
            IMPL = (ViewCompatBaseImpl)new ViewCompatApi17Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (ViewCompatBaseImpl)new ViewCompatApi16Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 15) {
            IMPL = (ViewCompatBaseImpl)new ViewCompatApi15Impl();
            return;
        }
        IMPL = new ViewCompatBaseImpl();
    }
    
    public static void addKeyboardNavigationClusters(@NonNull final View view, @NonNull final Collection<View> collection, final int n) {
        ViewCompat.IMPL.addKeyboardNavigationClusters(view, collection, n);
    }
    
    public static ViewPropertyAnimatorCompat animate(final View view) {
        return ViewCompat.IMPL.animate(view);
    }
    
    @Deprecated
    public static boolean canScrollHorizontally(final View view, final int n) {
        return view.canScrollHorizontally(n);
    }
    
    @Deprecated
    public static boolean canScrollVertically(final View view, final int n) {
        return view.canScrollVertically(n);
    }
    
    public static void cancelDragAndDrop(final View view) {
        ViewCompat.IMPL.cancelDragAndDrop(view);
    }
    
    @Deprecated
    public static int combineMeasuredStates(final int n, final int n2) {
        return View.combineMeasuredStates(n, n2);
    }
    
    public static WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        return ViewCompat.IMPL.dispatchApplyWindowInsets(view, windowInsetsCompat);
    }
    
    public static void dispatchFinishTemporaryDetach(final View view) {
        ViewCompat.IMPL.dispatchFinishTemporaryDetach(view);
    }
    
    public static boolean dispatchNestedFling(final View view, final float n, final float n2, final boolean b) {
        return ViewCompat.IMPL.dispatchNestedFling(view, n, n2, b);
    }
    
    public static boolean dispatchNestedPreFling(final View view, final float n, final float n2) {
        return ViewCompat.IMPL.dispatchNestedPreFling(view, n, n2);
    }
    
    public static boolean dispatchNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int[] array2) {
        return ViewCompat.IMPL.dispatchNestedPreScroll(view, n, n2, array, array2);
    }
    
    public static boolean dispatchNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
        return ViewCompat.IMPL.dispatchNestedScroll(view, n, n2, n3, n4, array);
    }
    
    public static void dispatchStartTemporaryDetach(final View view) {
        ViewCompat.IMPL.dispatchStartTemporaryDetach(view);
    }
    
    public static int getAccessibilityLiveRegion(final View view) {
        return ViewCompat.IMPL.getAccessibilityLiveRegion(view);
    }
    
    public static AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
        return ViewCompat.IMPL.getAccessibilityNodeProvider(view);
    }
    
    @Deprecated
    public static float getAlpha(final View view) {
        return view.getAlpha();
    }
    
    public static ColorStateList getBackgroundTintList(final View view) {
        return ViewCompat.IMPL.getBackgroundTintList(view);
    }
    
    public static PorterDuff$Mode getBackgroundTintMode(final View view) {
        return ViewCompat.IMPL.getBackgroundTintMode(view);
    }
    
    public static Rect getClipBounds(final View view) {
        return ViewCompat.IMPL.getClipBounds(view);
    }
    
    public static Display getDisplay(@NonNull final View view) {
        return ViewCompat.IMPL.getDisplay(view);
    }
    
    public static float getElevation(final View view) {
        return ViewCompat.IMPL.getElevation(view);
    }
    
    public static boolean getFitsSystemWindows(final View view) {
        return ViewCompat.IMPL.getFitsSystemWindows(view);
    }
    
    public static int getImportantForAccessibility(final View view) {
        return ViewCompat.IMPL.getImportantForAccessibility(view);
    }
    
    public static int getLabelFor(final View view) {
        return ViewCompat.IMPL.getLabelFor(view);
    }
    
    @Deprecated
    public static int getLayerType(final View view) {
        return view.getLayerType();
    }
    
    public static int getLayoutDirection(final View view) {
        return ViewCompat.IMPL.getLayoutDirection(view);
    }
    
    @Deprecated
    @Nullable
    public static Matrix getMatrix(final View view) {
        return view.getMatrix();
    }
    
    @Deprecated
    public static int getMeasuredHeightAndState(final View view) {
        return view.getMeasuredHeightAndState();
    }
    
    @Deprecated
    public static int getMeasuredState(final View view) {
        return view.getMeasuredState();
    }
    
    @Deprecated
    public static int getMeasuredWidthAndState(final View view) {
        return view.getMeasuredWidthAndState();
    }
    
    public static int getMinimumHeight(final View view) {
        return ViewCompat.IMPL.getMinimumHeight(view);
    }
    
    public static int getMinimumWidth(final View view) {
        return ViewCompat.IMPL.getMinimumWidth(view);
    }
    
    public static int getNextClusterForwardId(@NonNull final View view) {
        return ViewCompat.IMPL.getNextClusterForwardId(view);
    }
    
    @Deprecated
    public static int getOverScrollMode(final View view) {
        return view.getOverScrollMode();
    }
    
    public static int getPaddingEnd(final View view) {
        return ViewCompat.IMPL.getPaddingEnd(view);
    }
    
    public static int getPaddingStart(final View view) {
        return ViewCompat.IMPL.getPaddingStart(view);
    }
    
    public static ViewParent getParentForAccessibility(final View view) {
        return ViewCompat.IMPL.getParentForAccessibility(view);
    }
    
    @Deprecated
    public static float getPivotX(final View view) {
        return view.getPivotX();
    }
    
    @Deprecated
    public static float getPivotY(final View view) {
        return view.getPivotY();
    }
    
    @Deprecated
    public static float getRotation(final View view) {
        return view.getRotation();
    }
    
    @Deprecated
    public static float getRotationX(final View view) {
        return view.getRotationX();
    }
    
    @Deprecated
    public static float getRotationY(final View view) {
        return view.getRotationY();
    }
    
    @Deprecated
    public static float getScaleX(final View view) {
        return view.getScaleX();
    }
    
    @Deprecated
    public static float getScaleY(final View view) {
        return view.getScaleY();
    }
    
    public static int getScrollIndicators(@NonNull final View view) {
        return ViewCompat.IMPL.getScrollIndicators(view);
    }
    
    public static String getTransitionName(final View view) {
        return ViewCompat.IMPL.getTransitionName(view);
    }
    
    @Deprecated
    public static float getTranslationX(final View view) {
        return view.getTranslationX();
    }
    
    @Deprecated
    public static float getTranslationY(final View view) {
        return view.getTranslationY();
    }
    
    public static float getTranslationZ(final View view) {
        return ViewCompat.IMPL.getTranslationZ(view);
    }
    
    public static int getWindowSystemUiVisibility(final View view) {
        return ViewCompat.IMPL.getWindowSystemUiVisibility(view);
    }
    
    @Deprecated
    public static float getX(final View view) {
        return view.getX();
    }
    
    @Deprecated
    public static float getY(final View view) {
        return view.getY();
    }
    
    public static float getZ(final View view) {
        return ViewCompat.IMPL.getZ(view);
    }
    
    public static boolean hasAccessibilityDelegate(final View view) {
        return ViewCompat.IMPL.hasAccessibilityDelegate(view);
    }
    
    public static boolean hasNestedScrollingParent(final View view) {
        return ViewCompat.IMPL.hasNestedScrollingParent(view);
    }
    
    public static boolean hasOnClickListeners(final View view) {
        return ViewCompat.IMPL.hasOnClickListeners(view);
    }
    
    public static boolean hasOverlappingRendering(final View view) {
        return ViewCompat.IMPL.hasOverlappingRendering(view);
    }
    
    public static boolean hasTransientState(final View view) {
        return ViewCompat.IMPL.hasTransientState(view);
    }
    
    public static boolean isAttachedToWindow(final View view) {
        return ViewCompat.IMPL.isAttachedToWindow(view);
    }
    
    public static boolean isFocusedByDefault(@NonNull final View view) {
        return ViewCompat.IMPL.isFocusedByDefault(view);
    }
    
    public static boolean isImportantForAccessibility(final View view) {
        return ViewCompat.IMPL.isImportantForAccessibility(view);
    }
    
    public static boolean isInLayout(final View view) {
        return ViewCompat.IMPL.isInLayout(view);
    }
    
    public static boolean isKeyboardNavigationCluster(@NonNull final View view) {
        return ViewCompat.IMPL.isKeyboardNavigationCluster(view);
    }
    
    public static boolean isLaidOut(final View view) {
        return ViewCompat.IMPL.isLaidOut(view);
    }
    
    public static boolean isLayoutDirectionResolved(final View view) {
        return ViewCompat.IMPL.isLayoutDirectionResolved(view);
    }
    
    public static boolean isNestedScrollingEnabled(final View view) {
        return ViewCompat.IMPL.isNestedScrollingEnabled(view);
    }
    
    @Deprecated
    public static boolean isOpaque(final View view) {
        return view.isOpaque();
    }
    
    public static boolean isPaddingRelative(final View view) {
        return ViewCompat.IMPL.isPaddingRelative(view);
    }
    
    @Deprecated
    public static void jumpDrawablesToCurrentState(final View view) {
        view.jumpDrawablesToCurrentState();
    }
    
    public static View keyboardNavigationClusterSearch(@NonNull final View view, final View view2, final int n) {
        return ViewCompat.IMPL.keyboardNavigationClusterSearch(view, view2, n);
    }
    
    public static void offsetLeftAndRight(final View view, final int n) {
        ViewCompat.IMPL.offsetLeftAndRight(view, n);
    }
    
    public static void offsetTopAndBottom(final View view, final int n) {
        ViewCompat.IMPL.offsetTopAndBottom(view, n);
    }
    
    public static WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
        return ViewCompat.IMPL.onApplyWindowInsets(view, windowInsetsCompat);
    }
    
    @Deprecated
    public static void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        view.onInitializeAccessibilityEvent(accessibilityEvent);
    }
    
    public static void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ViewCompat.IMPL.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
    }
    
    @Deprecated
    public static void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        view.onPopulateAccessibilityEvent(accessibilityEvent);
    }
    
    public static boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
        return ViewCompat.IMPL.performAccessibilityAction(view, n, bundle);
    }
    
    public static void postInvalidateOnAnimation(final View view) {
        ViewCompat.IMPL.postInvalidateOnAnimation(view);
    }
    
    public static void postInvalidateOnAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
        ViewCompat.IMPL.postInvalidateOnAnimation(view, n, n2, n3, n4);
    }
    
    public static void postOnAnimation(final View view, final Runnable runnable) {
        ViewCompat.IMPL.postOnAnimation(view, runnable);
    }
    
    public static void postOnAnimationDelayed(final View view, final Runnable runnable, final long n) {
        ViewCompat.IMPL.postOnAnimationDelayed(view, runnable, n);
    }
    
    public static void requestApplyInsets(final View view) {
        ViewCompat.IMPL.requestApplyInsets(view);
    }
    
    @Deprecated
    public static int resolveSizeAndState(final int n, final int n2, final int n3) {
        return View.resolveSizeAndState(n, n2, n3);
    }
    
    public static boolean restoreDefaultFocus(@NonNull final View view) {
        return ViewCompat.IMPL.restoreDefaultFocus(view);
    }
    
    public static void setAccessibilityDelegate(final View view, final AccessibilityDelegateCompat accessibilityDelegateCompat) {
        ViewCompat.IMPL.setAccessibilityDelegate(view, accessibilityDelegateCompat);
    }
    
    public static void setAccessibilityLiveRegion(final View view, final int n) {
        ViewCompat.IMPL.setAccessibilityLiveRegion(view, n);
    }
    
    @Deprecated
    public static void setActivated(final View view, final boolean activated) {
        view.setActivated(activated);
    }
    
    @Deprecated
    public static void setAlpha(final View view, @FloatRange(from = 0.0, to = 1.0) final float alpha) {
        view.setAlpha(alpha);
    }
    
    public static void setBackground(final View view, final Drawable drawable) {
        ViewCompat.IMPL.setBackground(view, drawable);
    }
    
    public static void setBackgroundTintList(final View view, final ColorStateList list) {
        ViewCompat.IMPL.setBackgroundTintList(view, list);
    }
    
    public static void setBackgroundTintMode(final View view, final PorterDuff$Mode porterDuff$Mode) {
        ViewCompat.IMPL.setBackgroundTintMode(view, porterDuff$Mode);
    }
    
    public static void setChildrenDrawingOrderEnabled(final ViewGroup viewGroup, final boolean b) {
        ViewCompat.IMPL.setChildrenDrawingOrderEnabled(viewGroup, b);
    }
    
    public static void setClipBounds(final View view, final Rect rect) {
        ViewCompat.IMPL.setClipBounds(view, rect);
    }
    
    public static void setElevation(final View view, final float n) {
        ViewCompat.IMPL.setElevation(view, n);
    }
    
    @Deprecated
    public static void setFitsSystemWindows(final View view, final boolean fitsSystemWindows) {
        view.setFitsSystemWindows(fitsSystemWindows);
    }
    
    public static void setFocusedByDefault(@NonNull final View view, final boolean b) {
        ViewCompat.IMPL.setFocusedByDefault(view, b);
    }
    
    public static void setHasTransientState(final View view, final boolean b) {
        ViewCompat.IMPL.setHasTransientState(view, b);
    }
    
    public static void setImportantForAccessibility(final View view, final int n) {
        ViewCompat.IMPL.setImportantForAccessibility(view, n);
    }
    
    public static void setKeyboardNavigationCluster(@NonNull final View view, final boolean b) {
        ViewCompat.IMPL.setKeyboardNavigationCluster(view, b);
    }
    
    public static void setLabelFor(final View view, @IdRes final int n) {
        ViewCompat.IMPL.setLabelFor(view, n);
    }
    
    public static void setLayerPaint(final View view, final Paint paint) {
        ViewCompat.IMPL.setLayerPaint(view, paint);
    }
    
    @Deprecated
    public static void setLayerType(final View view, final int n, final Paint paint) {
        view.setLayerType(n, paint);
    }
    
    public static void setLayoutDirection(final View view, final int n) {
        ViewCompat.IMPL.setLayoutDirection(view, n);
    }
    
    public static void setNestedScrollingEnabled(final View view, final boolean b) {
        ViewCompat.IMPL.setNestedScrollingEnabled(view, b);
    }
    
    public static void setNextClusterForwardId(@NonNull final View view, final int n) {
        ViewCompat.IMPL.setNextClusterForwardId(view, n);
    }
    
    public static void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        ViewCompat.IMPL.setOnApplyWindowInsetsListener(view, onApplyWindowInsetsListener);
    }
    
    @Deprecated
    public static void setOverScrollMode(final View view, final int overScrollMode) {
        view.setOverScrollMode(overScrollMode);
    }
    
    public static void setPaddingRelative(final View view, final int n, final int n2, final int n3, final int n4) {
        ViewCompat.IMPL.setPaddingRelative(view, n, n2, n3, n4);
    }
    
    @Deprecated
    public static void setPivotX(final View view, final float pivotX) {
        view.setPivotX(pivotX);
    }
    
    @Deprecated
    public static void setPivotY(final View view, final float pivotY) {
        view.setPivotY(pivotY);
    }
    
    public static void setPointerIcon(@NonNull final View view, final PointerIconCompat pointerIconCompat) {
        ViewCompat.IMPL.setPointerIcon(view, pointerIconCompat);
    }
    
    @Deprecated
    public static void setRotation(final View view, final float rotation) {
        view.setRotation(rotation);
    }
    
    @Deprecated
    public static void setRotationX(final View view, final float rotationX) {
        view.setRotationX(rotationX);
    }
    
    @Deprecated
    public static void setRotationY(final View view, final float rotationY) {
        view.setRotationY(rotationY);
    }
    
    @Deprecated
    public static void setSaveFromParentEnabled(final View view, final boolean saveFromParentEnabled) {
        view.setSaveFromParentEnabled(saveFromParentEnabled);
    }
    
    @Deprecated
    public static void setScaleX(final View view, final float scaleX) {
        view.setScaleX(scaleX);
    }
    
    @Deprecated
    public static void setScaleY(final View view, final float scaleY) {
        view.setScaleY(scaleY);
    }
    
    public static void setScrollIndicators(@NonNull final View view, final int n) {
        ViewCompat.IMPL.setScrollIndicators(view, n);
    }
    
    public static void setScrollIndicators(@NonNull final View view, final int n, final int n2) {
        ViewCompat.IMPL.setScrollIndicators(view, n, n2);
    }
    
    public static void setTooltipText(@NonNull final View view, @Nullable final CharSequence charSequence) {
        ViewCompat.IMPL.setTooltipText(view, charSequence);
    }
    
    public static void setTransitionName(final View view, final String s) {
        ViewCompat.IMPL.setTransitionName(view, s);
    }
    
    @Deprecated
    public static void setTranslationX(final View view, final float translationX) {
        view.setTranslationX(translationX);
    }
    
    @Deprecated
    public static void setTranslationY(final View view, final float translationY) {
        view.setTranslationY(translationY);
    }
    
    public static void setTranslationZ(final View view, final float n) {
        ViewCompat.IMPL.setTranslationZ(view, n);
    }
    
    @Deprecated
    public static void setX(final View view, final float x) {
        view.setX(x);
    }
    
    @Deprecated
    public static void setY(final View view, final float y) {
        view.setY(y);
    }
    
    public static void setZ(final View view, final float n) {
        ViewCompat.IMPL.setZ(view, n);
    }
    
    public static boolean startDragAndDrop(final View view, final ClipData clipData, final View$DragShadowBuilder view$DragShadowBuilder, final Object o, final int n) {
        return ViewCompat.IMPL.startDragAndDrop(view, clipData, view$DragShadowBuilder, o, n);
    }
    
    public static boolean startNestedScroll(final View view, final int n) {
        return ViewCompat.IMPL.startNestedScroll(view, n);
    }
    
    public static void stopNestedScroll(final View view) {
        ViewCompat.IMPL.stopNestedScroll(view);
    }
    
    public static void updateDragShadow(final View view, final View$DragShadowBuilder view$DragShadowBuilder) {
        ViewCompat.IMPL.updateDragShadow(view, view$DragShadowBuilder);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface FocusDirection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface FocusRealDirection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface FocusRelativeDirection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface ScrollIndicators {
    }
    
    @RequiresApi(15)
    static class ViewCompatApi15Impl extends ViewCompatBaseImpl
    {
        @Override
        public boolean hasOnClickListeners(final View view) {
            return view.hasOnClickListeners();
        }
    }
    
    @RequiresApi(16)
    static class ViewCompatApi16Impl extends ViewCompatApi15Impl
    {
        @Override
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
            final AccessibilityNodeProvider accessibilityNodeProvider = view.getAccessibilityNodeProvider();
            if (accessibilityNodeProvider != null) {
                return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
            }
            return null;
        }
        
        @Override
        public boolean getFitsSystemWindows(final View view) {
            return view.getFitsSystemWindows();
        }
        
        @Override
        public int getImportantForAccessibility(final View view) {
            return view.getImportantForAccessibility();
        }
        
        @Override
        public int getMinimumHeight(final View view) {
            return view.getMinimumHeight();
        }
        
        @Override
        public int getMinimumWidth(final View view) {
            return view.getMinimumWidth();
        }
        
        @Override
        public ViewParent getParentForAccessibility(final View view) {
            return view.getParentForAccessibility();
        }
        
        @Override
        public boolean hasOverlappingRendering(final View view) {
            return view.hasOverlappingRendering();
        }
        
        @Override
        public boolean hasTransientState(final View view) {
            return view.hasTransientState();
        }
        
        @Override
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            return view.performAccessibilityAction(n, bundle);
        }
        
        @Override
        public void postInvalidateOnAnimation(final View view) {
            view.postInvalidateOnAnimation();
        }
        
        @Override
        public void postInvalidateOnAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
            view.postInvalidateOnAnimation(n, n2, n3, n4);
        }
        
        @Override
        public void postOnAnimation(final View view, final Runnable runnable) {
            view.postOnAnimation(runnable);
        }
        
        @Override
        public void postOnAnimationDelayed(final View view, final Runnable runnable, final long n) {
            view.postOnAnimationDelayed(runnable, n);
        }
        
        @Override
        public void requestApplyInsets(final View view) {
            view.requestFitSystemWindows();
        }
        
        @Override
        public void setBackground(final View view, final Drawable background) {
            view.setBackground(background);
        }
        
        @Override
        public void setHasTransientState(final View view, final boolean hasTransientState) {
            view.setHasTransientState(hasTransientState);
        }
        
        @Override
        public void setImportantForAccessibility(final View view, final int n) {
            int importantForAccessibility = n;
            if (n == 4) {
                importantForAccessibility = 2;
            }
            view.setImportantForAccessibility(importantForAccessibility);
        }
    }
    
    @RequiresApi(17)
    static class ViewCompatApi17Impl extends ViewCompatApi16Impl
    {
        @Override
        public Display getDisplay(final View view) {
            return view.getDisplay();
        }
        
        @Override
        public int getLabelFor(final View view) {
            return view.getLabelFor();
        }
        
        @Override
        public int getLayoutDirection(final View view) {
            return view.getLayoutDirection();
        }
        
        @Override
        public int getPaddingEnd(final View view) {
            return view.getPaddingEnd();
        }
        
        @Override
        public int getPaddingStart(final View view) {
            return view.getPaddingStart();
        }
        
        @Override
        public int getWindowSystemUiVisibility(final View view) {
            return view.getWindowSystemUiVisibility();
        }
        
        @Override
        public boolean isPaddingRelative(final View view) {
            return view.isPaddingRelative();
        }
        
        @Override
        public void setLabelFor(final View view, final int labelFor) {
            view.setLabelFor(labelFor);
        }
        
        @Override
        public void setLayerPaint(final View view, final Paint layerPaint) {
            view.setLayerPaint(layerPaint);
        }
        
        @Override
        public void setLayoutDirection(final View view, final int layoutDirection) {
            view.setLayoutDirection(layoutDirection);
        }
        
        @Override
        public void setPaddingRelative(final View view, final int n, final int n2, final int n3, final int n4) {
            view.setPaddingRelative(n, n2, n3, n4);
        }
    }
    
    @RequiresApi(18)
    static class ViewCompatApi18Impl extends ViewCompatApi17Impl
    {
        @Override
        public Rect getClipBounds(final View view) {
            return view.getClipBounds();
        }
        
        @Override
        public boolean isInLayout(final View view) {
            return view.isInLayout();
        }
        
        @Override
        public void setClipBounds(final View view, final Rect clipBounds) {
            view.setClipBounds(clipBounds);
        }
    }
    
    @RequiresApi(19)
    static class ViewCompatApi19Impl extends ViewCompatApi18Impl
    {
        @Override
        public int getAccessibilityLiveRegion(final View view) {
            return view.getAccessibilityLiveRegion();
        }
        
        @Override
        public boolean isAttachedToWindow(final View view) {
            return view.isAttachedToWindow();
        }
        
        @Override
        public boolean isLaidOut(final View view) {
            return view.isLaidOut();
        }
        
        @Override
        public boolean isLayoutDirectionResolved(final View view) {
            return view.isLayoutDirectionResolved();
        }
        
        @Override
        public void setAccessibilityLiveRegion(final View view, final int accessibilityLiveRegion) {
            view.setAccessibilityLiveRegion(accessibilityLiveRegion);
        }
        
        @Override
        public void setImportantForAccessibility(final View view, final int importantForAccessibility) {
            view.setImportantForAccessibility(importantForAccessibility);
        }
    }
    
    @RequiresApi(21)
    static class ViewCompatApi21Impl extends ViewCompatApi19Impl
    {
        private static ThreadLocal<Rect> sThreadLocalRect;
        
        private static Rect getEmptyTempRect() {
            if (ViewCompatApi21Impl.sThreadLocalRect == null) {
                ViewCompatApi21Impl.sThreadLocalRect = new ThreadLocal<Rect>();
            }
            Rect rect;
            if ((rect = ViewCompatApi21Impl.sThreadLocalRect.get()) == null) {
                rect = new Rect();
                ViewCompatApi21Impl.sThreadLocalRect.set(rect);
            }
            rect.setEmpty();
            return rect;
        }
        
        @Override
        public WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets dispatchApplyWindowInsets = view.dispatchApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2 = windowInsets;
            if (dispatchApplyWindowInsets != windowInsets) {
                windowInsets2 = new WindowInsets(dispatchApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        
        @Override
        public boolean dispatchNestedFling(final View view, final float n, final float n2, final boolean b) {
            return view.dispatchNestedFling(n, n2, b);
        }
        
        @Override
        public boolean dispatchNestedPreFling(final View view, final float n, final float n2) {
            return view.dispatchNestedPreFling(n, n2);
        }
        
        @Override
        public boolean dispatchNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int[] array2) {
            return view.dispatchNestedPreScroll(n, n2, array, array2);
        }
        
        @Override
        public boolean dispatchNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
            return view.dispatchNestedScroll(n, n2, n3, n4, array);
        }
        
        @Override
        public ColorStateList getBackgroundTintList(final View view) {
            return view.getBackgroundTintList();
        }
        
        @Override
        public PorterDuff$Mode getBackgroundTintMode(final View view) {
            return view.getBackgroundTintMode();
        }
        
        @Override
        public float getElevation(final View view) {
            return view.getElevation();
        }
        
        @Override
        public String getTransitionName(final View view) {
            return view.getTransitionName();
        }
        
        @Override
        public float getTranslationZ(final View view) {
            return view.getTranslationZ();
        }
        
        @Override
        public float getZ(final View view) {
            return view.getZ();
        }
        
        @Override
        public boolean hasNestedScrollingParent(final View view) {
            return view.hasNestedScrollingParent();
        }
        
        @Override
        public boolean isImportantForAccessibility(final View view) {
            return view.isImportantForAccessibility();
        }
        
        @Override
        public boolean isNestedScrollingEnabled(final View view) {
            return view.isNestedScrollingEnabled();
        }
        
        @Override
        public void offsetLeftAndRight(final View view, final int n) {
            final Rect emptyTempRect = getEmptyTempRect();
            boolean b = false;
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                final View view2 = (View)parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                if (!emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                    b = true;
                }
                else {
                    b = false;
                }
            }
            super.offsetLeftAndRight(view, n);
            if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)parent).invalidate(emptyTempRect);
            }
        }
        
        @Override
        public void offsetTopAndBottom(final View view, final int n) {
            final Rect emptyTempRect = getEmptyTempRect();
            boolean b = false;
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                final View view2 = (View)parent;
                emptyTempRect.set(view2.getLeft(), view2.getTop(), view2.getRight(), view2.getBottom());
                if (!emptyTempRect.intersects(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                    b = true;
                }
                else {
                    b = false;
                }
            }
            super.offsetTopAndBottom(view, n);
            if (b && emptyTempRect.intersect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom())) {
                ((View)parent).invalidate(emptyTempRect);
            }
        }
        
        @Override
        public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
            final WindowInsets windowInsets = (WindowInsets)WindowInsetsCompat.unwrap(windowInsetsCompat);
            final WindowInsets onApplyWindowInsets = view.onApplyWindowInsets(windowInsets);
            WindowInsets windowInsets2 = windowInsets;
            if (onApplyWindowInsets != windowInsets) {
                windowInsets2 = new WindowInsets(onApplyWindowInsets);
            }
            return WindowInsetsCompat.wrap(windowInsets2);
        }
        
        @Override
        public void requestApplyInsets(final View view) {
            view.requestApplyInsets();
        }
        
        @Override
        public void setBackgroundTintList(final View view, final ColorStateList backgroundTintList) {
            view.setBackgroundTintList(backgroundTintList);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable background = view.getBackground();
                boolean b;
                if (view.getBackgroundTintList() != null && view.getBackgroundTintMode() != null) {
                    b = true;
                }
                else {
                    b = false;
                }
                if (background != null && b) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        }
        
        @Override
        public void setBackgroundTintMode(final View view, final PorterDuff$Mode backgroundTintMode) {
            view.setBackgroundTintMode(backgroundTintMode);
            if (Build$VERSION.SDK_INT == 21) {
                final Drawable background = view.getBackground();
                boolean b;
                if (view.getBackgroundTintList() != null && view.getBackgroundTintMode() != null) {
                    b = true;
                }
                else {
                    b = false;
                }
                if (background != null && b) {
                    if (background.isStateful()) {
                        background.setState(view.getDrawableState());
                    }
                    view.setBackground(background);
                }
            }
        }
        
        @Override
        public void setElevation(final View view, final float elevation) {
            view.setElevation(elevation);
        }
        
        @Override
        public void setNestedScrollingEnabled(final View view, final boolean nestedScrollingEnabled) {
            view.setNestedScrollingEnabled(nestedScrollingEnabled);
        }
        
        @Override
        public void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
            if (onApplyWindowInsetsListener == null) {
                view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)null);
                return;
            }
            view.setOnApplyWindowInsetsListener((View$OnApplyWindowInsetsListener)new View$OnApplyWindowInsetsListener() {
                public WindowInsets onApplyWindowInsets(final View view, final WindowInsets windowInsets) {
                    return (WindowInsets)WindowInsetsCompat.unwrap(onApplyWindowInsetsListener.onApplyWindowInsets(view, WindowInsetsCompat.wrap(windowInsets)));
                }
            });
        }
        
        @Override
        public void setTransitionName(final View view, final String transitionName) {
            view.setTransitionName(transitionName);
        }
        
        @Override
        public void setTranslationZ(final View view, final float translationZ) {
            view.setTranslationZ(translationZ);
        }
        
        @Override
        public void setZ(final View view, final float z) {
            view.setZ(z);
        }
        
        @Override
        public boolean startNestedScroll(final View view, final int n) {
            return view.startNestedScroll(n);
        }
        
        @Override
        public void stopNestedScroll(final View view) {
            view.stopNestedScroll();
        }
    }
    
    @RequiresApi(23)
    static class ViewCompatApi23Impl extends ViewCompatApi21Impl
    {
        @Override
        public int getScrollIndicators(final View view) {
            return view.getScrollIndicators();
        }
        
        @Override
        public void offsetLeftAndRight(final View view, final int n) {
            view.offsetLeftAndRight(n);
        }
        
        @Override
        public void offsetTopAndBottom(final View view, final int n) {
            view.offsetTopAndBottom(n);
        }
        
        @Override
        public void setScrollIndicators(final View view, final int scrollIndicators) {
            view.setScrollIndicators(scrollIndicators);
        }
        
        @Override
        public void setScrollIndicators(final View view, final int n, final int n2) {
            view.setScrollIndicators(n, n2);
        }
    }
    
    @RequiresApi(24)
    static class ViewCompatApi24Impl extends ViewCompatApi23Impl
    {
        @Override
        public void cancelDragAndDrop(final View view) {
            view.cancelDragAndDrop();
        }
        
        @Override
        public void dispatchFinishTemporaryDetach(final View view) {
            view.dispatchFinishTemporaryDetach();
        }
        
        @Override
        public void dispatchStartTemporaryDetach(final View view) {
            view.dispatchStartTemporaryDetach();
        }
        
        @Override
        public void setPointerIcon(final View view, final PointerIconCompat pointerIconCompat) {
            Object pointerIcon;
            if (pointerIconCompat != null) {
                pointerIcon = pointerIconCompat.getPointerIcon();
            }
            else {
                pointerIcon = null;
            }
            view.setPointerIcon((PointerIcon)pointerIcon);
        }
        
        @Override
        public boolean startDragAndDrop(final View view, final ClipData clipData, final View$DragShadowBuilder view$DragShadowBuilder, final Object o, final int n) {
            return view.startDragAndDrop(clipData, view$DragShadowBuilder, o, n);
        }
        
        @Override
        public void updateDragShadow(final View view, final View$DragShadowBuilder view$DragShadowBuilder) {
            view.updateDragShadow(view$DragShadowBuilder);
        }
    }
    
    @RequiresApi(26)
    static class ViewCompatApi26Impl extends ViewCompatApi24Impl
    {
        @Override
        public void addKeyboardNavigationClusters(@NonNull final View view, @NonNull final Collection<View> collection, final int n) {
            view.addKeyboardNavigationClusters((Collection)collection, n);
        }
        
        @Override
        public int getNextClusterForwardId(@NonNull final View view) {
            return view.getNextClusterForwardId();
        }
        
        @Override
        public boolean isFocusedByDefault(@NonNull final View view) {
            return view.isFocusedByDefault();
        }
        
        @Override
        public boolean isKeyboardNavigationCluster(@NonNull final View view) {
            return view.isKeyboardNavigationCluster();
        }
        
        @Override
        public View keyboardNavigationClusterSearch(@NonNull final View view, final View view2, final int n) {
            return view.keyboardNavigationClusterSearch(view2, n);
        }
        
        @Override
        public boolean restoreDefaultFocus(@NonNull final View view) {
            return view.restoreDefaultFocus();
        }
        
        @Override
        public void setFocusedByDefault(@NonNull final View view, final boolean focusedByDefault) {
            view.setFocusedByDefault(focusedByDefault);
        }
        
        @Override
        public void setKeyboardNavigationCluster(@NonNull final View view, final boolean keyboardNavigationCluster) {
            view.setKeyboardNavigationCluster(keyboardNavigationCluster);
        }
        
        @Override
        public void setNextClusterForwardId(@NonNull final View view, final int nextClusterForwardId) {
            view.setNextClusterForwardId(nextClusterForwardId);
        }
        
        @Override
        public void setTooltipText(final View view, final CharSequence tooltipText) {
            view.setTooltipText(tooltipText);
        }
    }
    
    static class ViewCompatBaseImpl
    {
        static boolean sAccessibilityDelegateCheckFailed;
        static Field sAccessibilityDelegateField;
        private static Method sChildrenDrawingOrderMethod;
        private static Field sMinHeightField;
        private static boolean sMinHeightFieldFetched;
        private static Field sMinWidthField;
        private static boolean sMinWidthFieldFetched;
        private static WeakHashMap<View, String> sTransitionNameMap;
        private Method mDispatchFinishTemporaryDetach;
        private Method mDispatchStartTemporaryDetach;
        private boolean mTempDetachBound;
        WeakHashMap<View, ViewPropertyAnimatorCompat> mViewPropertyAnimatorCompatMap;
        
        static {
            ViewCompatBaseImpl.sAccessibilityDelegateCheckFailed = false;
        }
        
        ViewCompatBaseImpl() {
            this.mViewPropertyAnimatorCompatMap = null;
        }
        
        private void bindTempDetach() {
            while (true) {
                try {
                    this.mDispatchStartTemporaryDetach = View.class.getDeclaredMethod("dispatchStartTemporaryDetach", (Class<?>[])new Class[0]);
                    this.mDispatchFinishTemporaryDetach = View.class.getDeclaredMethod("dispatchFinishTemporaryDetach", (Class<?>[])new Class[0]);
                    this.mTempDetachBound = true;
                }
                catch (NoSuchMethodException ex) {
                    Log.e("ViewCompat", "Couldn't find method", (Throwable)ex);
                    continue;
                }
                break;
            }
        }
        
        private static void tickleInvalidationFlag(final View view) {
            final float translationY = view.getTranslationY();
            view.setTranslationY(1.0f + translationY);
            view.setTranslationY(translationY);
        }
        
        public void addKeyboardNavigationClusters(@NonNull final View view, @NonNull final Collection<View> collection, final int n) {
        }
        
        public ViewPropertyAnimatorCompat animate(final View view) {
            if (this.mViewPropertyAnimatorCompatMap == null) {
                this.mViewPropertyAnimatorCompatMap = new WeakHashMap<View, ViewPropertyAnimatorCompat>();
            }
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat;
            if ((viewPropertyAnimatorCompat = this.mViewPropertyAnimatorCompatMap.get(view)) == null) {
                viewPropertyAnimatorCompat = new ViewPropertyAnimatorCompat(view);
                this.mViewPropertyAnimatorCompatMap.put(view, viewPropertyAnimatorCompat);
            }
            return viewPropertyAnimatorCompat;
        }
        
        public void cancelDragAndDrop(final View view) {
        }
        
        public WindowInsetsCompat dispatchApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }
        
        public void dispatchFinishTemporaryDetach(final View view) {
            if (!this.mTempDetachBound) {
                this.bindTempDetach();
            }
            if (this.mDispatchFinishTemporaryDetach != null) {
                try {
                    this.mDispatchFinishTemporaryDetach.invoke(view, new Object[0]);
                    return;
                }
                catch (Exception ex) {
                    Log.d("ViewCompat", "Error calling dispatchFinishTemporaryDetach", (Throwable)ex);
                    return;
                }
            }
            view.onFinishTemporaryDetach();
        }
        
        public boolean dispatchNestedFling(final View view, final float n, final float n2, final boolean b) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedFling(n, n2, b);
        }
        
        public boolean dispatchNestedPreFling(final View view, final float n, final float n2) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedPreFling(n, n2);
        }
        
        public boolean dispatchNestedPreScroll(final View view, final int n, final int n2, final int[] array, final int[] array2) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedPreScroll(n, n2, array, array2);
        }
        
        public boolean dispatchNestedScroll(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).dispatchNestedScroll(n, n2, n3, n4, array);
        }
        
        public void dispatchStartTemporaryDetach(final View view) {
            if (!this.mTempDetachBound) {
                this.bindTempDetach();
            }
            if (this.mDispatchStartTemporaryDetach != null) {
                try {
                    this.mDispatchStartTemporaryDetach.invoke(view, new Object[0]);
                    return;
                }
                catch (Exception ex) {
                    Log.d("ViewCompat", "Error calling dispatchStartTemporaryDetach", (Throwable)ex);
                    return;
                }
            }
            view.onStartTemporaryDetach();
        }
        
        public int getAccessibilityLiveRegion(final View view) {
            return 0;
        }
        
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
            return null;
        }
        
        public ColorStateList getBackgroundTintList(final View view) {
            if (view instanceof TintableBackgroundView) {
                return ((TintableBackgroundView)view).getSupportBackgroundTintList();
            }
            return null;
        }
        
        public PorterDuff$Mode getBackgroundTintMode(final View view) {
            if (view instanceof TintableBackgroundView) {
                return ((TintableBackgroundView)view).getSupportBackgroundTintMode();
            }
            return null;
        }
        
        public Rect getClipBounds(final View view) {
            return null;
        }
        
        public Display getDisplay(final View view) {
            if (this.isAttachedToWindow(view)) {
                return ((WindowManager)view.getContext().getSystemService("window")).getDefaultDisplay();
            }
            return null;
        }
        
        public float getElevation(final View view) {
            return 0.0f;
        }
        
        public boolean getFitsSystemWindows(final View view) {
            return false;
        }
        
        long getFrameTime() {
            return ValueAnimator.getFrameDelay();
        }
        
        public int getImportantForAccessibility(final View view) {
            return 0;
        }
        
        public int getLabelFor(final View view) {
            return 0;
        }
        
        public int getLayoutDirection(final View view) {
            return 0;
        }
        
        public int getMinimumHeight(final View view) {
            Label_0027: {
                if (ViewCompatBaseImpl.sMinHeightFieldFetched) {
                    break Label_0027;
                }
                while (true) {
                    try {
                        (ViewCompatBaseImpl.sMinHeightField = View.class.getDeclaredField("mMinHeight")).setAccessible(true);
                        ViewCompatBaseImpl.sMinHeightFieldFetched = true;
                        if (ViewCompatBaseImpl.sMinHeightField != null) {
                            try {
                                return (int)ViewCompatBaseImpl.sMinHeightField.get(view);
                            }
                            catch (Exception ex) {}
                        }
                        return 0;
                    }
                    catch (NoSuchFieldException ex2) {
                        continue;
                    }
                    break;
                }
            }
        }
        
        public int getMinimumWidth(final View view) {
            Label_0027: {
                if (ViewCompatBaseImpl.sMinWidthFieldFetched) {
                    break Label_0027;
                }
                while (true) {
                    try {
                        (ViewCompatBaseImpl.sMinWidthField = View.class.getDeclaredField("mMinWidth")).setAccessible(true);
                        ViewCompatBaseImpl.sMinWidthFieldFetched = true;
                        if (ViewCompatBaseImpl.sMinWidthField != null) {
                            try {
                                return (int)ViewCompatBaseImpl.sMinWidthField.get(view);
                            }
                            catch (Exception ex) {}
                        }
                        return 0;
                    }
                    catch (NoSuchFieldException ex2) {
                        continue;
                    }
                    break;
                }
            }
        }
        
        public int getNextClusterForwardId(@NonNull final View view) {
            return -1;
        }
        
        public int getPaddingEnd(final View view) {
            return view.getPaddingRight();
        }
        
        public int getPaddingStart(final View view) {
            return view.getPaddingLeft();
        }
        
        public ViewParent getParentForAccessibility(final View view) {
            return view.getParent();
        }
        
        public int getScrollIndicators(final View view) {
            return 0;
        }
        
        public String getTransitionName(final View view) {
            if (ViewCompatBaseImpl.sTransitionNameMap == null) {
                return null;
            }
            return ViewCompatBaseImpl.sTransitionNameMap.get(view);
        }
        
        public float getTranslationZ(final View view) {
            return 0.0f;
        }
        
        public int getWindowSystemUiVisibility(final View view) {
            return 0;
        }
        
        public float getZ(final View view) {
            return this.getTranslationZ(view) + this.getElevation(view);
        }
        
        public boolean hasAccessibilityDelegate(final View p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: iconst_1       
            //     1: istore_2       
            //     2: getstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sAccessibilityDelegateCheckFailed:Z
            //     5: ifeq            10
            //     8: iconst_0       
            //     9: ireturn        
            //    10: getstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sAccessibilityDelegateField:Ljava/lang/reflect/Field;
            //    13: ifnonnull       34
            //    16: ldc             Landroid/view/View;.class
            //    18: ldc_w           "mAccessibilityDelegate"
            //    21: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
            //    24: putstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sAccessibilityDelegateField:Ljava/lang/reflect/Field;
            //    27: getstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sAccessibilityDelegateField:Ljava/lang/reflect/Field;
            //    30: iconst_1       
            //    31: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
            //    34: getstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sAccessibilityDelegateField:Ljava/lang/reflect/Field;
            //    37: aload_1        
            //    38: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
            //    41: astore_1       
            //    42: aload_1        
            //    43: ifnull          55
            //    46: iload_2        
            //    47: ireturn        
            //    48: astore_1       
            //    49: iconst_1       
            //    50: putstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sAccessibilityDelegateCheckFailed:Z
            //    53: iconst_0       
            //    54: ireturn        
            //    55: iconst_0       
            //    56: istore_2       
            //    57: goto            46
            //    60: astore_1       
            //    61: iconst_1       
            //    62: putstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sAccessibilityDelegateCheckFailed:Z
            //    65: iconst_0       
            //    66: ireturn        
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  16     34     48     55     Ljava/lang/Throwable;
            //  34     42     60     67     Ljava/lang/Throwable;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0034:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public boolean hasNestedScrollingParent(final View view) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).hasNestedScrollingParent();
        }
        
        public boolean hasOnClickListeners(final View view) {
            return false;
        }
        
        public boolean hasOverlappingRendering(final View view) {
            return true;
        }
        
        public boolean hasTransientState(final View view) {
            return false;
        }
        
        public boolean isAttachedToWindow(final View view) {
            return view.getWindowToken() != null;
        }
        
        public boolean isFocusedByDefault(@NonNull final View view) {
            return false;
        }
        
        public boolean isImportantForAccessibility(final View view) {
            return true;
        }
        
        public boolean isInLayout(final View view) {
            return false;
        }
        
        public boolean isKeyboardNavigationCluster(@NonNull final View view) {
            return false;
        }
        
        public boolean isLaidOut(final View view) {
            return view.getWidth() > 0 && view.getHeight() > 0;
        }
        
        public boolean isLayoutDirectionResolved(final View view) {
            return false;
        }
        
        public boolean isNestedScrollingEnabled(final View view) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).isNestedScrollingEnabled();
        }
        
        public boolean isPaddingRelative(final View view) {
            return false;
        }
        
        public View keyboardNavigationClusterSearch(@NonNull final View view, final View view2, final int n) {
            return null;
        }
        
        public void offsetLeftAndRight(final View view, final int n) {
            view.offsetLeftAndRight(n);
            if (view.getVisibility() == 0) {
                tickleInvalidationFlag(view);
                final ViewParent parent = view.getParent();
                if (parent instanceof View) {
                    tickleInvalidationFlag((View)parent);
                }
            }
        }
        
        public void offsetTopAndBottom(final View view, final int n) {
            view.offsetTopAndBottom(n);
            if (view.getVisibility() == 0) {
                tickleInvalidationFlag(view);
                final ViewParent parent = view.getParent();
                if (parent instanceof View) {
                    tickleInvalidationFlag((View)parent);
                }
            }
        }
        
        public WindowInsetsCompat onApplyWindowInsets(final View view, final WindowInsetsCompat windowInsetsCompat) {
            return windowInsetsCompat;
        }
        
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            view.onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat.unwrap());
        }
        
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            return false;
        }
        
        public void postInvalidateOnAnimation(final View view) {
            view.postInvalidate();
        }
        
        public void postInvalidateOnAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
            view.postInvalidate(n, n2, n3, n4);
        }
        
        public void postOnAnimation(final View view, final Runnable runnable) {
            view.postDelayed(runnable, this.getFrameTime());
        }
        
        public void postOnAnimationDelayed(final View view, final Runnable runnable, final long n) {
            view.postDelayed(runnable, this.getFrameTime() + n);
        }
        
        public void requestApplyInsets(final View view) {
        }
        
        public boolean restoreDefaultFocus(@NonNull final View view) {
            return view.requestFocus();
        }
        
        public void setAccessibilityDelegate(final View view, @Nullable final AccessibilityDelegateCompat accessibilityDelegateCompat) {
            View$AccessibilityDelegate bridge;
            if (accessibilityDelegateCompat == null) {
                bridge = null;
            }
            else {
                bridge = accessibilityDelegateCompat.getBridge();
            }
            view.setAccessibilityDelegate(bridge);
        }
        
        public void setAccessibilityLiveRegion(final View view, final int n) {
        }
        
        public void setBackground(final View view, final Drawable backgroundDrawable) {
            view.setBackgroundDrawable(backgroundDrawable);
        }
        
        public void setBackgroundTintList(final View view, final ColorStateList supportBackgroundTintList) {
            if (view instanceof TintableBackgroundView) {
                ((TintableBackgroundView)view).setSupportBackgroundTintList(supportBackgroundTintList);
            }
        }
        
        public void setBackgroundTintMode(final View view, final PorterDuff$Mode supportBackgroundTintMode) {
            if (view instanceof TintableBackgroundView) {
                ((TintableBackgroundView)view).setSupportBackgroundTintMode(supportBackgroundTintMode);
            }
        }
        
        public void setChildrenDrawingOrderEnabled(final ViewGroup p0, final boolean p1) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: getstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sChildrenDrawingOrderMethod:Ljava/lang/reflect/Method;
            //     3: ifnonnull       35
            //     6: ldc_w           Landroid/view/ViewGroup;.class
            //     9: ldc_w           "setChildrenDrawingOrderEnabled"
            //    12: iconst_1       
            //    13: anewarray       Ljava/lang/Class;
            //    16: dup            
            //    17: iconst_0       
            //    18: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
            //    21: aastore        
            //    22: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
            //    25: putstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sChildrenDrawingOrderMethod:Ljava/lang/reflect/Method;
            //    28: getstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sChildrenDrawingOrderMethod:Ljava/lang/reflect/Method;
            //    31: iconst_1       
            //    32: invokevirtual   java/lang/reflect/Method.setAccessible:(Z)V
            //    35: getstatic       android/support/v4/view/ViewCompat$ViewCompatBaseImpl.sChildrenDrawingOrderMethod:Ljava/lang/reflect/Method;
            //    38: aload_1        
            //    39: iconst_1       
            //    40: anewarray       Ljava/lang/Object;
            //    43: dup            
            //    44: iconst_0       
            //    45: iload_2        
            //    46: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    49: aastore        
            //    50: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
            //    53: pop            
            //    54: return         
            //    55: astore_3       
            //    56: ldc             "ViewCompat"
            //    58: ldc_w           "Unable to find childrenDrawingOrderEnabled"
            //    61: aload_3        
            //    62: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //    65: pop            
            //    66: goto            28
            //    69: astore_1       
            //    70: ldc             "ViewCompat"
            //    72: ldc_w           "Unable to invoke childrenDrawingOrderEnabled"
            //    75: aload_1        
            //    76: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //    79: pop            
            //    80: return         
            //    81: astore_1       
            //    82: ldc             "ViewCompat"
            //    84: ldc_w           "Unable to invoke childrenDrawingOrderEnabled"
            //    87: aload_1        
            //    88: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //    91: pop            
            //    92: return         
            //    93: astore_1       
            //    94: ldc             "ViewCompat"
            //    96: ldc_w           "Unable to invoke childrenDrawingOrderEnabled"
            //    99: aload_1        
            //   100: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   103: pop            
            //   104: return         
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                         
            //  -----  -----  -----  -----  ---------------------------------------------
            //  6      28     55     69     Ljava/lang/NoSuchMethodException;
            //  35     54     69     81     Ljava/lang/IllegalAccessException;
            //  35     54     81     93     Ljava/lang/IllegalArgumentException;
            //  35     54     93     105    Ljava/lang/reflect/InvocationTargetException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0035:
            //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
            //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        public void setClipBounds(final View view, final Rect rect) {
        }
        
        public void setElevation(final View view, final float n) {
        }
        
        public void setFocusedByDefault(@NonNull final View view, final boolean b) {
        }
        
        public void setHasTransientState(final View view, final boolean b) {
        }
        
        public void setImportantForAccessibility(final View view, final int n) {
        }
        
        public void setKeyboardNavigationCluster(@NonNull final View view, final boolean b) {
        }
        
        public void setLabelFor(final View view, final int n) {
        }
        
        public void setLayerPaint(final View view, final Paint paint) {
            view.setLayerType(view.getLayerType(), paint);
            view.invalidate();
        }
        
        public void setLayoutDirection(final View view, final int n) {
        }
        
        public void setNestedScrollingEnabled(final View view, final boolean nestedScrollingEnabled) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild)view).setNestedScrollingEnabled(nestedScrollingEnabled);
            }
        }
        
        public void setNextClusterForwardId(@NonNull final View view, final int n) {
        }
        
        public void setOnApplyWindowInsetsListener(final View view, final OnApplyWindowInsetsListener onApplyWindowInsetsListener) {
        }
        
        public void setPaddingRelative(final View view, final int n, final int n2, final int n3, final int n4) {
            view.setPadding(n, n2, n3, n4);
        }
        
        public void setPointerIcon(final View view, final PointerIconCompat pointerIconCompat) {
        }
        
        public void setScrollIndicators(final View view, final int n) {
        }
        
        public void setScrollIndicators(final View view, final int n, final int n2) {
        }
        
        public void setTooltipText(final View view, final CharSequence charSequence) {
        }
        
        public void setTransitionName(final View view, final String s) {
            if (ViewCompatBaseImpl.sTransitionNameMap == null) {
                ViewCompatBaseImpl.sTransitionNameMap = new WeakHashMap<View, String>();
            }
            ViewCompatBaseImpl.sTransitionNameMap.put(view, s);
        }
        
        public void setTranslationZ(final View view, final float n) {
        }
        
        public void setZ(final View view, final float n) {
        }
        
        public boolean startDragAndDrop(final View view, final ClipData clipData, final View$DragShadowBuilder view$DragShadowBuilder, final Object o, final int n) {
            return view.startDrag(clipData, view$DragShadowBuilder, o, n);
        }
        
        public boolean startNestedScroll(final View view, final int n) {
            return view instanceof NestedScrollingChild && ((NestedScrollingChild)view).startNestedScroll(n);
        }
        
        public void stopNestedScroll(final View view) {
            if (view instanceof NestedScrollingChild) {
                ((NestedScrollingChild)view).stopNestedScroll();
            }
        }
        
        public void updateDragShadow(final View view, final View$DragShadowBuilder view$DragShadowBuilder) {
        }
    }
}
