/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LocalStorage;

import javafx.application.Platform;

/**
 *
 * @author DSetiawan
 */
public class UserSession {
    private static String userId;
    private static String userName;
    private static String userDisplayName;
    private static String userRole;
    
    public static void setUser(String userId, String userName, String userDisplayName, String userRole){
        UserSession.userId = userId;
        UserSession.userName = userName;
        UserSession.userDisplayName = userDisplayName;
        UserSession.userRole = userRole;
    }
    
    public static void authorizeUser(){
        if (userId.isEmpty()) {
            Platform.exit();
            System.exit(0);   
        }
    }
    
    public static void removeUser(){
        UserSession.userId = null;
        UserSession.userName = null;
        UserSession.userDisplayName = null;
        UserSession.userRole = null;
    }
    
    public static String getId(){
        return userId;
    }
    
    public static String getName(){
        return userName;
    }
    
    public static String getDisplayName(){
        return userDisplayName;
    }
    
    public static String getRole(){
        return userRole;
    }
}
