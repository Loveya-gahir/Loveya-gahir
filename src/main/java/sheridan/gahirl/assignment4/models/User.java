package sheridan.gahirl.assignment4.models;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private Long userId;
	private String email;
	private String encryptedPassword;
	private boolean enabled;
}