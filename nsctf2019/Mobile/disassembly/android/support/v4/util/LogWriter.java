// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.util.Log;
import android.support.annotation.RestrictTo;
import java.io.Writer;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class LogWriter extends Writer
{
    private StringBuilder mBuilder;
    private final String mTag;
    
    public LogWriter(final String mTag) {
        this.mBuilder = new StringBuilder(128);
        this.mTag = mTag;
    }
    
    private void flushBuilder() {
        if (this.mBuilder.length() > 0) {
            Log.d(this.mTag, this.mBuilder.toString());
            this.mBuilder.delete(0, this.mBuilder.length());
        }
    }
    
    @Override
    public void close() {
        this.flushBuilder();
    }
    
    @Override
    public void flush() {
        this.flushBuilder();
    }
    
    @Override
    public void write(final char[] array, final int n, final int n2) {
        for (int i = 0; i < n2; ++i) {
            final char c = array[n + i];
            if (c == '\n') {
                this.flushBuilder();
            }
            else {
                this.mBuilder.append(c);
            }
        }
    }
}
