package org.example.service;

import model.Role;
import model.User;
import model.UserRoleRelation;
import org.example.common.RestResponse;
import req.*;
import resp.AuthenticateResp;
import resp.UserResp;

import java.util.List;

public interface AuthService {
    /**
     * create a new user
     *
     * @param req create user request
     * @return new user
     */
    RestResponse<UserResp> createUser(CreateUserReq req);

    /**
     * delete a exist user
     *
     * @param req delete user request
     * @return deleted user
     */
    RestResponse<UserResp> deleteUser(DeleteUserReq req);

    /**
     * create a new role
     *
     * @param req create role request
     * @return new role
     */
    RestResponse<Role> createRole(CreateRoleReq req);

    /**
     * delete a exist role
     *
     * @param req delete role request
     * @return deleted role
     */
    RestResponse<Role> deleteRole(DeleteRoleReq req);

    /**
     * build a relation between a role and a user
     *
     * @param req addRoleToUser request
     * @return relation
     */
    RestResponse<UserRoleRelation> addRoleToUser(AddRoleToUserReq req);

    /**
     * authenticate the username and password and get token
     *
     * @param req authenticate request
     * @return user info and token
     */
    RestResponse<AuthenticateResp> authenticate(AuthenticateReq req);

    /**
     * invalidate the token
     *
     * @param token token
     * @return invalid token
     */
    RestResponse<String> invalidate(String token);

    /**
     * Check if user belongs to a role
     *
     * @return true or false
     */
    RestResponse<Boolean> checkRole(CheckRoleReq req);

    /**
     * get the roles of a user by a token
     *
     * @param token token
     * @return roleList
     */
    RestResponse<List<Role>> listAllRoles(String token);
}
