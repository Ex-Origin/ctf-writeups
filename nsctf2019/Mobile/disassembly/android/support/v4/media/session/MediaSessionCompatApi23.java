// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media.session;

import android.os.Bundle;
import android.net.Uri;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
class MediaSessionCompatApi23
{
    public static Object createCallback(final Callback callback) {
        return new CallbackProxy(callback);
    }
    
    public interface Callback extends MediaSessionCompatApi21.Callback
    {
        void onPlayFromUri(final Uri p0, final Bundle p1);
    }
    
    static class CallbackProxy<T extends MediaSessionCompatApi23.Callback> extends MediaSessionCompatApi21.CallbackProxy<T>
    {
        public CallbackProxy(final T t) {
            super(t);
        }
        
        public void onPlayFromUri(final Uri uri, final Bundle bundle) {
            ((MediaSessionCompatApi23.Callback)this.mCallback).onPlayFromUri(uri, bundle);
        }
    }
}
