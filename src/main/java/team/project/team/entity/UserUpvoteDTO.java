package team.project.team.entity;

import java.math.BigInteger;

public class UserUpvoteDTO {// i create this class to return only the username and upvotes, i researched and found this was a popular method of returning related data
    private String username;
    private BigInteger upvotes;
    
    public UserUpvoteDTO(String username, BigInteger upvotes) {//this class is like a regualar class with getter and setters but it used the jpa interface to pull the username and upvote in the userservice class and map them together.
        this.username= username;
        this.upvotes = upvotes;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public BigInteger getUpvotes() {
        return upvotes;
    }
    
    public void setUpvotes(BigInteger upvotes) {
        this.upvotes = upvotes;
    }
}