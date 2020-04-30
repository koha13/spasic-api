package music.server.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotEmpty(message = "Username khong duoc de trong")
    @Size(min = 6, max = 32, message = "Username phai co tu 6 den 32 ki tu")
    private String username;
    @Size(min = 6, max = 32, message = "Password phai co tu 6 den 32 ki tu")
    private String password;
    private String role = "user";

    public SignupRequest(
            @NotEmpty(message = "Username khong duoc de trong") @Size(min = 6, max = 32, message = "Username phai co tu 6 den 32 ki tu") String username,
            @Size(min = 6, max = 32, message = "Password phai co tu 6 den 32 ki tu") String password) {
        this.username = username;
        this.password = password;
    }
}