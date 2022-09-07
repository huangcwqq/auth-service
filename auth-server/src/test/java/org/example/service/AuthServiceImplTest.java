package org.example.service;

import model.Role;
import model.User;
import model.UserRoleRelation;
import org.example.common.AuthErrors;
import org.example.common.BusinessException;
import org.example.data.DataStore;
import org.example.utils.PassWordUtils;
import org.example.common.RestResponse;
import org.junit.jupiter.api.*;
import req.*;
import resp.AuthenticateResp;
import resp.UserResp;

import java.util.List;

class AuthServiceImplTest {

    AuthServiceImpl authService = new AuthServiceImpl();

    final static String EXIST_USERNAME = "exist_username";
    final static String CREATE_USERNAME = "create_username";
    final static String NOT_EXIST_USERNAME = "username_not_exist";
    final static String CORRECT_PASSWORD = "123456";
    final static String INCORRECT_PASSWORD = "654321";
    final static String EXIST_ROLE_NAME = "exist_role";
    final static String EXIST_ROLE_NAME2 = "exist_role2";
    final static String EXIST_ROLE_NAME3 = "exist_role3";
    final static String CREATE_ROLE_NAME = "create_role";
    final static String CORRECT_TOKEN = "20220905001_1_123";
    final static String INCORRECT_TOKEN = "20220905001_1_321";

    @BeforeEach
    void setUp() {
        // init the data store
        DataStore.init();
        // init a user
        User user = new User();
        user.setId(1L);
        user.setUsername(EXIST_USERNAME);
        try {
            final String salt = PassWordUtils.generateSalt();
            user.setSalt(salt);
            user.setPassWord(PassWordUtils.encodePassword(CORRECT_PASSWORD, salt));
        } catch (Exception ignored) {
        }
        DataStore.addUser(user);
        // init two roles exist_role exist_role2
        Role role = new Role();
        role.setId(1L);
        role.setName(EXIST_ROLE_NAME);
        Role role2 = new Role();
        role2.setId(2L);
        role2.setName(EXIST_ROLE_NAME2);
        Role role3 = new Role();
        role3.setId(3L);
        role3.setName(EXIST_ROLE_NAME3);
        DataStore.addRole(role);
        DataStore.addRole(role2);
        DataStore.addRole(role3);
        // Build relationships between two roles and user
        UserRoleRelation relation = new UserRoleRelation();
        relation.setId(1L);
        relation.setUserId(1L);
        relation.setRoleId(1L);
        UserRoleRelation relation2 = new UserRoleRelation();
        relation2.setId(2L);
        relation2.setUserId(1L);
        relation2.setRoleId(2L);
        DataStore.addUserRoleRelation(relation);
        DataStore.addUserRoleRelation(relation2);
        // Build relationships between token and user
        DataStore.setTokenAndUser(CORRECT_TOKEN, user.getId());
    }

    @AfterEach
    void tearDown() {
        // init the data store
        DataStore.init();
    }

    @Test
    void methodParamCheck() {
        try {
            final CreateUserReq createUserReq = new CreateUserReq();
            authService.createUser(createUserReq);
        } catch (BusinessException e) {
            Assertions.assertEquals(AuthErrors.ERROR_REQUEST_PARAM, e.getCode());
        }

        try {
            final DeleteUserReq deleteUserReq = new DeleteUserReq();
            authService.deleteUser(deleteUserReq);
        } catch (BusinessException e) {
            Assertions.assertEquals(AuthErrors.ERROR_REQUEST_PARAM, e.getCode());
        }

        try {
            final CreateRoleReq createRoleReq = new CreateRoleReq();
            authService.createRole(createRoleReq);
        } catch (BusinessException e) {
            Assertions.assertEquals(AuthErrors.ERROR_REQUEST_PARAM, e.getCode());
        }

        try {
            final DeleteRoleReq deleteRoleReq = new DeleteRoleReq();
            authService.deleteRole(deleteRoleReq);
        } catch (BusinessException e) {
            Assertions.assertEquals(AuthErrors.ERROR_REQUEST_PARAM, e.getCode());
        }

        try {
            AddRoleToUserReq addRoleToUserReq = new AddRoleToUserReq();
            authService.addRoleToUser(addRoleToUserReq);
        } catch (BusinessException e) {
            Assertions.assertEquals(AuthErrors.ERROR_REQUEST_PARAM, e.getCode());
        }

        try {
            AuthenticateReq authenticateReq = new AuthenticateReq();
            authService.authenticate(authenticateReq);
        } catch (BusinessException e) {
            Assertions.assertEquals(AuthErrors.ERROR_REQUEST_PARAM, e.getCode());
        }

        try {
            authService.invalidate("");
        } catch (BusinessException e) {
            Assertions.assertEquals(AuthErrors.ERROR_REQUEST_PARAM, e.getCode());
        }

        try {
            CheckRoleReq checkRoleReq = new CheckRoleReq();
            authService.checkRole(checkRoleReq);
        } catch (BusinessException e) {
            Assertions.assertEquals(AuthErrors.ERROR_REQUEST_PARAM, e.getCode());
        }

        try {
            authService.listAllRoles("");
        } catch (BusinessException e) {
            Assertions.assertEquals(AuthErrors.ERROR_REQUEST_PARAM, e.getCode());
        }
    }

    @Test
    void createNewUser() {
        CreateUserReq req = new CreateUserReq();
        req.setUsername(CREATE_USERNAME);
        req.setPassWord(CORRECT_PASSWORD);
        final RestResponse<UserResp> response = authService.createUser(req);
        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertEquals(response.getData().getUsername(), CREATE_USERNAME);
    }

    @Test
    void createExistsUser() {
        CreateUserReq req = new CreateUserReq();
        req.setUsername(EXIST_USERNAME);
        req.setPassWord(CORRECT_PASSWORD);
        try {
            authService.createUser(req);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.USERNAME_ALREADY_EXIST);
        }
    }

    @Test
    void deleteNotExistsUser() {
        DeleteUserReq req = new DeleteUserReq();
        req.setUserId(9999L);
        try {
            authService.deleteUser(req);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.USER_DOES_NO_EXIST);
        }
    }

    @Test
    void deleteExistsUser() {
        DeleteUserReq req = new DeleteUserReq();
        req.setUserId(1L);
        final RestResponse<UserResp> response = authService.deleteUser(req);
        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertEquals(response.getData().getId(), 1L);
    }

    @Test
    void createNewRole() {
        CreateRoleReq req = new CreateRoleReq();
        req.setName(CREATE_ROLE_NAME);
        final RestResponse<Role> response = authService.createRole(req);
        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertEquals(response.getData().getName(), CREATE_ROLE_NAME);
    }

    @Test
    void createExistsRole() {
        CreateRoleReq req = new CreateRoleReq();
        req.setName(EXIST_ROLE_NAME);
        try {
            authService.createRole(req);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.ROLE_NAME_ALREADY_EXIST);
        }
    }

    @Test
    void deleteNotExistsRole() {
        DeleteRoleReq req = new DeleteRoleReq();
        req.setRoleId(9999L);
        try {
            authService.deleteRole(req);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.ROLE_DOES_NO_EXIST);
        }
    }

    @Test
    void deleteExistsRole() {
        DeleteRoleReq req = new DeleteRoleReq();
        req.setRoleId(1L);
        final RestResponse<Role> response = authService.deleteRole(req);
        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertEquals(response.getData().getId(), 1L);
    }

    @Test
    void addNotExistRoleToUser() {
        AddRoleToUserReq req = new AddRoleToUserReq();
        req.setRoleId(9999L);
        req.setUserId(1L);
        try {
            authService.addRoleToUser(req);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.ROLE_DOES_NO_EXIST);
        }
    }

    @Test
    void addRoleToNotExistUser() {
        AddRoleToUserReq req = new AddRoleToUserReq();
        req.setRoleId(1L);
        req.setUserId(9999L);
        try {
            authService.addRoleToUser(req);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.USER_DOES_NO_EXIST);
        }
    }

    @Test
    void addExistRoleToExistUser() {
        AddRoleToUserReq req = new AddRoleToUserReq();
        req.setRoleId(1L);
        req.setUserId(1L);
        final RestResponse<UserRoleRelation> response = authService.addRoleToUser(req);
        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertEquals(response.getData().getUserId(), 1L);
        Assertions.assertEquals(response.getData().getRoleId(), 1L);
    }

    @Test
    void addSameExistRoleToExistUser() {
        AddRoleToUserReq req = new AddRoleToUserReq();
        req.setRoleId(3L);
        req.setUserId(1L);
        final RestResponse<UserRoleRelation> response = authService.addRoleToUser(req);
        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertEquals(response.getData().getUserId(), 1L);
        Assertions.assertEquals(response.getData().getRoleId(), 3L);
    }

    @Test
    void authenticateErrorUserName() {
        AuthenticateReq req = new AuthenticateReq();
        req.setUsername(NOT_EXIST_USERNAME);
        req.setPassWord(CORRECT_PASSWORD);
        try {
            authService.authenticate(req);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.USERNAME_NOT_EXIST);
        }
    }

    @Test
    void authenticateErrorPassword() {
        AuthenticateReq req = new AuthenticateReq();
        req.setUsername(EXIST_USERNAME);
        req.setPassWord(INCORRECT_PASSWORD);
        try {
            authService.authenticate(req);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.PASSWORD_INPUT_ERROR);
        }
    }

    @Test
    void authenticateSuccess() {
        AuthenticateReq req = new AuthenticateReq();
        req.setUsername(EXIST_USERNAME);
        req.setPassWord(CORRECT_PASSWORD);
        final RestResponse<AuthenticateResp> response = authService.authenticate(req);
        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertEquals(response.getData().getUser().getUsername(), EXIST_USERNAME);
        Assertions.assertNotNull(response.getData().getToken());
    }

    @Test
    void testInvalidate() {
        final User userByTokenBefore = DataStore.getUserByToken(CORRECT_TOKEN);
        Assertions.assertNotNull(userByTokenBefore);
        final RestResponse<Void> response = authService.invalidate(CORRECT_TOKEN);
        final User userByTokenAfter = DataStore.getUserByToken(CORRECT_TOKEN);
        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertNull(userByTokenAfter);
    }

    @Test
    void testCheckRole() {
        CheckRoleReq req = new CheckRoleReq();
        req.setToken(CORRECT_TOKEN);
        req.setRoleId(9999L);
        final RestResponse<Boolean> response = authService.checkRole(req);
        Assertions.assertEquals(response.getCode(), 0);
        Assertions.assertFalse(response.getData());

        try {
            CheckRoleReq invalidReq = new CheckRoleReq();
            invalidReq.setToken(INCORRECT_TOKEN);
            invalidReq.setRoleId(1L);
            authService.checkRole(invalidReq);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.TOKEN_NOT_VALID);
        }

        CheckRoleReq correctReq = new CheckRoleReq();
        correctReq.setToken(CORRECT_TOKEN);
        correctReq.setRoleId(1L);
        final RestResponse<Boolean> correctResponse = authService.checkRole(correctReq);
        Assertions.assertEquals(correctResponse.getCode(), 0);
        Assertions.assertTrue(correctResponse.getData());
    }

    @Test
    void listAllRolesByInvalidToken() {
        Assertions.assertThrows(BusinessException.class, () -> authService.listAllRoles(INCORRECT_TOKEN));
        try {
            authService.listAllRoles(INCORRECT_TOKEN);
        } catch (BusinessException e) {
            Assertions.assertEquals(e.getCode(), AuthErrors.TOKEN_NOT_VALID);
        }
    }

    @Test
    void listAllRolesValidToken() {
        final RestResponse<List<Role>> response = authService.listAllRoles(CORRECT_TOKEN);
        Assertions.assertEquals(response.getCode(), 0L);
        Assertions.assertTrue(response.getData().size() > 0);
    }
}