// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content;

import android.support.annotation.NonNull;
import android.content.SharedPreferences$Editor;

public final class SharedPreferencesCompat
{
    public static final class EditorCompat
    {
        private static EditorCompat sInstance;
        private final Helper mHelper;
        
        private EditorCompat() {
            this.mHelper = new Helper();
        }
        
        public static EditorCompat getInstance() {
            if (EditorCompat.sInstance == null) {
                EditorCompat.sInstance = new EditorCompat();
            }
            return EditorCompat.sInstance;
        }
        
        public void apply(@NonNull final SharedPreferences$Editor sharedPreferences$Editor) {
            this.mHelper.apply(sharedPreferences$Editor);
        }
        
        private static class Helper
        {
            public void apply(@NonNull final SharedPreferences$Editor sharedPreferences$Editor) {
                try {
                    sharedPreferences$Editor.apply();
                }
                catch (AbstractMethodError abstractMethodError) {
                    sharedPreferences$Editor.commit();
                }
            }
        }
    }
}
