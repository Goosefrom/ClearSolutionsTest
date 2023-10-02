package com.goose.clearsolutionstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.goose.clearsolutionstest.dto.UserDTO;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class UserEndpointTest {

    private static final String URL = "/api/users";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createUserWithRequiredParamsOk() throws Exception{
        UserDTO user = new UserDTO();
        user.setEmail("user@mail.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        String content = objectMapper.writeValueAsString(user);

        mockMvc.perform(post(URL).content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(user.getEmail())))
                .andExpect(jsonPath("$.address", equalTo(null)));
    }

    @Test
    public void crateUserWithAllParamsOk() throws Exception {
        UserDTO user = new UserDTO();
        user.setEmail("userall@mail.com");
        user.setFirstName("firstNameAll");
        user.setLastName("lastNameAll");
        user.setBirthDate(LocalDate.of(2000, 1, 2));
        user.setAddress("someAddressAll");
        user.setPhoneNumber("1");
        String content = objectMapper.writeValueAsString(user);

        mockMvc.perform(post(URL).content(content)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(user.getEmail())))
                .andExpect(jsonPath("$.address", equalTo(user.getAddress())));
    }

    @Test
    @Sql(value = "classpath:data.sql", executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void createUserWithoutRequiredFieldsFail() throws Exception {
        //fail for email is null
        UserDTO user1 = new UserDTO();
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        user1.setBirthDate(LocalDate.of(2000, 1, 1));
        mockMvc.perform(post(URL).content(objectMapper.writeValueAsString(user1))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        //fail for email is empty
        UserDTO user2 = new UserDTO();
        user2.setEmail("");
        user2.setFirstName("firstName");
        user2.setLastName("lastName");
        user2.setBirthDate(LocalDate.of(2000, 1, 1));
        mockMvc.perform(post(URL).content(objectMapper.writeValueAsString(user2))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        //fail for email is not valid
        UserDTO user3 = new UserDTO();
        user3.setEmail("user");
        user3.setFirstName("firstName");
        user3.setLastName("lastName");
        user3.setBirthDate(LocalDate.of(2000, 1, 1));
        mockMvc.perform(post(URL).content(objectMapper.writeValueAsString(user3))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        //fail for first name is empty or null
        UserDTO user4 = new UserDTO();
        user4.setEmail("user@mail.com");
        user4.setFirstName("");
        user4.setLastName("lastName");
        user4.setBirthDate(LocalDate.of(2000, 1, 1));
        mockMvc.perform(post(URL).content(objectMapper.writeValueAsString(user4))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        //fail for last name is empty or null
        UserDTO user5 = new UserDTO();
        user5.setEmail("user@mail.com");
        user5.setFirstName("firstName");
        user5.setLastName("");
        user5.setBirthDate(LocalDate.of(2000, 1, 1));
        mockMvc.perform(post(URL).content(objectMapper.writeValueAsString(user5))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        //fail for birthdate is null
        UserDTO user6 = new UserDTO();
        user6.setEmail("user@mail.com");
        user6.setFirstName("firstName");
        user6.setLastName("lastName");
        mockMvc.perform(post(URL).content(objectMapper.writeValueAsString(user6))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        //fail for birthdate is not valid
        UserDTO user7 = new UserDTO();
        user7.setEmail("user@mail.com");
        user7.setFirstName("firstName");
        user7.setLastName("lastName");
        user7.setBirthDate(LocalDate.of(2022, 1, 1));
        assertThatThrownBy(() -> mockMvc.perform(post(URL).content(objectMapper.writeValueAsString(user7))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)))
                .matches(error ->
                        Objects.equals(((ServletException) error).getRootCause().getMessage(),
                                "You should be older than eighteen"));

        //fail if user already exist
        UserDTO user8 = new UserDTO();
        user8.setEmail("user@mail.com");
        user8.setFirstName("firstName");
        user8.setLastName("lastName");
        user8.setBirthDate(LocalDate.of(2001, 1, 1));
        assertThatThrownBy(() -> mockMvc.perform(post(URL).content(objectMapper.writeValueAsString(user8))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)))
                .matches(error ->
                        Objects.equals(((ServletException) error).getRootCause().getMessage(),
                            "This user is already exist"));
    }

    @Test
    @Sql(value = "classpath:data.sql", executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findAllByDateRangeOk() throws Exception {
        mockMvc.perform(get(URL
                            + "?from=" + LocalDate.of(2000, 1, 1)
                            + "&to=" + LocalDate.of(2001, 12, 12)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect((jsonPath("$[0].firstName", equalTo("firstName"))));
    }

    @Test
    public void findAllByDateRangeFail() {
        assertThatThrownBy(() -> mockMvc.perform(get(URL
                                    + "?from=" + LocalDate.of(2002, 1, 1)
                                    + "&to=" + LocalDate.of(2001, 12, 12))))
                .matches(error ->
                        Objects.equals(((ServletException) error).getRootCause().getMessage(),
                            "First date should be earlier than second"));
    }

    @Test
    @Sql(value = "classpath:data.sql", executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateOk() throws Exception {
        mockMvc.perform(patch(URL + "/0").content("{ \"address\":\"someAddress\" }")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address", equalTo("someAddress")));
    }

    @Test
    @Sql(value = "classpath:data.sql", executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateFail() throws Exception {
        //fail for invalid email
        mockMvc.perform(patch(URL + "/0").content("{ \"email\":\"someEmail\" }")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        //fail if user not found
        assertThatThrownBy(() -> mockMvc.perform(patch(URL + "/2").content("{ \"address\":\"someAddress\" }")
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)))
                .matches(error -> Objects.equals(((ServletException) error).getRootCause().getMessage(),
                        "User not found"));

        //fail for invalid date
        assertThatThrownBy(() -> mockMvc.perform(patch(URL + "/0").content("{ \"birthDate\":\"2022-01-01\" }")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)))
                .matches(error -> Objects.equals(((ServletException) error).getRootCause().getMessage(),
                        "You should be older than eighteen"));
    }

    @Test
    @Sql(value = "classpath:data.sql", executionPhase =
            Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void deleteOk() throws Exception {
        mockMvc.perform(delete(URL + "/0")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get(URL
                        + "?from=" + LocalDate.of(2000, 1, 1)
                        + "&to=" + LocalDate.of(2003, 1, 1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)));

    }

    @Test
    public void deleteFail() {
        //fail if user not found
        assertThatThrownBy(() -> mockMvc.perform(delete(URL + "/0")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)))
                .matches(error -> Objects.equals(((ServletException) error).getRootCause().getMessage(),
                        "User not found"));

    }
}
