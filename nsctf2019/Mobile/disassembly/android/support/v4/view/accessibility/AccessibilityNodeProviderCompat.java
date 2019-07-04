// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityNodeInfo;
import java.util.ArrayList;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import java.util.List;
import android.support.annotation.Nullable;
import android.os.Build$VERSION;

public class AccessibilityNodeProviderCompat
{
    public static final int HOST_VIEW_ID = -1;
    private static final AccessibilityNodeProviderImpl IMPL;
    private final Object mProvider;
    
    static {
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (AccessibilityNodeProviderImpl)new AccessibilityNodeProviderKitKatImpl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (AccessibilityNodeProviderImpl)new AccessibilityNodeProviderJellyBeanImpl();
            return;
        }
        IMPL = (AccessibilityNodeProviderImpl)new AccessibilityNodeProviderStubImpl();
    }
    
    public AccessibilityNodeProviderCompat() {
        this.mProvider = AccessibilityNodeProviderCompat.IMPL.newAccessibilityNodeProviderBridge(this);
    }
    
    public AccessibilityNodeProviderCompat(final Object mProvider) {
        this.mProvider = mProvider;
    }
    
    @Nullable
    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(final int n) {
        return null;
    }
    
    @Nullable
    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(final String s, final int n) {
        return null;
    }
    
    @Nullable
    public AccessibilityNodeInfoCompat findFocus(final int n) {
        return null;
    }
    
    public Object getProvider() {
        return this.mProvider;
    }
    
    public boolean performAction(final int n, final int n2, final Bundle bundle) {
        return false;
    }
    
    interface AccessibilityNodeProviderImpl
    {
        Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat p0);
    }
    
    @RequiresApi(16)
    private static class AccessibilityNodeProviderJellyBeanImpl extends AccessibilityNodeProviderStubImpl
    {
        @Override
        public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge((AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge)new AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge() {
                @Override
                public Object createAccessibilityNodeInfo(final int n) {
                    final AccessibilityNodeInfoCompat accessibilityNodeInfo = accessibilityNodeProviderCompat.createAccessibilityNodeInfo(n);
                    if (accessibilityNodeInfo == null) {
                        return null;
                    }
                    return accessibilityNodeInfo.unwrap();
                }
                
                @Override
                public List<Object> findAccessibilityNodeInfosByText(final String s, int n) {
                    final List<AccessibilityNodeInfoCompat> accessibilityNodeInfosByText = accessibilityNodeProviderCompat.findAccessibilityNodeInfosByText(s, n);
                    Object o;
                    if (accessibilityNodeInfosByText == null) {
                        o = null;
                    }
                    else {
                        final ArrayList<AccessibilityNodeInfo> list = new ArrayList<AccessibilityNodeInfo>();
                        final int size = accessibilityNodeInfosByText.size();
                        n = 0;
                        while (true) {
                            o = list;
                            if (n >= size) {
                                break;
                            }
                            list.add(accessibilityNodeInfosByText.get(n).unwrap());
                            ++n;
                        }
                    }
                    return (List<Object>)o;
                }
                
                @Override
                public boolean performAction(final int n, final int n2, final Bundle bundle) {
                    return accessibilityNodeProviderCompat.performAction(n, n2, bundle);
                }
            });
        }
    }
    
    @RequiresApi(19)
    private static class AccessibilityNodeProviderKitKatImpl extends AccessibilityNodeProviderStubImpl
    {
        @Override
        public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge((AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge)new AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge() {
                @Override
                public Object createAccessibilityNodeInfo(final int n) {
                    final AccessibilityNodeInfoCompat accessibilityNodeInfo = accessibilityNodeProviderCompat.createAccessibilityNodeInfo(n);
                    if (accessibilityNodeInfo == null) {
                        return null;
                    }
                    return accessibilityNodeInfo.unwrap();
                }
                
                @Override
                public List<Object> findAccessibilityNodeInfosByText(final String s, int n) {
                    final List<AccessibilityNodeInfoCompat> accessibilityNodeInfosByText = accessibilityNodeProviderCompat.findAccessibilityNodeInfosByText(s, n);
                    Object o;
                    if (accessibilityNodeInfosByText == null) {
                        o = null;
                    }
                    else {
                        final ArrayList<AccessibilityNodeInfo> list = new ArrayList<AccessibilityNodeInfo>();
                        final int size = accessibilityNodeInfosByText.size();
                        n = 0;
                        while (true) {
                            o = list;
                            if (n >= size) {
                                break;
                            }
                            list.add(accessibilityNodeInfosByText.get(n).unwrap());
                            ++n;
                        }
                    }
                    return (List<Object>)o;
                }
                
                @Override
                public Object findFocus(final int n) {
                    final AccessibilityNodeInfoCompat focus = accessibilityNodeProviderCompat.findFocus(n);
                    if (focus == null) {
                        return null;
                    }
                    return focus.unwrap();
                }
                
                @Override
                public boolean performAction(final int n, final int n2, final Bundle bundle) {
                    return accessibilityNodeProviderCompat.performAction(n, n2, bundle);
                }
            });
        }
    }
    
    static class AccessibilityNodeProviderStubImpl implements AccessibilityNodeProviderImpl
    {
        @Override
        public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            return null;
        }
    }
}
