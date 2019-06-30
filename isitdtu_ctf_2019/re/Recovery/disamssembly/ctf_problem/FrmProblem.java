// 
// Decompiled by Procyon v0.5.30
// 

package ctf_problem;

import javax.swing.JOptionPane;
import javax.swing.LayoutStyle;
import java.awt.LayoutManager;
import javax.swing.GroupLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.text.DefaultEditorKit;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;

class FrmProblem extends JFrame
{
    private JButton btnSubmit;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JTextArea txtInput;
    
    public FrmProblem() {
        this.initComponents();
        final JPopupMenu popup = new JPopupMenu();
        JMenuItem item = new JMenuItem(new DefaultEditorKit.CutAction());
        item.setText("Cut");
        popup.add(item);
        item = new JMenuItem(new DefaultEditorKit.CopyAction());
        item.setText("Copy");
        popup.add(item);
        item = new JMenuItem(new DefaultEditorKit.PasteAction());
        item.setText("Paste");
        popup.add(item);
        final Action selectAll = new SelectAll();
        popup.add(selectAll);
        this.txtInput.setComponentPopupMenu(popup);
    }
    
    public boolean check(final int[] a, final int[] b) {
        for (int i = 0; i < a.length; ++i) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }
    
    public String getFlag(final int[] a) {
        String s = "";
        for (int i = 0; i < a.length; ++i) {
            s = s + a[i] + ", ";
        }
        return s;
    }
    
    private void initComponents() {
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.jScrollPane1 = new JScrollPane();
        this.txtInput = new JTextArea();
        this.btnSubmit = new JButton();
        this.setDefaultCloseOperation(3);
        this.jLabel1.setText("Recovery Challenge");
        this.jLabel2.setText("Your solution:");
        this.txtInput.setColumns(20);
        this.txtInput.setRows(5);
        this.jScrollPane1.setViewportView(this.txtInput);
        this.btnSubmit.setText("Submit");
        this.btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent evt) {
                FrmProblem.this.btnSubmitActionPerformed(evt);
            }
        });
        final GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(16, 16, 16).addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 17, 32767).addComponent(this.jScrollPane1, -2, 405, -2).addGap(15, 15, 15)).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(-1, 32767).addComponent(this.btnSubmit).addGap(213, 213, 213)).addGroup(layout.createSequentialGroup().addGap(208, 208, 208).addComponent(this.jLabel1).addContainerGap(-1, 32767)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(14, 14, 14).addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jScrollPane1, -2, -1, -2)).addGroup(layout.createSequentialGroup().addGap(73, 73, 73).addComponent(this.jLabel2))).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.btnSubmit).addContainerGap(11, 32767)));
        this.pack();
    }
    
    private void btnSubmitActionPerformed(final ActionEvent evt) {
        try {
            final String txtInputS = this.txtInput.getText().trim();
            final String[] str = txtInputS.split(",");
            final int[] input = new int[str.length];
            for (int i = 0; i < str.length; ++i) {
                input[i] = Integer.parseInt(str[i].trim());
            }
            final int[] s = { 9, 11, 33, 35, 38, 40, 44, 48, 61, 85, 89, 101, 106, 110, 135, 150, 159, 180, 188, 200, 201, 214, 241, 253, 268, 269, 275, 278, 285, 301, 301, 327, 356, 358, 363, 381, 396, 399, 413, 428, 434, 445, 449, 462, 471, 476, 481, 492, 496, 497, 509, 520, 526, 534, 540, 589, 599, 613, 621, 621, 623, 628, 634, 650, 652, 653, 658, 665, 679, 691, 708, 711, 716, 722, 752, 756, 764, 771, 773, 786, 807, 808, 826, 827, 836, 842, 856, 867, 875, 877, 879, 889, 892, 922, 946, 951, 965, 980, 993, 996 };
            final int[] l = { 35, 33, 44, 40, 38, 48, 11, 85, 89, 61, 110, 150, 159, 135, 188, 200, 180, 106, 101, 214, 268, 275, 269, 253, 241, 201, 9, 301, 301, 285, 327, 356, 363, 396, 413, 399, 445, 434, 462, 449, 428, 471, 481, 492, 496, 497, 476, 381, 358, 278, 534, 526, 520, 613, 599, 623, 621, 621, 589, 540, 628, 650, 653, 652, 665, 691, 679, 711, 756, 752, 722, 716, 807, 786, 773, 771, 826, 808, 827, 764, 856, 875, 867, 842, 836, 708, 879, 892, 889, 922, 877, 951, 946, 658, 980, 996, 993, 965, 634, 509 };
            if (this.check(s, new CTF_Problem().getResultA(input))) {
                if (this.check(l, new CTF_Problem().getResultB(input))) {
                    JOptionPane.showMessageDialog(this.rootPane, "Recovery successfull\nFlag is your solution");
                }
                else {
                    JOptionPane.showMessageDialog(this.rootPane, "Wrong answer! Try angain...");
                }
            }
            else {
                JOptionPane.showMessageDialog(this.rootPane, "Wrong answer! Try angain...");
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this.rootPane, "Wrong answer! Try angain...");
        }
    }
}
