// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.content.ContentResolver;
import android.util.Log;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class DocumentsContractApi19
{
    private static final int FLAG_VIRTUAL_DOCUMENT = 512;
    private static final String TAG = "DocumentFile";
    
    public static boolean canRead(final Context context, final Uri uri) {
        return context.checkCallingOrSelfUriPermission(uri, 1) == 0 && !TextUtils.isEmpty((CharSequence)getRawType(context, uri));
    }
    
    public static boolean canWrite(final Context context, final Uri uri) {
        if (context.checkCallingOrSelfUriPermission(uri, 2) == 0) {
            final String rawType = getRawType(context, uri);
            final int queryForInt = queryForInt(context, uri, "flags", 0);
            if (!TextUtils.isEmpty((CharSequence)rawType)) {
                if ((queryForInt & 0x4) != 0x0) {
                    return true;
                }
                if ("vnd.android.document/directory".equals(rawType) && (queryForInt & 0x8) != 0x0) {
                    return true;
                }
                if (!TextUtils.isEmpty((CharSequence)rawType) && (queryForInt & 0x2) != 0x0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static void closeQuietly(final AutoCloseable autoCloseable) {
        if (autoCloseable == null) {
            return;
        }
        try {
            autoCloseable.close();
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {}
    }
    
    public static boolean delete(final Context context, final Uri uri) {
        try {
            return DocumentsContract.deleteDocument(context.getContentResolver(), uri);
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public static boolean exists(Context query, final Uri uri) {
        final ContentResolver contentResolver = query.getContentResolver();
        Object o = null;
        query = null;
        try {
            return ((Cursor)(o = (query = (Context)contentResolver.query(uri, new String[] { "document_id" }, (String)null, (String[])null, (String)null)))).getCount() > 0;
        }
        catch (Exception ex) {
            o = query;
            Log.w("DocumentFile", "Failed query: " + ex);
            return false;
        }
        finally {
            closeQuietly((AutoCloseable)o);
        }
    }
    
    public static long getFlags(final Context context, final Uri uri) {
        return queryForLong(context, uri, "flags", 0L);
    }
    
    public static String getName(final Context context, final Uri uri) {
        return queryForString(context, uri, "_display_name", null);
    }
    
    private static String getRawType(final Context context, final Uri uri) {
        return queryForString(context, uri, "mime_type", null);
    }
    
    public static String getType(final Context context, final Uri uri) {
        String rawType;
        if ("vnd.android.document/directory".equals(rawType = getRawType(context, uri))) {
            rawType = null;
        }
        return rawType;
    }
    
    public static boolean isDirectory(final Context context, final Uri uri) {
        return "vnd.android.document/directory".equals(getRawType(context, uri));
    }
    
    public static boolean isDocumentUri(final Context context, final Uri uri) {
        return DocumentsContract.isDocumentUri(context, uri);
    }
    
    public static boolean isFile(final Context context, final Uri uri) {
        final String rawType = getRawType(context, uri);
        return !"vnd.android.document/directory".equals(rawType) && !TextUtils.isEmpty((CharSequence)rawType);
    }
    
    public static boolean isVirtual(final Context context, final Uri uri) {
        return isDocumentUri(context, uri) && (getFlags(context, uri) & 0x200L) != 0x0L;
    }
    
    public static long lastModified(final Context context, final Uri uri) {
        return queryForLong(context, uri, "last_modified", 0L);
    }
    
    public static long length(final Context context, final Uri uri) {
        return queryForLong(context, uri, "_size", 0L);
    }
    
    private static int queryForInt(final Context context, final Uri uri, final String s, final int n) {
        return (int)queryForLong(context, uri, s, n);
    }
    
    private static long queryForLong(Context query, final Uri uri, final String s, final long n) {
        final ContentResolver contentResolver = query.getContentResolver();
        Object o = null;
        query = null;
        try {
            final Object o2 = o = (query = (Context)contentResolver.query(uri, new String[] { s }, (String)null, (String[])null, (String)null));
            if (((Cursor)o2).moveToFirst()) {
                query = (Context)o2;
                o = o2;
                if (!((Cursor)o2).isNull(0)) {
                    query = (Context)o2;
                    o = o2;
                    return ((Cursor)o2).getLong(0);
                }
            }
            return n;
        }
        catch (Exception ex) {
            o = query;
            Log.w("DocumentFile", "Failed query: " + ex);
            return n;
        }
        finally {
            closeQuietly((AutoCloseable)o);
        }
    }
    
    private static String queryForString(Context query, final Uri uri, String string, final String s) {
        final ContentResolver contentResolver = query.getContentResolver();
        Object o = null;
        query = null;
        try {
            final Object o2 = o = (query = (Context)contentResolver.query(uri, new String[] { string }, (String)null, (String[])null, (String)null));
            if (((Cursor)o2).moveToFirst()) {
                query = (Context)o2;
                o = o2;
                if (!((Cursor)o2).isNull(0)) {
                    query = (Context)o2;
                    o = o2;
                    string = ((Cursor)o2).getString(0);
                    return string;
                }
            }
            return s;
        }
        catch (Exception ex) {
            o = query;
            Log.w("DocumentFile", "Failed query: " + ex);
            return s;
        }
        finally {
            closeQuietly((AutoCloseable)o);
        }
    }
}
