package com.springsecurity.userservice.busniess;

import com.springsecurity.userservice.entities.Role;
import com.springsecurity.userservice.entities.User;
import com.springsecurity.userservice.repository.RoleRepository;
import com.springsecurity.userservice.repository.UserRepository;
import com.springsecurity.userservice.results.DataResult;
import com.springsecurity.userservice.results.Result;
import com.springsecurity.userservice.results.SuccessDataResult;
import com.springsecurity.userservice.results.SuccessResult;
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
public class UserManager implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if(username == null){
            log.error("user not found");
            throw new UsernameNotFoundException("user not found");
        }else{
            log.info("user found in database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role->{
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        System.out.println(user.getUsername()+ " "+ user.getPassword());
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

    @Override
    public DataResult<User> saveUser(User user) {
        log.info("User:{} saved",user.getUsername());
        Role role = this.roleRepository.findByName("ROLE_USER");
        System.out.println(role.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(role);
        this.userRepository.save(user);
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
        User updateUser = this.userRepository.findByUsername(username);
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
        User user = this.userRepository.findByUsername(username);
        Role role = this.roleRepository.findByName(roleName);
        user.getRoles().add(role);
        this.userRepository.save(user);
        log.info("User:{} add Role:{}",username,roleName);
        return new SuccessResult("Role to user saved");
    }

    @Override
    public Result deleteRoleToUser(String username, String roleName) {
        User user = this.userRepository.findByUsername(username);
        Role role = this.roleRepository.findByName(roleName);
        user.getRoles().remove(role);
        this.userRepository.save(user);
        log.info("User:{} deleting Role:{}",username,roleName);
        return new SuccessResult("Role delete to roleuser saved");
    }

    @Override
    public Result deleteUser(String username) {
        User user = this.userRepository.findByUsername(username);
        this.userRepository.delete(user);
        log.info("User:{} deleted",username);
        return new SuccessResult("User deleted");
    }

    @Override
    public User getUser(String username) {
        return this.userRepository.findByUsername(username);
    }
}
