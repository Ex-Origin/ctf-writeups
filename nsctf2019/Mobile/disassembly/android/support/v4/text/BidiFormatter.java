// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter
{
    private static final int DEFAULT_FLAGS = 2;
    private static final BidiFormatter DEFAULT_LTR_INSTANCE;
    private static final BidiFormatter DEFAULT_RTL_INSTANCE;
    private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC;
    private static final int DIR_LTR = -1;
    private static final int DIR_RTL = 1;
    private static final int DIR_UNKNOWN = 0;
    private static final String EMPTY_STRING = "";
    private static final int FLAG_STEREO_RESET = 2;
    private static final char LRE = '\u202a';
    private static final char LRM = '\u200e';
    private static final String LRM_STRING;
    private static final char PDF = '\u202c';
    private static final char RLE = '\u202b';
    private static final char RLM = '\u200f';
    private static final String RLM_STRING;
    private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    private final int mFlags;
    private final boolean mIsRtlContext;
    
    static {
        BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        LRM_STRING = Character.toString('\u200e');
        RLM_STRING = Character.toString('\u200f');
        DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC);
        DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC);
    }
    
    private BidiFormatter(final boolean mIsRtlContext, final int mFlags, final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat) {
        this.mIsRtlContext = mIsRtlContext;
        this.mFlags = mFlags;
        this.mDefaultTextDirectionHeuristicCompat = mDefaultTextDirectionHeuristicCompat;
    }
    
    private static int getEntryDir(final CharSequence charSequence) {
        return new DirectionalityEstimator(charSequence, false).getEntryDir();
    }
    
    private static int getExitDir(final CharSequence charSequence) {
        return new DirectionalityEstimator(charSequence, false).getExitDir();
    }
    
    public static BidiFormatter getInstance() {
        return new Builder().build();
    }
    
    public static BidiFormatter getInstance(final Locale locale) {
        return new Builder(locale).build();
    }
    
    public static BidiFormatter getInstance(final boolean b) {
        return new Builder(b).build();
    }
    
    private static boolean isRtlLocale(final Locale locale) {
        return TextUtilsCompat.getLayoutDirectionFromLocale(locale) == 1;
    }
    
    private String markAfter(final CharSequence charSequence, final TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        final boolean rtl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        if (!this.mIsRtlContext && (rtl || getExitDir(charSequence) == 1)) {
            return BidiFormatter.LRM_STRING;
        }
        if (this.mIsRtlContext && (!rtl || getExitDir(charSequence) == -1)) {
            return BidiFormatter.RLM_STRING;
        }
        return "";
    }
    
    private String markBefore(final CharSequence charSequence, final TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        final boolean rtl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
        if (!this.mIsRtlContext && (rtl || getEntryDir(charSequence) == 1)) {
            return BidiFormatter.LRM_STRING;
        }
        if (this.mIsRtlContext && (!rtl || getEntryDir(charSequence) == -1)) {
            return BidiFormatter.RLM_STRING;
        }
        return "";
    }
    
    public boolean getStereoReset() {
        return (this.mFlags & 0x2) != 0x0;
    }
    
    public boolean isRtl(final CharSequence charSequence) {
        return this.mDefaultTextDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
    }
    
    public boolean isRtl(final String s) {
        return this.isRtl((CharSequence)s);
    }
    
    public boolean isRtlContext() {
        return this.mIsRtlContext;
    }
    
    public CharSequence unicodeWrap(final CharSequence charSequence) {
        return this.unicodeWrap(charSequence, this.mDefaultTextDirectionHeuristicCompat, true);
    }
    
    public CharSequence unicodeWrap(final CharSequence charSequence, final TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        return this.unicodeWrap(charSequence, textDirectionHeuristicCompat, true);
    }
    
    public CharSequence unicodeWrap(final CharSequence charSequence, TextDirectionHeuristicCompat textDirectionHeuristicCompat, final boolean b) {
        Object o;
        if (charSequence == null) {
            o = null;
        }
        else {
            final boolean rtl = textDirectionHeuristicCompat.isRtl(charSequence, 0, charSequence.length());
            final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            if (this.getStereoReset() && b) {
                if (rtl) {
                    textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
                }
                else {
                    textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
                }
                spannableStringBuilder.append((CharSequence)this.markBefore(charSequence, textDirectionHeuristicCompat));
            }
            if (rtl != this.mIsRtlContext) {
                char c;
                if (rtl) {
                    c = '\u202b';
                }
                else {
                    c = '\u202a';
                }
                spannableStringBuilder.append(c);
                spannableStringBuilder.append(charSequence);
                spannableStringBuilder.append('\u202c');
            }
            else {
                spannableStringBuilder.append(charSequence);
            }
            o = spannableStringBuilder;
            if (b) {
                if (rtl) {
                    textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
                }
                else {
                    textDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
                }
                spannableStringBuilder.append((CharSequence)this.markAfter(charSequence, textDirectionHeuristicCompat));
                return (CharSequence)spannableStringBuilder;
            }
        }
        return (CharSequence)o;
    }
    
    public CharSequence unicodeWrap(final CharSequence charSequence, final boolean b) {
        return this.unicodeWrap(charSequence, this.mDefaultTextDirectionHeuristicCompat, b);
    }
    
    public String unicodeWrap(final String s) {
        return this.unicodeWrap(s, this.mDefaultTextDirectionHeuristicCompat, true);
    }
    
    public String unicodeWrap(final String s, final TextDirectionHeuristicCompat textDirectionHeuristicCompat) {
        return this.unicodeWrap(s, textDirectionHeuristicCompat, true);
    }
    
    public String unicodeWrap(final String s, final TextDirectionHeuristicCompat textDirectionHeuristicCompat, final boolean b) {
        if (s == null) {
            return null;
        }
        return this.unicodeWrap((CharSequence)s, textDirectionHeuristicCompat, b).toString();
    }
    
    public String unicodeWrap(final String s, final boolean b) {
        return this.unicodeWrap(s, this.mDefaultTextDirectionHeuristicCompat, b);
    }
    
    public static final class Builder
    {
        private int mFlags;
        private boolean mIsRtlContext;
        private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;
        
        public Builder() {
            this.initialize(isRtlLocale(Locale.getDefault()));
        }
        
        public Builder(final Locale locale) {
            this.initialize(isRtlLocale(locale));
        }
        
        public Builder(final boolean b) {
            this.initialize(b);
        }
        
        private static BidiFormatter getDefaultInstanceFromContext(final boolean b) {
            if (b) {
                return BidiFormatter.DEFAULT_RTL_INSTANCE;
            }
            return BidiFormatter.DEFAULT_LTR_INSTANCE;
        }
        
        private void initialize(final boolean mIsRtlContext) {
            this.mIsRtlContext = mIsRtlContext;
            this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
            this.mFlags = 2;
        }
        
        public BidiFormatter build() {
            if (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) {
                return getDefaultInstanceFromContext(this.mIsRtlContext);
            }
            return new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat, null);
        }
        
        public Builder setTextDirectionHeuristic(final TextDirectionHeuristicCompat mTextDirectionHeuristicCompat) {
            this.mTextDirectionHeuristicCompat = mTextDirectionHeuristicCompat;
            return this;
        }
        
        public Builder stereoReset(final boolean b) {
            if (b) {
                this.mFlags |= 0x2;
                return this;
            }
            this.mFlags &= 0xFFFFFFFD;
            return this;
        }
    }
    
    private static class DirectionalityEstimator
    {
        private static final byte[] DIR_TYPE_CACHE;
        private static final int DIR_TYPE_CACHE_SIZE = 1792;
        private int charIndex;
        private final boolean isHtml;
        private char lastChar;
        private final int length;
        private final CharSequence text;
        
        static {
            DIR_TYPE_CACHE = new byte[1792];
            for (int i = 0; i < 1792; ++i) {
                DirectionalityEstimator.DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
            }
        }
        
        DirectionalityEstimator(final CharSequence text, final boolean isHtml) {
            this.text = text;
            this.isHtml = isHtml;
            this.length = text.length();
        }
        
        private static byte getCachedDirectionality(final char c) {
            if (c < '\u0700') {
                return DirectionalityEstimator.DIR_TYPE_CACHE[c];
            }
            return Character.getDirectionality(c);
        }
        
        private byte skipEntityBackward() {
            final int charIndex = this.charIndex;
            while (this.charIndex > 0) {
                final CharSequence text = this.text;
                final int charIndex2 = this.charIndex - 1;
                this.charIndex = charIndex2;
                this.lastChar = text.charAt(charIndex2);
                if (this.lastChar == '&') {
                    return 12;
                }
                if (this.lastChar == ';') {
                    break;
                }
            }
            this.charIndex = charIndex;
            this.lastChar = ';';
            return 13;
        }
        
        private byte skipEntityForward() {
            while (this.charIndex < this.length && (this.lastChar = this.text.charAt(this.charIndex++)) != ';') {}
            return 12;
        }
        
        private byte skipTagBackward() {
            final int charIndex = this.charIndex;
            while (this.charIndex > 0) {
                final CharSequence text = this.text;
                final int charIndex2 = this.charIndex - 1;
                this.charIndex = charIndex2;
                this.lastChar = text.charAt(charIndex2);
                if (this.lastChar == '<') {
                    return 12;
                }
                if (this.lastChar == '>') {
                    break;
                }
                if (this.lastChar != '\"' && this.lastChar != '\'') {
                    continue;
                }
                final char lastChar = this.lastChar;
                while (this.charIndex > 0) {
                    final CharSequence text2 = this.text;
                    final int charIndex3 = this.charIndex - 1;
                    this.charIndex = charIndex3;
                    if ((this.lastChar = text2.charAt(charIndex3)) != lastChar) {
                        continue;
                    }
                    break;
                }
            }
            this.charIndex = charIndex;
            this.lastChar = '>';
            return 13;
        }
        
        private byte skipTagForward() {
            final int charIndex = this.charIndex;
            while (this.charIndex < this.length) {
                this.lastChar = this.text.charAt(this.charIndex++);
                if (this.lastChar == '>') {
                    return 12;
                }
                if (this.lastChar != '\"' && this.lastChar != '\'') {
                    continue;
                }
                final char lastChar = this.lastChar;
                while (this.charIndex < this.length && (this.lastChar = this.text.charAt(this.charIndex++)) != lastChar) {}
            }
            this.charIndex = charIndex;
            this.lastChar = '<';
            return 13;
        }
        
        byte dirTypeBackward() {
            this.lastChar = this.text.charAt(this.charIndex - 1);
            byte b;
            if (Character.isLowSurrogate(this.lastChar)) {
                final int codePointBefore = Character.codePointBefore(this.text, this.charIndex);
                this.charIndex -= Character.charCount(codePointBefore);
                b = Character.getDirectionality(codePointBefore);
            }
            else {
                --this.charIndex;
                b = getCachedDirectionality(this.lastChar);
                if (this.isHtml) {
                    if (this.lastChar == '>') {
                        return this.skipTagBackward();
                    }
                    b = b;
                    if (this.lastChar == ';') {
                        return this.skipEntityBackward();
                    }
                }
            }
            return b;
        }
        
        byte dirTypeForward() {
            this.lastChar = this.text.charAt(this.charIndex);
            byte b;
            if (Character.isHighSurrogate(this.lastChar)) {
                final int codePoint = Character.codePointAt(this.text, this.charIndex);
                this.charIndex += Character.charCount(codePoint);
                b = Character.getDirectionality(codePoint);
            }
            else {
                ++this.charIndex;
                b = getCachedDirectionality(this.lastChar);
                if (this.isHtml) {
                    if (this.lastChar == '<') {
                        return this.skipTagForward();
                    }
                    b = b;
                    if (this.lastChar == '&') {
                        return this.skipEntityForward();
                    }
                }
            }
            return b;
        }
        
        int getEntryDir() {
            this.charIndex = 0;
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            while (this.charIndex < this.length && n3 == 0) {
                switch (this.dirTypeForward()) {
                    case 18: {
                        --n;
                        n2 = 0;
                        continue;
                    }
                    case 16:
                    case 17: {
                        ++n;
                        n2 = 1;
                        continue;
                    }
                    case 14:
                    case 15: {
                        ++n;
                        n2 = -1;
                    }
                    case 9: {
                        continue;
                    }
                    default: {
                        n3 = n;
                        continue;
                    }
                    case 0: {
                        if (n == 0) {
                            return -1;
                        }
                        n3 = n;
                        continue;
                    }
                    case 1:
                    case 2: {
                        if (n == 0) {
                            return 1;
                        }
                        n3 = n;
                        continue;
                    }
                }
            }
            if (n3 == 0) {
                return 0;
            }
            int n4;
            if ((n4 = n2) == 0) {
                while (this.charIndex > 0) {
                    switch (this.dirTypeBackward()) {
                        default: {
                            continue;
                        }
                        case 14:
                        case 15: {
                            if (n3 == n) {
                                return -1;
                            }
                            --n;
                            continue;
                        }
                        case 16:
                        case 17: {
                            if (n3 == n) {
                                return 1;
                            }
                            --n;
                            continue;
                        }
                        case 18: {
                            ++n;
                            continue;
                        }
                    }
                }
                return 0;
            }
            return n4;
        }
        
        int getExitDir() {
            this.charIndex = this.length;
            int n = 0;
            int n2 = 0;
            while (this.charIndex > 0) {
                switch (this.dirTypeBackward()) {
                    case 18: {
                        ++n;
                    }
                    case 9: {
                        continue;
                    }
                    default: {
                        if (n2 == 0) {
                            n2 = n;
                            continue;
                        }
                        continue;
                    }
                    case 0: {
                        if (n == 0) {
                            break;
                        }
                        if (n2 == 0) {
                            n2 = n;
                            continue;
                        }
                        continue;
                    }
                    case 14:
                    case 15: {
                        if (n2 != n) {
                            --n;
                            continue;
                        }
                        break;
                    }
                    case 1:
                    case 2: {
                        if (n == 0) {
                            return 1;
                        }
                        if (n2 == 0) {
                            n2 = n;
                            continue;
                        }
                        continue;
                    }
                    case 16:
                    case 17: {
                        if (n2 == n) {
                            return 1;
                        }
                        --n;
                        continue;
                    }
                }
                return -1;
            }
            return 0;
        }
    }
}
