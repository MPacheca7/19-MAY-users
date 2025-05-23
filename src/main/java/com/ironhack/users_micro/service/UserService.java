package com.ironhack.users_micro.service;

import com.ironhack.users_micro.dto.AccountDTO;
import com.ironhack.users_micro.dto.UserResponseDTO;
import com.ironhack.users_micro.exception.UserNotFoundException;
import com.ironhack.users_micro.model.User;
import com.ironhack.users_micro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id){
        Optional<User> optionalUser = userRepository.findById(id);

        if(optionalUser.isPresent()){
            return optionalUser.get();
        }else{
            throw new UserNotFoundException("The user was not found");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public ResponseEntity<?> patchAccountId(Long userId, Long accountId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            AccountDTO accountDTO = restTemplate.getForObject("http://accounts_micro/api/account" +
                   optionalUser.get().getId(), AccountDTO.class);

            System.out.println("EL USUARIO ES: " + accountDTO);

            User user = optionalUser.get();
            UserResponseDTO responseDTO = new UserResponseDTO(user, accountDTO);

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
