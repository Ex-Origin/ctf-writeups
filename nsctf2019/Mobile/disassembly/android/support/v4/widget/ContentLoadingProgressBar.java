// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.util.AttributeSet;
import android.content.Context;
import android.widget.ProgressBar;

public class ContentLoadingProgressBar extends ProgressBar
{
    private static final int MIN_DELAY = 500;
    private static final int MIN_SHOW_TIME = 500;
    private final Runnable mDelayedHide;
    private final Runnable mDelayedShow;
    boolean mDismissed;
    boolean mPostedHide;
    boolean mPostedShow;
    long mStartTime;
    
    public ContentLoadingProgressBar(final Context context) {
        this(context, null);
    }
    
    public ContentLoadingProgressBar(final Context context, final AttributeSet set) {
        super(context, set, 0);
        this.mStartTime = -1L;
        this.mPostedHide = false;
        this.mPostedShow = false;
        this.mDismissed = false;
        this.mDelayedHide = new Runnable() {
            @Override
            public void run() {
                ContentLoadingProgressBar.this.mPostedHide = false;
                ContentLoadingProgressBar.this.mStartTime = -1L;
                ContentLoadingProgressBar.this.setVisibility(8);
            }
        };
        this.mDelayedShow = new Runnable() {
            @Override
            public void run() {
                ContentLoadingProgressBar.this.mPostedShow = false;
                if (!ContentLoadingProgressBar.this.mDismissed) {
                    ContentLoadingProgressBar.this.mStartTime = System.currentTimeMillis();
                    ContentLoadingProgressBar.this.setVisibility(0);
                }
            }
        };
    }
    
    private void removeCallbacks() {
        this.removeCallbacks(this.mDelayedHide);
        this.removeCallbacks(this.mDelayedShow);
    }
    
    public void hide() {
        this.mDismissed = true;
        this.removeCallbacks(this.mDelayedShow);
        final long n = System.currentTimeMillis() - this.mStartTime;
        if (n >= 500L || this.mStartTime == -1L) {
            this.setVisibility(8);
        }
        else if (!this.mPostedHide) {
            this.postDelayed(this.mDelayedHide, 500L - n);
            this.mPostedHide = true;
        }
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.removeCallbacks();
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks();
    }
    
    public void show() {
        this.mStartTime = -1L;
        this.mDismissed = false;
        this.removeCallbacks(this.mDelayedHide);
        if (!this.mPostedShow) {
            this.postDelayed(this.mDelayedShow, 500L);
            this.mPostedShow = true;
        }
    }
}
