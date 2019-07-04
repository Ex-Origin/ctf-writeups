// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import android.os.AsyncTask;

@Deprecated
public final class AsyncTaskCompat
{
    @Deprecated
    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> executeParallel(final AsyncTask<Params, Progress, Result> asyncTask, final Params... array) {
        if (asyncTask == null) {
            throw new IllegalArgumentException("task can not be null");
        }
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Object[])array);
        return asyncTask;
    }
}
