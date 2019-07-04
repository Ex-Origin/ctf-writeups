// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics;

import android.graphics.Typeface;
import android.annotation.TargetApi;
import java.nio.ByteBuffer;
import android.net.Uri;
import java.util.Map;
import android.support.v4.provider.FontsContractCompat;
import android.support.annotation.NonNull;
import android.support.v4.graphics.fonts.FontResult;
import java.util.List;
import android.support.annotation.Nullable;
import android.content.res.Resources;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.content.Context;
import android.support.annotation.GuardedBy;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class TypefaceCompat
{
    private static final Object sLock;
    @GuardedBy("sLock")
    private static TypefaceCompatImpl sTypefaceCompatImpl;
    
    static {
        sLock = new Object();
    }
    
    public static TypefaceHolder createFromResourcesFamilyXml(final Context context, final FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry, final Resources resources, final int n, final int n2) {
        maybeInitImpl(context);
        return TypefaceCompat.sTypefaceCompatImpl.createFromResourcesFamilyXml(familyResourceEntry, resources, n, n2);
    }
    
    @Nullable
    public static TypefaceHolder createFromResourcesFontFile(final Context context, final Resources resources, final int n, final int n2) {
        maybeInitImpl(context);
        return TypefaceCompat.sTypefaceCompatImpl.createFromResourcesFontFile(resources, n, n2);
    }
    
    public static TypefaceHolder createTypeface(final Context context, @NonNull final List<FontResult> list) {
        maybeInitImpl(context);
        return TypefaceCompat.sTypefaceCompatImpl.createTypeface(list);
    }
    
    public static TypefaceHolder createTypeface(final Context context, @NonNull final FontsContractCompat.FontInfo[] array, final Map<Uri, ByteBuffer> map) {
        maybeInitImpl(context);
        return TypefaceCompat.sTypefaceCompatImpl.createTypeface(array, map);
    }
    
    public static TypefaceHolder findFromCache(final Resources resources, final int n, final int n2) {
        synchronized (TypefaceCompat.sLock) {
            if (TypefaceCompat.sTypefaceCompatImpl == null) {
                return null;
            }
            // monitorexit(TypefaceCompat.sLock)
            return TypefaceCompat.sTypefaceCompatImpl.findFromCache(resources, n, n2);
        }
    }
    
    @TargetApi(26)
    private static void maybeInitImpl(final Context context) {
        if (TypefaceCompat.sTypefaceCompatImpl == null) {
            synchronized (TypefaceCompat.sLock) {
                if (TypefaceCompat.sTypefaceCompatImpl == null) {
                    TypefaceCompat.sTypefaceCompatImpl = (TypefaceCompatImpl)new TypefaceCompatBaseImpl(context);
                }
            }
        }
    }
    
    public abstract static class FontRequestCallback
    {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
        
        public abstract void onTypefaceRequestFailed(final int p0);
        
        public abstract void onTypefaceRetrieved(final Typeface p0);
    }
    
    interface TypefaceCompatImpl
    {
        TypefaceHolder createFromResourcesFamilyXml(final FontResourcesParserCompat.FamilyResourceEntry p0, final Resources p1, final int p2, final int p3);
        
        TypefaceHolder createFromResourcesFontFile(final Resources p0, final int p1, final int p2);
        
        TypefaceHolder createTypeface(@NonNull final List<FontResult> p0);
        
        TypefaceHolder createTypeface(@NonNull final FontsContractCompat.FontInfo[] p0, final Map<Uri, ByteBuffer> p1);
        
        TypefaceHolder findFromCache(final Resources p0, final int p1, final int p2);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class TypefaceHolder
    {
        private final boolean mItalic;
        private final Typeface mTypeface;
        private final int mWeight;
        
        public TypefaceHolder(final Typeface mTypeface, final int mWeight, final boolean mItalic) {
            this.mTypeface = mTypeface;
            this.mWeight = mWeight;
            this.mItalic = mItalic;
        }
        
        public Typeface getTypeface() {
            return this.mTypeface;
        }
        
        public int getWeight() {
            return this.mWeight;
        }
        
        public boolean isItalic() {
            return this.mItalic;
        }
    }
}
