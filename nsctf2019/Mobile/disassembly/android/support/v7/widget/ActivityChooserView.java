// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.widget.AdapterView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.View$MeasureSpec;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import android.view.ViewTreeObserver;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.View$OnTouchListener;
import android.support.v7.view.menu.ShowableListMenu;
import android.view.View;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.view.LayoutInflater;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.widget.PopupWindow$OnDismissListener;
import android.database.DataSetObserver;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.view.ViewGroup;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ActivityChooserView extends ViewGroup implements ActivityChooserModelClient
{
    private static final String LOG_TAG = "ActivityChooserView";
    private final LinearLayoutCompat mActivityChooserContent;
    private final Drawable mActivityChooserContentBackground;
    final ActivityChooserViewAdapter mAdapter;
    private final Callbacks mCallbacks;
    private int mDefaultActionButtonContentDescription;
    final FrameLayout mDefaultActivityButton;
    private final ImageView mDefaultActivityButtonImage;
    final FrameLayout mExpandActivityOverflowButton;
    private final ImageView mExpandActivityOverflowButtonImage;
    int mInitialActivityCount;
    private boolean mIsAttachedToWindow;
    boolean mIsSelectingDefaultActivity;
    private final int mListPopupMaxWidth;
    private ListPopupWindow mListPopupWindow;
    final DataSetObserver mModelDataSetObserver;
    PopupWindow$OnDismissListener mOnDismissListener;
    private final ViewTreeObserver$OnGlobalLayoutListener mOnGlobalLayoutListener;
    ActionProvider mProvider;
    
    public ActivityChooserView(final Context context) {
        this(context, null);
    }
    
    public ActivityChooserView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ActivityChooserView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mModelDataSetObserver = new DataSetObserver() {
            public void onChanged() {
                super.onChanged();
                ActivityChooserView.this.mAdapter.notifyDataSetChanged();
            }
            
            public void onInvalidated() {
                super.onInvalidated();
                ActivityChooserView.this.mAdapter.notifyDataSetInvalidated();
            }
        };
        this.mOnGlobalLayoutListener = (ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (ActivityChooserView.this.isShowingPopup()) {
                    if (!ActivityChooserView.this.isShown()) {
                        ActivityChooserView.this.getListPopupWindow().dismiss();
                    }
                    else {
                        ActivityChooserView.this.getListPopupWindow().show();
                        if (ActivityChooserView.this.mProvider != null) {
                            ActivityChooserView.this.mProvider.subUiVisibilityChanged(true);
                        }
                    }
                }
            }
        };
        this.mInitialActivityCount = 4;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ActivityChooserView, n, 0);
        this.mInitialActivityCount = obtainStyledAttributes.getInt(R.styleable.ActivityChooserView_initialActivityCount, 4);
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.ActivityChooserView_expandActivityOverflowButtonDrawable);
        obtainStyledAttributes.recycle();
        LayoutInflater.from(this.getContext()).inflate(R.layout.abc_activity_chooser_view, (ViewGroup)this, true);
        this.mCallbacks = new Callbacks();
        this.mActivityChooserContent = (LinearLayoutCompat)this.findViewById(R.id.activity_chooser_view_content);
        this.mActivityChooserContentBackground = this.mActivityChooserContent.getBackground();
        (this.mDefaultActivityButton = (FrameLayout)this.findViewById(R.id.default_activity_button)).setOnClickListener((View$OnClickListener)this.mCallbacks);
        this.mDefaultActivityButton.setOnLongClickListener((View$OnLongClickListener)this.mCallbacks);
        this.mDefaultActivityButtonImage = (ImageView)this.mDefaultActivityButton.findViewById(R.id.image);
        final FrameLayout mExpandActivityOverflowButton = (FrameLayout)this.findViewById(R.id.expand_activities_button);
        mExpandActivityOverflowButton.setOnClickListener((View$OnClickListener)this.mCallbacks);
        mExpandActivityOverflowButton.setOnTouchListener((View$OnTouchListener)new ForwardingListener(mExpandActivityOverflowButton) {
            @Override
            public ShowableListMenu getPopup() {
                return ActivityChooserView.this.getListPopupWindow();
            }
            
            @Override
            protected boolean onForwardingStarted() {
                ActivityChooserView.this.showPopup();
                return true;
            }
            
            @Override
            protected boolean onForwardingStopped() {
                ActivityChooserView.this.dismissPopup();
                return true;
            }
        });
        this.mExpandActivityOverflowButton = mExpandActivityOverflowButton;
        (this.mExpandActivityOverflowButtonImage = (ImageView)mExpandActivityOverflowButton.findViewById(R.id.image)).setImageDrawable(drawable);
        (this.mAdapter = new ActivityChooserViewAdapter()).registerDataSetObserver((DataSetObserver)new DataSetObserver() {
            public void onChanged() {
                super.onChanged();
                ActivityChooserView.this.updateAppearance();
            }
        });
        final Resources resources = context.getResources();
        this.mListPopupMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
    }
    
    public boolean dismissPopup() {
        if (this.isShowingPopup()) {
            this.getListPopupWindow().dismiss();
            final ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
            }
        }
        return true;
    }
    
    public ActivityChooserModel getDataModel() {
        return this.mAdapter.getDataModel();
    }
    
    ListPopupWindow getListPopupWindow() {
        if (this.mListPopupWindow == null) {
            (this.mListPopupWindow = new ListPopupWindow(this.getContext())).setAdapter((ListAdapter)this.mAdapter);
            this.mListPopupWindow.setAnchorView((View)this);
            this.mListPopupWindow.setModal(true);
            this.mListPopupWindow.setOnItemClickListener((AdapterView$OnItemClickListener)this.mCallbacks);
            this.mListPopupWindow.setOnDismissListener((PopupWindow$OnDismissListener)this.mCallbacks);
        }
        return this.mListPopupWindow;
    }
    
    public boolean isShowingPopup() {
        return this.getListPopupWindow().isShowing();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.registerObserver((Object)this.mModelDataSetObserver);
        }
        this.mIsAttachedToWindow = true;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        final ActivityChooserModel dataModel = this.mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.unregisterObserver((Object)this.mModelDataSetObserver);
        }
        final ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeGlobalOnLayoutListener(this.mOnGlobalLayoutListener);
        }
        if (this.isShowingPopup()) {
            this.dismissPopup();
        }
        this.mIsAttachedToWindow = false;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        this.mActivityChooserContent.layout(0, 0, n3 - n, n4 - n2);
        if (!this.isShowingPopup()) {
            this.dismissPopup();
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        final LinearLayoutCompat mActivityChooserContent = this.mActivityChooserContent;
        int measureSpec = n2;
        if (this.mDefaultActivityButton.getVisibility() != 0) {
            measureSpec = View$MeasureSpec.makeMeasureSpec(View$MeasureSpec.getSize(n2), 1073741824);
        }
        this.measureChild((View)mActivityChooserContent, n, measureSpec);
        this.setMeasuredDimension(((View)mActivityChooserContent).getMeasuredWidth(), ((View)mActivityChooserContent).getMeasuredHeight());
    }
    
    public void setActivityChooserModel(final ActivityChooserModel dataModel) {
        this.mAdapter.setDataModel(dataModel);
        if (this.isShowingPopup()) {
            this.dismissPopup();
            this.showPopup();
        }
    }
    
    public void setDefaultActionButtonContentDescription(final int mDefaultActionButtonContentDescription) {
        this.mDefaultActionButtonContentDescription = mDefaultActionButtonContentDescription;
    }
    
    public void setExpandActivityOverflowButtonContentDescription(final int n) {
        this.mExpandActivityOverflowButtonImage.setContentDescription((CharSequence)this.getContext().getString(n));
    }
    
    public void setExpandActivityOverflowButtonDrawable(final Drawable imageDrawable) {
        this.mExpandActivityOverflowButtonImage.setImageDrawable(imageDrawable);
    }
    
    public void setInitialActivityCount(final int mInitialActivityCount) {
        this.mInitialActivityCount = mInitialActivityCount;
    }
    
    public void setOnDismissListener(final PopupWindow$OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setProvider(final ActionProvider mProvider) {
        this.mProvider = mProvider;
    }
    
    public boolean showPopup() {
        if (this.isShowingPopup() || !this.mIsAttachedToWindow) {
            return false;
        }
        this.mIsSelectingDefaultActivity = false;
        this.showPopupUnchecked(this.mInitialActivityCount);
        return true;
    }
    
    void showPopupUnchecked(final int maxActivityCount) {
        if (this.mAdapter.getDataModel() == null) {
            throw new IllegalStateException("No data model. Did you call #setDataModel?");
        }
        this.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        final boolean b = this.mDefaultActivityButton.getVisibility() == 0;
        final int activityCount = this.mAdapter.getActivityCount();
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (maxActivityCount != Integer.MAX_VALUE && activityCount > maxActivityCount + n) {
            this.mAdapter.setShowFooterView(true);
            this.mAdapter.setMaxActivityCount(maxActivityCount - 1);
        }
        else {
            this.mAdapter.setShowFooterView(false);
            this.mAdapter.setMaxActivityCount(maxActivityCount);
        }
        final ListPopupWindow listPopupWindow = this.getListPopupWindow();
        if (!listPopupWindow.isShowing()) {
            if (this.mIsSelectingDefaultActivity || !b) {
                this.mAdapter.setShowDefaultActivity(true, b);
            }
            else {
                this.mAdapter.setShowDefaultActivity(false, false);
            }
            listPopupWindow.setContentWidth(Math.min(this.mAdapter.measureContentWidth(), this.mListPopupMaxWidth));
            listPopupWindow.show();
            if (this.mProvider != null) {
                this.mProvider.subUiVisibilityChanged(true);
            }
            listPopupWindow.getListView().setContentDescription((CharSequence)this.getContext().getString(R.string.abc_activitychooserview_choose_application));
        }
    }
    
    void updateAppearance() {
        if (this.mAdapter.getCount() > 0) {
            this.mExpandActivityOverflowButton.setEnabled(true);
        }
        else {
            this.mExpandActivityOverflowButton.setEnabled(false);
        }
        final int activityCount = this.mAdapter.getActivityCount();
        final int historySize = this.mAdapter.getHistorySize();
        if (activityCount == 1 || (activityCount > 1 && historySize > 0)) {
            this.mDefaultActivityButton.setVisibility(0);
            final ResolveInfo defaultActivity = this.mAdapter.getDefaultActivity();
            final PackageManager packageManager = this.getContext().getPackageManager();
            this.mDefaultActivityButtonImage.setImageDrawable(defaultActivity.loadIcon(packageManager));
            if (this.mDefaultActionButtonContentDescription != 0) {
                this.mDefaultActivityButton.setContentDescription((CharSequence)this.getContext().getString(this.mDefaultActionButtonContentDescription, new Object[] { defaultActivity.loadLabel(packageManager) }));
            }
        }
        else {
            this.mDefaultActivityButton.setVisibility(8);
        }
        if (this.mDefaultActivityButton.getVisibility() == 0) {
            this.mActivityChooserContent.setBackgroundDrawable(this.mActivityChooserContentBackground);
            return;
        }
        this.mActivityChooserContent.setBackgroundDrawable((Drawable)null);
    }
    
    private class ActivityChooserViewAdapter extends BaseAdapter
    {
        private static final int ITEM_VIEW_TYPE_ACTIVITY = 0;
        private static final int ITEM_VIEW_TYPE_COUNT = 3;
        private static final int ITEM_VIEW_TYPE_FOOTER = 1;
        public static final int MAX_ACTIVITY_COUNT_DEFAULT = 4;
        public static final int MAX_ACTIVITY_COUNT_UNLIMITED = Integer.MAX_VALUE;
        private ActivityChooserModel mDataModel;
        private boolean mHighlightDefaultActivity;
        private int mMaxActivityCount;
        private boolean mShowDefaultActivity;
        private boolean mShowFooterView;
        
        ActivityChooserViewAdapter() {
            this.mMaxActivityCount = 4;
        }
        
        public int getActivityCount() {
            return this.mDataModel.getActivityCount();
        }
        
        public int getCount() {
            int activityCount;
            final int n = activityCount = this.mDataModel.getActivityCount();
            if (!this.mShowDefaultActivity) {
                activityCount = n;
                if (this.mDataModel.getDefaultActivity() != null) {
                    activityCount = n - 1;
                }
            }
            int min = Math.min(activityCount, this.mMaxActivityCount);
            if (this.mShowFooterView) {
                ++min;
            }
            return min;
        }
        
        public ActivityChooserModel getDataModel() {
            return this.mDataModel;
        }
        
        public ResolveInfo getDefaultActivity() {
            return this.mDataModel.getDefaultActivity();
        }
        
        public int getHistorySize() {
            return this.mDataModel.getHistorySize();
        }
        
        public Object getItem(final int n) {
            switch (this.getItemViewType(n)) {
                default: {
                    throw new IllegalArgumentException();
                }
                case 1: {
                    return null;
                }
                case 0: {
                    int n2 = n;
                    if (!this.mShowDefaultActivity) {
                        n2 = n;
                        if (this.mDataModel.getDefaultActivity() != null) {
                            n2 = n + 1;
                        }
                    }
                    return this.mDataModel.getActivity(n2);
                }
            }
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public int getItemViewType(final int n) {
            if (this.mShowFooterView && n == this.getCount() - 1) {
                return 1;
            }
            return 0;
        }
        
        public boolean getShowDefaultActivity() {
            return this.mShowDefaultActivity;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            switch (this.getItemViewType(n)) {
                default: {
                    throw new IllegalArgumentException();
                }
                case 1: {
                    if (view != null) {
                        final View inflate = view;
                        if (view.getId() == 1) {
                            return inflate;
                        }
                    }
                    final View inflate = LayoutInflater.from(ActivityChooserView.this.getContext()).inflate(R.layout.abc_activity_chooser_view_list_item, viewGroup, false);
                    inflate.setId(1);
                    ((TextView)inflate.findViewById(R.id.title)).setText((CharSequence)ActivityChooserView.this.getContext().getString(R.string.abc_activity_chooser_view_see_all));
                    return inflate;
                }
                case 0: {
                    View inflate2 = null;
                    Label_0144: {
                        if (view != null) {
                            inflate2 = view;
                            if (view.getId() == R.id.list_item) {
                                break Label_0144;
                            }
                        }
                        inflate2 = LayoutInflater.from(ActivityChooserView.this.getContext()).inflate(R.layout.abc_activity_chooser_view_list_item, viewGroup, false);
                    }
                    final PackageManager packageManager = ActivityChooserView.this.getContext().getPackageManager();
                    final ImageView imageView = (ImageView)inflate2.findViewById(R.id.icon);
                    final ResolveInfo resolveInfo = (ResolveInfo)this.getItem(n);
                    imageView.setImageDrawable(resolveInfo.loadIcon(packageManager));
                    ((TextView)inflate2.findViewById(R.id.title)).setText(resolveInfo.loadLabel(packageManager));
                    if (this.mShowDefaultActivity && n == 0 && this.mHighlightDefaultActivity) {
                        inflate2.setActivated(true);
                    }
                    else {
                        inflate2.setActivated(false);
                    }
                    return inflate2;
                }
            }
        }
        
        public int getViewTypeCount() {
            return 3;
        }
        
        public int measureContentWidth() {
            final int mMaxActivityCount = this.mMaxActivityCount;
            this.mMaxActivityCount = Integer.MAX_VALUE;
            int max = 0;
            View view = null;
            final int measureSpec = View$MeasureSpec.makeMeasureSpec(0, 0);
            final int measureSpec2 = View$MeasureSpec.makeMeasureSpec(0, 0);
            for (int count = this.getCount(), i = 0; i < count; ++i) {
                view = this.getView(i, view, null);
                view.measure(measureSpec, measureSpec2);
                max = Math.max(max, view.getMeasuredWidth());
            }
            this.mMaxActivityCount = mMaxActivityCount;
            return max;
        }
        
        public void setDataModel(final ActivityChooserModel mDataModel) {
            final ActivityChooserModel dataModel = ActivityChooserView.this.mAdapter.getDataModel();
            if (dataModel != null && ActivityChooserView.this.isShown()) {
                dataModel.unregisterObserver((Object)ActivityChooserView.this.mModelDataSetObserver);
            }
            if ((this.mDataModel = mDataModel) != null && ActivityChooserView.this.isShown()) {
                mDataModel.registerObserver((Object)ActivityChooserView.this.mModelDataSetObserver);
            }
            this.notifyDataSetChanged();
        }
        
        public void setMaxActivityCount(final int mMaxActivityCount) {
            if (this.mMaxActivityCount != mMaxActivityCount) {
                this.mMaxActivityCount = mMaxActivityCount;
                this.notifyDataSetChanged();
            }
        }
        
        public void setShowDefaultActivity(final boolean mShowDefaultActivity, final boolean mHighlightDefaultActivity) {
            if (this.mShowDefaultActivity != mShowDefaultActivity || this.mHighlightDefaultActivity != mHighlightDefaultActivity) {
                this.mShowDefaultActivity = mShowDefaultActivity;
                this.mHighlightDefaultActivity = mHighlightDefaultActivity;
                this.notifyDataSetChanged();
            }
        }
        
        public void setShowFooterView(final boolean mShowFooterView) {
            if (this.mShowFooterView != mShowFooterView) {
                this.mShowFooterView = mShowFooterView;
                this.notifyDataSetChanged();
            }
        }
    }
    
    private class Callbacks implements AdapterView$OnItemClickListener, View$OnClickListener, View$OnLongClickListener, PopupWindow$OnDismissListener
    {
        private void notifyOnDismissListener() {
            if (ActivityChooserView.this.mOnDismissListener != null) {
                ActivityChooserView.this.mOnDismissListener.onDismiss();
            }
        }
        
        public void onClick(final View view) {
            if (view == ActivityChooserView.this.mDefaultActivityButton) {
                ActivityChooserView.this.dismissPopup();
                final Intent chooseActivity = ActivityChooserView.this.mAdapter.getDataModel().chooseActivity(ActivityChooserView.this.mAdapter.getDataModel().getActivityIndex(ActivityChooserView.this.mAdapter.getDefaultActivity()));
                if (chooseActivity != null) {
                    chooseActivity.addFlags(524288);
                    ActivityChooserView.this.getContext().startActivity(chooseActivity);
                }
                return;
            }
            if (view == ActivityChooserView.this.mExpandActivityOverflowButton) {
                ActivityChooserView.this.mIsSelectingDefaultActivity = false;
                ActivityChooserView.this.showPopupUnchecked(ActivityChooserView.this.mInitialActivityCount);
                return;
            }
            throw new IllegalArgumentException();
        }
        
        public void onDismiss() {
            this.notifyOnDismissListener();
            if (ActivityChooserView.this.mProvider != null) {
                ActivityChooserView.this.mProvider.subUiVisibilityChanged(false);
            }
        }
        
        public void onItemClick(final AdapterView<?> adapterView, final View view, int defaultActivity, final long n) {
            switch (((ActivityChooserViewAdapter)adapterView.getAdapter()).getItemViewType(defaultActivity)) {
                default: {
                    throw new IllegalArgumentException();
                }
                case 1: {
                    ActivityChooserView.this.showPopupUnchecked(Integer.MAX_VALUE);
                    break;
                }
                case 0: {
                    ActivityChooserView.this.dismissPopup();
                    if (ActivityChooserView.this.mIsSelectingDefaultActivity) {
                        if (defaultActivity > 0) {
                            ActivityChooserView.this.mAdapter.getDataModel().setDefaultActivity(defaultActivity);
                            return;
                        }
                        break;
                    }
                    else {
                        if (!ActivityChooserView.this.mAdapter.getShowDefaultActivity()) {
                            ++defaultActivity;
                        }
                        final Intent chooseActivity = ActivityChooserView.this.mAdapter.getDataModel().chooseActivity(defaultActivity);
                        if (chooseActivity != null) {
                            chooseActivity.addFlags(524288);
                            ActivityChooserView.this.getContext().startActivity(chooseActivity);
                            return;
                        }
                        break;
                    }
                    break;
                }
            }
        }
        
        public boolean onLongClick(final View view) {
            if (view == ActivityChooserView.this.mDefaultActivityButton) {
                if (ActivityChooserView.this.mAdapter.getCount() > 0) {
                    ActivityChooserView.this.mIsSelectingDefaultActivity = true;
                    ActivityChooserView.this.showPopupUnchecked(ActivityChooserView.this.mInitialActivityCount);
                }
                return true;
            }
            throw new IllegalArgumentException();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class InnerLayout extends LinearLayoutCompat
    {
        private static final int[] TINT_ATTRS;
        
        static {
            TINT_ATTRS = new int[] { 16842964 };
        }
        
        public InnerLayout(final Context context, final AttributeSet set) {
            super(context, set);
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, InnerLayout.TINT_ATTRS);
            this.setBackgroundDrawable(obtainStyledAttributes.getDrawable(0));
            obtainStyledAttributes.recycle();
        }
    }
}
