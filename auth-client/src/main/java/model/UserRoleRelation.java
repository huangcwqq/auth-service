package model;

/**
 * the model store the relation between role and user
 */
public class UserRoleRelation {
  //unique id
  private Long id;

  //unique user key
  private Long userId;

  //unique role key
  private Long roleId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
