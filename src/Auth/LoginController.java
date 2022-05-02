/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auth;

import Helpers.MySQLConnection;
import Helpers.Password;
import LocalStorage.UserSession;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author DSetiawan
 */
public class LoginController implements Initializable {
    @FXML
    private JFXTextField usernameField;
    
    @FXML
    private JFXPasswordField passwordField;
    
    Connection connection;
    
    Password trippleEnc;

    public LoginController() throws Exception {
        this.trippleEnc = new Password();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MySQLConnection connection = new MySQLConnection();
        connection.connect();
        this.connection = connection.connection;
    }  
    
    @FXML
    private void handleSignIn(ActionEvent event) throws Exception {
        String resource = null;
        String username = usernameField.getText().toLowerCase().trim();
        String password = passwordField.getText();

        if(!username.isEmpty() && !password.isEmpty()){
            try{
                PreparedStatement ps = connection.prepareStatement("SELECT * FROM kv_users WHERE username = ?");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    String userPassword = rs.getString("password");
                    
                    if(password.equals(trippleEnc.decrypt(userPassword))){
                        String userId = rs.getString("user_id");
                        String userName = rs.getString("username");
                        String userDisplayName = rs.getString("display_name");
                        String userRole = rs.getString("role_id");
                        boolean isBlocked = rs.getBoolean("is_blocked");

                        if(!isBlocked){
                            UserSession.setUser(userId, userName, userDisplayName, userRole);

                            if(userRole.equals("1")){
                                resource = "/Admin/Main.fxml";
                            } else{
                                resource = "/Cashier/Main.fxml";
                            }

                            Parent root = FXMLLoader.load(getClass().getResource(resource));

                            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

                            stage.getScene().setRoot(root);
                            
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Akun ini telah dinonaktifkan, hubungi admin untuk info lebih lanjut!");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                        }

                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Password tidak cocok dengan username tersebut!");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }

                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Username tidak ditemukan!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                }
                
            }catch(IOException | SQLException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Maaf sepertinya ada yang salah dengan sistem kami, tunggu beberapa saat dan coba lagi!");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    e.printStackTrace();
            }
            
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Mohon masukkan username dan password dengan benar!");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }    
}
