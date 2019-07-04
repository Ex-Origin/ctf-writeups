// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.content.res;

import android.support.v4.graphics.ColorUtils;
import android.graphics.Color;
import android.content.res.TypedArray;
import android.util.StateSet;
import android.support.v7.appcompat.R;
import java.io.IOException;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;
import android.content.res.ColorStateList;
import android.support.annotation.Nullable;
import android.content.res.Resources$Theme;
import org.xmlpull.v1.XmlPullParser;
import android.support.annotation.NonNull;
import android.content.res.Resources;

final class AppCompatColorStateListInflater
{
    private static final int DEFAULT_COLOR = -65536;
    
    @NonNull
    public static ColorStateList createFromXml(@NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        int next;
        do {
            next = xmlPullParser.next();
        } while (next != 2 && next != 1);
        if (next != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        return createFromXmlInner(resources, xmlPullParser, attributeSet, resources$Theme);
    }
    
    @NonNull
    private static ColorStateList createFromXmlInner(@NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final String name = xmlPullParser.getName();
        if (!name.equals("selector")) {
            throw new XmlPullParserException(xmlPullParser.getPositionDescription() + ": invalid color state list tag " + name);
        }
        return inflate(resources, xmlPullParser, set, resources$Theme);
    }
    
    private static ColorStateList inflate(@NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final int n = xmlPullParser.getDepth() + 1;
        int[][] array = new int[20][];
        int[] append = new int[array.length];
        int n2 = 0;
        while (true) {
            final int next = xmlPullParser.next();
            if (next == 1) {
                break;
            }
            final int depth = xmlPullParser.getDepth();
            if (depth < n && next == 3) {
                break;
            }
            if (next != 2 || depth > n || !xmlPullParser.getName().equals("item")) {
                continue;
            }
            final TypedArray obtainAttributes = obtainAttributes(resources, resources$Theme, set, R.styleable.ColorStateListItem);
            final int color = obtainAttributes.getColor(R.styleable.ColorStateListItem_android_color, -65281);
            float n3 = 1.0f;
            if (obtainAttributes.hasValue(R.styleable.ColorStateListItem_android_alpha)) {
                n3 = obtainAttributes.getFloat(R.styleable.ColorStateListItem_android_alpha, 1.0f);
            }
            else if (obtainAttributes.hasValue(R.styleable.ColorStateListItem_alpha)) {
                n3 = obtainAttributes.getFloat(R.styleable.ColorStateListItem_alpha, 1.0f);
            }
            obtainAttributes.recycle();
            final int attributeCount = set.getAttributeCount();
            final int[] array2 = new int[attributeCount];
            int i = 0;
            int n4 = 0;
            while (i < attributeCount) {
                int attributeNameResource = set.getAttributeNameResource(i);
                if (attributeNameResource != 16843173 && attributeNameResource != 16843551 && attributeNameResource != R.attr.alpha) {
                    final int n5 = n4 + 1;
                    if (!set.getAttributeBooleanValue(i, false)) {
                        attributeNameResource = -attributeNameResource;
                    }
                    array2[n4] = attributeNameResource;
                    n4 = n5;
                }
                ++i;
            }
            final int[] trimStateSet = StateSet.trimStateSet(array2, n4);
            final int modulateColorAlpha = modulateColorAlpha(color, n3);
            if (n2 == 0 || trimStateSet.length == 0) {}
            append = GrowingArrayUtils.append(append, n2, modulateColorAlpha);
            array = GrowingArrayUtils.append(array, n2, trimStateSet);
            ++n2;
        }
        final int[] array3 = new int[n2];
        final int[][] array4 = new int[n2][];
        System.arraycopy(append, 0, array3, 0, n2);
        System.arraycopy(array, 0, array4, 0, n2);
        return new ColorStateList(array4, array3);
    }
    
    private static int modulateColorAlpha(final int n, final float n2) {
        return ColorUtils.setAlphaComponent(n, Math.round(Color.alpha(n) * n2));
    }
    
    private static TypedArray obtainAttributes(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final int[] array) {
        if (resources$Theme == null) {
            return resources.obtainAttributes(set, array);
        }
        return resources$Theme.obtainStyledAttributes(set, array, 0, 0);
    }
}
