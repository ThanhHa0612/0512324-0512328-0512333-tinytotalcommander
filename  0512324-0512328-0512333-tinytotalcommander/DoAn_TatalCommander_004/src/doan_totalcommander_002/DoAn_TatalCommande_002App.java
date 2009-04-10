/*
 * DoAn_TatalCommande_002App.java
 */

package doan_totalcommander_002;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class DoAn_TatalCommande_002App extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new DoAn_TatalCommande_002View(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of DoAn_TatalCommande_002App
     */
    public static DoAn_TatalCommande_002App getApplication() {
        return Application.getInstance(DoAn_TatalCommande_002App.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(DoAn_TatalCommande_002App.class, args);
    }
}
