package io.einharjar.messageapp.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Response object that wraps a response result
 * @param <T> Type of data
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    @JsonProperty("data")
    private T result;
    @JsonProperty("status_code")
    private int statusCode;
    @JsonProperty("message")
    private String message;
}
