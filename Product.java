package com.mycompany.ecommerceproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable 
{
    public int ProductId;
    protected double price; 
    protected String Name;
    protected int UnitsSold;
    protected int TotalStock;
    protected String sellerUsername;
    protected String SupplierCompanyName;
    private Supplier supplier;
    private Seller seller;
    public static ArrayList<Product>products=new ArrayList<>();
    public ArrayList<Product> sellerproduct = new ArrayList<>();
    private ArrayList<String> feedbackList;
    protected String Category;
    public static double  TotalRevenue = 0.0;
    
    
    public Product()
    {
    
    }

public Product(int ProductId, String Name, int UnitsSold, int TotalStock, double price, String Category, String sellerUsername, String SupplierCompanyName) 
{
    this.ProductId = ProductId;
    this.Name = Name;
    this.UnitsSold = UnitsSold;
    this.TotalStock = TotalStock;
    this.price = price;
    this.Category = Category;

    this.supplier = getSupplier(SupplierCompanyName);
    this.seller = getSeller(sellerUsername);

    if (this.supplier == null) {
        System.out.println("Supplier not found. Using default supplier.");
        this.SupplierCompanyName = "Unknown";
    } else {
        this.SupplierCompanyName = supplier.getCompanyName();
    }

    if (this.seller == null) {
        System.out.println("Seller not found. Using default seller.");
        this.sellerUsername = "Unknown";
    } else {
        this.sellerUsername = seller.getUsername();
    }

    this.feedbackList = new ArrayList<>();
    products.add(this);
    if (this.seller != null) {
        sellerproduct.add(this);
    }
}


public Supplier getSupplier(String SupplierCompanyName) {
    for(Supplier s : Supplier.supplier){
    if(s.getCompanyName().equalsIgnoreCase(SupplierCompanyName)){
        System.out.println("Supplier found");
    return s;
    }
   
    }
    System.out.println("Supplier not found");
     return null;
}


public Seller getSeller(String sellerUsername) {
        for(Seller s : Seller.sellerList){
    if(s.getUsername().equalsIgnoreCase(sellerUsername)){
        System.out.println("Seller found");
    return s;
    }
   
    }
    System.out.println("Seller not found");
     return null;
}

    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getSupplierCompanyName() {
        return SupplierCompanyName;
    }



    public static double CalculateRevenue() {
        
        for (Product item : products) {
            TotalRevenue += item.UnitsSold * item.price;
        }
        return TotalRevenue;
    }


    
    
    public static String getAllProductsAsString() 
    {
        StringBuilder productDetails = new StringBuilder();
        for (Product product : products) {
            productDetails.append(product.toString()).append("\n");
        }
        return productDetails.toString();
    }

    public static ArrayList<Product> getProducts() 
    {
        return products;
    }

    public void setProductId(int ProductId) {
        this.ProductId = ProductId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setUnitsSold(int UnitsSold) {
        this.UnitsSold = UnitsSold;
    }

    public int getUnitsSold() {
        return UnitsSold;
    }

    public void adjustStock(int amount) {
        this.TotalStock += amount;
    }

    public int getTotalStock() 
    {
        return TotalStock;
    }

    public void addFeedback(String feedback) 
    {
        feedbackList.add(feedback);
    }

    public ArrayList<String> getFeedbackList() 
    {
        return feedbackList;
    }

    @Override
    public String toString() 
    {
        return "Product ID: " + ProductId + " | " + Name + " (" + Category + ") - $" + price + " by " + sellerUsername + "  " +"Supplier Company Name: "+SupplierCompanyName;
    }

}
