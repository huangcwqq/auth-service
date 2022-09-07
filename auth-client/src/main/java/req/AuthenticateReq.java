package req;

/**
 * the request use to authenticate
 */
public class AuthenticateReq {

    // unique username
    private String username;

    // original password
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
