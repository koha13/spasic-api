package music.server.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Size(min = 2, max = 32, message = "Username co do dai tu 2-32")
    @NotEmpty(message = "Username khong duoc de trong")
    private String username;
    @NotEmpty(message = "Password khong duoc de trong")
    @Size(min = 6, message = "Password co do dai toi theu la 6")
    private String password;
}