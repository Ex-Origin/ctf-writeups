// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.util.List;

public class FragmentManagerNonConfig
{
    private final List<FragmentManagerNonConfig> mChildNonConfigs;
    private final List<Fragment> mFragments;
    
    FragmentManagerNonConfig(final List<Fragment> mFragments, final List<FragmentManagerNonConfig> mChildNonConfigs) {
        this.mFragments = mFragments;
        this.mChildNonConfigs = mChildNonConfigs;
    }
    
    List<FragmentManagerNonConfig> getChildNonConfigs() {
        return this.mChildNonConfigs;
    }
    
    List<Fragment> getFragments() {
        return this.mFragments;
    }
}
