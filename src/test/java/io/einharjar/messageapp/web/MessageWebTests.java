package io.einharjar.messageapp.web;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.einharjar.messageapp.domain.Message;
import io.einharjar.messageapp.repository.MessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
@Transactional
public class MessageWebTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String MESSAGE_ENDPOINT = "/message/";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                                      .build();

        //Bootstrap data
        messageRepository.save(new Message(1L, "Crazy Diamond"));
        messageRepository.save(new Message(2L, "Time"));
        messageRepository.save(new Message(3L, "Childhood's End"));
        messageRepository.save(new Message(4L, "Atom Heart Mother"));
    }

    @Test
    public void postMessage() throws Exception {
        this.mockMvc.perform(post(MESSAGE_ENDPOINT)
                .content(toJson(new Message.Create(5L, "Money")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.message").value("Money"))
                    .andExpect(jsonPath("$.data.id").value(5));
    }


    //Check JSR-303 bean validation annotations
    @Test
    public void postInvalidMessage() throws Exception {
        this.mockMvc.perform(post(MESSAGE_ENDPOINT)
                .content(toJson(new Message.Create(null, "Money")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void postMessageAlreadyExists() throws Exception {
        this.mockMvc.perform(post(MESSAGE_ENDPOINT)
                .content(toJson(new Message.Create(1L, "Brain Damage")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void putMessage() throws Exception {
        this.mockMvc.perform(put(MESSAGE_ENDPOINT + "3")
                .content(toJson(new Message.Update("Sheep")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.message").value("Sheep"))
                    .andExpect(jsonPath("$.data.id").value(3));
    }

    @Test
    public void putInvalidMessage() throws Exception {
        this.mockMvc.perform(put(MESSAGE_ENDPOINT + "1337")
                .content(toJson(new Message.Update("Obscured By Clouds")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void putMessageBlankString() throws Exception {
        this.mockMvc.perform(put(MESSAGE_ENDPOINT + "3")
                .content(toJson(new Message.Update("")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteMessage() throws Exception {
        this.mockMvc.perform(delete(MESSAGE_ENDPOINT + "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isOk());
    }

    @Test
    public void deleteUnknownMessage() throws Exception {
        this.mockMvc.perform(delete(MESSAGE_ENDPOINT + "1337")
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void getMessage() throws Exception {
        this.mockMvc.perform(get(MESSAGE_ENDPOINT + "4")
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.message").value("Atom Heart Mother"))
                    .andExpect(jsonPath("$.data.id").value(4));
    }


    @Test
    public void getUnknownMessage() throws Exception {
        this.mockMvc.perform(get(MESSAGE_ENDPOINT + "1337")
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllMessages() throws Exception {
        this.mockMvc.perform(get(MESSAGE_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.length()").value(4));
    }


    private String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
