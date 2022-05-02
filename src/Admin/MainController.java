/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Helpers.Categories;
import Helpers.CategoriesListCell;
import Helpers.MySQLConnection;
import Helpers.NumberField;
import Helpers.Password;
import LocalStorage.UserSession;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author DSetiawan
 */
public class MainController implements Initializable {
    @FXML
    private VBox cashierItems;
    @FXML
    private VBox productItems;
    @FXML
    private VBox categoriesItems;
    @FXML
    private VBox historyItems;
    @FXML
    private VBox historyDetailItems;
    
    @FXML
    private JFXButton navCashier;
    @FXML
    private JFXButton navProduct;
    @FXML
    private JFXButton navCategories;
    @FXML
    private JFXButton navHistory;

    @FXML
    private AnchorPane paneCashier;
    @FXML
    private TextField searchCashier;
    @FXML
    private TextField fieldAddCashierName;

    @FXML
    private AnchorPane paneCashierDetail;
    @FXML
    private Label detailCashierName;
    @FXML
    private TextField fieldDetailCashierUsername;
    @FXML
    private TextField fieldDetailCashierName;
    @FXML
    private PasswordField fieldDetailCashierPassword;
    @FXML
    private JFXButton fieldDetailCashierSubmit;
    @FXML
    private JFXButton fieldDetailCashierStatus;

    @FXML
    private AnchorPane paneProduct;
    @FXML
    private TextField searchProduct;
    @FXML
    private TextField fieldAddProductCode;
    @FXML
    private TextField fieldAddProductName;
    @FXML
    private JFXComboBox fieldAddProductCategory;
    @FXML
    private TextField fieldAddProductStock;
    @FXML
    private TextField fieldAddProductPrice;

    @FXML
    private AnchorPane paneProductDetail;
    @FXML
    private Label detailProductName;
    @FXML
    private TextField fieldDetailProductCode;
    @FXML
    private TextField fieldDetailProductName;
    @FXML
    private JFXComboBox fieldDetailProductCategory;
    @FXML
    private TextField fieldDetailProductStock;
    @FXML
    private TextField fieldDetailProductPrice;
    @FXML
    private JFXButton fieldDetailProductSubmit;
    @FXML
    private JFXButton fieldDetailProductDelete;

    @FXML
    private AnchorPane paneCategories;
    @FXML
    private TextField searchCategory;
    @FXML
    private TextField fieldAddCategoryName;

    @FXML
    private AnchorPane paneCategoryDetail;
    @FXML
    private Label detailCategoryName;
    @FXML
    private Label fieldDetailCategoryNameHelp;
    @FXML
    private TextField fieldDetailCategoryName;
    @FXML
    private JFXButton fieldDetailCategorySubmit;
    @FXML
    private Label fieldDetailCategoryOr;
    @FXML
    private JFXButton fieldDetailCategoryDelete;

    @FXML
    private AnchorPane paneHistory;
    @FXML
    private JFXDatePicker historyDateStart;
    @FXML
    private JFXDatePicker historyDateEnd;
    @FXML
    private Button clearHistoryDate;
    
    private LocalDate historyStart; 
    private LocalDate historyEnd; 

    @FXML
    private AnchorPane paneHistoryDetail;
    @FXML
    private Label historyDetailId;
    @FXML
    private Label historyDetailDateTime;
    @FXML
    private Label historyDetailTotal;
    @FXML
    private Label historyDetailCustomerMoney;
    @FXML
    private Label historyDetailChangeMoney;
    
    private final String cashierList = "/Admin/CashierList.fxml";
    private final String productList = "/Admin/ProductList.fxml";
    private final String staticProductList = "/Admin/StaticProductList.fxml";
    private final String categoryList = "/Admin/CategoryList.fxml";
    private final String historyList = "/Admin/HistoryList.fxml";
    
    Connection connection;
    
    Password trippleEnc;

    public MainController() throws Exception {
        this.trippleEnc = new Password();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserSession.authorizeUser();
        
        MySQLConnection connection = new MySQLConnection();
        connection.connect();
        this.connection = connection.connection;
        
        paneCashier.toFront();
        navCashier.setStyle("-fx-background-color : #343541");
        
        try {
            renderCashiers();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Number Formatting
        new NumberField(fieldAddProductCode);
        new NumberField(fieldAddProductStock);
        new NumberField(fieldAddProductPrice);
        
        // Search Cashier
        searchCashier.textProperty().addListener((obs, oldValue, newValue) -> {
            cashierItems.getChildren().forEach((listItem) -> {
                String cashierName = ((Label) listItem.lookup("#cashierName")).getText().toLowerCase().trim();
                String cashierUsername = ((Label) listItem.lookup("#cashierUsername")).getText().toLowerCase().trim();
                String cashierStatus = ((Label) listItem.lookup("#cashierStatus")).getText().toLowerCase().trim();
                if(cashierName.contains(newValue.toLowerCase().trim())||cashierUsername.contains(newValue.toLowerCase().trim())||cashierStatus.contains(newValue.toLowerCase().trim())){
                    listItem.setVisible(true);
                    listItem.setManaged(true);
                }else{
                    listItem.setVisible(false);
                    listItem.setManaged(false);
                }
            });
        });
        
        // Search Product
        searchProduct.textProperty().addListener((obs, oldValue, newValue) -> {
            productItems.getChildren().forEach((listItem) -> {
                String productCode = ((Label) listItem.lookup("#productCode")).getText().toLowerCase().trim();
                String productName = ((Label) listItem.lookup("#productName")).getText().toLowerCase().trim();
                if(productCode.contains(newValue.toLowerCase().trim())||productName.contains(newValue.toLowerCase().trim())){
                    listItem.setVisible(true);
                    listItem.setManaged(true);
                }else{
                    listItem.setVisible(false);
                    listItem.setManaged(false);
                }
            });
        });
        
        // Search Category
        searchCategory.textProperty().addListener((obs, oldValue, newValue) -> {
            categoriesItems.getChildren().forEach((listItem) -> {
                String categoryName = ((Label) listItem.lookup("#categoryName")).getText().toLowerCase().trim();
                if(categoryName.contains(newValue.toLowerCase().trim())){
                    listItem.setVisible(true);
                    listItem.setManaged(true);
                }else{
                    listItem.setVisible(false);
                    listItem.setManaged(false);
                }
            });
        });
        
        // History filter
        historyDateStart.valueProperty().addListener((ov, oldValue, newValue) -> {
            this.historyStart = newValue;
            filterHistory();
        });
        
        historyDateEnd.valueProperty().addListener((ov, oldValue, newValue) -> {
            this.historyEnd = newValue;
            filterHistory();
        });
        
        clearHistoryDate.setOnAction((event) -> {
            historyDateStart.setValue(null);
            historyDateEnd.setValue(null);
            this.historyStart = null;
            this.historyEnd = null;
            renderHistory(null, null);
        });
    }    
     
    public void handleAddCashier(){
        try {
            String cashierName = fieldAddCashierName.getText().trim();

            if (!cashierName.isEmpty()) {
                String cashierId = UUID.randomUUID().toString();
                String cashierUsername = cashierName.toLowerCase().replaceAll("\\s+", "");
                String cashierPassword = trippleEnc.encrypt(cashierUsername);

                PreparedStatement ps = connection.prepareStatement("INSERT INTO kv_users SET user_id = ?, username = ?, password = ?, display_name = ?, role_id = 2");
                ps.setString(1, cashierId);
                ps.setString(2, cashierUsername);
                ps.setString(3, cashierPassword);
                ps.setString(4, cashierName);
                ps.execute();

                renderCashiers();
                fieldAddCashierName.setText("");
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon masukkan data dengan benar!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }  
        } catch (IOException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    public void handleAddProduct(){
        try {
            String productCode = fieldAddProductCode.getText().trim();
            String productName = fieldAddProductName.getText().trim();
            String productStock = fieldAddProductStock.getText().trim();
            String productPrice = fieldAddProductPrice.getText().trim();

            if (!productCode.isEmpty() && !productName.isEmpty() && !fieldAddProductCategory.getSelectionModel().isEmpty() && !productStock.isEmpty() && !productPrice.isEmpty()) {
                int productCategory = ((Categories) fieldAddProductCategory.getValue()).getId();
                PreparedStatement ps = connection.prepareStatement("INSERT INTO kv_products SET product_id = ?, product_name = ?, category_id = ?, product_stock = ?, product_price = ?");
                ps.setString(1, productCode);
                ps.setString(2, productName);
                ps.setInt(3, productCategory);
                ps.setInt(4, Integer.parseInt(productStock));
                ps.setInt(5, Integer.parseInt(productPrice));
                ps.execute();
                
                renderProducts();
                
                fieldAddProductCode.setText("");
                fieldAddProductName.setText("");
                fieldAddProductCategory.getSelectionModel().clearSelection();
                fieldAddProductStock.setText("");
                fieldAddProductPrice.setText("");
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon masukkan data dengan benar!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }  
        } catch (IOException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    public void handleAddCategory(){
        try {
            String categoryName = fieldAddCategoryName.getText().trim();

            if (!categoryName.isEmpty()) {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO kv_categories SET category_name = ?");
                ps.setString(1, categoryName);
                ps.execute();

                renderCategories();
                fieldAddCategoryName.setText("");
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon masukkan data dengan benar!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }  
        } catch (IOException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    public void renderCashiers() throws IOException{
        paneCashier.toFront();
        navCashier.setStyle("-fx-background-color : #343541");
        fieldAddCashierName.setText("");
        cashierItems.getChildren().clear();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM kview_cashiers");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Node listItem = FXMLLoader.load(getClass().getResource(cashierList));

                Label labelName = (Label) listItem.lookup("#cashierName");
                Label labelUsername = (Label) listItem.lookup("#cashierUsername");
                Label labelStatus = (Label) listItem.lookup("#cashierStatus");
                Label labelSummary = (Label) listItem.lookup("#cashierSummary");

                String id = rs.getString("user_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String diplayName = rs.getString("display_name");
                boolean isBlocked = rs.getBoolean("is_blocked");
                int summary = rs.getInt("summary");

                labelName.setText(diplayName);
                labelUsername.setText(username);
                labelStatus.setText(isBlocked ? "Nonaktif" : "Aktif");
                labelStatus.getStyleClass().removeAll("bgSuccess", "bgDanger");
                labelStatus.getStyleClass().add(isBlocked ? "bgDanger" : "bgSuccess");
                labelSummary.setText("Rp " + summary);

                listItem.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                    listItem.setStyle("-fx-background-color : #3e404c; -fx-cursor : hand;");
                });
                listItem.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                    listItem.setStyle("-fx-background-color : #4A4C5A");
                });
                listItem.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    renderCashierDetail(id, username, password, diplayName, isBlocked);
                });
                
                cashierItems.getChildren().add(listItem);
            }
            
        } catch (IOException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                alert.setHeaderText(null);
                alert.showAndWait();
                e.printStackTrace();
        }
    }
    
    public void renderCashierDetail(String id, String username, String encryptedPassword, String displayName, boolean isBlocked){
        paneCashierDetail.toFront();
        String password = trippleEnc.decrypt(encryptedPassword);
        
        detailCashierName.setText(displayName);
        fieldDetailCashierUsername.setText(username);
        fieldDetailCashierUsername.setDisable(true);
        fieldDetailCashierName.setText(displayName);
        fieldDetailCashierPassword.setText(password);
        
        fieldDetailCashierStatus.getStyleClass().removeAll("btnSuccess", "btnDanger");
        fieldDetailCashierStatus.getStyleClass().add(isBlocked ? "btnSuccess" : "btnDanger");
        fieldDetailCashierStatus.setText(isBlocked ? "Aktifkan Akun" : "Nonaktifkan Akun");
        
        fieldDetailCashierSubmit.setOnAction((event) -> {
            try {
                String newPassword = trippleEnc.encrypt(fieldDetailCashierPassword.getText());
                String newDisplayName = fieldDetailCashierName.getText().trim();
                
                if (!newDisplayName.isEmpty() && !newPassword.isEmpty()) {
                    PreparedStatement ps = connection.prepareStatement("UPDATE kv_users SET password = ?, display_name = ? WHERE user_id = ?");
                    ps.setString(1, newPassword);
                    ps.setString(2, newDisplayName);
                    ps.setString(3, id);
                    ps.execute();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data profil kasir telah ter-update!");
                    alert.setHeaderText(null);
                    alert.showAndWait();

                    renderCashierDetail(id, username, newPassword, newDisplayName, isBlocked);
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon masukkan data dengan benar!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                alert.setHeaderText(null);
                alert.showAndWait();
                e.printStackTrace();
            }
        });
        
        fieldDetailCashierStatus.setOnAction((event) -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, isBlocked 
                            ? "Apa anda yakin ingin mengaktifkan akses untuk akun " + displayName + " ?"
                            : "Apa anda yakin ingin me-nonaktifkan akses untuk akun " + displayName + " ?");
            confirm.setHeaderText(null);
            Optional<ButtonType> result = confirm.showAndWait();
            
            if (result.get() == ButtonType.OK) {
                try {
                    boolean newStatus = !isBlocked;
                    PreparedStatement ps = connection.prepareStatement("UPDATE kv_users SET is_blocked = ? WHERE user_id = ?");
                    ps.setBoolean(1, newStatus);
                    ps.setString(2, id);
                    ps.execute();

                    renderCashierDetail(id, username, encryptedPassword, displayName, newStatus);
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void renderProducts() throws IOException{
        paneProduct.toFront();
        navProduct.setStyle("-fx-background-color : #343541");
        fieldAddProductCode.setText("");
        fieldAddProductName.setText("");
        fieldAddProductCategory.getSelectionModel().clearSelection();
        fieldAddProductStock.setText("");
        fieldAddProductPrice.setText("");
        productItems.getChildren().clear();

        try {
            PreparedStatement psCategories = connection.prepareStatement("SELECT * FROM kview_categories WHERE is_deleted = 0");
            ResultSet rsCategories = psCategories.executeQuery();
            
            fieldAddProductCategory.setCellFactory(lv -> new CategoriesListCell());
            fieldAddProductCategory.setButtonCell(new CategoriesListCell());
            fieldAddProductCategory.getItems().clear();
            while(rsCategories.next()){
                int id = rsCategories.getInt("category_id");
                String name = rsCategories.getString("category_name");
                fieldAddProductCategory.getItems().add(new Categories(id, name));
            }
            
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM kview_products WHERE is_deleted = 0");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Node listItem = FXMLLoader.load(getClass().getResource(productList));

                Label labelCode = (Label) listItem.lookup("#productCode");
                Label labelName = (Label) listItem.lookup("#productName");
                Label labelCategory = (Label) listItem.lookup("#productCategory");
                Label labelStock = (Label) listItem.lookup("#productStock");
                Label labelPrice = (Label) listItem.lookup("#productPrice");

                String productCode = rs.getString("product_id");
                String productName = rs.getString("product_name");
                int productCategoryId = rs.getInt("category_id");
                String productCategoryName = rs.getString("category_name");
                int productStock = rs.getInt("product_stock");
                int productPrice = rs.getInt("product_price");

                labelCode.setText(productCode);
                labelName.setText(productName);
                labelCategory.setText(productCategoryName);
                labelStock.setText(Integer.toString(productStock));
                labelPrice.setText("Rp " + productPrice);

                listItem.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                    listItem.setStyle("-fx-background-color : #3e404c; -fx-cursor : hand;");
                });
                listItem.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                    listItem.setStyle("-fx-background-color : #4A4C5A");
                });
                listItem.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    renderProductDetail(productCode, productName, productCategoryId, productStock, productPrice);
                });
                
                productItems.getChildren().add(listItem);
            }
            
        } catch (IOException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    public void renderProductDetail(String productCode, String productName, int productCategoryId, int productStock, int productPrice){
        paneProductDetail.toFront();
        detailProductName.setText(productName);
        
        try {
            PreparedStatement psCategories = connection.prepareStatement("SELECT * FROM kview_categories WHERE is_deleted = 0");
            ResultSet rsCategories = psCategories.executeQuery();
            
            fieldDetailProductCategory.setCellFactory(lv -> new CategoriesListCell());
            fieldDetailProductCategory.setButtonCell(new CategoriesListCell());
            fieldDetailProductCategory.getItems().clear();
            
            int selectedValue = -1;
            int i = 0;
            while(rsCategories.next()){
                int id = rsCategories.getInt("category_id");
                String name = rsCategories.getString("category_name");
                fieldDetailProductCategory.getItems().add(new Categories(id, name));
                if(productCategoryId == id){
                    selectedValue = i;
                }
                i++;
            }

            fieldDetailProductCode.setText(productCode);
            fieldDetailProductName.setText(productName);
            if (selectedValue > -1) {
                fieldDetailProductCategory.getSelectionModel().select(selectedValue);
            }else{
                fieldDetailProductCategory.getSelectionModel().clearSelection();
            }
            fieldDetailProductStock.setText(Integer.toString(productStock));
            fieldDetailProductPrice.setText(Integer.toString(productPrice));


            fieldDetailProductSubmit.setOnAction((event) -> {
                try {
                    String newProductCode = fieldDetailProductCode.getText().trim();
                    String newProductName = fieldDetailProductName.getText().trim();
                    String newProductStock = fieldDetailProductStock.getText().trim();
                    String newProductPrice = fieldDetailProductPrice.getText().trim();

                    if (!newProductCode.isEmpty() && !newProductName.isEmpty() && !fieldDetailProductCategory.getSelectionModel().isEmpty() && !newProductStock.isEmpty() && !newProductPrice.isEmpty()) {
                        int newProductCategory = ((Categories) fieldDetailProductCategory.getValue()).getId();
                        PreparedStatement ps = connection.prepareStatement("UPDATE kv_products SET product_id = ?, product_name = ?, category_id = ?, product_stock = ?, product_price = ? WHERE product_id = ?");
                        ps.setString(1, newProductCode);
                        ps.setString(2, newProductName);
                        ps.setInt(3, newProductCategory);
                        ps.setInt(4, Integer.parseInt(newProductStock));
                        ps.setInt(5, Integer.parseInt(newProductPrice));
                        ps.setString(6, productCode);
                        ps.execute();
                        
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data produk telah ter-update!");
                        alert.setHeaderText(null);
                        alert.showAndWait();

                        renderProductDetail(newProductCode, newProductName, newProductCategory, Integer.parseInt(newProductStock), Integer.parseInt(newProductPrice));
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon masukkan data dengan benar!");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    e.printStackTrace();
                }
            });
            
            fieldDetailProductDelete.setOnAction((event) -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Apa anda yakin ingin menghapus produk " + productName + " ?");
                confirm.setHeaderText(null);
                Optional<ButtonType> result = confirm.showAndWait();

                if (result.get() == ButtonType.OK) {
                    try {
                        PreparedStatement ps = connection.prepareStatement("UPDATE kv_products SET is_deleted = ? WHERE product_id = ?");
                        ps.setBoolean(1, true);
                        ps.setString(2, productCode);
                        ps.execute();

                        renderProducts();
                    } catch (IOException | SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                        e.printStackTrace();
                    }
                }
            });
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    public void renderCategories() throws IOException{
        paneCategories.toFront();
        navCategories.setStyle("-fx-background-color : #343541");
        fieldAddCategoryName.setText("");
        categoriesItems.getChildren().clear();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM kview_categories WHERE is_deleted = 0");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Node listItem = FXMLLoader.load(getClass().getResource(categoryList));

                Label labelName = (Label) listItem.lookup("#categoryName");
                Label labelProduct = (Label) listItem.lookup("#categoryProduct");

                int categoryId = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");
                int categoryProduct = rs.getInt("total_product");

                labelName.setText(categoryName);
                labelProduct.setText(Integer.toString(categoryProduct));

                listItem.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                    listItem.setStyle("-fx-background-color : #3e404c; -fx-cursor : hand;");
                });
                listItem.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                    listItem.setStyle("-fx-background-color : #4A4C5A");
                });
                listItem.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    renderCategoryDetail(categoryId, categoryName, categoryProduct);
                });
                
                categoriesItems.getChildren().add(listItem);
            }
            
        } catch (IOException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                alert.setHeaderText(null);
                alert.showAndWait();
                e.printStackTrace();
        }
    }
    
    public void renderCategoryDetail(int categoryId, String categoryName, int categoryProduct){
        paneCategoryDetail.toFront();
        detailCategoryName.setText(categoryName);
        
        fieldDetailCategoryNameHelp.setText(categoryProduct > 0 ? "Terdapat " + categoryProduct + " produk dengan kategori ini." : "Tidak ada produk dengan kategori ini");
        fieldDetailCategoryName.setText(categoryName);

        if (categoryProduct > 0) {
            fieldDetailCategoryOr.setVisible(false);
            fieldDetailCategoryDelete.setVisible(false);
        } else {
            fieldDetailCategoryOr.setVisible(true);
            fieldDetailCategoryDelete.setVisible(true);
        }
        
        fieldDetailCategorySubmit.setOnAction((event) -> {
            try {
                String newCategoryName = fieldDetailCategoryName.getText().trim();

                if (!newCategoryName.isEmpty()) {
                    PreparedStatement ps = connection.prepareStatement("UPDATE kv_categories SET category_name = ? WHERE category_id = ?");
                    ps.setString(1, newCategoryName);
                    ps.setInt(2, categoryId);
                    ps.execute();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nama kategori telah ter-update!");
                    alert.setHeaderText(null);
                    alert.showAndWait();

                    renderCategoryDetail(categoryId, newCategoryName, categoryProduct);
                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon masukkan data dengan benar!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                alert.setHeaderText(null);
                alert.showAndWait();
                e.printStackTrace();
            }
        });
        
        fieldDetailCategoryDelete.setOnAction((event) -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Apa anda yakin ingin menghapus kategori " + categoryName + " ?");
            confirm.setHeaderText(null);
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    PreparedStatement ps = connection.prepareStatement("UPDATE kv_categories SET is_deleted = ? WHERE category_id = ?");
                    ps.setBoolean(1, true);
                    ps.setInt(2, categoryId);
                    ps.execute();

                    renderCategories();
                } catch (IOException | SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void renderHistory(LocalDate start, LocalDate end){
        paneHistory.toFront();
        navHistory.setStyle("-fx-background-color : #343541");
        historyItems.getChildren().clear();

        try {
            boolean filter = start != null && end != null;
            String sql = filter 
                    ? "SELECT * FROM kview_transactions WHERE datetime >= ? AND datetime <= ? ORDER BY transaction_id DESC"
                    : "SELECT * FROM kview_transactions ORDER BY transaction_id DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            if (filter) {
                ps.setString(1, start.toString());
                ps.setString(2, end.toString());
            }
            
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Node listItem = FXMLLoader.load(getClass().getResource(historyList));

                Label labelId = (Label) listItem.lookup("#historyId");
                Label labelDateTime = (Label) listItem.lookup("#historyDateTime");
                Label labelTotal = (Label) listItem.lookup("#historyTotal");

                String customId = rs.getString("custom_id");
                int transactionId = rs.getInt("transaction_id");
                String cashierName = rs.getString("display_name");
                String datetime = rs.getString("datetime");
                int total = rs.getInt("total");
                int customerMoney = rs.getInt("customer_money");
                int changeMoney = rs.getInt("change_money");

                labelId.setText("#" + customId);
                labelDateTime.setText(datetime);
                labelTotal.setText("Rp " + total);

                listItem.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                    listItem.setStyle("-fx-background-color : #3e404c; -fx-cursor : hand;");
                });
                listItem.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                    listItem.setStyle("-fx-background-color : #4A4C5A");
                });
                listItem.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    renderHistoryDetail(transactionId, customId, datetime, total, customerMoney, changeMoney, cashierName);
                });
                
                historyItems.getChildren().add(listItem);
            }

        } catch (IOException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    public void filterHistory(){
        if (historyStart != null && historyEnd != null) {
            renderHistory(historyStart, historyEnd);
        }
    }
    
    public void renderHistoryDetail(int transactionId, String customId, String datetime, int total, int customerMoney, int changeMoney, String cashierName){
        paneHistoryDetail.toFront();
        historyDetailItems.getChildren().clear();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM kview_transactions_detail WHERE transaction_id = ? ORDER BY detail_id DESC");
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            
            historyDetailId.setText("#" + customId);
            historyDetailDateTime.setText(cashierName + "       " + datetime);

            while(rs.next()){
                Node listItem = FXMLLoader.load(getClass().getResource(staticProductList));

                Label labelName = (Label) listItem.lookup("#productName");
                Label labelQty = (Label) listItem.lookup("#productQty");
                Label labelPrice = (Label) listItem.lookup("#productPrice");
                Label labelSubtotal = (Label) listItem.lookup("#productSubtotal");

                String productName = rs.getString("product_name");
                String productQty = Integer.toString(rs.getInt("product_qty"));
                String productPrice = Integer.toString(rs.getInt("product_price"));
                String productSubtotal = Integer.toString(rs.getInt("product_subtotal"));

                labelName.setText(productName);
                labelQty.setText(productQty);
                labelPrice.setText("Rp " + productPrice);
                labelSubtotal.setText("Rp " + productSubtotal);
                
                historyDetailItems.getChildren().add(listItem);
            }
            
            historyDetailTotal.setText("Rp " + total);
            historyDetailCustomerMoney.setText("Rp " + customerMoney);
            historyDetailChangeMoney.setText("Rp " + changeMoney);

        } catch (IOException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
            alert.setHeaderText(null);
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    
    public void renderPage(String page, ActionEvent actionEvent) throws IOException{
        navCashier.setStyle("-fx-background-color : transparent");
        navProduct.setStyle("-fx-background-color : transparent");
        navCategories.setStyle("-fx-background-color : transparent");
        navHistory.setStyle("-fx-background-color : transparent");
        
        if ("navCashier".equals(((Control)actionEvent.getSource()).getId())) {
            renderCashiers();
        }
        if ("navProduct".equals(((Control)actionEvent.getSource()).getId())) {
            renderProducts();
        }
        if ("navCategories".equals(((Control)actionEvent.getSource()).getId())) {
            renderCategories();
        }
        if ("navHistory".equals(((Control)actionEvent.getSource()).getId())) {
            renderHistory(null,null);
        }
        if ("signOut".equals(((Control)actionEvent.getSource()).getId())) {
            Parent root = FXMLLoader.load(getClass().getResource("/Auth/Login.fxml"));
        
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            
            stage.getScene().setRoot(root);
        }
    }
    
    public void renderCashierPage(ActionEvent actionEvent) throws IOException {
        renderPage("navCashier", actionEvent);
    }
    
    public void renderProductPage(ActionEvent actionEvent) throws IOException {
        renderPage("navProduct", actionEvent);
    }
    
    public void renderCategoriesPage(ActionEvent actionEvent) throws IOException {
        renderPage("navCategories", actionEvent);
    }
    
    public void renderHistoryPage(ActionEvent actionEvent) throws IOException {
        renderPage("navHistory", actionEvent);
    }
    
    public void signOut(ActionEvent actionEvent) throws IOException {
        renderPage("signOut", actionEvent);
    }
    
    public void handleNav(ActionEvent actionEvent) throws IOException {
        renderPage(((Control)actionEvent.getSource()).getId(), actionEvent);
    }
}
