// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view;

import android.view.ViewConfiguration;
import android.os.Build$VERSION;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.content.res.Configuration;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ActionBarPolicy
{
    private Context mContext;
    
    private ActionBarPolicy(final Context mContext) {
        this.mContext = mContext;
    }
    
    public static ActionBarPolicy get(final Context context) {
        return new ActionBarPolicy(context);
    }
    
    public boolean enableHomeButtonByDefault() {
        return this.mContext.getApplicationInfo().targetSdkVersion < 14;
    }
    
    public int getEmbeddedMenuWidthLimit() {
        return this.mContext.getResources().getDisplayMetrics().widthPixels / 2;
    }
    
    public int getMaxActionButtons() {
        final Configuration configuration = this.mContext.getResources().getConfiguration();
        final int screenWidthDp = configuration.screenWidthDp;
        final int screenHeightDp = configuration.screenHeightDp;
        if (configuration.smallestScreenWidthDp > 600 || screenWidthDp > 600 || (screenWidthDp > 960 && screenHeightDp > 720) || (screenWidthDp > 720 && screenHeightDp > 960)) {
            return 5;
        }
        if (screenWidthDp >= 500 || (screenWidthDp > 640 && screenHeightDp > 480) || (screenWidthDp > 480 && screenHeightDp > 640)) {
            return 4;
        }
        if (screenWidthDp >= 360) {
            return 3;
        }
        return 2;
    }
    
    public int getStackedTabMaxWidth() {
        return this.mContext.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_stacked_tab_max_width);
    }
    
    public int getTabContainerHeight() {
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes((AttributeSet)null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        final int layoutDimension = obtainStyledAttributes.getLayoutDimension(R.styleable.ActionBar_height, 0);
        final Resources resources = this.mContext.getResources();
        int min = layoutDimension;
        if (!this.hasEmbeddedTabs()) {
            min = Math.min(layoutDimension, resources.getDimensionPixelSize(R.dimen.abc_action_bar_stacked_max_height));
        }
        obtainStyledAttributes.recycle();
        return min;
    }
    
    public boolean hasEmbeddedTabs() {
        return this.mContext.getResources().getBoolean(R.bool.abc_action_bar_embed_tabs);
    }
    
    public boolean showsOverflowMenuButton() {
        return Build$VERSION.SDK_INT >= 19 || !ViewConfiguration.get(this.mContext).hasPermanentMenuKey();
    }
}
