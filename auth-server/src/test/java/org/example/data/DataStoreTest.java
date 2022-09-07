package org.example.data;

import cn.hutool.core.date.DateUtil;
import model.Role;
import model.User;
import model.UserRoleRelation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class DataStoreTest {

    @BeforeEach
    void setUp() {
        DataStore.init();
    }

    User initOneUserInUserList(){
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setSalt("salt");
        user.setPassWord("password");
        DataStore.userList.add(user);
        return user;
    }

    Role initOneRoleInRoleList(){
        Role role = new Role();
        role.setId(1L);
        role.setName("roleName");
        DataStore.roleList.add(role);
        return role;
    }

    UserRoleRelation initOneUserRoleRelationList(){
        initOneUserInUserList();
        initOneRoleInRoleList();
        UserRoleRelation userRoleRelation = new UserRoleRelation();
        userRoleRelation.setId(1L);
        userRoleRelation.setUserId(1L);
        userRoleRelation.setRoleId(1L);
        DataStore.userRoleRelationList.add(userRoleRelation);
        return userRoleRelation;
    }

    String initTokenAndUserRelation(Date date){
        final User user = initOneUserInUserList();
        String token = "20220905001_1_123";

        DataStore.tokenUserRelationMap.put(token,user);
        DataStore.tokenDateRelationMap.put(token,date);
        return token;
    }

    @Test
    void getIncrementUserId() {
        for(long i = 1L;i < 10000;i++){
            final Long userId = DataStore.getIncrementUserId();
            Assertions.assertEquals(userId,i);
        }
    }

    @Test
    void concurrentGetIncrementUserId() {
        int count = 10;
        List<FutureTask<Long>> futureTaskList = new ArrayList<>();
        for(int i = 0;i<count;i++){
            FutureTask<Long> task= new FutureTask<>(DataStore::getIncrementUserId);
            futureTaskList.add(task);
            new Thread(task).start();
        }
        Set<Long> userIdSet = new HashSet();
        for(FutureTask<Long> futureTask : futureTaskList){
            try {
                final Long userId = futureTask.get();
                userIdSet.add(userId);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(userIdSet.size(),count);
    }

    @Test
    void getIncrementRoleId() {
        for(long i = 1L;i < 10000;i++){
            final Long roleId = DataStore.getIncrementRoleId();
            Assertions.assertEquals(roleId,i);
        }
    }

    @Test
    void concurrentGetIncrementRoleId() {
        int count = 10;
        List<FutureTask<Long>> futureTaskList = new ArrayList<>();
        for(int i = 0;i<count;i++){
            FutureTask<Long> task= new FutureTask<>(DataStore::getIncrementRoleId);
            futureTaskList.add(task);
            new Thread(task).start();
        }
        Set<Long> userIdSet = new HashSet();
        for(FutureTask<Long> futureTask : futureTaskList){
            try {
                final Long userId = futureTask.get();
                userIdSet.add(userId);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(userIdSet.size(),count);
    }

    @Test
    void getIncrementUserRoleRelationId() {
        for(long i = 1L;i < 10000;i++){
            final Long userRoleRelationId = DataStore.getIncrementUserRoleRelationId();
            Assertions.assertEquals(userRoleRelationId,i);
        }
    }

    @Test
    void concurrentGetIncrementUserRoleRelationId() {
        int count = 10;
        List<FutureTask<Long>> futureTaskList = new ArrayList<>();
        for(int i = 0;i<count;i++){
            FutureTask<Long> task= new FutureTask<>(DataStore::getIncrementUserRoleRelationId);
            futureTaskList.add(task);
            new Thread(task).start();
        }
        Set<Long> userIdSet = new HashSet();
        for(FutureTask<Long> futureTask : futureTaskList){
            try {
                final Long userId = futureTask.get();
                userIdSet.add(userId);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(userIdSet.size(),count);
    }

    @Test
    void getIncrementTokenId() {
        for(long i = 1L;i < 10000;i++){
            final Long tokenId = DataStore.getIncrementTokenId();
            Assertions.assertEquals(tokenId,i);
        }
    }

    @Test
    void concurrentGetIncrementTokenId() {
        int count = 10;
        List<FutureTask<Long>> futureTaskList = new ArrayList<>();
        for(int i = 0;i<count;i++){
            FutureTask<Long> task= new FutureTask<>(DataStore::getIncrementTokenId);
            futureTaskList.add(task);
            new Thread(task).start();
        }
        Set<Long> userIdSet = new HashSet();
        for(FutureTask<Long> futureTask : futureTaskList){
            try {
                final Long userId = futureTask.get();
                userIdSet.add(userId);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(userIdSet.size(),count);
    }

    @Test
    void addUser() {
        User user = new User();
        user.setUsername("username");
        user.setSalt("salt");
        user.setPassWord("password");
        DataStore.addUser(user);
        Assertions.assertEquals(DataStore.userList.size(),1);
        Assertions.assertEquals(DataStore.userList.get(0).getId(),1L);
        Assertions.assertEquals(DataStore.userList.get(0).getUsername(),"username");
        Assertions.assertEquals(DataStore.userList.get(0).getSalt(),"salt");
        Assertions.assertEquals(DataStore.userList.get(0).getPassWord(),"password");
    }

    @Test
    void testGetUserById() {
        initOneUserInUserList();

        final User userById = DataStore.getUserById(9999L);
        Assertions.assertNull(userById);

        final User userById2 = DataStore.getUserById(1L);
        Assertions.assertNotNull(userById2);
        Assertions.assertEquals(userById2.getId(),1L);
    }

    @Test
    void testGetUserByUserName() {
        initOneUserInUserList();

        final User userByUserName = DataStore.getUserByUserName("username_not_exist");
        Assertions.assertNull(userByUserName);

        final User userByUserName2 = DataStore.getUserByUserName("username");
        Assertions.assertNotNull(userByUserName2);
        Assertions.assertEquals(userByUserName2.getUsername(),"username");
    }

    @Test
    void testDelUser() {
        initOneUserInUserList();

        final Long deleteId = DataStore.delUser(9999L);
        Assertions.assertNull(deleteId);

        final Long deleteSuccessId = DataStore.delUser(1L);
        Assertions.assertEquals(deleteSuccessId,1L);
        Assertions.assertEquals(DataStore.userList.size(),0);
    }

    @Test
    void addRole() {
        Role role = new Role();
        role.setName("roleName");
        final Role addRole = DataStore.addRole(role);
        Assertions.assertEquals(DataStore.roleList.size(),1);
        Assertions.assertEquals(addRole.getId(),1L);
        Assertions.assertEquals(addRole.getName(),"roleName");
    }

    @Test
    void getRoleById() {
        initOneRoleInRoleList();

        final Role roleById = DataStore.getRoleById(9999L);
        Assertions.assertNull(roleById);

        final Role roleById2 = DataStore.getRoleById(1L);
        Assertions.assertNotNull(roleById2);
        Assertions.assertEquals(roleById2.getId(),1L);
    }

    @Test
    void getRoleByName() {
        initOneRoleInRoleList();

        final Role roleByName = DataStore.getRoleByName("roleName_not_exist");
        Assertions.assertNull(roleByName);

        final Role roleByName2 = DataStore.getRoleByName("roleName");
        Assertions.assertNotNull(roleByName2);
        Assertions.assertEquals(roleByName2.getName(),"roleName");
    }

    @Test
    void delRole() {
        initOneRoleInRoleList();

        final Long deleteId = DataStore.delRole(9999L);
        Assertions.assertNull(deleteId);

        final Long deleteSuccessId = DataStore.delRole(1L);
        Assertions.assertNotNull(deleteSuccessId);
        Assertions.assertEquals(DataStore.roleList.size(),0);
    }

    @Test
    void addUserRoleRelation() {
        UserRoleRelation relation = new UserRoleRelation();
        relation.setUserId(1L);
        relation.setRoleId(1L);
        final UserRoleRelation userRoleRelation = DataStore.addUserRoleRelation(relation);
        Assertions.assertEquals(DataStore.userRoleRelationList.size(),1L);
        Assertions.assertEquals(userRoleRelation.getId(),1L);
        Assertions.assertEquals(userRoleRelation.getUserId(),1L);
        Assertions.assertEquals(userRoleRelation.getRoleId(),1L);
    }

    @Test
    void findUserRoleRelation() {
        initOneUserRoleRelationList();

        final UserRoleRelation relation = DataStore.findUserRoleRelation(9999L, 1L);
        Assertions.assertNull(relation);

        final UserRoleRelation relation2 = DataStore.findUserRoleRelation(1L, 9999L);
        Assertions.assertNull(relation2);

        final UserRoleRelation relationExist = DataStore.findUserRoleRelation(1L, 1L);
        Assertions.assertNotNull(relationExist);
        Assertions.assertEquals(relationExist.getUserId(),1L);
        Assertions.assertEquals(relationExist.getRoleId(),1L);
    }

    @Test
    void listRolesByUserId() {
        initOneUserRoleRelationList();

        final List<Role> roleList = DataStore.listRolesByUserId(9999L);
        Assertions.assertEquals(roleList.size(),0);

        final List<Role> existRoleList = DataStore.listRolesByUserId(1L);
        Assertions.assertEquals(existRoleList.size(),1);
        Assertions.assertEquals(existRoleList.get(0).getId(),1L);
    }

    @Test
    void delUserRoleRelation() {
        initOneUserRoleRelationList();

        final Long deleteId = DataStore.delUserRoleRelation(1L, 9999L);
        Assertions.assertNull(deleteId);
        Assertions.assertEquals(DataStore.userRoleRelationList.size(),1);

        final Long deleteSuccessId = DataStore.delUserRoleRelation(1L, 1L);
        Assertions.assertNotNull(deleteSuccessId);
        Assertions.assertEquals(DataStore.userRoleRelationList.size(),0);
    }

    @Test
    void setTokenAndUser() {
        User user = initOneUserInUserList();

        String token = "20220905001_1_123";
        DataStore.setTokenAndUser(token,user);

        Assertions.assertEquals(DataStore.tokenUserRelationMap.size(),1);
        Assertions.assertEquals(DataStore.tokenDateRelationMap.size(),1);
    }

    @Test
    void removeToken() {
        final String token = initTokenAndUserRelation(new Date());

        DataStore.removeToken(token);

        Assertions.assertEquals(DataStore.tokenUserRelationMap.size(),0);
        Assertions.assertEquals(DataStore.tokenDateRelationMap.size(),0);
    }

    @Test
    void testGetUserByToken() {
        final String token = initTokenAndUserRelation(new Date());

        final User userByToken = DataStore.getUserByToken("token_not_exist");
        Assertions.assertNull(userByToken);

        final User existUser = DataStore.getUserByToken(token);
        Assertions.assertNotNull(existUser);
        Assertions.assertEquals(existUser.getId(),1L);
    }

    @Test
    void testUserByInvalidToken() {
        Date date = DateUtil.offsetHour(new Date(),-3);
        final String token = initTokenAndUserRelation(date);

        final User expireTokenUser = DataStore.getUserByToken(token);
        Assertions.assertNull(expireTokenUser);
        Assertions.assertEquals(DataStore.tokenUserRelationMap.size(),0);
        Assertions.assertEquals(DataStore.tokenDateRelationMap.size(),0);
    }
}