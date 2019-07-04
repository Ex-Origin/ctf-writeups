// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.media.browse.MediaBrowser$MediaItem;
import java.util.List;
import android.support.annotation.NonNull;
import android.media.browse.MediaBrowser$SubscriptionCallback;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
class MediaBrowserCompatApi24
{
    public static Object createSubscriptionCallback(final SubscriptionCallback subscriptionCallback) {
        return new SubscriptionCallbackProxy(subscriptionCallback);
    }
    
    public static void subscribe(final Object o, final String s, final Bundle bundle, final Object o2) {
        ((MediaBrowser)o).subscribe(s, bundle, (MediaBrowser$SubscriptionCallback)o2);
    }
    
    public static void unsubscribe(final Object o, final String s, final Object o2) {
        ((MediaBrowser)o).unsubscribe(s, (MediaBrowser$SubscriptionCallback)o2);
    }
    
    interface SubscriptionCallback extends MediaBrowserCompatApi21.SubscriptionCallback
    {
        void onChildrenLoaded(@NonNull final String p0, final List<?> p1, @NonNull final Bundle p2);
        
        void onError(@NonNull final String p0, @NonNull final Bundle p1);
    }
    
    static class SubscriptionCallbackProxy<T extends MediaBrowserCompatApi24.SubscriptionCallback> extends MediaBrowserCompatApi21.SubscriptionCallbackProxy<T>
    {
        public SubscriptionCallbackProxy(final T t) {
            super(t);
        }
        
        public void onChildrenLoaded(@NonNull final String s, final List<MediaBrowser$MediaItem> list, @NonNull final Bundle bundle) {
            ((MediaBrowserCompatApi24.SubscriptionCallback)this.mSubscriptionCallback).onChildrenLoaded(s, list, bundle);
        }
        
        public void onError(@NonNull final String s, @NonNull final Bundle bundle) {
            ((MediaBrowserCompatApi24.SubscriptionCallback)this.mSubscriptionCallback).onError(s, bundle);
        }
    }
}
