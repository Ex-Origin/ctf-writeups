// 
// Decompiled by Procyon v0.5.30
// 

package ctf_problem;

import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import javax.swing.KeyStroke;
import javax.swing.text.TextAction;

class SelectAll extends TextAction
{
    public SelectAll() {
        super("Select All");
        this.putValue("AcceleratorKey", KeyStroke.getKeyStroke("control A"));
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        final JTextComponent component = this.getFocusedComponent();
        component.selectAll();
        component.requestFocusInWindow();
    }
}
