package org.example.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import model.Role;
import model.User;
import model.UserRoleRelation;
import org.example.common.AuthErrors;
import org.example.data.DataStore;
import org.example.utils.PassWordUtils;
import org.example.common.RestResponse;
import org.example.utils.TokenUtils;
import req.*;
import resp.AuthenticateResp;
import resp.UserResp;

import java.util.List;

/**
 * Implement the function of the authentication service
 */
public class AuthServiceImpl implements AuthService {

    @Override
    public RestResponse<UserResp> createUser(CreateUserReq req) {
        if (!StrUtil.isAllNotEmpty(req.getPassWord(), req.getUsername())) {
            throw AuthErrors.requestParamError();
        }

        User user = BeanUtil.copyProperties(req, User.class);
        final User oldUser = DataStore.getUserByUserName(user.getUsername());
        if (oldUser != null) {
            throw AuthErrors.usernameRepeatError();
        }
        String salt = PassWordUtils.generateSalt();
        String encodePassword = PassWordUtils.encodePassword(user.getPassWord(), salt);
        user.setSalt(salt);
        user.setPassWord(encodePassword);
        User newUser = DataStore.addUser(user);
        return RestResponse.ok(0, "create user success!", BeanUtil.copyProperties(newUser, UserResp.class));
    }

    @Override
    public RestResponse<UserResp> deleteUser(DeleteUserReq req) {
        if (StrUtil.isEmptyIfStr(req.getUserId())) {
            throw AuthErrors.requestParamError();
        }

        final User delUser = DataStore.getUserById(req.getUserId());
        if (delUser == null) {
            throw AuthErrors.userNotExistError();
        }
        Long delUserId = DataStore.delUser(req.getUserId());
        if (delUserId != null) {
            return RestResponse.ok(0, "delete user success!", BeanUtil.copyProperties(delUser, UserResp.class));
        }
        return RestResponse.ok(0, "delete user fail!", null);
    }

    @Override
    public RestResponse<Role> createRole(CreateRoleReq req) {
        if (StrUtil.isEmpty(req.getName())) {
            throw AuthErrors.requestParamError();
        }

        Role role = BeanUtil.copyProperties(req, Role.class);
        final Role oldRole = DataStore.getRoleByName(role.getName());
        if (oldRole != null) {
            throw AuthErrors.roleNameRepeatError();
        }
        Role newRole = DataStore.addRole(role);
        return RestResponse.ok(0, "create role success!", newRole);
    }

    @Override
    public RestResponse<Role> deleteRole(DeleteRoleReq req) {
        if (StrUtil.isEmptyIfStr(req.getRoleId())) {
            throw AuthErrors.requestParamError();
        }

        final Role delRole = DataStore.getRoleById(req.getRoleId());
        if (delRole == null) {
            throw AuthErrors.roleNotExistError();
        }
        final Long delRoleId = DataStore.delRole(req.getRoleId());
        if (delRoleId != null) {
            return RestResponse.ok(0, "delete role success!", delRole);
        }
        return RestResponse.ok(0, "delete role fail!", null);
    }

    @Override
    public RestResponse<UserRoleRelation> addRoleToUser(AddRoleToUserReq req) {
        if (StrUtil.isEmptyIfStr(req.getRoleId()) || StrUtil.isEmptyIfStr(req.getUserId())) {
            throw AuthErrors.requestParamError();
        }
        final User userById = DataStore.getUserById(req.getUserId());
        if (userById == null) {

            throw AuthErrors.userNotExistError();
        }
        final Role roleById = DataStore.getRoleById(req.getRoleId());
        if (roleById == null) {
            throw AuthErrors.roleNotExistError();
        }
        UserRoleRelation userRoleRelation = new UserRoleRelation();
        userRoleRelation.setRoleId(req.getRoleId());
        userRoleRelation.setUserId(req.getUserId());
        final UserRoleRelation oldRelation = DataStore.findUserRoleRelation(userRoleRelation.getUserId(), userRoleRelation.getRoleId());
        if (oldRelation != null) {
            return RestResponse.ok(0, "add role to user success", oldRelation);
        }
        final UserRoleRelation newRelation = DataStore.addUserRoleRelation(userRoleRelation);
        return RestResponse.ok(0, "add role to user success", newRelation);
    }

    @Override
    public RestResponse<AuthenticateResp> authenticate(AuthenticateReq req) {
        if (StrUtil.isEmptyIfStr(req.getUsername()) || StrUtil.isEmptyIfStr(req.getPassWord())) {
            throw AuthErrors.requestParamError();
        }
        final User userByUserName = DataStore.getUserByUserName(req.getUsername());
        if (userByUserName == null) {
            throw AuthErrors.usernameNotExistError();
        }

        String encodePassword = PassWordUtils.encodePassword(req.getPassWord(), userByUserName.getSalt());
        if (!encodePassword.equals(userByUserName.getPassWord())) {
            throw AuthErrors.passwordInputError();
        }
        AuthenticateResp resp = new AuthenticateResp();
        String token = TokenUtils.generateToken();
        DataStore.setTokenAndUser(token, userByUserName.getId());
        resp.setToken(token);
        resp.setUser(userByUserName);
        return RestResponse.ok(0, "user auth success", resp);
    }

    @Override
    public RestResponse<String> invalidate(String token) {
        if (StrUtil.isEmptyIfStr(token)) {
            throw AuthErrors.requestParamError();
        }
        DataStore.removeToken(token);
        return RestResponse.ok(0, "token invalidate success!", token);
    }

    @Override
    public RestResponse<Boolean> checkRole(CheckRoleReq req) {
        if (StrUtil.isEmptyIfStr(req.getToken()) || StrUtil.isEmptyIfStr(req.getRoleId())) {
            throw AuthErrors.requestParamError();
        }
        final User userByToken = DataStore.getUserByToken(req.getToken());
        if (userByToken == null) {
            throw AuthErrors.tokenValidError();
        }

        final UserRoleRelation relation = DataStore.findUserRoleRelation(userByToken.getId(), req.getRoleId());
        if (relation != null) {
            return RestResponse.ok(0, "user belongs to the role!", true);
        }
        return RestResponse.ok(0, "user not belongs to the role!", false);
    }

    @Override
    public RestResponse<List<Role>> listAllRoles(String token) {
        if (StrUtil.isEmptyIfStr(token)) {
            throw AuthErrors.requestParamError();
        }
        final User userByToken = DataStore.getUserByToken(token);
        if (userByToken == null) {
            throw AuthErrors.tokenValidError();
        }
        final List<Role> roles = DataStore.listRolesByUserId(userByToken.getId());
        return RestResponse.ok(0, "get roles success", roles);
    }
}
