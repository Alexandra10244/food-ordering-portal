package com.portal.foodordering.serivces.interfaces;

import com.portal.foodordering.models.dtos.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO  userDTO);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(Long id, UserDTO userDTO);
    String deleteUser(Long id);
    UserDTO findUserById(Long id);
    UserDTO findUserByEmail(String email);
}
