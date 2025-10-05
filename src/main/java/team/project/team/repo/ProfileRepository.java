/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package team.project.team.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team.project.team.dto.ProfileDTO;
import team.project.team.entity.Profile;

/**
 *
 * @author vvtat
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>{
    // Find a profile using the user's ID (inside the User entity)
    Profile findByUserId(Long id);
    
    // Custom query to fetch profile details along with username and email from the users table. 
    @Query("SELECT new team.project.team.dto.ProfileDTO( " +
       "COALESCE(p.bio, ''), COALESCE(p.school, ''), COALESCE(p.course, ''), " +
       "COALESCE(p.profileImage, 'darkmode.png'), COALESCE(u.username, ''), COALESCE(u.email, '') ) " +
       "FROM Profile p JOIN p.user u WHERE p.user.id = :id")  //using join to join the two tables, user and profile through their user id for fecth the data
    ProfileDTO findProfileWithUserDetails(@Param("id") Long id);
}
