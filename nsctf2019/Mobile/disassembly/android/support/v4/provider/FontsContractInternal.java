// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.provider;

import android.content.pm.PackageManager$NameNotFoundException;
import java.util.Collection;
import java.util.Collections;
import android.os.Bundle;
import android.util.Log;
import android.support.annotation.VisibleForTesting;
import android.content.pm.ProviderInfo;
import android.support.v4.os.ResultReceiver;
import android.support.v4.content.res.FontResourcesParserCompat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.Signature;
import android.os.HandlerThread;
import android.content.pm.PackageManager;
import android.support.annotation.GuardedBy;
import android.os.Handler;
import android.content.Context;
import java.util.Comparator;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class FontsContractInternal
{
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final String PARCEL_FONT_RESULTS = "font_results";
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final String TAG = "FontsContractCompat";
    private static final int THREAD_RENEWAL_THRESHOLD_MS = 10000;
    private static final Comparator<byte[]> sByteArrayComparator;
    private final Context mContext;
    @GuardedBy("mLock")
    private Handler mHandler;
    private final Object mLock;
    private final PackageManager mPackageManager;
    private final Runnable mReplaceDispatcherThreadRunnable;
    @GuardedBy("mLock")
    private HandlerThread mThread;
    
    static {
        sByteArrayComparator = new Comparator<byte[]>() {
            @Override
            public int compare(final byte[] array, final byte[] array2) {
                if (array.length != array2.length) {
                    return array.length - array2.length;
                }
                for (int i = 0; i < array.length; ++i) {
                    if (array[i] != array2[i]) {
                        return array[i] - array2[i];
                    }
                }
                return 0;
            }
        };
    }
    
    public FontsContractInternal(final Context context) {
        this.mLock = new Object();
        this.mReplaceDispatcherThreadRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (FontsContractInternal.this.mLock) {
                    if (FontsContractInternal.this.mThread != null) {
                        FontsContractInternal.this.mThread.quit();
                        FontsContractInternal.this.mThread = null;
                        FontsContractInternal.this.mHandler = null;
                    }
                }
            }
        };
        this.mContext = context.getApplicationContext();
        this.mPackageManager = this.mContext.getPackageManager();
    }
    
    FontsContractInternal(final Context mContext, final PackageManager mPackageManager) {
        this.mLock = new Object();
        this.mReplaceDispatcherThreadRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (FontsContractInternal.this.mLock) {
                    if (FontsContractInternal.this.mThread != null) {
                        FontsContractInternal.this.mThread.quit();
                        FontsContractInternal.this.mThread = null;
                        FontsContractInternal.this.mHandler = null;
                    }
                }
            }
        };
        this.mContext = mContext;
        this.mPackageManager = mPackageManager;
    }
    
    private List<byte[]> convertToByteArrayList(final Signature[] array) {
        final ArrayList<byte[]> list = new ArrayList<byte[]>();
        for (int i = 0; i < array.length; ++i) {
            list.add(array[i].toByteArray());
        }
        return list;
    }
    
    private boolean equalsByteArrayList(final List<byte[]> list, final List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (!Arrays.equals(list.get(i), list2.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private List<List<byte[]>> getCertificates(final FontRequest fontRequest) {
        if (fontRequest.getCertificates() != null) {
            return fontRequest.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(this.mContext.getResources(), fontRequest.getCertificatesArrayResId());
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void getFont(final FontRequest fontRequest, final ResultReceiver resultReceiver) {
        synchronized (this.mLock) {
            if (this.mHandler == null) {
                (this.mThread = new HandlerThread("fonts", 10)).start();
                this.mHandler = new Handler(this.mThread.getLooper());
            }
            this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final ProviderInfo provider = FontsContractInternal.this.getProvider(fontRequest, resultReceiver);
                    if (provider == null) {
                        return;
                    }
                    FontsContractInternal.this.getFontFromProvider(fontRequest, resultReceiver, provider.authority);
                }
            });
            this.mHandler.removeCallbacks(this.mReplaceDispatcherThreadRunnable);
            this.mHandler.postDelayed(this.mReplaceDispatcherThreadRunnable, 10000L);
        }
    }
    
    @VisibleForTesting
    void getFontFromProvider(final FontRequest p0, final ResultReceiver p1, final String p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/ArrayList.<init>:()V
        //     7: astore          16
        //     9: new             Landroid/net/Uri$Builder;
        //    12: dup            
        //    13: invokespecial   android/net/Uri$Builder.<init>:()V
        //    16: ldc             "content"
        //    18: invokevirtual   android/net/Uri$Builder.scheme:(Ljava/lang/String;)Landroid/net/Uri$Builder;
        //    21: aload_3        
        //    22: invokevirtual   android/net/Uri$Builder.authority:(Ljava/lang/String;)Landroid/net/Uri$Builder;
        //    25: invokevirtual   android/net/Uri$Builder.build:()Landroid/net/Uri;
        //    28: astore          17
        //    30: new             Landroid/net/Uri$Builder;
        //    33: dup            
        //    34: invokespecial   android/net/Uri$Builder.<init>:()V
        //    37: ldc             "content"
        //    39: invokevirtual   android/net/Uri$Builder.scheme:(Ljava/lang/String;)Landroid/net/Uri$Builder;
        //    42: aload_3        
        //    43: invokevirtual   android/net/Uri$Builder.authority:(Ljava/lang/String;)Landroid/net/Uri$Builder;
        //    46: ldc             "file"
        //    48: invokevirtual   android/net/Uri$Builder.appendPath:(Ljava/lang/String;)Landroid/net/Uri$Builder;
        //    51: invokevirtual   android/net/Uri$Builder.build:()Landroid/net/Uri;
        //    54: astore          18
        //    56: aconst_null    
        //    57: astore          15
        //    59: aload           15
        //    61: astore          14
        //    63: aload_0        
        //    64: getfield        android/support/v4/provider/FontsContractInternal.mContext:Landroid/content/Context;
        //    67: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    70: astore          19
        //    72: aload           15
        //    74: astore          14
        //    76: aload_1        
        //    77: invokevirtual   android/support/v4/provider/FontRequest.getQuery:()Ljava/lang/String;
        //    80: astore_1       
        //    81: aload           15
        //    83: astore          14
        //    85: aload           19
        //    87: aload           17
        //    89: bipush          7
        //    91: anewarray       Ljava/lang/String;
        //    94: dup            
        //    95: iconst_0       
        //    96: ldc             "_id"
        //    98: aastore        
        //    99: dup            
        //   100: iconst_1       
        //   101: ldc             "file_id"
        //   103: aastore        
        //   104: dup            
        //   105: iconst_2       
        //   106: ldc             "font_ttc_index"
        //   108: aastore        
        //   109: dup            
        //   110: iconst_3       
        //   111: ldc             "font_variation_settings"
        //   113: aastore        
        //   114: dup            
        //   115: iconst_4       
        //   116: ldc             "font_weight"
        //   118: aastore        
        //   119: dup            
        //   120: iconst_5       
        //   121: ldc             "font_italic"
        //   123: aastore        
        //   124: dup            
        //   125: bipush          6
        //   127: ldc             "result_code"
        //   129: aastore        
        //   130: ldc             "query = ?"
        //   132: iconst_1       
        //   133: anewarray       Ljava/lang/String;
        //   136: dup            
        //   137: iconst_0       
        //   138: aload_1        
        //   139: aastore        
        //   140: aconst_null    
        //   141: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   144: astore          15
        //   146: aload           15
        //   148: ifnull          652
        //   151: aload           15
        //   153: astore          14
        //   155: aload           15
        //   157: invokeinterface android/database/Cursor.getCount:()I
        //   162: ifle            652
        //   165: aload           15
        //   167: astore          14
        //   169: aload           15
        //   171: ldc             "result_code"
        //   173: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //   178: istore          6
        //   180: aload           15
        //   182: astore          14
        //   184: aload           15
        //   186: ldc             "_id"
        //   188: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //   193: istore          7
        //   195: aload           15
        //   197: astore          14
        //   199: aload           15
        //   201: ldc             "file_id"
        //   203: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //   208: istore          8
        //   210: aload           15
        //   212: astore          14
        //   214: aload           15
        //   216: ldc             "font_ttc_index"
        //   218: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //   223: istore          9
        //   225: aload           15
        //   227: astore          14
        //   229: aload           15
        //   231: ldc             "font_variation_settings"
        //   233: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //   238: istore          10
        //   240: aload           15
        //   242: astore          14
        //   244: aload           15
        //   246: ldc             "font_weight"
        //   248: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //   253: istore          11
        //   255: aload           15
        //   257: astore          14
        //   259: aload           15
        //   261: ldc             "font_italic"
        //   263: invokeinterface android/database/Cursor.getColumnIndex:(Ljava/lang/String;)I
        //   268: istore          12
        //   270: aload           15
        //   272: astore          14
        //   274: aload           15
        //   276: invokeinterface android/database/Cursor.moveToNext:()Z
        //   281: ifeq            652
        //   284: iload           6
        //   286: iconst_m1      
        //   287: if_icmpeq       355
        //   290: aload           15
        //   292: astore          14
        //   294: aload           15
        //   296: iload           6
        //   298: invokeinterface android/database/Cursor.getInt:(I)I
        //   303: istore          4
        //   305: goto            711
        //   308: aload           15
        //   310: astore          14
        //   312: aload           16
        //   314: invokevirtual   java/util/ArrayList.size:()I
        //   317: istore          6
        //   319: iload           4
        //   321: iload           6
        //   323: if_icmpge       361
        //   326: aload           15
        //   328: astore          14
        //   330: aload           16
        //   332: iload           4
        //   334: invokevirtual   java/util/ArrayList.get:(I)Ljava/lang/Object;
        //   337: checkcast       Landroid/support/v4/graphics/fonts/FontResult;
        //   340: invokevirtual   android/support/v4/graphics/fonts/FontResult.getFileDescriptor:()Landroid/os/ParcelFileDescriptor;
        //   343: invokevirtual   android/os/ParcelFileDescriptor.close:()V
        //   346: iload           4
        //   348: iconst_1       
        //   349: iadd           
        //   350: istore          4
        //   352: goto            308
        //   355: iconst_0       
        //   356: istore          4
        //   358: goto            711
        //   361: aload           15
        //   363: astore          14
        //   365: aload_2        
        //   366: iload           5
        //   368: aconst_null    
        //   369: invokevirtual   android/support/v4/os/ResultReceiver.send:(ILandroid/os/Bundle;)V
        //   372: aload           15
        //   374: ifnull          384
        //   377: aload           15
        //   379: invokeinterface android/database/Cursor.close:()V
        //   384: return         
        //   385: iload           8
        //   387: iconst_m1      
        //   388: if_icmpne       602
        //   391: aload           15
        //   393: astore          14
        //   395: aload           17
        //   397: aload           15
        //   399: iload           7
        //   401: invokeinterface android/database/Cursor.getLong:(I)J
        //   406: invokestatic    android/content/ContentUris.withAppendedId:(Landroid/net/Uri;J)Landroid/net/Uri;
        //   409: astore_1       
        //   410: aload           15
        //   412: astore          14
        //   414: aload_0        
        //   415: getfield        android/support/v4/provider/FontsContractInternal.mContext:Landroid/content/Context;
        //   418: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //   421: aload_1        
        //   422: ldc_w           "r"
        //   425: invokevirtual   android/content/ContentResolver.openFileDescriptor:(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;
        //   428: astore          19
        //   430: aload           19
        //   432: ifnull          270
        //   435: iload           9
        //   437: iconst_m1      
        //   438: if_icmpeq       624
        //   441: aload           15
        //   443: astore          14
        //   445: aload           15
        //   447: iload           9
        //   449: invokeinterface android/database/Cursor.getInt:(I)I
        //   454: istore          4
        //   456: iload           10
        //   458: iconst_m1      
        //   459: if_icmpeq       630
        //   462: aload           15
        //   464: astore          14
        //   466: aload           15
        //   468: iload           10
        //   470: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   475: astore_1       
        //   476: iload           11
        //   478: iconst_m1      
        //   479: if_icmpeq       641
        //   482: iload           12
        //   484: iconst_m1      
        //   485: if_icmpeq       641
        //   488: aload           15
        //   490: astore          14
        //   492: aload           15
        //   494: iload           11
        //   496: invokeinterface android/database/Cursor.getInt:(I)I
        //   501: istore          5
        //   503: aload           15
        //   505: astore          14
        //   507: aload           15
        //   509: iload           12
        //   511: invokeinterface android/database/Cursor.getInt:(I)I
        //   516: iconst_1       
        //   517: if_icmpne       635
        //   520: iconst_1       
        //   521: istore          13
        //   523: aload           15
        //   525: astore          14
        //   527: aload           16
        //   529: new             Landroid/support/v4/graphics/fonts/FontResult;
        //   532: dup            
        //   533: aload           19
        //   535: iload           4
        //   537: aload_1        
        //   538: iload           5
        //   540: iload           13
        //   542: invokespecial   android/support/v4/graphics/fonts/FontResult.<init>:(Landroid/os/ParcelFileDescriptor;ILjava/lang/String;IZ)V
        //   545: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   548: pop            
        //   549: goto            270
        //   552: astore_1       
        //   553: aload           15
        //   555: astore          14
        //   557: ldc             "FontsContractCompat"
        //   559: new             Ljava/lang/StringBuilder;
        //   562: dup            
        //   563: invokespecial   java/lang/StringBuilder.<init>:()V
        //   566: ldc_w           "FileNotFoundException raised when interacting with content provider "
        //   569: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   572: aload_3        
        //   573: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   576: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   579: aload_1        
        //   580: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   583: pop            
        //   584: goto            270
        //   587: astore_1       
        //   588: aload           14
        //   590: ifnull          600
        //   593: aload           14
        //   595: invokeinterface android/database/Cursor.close:()V
        //   600: aload_1        
        //   601: athrow         
        //   602: aload           15
        //   604: astore          14
        //   606: aload           18
        //   608: aload           15
        //   610: iload           8
        //   612: invokeinterface android/database/Cursor.getLong:(I)J
        //   617: invokestatic    android/content/ContentUris.withAppendedId:(Landroid/net/Uri;J)Landroid/net/Uri;
        //   620: astore_1       
        //   621: goto            410
        //   624: iconst_0       
        //   625: istore          4
        //   627: goto            456
        //   630: aconst_null    
        //   631: astore_1       
        //   632: goto            476
        //   635: iconst_0       
        //   636: istore          13
        //   638: goto            523
        //   641: sipush          400
        //   644: istore          5
        //   646: iconst_0       
        //   647: istore          13
        //   649: goto            523
        //   652: aload           15
        //   654: ifnull          664
        //   657: aload           15
        //   659: invokeinterface android/database/Cursor.close:()V
        //   664: aload           16
        //   666: ifnull          700
        //   669: aload           16
        //   671: invokevirtual   java/util/ArrayList.isEmpty:()Z
        //   674: ifne            700
        //   677: new             Landroid/os/Bundle;
        //   680: dup            
        //   681: invokespecial   android/os/Bundle.<init>:()V
        //   684: astore_1       
        //   685: aload_1        
        //   686: ldc             "font_results"
        //   688: aload           16
        //   690: invokevirtual   android/os/Bundle.putParcelableArrayList:(Ljava/lang/String;Ljava/util/ArrayList;)V
        //   693: aload_2        
        //   694: iconst_0       
        //   695: aload_1        
        //   696: invokevirtual   android/support/v4/os/ResultReceiver.send:(ILandroid/os/Bundle;)V
        //   699: return         
        //   700: aload_2        
        //   701: iconst_1       
        //   702: aconst_null    
        //   703: invokevirtual   android/support/v4/os/ResultReceiver.send:(ILandroid/os/Bundle;)V
        //   706: return         
        //   707: astore_1       
        //   708: goto            346
        //   711: iload           4
        //   713: ifeq            385
        //   716: iload           4
        //   718: istore          5
        //   720: iload           4
        //   722: ifge            728
        //   725: iconst_1       
        //   726: istore          5
        //   728: iconst_0       
        //   729: istore          4
        //   731: goto            308
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                           
        //  -----  -----  -----  -----  -------------------------------
        //  63     72     587    602    Any
        //  76     81     587    602    Any
        //  85     146    587    602    Any
        //  155    165    587    602    Any
        //  169    180    587    602    Any
        //  184    195    587    602    Any
        //  199    210    587    602    Any
        //  214    225    587    602    Any
        //  229    240    587    602    Any
        //  244    255    587    602    Any
        //  259    270    587    602    Any
        //  274    284    587    602    Any
        //  294    305    587    602    Any
        //  312    319    587    602    Any
        //  330    346    707    711    Ljava/io/IOException;
        //  330    346    587    602    Any
        //  365    372    587    602    Any
        //  395    410    587    602    Any
        //  414    430    552    587    Ljava/io/FileNotFoundException;
        //  414    430    587    602    Any
        //  445    456    552    587    Ljava/io/FileNotFoundException;
        //  445    456    587    602    Any
        //  466    476    552    587    Ljava/io/FileNotFoundException;
        //  466    476    587    602    Any
        //  492    503    552    587    Ljava/io/FileNotFoundException;
        //  492    503    587    602    Any
        //  507    520    552    587    Ljava/io/FileNotFoundException;
        //  507    520    587    602    Any
        //  527    549    552    587    Ljava/io/FileNotFoundException;
        //  527    549    587    602    Any
        //  557    584    587    602    Any
        //  606    621    587    602    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0346:
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
    
    @VisibleForTesting
    ProviderInfo getProvider(final FontRequest fontRequest, final ResultReceiver resultReceiver) {
        final String providerAuthority = fontRequest.getProviderAuthority();
        final ProviderInfo resolveContentProvider = this.mPackageManager.resolveContentProvider(providerAuthority, 0);
        ProviderInfo providerInfo;
        if (resolveContentProvider == null) {
            Log.e("FontsContractCompat", "Can't find content provider " + providerAuthority);
            resultReceiver.send(-1, null);
            providerInfo = null;
        }
        else {
            if (!resolveContentProvider.packageName.equals(fontRequest.getProviderPackage())) {
                Log.e("FontsContractCompat", "Found content provider " + providerAuthority + ", but package was not " + fontRequest.getProviderPackage());
                resultReceiver.send(-1, null);
                return null;
            }
            try {
                final List<byte[]> convertToByteArrayList = this.convertToByteArrayList(this.mPackageManager.getPackageInfo(resolveContentProvider.packageName, 64).signatures);
                Collections.sort((List<Object>)convertToByteArrayList, (Comparator<? super Object>)FontsContractInternal.sByteArrayComparator);
                final List<List<byte[]>> certificates = this.getCertificates(fontRequest);
                for (int i = 0; i < certificates.size(); ++i) {
                    final ArrayList list = new ArrayList<byte[]>((Collection<? extends T>)certificates.get(i));
                    Collections.sort((List<E>)list, (Comparator<? super E>)FontsContractInternal.sByteArrayComparator);
                    providerInfo = resolveContentProvider;
                    if (this.equalsByteArrayList(convertToByteArrayList, (List<byte[]>)list)) {
                        return providerInfo;
                    }
                }
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.e("FontsContractCompat", "Can't find content provider " + providerAuthority, (Throwable)ex);
                resultReceiver.send(-1, null);
                return null;
            }
            Log.e("FontsContractCompat", "Certificates don't match for given provider " + providerAuthority);
            resultReceiver.send(-2, null);
            return null;
        }
        return providerInfo;
    }
}
