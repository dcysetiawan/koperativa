/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cashier;

import Helpers.MySQLConnection;
import LocalStorage.CashierOrder;
import Helpers.NumberField;
import LocalStorage.UserSession;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author DSetiawan
 */
public class MainController implements Initializable {
    @FXML
    private VBox orderItems;
    @FXML
    private VBox orderResultItems;
    @FXML
    private VBox productItems;
    @FXML
    private VBox historyItems;
    @FXML
    private VBox historyDetailItems;
    
    @FXML
    private JFXButton navOrder;
    @FXML
    private JFXButton navProduct;
    @FXML
    private JFXButton navHistory;

    @FXML
    private AnchorPane paneOrder;
    @FXML
    private HBox orderListHeader;
    @FXML
    private HBox orderTotalContainer;
    @FXML
    private TextField fieldAddCode;
    @FXML
    private TextField fieldAddQty;
    @FXML
    private Label orderTotal;

    @FXML
    private AnchorPane paneOrderPay;
    @FXML
    private Label orderPayTotal;
    @FXML
    private TextField fieldPay;

    @FXML
    private AnchorPane paneOrderResult;
    @FXML
    private Label orderResultTotal;
    @FXML
    private Label orderResultCustomerMoney;
    @FXML
    private Label orderResultChangeMoney;

    @FXML
    private AnchorPane paneProduct;
    @FXML
    private TextField searchProduct;
    
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
    
    private final String staticProductList = "/Cashier/StaticProductList.fxml";
    private final String editableProductList = "/Cashier/EditableProductList.fxml";
    private final String productList = "/Cashier/ProductList.fxml";
    private final String historyList = "/Cashier/HistoryList.fxml";
    
    private ArrayList<CashierOrder> items = new ArrayList<CashierOrder>();
    
    Connection connection;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserSession.authorizeUser();
        
        MySQLConnection connection = new MySQLConnection();
        connection.connect();
        this.connection = connection.connection;
        
        paneOrder.toFront();
        
        try {
            renderOrderProducts();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Hide component
        orderListHeader.setVisible(false);
        orderTotalContainer.setVisible(false);
        
        // Number Formatting
        new NumberField(fieldAddCode);
        new NumberField(fieldAddQty);
        new NumberField(fieldPay);
        
        // Search Product
        searchProduct.textProperty().addListener((obs, oldValue, newValue) -> {
            for (Node listItem : productItems.getChildren()) {
                String productCode = ((Label) listItem.lookup("#productCode")).getText().toLowerCase().trim();
                String productName = ((Label) listItem.lookup("#productName")).getText().toLowerCase().trim();
                String productCategory = ((Label) listItem.lookup("#productCategory")).getText().toLowerCase().trim();
                if(productCode.contains(newValue.toLowerCase().trim())||productName.contains(newValue.toLowerCase().trim())||productCategory.contains(newValue.toLowerCase().trim())){
                    listItem.setVisible(true);
                    listItem.setManaged(true);
                }else{
                    listItem.setVisible(false);
                    listItem.setManaged(false);
                }
            }
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
    
    public void renderOrderProducts() throws IOException{
        int total = 0;
        orderItems.getChildren().clear();
        
        if (items.size()>0) {
            orderListHeader.setVisible(true);
            orderTotalContainer.setVisible(true);

            for (int i = items.size()-1; i >= 0 ; i--) {
                CashierOrder item = items.get(i);
                
                Node listItem = FXMLLoader.load(getClass().getResource(editableProductList));

                Label labelName = (Label) listItem.lookup("#productName");
                Label labelQty = (Label) listItem.lookup("#productQty");
                Label labelPrice = (Label) listItem.lookup("#productPrice");
                Label labelSubtotal = (Label) listItem.lookup("#productSubtotal");
                JFXButton buttonTrash = (JFXButton) listItem.lookup("#productTrash");

                labelName.setText(item.productName);
                labelQty.setText(Integer.toString(item.productQty));
                labelPrice.setText("Rp "+Integer.toString(item.productPrice));
                labelSubtotal.setText("Rp "+Integer.toString(item.productSubtotal));
                
                final int j = i;
                buttonTrash.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    try {
                        handleRemoveProduct(j);
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                
                orderItems.getChildren().add(listItem);

                total+=item.productSubtotal;
            }

            CashierOrder.setTotal(total);
            orderTotal.setText("Rp "+Integer.toString(total));
            orderPayTotal.setText("Total     Rp "+Integer.toString(total));
            
        }else{
            orderListHeader.setVisible(false);
            orderTotalContainer.setVisible(false);
        }
    }
    
    public void handleRemoveProduct(int index) throws IOException{
        items.remove(index);
        renderOrderProducts();
    }
    
    public void handleAddProduct(ActionEvent actionEvent) throws IOException{
        String productCode = fieldAddCode.getText().toLowerCase().trim();
        String productQtyString = fieldAddQty.getText().trim();
        
        if (!productCode.isEmpty() && !productQtyString.isEmpty()) {
            int productQty = Integer.parseInt(productQtyString);
            
            if (productQty > 0) {
                boolean exists = false;
                for (int i = 0; i < items.size(); i++) {
                    CashierOrder item = items.get(i);
                    if (item.productCode.equals(productCode)) {
                        items.set(i, new CashierOrder(item.productCode, item.productName, item.productQty + productQty, item.productPrice));
                        fieldAddCode.setText("");
                        fieldAddQty.setText("");

                        renderOrderProducts();
                        exists = true;
                    }
                }
                
                if (!exists) {
                    try {
                        PreparedStatement ps = connection.prepareStatement("SELECT * FROM kview_products WHERE product_id = ? AND is_deleted = 0");
                        ps.setString(1, productCode);
                        ResultSet rs = ps.executeQuery();

                        if(rs.next()) {
                            String productName = rs.getString("product_name");
                            int productStock = rs.getInt("product_stock");
                            int productPrice = rs.getInt("product_price");

                            if (productQty > productStock){
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Stok produk kurang dari kuantitas produk yang dibeli!");
                                alert.setHeaderText(null);
                                alert.showAndWait();
                                fieldAddQty.setText(Integer.toString(productStock));
                            }else{
                                items.add(new CashierOrder(productCode, productName, productQty, productPrice));

                                fieldAddCode.setText("");
                                fieldAddQty.setText("");

                                renderOrderProducts();
                            }
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Produk tidak ditemukan!");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                        }
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon masukkan data dengan benar!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon masukkan data dengan benar!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }
        
    public void customerPayment(ActionEvent actionEvent) throws IOException{
        String customerMoneyString = fieldPay.getText();
        int total = CashierOrder.getTotal();
        
        if (!customerMoneyString.isEmpty()) {
            int customerMoney = Integer.parseInt(customerMoneyString);
            
            if (customerMoney >= total) {
                CashierOrder.setCustomerMoney(customerMoney);

                try {
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO kv_transactions SET user_id = ?, total = ?, customer_money = ?, change_money = ?", Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, UserSession.getId());
                    ps.setInt(2, total);
                    ps.setInt(3, customerMoney);
                    ps.setInt(4, CashierOrder.getChangeMoney());
                    ps.executeUpdate();
                    
                    ResultSet rs = ps.getGeneratedKeys();
                    
                    if(rs.next()){
                        int insertedId = rs.getInt(1);
                        
                        for (int i = 0; i < items.size(); i++) {
                            CashierOrder item = items.get(i);
                            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO kv_transactions_detail SET transaction_id = ?, product_name = ?, product_qty = ?, product_price = ?");
                            ps2.setInt(1, insertedId);
                            ps2.setString(2, item.productName);
                            ps2.setInt(3, item.productQty);
                            ps2.setInt(4, item.productPrice);
                            ps2.execute();
                            
                            PreparedStatement ps3 = connection.prepareStatement("UPDATE kv_products SET product_stock = product_stock - ? WHERE product_id = ?");
                            ps3.setInt(1, item.productQty);
                            ps3.setString(2, item.productCode);
                            ps3.execute();
                        }

                        fieldPay.setText("");

                        renderPage("navOrderResult", actionEvent);
                        
                    }else{
                        throw new Error("I Dont Know");
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    e.printStackTrace();
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon isi nilai dengan benar!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon isi nilai dengan benar!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }
    
    public void renderOrderResult() throws IOException{
        orderResultItems.getChildren().clear();

        for (int i = items.size()-1; i >= 0 ; i--) {
            CashierOrder item = items.get(i);

            Node listItem = FXMLLoader.load(getClass().getResource(staticProductList));

            Label labelName = (Label) listItem.lookup("#productName");
            Label labelQty = (Label) listItem.lookup("#productQty");
            Label labelPrice = (Label) listItem.lookup("#productPrice");
            Label labelSubtotal = (Label) listItem.lookup("#productSubtotal");

            labelName.setText(item.productName);
            labelQty.setText(Integer.toString(item.productQty));
            labelPrice.setText("Rp "+Integer.toString(item.productPrice));
            labelSubtotal.setText("Rp "+Integer.toString(item.productSubtotal));

            orderResultItems.getChildren().add(listItem);
        }

        int total = CashierOrder.getTotal();
        int customerMoney = CashierOrder.getCustomerMoney();
        int changeMoney = CashierOrder.getChangeMoney();
        
        orderResultTotal.setText("Rp "+Integer.toString(total));
        orderResultCustomerMoney.setText("Rp "+Integer.toString(customerMoney));
        orderResultChangeMoney.setText("Rp "+Integer.toString(changeMoney));
        
        items = new ArrayList<CashierOrder>();
    }
    
    public void renderProducts() throws IOException{
        productItems.getChildren().clear();

        try {
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
                String productCategory = rs.getString("category_name");
                String productStock = Integer.toString(rs.getInt("product_stock"));
                String productPrice = Integer.toString(rs.getInt("product_price"));

                labelCode.setText(productCode);
                labelName.setText(productName);
                labelCategory.setText(productCategory);
                labelStock.setText(productStock);
                labelPrice.setText("Rp " + productPrice);

                productItems.getChildren().add(listItem);
            }
            
        } catch (IOException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                alert.setHeaderText(null);
                alert.showAndWait();
        }
    }
    
    public void renderHistory(LocalDate start, LocalDate end){
        historyItems.getChildren().clear();

        try {
            boolean filter = start != null && end != null;
            String sql = filter 
                    ? "SELECT * FROM kview_transactions WHERE user_id = ? AND datetime >= ? AND datetime <= ? ORDER BY transaction_id DESC"
                    : "SELECT * FROM kview_transactions WHERE user_id = ? ORDER BY transaction_id DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            
            ps.setString(1, UserSession.getId());
            if (filter) {
                ps.setString(2, start.toString());
                ps.setString(3, end.toString());
            }
            
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Node listItem = FXMLLoader.load(getClass().getResource(historyList));

                Label labelId = (Label) listItem.lookup("#historyId");
                Label labelDateTime = (Label) listItem.lookup("#historyDateTime");
                Label labelTotal = (Label) listItem.lookup("#historyTotal");

                String customId = rs.getString("custom_id");
                int transactionId = rs.getInt("transaction_id");
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
                    renderHistoryDetail(transactionId, customId, datetime, total, customerMoney, changeMoney);
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
    
    public void renderHistoryDetail(int transactionId, String customId, String datetime, int total, int customerMoney, int changeMoney){
        paneHistoryDetail.toFront();
        historyDetailItems.getChildren().clear();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM kview_transactions_detail WHERE transaction_id = ? ORDER BY detail_id DESC");
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            
            historyDetailId.setText("#" + customId);
            historyDetailDateTime.setText(datetime);

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
        navProduct.getStyleClass().remove("active");
        navHistory.getStyleClass().remove("active");
        
        if ("navOrder".equals(page)) {
            paneOrder.toFront();
            renderOrderProducts();
        }
        if ("navOrderPay".equals(page)) {
            paneOrderPay.toFront();
        }
        if ("navOrderResult".equals(page)) {
            paneOrderResult.toFront();
            renderOrderResult();
        }
        if ("navProduct".equals(page)) {
            paneProduct.toFront();
            navProduct.getStyleClass().add("active");
            renderProducts();
        }
        if ("navHistory".equals(page)) {
            paneHistory.toFront();
            navHistory.getStyleClass().add("active");
            renderHistory(null,null);
        }
        
        if ("signOut".equals(((Control)actionEvent.getSource()).getId())) {
            UserSession.removeUser();
            
            Parent root = FXMLLoader.load(getClass().getResource("/Auth/Login.fxml"));
        
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            
            stage.getScene().setRoot(root);
        }
    }
    
    public void renderOrderPage(ActionEvent actionEvent) throws IOException {
        renderPage("navOrder", actionEvent);
    }
    
    public void renderOrderPayPage(ActionEvent actionEvent) throws IOException {
        renderPage("navOrderPay", actionEvent);
    }
    
    public void renderOrderResultPage(ActionEvent actionEvent) throws IOException {
        renderPage("navOrderResult", actionEvent);
    }
    
    public void renderProductPage(ActionEvent actionEvent) throws IOException {
        renderPage("navProduct", actionEvent);
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
