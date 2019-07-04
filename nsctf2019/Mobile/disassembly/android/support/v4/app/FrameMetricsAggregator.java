// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import android.view.FrameMetrics;
import android.view.Window;
import android.view.Window$OnFrameMetricsAvailableListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.os.HandlerThread;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;
import android.support.annotation.NonNull;
import android.app.Activity;
import android.os.Build$VERSION;

public class FrameMetricsAggregator
{
    public static final int ANIMATION_DURATION = 256;
    public static final int ANIMATION_INDEX = 8;
    public static final int COMMAND_DURATION = 32;
    public static final int COMMAND_INDEX = 5;
    private static final boolean DBG = false;
    public static final int DELAY_DURATION = 128;
    public static final int DELAY_INDEX = 7;
    public static final int DRAW_DURATION = 8;
    public static final int DRAW_INDEX = 3;
    public static final int EVERY_DURATION = 511;
    public static final int INPUT_DURATION = 2;
    public static final int INPUT_INDEX = 1;
    private static final int LAST_INDEX = 8;
    public static final int LAYOUT_MEASURE_DURATION = 4;
    public static final int LAYOUT_MEASURE_INDEX = 2;
    public static final int SWAP_DURATION = 64;
    public static final int SWAP_INDEX = 6;
    public static final int SYNC_DURATION = 16;
    public static final int SYNC_INDEX = 4;
    private static final String TAG = "FrameMetrics";
    public static final int TOTAL_DURATION = 1;
    public static final int TOTAL_INDEX = 0;
    private FrameMetricsBaseImpl mInstance;
    
    public FrameMetricsAggregator() {
        this(1);
    }
    
    public FrameMetricsAggregator(final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            this.mInstance = (FrameMetricsBaseImpl)new FrameMetricsApi24Impl(n);
            return;
        }
        this.mInstance = new FrameMetricsBaseImpl();
    }
    
    public void add(@NonNull final Activity activity) {
        this.mInstance.add(activity);
    }
    
    @Nullable
    public SparseIntArray[] getMetrics() {
        return this.mInstance.getMetrics();
    }
    
    @Nullable
    public SparseIntArray[] remove(@NonNull final Activity activity) {
        return this.mInstance.remove(activity);
    }
    
    @Nullable
    public SparseIntArray[] reset() {
        return this.mInstance.reset();
    }
    
    @Nullable
    public SparseIntArray[] stop() {
        return this.mInstance.stop();
    }
    
    @RequiresApi(24)
    private static class FrameMetricsApi24Impl extends FrameMetricsBaseImpl
    {
        private static final int NANOS_PER_MS = 1000000;
        private static final int NANOS_ROUNDING_VALUE = 500000;
        private static Handler sHandler;
        private static HandlerThread sHandlerThread;
        private ArrayList<WeakReference<Activity>> mActivities;
        Window$OnFrameMetricsAvailableListener mListener;
        private SparseIntArray[] mMetrics;
        private int mTrackingFlags;
        
        static {
            FrameMetricsApi24Impl.sHandlerThread = null;
            FrameMetricsApi24Impl.sHandler = null;
        }
        
        FrameMetricsApi24Impl(final int mTrackingFlags) {
            this.mMetrics = new SparseIntArray[9];
            this.mActivities = new ArrayList<WeakReference<Activity>>();
            this.mListener = (Window$OnFrameMetricsAvailableListener)new Window$OnFrameMetricsAvailableListener() {
                public void onFrameMetricsAvailable(final Window window, final FrameMetrics frameMetrics, final int n) {
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 0x1) != 0x0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[0], frameMetrics.getMetric(8));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 0x2) != 0x0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[1], frameMetrics.getMetric(1));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 0x4) != 0x0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[2], frameMetrics.getMetric(3));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 0x8) != 0x0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[3], frameMetrics.getMetric(4));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 0x10) != 0x0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[4], frameMetrics.getMetric(5));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 0x40) != 0x0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[6], frameMetrics.getMetric(7));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 0x20) != 0x0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[5], frameMetrics.getMetric(6));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 0x80) != 0x0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[7], frameMetrics.getMetric(0));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 0x100) != 0x0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[8], frameMetrics.getMetric(2));
                    }
                }
            };
            this.mTrackingFlags = mTrackingFlags;
        }
        
        @Override
        public void add(final Activity activity) {
            if (FrameMetricsApi24Impl.sHandlerThread == null) {
                (FrameMetricsApi24Impl.sHandlerThread = new HandlerThread("FrameMetricsAggregator")).start();
                FrameMetricsApi24Impl.sHandler = new Handler(FrameMetricsApi24Impl.sHandlerThread.getLooper());
            }
            for (int i = 0; i <= 8; ++i) {
                if (this.mMetrics[i] == null && (this.mTrackingFlags & 1 << i) != 0x0) {
                    this.mMetrics[i] = new SparseIntArray();
                }
            }
            activity.getWindow().addOnFrameMetricsAvailableListener(this.mListener, FrameMetricsApi24Impl.sHandler);
            this.mActivities.add(new WeakReference<Activity>(activity));
        }
        
        void addDurationItem(final SparseIntArray sparseIntArray, final long n) {
            if (sparseIntArray != null) {
                final int n2 = (int)((500000L + n) / 1000000L);
                if (n >= 0L) {
                    sparseIntArray.put(n2, sparseIntArray.get(n2) + 1);
                }
            }
        }
        
        @Override
        public SparseIntArray[] getMetrics() {
            return this.mMetrics;
        }
        
        @Override
        public SparseIntArray[] remove(final Activity activity) {
            for (final WeakReference<Activity> weakReference : this.mActivities) {
                if (weakReference.get() == activity) {
                    this.mActivities.remove(weakReference);
                    break;
                }
            }
            activity.getWindow().removeOnFrameMetricsAvailableListener(this.mListener);
            return this.mMetrics;
        }
        
        @Override
        public SparseIntArray[] reset() {
            final SparseIntArray[] mMetrics = this.mMetrics;
            this.mMetrics = new SparseIntArray[9];
            return mMetrics;
        }
        
        @Override
        public SparseIntArray[] stop() {
            for (int i = this.mActivities.size() - 1; i >= 0; --i) {
                final WeakReference<Activity> weakReference = this.mActivities.get(i);
                final Activity activity = weakReference.get();
                if (weakReference.get() != null) {
                    activity.getWindow().removeOnFrameMetricsAvailableListener(this.mListener);
                    this.mActivities.remove(i);
                }
            }
            return this.mMetrics;
        }
    }
    
    private static class FrameMetricsBaseImpl
    {
        public void add(final Activity activity) {
        }
        
        public SparseIntArray[] getMetrics() {
            return null;
        }
        
        public SparseIntArray[] remove(final Activity activity) {
            return null;
        }
        
        public SparseIntArray[] reset() {
            return null;
        }
        
        public SparseIntArray[] stop() {
            return null;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface MetricType {
    }
}
