// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.ViewGroup;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import android.os.Build$VERSION;
import android.view.View$AccessibilityDelegate;

public class AccessibilityDelegateCompat
{
    private static final View$AccessibilityDelegate DEFAULT_DELEGATE;
    private static final AccessibilityDelegateBaseImpl IMPL;
    final View$AccessibilityDelegate mBridge;
    
    static {
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (AccessibilityDelegateBaseImpl)new AccessibilityDelegateApi16Impl();
        }
        else {
            IMPL = new AccessibilityDelegateBaseImpl();
        }
        DEFAULT_DELEGATE = new View$AccessibilityDelegate();
    }
    
    public AccessibilityDelegateCompat() {
        this.mBridge = AccessibilityDelegateCompat.IMPL.newAccessibilityDelegateBridge(this);
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        return AccessibilityDelegateCompat.DEFAULT_DELEGATE.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
    }
    
    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View view) {
        return AccessibilityDelegateCompat.IMPL.getAccessibilityNodeProvider(AccessibilityDelegateCompat.DEFAULT_DELEGATE, view);
    }
    
    View$AccessibilityDelegate getBridge() {
        return this.mBridge;
    }
    
    public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.onInitializeAccessibilityEvent(view, accessibilityEvent);
    }
    
    public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat.unwrap());
    }
    
    public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.onPopulateAccessibilityEvent(view, accessibilityEvent);
    }
    
    public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
        return AccessibilityDelegateCompat.DEFAULT_DELEGATE.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }
    
    public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
        return AccessibilityDelegateCompat.IMPL.performAccessibilityAction(AccessibilityDelegateCompat.DEFAULT_DELEGATE, view, n, bundle);
    }
    
    public void sendAccessibilityEvent(final View view, final int n) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.sendAccessibilityEvent(view, n);
    }
    
    public void sendAccessibilityEventUnchecked(final View view, final AccessibilityEvent accessibilityEvent) {
        AccessibilityDelegateCompat.DEFAULT_DELEGATE.sendAccessibilityEventUnchecked(view, accessibilityEvent);
    }
    
    @RequiresApi(16)
    static class AccessibilityDelegateApi16Impl extends AccessibilityDelegateBaseImpl
    {
        @Override
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View$AccessibilityDelegate view$AccessibilityDelegate, final View view) {
            final AccessibilityNodeProvider accessibilityNodeProvider = view$AccessibilityDelegate.getAccessibilityNodeProvider(view);
            if (accessibilityNodeProvider != null) {
                return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
            }
            return null;
        }
        
        @Override
        public View$AccessibilityDelegate newAccessibilityDelegateBridge(final AccessibilityDelegateCompat accessibilityDelegateCompat) {
            return new View$AccessibilityDelegate() {
                public boolean dispatchPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
                    return accessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
                }
                
                public AccessibilityNodeProvider getAccessibilityNodeProvider(final View view) {
                    final AccessibilityNodeProviderCompat accessibilityNodeProvider = accessibilityDelegateCompat.getAccessibilityNodeProvider(view);
                    if (accessibilityNodeProvider != null) {
                        return (AccessibilityNodeProvider)accessibilityNodeProvider.getProvider();
                    }
                    return null;
                }
                
                public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
                }
                
                public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfo accessibilityNodeInfo) {
                    accessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(view, AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo));
                }
                
                public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
                }
                
                public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
                    return accessibilityDelegateCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
                }
                
                public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
                    return accessibilityDelegateCompat.performAccessibilityAction(view, n, bundle);
                }
                
                public void sendAccessibilityEvent(final View view, final int n) {
                    accessibilityDelegateCompat.sendAccessibilityEvent(view, n);
                }
                
                public void sendAccessibilityEventUnchecked(final View view, final AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
                }
            };
        }
        
        @Override
        public boolean performAccessibilityAction(final View$AccessibilityDelegate view$AccessibilityDelegate, final View view, final int n, final Bundle bundle) {
            return view$AccessibilityDelegate.performAccessibilityAction(view, n, bundle);
        }
    }
    
    static class AccessibilityDelegateBaseImpl
    {
        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(final View$AccessibilityDelegate view$AccessibilityDelegate, final View view) {
            return null;
        }
        
        public View$AccessibilityDelegate newAccessibilityDelegateBridge(final AccessibilityDelegateCompat accessibilityDelegateCompat) {
            return new View$AccessibilityDelegate() {
                public boolean dispatchPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
                    return accessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
                }
                
                public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
                }
                
                public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfo accessibilityNodeInfo) {
                    accessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(view, AccessibilityNodeInfoCompat.wrap(accessibilityNodeInfo));
                }
                
                public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
                }
                
                public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
                    return accessibilityDelegateCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
                }
                
                public void sendAccessibilityEvent(final View view, final int n) {
                    accessibilityDelegateCompat.sendAccessibilityEvent(view, n);
                }
                
                public void sendAccessibilityEventUnchecked(final View view, final AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
                }
            };
        }
        
        public boolean performAccessibilityAction(final View$AccessibilityDelegate view$AccessibilityDelegate, final View view, final int n, final Bundle bundle) {
            return false;
        }
    }
}
