package POJO.Serialization.Posts;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CreatePostRequest {

@JsonInclude(JsonInclude.Include.NON_NULL)
private Map<String, Object> fields = new HashMap<>();


@JsonAnyGetter
public Map<String, Object> getFields() {
    return fields;
}

@JsonAnySetter
public void setFields(String key, Object value) {
    fields.put(key, value);
}


public void setTitle(String title) {
    fields.put("title", title);
}


public void setBody(String body) {
    fields.put("body", body);
}
}
