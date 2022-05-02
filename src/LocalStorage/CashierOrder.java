/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LocalStorage;

import java.util.ArrayList;

/**
 *
 * @author DSetiawan
 */
public class CashierOrder {
    public String productCode;
    public String productName;
    public int productQty;
    public int productPrice;
    public int productSubtotal;
    public static int total = 0;
    public static int customerMoney = 0;
    public static int changeMoney = 0;

    public CashierOrder(String productCode, String productName, int productQty, int productPrice) {
        this.productCode = productCode;
        this.productName = productName;
        this.productQty = productQty;
        this.productPrice = productPrice;
        this.productSubtotal = productPrice * productQty;
    }
    
    public static void setTotal(int total){
        CashierOrder.total = total;
    }
    
    public static int getTotal(){
        return total;
    }
    
    public static void setCustomerMoney(int customerMoney){
        CashierOrder.customerMoney = customerMoney;
    }
    
    public static int getCustomerMoney(){
        return customerMoney;
    }
    
    public static int getChangeMoney(){
        return CashierOrder.customerMoney - CashierOrder.total;
    }
}
