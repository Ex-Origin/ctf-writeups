// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.os.Parcel;
import android.media.browse.MediaBrowser$MediaItem;
import android.service.media.MediaBrowserService$Result;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
class MediaBrowserServiceCompatApi23
{
    public static Object createService(final Context context, final ServiceCompatProxy serviceCompatProxy) {
        return new MediaBrowserServiceAdaptor(context, serviceCompatProxy);
    }
    
    static class MediaBrowserServiceAdaptor extends MediaBrowserServiceCompatApi21.MediaBrowserServiceAdaptor
    {
        MediaBrowserServiceAdaptor(final Context context, final MediaBrowserServiceCompatApi23.ServiceCompatProxy serviceCompatProxy) {
            super(context, serviceCompatProxy);
        }
        
        public void onLoadItem(final String s, final MediaBrowserService$Result<MediaBrowser$MediaItem> mediaBrowserService$Result) {
            ((MediaBrowserServiceCompatApi23.ServiceCompatProxy)this.mServiceProxy).onLoadItem(s, (ResultWrapper<Parcel>)new MediaBrowserServiceCompatApi21.ResultWrapper(mediaBrowserService$Result));
        }
    }
    
    public interface ServiceCompatProxy extends MediaBrowserServiceCompatApi21.ServiceCompatProxy
    {
        void onLoadItem(final String p0, final ResultWrapper<Parcel> p1);
    }
}
