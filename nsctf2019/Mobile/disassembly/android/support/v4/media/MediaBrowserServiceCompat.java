// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.os.Binder;
import java.util.Collection;
import android.os.Message;
import android.text.TextUtils;
import android.os.Parcel;
import android.support.v4.media.session.IMediaSession;
import android.support.v4.app.BundleCompat;
import android.os.Handler;
import android.content.Context;
import android.os.Messenger;
import android.support.annotation.RequiresApi;
import java.util.HashMap;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.os.ResultReceiver;
import android.os.Build$VERSION;
import android.support.v4.os.BuildCompat;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import android.support.v4.util.Pair;
import java.util.List;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.media.session.MediaSessionCompat;
import android.os.IBinder;
import android.support.v4.util.ArrayMap;
import android.support.annotation.RestrictTo;
import android.app.Service;

public abstract class MediaBrowserServiceCompat extends Service
{
    static final boolean DEBUG;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final String KEY_MEDIA_ITEM = "media_item";
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final String KEY_SEARCH_RESULTS = "search_results";
    static final int RESULT_ERROR = -1;
    static final int RESULT_FLAG_ON_LOAD_ITEM_NOT_IMPLEMENTED = 2;
    static final int RESULT_FLAG_ON_SEARCH_NOT_IMPLEMENTED = 4;
    static final int RESULT_FLAG_OPTION_NOT_HANDLED = 1;
    static final int RESULT_OK = 0;
    static final int RESULT_PROGRESS_UPDATE = 1;
    public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
    static final String TAG = "MBServiceCompat";
    final ArrayMap<IBinder, ConnectionRecord> mConnections;
    ConnectionRecord mCurConnection;
    final ServiceHandler mHandler;
    private MediaBrowserServiceImpl mImpl;
    MediaSessionCompat.Token mSession;
    
    static {
        DEBUG = Log.isLoggable("MBServiceCompat", 3);
    }
    
    public MediaBrowserServiceCompat() {
        this.mConnections = new ArrayMap<IBinder, ConnectionRecord>();
        this.mHandler = new ServiceHandler();
    }
    
    void addSubscription(final String s, final ConnectionRecord connectionRecord, final IBinder binder, final Bundle bundle) {
        List<Pair<IBinder, Bundle>> list;
        if ((list = connectionRecord.subscriptions.get(s)) == null) {
            list = new ArrayList<Pair<IBinder, Bundle>>();
        }
        for (final Pair<IBinder, Bundle> pair : list) {
            if (binder == pair.first && MediaBrowserCompatUtils.areSameOptions(bundle, pair.second)) {
                return;
            }
        }
        list.add(new Pair<IBinder, Bundle>(binder, bundle));
        connectionRecord.subscriptions.put(s, list);
        this.performLoadChildren(s, connectionRecord, bundle);
    }
    
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
    
    public void dump(final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
    }
    
    public final Bundle getBrowserRootHints() {
        return this.mImpl.getBrowserRootHints();
    }
    
    @Nullable
    public MediaSessionCompat.Token getSessionToken() {
        return this.mSession;
    }
    
    boolean isValidPackage(final String s, int i) {
        if (s != null) {
            final String[] packagesForUid = this.getPackageManager().getPackagesForUid(i);
            int length;
            for (length = packagesForUid.length, i = 0; i < length; ++i) {
                if (packagesForUid[i].equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void notifyChildrenChanged(@NonNull final String s) {
        if (s == null) {
            throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        this.mImpl.notifyChildrenChanged(s, null);
    }
    
    public void notifyChildrenChanged(@NonNull final String s, @NonNull final Bundle bundle) {
        if (s == null) {
            throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        if (bundle == null) {
            throw new IllegalArgumentException("options cannot be null in notifyChildrenChanged");
        }
        this.mImpl.notifyChildrenChanged(s, bundle);
    }
    
    public IBinder onBind(final Intent intent) {
        return this.mImpl.onBind(intent);
    }
    
    public void onCreate() {
        super.onCreate();
        if (BuildCompat.isAtLeastO()) {
            this.mImpl = (MediaBrowserServiceImpl)new MediaBrowserServiceImplApi24();
        }
        else if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (MediaBrowserServiceImpl)new MediaBrowserServiceImplApi23();
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaBrowserServiceImpl)new MediaBrowserServiceImplApi21();
        }
        else {
            this.mImpl = (MediaBrowserServiceImpl)new MediaBrowserServiceImplBase();
        }
        this.mImpl.onCreate();
    }
    
    public void onCustomAction(@NonNull final String s, final Bundle bundle, @NonNull final Result<Bundle> result) {
        result.sendError(null);
    }
    
    @Nullable
    public abstract BrowserRoot onGetRoot(@NonNull final String p0, final int p1, @Nullable final Bundle p2);
    
    public abstract void onLoadChildren(@NonNull final String p0, @NonNull final Result<List<MediaBrowserCompat.MediaItem>> p1);
    
    public void onLoadChildren(@NonNull final String s, @NonNull final Result<List<MediaBrowserCompat.MediaItem>> result, @NonNull final Bundle bundle) {
        result.setFlags(1);
        this.onLoadChildren(s, result);
    }
    
    public void onLoadItem(final String s, @NonNull final Result<MediaBrowserCompat.MediaItem> result) {
        result.setFlags(2);
        result.sendResult(null);
    }
    
    public void onSearch(@NonNull final String s, final Bundle bundle, @NonNull final Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.setFlags(4);
        result.sendResult(null);
    }
    
    void performCustomAction(final String s, final Bundle bundle, final ConnectionRecord mCurConnection, final ResultReceiver resultReceiver) {
        final Result<Bundle> result = new Result<Bundle>(s) {
            @Override
            void onErrorSent(final Bundle bundle) {
                resultReceiver.send(-1, bundle);
            }
            
            @Override
            void onProgressUpdateSent(final Bundle bundle) {
                resultReceiver.send(1, bundle);
            }
            
            void onResultSent(final Bundle bundle) {
                resultReceiver.send(0, bundle);
            }
        };
        this.mCurConnection = mCurConnection;
        this.onCustomAction(s, bundle, (Result<Bundle>)result);
        this.mCurConnection = null;
        if (!((Result)result).isDone()) {
            throw new IllegalStateException("onCustomAction must call detach() or sendResult() or sendError() before returning for action=" + s + " extras=" + bundle);
        }
    }
    
    void performLoadChildren(final String s, final ConnectionRecord mCurConnection, final Bundle bundle) {
        final Result<List<MediaBrowserCompat.MediaItem>> result = new Result<List<MediaBrowserCompat.MediaItem>>(s) {
            void onResultSent(List<MediaBrowserCompat.MediaItem> applyOptions) {
                if (MediaBrowserServiceCompat.this.mConnections.get(mCurConnection.callbacks.asBinder()) != mCurConnection) {
                    if (MediaBrowserServiceCompat.DEBUG) {
                        Log.d("MBServiceCompat", "Not sending onLoadChildren result for connection that has been disconnected. pkg=" + mCurConnection.pkg + " id=" + s);
                    }
                    return;
                }
                if ((((Result)this).getFlags() & 0x1) != 0x0) {
                    applyOptions = MediaBrowserServiceCompat.this.applyOptions(applyOptions, bundle);
                }
                while (true) {
                    try {
                        mCurConnection.callbacks.onLoadChildren(s, applyOptions, bundle);
                        return;
                    }
                    catch (RemoteException ex) {
                        Log.w("MBServiceCompat", "Calling onLoadChildren() failed for id=" + s + " package=" + mCurConnection.pkg);
                        return;
                    }
                    continue;
                }
            }
        };
        this.mCurConnection = mCurConnection;
        if (bundle == null) {
            this.onLoadChildren(s, (Result<List<MediaBrowserCompat.MediaItem>>)result);
        }
        else {
            this.onLoadChildren(s, (Result<List<MediaBrowserCompat.MediaItem>>)result, bundle);
        }
        this.mCurConnection = null;
        if (!((Result)result).isDone()) {
            throw new IllegalStateException("onLoadChildren must call detach() or sendResult() before returning for package=" + mCurConnection.pkg + " id=" + s);
        }
    }
    
    void performLoadItem(final String s, final ConnectionRecord mCurConnection, final ResultReceiver resultReceiver) {
        final Result<MediaBrowserCompat.MediaItem> result = new Result<MediaBrowserCompat.MediaItem>(s) {
            void onResultSent(final MediaBrowserCompat.MediaItem mediaItem) {
                if ((((Result)this).getFlags() & 0x2) != 0x0) {
                    resultReceiver.send(-1, null);
                    return;
                }
                final Bundle bundle = new Bundle();
                bundle.putParcelable("media_item", (Parcelable)mediaItem);
                resultReceiver.send(0, bundle);
            }
        };
        this.mCurConnection = mCurConnection;
        this.onLoadItem(s, (Result<MediaBrowserCompat.MediaItem>)result);
        this.mCurConnection = null;
        if (!((Result)result).isDone()) {
            throw new IllegalStateException("onLoadItem must call detach() or sendResult() before returning for id=" + s);
        }
    }
    
    void performSearch(final String s, final Bundle bundle, final ConnectionRecord mCurConnection, final ResultReceiver resultReceiver) {
        final Result<List<MediaBrowserCompat.MediaItem>> result = new Result<List<MediaBrowserCompat.MediaItem>>(s) {
            void onResultSent(final List<MediaBrowserCompat.MediaItem> list) {
                if ((((Result)this).getFlags() & 0x4) != 0x0 || list == null) {
                    resultReceiver.send(-1, null);
                    return;
                }
                final Bundle bundle = new Bundle();
                bundle.putParcelableArray("search_results", (Parcelable[])list.toArray((Parcelable[])new MediaBrowserCompat.MediaItem[0]));
                resultReceiver.send(0, bundle);
            }
        };
        this.mCurConnection = mCurConnection;
        this.onSearch(s, bundle, (Result<List<MediaBrowserCompat.MediaItem>>)result);
        this.mCurConnection = null;
        if (!((Result)result).isDone()) {
            throw new IllegalStateException("onSearch must call detach() or sendResult() before returning for query=" + s);
        }
    }
    
    boolean removeSubscription(final String s, final ConnectionRecord connectionRecord, final IBinder binder) {
        if (binder == null) {
            return connectionRecord.subscriptions.remove(s) != null;
        }
        boolean b = false;
        boolean b2 = false;
        final List<Pair<IBinder, Bundle>> list = connectionRecord.subscriptions.get(s);
        if (list != null) {
            final Iterator<Pair<IBinder, Bundle>> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (binder == iterator.next().first) {
                    b2 = true;
                    iterator.remove();
                }
            }
            b = b2;
            if (list.size() == 0) {
                connectionRecord.subscriptions.remove(s);
                b = b2;
            }
        }
        return b;
    }
    
    public void setSessionToken(final MediaSessionCompat.Token token) {
        if (token == null) {
            throw new IllegalArgumentException("Session token may not be null.");
        }
        if (this.mSession != null) {
            throw new IllegalStateException("The session token has already been set.");
        }
        this.mSession = token;
        this.mImpl.setSessionToken(token);
    }
    
    public static final class BrowserRoot
    {
        public static final String EXTRA_OFFLINE = "android.service.media.extra.OFFLINE";
        public static final String EXTRA_RECENT = "android.service.media.extra.RECENT";
        public static final String EXTRA_SUGGESTED = "android.service.media.extra.SUGGESTED";
        @Deprecated
        public static final String EXTRA_SUGGESTION_KEYWORDS = "android.service.media.extra.SUGGESTION_KEYWORDS";
        private final Bundle mExtras;
        private final String mRootId;
        
        public BrowserRoot(@NonNull final String mRootId, @Nullable final Bundle mExtras) {
            if (mRootId == null) {
                throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
            }
            this.mRootId = mRootId;
            this.mExtras = mExtras;
        }
        
        public Bundle getExtras() {
            return this.mExtras;
        }
        
        public String getRootId() {
            return this.mRootId;
        }
    }
    
    private class ConnectionRecord
    {
        ServiceCallbacks callbacks;
        String pkg;
        BrowserRoot root;
        Bundle rootHints;
        HashMap<String, List<Pair<IBinder, Bundle>>> subscriptions;
        
        ConnectionRecord() {
            this.subscriptions = new HashMap<String, List<Pair<IBinder, Bundle>>>();
        }
    }
    
    interface MediaBrowserServiceImpl
    {
        Bundle getBrowserRootHints();
        
        void notifyChildrenChanged(final String p0, final Bundle p1);
        
        IBinder onBind(final Intent p0);
        
        void onCreate();
        
        void setSessionToken(final MediaSessionCompat.Token p0);
    }
    
    @RequiresApi(21)
    class MediaBrowserServiceImplApi21 implements MediaBrowserServiceImpl, ServiceCompatProxy
    {
        Messenger mMessenger;
        Object mServiceObj;
        
        @Override
        public Bundle getBrowserRootHints() {
            if (this.mMessenger != null) {
                if (MediaBrowserServiceCompat.this.mCurConnection == null) {
                    throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem or onSearch methods");
                }
                if (MediaBrowserServiceCompat.this.mCurConnection.rootHints != null) {
                    return new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
                }
            }
            return null;
        }
        
        @Override
        public void notifyChildrenChanged(final String s, final Bundle bundle) {
            if (this.mMessenger == null) {
                MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, s);
                return;
            }
            MediaBrowserServiceCompat.this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Iterator<IBinder> iterator = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();
                    while (iterator.hasNext()) {
                        final ConnectionRecord connectionRecord = MediaBrowserServiceCompat.this.mConnections.get(iterator.next());
                        final List<Pair<IBinder, Bundle>> list = connectionRecord.subscriptions.get(s);
                        if (list != null) {
                            for (final Pair<IBinder, Bundle> pair : list) {
                                if (MediaBrowserCompatUtils.hasDuplicatedItems(bundle, pair.second)) {
                                    MediaBrowserServiceCompat.this.performLoadChildren(s, connectionRecord, pair.second);
                                }
                            }
                        }
                    }
                }
            });
        }
        
        @Override
        public IBinder onBind(final Intent intent) {
            return MediaBrowserServiceCompatApi21.onBind(this.mServiceObj, intent);
        }
        
        @Override
        public void onCreate() {
            MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj = MediaBrowserServiceCompatApi21.createService((Context)MediaBrowserServiceCompat.this, (MediaBrowserServiceCompatApi21.ServiceCompatProxy)this));
        }
        
        @Override
        public MediaBrowserServiceCompatApi21.BrowserRoot onGetRoot(final String s, final int n, final Bundle bundle) {
            Bundle bundle3;
            final Bundle bundle2 = bundle3 = null;
            if (bundle != null) {
                bundle3 = bundle2;
                if (bundle.getInt("extra_client_version", 0) != 0) {
                    bundle.remove("extra_client_version");
                    this.mMessenger = new Messenger((Handler)MediaBrowserServiceCompat.this.mHandler);
                    final Bundle bundle4 = new Bundle();
                    bundle4.putInt("extra_service_version", 1);
                    BundleCompat.putBinder(bundle4, "extra_messenger", this.mMessenger.getBinder());
                    final IMediaSession extraBinder = MediaBrowserServiceCompat.this.mSession.getExtraBinder();
                    IBinder binder;
                    if (extraBinder == null) {
                        binder = null;
                    }
                    else {
                        binder = extraBinder.asBinder();
                    }
                    BundleCompat.putBinder(bundle4, "extra_session_binder", binder);
                    bundle3 = bundle4;
                }
            }
            final MediaBrowserServiceCompat.BrowserRoot onGetRoot = MediaBrowserServiceCompat.this.onGetRoot(s, n, bundle);
            if (onGetRoot == null) {
                return null;
            }
            Bundle extras;
            if (bundle3 == null) {
                extras = onGetRoot.getExtras();
            }
            else {
                extras = bundle3;
                if (onGetRoot.getExtras() != null) {
                    bundle3.putAll(onGetRoot.getExtras());
                    extras = bundle3;
                }
            }
            return new MediaBrowserServiceCompatApi21.BrowserRoot(onGetRoot.getRootId(), extras);
        }
        
        @Override
        public void onLoadChildren(final String s, final ResultWrapper<List<Parcel>> resultWrapper) {
            MediaBrowserServiceCompat.this.onLoadChildren(s, (Result<List<MediaBrowserCompat.MediaItem>>)new Result<List<MediaBrowserCompat.MediaItem>>(s) {
                @Override
                public void detach() {
                    resultWrapper.detach();
                }
                
                void onResultSent(final List<MediaBrowserCompat.MediaItem> list) {
                    ArrayList<Parcel> list2 = null;
                    if (list != null) {
                        final ArrayList<Parcel> list3 = new ArrayList<Parcel>();
                        final Iterator<MediaBrowserCompat.MediaItem> iterator = list.iterator();
                        while (true) {
                            list2 = list3;
                            if (!iterator.hasNext()) {
                                break;
                            }
                            final MediaBrowserCompat.MediaItem mediaItem = iterator.next();
                            final Parcel obtain = Parcel.obtain();
                            mediaItem.writeToParcel(obtain, 0);
                            list3.add(obtain);
                        }
                    }
                    resultWrapper.sendResult(list2);
                }
            });
        }
        
        @Override
        public void setSessionToken(final MediaSessionCompat.Token token) {
            MediaBrowserServiceCompatApi21.setSessionToken(this.mServiceObj, token.getToken());
        }
    }
    
    @RequiresApi(23)
    class MediaBrowserServiceImplApi23 extends MediaBrowserServiceImplApi21 implements MediaBrowserServiceCompatApi23.ServiceCompatProxy
    {
        @Override
        public void onCreate() {
            MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj = MediaBrowserServiceCompatApi23.createService((Context)MediaBrowserServiceCompat.this, (MediaBrowserServiceCompatApi23.ServiceCompatProxy)this));
        }
        
        @Override
        public void onLoadItem(final String s, final ResultWrapper<Parcel> resultWrapper) {
            MediaBrowserServiceCompat.this.onLoadItem(s, (Result<MediaBrowserCompat.MediaItem>)new Result<MediaBrowserCompat.MediaItem>(s) {
                @Override
                public void detach() {
                    resultWrapper.detach();
                }
                
                void onResultSent(final MediaBrowserCompat.MediaItem mediaItem) {
                    if (mediaItem == null) {
                        resultWrapper.sendResult(null);
                        return;
                    }
                    final Parcel obtain = Parcel.obtain();
                    mediaItem.writeToParcel(obtain, 0);
                    resultWrapper.sendResult(obtain);
                }
            });
        }
    }
    
    @RequiresApi(26)
    class MediaBrowserServiceImplApi24 extends MediaBrowserServiceImplApi23 implements MediaBrowserServiceCompatApi24.ServiceCompatProxy
    {
        @Override
        public Bundle getBrowserRootHints() {
            if (MediaBrowserServiceCompat.this.mCurConnection == null) {
                return MediaBrowserServiceCompatApi24.getBrowserRootHints(this.mServiceObj);
            }
            if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null) {
                return null;
            }
            return new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
        }
        
        @Override
        public void notifyChildrenChanged(final String s, final Bundle bundle) {
            if (bundle == null) {
                MediaBrowserServiceCompatApi21.notifyChildrenChanged(this.mServiceObj, s);
                return;
            }
            MediaBrowserServiceCompatApi24.notifyChildrenChanged(this.mServiceObj, s, bundle);
        }
        
        @Override
        public void onCreate() {
            MediaBrowserServiceCompatApi21.onCreate(this.mServiceObj = MediaBrowserServiceCompatApi24.createService((Context)MediaBrowserServiceCompat.this, (MediaBrowserServiceCompatApi24.ServiceCompatProxy)this));
        }
        
        @Override
        public void onLoadChildren(final String s, final MediaBrowserServiceCompatApi24.ResultWrapper resultWrapper, final Bundle bundle) {
            MediaBrowserServiceCompat.this.onLoadChildren(s, (Result<List<MediaBrowserCompat.MediaItem>>)new Result<List<MediaBrowserCompat.MediaItem>>(s) {
                @Override
                public void detach() {
                    resultWrapper.detach();
                }
                
                void onResultSent(final List<MediaBrowserCompat.MediaItem> list) {
                    List<Parcel> list2 = null;
                    if (list != null) {
                        final ArrayList<Parcel> list3 = new ArrayList<Parcel>();
                        final Iterator<MediaBrowserCompat.MediaItem> iterator = list.iterator();
                        while (true) {
                            list2 = list3;
                            if (!iterator.hasNext()) {
                                break;
                            }
                            final MediaBrowserCompat.MediaItem mediaItem = iterator.next();
                            final Parcel obtain = Parcel.obtain();
                            mediaItem.writeToParcel(obtain, 0);
                            list3.add(obtain);
                        }
                    }
                    resultWrapper.sendResult(list2, ((Result)this).getFlags());
                }
            }, bundle);
        }
    }
    
    class MediaBrowserServiceImplBase implements MediaBrowserServiceImpl
    {
        private Messenger mMessenger;
        
        @Override
        public Bundle getBrowserRootHints() {
            if (MediaBrowserServiceCompat.this.mCurConnection == null) {
                throw new IllegalStateException("This should be called inside of onLoadChildren, onLoadItem or onSearch methods");
            }
            if (MediaBrowserServiceCompat.this.mCurConnection.rootHints == null) {
                return null;
            }
            return new Bundle(MediaBrowserServiceCompat.this.mCurConnection.rootHints);
        }
        
        @Override
        public void notifyChildrenChanged(@NonNull final String s, final Bundle bundle) {
            MediaBrowserServiceCompat.this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Iterator<IBinder> iterator = MediaBrowserServiceCompat.this.mConnections.keySet().iterator();
                    while (iterator.hasNext()) {
                        final ConnectionRecord connectionRecord = MediaBrowserServiceCompat.this.mConnections.get(iterator.next());
                        final List<Pair<IBinder, Bundle>> list = connectionRecord.subscriptions.get(s);
                        if (list != null) {
                            for (final Pair<IBinder, Bundle> pair : list) {
                                if (MediaBrowserCompatUtils.hasDuplicatedItems(bundle, pair.second)) {
                                    MediaBrowserServiceCompat.this.performLoadChildren(s, connectionRecord, pair.second);
                                }
                            }
                        }
                    }
                }
            });
        }
        
        @Override
        public IBinder onBind(final Intent intent) {
            if ("android.media.browse.MediaBrowserService".equals(intent.getAction())) {
                return this.mMessenger.getBinder();
            }
            return null;
        }
        
        @Override
        public void onCreate() {
            this.mMessenger = new Messenger((Handler)MediaBrowserServiceCompat.this.mHandler);
        }
        
        @Override
        public void setSessionToken(final MediaSessionCompat.Token token) {
            MediaBrowserServiceCompat.this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final Iterator<ConnectionRecord> iterator = MediaBrowserServiceCompat.this.mConnections.values().iterator();
                    while (iterator.hasNext()) {
                        final ConnectionRecord connectionRecord = iterator.next();
                        try {
                            connectionRecord.callbacks.onConnect(connectionRecord.root.getRootId(), token, connectionRecord.root.getExtras());
                        }
                        catch (RemoteException ex) {
                            Log.w("MBServiceCompat", "Connection for " + connectionRecord.pkg + " is no longer valid.");
                            iterator.remove();
                        }
                    }
                }
            });
        }
    }
    
    public static class Result<T>
    {
        private final Object mDebug;
        private boolean mDetachCalled;
        private int mFlags;
        private boolean mSendErrorCalled;
        private boolean mSendProgressUpdateCalled;
        private boolean mSendResultCalled;
        
        Result(final Object mDebug) {
            this.mDebug = mDebug;
        }
        
        public void detach() {
            if (this.mDetachCalled) {
                throw new IllegalStateException("detach() called when detach() had already been called for: " + this.mDebug);
            }
            if (this.mSendResultCalled) {
                throw new IllegalStateException("detach() called when sendResult() had already been called for: " + this.mDebug);
            }
            if (this.mSendErrorCalled) {
                throw new IllegalStateException("detach() called when sendError() had already been called for: " + this.mDebug);
            }
            this.mDetachCalled = true;
        }
        
        int getFlags() {
            return this.mFlags;
        }
        
        boolean isDone() {
            return this.mDetachCalled || this.mSendResultCalled || this.mSendErrorCalled;
        }
        
        void onErrorSent(final Bundle bundle) {
            throw new UnsupportedOperationException("It is not supported to send an error for " + this.mDebug);
        }
        
        void onProgressUpdateSent(final Bundle bundle) {
            throw new UnsupportedOperationException("It is not supported to send an interim update for " + this.mDebug);
        }
        
        void onResultSent(final T t) {
        }
        
        public void sendError(final Bundle bundle) {
            if (this.mSendResultCalled || this.mSendErrorCalled) {
                throw new IllegalStateException("sendError() called when either sendResult() or sendError() had already been called for: " + this.mDebug);
            }
            this.mSendErrorCalled = true;
            this.onErrorSent(bundle);
        }
        
        public void sendProgressUpdate(final Bundle bundle) {
            if (this.mSendResultCalled || this.mSendErrorCalled) {
                throw new IllegalStateException("sendProgressUpdate() called when either sendResult() or sendError() had already been called for: " + this.mDebug);
            }
            this.mSendProgressUpdateCalled = true;
            this.onProgressUpdateSent(bundle);
        }
        
        public void sendResult(final T t) {
            if (this.mSendResultCalled || this.mSendErrorCalled) {
                throw new IllegalStateException("sendResult() called when either sendResult() or sendError() had already been called for: " + this.mDebug);
            }
            this.mSendResultCalled = true;
            this.onResultSent(t);
        }
        
        void setFlags(final int mFlags) {
            this.mFlags = mFlags;
        }
    }
    
    private class ServiceBinderImpl
    {
        public void addSubscription(final String s, final IBinder binder, final Bundle bundle, final ServiceCallbacks serviceCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                @Override
                public void run() {
                    final ConnectionRecord connectionRecord = MediaBrowserServiceCompat.this.mConnections.get(serviceCallbacks.asBinder());
                    if (connectionRecord == null) {
                        Log.w("MBServiceCompat", "addSubscription for callback that isn't registered id=" + s);
                        return;
                    }
                    MediaBrowserServiceCompat.this.addSubscription(s, connectionRecord, binder, bundle);
                }
            });
        }
        
        public void connect(final String s, final int n, final Bundle bundle, final ServiceCallbacks serviceCallbacks) {
            if (!MediaBrowserServiceCompat.this.isValidPackage(s, n)) {
                throw new IllegalArgumentException("Package/uid mismatch: uid=" + n + " package=" + s);
            }
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                @Override
                public void run() {
                    final IBinder binder = serviceCallbacks.asBinder();
                    MediaBrowserServiceCompat.this.mConnections.remove(binder);
                    final ConnectionRecord connectionRecord = new ConnectionRecord();
                    connectionRecord.pkg = s;
                    connectionRecord.rootHints = bundle;
                    connectionRecord.callbacks = serviceCallbacks;
                    connectionRecord.root = MediaBrowserServiceCompat.this.onGetRoot(s, n, bundle);
                    Label_0180: {
                        if (connectionRecord.root != null) {
                            break Label_0180;
                        }
                        Log.i("MBServiceCompat", "No root for client " + s + " from service " + this.getClass().getName());
                        try {
                            serviceCallbacks.onConnectFailed();
                            return;
                        }
                        catch (RemoteException ex) {
                            Log.w("MBServiceCompat", "Calling onConnectFailed() failed. Ignoring. pkg=" + s);
                            return;
                        }
                        try {
                            MediaBrowserServiceCompat.this.mConnections.put(binder, connectionRecord);
                            if (MediaBrowserServiceCompat.this.mSession != null) {
                                serviceCallbacks.onConnect(connectionRecord.root.getRootId(), MediaBrowserServiceCompat.this.mSession, connectionRecord.root.getExtras());
                            }
                        }
                        catch (RemoteException ex2) {
                            Log.w("MBServiceCompat", "Calling onConnect() failed. Dropping client. pkg=" + s);
                            MediaBrowserServiceCompat.this.mConnections.remove(binder);
                        }
                    }
                }
            });
        }
        
        public void disconnect(final ServiceCallbacks serviceCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                @Override
                public void run() {
                    if (MediaBrowserServiceCompat.this.mConnections.remove(serviceCallbacks.asBinder()) != null) {}
                }
            });
        }
        
        public void getMediaItem(final String s, final ResultReceiver resultReceiver, final ServiceCallbacks serviceCallbacks) {
            if (TextUtils.isEmpty((CharSequence)s) || resultReceiver == null) {
                return;
            }
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                @Override
                public void run() {
                    final ConnectionRecord connectionRecord = MediaBrowserServiceCompat.this.mConnections.get(serviceCallbacks.asBinder());
                    if (connectionRecord == null) {
                        Log.w("MBServiceCompat", "getMediaItem for callback that isn't registered id=" + s);
                        return;
                    }
                    MediaBrowserServiceCompat.this.performLoadItem(s, connectionRecord, resultReceiver);
                }
            });
        }
        
        public void registerCallbacks(final ServiceCallbacks serviceCallbacks, final Bundle bundle) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                @Override
                public void run() {
                    final IBinder binder = serviceCallbacks.asBinder();
                    MediaBrowserServiceCompat.this.mConnections.remove(binder);
                    final ConnectionRecord connectionRecord = new ConnectionRecord();
                    connectionRecord.callbacks = serviceCallbacks;
                    connectionRecord.rootHints = bundle;
                    MediaBrowserServiceCompat.this.mConnections.put(binder, connectionRecord);
                }
            });
        }
        
        public void removeSubscription(final String s, final IBinder binder, final ServiceCallbacks serviceCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                @Override
                public void run() {
                    final ConnectionRecord connectionRecord = MediaBrowserServiceCompat.this.mConnections.get(serviceCallbacks.asBinder());
                    if (connectionRecord == null) {
                        Log.w("MBServiceCompat", "removeSubscription for callback that isn't registered id=" + s);
                    }
                    else if (!MediaBrowserServiceCompat.this.removeSubscription(s, connectionRecord, binder)) {
                        Log.w("MBServiceCompat", "removeSubscription called for " + s + " which is not subscribed");
                    }
                }
            });
        }
        
        public void search(final String s, final Bundle bundle, final ResultReceiver resultReceiver, final ServiceCallbacks serviceCallbacks) {
            if (TextUtils.isEmpty((CharSequence)s) || resultReceiver == null) {
                return;
            }
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                @Override
                public void run() {
                    final ConnectionRecord connectionRecord = MediaBrowserServiceCompat.this.mConnections.get(serviceCallbacks.asBinder());
                    if (connectionRecord == null) {
                        Log.w("MBServiceCompat", "search for callback that isn't registered query=" + s);
                        return;
                    }
                    MediaBrowserServiceCompat.this.performSearch(s, bundle, connectionRecord, resultReceiver);
                }
            });
        }
        
        public void sendCustomAction(final String s, final Bundle bundle, final ResultReceiver resultReceiver, final ServiceCallbacks serviceCallbacks) {
            if (TextUtils.isEmpty((CharSequence)s) || resultReceiver == null) {
                return;
            }
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                @Override
                public void run() {
                    final ConnectionRecord connectionRecord = MediaBrowserServiceCompat.this.mConnections.get(serviceCallbacks.asBinder());
                    if (connectionRecord == null) {
                        Log.w("MBServiceCompat", "sendCustomAction for callback that isn't registered action=" + s + ", extras=" + bundle);
                        return;
                    }
                    MediaBrowserServiceCompat.this.performCustomAction(s, bundle, connectionRecord, resultReceiver);
                }
            });
        }
        
        public void unregisterCallbacks(final ServiceCallbacks serviceCallbacks) {
            MediaBrowserServiceCompat.this.mHandler.postOrRun(new Runnable() {
                @Override
                public void run() {
                    MediaBrowserServiceCompat.this.mConnections.remove(serviceCallbacks.asBinder());
                }
            });
        }
    }
    
    private interface ServiceCallbacks
    {
        IBinder asBinder();
        
        void onConnect(final String p0, final MediaSessionCompat.Token p1, final Bundle p2) throws RemoteException;
        
        void onConnectFailed() throws RemoteException;
        
        void onLoadChildren(final String p0, final List<MediaBrowserCompat.MediaItem> p1, final Bundle p2) throws RemoteException;
    }
    
    private static class ServiceCallbacksCompat implements ServiceCallbacks
    {
        final Messenger mCallbacks;
        
        ServiceCallbacksCompat(final Messenger mCallbacks) {
            this.mCallbacks = mCallbacks;
        }
        
        private void sendRequest(final int what, final Bundle data) throws RemoteException {
            final Message obtain = Message.obtain();
            obtain.what = what;
            obtain.arg1 = 1;
            obtain.setData(data);
            this.mCallbacks.send(obtain);
        }
        
        @Override
        public IBinder asBinder() {
            return this.mCallbacks.getBinder();
        }
        
        @Override
        public void onConnect(final String s, final MediaSessionCompat.Token token, final Bundle bundle) throws RemoteException {
            Bundle bundle2 = bundle;
            if (bundle == null) {
                bundle2 = new Bundle();
            }
            bundle2.putInt("extra_service_version", 1);
            final Bundle bundle3 = new Bundle();
            bundle3.putString("data_media_item_id", s);
            bundle3.putParcelable("data_media_session_token", (Parcelable)token);
            bundle3.putBundle("data_root_hints", bundle2);
            this.sendRequest(1, bundle3);
        }
        
        @Override
        public void onConnectFailed() throws RemoteException {
            this.sendRequest(2, null);
        }
        
        @Override
        public void onLoadChildren(final String s, final List<MediaBrowserCompat.MediaItem> list, final Bundle bundle) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", s);
            bundle2.putBundle("data_options", bundle);
            if (list != null) {
                ArrayList list2;
                if (list instanceof ArrayList) {
                    list2 = (ArrayList<? extends E>)list;
                }
                else {
                    list2 = new ArrayList((Collection<? extends E>)list);
                }
                bundle2.putParcelableArrayList("data_media_item_list", list2);
            }
            this.sendRequest(3, bundle2);
        }
    }
    
    private final class ServiceHandler extends Handler
    {
        private final ServiceBinderImpl mServiceBinderImpl;
        
        ServiceHandler() {
            this.mServiceBinderImpl = new ServiceBinderImpl();
        }
        
        public void handleMessage(final Message message) {
            final Bundle data = message.getData();
            switch (message.what) {
                default: {
                    Log.w("MBServiceCompat", "Unhandled message: " + message + "\n  Service version: " + 1 + "\n  Client version: " + message.arg1);
                }
                case 1: {
                    this.mServiceBinderImpl.connect(data.getString("data_package_name"), data.getInt("data_calling_uid"), data.getBundle("data_root_hints"), new ServiceCallbacksCompat(message.replyTo));
                }
                case 2: {
                    this.mServiceBinderImpl.disconnect(new ServiceCallbacksCompat(message.replyTo));
                }
                case 3: {
                    this.mServiceBinderImpl.addSubscription(data.getString("data_media_item_id"), BundleCompat.getBinder(data, "data_callback_token"), data.getBundle("data_options"), new ServiceCallbacksCompat(message.replyTo));
                }
                case 4: {
                    this.mServiceBinderImpl.removeSubscription(data.getString("data_media_item_id"), BundleCompat.getBinder(data, "data_callback_token"), new ServiceCallbacksCompat(message.replyTo));
                }
                case 5: {
                    this.mServiceBinderImpl.getMediaItem(data.getString("data_media_item_id"), (ResultReceiver)data.getParcelable("data_result_receiver"), new ServiceCallbacksCompat(message.replyTo));
                }
                case 6: {
                    this.mServiceBinderImpl.registerCallbacks(new ServiceCallbacksCompat(message.replyTo), data.getBundle("data_root_hints"));
                }
                case 7: {
                    this.mServiceBinderImpl.unregisterCallbacks(new ServiceCallbacksCompat(message.replyTo));
                }
                case 8: {
                    this.mServiceBinderImpl.search(data.getString("data_search_query"), data.getBundle("data_search_extras"), (ResultReceiver)data.getParcelable("data_result_receiver"), new ServiceCallbacksCompat(message.replyTo));
                }
                case 9: {
                    this.mServiceBinderImpl.sendCustomAction(data.getString("data_custom_action"), data.getBundle("data_custom_action_extras"), (ResultReceiver)data.getParcelable("data_result_receiver"), new ServiceCallbacksCompat(message.replyTo));
                }
            }
        }
        
        public void postOrRun(final Runnable runnable) {
            if (Thread.currentThread() == this.getLooper().getThread()) {
                runnable.run();
                return;
            }
            this.post(runnable);
        }
        
        public boolean sendMessageAtTime(final Message message, final long n) {
            final Bundle data = message.getData();
            data.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            data.putInt("data_calling_uid", Binder.getCallingUid());
            return super.sendMessageAtTime(message, n);
        }
    }
}
