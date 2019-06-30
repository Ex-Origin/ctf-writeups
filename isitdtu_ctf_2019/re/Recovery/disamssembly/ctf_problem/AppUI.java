// 
// Decompiled by Procyon v0.5.30
// 

package ctf_problem;

import java.awt.EventQueue;
import javafx.stage.Stage;
import javafx.application.Application;

public class AppUI extends Application
{
    public static void main(final String[] args) {
        launch(args);
    }
    
    public void start(final Stage stage) throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrmProblem().setVisible(true);
            }
        });
    }
}
