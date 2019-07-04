// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import java.math.BigDecimal;
import android.content.ComponentName;
import java.util.Collections;
import java.util.Collection;
import android.os.AsyncTask;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.content.Context;
import java.util.List;
import java.util.Map;
import android.database.DataSetObservable;

class ActivityChooserModel extends DataSetObservable
{
    static final String ATTRIBUTE_ACTIVITY = "activity";
    static final String ATTRIBUTE_TIME = "time";
    static final String ATTRIBUTE_WEIGHT = "weight";
    static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    static final String LOG_TAG;
    static final String TAG_HISTORICAL_RECORD = "historical-record";
    static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities;
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    boolean mCanReadHistoricalData;
    final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords;
    private boolean mHistoricalRecordsChanged;
    final String mHistoryFileName;
    private int mHistoryMaxSize;
    private final Object mInstanceLock;
    private Intent mIntent;
    private boolean mReadShareHistoryCalled;
    private boolean mReloadActivities;
    
    static {
        LOG_TAG = ActivityChooserModel.class.getSimpleName();
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap<String, ActivityChooserModel>();
    }
    
    private ActivityChooserModel(final Context context, final String mHistoryFileName) {
        this.mInstanceLock = new Object();
        this.mActivities = new ArrayList<ActivityResolveInfo>();
        this.mHistoricalRecords = new ArrayList<HistoricalRecord>();
        this.mActivitySorter = (ActivitySorter)new DefaultSorter();
        this.mHistoryMaxSize = 50;
        this.mCanReadHistoricalData = true;
        this.mReadShareHistoryCalled = false;
        this.mHistoricalRecordsChanged = true;
        this.mReloadActivities = false;
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)mHistoryFileName) && !mHistoryFileName.endsWith(".xml")) {
            this.mHistoryFileName = mHistoryFileName + ".xml";
            return;
        }
        this.mHistoryFileName = mHistoryFileName;
    }
    
    private boolean addHistoricalRecord(final HistoricalRecord historicalRecord) {
        final boolean add = this.mHistoricalRecords.add(historicalRecord);
        if (add) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return add;
    }
    
    private void ensureConsistentState() {
        final boolean loadActivitiesIfNeeded = this.loadActivitiesIfNeeded();
        final boolean historicalDataIfNeeded = this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (loadActivitiesIfNeeded | historicalDataIfNeeded) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }
    
    public static ActivityChooserModel get(final Context context, final String s) {
        synchronized (ActivityChooserModel.sRegistryLock) {
            ActivityChooserModel activityChooserModel;
            if ((activityChooserModel = ActivityChooserModel.sDataModelRegistry.get(s)) == null) {
                activityChooserModel = new ActivityChooserModel(context, s);
                ActivityChooserModel.sDataModelRegistry.put(s, activityChooserModel);
            }
            return activityChooserModel;
        }
    }
    
    private boolean loadActivitiesIfNeeded() {
        boolean b = false;
        if (this.mReloadActivities) {
            b = b;
            if (this.mIntent != null) {
                this.mReloadActivities = false;
                this.mActivities.clear();
                final List queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0);
                for (int size = queryIntentActivities.size(), i = 0; i < size; ++i) {
                    this.mActivities.add(new ActivityResolveInfo(queryIntentActivities.get(i)));
                }
                b = true;
            }
        }
        return b;
    }
    
    private void persistHistoricalDataIfNeeded() {
        if (!this.mReadShareHistoryCalled) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        }
        if (this.mHistoricalRecordsChanged) {
            this.mHistoricalRecordsChanged = false;
            if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
                new PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[] { new ArrayList(this.mHistoricalRecords), this.mHistoryFileName });
            }
        }
    }
    
    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        final int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n > 0) {
            this.mHistoricalRecordsChanged = true;
            for (int i = 0; i < n; ++i) {
                final HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0);
            }
        }
    }
    
    private boolean readHistoricalDataIfNeeded() {
        if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            this.mCanReadHistoricalData = false;
            this.mReadShareHistoryCalled = true;
            this.readHistoricalDataImpl();
            return true;
        }
        return false;
    }
    
    private void readHistoricalDataImpl() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        android/support/v7/widget/ActivityChooserModel.mContext:Landroid/content/Context;
        //     4: aload_0        
        //     5: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //     8: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
        //    11: astore_2       
        //    12: invokestatic    android/util/Xml.newPullParser:()Lorg/xmlpull/v1/XmlPullParser;
        //    15: astore_3       
        //    16: aload_3        
        //    17: aload_2        
        //    18: ldc_w           "UTF-8"
        //    21: invokeinterface org/xmlpull/v1/XmlPullParser.setInput:(Ljava/io/InputStream;Ljava/lang/String;)V
        //    26: iconst_0       
        //    27: istore_1       
        //    28: iload_1        
        //    29: iconst_1       
        //    30: if_icmpeq       48
        //    33: iload_1        
        //    34: iconst_2       
        //    35: if_icmpeq       48
        //    38: aload_3        
        //    39: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    44: istore_1       
        //    45: goto            28
        //    48: ldc             "historical-records"
        //    50: aload_3        
        //    51: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //    56: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    59: ifne            116
        //    62: new             Lorg/xmlpull/v1/XmlPullParserException;
        //    65: dup            
        //    66: ldc_w           "Share records file does not start with historical-records tag."
        //    69: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //    72: athrow         
        //    73: astore_3       
        //    74: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //    77: new             Ljava/lang/StringBuilder;
        //    80: dup            
        //    81: invokespecial   java/lang/StringBuilder.<init>:()V
        //    84: ldc_w           "Error reading historical recrod file: "
        //    87: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    90: aload_0        
        //    91: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //    94: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    97: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   100: aload_3        
        //   101: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   104: pop            
        //   105: aload_2        
        //   106: ifnull          297
        //   109: aload_2        
        //   110: invokevirtual   java/io/FileInputStream.close:()V
        //   113: return         
        //   114: astore_2       
        //   115: return         
        //   116: aload_0        
        //   117: getfield        android/support/v7/widget/ActivityChooserModel.mHistoricalRecords:Ljava/util/List;
        //   120: astore          4
        //   122: aload           4
        //   124: invokeinterface java/util/List.clear:()V
        //   129: aload_3        
        //   130: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //   135: istore_1       
        //   136: iload_1        
        //   137: iconst_1       
        //   138: if_icmpne       152
        //   141: aload_2        
        //   142: ifnull          297
        //   145: aload_2        
        //   146: invokevirtual   java/io/FileInputStream.close:()V
        //   149: return         
        //   150: astore_2       
        //   151: return         
        //   152: iload_1        
        //   153: iconst_3       
        //   154: if_icmpeq       129
        //   157: iload_1        
        //   158: iconst_4       
        //   159: if_icmpeq       129
        //   162: ldc             "historical-record"
        //   164: aload_3        
        //   165: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //   170: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   173: ifne            230
        //   176: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   179: dup            
        //   180: ldc_w           "Share records file not well-formed."
        //   183: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   186: athrow         
        //   187: astore_3       
        //   188: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   191: new             Ljava/lang/StringBuilder;
        //   194: dup            
        //   195: invokespecial   java/lang/StringBuilder.<init>:()V
        //   198: ldc_w           "Error reading historical recrod file: "
        //   201: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   204: aload_0        
        //   205: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   208: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   211: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   214: aload_3        
        //   215: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   218: pop            
        //   219: aload_2        
        //   220: ifnull          297
        //   223: aload_2        
        //   224: invokevirtual   java/io/FileInputStream.close:()V
        //   227: return         
        //   228: astore_2       
        //   229: return         
        //   230: aload           4
        //   232: new             Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
        //   235: dup            
        //   236: aload_3        
        //   237: aconst_null    
        //   238: ldc             "activity"
        //   240: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   245: aload_3        
        //   246: aconst_null    
        //   247: ldc             "time"
        //   249: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   254: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   257: aload_3        
        //   258: aconst_null    
        //   259: ldc             "weight"
        //   261: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   266: invokestatic    java/lang/Float.parseFloat:(Ljava/lang/String;)F
        //   269: invokespecial   android/support/v7/widget/ActivityChooserModel$HistoricalRecord.<init>:(Ljava/lang/String;JF)V
        //   272: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   277: pop            
        //   278: goto            129
        //   281: astore_3       
        //   282: aload_2        
        //   283: ifnull          290
        //   286: aload_2        
        //   287: invokevirtual   java/io/FileInputStream.close:()V
        //   290: aload_3        
        //   291: athrow         
        //   292: astore_2       
        //   293: goto            290
        //   296: astore_2       
        //   297: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  0      12     296    297    Ljava/io/FileNotFoundException;
        //  12     26     73     116    Lorg/xmlpull/v1/XmlPullParserException;
        //  12     26     187    230    Ljava/io/IOException;
        //  12     26     281    296    Any
        //  38     45     73     116    Lorg/xmlpull/v1/XmlPullParserException;
        //  38     45     187    230    Ljava/io/IOException;
        //  38     45     281    296    Any
        //  48     73     73     116    Lorg/xmlpull/v1/XmlPullParserException;
        //  48     73     187    230    Ljava/io/IOException;
        //  48     73     281    296    Any
        //  74     105    281    296    Any
        //  109    113    114    116    Ljava/io/IOException;
        //  116    129    73     116    Lorg/xmlpull/v1/XmlPullParserException;
        //  116    129    187    230    Ljava/io/IOException;
        //  116    129    281    296    Any
        //  129    136    73     116    Lorg/xmlpull/v1/XmlPullParserException;
        //  129    136    187    230    Ljava/io/IOException;
        //  129    136    281    296    Any
        //  145    149    150    152    Ljava/io/IOException;
        //  162    187    73     116    Lorg/xmlpull/v1/XmlPullParserException;
        //  162    187    187    230    Ljava/io/IOException;
        //  162    187    281    296    Any
        //  188    219    281    296    Any
        //  223    227    228    230    Ljava/io/IOException;
        //  230    278    73     116    Lorg/xmlpull/v1/XmlPullParserException;
        //  230    278    187    230    Ljava/io/IOException;
        //  230    278    281    296    Any
        //  286    290    292    296    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0116:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList((List<? extends HistoricalRecord>)this.mHistoricalRecords));
            return true;
        }
        return false;
    }
    
    public Intent chooseActivity(final int n) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == null) {
                return null;
            }
            this.ensureConsistentState();
            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            final ComponentName component = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
            final Intent intent = new Intent(this.mIntent);
            intent.setComponent(component);
            if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(intent))) {
                return null;
            }
            this.addHistoricalRecord(new HistoricalRecord(component, System.currentTimeMillis(), 1.0f));
            return intent;
        }
    }
    
    public ResolveInfo getActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.get(n).resolveInfo;
        }
    }
    
    public int getActivityCount() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }
    
    public int getActivityIndex(final ResolveInfo resolveInfo) {
        while (true) {
            while (true) {
                int n;
                synchronized (this.mInstanceLock) {
                    this.ensureConsistentState();
                    final List<ActivityResolveInfo> mActivities = this.mActivities;
                    final int size = mActivities.size();
                    n = 0;
                    if (n >= size) {
                        return -1;
                    }
                    if (mActivities.get(n).resolveInfo == resolveInfo) {
                        return n;
                    }
                }
                ++n;
                continue;
            }
        }
    }
    
    public ResolveInfo getDefaultActivity() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            if (!this.mActivities.isEmpty()) {
                return this.mActivities.get(0).resolveInfo;
            }
            return null;
        }
    }
    
    public int getHistoryMaxSize() {
        synchronized (this.mInstanceLock) {
            return this.mHistoryMaxSize;
        }
    }
    
    public int getHistorySize() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }
    
    public Intent getIntent() {
        synchronized (this.mInstanceLock) {
            return this.mIntent;
        }
    }
    
    public void setActivitySorter(final ActivitySorter mActivitySorter) {
        synchronized (this.mInstanceLock) {
            if (this.mActivitySorter == mActivitySorter) {
                return;
            }
            this.mActivitySorter = mActivitySorter;
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
        }
    }
    
    public void setDefaultActivity(final int n) {
        while (true) {
            while (true) {
                synchronized (this.mInstanceLock) {
                    this.ensureConsistentState();
                    final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
                    final ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(0);
                    if (activityResolveInfo2 != null) {
                        final float n2 = activityResolveInfo2.weight - activityResolveInfo.weight + 5.0f;
                        this.addHistoricalRecord(new HistoricalRecord(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), System.currentTimeMillis(), n2));
                        return;
                    }
                }
                final float n2 = 1.0f;
                continue;
            }
        }
    }
    
    public void setHistoryMaxSize(final int mHistoryMaxSize) {
        synchronized (this.mInstanceLock) {
            if (this.mHistoryMaxSize == mHistoryMaxSize) {
                return;
            }
            this.mHistoryMaxSize = mHistoryMaxSize;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
        }
    }
    
    public void setIntent(final Intent mIntent) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == mIntent) {
                return;
            }
            this.mIntent = mIntent;
            this.mReloadActivities = true;
            this.ensureConsistentState();
        }
    }
    
    public void setOnChooseActivityListener(final OnChooseActivityListener mActivityChoserModelPolicy) {
        synchronized (this.mInstanceLock) {
            this.mActivityChoserModelPolicy = mActivityChoserModelPolicy;
        }
    }
    
    public interface ActivityChooserModelClient
    {
        void setActivityChooserModel(final ActivityChooserModel p0);
    }
    
    public final class ActivityResolveInfo implements Comparable<ActivityResolveInfo>
    {
        public final ResolveInfo resolveInfo;
        public float weight;
        
        public ActivityResolveInfo(final ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }
        
        @Override
        public int compareTo(final ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits(activityResolveInfo.weight) - Float.floatToIntBits(this.weight);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o == null) {
                    return false;
                }
                if (this.getClass() != o.getClass()) {
                    return false;
                }
                if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(((ActivityResolveInfo)o).weight)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return Float.floatToIntBits(this.weight) + 31;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("resolveInfo:").append(this.resolveInfo.toString());
            sb.append("; weight:").append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface ActivitySorter
    {
        void sort(final Intent p0, final List<ActivityResolveInfo> p1, final List<HistoricalRecord> p2);
    }
    
    private final class DefaultSorter implements ActivitySorter
    {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap;
        
        DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap<ComponentName, ActivityResolveInfo>();
        }
        
        @Override
        public void sort(final Intent intent, final List<ActivityResolveInfo> list, final List<HistoricalRecord> list2) {
            final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap = this.mPackageNameToActivityMap;
            mPackageNameToActivityMap.clear();
            for (int size = list.size(), i = 0; i < size; ++i) {
                final ActivityResolveInfo activityResolveInfo = list.get(i);
                activityResolveInfo.weight = 0.0f;
                mPackageNameToActivityMap.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
            }
            final int size2 = list2.size();
            float n = 1.0f;
            float n2;
            for (int j = size2 - 1; j >= 0; --j, n = n2) {
                final HistoricalRecord historicalRecord = list2.get(j);
                final ActivityResolveInfo activityResolveInfo2 = mPackageNameToActivityMap.get(historicalRecord.activity);
                n2 = n;
                if (activityResolveInfo2 != null) {
                    activityResolveInfo2.weight += historicalRecord.weight * n;
                    n2 = n * 0.95f;
                }
            }
            Collections.sort((List<Comparable>)list);
        }
    }
    
    public static final class HistoricalRecord
    {
        public final ComponentName activity;
        public final long time;
        public final float weight;
        
        public HistoricalRecord(final ComponentName activity, final long time, final float weight) {
            this.activity = activity;
            this.time = time;
            this.weight = weight;
        }
        
        public HistoricalRecord(final String s, final long n, final float n2) {
            this(ComponentName.unflattenFromString(s), n, n2);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this != o) {
                if (o == null) {
                    return false;
                }
                if (this.getClass() != o.getClass()) {
                    return false;
                }
                final HistoricalRecord historicalRecord = (HistoricalRecord)o;
                if (this.activity == null) {
                    if (historicalRecord.activity != null) {
                        return false;
                    }
                }
                else if (!this.activity.equals((Object)historicalRecord.activity)) {
                    return false;
                }
                if (this.time != historicalRecord.time) {
                    return false;
                }
                if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(historicalRecord.weight)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            int hashCode;
            if (this.activity == null) {
                hashCode = 0;
            }
            else {
                hashCode = this.activity.hashCode();
            }
            return ((hashCode + 31) * 31 + (int)(this.time ^ this.time >>> 32)) * 31 + Float.floatToIntBits(this.weight);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("; activity:").append(this.activity);
            sb.append("; time:").append(this.time);
            sb.append("; weight:").append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface OnChooseActivityListener
    {
        boolean onChooseActivity(final ActivityChooserModel p0, final Intent p1);
    }
    
    private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void>
    {
        public Void doInBackground(final Object... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_1        
            //     1: iconst_0       
            //     2: aaload         
            //     3: checkcast       Ljava/util/List;
            //     6: astore          4
            //     8: aload_1        
            //     9: iconst_1       
            //    10: aaload         
            //    11: checkcast       Ljava/lang/String;
            //    14: astore          5
            //    16: aload_0        
            //    17: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //    20: getfield        android/support/v7/widget/ActivityChooserModel.mContext:Landroid/content/Context;
            //    23: aload           5
            //    25: iconst_0       
            //    26: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
            //    29: astore_1       
            //    30: invokestatic    android/util/Xml.newSerializer:()Lorg/xmlpull/v1/XmlSerializer;
            //    33: astore          5
            //    35: aload           5
            //    37: aload_1        
            //    38: aconst_null    
            //    39: invokeinterface org/xmlpull/v1/XmlSerializer.setOutput:(Ljava/io/OutputStream;Ljava/lang/String;)V
            //    44: aload           5
            //    46: ldc             "UTF-8"
            //    48: iconst_1       
            //    49: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    52: invokeinterface org/xmlpull/v1/XmlSerializer.startDocument:(Ljava/lang/String;Ljava/lang/Boolean;)V
            //    57: aload           5
            //    59: aconst_null    
            //    60: ldc             "historical-records"
            //    62: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //    67: pop            
            //    68: aload           4
            //    70: invokeinterface java/util/List.size:()I
            //    75: istore_3       
            //    76: iconst_0       
            //    77: istore_2       
            //    78: iload_2        
            //    79: iload_3        
            //    80: if_icmpge       213
            //    83: aload           4
            //    85: iconst_0       
            //    86: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
            //    91: checkcast       Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
            //    94: astore          6
            //    96: aload           5
            //    98: aconst_null    
            //    99: ldc             "historical-record"
            //   101: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   106: pop            
            //   107: aload           5
            //   109: aconst_null    
            //   110: ldc             "activity"
            //   112: aload           6
            //   114: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.activity:Landroid/content/ComponentName;
            //   117: invokevirtual   android/content/ComponentName.flattenToString:()Ljava/lang/String;
            //   120: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   125: pop            
            //   126: aload           5
            //   128: aconst_null    
            //   129: ldc             "time"
            //   131: aload           6
            //   133: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.time:J
            //   136: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
            //   139: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   144: pop            
            //   145: aload           5
            //   147: aconst_null    
            //   148: ldc             "weight"
            //   150: aload           6
            //   152: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.weight:F
            //   155: invokestatic    java/lang/String.valueOf:(F)Ljava/lang/String;
            //   158: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   163: pop            
            //   164: aload           5
            //   166: aconst_null    
            //   167: ldc             "historical-record"
            //   169: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   174: pop            
            //   175: iload_2        
            //   176: iconst_1       
            //   177: iadd           
            //   178: istore_2       
            //   179: goto            78
            //   182: astore_1       
            //   183: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   186: new             Ljava/lang/StringBuilder;
            //   189: dup            
            //   190: invokespecial   java/lang/StringBuilder.<init>:()V
            //   193: ldc             "Error writing historical record file: "
            //   195: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   198: aload           5
            //   200: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   203: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   206: aload_1        
            //   207: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   210: pop            
            //   211: aconst_null    
            //   212: areturn        
            //   213: aload           5
            //   215: aconst_null    
            //   216: ldc             "historical-records"
            //   218: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   223: pop            
            //   224: aload           5
            //   226: invokeinterface org/xmlpull/v1/XmlSerializer.endDocument:()V
            //   231: aload_0        
            //   232: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   235: iconst_1       
            //   236: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   239: aload_1        
            //   240: ifnull          247
            //   243: aload_1        
            //   244: invokevirtual   java/io/FileOutputStream.close:()V
            //   247: aconst_null    
            //   248: areturn        
            //   249: astore          4
            //   251: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   254: new             Ljava/lang/StringBuilder;
            //   257: dup            
            //   258: invokespecial   java/lang/StringBuilder.<init>:()V
            //   261: ldc             "Error writing historical record file: "
            //   263: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   266: aload_0        
            //   267: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   270: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   273: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   276: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   279: aload           4
            //   281: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   284: pop            
            //   285: aload_0        
            //   286: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   289: iconst_1       
            //   290: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   293: aload_1        
            //   294: ifnull          247
            //   297: aload_1        
            //   298: invokevirtual   java/io/FileOutputStream.close:()V
            //   301: goto            247
            //   304: astore_1       
            //   305: goto            247
            //   308: astore          4
            //   310: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   313: new             Ljava/lang/StringBuilder;
            //   316: dup            
            //   317: invokespecial   java/lang/StringBuilder.<init>:()V
            //   320: ldc             "Error writing historical record file: "
            //   322: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   325: aload_0        
            //   326: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   329: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   332: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   335: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   338: aload           4
            //   340: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   343: pop            
            //   344: aload_0        
            //   345: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   348: iconst_1       
            //   349: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   352: aload_1        
            //   353: ifnull          247
            //   356: aload_1        
            //   357: invokevirtual   java/io/FileOutputStream.close:()V
            //   360: goto            247
            //   363: astore_1       
            //   364: goto            247
            //   367: astore          4
            //   369: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   372: new             Ljava/lang/StringBuilder;
            //   375: dup            
            //   376: invokespecial   java/lang/StringBuilder.<init>:()V
            //   379: ldc             "Error writing historical record file: "
            //   381: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   384: aload_0        
            //   385: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   388: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   391: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   394: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   397: aload           4
            //   399: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   402: pop            
            //   403: aload_0        
            //   404: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   407: iconst_1       
            //   408: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   411: aload_1        
            //   412: ifnull          247
            //   415: aload_1        
            //   416: invokevirtual   java/io/FileOutputStream.close:()V
            //   419: goto            247
            //   422: astore_1       
            //   423: goto            247
            //   426: astore          4
            //   428: aload_0        
            //   429: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   432: iconst_1       
            //   433: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   436: aload_1        
            //   437: ifnull          444
            //   440: aload_1        
            //   441: invokevirtual   java/io/FileOutputStream.close:()V
            //   444: aload           4
            //   446: athrow         
            //   447: astore_1       
            //   448: goto            247
            //   451: astore_1       
            //   452: goto            444
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                
            //  -----  -----  -----  -----  ------------------------------------
            //  16     30     182    213    Ljava/io/FileNotFoundException;
            //  35     76     249    308    Ljava/lang/IllegalArgumentException;
            //  35     76     308    367    Ljava/lang/IllegalStateException;
            //  35     76     367    426    Ljava/io/IOException;
            //  35     76     426    447    Any
            //  83     175    249    308    Ljava/lang/IllegalArgumentException;
            //  83     175    308    367    Ljava/lang/IllegalStateException;
            //  83     175    367    426    Ljava/io/IOException;
            //  83     175    426    447    Any
            //  213    231    249    308    Ljava/lang/IllegalArgumentException;
            //  213    231    308    367    Ljava/lang/IllegalStateException;
            //  213    231    367    426    Ljava/io/IOException;
            //  213    231    426    447    Any
            //  243    247    447    451    Ljava/io/IOException;
            //  251    285    426    447    Any
            //  297    301    304    308    Ljava/io/IOException;
            //  310    344    426    447    Any
            //  356    360    363    367    Ljava/io/IOException;
            //  369    403    426    447    Any
            //  415    419    422    426    Ljava/io/IOException;
            //  440    444    451    455    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index: 210, Size: 210
            //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
            //     at java.util.ArrayList.get(ArrayList.java:433)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
}
