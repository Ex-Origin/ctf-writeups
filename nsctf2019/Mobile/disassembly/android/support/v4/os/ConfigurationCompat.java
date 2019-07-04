// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import java.util.Locale;
import android.os.Build$VERSION;
import android.content.res.Configuration;

public final class ConfigurationCompat
{
    public static LocaleListCompat getLocales(final Configuration configuration) {
        if (Build$VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap(configuration.getLocales());
        }
        return LocaleListCompat.create(configuration.locale);
    }
}
