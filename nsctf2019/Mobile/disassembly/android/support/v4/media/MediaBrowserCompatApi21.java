// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import java.util.List;
import android.support.annotation.NonNull;
import android.media.browse.MediaBrowser$MediaItem;
import android.media.browse.MediaBrowser$SubscriptionCallback;
import android.media.browse.MediaBrowser$ConnectionCallback;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.media.browse.MediaBrowser;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class MediaBrowserCompatApi21
{
    static final String NULL_MEDIA_ITEM_ID = "android.support.v4.media.MediaBrowserCompat.NULL_MEDIA_ITEM";
    
    public static void connect(final Object o) {
        ((MediaBrowser)o).connect();
    }
    
    public static Object createBrowser(final Context context, final ComponentName componentName, final Object o, final Bundle bundle) {
        return new MediaBrowser(context, componentName, (MediaBrowser$ConnectionCallback)o, bundle);
    }
    
    public static Object createConnectionCallback(final ConnectionCallback connectionCallback) {
        return new ConnectionCallbackProxy(connectionCallback);
    }
    
    public static Object createSubscriptionCallback(final SubscriptionCallback subscriptionCallback) {
        return new SubscriptionCallbackProxy(subscriptionCallback);
    }
    
    public static void disconnect(final Object o) {
        ((MediaBrowser)o).disconnect();
    }
    
    public static Bundle getExtras(final Object o) {
        return ((MediaBrowser)o).getExtras();
    }
    
    public static String getRoot(final Object o) {
        return ((MediaBrowser)o).getRoot();
    }
    
    public static ComponentName getServiceComponent(final Object o) {
        return ((MediaBrowser)o).getServiceComponent();
    }
    
    public static Object getSessionToken(final Object o) {
        return ((MediaBrowser)o).getSessionToken();
    }
    
    public static boolean isConnected(final Object o) {
        return ((MediaBrowser)o).isConnected();
    }
    
    public static void subscribe(final Object o, final String s, final Object o2) {
        ((MediaBrowser)o).subscribe(s, (MediaBrowser$SubscriptionCallback)o2);
    }
    
    public static void unsubscribe(final Object o, final String s) {
        ((MediaBrowser)o).unsubscribe(s);
    }
    
    interface ConnectionCallback
    {
        void onConnected();
        
        void onConnectionFailed();
        
        void onConnectionSuspended();
    }
    
    static class ConnectionCallbackProxy<T extends ConnectionCallback> extends MediaBrowser$ConnectionCallback
    {
        protected final T mConnectionCallback;
        
        public ConnectionCallbackProxy(final T mConnectionCallback) {
            this.mConnectionCallback = mConnectionCallback;
        }
        
        public void onConnected() {
            ((ConnectionCallback)this.mConnectionCallback).onConnected();
        }
        
        public void onConnectionFailed() {
            ((ConnectionCallback)this.mConnectionCallback).onConnectionFailed();
        }
        
        public void onConnectionSuspended() {
            ((ConnectionCallback)this.mConnectionCallback).onConnectionSuspended();
        }
    }
    
    static class MediaItem
    {
        public static Object getDescription(final Object o) {
            return ((MediaBrowser$MediaItem)o).getDescription();
        }
        
        public static int getFlags(final Object o) {
            return ((MediaBrowser$MediaItem)o).getFlags();
        }
    }
    
    interface SubscriptionCallback
    {
        void onChildrenLoaded(@NonNull final String p0, final List<?> p1);
        
        void onError(@NonNull final String p0);
    }
    
    static class SubscriptionCallbackProxy<T extends SubscriptionCallback> extends MediaBrowser$SubscriptionCallback
    {
        protected final T mSubscriptionCallback;
        
        public SubscriptionCallbackProxy(final T mSubscriptionCallback) {
            this.mSubscriptionCallback = mSubscriptionCallback;
        }
        
        public void onChildrenLoaded(@NonNull final String s, final List<MediaBrowser$MediaItem> list) {
            ((SubscriptionCallback)this.mSubscriptionCallback).onChildrenLoaded(s, list);
        }
        
        public void onError(@NonNull final String s) {
            ((SubscriptionCallback)this.mSubscriptionCallback).onError(s);
        }
    }
}
