package music.server.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @NotEmpty(message = "Old Password must not empty")
    @Size(min = 6, message = "Password is too short")
    private String oldPass;
    @NotEmpty(message = "New Password must not empty")
    @Size(min = 6, message = "Password is too short")
    private String newPass;
}