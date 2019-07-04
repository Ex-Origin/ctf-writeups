// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.support.v4.util.SimpleArrayMap;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import java.util.List;
import android.support.annotation.Nullable;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Configuration;

public class FragmentController
{
    private final FragmentHostCallback<?> mHost;
    
    private FragmentController(final FragmentHostCallback<?> mHost) {
        this.mHost = mHost;
    }
    
    public static final FragmentController createController(final FragmentHostCallback<?> fragmentHostCallback) {
        return new FragmentController(fragmentHostCallback);
    }
    
    public void attachHost(final Fragment fragment) {
        this.mHost.mFragmentManager.attachController(this.mHost, this.mHost, fragment);
    }
    
    public void dispatchActivityCreated() {
        this.mHost.mFragmentManager.dispatchActivityCreated();
    }
    
    public void dispatchConfigurationChanged(final Configuration configuration) {
        this.mHost.mFragmentManager.dispatchConfigurationChanged(configuration);
    }
    
    public boolean dispatchContextItemSelected(final MenuItem menuItem) {
        return this.mHost.mFragmentManager.dispatchContextItemSelected(menuItem);
    }
    
    public void dispatchCreate() {
        this.mHost.mFragmentManager.dispatchCreate();
    }
    
    public boolean dispatchCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        return this.mHost.mFragmentManager.dispatchCreateOptionsMenu(menu, menuInflater);
    }
    
    public void dispatchDestroy() {
        this.mHost.mFragmentManager.dispatchDestroy();
    }
    
    public void dispatchDestroyView() {
        this.mHost.mFragmentManager.dispatchDestroyView();
    }
    
    public void dispatchLowMemory() {
        this.mHost.mFragmentManager.dispatchLowMemory();
    }
    
    public void dispatchMultiWindowModeChanged(final boolean b) {
        this.mHost.mFragmentManager.dispatchMultiWindowModeChanged(b);
    }
    
    public boolean dispatchOptionsItemSelected(final MenuItem menuItem) {
        return this.mHost.mFragmentManager.dispatchOptionsItemSelected(menuItem);
    }
    
    public void dispatchOptionsMenuClosed(final Menu menu) {
        this.mHost.mFragmentManager.dispatchOptionsMenuClosed(menu);
    }
    
    public void dispatchPause() {
        this.mHost.mFragmentManager.dispatchPause();
    }
    
    public void dispatchPictureInPictureModeChanged(final boolean b) {
        this.mHost.mFragmentManager.dispatchPictureInPictureModeChanged(b);
    }
    
    public boolean dispatchPrepareOptionsMenu(final Menu menu) {
        return this.mHost.mFragmentManager.dispatchPrepareOptionsMenu(menu);
    }
    
    public void dispatchReallyStop() {
        this.mHost.mFragmentManager.dispatchReallyStop();
    }
    
    public void dispatchResume() {
        this.mHost.mFragmentManager.dispatchResume();
    }
    
    public void dispatchStart() {
        this.mHost.mFragmentManager.dispatchStart();
    }
    
    public void dispatchStop() {
        this.mHost.mFragmentManager.dispatchStop();
    }
    
    public void doLoaderDestroy() {
        this.mHost.doLoaderDestroy();
    }
    
    public void doLoaderRetain() {
        this.mHost.doLoaderRetain();
    }
    
    public void doLoaderStart() {
        this.mHost.doLoaderStart();
    }
    
    public void doLoaderStop(final boolean b) {
        this.mHost.doLoaderStop(b);
    }
    
    public void dumpLoaders(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        this.mHost.dumpLoaders(s, fileDescriptor, printWriter, array);
    }
    
    public boolean execPendingActions() {
        return this.mHost.mFragmentManager.execPendingActions();
    }
    
    @Nullable
    public Fragment findFragmentByWho(final String s) {
        return this.mHost.mFragmentManager.findFragmentByWho(s);
    }
    
    public List<Fragment> getActiveFragments(final List<Fragment> list) {
        return this.mHost.mFragmentManager.getActiveFragments();
    }
    
    public int getActiveFragmentsCount() {
        return this.mHost.mFragmentManager.getActiveFragmentCount();
    }
    
    public FragmentManager getSupportFragmentManager() {
        return this.mHost.getFragmentManagerImpl();
    }
    
    public LoaderManager getSupportLoaderManager() {
        return this.mHost.getLoaderManagerImpl();
    }
    
    public void noteStateNotSaved() {
        this.mHost.mFragmentManager.noteStateNotSaved();
    }
    
    public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
        return this.mHost.mFragmentManager.onCreateView(view, s, context, set);
    }
    
    public void reportLoaderStart() {
        this.mHost.reportLoaderStart();
    }
    
    public void restoreAllState(final Parcelable parcelable, final FragmentManagerNonConfig fragmentManagerNonConfig) {
        this.mHost.mFragmentManager.restoreAllState(parcelable, fragmentManagerNonConfig);
    }
    
    @Deprecated
    public void restoreAllState(final Parcelable parcelable, final List<Fragment> list) {
        this.mHost.mFragmentManager.restoreAllState(parcelable, new FragmentManagerNonConfig(list, null));
    }
    
    public void restoreLoaderNonConfig(final SimpleArrayMap<String, LoaderManager> simpleArrayMap) {
        this.mHost.restoreLoaderNonConfig(simpleArrayMap);
    }
    
    public SimpleArrayMap<String, LoaderManager> retainLoaderNonConfig() {
        return this.mHost.retainLoaderNonConfig();
    }
    
    public FragmentManagerNonConfig retainNestedNonConfig() {
        return this.mHost.mFragmentManager.retainNonConfig();
    }
    
    @Deprecated
    public List<Fragment> retainNonConfig() {
        final FragmentManagerNonConfig retainNonConfig = this.mHost.mFragmentManager.retainNonConfig();
        if (retainNonConfig != null) {
            return retainNonConfig.getFragments();
        }
        return null;
    }
    
    public Parcelable saveAllState() {
        return this.mHost.mFragmentManager.saveAllState();
    }
}
