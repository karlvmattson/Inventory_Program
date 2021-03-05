//Title:  Inventory Program
//Course: C482 Object Oriented Application Development
//School: WGU
//Author: Karl Mattson
//
//Purpose:    Simple inventory program.  No permanent data storage.
package inventoryprogram;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Karl Mattson
 */
public class InventoryProgram extends Application {

    private Inventory inventory;

    @Override
    public void start(Stage stage) throws Exception {
        //create a blank inventory object
        this.inventory = new Inventory();

        //load test data here
        //pass our inventory object to MainMenu and start loading it
        MainMenuController mainMenu = new MainMenuController(inventory);

        //display the stage to the user
        mainMenu.showStage();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
