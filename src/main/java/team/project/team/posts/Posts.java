package team.project.team.posts;

import jakarta.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
public class Posts {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postID;

    @Column(nullable = false)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    private String postContent;
    
    @Column(nullable = false)
    private LocalDateTime timeSent;
    
    private String base64File;//used to convert from blob to base64

    public String getBase64File() {
        return base64File;
    }

    public void setBase64File(String base64File) {
        this.base64File = base64File;
    }
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false)
    private String contentType;
 
    @Column(nullable = false)
    private Long fileLength;
    
    @Column
    private Boolean isReadable;
    
    @Column
    private Boolean isFileEmpty;
    
    @Column 
    private BigInteger postLikes;

    @Column
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
 
    
    
    public BigInteger getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(BigInteger postLikes) {
        this.postLikes = postLikes;
    }
    
        
    public Boolean getIsReadable() {
        return isReadable;
    }

    public void setIsReadable(Boolean isReadable) {
        this.isReadable = isReadable;
    }

    public Boolean getIsFileEmpty() {
        return isFileEmpty;
    }

    public void setIsFileEmpty(Boolean isFileEmpty) {
        this.isFileEmpty = isFileEmpty;
    }
    
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long  fileLength) {
        this.fileLength = fileLength;
    }
    
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public LocalDateTime getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(LocalDateTime timeSent) {
        this.timeSent = timeSent;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
    public Posts(){
        
    }

    public Posts(Long id, String postContent, LocalDateTime timeSent, String base64File, String fileName, String contentType, Long fileLength, Boolean isReadable, Boolean isFileEmpty, BigInteger postLikes, String username, byte[] fileData) {
        this.id = id;
        this.postContent = postContent;
        this.timeSent = timeSent;
        this.base64File = base64File;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileLength = fileLength;
        this.isReadable = isReadable;
        this.isFileEmpty = isFileEmpty;
        this.postLikes = postLikes;
        this.username = username;
        this.fileData = fileData;
    }





    
}