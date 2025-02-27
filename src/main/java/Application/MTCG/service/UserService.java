package Application.MTCG.service;

import Application.MTCG.entity.User;
import Application.MTCG.dto.CreateUserDTO;
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

        Optional<User>  existingUser = userRepo.findUserByName(user.getUsername());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExisting("User is already existing");
        }

        user.setUuid(UUID.randomUUID().toString());

        User newUser = userRepo.save(user);

        return new CreateUserDTO(newUser.getUsername(), newUser.getToken());

    }
}
