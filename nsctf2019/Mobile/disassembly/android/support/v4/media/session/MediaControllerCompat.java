// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media.session;

import android.support.v4.media.RatingCompat;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.BundleCompat;
import java.lang.ref.WeakReference;
import android.os.Parcelable;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import android.support.annotation.RequiresApi;
import android.os.Message;
import android.os.Looper;
import android.os.IBinder$DeathRecipient;
import android.text.TextUtils;
import android.os.ResultReceiver;
import android.os.Handler;
import android.app.PendingIntent;
import java.util.List;
import android.support.v4.media.MediaMetadataCompat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Log;
import android.support.v4.app.SupportActivity;
import android.app.Activity;
import android.os.RemoteException;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.Context;

public final class MediaControllerCompat
{
    static final String COMMAND_ADD_QUEUE_ITEM = "android.support.v4.media.session.command.ADD_QUEUE_ITEM";
    static final String COMMAND_ADD_QUEUE_ITEM_AT = "android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT";
    static final String COMMAND_ARGUMENT_INDEX = "android.support.v4.media.session.command.ARGUMENT_INDEX";
    static final String COMMAND_ARGUMENT_MEDIA_DESCRIPTION = "android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION";
    static final String COMMAND_GET_EXTRA_BINDER = "android.support.v4.media.session.command.GET_EXTRA_BINDER";
    static final String COMMAND_REMOVE_QUEUE_ITEM = "android.support.v4.media.session.command.REMOVE_QUEUE_ITEM";
    static final String COMMAND_REMOVE_QUEUE_ITEM_AT = "android.support.v4.media.session.command.REMOVE_QUEUE_ITEM_AT";
    static final String TAG = "MediaControllerCompat";
    private final MediaControllerImpl mImpl;
    private final MediaSessionCompat.Token mToken;
    
    public MediaControllerCompat(final Context context, @NonNull final MediaSessionCompat.Token mToken) throws RemoteException {
        if (mToken == null) {
            throw new IllegalArgumentException("sessionToken must not be null");
        }
        this.mToken = mToken;
        if (Build$VERSION.SDK_INT >= 24) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi24(context, mToken);
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi23(context, mToken);
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi21(context, mToken);
            return;
        }
        this.mImpl = (MediaControllerImpl)new MediaControllerImplBase(this.mToken);
    }
    
    public MediaControllerCompat(final Context context, @NonNull final MediaSessionCompat mediaSessionCompat) {
        if (mediaSessionCompat == null) {
            throw new IllegalArgumentException("session must not be null");
        }
        this.mToken = mediaSessionCompat.getSessionToken();
        if (Build$VERSION.SDK_INT >= 24) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi24(context, mediaSessionCompat);
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi23(context, mediaSessionCompat);
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi21(context, mediaSessionCompat);
            return;
        }
        this.mImpl = (MediaControllerImpl)new MediaControllerImplBase(this.mToken);
    }
    
    public static MediaControllerCompat getMediaController(@NonNull final Activity activity) {
        final MediaControllerCompat mediaControllerCompat = null;
        MediaControllerCompat mediaController;
        if (activity instanceof SupportActivity) {
            final MediaControllerExtraData mediaControllerExtraData = ((SupportActivity)activity).getExtraData(MediaControllerExtraData.class);
            mediaController = mediaControllerCompat;
            if (mediaControllerExtraData != null) {
                mediaController = mediaControllerExtraData.getMediaController();
            }
        }
        else {
            mediaController = mediaControllerCompat;
            if (Build$VERSION.SDK_INT >= 21) {
                final Object mediaController2 = MediaControllerCompatApi21.getMediaController(activity);
                mediaController = mediaControllerCompat;
                if (mediaController2 != null) {
                    final Object sessionToken = MediaControllerCompatApi21.getSessionToken(mediaController2);
                    try {
                        return new MediaControllerCompat((Context)activity, MediaSessionCompat.Token.fromToken(sessionToken));
                    }
                    catch (RemoteException ex) {
                        Log.e("MediaControllerCompat", "Dead object in getMediaController.", (Throwable)ex);
                        return null;
                    }
                }
            }
        }
        return mediaController;
    }
    
    public static void setMediaController(@NonNull final Activity activity, final MediaControllerCompat mediaControllerCompat) {
        if (activity instanceof SupportActivity) {
            ((SupportActivity)activity).putExtraData((SupportActivity.ExtraData)new MediaControllerExtraData(mediaControllerCompat));
        }
        if (Build$VERSION.SDK_INT >= 21) {
            Object fromToken = null;
            if (mediaControllerCompat != null) {
                fromToken = MediaControllerCompatApi21.fromToken((Context)activity, mediaControllerCompat.getSessionToken().getToken());
            }
            MediaControllerCompatApi21.setMediaController(activity, fromToken);
        }
    }
    
    public void addQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
        this.mImpl.addQueueItem(mediaDescriptionCompat);
    }
    
    public void addQueueItem(final MediaDescriptionCompat mediaDescriptionCompat, final int n) {
        this.mImpl.addQueueItem(mediaDescriptionCompat, n);
    }
    
    public void adjustVolume(final int n, final int n2) {
        this.mImpl.adjustVolume(n, n2);
    }
    
    public boolean dispatchMediaButtonEvent(final KeyEvent keyEvent) {
        if (keyEvent == null) {
            throw new IllegalArgumentException("KeyEvent may not be null");
        }
        return this.mImpl.dispatchMediaButtonEvent(keyEvent);
    }
    
    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }
    
    public long getFlags() {
        return this.mImpl.getFlags();
    }
    
    public Object getMediaController() {
        return this.mImpl.getMediaController();
    }
    
    public MediaMetadataCompat getMetadata() {
        return this.mImpl.getMetadata();
    }
    
    public String getPackageName() {
        return this.mImpl.getPackageName();
    }
    
    public PlaybackInfo getPlaybackInfo() {
        return this.mImpl.getPlaybackInfo();
    }
    
    public PlaybackStateCompat getPlaybackState() {
        return this.mImpl.getPlaybackState();
    }
    
    public List<MediaSessionCompat.QueueItem> getQueue() {
        return this.mImpl.getQueue();
    }
    
    public CharSequence getQueueTitle() {
        return this.mImpl.getQueueTitle();
    }
    
    public int getRatingType() {
        return this.mImpl.getRatingType();
    }
    
    public int getRepeatMode() {
        return this.mImpl.getRepeatMode();
    }
    
    public PendingIntent getSessionActivity() {
        return this.mImpl.getSessionActivity();
    }
    
    public MediaSessionCompat.Token getSessionToken() {
        return this.mToken;
    }
    
    public TransportControls getTransportControls() {
        return this.mImpl.getTransportControls();
    }
    
    public boolean isCaptioningEnabled() {
        return this.mImpl.isCaptioningEnabled();
    }
    
    public boolean isShuffleModeEnabled() {
        return this.mImpl.isShuffleModeEnabled();
    }
    
    public void registerCallback(@NonNull final Callback callback) {
        this.registerCallback(callback, null);
    }
    
    public void registerCallback(@NonNull final Callback callback, final Handler handler) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        Handler handler2;
        if ((handler2 = handler) == null) {
            handler2 = new Handler();
        }
        this.mImpl.registerCallback(callback, handler2);
    }
    
    public void removeQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
        this.mImpl.removeQueueItem(mediaDescriptionCompat);
    }
    
    @Deprecated
    public void removeQueueItemAt(final int n) {
        final List<MediaSessionCompat.QueueItem> queue = this.getQueue();
        if (queue != null && n >= 0 && n < queue.size()) {
            final MediaSessionCompat.QueueItem queueItem = queue.get(n);
            if (queueItem != null) {
                this.removeQueueItem(queueItem.getDescription());
            }
        }
    }
    
    public void sendCommand(@NonNull final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("command must neither be null nor empty");
        }
        this.mImpl.sendCommand(s, bundle, resultReceiver);
    }
    
    public void setVolumeTo(final int n, final int n2) {
        this.mImpl.setVolumeTo(n, n2);
    }
    
    public void unregisterCallback(@NonNull final Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        this.mImpl.unregisterCallback(callback);
    }
    
    public abstract static class Callback implements IBinder$DeathRecipient
    {
        private final Object mCallbackObj;
        MessageHandler mHandler;
        boolean mHasExtraCallback;
        boolean mRegistered;
        
        public Callback() {
            this.mRegistered = false;
            if (Build$VERSION.SDK_INT >= 21) {
                this.mCallbackObj = MediaControllerCompatApi21.createCallback((MediaControllerCompatApi21.Callback)new StubApi21());
                return;
            }
            this.mCallbackObj = new StubCompat();
        }
        
        private void setHandler(final Handler handler) {
            this.mHandler = new MessageHandler(handler.getLooper());
        }
        
        public void binderDied() {
            this.onSessionDestroyed();
        }
        
        public void onAudioInfoChanged(final PlaybackInfo playbackInfo) {
        }
        
        public void onCaptioningEnabledChanged(final boolean b) {
        }
        
        public void onExtrasChanged(final Bundle bundle) {
        }
        
        public void onMetadataChanged(final MediaMetadataCompat mediaMetadataCompat) {
        }
        
        public void onPlaybackStateChanged(final PlaybackStateCompat playbackStateCompat) {
        }
        
        public void onQueueChanged(final List<MediaSessionCompat.QueueItem> list) {
        }
        
        public void onQueueTitleChanged(final CharSequence charSequence) {
        }
        
        public void onRepeatModeChanged(final int n) {
        }
        
        public void onSessionDestroyed() {
        }
        
        public void onSessionEvent(final String s, final Bundle bundle) {
        }
        
        public void onShuffleModeChanged(final boolean b) {
        }
        
        private class MessageHandler extends Handler
        {
            private static final int MSG_DESTROYED = 8;
            private static final int MSG_EVENT = 1;
            private static final int MSG_UPDATE_CAPTIONING_ENABLED = 11;
            private static final int MSG_UPDATE_EXTRAS = 7;
            private static final int MSG_UPDATE_METADATA = 3;
            private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
            private static final int MSG_UPDATE_QUEUE = 5;
            private static final int MSG_UPDATE_QUEUE_TITLE = 6;
            private static final int MSG_UPDATE_REPEAT_MODE = 9;
            private static final int MSG_UPDATE_SHUFFLE_MODE = 10;
            private static final int MSG_UPDATE_VOLUME = 4;
            
            public MessageHandler(final Looper looper) {
                super(looper);
            }
            
            public void handleMessage(final Message message) {
                if (!Callback.this.mRegistered) {
                    return;
                }
                switch (message.what) {
                    default: {}
                    case 1: {
                        Callback.this.onSessionEvent((String)message.obj, message.getData());
                    }
                    case 2: {
                        Callback.this.onPlaybackStateChanged((PlaybackStateCompat)message.obj);
                    }
                    case 3: {
                        Callback.this.onMetadataChanged((MediaMetadataCompat)message.obj);
                    }
                    case 5: {
                        Callback.this.onQueueChanged((List<MediaSessionCompat.QueueItem>)message.obj);
                    }
                    case 6: {
                        Callback.this.onQueueTitleChanged((CharSequence)message.obj);
                    }
                    case 11: {
                        Callback.this.onCaptioningEnabledChanged((boolean)message.obj);
                    }
                    case 9: {
                        Callback.this.onRepeatModeChanged((int)message.obj);
                    }
                    case 10: {
                        Callback.this.onShuffleModeChanged((boolean)message.obj);
                    }
                    case 7: {
                        Callback.this.onExtrasChanged((Bundle)message.obj);
                    }
                    case 4: {
                        Callback.this.onAudioInfoChanged((PlaybackInfo)message.obj);
                    }
                    case 8: {
                        Callback.this.onSessionDestroyed();
                    }
                }
            }
            
            public void post(final int n, final Object o, final Bundle data) {
                final Message obtainMessage = this.obtainMessage(n, o);
                obtainMessage.setData(data);
                obtainMessage.sendToTarget();
            }
        }
        
        private class StubApi21 implements MediaControllerCompatApi21.Callback
        {
            @Override
            public void onAudioInfoChanged(final int n, final int n2, final int n3, final int n4, final int n5) {
                MediaControllerCompat.Callback.this.onAudioInfoChanged(new MediaControllerCompat.PlaybackInfo(n, n2, n3, n4, n5));
            }
            
            @Override
            public void onExtrasChanged(final Bundle bundle) {
                MediaControllerCompat.Callback.this.onExtrasChanged(bundle);
            }
            
            @Override
            public void onMetadataChanged(final Object o) {
                MediaControllerCompat.Callback.this.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(o));
            }
            
            @Override
            public void onPlaybackStateChanged(final Object o) {
                if (MediaControllerCompat.Callback.this.mHasExtraCallback) {
                    return;
                }
                MediaControllerCompat.Callback.this.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(o));
            }
            
            @Override
            public void onQueueChanged(final List<?> list) {
                MediaControllerCompat.Callback.this.onQueueChanged(MediaSessionCompat.QueueItem.fromQueueItemList(list));
            }
            
            @Override
            public void onQueueTitleChanged(final CharSequence charSequence) {
                MediaControllerCompat.Callback.this.onQueueTitleChanged(charSequence);
            }
            
            @Override
            public void onSessionDestroyed() {
                MediaControllerCompat.Callback.this.onSessionDestroyed();
            }
            
            @Override
            public void onSessionEvent(final String s, final Bundle bundle) {
                if (MediaControllerCompat.Callback.this.mHasExtraCallback && Build$VERSION.SDK_INT < 23) {
                    return;
                }
                MediaControllerCompat.Callback.this.onSessionEvent(s, bundle);
            }
        }
        
        private class StubCompat extends Stub
        {
            public void onCaptioningEnabledChanged(final boolean b) throws RemoteException {
                Callback.this.mHandler.post(11, b, null);
            }
            
            public void onEvent(final String s, final Bundle bundle) throws RemoteException {
                Callback.this.mHandler.post(1, s, bundle);
            }
            
            public void onExtrasChanged(final Bundle bundle) throws RemoteException {
                Callback.this.mHandler.post(7, bundle, null);
            }
            
            public void onMetadataChanged(final MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                Callback.this.mHandler.post(3, mediaMetadataCompat, null);
            }
            
            public void onPlaybackStateChanged(final PlaybackStateCompat playbackStateCompat) throws RemoteException {
                Callback.this.mHandler.post(2, playbackStateCompat, null);
            }
            
            public void onQueueChanged(final List<MediaSessionCompat.QueueItem> list) throws RemoteException {
                Callback.this.mHandler.post(5, list, null);
            }
            
            public void onQueueTitleChanged(final CharSequence charSequence) throws RemoteException {
                Callback.this.mHandler.post(6, charSequence, null);
            }
            
            public void onRepeatModeChanged(final int n) throws RemoteException {
                Callback.this.mHandler.post(9, n, null);
            }
            
            public void onSessionDestroyed() throws RemoteException {
                Callback.this.mHandler.post(8, null, null);
            }
            
            public void onShuffleModeChanged(final boolean b) throws RemoteException {
                Callback.this.mHandler.post(10, b, null);
            }
            
            public void onVolumeInfoChanged(final ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                Object o = null;
                if (parcelableVolumeInfo != null) {
                    o = new PlaybackInfo(parcelableVolumeInfo.volumeType, parcelableVolumeInfo.audioStream, parcelableVolumeInfo.controlType, parcelableVolumeInfo.maxVolume, parcelableVolumeInfo.currentVolume);
                }
                Callback.this.mHandler.post(4, o, null);
            }
        }
    }
    
    private static class MediaControllerExtraData extends ExtraData
    {
        private final MediaControllerCompat mMediaController;
        
        MediaControllerExtraData(final MediaControllerCompat mMediaController) {
            this.mMediaController = mMediaController;
        }
        
        MediaControllerCompat getMediaController() {
            return this.mMediaController;
        }
    }
    
    interface MediaControllerImpl
    {
        void addQueueItem(final MediaDescriptionCompat p0);
        
        void addQueueItem(final MediaDescriptionCompat p0, final int p1);
        
        void adjustVolume(final int p0, final int p1);
        
        boolean dispatchMediaButtonEvent(final KeyEvent p0);
        
        Bundle getExtras();
        
        long getFlags();
        
        Object getMediaController();
        
        MediaMetadataCompat getMetadata();
        
        String getPackageName();
        
        PlaybackInfo getPlaybackInfo();
        
        PlaybackStateCompat getPlaybackState();
        
        List<MediaSessionCompat.QueueItem> getQueue();
        
        CharSequence getQueueTitle();
        
        int getRatingType();
        
        int getRepeatMode();
        
        PendingIntent getSessionActivity();
        
        TransportControls getTransportControls();
        
        boolean isCaptioningEnabled();
        
        boolean isShuffleModeEnabled();
        
        void registerCallback(final Callback p0, final Handler p1);
        
        void removeQueueItem(final MediaDescriptionCompat p0);
        
        void sendCommand(final String p0, final Bundle p1, final ResultReceiver p2);
        
        void setVolumeTo(final int p0, final int p1);
        
        void unregisterCallback(final Callback p0);
    }
    
    @RequiresApi(21)
    static class MediaControllerImplApi21 implements MediaControllerImpl
    {
        private HashMap<Callback, ExtraCallback> mCallbackMap;
        protected final Object mControllerObj;
        private IMediaSession mExtraBinder;
        private List<Callback> mPendingCallbacks;
        
        public MediaControllerImplApi21(final Context context, final MediaSessionCompat.Token token) throws RemoteException {
            this.mCallbackMap = new HashMap<Callback, ExtraCallback>();
            this.mPendingCallbacks = new ArrayList<Callback>();
            this.mControllerObj = MediaControllerCompatApi21.fromToken(context, token.getToken());
            if (this.mControllerObj == null) {
                throw new RemoteException();
            }
            this.mExtraBinder = token.getExtraBinder();
            if (this.mExtraBinder == null) {
                this.requestExtraBinder();
            }
        }
        
        public MediaControllerImplApi21(final Context context, final MediaSessionCompat mediaSessionCompat) {
            this.mCallbackMap = new HashMap<Callback, ExtraCallback>();
            this.mPendingCallbacks = new ArrayList<Callback>();
            this.mControllerObj = MediaControllerCompatApi21.fromToken(context, mediaSessionCompat.getSessionToken().getToken());
            this.mExtraBinder = mediaSessionCompat.getSessionToken().getExtraBinder();
            if (this.mExtraBinder == null) {
                this.requestExtraBinder();
            }
        }
        
        private void processPendingCallbacks() {
            if (this.mExtraBinder == null) {
                return;
            }
            synchronized (this.mPendingCallbacks) {
                for (final Callback callback : this.mPendingCallbacks) {
                    final ExtraCallback extraCallback = new ExtraCallback(callback);
                    this.mCallbackMap.put(callback, extraCallback);
                    callback.mHasExtraCallback = true;
                    try {
                        this.mExtraBinder.registerCallbackListener(extraCallback);
                        continue;
                    }
                    catch (RemoteException ex) {
                        Log.e("MediaControllerCompat", "Dead object in registerCallback.", (Throwable)ex);
                    }
                    break;
                }
                this.mPendingCallbacks.clear();
            }
        }
        
        private void requestExtraBinder() {
            this.sendCommand("android.support.v4.media.session.command.GET_EXTRA_BINDER", null, new ExtraBinderRequestResultReceiver(this, new Handler()));
        }
        
        @Override
        public void addQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
            if ((0x4L & this.getFlags()) == 0x0L) {
                throw new UnsupportedOperationException("This session doesn't support queue management operations");
            }
            final Bundle bundle = new Bundle();
            bundle.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", (Parcelable)mediaDescriptionCompat);
            this.sendCommand("android.support.v4.media.session.command.ADD_QUEUE_ITEM", bundle, null);
        }
        
        @Override
        public void addQueueItem(final MediaDescriptionCompat mediaDescriptionCompat, final int n) {
            if ((0x4L & this.getFlags()) == 0x0L) {
                throw new UnsupportedOperationException("This session doesn't support queue management operations");
            }
            final Bundle bundle = new Bundle();
            bundle.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", (Parcelable)mediaDescriptionCompat);
            bundle.putInt("android.support.v4.media.session.command.ARGUMENT_INDEX", n);
            this.sendCommand("android.support.v4.media.session.command.ADD_QUEUE_ITEM_AT", bundle, null);
        }
        
        @Override
        public void adjustVolume(final int n, final int n2) {
            MediaControllerCompatApi21.adjustVolume(this.mControllerObj, n, n2);
        }
        
        @Override
        public boolean dispatchMediaButtonEvent(final KeyEvent keyEvent) {
            return MediaControllerCompatApi21.dispatchMediaButtonEvent(this.mControllerObj, keyEvent);
        }
        
        @Override
        public Bundle getExtras() {
            return MediaControllerCompatApi21.getExtras(this.mControllerObj);
        }
        
        @Override
        public long getFlags() {
            return MediaControllerCompatApi21.getFlags(this.mControllerObj);
        }
        
        @Override
        public Object getMediaController() {
            return this.mControllerObj;
        }
        
        @Override
        public MediaMetadataCompat getMetadata() {
            final Object metadata = MediaControllerCompatApi21.getMetadata(this.mControllerObj);
            if (metadata != null) {
                return MediaMetadataCompat.fromMediaMetadata(metadata);
            }
            return null;
        }
        
        @Override
        public String getPackageName() {
            return MediaControllerCompatApi21.getPackageName(this.mControllerObj);
        }
        
        @Override
        public PlaybackInfo getPlaybackInfo() {
            final Object playbackInfo = MediaControllerCompatApi21.getPlaybackInfo(this.mControllerObj);
            if (playbackInfo != null) {
                return new PlaybackInfo(MediaControllerCompatApi21.PlaybackInfo.getPlaybackType(playbackInfo), MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(playbackInfo), MediaControllerCompatApi21.PlaybackInfo.getVolumeControl(playbackInfo), MediaControllerCompatApi21.PlaybackInfo.getMaxVolume(playbackInfo), MediaControllerCompatApi21.PlaybackInfo.getCurrentVolume(playbackInfo));
            }
            return null;
        }
        
        @Override
        public PlaybackStateCompat getPlaybackState() {
            if (this.mExtraBinder != null) {
                try {
                    return this.mExtraBinder.getPlaybackState();
                }
                catch (RemoteException ex) {
                    Log.e("MediaControllerCompat", "Dead object in getPlaybackState.", (Throwable)ex);
                }
            }
            final Object playbackState = MediaControllerCompatApi21.getPlaybackState(this.mControllerObj);
            if (playbackState != null) {
                return PlaybackStateCompat.fromPlaybackState(playbackState);
            }
            return null;
        }
        
        @Override
        public List<MediaSessionCompat.QueueItem> getQueue() {
            final List<Object> queue = MediaControllerCompatApi21.getQueue(this.mControllerObj);
            if (queue != null) {
                return MediaSessionCompat.QueueItem.fromQueueItemList(queue);
            }
            return null;
        }
        
        @Override
        public CharSequence getQueueTitle() {
            return MediaControllerCompatApi21.getQueueTitle(this.mControllerObj);
        }
        
        @Override
        public int getRatingType() {
            if (Build$VERSION.SDK_INT < 22 && this.mExtraBinder != null) {
                try {
                    return this.mExtraBinder.getRatingType();
                }
                catch (RemoteException ex) {
                    Log.e("MediaControllerCompat", "Dead object in getRatingType.", (Throwable)ex);
                }
            }
            return MediaControllerCompatApi21.getRatingType(this.mControllerObj);
        }
        
        @Override
        public int getRepeatMode() {
            if (this.mExtraBinder != null) {
                try {
                    return this.mExtraBinder.getRepeatMode();
                }
                catch (RemoteException ex) {
                    Log.e("MediaControllerCompat", "Dead object in getRepeatMode.", (Throwable)ex);
                }
            }
            return 0;
        }
        
        @Override
        public PendingIntent getSessionActivity() {
            return MediaControllerCompatApi21.getSessionActivity(this.mControllerObj);
        }
        
        @Override
        public TransportControls getTransportControls() {
            final Object transportControls = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
            if (transportControls != null) {
                return new TransportControlsApi21(transportControls);
            }
            return null;
        }
        
        @Override
        public boolean isCaptioningEnabled() {
            if (this.mExtraBinder != null) {
                try {
                    return this.mExtraBinder.isCaptioningEnabled();
                }
                catch (RemoteException ex) {
                    Log.e("MediaControllerCompat", "Dead object in isCaptioningEnabled.", (Throwable)ex);
                }
            }
            return false;
        }
        
        @Override
        public boolean isShuffleModeEnabled() {
            if (this.mExtraBinder != null) {
                try {
                    return this.mExtraBinder.isShuffleModeEnabled();
                }
                catch (RemoteException ex) {
                    Log.e("MediaControllerCompat", "Dead object in isShuffleModeEnabled.", (Throwable)ex);
                }
            }
            return false;
        }
        
        @Override
        public final void registerCallback(final Callback callback, final Handler handler) {
            MediaControllerCompatApi21.registerCallback(this.mControllerObj, callback.mCallbackObj, handler);
            if (this.mExtraBinder != null) {
                callback.setHandler(handler);
                final ExtraCallback extraCallback = new ExtraCallback(callback);
                this.mCallbackMap.put(callback, extraCallback);
                callback.mHasExtraCallback = true;
                try {
                    this.mExtraBinder.registerCallbackListener(extraCallback);
                    return;
                }
                catch (RemoteException ex) {
                    Log.e("MediaControllerCompat", "Dead object in registerCallback.", (Throwable)ex);
                    return;
                }
            }
            callback.setHandler(handler);
            synchronized (this.mPendingCallbacks) {
                this.mPendingCallbacks.add(callback);
            }
        }
        
        @Override
        public void removeQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
            if ((0x4L & this.getFlags()) == 0x0L) {
                throw new UnsupportedOperationException("This session doesn't support queue management operations");
            }
            final Bundle bundle = new Bundle();
            bundle.putParcelable("android.support.v4.media.session.command.ARGUMENT_MEDIA_DESCRIPTION", (Parcelable)mediaDescriptionCompat);
            this.sendCommand("android.support.v4.media.session.command.REMOVE_QUEUE_ITEM", bundle, null);
        }
        
        @Override
        public void sendCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
            MediaControllerCompatApi21.sendCommand(this.mControllerObj, s, bundle, resultReceiver);
        }
        
        @Override
        public void setVolumeTo(final int n, final int n2) {
            MediaControllerCompatApi21.setVolumeTo(this.mControllerObj, n, n2);
        }
        
        @Override
        public final void unregisterCallback(final Callback callback) {
            MediaControllerCompatApi21.unregisterCallback(this.mControllerObj, callback.mCallbackObj);
            if (this.mExtraBinder != null) {
                try {
                    final ExtraCallback extraCallback = this.mCallbackMap.remove(callback);
                    if (extraCallback != null) {
                        this.mExtraBinder.unregisterCallbackListener(extraCallback);
                    }
                    return;
                }
                catch (RemoteException ex) {
                    Log.e("MediaControllerCompat", "Dead object in unregisterCallback.", (Throwable)ex);
                    return;
                }
            }
            synchronized (this.mPendingCallbacks) {
                this.mPendingCallbacks.remove(callback);
            }
        }
        
        private static class ExtraBinderRequestResultReceiver extends ResultReceiver
        {
            private WeakReference<MediaControllerImplApi21> mMediaControllerImpl;
            
            public ExtraBinderRequestResultReceiver(final MediaControllerImplApi21 mediaControllerImplApi21, final Handler handler) {
                super(handler);
                this.mMediaControllerImpl = new WeakReference<MediaControllerImplApi21>(mediaControllerImplApi21);
            }
            
            protected void onReceiveResult(final int n, final Bundle bundle) {
                final MediaControllerImplApi21 mediaControllerImplApi21 = this.mMediaControllerImpl.get();
                if (mediaControllerImplApi21 == null || bundle == null) {
                    return;
                }
                mediaControllerImplApi21.mExtraBinder = IMediaSession.Stub.asInterface(BundleCompat.getBinder(bundle, "android.support.v4.media.session.EXTRA_BINDER"));
                mediaControllerImplApi21.processPendingCallbacks();
            }
        }
        
        private class ExtraCallback extends Stub
        {
            private Callback mCallback;
            
            ExtraCallback(final Callback mCallback) {
                this.mCallback = mCallback;
            }
            
            public void onCaptioningEnabledChanged(final boolean b) throws RemoteException {
                this.mCallback.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ExtraCallback.this.mCallback.onCaptioningEnabledChanged(b);
                    }
                });
            }
            
            public void onEvent(final String s, final Bundle bundle) throws RemoteException {
                this.mCallback.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ExtraCallback.this.mCallback.onSessionEvent(s, bundle);
                    }
                });
            }
            
            public void onExtrasChanged(final Bundle bundle) throws RemoteException {
                throw new AssertionError();
            }
            
            public void onMetadataChanged(final MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                throw new AssertionError();
            }
            
            public void onPlaybackStateChanged(final PlaybackStateCompat playbackStateCompat) throws RemoteException {
                this.mCallback.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ExtraCallback.this.mCallback.onPlaybackStateChanged(playbackStateCompat);
                    }
                });
            }
            
            public void onQueueChanged(final List<MediaSessionCompat.QueueItem> list) throws RemoteException {
                throw new AssertionError();
            }
            
            public void onQueueTitleChanged(final CharSequence charSequence) throws RemoteException {
                throw new AssertionError();
            }
            
            public void onRepeatModeChanged(final int n) throws RemoteException {
                this.mCallback.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ExtraCallback.this.mCallback.onRepeatModeChanged(n);
                    }
                });
            }
            
            public void onSessionDestroyed() throws RemoteException {
                throw new AssertionError();
            }
            
            public void onShuffleModeChanged(final boolean b) throws RemoteException {
                this.mCallback.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ExtraCallback.this.mCallback.onShuffleModeChanged(b);
                    }
                });
            }
            
            public void onVolumeInfoChanged(final ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                throw new AssertionError();
            }
        }
    }
    
    @RequiresApi(23)
    static class MediaControllerImplApi23 extends MediaControllerImplApi21
    {
        public MediaControllerImplApi23(final Context context, final MediaSessionCompat.Token token) throws RemoteException {
            super(context, token);
        }
        
        public MediaControllerImplApi23(final Context context, final MediaSessionCompat mediaSessionCompat) {
            super(context, mediaSessionCompat);
        }
        
        @Override
        public TransportControls getTransportControls() {
            final Object transportControls = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
            if (transportControls != null) {
                return new TransportControlsApi23(transportControls);
            }
            return null;
        }
    }
    
    @RequiresApi(24)
    static class MediaControllerImplApi24 extends MediaControllerImplApi23
    {
        public MediaControllerImplApi24(final Context context, final MediaSessionCompat.Token token) throws RemoteException {
            super(context, token);
        }
        
        public MediaControllerImplApi24(final Context context, final MediaSessionCompat mediaSessionCompat) {
            super(context, mediaSessionCompat);
        }
        
        @Override
        public TransportControls getTransportControls() {
            final Object transportControls = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
            if (transportControls != null) {
                return new TransportControlsApi24(transportControls);
            }
            return null;
        }
    }
    
    static class MediaControllerImplBase implements MediaControllerImpl
    {
        private IMediaSession mBinder;
        private TransportControls mTransportControls;
        
        public MediaControllerImplBase(final MediaSessionCompat.Token token) {
            this.mBinder = IMediaSession.Stub.asInterface((IBinder)token.getToken());
        }
        
        @Override
        public void addQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
            try {
                if ((0x4L & this.mBinder.getFlags()) == 0x0L) {
                    throw new UnsupportedOperationException("This session doesn't support queue management operations");
                }
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in addQueueItem.", (Throwable)ex);
                return;
            }
            this.mBinder.addQueueItem(mediaDescriptionCompat);
        }
        
        @Override
        public void addQueueItem(final MediaDescriptionCompat mediaDescriptionCompat, final int n) {
            try {
                if ((0x4L & this.mBinder.getFlags()) == 0x0L) {
                    throw new UnsupportedOperationException("This session doesn't support queue management operations");
                }
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in addQueueItemAt.", (Throwable)ex);
                return;
            }
            this.mBinder.addQueueItemAt(mediaDescriptionCompat, n);
        }
        
        @Override
        public void adjustVolume(final int n, final int n2) {
            try {
                this.mBinder.adjustVolume(n, n2, null);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in adjustVolume.", (Throwable)ex);
            }
        }
        
        @Override
        public boolean dispatchMediaButtonEvent(final KeyEvent keyEvent) {
            if (keyEvent == null) {
                throw new IllegalArgumentException("event may not be null.");
            }
            try {
                this.mBinder.sendMediaButton(keyEvent);
                return false;
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in dispatchMediaButtonEvent.", (Throwable)ex);
                return false;
            }
        }
        
        @Override
        public Bundle getExtras() {
            try {
                return this.mBinder.getExtras();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getExtras.", (Throwable)ex);
                return null;
            }
        }
        
        @Override
        public long getFlags() {
            try {
                return this.mBinder.getFlags();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getFlags.", (Throwable)ex);
                return 0L;
            }
        }
        
        @Override
        public Object getMediaController() {
            return null;
        }
        
        @Override
        public MediaMetadataCompat getMetadata() {
            try {
                return this.mBinder.getMetadata();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getMetadata.", (Throwable)ex);
                return null;
            }
        }
        
        @Override
        public String getPackageName() {
            try {
                return this.mBinder.getPackageName();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getPackageName.", (Throwable)ex);
                return null;
            }
        }
        
        @Override
        public PlaybackInfo getPlaybackInfo() {
            try {
                final ParcelableVolumeInfo volumeAttributes = this.mBinder.getVolumeAttributes();
                return new PlaybackInfo(volumeAttributes.volumeType, volumeAttributes.audioStream, volumeAttributes.controlType, volumeAttributes.maxVolume, volumeAttributes.currentVolume);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getPlaybackInfo.", (Throwable)ex);
                return null;
            }
        }
        
        @Override
        public PlaybackStateCompat getPlaybackState() {
            try {
                return this.mBinder.getPlaybackState();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getPlaybackState.", (Throwable)ex);
                return null;
            }
        }
        
        @Override
        public List<MediaSessionCompat.QueueItem> getQueue() {
            try {
                return this.mBinder.getQueue();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getQueue.", (Throwable)ex);
                return null;
            }
        }
        
        @Override
        public CharSequence getQueueTitle() {
            try {
                return this.mBinder.getQueueTitle();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getQueueTitle.", (Throwable)ex);
                return null;
            }
        }
        
        @Override
        public int getRatingType() {
            try {
                return this.mBinder.getRatingType();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getRatingType.", (Throwable)ex);
                return 0;
            }
        }
        
        @Override
        public int getRepeatMode() {
            try {
                return this.mBinder.getRepeatMode();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getRepeatMode.", (Throwable)ex);
                return 0;
            }
        }
        
        @Override
        public PendingIntent getSessionActivity() {
            try {
                return this.mBinder.getLaunchPendingIntent();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in getSessionActivity.", (Throwable)ex);
                return null;
            }
        }
        
        @Override
        public TransportControls getTransportControls() {
            if (this.mTransportControls == null) {
                this.mTransportControls = new TransportControlsBase(this.mBinder);
            }
            return this.mTransportControls;
        }
        
        @Override
        public boolean isCaptioningEnabled() {
            try {
                return this.mBinder.isCaptioningEnabled();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in isCaptioningEnabled.", (Throwable)ex);
                return false;
            }
        }
        
        @Override
        public boolean isShuffleModeEnabled() {
            try {
                return this.mBinder.isShuffleModeEnabled();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in isShuffleModeEnabled.", (Throwable)ex);
                return false;
            }
        }
        
        @Override
        public void registerCallback(final Callback callback, final Handler handler) {
            if (callback == null) {
                throw new IllegalArgumentException("callback may not be null.");
            }
            try {
                this.mBinder.asBinder().linkToDeath((IBinder$DeathRecipient)callback, 0);
                this.mBinder.registerCallbackListener((IMediaControllerCallback)callback.mCallbackObj);
                callback.setHandler(handler);
                callback.mRegistered = true;
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in registerCallback.", (Throwable)ex);
                callback.onSessionDestroyed();
            }
        }
        
        @Override
        public void removeQueueItem(final MediaDescriptionCompat mediaDescriptionCompat) {
            try {
                if ((0x4L & this.mBinder.getFlags()) == 0x0L) {
                    throw new UnsupportedOperationException("This session doesn't support queue management operations");
                }
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in removeQueueItem.", (Throwable)ex);
                return;
            }
            this.mBinder.removeQueueItem(mediaDescriptionCompat);
        }
        
        @Override
        public void sendCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
            try {
                this.mBinder.sendCommand(s, bundle, new MediaSessionCompat.ResultReceiverWrapper(resultReceiver));
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in sendCommand.", (Throwable)ex);
            }
        }
        
        @Override
        public void setVolumeTo(final int n, final int n2) {
            try {
                this.mBinder.setVolumeTo(n, n2, null);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in setVolumeTo.", (Throwable)ex);
            }
        }
        
        @Override
        public void unregisterCallback(final Callback callback) {
            if (callback == null) {
                throw new IllegalArgumentException("callback may not be null.");
            }
            try {
                this.mBinder.unregisterCallbackListener((IMediaControllerCallback)callback.mCallbackObj);
                this.mBinder.asBinder().unlinkToDeath((IBinder$DeathRecipient)callback, 0);
                callback.mRegistered = false;
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in unregisterCallback.", (Throwable)ex);
            }
        }
    }
    
    public static final class PlaybackInfo
    {
        public static final int PLAYBACK_TYPE_LOCAL = 1;
        public static final int PLAYBACK_TYPE_REMOTE = 2;
        private final int mAudioStream;
        private final int mCurrentVolume;
        private final int mMaxVolume;
        private final int mPlaybackType;
        private final int mVolumeControl;
        
        PlaybackInfo(final int mPlaybackType, final int mAudioStream, final int mVolumeControl, final int mMaxVolume, final int mCurrentVolume) {
            this.mPlaybackType = mPlaybackType;
            this.mAudioStream = mAudioStream;
            this.mVolumeControl = mVolumeControl;
            this.mMaxVolume = mMaxVolume;
            this.mCurrentVolume = mCurrentVolume;
        }
        
        public int getAudioStream() {
            return this.mAudioStream;
        }
        
        public int getCurrentVolume() {
            return this.mCurrentVolume;
        }
        
        public int getMaxVolume() {
            return this.mMaxVolume;
        }
        
        public int getPlaybackType() {
            return this.mPlaybackType;
        }
        
        public int getVolumeControl() {
            return this.mVolumeControl;
        }
    }
    
    public abstract static class TransportControls
    {
        public abstract void fastForward();
        
        public abstract void pause();
        
        public abstract void play();
        
        public abstract void playFromMediaId(final String p0, final Bundle p1);
        
        public abstract void playFromSearch(final String p0, final Bundle p1);
        
        public abstract void playFromUri(final Uri p0, final Bundle p1);
        
        public abstract void prepare();
        
        public abstract void prepareFromMediaId(final String p0, final Bundle p1);
        
        public abstract void prepareFromSearch(final String p0, final Bundle p1);
        
        public abstract void prepareFromUri(final Uri p0, final Bundle p1);
        
        public abstract void rewind();
        
        public abstract void seekTo(final long p0);
        
        public abstract void sendCustomAction(final PlaybackStateCompat.CustomAction p0, final Bundle p1);
        
        public abstract void sendCustomAction(final String p0, final Bundle p1);
        
        public abstract void setCaptioningEnabled(final boolean p0);
        
        public abstract void setRating(final RatingCompat p0);
        
        public abstract void setRepeatMode(final int p0);
        
        public abstract void setShuffleModeEnabled(final boolean p0);
        
        public abstract void skipToNext();
        
        public abstract void skipToPrevious();
        
        public abstract void skipToQueueItem(final long p0);
        
        public abstract void stop();
    }
    
    static class TransportControlsApi21 extends TransportControls
    {
        protected final Object mControlsObj;
        
        public TransportControlsApi21(final Object mControlsObj) {
            this.mControlsObj = mControlsObj;
        }
        
        @Override
        public void fastForward() {
            MediaControllerCompatApi21.TransportControls.fastForward(this.mControlsObj);
        }
        
        @Override
        public void pause() {
            MediaControllerCompatApi21.TransportControls.pause(this.mControlsObj);
        }
        
        @Override
        public void play() {
            MediaControllerCompatApi21.TransportControls.play(this.mControlsObj);
        }
        
        @Override
        public void playFromMediaId(final String s, final Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.playFromMediaId(this.mControlsObj, s, bundle);
        }
        
        @Override
        public void playFromSearch(final String s, final Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.playFromSearch(this.mControlsObj, s, bundle);
        }
        
        @Override
        public void playFromUri(final Uri uri, final Bundle bundle) {
            if (uri == null || Uri.EMPTY.equals((Object)uri)) {
                throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
            }
            final Bundle bundle2 = new Bundle();
            bundle2.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", (Parcelable)uri);
            bundle2.putParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS", (Parcelable)bundle);
            this.sendCustomAction("android.support.v4.media.session.action.PLAY_FROM_URI", bundle2);
        }
        
        @Override
        public void prepare() {
            this.sendCustomAction("android.support.v4.media.session.action.PREPARE", null);
        }
        
        @Override
        public void prepareFromMediaId(final String s, final Bundle bundle) {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("android.support.v4.media.session.action.ARGUMENT_MEDIA_ID", s);
            bundle2.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", bundle);
            this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_MEDIA_ID", bundle2);
        }
        
        @Override
        public void prepareFromSearch(final String s, final Bundle bundle) {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("android.support.v4.media.session.action.ARGUMENT_QUERY", s);
            bundle2.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", bundle);
            this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_SEARCH", bundle2);
        }
        
        @Override
        public void prepareFromUri(final Uri uri, final Bundle bundle) {
            final Bundle bundle2 = new Bundle();
            bundle2.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", (Parcelable)uri);
            bundle2.putBundle("android.support.v4.media.session.action.ARGUMENT_EXTRAS", bundle);
            this.sendCustomAction("android.support.v4.media.session.action.PREPARE_FROM_URI", bundle2);
        }
        
        @Override
        public void rewind() {
            MediaControllerCompatApi21.TransportControls.rewind(this.mControlsObj);
        }
        
        @Override
        public void seekTo(final long n) {
            MediaControllerCompatApi21.TransportControls.seekTo(this.mControlsObj, n);
        }
        
        @Override
        public void sendCustomAction(final PlaybackStateCompat.CustomAction customAction, final Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, customAction.getAction(), bundle);
        }
        
        @Override
        public void sendCustomAction(final String s, final Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, s, bundle);
        }
        
        @Override
        public void setCaptioningEnabled(final boolean b) {
            final Bundle bundle = new Bundle();
            bundle.putBoolean("android.support.v4.media.session.action.ARGUMENT_CAPTIONING_ENABLED", b);
            this.sendCustomAction("android.support.v4.media.session.action.SET_CAPTIONING_ENABLED", bundle);
        }
        
        @Override
        public void setRating(final RatingCompat ratingCompat) {
            final Object mControlsObj = this.mControlsObj;
            Object rating;
            if (ratingCompat != null) {
                rating = ratingCompat.getRating();
            }
            else {
                rating = null;
            }
            MediaControllerCompatApi21.TransportControls.setRating(mControlsObj, rating);
        }
        
        @Override
        public void setRepeatMode(final int n) {
            final Bundle bundle = new Bundle();
            bundle.putInt("android.support.v4.media.session.action.ARGUMENT_REPEAT_MODE", n);
            this.sendCustomAction("android.support.v4.media.session.action.SET_REPEAT_MODE", bundle);
        }
        
        @Override
        public void setShuffleModeEnabled(final boolean b) {
            final Bundle bundle = new Bundle();
            bundle.putBoolean("android.support.v4.media.session.action.ARGUMENT_SHUFFLE_MODE_ENABLED", b);
            this.sendCustomAction("android.support.v4.media.session.action.SET_SHUFFLE_MODE_ENABLED", bundle);
        }
        
        @Override
        public void skipToNext() {
            MediaControllerCompatApi21.TransportControls.skipToNext(this.mControlsObj);
        }
        
        @Override
        public void skipToPrevious() {
            MediaControllerCompatApi21.TransportControls.skipToPrevious(this.mControlsObj);
        }
        
        @Override
        public void skipToQueueItem(final long n) {
            MediaControllerCompatApi21.TransportControls.skipToQueueItem(this.mControlsObj, n);
        }
        
        @Override
        public void stop() {
            MediaControllerCompatApi21.TransportControls.stop(this.mControlsObj);
        }
    }
    
    @RequiresApi(23)
    static class TransportControlsApi23 extends TransportControlsApi21
    {
        public TransportControlsApi23(final Object o) {
            super(o);
        }
        
        @Override
        public void playFromUri(final Uri uri, final Bundle bundle) {
            MediaControllerCompatApi23.TransportControls.playFromUri(this.mControlsObj, uri, bundle);
        }
    }
    
    @RequiresApi(24)
    static class TransportControlsApi24 extends TransportControlsApi23
    {
        public TransportControlsApi24(final Object o) {
            super(o);
        }
        
        @Override
        public void prepare() {
            MediaControllerCompatApi24.TransportControls.prepare(this.mControlsObj);
        }
        
        @Override
        public void prepareFromMediaId(final String s, final Bundle bundle) {
            MediaControllerCompatApi24.TransportControls.prepareFromMediaId(this.mControlsObj, s, bundle);
        }
        
        @Override
        public void prepareFromSearch(final String s, final Bundle bundle) {
            MediaControllerCompatApi24.TransportControls.prepareFromSearch(this.mControlsObj, s, bundle);
        }
        
        @Override
        public void prepareFromUri(final Uri uri, final Bundle bundle) {
            MediaControllerCompatApi24.TransportControls.prepareFromUri(this.mControlsObj, uri, bundle);
        }
    }
    
    static class TransportControlsBase extends TransportControls
    {
        private IMediaSession mBinder;
        
        public TransportControlsBase(final IMediaSession mBinder) {
            this.mBinder = mBinder;
        }
        
        @Override
        public void fastForward() {
            try {
                this.mBinder.fastForward();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in fastForward.", (Throwable)ex);
            }
        }
        
        @Override
        public void pause() {
            try {
                this.mBinder.pause();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in pause.", (Throwable)ex);
            }
        }
        
        @Override
        public void play() {
            try {
                this.mBinder.play();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in play.", (Throwable)ex);
            }
        }
        
        @Override
        public void playFromMediaId(final String s, final Bundle bundle) {
            try {
                this.mBinder.playFromMediaId(s, bundle);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in playFromMediaId.", (Throwable)ex);
            }
        }
        
        @Override
        public void playFromSearch(final String s, final Bundle bundle) {
            try {
                this.mBinder.playFromSearch(s, bundle);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in playFromSearch.", (Throwable)ex);
            }
        }
        
        @Override
        public void playFromUri(final Uri uri, final Bundle bundle) {
            try {
                this.mBinder.playFromUri(uri, bundle);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in playFromUri.", (Throwable)ex);
            }
        }
        
        @Override
        public void prepare() {
            try {
                this.mBinder.prepare();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in prepare.", (Throwable)ex);
            }
        }
        
        @Override
        public void prepareFromMediaId(final String s, final Bundle bundle) {
            try {
                this.mBinder.prepareFromMediaId(s, bundle);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in prepareFromMediaId.", (Throwable)ex);
            }
        }
        
        @Override
        public void prepareFromSearch(final String s, final Bundle bundle) {
            try {
                this.mBinder.prepareFromSearch(s, bundle);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in prepareFromSearch.", (Throwable)ex);
            }
        }
        
        @Override
        public void prepareFromUri(final Uri uri, final Bundle bundle) {
            try {
                this.mBinder.prepareFromUri(uri, bundle);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in prepareFromUri.", (Throwable)ex);
            }
        }
        
        @Override
        public void rewind() {
            try {
                this.mBinder.rewind();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in rewind.", (Throwable)ex);
            }
        }
        
        @Override
        public void seekTo(final long n) {
            try {
                this.mBinder.seekTo(n);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in seekTo.", (Throwable)ex);
            }
        }
        
        @Override
        public void sendCustomAction(final PlaybackStateCompat.CustomAction customAction, final Bundle bundle) {
            this.sendCustomAction(customAction.getAction(), bundle);
        }
        
        @Override
        public void sendCustomAction(final String s, final Bundle bundle) {
            try {
                this.mBinder.sendCustomAction(s, bundle);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in sendCustomAction.", (Throwable)ex);
            }
        }
        
        @Override
        public void setCaptioningEnabled(final boolean captioningEnabled) {
            try {
                this.mBinder.setCaptioningEnabled(captioningEnabled);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in setCaptioningEnabled.", (Throwable)ex);
            }
        }
        
        @Override
        public void setRating(final RatingCompat ratingCompat) {
            try {
                this.mBinder.rate(ratingCompat);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in setRating.", (Throwable)ex);
            }
        }
        
        @Override
        public void setRepeatMode(final int repeatMode) {
            try {
                this.mBinder.setRepeatMode(repeatMode);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in setRepeatMode.", (Throwable)ex);
            }
        }
        
        @Override
        public void setShuffleModeEnabled(final boolean shuffleModeEnabled) {
            try {
                this.mBinder.setShuffleModeEnabled(shuffleModeEnabled);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in setShuffleModeEnabled.", (Throwable)ex);
            }
        }
        
        @Override
        public void skipToNext() {
            try {
                this.mBinder.next();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in skipToNext.", (Throwable)ex);
            }
        }
        
        @Override
        public void skipToPrevious() {
            try {
                this.mBinder.previous();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in skipToPrevious.", (Throwable)ex);
            }
        }
        
        @Override
        public void skipToQueueItem(final long n) {
            try {
                this.mBinder.skipToQueueItem(n);
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in skipToQueueItem.", (Throwable)ex);
            }
        }
        
        @Override
        public void stop() {
            try {
                this.mBinder.stop();
            }
            catch (RemoteException ex) {
                Log.e("MediaControllerCompat", "Dead object in stop.", (Throwable)ex);
            }
        }
    }
}
