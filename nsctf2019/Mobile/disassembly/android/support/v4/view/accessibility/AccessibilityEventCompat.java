// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityRecord;
import android.view.accessibility.AccessibilityEvent;
import android.os.Build$VERSION;

public final class AccessibilityEventCompat
{
    public static final int CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 4;
    public static final int CONTENT_CHANGE_TYPE_SUBTREE = 1;
    public static final int CONTENT_CHANGE_TYPE_TEXT = 2;
    public static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0;
    private static final AccessibilityEventCompatBaseImpl IMPL;
    public static final int TYPES_ALL_MASK = -1;
    public static final int TYPE_ANNOUNCEMENT = 16384;
    public static final int TYPE_ASSIST_READING_CONTEXT = 16777216;
    public static final int TYPE_GESTURE_DETECTION_END = 524288;
    public static final int TYPE_GESTURE_DETECTION_START = 262144;
    @Deprecated
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END = 1024;
    @Deprecated
    public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START = 512;
    public static final int TYPE_TOUCH_INTERACTION_END = 2097152;
    public static final int TYPE_TOUCH_INTERACTION_START = 1048576;
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 32768;
    public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 65536;
    public static final int TYPE_VIEW_CONTEXT_CLICKED = 8388608;
    @Deprecated
    public static final int TYPE_VIEW_HOVER_ENTER = 128;
    @Deprecated
    public static final int TYPE_VIEW_HOVER_EXIT = 256;
    @Deprecated
    public static final int TYPE_VIEW_SCROLLED = 4096;
    @Deprecated
    public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED = 8192;
    public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 131072;
    public static final int TYPE_WINDOWS_CHANGED = 4194304;
    @Deprecated
    public static final int TYPE_WINDOW_CONTENT_CHANGED = 2048;
    
    static {
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (AccessibilityEventCompatBaseImpl)new AccessibilityEventCompatApi19Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (AccessibilityEventCompatBaseImpl)new AccessibilityEventCompatApi16Impl();
            return;
        }
        IMPL = new AccessibilityEventCompatBaseImpl();
    }
    
    @Deprecated
    public static void appendRecord(final AccessibilityEvent accessibilityEvent, final AccessibilityRecordCompat accessibilityRecordCompat) {
        accessibilityEvent.appendRecord((AccessibilityRecord)accessibilityRecordCompat.getImpl());
    }
    
    @Deprecated
    public static AccessibilityRecordCompat asRecord(final AccessibilityEvent accessibilityEvent) {
        return new AccessibilityRecordCompat(accessibilityEvent);
    }
    
    public static int getContentChangeTypes(final AccessibilityEvent accessibilityEvent) {
        return AccessibilityEventCompat.IMPL.getContentChangeTypes(accessibilityEvent);
    }
    
    @Deprecated
    public static AccessibilityRecordCompat getRecord(final AccessibilityEvent accessibilityEvent, final int n) {
        return new AccessibilityRecordCompat(accessibilityEvent.getRecord(n));
    }
    
    @Deprecated
    public static int getRecordCount(final AccessibilityEvent accessibilityEvent) {
        return accessibilityEvent.getRecordCount();
    }
    
    public static void setContentChangeTypes(final AccessibilityEvent accessibilityEvent, final int n) {
        AccessibilityEventCompat.IMPL.setContentChangeTypes(accessibilityEvent, n);
    }
    
    public int getAction(final AccessibilityEvent accessibilityEvent) {
        return AccessibilityEventCompat.IMPL.getAction(accessibilityEvent);
    }
    
    public int getMovementGranularity(final AccessibilityEvent accessibilityEvent) {
        return AccessibilityEventCompat.IMPL.getMovementGranularity(accessibilityEvent);
    }
    
    public void setAction(final AccessibilityEvent accessibilityEvent, final int n) {
        AccessibilityEventCompat.IMPL.setAction(accessibilityEvent, n);
    }
    
    public void setMovementGranularity(final AccessibilityEvent accessibilityEvent, final int n) {
        AccessibilityEventCompat.IMPL.setMovementGranularity(accessibilityEvent, n);
    }
    
    @RequiresApi(16)
    static class AccessibilityEventCompatApi16Impl extends AccessibilityEventCompatBaseImpl
    {
        @Override
        public int getAction(final AccessibilityEvent accessibilityEvent) {
            return accessibilityEvent.getAction();
        }
        
        @Override
        public int getMovementGranularity(final AccessibilityEvent accessibilityEvent) {
            return accessibilityEvent.getMovementGranularity();
        }
        
        @Override
        public void setAction(final AccessibilityEvent accessibilityEvent, final int action) {
            accessibilityEvent.setAction(action);
        }
        
        @Override
        public void setMovementGranularity(final AccessibilityEvent accessibilityEvent, final int movementGranularity) {
            accessibilityEvent.setMovementGranularity(movementGranularity);
        }
    }
    
    @RequiresApi(19)
    static class AccessibilityEventCompatApi19Impl extends AccessibilityEventCompatApi16Impl
    {
        @Override
        public int getContentChangeTypes(final AccessibilityEvent accessibilityEvent) {
            return accessibilityEvent.getContentChangeTypes();
        }
        
        @Override
        public void setContentChangeTypes(final AccessibilityEvent accessibilityEvent, final int contentChangeTypes) {
            accessibilityEvent.setContentChangeTypes(contentChangeTypes);
        }
    }
    
    static class AccessibilityEventCompatBaseImpl
    {
        public int getAction(final AccessibilityEvent accessibilityEvent) {
            return 0;
        }
        
        public int getContentChangeTypes(final AccessibilityEvent accessibilityEvent) {
            return 0;
        }
        
        public int getMovementGranularity(final AccessibilityEvent accessibilityEvent) {
            return 0;
        }
        
        public void setAction(final AccessibilityEvent accessibilityEvent, final int n) {
        }
        
        public void setContentChangeTypes(final AccessibilityEvent accessibilityEvent, final int n) {
        }
        
        public void setMovementGranularity(final AccessibilityEvent accessibilityEvent, final int n) {
        }
    }
}
