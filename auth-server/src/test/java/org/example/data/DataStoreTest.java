package org.example.data;

import cn.hutool.core.date.DateUtil;
import model.Role;
import model.TokenInfo;
import model.User;
import model.UserRoleRelation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

class DataStoreTest {

    public static final Long NOT_EXIST_USER_ID = 9999L;
    public static final Long NOT_EXIST_ROLE_ID = 9999L;
    public static final String NOT_EXIST_USERNAME = "username_not_exist";
    public static final String NOT_EXIST_ROLE_NAME = "roleName_not_exist";
    public static final String CORRECT_TOKEN = "20220905001_1_123";

    @BeforeEach
    void setUp() {
        DataStore.init();
    }

    User initOneUserInUserList() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setSalt("salt");
        user.setPassWord("password");
        DataStore.userList.add(user);
        return user;
    }

    Role initOneRoleInRoleList() {
        Role role = new Role();
        role.setId(1L);
        role.setName("roleName");
        DataStore.roleList.add(role);
        return role;
    }

    UserRoleRelation initOneUserRoleRelationList() {
        final User user = initOneUserInUserList();
        final Role role = initOneRoleInRoleList();
        UserRoleRelation userRoleRelation = new UserRoleRelation();
        userRoleRelation.setId(1L);
        userRoleRelation.setUserId(user.getId());
        userRoleRelation.setRoleId(role.getId());
        DataStore.userRoleRelationList.add(userRoleRelation);
        return userRoleRelation;
    }

    String initTokenAndUserRelation(Date date) {
        final User user = initOneUserInUserList();

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(user.getId());
        tokenInfo.setCreateAt(date);
        DataStore.tokenInfoMap.put(CORRECT_TOKEN, tokenInfo);
        return CORRECT_TOKEN;
    }

    @Test
    void getIncrementUserId() {
        for (long i = 1L; i < 10000; i++) {
            final Long userId = DataStore.getIncrementUserId();
            Assertions.assertEquals(userId, i);
        }
    }

    @Test
    void concurrentGetIncrementUserId() {
        int count = 10;
        List<FutureTask<Long>> futureTaskList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FutureTask<Long> task = new FutureTask<>(DataStore::getIncrementUserId);
            futureTaskList.add(task);
            new Thread(task).start();
        }
        Set<Long> userIdSet = new HashSet<>();
        for (FutureTask<Long> futureTask : futureTaskList) {
            try {
                final Long userId = futureTask.get();
                userIdSet.add(userId);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(userIdSet.size(), count);
    }

    @Test
    void getIncrementRoleId() {
        for (long i = 1L; i < 10000; i++) {
            final Long roleId = DataStore.getIncrementRoleId();
            Assertions.assertEquals(roleId, i);
        }
    }

    @Test
    void concurrentGetIncrementRoleId() {
        int count = 10;
        List<FutureTask<Long>> futureTaskList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FutureTask<Long> task = new FutureTask<>(DataStore::getIncrementRoleId);
            futureTaskList.add(task);
            new Thread(task).start();
        }
        Set<Long> userIdSet = new HashSet<>();
        for (FutureTask<Long> futureTask : futureTaskList) {
            try {
                final Long userId = futureTask.get();
                userIdSet.add(userId);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(userIdSet.size(), count);
    }

    @Test
    void getIncrementUserRoleRelationId() {
        for (long i = 1L; i < 10000; i++) {
            final Long userRoleRelationId = DataStore.getIncrementUserRoleRelationId();
            Assertions.assertEquals(userRoleRelationId, i);
        }
    }

    @Test
    void concurrentGetIncrementUserRoleRelationId() {
        int count = 10;
        List<FutureTask<Long>> futureTaskList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FutureTask<Long> task = new FutureTask<>(DataStore::getIncrementUserRoleRelationId);
            futureTaskList.add(task);
            new Thread(task).start();
        }
        Set<Long> userIdSet = new HashSet<>();
        for (FutureTask<Long> futureTask : futureTaskList) {
            try {
                final Long userId = futureTask.get();
                userIdSet.add(userId);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(userIdSet.size(), count);
    }

    @Test
    void getIncrementTokenId() {
        for (long i = 1L; i < 10000; i++) {
            final Long tokenId = DataStore.getIncrementTokenId();
            Assertions.assertEquals(tokenId, i);
        }
    }

    @Test
    void concurrentGetIncrementTokenId() {
        int count = 10;
        List<FutureTask<Long>> futureTaskList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FutureTask<Long> task = new FutureTask<>(DataStore::getIncrementTokenId);
            futureTaskList.add(task);
            new Thread(task).start();
        }
        Set<Long> userIdSet = new HashSet<>();
        for (FutureTask<Long> futureTask : futureTaskList) {
            try {
                final Long userId = futureTask.get();
                userIdSet.add(userId);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        Assertions.assertEquals(userIdSet.size(), count);
    }

    @Test
    void addUser() {
        User user = new User();
        user.setUsername("username");
        user.setSalt("salt");
        user.setPassWord("password");
        DataStore.addUser(user);
        Assertions.assertEquals(DataStore.userList.size(), 1);
        Assertions.assertEquals(DataStore.userList.get(0).getId(), 1L);
        Assertions.assertEquals(DataStore.userList.get(0).getUsername(), "username");
        Assertions.assertEquals(DataStore.userList.get(0).getSalt(), "salt");
        Assertions.assertEquals(DataStore.userList.get(0).getPassWord(), "password");
    }

    @Test
    void testGetUserById() {
        final User user = initOneUserInUserList();

        final User userById = DataStore.getUserById(NOT_EXIST_USER_ID);
        Assertions.assertNull(userById);

        final User userById2 = DataStore.getUserById(user.getId());
        Assertions.assertNotNull(userById2);
        Assertions.assertEquals(userById2.getId(), user.getId());
    }

    @Test
    void testGetUserByUserName() {
        final User user = initOneUserInUserList();

        final User userByUserName = DataStore.getUserByUserName(NOT_EXIST_USERNAME);
        Assertions.assertNull(userByUserName);

        final User userByUserName2 = DataStore.getUserByUserName(user.getUsername());
        Assertions.assertNotNull(userByUserName2);
        Assertions.assertEquals(userByUserName2.getUsername(), user.getUsername());
    }

    @Test
    void testDelUser() {
        final User user = initOneUserInUserList();

        final Long deleteId = DataStore.delUser(NOT_EXIST_USER_ID);
        Assertions.assertNull(deleteId);

        final Long deleteSuccessId = DataStore.delUser(user.getId());
        Assertions.assertEquals(deleteSuccessId, user.getId());
        Assertions.assertEquals(DataStore.userList.size(), 0);
    }

    @Test
    void addRole() {
        Role role = new Role();
        role.setName("roleName");
        final Role addRole = DataStore.addRole(role);
        Assertions.assertEquals(DataStore.roleList.size(), 1);
        Assertions.assertEquals(addRole.getId(), 1L);
        Assertions.assertEquals(addRole.getName(), "roleName");
    }

    @Test
    void getRoleById() {
        final Role role = initOneRoleInRoleList();

        final Role roleById = DataStore.getRoleById(NOT_EXIST_ROLE_ID);
        Assertions.assertNull(roleById);

        final Role roleById2 = DataStore.getRoleById(role.getId());
        Assertions.assertNotNull(roleById2);
        Assertions.assertEquals(roleById2.getId(), role.getId());
    }

    @Test
    void getRoleByName() {
        final Role role = initOneRoleInRoleList();

        final Role roleByName = DataStore.getRoleByName(NOT_EXIST_ROLE_NAME);
        Assertions.assertNull(roleByName);

        final Role roleByName2 = DataStore.getRoleByName(role.getName());
        Assertions.assertNotNull(roleByName2);
        Assertions.assertEquals(roleByName2.getName(), role.getName());
    }

    @Test
    void delRole() {
        final Role role = initOneRoleInRoleList();

        final Long deleteId = DataStore.delRole(NOT_EXIST_ROLE_ID);
        Assertions.assertNull(deleteId);

        final Long deleteSuccessId = DataStore.delRole(role.getId());
        Assertions.assertNotNull(deleteSuccessId);
        Assertions.assertEquals(DataStore.roleList.size(), 0);
    }

    @Test
    void addUserRoleRelation() {
        final User user = initOneUserInUserList();
        final Role role = initOneRoleInRoleList();

        UserRoleRelation relation = new UserRoleRelation();
        relation.setUserId(user.getId());
        relation.setRoleId(role.getId());
        final UserRoleRelation userRoleRelation = DataStore.addUserRoleRelation(relation);
        Assertions.assertEquals(DataStore.userRoleRelationList.size(), 1);
        Assertions.assertEquals(userRoleRelation.getId(), 1L);
        Assertions.assertEquals(userRoleRelation.getUserId(), user.getId());
        Assertions.assertEquals(userRoleRelation.getRoleId(), role.getId());
    }

    @Test
    void findUserRoleRelation() {
        UserRoleRelation userRoleRelation = initOneUserRoleRelationList();

        final UserRoleRelation relation = DataStore.findUserRoleRelation(NOT_EXIST_USER_ID, userRoleRelation.getRoleId());
        Assertions.assertNull(relation);

        final UserRoleRelation relation2 = DataStore.findUserRoleRelation(userRoleRelation.getUserId(), NOT_EXIST_ROLE_ID);
        Assertions.assertNull(relation2);

        final UserRoleRelation relationExist = DataStore.findUserRoleRelation(userRoleRelation.getUserId(), userRoleRelation.getRoleId());
        Assertions.assertNotNull(relationExist);
        Assertions.assertEquals(relationExist.getUserId(), userRoleRelation.getUserId());
        Assertions.assertEquals(relationExist.getRoleId(), userRoleRelation.getRoleId());
    }

    @Test
    void listRolesByUserId() {
        final UserRoleRelation userRoleRelation = initOneUserRoleRelationList();

        final List<Role> roleList = DataStore.listRolesByUserId(NOT_EXIST_USER_ID);
        Assertions.assertEquals(roleList.size(), 0);

        final List<Role> existRoleList = DataStore.listRolesByUserId(userRoleRelation.getUserId());
        Assertions.assertEquals(existRoleList.size(), 1);
        Assertions.assertEquals(existRoleList.get(0).getId(), userRoleRelation.getId());
    }

    @Test
    void delUserRoleRelation() {
        final UserRoleRelation userRoleRelation = initOneUserRoleRelationList();

        final Long deleteId = DataStore.delUserRoleRelation(userRoleRelation.getUserId(), NOT_EXIST_ROLE_ID);
        Assertions.assertNull(deleteId);
        Assertions.assertEquals(DataStore.userRoleRelationList.size(), 1);

        final Long deleteSuccessId = DataStore.delUserRoleRelation(userRoleRelation.getUserId(), userRoleRelation.getRoleId());
        Assertions.assertNotNull(deleteSuccessId);
        Assertions.assertEquals(DataStore.userRoleRelationList.size(), 0);
    }

    @Test
    void setTokenAndUser() {
        User user = initOneUserInUserList();

        DataStore.setTokenAndUser(CORRECT_TOKEN, user.getId());

        Assertions.assertEquals(DataStore.tokenInfoMap.size(), 1);
    }

    @Test
    void removeToken() {
        final String token = initTokenAndUserRelation(new Date());

        DataStore.removeToken(token);

        Assertions.assertEquals(DataStore.tokenInfoMap.size(), 0);
    }

    @Test
    void testGetUserByToken() {
        final String token = initTokenAndUserRelation(new Date());

        final User userByToken = DataStore.getUserByToken("token_not_exist");
        Assertions.assertNull(userByToken);

        final User existUser = DataStore.getUserByToken(token);
        Assertions.assertNotNull(existUser);
        Assertions.assertEquals(existUser.getId(), 1L);
    }

    @Test
    void testUserByInvalidToken() {
        Date date = DateUtil.offsetHour(new Date(), -3);
        final String token = initTokenAndUserRelation(date);

        final User expireTokenUser = DataStore.getUserByToken(token);
        Assertions.assertNull(expireTokenUser);
        Assertions.assertEquals(DataStore.tokenInfoMap.size(), 0);
    }
}