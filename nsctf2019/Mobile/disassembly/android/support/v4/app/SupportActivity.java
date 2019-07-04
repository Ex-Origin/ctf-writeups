// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.support.v4.util.SimpleArrayMap;
import android.support.annotation.RestrictTo;
import android.app.Activity;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class SupportActivity extends Activity
{
    private SimpleArrayMap<Class<? extends ExtraData>, ExtraData> mExtraDataMap;
    
    public SupportActivity() {
        this.mExtraDataMap = new SimpleArrayMap<Class<? extends ExtraData>, ExtraData>();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public <T extends ExtraData> T getExtraData(final Class<T> clazz) {
        return (T)this.mExtraDataMap.get(clazz);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void putExtraData(final ExtraData extraData) {
        this.mExtraDataMap.put(extraData.getClass(), extraData);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class ExtraData
    {
    }
}
