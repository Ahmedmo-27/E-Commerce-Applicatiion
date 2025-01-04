package com.mycompany.ecommerceproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable
{
    protected int cartId;
    protected ArrayList<Product>productlist = new ArrayList();
    protected double totalAmount;  
    private ArrayList<Integer> quantities; // Store quantities for corresponding products
    public Product_Service productService = Product_Service.getInstance();
  
   private boolean isEmpty() 
   {
     return productlist.isEmpty();
   }


    public Cart() {
        this.productlist = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.productService = Product_Service.getInstance();
    
    }

    public Cart(int cartId) {
        this.cartId = cartId;
          this.productlist = new ArrayList<>();
         this.quantities = new ArrayList<>();
         this.productService = Product_Service.getInstance();
    }

    public ArrayList<Product> getProductlist() {
        return productlist;
    }
   
    
    public void Additem(int productId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero.");
            return;
        }

        Product product = null;
        for (Product p : Product.products) {
            if (p.getProductId() == productId) {
                product = p;
                break;
            }
        }

        if (product == null) {
            System.out.println("Product with ID " + productId + " not found.");
            return;
        }

        int index = productlist.indexOf(product);
        if (index >= 0) {
            // Product already exists, update quantity
            int currentQuantity = quantities.get(index);
            quantities.set(index, currentQuantity + quantity);
            System.out.println(product.getName() + " quantity updated to: " + quantities.get(index));
        } else {
            // New product, add to the list
            productlist.add(product);
            quantities.add(quantity);
            System.out.println(product.getName() + " added with quantity: " + quantity);
        }
    }
    
    public void Removeitem(Product product) {
        int index = productlist.indexOf(product);
        if (index >= 0) {
            productlist.remove(index);
            quantities.remove(index);
            System.out.println(product.getName() + " removed successfully.");
        } else {
            System.out.println(product.getName() + " is not in the cart.");
        }
    }
    
     public double calculateTotalAmount() {
        double total = 0;
        for (int i = 0; i < productlist.size(); i++) {
            Product product = productlist.get(i);
            int quantity = quantities.get(i);
            total += product.getPrice() * quantity;
        }
        return total;
    }



    public Order checkout() 
    {
        if (isEmpty()) 
        {
            System.out.println("Sorry, you must add items to the cart before checking out.");
            return null;
        }

        double totalAmount = calculateTotalAmount();

          for (int i = 0; i < productlist.size(); i++) {
            Product product = productlist.get(i);
            int quantity = quantities.get(i);
            double productRevenue = product.getPrice() * quantity;

        // Update supplier's revenue
        Supplier supplier = product.getSupplier(product.getSupplierCompanyName());
        supplier.incrementRevenue(productRevenue);
            
        Seller seller = product.getSeller(product.getSellerUsername());
        seller.incrementRevenue(productRevenue);
            
            boolean success = productService.SellItems(product.getProductId(), quantity);
            if (!success) {
                System.out.println("Failed to process item: " + product.getName());
                return null;
            }
        }

        int orderId = (int) (System.currentTimeMillis() % 100000);
        Order order = new Order(orderId, new ArrayList<>(productlist), totalAmount, "Pending");

        productlist.clear();
        quantities.clear();
        System.out.println("Checkout successful! Order ID: " + orderId);

        return order;
    }
    
    public void clearCart() {
    if (isEmpty()) {
        System.out.println("The cart is already empty.");
        return;
    }

    productlist.clear();
    quantities.clear();
    System.out.println("The cart has been cleared successfully.");
}
    
public void generateReceipt(Order order) {
    if (order == null) {
        System.out.println("No order available to generate a receipt.");
        return;
    }

    System.out.println("========== RECEIPT ==========");
    System.out.println("Order ID: " + order.getOrderId());
    System.out.println("Order Status: " + order.getStatus());
    System.out.println("Items:");
    for (int i = 0; i < productlist.size(); i++) {
        Product product = productlist.get(i);
        int quantity = quantities.get(i);
        System.out.println("- " + product.getName() + " (x" + quantity + ") " + product.getPrice() + " $ each");
    }
    System.out.println("Total Amount: " + order.getTotalAmount() + " $");
    System.out.println("=============================");
}
}