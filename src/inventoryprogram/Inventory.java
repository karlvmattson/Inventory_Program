/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventoryprogram;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Karl Mattson
 */
public class Inventory {

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public void addPart(Part newPart) {
        allParts.add(newPart);
    }

    public void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    public Part lookupPart(int partId) {
        for (Part selection : allParts) {
            if (selection.getId() == partId) {
                return selection;
            }
        }
        return null;
    }

    public Product lookupProduct(int productId) {
        for (Product selection : allProducts) {
            if (selection.getId() == productId) {
                return selection;
            }
        }
        return null;
    }

    public ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> found = FXCollections.observableArrayList();
        for (Part selection : allParts) {
            if (selection.getName().contains(partName)) {
                found.add(selection);
            }
        }
        return found;
    }

    public ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> found = FXCollections.observableArrayList();
        for (Product selection : allProducts) {
            if (selection.getName().contains(productName)) {
                found.add(selection);
            }
        }
        return found;
    }

    public void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    public void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    public boolean deletePart(Part selectedPart) {
        return this.allParts.remove(selectedPart);
    }

    public boolean deleteProduct(Product selectedProduct) {
        return this.allProducts.remove(selectedProduct);
    }

    public ObservableList<Part> getAllParts() {
        return this.allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return this.allProducts;
    }
}
