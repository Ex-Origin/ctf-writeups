// 
// Decompiled by Procyon v0.5.30
// 

package edu.fudan.debugme;

import android.content.Context;
import android.app.Activity;
import android.widget.Toast;
import android.view.View;
import android.view.View$OnClickListener;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private Button button1;
    private EditText text1;
    
    static {
        System.loadLibrary("debugme");
    }
    
    public MainActivity() {
        this.button1 = null;
        this.text1 = null;
    }
    
    public native boolean flag1(final String p0);
    
    public native boolean flag2(final String p0);
    
    public native boolean flag3(final String p0);
    
    public native boolean flag4(final String p0);
    
    public native boolean flag5(final String p0);
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131296283);
        this.button1 = this.findViewById(2131165220);
        this.text1 = this.findViewById(2131165296);
        this.button1.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (MainActivity.this.flag1(MainActivity.this.text1.getText().toString())) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), (CharSequence)"Right!", 0).show();
                    return;
                }
                Toast.makeText(MainActivity.this.getApplicationContext(), (CharSequence)"Wrong!", 0).show();
            }
        });
        if (a.a(this) || b.b((Context)this) || b.c() || b.d()) {
            System.exit(0);
        }
    }
}
