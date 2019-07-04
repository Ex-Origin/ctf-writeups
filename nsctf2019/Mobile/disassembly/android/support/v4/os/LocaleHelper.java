// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import java.util.Locale;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
final class LocaleHelper
{
    static Locale forLanguageTag(final String s) {
        if (s.contains("-")) {
            final String[] split = s.split("-");
            if (split.length > 2) {
                return new Locale(split[0], split[1], split[2]);
            }
            if (split.length > 1) {
                return new Locale(split[0], split[1]);
            }
            if (split.length == 1) {
                return new Locale(split[0]);
            }
        }
        else {
            if (!s.contains("_")) {
                return new Locale(s);
            }
            final String[] split2 = s.split("_");
            if (split2.length > 2) {
                return new Locale(split2[0], split2[1], split2[2]);
            }
            if (split2.length > 1) {
                return new Locale(split2[0], split2[1]);
            }
            if (split2.length == 1) {
                return new Locale(split2[0]);
            }
        }
        throw new IllegalArgumentException("Can not parse language tag: [" + s + "]");
    }
    
    static String toLanguageTag(final Locale locale) {
        final StringBuilder sb = new StringBuilder();
        sb.append(locale.getLanguage());
        final String country = locale.getCountry();
        if (country != null && !country.equals("")) {
            sb.append("-");
            sb.append(locale.getCountry());
        }
        return sb.toString();
    }
}
