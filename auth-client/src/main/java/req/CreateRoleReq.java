package req;

/**
 * the request use to create role
 */
public class CreateRoleReq {

    // unique role name
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
