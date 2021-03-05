/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryprogram;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karl Mattson
 */
public class ModifyProductMenuController implements Initializable {

    private Inventory inventory;
    private Stage window;
    private Product selectedProduct;
    @FXML
    private TextField textId;
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
    private Button buttonSearch;
    @FXML
    private TextField textSearch;
    @FXML
    private TableView<Part> tableViewPartsList;
    @FXML
    private TableColumn<Part, Integer> columnPartId;
    @FXML
    private TableColumn<Part, String> columnPartName;
    @FXML
    private TableColumn<Part, Integer> columnPartInventory;
    @FXML
    private TableColumn<Part, Double> columnPartPrice;
    @FXML
    private Button buttonAddPart;
    @FXML
    private TableView<Part> tableViewPartsInProduct;
    @FXML
    private TableColumn<Part, Integer> columnPartInProductId;
    @FXML
    private TableColumn<Part, String> columnPartInProductName;
    @FXML
    private TableColumn<Part, Integer> columnPartInProductInventory;
    @FXML
    private TableColumn<Part, Double> columnPartInProductPrice;
    @FXML
    private Button buttonDeletePart;
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonCancel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Need a way to format price as a dollar amount
        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        //Set up TableView
        //Part table
        columnPartId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnPartName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnPartName.setStyle("-fx-alignment: CENTER-RIGHT;");
        columnPartInventory.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        columnPartInventory.setStyle("-fx-alignment: CENTER-RIGHT;");
        columnPartPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        columnPartPrice.setStyle("-fx-alignment: CENTER-RIGHT;");

        //Format Price column
        columnPartPrice.setCellFactory(tc -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(price));
                }
            }
        });

        //Part in Product table
        columnPartInProductId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnPartInProductName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnPartInProductName.setStyle("-fx-alignment: CENTER-RIGHT;");
        columnPartInProductInventory.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        columnPartInProductInventory.setStyle("-fx-alignment: CENTER-RIGHT;");
        columnPartInProductPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        columnPartInProductPrice.setStyle("-fx-alignment: CENTER-RIGHT;");

        //Format Price column
        columnPartInProductPrice.setCellFactory(tc -> new TableCell<Part, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(price));
                }
            }
        });

        //load items into the TableView nodes
        tableViewPartsList.getItems().setAll(inventory.getAllParts());
        if(selectedProduct.getAllAssociatedParts() != null)
            tableViewPartsInProduct.getItems().setAll(selectedProduct.getAllAssociatedParts());
    }

    public ModifyProductMenuController(Inventory inventory, Product selectedProduct) throws IOException {
        //Store our inventory into local variable
        this.inventory = inventory;
        this.selectedProduct = selectedProduct;

        //create a new stage
        window = new Stage();

        //load our fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProductMenu.fxml"));

        //assign this controller to the fxml file
        loader.setController(this);

        //load our fxml into a scene and apply it to the stage
        window.setScene(new Scene(loader.load()));

        //populate fields with data from selected product
        textName.setText(selectedProduct.getName());
        textId.setText(Integer.toString(selectedProduct.getId()));
        textStock.setText(Integer.toString(selectedProduct.getStock()));
        textPrice.setText(Double.toString(selectedProduct.getPrice()));
        textMin.setText(Integer.toString(selectedProduct.getMin()));
        textMax.setText(Integer.toString(selectedProduct.getMax()));
    }

    public void showStage() {
        window.show();
    }

    @FXML
    private void handleSearchEvent(ActionEvent event) {
        String searchString;

        //if text box is empty set to an empty string
        if (textSearch.getText() == null) {
            searchString = "";
        } else {

            //get the search text
            searchString = textSearch.getText();
        }

        //repopulate table with the filtered data
        tableViewPartsList.getItems().setAll(inventory.lookupPart(searchString));
    }

    @FXML
    private void handleAddPartEvent(ActionEvent event) {
        Part selected;

        //Check to see if an item is selected in the part list tableview
        if (tableViewPartsList.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        //Get selected item from part tableview
        selected = tableViewPartsList.getSelectionModel().getSelectedItem();

        //Add part to second tableview
        tableViewPartsInProduct.getItems().add(selected);
    }

    @FXML
    private void handleDeletePartEvent(ActionEvent event) {
        int index;

        //Check to see if an item is selected in the part list tableview
        if (tableViewPartsInProduct.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        //Get selected item's index
        index = tableViewPartsInProduct.getSelectionModel().getSelectedIndex();

        //Delete item at the desired index
        tableViewPartsInProduct.getItems().remove(index);
    }

    @FXML
    private void handleSaveEvent(ActionEvent event) throws IOException {

        int newMin, newMax, newStock;
        double newPrice;
        String newName;

        //Get user inputs, make sure they are all filled in.
        if (textName.getText() == null || textName.getText().isEmpty()) {
            errMessage("Product name field must have a value.");
            return;
        }
        newName = textName.getText();

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

        //Further validation of inputs
        if (newMin > newMax) {
            errMessage("Minimum must be lower than maximum");
            return;
        }

        if (newStock < newMin || newStock > newMax) {
            errMessage("Inventory level must be within the minimum and maximum levels.");
            return;
        }

        //Create new Product
        Product newProduct = new Product(selectedProduct.getId(), newName, newPrice, newStock, newMin, newMax);

        //Add associated parts to product.  Also, check that product price is not less than the sum of its parts.
        ObservableList<Part> partsInProduct = tableViewPartsInProduct.getItems();
        Double totalPrice = 0.00;
        //Must have at least one part
        if (partsInProduct.size() == 0) {
            errMessage("The product must contain at least one part.");
            return;
        }
        for (Part part : partsInProduct) {
            totalPrice += part.getPrice();
            newProduct.addAssociatedPart(part);
        }
        if (totalPrice > newPrice) {
            errMessage("The product must cost at least as much as the total cost of its parts.");
            return;
        }

        //Remove old product from inventory
        inventory.deleteProduct(selectedProduct);

        //Add product to inventory
        inventory.addProduct(newProduct);

        //Return modified inventory to MainMenuController
        MainMenuController mainMenu = new MainMenuController(inventory);

        //display the stage to the user
        mainMenu.showStage();

        //close this window
        window.close();
    }

    @FXML
    private void handleCancelEvent(ActionEvent event) throws IOException {
        //Confirm that the user wishes to exit
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Do you wish to cancel modifying this product?");
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

    private void errMessage(String error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);

        alert.showAndWait();

    }
}
