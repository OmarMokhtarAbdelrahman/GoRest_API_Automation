package POJO.Deserialization.Todo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateTodoResponse {

private int id;
private int user_id;
private String title;
private String due_on;
private String status;
}
