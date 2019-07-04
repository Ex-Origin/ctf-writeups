// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.lang.reflect.Modifier;
import android.support.v4.util.DebugUtils;
import android.support.v4.content.Loader;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.util.Log;
import android.os.Bundle;
import android.support.v4.util.SparseArrayCompat;

class LoaderManagerImpl extends LoaderManager
{
    static boolean DEBUG = false;
    static final String TAG = "LoaderManager";
    boolean mCreatingLoader;
    FragmentHostCallback mHost;
    final SparseArrayCompat<LoaderInfo> mInactiveLoaders;
    final SparseArrayCompat<LoaderInfo> mLoaders;
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;
    final String mWho;
    
    static {
        LoaderManagerImpl.DEBUG = false;
    }
    
    LoaderManagerImpl(final String mWho, final FragmentHostCallback mHost, final boolean mStarted) {
        this.mLoaders = new SparseArrayCompat<LoaderInfo>();
        this.mInactiveLoaders = new SparseArrayCompat<LoaderInfo>();
        this.mWho = mWho;
        this.mHost = mHost;
        this.mStarted = mStarted;
    }
    
    private LoaderInfo createAndInstallLoader(final int n, final Bundle bundle, final LoaderCallbacks<Object> loaderCallbacks) {
        try {
            this.mCreatingLoader = true;
            final LoaderInfo loader = this.createLoader(n, bundle, loaderCallbacks);
            this.installLoader(loader);
            return loader;
        }
        finally {
            this.mCreatingLoader = false;
        }
    }
    
    private LoaderInfo createLoader(final int n, final Bundle bundle, final LoaderCallbacks<Object> loaderCallbacks) {
        final LoaderInfo loaderInfo = new LoaderInfo(n, bundle, loaderCallbacks);
        loaderInfo.mLoader = loaderCallbacks.onCreateLoader(n, bundle);
        return loaderInfo;
    }
    
    @Override
    public void destroyLoader(int indexOfKey) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "destroyLoader in " + this + " of " + indexOfKey);
        }
        final int indexOfKey2 = this.mLoaders.indexOfKey(indexOfKey);
        if (indexOfKey2 >= 0) {
            final LoaderInfo loaderInfo = this.mLoaders.valueAt(indexOfKey2);
            this.mLoaders.removeAt(indexOfKey2);
            loaderInfo.destroy();
        }
        indexOfKey = this.mInactiveLoaders.indexOfKey(indexOfKey);
        if (indexOfKey >= 0) {
            final LoaderInfo loaderInfo2 = this.mInactiveLoaders.valueAt(indexOfKey);
            this.mInactiveLoaders.removeAt(indexOfKey);
            loaderInfo2.destroy();
        }
        if (this.mHost != null && !this.hasRunningLoaders()) {
            this.mHost.mFragmentManager.startPendingDeferredFragments();
        }
    }
    
    void doDestroy() {
        if (!this.mRetaining) {
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "Destroying Active in " + this);
            }
            for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
                this.mLoaders.valueAt(i).destroy();
            }
            this.mLoaders.clear();
        }
        if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "Destroying Inactive in " + this);
        }
        for (int j = this.mInactiveLoaders.size() - 1; j >= 0; --j) {
            this.mInactiveLoaders.valueAt(j).destroy();
        }
        this.mInactiveLoaders.clear();
        this.mHost = null;
    }
    
    void doReportNextStart() {
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).mReportNextStart = true;
        }
    }
    
    void doReportStart() {
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).reportStart();
        }
    }
    
    void doRetain() {
        if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "Retaining in " + this);
        }
        if (!this.mStarted) {
            final RuntimeException ex = new RuntimeException("here");
            ex.fillInStackTrace();
            Log.w("LoaderManager", "Called doRetain when not started: " + this, (Throwable)ex);
        }
        else {
            this.mRetaining = true;
            this.mStarted = false;
            for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
                this.mLoaders.valueAt(i).retain();
            }
        }
    }
    
    void doStart() {
        if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "Starting in " + this);
        }
        if (this.mStarted) {
            final RuntimeException ex = new RuntimeException("here");
            ex.fillInStackTrace();
            Log.w("LoaderManager", "Called doStart when already started: " + this, (Throwable)ex);
        }
        else {
            this.mStarted = true;
            for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
                this.mLoaders.valueAt(i).start();
            }
        }
    }
    
    void doStop() {
        if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "Stopping in " + this);
        }
        if (!this.mStarted) {
            final RuntimeException ex = new RuntimeException("here");
            ex.fillInStackTrace();
            Log.w("LoaderManager", "Called doStop when not started: " + this, (Throwable)ex);
            return;
        }
        for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
            this.mLoaders.valueAt(i).stop();
        }
        this.mStarted = false;
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        if (this.mLoaders.size() > 0) {
            printWriter.print(s);
            printWriter.println("Active Loaders:");
            final String string = s + "    ";
            for (int i = 0; i < this.mLoaders.size(); ++i) {
                final LoaderInfo loaderInfo = this.mLoaders.valueAt(i);
                printWriter.print(s);
                printWriter.print("  #");
                printWriter.print(this.mLoaders.keyAt(i));
                printWriter.print(": ");
                printWriter.println(loaderInfo.toString());
                loaderInfo.dump(string, fileDescriptor, printWriter, array);
            }
        }
        if (this.mInactiveLoaders.size() > 0) {
            printWriter.print(s);
            printWriter.println("Inactive Loaders:");
            final String string2 = s + "    ";
            for (int j = 0; j < this.mInactiveLoaders.size(); ++j) {
                final LoaderInfo loaderInfo2 = this.mInactiveLoaders.valueAt(j);
                printWriter.print(s);
                printWriter.print("  #");
                printWriter.print(this.mInactiveLoaders.keyAt(j));
                printWriter.print(": ");
                printWriter.println(loaderInfo2.toString());
                loaderInfo2.dump(string2, fileDescriptor, printWriter, array);
            }
        }
    }
    
    void finishRetain() {
        if (this.mRetaining) {
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "Finished Retaining in " + this);
            }
            this.mRetaining = false;
            for (int i = this.mLoaders.size() - 1; i >= 0; --i) {
                this.mLoaders.valueAt(i).finishRetain();
            }
        }
    }
    
    @Override
    public <D> Loader<D> getLoader(final int n) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        final LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (loaderInfo == null) {
            return null;
        }
        if (loaderInfo.mPendingLoader != null) {
            return (Loader<D>)loaderInfo.mPendingLoader.mLoader;
        }
        return (Loader<D>)loaderInfo.mLoader;
    }
    
    @Override
    public boolean hasRunningLoaders() {
        boolean b = false;
        for (int size = this.mLoaders.size(), i = 0; i < size; ++i) {
            final LoaderInfo loaderInfo = this.mLoaders.valueAt(i);
            b |= (loaderInfo.mStarted && !loaderInfo.mDeliveredData);
        }
        return b;
    }
    
    @Override
    public <D> Loader<D> initLoader(final int n, final Bundle bundle, final LoaderCallbacks<D> mCallbacks) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        final LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "initLoader in " + this + ": args=" + bundle);
        }
        LoaderInfo andInstallLoader;
        if (loaderInfo == null) {
            final LoaderInfo loaderInfo2 = andInstallLoader = this.createAndInstallLoader(n, bundle, (LoaderCallbacks<Object>)mCallbacks);
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "  Created new loader " + loaderInfo2);
                andInstallLoader = loaderInfo2;
            }
        }
        else {
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "  Re-using existing loader " + loaderInfo);
            }
            loaderInfo.mCallbacks = (LoaderCallbacks<Object>)mCallbacks;
            andInstallLoader = loaderInfo;
        }
        if (andInstallLoader.mHaveData && this.mStarted) {
            andInstallLoader.callOnLoadFinished(andInstallLoader.mLoader, andInstallLoader.mData);
        }
        return (Loader<D>)andInstallLoader.mLoader;
    }
    
    void installLoader(final LoaderInfo loaderInfo) {
        this.mLoaders.put(loaderInfo.mId, loaderInfo);
        if (this.mStarted) {
            loaderInfo.start();
        }
    }
    
    @Override
    public <D> Loader<D> restartLoader(final int n, final Bundle bundle, final LoaderCallbacks<D> loaderCallbacks) {
        if (this.mCreatingLoader) {
            throw new IllegalStateException("Called while creating a loader");
        }
        final LoaderInfo loaderInfo = this.mLoaders.get(n);
        if (LoaderManagerImpl.DEBUG) {
            Log.v("LoaderManager", "restartLoader in " + this + ": args=" + bundle);
        }
        if (loaderInfo != null) {
            final LoaderInfo loaderInfo2 = this.mInactiveLoaders.get(n);
            if (loaderInfo2 != null) {
                if (loaderInfo.mHaveData) {
                    if (LoaderManagerImpl.DEBUG) {
                        Log.v("LoaderManager", "  Removing last inactive loader: " + loaderInfo);
                    }
                    loaderInfo2.mDeliveredData = false;
                    loaderInfo2.destroy();
                    loaderInfo.mLoader.abandon();
                    this.mInactiveLoaders.put(n, loaderInfo);
                }
                else {
                    if (loaderInfo.cancel()) {
                        if (LoaderManagerImpl.DEBUG) {
                            Log.v("LoaderManager", "  Current loader is running; configuring pending loader");
                        }
                        if (loaderInfo.mPendingLoader != null) {
                            if (LoaderManagerImpl.DEBUG) {
                                Log.v("LoaderManager", "  Removing pending loader: " + loaderInfo.mPendingLoader);
                            }
                            loaderInfo.mPendingLoader.destroy();
                            loaderInfo.mPendingLoader = null;
                        }
                        if (LoaderManagerImpl.DEBUG) {
                            Log.v("LoaderManager", "  Enqueuing as new pending loader");
                        }
                        loaderInfo.mPendingLoader = this.createLoader(n, bundle, (LoaderCallbacks<Object>)loaderCallbacks);
                        return (Loader<D>)loaderInfo.mPendingLoader.mLoader;
                    }
                    if (LoaderManagerImpl.DEBUG) {
                        Log.v("LoaderManager", "  Current loader is stopped; replacing");
                    }
                    this.mLoaders.put(n, null);
                    loaderInfo.destroy();
                }
            }
            else {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Making last loader inactive: " + loaderInfo);
                }
                loaderInfo.mLoader.abandon();
                this.mInactiveLoaders.put(n, loaderInfo);
            }
        }
        return (Loader<D>)this.createAndInstallLoader(n, bundle, (LoaderCallbacks<Object>)loaderCallbacks).mLoader;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("LoaderManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        DebugUtils.buildShortClassTag(this.mHost, sb);
        sb.append("}}");
        return sb.toString();
    }
    
    void updateHostController(final FragmentHostCallback mHost) {
        this.mHost = mHost;
    }
    
    final class LoaderInfo implements OnLoadCompleteListener<Object>, OnLoadCanceledListener<Object>
    {
        final Bundle mArgs;
        LoaderCallbacks<Object> mCallbacks;
        Object mData;
        boolean mDeliveredData;
        boolean mDestroyed;
        boolean mHaveData;
        final int mId;
        boolean mListenerRegistered;
        Loader<Object> mLoader;
        LoaderInfo mPendingLoader;
        boolean mReportNextStart;
        boolean mRetaining;
        boolean mRetainingStarted;
        boolean mStarted;
        
        public LoaderInfo(final int mId, final Bundle mArgs, final LoaderCallbacks<Object> mCallbacks) {
            this.mId = mId;
            this.mArgs = mArgs;
            this.mCallbacks = mCallbacks;
        }
        
        void callOnLoadFinished(final Loader<Object> loader, final Object o) {
            if (this.mCallbacks == null) {
                return;
            }
            String mNoTransactionsBecause = null;
            if (LoaderManagerImpl.this.mHost != null) {
                mNoTransactionsBecause = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
                LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoadFinished";
            }
            try {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  onLoadFinished in " + loader + ": " + loader.dataToString(o));
                }
                this.mCallbacks.onLoadFinished(loader, o);
                if (LoaderManagerImpl.this.mHost != null) {
                    LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = mNoTransactionsBecause;
                }
                this.mDeliveredData = true;
            }
            finally {
                if (LoaderManagerImpl.this.mHost != null) {
                    LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = mNoTransactionsBecause;
                }
            }
        }
        
        boolean cancel() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "  Canceling: " + this);
            }
            if (this.mStarted && this.mLoader != null && this.mListenerRegistered) {
                final boolean cancelLoad = this.mLoader.cancelLoad();
                if (!cancelLoad) {
                    this.onLoadCanceled(this.mLoader);
                }
                return cancelLoad;
            }
            return false;
        }
        
        void destroy() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "  Destroying: " + this);
            }
            this.mDestroyed = true;
            final boolean mDeliveredData = this.mDeliveredData;
            this.mDeliveredData = false;
            Label_0180: {
                if (this.mCallbacks == null || this.mLoader == null || !this.mHaveData || !mDeliveredData) {
                    break Label_0180;
                }
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Resetting: " + this);
                }
                String mNoTransactionsBecause = null;
                if (LoaderManagerImpl.this.mHost != null) {
                    mNoTransactionsBecause = LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause;
                    LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = "onLoaderReset";
                }
                try {
                    this.mCallbacks.onLoaderReset(this.mLoader);
                    if (LoaderManagerImpl.this.mHost != null) {
                        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = mNoTransactionsBecause;
                    }
                    this.mCallbacks = null;
                    this.mData = null;
                    this.mHaveData = false;
                    if (this.mLoader != null) {
                        if (this.mListenerRegistered) {
                            this.mListenerRegistered = false;
                            this.mLoader.unregisterListener((Loader.OnLoadCompleteListener<Object>)this);
                            this.mLoader.unregisterOnLoadCanceledListener((Loader.OnLoadCanceledListener<Object>)this);
                        }
                        this.mLoader.reset();
                    }
                    if (this.mPendingLoader != null) {
                        this.mPendingLoader.destroy();
                    }
                }
                finally {
                    if (LoaderManagerImpl.this.mHost != null) {
                        LoaderManagerImpl.this.mHost.mFragmentManager.mNoTransactionsBecause = mNoTransactionsBecause;
                    }
                }
            }
        }
        
        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            printWriter.print(s);
            printWriter.print("mId=");
            printWriter.print(this.mId);
            printWriter.print(" mArgs=");
            printWriter.println(this.mArgs);
            printWriter.print(s);
            printWriter.print("mCallbacks=");
            printWriter.println(this.mCallbacks);
            printWriter.print(s);
            printWriter.print("mLoader=");
            printWriter.println(this.mLoader);
            if (this.mLoader != null) {
                this.mLoader.dump(s + "  ", fileDescriptor, printWriter, array);
            }
            if (this.mHaveData || this.mDeliveredData) {
                printWriter.print(s);
                printWriter.print("mHaveData=");
                printWriter.print(this.mHaveData);
                printWriter.print("  mDeliveredData=");
                printWriter.println(this.mDeliveredData);
                printWriter.print(s);
                printWriter.print("mData=");
                printWriter.println(this.mData);
            }
            printWriter.print(s);
            printWriter.print("mStarted=");
            printWriter.print(this.mStarted);
            printWriter.print(" mReportNextStart=");
            printWriter.print(this.mReportNextStart);
            printWriter.print(" mDestroyed=");
            printWriter.println(this.mDestroyed);
            printWriter.print(s);
            printWriter.print("mRetaining=");
            printWriter.print(this.mRetaining);
            printWriter.print(" mRetainingStarted=");
            printWriter.print(this.mRetainingStarted);
            printWriter.print(" mListenerRegistered=");
            printWriter.println(this.mListenerRegistered);
            if (this.mPendingLoader != null) {
                printWriter.print(s);
                printWriter.println("Pending Loader ");
                printWriter.print(this.mPendingLoader);
                printWriter.println(":");
                this.mPendingLoader.dump(s + "  ", fileDescriptor, printWriter, array);
            }
        }
        
        void finishRetain() {
            if (this.mRetaining) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Finished Retaining: " + this);
                }
                this.mRetaining = false;
                if (this.mStarted != this.mRetainingStarted && !this.mStarted) {
                    this.stop();
                }
            }
            if (this.mStarted && this.mHaveData && !this.mReportNextStart) {
                this.callOnLoadFinished(this.mLoader, this.mData);
            }
        }
        
        @Override
        public void onLoadCanceled(final Loader<Object> loader) {
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "onLoadCanceled: " + this);
            }
            if (this.mDestroyed) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Ignoring load canceled -- destroyed");
                }
            }
            else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Ignoring load canceled -- not active");
                }
            }
            else {
                final LoaderInfo mPendingLoader = this.mPendingLoader;
                if (mPendingLoader != null) {
                    if (LoaderManagerImpl.DEBUG) {
                        Log.v("LoaderManager", "  Switching to pending loader: " + mPendingLoader);
                    }
                    this.mPendingLoader = null;
                    LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                    this.destroy();
                    LoaderManagerImpl.this.installLoader(mPendingLoader);
                }
            }
        }
        
        @Override
        public void onLoadComplete(final Loader<Object> loader, final Object mData) {
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "onLoadComplete: " + this);
            }
            if (this.mDestroyed) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Ignoring load complete -- destroyed");
                }
            }
            else if (LoaderManagerImpl.this.mLoaders.get(this.mId) != this) {
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Ignoring load complete -- not active");
                }
            }
            else {
                final LoaderInfo mPendingLoader = this.mPendingLoader;
                if (mPendingLoader != null) {
                    if (LoaderManagerImpl.DEBUG) {
                        Log.v("LoaderManager", "  Switching to pending loader: " + mPendingLoader);
                    }
                    this.mPendingLoader = null;
                    LoaderManagerImpl.this.mLoaders.put(this.mId, null);
                    this.destroy();
                    LoaderManagerImpl.this.installLoader(mPendingLoader);
                    return;
                }
                if (this.mData != mData || !this.mHaveData) {
                    this.mData = mData;
                    this.mHaveData = true;
                    if (this.mStarted) {
                        this.callOnLoadFinished(loader, mData);
                    }
                }
                final LoaderInfo loaderInfo = LoaderManagerImpl.this.mInactiveLoaders.get(this.mId);
                if (loaderInfo != null && loaderInfo != this) {
                    loaderInfo.mDeliveredData = false;
                    loaderInfo.destroy();
                    LoaderManagerImpl.this.mInactiveLoaders.remove(this.mId);
                }
                if (LoaderManagerImpl.this.mHost != null && !LoaderManagerImpl.this.hasRunningLoaders()) {
                    LoaderManagerImpl.this.mHost.mFragmentManager.startPendingDeferredFragments();
                }
            }
        }
        
        void reportStart() {
            if (this.mStarted && this.mReportNextStart) {
                this.mReportNextStart = false;
                if (this.mHaveData && !this.mRetaining) {
                    this.callOnLoadFinished(this.mLoader, this.mData);
                }
            }
        }
        
        void retain() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "  Retaining: " + this);
            }
            this.mRetaining = true;
            this.mRetainingStarted = this.mStarted;
            this.mStarted = false;
            this.mCallbacks = null;
        }
        
        void start() {
            if (this.mRetaining && this.mRetainingStarted) {
                this.mStarted = true;
            }
            else if (!this.mStarted) {
                this.mStarted = true;
                if (LoaderManagerImpl.DEBUG) {
                    Log.v("LoaderManager", "  Starting: " + this);
                }
                if (this.mLoader == null && this.mCallbacks != null) {
                    this.mLoader = this.mCallbacks.onCreateLoader(this.mId, this.mArgs);
                }
                if (this.mLoader != null) {
                    if (this.mLoader.getClass().isMemberClass() && !Modifier.isStatic(this.mLoader.getClass().getModifiers())) {
                        throw new IllegalArgumentException("Object returned from onCreateLoader must not be a non-static inner member class: " + this.mLoader);
                    }
                    if (!this.mListenerRegistered) {
                        this.mLoader.registerListener(this.mId, (Loader.OnLoadCompleteListener<Object>)this);
                        this.mLoader.registerOnLoadCanceledListener((Loader.OnLoadCanceledListener<Object>)this);
                        this.mListenerRegistered = true;
                    }
                    this.mLoader.startLoading();
                }
            }
        }
        
        void stop() {
            if (LoaderManagerImpl.DEBUG) {
                Log.v("LoaderManager", "  Stopping: " + this);
            }
            this.mStarted = false;
            if (!this.mRetaining && this.mLoader != null && this.mListenerRegistered) {
                this.mListenerRegistered = false;
                this.mLoader.unregisterListener((Loader.OnLoadCompleteListener<Object>)this);
                this.mLoader.unregisterOnLoadCanceledListener((Loader.OnLoadCanceledListener<Object>)this);
                this.mLoader.stopLoading();
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(64);
            sb.append("LoaderInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" #");
            sb.append(this.mId);
            sb.append(" : ");
            DebugUtils.buildShortClassTag(this.mLoader, sb);
            sb.append("}}");
            return sb.toString();
        }
    }
}
