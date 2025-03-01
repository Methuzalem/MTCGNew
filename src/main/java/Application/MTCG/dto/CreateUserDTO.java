package Application.MTCG.dto;

public class CreateUserDTO {
    private final String username;
    //password?

    public CreateUserDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
