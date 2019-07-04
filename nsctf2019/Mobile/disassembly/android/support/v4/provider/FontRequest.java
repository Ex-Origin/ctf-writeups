// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.provider;

import android.util.Base64;
import android.support.annotation.Nullable;
import android.support.v4.util.Preconditions;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import java.util.List;

public final class FontRequest
{
    private final List<List<byte[]>> mCertificates;
    private final int mCertificatesArray;
    private final String mProviderAuthority;
    private final String mProviderPackage;
    private final String mQuery;
    
    public FontRequest(@NonNull final String s, @NonNull final String s2, @NonNull final String s3, @ArrayRes final int mCertificatesArray) {
        this.mProviderAuthority = Preconditions.checkNotNull(s);
        this.mProviderPackage = Preconditions.checkNotNull(s2);
        this.mQuery = Preconditions.checkNotNull(s3);
        this.mCertificates = null;
        Preconditions.checkArgument(mCertificatesArray != 0);
        this.mCertificatesArray = mCertificatesArray;
    }
    
    public FontRequest(@NonNull final String s, @NonNull final String s2, @NonNull final String s3, @NonNull final List<List<byte[]>> list) {
        this.mProviderAuthority = Preconditions.checkNotNull(s);
        this.mProviderPackage = Preconditions.checkNotNull(s2);
        this.mQuery = Preconditions.checkNotNull(s3);
        this.mCertificates = Preconditions.checkNotNull(list);
        this.mCertificatesArray = 0;
    }
    
    @Nullable
    public List<List<byte[]>> getCertificates() {
        return this.mCertificates;
    }
    
    @ArrayRes
    public int getCertificatesArrayResId() {
        return this.mCertificatesArray;
    }
    
    public String getProviderAuthority() {
        return this.mProviderAuthority;
    }
    
    public String getProviderPackage() {
        return this.mProviderPackage;
    }
    
    public String getQuery() {
        return this.mQuery;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FontRequest {mProviderAuthority: " + this.mProviderAuthority + ", mProviderPackage: " + this.mProviderPackage + ", mQuery: " + this.mQuery + ", mCertificates:");
        for (int i = 0; i < this.mCertificates.size(); ++i) {
            sb.append(" [");
            final List<byte[]> list = this.mCertificates.get(i);
            for (int j = 0; j < list.size(); ++j) {
                sb.append(" \"");
                sb.append(Base64.encodeToString((byte[])list.get(j), 0));
                sb.append("\"");
            }
            sb.append(" ]");
        }
        sb.append("}");
        sb.append("mCertificatesArray: " + this.mCertificatesArray);
        return sb.toString();
    }
}
