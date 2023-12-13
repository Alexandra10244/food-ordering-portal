package com.portal.foodordering.serivces.implementations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.foodordering.exceptions.InvalidEmailFormatException;
import com.portal.foodordering.exceptions.InvalidPhoneNumberException;
import com.portal.foodordering.exceptions.UserNotFoundException;
import com.portal.foodordering.models.dtos.EditUserDTO;
import com.portal.foodordering.models.dtos.UserDTO;
import com.portal.foodordering.models.entities.User;
import com.portal.foodordering.repositories.UserRepository;
import com.portal.foodordering.serivces.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        validatedUserDTO(userDTO);
        User user = userRepository.save(objectMapper.convertValue(userDTO, User.class));
        return objectMapper.convertValue(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> objectMapper.convertValue(user, UserDTO.class))
                .toList();
    }

    @Override
    public UserDTO updateUser(Long id, EditUserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!"));

        if (userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty()) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null && !userDTO.getLastName().isEmpty()) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getPhoneNumber() != null && !userDTO.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getAddress() != null && !userDTO.getAddress().isEmpty()) {
            user.setAddress(userDTO.getAddress());
        }

        User userUpdated = userRepository.save(user);

        return objectMapper.convertValue(userUpdated, UserDTO.class);
    }

    @Override
    public String deleteUser(Long id) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User " + id + " successfully deleted!";
        } else {
            throw new UserNotFoundException("User not found");
        }

    }

    @Override
    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found!"));

        return objectMapper.convertValue(user, UserDTO.class);
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found!"));

        return objectMapper.convertValue(user, UserDTO.class);
    }

    private void validatedUserDTO(UserDTO userDTO) {
        String email = userDTO.getEmail();
        String phoneNumber = userDTO.getPhoneNumber();

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidEmailFormatException("Invalid email.");
        }

        if (phoneNumber == null || !phoneNumber.matches("^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$")) {
            throw new InvalidPhoneNumberException("Invalid phone number.");
        }
    }
}

