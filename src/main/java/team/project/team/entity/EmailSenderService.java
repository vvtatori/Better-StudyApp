/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package team.project.team.entity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * @author samor
 */
@Service
public class EmailSenderService {
     
    @Autowired
    private JavaMailSender mailSender;
    
    StringBuilder ss = new StringBuilder();
    String code;
    
    public void sendMail(String toEmail) throws MessagingException{
        
        int randomLetter = 0;
        int secondRandomLetter = 0;
        int thirdRandomLetter = 0;
        int letterPos = 0;
        int secondLetterPos = 0;
        int thirdLetterPos = 0;

        ss.setLength(0);//reset stringbuilder because i was having issues new emails taking prior codes and adding on to it
        
        
        //SimpleMailMessage message = new SimpleMailMessage();
        MimeMessage message = mailSender.createMimeMessage();//switched to mimemessage because i couldnt use simplemailmessages with html content
        MimeMessageHelper mime = new MimeMessageHelper(message,true);
        
        Random random = new Random();
        
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        
            
        String passwordCode = String.valueOf(random.nextInt(99999)+10000);//used to generate number from 10000-99999 for a secure code   
        randomLetter = random.nextInt(26);//random number generator to get a random letter from the alphabet
        secondRandomLetter = random.nextInt(26);//..
        thirdRandomLetter = random.nextInt(26);//..        
        letterPos = random.nextInt(5)+1;//random number generator used to insert a letter at a random position in the 5 digit code
        secondLetterPos = random.nextInt(6)+1;//..
        thirdLetterPos = random.nextInt(7)+1;//..

        ss.append(passwordCode);//appends code
        ss.insert(letterPos,letters[randomLetter ]);//inserts random letter
        ss.insert(secondLetterPos,letters[secondRandomLetter]);//inserts random letter
        ss.insert(thirdLetterPos,letters[thirdRandomLetter]);//inserts random letter
        code = ss.toString();
    String emailContent = "<!DOCTYPE html>" +
    "<html lang=\"en\">" +
    "<head>" +
    "<meta charset=\"UTF-8\">" +
    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
    "<title>Password Reset</title>" +
    "</head>" +
    "<body style=\"background-color: #f4f4f4; padding: 20px; text-align: center;\">" +
    "<div style=\"background: #ffffff; padding: 20px; border-radius: 10px; width: 80%; max-width: 400px; margin: auto;\">" +
        "<img src=\"https://i.imgur.com/gELcCLN.jpeg\" width=\"50\" height=\"50\" alt=\"Book\">" +
        "<p>Your password reset code is:</p>" +
        "<p style=\"font-size: 24px; font-weight: bold; background: #cccfca; padding: 10px; border-radius: 5px;\">" +
            ss.toString() + 
        "</p>" +
        "<p><strong>From BetterStudy</strong></p>" +
    "</div>" +
    "</body>" +
    "</html>";            
        
        


        
        mime.setFrom("teamproject23756575@gmail.com");
        mime.setTo(toEmail);
        mime.setSubject("Your reset code");    
        mime.setText(emailContent,true);
        
        mailSender.send(message);
        
        System.out.println("mail sent succesfully");    
        
    }
    public String getCode(){
          return code;
                  
    } 
    
}
