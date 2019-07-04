// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import android.support.annotation.VisibleForTesting;
import java.util.ArrayList;
import android.net.Uri;
import java.util.Map;
import android.support.v4.provider.FontsContractCompat;
import java.io.FileInputStream;
import android.support.v4.graphics.fonts.FontResult;
import java.util.List;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.provider.FontRequest;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.content.res.Resources;
import java.nio.ByteBuffer;
import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import android.util.Log;
import java.io.Closeable;
import android.content.Context;
import android.support.annotation.GuardedBy;
import android.support.v4.provider.FontsContractInternal;
import android.support.v4.util.LruCache;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class TypefaceCompatBaseImpl implements TypefaceCompatImpl
{
    private static final String CACHE_FILE_PREFIX = "cached_font_";
    private static final int SYNC_FETCH_TIMEOUT_MS = 500;
    private static final String TAG = "TypefaceCompatBaseImpl";
    private static final boolean VERBOSE_TRACING = false;
    private static final LruCache<String, TypefaceHolder> sDynamicTypefaceCache;
    @GuardedBy("sLock")
    private static FontsContractInternal sFontsContract;
    private static final Object sLock;
    private final Context mApplicationContext;
    
    static {
        sDynamicTypefaceCache = new LruCache<String, TypefaceHolder>(16);
        sLock = new Object();
    }
    
    TypefaceCompatBaseImpl(final Context context) {
        this.mApplicationContext = context.getApplicationContext();
    }
    
    static void closeQuietly(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (IOException ex) {
            Log.e("TypefaceCompatBaseImpl", "Error closing stream", (Throwable)ex);
        }
    }
    
    static void closeQuietly(final InputStream inputStream) {
        if (inputStream == null) {
            return;
        }
        try {
            inputStream.close();
        }
        catch (IOException ex) {
            Log.e("TypefaceCompatBaseImpl", "Error closing input stream", (Throwable)ex);
        }
    }
    
    private File copyToCacheFile(final InputStream inputStream) {
        final Closeable closeable = null;
        final byte[] array = null;
        Object o = closeable;
        while (true) {
            try {
                try {
                    final File file = new File(this.mApplicationContext.getCacheDir(), "cached_font_" + Thread.currentThread().getId());
                    o = closeable;
                    Object o2 = new FileOutputStream(file, false);
                    Label_0118: {
                        try {
                            o = new byte[1024];
                            while (true) {
                                final int read = inputStream.read((byte[])o);
                                if (read == -1) {
                                    break Label_0118;
                                }
                                ((FileOutputStream)o2).write((byte[])o, 0, read);
                            }
                        }
                        catch (IOException ex) {
                            o = o2;
                            Log.e("TypefaceCompatBaseImpl", "Error copying font file descriptor to temp local file.", (Throwable)file);
                            closeQuietly(inputStream);
                            closeQuietly((Closeable)o2);
                            return null;
                            closeQuietly(inputStream);
                            closeQuietly((Closeable)o2);
                            final FileOutputStream fileOutputStream;
                            return (File)fileOutputStream;
                            closeQuietly(inputStream);
                            closeQuietly((Closeable)o);
                            throw o2;
                        }
                        finally {
                            o = o2;
                            final FileOutputStream fileOutputStream;
                            o2 = fileOutputStream;
                        }
                    }
                }
                finally {}
            }
            catch (IOException file) {
                final Object o2 = array;
                continue;
            }
            break;
        }
    }
    
    private File copyToCacheFile(final ByteBuffer byteBuffer) {
        Closeable closeable = null;
        final Closeable closeable2 = null;
        Object o = closeable;
        while (true) {
            try {
                try {
                    final File file = new File(this.mApplicationContext.getCacheDir(), "cached_font_" + Thread.currentThread().getId());
                    o = closeable;
                    closeable = new FileOutputStream(file, false);
                    try {
                        o = new byte[1024];
                        while (byteBuffer.hasRemaining()) {
                            final int min = Math.min(1024, byteBuffer.remaining());
                            byteBuffer.get((byte[])o, 0, min);
                            ((FileOutputStream)closeable).write((byte[])o, 0, min);
                        }
                    }
                    catch (IOException o) {
                        final Closeable closeable3 = closeable;
                        closeable = (Closeable)o;
                        o = closeable3;
                        Log.e("TypefaceCompatBaseImpl", "Error copying font file descriptor to temp local file.", (Throwable)closeable);
                        closeQuietly(closeable3);
                        return null;
                        closeQuietly(closeable);
                        return file;
                        closeQuietly((Closeable)o);
                        throw;
                    }
                    finally {
                        o = closeable;
                    }
                }
                finally {}
            }
            catch (IOException closeable) {
                final Closeable closeable3 = closeable2;
                continue;
            }
            break;
        }
    }
    
    private static String createAssetUid(final Resources resources, final int n, final int n2) {
        return resources.getResourcePackageName(n) + "-" + n + "-" + n2;
    }
    
    @Nullable
    private TypefaceHolder createFromResources(final FontResourcesParserCompat.ProviderResourceEntry providerResourceEntry) {
        final TypefaceHolder fromCache = findFromCache(providerResourceEntry.getAuthority(), providerResourceEntry.getQuery());
        if (fromCache != null) {
            return fromCache;
        }
        final FontRequest fontRequest = new FontRequest(providerResourceEntry.getAuthority(), providerResourceEntry.getPackage(), providerResourceEntry.getQuery(), providerResourceEntry.getCerts());
        final WaitableCallback waitableCallback = new WaitableCallback(providerResourceEntry.getAuthority() + "/" + providerResourceEntry.getQuery());
        this.create(fontRequest, waitableCallback);
        return new TypefaceCompat.TypefaceHolder(waitableCallback.waitWithTimeout(500L), 400, false);
    }
    
    private static String createProviderUid(final String s, final String s2) {
        return "provider:" + s + "-" + s2;
    }
    
    private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final int n, final boolean b) {
        final FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry = null;
        int n2 = Integer.MAX_VALUE;
        final FontResourcesParserCompat.FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        final int length = entries.length;
        int i = 0;
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry2 = fontFileResourceEntry;
        while (i < length) {
            final FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry3 = entries[i];
            final int abs = Math.abs(fontFileResourceEntry3.getWeight() - n);
            int n3;
            if (b == fontFileResourceEntry3.isItalic()) {
                n3 = 0;
            }
            else {
                n3 = 1;
            }
            final int n4 = abs * 2 + n3;
            int n5;
            if (fontFileResourceEntry2 == null || (n5 = n2) > n4) {
                fontFileResourceEntry2 = fontFileResourceEntry3;
                n5 = n4;
            }
            ++i;
            n2 = n5;
        }
        return fontFileResourceEntry2;
    }
    
    private static TypefaceHolder findFromCache(String providerUid, final String s) {
        synchronized (TypefaceCompatBaseImpl.sDynamicTypefaceCache) {
            providerUid = createProviderUid(providerUid, s);
            final TypefaceHolder typefaceHolder = TypefaceCompatBaseImpl.sDynamicTypefaceCache.get(providerUid);
            if (typefaceHolder != null) {
                return typefaceHolder;
            }
            return null;
        }
    }
    
    static void putInCache(final String s, String providerUid, final TypefaceHolder typefaceHolder) {
        providerUid = createProviderUid(s, providerUid);
        synchronized (TypefaceCompatBaseImpl.sDynamicTypefaceCache) {
            TypefaceCompatBaseImpl.sDynamicTypefaceCache.put(providerUid, typefaceHolder);
        }
    }
    
    public void create(@NonNull final FontRequest fontRequest, @NonNull final FontRequestCallback fontRequestCallback) {
        final TypefaceHolder fromCache = findFromCache(fontRequest.getProviderAuthority(), fontRequest.getQuery());
        if (fromCache != null) {
            fontRequestCallback.onTypefaceRetrieved(fromCache.getTypeface());
        }
        synchronized (TypefaceCompatBaseImpl.sLock) {
            if (TypefaceCompatBaseImpl.sFontsContract == null) {
                TypefaceCompatBaseImpl.sFontsContract = new FontsContractInternal(this.mApplicationContext);
            }
            TypefaceCompatBaseImpl.sFontsContract.getFont(fontRequest, new ResultReceiver(null) {
                public void onReceiveResult(final int n, final Bundle bundle) {
                    TypefaceCompatBaseImpl.this.receiveResult(fontRequest, fontRequestCallback, n, bundle);
                }
            });
        }
    }
    
    @Nullable
    TypefaceHolder createFromResources(FontResourcesParserCompat.FontFamilyFilesResourceEntry openRawResource, final Resources resources, int n, final int n2) {
        if ((n2 & 0x1) == 0x0) {
            n = 400;
        }
        else {
            n = 700;
        }
        final FontResourcesParserCompat.FontFileResourceEntry bestEntry = this.findBestEntry(openRawResource, n, (n2 & 0x2) != 0x0);
        if (bestEntry == null) {
            return null;
        }
        InputStream inputStream = null;
        openRawResource = null;
        try {
            final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry = (FontResourcesParserCompat.FontFamilyFilesResourceEntry)(inputStream = (InputStream)(openRawResource = (FontResourcesParserCompat.FontFamilyFilesResourceEntry)resources.openRawResource(bestEntry.getResourceId())));
            final Typeface typeface = this.createTypeface(resources, (InputStream)fontFamilyFilesResourceEntry);
            if (typeface == null) {
                return null;
            }
            openRawResource = fontFamilyFilesResourceEntry;
            inputStream = (InputStream)fontFamilyFilesResourceEntry;
            final Typeface create = Typeface.create(typeface, n2);
            if (create == null) {
                return null;
            }
            openRawResource = fontFamilyFilesResourceEntry;
            inputStream = (InputStream)fontFamilyFilesResourceEntry;
            return new TypefaceCompat.TypefaceHolder(create, bestEntry.getWeight(), bestEntry.isItalic());
        }
        catch (IOException ex) {
            return null;
        }
        finally {
            closeQuietly(inputStream);
        }
    }
    
    @Nullable
    @Override
    public TypefaceHolder createFromResourcesFamilyXml(final FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry, final Resources resources, final int n, final int n2) {
        TypefaceHolder fromResources;
        if (familyResourceEntry instanceof FontResourcesParserCompat.ProviderResourceEntry) {
            fromResources = this.createFromResources((FontResourcesParserCompat.ProviderResourceEntry)familyResourceEntry);
        }
        else {
            final TypefaceHolder fromResources2 = this.createFromResources((FontResourcesParserCompat.FontFamilyFilesResourceEntry)familyResourceEntry, resources, n, n2);
            if ((fromResources = fromResources2) != null) {
                TypefaceCompatBaseImpl.sDynamicTypefaceCache.put(createAssetUid(resources, n, n2), fromResources2);
                return fromResources2;
            }
        }
        return fromResources;
    }
    
    @Nullable
    @Override
    public TypefaceHolder createFromResourcesFontFile(final Resources resources, final int n, final int n2) {
        InputStream openRawResource = null;
        try {
            final InputStream inputStream = openRawResource = resources.openRawResource(n);
            final Typeface typeface = this.createTypeface(resources, inputStream);
            if (typeface == null) {
                return null;
            }
            openRawResource = inputStream;
            final Typeface create = Typeface.create(typeface, n2);
            if (create == null) {
                return null;
            }
            openRawResource = inputStream;
            final TypefaceCompat.TypefaceHolder typefaceHolder = new TypefaceCompat.TypefaceHolder(create, 400, false);
            openRawResource = inputStream;
            final String assetUid = createAssetUid(resources, n, n2);
            openRawResource = inputStream;
            TypefaceCompatBaseImpl.sDynamicTypefaceCache.put(assetUid, typefaceHolder);
            return typefaceHolder;
        }
        catch (IOException ex) {
            return null;
        }
        finally {
            closeQuietly(openRawResource);
        }
    }
    
    Typeface createTypeface(final Resources resources, InputStream copyToCacheFile) throws IOException {
        Typeface fromFile = null;
        copyToCacheFile = (InputStream)this.copyToCacheFile(copyToCacheFile);
        if (copyToCacheFile == null) {
            return fromFile;
        }
        try {
            fromFile = Typeface.createFromFile(((File)copyToCacheFile).getPath());
            return fromFile;
        }
        catch (RuntimeException ex) {
            Log.e("TypefaceCompatBaseImpl", "Failed to create font", (Throwable)ex);
            return null;
        }
        finally {
            ((File)copyToCacheFile).delete();
        }
    }
    
    @Override
    public TypefaceHolder createTypeface(final List<FontResult> list) {
        final Typeface typeface = null;
        final FontResult fontResult = list.get(0);
        final File copyToCacheFile = this.copyToCacheFile(new FileInputStream(fontResult.getFileDescriptor().getFileDescriptor()));
        Typeface fromFile = typeface;
        while (true) {
            if (copyToCacheFile != null) {
                try {
                    fromFile = Typeface.createFromFile(copyToCacheFile.getPath());
                    copyToCacheFile.delete();
                    if (fromFile == null) {
                        return null;
                    }
                }
                catch (RuntimeException ex) {
                    return null;
                }
                finally {
                    copyToCacheFile.delete();
                }
                final Typeface typeface2;
                return new TypefaceCompat.TypefaceHolder(typeface2, fontResult.getWeight(), fontResult.getItalic());
            }
            continue;
        }
    }
    
    @Override
    public TypefaceHolder createTypeface(@NonNull final FontsContractCompat.FontInfo[] array, Map<Uri, ByteBuffer> copyToCacheFile) {
        if (array.length >= 1) {
            final Typeface typeface = null;
            final FontsContractCompat.FontInfo fontInfo = array[0];
            copyToCacheFile = this.copyToCacheFile(((Map<Uri, ByteBuffer>)copyToCacheFile).get((Object)fontInfo.getUri()));
            Typeface fromFile = typeface;
            Label_0053: {
                if (copyToCacheFile == null) {
                    break Label_0053;
                }
                try {
                    fromFile = Typeface.createFromFile(copyToCacheFile.getPath());
                    copyToCacheFile.delete();
                    if (fromFile != null) {
                        return new TypefaceCompat.TypefaceHolder(fromFile, fontInfo.getWeight(), fontInfo.isItalic());
                    }
                }
                catch (RuntimeException ex) {
                    return null;
                }
                finally {
                    copyToCacheFile.delete();
                }
            }
        }
        return null;
    }
    
    @Override
    public TypefaceHolder findFromCache(final Resources resources, final int n, final int n2) {
        final String assetUid = createAssetUid(resources, n, n2);
        synchronized (TypefaceCompatBaseImpl.sDynamicTypefaceCache) {
            return TypefaceCompatBaseImpl.sDynamicTypefaceCache.get(assetUid);
        }
    }
    
    @VisibleForTesting
    void receiveResult(final FontRequest fontRequest, final FontRequestCallback fontRequestCallback, final int n, final Bundle bundle) {
        final TypefaceHolder fromCache = findFromCache(fontRequest.getProviderAuthority(), fontRequest.getQuery());
        if (fromCache != null) {
            fontRequestCallback.onTypefaceRetrieved(fromCache.getTypeface());
            return;
        }
        if (n != 0) {
            fontRequestCallback.onTypefaceRequestFailed(n);
            return;
        }
        if (bundle == null) {
            fontRequestCallback.onTypefaceRequestFailed(1);
            return;
        }
        final ArrayList parcelableArrayList = bundle.getParcelableArrayList("font_results");
        if (parcelableArrayList == null || parcelableArrayList.isEmpty()) {
            fontRequestCallback.onTypefaceRequestFailed(1);
            return;
        }
        final TypefaceHolder typeface = this.createTypeface(parcelableArrayList);
        if (typeface == null) {
            Log.e("TypefaceCompatBaseImpl", "Error creating font " + fontRequest.getQuery());
            fontRequestCallback.onTypefaceRequestFailed(-3);
            return;
        }
        putInCache(fontRequest.getProviderAuthority(), fontRequest.getQuery(), typeface);
        fontRequestCallback.onTypefaceRetrieved(typeface.getTypeface());
    }
    
    private static final class WaitableCallback extends FontRequestCallback
    {
        private static final int FINISHED = 2;
        private static final int NOT_STARTED = 0;
        private static final int WAITING = 1;
        private final Condition mCond;
        private final String mFontTitle;
        private final ReentrantLock mLock;
        @GuardedBy("mLock")
        private int mState;
        @GuardedBy("mLock")
        private Typeface mTypeface;
        
        WaitableCallback(final String mFontTitle) {
            this.mLock = new ReentrantLock();
            this.mCond = this.mLock.newCondition();
            this.mState = 0;
            this.mFontTitle = mFontTitle;
        }
        
        @Override
        public void onTypefaceRequestFailed(final int n) {
            Log.w("TypefaceCompatBaseImpl", "Remote font fetch failed(" + n + "): " + this.mFontTitle);
            this.mLock.lock();
            try {
                if (this.mState == 1) {
                    this.mTypeface = null;
                    this.mState = 2;
                }
                this.mCond.signal();
            }
            finally {
                this.mLock.unlock();
            }
        }
        
        @Override
        public void onTypefaceRetrieved(final Typeface mTypeface) {
            this.mLock.lock();
            try {
                if (this.mState == 1) {
                    this.mTypeface = mTypeface;
                    this.mState = 2;
                }
                this.mCond.signal();
            }
            finally {
                this.mLock.unlock();
            }
        }
        
        public Typeface waitWithTimeout(final long p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0        
            //     1: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mLock:Ljava/util/concurrent/locks/ReentrantLock;
            //     4: invokevirtual   java/util/concurrent/locks/ReentrantLock.lock:()V
            //     7: aload_0        
            //     8: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mState:I
            //    11: iconst_2       
            //    12: if_icmpne       31
            //    15: aload_0        
            //    16: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mTypeface:Landroid/graphics/Typeface;
            //    19: astore          8
            //    21: aload_0        
            //    22: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mLock:Ljava/util/concurrent/locks/ReentrantLock;
            //    25: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
            //    28: aload           8
            //    30: areturn        
            //    31: aload_0        
            //    32: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mState:I
            //    35: istore_3       
            //    36: iload_3        
            //    37: ifeq            49
            //    40: aload_0        
            //    41: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mLock:Ljava/util/concurrent/locks/ReentrantLock;
            //    44: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
            //    47: aconst_null    
            //    48: areturn        
            //    49: aload_0        
            //    50: iconst_1       
            //    51: putfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mState:I
            //    54: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
            //    57: lload_1        
            //    58: invokevirtual   java/util/concurrent/TimeUnit.toNanos:(J)J
            //    61: lstore          4
            //    63: aload_0        
            //    64: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mState:I
            //    67: istore_3       
            //    68: iload_3        
            //    69: iconst_1       
            //    70: if_icmpne       213
            //    73: aload_0        
            //    74: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mCond:Ljava/util/concurrent/locks/Condition;
            //    77: lload           4
            //    79: invokeinterface java/util/concurrent/locks/Condition.awaitNanos:(J)J
            //    84: lstore          6
            //    86: aload_0        
            //    87: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mState:I
            //    90: iconst_2       
            //    91: if_icmpne       160
            //    94: getstatic       java/util/concurrent/TimeUnit.NANOSECONDS:Ljava/util/concurrent/TimeUnit;
            //    97: lload           6
            //    99: invokevirtual   java/util/concurrent/TimeUnit.toMillis:(J)J
            //   102: lstore          4
            //   104: ldc             "TypefaceCompatBaseImpl"
            //   106: new             Ljava/lang/StringBuilder;
            //   109: dup            
            //   110: invokespecial   java/lang/StringBuilder.<init>:()V
            //   113: ldc             "Remote font fetched in "
            //   115: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   118: lload_1        
            //   119: lload           4
            //   121: lsub           
            //   122: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
            //   125: ldc             "ms :"
            //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   130: aload_0        
            //   131: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mFontTitle:Ljava/lang/String;
            //   134: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   137: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   140: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
            //   143: pop            
            //   144: aload_0        
            //   145: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mTypeface:Landroid/graphics/Typeface;
            //   148: astore          8
            //   150: aload_0        
            //   151: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mLock:Ljava/util/concurrent/locks/ReentrantLock;
            //   154: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
            //   157: aload           8
            //   159: areturn        
            //   160: lload           6
            //   162: lstore          4
            //   164: lload           6
            //   166: lconst_0       
            //   167: lcmp           
            //   168: ifge            63
            //   171: ldc             "TypefaceCompatBaseImpl"
            //   173: new             Ljava/lang/StringBuilder;
            //   176: dup            
            //   177: invokespecial   java/lang/StringBuilder.<init>:()V
            //   180: ldc             "Remote font fetch timed out: "
            //   182: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   185: aload_0        
            //   186: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mFontTitle:Ljava/lang/String;
            //   189: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   192: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   195: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
            //   198: pop            
            //   199: aload_0        
            //   200: iconst_2       
            //   201: putfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mState:I
            //   204: aload_0        
            //   205: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mLock:Ljava/util/concurrent/locks/ReentrantLock;
            //   208: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
            //   211: aconst_null    
            //   212: areturn        
            //   213: aload_0        
            //   214: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mLock:Ljava/util/concurrent/locks/ReentrantLock;
            //   217: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
            //   220: aconst_null    
            //   221: areturn        
            //   222: astore          8
            //   224: aload_0        
            //   225: getfield        android/support/v4/graphics/TypefaceCompatBaseImpl$WaitableCallback.mLock:Ljava/util/concurrent/locks/ReentrantLock;
            //   228: invokevirtual   java/util/concurrent/locks/ReentrantLock.unlock:()V
            //   231: aload           8
            //   233: athrow         
            //   234: astore          8
            //   236: lload           4
            //   238: lstore          6
            //   240: goto            86
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                            
            //  -----  -----  -----  -----  --------------------------------
            //  7      21     222    234    Any
            //  31     36     222    234    Any
            //  49     63     222    234    Any
            //  63     68     222    234    Any
            //  73     86     234    243    Ljava/lang/InterruptedException;
            //  73     86     222    234    Any
            //  86     150    222    234    Any
            //  171    204    222    234    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0086:
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
