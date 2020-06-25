package com.paymybuddy.payapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.Bill;
import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User Controller tests on : ")
public class UserControllerTest {
    private final MultiValueMap<String, String> paramsPUT = new LinkedMultiValueMap<>();
    private final MultiValueMap<String, String> paramsPOST = new LinkedMultiValueMap<>();
    // Beans
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    // Mocks
    private MockMvc mvc;
    @MockBean
    private UserService userService;

    private final String ZONE_ID;

    public UserControllerTest(@Value("${default.zoneID}") String ZONE_ID) {
        this.ZONE_ID = ZONE_ID;
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
        paramsPUT.add("id", "6");
        paramsPUT.add("password", "somePass");
        paramsPUT.add("username", "someUsername");
        paramsPUT.add("mail", "someMail@mail.com");
        paramsPUT.add("newPassword", null);


    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("GET on User ")
    public void Given_authenticatedUser_When_getUser_Then_returnUserSpecificInstance() throws Exception {
        User user = new User("user",
                "user@mail.com",
                bcryptEncoder.encode("userpass"),
                Collections.singleton(Role.USER));
        String userJson = objectMapper.writeValueAsString(user);

        when(userService.getUserByMail())
                .thenReturn(Optional.of(user))
                .thenReturn(Optional.empty());

        MvcResult result;

        result = mvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(userJson);

        mvc.perform(get("/user"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("GET on User Balance ")
    public void Given_authenticatedUser_When_getUserBalance_Then_returnDoubleInstance() throws Exception {
        BigDecimal balance = BigDecimal.valueOf(55.00);
        String balanceJson = objectMapper.writeValueAsString(balance);

        when(userService.getUserBalance())
                .thenReturn(Optional.of(balance))
                .thenReturn(Optional.empty());

        MvcResult result;

        result = mvc.perform(get("/user/balance"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(balanceJson);

        mvc.perform(get("/user/balance"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("GET on User Bills ")
    public void Given_authenticatedUser_When_getUserBills_Then_returnBillsCollection() throws Exception {
        ZonedDateTime creationDate = ZonedDateTime.of(2020, 2, 1, 1, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime startDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime endDate = ZonedDateTime.of(2020, 2, 1, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        Bill bill = new Bill(3, 2, creationDate, startDate, endDate, BigDecimal.valueOf(0.50));
        Collection<Bill> returnValue = Collections.singletonList(bill);
        MvcResult result;

        when(userService.getUserBills())
                .thenReturn(Collections.singletonList(bill))
                .thenReturn(Collections.emptyList())
                .thenReturn(null);


        // Collection with object
        String billsJSON = objectMapper.writeValueAsString(returnValue);
        result = mvc.perform(get("/user/bills"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(billsJSON);

        // Empty collection
        billsJSON = objectMapper.writeValueAsString(Collections.emptyList());
        result = mvc.perform(get("/user/bills"))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(billsJSON);

        // Null
        mvc.perform(get("/user/bills"))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("PUT on User profile succeed")
    public void Given_authenticatedUser_When_updateSettings_Then_returnUserID() throws Exception {
        doNothing().when(userService).updateUserProfile(anyString(), anyString(), anyString(), nullable(String.class));
        mvc.perform(put("/user/profile")
                .params(paramsPUT)
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("PUT on User profile wrong values")
    public void Given_constraintEntryViolation_When_updateSettings_Then_statusIsBadRequest() throws Exception {
        doThrow(ConstraintViolationException.class).when(userService)
                .updateUserProfile(anyString(), anyString(), anyString(), nullable(String.class));

        mvc.perform(put("/user/profile")
                .params(paramsPUT)
                .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("PUT on User profile password does not match")
    public void Given_notMatchingPasswords_When_updateSettings_Then_statusIsForbidden() throws Exception {

        doThrow(BadCredentialsException.class).when(userService)
                .updateUserProfile(anyString(), anyString(), anyString(), nullable(String.class));

        mvc.perform(put("/user/profile")
                .params(paramsPUT)
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("PUT on User profile server error")
    public void Given_constraintEntryViolation_When_updateSettings_Then_statusIsInternalServerError() throws Exception {
        doThrow(SQLException.class).when(userService)
                .updateUserProfile(anyString(), anyString(), anyString(), nullable(String.class));

        mvc.perform(put("/user/profile")
                .params(paramsPUT)
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "user@mail.com")
    @DisplayName("POST on User Bill results")
    public void Given_authenticatedUser_When_createUserBill_Then_returnCreatedBill() throws Exception {
        ZonedDateTime creationDate = ZonedDateTime.of(2020, 2, 1, 1, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime startDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime endDate = ZonedDateTime.of(2020, 2, 1, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        Bill bill = new Bill(3, 2, creationDate, startDate, endDate, BigDecimal.valueOf(0.50));
        MvcResult result;
        String billJSON = objectMapper.writeValueAsString(bill);

        when(userService.createBill(any(ZonedDateTime.class), any(ZonedDateTime.class)))
                .thenReturn(bill)
                .thenThrow(IllegalArgumentException.class)
                .thenThrow(RuntimeException.class)
                .thenThrow(SQLException.class);

        MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
        postParams.add("startDateYear",
                String.valueOf(startDate.getYear()));
        postParams.add("startDateMonth",
                String.valueOf(startDate.getMonthValue()));
        postParams.add("startDateDay",
                String.valueOf(startDate.getDayOfMonth()));
        postParams.add("endDateYear",
                String.valueOf(endDate.getYear()));
        postParams.add("endDateMonth",
                String.valueOf(endDate.getMonthValue()));
        postParams.add("endDateDay",
                String.valueOf(endDate.getDayOfMonth()));

        // Object returned
        result = mvc.perform(post("/user/newBill")
                .params(postParams)
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(billJSON);

        // IllegalArgumentException thrown
        mvc.perform(post("/user/newBill")
                .params(postParams)
                .with(csrf()))
                .andExpect(status().isBadRequest());

        // RuntimeException thrown
        mvc.perform(post("/user/newBill")
                .params(postParams)
                .with(csrf()))
                .andExpect(status().isInternalServerError());

        // SQLException thrown
        mvc.perform(post("/user/newBill")
                .params(postParams)
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }
}
