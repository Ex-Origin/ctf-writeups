// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.provider;

import java.util.ArrayList;
import android.net.Uri;
import java.io.IOException;
import android.webkit.MimeTypeMap;
import android.util.Log;
import java.io.File;

class RawDocumentFile extends DocumentFile
{
    private File mFile;
    
    RawDocumentFile(final DocumentFile documentFile, final File mFile) {
        super(documentFile);
        this.mFile = mFile;
    }
    
    private static boolean deleteContents(final File file) {
        final File[] listFiles = file.listFiles();
        boolean b = true;
        boolean b2 = true;
        if (listFiles != null) {
            final int length = listFiles.length;
            int n = 0;
            while (true) {
                b = b2;
                if (n >= length) {
                    break;
                }
                final File file2 = listFiles[n];
                boolean b3 = b2;
                if (file2.isDirectory()) {
                    b3 = (b2 & deleteContents(file2));
                }
                b2 = b3;
                if (!file2.delete()) {
                    Log.w("DocumentFile", "Failed to delete " + file2);
                    b2 = false;
                }
                ++n;
            }
        }
        return b;
    }
    
    private static String getTypeForName(String s) {
        final int lastIndex = s.lastIndexOf(46);
        if (lastIndex >= 0) {
            s = s.substring(lastIndex + 1).toLowerCase();
            s = MimeTypeMap.getSingleton().getMimeTypeFromExtension(s);
            if (s != null) {
                return s;
            }
        }
        return "application/octet-stream";
    }
    
    @Override
    public boolean canRead() {
        return this.mFile.canRead();
    }
    
    @Override
    public boolean canWrite() {
        return this.mFile.canWrite();
    }
    
    @Override
    public DocumentFile createDirectory(final String s) {
        final File file = new File(this.mFile, s);
        if (file.isDirectory() || file.mkdir()) {
            return new RawDocumentFile(this, file);
        }
        return null;
    }
    
    @Override
    public DocumentFile createFile(String string, final String s) {
        final String extensionFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(string);
        string = s;
        if (extensionFromMimeType != null) {
            string = s + "." + extensionFromMimeType;
        }
        final File file = new File(this.mFile, string);
        try {
            file.createNewFile();
            return new RawDocumentFile(this, file);
        }
        catch (IOException ex) {
            Log.w("DocumentFile", "Failed to createFile: " + ex);
            return null;
        }
    }
    
    @Override
    public boolean delete() {
        deleteContents(this.mFile);
        return this.mFile.delete();
    }
    
    @Override
    public boolean exists() {
        return this.mFile.exists();
    }
    
    @Override
    public String getName() {
        return this.mFile.getName();
    }
    
    @Override
    public String getType() {
        if (this.mFile.isDirectory()) {
            return null;
        }
        return getTypeForName(this.mFile.getName());
    }
    
    @Override
    public Uri getUri() {
        return Uri.fromFile(this.mFile);
    }
    
    @Override
    public boolean isDirectory() {
        return this.mFile.isDirectory();
    }
    
    @Override
    public boolean isFile() {
        return this.mFile.isFile();
    }
    
    @Override
    public boolean isVirtual() {
        return false;
    }
    
    @Override
    public long lastModified() {
        return this.mFile.lastModified();
    }
    
    @Override
    public long length() {
        return this.mFile.length();
    }
    
    @Override
    public DocumentFile[] listFiles() {
        final ArrayList<RawDocumentFile> list = new ArrayList<RawDocumentFile>();
        final File[] listFiles = this.mFile.listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                list.add(new RawDocumentFile(this, listFiles[i]));
            }
        }
        return list.toArray(new DocumentFile[list.size()]);
    }
    
    @Override
    public boolean renameTo(final String s) {
        final File mFile = new File(this.mFile.getParentFile(), s);
        if (this.mFile.renameTo(mFile)) {
            this.mFile = mFile;
            return true;
        }
        return false;
    }
}
