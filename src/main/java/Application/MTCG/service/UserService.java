package Application.MTCG.service;

import Application.MTCG.dto.UpdateUserDTO;
import Application.MTCG.entity.User;
import Application.MTCG.dto.CreateUserDTO;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.exceptions.UserAlreadyExisting;
import Application.MTCG.repositorys.UserRepo;
import Application.MTCG.exceptions.NullPointerException;
import Server.http.Request;

import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public CreateUserDTO create(User user) {
        if (user == null) {
            throw new NullPointerException("User can not be NULL");
        }

        Optional<User> existingUser = userRepo.findUserByName(user.getUsername());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExisting("User is already existing");
        }

        user.setUuid(UUID.randomUUID().toString());
        User newUser = userRepo.save(user);

        return new CreateUserDTO(newUser.getUsername());
    }

    public User getUserByToken(String token) {
        if (token == null) {
            throw new NullPointerException("Token can not be NULL");
        }
        Optional<User> findUser = userRepo.findUserByToken(token);

        if (findUser.isPresent()) {
            return findUser.get();
        } else {
            throw new InvalidUserData("Token not found");
        }
    }

    public void updateUserByUuid(User user) {
        if (user == null) {
            throw new NullPointerException("User can not be NULL");
        }
        userRepo.updateUserByUuid(user);
    }

    public boolean matchTokenWithPath(String loginToken, String path) {
        if (loginToken == null || path == null) {
            throw new NullPointerException("Login token can not be NULL");
        }

        if (path.startsWith("/users/")) {
            String username = path.substring(7);
            return loginToken.startsWith(username);
        }
        return false;
    }

    public UpdateUserDTO modelUpdateDTO(User user) {
        if (user == null) {
            throw new NullPointerException("User can not be NULL");
        }
        return new UpdateUserDTO(user.getName(), user.getBio(), user.getImage());
    }

    public UpdateUserDTO modelUpdateDTOFromRequest(Request request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(request.getBody(), UpdateUserDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error with Request parsing", e);
        }
    }

    public void updateUserNameBioImage(User user, UpdateUserDTO dto) {
        if (user == null || dto == null) {
            throw new NullPointerException("User/DTO can not be NULL");
        }
        user.setName(dto.getName());
        user.setBio(dto.getBio());
        user.setImage(dto.getImage());

        updateUserByUuid(user);
    }
}
