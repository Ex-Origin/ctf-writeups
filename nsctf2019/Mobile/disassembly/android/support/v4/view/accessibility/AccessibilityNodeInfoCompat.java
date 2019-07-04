// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo$AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo$RangeInfo;
import android.view.accessibility.AccessibilityNodeInfo$CollectionItemInfo;
import android.view.accessibility.AccessibilityNodeInfo$CollectionInfo;
import android.support.annotation.RequiresApi;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.graphics.Rect;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import android.support.annotation.NonNull;
import android.view.View;
import android.os.Build$VERSION;
import android.support.annotation.RestrictTo;
import android.view.accessibility.AccessibilityNodeInfo;

public class AccessibilityNodeInfoCompat
{
    public static final int ACTION_ACCESSIBILITY_FOCUS = 64;
    public static final String ACTION_ARGUMENT_COLUMN_INT = "android.view.accessibility.action.ARGUMENT_COLUMN_INT";
    public static final String ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN = "ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN";
    public static final String ACTION_ARGUMENT_HTML_ELEMENT_STRING = "ACTION_ARGUMENT_HTML_ELEMENT_STRING";
    public static final String ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT = "ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT";
    public static final String ACTION_ARGUMENT_PROGRESS_VALUE = "android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE";
    public static final String ACTION_ARGUMENT_ROW_INT = "android.view.accessibility.action.ARGUMENT_ROW_INT";
    public static final String ACTION_ARGUMENT_SELECTION_END_INT = "ACTION_ARGUMENT_SELECTION_END_INT";
    public static final String ACTION_ARGUMENT_SELECTION_START_INT = "ACTION_ARGUMENT_SELECTION_START_INT";
    public static final String ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE = "ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE";
    public static final int ACTION_CLEAR_ACCESSIBILITY_FOCUS = 128;
    public static final int ACTION_CLEAR_FOCUS = 2;
    public static final int ACTION_CLEAR_SELECTION = 8;
    public static final int ACTION_CLICK = 16;
    public static final int ACTION_COLLAPSE = 524288;
    public static final int ACTION_COPY = 16384;
    public static final int ACTION_CUT = 65536;
    public static final int ACTION_DISMISS = 1048576;
    public static final int ACTION_EXPAND = 262144;
    public static final int ACTION_FOCUS = 1;
    public static final int ACTION_LONG_CLICK = 32;
    public static final int ACTION_NEXT_AT_MOVEMENT_GRANULARITY = 256;
    public static final int ACTION_NEXT_HTML_ELEMENT = 1024;
    public static final int ACTION_PASTE = 32768;
    public static final int ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = 512;
    public static final int ACTION_PREVIOUS_HTML_ELEMENT = 2048;
    public static final int ACTION_SCROLL_BACKWARD = 8192;
    public static final int ACTION_SCROLL_FORWARD = 4096;
    public static final int ACTION_SELECT = 4;
    public static final int ACTION_SET_SELECTION = 131072;
    public static final int ACTION_SET_TEXT = 2097152;
    public static final int FOCUS_ACCESSIBILITY = 2;
    public static final int FOCUS_INPUT = 1;
    static final AccessibilityNodeInfoBaseImpl IMPL;
    public static final int MOVEMENT_GRANULARITY_CHARACTER = 1;
    public static final int MOVEMENT_GRANULARITY_LINE = 4;
    public static final int MOVEMENT_GRANULARITY_PAGE = 16;
    public static final int MOVEMENT_GRANULARITY_PARAGRAPH = 8;
    public static final int MOVEMENT_GRANULARITY_WORD = 2;
    private final AccessibilityNodeInfo mInfo;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int mParentVirtualDescendantId;
    
    static {
        if (Build$VERSION.SDK_INT >= 24) {
            IMPL = (AccessibilityNodeInfoBaseImpl)new AccessibilityNodeInfoApi24Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            IMPL = (AccessibilityNodeInfoBaseImpl)new AccessibilityNodeInfoApi23Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 22) {
            IMPL = (AccessibilityNodeInfoBaseImpl)new AccessibilityNodeInfoApi22Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (AccessibilityNodeInfoBaseImpl)new AccessibilityNodeInfoApi21Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (AccessibilityNodeInfoBaseImpl)new AccessibilityNodeInfoApi19Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            IMPL = (AccessibilityNodeInfoBaseImpl)new AccessibilityNodeInfoApi18Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 17) {
            IMPL = (AccessibilityNodeInfoBaseImpl)new AccessibilityNodeInfoApi17Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (AccessibilityNodeInfoBaseImpl)new AccessibilityNodeInfoApi16Impl();
            return;
        }
        IMPL = new AccessibilityNodeInfoBaseImpl();
    }
    
    private AccessibilityNodeInfoCompat(final AccessibilityNodeInfo mInfo) {
        this.mParentVirtualDescendantId = -1;
        this.mInfo = mInfo;
    }
    
    public AccessibilityNodeInfoCompat(final Object o) {
        this.mParentVirtualDescendantId = -1;
        this.mInfo = (AccessibilityNodeInfo)o;
    }
    
    private static String getActionSymbolicName(final int n) {
        switch (n) {
            default: {
                return "ACTION_UNKNOWN";
            }
            case 1: {
                return "ACTION_FOCUS";
            }
            case 2: {
                return "ACTION_CLEAR_FOCUS";
            }
            case 4: {
                return "ACTION_SELECT";
            }
            case 8: {
                return "ACTION_CLEAR_SELECTION";
            }
            case 16: {
                return "ACTION_CLICK";
            }
            case 32: {
                return "ACTION_LONG_CLICK";
            }
            case 64: {
                return "ACTION_ACCESSIBILITY_FOCUS";
            }
            case 128: {
                return "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
            }
            case 256: {
                return "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
            }
            case 512: {
                return "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
            }
            case 1024: {
                return "ACTION_NEXT_HTML_ELEMENT";
            }
            case 2048: {
                return "ACTION_PREVIOUS_HTML_ELEMENT";
            }
            case 4096: {
                return "ACTION_SCROLL_FORWARD";
            }
            case 8192: {
                return "ACTION_SCROLL_BACKWARD";
            }
            case 65536: {
                return "ACTION_CUT";
            }
            case 16384: {
                return "ACTION_COPY";
            }
            case 32768: {
                return "ACTION_PASTE";
            }
            case 131072: {
                return "ACTION_SET_SELECTION";
            }
        }
    }
    
    public static AccessibilityNodeInfoCompat obtain() {
        return wrap(AccessibilityNodeInfo.obtain());
    }
    
    public static AccessibilityNodeInfoCompat obtain(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        return wrap(AccessibilityNodeInfo.obtain(accessibilityNodeInfoCompat.mInfo));
    }
    
    public static AccessibilityNodeInfoCompat obtain(final View view) {
        return wrap(AccessibilityNodeInfo.obtain(view));
    }
    
    public static AccessibilityNodeInfoCompat obtain(final View view, final int n) {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.obtain(view, n));
    }
    
    public static AccessibilityNodeInfoCompat wrap(@NonNull final AccessibilityNodeInfo accessibilityNodeInfo) {
        return new AccessibilityNodeInfoCompat(accessibilityNodeInfo);
    }
    
    static AccessibilityNodeInfoCompat wrapNonNullInstance(final Object o) {
        if (o != null) {
            return new AccessibilityNodeInfoCompat(o);
        }
        return null;
    }
    
    public void addAction(final int n) {
        this.mInfo.addAction(n);
    }
    
    public void addAction(final AccessibilityActionCompat accessibilityActionCompat) {
        AccessibilityNodeInfoCompat.IMPL.addAction(this.mInfo, accessibilityActionCompat.mAction);
    }
    
    public void addChild(final View view) {
        this.mInfo.addChild(view);
    }
    
    public void addChild(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.addChild(this.mInfo, view, n);
    }
    
    public boolean canOpenPopup() {
        return AccessibilityNodeInfoCompat.IMPL.canOpenPopup(this.mInfo);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat)o;
            if (this.mInfo == null) {
                if (accessibilityNodeInfoCompat.mInfo != null) {
                    return false;
                }
            }
            else if (!this.mInfo.equals((Object)accessibilityNodeInfoCompat.mInfo)) {
                return false;
            }
        }
        return true;
    }
    
    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(final String s) {
        final ArrayList<AccessibilityNodeInfoCompat> list = new ArrayList<AccessibilityNodeInfoCompat>();
        final List accessibilityNodeInfosByText = this.mInfo.findAccessibilityNodeInfosByText(s);
        for (int size = accessibilityNodeInfosByText.size(), i = 0; i < size; ++i) {
            list.add(wrap(accessibilityNodeInfosByText.get(i)));
        }
        return list;
    }
    
    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByViewId(final String s) {
        final List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = AccessibilityNodeInfoCompat.IMPL.findAccessibilityNodeInfosByViewId(this.mInfo, s);
        List<AccessibilityNodeInfoCompat> emptyList;
        if (accessibilityNodeInfosByViewId != null) {
            final ArrayList<AccessibilityNodeInfoCompat> list = new ArrayList<AccessibilityNodeInfoCompat>();
            final Iterator<AccessibilityNodeInfo> iterator = accessibilityNodeInfosByViewId.iterator();
            while (true) {
                emptyList = list;
                if (!iterator.hasNext()) {
                    break;
                }
                list.add(wrap(iterator.next()));
            }
        }
        else {
            emptyList = Collections.emptyList();
        }
        return emptyList;
    }
    
    public AccessibilityNodeInfoCompat findFocus(final int n) {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.findFocus(this.mInfo, n));
    }
    
    public AccessibilityNodeInfoCompat focusSearch(final int n) {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.focusSearch(this.mInfo, n));
    }
    
    public List<AccessibilityActionCompat> getActionList() {
        final List<Object> actionList = AccessibilityNodeInfoCompat.IMPL.getActionList(this.mInfo);
        List<AccessibilityActionCompat> emptyList;
        if (actionList != null) {
            final ArrayList<AccessibilityActionCompat> list = new ArrayList<AccessibilityActionCompat>();
            final int size = actionList.size();
            int n = 0;
            while (true) {
                emptyList = list;
                if (n >= size) {
                    break;
                }
                list.add(new AccessibilityActionCompat(actionList.get(n)));
                ++n;
            }
        }
        else {
            emptyList = Collections.emptyList();
        }
        return emptyList;
    }
    
    public int getActions() {
        return this.mInfo.getActions();
    }
    
    public void getBoundsInParent(final Rect rect) {
        this.mInfo.getBoundsInParent(rect);
    }
    
    public void getBoundsInScreen(final Rect rect) {
        this.mInfo.getBoundsInScreen(rect);
    }
    
    public AccessibilityNodeInfoCompat getChild(final int n) {
        return wrapNonNullInstance(this.mInfo.getChild(n));
    }
    
    public int getChildCount() {
        return this.mInfo.getChildCount();
    }
    
    public CharSequence getClassName() {
        return this.mInfo.getClassName();
    }
    
    public CollectionInfoCompat getCollectionInfo() {
        final Object collectionInfo = AccessibilityNodeInfoCompat.IMPL.getCollectionInfo(this.mInfo);
        if (collectionInfo == null) {
            return null;
        }
        return new CollectionInfoCompat(collectionInfo);
    }
    
    public CollectionItemInfoCompat getCollectionItemInfo() {
        final Object collectionItemInfo = AccessibilityNodeInfoCompat.IMPL.getCollectionItemInfo(this.mInfo);
        if (collectionItemInfo == null) {
            return null;
        }
        return new CollectionItemInfoCompat(collectionItemInfo);
    }
    
    public CharSequence getContentDescription() {
        return this.mInfo.getContentDescription();
    }
    
    public int getDrawingOrder() {
        return AccessibilityNodeInfoCompat.IMPL.getDrawingOrder(this.mInfo);
    }
    
    public CharSequence getError() {
        return AccessibilityNodeInfoCompat.IMPL.getError(this.mInfo);
    }
    
    public Bundle getExtras() {
        return AccessibilityNodeInfoCompat.IMPL.getExtras(this.mInfo);
    }
    
    @Deprecated
    public Object getInfo() {
        return this.mInfo;
    }
    
    public int getInputType() {
        return AccessibilityNodeInfoCompat.IMPL.getInputType(this.mInfo);
    }
    
    public AccessibilityNodeInfoCompat getLabelFor() {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.getLabelFor(this.mInfo));
    }
    
    public AccessibilityNodeInfoCompat getLabeledBy() {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.getLabeledBy(this.mInfo));
    }
    
    public int getLiveRegion() {
        return AccessibilityNodeInfoCompat.IMPL.getLiveRegion(this.mInfo);
    }
    
    public int getMaxTextLength() {
        return AccessibilityNodeInfoCompat.IMPL.getMaxTextLength(this.mInfo);
    }
    
    public int getMovementGranularities() {
        return AccessibilityNodeInfoCompat.IMPL.getMovementGranularities(this.mInfo);
    }
    
    public CharSequence getPackageName() {
        return this.mInfo.getPackageName();
    }
    
    public AccessibilityNodeInfoCompat getParent() {
        return wrapNonNullInstance(this.mInfo.getParent());
    }
    
    public RangeInfoCompat getRangeInfo() {
        final Object rangeInfo = AccessibilityNodeInfoCompat.IMPL.getRangeInfo(this.mInfo);
        if (rangeInfo == null) {
            return null;
        }
        return new RangeInfoCompat(rangeInfo);
    }
    
    @Nullable
    public CharSequence getRoleDescription() {
        return AccessibilityNodeInfoCompat.IMPL.getRoleDescription(this.mInfo);
    }
    
    public CharSequence getText() {
        return this.mInfo.getText();
    }
    
    public int getTextSelectionEnd() {
        return AccessibilityNodeInfoCompat.IMPL.getTextSelectionEnd(this.mInfo);
    }
    
    public int getTextSelectionStart() {
        return AccessibilityNodeInfoCompat.IMPL.getTextSelectionStart(this.mInfo);
    }
    
    public AccessibilityNodeInfoCompat getTraversalAfter() {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.getTraversalAfter(this.mInfo));
    }
    
    public AccessibilityNodeInfoCompat getTraversalBefore() {
        return wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.getTraversalBefore(this.mInfo));
    }
    
    public String getViewIdResourceName() {
        return AccessibilityNodeInfoCompat.IMPL.getViewIdResourceName(this.mInfo);
    }
    
    public AccessibilityWindowInfoCompat getWindow() {
        return AccessibilityWindowInfoCompat.wrapNonNullInstance(AccessibilityNodeInfoCompat.IMPL.getWindow(this.mInfo));
    }
    
    public int getWindowId() {
        return this.mInfo.getWindowId();
    }
    
    @Override
    public int hashCode() {
        if (this.mInfo == null) {
            return 0;
        }
        return this.mInfo.hashCode();
    }
    
    public boolean isAccessibilityFocused() {
        return AccessibilityNodeInfoCompat.IMPL.isAccessibilityFocused(this.mInfo);
    }
    
    public boolean isCheckable() {
        return this.mInfo.isCheckable();
    }
    
    public boolean isChecked() {
        return this.mInfo.isChecked();
    }
    
    public boolean isClickable() {
        return this.mInfo.isClickable();
    }
    
    public boolean isContentInvalid() {
        return AccessibilityNodeInfoCompat.IMPL.isContentInvalid(this.mInfo);
    }
    
    public boolean isContextClickable() {
        return AccessibilityNodeInfoCompat.IMPL.isContextClickable(this.mInfo);
    }
    
    public boolean isDismissable() {
        return AccessibilityNodeInfoCompat.IMPL.isDismissable(this.mInfo);
    }
    
    public boolean isEditable() {
        return AccessibilityNodeInfoCompat.IMPL.isEditable(this.mInfo);
    }
    
    public boolean isEnabled() {
        return this.mInfo.isEnabled();
    }
    
    public boolean isFocusable() {
        return this.mInfo.isFocusable();
    }
    
    public boolean isFocused() {
        return this.mInfo.isFocused();
    }
    
    public boolean isImportantForAccessibility() {
        return AccessibilityNodeInfoCompat.IMPL.isImportantForAccessibility(this.mInfo);
    }
    
    public boolean isLongClickable() {
        return this.mInfo.isLongClickable();
    }
    
    public boolean isMultiLine() {
        return AccessibilityNodeInfoCompat.IMPL.isMultiLine(this.mInfo);
    }
    
    public boolean isPassword() {
        return this.mInfo.isPassword();
    }
    
    public boolean isScrollable() {
        return this.mInfo.isScrollable();
    }
    
    public boolean isSelected() {
        return this.mInfo.isSelected();
    }
    
    public boolean isVisibleToUser() {
        return AccessibilityNodeInfoCompat.IMPL.isVisibleToUser(this.mInfo);
    }
    
    public boolean performAction(final int n) {
        return this.mInfo.performAction(n);
    }
    
    public boolean performAction(final int n, final Bundle bundle) {
        return AccessibilityNodeInfoCompat.IMPL.performAction(this.mInfo, n, bundle);
    }
    
    public void recycle() {
        this.mInfo.recycle();
    }
    
    public boolean refresh() {
        return AccessibilityNodeInfoCompat.IMPL.refresh(this.mInfo);
    }
    
    public boolean removeAction(final AccessibilityActionCompat accessibilityActionCompat) {
        return AccessibilityNodeInfoCompat.IMPL.removeAction(this.mInfo, accessibilityActionCompat.mAction);
    }
    
    public boolean removeChild(final View view) {
        return AccessibilityNodeInfoCompat.IMPL.removeChild(this.mInfo, view);
    }
    
    public boolean removeChild(final View view, final int n) {
        return AccessibilityNodeInfoCompat.IMPL.removeChild(this.mInfo, view, n);
    }
    
    public void setAccessibilityFocused(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setAccessibilityFocused(this.mInfo, b);
    }
    
    public void setBoundsInParent(final Rect boundsInParent) {
        this.mInfo.setBoundsInParent(boundsInParent);
    }
    
    public void setBoundsInScreen(final Rect boundsInScreen) {
        this.mInfo.setBoundsInScreen(boundsInScreen);
    }
    
    public void setCanOpenPopup(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setCanOpenPopup(this.mInfo, b);
    }
    
    public void setCheckable(final boolean checkable) {
        this.mInfo.setCheckable(checkable);
    }
    
    public void setChecked(final boolean checked) {
        this.mInfo.setChecked(checked);
    }
    
    public void setClassName(final CharSequence className) {
        this.mInfo.setClassName(className);
    }
    
    public void setClickable(final boolean clickable) {
        this.mInfo.setClickable(clickable);
    }
    
    public void setCollectionInfo(final Object o) {
        AccessibilityNodeInfoCompat.IMPL.setCollectionInfo(this.mInfo, ((CollectionInfoCompat)o).mInfo);
    }
    
    public void setCollectionItemInfo(final Object o) {
        AccessibilityNodeInfoCompat.IMPL.setCollectionItemInfo(this.mInfo, ((CollectionItemInfoCompat)o).mInfo);
    }
    
    public void setContentDescription(final CharSequence contentDescription) {
        this.mInfo.setContentDescription(contentDescription);
    }
    
    public void setContentInvalid(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setContentInvalid(this.mInfo, b);
    }
    
    public void setContextClickable(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setContextClickable(this.mInfo, b);
    }
    
    public void setDismissable(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setDismissable(this.mInfo, b);
    }
    
    public void setDrawingOrder(final int n) {
        AccessibilityNodeInfoCompat.IMPL.setDrawingOrder(this.mInfo, n);
    }
    
    public void setEditable(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setEditable(this.mInfo, b);
    }
    
    public void setEnabled(final boolean enabled) {
        this.mInfo.setEnabled(enabled);
    }
    
    public void setError(final CharSequence charSequence) {
        AccessibilityNodeInfoCompat.IMPL.setError(this.mInfo, charSequence);
    }
    
    public void setFocusable(final boolean focusable) {
        this.mInfo.setFocusable(focusable);
    }
    
    public void setFocused(final boolean focused) {
        this.mInfo.setFocused(focused);
    }
    
    public void setImportantForAccessibility(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setImportantForAccessibility(this.mInfo, b);
    }
    
    public void setInputType(final int n) {
        AccessibilityNodeInfoCompat.IMPL.setInputType(this.mInfo, n);
    }
    
    public void setLabelFor(final View view) {
        AccessibilityNodeInfoCompat.IMPL.setLabelFor(this.mInfo, view);
    }
    
    public void setLabelFor(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.setLabelFor(this.mInfo, view, n);
    }
    
    public void setLabeledBy(final View view) {
        AccessibilityNodeInfoCompat.IMPL.setLabeledBy(this.mInfo, view);
    }
    
    public void setLabeledBy(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.setLabeledBy(this.mInfo, view, n);
    }
    
    public void setLiveRegion(final int n) {
        AccessibilityNodeInfoCompat.IMPL.setLiveRegion(this.mInfo, n);
    }
    
    public void setLongClickable(final boolean longClickable) {
        this.mInfo.setLongClickable(longClickable);
    }
    
    public void setMaxTextLength(final int n) {
        AccessibilityNodeInfoCompat.IMPL.setMaxTextLength(this.mInfo, n);
    }
    
    public void setMovementGranularities(final int n) {
        AccessibilityNodeInfoCompat.IMPL.setMovementGranularities(this.mInfo, n);
    }
    
    public void setMultiLine(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setMultiLine(this.mInfo, b);
    }
    
    public void setPackageName(final CharSequence packageName) {
        this.mInfo.setPackageName(packageName);
    }
    
    public void setParent(final View parent) {
        this.mInfo.setParent(parent);
    }
    
    public void setParent(final View view, final int mParentVirtualDescendantId) {
        this.mParentVirtualDescendantId = mParentVirtualDescendantId;
        AccessibilityNodeInfoCompat.IMPL.setParent(this.mInfo, view, mParentVirtualDescendantId);
    }
    
    public void setPassword(final boolean password) {
        this.mInfo.setPassword(password);
    }
    
    public void setRangeInfo(final RangeInfoCompat rangeInfoCompat) {
        AccessibilityNodeInfoCompat.IMPL.setRangeInfo(this.mInfo, rangeInfoCompat.mInfo);
    }
    
    public void setRoleDescription(@Nullable final CharSequence charSequence) {
        AccessibilityNodeInfoCompat.IMPL.setRoleDescription(this.mInfo, charSequence);
    }
    
    public void setScrollable(final boolean scrollable) {
        this.mInfo.setScrollable(scrollable);
    }
    
    public void setSelected(final boolean selected) {
        this.mInfo.setSelected(selected);
    }
    
    public void setSource(final View source) {
        this.mInfo.setSource(source);
    }
    
    public void setSource(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.setSource(this.mInfo, view, n);
    }
    
    public void setText(final CharSequence text) {
        this.mInfo.setText(text);
    }
    
    public void setTextSelection(final int n, final int n2) {
        AccessibilityNodeInfoCompat.IMPL.setTextSelection(this.mInfo, n, n2);
    }
    
    public void setTraversalAfter(final View view) {
        AccessibilityNodeInfoCompat.IMPL.setTraversalAfter(this.mInfo, view);
    }
    
    public void setTraversalAfter(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.setTraversalAfter(this.mInfo, view, n);
    }
    
    public void setTraversalBefore(final View view) {
        AccessibilityNodeInfoCompat.IMPL.setTraversalBefore(this.mInfo, view);
    }
    
    public void setTraversalBefore(final View view, final int n) {
        AccessibilityNodeInfoCompat.IMPL.setTraversalBefore(this.mInfo, view, n);
    }
    
    public void setViewIdResourceName(final String s) {
        AccessibilityNodeInfoCompat.IMPL.setViewIdResourceName(this.mInfo, s);
    }
    
    public void setVisibleToUser(final boolean b) {
        AccessibilityNodeInfoCompat.IMPL.setVisibleToUser(this.mInfo, b);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        final Rect rect = new Rect();
        this.getBoundsInParent(rect);
        sb.append("; boundsInParent: " + rect);
        this.getBoundsInScreen(rect);
        sb.append("; boundsInScreen: " + rect);
        sb.append("; packageName: ").append(this.getPackageName());
        sb.append("; className: ").append(this.getClassName());
        sb.append("; text: ").append(this.getText());
        sb.append("; contentDescription: ").append(this.getContentDescription());
        sb.append("; viewId: ").append(this.getViewIdResourceName());
        sb.append("; checkable: ").append(this.isCheckable());
        sb.append("; checked: ").append(this.isChecked());
        sb.append("; focusable: ").append(this.isFocusable());
        sb.append("; focused: ").append(this.isFocused());
        sb.append("; selected: ").append(this.isSelected());
        sb.append("; clickable: ").append(this.isClickable());
        sb.append("; longClickable: ").append(this.isLongClickable());
        sb.append("; enabled: ").append(this.isEnabled());
        sb.append("; password: ").append(this.isPassword());
        sb.append("; scrollable: " + this.isScrollable());
        sb.append("; [");
        int n2;
        for (int i = this.getActions(); i != 0; i = n2) {
            final int n = 1 << Integer.numberOfTrailingZeros(i);
            n2 = (i & ~n);
            sb.append(getActionSymbolicName(n));
            if ((i = n2) != 0) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public AccessibilityNodeInfo unwrap() {
        return this.mInfo;
    }
    
    public static class AccessibilityActionCompat
    {
        public static final AccessibilityActionCompat ACTION_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_ACCESSIBILITY_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_FOCUS;
        public static final AccessibilityActionCompat ACTION_CLEAR_SELECTION;
        public static final AccessibilityActionCompat ACTION_CLICK;
        public static final AccessibilityActionCompat ACTION_COLLAPSE;
        public static final AccessibilityActionCompat ACTION_CONTEXT_CLICK;
        public static final AccessibilityActionCompat ACTION_COPY;
        public static final AccessibilityActionCompat ACTION_CUT;
        public static final AccessibilityActionCompat ACTION_DISMISS;
        public static final AccessibilityActionCompat ACTION_EXPAND;
        public static final AccessibilityActionCompat ACTION_FOCUS;
        public static final AccessibilityActionCompat ACTION_LONG_CLICK;
        public static final AccessibilityActionCompat ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_NEXT_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_PASTE;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
        public static final AccessibilityActionCompat ACTION_PREVIOUS_HTML_ELEMENT;
        public static final AccessibilityActionCompat ACTION_SCROLL_BACKWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_DOWN;
        public static final AccessibilityActionCompat ACTION_SCROLL_FORWARD;
        public static final AccessibilityActionCompat ACTION_SCROLL_LEFT;
        public static final AccessibilityActionCompat ACTION_SCROLL_RIGHT;
        public static final AccessibilityActionCompat ACTION_SCROLL_TO_POSITION;
        public static final AccessibilityActionCompat ACTION_SCROLL_UP;
        public static final AccessibilityActionCompat ACTION_SELECT;
        public static final AccessibilityActionCompat ACTION_SET_PROGRESS;
        public static final AccessibilityActionCompat ACTION_SET_SELECTION;
        public static final AccessibilityActionCompat ACTION_SET_TEXT;
        public static final AccessibilityActionCompat ACTION_SHOW_ON_SCREEN;
        final Object mAction;
        
        static {
            ACTION_FOCUS = new AccessibilityActionCompat(1, null);
            ACTION_CLEAR_FOCUS = new AccessibilityActionCompat(2, null);
            ACTION_SELECT = new AccessibilityActionCompat(4, null);
            ACTION_CLEAR_SELECTION = new AccessibilityActionCompat(8, null);
            ACTION_CLICK = new AccessibilityActionCompat(16, null);
            ACTION_LONG_CLICK = new AccessibilityActionCompat(32, null);
            ACTION_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(64, null);
            ACTION_CLEAR_ACCESSIBILITY_FOCUS = new AccessibilityActionCompat(128, null);
            ACTION_NEXT_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(256, null);
            ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY = new AccessibilityActionCompat(512, null);
            ACTION_NEXT_HTML_ELEMENT = new AccessibilityActionCompat(1024, null);
            ACTION_PREVIOUS_HTML_ELEMENT = new AccessibilityActionCompat(2048, null);
            ACTION_SCROLL_FORWARD = new AccessibilityActionCompat(4096, null);
            ACTION_SCROLL_BACKWARD = new AccessibilityActionCompat(8192, null);
            ACTION_COPY = new AccessibilityActionCompat(16384, null);
            ACTION_PASTE = new AccessibilityActionCompat(32768, null);
            ACTION_CUT = new AccessibilityActionCompat(65536, null);
            ACTION_SET_SELECTION = new AccessibilityActionCompat(131072, null);
            ACTION_EXPAND = new AccessibilityActionCompat(262144, null);
            ACTION_COLLAPSE = new AccessibilityActionCompat(524288, null);
            ACTION_DISMISS = new AccessibilityActionCompat(1048576, null);
            ACTION_SET_TEXT = new AccessibilityActionCompat(2097152, null);
            ACTION_SHOW_ON_SCREEN = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionShowOnScreen());
            ACTION_SCROLL_TO_POSITION = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollToPosition());
            ACTION_SCROLL_UP = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollUp());
            ACTION_SCROLL_LEFT = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollLeft());
            ACTION_SCROLL_DOWN = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollDown());
            ACTION_SCROLL_RIGHT = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionScrollRight());
            ACTION_CONTEXT_CLICK = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionContextClick());
            ACTION_SET_PROGRESS = new AccessibilityActionCompat(AccessibilityNodeInfoCompat.IMPL.getActionSetProgress());
        }
        
        public AccessibilityActionCompat(final int n, final CharSequence charSequence) {
            this(AccessibilityNodeInfoCompat.IMPL.newAccessibilityAction(n, charSequence));
        }
        
        AccessibilityActionCompat(final Object mAction) {
            this.mAction = mAction;
        }
        
        public int getId() {
            return AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionId(this.mAction);
        }
        
        public CharSequence getLabel() {
            return AccessibilityNodeInfoCompat.IMPL.getAccessibilityActionLabel(this.mAction);
        }
    }
    
    @RequiresApi(16)
    static class AccessibilityNodeInfoApi16Impl extends AccessibilityNodeInfoBaseImpl
    {
        @Override
        public void addChild(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
            accessibilityNodeInfo.addChild(view, n);
        }
        
        @Override
        public Object findFocus(final AccessibilityNodeInfo accessibilityNodeInfo, final int n) {
            return accessibilityNodeInfo.findFocus(n);
        }
        
        @Override
        public Object focusSearch(final AccessibilityNodeInfo accessibilityNodeInfo, final int n) {
            return accessibilityNodeInfo.focusSearch(n);
        }
        
        @Override
        public int getMovementGranularities(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getMovementGranularities();
        }
        
        @Override
        public boolean isAccessibilityFocused(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isAccessibilityFocused();
        }
        
        @Override
        public boolean isVisibleToUser(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isVisibleToUser();
        }
        
        @Override
        public AccessibilityNodeInfo obtain(final View view, final int n) {
            return AccessibilityNodeInfo.obtain(view, n);
        }
        
        @Override
        public boolean performAction(final AccessibilityNodeInfo accessibilityNodeInfo, final int n, final Bundle bundle) {
            return accessibilityNodeInfo.performAction(n, bundle);
        }
        
        @Override
        public void setAccessibilityFocused(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean accessibilityFocused) {
            accessibilityNodeInfo.setAccessibilityFocused(accessibilityFocused);
        }
        
        @Override
        public void setMovementGranularities(final AccessibilityNodeInfo accessibilityNodeInfo, final int movementGranularities) {
            accessibilityNodeInfo.setMovementGranularities(movementGranularities);
        }
        
        @Override
        public void setParent(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
            accessibilityNodeInfo.setParent(view, n);
        }
        
        @Override
        public void setSource(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
            accessibilityNodeInfo.setSource(view, n);
        }
        
        @Override
        public void setVisibleToUser(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean visibleToUser) {
            accessibilityNodeInfo.setVisibleToUser(visibleToUser);
        }
    }
    
    @RequiresApi(17)
    static class AccessibilityNodeInfoApi17Impl extends AccessibilityNodeInfoApi16Impl
    {
        @Override
        public Object getLabelFor(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getLabelFor();
        }
        
        @Override
        public Object getLabeledBy(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getLabeledBy();
        }
        
        @Override
        public void setLabelFor(final AccessibilityNodeInfo accessibilityNodeInfo, final View labelFor) {
            accessibilityNodeInfo.setLabelFor(labelFor);
        }
        
        @Override
        public void setLabelFor(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
            accessibilityNodeInfo.setLabelFor(view, n);
        }
        
        @Override
        public void setLabeledBy(final AccessibilityNodeInfo accessibilityNodeInfo, final View labeledBy) {
            accessibilityNodeInfo.setLabeledBy(labeledBy);
        }
        
        @Override
        public void setLabeledBy(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
            accessibilityNodeInfo.setLabeledBy(view, n);
        }
    }
    
    @RequiresApi(18)
    static class AccessibilityNodeInfoApi18Impl extends AccessibilityNodeInfoApi17Impl
    {
        @Override
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(final AccessibilityNodeInfo accessibilityNodeInfo, final String s) {
            return (List<AccessibilityNodeInfo>)accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(s);
        }
        
        @Override
        public int getTextSelectionEnd(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getTextSelectionEnd();
        }
        
        @Override
        public int getTextSelectionStart(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getTextSelectionStart();
        }
        
        @Override
        public String getViewIdResourceName(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getViewIdResourceName();
        }
        
        @Override
        public boolean isEditable(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isEditable();
        }
        
        @Override
        public boolean refresh(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.refresh();
        }
        
        @Override
        public void setEditable(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean editable) {
            accessibilityNodeInfo.setEditable(editable);
        }
        
        @Override
        public void setTextSelection(final AccessibilityNodeInfo accessibilityNodeInfo, final int n, final int n2) {
            accessibilityNodeInfo.setTextSelection(n, n2);
        }
        
        @Override
        public void setViewIdResourceName(final AccessibilityNodeInfo accessibilityNodeInfo, final String viewIdResourceName) {
            accessibilityNodeInfo.setViewIdResourceName(viewIdResourceName);
        }
    }
    
    @RequiresApi(19)
    static class AccessibilityNodeInfoApi19Impl extends AccessibilityNodeInfoApi18Impl
    {
        private static final String ROLE_DESCRIPTION_KEY = "AccessibilityNodeInfo.roleDescription";
        
        @Override
        public boolean canOpenPopup(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.canOpenPopup();
        }
        
        @Override
        public Object getCollectionInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getCollectionInfo();
        }
        
        @Override
        public int getCollectionInfoColumnCount(final Object o) {
            return ((AccessibilityNodeInfo$CollectionInfo)o).getColumnCount();
        }
        
        @Override
        public int getCollectionInfoRowCount(final Object o) {
            return ((AccessibilityNodeInfo$CollectionInfo)o).getRowCount();
        }
        
        @Override
        public int getCollectionItemColumnIndex(final Object o) {
            return ((AccessibilityNodeInfo$CollectionItemInfo)o).getColumnIndex();
        }
        
        @Override
        public int getCollectionItemColumnSpan(final Object o) {
            return ((AccessibilityNodeInfo$CollectionItemInfo)o).getColumnSpan();
        }
        
        @Override
        public Object getCollectionItemInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getCollectionItemInfo();
        }
        
        @Override
        public int getCollectionItemRowIndex(final Object o) {
            return ((AccessibilityNodeInfo$CollectionItemInfo)o).getRowIndex();
        }
        
        @Override
        public int getCollectionItemRowSpan(final Object o) {
            return ((AccessibilityNodeInfo$CollectionItemInfo)o).getRowSpan();
        }
        
        @Override
        public Bundle getExtras(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getExtras();
        }
        
        @Override
        public int getInputType(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getInputType();
        }
        
        @Override
        public int getLiveRegion(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getLiveRegion();
        }
        
        @Override
        public Object getRangeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getRangeInfo();
        }
        
        @Override
        public CharSequence getRoleDescription(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return this.getExtras(accessibilityNodeInfo).getCharSequence("AccessibilityNodeInfo.roleDescription");
        }
        
        @Override
        public boolean isCollectionInfoHierarchical(final Object o) {
            return ((AccessibilityNodeInfo$CollectionInfo)o).isHierarchical();
        }
        
        @Override
        public boolean isCollectionItemHeading(final Object o) {
            return ((AccessibilityNodeInfo$CollectionItemInfo)o).isHeading();
        }
        
        @Override
        public boolean isContentInvalid(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isContentInvalid();
        }
        
        @Override
        public boolean isDismissable(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isDismissable();
        }
        
        @Override
        public boolean isMultiLine(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isMultiLine();
        }
        
        @Override
        public Object obtainCollectionInfo(final int n, final int n2, final boolean b) {
            return AccessibilityNodeInfo$CollectionInfo.obtain(n, n2, b);
        }
        
        @Override
        public Object obtainCollectionInfo(final int n, final int n2, final boolean b, final int n3) {
            return AccessibilityNodeInfo$CollectionInfo.obtain(n, n2, b);
        }
        
        @Override
        public Object obtainCollectionItemInfo(final int n, final int n2, final int n3, final int n4, final boolean b) {
            return AccessibilityNodeInfo$CollectionItemInfo.obtain(n, n2, n3, n4, b);
        }
        
        @Override
        public Object obtainCollectionItemInfo(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            return AccessibilityNodeInfo$CollectionItemInfo.obtain(n, n2, n3, n4, b);
        }
        
        @Override
        public Object obtainRangeInfo(final int n, final float n2, final float n3, final float n4) {
            return AccessibilityNodeInfo$RangeInfo.obtain(n, n2, n3, n4);
        }
        
        @Override
        public void setCanOpenPopup(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean canOpenPopup) {
            accessibilityNodeInfo.setCanOpenPopup(canOpenPopup);
        }
        
        @Override
        public void setCollectionInfo(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
            accessibilityNodeInfo.setCollectionInfo((AccessibilityNodeInfo$CollectionInfo)o);
        }
        
        @Override
        public void setCollectionItemInfo(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
            accessibilityNodeInfo.setCollectionItemInfo((AccessibilityNodeInfo$CollectionItemInfo)o);
        }
        
        @Override
        public void setContentInvalid(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean contentInvalid) {
            accessibilityNodeInfo.setContentInvalid(contentInvalid);
        }
        
        @Override
        public void setDismissable(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean dismissable) {
            accessibilityNodeInfo.setDismissable(dismissable);
        }
        
        @Override
        public void setInputType(final AccessibilityNodeInfo accessibilityNodeInfo, final int inputType) {
            accessibilityNodeInfo.setInputType(inputType);
        }
        
        @Override
        public void setLiveRegion(final AccessibilityNodeInfo accessibilityNodeInfo, final int liveRegion) {
            accessibilityNodeInfo.setLiveRegion(liveRegion);
        }
        
        @Override
        public void setMultiLine(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean multiLine) {
            accessibilityNodeInfo.setMultiLine(multiLine);
        }
        
        @Override
        public void setRangeInfo(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
            accessibilityNodeInfo.setRangeInfo((AccessibilityNodeInfo$RangeInfo)o);
        }
        
        @Override
        public void setRoleDescription(final AccessibilityNodeInfo accessibilityNodeInfo, final CharSequence charSequence) {
            this.getExtras(accessibilityNodeInfo).putCharSequence("AccessibilityNodeInfo.roleDescription", charSequence);
        }
    }
    
    @RequiresApi(21)
    static class AccessibilityNodeInfoApi21Impl extends AccessibilityNodeInfoApi19Impl
    {
        @Override
        public void addAction(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
            accessibilityNodeInfo.addAction((AccessibilityNodeInfo$AccessibilityAction)o);
        }
        
        @Override
        public int getAccessibilityActionId(final Object o) {
            return ((AccessibilityNodeInfo$AccessibilityAction)o).getId();
        }
        
        @Override
        public CharSequence getAccessibilityActionLabel(final Object o) {
            return ((AccessibilityNodeInfo$AccessibilityAction)o).getLabel();
        }
        
        @Override
        public List<Object> getActionList(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return (List<Object>)accessibilityNodeInfo.getActionList();
        }
        
        @Override
        public int getCollectionInfoSelectionMode(final Object o) {
            return ((AccessibilityNodeInfo$CollectionInfo)o).getSelectionMode();
        }
        
        @Override
        public CharSequence getError(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getError();
        }
        
        @Override
        public int getMaxTextLength(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getMaxTextLength();
        }
        
        @Override
        public Object getWindow(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getWindow();
        }
        
        @Override
        public boolean isCollectionItemSelected(final Object o) {
            return ((AccessibilityNodeInfo$CollectionItemInfo)o).isSelected();
        }
        
        @Override
        public Object newAccessibilityAction(final int n, final CharSequence charSequence) {
            return new AccessibilityNodeInfo$AccessibilityAction(n, charSequence);
        }
        
        @Override
        public Object obtainCollectionInfo(final int n, final int n2, final boolean b, final int n3) {
            return AccessibilityNodeInfo$CollectionInfo.obtain(n, n2, b, n3);
        }
        
        @Override
        public Object obtainCollectionItemInfo(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            return AccessibilityNodeInfo$CollectionItemInfo.obtain(n, n2, n3, n4, b, b2);
        }
        
        @Override
        public boolean removeAction(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
            return accessibilityNodeInfo.removeAction((AccessibilityNodeInfo$AccessibilityAction)o);
        }
        
        @Override
        public boolean removeChild(final AccessibilityNodeInfo accessibilityNodeInfo, final View view) {
            return accessibilityNodeInfo.removeChild(view);
        }
        
        @Override
        public boolean removeChild(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
            return accessibilityNodeInfo.removeChild(view, n);
        }
        
        @Override
        public void setError(final AccessibilityNodeInfo accessibilityNodeInfo, final CharSequence error) {
            accessibilityNodeInfo.setError(error);
        }
        
        @Override
        public void setMaxTextLength(final AccessibilityNodeInfo accessibilityNodeInfo, final int maxTextLength) {
            accessibilityNodeInfo.setMaxTextLength(maxTextLength);
        }
    }
    
    @RequiresApi(22)
    static class AccessibilityNodeInfoApi22Impl extends AccessibilityNodeInfoApi21Impl
    {
        @Override
        public Object getTraversalAfter(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getTraversalAfter();
        }
        
        @Override
        public Object getTraversalBefore(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getTraversalBefore();
        }
        
        @Override
        public void setTraversalAfter(final AccessibilityNodeInfo accessibilityNodeInfo, final View traversalAfter) {
            accessibilityNodeInfo.setTraversalAfter(traversalAfter);
        }
        
        @Override
        public void setTraversalAfter(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
            accessibilityNodeInfo.setTraversalAfter(view, n);
        }
        
        @Override
        public void setTraversalBefore(final AccessibilityNodeInfo accessibilityNodeInfo, final View traversalBefore) {
            accessibilityNodeInfo.setTraversalBefore(traversalBefore);
        }
        
        @Override
        public void setTraversalBefore(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
            accessibilityNodeInfo.setTraversalBefore(view, n);
        }
    }
    
    @RequiresApi(23)
    static class AccessibilityNodeInfoApi23Impl extends AccessibilityNodeInfoApi22Impl
    {
        @Override
        public Object getActionContextClick() {
            return AccessibilityNodeInfo$AccessibilityAction.ACTION_CONTEXT_CLICK;
        }
        
        @Override
        public Object getActionScrollDown() {
            return AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_DOWN;
        }
        
        @Override
        public Object getActionScrollLeft() {
            return AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_LEFT;
        }
        
        @Override
        public Object getActionScrollRight() {
            return AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_RIGHT;
        }
        
        @Override
        public Object getActionScrollToPosition() {
            return AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_TO_POSITION;
        }
        
        @Override
        public Object getActionScrollUp() {
            return AccessibilityNodeInfo$AccessibilityAction.ACTION_SCROLL_UP;
        }
        
        @Override
        public Object getActionShowOnScreen() {
            return AccessibilityNodeInfo$AccessibilityAction.ACTION_SHOW_ON_SCREEN;
        }
        
        @Override
        public boolean isContextClickable(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isContextClickable();
        }
        
        @Override
        public void setContextClickable(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean contextClickable) {
            accessibilityNodeInfo.setContextClickable(contextClickable);
        }
    }
    
    @RequiresApi(24)
    static class AccessibilityNodeInfoApi24Impl extends AccessibilityNodeInfoApi23Impl
    {
        @Override
        public Object getActionSetProgress() {
            return AccessibilityNodeInfo$AccessibilityAction.ACTION_SET_PROGRESS;
        }
        
        @Override
        public int getDrawingOrder(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.getDrawingOrder();
        }
        
        @Override
        public boolean isImportantForAccessibility(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return accessibilityNodeInfo.isImportantForAccessibility();
        }
        
        @Override
        public void setDrawingOrder(final AccessibilityNodeInfo accessibilityNodeInfo, final int drawingOrder) {
            accessibilityNodeInfo.setDrawingOrder(drawingOrder);
        }
        
        @Override
        public void setImportantForAccessibility(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean importantForAccessibility) {
            accessibilityNodeInfo.setImportantForAccessibility(importantForAccessibility);
        }
    }
    
    static class AccessibilityNodeInfoBaseImpl
    {
        public void addAction(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
        }
        
        public void addChild(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
        }
        
        public boolean canOpenPopup(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }
        
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByViewId(final AccessibilityNodeInfo accessibilityNodeInfo, final String s) {
            return Collections.emptyList();
        }
        
        public Object findFocus(final AccessibilityNodeInfo accessibilityNodeInfo, final int n) {
            return null;
        }
        
        public Object focusSearch(final AccessibilityNodeInfo accessibilityNodeInfo, final int n) {
            return null;
        }
        
        public int getAccessibilityActionId(final Object o) {
            return 0;
        }
        
        public CharSequence getAccessibilityActionLabel(final Object o) {
            return null;
        }
        
        public Object getActionContextClick() {
            return null;
        }
        
        public List<Object> getActionList(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public Object getActionScrollDown() {
            return null;
        }
        
        public Object getActionScrollLeft() {
            return null;
        }
        
        public Object getActionScrollRight() {
            return null;
        }
        
        public Object getActionScrollToPosition() {
            return null;
        }
        
        public Object getActionScrollUp() {
            return null;
        }
        
        public Object getActionSetProgress() {
            return null;
        }
        
        public Object getActionShowOnScreen() {
            return null;
        }
        
        public Object getCollectionInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public int getCollectionInfoColumnCount(final Object o) {
            return 0;
        }
        
        public int getCollectionInfoRowCount(final Object o) {
            return 0;
        }
        
        public int getCollectionInfoSelectionMode(final Object o) {
            return 0;
        }
        
        public int getCollectionItemColumnIndex(final Object o) {
            return 0;
        }
        
        public int getCollectionItemColumnSpan(final Object o) {
            return 0;
        }
        
        public Object getCollectionItemInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public int getCollectionItemRowIndex(final Object o) {
            return 0;
        }
        
        public int getCollectionItemRowSpan(final Object o) {
            return 0;
        }
        
        public int getDrawingOrder(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return 0;
        }
        
        public CharSequence getError(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public Bundle getExtras(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return new Bundle();
        }
        
        public int getInputType(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return 0;
        }
        
        public Object getLabelFor(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public Object getLabeledBy(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public int getLiveRegion(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return 0;
        }
        
        public int getMaxTextLength(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return -1;
        }
        
        public int getMovementGranularities(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return 0;
        }
        
        public Object getRangeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public CharSequence getRoleDescription(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public int getTextSelectionEnd(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return -1;
        }
        
        public int getTextSelectionStart(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return -1;
        }
        
        public Object getTraversalAfter(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public Object getTraversalBefore(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public String getViewIdResourceName(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public Object getWindow(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return null;
        }
        
        public boolean isAccessibilityFocused(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }
        
        public boolean isCollectionInfoHierarchical(final Object o) {
            return false;
        }
        
        public boolean isCollectionItemHeading(final Object o) {
            return false;
        }
        
        public boolean isCollectionItemSelected(final Object o) {
            return false;
        }
        
        public boolean isContentInvalid(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }
        
        public boolean isContextClickable(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }
        
        public boolean isDismissable(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }
        
        public boolean isEditable(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }
        
        public boolean isImportantForAccessibility(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return true;
        }
        
        public boolean isMultiLine(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }
        
        public boolean isVisibleToUser(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }
        
        public Object newAccessibilityAction(final int n, final CharSequence charSequence) {
            return null;
        }
        
        public AccessibilityNodeInfo obtain(final View view, final int n) {
            return null;
        }
        
        public Object obtainCollectionInfo(final int n, final int n2, final boolean b) {
            return null;
        }
        
        public Object obtainCollectionInfo(final int n, final int n2, final boolean b, final int n3) {
            return null;
        }
        
        public Object obtainCollectionItemInfo(final int n, final int n2, final int n3, final int n4, final boolean b) {
            return null;
        }
        
        public Object obtainCollectionItemInfo(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            return null;
        }
        
        public Object obtainRangeInfo(final int n, final float n2, final float n3, final float n4) {
            return null;
        }
        
        public boolean performAction(final AccessibilityNodeInfo accessibilityNodeInfo, final int n, final Bundle bundle) {
            return false;
        }
        
        public boolean refresh(final AccessibilityNodeInfo accessibilityNodeInfo) {
            return false;
        }
        
        public boolean removeAction(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
            return false;
        }
        
        public boolean removeChild(final AccessibilityNodeInfo accessibilityNodeInfo, final View view) {
            return false;
        }
        
        public boolean removeChild(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
            return false;
        }
        
        public void setAccessibilityFocused(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean b) {
        }
        
        public void setCanOpenPopup(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean b) {
        }
        
        public void setCollectionInfo(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
        }
        
        public void setCollectionItemInfo(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
        }
        
        public void setContentInvalid(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean b) {
        }
        
        public void setContextClickable(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean b) {
        }
        
        public void setDismissable(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean b) {
        }
        
        public void setDrawingOrder(final AccessibilityNodeInfo accessibilityNodeInfo, final int n) {
        }
        
        public void setEditable(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean b) {
        }
        
        public void setError(final AccessibilityNodeInfo accessibilityNodeInfo, final CharSequence charSequence) {
        }
        
        public void setImportantForAccessibility(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean b) {
        }
        
        public void setInputType(final AccessibilityNodeInfo accessibilityNodeInfo, final int n) {
        }
        
        public void setLabelFor(final AccessibilityNodeInfo accessibilityNodeInfo, final View view) {
        }
        
        public void setLabelFor(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
        }
        
        public void setLabeledBy(final AccessibilityNodeInfo accessibilityNodeInfo, final View view) {
        }
        
        public void setLabeledBy(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
        }
        
        public void setLiveRegion(final AccessibilityNodeInfo accessibilityNodeInfo, final int n) {
        }
        
        public void setMaxTextLength(final AccessibilityNodeInfo accessibilityNodeInfo, final int n) {
        }
        
        public void setMovementGranularities(final AccessibilityNodeInfo accessibilityNodeInfo, final int n) {
        }
        
        public void setMultiLine(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean b) {
        }
        
        public void setParent(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
        }
        
        public void setRangeInfo(final AccessibilityNodeInfo accessibilityNodeInfo, final Object o) {
        }
        
        public void setRoleDescription(final AccessibilityNodeInfo accessibilityNodeInfo, final CharSequence charSequence) {
        }
        
        public void setSource(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
        }
        
        public void setTextSelection(final AccessibilityNodeInfo accessibilityNodeInfo, final int n, final int n2) {
        }
        
        public void setTraversalAfter(final AccessibilityNodeInfo accessibilityNodeInfo, final View view) {
        }
        
        public void setTraversalAfter(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
        }
        
        public void setTraversalBefore(final AccessibilityNodeInfo accessibilityNodeInfo, final View view) {
        }
        
        public void setTraversalBefore(final AccessibilityNodeInfo accessibilityNodeInfo, final View view, final int n) {
        }
        
        public void setViewIdResourceName(final AccessibilityNodeInfo accessibilityNodeInfo, final String s) {
        }
        
        public void setVisibleToUser(final AccessibilityNodeInfo accessibilityNodeInfo, final boolean b) {
        }
    }
    
    public static class CollectionInfoCompat
    {
        public static final int SELECTION_MODE_MULTIPLE = 2;
        public static final int SELECTION_MODE_NONE = 0;
        public static final int SELECTION_MODE_SINGLE = 1;
        final Object mInfo;
        
        CollectionInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static CollectionInfoCompat obtain(final int n, final int n2, final boolean b) {
            return new CollectionInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionInfo(n, n2, b));
        }
        
        public static CollectionInfoCompat obtain(final int n, final int n2, final boolean b, final int n3) {
            return new CollectionInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionInfo(n, n2, b, n3));
        }
        
        public int getColumnCount() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionInfoColumnCount(this.mInfo);
        }
        
        public int getRowCount() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionInfoRowCount(this.mInfo);
        }
        
        public int getSelectionMode() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionInfoSelectionMode(this.mInfo);
        }
        
        public boolean isHierarchical() {
            return AccessibilityNodeInfoCompat.IMPL.isCollectionInfoHierarchical(this.mInfo);
        }
    }
    
    public static class CollectionItemInfoCompat
    {
        final Object mInfo;
        
        CollectionItemInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static CollectionItemInfoCompat obtain(final int n, final int n2, final int n3, final int n4, final boolean b) {
            return new CollectionItemInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionItemInfo(n, n2, n3, n4, b));
        }
        
        public static CollectionItemInfoCompat obtain(final int n, final int n2, final int n3, final int n4, final boolean b, final boolean b2) {
            return new CollectionItemInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainCollectionItemInfo(n, n2, n3, n4, b, b2));
        }
        
        public int getColumnIndex() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionItemColumnIndex(this.mInfo);
        }
        
        public int getColumnSpan() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionItemColumnSpan(this.mInfo);
        }
        
        public int getRowIndex() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionItemRowIndex(this.mInfo);
        }
        
        public int getRowSpan() {
            return AccessibilityNodeInfoCompat.IMPL.getCollectionItemRowSpan(this.mInfo);
        }
        
        public boolean isHeading() {
            return AccessibilityNodeInfoCompat.IMPL.isCollectionItemHeading(this.mInfo);
        }
        
        public boolean isSelected() {
            return AccessibilityNodeInfoCompat.IMPL.isCollectionItemSelected(this.mInfo);
        }
    }
    
    public static class RangeInfoCompat
    {
        public static final int RANGE_TYPE_FLOAT = 1;
        public static final int RANGE_TYPE_INT = 0;
        public static final int RANGE_TYPE_PERCENT = 2;
        final Object mInfo;
        
        RangeInfoCompat(final Object mInfo) {
            this.mInfo = mInfo;
        }
        
        public static RangeInfoCompat obtain(final int n, final float n2, final float n3, final float n4) {
            return new RangeInfoCompat(AccessibilityNodeInfoCompat.IMPL.obtainRangeInfo(n, n2, n3, n4));
        }
        
        public float getCurrent() {
            return AccessibilityNodeInfoCompatKitKat.RangeInfo.getCurrent(this.mInfo);
        }
        
        public float getMax() {
            return AccessibilityNodeInfoCompatKitKat.RangeInfo.getMax(this.mInfo);
        }
        
        public float getMin() {
            return AccessibilityNodeInfoCompatKitKat.RangeInfo.getMin(this.mInfo);
        }
        
        public int getType() {
            return AccessibilityNodeInfoCompatKitKat.RangeInfo.getType(this.mInfo);
        }
    }
}
