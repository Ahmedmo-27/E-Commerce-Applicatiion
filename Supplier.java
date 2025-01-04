package com.mycompany.ecommerceproject;
import java.io.Serializable;
import java.util.ArrayList;

public class Supplier implements Serializable {

    private String companyName;
    private ArrayList<Product> productList;
    private double revenue;
    
    protected static ArrayList<Supplier> supplier = new ArrayList<>(); // Initialize the supplier list

    public Supplier()
    {
    }
    
    // Constructor
    public Supplier(String companyName) {
        this.companyName = companyName;
        this.productList = new ArrayList<>();
        this.revenue = 0.0;
        supplier.add(this); // Add this instance to the supplier list
    }

    // Getter for supplier list
    public static ArrayList<Supplier> getSupplier() {
        return supplier;
    }

    // Setter for supplier list
    public static void setSupplier(ArrayList<Supplier> supplier) {
        Supplier.supplier = supplier;
    }

    // Getter for companyName
    public String getCompanyName() {
        return companyName;
    }

    // Setter for companyName
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // Getter for productList
    public ArrayList<Product> getProductList() {
        return this.productList;
    }

    // Setter for productList
    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }

    // Add a product to the product list
    public void addProduct(Product product) {
        this.productList.add(product);
    }

    // Remove a product from the product list
    public void removeProduct(Product product) {
        this.productList.remove(product);
    }

    // Getter for revenue
    public double getRevenue() {
        return this.revenue;
    }

    // Setter for revenue
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    // Increment the revenue by a specified amount
    public void incrementRevenue(double additionalRevenue) {
        this.revenue += additionalRevenue;
    }

    // Get or create a Supplier by company name
    public static Supplier getOrCreate(String companyName) {
        for (Supplier supplier : Supplier.getSupplier()) {
            if (supplier.getCompanyName().equalsIgnoreCase(companyName)) {
                return supplier;
            }
        }
        return new Supplier(companyName);
    }

    // Remove a supplier by company name
    public static boolean remove(String companyName) {
        for (int i = 0; i < supplier.size(); i++) {
            if (supplier.get(i).companyName.equalsIgnoreCase(companyName)) {
                supplier.remove(i);
                System.out.println("Supplier with company name: " + companyName + " has been removed successfully.");
                return true;
            }
        }
        System.out.println("Supplier not found with company name: " + companyName);
        return false;
    }

    // Override toString method
    @Override
    public String toString() {
        return "Supplier: " + "companyName: " + companyName + ", revenue: " + revenue + ", productList: " + productList;
    }
}
