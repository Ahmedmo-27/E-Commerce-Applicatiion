package com.mycompany.ecommerceproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class Product_Service implements Serializable 
{
    private ArrayList<Product> products;
    private static Product_Service instance;

    private Product_Service() {
        products = new ArrayList<>();
    }

    public static Product_Service getInstance() {
        if (instance == null) {
            instance = new Product_Service();
        }
        return instance;
    }

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            System.out.println("Cannot add a null product.");
            return;
        }
        Product existingProduct = findProductByID(product.ProductId);
        if (existingProduct != null) {
            existingProduct.TotalStock += quantity;
            System.out.println("Added " + quantity + " units to product: " + existingProduct.Name);
        } else {
            product.TotalStock = quantity;
            products.add(product);
            System.out.println("New product added: " + product.Name);
        }
    }

    public Product findProductByID(int productId) {
        for (Product product : products) {
            if (product.getProductId() == productId) {
                return product;
            }
        }
        return null;
    }

    public void updateStock(Product product, int quantity) {
        if (product == null) {
            System.out.println("Cannot update stock for a null product.");
            return;
        }
        Product existingProduct = findProductByID(product.ProductId);
        if (existingProduct != null) {
            existingProduct.TotalStock = quantity;
            System.out.println("Stock updated for product: " + existingProduct.Name + " to " + quantity);
        } else {
            System.out.println("Product not found.");
        }
    }

    public void removeItem(Product product) {
        if (product == null) {
            System.out.println("Invalid product.");
            return;
        }
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            Product item = iterator.next();
            if (item.ProductId == product.ProductId) {
                iterator.remove();
                System.out.println("Product " + item.Name + " removed.");
                return;
            }
        }
        System.out.println("Product not found.");
    }

    public boolean SellItems(int productId, int quantity) {
        Product product = findProductByID(productId);
        if (product != null) {
            if (product.TotalStock >= quantity) {
                product.TotalStock -= quantity;
                product.UnitsSold += quantity;
                System.out.println("Sold " + quantity + " units of product: " + product.Name);
                return true;
            } else {
                System.out.println("Not enough stock. Only " + product.TotalStock + " units available.");
            }
        } else {
            System.out.println("Product not found.");
        }
        return false;
    }
}
