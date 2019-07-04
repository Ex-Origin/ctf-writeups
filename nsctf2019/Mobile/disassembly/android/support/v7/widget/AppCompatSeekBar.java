// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.Canvas;
import android.support.annotation.RequiresApi;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.SeekBar;

public class AppCompatSeekBar extends SeekBar
{
    private final AppCompatSeekBarHelper mAppCompatSeekBarHelper;
    
    public AppCompatSeekBar(final Context context) {
        this(context, null);
    }
    
    public AppCompatSeekBar(final Context context, final AttributeSet set) {
        this(context, set, R.attr.seekBarStyle);
    }
    
    public AppCompatSeekBar(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        (this.mAppCompatSeekBarHelper = new AppCompatSeekBarHelper(this)).loadFromAttributes(set, n);
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.mAppCompatSeekBarHelper.drawableStateChanged();
    }
    
    @RequiresApi(11)
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        this.mAppCompatSeekBarHelper.jumpDrawablesToCurrentState();
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.mAppCompatSeekBarHelper.drawTickMarks(canvas);
    }
}
