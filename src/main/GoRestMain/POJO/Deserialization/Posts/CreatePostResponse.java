package POJO.Deserialization.Posts;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatePostResponse {

private int id;
private int user_id;
private String title;
private String body;


}
