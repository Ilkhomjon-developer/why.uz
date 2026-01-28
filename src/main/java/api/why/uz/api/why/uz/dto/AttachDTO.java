package api.why.uz.api.why.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttachDTO {
   private String id;
   private String originName;
   private String size;
   private String extension;
   private String createdDate;
   private String url;

}
