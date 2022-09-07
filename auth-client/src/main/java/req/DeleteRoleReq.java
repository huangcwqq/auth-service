package req;

/**
 * the request to delete role
 */
public class DeleteRoleReq {

    // unique role key
    Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
