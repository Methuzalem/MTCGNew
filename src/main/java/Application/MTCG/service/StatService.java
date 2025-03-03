package Application.MTCG.service;

import Application.MTCG.dto.ShowStatsDTO;
import Application.MTCG.entity.User;
import Application.MTCG.repositorys.UserRepo;

public class StatService {
    private final UserService userService;
    private final UserRepo userRepo;

    public StatService(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    public ShowStatsDTO getStatsOfUser(String loginToken){
        User user = userService.getUserByToken(loginToken);
        ShowStatsDTO userStats = userRepo.getStatsOfUser(user);
        return userStats;
    }
}
