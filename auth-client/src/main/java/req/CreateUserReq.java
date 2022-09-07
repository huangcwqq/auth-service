package req;

/**
 * the request use to create user
 */
public class CreateUserReq {

    // username
    private String username;

    // password
    private String passWord;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
