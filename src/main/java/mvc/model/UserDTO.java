package mvc.model;

public class UserDTO {

    private int    id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String createdAt;
    private String updatedAt;

    public UserDTO() {
        super();
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public String getUsername()                 { return username; }
    public void setUsername(String username)    { this.username = username; }

    public String getPassword()                 { return password; }
    public void setPassword(String password)    { this.password = password; }

    public String getEmail()                    { return email; }
    public void setEmail(String email)          { this.email = email; }

    public String getNickname()                 { return nickname; }
    public void setNickname(String nickname)    { this.nickname = nickname; }

    public String getCreatedAt()                { return createdAt; }
    public void setCreatedAt(String createdAt)  { this.createdAt = createdAt; }

    public String getUpdatedAt()                { return updatedAt; }
    public void setUpdatedAt(String updatedAt)  { this.updatedAt = updatedAt; }
}