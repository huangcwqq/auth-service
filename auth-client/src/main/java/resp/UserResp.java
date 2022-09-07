package resp;

/**
 * return the user without the salt and password
 */
public class UserResp {
    //unique id
    private Long id;

    //unique username
    private String username;

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
}
