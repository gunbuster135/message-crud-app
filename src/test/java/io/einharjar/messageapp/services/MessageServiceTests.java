package io.einharjar.messageapp.services;


import io.einharjar.messageapp.domain.Message;
import io.einharjar.messageapp.repository.MessageRepository;
import io.einharjar.messageapp.service.MessageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest()
@Transactional
public class MessageServiceTests {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageRepository messageRepository;

    @Before
    public void setup() {
        //Bootstrap some data
        messageRepository.save(new Message(1L, "Crazy Diamond"));
        messageRepository.save(new Message(2L, "Time"));
        messageRepository.save(new Message(3L, "Childhood's End"));
    }

    @Test
    public void createMessage() {
        //Create new message
        Optional<Message.Shallow> messageOptional = messageService.createMessage(new Message.Create(4L, "In The Flesh"));
        Assert.assertTrue(messageOptional.isPresent());
        Assert.assertTrue(messageOptional.get()
                                         .getMessage()
                                         .equals("In The Flesh"));
        Assert.assertTrue(messageOptional.get()
                                         .getId()
                                         .equals(4L));
        //Try to create a message
        Optional<Message.Shallow> emptyOptional = messageService.createMessage(new Message.Create(1L, "Shine On"));
        Assert.assertFalse(emptyOptional.isPresent());
    }

    @Test
    public void updateMessage() {
        //Update existing message
        Optional<Message.Shallow> messageOptional = messageService.updateMessage(new Message.Update("Comfortably Numb"), 1L);
        Assert.assertTrue(messageOptional.isPresent());
        Assert.assertTrue(messageOptional.get()
                                         .getId()
                                         .equals(1L));
        Assert.assertTrue(messageOptional.get()
                                         .getMessage()
                                         .equals("Comfortably Numb"));

        //Try to update a non-existing message
        Optional<Message.Shallow> emptyOptional = messageService.updateMessage(new Message.Update("Wish you were here"), 1337L);
        Assert.assertFalse(emptyOptional.isPresent());
    }

    @Test
    public void deleteMessage() {
        boolean deleteResult = messageService.deleteMessage(1L);
        Assert.assertTrue(deleteResult);

        boolean deleteNonExistingResult = messageService.deleteMessage(1337L);
        Assert.assertFalse(deleteNonExistingResult);
    }

    @Test
    public void getMessage() {
        Optional<Message.Shallow> messageOptional = messageService.getMessage(1L);
        Assert.assertTrue(messageOptional.isPresent());
        Assert.assertTrue(messageOptional.get()
                                         .getId()
                                         .equals(1L));
        Assert.assertTrue(messageOptional.get()
                                         .getMessage()
                                         .equals("Crazy Diamond"));

    }
    @Test
    public void getAllMessage(){
        List<Message.Shallow> messages = messageService.getAllMessages();
        Assert.assertTrue(messages.size() == 3);
    }

}
