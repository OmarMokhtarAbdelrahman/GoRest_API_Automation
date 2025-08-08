package POJO.Serialization.Users;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateUserRequest {

private String name;
private String gender;
private String email;
private String status;

}
