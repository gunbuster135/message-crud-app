package io.einharjar.messageapp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Message Entity
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private Long id;
    @NotBlank
    private String message;



    //Helper methods for mapping message <--> dto
    public Message from(Update update){
        this.message = update.getMessage();
        return this;
    }

    public static Message from(Create create){
        Message message = new Message();
        message.setId(create.getId());
        message.setMessage(create.getMessage());
        return message;
    }

    public Shallow toShallow(){
        Shallow shallow = new Shallow();
        shallow.setId(this.id);
        shallow.setMessage(this.message);
        return shallow;
    }

    /**
     * DTO for message entity
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Shallow {
        private Long id;
        private String message;
    }

    /**
     * DTO for use when creating a new message from scratch
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        @NotNull
        @JsonProperty("id")
        private Long id;
        @NotBlank
        @JsonProperty("message")
        private String message;
    }

    /**
     * DTO for use when updating a existing message
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        @NotBlank
        private String message;
    }
}
