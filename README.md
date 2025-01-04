# Java E-Commerce System

A JavaFX-based e-commerce application that provides a comprehensive platform for managing products, users, orders, and inventory. The system supports multiple user roles including administrators, sellers, and customers.

## Features

### User Management
- Multiple user roles (Admin, Seller, Customer)
- Secure login and account creation
- Role-specific dashboards and functionalities

### Product Management
- Add, remove, and modify products
- Product categorization
- Inventory tracking
- Product search functionality
- Supplier management

### Shopping Features
- Shopping cart functionality
- Order processing
- Order history tracking
- Receipt generation

### Admin Features
- User management (add/remove users)
- System-wide product management
- Revenue analysis and reporting
- Performance metrics tracking

### Seller Features
- Product inventory management
- Sales tracking
- Revenue analysis
- Best-selling product metrics

### Data Management
- Persistent data storage
- Data backup and recovery
- File-based data management

## Technical Details

### Technologies Used
- Java
- JavaFX for GUI
- Concurrent programming with ExecutorService
- File I/O for data persistence
- Logger for system monitoring

### Architecture
- Object-oriented design
- MVC pattern implementation
- Service-based architecture
- Multi-threaded operations

### Key Components
- User authentication system
- Product service management
- Order processing system
- Cart management
- File operations handler

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- JavaFX SDK
- IDE supporting JavaFX (e.g., Eclipse, IntelliJ IDEA)

### Installation
1. Clone the repository
```bash
git clone [https://github.com/Ahmedmo-27/E-Commerce-Application]
```

2. Open the project in your preferred IDE

3. Ensure JavaFX SDK is properly configured in your development environment

4. Build and run the project

### Running the Application
1. Navigate to the project directory
2. Compile the source code
3. Run the main class `NewMain`

## Usage

### First-Time Setup
1. Launch the application
2. Create an admin account if not already present
3. Set up initial inventory and user accounts

### Basic Operations
1. **Login/Register**
   - Choose appropriate role
   - Enter credentials

2. **Customer Operations**
   - Browse products
   - Add items to cart
   - Place orders
   - View order history

3. **Seller Operations**
   - Manage product inventory
   - Track sales
   - View analytics

4. **Admin Operations**
   - Manage users
   - Overview system analytics
   - Generate reports

## Security Features
- Password protection
- Role-based access control
- Secure data storage

## Data Persistence
- Binary file storage
- Automatic data saving
- Data recovery options
