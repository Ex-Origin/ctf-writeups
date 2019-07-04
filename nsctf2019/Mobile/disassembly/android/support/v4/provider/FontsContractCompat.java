// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.support.v4.util.Preconditions;
import android.support.annotation.IntRange;
import android.provider.BaseColumns;
import android.os.Handler;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import android.content.pm.PackageManager;
import android.support.annotation.VisibleForTesting;
import android.content.ContentResolver;
import android.net.Uri;
import android.content.ContentUris;
import android.os.Build$VERSION;
import android.net.Uri$Builder;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.content.res.Resources;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.pm.ProviderInfo;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.Signature;
import android.support.v4.graphics.TypefaceCompat;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.content.Context;
import java.util.Comparator;
import android.support.annotation.RestrictTo;

public class FontsContractCompat
{
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final String PARCEL_FONT_RESULTS = "font_results";
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final String TAG = "FontsContractCompat";
    private static final Comparator<byte[]> sByteArrayComparator;
    
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
    
    public static Typeface buildTypeface(@NonNull final Context context, @Nullable final CancellationSignal cancellationSignal, @NonNull final FontInfo[] array) {
        return TypefaceCompat.createTypeface(context, array, prepareFontData(context, array, cancellationSignal)).getTypeface();
    }
    
    private static List<byte[]> convertToByteArrayList(final Signature[] array) {
        final ArrayList<byte[]> list = new ArrayList<byte[]>();
        for (int i = 0; i < array.length; ++i) {
            list.add(array[i].toByteArray());
        }
        return list;
    }
    
    private static boolean equalsByteArrayList(final List<byte[]> list, final List<byte[]> list2) {
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
    
    @NonNull
    public static FontFamilyResult fetchFonts(@NonNull final Context context, @Nullable final CancellationSignal cancellationSignal, @NonNull final FontRequest fontRequest) throws PackageManager$NameNotFoundException {
        final ProviderInfo provider = getProvider(context.getPackageManager(), fontRequest, context.getResources());
        if (provider == null) {
            return new FontFamilyResult(1, null);
        }
        return new FontFamilyResult(0, getFontFromProvider(context, fontRequest, provider.authority, cancellationSignal));
    }
    
    private static List<List<byte[]>> getCertificates(final FontRequest fontRequest, final Resources resources) {
        if (fontRequest.getCertificates() != null) {
            return fontRequest.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(resources, fontRequest.getCertificatesArrayResId());
    }
    
    @NonNull
    @VisibleForTesting
    static FontInfo[] getFontFromProvider(Context context, final FontRequest fontRequest, String s, CancellationSignal cancellationSignal) {
        final ArrayList list = new ArrayList();
        final Uri build = new Uri$Builder().scheme("content").authority(s).build();
        final Uri build2 = new Uri$Builder().scheme("content").authority(s).appendPath("file").build();
        final String s2 = s = null;
        while (true) {
            try {
                if (Build$VERSION.SDK_INT > 16) {
                    s = s2;
                    final ContentResolver contentResolver = context.getContentResolver();
                    s = s2;
                    final String query = fontRequest.getQuery();
                    s = s2;
                    context = (Context)contentResolver.query(build, new String[] { "_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code" }, "query = ?", new String[] { query }, (String)null, cancellationSignal);
                }
                else {
                    s = s2;
                    final ContentResolver contentResolver2 = context.getContentResolver();
                    s = s2;
                    final String query2 = fontRequest.getQuery();
                    s = s2;
                    context = (Context)contentResolver2.query(build, new String[] { "_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code" }, "query = ?", new String[] { query2 }, (String)null);
                }
                Object o = list;
                if (context != null) {
                    o = list;
                    s = (String)context;
                    if (((Cursor)context).getCount() > 0) {
                        int columnIndex;
                        int columnIndex2;
                        int columnIndex3 = 0;
                        int columnIndex4;
                        int columnIndex5;
                        int columnIndex6;
                        int int1;
                        int int2;
                        Object o2 = null;
                        int int3;
                        boolean b;
                        Label_0283_Outer:Label_0303_Outer:Label_0319_Outer:Label_0340_Outer:
                        while (true) {
                            s = (String)context;
                            columnIndex = ((Cursor)context).getColumnIndex("result_code");
                            s = (String)context;
                            cancellationSignal = (CancellationSignal)new ArrayList();
                            Label_0505: {
                                while (true) {
                                Label_0499:
                                    while (true) {
                                    Label_0491:
                                        while (true) {
                                        Label_0474:
                                            while (true) {
                                            Label_0468:
                                                while (true) {
                                                    Label_0462: {
                                                        try {
                                                            columnIndex2 = ((Cursor)context).getColumnIndex("_id");
                                                            columnIndex3 = ((Cursor)context).getColumnIndex("file_id");
                                                            columnIndex4 = ((Cursor)context).getColumnIndex("font_ttc_index");
                                                            columnIndex5 = ((Cursor)context).getColumnIndex("font_weight");
                                                            columnIndex6 = ((Cursor)context).getColumnIndex("font_italic");
                                                            while (((Cursor)context).moveToNext()) {
                                                                if (columnIndex == -1) {
                                                                    break Label_0462;
                                                                }
                                                                int1 = ((Cursor)context).getInt(columnIndex);
                                                                if (columnIndex4 == -1) {
                                                                    break Label_0468;
                                                                }
                                                                int2 = ((Cursor)context).getInt(columnIndex4);
                                                                if (columnIndex3 != -1) {
                                                                    break Label_0474;
                                                                }
                                                                o2 = ContentUris.withAppendedId(build, ((Cursor)context).getLong(columnIndex2));
                                                                if (columnIndex5 == -1) {
                                                                    break Label_0491;
                                                                }
                                                                int3 = ((Cursor)context).getInt(columnIndex5);
                                                                if (columnIndex6 == -1 || ((Cursor)context).getInt(columnIndex6) != 1) {
                                                                    break Label_0499;
                                                                }
                                                                b = true;
                                                                ((ArrayList<FontInfo>)cancellationSignal).add(new FontInfo((Uri)o2, int2, int3, b, int1));
                                                            }
                                                            break Label_0505;
                                                        }
                                                        finally {
                                                            s = (String)context;
                                                            context = (Context)o2;
                                                        }
                                                    }
                                                    int1 = 0;
                                                    continue Label_0283_Outer;
                                                }
                                                int2 = 0;
                                                continue Label_0303_Outer;
                                            }
                                            o2 = ContentUris.withAppendedId(build2, ((Cursor)context).getLong(columnIndex3));
                                            continue Label_0319_Outer;
                                        }
                                        int3 = 400;
                                        continue Label_0340_Outer;
                                    }
                                    b = false;
                                    continue;
                                }
                                if (s != null) {
                                    ((Cursor)s).close();
                                }
                                throw context;
                            }
                            o = cancellationSignal;
                            break;
                        }
                    }
                }
                if (context != null) {
                    ((Cursor)context).close();
                }
                return (FontInfo[])((ArrayList)o).toArray(new FontInfo[0]);
            }
            finally {
                continue;
            }
            break;
        }
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @VisibleForTesting
    public static ProviderInfo getProvider(@NonNull final PackageManager packageManager, @NonNull final FontRequest fontRequest, @Nullable final Resources resources) throws PackageManager$NameNotFoundException {
        final String providerAuthority = fontRequest.getProviderAuthority();
        final ProviderInfo resolveContentProvider = packageManager.resolveContentProvider(providerAuthority, 0);
        if (resolveContentProvider == null) {
            throw new PackageManager$NameNotFoundException("No package found for authority: " + providerAuthority);
        }
        if (!resolveContentProvider.packageName.equals(fontRequest.getProviderPackage())) {
            throw new PackageManager$NameNotFoundException("Found content provider " + providerAuthority + ", but package was not " + fontRequest.getProviderPackage());
        }
        final List<byte[]> convertToByteArrayList = convertToByteArrayList(packageManager.getPackageInfo(resolveContentProvider.packageName, 64).signatures);
        Collections.sort((List<Object>)convertToByteArrayList, (Comparator<? super Object>)FontsContractCompat.sByteArrayComparator);
        final List<List<byte[]>> certificates = getCertificates(fontRequest, resources);
        for (int i = 0; i < certificates.size(); ++i) {
            final ArrayList list = new ArrayList<byte[]>((Collection<? extends T>)certificates.get(i));
            Collections.sort((List<E>)list, (Comparator<? super E>)FontsContractCompat.sByteArrayComparator);
            if (equalsByteArrayList(convertToByteArrayList, (List<byte[]>)list)) {
                return resolveContentProvider;
            }
        }
        return null;
    }
    
    private static Map<Uri, ByteBuffer> prepareFontData(final Context p0, final FontInfo[] p1, final CancellationSignal p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: new             Ljava/util/HashMap;
        //     3: dup            
        //     4: invokespecial   java/util/HashMap.<init>:()V
        //     7: astore          8
        //     9: aload_0        
        //    10: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //    13: astore          9
        //    15: aload_1        
        //    16: arraylength    
        //    17: istore          4
        //    19: iconst_0       
        //    20: istore_3       
        //    21: iload_3        
        //    22: iload           4
        //    24: if_icmpge       144
        //    27: aload_1        
        //    28: iload_3        
        //    29: aaload         
        //    30: astore_0       
        //    31: aload_0        
        //    32: invokevirtual   android/support/v4/provider/FontsContractCompat$FontInfo.getResultCode:()I
        //    35: ifeq            45
        //    38: iload_3        
        //    39: iconst_1       
        //    40: iadd           
        //    41: istore_3       
        //    42: goto            21
        //    45: aload_0        
        //    46: invokevirtual   android/support/v4/provider/FontsContractCompat$FontInfo.getUri:()Landroid/net/Uri;
        //    49: astore          10
        //    51: aload           8
        //    53: aload           10
        //    55: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //    58: ifne            38
        //    61: aconst_null    
        //    62: astore          7
        //    64: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    67: bipush          19
        //    69: if_icmple       130
        //    72: aload           9
        //    74: aload           10
        //    76: ldc_w           "r"
        //    79: aload_2        
        //    80: invokevirtual   android/content/ContentResolver.openFileDescriptor:(Landroid/net/Uri;Ljava/lang/String;Landroid/os/CancellationSignal;)Landroid/os/ParcelFileDescriptor;
        //    83: astore_0       
        //    84: new             Ljava/io/FileInputStream;
        //    87: dup            
        //    88: aload_0        
        //    89: invokevirtual   android/os/ParcelFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
        //    92: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/FileDescriptor;)V
        //    95: astore_0       
        //    96: aload_0        
        //    97: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
        //   100: astore_0       
        //   101: aload_0        
        //   102: invokevirtual   java/nio/channels/FileChannel.size:()J
        //   105: lstore          5
        //   107: aload_0        
        //   108: getstatic       java/nio/channels/FileChannel$MapMode.READ_ONLY:Ljava/nio/channels/FileChannel$MapMode;
        //   111: lconst_0       
        //   112: lload           5
        //   114: invokevirtual   java/nio/channels/FileChannel.map:(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
        //   117: astore_0       
        //   118: aload           8
        //   120: aload           10
        //   122: aload_0        
        //   123: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   126: pop            
        //   127: goto            38
        //   130: aload           9
        //   132: aload           10
        //   134: ldc_w           "r"
        //   137: invokevirtual   android/content/ContentResolver.openFileDescriptor:(Landroid/net/Uri;Ljava/lang/String;)Landroid/os/ParcelFileDescriptor;
        //   140: astore_0       
        //   141: goto            84
        //   144: aload           8
        //   146: invokestatic    java/util/Collections.unmodifiableMap:(Ljava/util/Map;)Ljava/util/Map;
        //   149: areturn        
        //   150: astore_0       
        //   151: aload           7
        //   153: astore_0       
        //   154: goto            118
        //   157: astore_0       
        //   158: aload           7
        //   160: astore_0       
        //   161: goto            118
        //    Signature:
        //  (Landroid/content/Context;[Landroid/support/v4/provider/FontsContractCompat$FontInfo;Landroid/os/CancellationSignal;)Ljava/util/Map<Landroid/net/Uri;Ljava/nio/ByteBuffer;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  64     84     150    157    Ljava/io/IOException;
        //  84     96     150    157    Ljava/io/IOException;
        //  96     118    157    164    Ljava/io/IOException;
        //  130    141    150    157    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0118:
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
    
    public static void requestFont(@NonNull final Context context, @NonNull final FontRequest fontRequest, @NonNull final FontRequestCallback fontRequestCallback, @NonNull final Handler handler) {
        handler.post((Runnable)new Runnable() {
            final /* synthetic */ Handler val$callerThreadHandler = new Handler();
            
            @Override
            public void run() {
                FontFamilyResult fetchFonts = null;
                Label_0117: {
                    Label_0100: {
                        try {
                            fetchFonts = FontsContractCompat.fetchFonts(context, null, fontRequest);
                            if (fetchFonts.getStatusCode() == 0) {
                                break Label_0117;
                            }
                            switch (fetchFonts.getStatusCode()) {
                                default: {
                                    this.val$callerThreadHandler.post((Runnable)new Runnable() {
                                        @Override
                                        public void run() {
                                            fontRequestCallback.onTypefaceRequestFailed(-3);
                                        }
                                    });
                                    return;
                                }
                                case 1: {
                                    break;
                                }
                                case 2: {
                                    break Label_0100;
                                }
                            }
                        }
                        catch (PackageManager$NameNotFoundException ex) {
                            this.val$callerThreadHandler.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(-1);
                                }
                            });
                            return;
                        }
                        this.val$callerThreadHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                fontRequestCallback.onTypefaceRequestFailed(-2);
                            }
                        });
                        return;
                    }
                    this.val$callerThreadHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            fontRequestCallback.onTypefaceRequestFailed(-3);
                        }
                    });
                    return;
                }
                final FontInfo[] fonts = fetchFonts.getFonts();
                if (fonts == null || fonts.length == 0) {
                    this.val$callerThreadHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            fontRequestCallback.onTypefaceRequestFailed(1);
                        }
                    });
                    return;
                }
                final int length = fonts.length;
                int i = 0;
                while (i < length) {
                    final FontInfo fontInfo = fonts[i];
                    if (fontInfo.getResultCode() != 0) {
                        final int resultCode = fontInfo.getResultCode();
                        if (resultCode < 0) {
                            this.val$callerThreadHandler.post((Runnable)new Runnable() {
                                @Override
                                public void run() {
                                    fontRequestCallback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        this.val$callerThreadHandler.post((Runnable)new Runnable() {
                            @Override
                            public void run() {
                                fontRequestCallback.onTypefaceRequestFailed(resultCode);
                            }
                        });
                        return;
                    }
                    else {
                        ++i;
                    }
                }
                final Typeface buildTypeface = FontsContractCompat.buildTypeface(context, null, fonts);
                if (buildTypeface == null) {
                    this.val$callerThreadHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            fontRequestCallback.onTypefaceRequestFailed(-3);
                        }
                    });
                    return;
                }
                this.val$callerThreadHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        fontRequestCallback.onTypefaceRetrieved(buildTypeface);
                    }
                });
            }
        });
    }
    
    public static final class Columns implements BaseColumns
    {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";
    }
    
    public static class FontFamilyResult
    {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;
        
        public FontFamilyResult(final int mStatusCode, @Nullable final FontInfo[] mFonts) {
            this.mStatusCode = mStatusCode;
            this.mFonts = mFonts;
        }
        
        public FontInfo[] getFonts() {
            return this.mFonts;
        }
        
        public int getStatusCode() {
            return this.mStatusCode;
        }
    }
    
    public static class FontInfo
    {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;
        
        public FontInfo(@NonNull final Uri uri, @IntRange(from = 0L) final int mTtcIndex, @IntRange(from = 1L, to = 1000L) final int mWeight, final boolean mItalic, final int mResultCode) {
            this.mUri = Preconditions.checkNotNull(uri);
            this.mTtcIndex = mTtcIndex;
            this.mWeight = mWeight;
            this.mItalic = mItalic;
            this.mResultCode = mResultCode;
        }
        
        public int getResultCode() {
            return this.mResultCode;
        }
        
        @IntRange(from = 0L)
        public int getTtcIndex() {
            return this.mTtcIndex;
        }
        
        @NonNull
        public Uri getUri() {
            return this.mUri;
        }
        
        @IntRange(from = 1L, to = 1000L)
        public int getWeight() {
            return this.mWeight;
        }
        
        public boolean isItalic() {
            return this.mItalic;
        }
    }
    
    public static class FontRequestCallback
    {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
        
        public void onTypefaceRequestFailed(final int n) {
        }
        
        public void onTypefaceRetrieved(final Typeface typeface) {
        }
    }
}
