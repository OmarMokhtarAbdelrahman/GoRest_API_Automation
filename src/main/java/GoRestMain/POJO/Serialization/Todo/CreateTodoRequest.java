package GoRestMain.POJO.Serialization.Todo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTodoRequest {


private String title;
private String due_on;
private String status;
}
