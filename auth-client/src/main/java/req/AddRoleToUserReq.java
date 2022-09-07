package req;

/**
 * the request use to add a role to a user
 */
public class AddRoleToUserReq {

    // unique user id
    private Long userId;

    // unique role id
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
