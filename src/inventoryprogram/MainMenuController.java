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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
public class MainMenuController implements Initializable {

    private Inventory inventory;
    private Stage window;
    private Parent root;
    @FXML
    private Button buttonProductsSearch;
    @FXML
    private Button buttonProductsAdd;
    @FXML
    private Button buttonProductsModify;
    @FXML
    private Button buttonProductsDelete;
    @FXML
    private Button buttonPartsSearch;
    @FXML
    private Button buttonPartsAdd;
    @FXML
    private Button buttonPartsModify;
    @FXML
    private Button buttonPartsDelete;
    @FXML
    private Button buttonExit;
    @FXML
    private TableView<Product> tableViewProducts;
    @FXML
    private TableColumn<Product, Integer> columnProductId;
    @FXML
    private TableColumn<Product, String> columnProductName;
    @FXML
    private TableColumn<Product, Integer> columnProductInventory;
    @FXML
    private TableColumn<Product, Double> columnProductPrice;
    @FXML
    private TableView<Part> tableViewParts;
    @FXML
    private TableColumn<Part, Integer> columnPartId;
    @FXML
    private TableColumn<Part, String> columnPartName;
    @FXML
    private TableColumn<Part, Integer> columnPartInventory;
    @FXML
    private TableColumn<Part, Double> columnPartPrice;
    @FXML
    private TextField textProductSearch;
    @FXML
    private TextField textPartSearch;

    public MainMenuController(Inventory inventory) throws IOException {
        //Store our inventory into local variable
        this.inventory = inventory;

        //create a new stage
        window = new Stage();

        //load our fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));

        //assign this controller to the fxml file
        loader.setController(this);

        //load our fxml into a scene and apply it to the stage
        window.setScene(new Scene(loader.load()));
    }

    public void showStage() {
        window.show();
    }

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

        //Product table        
        columnProductId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnProductName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnProductName.setStyle("-fx-alignment: CENTER-RIGHT;");
        columnProductInventory.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        columnProductInventory.setStyle("-fx-alignment: CENTER-RIGHT;");
        columnProductPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        columnProductPrice.setStyle("-fx-alignment: CENTER-RIGHT;");

        //Format Price column
        columnProductPrice.setCellFactory(tc -> new TableCell<Product, Double>() {
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
        tableViewProducts.getItems().setAll(inventory.getAllProducts());
        tableViewParts.getItems().setAll(inventory.getAllParts());
    }

    public void loadInventory(Inventory inventory) {
        this.inventory = inventory;

    }

    @FXML
    private void handleProductSearchEvent(ActionEvent event) {
        String searchString;

        //if text box is empty set to an empty string
        if (textProductSearch.getText() == null) {
            searchString = "";
        } else {

            //get the search text
            searchString = textProductSearch.getText();
        }

        //repopulate table with the filtered data
        tableViewProducts.getItems().setAll(inventory.lookupProduct(searchString));
    }

    @FXML
    private void handleProductAddEvent(ActionEvent event) throws IOException {

        //pass our inventory object to AddPartMenu and start loading it
        AddProductMenuController addProductMenu = new AddProductMenuController(inventory);

        //display the stage to the user
        addProductMenu.showStage();

        //close this window
        window.close();
    }

    @FXML
    private void handleProductModifyEvent(ActionEvent event) throws IOException {
        Product selectedProduct;

        //Check to see if an item is selected in the part tableview
        if (tableViewProducts.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        //Get selected item from part tableview
        selectedProduct = tableViewProducts.getSelectionModel().getSelectedItem();

        //Open modify part menu
        ModifyProductMenuController modifyProductMenu = new ModifyProductMenuController(inventory, selectedProduct);
        modifyProductMenu.showStage();
        window.close();
    }

    @FXML
    private void handleProductDeleteEvent(ActionEvent event) {
        Product selectedProduct;

        //Check to see if an item is selected in the product tableview
        if (tableViewProducts.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        //Get selected item from product tableview
        selectedProduct = tableViewProducts.getSelectionModel().getSelectedItem();

        //Confirm that the user wishes to delete the product
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Product " + selectedProduct.getName() + " will be deleted.");
        confirmDialog.setContentText("Press OK to confirm.");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.get() == ButtonType.OK) {

            //Delete the selected Product
            inventory.deleteProduct(selectedProduct);
            tableViewProducts.getItems().setAll(inventory.getAllProducts());
        }

    }

    @FXML
    private void handlePartSearchEvent(ActionEvent event) {
        String searchString;

        //if text box is empty set to an empty string
        if (textPartSearch.getText() == null) {
            searchString = "";
        } else {

            //get the search text
            searchString = textPartSearch.getText();
        }

        //repopulate table with the filtered data
        tableViewParts.getItems().setAll(inventory.lookupPart(searchString));

    }

    @FXML
    private void handlePartAddEvent(ActionEvent event) throws IOException {

        //pass our inventory object to AddPartMenu and start loading it
        AddPartMenuController addPartMenu = new AddPartMenuController(inventory);

        //display the stage to the user
        addPartMenu.showStage();

        //close this window
        window.close();
    }

    @FXML
    private void handlePartModifyEvent(ActionEvent event) throws IOException {

        Part selectedPart;

        //Check to see if an item is selected in the part tableview
        if (tableViewParts.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        //Get selected item from part tableview
        selectedPart = tableViewParts.getSelectionModel().getSelectedItem();

        //Open modify part menu
        ModifyPartMenuController modifyPartMenu = new ModifyPartMenuController(inventory, selectedPart);
        modifyPartMenu.showStage();
        window.close();
    }

    @FXML
    private void handlePartDeleteEvent(ActionEvent event) {
        Part selectedPart;

        //Check to see if an item is selected in the part tableview
        if (tableViewParts.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        //Get selected item from part tableview
        selectedPart = tableViewParts.getSelectionModel().getSelectedItem();

        //Confirm that the user wishes to delete the part
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Deletion");
        confirmDialog.setHeaderText("Part " + selectedPart.getName() + " will be deleted.");
        confirmDialog.setContentText("Press OK to confirm.");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.get() == ButtonType.OK) {

            //Delete the selected Part
            inventory.deletePart(selectedPart);
            tableViewParts.getItems().setAll(inventory.getAllParts());
        }
    }

    @FXML
    private void handleExitEvent(ActionEvent event) {

        //Confirm that the user wishes to exit
        Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm");
        confirmDialog.setHeaderText(null);
        confirmDialog.setContentText("Exit program?");
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

}
