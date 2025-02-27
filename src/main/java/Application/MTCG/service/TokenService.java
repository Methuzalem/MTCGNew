package Application.MTCG.service;

import Application.MTCG.dto.CreateTokenDTO;
import Application.MTCG.entity.TokenAuthenticator;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.repositorys.UserRepo;
import Application.MTCG.entity.User;

import java.util.Optional;

public class TokenService {
    private final UserRepo userRepo;

    public TokenService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public CreateTokenDTO createToken(TokenAuthenticator tokenAuthenticator) {
        Optional<User> userOptional = this.userRepo.findUserByName(tokenAuthenticator.getUsername());

        if (userOptional.isPresent() && userOptional.get().getPassword().equals(tokenAuthenticator.getPassword())) {
            String tokenName ="%s-mtcgToken".formatted(tokenAuthenticator.getUsername());
            tokenAuthenticator.setToken(tokenName);
            User user = userOptional.get();
            user.setToken(tokenName);

            this.userRepo.updateUserByUuid(user);
            return new CreateTokenDTO(tokenAuthenticator.getUsername(), tokenAuthenticator.getToken());
        } else {
            throw new InvalidUserData("Invalid User Data");
        }
    }
}
