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

    public CreateTokenDTO createTokenInitializeStats(TokenAuthenticator tokenAuthenticator) {
        Optional<User> existingUser = this.userRepo.findUserByName(tokenAuthenticator.getUsername());

        if (existingUser.isPresent() && existingUser.get().getPassword().equals(tokenAuthenticator.getPassword())) {
            String tokenName ="%s-mtcgToken".formatted(tokenAuthenticator.getUsername());
            existingUser.get().setToken(tokenName);
            existingUser.get().setCoins(20);
            existingUser.get().setElo(100);
            existingUser.get().setBio("Here comes your Bio");
            existingUser.get().setImage("Here comes your Image");
            tokenAuthenticator.setToken(tokenName);
            this.userRepo.updateUserByUuid(existingUser.get());
            return new CreateTokenDTO(tokenAuthenticator.getToken());
        } else {
            throw new InvalidUserData("Invalid User Data");
        }
    }
}
