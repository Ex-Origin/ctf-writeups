// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content;

import android.os.AsyncTask;
import java.util.concurrent.Executor;

@Deprecated
public final class ParallelExecutorCompat
{
    @Deprecated
    public static Executor getParallelExecutor() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }
}
