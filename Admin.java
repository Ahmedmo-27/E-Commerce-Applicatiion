package com.mycompany.ecommerceproject;

import static com.mycompany.ecommerceproject.Seller.sellerList;
import static com.mycompany.ecommerceproject.Supplier.supplier;
import java.io.Serializable;
import java.util.ArrayList;

public class Admin extends User implements Serializable {
    
  
    protected static ArrayList<Admin>List1=new ArrayList<>();
     
    public Admin()
    {
        super("Admin","1234");
    }
  
    
    
 public Admin(String Username, String Password) {
    super(Username, Password);
    List1.add(this);
}

    public static void setList1(ArrayList<Admin> List1) {
        Admin.List1 = List1;
    }

    public static ArrayList<Admin> getList1() {
        return List1;
    }
     
    
    
    public static Admin Login(String Username,String Password){
    for (int i =0;i<List1.size();i++){
        if((Username.equals(List1.get(i).Username))&&Password.equals(List1.get(i).Password))
            return List1.get(i);
    
    }
    return null;
    }
    public void ShowCustomerList(){
        
       System.out.println(Customer.ListC);
        
        }
    
    
         public void ShowSellerList(){
        
            System.out.println(Seller.sellerList);
       }
       
         public void ShowSupplierList(){
         
         System.out.println(Supplier.supplier);
         }
         
         
         
         
public boolean AddCustomers(String Username,String Password ){

new Customer(Username,Password);
System.out.println("Customer has been added successfully");
return true;
}


 public boolean AddSeller(String Username,String Password) {
 new Seller(Username,Password);
 System.out.println("Seller has been added successfully");
 return true;
 
 }   
 

 public boolean AddSupplier(String companyName){
 new Supplier(companyName);
 System.out.println("Supplier has been added successfully");
 return true;
 
 
 }
 
 
       public boolean RemoveCustomer(int id){
       if (Customer.removeById(id)){
       System.out.println("customer with id: "+id + "has been successfully removed by admin: "+ this.Username);
       return true;
       }
       else 
           System.out.println("Customer not found");
       return false;
       } 
       
       
           public boolean RemoveSeller(String name){
           if(Seller.removeSellerByName(name)){
           System.out.println("Seller: "+name + "has been successfully removed by admin: "+ this.Username);
           return true;
           
           }
           else 
              System.out.println("Customer not found"); 
           return false;
           
           }
           
           public boolean RemoveSupplier(String companyName){
           if(Supplier.remove(companyName)){
               System.out.println("Supplier: "+companyName + " has been successfully removed by admin: "+ this.Username);
               return true;
           
           }
           else 
              System.out.println("Supplier not found"); 
           return false;
           
           
           }
           public void AddProduct(int ProductId,String Name,int UnitsSold,int TotalStock,int price,String Category, String seller , String sup){
           Product newProduct =new Product( ProductId , Name, UnitsSold, TotalStock, price, Category, seller,sup);
   Product.products.add(newProduct);
           
           
           }
           
           public void SupplierWithMaxRevenue(){
               Supplier max = supplier.get(0);
           for(Supplier s : Supplier.supplier){
           if(s.getRevenue()>max.getRevenue()){
           max=s;
           }
           }
           System.out.println("Supplier with the highest revenue is: "+ max.getCompanyName() + " : " + max.getRevenue());
           }
           
           
              public void SellerWithMaxRevenue(){
               Seller max = sellerList.get(0);
           for(Seller s : Seller.sellerList){
           if(s.getRevenue()>max.getRevenue()){
           max=s;
           }
           }
           System.out.println("Supplier with the highest revenue is: "+ max.Username + " : " + max.getRevenue());
           }
              
              public void customerMaxRevenue(){
                  Customer max=Customer.ListC.get(0);
              for(Customer c : Customer.ListC){
              if(c.revenue>max.revenue){
              max=c;
              }
              }
              System.out.println("Customer with the most amount spent : "+max.Username);
              System.out.println("Spent: "+max.revenue);
              }
              
              public void MostRevenueProduct(){
              Product max = Product.products.get(0);
              for(Product p : Product.products){
              if(p.TotalRevenue>max.TotalRevenue){
              max=p;
              }
              
              }
                 System.out.println("Product with the most revenue: "+max.Name);
                 System.out.println("Revenue: "+max.TotalRevenue);
              }
              
              public void BestSeller(){
               Product max = Product.products.get(0);
              for(Product p : Product.products){
              if(p.UnitsSold>max.UnitsSold){
              max=p;
              }
              
              } 
                 System.out.println("Product with the most revenue: "+max.Name);
                 System.out.println("Revenue: "+max.UnitsSold);
              
              }
              
       
        @Override
   public String toString(){
    
       return "Admin Username: "+Username;
   
   } 
    
    

    
}