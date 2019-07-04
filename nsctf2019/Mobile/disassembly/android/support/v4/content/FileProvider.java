// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content;

import java.util.Iterator;
import android.net.Uri$Builder;
import java.util.Map;
import android.text.TextUtils;
import android.database.MatrixCursor;
import android.database.Cursor;
import java.io.FileNotFoundException;
import android.os.ParcelFileDescriptor;
import android.content.ContentValues;
import android.webkit.MimeTypeMap;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.net.Uri;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import android.content.Context;
import android.support.annotation.GuardedBy;
import java.util.HashMap;
import java.io.File;
import android.content.ContentProvider;

public class FileProvider extends ContentProvider
{
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS;
    private static final File DEVICE_ROOT;
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    @GuardedBy("sCache")
    private static HashMap<String, PathStrategy> sCache;
    private PathStrategy mStrategy;
    
    static {
        COLUMNS = new String[] { "_display_name", "_size" };
        DEVICE_ROOT = new File("/");
        FileProvider.sCache = new HashMap<String, PathStrategy>();
    }
    
    private static File buildPath(File file, final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final String s = array[i];
            if (s != null) {
                file = new File(file, s);
            }
        }
        return file;
    }
    
    private static Object[] copyOf(final Object[] array, final int n) {
        final Object[] array2 = new Object[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static String[] copyOf(final String[] array, final int n) {
        final String[] array2 = new String[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static PathStrategy getPathStrategy(final Context context, final String s) {
        // monitorenter(FileProvider.sCache)
        try {
            PathStrategy pathStrategy;
            if ((pathStrategy = FileProvider.sCache.get(s)) != null) {
                return pathStrategy;
            }
            try {
                pathStrategy = parsePathStrategy(context, s);
                FileProvider.sCache.put(s, pathStrategy);
                return pathStrategy;
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", ex);
            }
            catch (XmlPullParserException ex2) {
                throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", (Throwable)ex2);
            }
        }
        finally {}
    }
    
    public static Uri getUriForFile(final Context context, final String s, final File file) {
        return getPathStrategy(context, s).getUriForFile(file);
    }
    
    private static int modeToMode(final String s) {
        if ("r".equals(s)) {
            return 268435456;
        }
        if ("w".equals(s) || "wt".equals(s)) {
            return 738197504;
        }
        if ("wa".equals(s)) {
            return 704643072;
        }
        if ("rw".equals(s)) {
            return 939524096;
        }
        if ("rwt".equals(s)) {
            return 1006632960;
        }
        throw new IllegalArgumentException("Invalid mode: " + s);
    }
    
    private static PathStrategy parsePathStrategy(final Context context, final String s) throws IOException, XmlPullParserException {
        final SimplePathStrategy simplePathStrategy = new SimplePathStrategy(s);
        final XmlResourceParser loadXmlMetaData = context.getPackageManager().resolveContentProvider(s, 128).loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (loadXmlMetaData == null) {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
        while (true) {
            final int next = loadXmlMetaData.next();
            if (next == 1) {
                break;
            }
            if (next != 2) {
                continue;
            }
            final String name = loadXmlMetaData.getName();
            final String attributeValue = loadXmlMetaData.getAttributeValue((String)null, "name");
            final String attributeValue2 = loadXmlMetaData.getAttributeValue((String)null, "path");
            final File file = null;
            File file2;
            if ("root-path".equals(name)) {
                file2 = FileProvider.DEVICE_ROOT;
            }
            else if ("files-path".equals(name)) {
                file2 = context.getFilesDir();
            }
            else if ("cache-path".equals(name)) {
                file2 = context.getCacheDir();
            }
            else if ("external-path".equals(name)) {
                file2 = Environment.getExternalStorageDirectory();
            }
            else if ("external-files-path".equals(name)) {
                final File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
                file2 = file;
                if (externalFilesDirs.length > 0) {
                    file2 = externalFilesDirs[0];
                }
            }
            else {
                file2 = file;
                if ("external-cache-path".equals(name)) {
                    final File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
                    file2 = file;
                    if (externalCacheDirs.length > 0) {
                        file2 = externalCacheDirs[0];
                    }
                }
            }
            if (file2 == null) {
                continue;
            }
            simplePathStrategy.addRoot(attributeValue, buildPath(file2, attributeValue2));
        }
        return (PathStrategy)simplePathStrategy;
    }
    
    public void attachInfo(final Context context, final ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (providerInfo.exported) {
            throw new SecurityException("Provider must not be exported");
        }
        if (!providerInfo.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }
        this.mStrategy = getPathStrategy(context, providerInfo.authority);
    }
    
    public int delete(final Uri uri, final String s, final String[] array) {
        if (this.mStrategy.getFileForUri(uri).delete()) {
            return 1;
        }
        return 0;
    }
    
    public String getType(final Uri uri) {
        final File fileForUri = this.mStrategy.getFileForUri(uri);
        final int lastIndex = fileForUri.getName().lastIndexOf(46);
        if (lastIndex >= 0) {
            final String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileForUri.getName().substring(lastIndex + 1));
            if (mimeTypeFromExtension != null) {
                return mimeTypeFromExtension;
            }
        }
        return "application/octet-stream";
    }
    
    public Uri insert(final Uri uri, final ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }
    
    public boolean onCreate() {
        return true;
    }
    
    public ParcelFileDescriptor openFile(final Uri uri, final String s) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(s));
    }
    
    public Cursor query(final Uri uri, final String[] array, final String s, String[] array2, String s2) {
        final File fileForUri = this.mStrategy.getFileForUri(uri);
        String[] columns = array;
        if (array == null) {
            columns = FileProvider.COLUMNS;
        }
        array2 = new String[columns.length];
        final Object[] array3 = new Object[columns.length];
        final int length = columns.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            s2 = columns[i];
            if ("_display_name".equals(s2)) {
                array2[n] = "_display_name";
                final int n2 = n + 1;
                array3[n] = fileForUri.getName();
                n = n2;
            }
            else if ("_size".equals(s2)) {
                array2[n] = "_size";
                final int n3 = n + 1;
                array3[n] = fileForUri.length();
                n = n3;
            }
            ++i;
        }
        final String[] copy = copyOf(array2, n);
        final Object[] copy2 = copyOf(array3, n);
        final MatrixCursor matrixCursor = new MatrixCursor(copy, 1);
        matrixCursor.addRow(copy2);
        return (Cursor)matrixCursor;
    }
    
    public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        throw new UnsupportedOperationException("No external updates");
    }
    
    interface PathStrategy
    {
        File getFileForUri(final Uri p0);
        
        Uri getUriForFile(final File p0);
    }
    
    static class SimplePathStrategy implements PathStrategy
    {
        private final String mAuthority;
        private final HashMap<String, File> mRoots;
        
        public SimplePathStrategy(final String mAuthority) {
            this.mRoots = new HashMap<String, File>();
            this.mAuthority = mAuthority;
        }
        
        public void addRoot(final String s, final File file) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                this.mRoots.put(s, file.getCanonicalFile());
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + file, ex);
            }
        }
        
        @Override
        public File getFileForUri(Uri uri) {
            final String encodedPath = uri.getEncodedPath();
            final int index = encodedPath.indexOf(47, 1);
            final String decode = Uri.decode(encodedPath.substring(1, index));
            final String decode2 = Uri.decode(encodedPath.substring(index + 1));
            final File file = this.mRoots.get(decode);
            if (file == null) {
                throw new IllegalArgumentException("Unable to find configured root for " + uri);
            }
            uri = (Uri)new File(file, decode2);
            File canonicalFile;
            try {
                canonicalFile = ((File)uri).getCanonicalFile();
                if (!canonicalFile.getPath().startsWith(file.getPath())) {
                    throw new SecurityException("Resolved path jumped beyond configured root");
                }
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + uri);
            }
            return canonicalFile;
        }
        
        @Override
        public Uri getUriForFile(File o) {
            String canonicalPath;
            try {
                canonicalPath = ((File)o).getCanonicalPath();
                o = null;
                for (final Map.Entry<String, File> entry : this.mRoots.entrySet()) {
                    final String path = entry.getValue().getPath();
                    if (canonicalPath.startsWith(path) && (o == null || path.length() > ((Map.Entry<K, File>)o).getValue().getPath().length())) {
                        o = entry;
                    }
                }
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("Failed to resolve canonical path for " + o);
            }
            if (o == null) {
                throw new IllegalArgumentException("Failed to find configured root that contains " + canonicalPath);
            }
            final String path2 = ((Map.Entry<K, File>)o).getValue().getPath();
            String s;
            if (path2.endsWith("/")) {
                s = canonicalPath.substring(path2.length());
            }
            else {
                s = canonicalPath.substring(path2.length() + 1);
            }
            return new Uri$Builder().scheme("content").authority(this.mAuthority).encodedPath(Uri.encode((String)((Map.Entry<String, V>)o).getKey()) + '/' + Uri.encode(s, "/")).build();
        }
    }
}
