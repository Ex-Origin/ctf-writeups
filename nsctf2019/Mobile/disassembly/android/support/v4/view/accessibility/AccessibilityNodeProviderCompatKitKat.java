// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.os.Bundle;
import java.util.List;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class AccessibilityNodeProviderCompatKitKat
{
    public static Object newAccessibilityNodeProviderBridge(final AccessibilityNodeInfoBridge accessibilityNodeInfoBridge) {
        return new AccessibilityNodeProvider() {
            public AccessibilityNodeInfo createAccessibilityNodeInfo(final int n) {
                return (AccessibilityNodeInfo)accessibilityNodeInfoBridge.createAccessibilityNodeInfo(n);
            }
            
            public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(final String s, final int n) {
                return (List<AccessibilityNodeInfo>)accessibilityNodeInfoBridge.findAccessibilityNodeInfosByText(s, n);
            }
            
            public AccessibilityNodeInfo findFocus(final int n) {
                return (AccessibilityNodeInfo)accessibilityNodeInfoBridge.findFocus(n);
            }
            
            public boolean performAction(final int n, final int n2, final Bundle bundle) {
                return accessibilityNodeInfoBridge.performAction(n, n2, bundle);
            }
        };
    }
    
    interface AccessibilityNodeInfoBridge
    {
        Object createAccessibilityNodeInfo(final int p0);
        
        List<Object> findAccessibilityNodeInfosByText(final String p0, final int p1);
        
        Object findFocus(final int p0);
        
        boolean performAction(final int p0, final int p1, final Bundle p2);
    }
}
