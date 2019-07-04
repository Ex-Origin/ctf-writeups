// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.content.ContentResolver;
import android.util.Log;
import java.util.ArrayList;
import android.provider.DocumentsContract;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class DocumentsContractApi21
{
    private static final String TAG = "DocumentFile";
    
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
    
    public static Uri createDirectory(final Context context, final Uri uri, final String s) {
        return createFile(context, uri, "vnd.android.document/directory", s);
    }
    
    public static Uri createFile(final Context context, final Uri uri, final String s, final String s2) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), uri, s, s2);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Uri[] listFiles(Context context, final Uri uri) {
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri buildChildDocumentsUriUsingTree = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri));
        final ArrayList<Uri> list = new ArrayList<Uri>();
        Object o = null;
        context = null;
        try {
            Object query = null;
            Label_0133: {
                try {
                    query = contentResolver.query(buildChildDocumentsUriUsingTree, new String[] { "document_id" }, (String)null, (String[])null, (String)null);
                    while (true) {
                        context = (Context)query;
                        o = query;
                        if (!((Cursor)query).moveToNext()) {
                            break Label_0133;
                        }
                        context = (Context)query;
                        o = query;
                        list.add(DocumentsContract.buildDocumentUriUsingTree(uri, ((Cursor)query).getString(0)));
                    }
                }
                catch (Exception ex) {
                    o = context;
                    Log.w("DocumentFile", "Failed query: " + ex);
                }
                return list.toArray(new Uri[list.size()]);
            }
            closeQuietly((AutoCloseable)query);
            return list.toArray(new Uri[list.size()]);
        }
        finally {
            closeQuietly((AutoCloseable)o);
        }
    }
    
    public static Uri prepareTreeUri(final Uri uri) {
        return DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
    }
    
    public static Uri renameTo(final Context context, final Uri uri, final String s) {
        try {
            return DocumentsContract.renameDocument(context.getContentResolver(), uri, s);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
