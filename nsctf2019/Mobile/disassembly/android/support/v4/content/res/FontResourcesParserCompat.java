// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content.res;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.support.compat.R;
import android.util.Xml;
import android.content.res.TypedArray;
import java.util.ArrayList;
import java.util.List;
import android.support.annotation.ArrayRes;
import android.support.annotation.Nullable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.content.res.Resources;
import org.xmlpull.v1.XmlPullParser;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class FontResourcesParserCompat
{
    private static final int ITALIC = 1;
    private static final int NORMAL_WEIGHT = 400;
    
    @Nullable
    public static FamilyResourceEntry parse(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        int next;
        do {
            next = xmlPullParser.next();
        } while (next != 2 && next != 1);
        if (next != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        return readFamilies(xmlPullParser, resources);
    }
    
    public static List<List<byte[]>> readCerts(final Resources resources, @ArrayRes int n) {
        List<List<byte[]>> list = null;
        if (n != 0) {
            final TypedArray obtainTypedArray = resources.obtainTypedArray(n);
            list = list;
            if (obtainTypedArray.length() > 0) {
                final ArrayList<List<byte[]>> list2 = new ArrayList<List<byte[]>>();
                int n2;
                if (obtainTypedArray.getResourceId(0, 0) != 0) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                if (n2 != 0) {
                    n = 0;
                    while (true) {
                        list = list2;
                        if (n >= obtainTypedArray.length()) {
                            break;
                        }
                        list2.add(toByteArrayList(resources.getStringArray(obtainTypedArray.getResourceId(n, 0))));
                        ++n;
                    }
                }
                else {
                    list2.add(toByteArrayList(resources.getStringArray(n)));
                    list = list2;
                }
            }
        }
        return list;
    }
    
    @Nullable
    private static FamilyResourceEntry readFamilies(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, (String)null, "font-family");
        if (xmlPullParser.getName().equals("font-family")) {
            return readFamily(xmlPullParser, resources);
        }
        skip(xmlPullParser);
        return null;
    }
    
    @Nullable
    private static FamilyResourceEntry readFamily(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), R.styleable.FontFamily);
        final String string = obtainAttributes.getString(R.styleable.FontFamily_fontProviderAuthority);
        final String string2 = obtainAttributes.getString(R.styleable.FontFamily_fontProviderPackage);
        final String string3 = obtainAttributes.getString(R.styleable.FontFamily_fontProviderQuery);
        final int resourceId = obtainAttributes.getResourceId(R.styleable.FontFamily_fontProviderCerts, 0);
        obtainAttributes.recycle();
        if (string != null && string2 != null && string3 != null) {
            while (xmlPullParser.next() != 3) {
                skip(xmlPullParser);
            }
            return (FamilyResourceEntry)new ProviderResourceEntry(string, string2, string3, readCerts(resources, resourceId));
        }
        final ArrayList<FontFileResourceEntry> list = new ArrayList<FontFileResourceEntry>();
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equals("font")) {
                    list.add(readFont(xmlPullParser, resources));
                }
                else {
                    skip(xmlPullParser);
                }
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return (FamilyResourceEntry)new FontFamilyFilesResourceEntry((FontFileResourceEntry[])list.toArray(new FontFileResourceEntry[list.size()]));
    }
    
    private static FontFileResourceEntry readFont(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        boolean b = true;
        final TypedArray obtainAttributes = resources.obtainAttributes(Xml.asAttributeSet(xmlPullParser), R.styleable.FontFamilyFont);
        final int int1 = obtainAttributes.getInt(R.styleable.FontFamilyFont_fontWeight, 400);
        if (1 != obtainAttributes.getInt(R.styleable.FontFamilyFont_fontStyle, 0)) {
            b = false;
        }
        final String string = obtainAttributes.getString(R.styleable.FontFamilyFont_font);
        final int resourceId = obtainAttributes.getResourceId(R.styleable.FontFamilyFont_font, 0);
        obtainAttributes.recycle();
        while (xmlPullParser.next() != 3) {
            skip(xmlPullParser);
        }
        return new FontFileResourceEntry(string, int1, b, resourceId);
    }
    
    private static void skip(final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        int i = 1;
        while (i > 0) {
            switch (xmlPullParser.next()) {
                default: {
                    continue;
                }
                case 2: {
                    ++i;
                    continue;
                }
                case 3: {
                    --i;
                    continue;
                }
            }
        }
    }
    
    private static List<byte[]> toByteArrayList(final String[] array) {
        final ArrayList<byte[]> list = new ArrayList<byte[]>();
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(Base64.decode(array[i], 0));
        }
        return list;
    }
    
    public interface FamilyResourceEntry
    {
    }
    
    public static final class FontFamilyFilesResourceEntry implements FamilyResourceEntry
    {
        @NonNull
        private final FontFileResourceEntry[] mEntries;
        
        public FontFamilyFilesResourceEntry(@NonNull final FontFileResourceEntry[] mEntries) {
            this.mEntries = mEntries;
        }
        
        @NonNull
        public FontFileResourceEntry[] getEntries() {
            return this.mEntries;
        }
    }
    
    public static final class FontFileResourceEntry
    {
        @NonNull
        private final String mFileName;
        private boolean mItalic;
        private int mResourceId;
        private int mWeight;
        
        public FontFileResourceEntry(@NonNull final String mFileName, final int mWeight, final boolean mItalic, final int mResourceId) {
            this.mFileName = mFileName;
            this.mWeight = mWeight;
            this.mItalic = mItalic;
            this.mResourceId = mResourceId;
        }
        
        @NonNull
        public String getFileName() {
            return this.mFileName;
        }
        
        public int getResourceId() {
            return this.mResourceId;
        }
        
        public int getWeight() {
            return this.mWeight;
        }
        
        public boolean isItalic() {
            return this.mItalic;
        }
    }
    
    public static final class ProviderResourceEntry implements FamilyResourceEntry
    {
        @Nullable
        private final List<List<byte[]>> mCerts;
        @NonNull
        private final String mProviderAuthority;
        @NonNull
        private final String mProviderPackage;
        @NonNull
        private final String mQuery;
        
        public ProviderResourceEntry(@NonNull final String mProviderAuthority, @NonNull final String mProviderPackage, @NonNull final String mQuery, @Nullable final List<List<byte[]>> mCerts) {
            this.mProviderAuthority = mProviderAuthority;
            this.mProviderPackage = mProviderPackage;
            this.mQuery = mQuery;
            this.mCerts = mCerts;
        }
        
        @NonNull
        public String getAuthority() {
            return this.mProviderAuthority;
        }
        
        @Nullable
        public List<List<byte[]>> getCerts() {
            return this.mCerts;
        }
        
        @NonNull
        public String getPackage() {
            return this.mProviderPackage;
        }
        
        @NonNull
        public String getQuery() {
            return this.mQuery;
        }
    }
}
