package com.mycompany.ecommerceproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Order implements Serializable
{
    protected int orderId;
    protected ArrayList<Product> productlist = new ArrayList();
    private ArrayList<Integer> quantities;
    public static ArrayList<Order>OrderList=new ArrayList();
    public static double totalAmount;
    protected String status;

    public Order() 
    {
    }
    
    public Order(int orderId,ArrayList<Product> product, double totalAmount, String status) {
        this.orderId = orderId;
        this.productlist = product;
        this.totalAmount = totalAmount;
        this.status = status;
    }
        public Order fromCart(Cart cart) 
        {
        int orderId = (int) (System.currentTimeMillis() % 100000);
        ArrayList<Product> productList = new ArrayList<>(cart.productlist);
        double totalAmount = cart.calculateTotalAmount();
        return new Order(orderId, productList, totalAmount, "Pending");
        }
    
    public double calculateTotalAmount(){
        double total = 0;
        for (int i = 0; i < productlist.size(); i++) {
            Product product = productlist.get(i);
            int quantity = quantities.get(i);
            total += product.getPrice() * quantity;
        }
        return total;
    }
    
    public void updateStatus(String Nstatus)
    {
    this.status = Nstatus;
    }
    
    void InsertOrder(Order order)
    {
        Cart cart = new Cart();
        this.OrderList.add(cart.checkout());
    }
    
    
    public void getOrderDetails()
    {
        System.out.println(" Current order ID : " + orderId);
        System.out.println( "products : " );
        
        for(Product product : productlist)
        {
            System.out.println("** " + product.getName() + " " + product.getPrice()+"$");
        }
        
        System.out.println("Total amount : " + totalAmount+"$");
        System.out.println("Order status : " + status);
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public int getOrderId() {
        return orderId;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productlist = productList;
    }

    public ArrayList<Product> getProductList() {
        return productlist;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public static ArrayList<Order> getOrderList() {
        return OrderList;
    }

    public static void restoreOrders(ArrayList<Order> orders) 
    {
        if (orders != null) 
        {
            OrderList = new ArrayList<>(orders);
            System.out.println("Orders restored successfully.");
        } 
        
        else 
        {
            System.out.println("No orders to restore.");
        }
    }
    
}