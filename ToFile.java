package com.mycompany.ecommerceproject;

import java.util.ArrayList;
import java.io.*;

public class ToFile {
    private static final String FILENAME = "ecommerce_data.bin";

    public void saveData(ArrayList<?> dataList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            // Create a container object to hold all data
            DataContainer container = new DataContainer();
            
            // Add all static lists to container
            container.products = new ArrayList<>(Product.products);
            container.customers = new ArrayList<>(Customer.ListC);
            container.sellers = new ArrayList<>(Seller.sellerList);
            container.suppliers = new ArrayList<>(Supplier.supplier);
            container.orders = Order.getOrderList();
            
            // Save the container
            oos.writeObject(container);
            System.out.println("Data saved successfully!");
            
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            // Read the container object
            DataContainer container = (DataContainer) ois.readObject();
            
            // Restore all static lists
            Product.products = new ArrayList<>(container.products);
            Customer.ListC = new ArrayList<>(container.customers);
            Seller.sellerList = new ArrayList<>(container.sellers);
            Supplier.supplier = new ArrayList<>(container.suppliers);
            Order.restoreOrders(container.orders);
            
            System.out.println("Data loaded successfully!");
            
        } catch (FileNotFoundException e) {
            System.out.println("No saved data found.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

// Container class to hold all serializable data
class DataContainer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    ArrayList<Product> products;
    ArrayList<Customer> customers;
    ArrayList<Seller> sellers;
    ArrayList<Supplier> suppliers;
    ArrayList<Order> orders;
}