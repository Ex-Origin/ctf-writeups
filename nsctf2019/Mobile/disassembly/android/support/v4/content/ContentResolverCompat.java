// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content;

import android.os.OperationCanceledException;
import android.os.Build$VERSION;
import android.database.Cursor;
import android.support.v4.os.CancellationSignal;
import android.net.Uri;
import android.content.ContentResolver;

public final class ContentResolverCompat
{
    public static Cursor query(final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2, final CancellationSignal cancellationSignal) {
        if (Build$VERSION.SDK_INT >= 16) {
            Label_0042: {
                if (cancellationSignal == null) {
                    break Label_0042;
                }
                try {
                    Object cancellationSignalObject = cancellationSignal.getCancellationSignalObject();
                    return contentResolver.query(uri, array, s, array2, s2, (android.os.CancellationSignal)cancellationSignalObject);
                    cancellationSignalObject = null;
                    return contentResolver.query(uri, array, s, array2, s2, (android.os.CancellationSignal)cancellationSignalObject);
                }
                catch (Exception ex) {
                    if (ex instanceof OperationCanceledException) {
                        throw new android.support.v4.os.OperationCanceledException();
                    }
                    throw ex;
                }
            }
        }
        if (cancellationSignal != null) {
            cancellationSignal.throwIfCanceled();
        }
        return contentResolver.query(uri, array, s, array2, s2);
    }
}
