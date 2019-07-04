// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import java.lang.reflect.InvocationTargetException;
import android.support.annotation.Nullable;
import java.lang.reflect.Method;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintContextWrapper;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.support.v7.appcompat.R;
import android.view.InflateException;
import android.content.res.TypedArray;
import android.view.View$OnClickListener;
import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import android.content.ContextWrapper;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import java.lang.reflect.Constructor;
import java.util.Map;

class AppCompatViewInflater
{
    private static final String LOG_TAG = "AppCompatViewInflater";
    private static final String[] sClassPrefixList;
    private static final Map<String, Constructor<? extends View>> sConstructorMap;
    private static final Class<?>[] sConstructorSignature;
    private static final int[] sOnClickAttrs;
    private final Object[] mConstructorArgs;
    
    static {
        sConstructorSignature = new Class[] { Context.class, AttributeSet.class };
        sOnClickAttrs = new int[] { 16843375 };
        sClassPrefixList = new String[] { "android.widget.", "android.view.", "android.webkit." };
        sConstructorMap = new ArrayMap<String, Constructor<? extends View>>();
    }
    
    AppCompatViewInflater() {
        this.mConstructorArgs = new Object[2];
    }
    
    private void checkOnClickListener(final View view, final AttributeSet set) {
        final Context context = view.getContext();
        if (!(context instanceof ContextWrapper) || (Build$VERSION.SDK_INT >= 15 && !ViewCompat.hasOnClickListeners(view))) {
            return;
        }
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, AppCompatViewInflater.sOnClickAttrs);
        final String string = obtainStyledAttributes.getString(0);
        if (string != null) {
            view.setOnClickListener((View$OnClickListener)new DeclaredOnClickListener(view, string));
        }
        obtainStyledAttributes.recycle();
    }
    
    private View createView(final Context context, final String s, final String s2) throws ClassNotFoundException, InflateException {
        Label_0083: {
            Constructor<? extends View> constructor;
            if ((constructor = AppCompatViewInflater.sConstructorMap.get(s)) != null) {
                break Label_0083;
            }
            try {
                final ClassLoader classLoader = context.getClassLoader();
                String string;
                if (s2 != null) {
                    string = s2 + s;
                }
                else {
                    string = s;
                }
                constructor = classLoader.loadClass(string).asSubclass(View.class).getConstructor(AppCompatViewInflater.sConstructorSignature);
                AppCompatViewInflater.sConstructorMap.put(s, constructor);
                constructor.setAccessible(true);
                return (View)constructor.newInstance(this.mConstructorArgs);
            }
            catch (Exception ex) {
                return null;
            }
        }
    }
    
    private View createViewFromTag(final Context context, final String s, final AttributeSet set) {
        String attributeValue = s;
        if (s.equals("view")) {
            attributeValue = set.getAttributeValue((String)null, "class");
        }
        try {
            this.mConstructorArgs[0] = context;
            this.mConstructorArgs[1] = set;
            if (-1 == attributeValue.indexOf(46)) {
                for (int i = 0; i < AppCompatViewInflater.sClassPrefixList.length; ++i) {
                    final View view = this.createView(context, attributeValue, AppCompatViewInflater.sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            }
            return this.createView(context, attributeValue, null);
        }
        catch (Exception ex) {
            return null;
        }
        finally {
            this.mConstructorArgs[0] = null;
            this.mConstructorArgs[1] = null;
        }
    }
    
    private static Context themifyContext(final Context context, final AttributeSet set, final boolean b, final boolean b2) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.View, 0, 0);
        int resourceId = 0;
        if (b) {
            resourceId = obtainStyledAttributes.getResourceId(R.styleable.View_android_theme, 0);
        }
        int n = resourceId;
        if (b2 && (n = resourceId) == 0) {
            final int resourceId2 = obtainStyledAttributes.getResourceId(R.styleable.View_theme, 0);
            if ((n = resourceId2) != 0) {
                Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
                n = resourceId2;
            }
        }
        obtainStyledAttributes.recycle();
        Object o = context;
        if (n != 0) {
            if (context instanceof ContextThemeWrapper) {
                o = context;
                if (((ContextThemeWrapper)context).getThemeResId() == n) {
                    return (Context)o;
                }
            }
            o = new ContextThemeWrapper(context, n);
        }
        return (Context)o;
    }
    
    public final View createView(final View view, final String s, @NonNull final Context context, @NonNull final AttributeSet set, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        Context context2 = context;
        if (b) {
            context2 = context;
            if (view != null) {
                context2 = view.getContext();
            }
        }
        Context themifyContext = null;
        Label_0046: {
            if (!b2) {
                themifyContext = context2;
                if (!b3) {
                    break Label_0046;
                }
            }
            themifyContext = themifyContext(context2, set, b2, b3);
        }
        Context wrap = themifyContext;
        if (b4) {
            wrap = TintContextWrapper.wrap(themifyContext);
        }
        Object o = null;
        switch (s) {
            case "TextView": {
                o = new AppCompatTextView(wrap, set);
                break;
            }
            case "ImageView": {
                o = new AppCompatImageView(wrap, set);
                break;
            }
            case "Button": {
                o = new AppCompatButton(wrap, set);
                break;
            }
            case "EditText": {
                o = new AppCompatEditText(wrap, set);
                break;
            }
            case "Spinner": {
                o = new AppCompatSpinner(wrap, set);
                break;
            }
            case "ImageButton": {
                o = new AppCompatImageButton(wrap, set);
                break;
            }
            case "CheckBox": {
                o = new AppCompatCheckBox(wrap, set);
                break;
            }
            case "RadioButton": {
                o = new AppCompatRadioButton(wrap, set);
                break;
            }
            case "CheckedTextView": {
                o = new AppCompatCheckedTextView(wrap, set);
                break;
            }
            case "AutoCompleteTextView": {
                o = new AppCompatAutoCompleteTextView(wrap, set);
                break;
            }
            case "MultiAutoCompleteTextView": {
                o = new AppCompatMultiAutoCompleteTextView(wrap, set);
                break;
            }
            case "RatingBar": {
                o = new AppCompatRatingBar(wrap, set);
                break;
            }
            case "SeekBar": {
                o = new AppCompatSeekBar(wrap, set);
                break;
            }
        }
        Object viewFromTag = o;
        if (o == null) {
            viewFromTag = o;
            if (context != wrap) {
                viewFromTag = this.createViewFromTag(wrap, s, set);
            }
        }
        if (viewFromTag != null) {
            this.checkOnClickListener((View)viewFromTag, set);
        }
        return (View)viewFromTag;
    }
    
    private static class DeclaredOnClickListener implements View$OnClickListener
    {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;
        
        public DeclaredOnClickListener(@NonNull final View mHostView, @NonNull final String mMethodName) {
            this.mHostView = mHostView;
            this.mMethodName = mMethodName;
        }
        
        @NonNull
        private void resolveMethod(@Nullable Context baseContext, @NonNull final String s) {
            while (baseContext != null) {
                try {
                    if (!baseContext.isRestricted()) {
                        final Method method = baseContext.getClass().getMethod(this.mMethodName, View.class);
                        if (method != null) {
                            this.mResolvedMethod = method;
                            this.mResolvedContext = baseContext;
                            return;
                        }
                    }
                }
                catch (NoSuchMethodException ex) {}
                if (baseContext instanceof ContextWrapper) {
                    baseContext = ((ContextWrapper)baseContext).getBaseContext();
                }
                else {
                    baseContext = null;
                }
            }
            final int id = this.mHostView.getId();
            String string;
            if (id == -1) {
                string = "";
            }
            else {
                string = " with id '" + this.mHostView.getContext().getResources().getResourceEntryName(id) + "'";
            }
            throw new IllegalStateException("Could not find method " + this.mMethodName + "(View) in a parent or ancestor Context for android:onClick " + "attribute defined on view " + this.mHostView.getClass() + string);
        }
        
        public void onClick(@NonNull final View view) {
            if (this.mResolvedMethod == null) {
                this.resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mResolvedMethod.invoke(this.mResolvedContext, view);
            }
            catch (IllegalAccessException ex) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", ex);
            }
            catch (InvocationTargetException ex2) {
                throw new IllegalStateException("Could not execute method for android:onClick", ex2);
            }
        }
    }
}
