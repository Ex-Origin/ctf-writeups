// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import java.lang.reflect.InvocationTargetException;
import android.media.browse.MediaBrowser$MediaItem;
import java.util.List;
import java.lang.reflect.Constructor;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class ParceledListSliceAdapterApi21
{
    private static Constructor sConstructor;
    
    static {
        try {
            ParceledListSliceAdapterApi21.sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(List.class);
        }
        catch (ClassNotFoundException ex) {}
        catch (NoSuchMethodException ex2) {
            goto Label_0022;
        }
    }
    
    static Object newInstance(List<MediaBrowser$MediaItem> instance) {
        try {
            instance = (IllegalAccessException)ParceledListSliceAdapterApi21.sConstructor.newInstance(instance);
            return instance;
        }
        catch (InstantiationException ex) {}
        catch (IllegalAccessException instance) {
            goto Label_0018;
        }
        catch (InvocationTargetException instance) {
            goto Label_0018;
        }
    }
}
