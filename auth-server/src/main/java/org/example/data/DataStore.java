package org.example.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.sun.istack.internal.NotNull;
import model.Role;
import model.User;
import model.UserRoleRelation;

/**
 * unify data store and operate
 */
public class DataStore {
  static AtomicLong incrementUserId;
  static AtomicLong incrementRoleId;
  static AtomicLong incrementUserRoleRelationId;
  static AtomicLong incrementTokenId;
  static volatile Map<String, User> tokenUserRelationMap;
  static volatile Map<String, Date> tokenDateRelationMap;
  static volatile List<UserRoleRelation> userRoleRelationList;
  static volatile List<User> userList;
  static volatile List<Role> roleList;

  static {
    init();
  }

  /**
   * store struct init
   */
  public static void init(){
    incrementUserId = new AtomicLong(1L);
    incrementRoleId = new AtomicLong(1L);
    incrementUserRoleRelationId = new AtomicLong(1L);
    incrementTokenId = new AtomicLong(1L);
    tokenUserRelationMap = new ConcurrentHashMap<>();
    tokenDateRelationMap = new ConcurrentHashMap<>();
    userRoleRelationList = new CopyOnWriteArrayList<>();
    userList = new CopyOnWriteArrayList<>();
    roleList = new CopyOnWriteArrayList<>();
  }

  /**
   * generate increment user id
   * @return user id
   */
  static Long getIncrementUserId() {
    return incrementUserId.getAndIncrement();
  }

  /**
   * generate increment role id
   * @return user id
   */
  static Long getIncrementRoleId() {
    return incrementRoleId.getAndIncrement();
  }

  /**
   * generate increment userRoleRelation id
   * @return user id
   */
  static Long getIncrementUserRoleRelationId() {
    return incrementUserRoleRelationId.getAndIncrement();
  }

  /**
   * generate increment token id
   * @return token id
   */
  public static Long getIncrementTokenId() {
    return incrementTokenId.getAndIncrement();
  }

  /**
   * add a user to userList
   * @param user user info
   * @return user info
   */
  public static User addUser(@NotNull User user) {
    Long id = getIncrementUserId();
    user.setId(id);
    userList.add(user);
    return user;
  }

  /**
   * get a user by id
   * @param id user id
   * @return user info
   */
  public static User getUserById(@NotNull Long id) {
    for (User user : userList) {
      if (user.getId().equals(id)) {
        return user;
      }
    }
    return null;
  }

  /**
   * get a user by username
   * @param userName username
   * @return user info
   */
  public static User getUserByUserName(@NotNull String userName) {
    for (User user : userList) {
      if (user.getUsername().equals(userName)) {
        return user;
      }
    }
    return null;
  }

  /**
   * delete a user by id
   * @param id user id
   * @return user id
   */
  public static Long delUser(@NotNull Long id) {
    int deleteIndex = -1;
    for(int i = 0;i<userList.size();i++){
      User user = userList.get(i);
      if (user.getId().equals(id)) {
        deleteIndex = i;
        break;
      }
    }
    if(deleteIndex != -1){
      userList.remove(deleteIndex);
      return id;
    }
    return null;
  }

  /**
   * add a role to roleList
   * @param role role info
   * @return role info
   */
  public static Role addRole(@NotNull Role role) {
    Long id = getIncrementRoleId();
    role.setId(id);
    roleList.add(role);
    return role;
  }

  /**
   * get a role by id
   * @param id role id
   * @return role info
   */
  public static Role getRoleById(@NotNull Long id) {
    for (Role role : roleList) {
      if (role.getId().equals(id)) {
        return role;
      }
    }
    return null;
  }

  /**
   * get a role by name
   * @param name role name
   * @return role info
   */
  public static Role getRoleByName(@NotNull String name) {
    for (Role role : roleList) {
      if (role.getName().equals(name)) {
        return role;
      }
    }
    return null;
  }

  /**
   * delete a role by id
   * @param id role id
   * @return role id
   */
  public static Long delRole(@NotNull Long id) {
    int deleteIndex = -1;
    for(int i = 0;i<roleList.size();i++){
      Role role = roleList.get(i);
      if (role.getId().equals(id)) {
        deleteIndex = i;
        break;
      }
    }
    if(deleteIndex != -1){
      roleList.remove(deleteIndex);
      return id;
    }
    return null;
  }

  /**
   * add userRoleRelation to userRoleRelationList
   * @param userRoleRelation relation between role and user
   * @return userRoleRelation
   */
  public static UserRoleRelation addUserRoleRelation(@NotNull UserRoleRelation userRoleRelation) {
    Long id = getIncrementUserRoleRelationId();
    userRoleRelation.setId(id);
    userRoleRelationList.add(userRoleRelation);
    return userRoleRelation;
  }

  /**
   * find UserRoleRelation by userId and roleId
   * @param userId user id
   * @param roleId role id
   * @return userRoleRelation
   */
  public static UserRoleRelation findUserRoleRelation(@NotNull Long userId,@NotNull Long roleId){
    for(UserRoleRelation userRoleRelation : userRoleRelationList){
      if(userRoleRelation.getRoleId().equals(roleId) && userRoleRelation.getUserId().equals(userId)){
        return userRoleRelation;
      }
    }
    return null;
  }

  /**
   * list user's roles by user id
   * @param userId user id
   * @return roleList
   */
  public static List<Role> listRolesByUserId(@NotNull Long userId){
    List<Long> roleIdList = new ArrayList<>();
    for(UserRoleRelation userRoleRelation : userRoleRelationList){
      if(userRoleRelation.getUserId().equals(userId)){
        roleIdList.add(userRoleRelation.getRoleId());
      }
    }
    if(CollectionUtil.isNotEmpty(roleIdList)){
      List<Role> roles = new ArrayList<>();
      for(Long roleId : roleIdList){
        final Role roleById = getRoleById(roleId);
        roles.add(roleById);
      }
      return roles;
    }
    return new ArrayList<>();
  }

  /**
   * del the relation between role and userId
   * @param userid user id
   * @param roleId role id
   * @return deleteId
   */
  public static Long delUserRoleRelation(@NotNull Long userid,@NotNull Long roleId) {
    int deleteIndex = -1;
    for(int i = 0;i < userRoleRelationList.size();i++){
      UserRoleRelation relation = userRoleRelationList.get(0);
      if(relation.getUserId().equals(userid) && relation.getRoleId().equals(roleId)){
        deleteIndex = i;
        break;
      }
    }
    if(deleteIndex != -1){
      Long deletedId = userRoleRelationList.get(deleteIndex).getId();
      userRoleRelationList.remove(deleteIndex);
      return deletedId;
    }
    return null;
  }

  /**
   * set the relation between token and user
   * @param token token
   * @param user user info
   */
  public static void setTokenAndUser(@NotNull String token,@NotNull User user) {
    tokenUserRelationMap.put(token, user);
    tokenDateRelationMap.put(token,new Date());
  }

  /**
   * remove the token from the map
   * @param token token
   */
  public static void removeToken(@NotNull String token) {
    tokenUserRelationMap.remove(token);
    tokenDateRelationMap.remove(token);
  }

  /**
   * get user info from token
   * @param token token
   * @return user info
   */
  public static User getUserByToken(@NotNull String token) {
    Date nowDate = new Date();
    final Date tokenDate = tokenDateRelationMap.get(token);
    if(tokenDate == null){
      return null;
    }
    final DateTime twoHoursBefore = DateUtil.offsetHour(nowDate, -2);
    final int compare = DateUtil.compare(tokenDate, twoHoursBefore);
    if(compare < 0){
      tokenUserRelationMap.remove(token);
      tokenDateRelationMap.remove(token);
      return null;
    }
    return tokenUserRelationMap.get(token);
  }

}
