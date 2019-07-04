// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.print;

import android.os.CancellationSignal$OnCancelListener;
import android.print.PrintDocumentInfo;
import android.print.PrintDocumentInfo$Builder;
import android.os.Bundle;
import android.print.PrintDocumentAdapter$LayoutResultCallback;
import android.print.PrintDocumentAdapter;
import android.print.PrintAttributes$MediaSize;
import android.print.PrintManager;
import android.print.PrintAttributes$Builder;
import android.print.PageRange;
import android.os.AsyncTask;
import android.print.PrintAttributes$Margins;
import java.io.InputStream;
import java.io.IOException;
import android.util.Log;
import android.graphics.Rect;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import java.io.FileNotFoundException;
import android.net.Uri;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.print.PrintDocumentAdapter$WriteResultCallback;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.graphics.Bitmap;
import android.print.PrintAttributes;
import android.graphics.BitmapFactory$Options;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class PrintHelperKitkat
{
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode;
    final Context mContext;
    BitmapFactory$Options mDecodeOptions;
    protected boolean mIsMinMarginsHandlingCorrect;
    private final Object mLock;
    int mOrientation;
    protected boolean mPrintActivityRespectsOrientation;
    int mScaleMode;
    
    PrintHelperKitkat(final Context mContext) {
        this.mDecodeOptions = null;
        this.mLock = new Object();
        this.mScaleMode = 2;
        this.mColorMode = 2;
        this.mPrintActivityRespectsOrientation = true;
        this.mIsMinMarginsHandlingCorrect = true;
        this.mContext = mContext;
    }
    
    private Bitmap convertBitmapForColorMode(final Bitmap bitmap, final int n) {
        if (n != 1) {
            return bitmap;
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint();
        final ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        canvas.setBitmap((Bitmap)null);
        return bitmap2;
    }
    
    private Matrix getMatrix(final int n, final int n2, final RectF rectF, final int n3) {
        final Matrix matrix = new Matrix();
        final float n4 = rectF.width() / n;
        float n5;
        if (n3 == 2) {
            n5 = Math.max(n4, rectF.height() / n2);
        }
        else {
            n5 = Math.min(n4, rectF.height() / n2);
        }
        matrix.postScale(n5, n5);
        matrix.postTranslate((rectF.width() - n * n5) / 2.0f, (rectF.height() - n2 * n5) / 2.0f);
        return matrix;
    }
    
    private static boolean isPortrait(final Bitmap bitmap) {
        return bitmap.getWidth() <= bitmap.getHeight();
    }
    
    private Bitmap loadBitmap(final Uri uri, BitmapFactory$Options decodeStream) throws FileNotFoundException {
        if (uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        InputStream openInputStream = null;
        try {
            final InputStream inputStream = openInputStream = this.mContext.getContentResolver().openInputStream(uri);
            decodeStream = (BitmapFactory$Options)BitmapFactory.decodeStream(inputStream, (Rect)null, decodeStream);
            if (inputStream == null) {
                return (Bitmap)decodeStream;
            }
            try {
                inputStream.close();
                return (Bitmap)decodeStream;
            }
            catch (IOException ex) {
                Log.w("PrintHelperKitkat", "close fail ", (Throwable)ex);
                return (Bitmap)decodeStream;
            }
        }
        finally {
            Label_0075: {
                if (openInputStream == null) {
                    break Label_0075;
                }
                try {
                    openInputStream.close();
                }
                catch (IOException ex2) {
                    Log.w("PrintHelperKitkat", "close fail ", (Throwable)ex2);
                }
            }
        }
    }
    
    private Bitmap loadConstrainedBitmap(Uri uri, final int n) throws FileNotFoundException {
        if (n <= 0 || uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        this.loadBitmap(uri, bitmapFactory$Options);
        final int outWidth = bitmapFactory$Options.outWidth;
        final int outHeight = bitmapFactory$Options.outHeight;
        if (outWidth > 0 && outHeight > 0) {
            int i;
            int inSampleSize;
            for (i = Math.max(outWidth, outHeight), inSampleSize = 1; i > n; i >>>= 1, inSampleSize <<= 1) {}
            if (inSampleSize > 0 && Math.min(outWidth, outHeight) / inSampleSize > 0) {
                final Object mLock = this.mLock;
                final BitmapFactory$Options mDecodeOptions;
                synchronized (mLock) {
                    this.mDecodeOptions = new BitmapFactory$Options();
                    this.mDecodeOptions.inMutable = true;
                    this.mDecodeOptions.inSampleSize = inSampleSize;
                    mDecodeOptions = this.mDecodeOptions;
                    // monitorexit(mLock)
                    final PrintHelperKitkat printHelperKitkat = this;
                    final Uri uri2 = uri;
                    final BitmapFactory$Options bitmapFactory$Options2 = mDecodeOptions;
                    final Bitmap bitmap = printHelperKitkat.loadBitmap(uri2, bitmapFactory$Options2);
                    final PrintHelperKitkat printHelperKitkat2 = this;
                    final Object o = printHelperKitkat2.mLock;
                    final Object o2;
                    uri = (Uri)(o2 = o);
                    synchronized (o2) {
                        final PrintHelperKitkat printHelperKitkat3 = this;
                        final BitmapFactory$Options bitmapFactory$Options3 = null;
                        printHelperKitkat3.mDecodeOptions = bitmapFactory$Options3;
                        return bitmap;
                    }
                }
                try {
                    final PrintHelperKitkat printHelperKitkat = this;
                    final Uri uri2 = uri;
                    final BitmapFactory$Options bitmapFactory$Options2 = mDecodeOptions;
                    final Bitmap bitmap = printHelperKitkat.loadBitmap(uri2, bitmapFactory$Options2);
                    final PrintHelperKitkat printHelperKitkat2 = this;
                    final Object o = printHelperKitkat2.mLock;
                    final Object o2;
                    uri = (Uri)(o2 = o);
                    // monitorenter(o2)
                    final PrintHelperKitkat printHelperKitkat3 = this;
                    final BitmapFactory$Options bitmapFactory$Options3 = null;
                    printHelperKitkat3.mDecodeOptions = bitmapFactory$Options3;
                    return bitmap;
                }
                finally {
                    synchronized (this.mLock) {
                        this.mDecodeOptions = null;
                    }
                    // monitorexit(this.mLock)
                }
            }
        }
        return null;
    }
    
    private void writeBitmap(final PrintAttributes printAttributes, final int n, final Bitmap bitmap, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
        PrintAttributes build;
        if (this.mIsMinMarginsHandlingCorrect) {
            build = printAttributes;
        }
        else {
            build = this.copyAttributes(printAttributes).setMinMargins(new PrintAttributes$Margins(0, 0, 0, 0)).build();
        }
        new AsyncTask<Void, Void, Throwable>() {
            protected Throwable doInBackground(final Void... p0) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     0: aload_0        
                //     1: getfield        android/support/v4/print/PrintHelperKitkat$2.val$cancellationSignal:Landroid/os/CancellationSignal;
                //     4: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                //     7: ifeq            12
                //    10: aconst_null    
                //    11: areturn        
                //    12: new             Landroid/print/pdf/PrintedPdfDocument;
                //    15: dup            
                //    16: aload_0        
                //    17: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //    20: getfield        android/support/v4/print/PrintHelperKitkat.mContext:Landroid/content/Context;
                //    23: aload_0        
                //    24: getfield        android/support/v4/print/PrintHelperKitkat$2.val$pdfAttributes:Landroid/print/PrintAttributes;
                //    27: invokespecial   android/print/pdf/PrintedPdfDocument.<init>:(Landroid/content/Context;Landroid/print/PrintAttributes;)V
                //    30: astore          4
                //    32: aload_0        
                //    33: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //    36: aload_0        
                //    37: getfield        android/support/v4/print/PrintHelperKitkat$2.val$bitmap:Landroid/graphics/Bitmap;
                //    40: aload_0        
                //    41: getfield        android/support/v4/print/PrintHelperKitkat$2.val$pdfAttributes:Landroid/print/PrintAttributes;
                //    44: invokevirtual   android/print/PrintAttributes.getColorMode:()I
                //    47: invokestatic    android/support/v4/print/PrintHelperKitkat.access$100:(Landroid/support/v4/print/PrintHelperKitkat;Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
                //    50: astore_3       
                //    51: aload_0        
                //    52: getfield        android/support/v4/print/PrintHelperKitkat$2.val$cancellationSignal:Landroid/os/CancellationSignal;
                //    55: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                //    58: istore_2       
                //    59: iload_2        
                //    60: ifne            389
                //    63: aload           4
                //    65: iconst_1       
                //    66: invokevirtual   android/print/pdf/PrintedPdfDocument.startPage:(I)Landroid/graphics/pdf/PdfDocument$Page;
                //    69: astore          5
                //    71: aload_0        
                //    72: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //    75: getfield        android/support/v4/print/PrintHelperKitkat.mIsMinMarginsHandlingCorrect:Z
                //    78: ifeq            198
                //    81: new             Landroid/graphics/RectF;
                //    84: dup            
                //    85: aload           5
                //    87: invokevirtual   android/graphics/pdf/PdfDocument$Page.getInfo:()Landroid/graphics/pdf/PdfDocument$PageInfo;
                //    90: invokevirtual   android/graphics/pdf/PdfDocument$PageInfo.getContentRect:()Landroid/graphics/Rect;
                //    93: invokespecial   android/graphics/RectF.<init>:(Landroid/graphics/Rect;)V
                //    96: astore_1       
                //    97: aload_0        
                //    98: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //   101: aload_3        
                //   102: invokevirtual   android/graphics/Bitmap.getWidth:()I
                //   105: aload_3        
                //   106: invokevirtual   android/graphics/Bitmap.getHeight:()I
                //   109: aload_1        
                //   110: aload_0        
                //   111: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fittingMode:I
                //   114: invokestatic    android/support/v4/print/PrintHelperKitkat.access$200:(Landroid/support/v4/print/PrintHelperKitkat;IILandroid/graphics/RectF;I)Landroid/graphics/Matrix;
                //   117: astore          6
                //   119: aload_0        
                //   120: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //   123: getfield        android/support/v4/print/PrintHelperKitkat.mIsMinMarginsHandlingCorrect:Z
                //   126: ifeq            295
                //   129: aload           5
                //   131: invokevirtual   android/graphics/pdf/PdfDocument$Page.getCanvas:()Landroid/graphics/Canvas;
                //   134: aload_3        
                //   135: aload           6
                //   137: aconst_null    
                //   138: invokevirtual   android/graphics/Canvas.drawBitmap:(Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V
                //   141: aload           4
                //   143: aload           5
                //   145: invokevirtual   android/print/pdf/PrintedPdfDocument.finishPage:(Landroid/graphics/pdf/PdfDocument$Page;)V
                //   148: aload_0        
                //   149: getfield        android/support/v4/print/PrintHelperKitkat$2.val$cancellationSignal:Landroid/os/CancellationSignal;
                //   152: invokevirtual   android/os/CancellationSignal.isCanceled:()Z
                //   155: istore_2       
                //   156: iload_2        
                //   157: ifeq            322
                //   160: aload           4
                //   162: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   165: aload_0        
                //   166: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   169: astore_1       
                //   170: aload_1        
                //   171: ifnull          181
                //   174: aload_0        
                //   175: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   178: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                //   181: aload_3        
                //   182: aload_0        
                //   183: getfield        android/support/v4/print/PrintHelperKitkat$2.val$bitmap:Landroid/graphics/Bitmap;
                //   186: if_acmpeq       389
                //   189: aload_3        
                //   190: invokevirtual   android/graphics/Bitmap.recycle:()V
                //   193: aconst_null    
                //   194: areturn        
                //   195: astore_1       
                //   196: aload_1        
                //   197: areturn        
                //   198: new             Landroid/print/pdf/PrintedPdfDocument;
                //   201: dup            
                //   202: aload_0        
                //   203: getfield        android/support/v4/print/PrintHelperKitkat$2.this$0:Landroid/support/v4/print/PrintHelperKitkat;
                //   206: getfield        android/support/v4/print/PrintHelperKitkat.mContext:Landroid/content/Context;
                //   209: aload_0        
                //   210: getfield        android/support/v4/print/PrintHelperKitkat$2.val$attributes:Landroid/print/PrintAttributes;
                //   213: invokespecial   android/print/pdf/PrintedPdfDocument.<init>:(Landroid/content/Context;Landroid/print/PrintAttributes;)V
                //   216: astore          6
                //   218: aload           6
                //   220: iconst_1       
                //   221: invokevirtual   android/print/pdf/PrintedPdfDocument.startPage:(I)Landroid/graphics/pdf/PdfDocument$Page;
                //   224: astore          7
                //   226: new             Landroid/graphics/RectF;
                //   229: dup            
                //   230: aload           7
                //   232: invokevirtual   android/graphics/pdf/PdfDocument$Page.getInfo:()Landroid/graphics/pdf/PdfDocument$PageInfo;
                //   235: invokevirtual   android/graphics/pdf/PdfDocument$PageInfo.getContentRect:()Landroid/graphics/Rect;
                //   238: invokespecial   android/graphics/RectF.<init>:(Landroid/graphics/Rect;)V
                //   241: astore_1       
                //   242: aload           6
                //   244: aload           7
                //   246: invokevirtual   android/print/pdf/PrintedPdfDocument.finishPage:(Landroid/graphics/pdf/PdfDocument$Page;)V
                //   249: aload           6
                //   251: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   254: goto            97
                //   257: astore_1       
                //   258: aload           4
                //   260: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   263: aload_0        
                //   264: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   267: astore          4
                //   269: aload           4
                //   271: ifnull          281
                //   274: aload_0        
                //   275: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   278: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                //   281: aload_3        
                //   282: aload_0        
                //   283: getfield        android/support/v4/print/PrintHelperKitkat$2.val$bitmap:Landroid/graphics/Bitmap;
                //   286: if_acmpeq       293
                //   289: aload_3        
                //   290: invokevirtual   android/graphics/Bitmap.recycle:()V
                //   293: aload_1        
                //   294: athrow         
                //   295: aload           6
                //   297: aload_1        
                //   298: getfield        android/graphics/RectF.left:F
                //   301: aload_1        
                //   302: getfield        android/graphics/RectF.top:F
                //   305: invokevirtual   android/graphics/Matrix.postTranslate:(FF)Z
                //   308: pop            
                //   309: aload           5
                //   311: invokevirtual   android/graphics/pdf/PdfDocument$Page.getCanvas:()Landroid/graphics/Canvas;
                //   314: aload_1        
                //   315: invokevirtual   android/graphics/Canvas.clipRect:(Landroid/graphics/RectF;)Z
                //   318: pop            
                //   319: goto            129
                //   322: aload           4
                //   324: new             Ljava/io/FileOutputStream;
                //   327: dup            
                //   328: aload_0        
                //   329: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   332: invokevirtual   android/os/ParcelFileDescriptor.getFileDescriptor:()Ljava/io/FileDescriptor;
                //   335: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/FileDescriptor;)V
                //   338: invokevirtual   android/print/pdf/PrintedPdfDocument.writeTo:(Ljava/io/OutputStream;)V
                //   341: aload           4
                //   343: invokevirtual   android/print/pdf/PrintedPdfDocument.close:()V
                //   346: aload_0        
                //   347: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   350: astore_1       
                //   351: aload_1        
                //   352: ifnull          362
                //   355: aload_0        
                //   356: getfield        android/support/v4/print/PrintHelperKitkat$2.val$fileDescriptor:Landroid/os/ParcelFileDescriptor;
                //   359: invokevirtual   android/os/ParcelFileDescriptor.close:()V
                //   362: aload_3        
                //   363: aload_0        
                //   364: getfield        android/support/v4/print/PrintHelperKitkat$2.val$bitmap:Landroid/graphics/Bitmap;
                //   367: if_acmpeq       389
                //   370: aload_3        
                //   371: invokevirtual   android/graphics/Bitmap.recycle:()V
                //   374: aconst_null    
                //   375: areturn        
                //   376: astore          4
                //   378: goto            281
                //   381: astore_1       
                //   382: goto            362
                //   385: astore_1       
                //   386: goto            181
                //   389: aconst_null    
                //   390: areturn        
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type                 
                //  -----  -----  -----  -----  ---------------------
                //  0      10     195    198    Ljava/lang/Throwable;
                //  12     59     195    198    Ljava/lang/Throwable;
                //  63     97     257    295    Any
                //  97     129    257    295    Any
                //  129    156    257    295    Any
                //  160    170    195    198    Ljava/lang/Throwable;
                //  174    181    385    389    Ljava/io/IOException;
                //  174    181    195    198    Ljava/lang/Throwable;
                //  181    193    195    198    Ljava/lang/Throwable;
                //  198    254    257    295    Any
                //  258    269    195    198    Ljava/lang/Throwable;
                //  274    281    376    381    Ljava/io/IOException;
                //  274    281    195    198    Ljava/lang/Throwable;
                //  281    293    195    198    Ljava/lang/Throwable;
                //  293    295    195    198    Ljava/lang/Throwable;
                //  295    319    257    295    Any
                //  322    341    257    295    Any
                //  341    351    195    198    Ljava/lang/Throwable;
                //  355    362    381    385    Ljava/io/IOException;
                //  355    362    195    198    Ljava/lang/Throwable;
                //  362    374    195    198    Ljava/lang/Throwable;
                // 
                // The error that occurred was:
                // 
                // java.lang.IndexOutOfBoundsException: Index: 188, Size: 188
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
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1163)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1010)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
            
            protected void onPostExecute(final Throwable t) {
                if (cancellationSignal.isCanceled()) {
                    printDocumentAdapter$WriteResultCallback.onWriteCancelled();
                    return;
                }
                if (t == null) {
                    printDocumentAdapter$WriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
                    return;
                }
                Log.e("PrintHelperKitkat", "Error writing printed content", t);
                printDocumentAdapter$WriteResultCallback.onWriteFailed((CharSequence)null);
            }
        }.execute((Object[])new Void[0]);
    }
    
    protected PrintAttributes$Builder copyAttributes(final PrintAttributes printAttributes) {
        final PrintAttributes$Builder setMinMargins = new PrintAttributes$Builder().setMediaSize(printAttributes.getMediaSize()).setResolution(printAttributes.getResolution()).setMinMargins(printAttributes.getMinMargins());
        if (printAttributes.getColorMode() != 0) {
            setMinMargins.setColorMode(printAttributes.getColorMode());
        }
        return setMinMargins;
    }
    
    public int getColorMode() {
        return this.mColorMode;
    }
    
    public int getOrientation() {
        if (this.mOrientation == 0) {
            return 1;
        }
        return this.mOrientation;
    }
    
    public int getScaleMode() {
        return this.mScaleMode;
    }
    
    public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        if (bitmap == null) {
            return;
        }
        final int mScaleMode = this.mScaleMode;
        final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        PrintAttributes$MediaSize mediaSize;
        if (isPortrait(bitmap)) {
            mediaSize = PrintAttributes$MediaSize.UNKNOWN_PORTRAIT;
        }
        else {
            mediaSize = PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE;
        }
        printManager.print(s, (PrintDocumentAdapter)new PrintDocumentAdapter() {
            private PrintAttributes mAttributes;
            
            public void onFinish() {
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
            }
            
            public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                boolean b = true;
                this.mAttributes = mAttributes;
                final PrintDocumentInfo build = new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build();
                if (mAttributes.equals((Object)printAttributes)) {
                    b = false;
                }
                printDocumentAdapter$LayoutResultCallback.onLayoutFinished(build, b);
            }
            
            public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                PrintHelperKitkat.this.writeBitmap(this.mAttributes, mScaleMode, bitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
            }
        }, new PrintAttributes$Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
    }
    
    public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        final PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
            private PrintAttributes mAttributes;
            Bitmap mBitmap = null;
            AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
            final /* synthetic */ int val$fittingMode = PrintHelperKitkat.this.mScaleMode;
            
            private void cancelLoad() {
                synchronized (PrintHelperKitkat.this.mLock) {
                    if (PrintHelperKitkat.this.mDecodeOptions != null) {
                        PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
                        PrintHelperKitkat.this.mDecodeOptions = null;
                    }
                }
            }
            
            public void onFinish() {
                super.onFinish();
                this.cancelLoad();
                if (this.mLoadBitmap != null) {
                    this.mLoadBitmap.cancel(true);
                }
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
                if (this.mBitmap != null) {
                    this.mBitmap.recycle();
                    this.mBitmap = null;
                }
            }
            
            public void onLayout(final PrintAttributes printAttributes, final PrintAttributes mAttributes, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$LayoutResultCallback printDocumentAdapter$LayoutResultCallback, final Bundle bundle) {
                boolean b = true;
                synchronized (this) {
                    this.mAttributes = mAttributes;
                    // monitorexit(this)
                    if (cancellationSignal.isCanceled()) {
                        printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                        return;
                    }
                }
                final PrintAttributes printAttributes2;
                if (this.mBitmap != null) {
                    final PrintDocumentInfo build = new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build();
                    if (mAttributes.equals((Object)printAttributes2)) {
                        b = false;
                    }
                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(build, b);
                    return;
                }
                this.mLoadBitmap = (AsyncTask<Uri, Boolean, Bitmap>)new AsyncTask<Uri, Boolean, Bitmap>() {
                    protected Bitmap doInBackground(final Uri... array) {
                        try {
                            return PrintHelperKitkat.this.loadConstrainedBitmap(uri, 3500);
                        }
                        catch (FileNotFoundException ex) {
                            return null;
                        }
                    }
                    
                    protected void onCancelled(final Bitmap bitmap) {
                        printDocumentAdapter$LayoutResultCallback.onLayoutCancelled();
                        PrintDocumentAdapter.this.mLoadBitmap = null;
                    }
                    
                    protected void onPostExecute(final Bitmap bitmap) {
                        super.onPostExecute((Object)bitmap);
                        Bitmap bitmap2 = bitmap;
                        Label_0108: {
                            if (bitmap == null) {
                                break Label_0108;
                            }
                            if (PrintHelperKitkat.this.mPrintActivityRespectsOrientation) {
                                bitmap2 = bitmap;
                                if (PrintHelperKitkat.this.mOrientation != 0) {
                                    break Label_0108;
                                }
                            }
                        Label_0171_Outer:
                            while (true) {
                                while (true) {
                                    Label_0190: {
                                        while (true) {
                                            synchronized (this) {
                                                final PrintAttributes$MediaSize mediaSize = PrintDocumentAdapter.this.mAttributes.getMediaSize();
                                                // monitorexit(this)
                                                bitmap2 = bitmap;
                                                if (mediaSize != null) {
                                                    bitmap2 = bitmap;
                                                    if (mediaSize.isPortrait() != isPortrait(bitmap)) {
                                                        final Matrix matrix = new Matrix();
                                                        matrix.postRotate(90.0f);
                                                        bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                                                    }
                                                }
                                                if ((PrintDocumentAdapter.this.mBitmap = bitmap2) == null) {
                                                    break Label_0190;
                                                }
                                                final PrintDocumentInfo build = new PrintDocumentInfo$Builder(s).setContentType(1).setPageCount(1).build();
                                                if (!mAttributes.equals((Object)printAttributes2)) {
                                                    final boolean b = true;
                                                    printDocumentAdapter$LayoutResultCallback.onLayoutFinished(build, b);
                                                    PrintDocumentAdapter.this.mLoadBitmap = null;
                                                    return;
                                                }
                                            }
                                            final boolean b = false;
                                            continue Label_0171_Outer;
                                        }
                                    }
                                    printDocumentAdapter$LayoutResultCallback.onLayoutFailed((CharSequence)null);
                                    continue;
                                }
                            }
                        }
                    }
                    
                    protected void onPreExecute() {
                        cancellationSignal.setOnCancelListener((CancellationSignal$OnCancelListener)new CancellationSignal$OnCancelListener() {
                            public void onCancel() {
                                PrintDocumentAdapter.this.cancelLoad();
                                AsyncTask.this.cancel(false);
                            }
                        });
                    }
                }.execute((Object[])new Uri[0]);
            }
            
            public void onWrite(final PageRange[] array, final ParcelFileDescriptor parcelFileDescriptor, final CancellationSignal cancellationSignal, final PrintDocumentAdapter$WriteResultCallback printDocumentAdapter$WriteResultCallback) {
                PrintHelperKitkat.this.writeBitmap(this.mAttributes, this.val$fittingMode, this.mBitmap, parcelFileDescriptor, cancellationSignal, printDocumentAdapter$WriteResultCallback);
            }
        };
        final PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
        final PrintAttributes$Builder printAttributes$Builder = new PrintAttributes$Builder();
        printAttributes$Builder.setColorMode(this.mColorMode);
        if (this.mOrientation == 1 || this.mOrientation == 0) {
            printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_LANDSCAPE);
        }
        else if (this.mOrientation == 2) {
            printAttributes$Builder.setMediaSize(PrintAttributes$MediaSize.UNKNOWN_PORTRAIT);
        }
        printManager.print(s, (PrintDocumentAdapter)printDocumentAdapter, printAttributes$Builder.build());
    }
    
    public void setColorMode(final int mColorMode) {
        this.mColorMode = mColorMode;
    }
    
    public void setOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
    }
    
    public void setScaleMode(final int mScaleMode) {
        this.mScaleMode = mScaleMode;
    }
    
    public interface OnPrintFinishCallback
    {
        void onFinish();
    }
}
