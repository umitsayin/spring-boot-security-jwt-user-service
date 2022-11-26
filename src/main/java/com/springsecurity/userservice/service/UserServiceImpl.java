package com.springsecurity.userservice.service;

import com.springsecurity.userservice.model.Role;
import com.springsecurity.userservice.model.User;
import com.springsecurity.userservice.repository.RoleRepository;
import com.springsecurity.userservice.repository.UserRepository;
import com.springsecurity.userservice.result.DataResult;
import com.springsecurity.userservice.result.Result;
import com.springsecurity.userservice.result.SuccessDataResult;
import com.springsecurity.userservice.result.SuccessResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = this.getUserByUsername(username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role->{
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public DataResult<User> saveUser(User user) {
        final Role role = this.roleRepository.findByName("ROLE_USER");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(role);
        this.userRepository.save(user);
        log.info("User:{} saved",user.getUsername());

        return new SuccessDataResult<User>("eklendi",user);
    }

    @Override
    public Result saveRole(String roleName) {
        log.info("Role:{} saved",roleName);
        this.roleRepository.save(new Role(null,roleName));

        return new SuccessResult("Role saved");
    }

    @Override
    public DataResult<User> updateUser(User user,String username) {
        final User updateUser = getUserByUsername(username);

        updateUser.setName(user.getName());
        updateUser.setPassword(this.passwordEncoder.encode(user.getPassword()));

        return new SuccessDataResult<User>("User updated",updateUser);
    }

    @Override
    public DataResult<List<User>> getUsers() {
        return new SuccessDataResult<List<User>>("User listing.",this.userRepository.findAll());
    }

    @Override
    public Result addRoleToUser(String username, String roleName) {
        final User user = getUserByUsername(username);
        final Role role = this.roleRepository.findByName(roleName);

        user.getRoles().add(role);
        this.userRepository.save(user);
        log.info("User:{} add Role:{}",username,roleName);

        return new SuccessResult("Role to user saved");
    }

    @Override
    public Result deleteRoleToUser(String username, String roleName) {
        final User user = getUserByUsername(username);
        final Role role = this.roleRepository.findByName(roleName);

        user.getRoles().remove(role);
        this.userRepository.save(user);
        log.info("User:{} deleting Role:{}",username,roleName);

        return new SuccessResult("Role delete to roleuser saved");
    }

    @Override
    public Result deleteUser(String username) {
        final User user = getUserByUsername(username);

        this.userRepository.delete(user);
        log.info("User:{} deleted",username);

        return new SuccessResult("User deleted");
    }

    @Override
    public User getUser(String username) {
        return getUserByUsername(username);
    }

    private User getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("user not found"));
    }
}
