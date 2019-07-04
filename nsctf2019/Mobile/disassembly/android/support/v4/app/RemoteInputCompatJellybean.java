// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.util.HashMap;
import android.content.ClipDescription;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import android.content.ClipData;
import android.os.Bundle;
import android.net.Uri;
import java.util.Map;
import android.content.Intent;
import android.support.annotation.RequiresApi;

@RequiresApi(16)
class RemoteInputCompatJellybean
{
    private static final String EXTRA_DATA_TYPE_RESULTS_DATA = "android.remoteinput.dataTypeResultsData";
    public static final String EXTRA_RESULTS_DATA = "android.remoteinput.resultsData";
    private static final String KEY_ALLOWED_DATA_TYPES = "allowedDataTypes";
    private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_LABEL = "label";
    private static final String KEY_RESULT_KEY = "resultKey";
    public static final String RESULTS_CLIP_LABEL = "android.remoteinput.results";
    
    public static void addDataResultToIntent(final RemoteInput remoteInput, final Intent intent, final Map<String, Uri> map) {
        Intent clipDataIntentFromIntent;
        if ((clipDataIntentFromIntent = getClipDataIntentFromIntent(intent)) == null) {
            clipDataIntentFromIntent = new Intent();
        }
        for (final Map.Entry<String, Uri> entry : map.entrySet()) {
            final String s = entry.getKey();
            final Uri uri = entry.getValue();
            if (s != null) {
                Bundle bundleExtra;
                if ((bundleExtra = clipDataIntentFromIntent.getBundleExtra(getExtraResultsKeyForData(s))) == null) {
                    bundleExtra = new Bundle();
                }
                bundleExtra.putString(remoteInput.getResultKey(), uri.toString());
                clipDataIntentFromIntent.putExtra(getExtraResultsKeyForData(s), bundleExtra);
            }
        }
        intent.setClipData(ClipData.newIntent((CharSequence)"android.remoteinput.results", clipDataIntentFromIntent));
    }
    
    static void addResultsToIntent(final RemoteInputCompatBase.RemoteInput[] array, final Intent intent, final Bundle bundle) {
        Intent clipDataIntentFromIntent;
        if ((clipDataIntentFromIntent = getClipDataIntentFromIntent(intent)) == null) {
            clipDataIntentFromIntent = new Intent();
        }
        Bundle bundleExtra;
        if ((bundleExtra = clipDataIntentFromIntent.getBundleExtra("android.remoteinput.resultsData")) == null) {
            bundleExtra = new Bundle();
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            final RemoteInputCompatBase.RemoteInput remoteInput = array[i];
            final Object value = bundle.get(remoteInput.getResultKey());
            if (value instanceof CharSequence) {
                bundleExtra.putCharSequence(remoteInput.getResultKey(), (CharSequence)value);
            }
        }
        clipDataIntentFromIntent.putExtra("android.remoteinput.resultsData", bundleExtra);
        intent.setClipData(ClipData.newIntent((CharSequence)"android.remoteinput.results", clipDataIntentFromIntent));
    }
    
    static RemoteInputCompatBase.RemoteInput fromBundle(final Bundle bundle, final RemoteInputCompatBase.RemoteInput.Factory factory) {
        final ArrayList stringArrayList = bundle.getStringArrayList("allowedDataTypes");
        final HashSet<String> set = new HashSet<String>();
        if (stringArrayList != null) {
            final Iterator<String> iterator = stringArrayList.iterator();
            while (iterator.hasNext()) {
                set.add(iterator.next());
            }
        }
        return factory.build(bundle.getString("resultKey"), bundle.getCharSequence("label"), bundle.getCharSequenceArray("choices"), bundle.getBoolean("allowFreeFormInput"), bundle.getBundle("extras"), set);
    }
    
    static RemoteInputCompatBase.RemoteInput[] fromBundleArray(final Bundle[] array, final RemoteInputCompatBase.RemoteInput.Factory factory) {
        RemoteInputCompatBase.RemoteInput[] array2;
        if (array == null) {
            array2 = null;
        }
        else {
            final RemoteInputCompatBase.RemoteInput[] array3 = factory.newArray(array.length);
            int n = 0;
            while (true) {
                array2 = array3;
                if (n >= array.length) {
                    break;
                }
                array3[n] = fromBundle(array[n], factory);
                ++n;
            }
        }
        return array2;
    }
    
    private static Intent getClipDataIntentFromIntent(final Intent intent) {
        final ClipData clipData = intent.getClipData();
        if (clipData != null) {
            final ClipDescription description = clipData.getDescription();
            if (description.hasMimeType("text/vnd.android.intent") && description.getLabel().equals("android.remoteinput.results")) {
                return clipData.getItemAt(0).getIntent();
            }
        }
        return null;
    }
    
    static Map<String, Uri> getDataResultsFromIntent(Intent clipDataIntentFromIntent, final String s) {
        clipDataIntentFromIntent = getClipDataIntentFromIntent(clipDataIntentFromIntent);
        if (clipDataIntentFromIntent == null) {
            return null;
        }
        final HashMap<String, Uri> hashMap = new HashMap<String, Uri>();
        for (final String s2 : clipDataIntentFromIntent.getExtras().keySet()) {
            if (s2.startsWith("android.remoteinput.dataTypeResultsData")) {
                final String substring = s2.substring("android.remoteinput.dataTypeResultsData".length());
                if (substring == null || substring.isEmpty()) {
                    continue;
                }
                final String string = clipDataIntentFromIntent.getBundleExtra(s2).getString(s);
                if (string == null || string.isEmpty()) {
                    continue;
                }
                hashMap.put(substring, Uri.parse(string));
            }
        }
        HashMap<String, Uri> hashMap2 = hashMap;
        if (hashMap.isEmpty()) {
            hashMap2 = null;
        }
        return hashMap2;
    }
    
    private static String getExtraResultsKeyForData(final String s) {
        return "android.remoteinput.dataTypeResultsData" + s;
    }
    
    static Bundle getResultsFromIntent(Intent clipDataIntentFromIntent) {
        clipDataIntentFromIntent = getClipDataIntentFromIntent(clipDataIntentFromIntent);
        if (clipDataIntentFromIntent == null) {
            return null;
        }
        return (Bundle)clipDataIntentFromIntent.getExtras().getParcelable("android.remoteinput.resultsData");
    }
    
    static Bundle toBundle(final RemoteInputCompatBase.RemoteInput remoteInput) {
        final Bundle bundle = new Bundle();
        bundle.putString("resultKey", remoteInput.getResultKey());
        bundle.putCharSequence("label", remoteInput.getLabel());
        bundle.putCharSequenceArray("choices", remoteInput.getChoices());
        bundle.putBoolean("allowFreeFormInput", remoteInput.getAllowFreeFormInput());
        bundle.putBundle("extras", remoteInput.getExtras());
        final Set<String> allowedDataTypes = remoteInput.getAllowedDataTypes();
        if (allowedDataTypes != null && !allowedDataTypes.isEmpty()) {
            final ArrayList list = new ArrayList<String>(allowedDataTypes.size());
            final Iterator<String> iterator = allowedDataTypes.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
            bundle.putStringArrayList("allowedDataTypes", list);
        }
        return bundle;
    }
    
    static Bundle[] toBundleArray(final RemoteInputCompatBase.RemoteInput[] array) {
        Bundle[] array2;
        if (array == null) {
            array2 = null;
        }
        else {
            final Bundle[] array3 = new Bundle[array.length];
            int n = 0;
            while (true) {
                array2 = array3;
                if (n >= array.length) {
                    break;
                }
                array3[n] = toBundle(array[n]);
                ++n;
            }
        }
        return array2;
    }
}
