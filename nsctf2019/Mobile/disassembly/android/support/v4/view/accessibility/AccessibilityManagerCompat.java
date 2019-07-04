// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.support.annotation.RequiresApi;
import android.support.annotation.NonNull;
import android.accessibilityservice.AccessibilityServiceInfo;
import java.util.List;
import android.view.accessibility.AccessibilityManager$TouchExplorationStateChangeListener;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityManager$AccessibilityStateChangeListener;
import android.view.accessibility.AccessibilityManager;

public final class AccessibilityManagerCompat
{
    @Deprecated
    public static boolean addAccessibilityStateChangeListener(final AccessibilityManager accessibilityManager, final AccessibilityStateChangeListener accessibilityStateChangeListener) {
        return accessibilityStateChangeListener != null && accessibilityManager.addAccessibilityStateChangeListener((AccessibilityManager$AccessibilityStateChangeListener)new AccessibilityStateChangeListenerWrapper(accessibilityStateChangeListener));
    }
    
    public static boolean addTouchExplorationStateChangeListener(final AccessibilityManager accessibilityManager, final TouchExplorationStateChangeListener touchExplorationStateChangeListener) {
        return Build$VERSION.SDK_INT >= 19 && touchExplorationStateChangeListener != null && accessibilityManager.addTouchExplorationStateChangeListener((AccessibilityManager$TouchExplorationStateChangeListener)new TouchExplorationStateChangeListenerWrapper(touchExplorationStateChangeListener));
    }
    
    @Deprecated
    public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(final AccessibilityManager accessibilityManager, final int n) {
        return (List<AccessibilityServiceInfo>)accessibilityManager.getEnabledAccessibilityServiceList(n);
    }
    
    @Deprecated
    public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(final AccessibilityManager accessibilityManager) {
        return (List<AccessibilityServiceInfo>)accessibilityManager.getInstalledAccessibilityServiceList();
    }
    
    @Deprecated
    public static boolean isTouchExplorationEnabled(final AccessibilityManager accessibilityManager) {
        return accessibilityManager.isTouchExplorationEnabled();
    }
    
    @Deprecated
    public static boolean removeAccessibilityStateChangeListener(final AccessibilityManager accessibilityManager, final AccessibilityStateChangeListener accessibilityStateChangeListener) {
        return accessibilityStateChangeListener != null && accessibilityManager.removeAccessibilityStateChangeListener((AccessibilityManager$AccessibilityStateChangeListener)new AccessibilityStateChangeListenerWrapper(accessibilityStateChangeListener));
    }
    
    public static boolean removeTouchExplorationStateChangeListener(final AccessibilityManager accessibilityManager, final TouchExplorationStateChangeListener touchExplorationStateChangeListener) {
        return Build$VERSION.SDK_INT >= 19 && touchExplorationStateChangeListener != null && accessibilityManager.removeTouchExplorationStateChangeListener((AccessibilityManager$TouchExplorationStateChangeListener)new TouchExplorationStateChangeListenerWrapper(touchExplorationStateChangeListener));
    }
    
    @Deprecated
    public interface AccessibilityStateChangeListener
    {
        @Deprecated
        void onAccessibilityStateChanged(final boolean p0);
    }
    
    @Deprecated
    public abstract static class AccessibilityStateChangeListenerCompat implements AccessibilityStateChangeListener
    {
    }
    
    private static class AccessibilityStateChangeListenerWrapper implements AccessibilityManager$AccessibilityStateChangeListener
    {
        AccessibilityStateChangeListener mListener;
        
        AccessibilityStateChangeListenerWrapper(@NonNull final AccessibilityStateChangeListener mListener) {
            this.mListener = mListener;
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o != null && this.getClass() == o.getClass() && this.mListener.equals(((AccessibilityStateChangeListenerWrapper)o).mListener));
        }
        
        @Override
        public int hashCode() {
            return this.mListener.hashCode();
        }
        
        public void onAccessibilityStateChanged(final boolean b) {
            this.mListener.onAccessibilityStateChanged(b);
        }
    }
    
    public interface TouchExplorationStateChangeListener
    {
        void onTouchExplorationStateChanged(final boolean p0);
    }
    
    @RequiresApi(19)
    private static class TouchExplorationStateChangeListenerWrapper implements AccessibilityManager$TouchExplorationStateChangeListener
    {
        final TouchExplorationStateChangeListener mListener;
        
        TouchExplorationStateChangeListenerWrapper(@NonNull final TouchExplorationStateChangeListener mListener) {
            this.mListener = mListener;
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o != null && this.getClass() == o.getClass() && this.mListener.equals(((TouchExplorationStateChangeListenerWrapper)o).mListener));
        }
        
        @Override
        public int hashCode() {
            return this.mListener.hashCode();
        }
        
        public void onTouchExplorationStateChanged(final boolean b) {
            this.mListener.onTouchExplorationStateChanged(b);
        }
    }
}
