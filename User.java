package com.mycompany.ecommerceproject;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class User implements Serializable
{
    protected int UserId;
    protected String Username;
    protected String Password;
    protected static ArrayList<User> List = new ArrayList<>();

    public User(String Username, String Password) 
    {
        if (Username == null || Username.isEmpty() || Password == null || Password.isEmpty()) {
            throw new IllegalArgumentException("Username and Password cannot be null or empty");
        }
        this.Username = Username;
        this.Password = Password;
        List.add(this);
    }

    public static User login(String Username, String Password) 
    {
        for (int i=0;i<List.size();i++) 
        {
            if((Username.equals(List.get(i).Username))&&Password.equals(List.get(i).Password))
            {
                return List.get(i);
            }
        }
        return null;
    }

    public int getUserId() 
    {
        return UserId;
    }

    public String getUsername() 
    {
        return Username;
    }
    
}