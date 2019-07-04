// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.hardware.fingerprint;

import java.security.Signature;
import javax.crypto.Mac;
import javax.crypto.Cipher;
import android.hardware.fingerprint.FingerprintManager$AuthenticationResult;
import android.hardware.fingerprint.FingerprintManager$AuthenticationCallback;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager$CryptoObject;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class FingerprintManagerCompatApi23
{
    public static void authenticate(final Context context, final CryptoObject cryptoObject, final int n, final Object o, final AuthenticationCallback authenticationCallback, final Handler handler) {
        final FingerprintManager fingerprintManagerOrNull = getFingerprintManagerOrNull(context);
        if (fingerprintManagerOrNull != null) {
            fingerprintManagerOrNull.authenticate(wrapCryptoObject(cryptoObject), (CancellationSignal)o, n, wrapCallback(authenticationCallback), handler);
        }
    }
    
    private static FingerprintManager getFingerprintManagerOrNull(final Context context) {
        if (context.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            return (FingerprintManager)context.getSystemService((Class)FingerprintManager.class);
        }
        return null;
    }
    
    public static boolean hasEnrolledFingerprints(final Context context) {
        final FingerprintManager fingerprintManagerOrNull = getFingerprintManagerOrNull(context);
        return fingerprintManagerOrNull != null && fingerprintManagerOrNull.hasEnrolledFingerprints();
    }
    
    public static boolean isHardwareDetected(final Context context) {
        final FingerprintManager fingerprintManagerOrNull = getFingerprintManagerOrNull(context);
        return fingerprintManagerOrNull != null && fingerprintManagerOrNull.isHardwareDetected();
    }
    
    private static CryptoObject unwrapCryptoObject(final FingerprintManager$CryptoObject fingerprintManager$CryptoObject) {
        if (fingerprintManager$CryptoObject != null) {
            if (fingerprintManager$CryptoObject.getCipher() != null) {
                return new CryptoObject(fingerprintManager$CryptoObject.getCipher());
            }
            if (fingerprintManager$CryptoObject.getSignature() != null) {
                return new CryptoObject(fingerprintManager$CryptoObject.getSignature());
            }
            if (fingerprintManager$CryptoObject.getMac() != null) {
                return new CryptoObject(fingerprintManager$CryptoObject.getMac());
            }
        }
        return null;
    }
    
    private static FingerprintManager$AuthenticationCallback wrapCallback(final AuthenticationCallback authenticationCallback) {
        return new FingerprintManager$AuthenticationCallback() {
            public void onAuthenticationError(final int n, final CharSequence charSequence) {
                authenticationCallback.onAuthenticationError(n, charSequence);
            }
            
            public void onAuthenticationFailed() {
                authenticationCallback.onAuthenticationFailed();
            }
            
            public void onAuthenticationHelp(final int n, final CharSequence charSequence) {
                authenticationCallback.onAuthenticationHelp(n, charSequence);
            }
            
            public void onAuthenticationSucceeded(final FingerprintManager$AuthenticationResult fingerprintManager$AuthenticationResult) {
                authenticationCallback.onAuthenticationSucceeded(new AuthenticationResultInternal(unwrapCryptoObject(fingerprintManager$AuthenticationResult.getCryptoObject())));
            }
        };
    }
    
    private static FingerprintManager$CryptoObject wrapCryptoObject(final CryptoObject cryptoObject) {
        if (cryptoObject != null) {
            if (cryptoObject.getCipher() != null) {
                return new FingerprintManager$CryptoObject(cryptoObject.getCipher());
            }
            if (cryptoObject.getSignature() != null) {
                return new FingerprintManager$CryptoObject(cryptoObject.getSignature());
            }
            if (cryptoObject.getMac() != null) {
                return new FingerprintManager$CryptoObject(cryptoObject.getMac());
            }
        }
        return null;
    }
    
    public abstract static class AuthenticationCallback
    {
        public void onAuthenticationError(final int n, final CharSequence charSequence) {
        }
        
        public void onAuthenticationFailed() {
        }
        
        public void onAuthenticationHelp(final int n, final CharSequence charSequence) {
        }
        
        public void onAuthenticationSucceeded(final AuthenticationResultInternal authenticationResultInternal) {
        }
    }
    
    public static final class AuthenticationResultInternal
    {
        private CryptoObject mCryptoObject;
        
        public AuthenticationResultInternal(final CryptoObject mCryptoObject) {
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
}
