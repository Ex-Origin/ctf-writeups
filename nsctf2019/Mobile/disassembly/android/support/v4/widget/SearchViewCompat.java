// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.app.SearchManager;
import android.content.ComponentName;
import android.widget.SearchView$OnQueryTextListener;
import android.widget.SearchView$OnCloseListener;
import android.widget.SearchView;
import android.view.View;
import android.content.Context;

@Deprecated
public final class SearchViewCompat
{
    private SearchViewCompat(final Context context) {
    }
    
    private static void checkIfLegalArg(final View view) {
        if (view == null) {
            throw new IllegalArgumentException("searchView must be non-null");
        }
        if (!(view instanceof SearchView)) {
            throw new IllegalArgumentException("searchView must be an instance of android.widget.SearchView");
        }
    }
    
    @Deprecated
    public static CharSequence getQuery(final View view) {
        checkIfLegalArg(view);
        return ((SearchView)view).getQuery();
    }
    
    @Deprecated
    public static boolean isIconified(final View view) {
        checkIfLegalArg(view);
        return ((SearchView)view).isIconified();
    }
    
    @Deprecated
    public static boolean isQueryRefinementEnabled(final View view) {
        checkIfLegalArg(view);
        return ((SearchView)view).isQueryRefinementEnabled();
    }
    
    @Deprecated
    public static boolean isSubmitButtonEnabled(final View view) {
        checkIfLegalArg(view);
        return ((SearchView)view).isSubmitButtonEnabled();
    }
    
    private static SearchView$OnCloseListener newOnCloseListener(final OnCloseListener onCloseListener) {
        return (SearchView$OnCloseListener)new SearchView$OnCloseListener() {
            public boolean onClose() {
                return onCloseListener.onClose();
            }
        };
    }
    
    private static SearchView$OnQueryTextListener newOnQueryTextListener(final OnQueryTextListener onQueryTextListener) {
        return (SearchView$OnQueryTextListener)new SearchView$OnQueryTextListener() {
            public boolean onQueryTextChange(final String s) {
                return onQueryTextListener.onQueryTextChange(s);
            }
            
            public boolean onQueryTextSubmit(final String s) {
                return onQueryTextListener.onQueryTextSubmit(s);
            }
        };
    }
    
    @Deprecated
    public static View newSearchView(final Context context) {
        return (View)new SearchView(context);
    }
    
    @Deprecated
    public static void setIconified(final View view, final boolean iconified) {
        checkIfLegalArg(view);
        ((SearchView)view).setIconified(iconified);
    }
    
    @Deprecated
    public static void setImeOptions(final View view, final int imeOptions) {
        checkIfLegalArg(view);
        ((SearchView)view).setImeOptions(imeOptions);
    }
    
    @Deprecated
    public static void setInputType(final View view, final int inputType) {
        checkIfLegalArg(view);
        ((SearchView)view).setInputType(inputType);
    }
    
    @Deprecated
    public static void setMaxWidth(final View view, final int maxWidth) {
        checkIfLegalArg(view);
        ((SearchView)view).setMaxWidth(maxWidth);
    }
    
    @Deprecated
    public static void setOnCloseListener(final View view, final OnCloseListener onCloseListener) {
        checkIfLegalArg(view);
        ((SearchView)view).setOnCloseListener(newOnCloseListener(onCloseListener));
    }
    
    @Deprecated
    public static void setOnQueryTextListener(final View view, final OnQueryTextListener onQueryTextListener) {
        checkIfLegalArg(view);
        ((SearchView)view).setOnQueryTextListener(newOnQueryTextListener(onQueryTextListener));
    }
    
    @Deprecated
    public static void setQuery(final View view, final CharSequence charSequence, final boolean b) {
        checkIfLegalArg(view);
        ((SearchView)view).setQuery(charSequence, b);
    }
    
    @Deprecated
    public static void setQueryHint(final View view, final CharSequence queryHint) {
        checkIfLegalArg(view);
        ((SearchView)view).setQueryHint(queryHint);
    }
    
    @Deprecated
    public static void setQueryRefinementEnabled(final View view, final boolean queryRefinementEnabled) {
        checkIfLegalArg(view);
        ((SearchView)view).setQueryRefinementEnabled(queryRefinementEnabled);
    }
    
    @Deprecated
    public static void setSearchableInfo(final View view, final ComponentName componentName) {
        checkIfLegalArg(view);
        ((SearchView)view).setSearchableInfo(((SearchManager)view.getContext().getSystemService("search")).getSearchableInfo(componentName));
    }
    
    @Deprecated
    public static void setSubmitButtonEnabled(final View view, final boolean submitButtonEnabled) {
        checkIfLegalArg(view);
        ((SearchView)view).setSubmitButtonEnabled(submitButtonEnabled);
    }
    
    @Deprecated
    public interface OnCloseListener
    {
        boolean onClose();
    }
    
    @Deprecated
    public abstract static class OnCloseListenerCompat implements OnCloseListener
    {
        @Override
        public boolean onClose() {
            return false;
        }
    }
    
    @Deprecated
    public interface OnQueryTextListener
    {
        boolean onQueryTextChange(final String p0);
        
        boolean onQueryTextSubmit(final String p0);
    }
    
    @Deprecated
    public abstract static class OnQueryTextListenerCompat implements OnQueryTextListener
    {
        @Override
        public boolean onQueryTextChange(final String s) {
            return false;
        }
        
        @Override
        public boolean onQueryTextSubmit(final String s) {
            return false;
        }
    }
}
