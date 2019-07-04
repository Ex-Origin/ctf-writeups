// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.os.Bundle;
import java.util.Set;

@Deprecated
class RemoteInputCompatBase
{
    public abstract static class RemoteInput
    {
        protected abstract boolean getAllowFreeFormInput();
        
        protected abstract Set<String> getAllowedDataTypes();
        
        protected abstract CharSequence[] getChoices();
        
        protected abstract Bundle getExtras();
        
        protected abstract CharSequence getLabel();
        
        protected abstract String getResultKey();
        
        public interface Factory
        {
            RemoteInput build(final String p0, final CharSequence p1, final CharSequence[] p2, final boolean p3, final Bundle p4, final Set<String> p5);
            
            RemoteInput[] newArray(final int p0);
        }
    }
}
