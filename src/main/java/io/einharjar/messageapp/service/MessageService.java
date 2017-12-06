package io.einharjar.messageapp.service;

import io.einharjar.messageapp.domain.Message;
import io.einharjar.messageapp.repository.MessageRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private Logger log = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Create a message to persist in DB
     * @param createMessage Message DTO that will be used for message entity
     * @return A shallow copy of the persisted message
     */
    @Transactional
    public Optional<Message.Shallow> createMessage(@NonNull Message.Create createMessage) {
        if (messageRepository.exists(createMessage.getId())) {
            log.info("Following message already exists = {?}", createMessage);
            return Optional.empty();
        }

        Message message = messageRepository.save(Message.from(createMessage));
        return Optional.of(message.toShallow());
    }

    /**
     * Update a message entity with a new message string
     * @param updateMessage Message DTO that contains the new message string
     * @param id Entity ID
     * @return A shallow copy of the persisted message
     */
    @Transactional
    public Optional<Message.Shallow> updateMessage(@NonNull Message.Update updateMessage, @NonNull Long id) {
        Optional<Message> messageOptional = messageRepository.findById(id);

        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setMessage(updateMessage.getMessage());
            return Optional.of(messageRepository.save(message).toShallow());
        }
        log.info("Failed to find a message with the following id = {?}", id);
        return Optional.empty();
    }

    /**
     * Delete a message with its id
     * @param id The persisted entity primary key
     * @return true/false based if it actually deleted a entity or not
     */
    @Transactional
    public boolean deleteMessage(@NonNull Long id){
        Optional<Message> messageOptional = messageRepository.findById(id);
        if(messageOptional.isPresent()){
            messageRepository.delete(messageOptional.get());
            return true;
        }
        log.info("Failed to find a message with the following id = {?}", id);
        return false;
    }

    /**
     * Retrieve a persisted message with its primary key
     * @param id the primary key of the entity, not null
     * @return A shallow copy of the message
     */
    @Transactional
    public Optional<Message.Shallow> getMessage(@NonNull Long id){
        Optional<Message> messageOptional = messageRepository.findById(id);
        if(!messageOptional.isPresent()){
            log.info("Failed to find a message with the following id = {?}", id);
        }
        return messageOptional.map(Message::toShallow);
    }

    /**
     * Retrieve all messages in the DB
     * @return A list of shallow copies of persisted messages
     */
    @Transactional
    public List<Message.Shallow> getAllMessages(){
        List<Message.Shallow> messageShallowList = new ArrayList<>();
        Iterable<Message> messageList = messageRepository.findAll();
        messageList.forEach(message -> messageShallowList.add(message.toShallow()));
        return messageShallowList;
    }

}
