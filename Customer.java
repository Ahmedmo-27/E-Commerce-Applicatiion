package com.mycompany.ecommerceproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer extends User implements Serializable {
    protected static int idCounter = 0;
    protected int customerId;
    public static ArrayList<Customer> ListC = new ArrayList<>();
    protected final ArrayList<Order> orderList = new ArrayList<>();
    protected Cart cart;
    protected double revenue;

    public Customer()
    {
        super("Customer1","1234");
        this.cart=new Cart();
    }
    
    public Customer(String username, String password) {
        super(username, password);
        this.customerId = idCounter++;
        this.cart = new Cart();
        ListC.add(this);
        this.revenue = 0.0;
    }

    public int getCustomerId() {
        return customerId;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public static ArrayList<Customer> getListC() {
        return ListC;
    }

    public static void setListC(ArrayList<Customer> ListC) {
        Customer.ListC = ListC;
    }

    public void addToCart(int productId, int quantity) {
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

        if (product != null) {
            this.cart.Additem(productId, quantity);
            System.out.println("Product added to the cart: " + product.getName());
        } else {
            System.out.println("Invalid product ID. Cannot add to cart.");
        }
    }

    public void removeFromCart(Product product) {
        if (product != null) {
            this.cart.Removeitem(product);
            System.out.println("Product removed from the cart: " + product.getName());
        } else {
            System.out.println("Invalid item. Cannot remove from cart.");
        }
    }

    public Order createOrder() {
        if (this.cart.getProductlist().isEmpty()) {
            System.out.println("* Cart is empty *\n* Cannot create an order *");
            return null;
        }

        Order order = this.cart.checkout();
        if (order != null) {
            this.orderList.add(order);
            System.out.println("Order created successfully: " + order.getOrderId());
        }
        return order;
    }

    public void insertOrder(Order order) {
        if (order != null) {
            this.orderList.add(order);
        } else {
            Order newOrder = cart.checkout();
            if (newOrder != null) {
                this.orderList.add(newOrder);
            }
        }
    }

    public boolean removeOrderById(int orderId) {
        for (Order order : this.orderList) {
            if (order.getOrderId() == orderId) {
                this.orderList.remove(order);
                System.out.println("Order with ID: " + orderId + " has been removed.");
                return true;
            }
        }
        System.out.println("Order with ID: " + orderId + " not found.");
        return false;
    }

    public void makePayment(double amount) {
        if (amount > 0) {
            System.out.println("Payment of: " + amount + " pounds has been processed.");
            revenue += amount;
        } else {
            System.out.println("Invalid payment amount.");
        }
    }

    public static boolean removeById(int id) {
        for (int i = 0; i < ListC.size(); i++) {
            if (ListC.get(i).customerId == id) {
                ListC.remove(i);
                System.out.println("Customer with ID: " + id + " has been removed successfully.");
                return true;
            }
        }
        System.out.println("Customer not found with ID: " + id);
        return false;
    }

    public void showOrderDetails() {
        if (this.orderList.isEmpty()) {
            System.out.println("No orders found for this customer.");
        } else {
            System.out.println("========== Order Details ==========");
            for (Order order : this.orderList) {
                System.out.println("Order ID: " + order.getOrderId());
                System.out.println("Status: " + order.getStatus());
                System.out.println("Total Amount: " + order.getTotalAmount() + " pounds");
                System.out.println("Products:");

                for (Product product : order.getProductList()) {
                    System.out.println("- " + product.getName() + " @ " + product.getPrice() + " pounds");
                }
                System.out.println("----------------------------------");
            }
        }
    }

    @Override
    public String toString() {
        return "Customer Username: " + getUsername() + " | Customer ID: " + customerId;
    }
}

