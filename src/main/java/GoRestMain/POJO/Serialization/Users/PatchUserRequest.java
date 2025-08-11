package GoRestMain.POJO.Serialization.Users;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PatchUserRequest {

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


public void setName(String name) {
    fields.put("name", name);
}

public void setGender(String gender) {
    fields.put("gender", gender);
}

public void setEmail(String email) {
    fields.put("email", email);
}

public void setStatus(String status) {
    fields.put("status", status);
}
}
