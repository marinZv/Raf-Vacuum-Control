package rs.raf.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rs.raf.demo.configuration.PasswordEncoderConfig;
import rs.raf.demo.domain.dto.user.UserCreateDto;
import rs.raf.demo.domain.dto.user.UserDto;
import rs.raf.demo.domain.dto.user.UserUpdateDto;
import rs.raf.demo.domain.entities.user.Role;
import rs.raf.demo.domain.entities.user.RoleType;
import rs.raf.demo.domain.entities.user.User;
import rs.raf.demo.domain.mapper.UserMapper;

import rs.raf.demo.repositories.RoleRepository;
import rs.raf.demo.repositories.UserRepository;
import rs.raf.demo.utils.PermissionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoderConfig passwordEncoderConfig) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoderConfig = passwordEncoderConfig;;
    }


    public UserDto createUser(UserCreateDto userCreateDto){
        if(!PermissionUtils.hasPermission(RoleType.CAN_CREATE)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, PermissionUtils.permissionMessage);
        }

        List<Role> roles = roleRepository.findAllByRoleIn(userCreateDto.getUserRoles().stream().map(RoleType::valueOf).collect(Collectors.toList()));

        User user = UserMapper.INSTANCE.userCreateDtoToUser(userCreateDto);
        user.setPassword(passwordEncoderConfig.passwordEncoder().encode(user.getPassword()));
        user.setRoles(roles);


        return UserMapper.INSTANCE.userToUserDto(userRepository.save(user));
    }

    public List<UserDto> findAllUsers(){
        if(!PermissionUtils.hasPermission(RoleType.CAN_READ)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, PermissionUtils.permissionMessage);
        }

        return userRepository.findAll().stream().map(UserMapper.INSTANCE::userToUserDto).collect(Collectors.toList());
    }

    public UserDto findUserById(Long id){
        if(!PermissionUtils.hasPermission(RoleType.CAN_READ)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, PermissionUtils.permissionMessage);
        }

        Optional<User> user = userRepository.findById(id);

        return user.map(UserMapper.INSTANCE::userToUserDto)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id!"));
    }

    public UserDto findUserByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);

        return user.map(UserMapper.INSTANCE::userToUserDto)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email!"));
    }

    public UserDto updateUserById(Long id, UserUpdateDto userUpdateDto){
        if(!PermissionUtils.hasPermission(RoleType.CAN_UPDATE)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, PermissionUtils.permissionMessage);
        }

        List<Role> roles = roleRepository.findAllByRoleIn(userUpdateDto.getUserRoles().stream().map(RoleType::valueOf).collect(Collectors.toList()));

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid id!"));

        user = UserMapper.INSTANCE.updateUser(user, userUpdateDto);
        user.setRoles(roles);

        return UserMapper.INSTANCE.userToUserDto(userRepository.save(user));
    }

    public void deleteUserById(Long id){
        if(!PermissionUtils.hasPermission(RoleType.CAN_DELETE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, PermissionUtils.permissionMessage);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid id!"));

        userRepository.delete(user);
    }


    @Override
    public UserDetails loadUserByUsername(String email){
        Optional<User> myUser = userRepository.findByEmail(email);

        if(!myUser.isPresent()){
            throw new UsernameNotFoundException("Username " + email + " not found");
        }



        List<RoleType> roles = myUser.get().getRoles().stream().map(Role::getRole).collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(myUser.get().getEmail(), myUser.get().getPassword(), roles);
    }


}
