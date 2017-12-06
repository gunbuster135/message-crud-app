package io.einharjar.messageapp.web;


import io.einharjar.messageapp.domain.Message;
import io.einharjar.messageapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Message.Shallow>> createMessage(@RequestBody @Validated Message.Create messageCreate) {
        Optional<Message.Shallow> shallowOptional = messageService.createMessage(messageCreate);
        Response<Message.Shallow> response = new Response<>();
        if (shallowOptional.isPresent()) {
            response.setResult(shallowOptional.get());
            response.setMessage("Succesfully persisted message");
            response.setStatusCode(HttpStatus.OK.value());
            return ok(response);
        }
        response.setMessage("Failed to persist message because it already exists!");
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return badRequest(response);
    }

    @PutMapping(value = "/message/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Message.Shallow>> updateMessage(@RequestBody @Validated Message.Update updateMessage,
                                                     @PathVariable(value = "id") Long id) {
        Optional<Message.Shallow> shallowOptional = messageService.updateMessage(updateMessage, id);
        Response<Message.Shallow> response = new Response<>();
        if (shallowOptional.isPresent()) {
            response.setResult(shallowOptional.get());
            response.setMessage("Succesfully updated message");
            response.setStatusCode(HttpStatus.OK.value());
            return ok(response);
        }
        response.setMessage("Failed to find a message with the following id: "+id);
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return badRequest(response);
    }

    @DeleteMapping(value = "/message/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Message.Shallow>> deleteMessage(@PathVariable(value = "id") Long id){
        Response<Message.Shallow> response = new Response<>();
        if(messageService.deleteMessage(id)){
            response.setMessage("Succesfully deleted message with id: "+ id);
            response.setStatusCode(HttpStatus.OK.value());
            return ok(response);
        }
        response.setMessage("Failed to find a message with the following id: "+ id);
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return badRequest(response);
    }

    @GetMapping( value = "/message/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Message.Shallow>> getMessage(@PathVariable(value = "id") Long id){
        Optional<Message.Shallow> messageOptional = messageService.getMessage(id);
        Response<Message.Shallow> response = new Response<>();
        if(messageOptional.isPresent()){
            response.setResult(messageOptional.get());
            response.setMessage("Succesfully retrieved message with id: "+ id);
            response.setStatusCode(HttpStatus.OK.value());
            return ok(response);
        }
        response.setMessage("Failed to find a message with the following id: "+ id);
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return badRequest(response);
    }

    @GetMapping(value = "/message", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<Message.Shallow>>> getAllMessages(){
        List<Message.Shallow> messageList = messageService.getAllMessages();
        Response<List<Message.Shallow>> response = new Response<>();
        if(!messageList.isEmpty()){
            response.setResult(messageList);
            response.setMessage("Succesfully retrieved all messages!");
            response.setStatusCode(HttpStatus.OK.value());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setMessage("No messages were found!");
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Response<Message.Shallow>> ok(Response<Message.Shallow> response){
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<Response<Message.Shallow>> badRequest(Response<Message.Shallow> response){
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
