// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.hardware.fingerprint;

import java.security.Signature;
import javax.crypto.Mac;
import javax.crypto.Cipher;
import android.support.annotation.RequiresApi;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.os.Build$VERSION;
import android.content.Context;

public final class FingerprintManagerCompat
{
    static final FingerprintManagerCompatImpl IMPL;
    private Context mContext;
    
    static {
        if (Build$VERSION.SDK_INT >= 23) {
            IMPL = (FingerprintManagerCompatImpl)new Api23FingerprintManagerCompatImpl();
            return;
        }
        IMPL = (FingerprintManagerCompatImpl)new LegacyFingerprintManagerCompatImpl();
    }
    
    private FingerprintManagerCompat(final Context mContext) {
        this.mContext = mContext;
    }
    
    public static FingerprintManagerCompat from(final Context context) {
        return new FingerprintManagerCompat(context);
    }
    
    public void authenticate(@Nullable final CryptoObject cryptoObject, final int n, @Nullable final CancellationSignal cancellationSignal, @NonNull final AuthenticationCallback authenticationCallback, @Nullable final Handler handler) {
        FingerprintManagerCompat.IMPL.authenticate(this.mContext, cryptoObject, n, cancellationSignal, authenticationCallback, handler);
    }
    
    public boolean hasEnrolledFingerprints() {
        return FingerprintManagerCompat.IMPL.hasEnrolledFingerprints(this.mContext);
    }
    
    public boolean isHardwareDetected() {
        return FingerprintManagerCompat.IMPL.isHardwareDetected(this.mContext);
    }
    
    @RequiresApi(23)
    private static class Api23FingerprintManagerCompatImpl implements FingerprintManagerCompatImpl
    {
        static CryptoObject unwrapCryptoObject(final FingerprintManagerCompatApi23.CryptoObject cryptoObject) {
            if (cryptoObject != null) {
                if (cryptoObject.getCipher() != null) {
                    return new CryptoObject(cryptoObject.getCipher());
                }
                if (cryptoObject.getSignature() != null) {
                    return new CryptoObject(cryptoObject.getSignature());
                }
                if (cryptoObject.getMac() != null) {
                    return new CryptoObject(cryptoObject.getMac());
                }
            }
            return null;
        }
        
        private static FingerprintManagerCompatApi23.AuthenticationCallback wrapCallback(final AuthenticationCallback authenticationCallback) {
            return new FingerprintManagerCompatApi23.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(final int n, final CharSequence charSequence) {
                    authenticationCallback.onAuthenticationError(n, charSequence);
                }
                
                @Override
                public void onAuthenticationFailed() {
                    authenticationCallback.onAuthenticationFailed();
                }
                
                @Override
                public void onAuthenticationHelp(final int n, final CharSequence charSequence) {
                    authenticationCallback.onAuthenticationHelp(n, charSequence);
                }
                
                @Override
                public void onAuthenticationSucceeded(final AuthenticationResultInternal authenticationResultInternal) {
                    authenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(Api23FingerprintManagerCompatImpl.unwrapCryptoObject(authenticationResultInternal.getCryptoObject())));
                }
            };
        }
        
        private static FingerprintManagerCompatApi23.CryptoObject wrapCryptoObject(final CryptoObject cryptoObject) {
            if (cryptoObject != null) {
                if (cryptoObject.getCipher() != null) {
                    return new FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getCipher());
                }
                if (cryptoObject.getSignature() != null) {
                    return new FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getSignature());
                }
                if (cryptoObject.getMac() != null) {
                    return new FingerprintManagerCompatApi23.CryptoObject(cryptoObject.getMac());
                }
            }
            return null;
        }
        
        @Override
        public void authenticate(final Context context, final CryptoObject cryptoObject, final int n, final CancellationSignal cancellationSignal, final AuthenticationCallback authenticationCallback, final Handler handler) {
            final FingerprintManagerCompatApi23.CryptoObject wrapCryptoObject = wrapCryptoObject(cryptoObject);
            Object cancellationSignalObject;
            if (cancellationSignal != null) {
                cancellationSignalObject = cancellationSignal.getCancellationSignalObject();
            }
            else {
                cancellationSignalObject = null;
            }
            FingerprintManagerCompatApi23.authenticate(context, wrapCryptoObject, n, cancellationSignalObject, wrapCallback(authenticationCallback), handler);
        }
        
        @Override
        public boolean hasEnrolledFingerprints(final Context context) {
            return FingerprintManagerCompatApi23.hasEnrolledFingerprints(context);
        }
        
        @Override
        public boolean isHardwareDetected(final Context context) {
            return FingerprintManagerCompatApi23.isHardwareDetected(context);
        }
    }
    
    public abstract static class AuthenticationCallback
    {
        public void onAuthenticationError(final int n, final CharSequence charSequence) {
        }
        
        public void onAuthenticationFailed() {
        }
        
        public void onAuthenticationHelp(final int n, final CharSequence charSequence) {
        }
        
        public void onAuthenticationSucceeded(final AuthenticationResult authenticationResult) {
        }
    }
    
    public static final class AuthenticationResult
    {
        private CryptoObject mCryptoObject;
        
        public AuthenticationResult(final CryptoObject mCryptoObject) {
            this.mCryptoObject = mCryptoObject;
        }
        
        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }
    }
    
    public static class CryptoObject
    {
        private final Cipher mCipher;
        private final Mac mMac;
        private final Signature mSignature;
        
        public CryptoObject(final Signature mSignature) {
            this.mSignature = mSignature;
            this.mCipher = null;
            this.mMac = null;
        }
        
        public CryptoObject(final Cipher mCipher) {
            this.mCipher = mCipher;
            this.mSignature = null;
            this.mMac = null;
        }
        
        public CryptoObject(final Mac mMac) {
            this.mMac = mMac;
            this.mCipher = null;
            this.mSignature = null;
        }
        
        public Cipher getCipher() {
            return this.mCipher;
        }
        
        public Mac getMac() {
            return this.mMac;
        }
        
        public Signature getSignature() {
            return this.mSignature;
        }
    }
    
    private interface FingerprintManagerCompatImpl
    {
        void authenticate(final Context p0, final CryptoObject p1, final int p2, final CancellationSignal p3, final AuthenticationCallback p4, final Handler p5);
        
        boolean hasEnrolledFingerprints(final Context p0);
        
        boolean isHardwareDetected(final Context p0);
    }
    
    private static class LegacyFingerprintManagerCompatImpl implements FingerprintManagerCompatImpl
    {
        @Override
        public void authenticate(final Context context, final CryptoObject cryptoObject, final int n, final CancellationSignal cancellationSignal, final AuthenticationCallback authenticationCallback, final Handler handler) {
        }
        
        @Override
        public boolean hasEnrolledFingerprints(final Context context) {
            return false;
        }
        
        @Override
        public boolean isHardwareDetected(final Context context) {
            return false;
        }
    }
}
