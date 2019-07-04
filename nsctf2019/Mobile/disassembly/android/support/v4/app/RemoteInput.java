// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.util.Log;
import android.support.annotation.RequiresApi;
import java.util.HashSet;
import android.net.Uri;
import java.util.Map;
import android.content.Intent;
import android.os.Build$VERSION;
import android.os.Bundle;
import java.util.Set;
import android.support.annotation.RestrictTo;

public final class RemoteInput extends RemoteInputCompatBase.RemoteInput
{
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final Factory FACTORY;
    private static final Impl IMPL;
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    private static final String TAG = "RemoteInput";
    private final boolean mAllowFreeFormTextInput;
    private final Set<String> mAllowedDataTypes;
    private final CharSequence[] mChoices;
    private final Bundle mExtras;
    private final CharSequence mLabel;
    private final String mResultKey;
    
    static {
        if (Build$VERSION.SDK_INT >= 20) {
            IMPL = (Impl)new ImplApi20();
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (Impl)new ImplJellybean();
        }
        else {
            IMPL = (Impl)new ImplBase();
        }
        FACTORY = new Factory() {
            public RemoteInput build(final String s, final CharSequence charSequence, final CharSequence[] array, final boolean b, final Bundle bundle, final Set<String> set) {
                return new RemoteInput(s, charSequence, array, b, bundle, set);
            }
            
            public RemoteInput[] newArray(final int n) {
                return new RemoteInput[n];
            }
        };
    }
    
    RemoteInput(final String mResultKey, final CharSequence mLabel, final CharSequence[] mChoices, final boolean mAllowFreeFormTextInput, final Bundle mExtras, final Set<String> mAllowedDataTypes) {
        this.mResultKey = mResultKey;
        this.mLabel = mLabel;
        this.mChoices = mChoices;
        this.mAllowFreeFormTextInput = mAllowFreeFormTextInput;
        this.mExtras = mExtras;
        this.mAllowedDataTypes = mAllowedDataTypes;
    }
    
    public static void addDataResultToIntent(final RemoteInput remoteInput, final Intent intent, final Map<String, Uri> map) {
        RemoteInput.IMPL.addDataResultToIntent(remoteInput, intent, map);
    }
    
    public static void addResultsToIntent(final RemoteInput[] array, final Intent intent, final Bundle bundle) {
        RemoteInput.IMPL.addResultsToIntent(array, intent, bundle);
    }
    
    public static Map<String, Uri> getDataResultsFromIntent(final Intent intent, final String s) {
        return RemoteInput.IMPL.getDataResultsFromIntent(intent, s);
    }
    
    public static Bundle getResultsFromIntent(final Intent intent) {
        return RemoteInput.IMPL.getResultsFromIntent(intent);
    }
    
    public boolean getAllowFreeFormInput() {
        return this.mAllowFreeFormTextInput;
    }
    
    public Set<String> getAllowedDataTypes() {
        return this.mAllowedDataTypes;
    }
    
    public CharSequence[] getChoices() {
        return this.mChoices;
    }
    
    public Bundle getExtras() {
        return this.mExtras;
    }
    
    public CharSequence getLabel() {
        return this.mLabel;
    }
    
    public String getResultKey() {
        return this.mResultKey;
    }
    
    public boolean isDataOnly() {
        return !this.getAllowFreeFormInput() && (this.getChoices() == null || this.getChoices().length == 0) && this.getAllowedDataTypes() != null && !this.getAllowedDataTypes().isEmpty();
    }
    
    public static final class Builder
    {
        private boolean mAllowFreeFormTextInput;
        private final Set<String> mAllowedDataTypes;
        private CharSequence[] mChoices;
        private Bundle mExtras;
        private CharSequence mLabel;
        private final String mResultKey;
        
        public Builder(final String mResultKey) {
            this.mAllowFreeFormTextInput = true;
            this.mExtras = new Bundle();
            this.mAllowedDataTypes = new HashSet<String>();
            if (mResultKey == null) {
                throw new IllegalArgumentException("Result key can't be null");
            }
            this.mResultKey = mResultKey;
        }
        
        public Builder addExtras(final Bundle bundle) {
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            return this;
        }
        
        public RemoteInput build() {
            return new RemoteInput(this.mResultKey, this.mLabel, this.mChoices, this.mAllowFreeFormTextInput, this.mExtras, this.mAllowedDataTypes);
        }
        
        public Bundle getExtras() {
            return this.mExtras;
        }
        
        public Builder setAllowDataType(final String s, final boolean b) {
            if (b) {
                this.mAllowedDataTypes.add(s);
                return this;
            }
            this.mAllowedDataTypes.remove(s);
            return this;
        }
        
        public Builder setAllowFreeFormInput(final boolean mAllowFreeFormTextInput) {
            this.mAllowFreeFormTextInput = mAllowFreeFormTextInput;
            return this;
        }
        
        public Builder setChoices(final CharSequence[] mChoices) {
            this.mChoices = mChoices;
            return this;
        }
        
        public Builder setLabel(final CharSequence mLabel) {
            this.mLabel = mLabel;
            return this;
        }
    }
    
    interface Impl
    {
        void addDataResultToIntent(final RemoteInput p0, final Intent p1, final Map<String, Uri> p2);
        
        void addResultsToIntent(final RemoteInput[] p0, final Intent p1, final Bundle p2);
        
        Map<String, Uri> getDataResultsFromIntent(final Intent p0, final String p1);
        
        Bundle getResultsFromIntent(final Intent p0);
    }
    
    @RequiresApi(20)
    static class ImplApi20 implements Impl
    {
        @Override
        public void addDataResultToIntent(final RemoteInput remoteInput, final Intent intent, final Map<String, Uri> map) {
            RemoteInputCompatApi20.addDataResultToIntent(remoteInput, intent, map);
        }
        
        @Override
        public void addResultsToIntent(final RemoteInput[] array, final Intent intent, final Bundle bundle) {
            RemoteInputCompatApi20.addResultsToIntent(array, intent, bundle);
        }
        
        @Override
        public Map<String, Uri> getDataResultsFromIntent(final Intent intent, final String s) {
            return RemoteInputCompatApi20.getDataResultsFromIntent(intent, s);
        }
        
        @Override
        public Bundle getResultsFromIntent(final Intent intent) {
            return RemoteInputCompatApi20.getResultsFromIntent(intent);
        }
    }
    
    static class ImplBase implements Impl
    {
        @Override
        public void addDataResultToIntent(final RemoteInput remoteInput, final Intent intent, final Map<String, Uri> map) {
            Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
        }
        
        @Override
        public void addResultsToIntent(final RemoteInput[] array, final Intent intent, final Bundle bundle) {
            Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
        }
        
        @Override
        public Map<String, Uri> getDataResultsFromIntent(final Intent intent, final String s) {
            Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
            return null;
        }
        
        @Override
        public Bundle getResultsFromIntent(final Intent intent) {
            Log.w("RemoteInput", "RemoteInput is only supported from API Level 16");
            return null;
        }
    }
    
    @RequiresApi(16)
    static class ImplJellybean implements Impl
    {
        @Override
        public void addDataResultToIntent(final RemoteInput remoteInput, final Intent intent, final Map<String, Uri> map) {
            RemoteInputCompatJellybean.addDataResultToIntent(remoteInput, intent, map);
        }
        
        @Override
        public void addResultsToIntent(final RemoteInput[] array, final Intent intent, final Bundle bundle) {
            RemoteInputCompatJellybean.addResultsToIntent(array, intent, bundle);
        }
        
        @Override
        public Map<String, Uri> getDataResultsFromIntent(final Intent intent, final String s) {
            return RemoteInputCompatJellybean.getDataResultsFromIntent(intent, s);
        }
        
        @Override
        public Bundle getResultsFromIntent(final Intent intent) {
            return RemoteInputCompatJellybean.getResultsFromIntent(intent);
        }
    }
}
