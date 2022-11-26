package com.springsecurity.userservice.service;

import com.springsecurity.userservice.model.User;
import com.springsecurity.userservice.result.DataResult;
import com.springsecurity.userservice.result.Result;

import java.util.List;

public interface UserService {
    DataResult<User> saveUser(User user);
    Result saveRole(String roleName);
    DataResult<User> updateUser(User user,String username);
    DataResult<List<User>>  getUsers();
    Result addRoleToUser(String username,String roleName);
    Result deleteRoleToUser(String username,String roleName);
    Result deleteUser(String username);

    User getUser(String username);
}
