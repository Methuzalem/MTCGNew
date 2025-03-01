package Application.MTCG.service;

import Application.MTCG.entity.User;
import Application.MTCG.dto.CreateUserDTO;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.exceptions.UserAlreadyExisting;
import Application.MTCG.repositorys.UserRepo;
import Application.MTCG.exceptions.NullPointerException;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public CreateUserDTO create (User user) {
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

    public User getUserByToken (String token) {
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

    public void updateUserByUuid (User user) {
        userRepo.updateUserByUuid(user);
    }
}
