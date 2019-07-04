// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo$RangeInfo;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class AccessibilityNodeInfoCompatKitKat
{
    static class RangeInfo
    {
        static float getCurrent(final Object o) {
            return ((AccessibilityNodeInfo$RangeInfo)o).getCurrent();
        }
        
        static float getMax(final Object o) {
            return ((AccessibilityNodeInfo$RangeInfo)o).getMax();
        }
        
        static float getMin(final Object o) {
            return ((AccessibilityNodeInfo$RangeInfo)o).getMin();
        }
        
        static int getType(final Object o) {
            return ((AccessibilityNodeInfo$RangeInfo)o).getType();
        }
    }
}
