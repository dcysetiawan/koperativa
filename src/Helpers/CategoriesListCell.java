/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import javafx.scene.control.ListCell;

/**
 *
 * @author DSetiawan
 */
public class CategoriesListCell extends ListCell<Categories>{
    protected void updateItem(Categories categories, boolean empty){
        super.updateItem(categories, empty);
        if (empty) {
            setText(null);
        } else {
            setText(categories.getName());
        }
    }
}
