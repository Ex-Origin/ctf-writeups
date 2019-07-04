// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.util.LinkedList;
import android.os.Message;
import java.util.List;
import android.content.pm.ResolveInfo;
import android.os.DeadObjectException;
import java.util.Iterator;
import android.util.Log;
import android.content.Intent;
import java.util.HashMap;
import java.util.Map;
import android.os.HandlerThread;
import android.os.Handler;
import android.content.ServiceConnection;
import android.os.Handler$Callback;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.os.RemoteException;
import android.os.Bundle;
import android.app.Notification;
import android.content.ComponentName;
import android.provider.Settings$Secure;
import android.os.Build$VERSION;
import java.util.HashSet;
import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.GuardedBy;
import java.util.Set;

public final class NotificationManagerCompat
{
    public static final String ACTION_BIND_SIDE_CHANNEL = "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";
    public static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
    private static final Impl IMPL;
    public static final int IMPORTANCE_DEFAULT = 3;
    public static final int IMPORTANCE_HIGH = 4;
    public static final int IMPORTANCE_LOW = 2;
    public static final int IMPORTANCE_MAX = 5;
    public static final int IMPORTANCE_MIN = 1;
    public static final int IMPORTANCE_NONE = 0;
    public static final int IMPORTANCE_UNSPECIFIED = -1000;
    static final int MAX_SIDE_CHANNEL_SDK_VERSION = 19;
    private static final String SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;
    private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;
    private static final String TAG = "NotifManCompat";
    @GuardedBy("sEnabledNotificationListenersLock")
    private static Set<String> sEnabledNotificationListenerPackages;
    @GuardedBy("sEnabledNotificationListenersLock")
    private static String sEnabledNotificationListeners;
    private static final Object sEnabledNotificationListenersLock;
    private static final Object sLock;
    @GuardedBy("sLock")
    private static SideChannelManager sSideChannelManager;
    private final Context mContext;
    private final NotificationManager mNotificationManager;
    
    static {
        sEnabledNotificationListenersLock = new Object();
        NotificationManagerCompat.sEnabledNotificationListenerPackages = new HashSet<String>();
        sLock = new Object();
        if (Build$VERSION.SDK_INT >= 24) {
            IMPL = (Impl)new ImplApi24();
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (Impl)new ImplKitKat();
            return;
        }
        IMPL = (Impl)new ImplBase();
    }
    
    private NotificationManagerCompat(final Context mContext) {
        this.mContext = mContext;
        this.mNotificationManager = (NotificationManager)this.mContext.getSystemService("notification");
    }
    
    public static NotificationManagerCompat from(final Context context) {
        return new NotificationManagerCompat(context);
    }
    
    public static Set<String> getEnabledListenerPackages(Context sEnabledNotificationListenersLock) {
        final String string = Settings$Secure.getString(sEnabledNotificationListenersLock.getContentResolver(), "enabled_notification_listeners");
        sEnabledNotificationListenersLock = (Context)NotificationManagerCompat.sEnabledNotificationListenersLock;
        // monitorenter(sEnabledNotificationListenersLock)
        Label_0100: {
            if (string == null) {
                break Label_0100;
            }
            while (true) {
                while (true) {
                    int n = 0;
                    Label_0113: {
                        try {
                            if (!string.equals(NotificationManagerCompat.sEnabledNotificationListeners)) {
                                final String[] split = string.split(":");
                                final HashSet sEnabledNotificationListenerPackages = new HashSet<String>(split.length);
                                final int length = split.length;
                                n = 0;
                                if (n < length) {
                                    final ComponentName unflattenFromString = ComponentName.unflattenFromString(split[n]);
                                    if (unflattenFromString != null) {
                                        sEnabledNotificationListenerPackages.add(unflattenFromString.getPackageName());
                                    }
                                    break Label_0113;
                                }
                                else {
                                    NotificationManagerCompat.sEnabledNotificationListenerPackages = (Set<String>)sEnabledNotificationListenerPackages;
                                    NotificationManagerCompat.sEnabledNotificationListeners = string;
                                }
                            }
                            return NotificationManagerCompat.sEnabledNotificationListenerPackages;
                        }
                        finally {
                        }
                        // monitorexit(sEnabledNotificationListenersLock)
                    }
                    ++n;
                    continue;
                }
            }
        }
    }
    
    private void pushSideChannelQueue(final Task task) {
        synchronized (NotificationManagerCompat.sLock) {
            if (NotificationManagerCompat.sSideChannelManager == null) {
                NotificationManagerCompat.sSideChannelManager = new SideChannelManager(this.mContext.getApplicationContext());
            }
            NotificationManagerCompat.sSideChannelManager.queueTask(task);
        }
    }
    
    private static boolean useSideChannelForNotification(final Notification notification) {
        final Bundle extras = NotificationCompat.getExtras(notification);
        return extras != null && extras.getBoolean("android.support.useSideChannel");
    }
    
    public boolean areNotificationsEnabled() {
        return NotificationManagerCompat.IMPL.areNotificationsEnabled(this.mContext, this.mNotificationManager);
    }
    
    public void cancel(final int n) {
        this.cancel(null, n);
    }
    
    public void cancel(final String s, final int n) {
        NotificationManagerCompat.IMPL.cancelNotification(this.mNotificationManager, s, n);
        if (Build$VERSION.SDK_INT <= 19) {
            this.pushSideChannelQueue((Task)new CancelTask(this.mContext.getPackageName(), n, s));
        }
    }
    
    public void cancelAll() {
        this.mNotificationManager.cancelAll();
        if (Build$VERSION.SDK_INT <= 19) {
            this.pushSideChannelQueue((Task)new CancelTask(this.mContext.getPackageName()));
        }
    }
    
    public int getImportance() {
        return NotificationManagerCompat.IMPL.getImportance(this.mNotificationManager);
    }
    
    public void notify(final int n, final Notification notification) {
        this.notify(null, n, notification);
    }
    
    public void notify(final String s, final int n, final Notification notification) {
        if (useSideChannelForNotification(notification)) {
            this.pushSideChannelQueue((Task)new NotifyTask(this.mContext.getPackageName(), n, s, notification));
            NotificationManagerCompat.IMPL.cancelNotification(this.mNotificationManager, s, n);
            return;
        }
        NotificationManagerCompat.IMPL.postNotification(this.mNotificationManager, s, n, notification);
    }
    
    private static class CancelTask implements Task
    {
        final boolean all;
        final int id;
        final String packageName;
        final String tag;
        
        CancelTask(final String packageName) {
            this.packageName = packageName;
            this.id = 0;
            this.tag = null;
            this.all = true;
        }
        
        CancelTask(final String packageName, final int id, final String tag) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
            this.all = false;
        }
        
        @Override
        public void send(final INotificationSideChannel notificationSideChannel) throws RemoteException {
            if (this.all) {
                notificationSideChannel.cancelAll(this.packageName);
                return;
            }
            notificationSideChannel.cancel(this.packageName, this.id, this.tag);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("CancelTask[");
            sb.append("packageName:").append(this.packageName);
            sb.append(", id:").append(this.id);
            sb.append(", tag:").append(this.tag);
            sb.append(", all:").append(this.all);
            sb.append("]");
            return sb.toString();
        }
    }
    
    interface Impl
    {
        boolean areNotificationsEnabled(final Context p0, final NotificationManager p1);
        
        void cancelNotification(final NotificationManager p0, final String p1, final int p2);
        
        int getImportance(final NotificationManager p0);
        
        void postNotification(final NotificationManager p0, final String p1, final int p2, final Notification p3);
    }
    
    @RequiresApi(24)
    static class ImplApi24 extends ImplKitKat
    {
        @Override
        public boolean areNotificationsEnabled(final Context context, final NotificationManager notificationManager) {
            return NotificationManagerCompatApi24.areNotificationsEnabled(notificationManager);
        }
        
        @Override
        public int getImportance(final NotificationManager notificationManager) {
            return NotificationManagerCompatApi24.getImportance(notificationManager);
        }
    }
    
    static class ImplBase implements Impl
    {
        @Override
        public boolean areNotificationsEnabled(final Context context, final NotificationManager notificationManager) {
            return true;
        }
        
        @Override
        public void cancelNotification(final NotificationManager notificationManager, final String s, final int n) {
            notificationManager.cancel(s, n);
        }
        
        @Override
        public int getImportance(final NotificationManager notificationManager) {
            return -1000;
        }
        
        @Override
        public void postNotification(final NotificationManager notificationManager, final String s, final int n, final Notification notification) {
            notificationManager.notify(s, n, notification);
        }
    }
    
    @RequiresApi(19)
    static class ImplKitKat extends ImplBase
    {
        @Override
        public boolean areNotificationsEnabled(final Context context, final NotificationManager notificationManager) {
            return NotificationManagerCompatKitKat.areNotificationsEnabled(context);
        }
    }
    
    private static class NotifyTask implements Task
    {
        final int id;
        final Notification notif;
        final String packageName;
        final String tag;
        
        NotifyTask(final String packageName, final int id, final String tag, final Notification notif) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
            this.notif = notif;
        }
        
        @Override
        public void send(final INotificationSideChannel notificationSideChannel) throws RemoteException {
            notificationSideChannel.notify(this.packageName, this.id, this.tag, this.notif);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("NotifyTask[");
            sb.append("packageName:").append(this.packageName);
            sb.append(", id:").append(this.id);
            sb.append(", tag:").append(this.tag);
            sb.append("]");
            return sb.toString();
        }
    }
    
    private static class ServiceConnectedEvent
    {
        final ComponentName componentName;
        final IBinder iBinder;
        
        ServiceConnectedEvent(final ComponentName componentName, final IBinder iBinder) {
            this.componentName = componentName;
            this.iBinder = iBinder;
        }
    }
    
    private static class SideChannelManager implements Handler$Callback, ServiceConnection
    {
        private static final int MSG_QUEUE_TASK = 0;
        private static final int MSG_RETRY_LISTENER_QUEUE = 3;
        private static final int MSG_SERVICE_CONNECTED = 1;
        private static final int MSG_SERVICE_DISCONNECTED = 2;
        private Set<String> mCachedEnabledPackages;
        private final Context mContext;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread;
        private final Map<ComponentName, ListenerRecord> mRecordMap;
        
        public SideChannelManager(final Context mContext) {
            this.mRecordMap = new HashMap<ComponentName, ListenerRecord>();
            this.mCachedEnabledPackages = new HashSet<String>();
            this.mContext = mContext;
            (this.mHandlerThread = new HandlerThread("NotificationManagerCompat")).start();
            this.mHandler = new Handler(this.mHandlerThread.getLooper(), (Handler$Callback)this);
        }
        
        private boolean ensureServiceBound(final ListenerRecord listenerRecord) {
            if (listenerRecord.bound) {
                return true;
            }
            listenerRecord.bound = this.mContext.bindService(new Intent("android.support.BIND_NOTIFICATION_SIDE_CHANNEL").setComponent(listenerRecord.componentName), (ServiceConnection)this, 33);
            if (listenerRecord.bound) {
                listenerRecord.retryCount = 0;
            }
            else {
                Log.w("NotifManCompat", "Unable to bind to listener " + listenerRecord.componentName);
                this.mContext.unbindService((ServiceConnection)this);
            }
            return listenerRecord.bound;
        }
        
        private void ensureServiceUnbound(final ListenerRecord listenerRecord) {
            if (listenerRecord.bound) {
                this.mContext.unbindService((ServiceConnection)this);
                listenerRecord.bound = false;
            }
            listenerRecord.service = null;
        }
        
        private void handleQueueTask(final Task task) {
            this.updateListenerMap();
            for (final ListenerRecord listenerRecord : this.mRecordMap.values()) {
                listenerRecord.taskQueue.add(task);
                this.processListenerQueue(listenerRecord);
            }
        }
        
        private void handleRetryListenerQueue(final ComponentName componentName) {
            final ListenerRecord listenerRecord = this.mRecordMap.get(componentName);
            if (listenerRecord != null) {
                this.processListenerQueue(listenerRecord);
            }
        }
        
        private void handleServiceConnected(final ComponentName componentName, final IBinder binder) {
            final ListenerRecord listenerRecord = this.mRecordMap.get(componentName);
            if (listenerRecord != null) {
                listenerRecord.service = INotificationSideChannel.Stub.asInterface(binder);
                listenerRecord.retryCount = 0;
                this.processListenerQueue(listenerRecord);
            }
        }
        
        private void handleServiceDisconnected(final ComponentName componentName) {
            final ListenerRecord listenerRecord = this.mRecordMap.get(componentName);
            if (listenerRecord != null) {
                this.ensureServiceUnbound(listenerRecord);
            }
        }
        
        private void processListenerQueue(final ListenerRecord listenerRecord) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                Log.d("NotifManCompat", "Processing component " + listenerRecord.componentName + ", " + listenerRecord.taskQueue.size() + " queued tasks");
            }
            if (!listenerRecord.taskQueue.isEmpty()) {
                if (!this.ensureServiceBound(listenerRecord) || listenerRecord.service == null) {
                    this.scheduleListenerRetry(listenerRecord);
                    return;
                }
                Label_0141: {
                    break Label_0141;
                    while (true) {
                        try {
                            Task task = null;
                            do {
                                if (Log.isLoggable("NotifManCompat", 3)) {
                                    Log.d("NotifManCompat", "Sending task " + task);
                                }
                                task.send(listenerRecord.service);
                                listenerRecord.taskQueue.remove();
                                task = listenerRecord.taskQueue.peek();
                            } while (task != null);
                            if (!listenerRecord.taskQueue.isEmpty()) {
                                this.scheduleListenerRetry(listenerRecord);
                            }
                        }
                        catch (DeadObjectException ex2) {
                            if (Log.isLoggable("NotifManCompat", 3)) {
                                Log.d("NotifManCompat", "Remote service has died: " + listenerRecord.componentName);
                            }
                            continue;
                        }
                        catch (RemoteException ex) {
                            Log.w("NotifManCompat", "RemoteException communicating with " + listenerRecord.componentName, (Throwable)ex);
                            continue;
                        }
                        break;
                    }
                }
            }
        }
        
        private void scheduleListenerRetry(final ListenerRecord listenerRecord) {
            if (this.mHandler.hasMessages(3, (Object)listenerRecord.componentName)) {
                return;
            }
            ++listenerRecord.retryCount;
            if (listenerRecord.retryCount > 6) {
                Log.w("NotifManCompat", "Giving up on delivering " + listenerRecord.taskQueue.size() + " tasks to " + listenerRecord.componentName + " after " + listenerRecord.retryCount + " retries");
                listenerRecord.taskQueue.clear();
                return;
            }
            final int n = (1 << listenerRecord.retryCount - 1) * 1000;
            if (Log.isLoggable("NotifManCompat", 3)) {
                Log.d("NotifManCompat", "Scheduling retry for " + n + " ms");
            }
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3, (Object)listenerRecord.componentName), (long)n);
        }
        
        private void updateListenerMap() {
            final Set<String> enabledListenerPackages = NotificationManagerCompat.getEnabledListenerPackages(this.mContext);
            if (!enabledListenerPackages.equals(this.mCachedEnabledPackages)) {
                this.mCachedEnabledPackages = enabledListenerPackages;
                final List queryIntentServices = this.mContext.getPackageManager().queryIntentServices(new Intent().setAction("android.support.BIND_NOTIFICATION_SIDE_CHANNEL"), 4);
                final HashSet<ComponentName> set = new HashSet<ComponentName>();
                for (final ResolveInfo resolveInfo : queryIntentServices) {
                    if (enabledListenerPackages.contains(resolveInfo.serviceInfo.packageName)) {
                        final ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
                        if (resolveInfo.serviceInfo.permission != null) {
                            Log.w("NotifManCompat", "Permission present on component " + componentName + ", not adding listener record.");
                        }
                        else {
                            set.add(componentName);
                        }
                    }
                }
                for (final ComponentName componentName2 : set) {
                    if (!this.mRecordMap.containsKey(componentName2)) {
                        if (Log.isLoggable("NotifManCompat", 3)) {
                            Log.d("NotifManCompat", "Adding listener record for " + componentName2);
                        }
                        this.mRecordMap.put(componentName2, new ListenerRecord(componentName2));
                    }
                }
                final Iterator<Map.Entry<ComponentName, ListenerRecord>> iterator3 = this.mRecordMap.entrySet().iterator();
                while (iterator3.hasNext()) {
                    final Map.Entry<ComponentName, ListenerRecord> entry = iterator3.next();
                    if (!set.contains(entry.getKey())) {
                        if (Log.isLoggable("NotifManCompat", 3)) {
                            Log.d("NotifManCompat", "Removing listener record for " + entry.getKey());
                        }
                        this.ensureServiceUnbound(entry.getValue());
                        iterator3.remove();
                    }
                }
            }
        }
        
        public boolean handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    return false;
                }
                case 0: {
                    this.handleQueueTask((Task)message.obj);
                    return true;
                }
                case 1: {
                    final ServiceConnectedEvent serviceConnectedEvent = (ServiceConnectedEvent)message.obj;
                    this.handleServiceConnected(serviceConnectedEvent.componentName, serviceConnectedEvent.iBinder);
                    return true;
                }
                case 2: {
                    this.handleServiceDisconnected((ComponentName)message.obj);
                    return true;
                }
                case 3: {
                    this.handleRetryListenerQueue((ComponentName)message.obj);
                    return true;
                }
            }
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                Log.d("NotifManCompat", "Connected to service " + componentName);
            }
            this.mHandler.obtainMessage(1, (Object)new ServiceConnectedEvent(componentName, binder)).sendToTarget();
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                Log.d("NotifManCompat", "Disconnected from service " + componentName);
            }
            this.mHandler.obtainMessage(2, (Object)componentName).sendToTarget();
        }
        
        public void queueTask(final Task task) {
            this.mHandler.obtainMessage(0, (Object)task).sendToTarget();
        }
        
        private static class ListenerRecord
        {
            public boolean bound;
            public final ComponentName componentName;
            public int retryCount;
            public INotificationSideChannel service;
            public LinkedList<Task> taskQueue;
            
            public ListenerRecord(final ComponentName componentName) {
                this.bound = false;
                this.taskQueue = new LinkedList<Task>();
                this.retryCount = 0;
                this.componentName = componentName;
            }
        }
    }
    
    private interface Task
    {
        void send(final INotificationSideChannel p0) throws RemoteException;
    }
}
