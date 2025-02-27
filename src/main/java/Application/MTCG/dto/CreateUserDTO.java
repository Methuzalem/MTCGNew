package Application.MTCG.dto;

public class CreateUserDTO {
    private final String username;
    private String token;

    public CreateUserDTO(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
