/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryprogram;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karl Mattson
 */
public class AddPartMenuController implements Initializable {

    private Inventory inventory;
    private Stage window;
    @FXML
    private RadioButton radioInHouse;
    @FXML
    private RadioButton radioOutsourced;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;
    @FXML
    private TextField textName;
    @FXML
    private TextField textStock;
    @FXML
    private TextField textPrice;
    @FXML
    private TextField textMax;
    @FXML
    private TextField textMin;
    @FXML
    private Label labelCompanyMachine;
    @FXML
    private TextField textCompanyMachine;

    /**
     * Initializes the controller class.
     *
     * @param url
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public AddPartMenuController(Inventory inventory) throws IOException {
        //Store our inventory into local variable
        this.inventory = inventory;

        //create a new stage
        window = new Stage();

        //load our fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPartMenu.fxml"));

        //assign this controller to the fxml file
        loader.setController(this);

        //load our fxml into a scene and apply it to the stage
        window.setScene(new Scene(loader.load()));

        //Set up radio buttons
        radioInHouse.setSelected(true);
        radioOutsourced.setSelected(false);
    }

    public void showStage() {
        window.show();
    }

    @FXML
    private void handleRadioInHouse(ActionEvent event) {

        //deselect the other radio button
        radioOutsourced.setSelected(false);

        //change label and text fields as needed
        textCompanyMachine.setPromptText("Mach ID");
        labelCompanyMachine.setText("Machine ID");

    }

    @FXML
    private void handleRadioOutsourced(ActionEvent event) {
        //deselect the other radio button
        radioInHouse.setSelected(false);

        //change label and text fields as needed
        textCompanyMachine.setPromptText("Comp Nm");
        labelCompanyMachine.setText("Company Name");
    }

    @FXML
    private void handleCancel(ActionEvent event) throws IOException {

        //Confirm that the user wishes to exit
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Do you wish to cancel adding a part?");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            //pass our inventory object to MainMenu and start loading it
            MainMenuController mainMenu = new MainMenuController(inventory);

            //display the stage to the user
            mainMenu.showStage();

            //close this window
            window.close();
        }

    }

    @FXML
    private void handleSave(ActionEvent event) throws IOException {

        int newMin, newMax, newStock;
        double newPrice;
        boolean inHouse = radioInHouse.isSelected();
        String newCompanyMachine;

        //Get user inputs, make sure they are all filled in.
        if (textName.getText() == null || textName.getText().isEmpty()) {
            errMessage("Part name field must have a value.");
            return;
        }
        String newName = textName.getText();

        if (textStock.getText() == null || textStock.getText().isEmpty()) {
            errMessage("Inventory field must have a value.");
            return;
        }
        try {
            newStock = Integer.parseInt(textStock.getText());
        } catch (NumberFormatException e) {
            errMessage("Inventory level must be an integer.");
            return;
        }
        if (textPrice.getText() == null || textPrice.getText().isEmpty()) {
            errMessage("Price field must have a value.");
            return;
        }
        try {
            newPrice = Double.parseDouble(textPrice.getText());
        } catch (NumberFormatException e) {
            errMessage("Price field is invalid.");
            return;
        }

        if (textMax.getText() == null || textMax.getText().isEmpty()) {
            errMessage("Max field must have a value.");
            return;
        }
        try {
            newMax = Integer.parseInt(textMax.getText());
        } catch (NumberFormatException e) {
            errMessage("Max field must be an integer.");
            return;
        }

        if (textMin.getText() == null || textMin.getText().isEmpty()) {
            errMessage("Min field must have a value.");
            return;
        }
        try {
            newMin = Integer.parseInt(textMin.getText());
        } catch (NumberFormatException e) {
            errMessage("Min field must be an integer.");
            return;
        }

        if (inHouse) {
            if (textCompanyMachine.getText() == null || textCompanyMachine.getText().isEmpty()) {
                errMessage("Machine ID field must have a value.");
                return;
            }
            try {
                int errorCheckingInt = Integer.parseInt(textCompanyMachine.getText());
            } catch (NumberFormatException e) {
                errMessage("Machine ID must be an integer.");
                return;
            }

        } else {
            if (textCompanyMachine.getText() == null || textCompanyMachine.getText().isEmpty()) {
                errMessage("Company Name field must have a value.");
                return;
            }

        }
        newCompanyMachine = textCompanyMachine.getText();

        //Further validation of inputs
        if (newMin > newMax) {
            errMessage("Minimum must be lower than maximum");
            return;
        }

        if (newStock < newMin || newStock > newMax) {
            errMessage("Inventory level must be within the minimum and maximum levels.");
            return;
        }

        //Generate new ID value
        int maxId = 0;
        for (Part part : inventory.getAllParts()) {
            if (part.getId() > maxId) {
                maxId = part.getId();
            }
        }

        //Create new Part and add to inventory
        if (inHouse) {
            InHouse newPart = new InHouse(++maxId, newName, newPrice, newStock, newMin, newMax, Integer.parseInt(newCompanyMachine));
            inventory.addPart(newPart);
        } else {
            Outsourced newPart = new Outsourced(++maxId, newName, newPrice, newStock, newMin, newMax, newCompanyMachine);
            inventory.addPart(newPart);
        }

        //Return modified inventory to MainMenuController
        MainMenuController mainMenu = new MainMenuController(inventory);

        //display the stage to the user
        mainMenu.showStage();

        //close this window
        window.close();

    }

    private void errMessage(String error) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);

        alert.showAndWait();

    }
}
