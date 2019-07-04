// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.view.accessibility.AccessibilityWindowInfo;
import android.os.Build$VERSION;

public class AccessibilityWindowInfoCompat
{
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private Object mInfo;
    
    private AccessibilityWindowInfoCompat(final Object mInfo) {
        this.mInfo = mInfo;
    }
    
    public static AccessibilityWindowInfoCompat obtain() {
        if (Build$VERSION.SDK_INT >= 21) {
            return wrapNonNullInstance(AccessibilityWindowInfo.obtain());
        }
        return null;
    }
    
    public static AccessibilityWindowInfoCompat obtain(final AccessibilityWindowInfoCompat accessibilityWindowInfoCompat) {
        if (Build$VERSION.SDK_INT < 21 || accessibilityWindowInfoCompat == null) {
            return null;
        }
        return wrapNonNullInstance(AccessibilityWindowInfo.obtain((AccessibilityWindowInfo)accessibilityWindowInfoCompat.mInfo));
    }
    
    private static String typeToString(final int n) {
        switch (n) {
            default: {
                return "<UNKNOWN>";
            }
            case 1: {
                return "TYPE_APPLICATION";
            }
            case 2: {
                return "TYPE_INPUT_METHOD";
            }
            case 3: {
                return "TYPE_SYSTEM";
            }
            case 4: {
                return "TYPE_ACCESSIBILITY_OVERLAY";
            }
        }
    }
    
    static AccessibilityWindowInfoCompat wrapNonNullInstance(final Object o) {
        if (o != null) {
            return new AccessibilityWindowInfoCompat(o);
        }
        return null;
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
            final AccessibilityWindowInfoCompat accessibilityWindowInfoCompat = (AccessibilityWindowInfoCompat)o;
            if (this.mInfo == null) {
                if (accessibilityWindowInfoCompat.mInfo != null) {
                    return false;
                }
            }
            else if (!this.mInfo.equals(accessibilityWindowInfoCompat.mInfo)) {
                return false;
            }
        }
        return true;
    }
    
    public AccessibilityNodeInfoCompat getAnchor() {
        if (Build$VERSION.SDK_INT >= 24) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getAnchor());
        }
        return null;
    }
    
    public void getBoundsInScreen(final Rect rect) {
        if (Build$VERSION.SDK_INT >= 21) {
            ((AccessibilityWindowInfo)this.mInfo).getBoundsInScreen(rect);
        }
    }
    
    public AccessibilityWindowInfoCompat getChild(final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            return wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getChild(n));
        }
        return null;
    }
    
    public int getChildCount() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getChildCount();
        }
        return 0;
    }
    
    public int getId() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getId();
        }
        return -1;
    }
    
    public int getLayer() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getLayer();
        }
        return -1;
    }
    
    public AccessibilityWindowInfoCompat getParent() {
        if (Build$VERSION.SDK_INT >= 21) {
            return wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getParent());
        }
        return null;
    }
    
    public AccessibilityNodeInfoCompat getRoot() {
        if (Build$VERSION.SDK_INT >= 21) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance(((AccessibilityWindowInfo)this.mInfo).getRoot());
        }
        return null;
    }
    
    public CharSequence getTitle() {
        if (Build$VERSION.SDK_INT >= 24) {
            return ((AccessibilityWindowInfo)this.mInfo).getTitle();
        }
        return null;
    }
    
    public int getType() {
        if (Build$VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getType();
        }
        return -1;
    }
    
    @Override
    public int hashCode() {
        if (this.mInfo == null) {
            return 0;
        }
        return this.mInfo.hashCode();
    }
    
    public boolean isAccessibilityFocused() {
        return Build$VERSION.SDK_INT < 21 || ((AccessibilityWindowInfo)this.mInfo).isAccessibilityFocused();
    }
    
    public boolean isActive() {
        return Build$VERSION.SDK_INT < 21 || ((AccessibilityWindowInfo)this.mInfo).isActive();
    }
    
    public boolean isFocused() {
        return Build$VERSION.SDK_INT < 21 || ((AccessibilityWindowInfo)this.mInfo).isFocused();
    }
    
    public void recycle() {
        if (Build$VERSION.SDK_INT >= 21) {
            ((AccessibilityWindowInfo)this.mInfo).recycle();
        }
    }
    
    @Override
    public String toString() {
        final boolean b = true;
        final StringBuilder sb = new StringBuilder();
        final Rect rect = new Rect();
        this.getBoundsInScreen(rect);
        sb.append("AccessibilityWindowInfo[");
        sb.append("id=").append(this.getId());
        sb.append(", type=").append(typeToString(this.getType()));
        sb.append(", layer=").append(this.getLayer());
        sb.append(", bounds=").append(rect);
        sb.append(", focused=").append(this.isFocused());
        sb.append(", active=").append(this.isActive());
        sb.append(", hasParent=").append(this.getParent() != null);
        sb.append(", hasChildren=").append(this.getChildCount() > 0 && b);
        sb.append(']');
        return sb.toString();
    }
}
