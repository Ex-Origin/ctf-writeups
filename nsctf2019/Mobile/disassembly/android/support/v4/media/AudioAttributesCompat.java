// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.media.AudioAttributes$Builder;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import android.support.annotation.Nullable;
import android.media.AudioAttributes;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.util.SparseIntArray;

public class AudioAttributesCompat
{
    public static final int CONTENT_TYPE_MOVIE = 3;
    public static final int CONTENT_TYPE_MUSIC = 2;
    public static final int CONTENT_TYPE_SONIFICATION = 4;
    public static final int CONTENT_TYPE_SPEECH = 1;
    public static final int CONTENT_TYPE_UNKNOWN = 0;
    private static final int FLAG_ALL = 1023;
    private static final int FLAG_ALL_PUBLIC = 273;
    public static final int FLAG_AUDIBILITY_ENFORCED = 1;
    private static final int FLAG_BEACON = 8;
    private static final int FLAG_BYPASS_INTERRUPTION_POLICY = 64;
    private static final int FLAG_BYPASS_MUTE = 128;
    private static final int FLAG_DEEP_BUFFER = 512;
    public static final int FLAG_HW_AV_SYNC = 16;
    private static final int FLAG_HW_HOTWORD = 32;
    private static final int FLAG_LOW_LATENCY = 256;
    private static final int FLAG_SCO = 4;
    private static final int FLAG_SECURE = 2;
    private static final int[] SDK_USAGES;
    private static final int SUPPRESSIBLE_CALL = 2;
    private static final int SUPPRESSIBLE_NOTIFICATION = 1;
    private static final SparseIntArray SUPPRESSIBLE_USAGES;
    private static final String TAG = "AudioAttributesCompat";
    public static final int USAGE_ALARM = 4;
    public static final int USAGE_ASSISTANCE_ACCESSIBILITY = 11;
    public static final int USAGE_ASSISTANCE_NAVIGATION_GUIDANCE = 12;
    public static final int USAGE_ASSISTANCE_SONIFICATION = 13;
    public static final int USAGE_ASSISTANT = 16;
    public static final int USAGE_GAME = 14;
    public static final int USAGE_MEDIA = 1;
    public static final int USAGE_NOTIFICATION = 5;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_DELAYED = 9;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_INSTANT = 8;
    public static final int USAGE_NOTIFICATION_COMMUNICATION_REQUEST = 7;
    public static final int USAGE_NOTIFICATION_EVENT = 10;
    public static final int USAGE_NOTIFICATION_RINGTONE = 6;
    public static final int USAGE_UNKNOWN = 0;
    private static final int USAGE_VIRTUAL_SOURCE = 15;
    public static final int USAGE_VOICE_COMMUNICATION = 2;
    public static final int USAGE_VOICE_COMMUNICATION_SIGNALLING = 3;
    private static boolean sForceLegacyBehavior;
    private AudioAttributesCompatApi21.Wrapper mAudioAttributesWrapper;
    int mContentType;
    int mFlags;
    Integer mLegacyStream;
    int mUsage;
    
    static {
        (SUPPRESSIBLE_USAGES = new SparseIntArray()).put(5, 1);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(6, 2);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(7, 2);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(8, 1);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(9, 1);
        AudioAttributesCompat.SUPPRESSIBLE_USAGES.put(10, 1);
        SDK_USAGES = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16 };
    }
    
    private AudioAttributesCompat() {
        this.mUsage = 0;
        this.mContentType = 0;
        this.mFlags = 0;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static void setForceLegacyBehavior(final boolean sForceLegacyBehavior) {
        AudioAttributesCompat.sForceLegacyBehavior = sForceLegacyBehavior;
    }
    
    static int toVolumeStreamType(final boolean b, int n, final int n2) {
        final int n3 = 0;
        if ((n & 0x1) == 0x1) {
            if (!b) {
                return 7;
            }
        }
        else if ((n & 0x4) == 0x4) {
            if (b) {
                return 0;
            }
            return 6;
        }
        else {
            switch (n2) {
                case 13: {
                    break;
                }
                default: {
                    if (b) {
                        throw new IllegalArgumentException("Unknown usage value " + n2 + " in audio attributes");
                    }
                    return 3;
                }
                case 1:
                case 12:
                case 14:
                case 16: {
                    return 3;
                }
                case 2: {
                    return 0;
                }
                case 3: {
                    if (b) {
                        n = n3;
                    }
                    else {
                        n = 8;
                    }
                    return n;
                }
                case 4: {
                    return 4;
                }
                case 6: {
                    return 2;
                }
                case 5:
                case 7:
                case 8:
                case 9:
                case 10: {
                    return 5;
                }
                case 11: {
                    return 10;
                }
                case 0: {
                    if (b) {
                        return Integer.MIN_VALUE;
                    }
                    return 3;
                }
            }
        }
        return 1;
    }
    
    static int toVolumeStreamType(final boolean b, final AudioAttributesCompat audioAttributesCompat) {
        return toVolumeStreamType(b, audioAttributesCompat.getFlags(), audioAttributesCompat.getUsage());
    }
    
    private static int usageForStreamType(final int n) {
        int n2 = 2;
        switch (n) {
            default: {
                n2 = 0;
                return n2;
            }
            case 0:
            case 6: {
                return n2;
            }
            case 1:
            case 7: {
                return 13;
            }
            case 2: {
                return 6;
            }
            case 3: {
                return 1;
            }
            case 4: {
                return 4;
            }
            case 5: {
                return 5;
            }
            case 8: {
                return 3;
            }
            case 10: {
                return 11;
            }
        }
    }
    
    static String usageToString(final int n) {
        switch (n) {
            default: {
                return new String("unknown usage " + n);
            }
            case 0: {
                return new String("USAGE_UNKNOWN");
            }
            case 1: {
                return new String("USAGE_MEDIA");
            }
            case 2: {
                return new String("USAGE_VOICE_COMMUNICATION");
            }
            case 3: {
                return new String("USAGE_VOICE_COMMUNICATION_SIGNALLING");
            }
            case 4: {
                return new String("USAGE_ALARM");
            }
            case 5: {
                return new String("USAGE_NOTIFICATION");
            }
            case 6: {
                return new String("USAGE_NOTIFICATION_RINGTONE");
            }
            case 7: {
                return new String("USAGE_NOTIFICATION_COMMUNICATION_REQUEST");
            }
            case 8: {
                return new String("USAGE_NOTIFICATION_COMMUNICATION_INSTANT");
            }
            case 9: {
                return new String("USAGE_NOTIFICATION_COMMUNICATION_DELAYED");
            }
            case 10: {
                return new String("USAGE_NOTIFICATION_EVENT");
            }
            case 11: {
                return new String("USAGE_ASSISTANCE_ACCESSIBILITY");
            }
            case 12: {
                return new String("USAGE_ASSISTANCE_NAVIGATION_GUIDANCE");
            }
            case 13: {
                return new String("USAGE_ASSISTANCE_SONIFICATION");
            }
            case 14: {
                return new String("USAGE_GAME");
            }
            case 16: {
                return new String("USAGE_ASSISTANT");
            }
        }
    }
    
    @Nullable
    public static AudioAttributesCompat wrap(@NonNull final Object o) {
        if (Build$VERSION.SDK_INT >= 21 && !AudioAttributesCompat.sForceLegacyBehavior) {
            final AudioAttributesCompat audioAttributesCompat = new AudioAttributesCompat();
            audioAttributesCompat.mAudioAttributesWrapper = AudioAttributesCompatApi21.Wrapper.wrap((AudioAttributes)o);
            return audioAttributesCompat;
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final AudioAttributesCompat audioAttributesCompat = (AudioAttributesCompat)o;
            if (Build$VERSION.SDK_INT >= 21 && !AudioAttributesCompat.sForceLegacyBehavior && this.mAudioAttributesWrapper != null) {
                return this.mAudioAttributesWrapper.unwrap().equals(audioAttributesCompat.unwrap());
            }
            if (this.mContentType != audioAttributesCompat.getContentType() || this.mFlags != audioAttributesCompat.getFlags() || this.mUsage != audioAttributesCompat.getUsage() || this.mLegacyStream != audioAttributesCompat.mLegacyStream) {
                return false;
            }
        }
        return true;
    }
    
    public int getContentType() {
        if (Build$VERSION.SDK_INT >= 21 && !AudioAttributesCompat.sForceLegacyBehavior && this.mAudioAttributesWrapper != null) {
            return this.mAudioAttributesWrapper.unwrap().getContentType();
        }
        return this.mContentType;
    }
    
    public int getFlags() {
        if (Build$VERSION.SDK_INT >= 21 && !AudioAttributesCompat.sForceLegacyBehavior && this.mAudioAttributesWrapper != null) {
            return this.mAudioAttributesWrapper.unwrap().getFlags();
        }
        final int mFlags = this.mFlags;
        final int legacyStreamType = this.getLegacyStreamType();
        int n;
        if (legacyStreamType == 6) {
            n = (mFlags | 0x4);
        }
        else {
            n = mFlags;
            if (legacyStreamType == 7) {
                n = (mFlags | 0x1);
            }
        }
        return n & 0x111;
    }
    
    public int getLegacyStreamType() {
        if (this.mLegacyStream != null) {
            return this.mLegacyStream;
        }
        if (Build$VERSION.SDK_INT >= 21 && !AudioAttributesCompat.sForceLegacyBehavior) {
            return AudioAttributesCompatApi21.toLegacyStreamType(this.mAudioAttributesWrapper);
        }
        return toVolumeStreamType(false, this.mFlags, this.mUsage);
    }
    
    public int getUsage() {
        if (Build$VERSION.SDK_INT >= 21 && !AudioAttributesCompat.sForceLegacyBehavior && this.mAudioAttributesWrapper != null) {
            return this.mAudioAttributesWrapper.unwrap().getUsage();
        }
        return this.mUsage;
    }
    
    public int getVolumeControlStream() {
        if (this == null) {
            throw new IllegalArgumentException("Invalid null audio attributes");
        }
        if (Build$VERSION.SDK_INT >= 26 && !AudioAttributesCompat.sForceLegacyBehavior && this.unwrap() != null) {
            return ((AudioAttributes)this.unwrap()).getVolumeControlStream();
        }
        return toVolumeStreamType(true, this);
    }
    
    @Override
    public int hashCode() {
        if (Build$VERSION.SDK_INT >= 21 && !AudioAttributesCompat.sForceLegacyBehavior && this.mAudioAttributesWrapper != null) {
            return this.mAudioAttributesWrapper.unwrap().hashCode();
        }
        return Arrays.hashCode(new Object[] { this.mContentType, this.mFlags, this.mUsage, this.mLegacyStream });
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AudioAttributesCompat:");
        if (this.unwrap() != null) {
            sb.append(" audioattributes=").append(this.unwrap());
        }
        else {
            if (this.mLegacyStream != null) {
                sb.append(" stream=").append(this.mLegacyStream);
                sb.append(" derived");
            }
            sb.append(" usage=").append(this.usageToString()).append(" content=").append(this.mContentType).append(" flags=0x").append(Integer.toHexString(this.mFlags).toUpperCase());
        }
        return sb.toString();
    }
    
    @Nullable
    public Object unwrap() {
        if (this.mAudioAttributesWrapper != null) {
            return this.mAudioAttributesWrapper.unwrap();
        }
        return null;
    }
    
    String usageToString() {
        return usageToString(this.mUsage);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface AttributeContentType {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface AttributeUsage {
    }
    
    private abstract static class AudioManagerHidden
    {
        public static final int STREAM_ACCESSIBILITY = 10;
        public static final int STREAM_BLUETOOTH_SCO = 6;
        public static final int STREAM_SYSTEM_ENFORCED = 7;
        public static final int STREAM_TTS = 9;
    }
    
    public static class Builder
    {
        private Object mAAObject;
        private int mContentType;
        private int mFlags;
        private Integer mLegacyStream;
        private int mUsage;
        
        public Builder() {
            this.mUsage = 0;
            this.mContentType = 0;
            this.mFlags = 0;
        }
        
        public Builder(final AudioAttributesCompat audioAttributesCompat) {
            this.mUsage = 0;
            this.mContentType = 0;
            this.mFlags = 0;
            this.mUsage = audioAttributesCompat.mUsage;
            this.mContentType = audioAttributesCompat.mContentType;
            this.mFlags = audioAttributesCompat.mFlags;
            this.mLegacyStream = audioAttributesCompat.mLegacyStream;
            this.mAAObject = audioAttributesCompat.unwrap();
        }
        
        public AudioAttributesCompat build() {
            if (AudioAttributesCompat.sForceLegacyBehavior || Build$VERSION.SDK_INT < 21) {
                final AudioAttributesCompat audioAttributesCompat = new AudioAttributesCompat(null);
                audioAttributesCompat.mContentType = this.mContentType;
                audioAttributesCompat.mFlags = this.mFlags;
                audioAttributesCompat.mUsage = this.mUsage;
                audioAttributesCompat.mLegacyStream = this.mLegacyStream;
                audioAttributesCompat.mAudioAttributesWrapper = null;
                return audioAttributesCompat;
            }
            if (this.mAAObject != null) {
                return AudioAttributesCompat.wrap(this.mAAObject);
            }
            final AudioAttributes$Builder setUsage = new AudioAttributes$Builder().setContentType(this.mContentType).setFlags(this.mFlags).setUsage(this.mUsage);
            if (this.mLegacyStream != null) {
                setUsage.setLegacyStreamType((int)this.mLegacyStream);
            }
            return AudioAttributesCompat.wrap(setUsage.build());
        }
        
        public Builder setContentType(final int mContentType) {
            switch (mContentType) {
                default: {
                    this.mUsage = 0;
                    return this;
                }
                case 0:
                case 1:
                case 2:
                case 3:
                case 4: {
                    this.mContentType = mContentType;
                    return this;
                }
            }
        }
        
        public Builder setFlags(final int n) {
            this.mFlags |= (n & 0x3FF);
            return this;
        }
        
        public Builder setLegacyStreamType(final int n) {
            if (n == 10) {
                throw new IllegalArgumentException("STREAM_ACCESSIBILITY is not a legacy stream type that was used for audio playback");
            }
            this.mLegacyStream = n;
            this.mUsage = usageForStreamType(n);
            return this;
        }
        
        public Builder setUsage(final int n) {
            switch (n) {
                default: {
                    this.mUsage = 0;
                    return this;
                }
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15: {
                    this.mUsage = n;
                    return this;
                }
                case 16: {
                    if (!AudioAttributesCompat.sForceLegacyBehavior && Build$VERSION.SDK_INT > 25) {
                        this.mUsage = n;
                        return this;
                    }
                    this.mUsage = 12;
                    return this;
                }
            }
        }
    }
}
