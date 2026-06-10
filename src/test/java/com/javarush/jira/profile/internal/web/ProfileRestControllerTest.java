package com.javarush.jira.profile.internal.web;

import com.javarush.jira.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = ProfileRestController.REST_URL;
    private static final String USER_MAIL = "user@gmail.com";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getProfile_Success() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getProfile_Unauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfile_Success() throws Exception {
        String updatedProfileJson = "{\"id\": 1, \"mailNotifications\": [\"assigned\"], \"contacts\": [{\"code\": \"skype\", \"value\": \"userSkype\"}]}";

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedProfileJson))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void updateProfile_Unauthorized() throws Exception {
        String updatedProfileJson = "{\"id\": 1, \"mailNotifications\": [\"assigned\"], \"contacts\": [{\"code\": \"skype\", \"value\": \"userSkype\"}]}";

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedProfileJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateProfile_InvalidData() throws Exception {
        String invalidProfileJson = "{\"id\": 1, \"mailNotifications\": null, \"contacts\": [{\"code\": \"\", \"value\": \"\"}]}";

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidProfileJson))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}