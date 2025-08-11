package GoRestMain.POJO.Serialization.Comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {

private Integer post_id;
private String name;
private String email;
private String body;

}
