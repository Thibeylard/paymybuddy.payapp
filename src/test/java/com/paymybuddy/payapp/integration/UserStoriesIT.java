package com.paymybuddy.payapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.payapp.dtos.ContactUserDTO;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.*;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO test d'intégration sur suppression de contact
//TODO test d'integration sur les bank accounts
//TODO test d'integration sur création de facture

@ExtendWith(SpringExtension.class)
@ExtendWith({FlywayTestExtension.class})
@SpringBootTest
@ActiveProfiles("prod")
@DisplayName("User Stories tests : ")
public class UserStoriesIT {
    // Beans
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    // Mocks
    private MockMvc mvc;
    //Attributes
    private String baseUrl;
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    private MvcResult result;
    private String jsonExpected;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // Integrate SpringSecurity to SpringMVC
                .build();
        baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    @Test
    @FlywayTest
    @DisplayName("Story#1 : New User got blank account and add bank account")
    public void newUserSignsInAndAddBankAccountToFreshAccount() throws Exception {
        String userPass = "fiftybucks";

        User user = new User("maurobertson",
                "maurice.robertson@example.com",
                bcryptEncoder.encode(userPass),
                Collections.singletonList(Role.USER));

        // ---------------------------------------------------------------- User signs in
        params.clear();
        params.add("username", user.getUsername());
        params.add("mail", user.getMail());
        params.add("password", userPass);
        mvc.perform(post("/registration")
                .params(params)
                .with(csrf())
                .with(anonymous()))
                .andExpect(status().isOk());

        // ---------------------------------------------------------------- User logs in successfully
        mvc.perform(formLogin()
                .user(user.getMail())
                .password(userPass))
                .andExpect(authenticated());

        // ---------------------------------------------------------------- User has been properly saved
        result = mvc.perform(get("/user")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        User savedUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(user.getMail()).isEqualTo(savedUser.getMail());
        assertThat(user.getRoles()).usingRecursiveFieldByFieldElementComparator().isEqualTo(savedUser.getRoles());
        assertThat(bcryptEncoder.matches(userPass, user.getPassword())).isTrue();
        assertThat(bcryptEncoder.matches(userPass, savedUser.getPassword())).isTrue();

        // ---------------------------------------------------------------- User has no contacts
        result = mvc.perform(get("/contacts")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User has no transaction
        result = mvc.perform(get("/transactions")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User has balance at 0
        result = mvc.perform(get("/user/balance")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(BigDecimal.valueOf(0.0));

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User has no bill
        result = mvc.perform(get("/user/bills")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User has no bank account
        result = mvc.perform(get("/bankAccounts")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User add bank account
        params.clear();
        params.add("ownerFullName", "Maurice ROBERTSON");
        params.add("description", "My bank account");
        params.add("IBAN", "GB56BARC20038498695629");
        mvc.perform(post("/bankAccounts")
                .params(params)
                .with(user(user.getMail()))
                .with(csrf()))
                .andExpect(status().isCreated());

        // ---------------------------------------------------------------- User has now one bank account
        result = mvc.perform(get("/bankAccounts")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        Collection<BankAccount> userBankAccounts = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        assertThat(userBankAccounts)
                .extracting("id").contains(5);
        assertThat(userBankAccounts)
                .extracting("iban").contains("GB56BARC20038498695629");

        // ---------------------------------------------------------------- There is no operation on this bank account
        params.clear();
        params.add("bankAccountID", "5");
        result = mvc.perform(get("/bankAccount/operations")
                .params(params)
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- User transfer money to its account
        params.clear();
        params.add("bankAccountID", "5");
        params.add("amount", "200.0");
        mvc.perform(post("/bankAccount/withdraw")
                .params(params)
                .with(csrf())
                .with(user(user.getMail())))
                .andExpect(status().isOk());

        // ---------------------------------------------------------------- User has now one operation on bank account
        params.clear();
        params.add("bankAccountID", "5");
        result = mvc.perform(get("/bankAccount/operations")
                .params(params)
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        Collection<BankOperation> userBankOperations = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        assertThat(userBankOperations)
                .extracting("id").contains(9);
        assertThat(userBankOperations)
                .extracting("bankAccountID").contains(5);

        // ---------------------------------------------------------------- User get their updated balance
        result = mvc.perform(get("/user/balance")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("200.0");

        // ---------------------------------------------------------------- User delete its bank account
        params.clear();
        params.add("bankAccountID", "5");
        mvc.perform(delete("/bankAccounts")
                .params(params)
                .with(user(user.getMail()))
                .with(csrf()))
                .andExpect(status().isOk());

        // ---------------------------------------------------------------- User has no more bank account
        result = mvc.perform(get("/bankAccounts")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);

        // ---------------------------------------------------------------- There is no more operation about this bank account
        params.clear();
        params.add("bankAccountID", "5");
        result = mvc.perform(get("/bankAccount/operations")
                .params(params)
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(jsonExpected);
    }

    @Test
    @FlywayTest
    @DisplayName("Story#2 : User update profile")
    public void userUpdateProfileSuccessfully() throws Exception {
        final String userPass = "superarcher";

        User user = new User("leslin",
                "leslie.austin@example.com",
                bcryptEncoder.encode(userPass),
                Collections.singletonList(Role.USER));

        // ---------------------------------------------------------------- User logs in successfully
        params.clear();
        mvc.perform(formLogin()
                .user(user.getMail())
                .password(userPass))
                .andExpect(authenticated());

        // ---------------------------------------------------------------- User is retrieved correctly
        result = mvc.perform(get("/user")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        User savedUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(user.getMail()).isEqualTo(savedUser.getMail());
        assertThat(user.getRoles()).usingRecursiveFieldByFieldElementComparator().isEqualTo(savedUser.getRoles());
        assertThat(bcryptEncoder.matches(userPass, user.getPassword())).isTrue();
        assertThat(bcryptEncoder.matches(userPass, savedUser.getPassword())).isTrue();

        // ---------------------------------------------------------------- User update its profile
        UserDetails userDetails = new UserCredentials(user.getMail(), user.getPassword(), user.getRoles());

        user = new User("leslied",
                "leslie.married@example.com",
                bcryptEncoder.encode(userPass),
                Collections.singletonList(Role.USER));

        params.add("username", user.getUsername());
        params.add("mail", user.getMail());
        params.add("password", userPass);
        result = mvc.perform(put("/user/profile")
                .params(params)
                .with(user(userDetails))
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        // ---------------------------------------------------------------- User has been properly modified
        result = mvc.perform(get("/user")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        savedUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(user.getMail()).isEqualTo(savedUser.getMail());

        // ---------------------------------------------------------------- All User data can be accessed normally
        // ---------------------------------------------------------------- User has contacts with id 1 and 3
        result = mvc.perform(get("/contacts")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        ArrayList<ContactUserDTO> userContacts = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        assertThat(userContacts.size()).isEqualTo(2);
        assertThat(userContacts)
                .extracting("id").contains(1, 3);
        assertThat(userContacts)
                .extracting("username").contains("antowright", "nharvey");

        // ---------------------------------------------------------------- User has three transactions
        result = mvc.perform(get("/transactions")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        ArrayList<Transaction> userTransactions = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        assertThat(userTransactions.size()).isEqualTo(3);
        assertThat(userTransactions)
                .extracting("id").contains(1, 3, 6);
        assertThat(userTransactions)
                .extracting("description").contains("restaurant bill", "misc drinks", "flat-share week rent");
    }

    @Test
    @FlywayTest
    @DisplayName("Story#3 : User send money to new contact")
    public void existingUserLogsInAndAsksForBill() throws Exception {

        final String userPass = "book7books";

        User user = new User("antowright",
                "antonio.wright@example.com",
                bcryptEncoder.encode(userPass),
                Collections.singletonList(Role.USER));

        // ---------------------------------------------------------------- User logs in successfully
        params.clear();
        mvc.perform(formLogin()
                .user(user.getMail())
                .password(userPass))
                .andExpect(authenticated());

        // ---------------------------------------------------------------- User has contacts with id 1 and 2
        result = mvc.perform(get("/contacts")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        ArrayList<ContactUserDTO> userContacts = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        assertThat(userContacts.size()).isEqualTo(2);
        assertThat(userContacts)
                .extracting("id").contains(1, 2);
        assertThat(userContacts)
                .extracting("username").contains("leslin", "nharvey");

        // ---------------------------------------------------------------- User add new contact
        params.clear();
        params.add("contactMail", "diana.moreno@example.com");
        mvc.perform(post("/contacts")
                .params(params)
                .with(csrf())
                .with(user(user.getMail())))
                .andExpect(status().isCreated());

        // ---------------------------------------------------------------- User has contacts with id 1, 2 and 4
        result = mvc.perform(get("/contacts")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        userContacts = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        assertThat(userContacts.size()).isEqualTo(3);
        assertThat(userContacts)
                .extracting("id").contains(1, 2, 4);
        assertThat(userContacts)
                .extracting("username").contains("leslin", "nharvey", "dianoreno");

        // ---------------------------------------------------------------- User get their balance
        result = mvc.perform(get("/user/balance")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("67.9");

        // ---------------------------------------------------------------- User send money to new contact
        params.clear();
        params.add("recipientMail", "diana.moreno@example.com");
        params.add("description", "parking bill");
        params.add("amount", "5");
        mvc.perform(post("/transactions")
                .params(params)
                .with(csrf())
                .with(user(user.getMail())))
                .andExpect(status().isCreated());

        // ---------------------------------------------------------------- User get their debit transactions
        result = mvc.perform(get("/transactions/debit")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        userContacts = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        assertThat(userContacts.size()).isEqualTo(4);
        assertThat(userContacts)
                .extracting("id").contains(2, 4, 6, 7);
        assertThat(userContacts)
                .extracting("description").contains("cinema ticket", "car pool", "flat-share week rent", "parking bill");

        // ---------------------------------------------------------------- User get their credit transactions
        result = mvc.perform(get("/transactions/credit")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        userContacts = objectMapper.readValue(result.getResponse().getContentAsString(), ArrayList.class);

        assertThat(userContacts.size()).isEqualTo(1);
        assertThat(userContacts)
                .extracting("id").contains(3);
        assertThat(userContacts)
                .extracting("description").contains("misc drinks");

        // ---------------------------------------------------------------- User get their updated balance
        result = mvc.perform(get("/user/balance")
                .with(user(user.getMail())))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("62.9");
    }
}
