package com.springsecurity.userservice.busniess;

import com.springsecurity.userservice.entities.User;
import com.springsecurity.userservice.results.DataResult;
import com.springsecurity.userservice.results.Result;

import javax.xml.crypto.Data;
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
