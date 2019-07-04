// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import java.util.Iterator;
import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import java.util.Set;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.graphics.Bitmap;
import android.os.Build$VERSION;
import android.os.Parcel;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public final class MediaMetadataCompat implements Parcelable
{
    public static final Parcelable$Creator<MediaMetadataCompat> CREATOR;
    static final ArrayMap<String, Integer> METADATA_KEYS_TYPE;
    public static final String METADATA_KEY_ADVERTISEMENT = "android.media.metadata.ADVERTISEMENT";
    public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
    public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
    public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
    public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
    public static final String METADATA_KEY_ART = "android.media.metadata.ART";
    public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
    public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
    public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
    public static final String METADATA_KEY_BT_FOLDER_TYPE = "android.media.metadata.BT_FOLDER_TYPE";
    public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
    public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
    public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
    public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
    public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
    public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
    public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
    public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
    public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
    public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
    public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
    public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
    public static final String METADATA_KEY_MEDIA_URI = "android.media.metadata.MEDIA_URI";
    public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
    public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
    public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
    public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
    public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
    public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
    public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
    static final int METADATA_TYPE_BITMAP = 2;
    static final int METADATA_TYPE_LONG = 0;
    static final int METADATA_TYPE_RATING = 3;
    static final int METADATA_TYPE_TEXT = 1;
    private static final String[] PREFERRED_BITMAP_ORDER;
    private static final String[] PREFERRED_DESCRIPTION_ORDER;
    private static final String[] PREFERRED_URI_ORDER;
    private static final String TAG = "MediaMetadata";
    final Bundle mBundle;
    private MediaDescriptionCompat mDescription;
    private Object mMetadataObj;
    
    static {
        (METADATA_KEYS_TYPE = new ArrayMap<String, Integer>()).put("android.media.metadata.TITLE", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.DURATION", 0);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.DATE", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", 0);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", 0);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", 0);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", 0);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.ART", 2);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", 2);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.USER_RATING", 3);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.RATING", 3);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", 2);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.BT_FOLDER_TYPE", 0);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_URI", 1);
        MediaMetadataCompat.METADATA_KEYS_TYPE.put("android.media.metadata.ADVERTISEMENT", 0);
        PREFERRED_DESCRIPTION_ORDER = new String[] { "android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER" };
        PREFERRED_BITMAP_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART" };
        PREFERRED_URI_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI" };
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<MediaMetadataCompat>() {
            public MediaMetadataCompat createFromParcel(final Parcel parcel) {
                return new MediaMetadataCompat(parcel);
            }
            
            public MediaMetadataCompat[] newArray(final int n) {
                return new MediaMetadataCompat[n];
            }
        };
    }
    
    MediaMetadataCompat(final Bundle bundle) {
        this.mBundle = new Bundle(bundle);
    }
    
    MediaMetadataCompat(final Parcel parcel) {
        this.mBundle = parcel.readBundle();
    }
    
    public static MediaMetadataCompat fromMediaMetadata(final Object mMetadataObj) {
        if (mMetadataObj != null && Build$VERSION.SDK_INT >= 21) {
            final Parcel obtain = Parcel.obtain();
            MediaMetadataCompatApi21.writeToParcel(mMetadataObj, obtain, 0);
            obtain.setDataPosition(0);
            final MediaMetadataCompat mediaMetadataCompat = (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(obtain);
            obtain.recycle();
            mediaMetadataCompat.mMetadataObj = mMetadataObj;
            return mediaMetadataCompat;
        }
        return null;
    }
    
    public boolean containsKey(final String s) {
        return this.mBundle.containsKey(s);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Bitmap getBitmap(final String s) {
        try {
            return (Bitmap)this.mBundle.getParcelable(s);
        }
        catch (Exception ex) {
            Log.w("MediaMetadata", "Failed to retrieve a key as Bitmap.", (Throwable)ex);
            return null;
        }
    }
    
    public Bundle getBundle() {
        return this.mBundle;
    }
    
    public MediaDescriptionCompat getDescription() {
        if (this.mDescription != null) {
            return this.mDescription;
        }
        final String string = this.getString("android.media.metadata.MEDIA_ID");
        final CharSequence[] array = new CharSequence[3];
        final Bitmap bitmap = null;
        final Uri uri = null;
        final CharSequence text = this.getText("android.media.metadata.DISPLAY_TITLE");
        if (!TextUtils.isEmpty(text)) {
            array[0] = text;
            array[1] = this.getText("android.media.metadata.DISPLAY_SUBTITLE");
            array[2] = this.getText("android.media.metadata.DISPLAY_DESCRIPTION");
        }
        else {
            int n3;
            for (int n = 0, n2 = 0; n < array.length && n2 < MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER.length; ++n2, n = n3) {
                final CharSequence text2 = this.getText(MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER[n2]);
                n3 = n;
                if (!TextUtils.isEmpty(text2)) {
                    array[n] = text2;
                    n3 = n + 1;
                }
            }
        }
        int n4 = 0;
        Bitmap bitmap2;
        while (true) {
            bitmap2 = bitmap;
            if (n4 >= MediaMetadataCompat.PREFERRED_BITMAP_ORDER.length) {
                break;
            }
            bitmap2 = this.getBitmap(MediaMetadataCompat.PREFERRED_BITMAP_ORDER[n4]);
            if (bitmap2 != null) {
                break;
            }
            ++n4;
        }
        int n5 = 0;
        Uri parse;
        while (true) {
            parse = uri;
            if (n5 >= MediaMetadataCompat.PREFERRED_URI_ORDER.length) {
                break;
            }
            final String string2 = this.getString(MediaMetadataCompat.PREFERRED_URI_ORDER[n5]);
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                parse = Uri.parse(string2);
                break;
            }
            ++n5;
        }
        Uri parse2 = null;
        final String string3 = this.getString("android.media.metadata.MEDIA_URI");
        if (!TextUtils.isEmpty((CharSequence)string3)) {
            parse2 = Uri.parse(string3);
        }
        final MediaDescriptionCompat.Builder builder = new MediaDescriptionCompat.Builder();
        builder.setMediaId(string);
        builder.setTitle(array[0]);
        builder.setSubtitle(array[1]);
        builder.setDescription(array[2]);
        builder.setIconBitmap(bitmap2);
        builder.setIconUri(parse);
        builder.setMediaUri(parse2);
        if (this.mBundle.containsKey("android.media.metadata.BT_FOLDER_TYPE")) {
            final Bundle extras = new Bundle();
            extras.putLong("android.media.extra.BT_FOLDER_TYPE", this.getLong("android.media.metadata.BT_FOLDER_TYPE"));
            builder.setExtras(extras);
        }
        return this.mDescription = builder.build();
    }
    
    public long getLong(final String s) {
        return this.mBundle.getLong(s, 0L);
    }
    
    public Object getMediaMetadata() {
        if (this.mMetadataObj == null && Build$VERSION.SDK_INT >= 21) {
            final Parcel obtain = Parcel.obtain();
            this.writeToParcel(obtain, 0);
            obtain.setDataPosition(0);
            this.mMetadataObj = MediaMetadataCompatApi21.createFromParcel(obtain);
            obtain.recycle();
        }
        return this.mMetadataObj;
    }
    
    public RatingCompat getRating(final String s) {
        try {
            if (Build$VERSION.SDK_INT >= 19) {
                return RatingCompat.fromRating(this.mBundle.getParcelable(s));
            }
            return (RatingCompat)this.mBundle.getParcelable(s);
        }
        catch (Exception ex) {
            Log.w("MediaMetadata", "Failed to retrieve a key as Rating.", (Throwable)ex);
            return null;
        }
    }
    
    public String getString(final String s) {
        final CharSequence charSequence = this.mBundle.getCharSequence(s);
        if (charSequence != null) {
            return charSequence.toString();
        }
        return null;
    }
    
    public CharSequence getText(final String s) {
        return this.mBundle.getCharSequence(s);
    }
    
    public Set<String> keySet() {
        return (Set<String>)this.mBundle.keySet();
    }
    
    public int size() {
        return this.mBundle.size();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeBundle(this.mBundle);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface BitmapKey {
    }
    
    public static final class Builder
    {
        private final Bundle mBundle;
        
        public Builder() {
            this.mBundle = new Bundle();
        }
        
        public Builder(final MediaMetadataCompat mediaMetadataCompat) {
            this.mBundle = new Bundle(mediaMetadataCompat.mBundle);
        }
        
        public Builder(final MediaMetadataCompat mediaMetadataCompat, final int n) {
            this(mediaMetadataCompat);
            for (final String s : this.mBundle.keySet()) {
                final Object value = this.mBundle.get(s);
                if (value != null && value instanceof Bitmap) {
                    final Bitmap bitmap = (Bitmap)value;
                    if (bitmap.getHeight() > n || bitmap.getWidth() > n) {
                        this.putBitmap(s, this.scaleBitmap(bitmap, n));
                    }
                    else {
                        if (Build$VERSION.SDK_INT < 14 || (!s.equals("android.media.metadata.ART") && !s.equals("android.media.metadata.ALBUM_ART"))) {
                            continue;
                        }
                        this.putBitmap(s, bitmap.copy(bitmap.getConfig(), false));
                    }
                }
            }
        }
        
        private Bitmap scaleBitmap(final Bitmap bitmap, int n) {
            final float n2 = n;
            final float min = Math.min(n2 / bitmap.getWidth(), n2 / bitmap.getHeight());
            n = (int)(bitmap.getHeight() * min);
            return Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * min), n, true);
        }
        
        public MediaMetadataCompat build() {
            return new MediaMetadataCompat(this.mBundle);
        }
        
        public Builder putBitmap(final String s, final Bitmap bitmap) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(s) != 2) {
                throw new IllegalArgumentException("The " + s + " key cannot be used to put a Bitmap");
            }
            this.mBundle.putParcelable(s, (Parcelable)bitmap);
            return this;
        }
        
        public Builder putLong(final String s, final long n) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(s) != 0) {
                throw new IllegalArgumentException("The " + s + " key cannot be used to put a long");
            }
            this.mBundle.putLong(s, n);
            return this;
        }
        
        public Builder putRating(final String s, final RatingCompat ratingCompat) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(s) != 3) {
                throw new IllegalArgumentException("The " + s + " key cannot be used to put a Rating");
            }
            if (Build$VERSION.SDK_INT >= 19) {
                this.mBundle.putParcelable(s, (Parcelable)ratingCompat.getRating());
                return this;
            }
            this.mBundle.putParcelable(s, (Parcelable)ratingCompat);
            return this;
        }
        
        public Builder putString(final String s, final String s2) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(s) != 1) {
                throw new IllegalArgumentException("The " + s + " key cannot be used to put a String");
            }
            this.mBundle.putCharSequence(s, (CharSequence)s2);
            return this;
        }
        
        public Builder putText(final String s, final CharSequence charSequence) {
            if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(s) && MediaMetadataCompat.METADATA_KEYS_TYPE.get(s) != 1) {
                throw new IllegalArgumentException("The " + s + " key cannot be used to put a CharSequence");
            }
            this.mBundle.putCharSequence(s, charSequence);
            return this;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface LongKey {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface RatingKey {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface TextKey {
    }
}
