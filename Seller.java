package com.mycompany.ecommerceproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Seller extends User implements Serializable {


    private ArrayList<Product> productList = new ArrayList<>(); // Instance-specific product list
    private double totalSales;
    protected double revenue;
    public static ArrayList<Seller> sellerList = new ArrayList<>(); // Shared list of all sellers
    
    public Seller()
    {
        super("Seller1","1234");
    }


    public Seller(String username, String password) {
        super(username, password);
        this.revenue = 0.0;
        sellerList.add(this);
    }

    public static void setSellerList(ArrayList<Seller> sellerList) {
        Seller.sellerList = sellerList;
    }

    public static ArrayList<Seller> getSellerList() {
        return sellerList;
    }

        public String getName()
    {
        return Username;
    }
    
    
    public void incrementRevenue(double additionalRevenue) {
        this.revenue += additionalRevenue;
    }

    public double getRevenue() {
        return this.revenue;
    }

    public static Seller Login(String Username,String Password){
    for (int i =0;i<sellerList.size();i++){
        if((Username.equals(sellerList.get(i).Username))&&Password.equals(sellerList.get(i).Password))
            return sellerList.get(i);
    
    }
    return null;
    }

    public static boolean removeSellerByName(String name) {
        for (Seller seller : sellerList) {
            if (seller.getUsername().equalsIgnoreCase(name)) {
                sellerList.remove(seller);
                System.out.println("Seller has been removed successfully.");
                return true;
            }
        }
        System.out.println("Seller not found.");
        return false;
    }

    public ArrayList<Product> getProductList() {
        return new ArrayList<>(productList); // Return a copy to ensure encapsulation
    }

    public double getTotalSales() {
        return totalSales;
    }

    public void addProduct(Product product) {
        productList.add(product);
        System.out.println("Product added successfully: " + product.getProductId());
    }

    public void removeProduct(Product product) {
        productList.remove(product);
        System.out.println("Product removed successfully: " + product.getProductId());
    }

   public Product getTopSellingProduct() {
    if (productList.isEmpty()) {
        System.out.println("No products available in the seller's product list.");
        return null;
    }

    Product topSellingProduct = null;
    int maxUnitsSold = 0;

    for (Product p : productList) {
        if (p.getUnitsSold() > maxUnitsSold) {
            maxUnitsSold = p.getUnitsSold();
            topSellingProduct = p;
        }
    }

    if (topSellingProduct != null) {
        System.out.println("Top Selling Product: " + topSellingProduct.getName() +
                " (ID: " + topSellingProduct.getProductId() +
                ") with Units Sold: " + topSellingProduct.getUnitsSold());
    }

    return topSellingProduct;
}





   public double calculateTotalAmount() {
    double total = 0.0;

    for (Product p : productList) {
        double productRevenue = Double.parseDouble(String.valueOf(p.getUnitsSold())) * Double.parseDouble(String.valueOf(p.getPrice()));
        total += productRevenue;
    }

    System.out.println("Total amount from product sales: " + total);
    return total;
}



    public void viewOrderDetails(Order order) {
        if (order == null) {
            System.out.println("No order found. Cannot view details.");
            return;
        }
        System.out.println("Order Details:");
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Products in Order: " + order.getProductList());
        System.out.println("Total Amount: " + order.getTotalAmount());
    }

    public double calculateAvgRevenue() {
        if (productList.isEmpty()) {
            System.out.println("No products available to calculate average revenue.");
            return 0;
        }

        double totalRevenue = calculateTotalAmount();
        double avgRevenue = totalRevenue / productList.size();
        System.out.println("Average revenue per product: " + avgRevenue);
        return avgRevenue;
    }

    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String toString() {
        return "Seller Username: " + getUsername();
    }
}
