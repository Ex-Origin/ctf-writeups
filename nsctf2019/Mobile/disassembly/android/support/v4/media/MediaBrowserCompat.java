// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import java.util.Collections;
import android.os.Binder;
import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import android.os.Parcelable$Creator;
import java.util.Iterator;
import java.util.Map;
import android.content.ServiceConnection;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.app.BundleCompat;
import android.os.RemoteException;
import android.support.v4.util.ArrayMap;
import android.support.annotation.RequiresApi;
import android.os.Parcelable;
import android.os.Parcel;
import android.support.v4.os.ResultReceiver;
import java.util.List;
import android.os.Message;
import android.os.Messenger;
import java.lang.ref.WeakReference;
import android.os.Handler;
import android.text.TextUtils;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Build$VERSION;
import android.support.v4.os.BuildCompat;
import android.os.Bundle;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public final class MediaBrowserCompat
{
    static final boolean DEBUG;
    public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
    public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
    static final String TAG = "MediaBrowserCompat";
    private final MediaBrowserImpl mImpl;
    
    static {
        DEBUG = Log.isLoggable("MediaBrowserCompat", 3);
    }
    
    public MediaBrowserCompat(final Context context, final ComponentName componentName, final ConnectionCallback connectionCallback, final Bundle bundle) {
        if (BuildCompat.isAtLeastO()) {
            this.mImpl = (MediaBrowserImpl)new MediaBrowserImplApi24(context, componentName, connectionCallback, bundle);
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (MediaBrowserImpl)new MediaBrowserImplApi23(context, componentName, connectionCallback, bundle);
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaBrowserImpl)new MediaBrowserImplApi21(context, componentName, connectionCallback, bundle);
            return;
        }
        this.mImpl = (MediaBrowserImpl)new MediaBrowserImplBase(context, componentName, connectionCallback, bundle);
    }
    
    public void connect() {
        this.mImpl.connect();
    }
    
    public void disconnect() {
        this.mImpl.disconnect();
    }
    
    @Nullable
    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }
    
    public void getItem(@NonNull final String s, @NonNull final ItemCallback itemCallback) {
        this.mImpl.getItem(s, itemCallback);
    }
    
    @NonNull
    public String getRoot() {
        return this.mImpl.getRoot();
    }
    
    @NonNull
    public ComponentName getServiceComponent() {
        return this.mImpl.getServiceComponent();
    }
    
    @NonNull
    public MediaSessionCompat.Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }
    
    public boolean isConnected() {
        return this.mImpl.isConnected();
    }
    
    public void search(@NonNull final String s, final Bundle bundle, @NonNull final SearchCallback searchCallback) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("query cannot be empty");
        }
        if (searchCallback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        this.mImpl.search(s, bundle, searchCallback);
    }
    
    public void sendCustomAction(@NonNull final String s, final Bundle bundle, @Nullable final CustomActionCallback customActionCallback) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("action cannot be empty");
        }
        this.mImpl.sendCustomAction(s, bundle, customActionCallback);
    }
    
    public void subscribe(@NonNull final String s, @NonNull final Bundle bundle, @NonNull final SubscriptionCallback subscriptionCallback) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        if (subscriptionCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        if (bundle == null) {
            throw new IllegalArgumentException("options are null");
        }
        this.mImpl.subscribe(s, bundle, subscriptionCallback);
    }
    
    public void subscribe(@NonNull final String s, @NonNull final SubscriptionCallback subscriptionCallback) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        if (subscriptionCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        this.mImpl.subscribe(s, null, subscriptionCallback);
    }
    
    public void unsubscribe(@NonNull final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        this.mImpl.unsubscribe(s, null);
    }
    
    public void unsubscribe(@NonNull final String s, @NonNull final SubscriptionCallback subscriptionCallback) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("parentId is empty");
        }
        if (subscriptionCallback == null) {
            throw new IllegalArgumentException("callback is null");
        }
        this.mImpl.unsubscribe(s, subscriptionCallback);
    }
    
    private static class CallbackHandler extends Handler
    {
        private final WeakReference<MediaBrowserServiceCallbackImpl> mCallbackImplRef;
        private WeakReference<Messenger> mCallbacksMessengerRef;
        
        CallbackHandler(final MediaBrowserServiceCallbackImpl mediaBrowserServiceCallbackImpl) {
            this.mCallbackImplRef = new WeakReference<MediaBrowserServiceCallbackImpl>(mediaBrowserServiceCallbackImpl);
        }
        
        public void handleMessage(final Message message) {
            if (this.mCallbacksMessengerRef == null || this.mCallbacksMessengerRef.get() == null || this.mCallbackImplRef.get() == null) {
                return;
            }
            final Bundle data = message.getData();
            data.setClassLoader(MediaSessionCompat.class.getClassLoader());
            switch (message.what) {
                default: {
                    Log.w("MediaBrowserCompat", "Unhandled message: " + message + "\n  Client version: " + 1 + "\n  Service version: " + message.arg1);
                }
                case 1: {
                    ((MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onServiceConnected(this.mCallbacksMessengerRef.get(), data.getString("data_media_item_id"), (MediaSessionCompat.Token)data.getParcelable("data_media_session_token"), data.getBundle("data_root_hints"));
                }
                case 2: {
                    ((MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onConnectionFailed(this.mCallbacksMessengerRef.get());
                }
                case 3: {
                    ((MediaBrowserServiceCallbackImpl)this.mCallbackImplRef.get()).onLoadChildren(this.mCallbacksMessengerRef.get(), data.getString("data_media_item_id"), data.getParcelableArrayList("data_media_item_list"), data.getBundle("data_options"));
                }
            }
        }
        
        void setCallbacksMessenger(final Messenger messenger) {
            this.mCallbacksMessengerRef = new WeakReference<Messenger>(messenger);
        }
    }
    
    public static class ConnectionCallback
    {
        ConnectionCallbackInternal mConnectionCallbackInternal;
        final Object mConnectionCallbackObj;
        
        public ConnectionCallback() {
            if (Build$VERSION.SDK_INT >= 21) {
                this.mConnectionCallbackObj = MediaBrowserCompatApi21.createConnectionCallback((MediaBrowserCompatApi21.ConnectionCallback)new StubApi21());
                return;
            }
            this.mConnectionCallbackObj = null;
        }
        
        public void onConnected() {
        }
        
        public void onConnectionFailed() {
        }
        
        public void onConnectionSuspended() {
        }
        
        void setInternalConnectionCallback(final ConnectionCallbackInternal mConnectionCallbackInternal) {
            this.mConnectionCallbackInternal = mConnectionCallbackInternal;
        }
        
        interface ConnectionCallbackInternal
        {
            void onConnected();
            
            void onConnectionFailed();
            
            void onConnectionSuspended();
        }
        
        private class StubApi21 implements MediaBrowserCompatApi21.ConnectionCallback
        {
            @Override
            public void onConnected() {
                if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnected();
                }
                MediaBrowserCompat.ConnectionCallback.this.onConnected();
            }
            
            @Override
            public void onConnectionFailed() {
                if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionFailed();
                }
                MediaBrowserCompat.ConnectionCallback.this.onConnectionFailed();
            }
            
            @Override
            public void onConnectionSuspended() {
                if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionSuspended();
                }
                MediaBrowserCompat.ConnectionCallback.this.onConnectionSuspended();
            }
        }
    }
    
    public abstract static class CustomActionCallback
    {
        public void onError(final String s, final Bundle bundle, final Bundle bundle2) {
        }
        
        public void onProgressUpdate(final String s, final Bundle bundle, final Bundle bundle2) {
        }
        
        public void onResult(final String s, final Bundle bundle, final Bundle bundle2) {
        }
    }
    
    private static class CustomActionResultReceiver extends ResultReceiver
    {
        private final String mAction;
        private final CustomActionCallback mCallback;
        private final Bundle mExtras;
        
        CustomActionResultReceiver(final String mAction, final Bundle mExtras, final CustomActionCallback mCallback, final Handler handler) {
            super(handler);
            this.mAction = mAction;
            this.mExtras = mExtras;
            this.mCallback = mCallback;
        }
        
        @Override
        protected void onReceiveResult(final int n, final Bundle bundle) {
            if (this.mCallback == null) {
                return;
            }
            switch (n) {
                default: {
                    Log.w("MediaBrowserCompat", "Unknown result code: " + n + " (extras=" + this.mExtras + ", resultData=" + bundle + ")");
                }
                case 1: {
                    this.mCallback.onProgressUpdate(this.mAction, this.mExtras, bundle);
                }
                case 0: {
                    this.mCallback.onResult(this.mAction, this.mExtras, bundle);
                }
                case -1: {
                    this.mCallback.onError(this.mAction, this.mExtras, bundle);
                }
            }
        }
    }
    
    public abstract static class ItemCallback
    {
        final Object mItemCallbackObj;
        
        public ItemCallback() {
            if (Build$VERSION.SDK_INT >= 23) {
                this.mItemCallbackObj = MediaBrowserCompatApi23.createItemCallback((MediaBrowserCompatApi23.ItemCallback)new StubApi23());
                return;
            }
            this.mItemCallbackObj = null;
        }
        
        public void onError(@NonNull final String s) {
        }
        
        public void onItemLoaded(final MediaItem mediaItem) {
        }
        
        private class StubApi23 implements MediaBrowserCompatApi23.ItemCallback
        {
            @Override
            public void onError(@NonNull final String s) {
                MediaBrowserCompat.ItemCallback.this.onError(s);
            }
            
            @Override
            public void onItemLoaded(final Parcel parcel) {
                if (parcel == null) {
                    MediaBrowserCompat.ItemCallback.this.onItemLoaded(null);
                    return;
                }
                parcel.setDataPosition(0);
                final MediaItem mediaItem = (MediaItem)MediaItem.CREATOR.createFromParcel(parcel);
                parcel.recycle();
                MediaBrowserCompat.ItemCallback.this.onItemLoaded(mediaItem);
            }
        }
    }
    
    private static class ItemReceiver extends ResultReceiver
    {
        private final ItemCallback mCallback;
        private final String mMediaId;
        
        ItemReceiver(final String mMediaId, final ItemCallback mCallback, final Handler handler) {
            super(handler);
            this.mMediaId = mMediaId;
            this.mCallback = mCallback;
        }
        
        @Override
        protected void onReceiveResult(final int n, final Bundle bundle) {
            if (bundle != null) {
                bundle.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            }
            if (n != 0 || bundle == null || !bundle.containsKey("media_item")) {
                this.mCallback.onError(this.mMediaId);
                return;
            }
            final Parcelable parcelable = bundle.getParcelable("media_item");
            if (parcelable == null || parcelable instanceof MediaItem) {
                this.mCallback.onItemLoaded((MediaItem)parcelable);
                return;
            }
            this.mCallback.onError(this.mMediaId);
        }
    }
    
    interface MediaBrowserImpl
    {
        void connect();
        
        void disconnect();
        
        @Nullable
        Bundle getExtras();
        
        void getItem(@NonNull final String p0, @NonNull final ItemCallback p1);
        
        @NonNull
        String getRoot();
        
        ComponentName getServiceComponent();
        
        @NonNull
        MediaSessionCompat.Token getSessionToken();
        
        boolean isConnected();
        
        void search(@NonNull final String p0, final Bundle p1, @NonNull final SearchCallback p2);
        
        void sendCustomAction(final String p0, final Bundle p1, final CustomActionCallback p2);
        
        void subscribe(@NonNull final String p0, final Bundle p1, @NonNull final SubscriptionCallback p2);
        
        void unsubscribe(@NonNull final String p0, final SubscriptionCallback p1);
    }
    
    @RequiresApi(21)
    static class MediaBrowserImplApi21 implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl, ConnectionCallbackInternal
    {
        protected final Object mBrowserObj;
        protected Messenger mCallbacksMessenger;
        protected final CallbackHandler mHandler;
        private MediaSessionCompat.Token mMediaSessionToken;
        protected final Bundle mRootHints;
        protected ServiceBinderWrapper mServiceBinderWrapper;
        private final ArrayMap<String, Subscription> mSubscriptions;
        
        public MediaBrowserImplApi21(final Context context, final ComponentName componentName, final ConnectionCallback connectionCallback, Bundle mRootHints) {
            this.mHandler = new CallbackHandler(this);
            this.mSubscriptions = new ArrayMap<String, Subscription>();
            if (Build$VERSION.SDK_INT <= 25) {
                Bundle bundle;
                if ((bundle = mRootHints) == null) {
                    bundle = new Bundle();
                }
                bundle.putInt("extra_client_version", 1);
                this.mRootHints = new Bundle(bundle);
            }
            else {
                if (mRootHints == null) {
                    mRootHints = null;
                }
                else {
                    mRootHints = new Bundle(mRootHints);
                }
                this.mRootHints = mRootHints;
            }
            connectionCallback.setInternalConnectionCallback((ConnectionCallbackInternal)this);
            this.mBrowserObj = MediaBrowserCompatApi21.createBrowser(context, componentName, connectionCallback.mConnectionCallbackObj, this.mRootHints);
        }
        
        @Override
        public void connect() {
            MediaBrowserCompatApi21.connect(this.mBrowserObj);
        }
        
        @Override
        public void disconnect() {
            while (true) {
                if (this.mServiceBinderWrapper == null || this.mCallbacksMessenger == null) {
                    break Label_0025;
                }
                try {
                    this.mServiceBinderWrapper.unregisterCallbackMessenger(this.mCallbacksMessenger);
                    MediaBrowserCompatApi21.disconnect(this.mBrowserObj);
                }
                catch (RemoteException ex) {
                    Log.i("MediaBrowserCompat", "Remote error unregistering client messenger.");
                    continue;
                }
                break;
            }
        }
        
        @Nullable
        @Override
        public Bundle getExtras() {
            return MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
        }
        
        @Override
        public void getItem(@NonNull final String s, @NonNull final ItemCallback itemCallback) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("mediaId is empty");
            }
            if (itemCallback == null) {
                throw new IllegalArgumentException("cb is null");
            }
            if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
                Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        itemCallback.onError(s);
                    }
                });
                return;
            }
            if (this.mServiceBinderWrapper == null) {
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        itemCallback.onError(s);
                    }
                });
                return;
            }
            final ItemReceiver itemReceiver = new ItemReceiver(s, itemCallback, this.mHandler);
            try {
                this.mServiceBinderWrapper.getMediaItem(s, itemReceiver, this.mCallbacksMessenger);
            }
            catch (RemoteException ex) {
                Log.i("MediaBrowserCompat", "Remote error getting media item: " + s);
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        itemCallback.onError(s);
                    }
                });
            }
        }
        
        @NonNull
        @Override
        public String getRoot() {
            return MediaBrowserCompatApi21.getRoot(this.mBrowserObj);
        }
        
        @Override
        public ComponentName getServiceComponent() {
            return MediaBrowserCompatApi21.getServiceComponent(this.mBrowserObj);
        }
        
        @NonNull
        @Override
        public MediaSessionCompat.Token getSessionToken() {
            if (this.mMediaSessionToken == null) {
                this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
            }
            return this.mMediaSessionToken;
        }
        
        @Override
        public boolean isConnected() {
            return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
        }
        
        @Override
        public void onConnected() {
            Object o = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
            if (o != null) {
                final IBinder binder = BundleCompat.getBinder((Bundle)o, "extra_messenger");
                while (true) {
                    if (binder == null) {
                        break Label_0077;
                    }
                    this.mServiceBinderWrapper = new ServiceBinderWrapper(binder, this.mRootHints);
                    this.mCallbacksMessenger = new Messenger((Handler)this.mHandler);
                    this.mHandler.setCallbacksMessenger(this.mCallbacksMessenger);
                    try {
                        this.mServiceBinderWrapper.registerCallbackMessenger(this.mCallbacksMessenger);
                        o = IMediaSession.Stub.asInterface(BundleCompat.getBinder((Bundle)o, "extra_session_binder"));
                        if (o != null) {
                            this.mMediaSessionToken = MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj), (IMediaSession)o);
                        }
                    }
                    catch (RemoteException ex) {
                        Log.i("MediaBrowserCompat", "Remote error registering client messenger.");
                        continue;
                    }
                    break;
                }
            }
        }
        
        @Override
        public void onConnectionFailed() {
        }
        
        @Override
        public void onConnectionFailed(final Messenger messenger) {
        }
        
        @Override
        public void onConnectionSuspended() {
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mMediaSessionToken = null;
            this.mHandler.setCallbacksMessenger(null);
        }
        
        @Override
        public void onLoadChildren(final Messenger messenger, final String s, final List list, final Bundle bundle) {
            if (this.mCallbacksMessenger == messenger) {
                final Subscription subscription = this.mSubscriptions.get(s);
                if (subscription == null) {
                    if (MediaBrowserCompat.DEBUG) {
                        Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + s);
                    }
                }
                else {
                    final SubscriptionCallback callback = subscription.getCallback(bundle);
                    if (callback != null) {
                        if (bundle == null) {
                            if (list == null) {
                                callback.onError(s);
                                return;
                            }
                            callback.onChildrenLoaded(s, list);
                        }
                        else {
                            if (list == null) {
                                callback.onError(s, bundle);
                                return;
                            }
                            callback.onChildrenLoaded(s, list, bundle);
                        }
                    }
                }
            }
        }
        
        @Override
        public void onServiceConnected(final Messenger messenger, final String s, final MediaSessionCompat.Token token, final Bundle bundle) {
        }
        
        @Override
        public void search(@NonNull final String s, final Bundle bundle, @NonNull final SearchCallback searchCallback) {
            if (!this.isConnected()) {
                throw new IllegalStateException("search() called while not connected");
            }
            if (this.mServiceBinderWrapper == null) {
                Log.i("MediaBrowserCompat", "The connected service doesn't support search.");
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        searchCallback.onError(s, bundle);
                    }
                });
                return;
            }
            final SearchResultReceiver searchResultReceiver = new SearchResultReceiver(s, bundle, searchCallback, this.mHandler);
            try {
                this.mServiceBinderWrapper.search(s, bundle, searchResultReceiver, this.mCallbacksMessenger);
            }
            catch (RemoteException ex) {
                Log.i("MediaBrowserCompat", "Remote error searching items with query: " + s, (Throwable)ex);
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        searchCallback.onError(s, bundle);
                    }
                });
            }
        }
        
        @Override
        public void sendCustomAction(final String s, final Bundle bundle, final CustomActionCallback customActionCallback) {
            if (!this.isConnected()) {
                throw new IllegalStateException("Cannot send a custom action (" + s + ") with " + "extras " + bundle + " because the browser is not connected to the " + "service.");
            }
            if (this.mServiceBinderWrapper == null) {
                Log.i("MediaBrowserCompat", "The connected service doesn't support sendCustomAction.");
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        customActionCallback.onError(s, bundle, null);
                    }
                });
            }
            final CustomActionResultReceiver customActionResultReceiver = new CustomActionResultReceiver(s, bundle, customActionCallback, this.mHandler);
            try {
                this.mServiceBinderWrapper.sendCustomAction(s, bundle, customActionResultReceiver, this.mCallbacksMessenger);
            }
            catch (RemoteException ex) {
                Log.i("MediaBrowserCompat", "Remote error sending a custom action: action=" + s + ", extras=" + bundle, (Throwable)ex);
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        customActionCallback.onError(s, bundle, null);
                    }
                });
            }
        }
        
        @Override
        public void subscribe(@NonNull final String s, final Bundle bundle, @NonNull final SubscriptionCallback subscriptionCallback) {
            Subscription subscription;
            if ((subscription = this.mSubscriptions.get(s)) == null) {
                subscription = new Subscription();
                this.mSubscriptions.put(s, subscription);
            }
            subscriptionCallback.setSubscription(subscription);
            Bundle bundle2;
            if (bundle == null) {
                bundle2 = null;
            }
            else {
                bundle2 = new Bundle(bundle);
            }
            subscription.putCallback(bundle2, subscriptionCallback);
            if (this.mServiceBinderWrapper == null) {
                MediaBrowserCompatApi21.subscribe(this.mBrowserObj, s, subscriptionCallback.mSubscriptionCallbackObj);
                return;
            }
            try {
                this.mServiceBinderWrapper.addSubscription(s, subscriptionCallback.mToken, bundle2, this.mCallbacksMessenger);
            }
            catch (RemoteException ex) {
                Log.i("MediaBrowserCompat", "Remote error subscribing media item: " + s);
            }
        }
        
        @Override
        public void unsubscribe(@NonNull final String s, final SubscriptionCallback subscriptionCallback) {
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription != null) {
                if (this.mServiceBinderWrapper == null) {
                    if (subscriptionCallback == null) {
                        MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, s);
                    }
                    else {
                        final List<SubscriptionCallback> callbacks = subscription.getCallbacks();
                        final List<Bundle> optionsList = subscription.getOptionsList();
                        for (int i = callbacks.size() - 1; i >= 0; --i) {
                            if (callbacks.get(i) == subscriptionCallback) {
                                callbacks.remove(i);
                                optionsList.remove(i);
                            }
                        }
                        if (callbacks.size() == 0) {
                            MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, s);
                        }
                    }
                }
                else if (subscriptionCallback == null) {
                    try {
                        this.mServiceBinderWrapper.removeSubscription(s, null, this.mCallbacksMessenger);
                    }
                    catch (RemoteException ex) {
                        Log.d("MediaBrowserCompat", "removeSubscription failed with RemoteException parentId=" + s);
                    }
                }
                else {
                    final List<SubscriptionCallback> callbacks2 = subscription.getCallbacks();
                    final List<Bundle> optionsList2 = subscription.getOptionsList();
                    for (int j = callbacks2.size() - 1; j >= 0; --j) {
                        if (callbacks2.get(j) == subscriptionCallback) {
                            this.mServiceBinderWrapper.removeSubscription(s, subscriptionCallback.mToken, this.mCallbacksMessenger);
                            callbacks2.remove(j);
                            optionsList2.remove(j);
                        }
                    }
                }
                if (subscription.isEmpty() || subscriptionCallback == null) {
                    this.mSubscriptions.remove(s);
                }
            }
        }
    }
    
    @RequiresApi(23)
    static class MediaBrowserImplApi23 extends MediaBrowserImplApi21
    {
        public MediaBrowserImplApi23(final Context context, final ComponentName componentName, final ConnectionCallback connectionCallback, final Bundle bundle) {
            super(context, componentName, connectionCallback, bundle);
        }
        
        @Override
        public void getItem(@NonNull final String s, @NonNull final ItemCallback itemCallback) {
            if (this.mServiceBinderWrapper == null) {
                MediaBrowserCompatApi23.getItem(this.mBrowserObj, s, itemCallback.mItemCallbackObj);
                return;
            }
            super.getItem(s, itemCallback);
        }
    }
    
    @RequiresApi(26)
    static class MediaBrowserImplApi24 extends MediaBrowserImplApi23
    {
        public MediaBrowserImplApi24(final Context context, final ComponentName componentName, final ConnectionCallback connectionCallback, final Bundle bundle) {
            super(context, componentName, connectionCallback, bundle);
        }
        
        @Override
        public void subscribe(@NonNull final String s, @NonNull final Bundle bundle, @NonNull final SubscriptionCallback subscriptionCallback) {
            if (bundle == null) {
                MediaBrowserCompatApi21.subscribe(this.mBrowserObj, s, subscriptionCallback.mSubscriptionCallbackObj);
                return;
            }
            MediaBrowserCompatApi24.subscribe(this.mBrowserObj, s, bundle, subscriptionCallback.mSubscriptionCallbackObj);
        }
        
        @Override
        public void unsubscribe(@NonNull final String s, final SubscriptionCallback subscriptionCallback) {
            if (subscriptionCallback == null) {
                MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, s);
                return;
            }
            MediaBrowserCompatApi24.unsubscribe(this.mBrowserObj, s, subscriptionCallback.mSubscriptionCallbackObj);
        }
    }
    
    static class MediaBrowserImplBase implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl
    {
        static final int CONNECT_STATE_CONNECTED = 3;
        static final int CONNECT_STATE_CONNECTING = 2;
        static final int CONNECT_STATE_DISCONNECTED = 1;
        static final int CONNECT_STATE_DISCONNECTING = 0;
        static final int CONNECT_STATE_SUSPENDED = 4;
        final ConnectionCallback mCallback;
        Messenger mCallbacksMessenger;
        final Context mContext;
        private Bundle mExtras;
        final CallbackHandler mHandler;
        private MediaSessionCompat.Token mMediaSessionToken;
        final Bundle mRootHints;
        private String mRootId;
        ServiceBinderWrapper mServiceBinderWrapper;
        final ComponentName mServiceComponent;
        MediaServiceConnection mServiceConnection;
        int mState;
        private final ArrayMap<String, Subscription> mSubscriptions;
        
        public MediaBrowserImplBase(final Context mContext, final ComponentName mServiceComponent, final ConnectionCallback mCallback, final Bundle bundle) {
            this.mHandler = new CallbackHandler(this);
            this.mSubscriptions = new ArrayMap<String, Subscription>();
            this.mState = 1;
            if (mContext == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            if (mServiceComponent == null) {
                throw new IllegalArgumentException("service component must not be null");
            }
            if (mCallback == null) {
                throw new IllegalArgumentException("connection callback must not be null");
            }
            this.mContext = mContext;
            this.mServiceComponent = mServiceComponent;
            this.mCallback = mCallback;
            Bundle mRootHints;
            if (bundle == null) {
                mRootHints = null;
            }
            else {
                mRootHints = new Bundle(bundle);
            }
            this.mRootHints = mRootHints;
        }
        
        private static String getStateLabel(final int n) {
            switch (n) {
                default: {
                    return "UNKNOWN/" + n;
                }
                case 0: {
                    return "CONNECT_STATE_DISCONNECTING";
                }
                case 1: {
                    return "CONNECT_STATE_DISCONNECTED";
                }
                case 2: {
                    return "CONNECT_STATE_CONNECTING";
                }
                case 3: {
                    return "CONNECT_STATE_CONNECTED";
                }
                case 4: {
                    return "CONNECT_STATE_SUSPENDED";
                }
            }
        }
        
        private boolean isCurrent(final Messenger messenger, final String s) {
            boolean b = true;
            if (this.mCallbacksMessenger != messenger) {
                if (this.mState != 1) {
                    Log.i("MediaBrowserCompat", s + " for " + this.mServiceComponent + " with mCallbacksMessenger=" + this.mCallbacksMessenger + " this=" + this);
                }
                b = false;
            }
            return b;
        }
        
        @Override
        public void connect() {
            if (this.mState != 1) {
                throw new IllegalStateException("connect() called while not disconnected (state=" + getStateLabel(this.mState) + ")");
            }
            if (MediaBrowserCompat.DEBUG && this.mServiceConnection != null) {
                throw new RuntimeException("mServiceConnection should be null. Instead it is " + this.mServiceConnection);
            }
            if (this.mServiceBinderWrapper != null) {
                throw new RuntimeException("mServiceBinderWrapper should be null. Instead it is " + this.mServiceBinderWrapper);
            }
            if (this.mCallbacksMessenger != null) {
                throw new RuntimeException("mCallbacksMessenger should be null. Instead it is " + this.mCallbacksMessenger);
            }
            this.mState = 2;
            final Intent intent = new Intent("android.media.browse.MediaBrowserService");
            intent.setComponent(this.mServiceComponent);
            final MediaServiceConnection mServiceConnection = new MediaServiceConnection();
            this.mServiceConnection = mServiceConnection;
            int bindService = 0;
            while (true) {
                try {
                    bindService = (this.mContext.bindService(intent, (ServiceConnection)this.mServiceConnection, 1) ? 1 : 0);
                    if (bindService == 0) {
                        this.mHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                if (mServiceConnection == MediaBrowserImplBase.this.mServiceConnection) {
                                    MediaBrowserImplBase.this.forceCloseConnection();
                                    MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                                }
                            }
                        });
                    }
                    if (MediaBrowserCompat.DEBUG) {
                        Log.d("MediaBrowserCompat", "connect...");
                        this.dump();
                    }
                }
                catch (Exception ex) {
                    Log.e("MediaBrowserCompat", "Failed binding to service " + this.mServiceComponent);
                    continue;
                }
                break;
            }
        }
        
        @Override
        public void disconnect() {
            this.mState = 0;
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (MediaBrowserImplBase.this.mCallbacksMessenger == null) {
                            break Label_0027;
                        }
                        try {
                            MediaBrowserImplBase.this.mServiceBinderWrapper.disconnect(MediaBrowserImplBase.this.mCallbacksMessenger);
                            MediaBrowserImplBase.this.forceCloseConnection();
                            if (MediaBrowserCompat.DEBUG) {
                                Log.d("MediaBrowserCompat", "disconnect...");
                                MediaBrowserImplBase.this.dump();
                            }
                        }
                        catch (RemoteException ex) {
                            Log.w("MediaBrowserCompat", "RemoteException during connect for " + MediaBrowserImplBase.this.mServiceComponent);
                            continue;
                        }
                        break;
                    }
                }
            });
        }
        
        void dump() {
            Log.d("MediaBrowserCompat", "MediaBrowserCompat...");
            Log.d("MediaBrowserCompat", "  mServiceComponent=" + this.mServiceComponent);
            Log.d("MediaBrowserCompat", "  mCallback=" + this.mCallback);
            Log.d("MediaBrowserCompat", "  mRootHints=" + this.mRootHints);
            Log.d("MediaBrowserCompat", "  mState=" + getStateLabel(this.mState));
            Log.d("MediaBrowserCompat", "  mServiceConnection=" + this.mServiceConnection);
            Log.d("MediaBrowserCompat", "  mServiceBinderWrapper=" + this.mServiceBinderWrapper);
            Log.d("MediaBrowserCompat", "  mCallbacksMessenger=" + this.mCallbacksMessenger);
            Log.d("MediaBrowserCompat", "  mRootId=" + this.mRootId);
            Log.d("MediaBrowserCompat", "  mMediaSessionToken=" + this.mMediaSessionToken);
        }
        
        void forceCloseConnection() {
            if (this.mServiceConnection != null) {
                this.mContext.unbindService((ServiceConnection)this.mServiceConnection);
            }
            this.mState = 1;
            this.mServiceConnection = null;
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mHandler.setCallbacksMessenger(null);
            this.mRootId = null;
            this.mMediaSessionToken = null;
        }
        
        @Nullable
        @Override
        public Bundle getExtras() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getExtras() called while not connected (state=" + getStateLabel(this.mState) + ")");
            }
            return this.mExtras;
        }
        
        @Override
        public void getItem(@NonNull final String s, @NonNull final ItemCallback itemCallback) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("mediaId is empty");
            }
            if (itemCallback == null) {
                throw new IllegalArgumentException("cb is null");
            }
            if (!this.isConnected()) {
                Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        itemCallback.onError(s);
                    }
                });
                return;
            }
            final ItemReceiver itemReceiver = new ItemReceiver(s, itemCallback, this.mHandler);
            try {
                this.mServiceBinderWrapper.getMediaItem(s, itemReceiver, this.mCallbacksMessenger);
            }
            catch (RemoteException ex) {
                Log.i("MediaBrowserCompat", "Remote error getting media item: " + s);
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        itemCallback.onError(s);
                    }
                });
            }
        }
        
        @NonNull
        @Override
        public String getRoot() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getRoot() called while not connected(state=" + getStateLabel(this.mState) + ")");
            }
            return this.mRootId;
        }
        
        @NonNull
        @Override
        public ComponentName getServiceComponent() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getServiceComponent() called while not connected (state=" + this.mState + ")");
            }
            return this.mServiceComponent;
        }
        
        @NonNull
        @Override
        public MediaSessionCompat.Token getSessionToken() {
            if (!this.isConnected()) {
                throw new IllegalStateException("getSessionToken() called while not connected(state=" + this.mState + ")");
            }
            return this.mMediaSessionToken;
        }
        
        @Override
        public boolean isConnected() {
            return this.mState == 3;
        }
        
        @Override
        public void onConnectionFailed(final Messenger messenger) {
            Log.e("MediaBrowserCompat", "onConnectFailed for " + this.mServiceComponent);
            if (!this.isCurrent(messenger, "onConnectFailed")) {
                return;
            }
            if (this.mState != 2) {
                Log.w("MediaBrowserCompat", "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
                return;
            }
            this.forceCloseConnection();
            this.mCallback.onConnectionFailed();
        }
        
        @Override
        public void onLoadChildren(final Messenger messenger, final String s, final List list, final Bundle bundle) {
            if (this.isCurrent(messenger, "onLoadChildren")) {
                if (MediaBrowserCompat.DEBUG) {
                    Log.d("MediaBrowserCompat", "onLoadChildren for " + this.mServiceComponent + " id=" + s);
                }
                final Subscription subscription = this.mSubscriptions.get(s);
                if (subscription == null) {
                    if (MediaBrowserCompat.DEBUG) {
                        Log.d("MediaBrowserCompat", "onLoadChildren for id that isn't subscribed id=" + s);
                    }
                }
                else {
                    final SubscriptionCallback callback = subscription.getCallback(bundle);
                    if (callback != null) {
                        if (bundle == null) {
                            if (list == null) {
                                callback.onError(s);
                                return;
                            }
                            callback.onChildrenLoaded(s, list);
                        }
                        else {
                            if (list == null) {
                                callback.onError(s, bundle);
                                return;
                            }
                            callback.onChildrenLoaded(s, list, bundle);
                        }
                    }
                }
            }
        }
        
        @Override
        public void onServiceConnected(final Messenger messenger, String mRootId, final MediaSessionCompat.Token mMediaSessionToken, final Bundle mExtras) {
            if (this.isCurrent(messenger, "onConnect")) {
                if (this.mState != 2) {
                    Log.w("MediaBrowserCompat", "onConnect from service while mState=" + getStateLabel(this.mState) + "... ignoring");
                    return;
                }
                this.mRootId = mRootId;
                this.mMediaSessionToken = mMediaSessionToken;
                this.mExtras = mExtras;
                this.mState = 3;
                if (MediaBrowserCompat.DEBUG) {
                    Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                    this.dump();
                }
                this.mCallback.onConnected();
                try {
                    for (final Map.Entry<String, Subscription> entry : this.mSubscriptions.entrySet()) {
                        mRootId = entry.getKey();
                        final Subscription subscription = entry.getValue();
                        final List<SubscriptionCallback> callbacks = subscription.getCallbacks();
                        final List<Bundle> optionsList = subscription.getOptionsList();
                        for (int i = 0; i < callbacks.size(); ++i) {
                            this.mServiceBinderWrapper.addSubscription(mRootId, callbacks.get(i).mToken, optionsList.get(i), this.mCallbacksMessenger);
                        }
                    }
                }
                catch (RemoteException ex) {
                    Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException.");
                }
            }
        }
        
        @Override
        public void search(@NonNull final String s, final Bundle bundle, @NonNull final SearchCallback searchCallback) {
            if (!this.isConnected()) {
                throw new IllegalStateException("search() called while not connected (state=" + getStateLabel(this.mState) + ")");
            }
            final SearchResultReceiver searchResultReceiver = new SearchResultReceiver(s, bundle, searchCallback, this.mHandler);
            try {
                this.mServiceBinderWrapper.search(s, bundle, searchResultReceiver, this.mCallbacksMessenger);
            }
            catch (RemoteException ex) {
                Log.i("MediaBrowserCompat", "Remote error searching items with query: " + s, (Throwable)ex);
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        searchCallback.onError(s, bundle);
                    }
                });
            }
        }
        
        @Override
        public void sendCustomAction(@NonNull final String s, final Bundle bundle, @Nullable final CustomActionCallback customActionCallback) {
            if (!this.isConnected()) {
                throw new IllegalStateException("Cannot send a custom action (" + s + ") with " + "extras " + bundle + " because the browser is not connected to the " + "service.");
            }
            final CustomActionResultReceiver customActionResultReceiver = new CustomActionResultReceiver(s, bundle, customActionCallback, this.mHandler);
            try {
                this.mServiceBinderWrapper.sendCustomAction(s, bundle, customActionResultReceiver, this.mCallbacksMessenger);
            }
            catch (RemoteException ex) {
                Log.i("MediaBrowserCompat", "Remote error sending a custom action: action=" + s + ", extras=" + bundle, (Throwable)ex);
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        customActionCallback.onError(s, bundle, null);
                    }
                });
            }
        }
        
        @Override
        public void subscribe(@NonNull final String s, final Bundle bundle, @NonNull final SubscriptionCallback subscriptionCallback) {
            Subscription subscription;
            if ((subscription = this.mSubscriptions.get(s)) == null) {
                subscription = new Subscription();
                this.mSubscriptions.put(s, subscription);
            }
            Label_0080: {
                if (bundle != null) {
                    break Label_0080;
                }
                Bundle bundle2 = null;
                while (true) {
                    subscription.putCallback(bundle2, subscriptionCallback);
                    if (!this.isConnected()) {
                        return;
                    }
                    try {
                        this.mServiceBinderWrapper.addSubscription(s, subscriptionCallback.mToken, bundle2, this.mCallbacksMessenger);
                        return;
                        bundle2 = new Bundle(bundle);
                        continue;
                    }
                    catch (RemoteException ex) {
                        Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException parentId=" + s);
                    }
                    break;
                }
            }
        }
        
        @Override
        public void unsubscribe(@NonNull final String s, final SubscriptionCallback subscriptionCallback) {
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription != null) {
                Label_0065: {
                    if (subscriptionCallback != null) {
                        break Label_0065;
                    }
                    List<SubscriptionCallback> callbacks;
                    List<Bundle> optionsList;
                    int n;
                    Label_0089_Outer:Block_6_Outer:Label_0146_Outer:
                    while (true) {
                        try {
                            if (this.isConnected()) {
                                this.mServiceBinderWrapper.removeSubscription(s, null, this.mCallbacksMessenger);
                            }
                            if (subscription.isEmpty() || subscriptionCallback == null) {
                                this.mSubscriptions.remove(s);
                                return;
                            }
                            return;
                            // iftrue(Label_0043:, n < 0)
                            // iftrue(Label_0128:, !this.isConnected())
                            // iftrue(Label_0146:, callbacks.get(n) != subscriptionCallback)
                            while (true) {
                                Label_0128: {
                                    while (true) {
                                    Block_5:
                                        while (true) {
                                            while (true) {
                                                this.mServiceBinderWrapper.removeSubscription(s, subscriptionCallback.mToken, this.mCallbacksMessenger);
                                                break Label_0128;
                                                callbacks = subscription.getCallbacks();
                                                optionsList = subscription.getOptionsList();
                                                n = callbacks.size() - 1;
                                                break Block_5;
                                                continue Label_0089_Outer;
                                            }
                                            --n;
                                            continue Block_6_Outer;
                                        }
                                        continue Label_0146_Outer;
                                    }
                                }
                                callbacks.remove(n);
                                optionsList.remove(n);
                                continue;
                            }
                        }
                        catch (RemoteException ex) {
                            Log.d("MediaBrowserCompat", "removeSubscription failed with RemoteException parentId=" + s);
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private class MediaServiceConnection implements ServiceConnection
        {
            private void postOrRun(final Runnable runnable) {
                if (Thread.currentThread() == MediaBrowserImplBase.this.mHandler.getLooper().getThread()) {
                    runnable.run();
                    return;
                }
                MediaBrowserImplBase.this.mHandler.post(runnable);
            }
            
            boolean isCurrent(final String s) {
                boolean b = true;
                if (MediaBrowserImplBase.this.mServiceConnection != this) {
                    if (MediaBrowserImplBase.this.mState != 1) {
                        Log.i("MediaBrowserCompat", s + " for " + MediaBrowserImplBase.this.mServiceComponent + " with mServiceConnection=" + MediaBrowserImplBase.this.mServiceConnection + " this=" + this);
                    }
                    b = false;
                }
                return b;
            }
            
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                this.postOrRun(new Runnable() {
                    @Override
                    public void run() {
                        if (MediaBrowserCompat.DEBUG) {
                            Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceConnected name=" + componentName + " binder=" + binder);
                            MediaBrowserImplBase.this.dump();
                        }
                        if (MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                            MediaBrowserImplBase.this.mServiceBinderWrapper = new ServiceBinderWrapper(binder, MediaBrowserImplBase.this.mRootHints);
                            MediaBrowserImplBase.this.mCallbacksMessenger = new Messenger((Handler)MediaBrowserImplBase.this.mHandler);
                            MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserImplBase.this.mCallbacksMessenger);
                            MediaBrowserImplBase.this.mState = 2;
                            try {
                                if (MediaBrowserCompat.DEBUG) {
                                    Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                                    MediaBrowserImplBase.this.dump();
                                }
                                MediaBrowserImplBase.this.mServiceBinderWrapper.connect(MediaBrowserImplBase.this.mContext, MediaBrowserImplBase.this.mCallbacksMessenger);
                            }
                            catch (RemoteException ex) {
                                Log.w("MediaBrowserCompat", "RemoteException during connect for " + MediaBrowserImplBase.this.mServiceComponent);
                                if (MediaBrowserCompat.DEBUG) {
                                    Log.d("MediaBrowserCompat", "ServiceCallbacks.onConnect...");
                                    MediaBrowserImplBase.this.dump();
                                }
                            }
                        }
                    }
                });
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                this.postOrRun(new Runnable() {
                    @Override
                    public void run() {
                        if (MediaBrowserCompat.DEBUG) {
                            Log.d("MediaBrowserCompat", "MediaServiceConnection.onServiceDisconnected name=" + componentName + " this=" + this + " mServiceConnection=" + MediaBrowserImplBase.this.mServiceConnection);
                            MediaBrowserImplBase.this.dump();
                        }
                        if (!MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                            return;
                        }
                        MediaBrowserImplBase.this.mServiceBinderWrapper = null;
                        MediaBrowserImplBase.this.mCallbacksMessenger = null;
                        MediaBrowserImplBase.this.mHandler.setCallbacksMessenger(null);
                        MediaBrowserImplBase.this.mState = 4;
                        MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
                    }
                });
            }
        }
    }
    
    interface MediaBrowserServiceCallbackImpl
    {
        void onConnectionFailed(final Messenger p0);
        
        void onLoadChildren(final Messenger p0, final String p1, final List p2, final Bundle p3);
        
        void onServiceConnected(final Messenger p0, final String p1, final MediaSessionCompat.Token p2, final Bundle p3);
    }
    
    public static class MediaItem implements Parcelable
    {
        public static final Parcelable$Creator<MediaItem> CREATOR;
        public static final int FLAG_BROWSABLE = 1;
        public static final int FLAG_PLAYABLE = 2;
        private final MediaDescriptionCompat mDescription;
        private final int mFlags;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<MediaItem>() {
                public MediaItem createFromParcel(final Parcel parcel) {
                    return new MediaItem(parcel);
                }
                
                public MediaItem[] newArray(final int n) {
                    return new MediaItem[n];
                }
            };
        }
        
        MediaItem(final Parcel parcel) {
            this.mFlags = parcel.readInt();
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
        }
        
        public MediaItem(@NonNull final MediaDescriptionCompat mDescription, final int mFlags) {
            if (mDescription == null) {
                throw new IllegalArgumentException("description cannot be null");
            }
            if (TextUtils.isEmpty((CharSequence)mDescription.getMediaId())) {
                throw new IllegalArgumentException("description must have a non-empty media id");
            }
            this.mFlags = mFlags;
            this.mDescription = mDescription;
        }
        
        public static MediaItem fromMediaItem(final Object o) {
            if (o == null || Build$VERSION.SDK_INT < 21) {
                return null;
            }
            return new MediaItem(MediaDescriptionCompat.fromMediaDescription(MediaBrowserCompatApi21.MediaItem.getDescription(o)), MediaBrowserCompatApi21.MediaItem.getFlags(o));
        }
        
        public static List<MediaItem> fromMediaItemList(final List<?> list) {
            List<MediaItem> list2;
            if (list == null || Build$VERSION.SDK_INT < 21) {
                list2 = null;
            }
            else {
                final ArrayList<MediaItem> list3 = new ArrayList<MediaItem>(list.size());
                final Iterator<?> iterator = list.iterator();
                while (true) {
                    list2 = list3;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    list3.add(fromMediaItem(iterator.next()));
                }
            }
            return list2;
        }
        
        public int describeContents() {
            return 0;
        }
        
        @NonNull
        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }
        
        public int getFlags() {
            return this.mFlags;
        }
        
        @Nullable
        public String getMediaId() {
            return this.mDescription.getMediaId();
        }
        
        public boolean isBrowsable() {
            return (this.mFlags & 0x1) != 0x0;
        }
        
        public boolean isPlayable() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MediaItem{");
            sb.append("mFlags=").append(this.mFlags);
            sb.append(", mDescription=").append(this.mDescription);
            sb.append('}');
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.mFlags);
            this.mDescription.writeToParcel(parcel, n);
        }
        
        @Retention(RetentionPolicy.SOURCE)
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public @interface Flags {
        }
    }
    
    public abstract static class SearchCallback
    {
        public void onError(@NonNull final String s, final Bundle bundle) {
        }
        
        public void onSearchResult(@NonNull final String s, final Bundle bundle, @NonNull final List<MediaItem> list) {
        }
    }
    
    private static class SearchResultReceiver extends ResultReceiver
    {
        private final SearchCallback mCallback;
        private final Bundle mExtras;
        private final String mQuery;
        
        SearchResultReceiver(final String mQuery, final Bundle mExtras, final SearchCallback mCallback, final Handler handler) {
            super(handler);
            this.mQuery = mQuery;
            this.mExtras = mExtras;
            this.mCallback = mCallback;
        }
        
        @Override
        protected void onReceiveResult(int n, final Bundle bundle) {
            if (bundle != null) {
                bundle.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            }
            if (n != 0 || bundle == null || !bundle.containsKey("search_results")) {
                this.mCallback.onError(this.mQuery, this.mExtras);
                return;
            }
            final Parcelable[] parcelableArray = bundle.getParcelableArray("search_results");
            List<MediaItem> list = null;
            if (parcelableArray != null) {
                final ArrayList<MediaItem> list2 = new ArrayList<MediaItem>();
                final int length = parcelableArray.length;
                n = 0;
                while (true) {
                    list = list2;
                    if (n >= length) {
                        break;
                    }
                    list2.add((MediaItem)parcelableArray[n]);
                    ++n;
                }
            }
            this.mCallback.onSearchResult(this.mQuery, this.mExtras, list);
        }
    }
    
    private static class ServiceBinderWrapper
    {
        private Messenger mMessenger;
        private Bundle mRootHints;
        
        public ServiceBinderWrapper(final IBinder binder, final Bundle mRootHints) {
            this.mMessenger = new Messenger(binder);
            this.mRootHints = mRootHints;
        }
        
        private void sendRequest(final int what, final Bundle data, final Messenger replyTo) throws RemoteException {
            final Message obtain = Message.obtain();
            obtain.what = what;
            obtain.arg1 = 1;
            obtain.setData(data);
            obtain.replyTo = replyTo;
            this.mMessenger.send(obtain);
        }
        
        void addSubscription(final String s, final IBinder binder, final Bundle bundle, final Messenger messenger) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", s);
            BundleCompat.putBinder(bundle2, "data_callback_token", binder);
            bundle2.putBundle("data_options", bundle);
            this.sendRequest(3, bundle2, messenger);
        }
        
        void connect(final Context context, final Messenger messenger) throws RemoteException {
            final Bundle bundle = new Bundle();
            bundle.putString("data_package_name", context.getPackageName());
            bundle.putBundle("data_root_hints", this.mRootHints);
            this.sendRequest(1, bundle, messenger);
        }
        
        void disconnect(final Messenger messenger) throws RemoteException {
            this.sendRequest(2, null, messenger);
        }
        
        void getMediaItem(final String s, final ResultReceiver resultReceiver, final Messenger messenger) throws RemoteException {
            final Bundle bundle = new Bundle();
            bundle.putString("data_media_item_id", s);
            bundle.putParcelable("data_result_receiver", (Parcelable)resultReceiver);
            this.sendRequest(5, bundle, messenger);
        }
        
        void registerCallbackMessenger(final Messenger messenger) throws RemoteException {
            final Bundle bundle = new Bundle();
            bundle.putBundle("data_root_hints", this.mRootHints);
            this.sendRequest(6, bundle, messenger);
        }
        
        void removeSubscription(final String s, final IBinder binder, final Messenger messenger) throws RemoteException {
            final Bundle bundle = new Bundle();
            bundle.putString("data_media_item_id", s);
            BundleCompat.putBinder(bundle, "data_callback_token", binder);
            this.sendRequest(4, bundle, messenger);
        }
        
        void search(final String s, final Bundle bundle, final ResultReceiver resultReceiver, final Messenger messenger) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_search_query", s);
            bundle2.putBundle("data_search_extras", bundle);
            bundle2.putParcelable("data_result_receiver", (Parcelable)resultReceiver);
            this.sendRequest(8, bundle2, messenger);
        }
        
        void sendCustomAction(final String s, final Bundle bundle, final ResultReceiver resultReceiver, final Messenger messenger) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_custom_action", s);
            bundle2.putBundle("data_custom_action_extras", bundle);
            bundle2.putParcelable("data_result_receiver", (Parcelable)resultReceiver);
            this.sendRequest(9, bundle2, messenger);
        }
        
        void unregisterCallbackMessenger(final Messenger messenger) throws RemoteException {
            this.sendRequest(7, null, messenger);
        }
    }
    
    private static class Subscription
    {
        private final List<SubscriptionCallback> mCallbacks;
        private final List<Bundle> mOptionsList;
        
        public Subscription() {
            this.mCallbacks = new ArrayList<SubscriptionCallback>();
            this.mOptionsList = new ArrayList<Bundle>();
        }
        
        public SubscriptionCallback getCallback(final Bundle bundle) {
            for (int i = 0; i < this.mOptionsList.size(); ++i) {
                if (MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(i), bundle)) {
                    return this.mCallbacks.get(i);
                }
            }
            return null;
        }
        
        public List<SubscriptionCallback> getCallbacks() {
            return this.mCallbacks;
        }
        
        public List<Bundle> getOptionsList() {
            return this.mOptionsList;
        }
        
        public boolean isEmpty() {
            return this.mCallbacks.isEmpty();
        }
        
        public void putCallback(final Bundle bundle, final SubscriptionCallback subscriptionCallback) {
            for (int i = 0; i < this.mOptionsList.size(); ++i) {
                if (MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(i), bundle)) {
                    this.mCallbacks.set(i, subscriptionCallback);
                    return;
                }
            }
            this.mCallbacks.add(subscriptionCallback);
            this.mOptionsList.add(bundle);
        }
    }
    
    public abstract static class SubscriptionCallback
    {
        private final Object mSubscriptionCallbackObj;
        WeakReference<Subscription> mSubscriptionRef;
        private final IBinder mToken;
        
        public SubscriptionCallback() {
            if (BuildCompat.isAtLeastO()) {
                this.mSubscriptionCallbackObj = MediaBrowserCompatApi24.createSubscriptionCallback((MediaBrowserCompatApi24.SubscriptionCallback)new StubApi24());
                this.mToken = null;
                return;
            }
            if (Build$VERSION.SDK_INT >= 21) {
                this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback((MediaBrowserCompatApi21.SubscriptionCallback)new StubApi21());
                this.mToken = (IBinder)new Binder();
                return;
            }
            this.mSubscriptionCallbackObj = null;
            this.mToken = (IBinder)new Binder();
        }
        
        private void setSubscription(final Subscription subscription) {
            this.mSubscriptionRef = new WeakReference<Subscription>(subscription);
        }
        
        public void onChildrenLoaded(@NonNull final String s, @NonNull final List<MediaItem> list) {
        }
        
        public void onChildrenLoaded(@NonNull final String s, @NonNull final List<MediaItem> list, @NonNull final Bundle bundle) {
        }
        
        public void onError(@NonNull final String s) {
        }
        
        public void onError(@NonNull final String s, @NonNull final Bundle bundle) {
        }
        
        private class StubApi21 implements MediaBrowserCompatApi21.SubscriptionCallback
        {
            List<MediaBrowserCompat.MediaItem> applyOptions(final List<MediaBrowserCompat.MediaItem> list, final Bundle bundle) {
                List<MediaBrowserCompat.MediaItem> list2;
                if (list == null) {
                    list2 = null;
                }
                else {
                    final int int1 = bundle.getInt("android.media.browse.extra.PAGE", -1);
                    final int int2 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
                    if (int1 == -1) {
                        list2 = list;
                        if (int2 == -1) {
                            return list2;
                        }
                    }
                    final int n = int2 * int1;
                    final int n2 = n + int2;
                    if (int1 < 0 || int2 < 1 || n >= list.size()) {
                        return (List<MediaBrowserCompat.MediaItem>)Collections.EMPTY_LIST;
                    }
                    int size;
                    if ((size = n2) > list.size()) {
                        size = list.size();
                    }
                    return list.subList(n, size);
                }
                return list2;
            }
            
            @Override
            public void onChildrenLoaded(@NonNull final String s, final List<?> list) {
                Object o;
                if (MediaBrowserCompat.SubscriptionCallback.this.mSubscriptionRef == null) {
                    o = null;
                }
                else {
                    o = MediaBrowserCompat.SubscriptionCallback.this.mSubscriptionRef.get();
                }
                if (o == null) {
                    MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(s, MediaBrowserCompat.MediaItem.fromMediaItemList(list));
                }
                else {
                    final List<MediaBrowserCompat.MediaItem> fromMediaItemList = MediaBrowserCompat.MediaItem.fromMediaItemList(list);
                    final List<MediaBrowserCompat.SubscriptionCallback> callbacks = ((Subscription)o).getCallbacks();
                    final List<Bundle> optionsList = ((Subscription)o).getOptionsList();
                    for (int i = 0; i < callbacks.size(); ++i) {
                        final Bundle bundle = optionsList.get(i);
                        if (bundle == null) {
                            MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(s, fromMediaItemList);
                        }
                        else {
                            MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(s, this.applyOptions(fromMediaItemList, bundle), bundle);
                        }
                    }
                }
            }
            
            @Override
            public void onError(@NonNull final String s) {
                MediaBrowserCompat.SubscriptionCallback.this.onError(s);
            }
        }
        
        private class StubApi24 extends StubApi21 implements MediaBrowserCompatApi24.SubscriptionCallback
        {
            @Override
            public void onChildrenLoaded(@NonNull final String s, final List<?> list, @NonNull final Bundle bundle) {
                MediaBrowserCompat.SubscriptionCallback.this.onChildrenLoaded(s, MediaBrowserCompat.MediaItem.fromMediaItemList(list), bundle);
            }
            
            @Override
            public void onError(@NonNull final String s, @NonNull final Bundle bundle) {
                MediaBrowserCompat.SubscriptionCallback.this.onError(s, bundle);
            }
        }
    }
}
