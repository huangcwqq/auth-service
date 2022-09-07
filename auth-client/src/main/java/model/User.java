package model;

/**
 * User store model
 */
public class User {

  //unique id
  private Long id;

  //unique username
  private String username;

  // the salt use to encrypt
  private String salt;

  // encrypted password
  private String passWord;

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

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getPassWord() {
    return passWord;
  }

  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }
}
