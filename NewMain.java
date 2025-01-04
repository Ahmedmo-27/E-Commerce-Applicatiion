package com.mycompany.ecommerceproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.io.*;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.application.Platform;


public class NewMain extends Application implements Serializable {
    private Stage primaryStage;
    private Order order;
    private Seller seller;
    private Customer customer;
    private Cart cart;
    private Product product;
    private Product_Service PService = Product_Service.getInstance();
    private ToFile fileHandler = new ToFile();
    private Admin admin = new Admin("Admin", "1234");
    private ArrayList<Object> dataList = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(FileOperations.class.getName());
    private ExecutorService executorService = Executors.newFixedThreadPool(4); // Adjust the number of threads as needed
 
    
    @Override
    public void start(Stage stage) {
        Platform.runLater(() -> {
            try {
                this.primaryStage = stage;
                primaryStage.setTitle("E-Commerce System");
                
                // Apply default window size
                primaryStage.setMinWidth(800);
                primaryStage.setMinHeight(600);
                
                showMainMenu();
                primaryStage.show();
            } catch (Exception e) {
                showError("Error starting application: " + e.getMessage());
                logger.log(Level.SEVERE, "Error during application startup", e);
            }
        });
    }

    private VBox createStyledVBox() {
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #f5f5f5;");
        return vbox;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("""
            -fx-background-color: #2196F3;
            -fx-text-fill: white;
            -fx-padding: 10 20;
            -fx-min-width: 150;
            -fx-cursor: hand;
            """);
        return button;
    }

     private void showMainMenu() {
        try {
            VBox menuLayout = UIStyles.createStyledVBox();

            Label titleLabel = UIStyles.createHeaderLabel("E-Commerce System");
            
            Button loginButton = UIStyles.createStyledButton("Login", true);
            Button createAccountButton = UIStyles.createStyledButton("Create Account", true);
            Button saveLoadButton = UIStyles.createStyledButton("Save/Load Data", false);

            loginButton.setOnAction(e -> Platform.runLater(this::showLoginScreen));
            createAccountButton.setOnAction(e -> Platform.runLater(this::showCreateAccountScreen));
            saveLoadButton.setOnAction(e -> Platform.runLater(this::showDataManagementScreen));

            menuLayout.getChildren().addAll(
                titleLabel,
                new Separator(),
                loginButton,
                createAccountButton,
                saveLoadButton
            );

            Scene menuScene = new Scene(menuLayout);
            primaryStage.setScene(menuScene);
        } catch (Exception e) {
            showError("Error showing main menu: " + e.getMessage());
            logger.log(Level.SEVERE, "Error in showMainMenu", e);
        }
    }
    
    private void showCreateAccountScreen() {
        VBox layout = UIStyles.createStyledVBox();
        
        Label titleLabel = UIStyles.createHeaderLabel("Create Account");
        
        TextField usernameField = UIStyles.createStyledTextField("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle(UIStyles.TEXT_FIELD_STYLE);
        
        ComboBox<String> accountTypeComboBox = new ComboBox<>();
        accountTypeComboBox.getItems().addAll("Admin", "Seller", "Customer");
        accountTypeComboBox.setValue("Customer");
        accountTypeComboBox.setStyle(UIStyles.TEXT_FIELD_STYLE);
        
        Button createButton = UIStyles.createStyledButton("Create Account", true);
        Button backButton = UIStyles.createStyledButton("Back", false);
        
        createButton.setOnAction(e -> handleCreateAccount(
            usernameField.getText(), 
            passwordField.getText(), 
            accountTypeComboBox.getValue()
        ));
        backButton.setOnAction(e -> showMainMenu());
        
        layout.getChildren().addAll(
            titleLabel,
            new Label("Username:"),
            usernameField,
            new Label("Password:"),
            passwordField,
            new Label("Account Type:"),
            accountTypeComboBox,
            createButton,
            backButton
        );
        
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
    }
   
    private void handleCreateAccount(String username, String password, String accountType) 
    {
        if (User.login(username, password) != null) 
        {
            showError("Username already exists.");
            return;
        }

        switch (accountType) 
        {
            case "Admin":
                new Admin(username, password);
                break;
            case "Seller":
                new Seller(username, password);
                break;
            case "Customer":
                new Customer(username, password);
                break;
            default:
                break;
        }

        showSuccess("Account created successfully!");
        showLoginScreen();
    }
    
    private void showLoginScreen() {
        VBox loginLayout = UIStyles.createStyledVBox();
        
        Label titleLabel = UIStyles.createHeaderLabel("Login");
        
        TextField usernameField = UIStyles.createStyledTextField("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle(UIStyles.TEXT_FIELD_STYLE);
        
        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-text-fill: " + UIStyles.ERROR_COLOR + ";");
        
        Button loginButton = UIStyles.createStyledButton("Login", true);
        Button backButton = UIStyles.createStyledButton("Back", false);
        
        loginButton.setOnAction(e -> {
            if (validateLogin(usernameField.getText(), passwordField.getText())) {
                handleLogin(usernameField.getText(), passwordField.getText());
            } else {
                feedbackLabel.setText("Please fill in all fields");
            }
        });
        backButton.setOnAction(e -> showMainMenu());
        
        loginLayout.getChildren().addAll(
            titleLabel,
            new Separator(),
            usernameField,
            passwordField,
            feedbackLabel,
            loginButton,
            backButton
        );
        
        Scene loginScene = new Scene(loginLayout);
        primaryStage.setScene(loginScene);
    }

    
        private void handleLogin(String username, String password) 
    {
        User loggedInUser  = User.login(username, password);
        if (loggedInUser  == null) 
        {
            showError("Invalid login credentials.");
        } 
        else 
        {
            if (loggedInUser  instanceof Admin) 
            {
                showAdminDashboard((Admin) loggedInUser );
            } 
        
            else if (loggedInUser  instanceof Seller) 
            {
                seller = (Seller) loggedInUser ; // Initialize the seller variable
                showSellerDashboard(seller);
            } 
            else if (loggedInUser  instanceof Customer) 
            {
                customer = (Customer) loggedInUser ; // Initialize the customer variable
                showCustomerDashboard(customer);
            }
        }
    }
        
        private void showSellerDashboard(Seller seller) {
    // Create styled VBox container
    VBox sellerLayout = UIStyles.createStyledVBox();
    
    // Create styled buttons
    Button showProductsButton = UIStyles.createStyledButton("Show Products", true);
    Button addProductButton = UIStyles.createStyledButton("Add Product", true);
    Button deleteProductButton = UIStyles.createStyledButton("Delete Product", false);
    Button searchProductButton = UIStyles.createStyledButton("Search Product", true);
    Button piecesSoldButton = UIStyles.createStyledButton("Number of Pieces Sold", false);
    Button bestSellerButton = UIStyles.createStyledButton("Best Selling Product", true);
    Button mostRevenueButton = UIStyles.createStyledButton("Most Revenue Product", true);
    Button backButton = BackButtonToLogin();
    backButton.setStyle(UIStyles.SECONDARY_BUTTON_STYLE);

    // Add event handlers
    showProductsButton.setOnAction(e -> showProducts());
    addProductButton.setOnAction(e -> addProduct());
    deleteProductButton.setOnAction(e -> deleteProduct(seller));
    searchProductButton.setOnAction(e -> searchProduct(seller));
    piecesSoldButton.setOnAction(e -> calculatePiecesSold(seller));
    bestSellerButton.setOnAction(e -> showBestSellingProduct(seller));
    mostRevenueButton.setOnAction(e -> showMostRevenueProduct(seller));

    // Add header
    Label dashboardHeader = UIStyles.createHeaderLabel("Seller Dashboard");
    
    // Add all components to layout
    sellerLayout.getChildren().addAll(
        dashboardHeader,
        showProductsButton,
        addProductButton,
        deleteProductButton,
        searchProductButton,
        piecesSoldButton,
        bestSellerButton,
        mostRevenueButton,
        backButton
    );

    // Create and set the scene
    Scene sellerScene = new Scene(sellerLayout, UIStyles.MIN_WIDTH, UIStyles.MIN_HEIGHT);
    primaryStage.setScene(sellerScene);
}

    private void showMostRevenueProduct(Seller seller) {
    if (seller.getProductList().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "No products available.");
        alert.showAndWait();
        return;
    }

    Product mostRevenueProduct = null;
    double maxRevenue = 0;

    for (Product product : seller.getProductList()) {
        double revenue = product.getUnitsSold() * product.getPrice();
        if (revenue > maxRevenue) {
            maxRevenue = revenue;
            mostRevenueProduct = product;
        }
    }

    if (mostRevenueProduct != null) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Most Revenue Product: " + mostRevenueProduct.getName() +
                "\nRevenue: " + maxRevenue);
        alert.showAndWait();
    }
}
    
    private void calculatePiecesSold(Seller seller) {
    int totalPiecesSold = 0;

    for (Product product : seller.getProductList()) {
        totalPiecesSold += Integer.parseInt(String.valueOf(product.getUnitsSold()));
    }

    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Total Pieces Sold: " + totalPiecesSold);
    alert.showAndWait();
}


private void deleteProduct(Seller seller) {
    if (seller == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR, "Seller is not available.");
        alert.showAndWait();
        return;
    }

    Product_Service productService = Product_Service.getInstance();

    TextInputDialog productIdDialog = new TextInputDialog();
    productIdDialog.setTitle("Delete Product");
    productIdDialog.setHeaderText("Enter Product ID:");
    String input = productIdDialog.showAndWait().orElse("");

    if (!input.isEmpty()) {
        try {
            int productId = Integer.parseInt(input);
            Product product = productService.findProductByID(productId);

            if (product != null) {
                seller.removeProduct(product);
                productService.removeItem(product);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product deleted successfully: " + product.getName());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Product not found.");
                alert.showAndWait();
            }
        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Product ID. Please enter a valid number.");
            alert.showAndWait();
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Product ID cannot be empty.");
        alert.showAndWait();
    }
}


private void searchProduct(Seller seller) {
    TextInputDialog searchDialog = new TextInputDialog();
    searchDialog.setTitle("Search Product");
    searchDialog.setHeaderText("Enter Product Name or ID:");
    String input = searchDialog.showAndWait().orElse("");

    if (!input.isEmpty()) {
        Product product = null;

        try {
            int productId = Integer.parseInt(input);
            // Search by product ID in the static product list
            for (Product p : Product.products) {
                if (p.getProductId() == productId) {
                    product = p;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            // Search by product name in the static product list
            for (Product p : Product.products) {
                if (p.getName().equalsIgnoreCase(input)) {
                    product = p;
                    break;
                }
            }
        }

        if (product != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product Found:\n" + product.toString());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Product not found.");
            alert.showAndWait();
        }
    }
}

    private Button BackButtonToLogin() 
    {
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> 
        {
            showLoginScreen();  // Navigate back to the login screen
        });
        return backButton;
    }

    private void showBestSellingProduct(Seller seller) 
    {
    Product bestSeller = seller.getTopSellingProduct();
    if (bestSeller != null) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Best Selling Product: " + bestSeller.getName() +
                "\nUnits Sold: " + bestSeller.getUnitsSold());
        alert.showAndWait();
    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR, "No products available.");
        alert.showAndWait();
    }
}   

    private void showCustomerDashboard(Customer customer) {
    // Create styled VBox container
    VBox customerLayout = UIStyles.createStyledVBox();
    
    // Create styled buttons using the helper method
    Button addToCartButton = UIStyles.createStyledButton("Add to Cart", true);
    Button removeFromCartButton = UIStyles.createStyledButton("Remove from Cart", false);
    Button createOrderButton = UIStyles.createStyledButton("Create Order", true);
    Button viewOrdersButton = UIStyles.createStyledButton("View Orders", false);
    Button cancelCartButton = UIStyles.createStyledButton("Cancel Cart", false);
    Button searchProductButton = UIStyles.createStyledButton("Search Product", true);
    Button logoutButton = BackButtonToLogin();
    logoutButton.setStyle(UIStyles.SECONDARY_BUTTON_STYLE);

    // Add event handlers
    addToCartButton.setOnAction(e -> addToCart());
    removeFromCartButton.setOnAction(e -> removeFromCart());
    createOrderButton.setOnAction(e -> createOrder(customer));
    viewOrdersButton.setOnAction(e -> viewOrders());
    cancelCartButton.setOnAction(e -> cancelCart(customer));
    searchProductButton.setOnAction(e -> searchProduct());

    // Add header
    Label dashboardHeader = UIStyles.createHeaderLabel("Customer Dashboard");
    
    // Add all components to layout
    customerLayout.getChildren().addAll(
        dashboardHeader,
        addToCartButton,
        removeFromCartButton,
        createOrderButton,
        viewOrdersButton,
        cancelCartButton,
        searchProductButton,
        logoutButton
    );

    // Create and set the scene
    Scene customerScene = new Scene(customerLayout, UIStyles.MIN_WIDTH, UIStyles.MIN_HEIGHT);
    primaryStage.setScene(customerScene);
}




    private void cancelCart(Customer customer) {
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Cancel Cart");
    confirmationAlert.setHeaderText("Are you sure you want to cancel the cart?");
    confirmationAlert.setContentText("This will remove all items from your cart.");

    confirmationAlert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            customer.getCart().clearCart(); // Assuming `Cart` class has a `clearCart` method.
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cart has been cleared.");
            alert.showAndWait();
        }
    });
}

    
    private void searchProduct() {
    TextInputDialog searchDialog = new TextInputDialog();
    searchDialog.setTitle("Search Product");
    searchDialog.setHeaderText("Enter Product Name or ID:");
    searchDialog.setContentText("Search:");

    searchDialog.showAndWait().ifPresent(input -> {
        if (!input.isEmpty()) {
            Product foundProduct = null;
            try {
                int productId = Integer.parseInt(input);
                for (Product p : Product.products) {
                    if (p.getProductId() == productId) {
                        foundProduct = p;
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                for (Product p : Product.products) {
                    if (p.getName().equalsIgnoreCase(input)) {
                        foundProduct = p;
                        break;
                    }
                }
            }

            if (foundProduct != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                        "Product Found:\nName: " + foundProduct.getName() + 
                        "\nID: " + foundProduct.getProductId() +
                        "\nPrice: " + foundProduct.getPrice());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Product not found.");
                alert.showAndWait();
            }
        }
    });
}

    
    
    private void showCustomerList() 
    {
        ListView<String> customerListView = new ListView<>();
        for (Customer customer : Customer.ListC) 
        {
            customerListView.getItems().add(customer.toString());
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showAdminDashboard(new Admin("admin", "admin"))); // Navigate back to Admin Dashboard

        VBox customerListLayout = new VBox(10, customerListView, backButton);
        customerListLayout.setPadding(new javafx.geometry.Insets(20));

        Scene customerListScene = new Scene(customerListLayout, 300, 200);
        primaryStage.setScene(customerListScene);
    }

    private void addCustomer() 
    {
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button addButton = new Button("Add");

        addButton.setOnAction(e -> 
        {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) 
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("Both fields are required!");
                alert.showAndWait();
                return;
            }

            Admin admin = new Admin("admin", "1234");
            if (admin.AddCustomers(username, password)) 
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Customer added successfully!");
                alert.showAndWait();
                showAdminDashboard(admin); // Refresh the dashboard after adding
            }    
            
            else 
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Customer could not be added. Username may already exist.");
                alert.showAndWait();
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showAdminDashboard(new Admin("admin", "admin"))); // Navigate back to Admin Dashboard
    
        VBox addCustomerLayout = new VBox(10, new Label("Username:"), usernameField, new Label("Password:"), passwordField, addButton,backButton);
        addCustomerLayout.setPadding(new javafx.geometry.Insets(20));

        Scene addCustomerScene = new Scene(addCustomerLayout, 300, 200);
        primaryStage.setScene(addCustomerScene);
    }


    private void removeCustomer() 
    {
        TextField customerIdField = new TextField();
        Button removeButton = new Button("Remove");

        removeButton.setOnAction(e -> {
        String input = customerIdField.getText().trim();

        if (input.isEmpty()) 
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Customer ID is required!");
            alert.showAndWait();
            return;
        }

        try 
        {
            int id = Integer.parseInt(input);

            Admin admin = new Admin("admin", "1234");
            if (admin.RemoveCustomer(id)) 
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Customer removed successfully!");
                alert.showAndWait();
                showAdminDashboard(admin); // Refresh the dashboard after removal
            } 
        
            else 
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Customer with ID " + id + " does not exist.");
                alert.showAndWait();
            }
        } 
        
        catch (NumberFormatException ex) 
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Customer ID must be a valid number!");
            alert.showAndWait();
        }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> showAdminDashboard(new Admin("admin", "admin"))); // Navigate back to Admin Dashboard
    
        VBox removeCustomerLayout = new VBox(10, new Label("Customer ID:"), customerIdField, removeButton,backButton);
        removeCustomerLayout.setPadding(new javafx.geometry.Insets(20));

        Scene removeCustomerScene = new Scene(removeCustomerLayout, 300, 200);
        primaryStage.setScene(removeCustomerScene);
    }


    private void showProducts() {
    VBox layout = UIStyles.createStyledVBox();
    
    Label titleLabel = UIStyles.createHeaderLabel("Product List");
    Label subtitleLabel = new Label("Browse all available products");
    subtitleLabel.setStyle(UIStyles.SUBHEADER_LABEL_STYLE);
    
    @SuppressWarnings("unchecked")
    TableView<Product> productTable = (TableView<Product>) UIStyles.createStyledTableView();
    
    // Create and style table columns
    TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
    TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
    TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
    TableColumn<Product, Integer> stockColumn = new TableColumn<>("Stock");
    TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
    
    // Set cell value factories
    idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    
    productTable.getColumns().addAll(idColumn, nameColumn, priceColumn, stockColumn, categoryColumn);
    
    // Load products into table
    productTable.getItems().addAll(Product.getProducts());
    
    Button backButton = UIStyles.createStyledButton("Back to Dashboard", false);
    backButton.setOnAction(e -> showSellerDashboard(seller));
    
    layout.getChildren().addAll(
        titleLabel,
        subtitleLabel,
        productTable,
        backButton
    );
    
    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
}

    private void addProduct() 
    {
        TextField productNameField = new TextField();
        TextField productPriceField = new TextField();
        TextField productStockField = new TextField();
        TextField productCategoryField = new TextField();
        TextField productUnitsSoldField = new TextField();
        TextField productIDField = new TextField();
        TextField productSupplierField = new TextField();

        Button addButton = new Button("Add");

        addButton.setOnAction(e -> {
            try {
                // Ensure seller is initialized
                if (seller == null) 
                {
                    showError("Seller is not initialized.");
                    return;
                }

                // Validate supplier input and retrieve or create a supplier
                String supplierName = productSupplierField.getText();
                if (supplierName == null || supplierName.trim().isEmpty()) 
                {
                    showError("Supplier name cannot be empty.");
                    return;
                }
                
                Supplier sup = Supplier.getOrCreate(supplierName); // Use getOrCreate method to avoid duplicates

            // Validate and parse product fields
            String name = productNameField.getText();
            if (name == null || name.trim().isEmpty()) {
                showError("Product name cannot be empty.");
                return;
            }

            String category = productCategoryField.getText();
            if (category == null || category.trim().isEmpty()) {
                showError("Product category cannot be empty.");
                return;
            }

            double price = Double.parseDouble(productPriceField.getText());
            if (price <= 0) {
                showError("Price must be greater than 0.");
                return;
            }

            int stock = Integer.parseInt(productStockField.getText());
            if (stock < 0) {
                showError("Stock cannot be negative.");
                return;
            }

            int unitsSold = Integer.parseInt(productUnitsSoldField.getText());
            if (unitsSold < 0) {
                showError("Units sold cannot be negative.");
                return;
            }

            int id = Integer.parseInt(productIDField.getText());
            if (id <= 0) {
                showError("Product ID must be greater than 0.");
                return;
            }

            // Create and associate the product
            Product newProduct = new Product(id, name, unitsSold, stock, price, category, seller.getName(), sup.getCompanyName());
            PService.addProduct(newProduct,stock);
            sup.addProduct(newProduct);

            // Confirm successful addition
            showSuccess("Product added successfully!");
            showSellerDashboard(seller);
        } catch (NumberFormatException ex) {
            showError("Please enter valid numeric values for price, stock, units sold, and product ID.");
        } catch (Exception ex) {
            showError("An error occurred: " + ex.getMessage());
        }
    });

    VBox addProductLayout = new VBox(10,
        new Label("Product Name:"), productNameField,
        new Label("Price:"), productPriceField,
        new Label("Stock:"), productStockField,
        new Label("Category:"), productCategoryField,
        new Label("Units Sold:"), productUnitsSoldField,
        new Label("Product ID:"), productIDField,
        new Label("Supplier:"), productSupplierField, // Added label for supplier
        addButton
    );
    addProductLayout.setPadding(new javafx.geometry.Insets(20));

    Scene addProductScene = new Scene(addProductLayout, 300, 500); // Adjusted height for more fields
    primaryStage.setScene(addProductScene);
    primaryStage.show();
}


private void addToCart() {
        try {
            if (cart == null) {
                cart = new Cart();
            }
            
            VBox layout = UIStyles.createStyledVBox();
            
            Label titleLabel = UIStyles.createHeaderLabel("Add to Cart");
            
            TextField productIdField = UIStyles.createStyledTextField("Enter Product ID");
            TextField quantityField = UIStyles.createStyledTextField("Enter Quantity");
            
            TextArea productListArea = new TextArea();
            productListArea.setText(Product.getAllProductsAsString());
            productListArea.setEditable(false);
            productListArea.setWrapText(true);
            productListArea.setStyle(UIStyles.TEXT_FIELD_STYLE);
            
            Button addButton = UIStyles.createStyledButton("Add to Cart", true);
            addButton.setOnAction(e -> {
                try {
                    int productId = Integer.parseInt(productIdField.getText().trim());
                    int quantity = Integer.parseInt(quantityField.getText().trim());
                    
                    Product product = Product.products.stream()
                        .filter(p -> p.getProductId() == productId)
                        .findFirst()
                        .orElse(null);
                    
                    if (product != null) {
                        cart.Additem(productId, quantity);
                        showSuccess("Product added to cart successfully!");
                        productIdField.clear();
                        quantityField.clear();
                    } else {
                        showError("Product not found. Please check the product ID and try again.");
                    }
                } catch (NumberFormatException ex) {
                    showError("Please enter valid numeric values for Product ID and Quantity.");
                }
            });
            
            Button backButton = UIStyles.createStyledButton("Back", false);
            backButton.setOnAction(e -> Platform.runLater(() -> showCustomerDashboard(customer)));
            
            layout.getChildren().addAll(
                titleLabel,
                new Label("Available Products:"),
                productListArea,
                new Label("Product ID:"),
                productIdField,
                new Label("Quantity:"),
                quantityField,
                addButton,
                backButton
            );
            
            Scene scene = new Scene(layout);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            showError("Error adding to cart: " + e.getMessage());
            logger.log(Level.SEVERE, "Error in addToCart", e);
        }
    }



private void showFinalizeOrderWindow(Customer customer, Order order) {
    VBox layout = UIStyles.createStyledVBox();
    
    Label titleLabel = UIStyles.createHeaderLabel("Finalize Order");
    Label successLabel = new Label("Order finalized successfully!");
    
    Button finalizeButton = UIStyles.createStyledButton("Create Order", true);
    finalizeButton.setOnAction(e -> {
        try {
            customer.createOrder();
            showSuccess("Order has been successfully created!");
            showCustomerDashboard(customer);
        } catch (Exception ex) {
            showError("Error finalizing order. Please try again.");
        }
    });
    
    layout.getChildren().addAll(
        titleLabel,
        successLabel,
        finalizeButton
    );
    
    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
}

private void showConfirmPurchaseWindow(Customer customer, Order order) {
    VBox layout = UIStyles.createStyledVBox();
    
    Label titleLabel = UIStyles.createHeaderLabel("Confirm Purchase");
    Label infoLabel = new Label("Click the button below to generate the receipt:");
    
    Button generateReceiptButton = UIStyles.createStyledButton("Generate Receipt", true);
    generateReceiptButton.setOnAction(e -> {
        cart.generateReceipt(order);
        showSuccess("Receipt has been generated. Check the console for details.");
    });
    
    Button confirmPurchaseButton = UIStyles.createStyledButton("Confirm Purchase", true);
    confirmPurchaseButton.setOnAction(e -> {
        showFinalizeOrderWindow(customer, order);
    });
    
    Button backButton = UIStyles.createStyledButton("Back", false);
    backButton.setOnAction(e -> createOrder(customer));
    
    layout.getChildren().addAll(
        titleLabel,
        infoLabel,
        generateReceiptButton,
        confirmPurchaseButton,
        backButton
    );
    
    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
}


private void removeFromCart() {
    VBox layout = UIStyles.createStyledVBox();
    
    Label titleLabel = UIStyles.createHeaderLabel("Remove from Cart");
    
    // Create a styled list view to show current cart items
    @SuppressWarnings("unchecked")
    ListView<String> cartItemsView = (ListView<String>) UIStyles.createStyledListView();
    if (cart != null && !cart.getProductlist().isEmpty()) {
        for (Product product : cart.getProductlist()) {
            cartItemsView.getItems().add(product.toString());
        }
    } else {
        cartItemsView.getItems().add("Cart is empty");
    }
    
    Label currentItemsLabel = new Label("Current Items in Cart:");
    currentItemsLabel.setStyle(UIStyles.SUBHEADER_LABEL_STYLE);
    
    TextField productIdField = UIStyles.createStyledTextField("Enter Product ID");
    
    Button removeButton = UIStyles.createStyledButton("Remove Item", true);
    removeButton.setOnAction(e -> {
        String input = productIdField.getText().trim();
        if (input.isEmpty()) {
            showError("Product ID cannot be empty.");
            return;
        }
        try {
            int productId = Integer.parseInt(input);
            Product selectedProduct = PService.findProductByID(productId);
            
            if (selectedProduct != null && cart.getProductlist().contains(selectedProduct)) {
                cart.Removeitem(selectedProduct);
                productIdField.clear();
                // Refresh the cart items view
                cartItemsView.getItems().clear();
                if (!cart.getProductlist().isEmpty()) {
                    for (Product product : cart.getProductlist()) {
                        cartItemsView.getItems().add(product.toString());
                    }
                } else {
                    cartItemsView.getItems().add("Cart is empty");
                }
                showSuccess("Product removed from cart successfully!");
            } else {
                showError("Product not found in cart.");
            }
        } catch (NumberFormatException ex) {
            showError("Invalid Product ID. Please enter a numeric value.");
        }
    });
    
    Button backButton = UIStyles.createStyledButton("Back to Dashboard", false);
    backButton.setOnAction(e -> showCustomerDashboard(customer));
    
    layout.getChildren().addAll(
        titleLabel,
        currentItemsLabel,
        cartItemsView,
        new Label("Enter Product ID to Remove:"),
        productIdField,
        removeButton,
        backButton
    );
    
    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
}

private void createOrder(Customer customer) {
    VBox layout = UIStyles.createStyledVBox();
    
    Label titleLabel = UIStyles.createHeaderLabel("Create Order");
    Label subtitleLabel = new Label("Review your cart and create your order");
    subtitleLabel.setStyle(UIStyles.SUBHEADER_LABEL_STYLE);
    
    @SuppressWarnings("unchecked")
    ListView<String> itemsList = (ListView<String>) UIStyles.createStyledListView();
    Label cartLabel = new Label("Products in Cart:");
    cartLabel.setStyle(UIStyles.SUBHEADER_LABEL_STYLE);
    
    if (cart != null && !cart.getProductlist().isEmpty()) {
        for (Product product : cart.getProductlist()) {
            itemsList.getItems().add(product.toString());
        }
    } else {
        itemsList.getItems().add("Cart is empty");
    }
    
    Label totalLabel = new Label("Total Amount: $" + 
        String.format("%.2f", cart != null ? cart.calculateTotalAmount() : 0.0));
    totalLabel.setStyle("""
        -fx-font-size: 18;
        -fx-font-weight: bold;
        -fx-text-fill: %s;
        -fx-padding: 10;
        """.formatted(UIStyles.PRIMARY_COLOR));
    
    Button createOrderButton = UIStyles.createStyledButton("Create Order", true);
    createOrderButton.setOnAction(e -> {
        if (cart != null && !cart.getProductlist().isEmpty()) {
            try {
                order = cart.checkout();
                order.InsertOrder(order);
                customer.insertOrder(order);
                showConfirmPurchaseWindow(customer, order);
            } catch (Exception ex) {
                showError("Error creating order: " + ex.getMessage());
            }
        } else {
            showError("Cart is empty. Cannot create order.");
        }
    });
    
    Button backButton = UIStyles.createStyledButton("Back to Dashboard", false);
    backButton.setOnAction(e -> showCustomerDashboard(customer));
    
    layout.getChildren().addAll(
        titleLabel,
        subtitleLabel,
        cartLabel,
        itemsList,
        totalLabel,
        createOrderButton,
        backButton
    );
    
    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
}



    private void viewOrders() {
    VBox layout = UIStyles.createStyledVBox();
    
    Label titleLabel = UIStyles.createHeaderLabel("Your Orders");
    Label subtitleLabel = new Label("View your order history");
    subtitleLabel.setStyle(UIStyles.SUBHEADER_LABEL_STYLE);
    
    @SuppressWarnings("unchecked")
    TableView<Order> orderTable = (TableView<Order>) UIStyles.createStyledTableView();
    
    // Create and style table columns
    TableColumn<Order, Integer> orderIdColumn = new TableColumn<>("Order ID");
    TableColumn<Order, Double> totalColumn = new TableColumn<>("Total Amount");
    TableColumn<Order, String> dateColumn = new TableColumn<>("Order Date");
    
    // Set cell value factories
    orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
    totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    
    orderTable.getColumns().addAll(orderIdColumn, totalColumn, dateColumn);
    
    if (!customer.getOrderList().isEmpty()) {
        orderTable.getItems().addAll(customer.getOrderList());
    }
    
    Button backButton = UIStyles.createStyledButton("Back to Dashboard", false);
    backButton.setOnAction(e -> showCustomerDashboard(customer));
    
    layout.getChildren().addAll(
        titleLabel,
        subtitleLabel,
        orderTable,
        backButton
    );
    
    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
}


    private TextField createStyledTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        textField.setStyle("""
            -fx-padding: 8;
            -fx-background-radius: 5;
            -fx-min-width: 250;
            """);
        return textField;
    }

    private PasswordField createStyledPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("""
            -fx-padding: 8;
            -fx-background-radius: 5;
            -fx-min-width: 250;
            """);
        return passwordField;
    }

    private boolean validateLogin(String username, String password) {
        return !username.trim().isEmpty() && !password.trim().isEmpty();
    }

    private void showAdminDashboard(Admin admin) {
        VBox dashboardLayout = UIStyles.createStyledVBox();
        
        Label titleLabel = UIStyles.createHeaderLabel("Admin Dashboard");
        
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Create tabs with styled content
        Tab customersTab = new Tab("Customers", createCustomerManagementPane());
        Tab sellersTab = new Tab("Sellers", createSellerManagementPane());
        Tab productsTab = new Tab("Products", createProductPane());
        Tab reportsTab = new Tab("Reports", createReportsPane());
        
        tabPane.getTabs().addAll(customersTab, sellersTab, productsTab, reportsTab);
        
        Button logoutButton = UIStyles.createStyledButton("Logout", false);
        logoutButton.setOnAction(e -> showMainMenu());
        
        dashboardLayout.getChildren().addAll(titleLabel, tabPane, logoutButton);
        
        Scene dashboardScene = new Scene(dashboardLayout);
        primaryStage.setScene(dashboardScene);
    }

    
    private VBox createProductPane() {
    VBox productPane = createStyledVBox();

    // Title
    Label titleLabel = UIStyles.createHeaderLabel("Product Management");

    // Create TableView for products
    TableView<Product> productTable = new TableView<>();
    productTable.setStyle("-fx-min-height: 300px;");

    // Define columns
    TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));

    TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

    TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

    TableColumn<Product, Integer> stockColumn = new TableColumn<>("Stock");
    stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

    TableColumn<Product, String> categoryColumn = new TableColumn<>("Category");
    categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

    TableColumn<Product, Integer> soldColumn = new TableColumn<>("Units Sold");
    soldColumn.setCellValueFactory(new PropertyValueFactory<>("unitsSold"));

    TableColumn<Product, String> sellerColumn = new TableColumn<>("Seller");
    sellerColumn.setCellValueFactory(new PropertyValueFactory<>("sellerName"));

    TableColumn<Product, String> supplierColumn = new TableColumn<>("Supplier");
    supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));

    productTable.getColumns().addAll(
        idColumn, nameColumn, priceColumn, stockColumn,
        categoryColumn, soldColumn, sellerColumn, supplierColumn
    );

    // Buttons container
    VBox buttonContainer = new VBox(10);
    buttonContainer.setAlignment(Pos.CENTER);

    // Action Buttons
    Button showAllButton = UIStyles.createStyledButton("Show All Products", true);
    Button addProductButton = UIStyles.createStyledButton("Add New Product", true);
    Button searchProductButton = UIStyles.createStyledButton("Search Product", true);
    Button deleteProductButton = UIStyles.createStyledButton("Delete Product", true);
    Button revenueAnalysisButton = UIStyles.createStyledButton("Revenue Analysis", true);
    Button refreshButton = UIStyles.createStyledButton("Refresh", false);

    // Button actions
    showAllButton.setOnAction(e -> refreshTable(productTable));

    addProductButton.setOnAction(e -> showAddProductDialog());

    searchProductButton.setOnAction(e -> {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Product");
        dialog.setHeaderText("Enter Product ID or Name:");
        dialog.showAndWait().ifPresent(this::searchAndDisplayProduct);
    });

    deleteProductButton.setOnAction(e -> {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            if (deleteProduct(selectedProduct)) {
                productTable.getItems().remove(selectedProduct);
                showSuccess("Product deleted successfully.");
            } else {
                showError("Failed to delete the product. Please try again.");
            }
        } else {
            showError("Please select a product to delete.");
        }
    });

    revenueAnalysisButton.setOnAction(e -> showRevenueAnalysis());

    refreshButton.setOnAction(e -> refreshTable(productTable));

    // Add all components to the pane
    buttonContainer.getChildren().addAll(
        showAllButton, addProductButton, searchProductButton,
        deleteProductButton, revenueAnalysisButton, refreshButton
    );

    productPane.getChildren().addAll(titleLabel, productTable, buttonContainer);
    return productPane;
}

private boolean deleteProduct(Product selectedProduct) {
    Product_Service productService = Product_Service.getInstance();
    try {
        productService.removeItem(selectedProduct);
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

private void refreshTable(TableView<Product> productTable) {
    productTable.getItems().clear();
    productTable.getItems().addAll(Product.products);
}


    private void showAddProductDialog() {
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Add New Product");
    
    VBox layout = createStyledVBox();
    
    // Create form fields
    TextField idField = UIStyles.createStyledTextField("Product ID");
    TextField nameField = UIStyles.createStyledTextField("Product Name");
    TextField priceField = UIStyles.createStyledTextField("Price");
    TextField stockField = UIStyles.createStyledTextField("Stock");
    TextField categoryField = UIStyles.createStyledTextField("Category");
    TextField supplierField = UIStyles.createStyledTextField("Supplier Name");
    
    Button saveButton = UIStyles.createStyledButton("Save Product", true);
    saveButton.setOnAction(e -> {
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String category = categoryField.getText();
            String supplierName = supplierField.getText();
            
            // Create supplier if doesn't exist
            Supplier supplier = Supplier.getOrCreate(supplierName);
            
            // Create new product
            Product newProduct = new Product(id, name, 0, stock, price, category, 
                                          "Admin", supplier.getCompanyName());
            
            // Add product to service and supplier
            PService.addProduct(newProduct, stock);
            supplier.addProduct(newProduct);
            
            showSuccess("Product added successfully!");
            dialog.close();
            
        } catch (NumberFormatException ex) {
            showError("Please enter valid numeric values.");
        } catch (Exception ex) {
            showError("Error adding product: " + ex.getMessage());
        }
    });
    
    layout.getChildren().addAll(
        new Label("Product ID:"), idField,
        new Label("Name:"), nameField,
        new Label("Price:"), priceField,
        new Label("Stock:"), stockField,
        new Label("Category:"), categoryField,
        new Label("Supplier:"), supplierField,
        saveButton
    );
    
    Scene scene = new Scene(layout);
    dialog.setScene(scene);
    dialog.showAndWait();
}

private void searchAndDisplayProduct(String input) {
    Product product = null;
    try {
        int productId = Integer.parseInt(input);
        product = PService.findProductByID(productId);
    } catch (NumberFormatException e) {
        // Search by name if input is not a number
        product = Product.products.stream()
                .filter(p -> p.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);
    }
    
    if (product != null) {
        showSuccess("Product found:\n" + product.toString());
    } else {
        showError("Product not found.");
    }
}
    
    private void showRevenueAnalysis() {
    VBox analysisLayout = createStyledVBox();
    Stage analysisStage = new Stage();
    analysisStage.initModality(Modality.APPLICATION_MODAL);
    analysisStage.setTitle("Revenue Analysis");
    
    // Create analysis components
    Label totalRevenueLabel = new Label("Total Revenue: $" + 
        Product.products.stream()
            .mapToDouble(p -> p.getPrice() * p.getUnitsSold())
            .sum()
    );
    
    Product mostRevenue = Product.products.stream()
        .max((p1, p2) -> Double.compare(
            p1.getPrice() * p1.getUnitsSold(),
            p2.getPrice() * p2.getUnitsSold()))
        .orElse(null);
    
    Label bestProductLabel = new Label("Best Performing Product: " + 
        (mostRevenue != null ? mostRevenue.getName() : "No data"));
    
    Button closeButton = UIStyles.createStyledButton("Close", false);
    closeButton.setOnAction(e -> analysisStage.close());
    
    analysisLayout.getChildren().addAll(
        totalRevenueLabel,
        bestProductLabel,
        closeButton
    );
    
    Scene scene = new Scene(analysisLayout);
    analysisStage.setScene(scene);
    analysisStage.showAndWait();
}

    private VBox createReportsPane() {
        VBox reportsPane = new VBox(10);

        Button maxRevenueCustomer = new Button("Customer with Max Revenue");
        Button maxRevenueSeller = new Button("Seller with Max Revenue");
        Button maxRevenueSupplier = new Button("Supplier with Max Revenue");

        reportsPane.getChildren().addAll(maxRevenueCustomer, maxRevenueSeller, maxRevenueSupplier);

        // Event Listeners
        maxRevenueCustomer.setOnAction(e -> admin.customerMaxRevenue());
        maxRevenueSeller.setOnAction(e -> admin.SellerWithMaxRevenue());
        maxRevenueSupplier.setOnAction(e -> admin.SupplierWithMaxRevenue());

        return reportsPane;
    }


    private HBox createHeader(String title) {
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #2196F3;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20; -fx-text-fill: white;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("""
            -fx-background-color: white;
            -fx-text-fill: #2196F3;
            """);
        logoutButton.setOnAction(e -> showMainMenu());

        header.getChildren().addAll(titleLabel, new Separator(), logoutButton);
        return header;
    }

    private Tab createTab(String title, Node content) {
        Tab tab = new Tab(title);
        tab.setContent(content);
        return tab;
    }

    private VBox createCustomerManagementPane() {
        VBox pane = createStyledVBox();
        
        TableView<Customer> customerTable = new TableView<>();
        
        // Add columns
        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        TableColumn<Customer, String> idCol = new TableColumn<>("ID");
        
        customerTable.getColumns().addAll(nameCol, idCol);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button addButton = createStyledButton("Add Customer");
        Button removeButton = createStyledButton("Remove Customer");
        Button refreshButton = createStyledButton("Refresh");
        
        buttonBox.getChildren().addAll(addButton, removeButton, refreshButton);
        
        pane.getChildren().addAll(customerTable, buttonBox);
        return pane;
    }

    private VBox createSellerManagementPane() {
        VBox pane = createStyledVBox();
        
        TableView<Seller> sellerTable = new TableView<>();
        
        TableColumn<Seller, String> nameCol = new TableColumn<>("Name");
        TableColumn<Seller, String> idCol = new TableColumn<>("ID");
        
        sellerTable.getColumns().addAll(nameCol, idCol);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button addButton = createStyledButton("Add Seller");
        Button removeButton = createStyledButton("Remove Seller");
        Button refreshButton = createStyledButton("Refresh");
        
        buttonBox.getChildren().addAll(addButton, removeButton, refreshButton);
        
        pane.getChildren().addAll(sellerTable, buttonBox);
        return pane;
    }

private void showDataManagementScreen() {
    VBox layout = createStyledVBox();

    Label titleLabel = new Label("Data Management");
    titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

    Button saveButton = createStyledButton("Save Data");
    Button loadButton = createStyledButton("Load Data");
    Button backButton = createStyledButton("Back");
    Button deleteButton = createStyledButton("Delete");
    
    // Status label for feedback
    Label statusLabel = new Label();
    statusLabel.setStyle("-fx-text-fill: green;");

    saveButton.setOnAction(e -> {
        try {
            fileHandler.saveData(dataList);
            statusLabel.setText("Data saved successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception ex) {
            statusLabel.setText("Error saving data: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    loadButton.setOnAction(e -> {
        try {
            fileHandler.loadData();
            statusLabel.setText("Data loaded successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception ex) {
            statusLabel.setText("Error loading data: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });

    deleteButton.setOnAction(e -> {
        try {
            File file = new File("ecommerce_data.bin");
            if (file.exists()) {
                if (file.delete()) {
                    // Clear all static lists
                    Product.products.clear();
                    Customer.ListC.clear();
                    Seller.sellerList.clear();
                    Supplier.supplier.clear();
                    Order.getOrderList().clear();
                    
                    statusLabel.setText("File deleted successfully");
                    statusLabel.setStyle("-fx-text-fill: green;");
                } else {
                    statusLabel.setText("Failed to delete file");
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            } else {
                statusLabel.setText("No file exists to delete");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception ex) {
            statusLabel.setText("Error deleting file: " + ex.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    });
    
    backButton.setOnAction(e -> showMainMenu());

    layout.getChildren().addAll(
        titleLabel,
        new Separator(),
        saveButton,
        loadButton,
        deleteButton,
        statusLabel,
        backButton
    );

    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
}

private void saveDataToFile() {
    executorService.submit(() -> {
        try {
            // Update dataList with current state of all entities
            dataList = new ArrayList<>();
            dataList.addAll(Product.products);
            dataList.addAll(Customer.ListC);
            dataList.addAll(Seller.sellerList);
            dataList.addAll(Supplier.supplier);
            dataList.addAll(Order.getOrderList());

            if (dataList.isEmpty()) {
                showError("No data to save.");
                return;
            }

            fileHandler.saveData(dataList);
            Platform.runLater(() -> showSuccess("Data saved successfully!"));
            logger.log(Level.INFO, "Successfully saved data to file.");
        } catch (Exception e) {
            String errorMessage = "Failed to save data: " + e.getMessage();
            Platform.runLater(() -> showError(errorMessage));
            logger.log(Level.SEVERE, errorMessage, e);
        }
    });
}

private void loadDataFromFile() {
    executorService.submit(() -> {
        try {
            fileHandler.loadData();
            Platform.runLater(() -> showSuccess("Data loaded successfully!"));
            logger.log(Level.INFO, "Successfully loaded data from file.");
        } catch (Exception e) {
            String errorMessage = "Error loading data: " + e.getMessage();
            Platform.runLater(() -> showError(errorMessage));
            logger.log(Level.SEVERE, errorMessage, e);
        }
    });
}

private void deleteDataFile() {
    try {
        File file = new File("ecommerce_data.bin");
        if (!file.exists()) {
            showError("No data file exists to delete.");
            return;
        }

        if (file.delete()) {
            // Clear all static lists
            Product.products.clear();
            Customer.ListC.clear();
            Seller.sellerList.clear();
            Supplier.supplier.clear();
            Order.getOrderList().clear();

            showSuccess("Data file deleted successfully!");
            logger.log(Level.INFO, "Data file deleted and all lists cleared.");
        } else {
            throw new IOException("Failed to delete file");
        }
    } catch (Exception e) {
        String errorMessage = "Error deleting data file: " + e.getMessage();
        showError(errorMessage);
        logger.log(Level.SEVERE, errorMessage, e);
    }
}
    
    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.getDialogPane().setStyle("-fx-background-color: " + UIStyles.BACKGROUND_COLOR + ";");
            alert.showAndWait();
        });
    }

    private void showSuccess(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.getDialogPane().setStyle("-fx-background-color: " + UIStyles.BACKGROUND_COLOR + ";");
            alert.showAndWait();
        });
    }

    @Override
    public void stop() {
        try {
            executorService.shutdown();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error shutting down executor service", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}