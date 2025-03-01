package Application.MTCG.dto;

public class CreateTokenDTO {
    private String token;

    public CreateTokenDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
