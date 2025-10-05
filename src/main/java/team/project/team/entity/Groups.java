/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 *
 * @author samor
 */
@Entity
@Table(name = "grouptable")
public class Groups {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// so every group has a different id
    private Long groupID;
    
    @Column(nullable = false)
    private String groupName;
    
    @Column//this sets the default value as false, so the group is public
    private boolean privacyOn;
    
    @Column(columnDefinition = "Integer default 1")
    private int memberAmount;
    
    @Column(nullable = true)
    private String groupType;
    
    @ManyToMany(mappedBy = "groups")
    private List<User> users;

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isPrivacyOn() {
        return privacyOn;
    }

    public void setPrivacyOn(boolean privacyOn) {
        this.privacyOn = privacyOn;
    }

    public int getMemberAmount() {
        return memberAmount;
    }

    public void setMemberAmount(int memberAmount) {
        this.memberAmount = memberAmount;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Groups( String groupName,int memberAmount,boolean privacyOn,String groupType) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.privacyOn = privacyOn;
        this.memberAmount = memberAmount;
        this.groupType = groupType;
    }
    public Groups(){
        
    }

    
}
