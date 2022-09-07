package req;

/**
 * check role request
 */
public class CheckRoleReq {
    // auth token
    String token;

    // unique role id
    Long roleId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
