/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.entity;

/**
 *
 * @author samor
 */
public class Users {
    
    private String email,firstName,lastName,password,userName;
    
    public Users(String email,String firstName,String lastName,String password,String userName){
        
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userName = userName;        
    }
    
}
