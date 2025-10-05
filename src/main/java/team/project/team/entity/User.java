/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author samor
 */
@Entity
public class User {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// so every user has a different id
    private Long id;

    @Column(nullable = false, unique = true) // this ensures the username is unique
    private String username;
    
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private BigInteger upvotes;

    public BigInteger getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(BigInteger upvotes) {
        this.upvotes = upvotes;
    }
    //Mapping to the profile page to link a user to their profile
    @JsonManagedReference
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Profile profile; // One-to-One relationship with Profile

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
    
    @ManyToMany
    @JoinTable(
        name = "user_group", // This is the join table
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "groupID")
    )
    private List<Groups> groups;
    
    public List<Groups> getGroups(){//returns all user groups
        return groups;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String email, String firstName, String lastName, String password, String username,BigInteger upvotes) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.upvotes = upvotes;
    }
    public User(){
        
    }
}
