// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import android.support.annotation.IntRange;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.os.LocaleList;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import java.util.Locale;
import android.os.Build$VERSION;

public final class LocaleListCompat
{
    static final LocaleListInterface IMPL;
    private static final LocaleListCompat sEmptyLocaleList;
    
    static {
        sEmptyLocaleList = new LocaleListCompat();
        if (Build$VERSION.SDK_INT >= 24) {
            IMPL = new LocaleListCompatApi24Impl();
            return;
        }
        IMPL = new LocaleListCompatBaseImpl();
    }
    
    public static LocaleListCompat create(@NonNull final Locale... localeListArray) {
        final LocaleListCompat localeListCompat = new LocaleListCompat();
        localeListCompat.setLocaleListArray(localeListArray);
        return localeListCompat;
    }
    
    @NonNull
    public static LocaleListCompat forLanguageTags(@Nullable final String s) {
        if (s == null || s.equals("")) {
            return getEmptyLocaleList();
        }
        final String[] split = s.split(",");
        final Locale[] localeListArray = new Locale[split.length];
        for (int i = 0; i < localeListArray.length; ++i) {
            Locale locale;
            if (Build$VERSION.SDK_INT >= 21) {
                locale = Locale.forLanguageTag(split[i]);
            }
            else {
                locale = LocaleHelper.forLanguageTag(split[i]);
            }
            localeListArray[i] = locale;
        }
        final LocaleListCompat localeListCompat = new LocaleListCompat();
        localeListCompat.setLocaleListArray(localeListArray);
        return localeListCompat;
    }
    
    @NonNull
    @Size(min = 1L)
    public static LocaleListCompat getAdjustedDefault() {
        if (Build$VERSION.SDK_INT >= 24) {
            return wrap(LocaleList.getAdjustedDefault());
        }
        return create(Locale.getDefault());
    }
    
    @NonNull
    @Size(min = 1L)
    public static LocaleListCompat getDefault() {
        if (Build$VERSION.SDK_INT >= 24) {
            return wrap(LocaleList.getDefault());
        }
        return create(Locale.getDefault());
    }
    
    @NonNull
    public static LocaleListCompat getEmptyLocaleList() {
        return LocaleListCompat.sEmptyLocaleList;
    }
    
    @RequiresApi(24)
    private void setLocaleList(final LocaleList list) {
        final int size = list.size();
        if (size > 0) {
            final Locale[] localeList = new Locale[size];
            for (int i = 0; i < size; ++i) {
                localeList[i] = list.get(i);
            }
            LocaleListCompat.IMPL.setLocaleList(localeList);
        }
    }
    
    private void setLocaleListArray(final Locale... localeList) {
        LocaleListCompat.IMPL.setLocaleList(localeList);
    }
    
    @RequiresApi(24)
    public static LocaleListCompat wrap(final Object o) {
        final LocaleListCompat localeListCompat = new LocaleListCompat();
        if (o instanceof LocaleList) {
            localeListCompat.setLocaleList((LocaleList)o);
        }
        return localeListCompat;
    }
    
    @Override
    public boolean equals(final Object o) {
        return LocaleListCompat.IMPL.equals(o);
    }
    
    public Locale get(final int n) {
        return LocaleListCompat.IMPL.get(n);
    }
    
    public Locale getFirstMatch(final String[] array) {
        return LocaleListCompat.IMPL.getFirstMatch(array);
    }
    
    @Override
    public int hashCode() {
        return LocaleListCompat.IMPL.hashCode();
    }
    
    @IntRange(from = -1L)
    public int indexOf(final Locale locale) {
        return LocaleListCompat.IMPL.indexOf(locale);
    }
    
    public boolean isEmpty() {
        return LocaleListCompat.IMPL.isEmpty();
    }
    
    @IntRange(from = 0L)
    public int size() {
        return LocaleListCompat.IMPL.size();
    }
    
    @NonNull
    public String toLanguageTags() {
        return LocaleListCompat.IMPL.toLanguageTags();
    }
    
    @Override
    public String toString() {
        return LocaleListCompat.IMPL.toString();
    }
    
    @Nullable
    public Object unwrap() {
        return LocaleListCompat.IMPL.getLocaleList();
    }
    
    @RequiresApi(24)
    static class LocaleListCompatApi24Impl implements LocaleListInterface
    {
        private LocaleList mLocaleList;
        
        LocaleListCompatApi24Impl() {
            this.mLocaleList = new LocaleList(new Locale[0]);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this.mLocaleList.equals(((LocaleListCompat)o).unwrap());
        }
        
        @Override
        public Locale get(final int n) {
            return this.mLocaleList.get(n);
        }
        
        @Nullable
        @Override
        public Locale getFirstMatch(final String[] array) {
            if (this.mLocaleList != null) {
                return this.mLocaleList.getFirstMatch(array);
            }
            return null;
        }
        
        @Override
        public Object getLocaleList() {
            return this.mLocaleList;
        }
        
        @Override
        public int hashCode() {
            return this.mLocaleList.hashCode();
        }
        
        @IntRange(from = -1L)
        @Override
        public int indexOf(final Locale locale) {
            return this.mLocaleList.indexOf(locale);
        }
        
        @Override
        public boolean isEmpty() {
            return this.mLocaleList.isEmpty();
        }
        
        @Override
        public void setLocaleList(@NonNull final Locale... array) {
            this.mLocaleList = new LocaleList(array);
        }
        
        @IntRange(from = 0L)
        @Override
        public int size() {
            return this.mLocaleList.size();
        }
        
        @Override
        public String toLanguageTags() {
            return this.mLocaleList.toLanguageTags();
        }
        
        @Override
        public String toString() {
            return this.mLocaleList.toString();
        }
    }
    
    static class LocaleListCompatBaseImpl implements LocaleListInterface
    {
        private LocaleListHelper mLocaleList;
        
        LocaleListCompatBaseImpl() {
            this.mLocaleList = new LocaleListHelper(new Locale[0]);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this.mLocaleList.equals(((LocaleListCompat)o).unwrap());
        }
        
        @Override
        public Locale get(final int n) {
            return this.mLocaleList.get(n);
        }
        
        @Nullable
        @Override
        public Locale getFirstMatch(final String[] array) {
            if (this.mLocaleList != null) {
                return this.mLocaleList.getFirstMatch(array);
            }
            return null;
        }
        
        @Override
        public Object getLocaleList() {
            return this.mLocaleList;
        }
        
        @Override
        public int hashCode() {
            return this.mLocaleList.hashCode();
        }
        
        @IntRange(from = -1L)
        @Override
        public int indexOf(final Locale locale) {
            return this.mLocaleList.indexOf(locale);
        }
        
        @Override
        public boolean isEmpty() {
            return this.mLocaleList.isEmpty();
        }
        
        @Override
        public void setLocaleList(@NonNull final Locale... array) {
            this.mLocaleList = new LocaleListHelper(array);
        }
        
        @IntRange(from = 0L)
        @Override
        public int size() {
            return this.mLocaleList.size();
        }
        
        @Override
        public String toLanguageTags() {
            return this.mLocaleList.toLanguageTags();
        }
        
        @Override
        public String toString() {
            return this.mLocaleList.toString();
        }
    }
}
