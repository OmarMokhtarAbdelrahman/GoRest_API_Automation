package POJO.Deserialization.Comment;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentResponse {

private int id;
private int post_id;
private String name;
private String email;
private String body;
}
