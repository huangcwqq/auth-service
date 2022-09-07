package resp;

import model.User;

/**
 * thr response data after authenticate success
 */
public class AuthenticateResp {

    // auth user info
    UserResp userResp;

    // a special secret auth token
    String token;

    public UserResp getUser() {
        return userResp;
    }

    public void setUser(UserResp userResp) {
        this.userResp = userResp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
