package resp;

import model.User;

/**
 * thr response data after authenticate success
 */
public class AuthenticateResp {

    // auth user info
    User user;

    // a special secret auth token
    String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
