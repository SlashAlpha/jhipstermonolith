package slash.process.meapp.web.rest;

import slash.process.meapp.MeApp;
import slash.process.meapp.config.TestSecurityConfiguration;
import slash.process.meapp.domain.User;
import slash.process.meapp.repository.UserRepository;
import slash.process.meapp.security.AuthoritiesConstants;
import slash.process.meapp.service.dto.UserDTO;
import slash.process.meapp.service.mapper.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureWebTestClient
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@SpringBootTest(classes = {MeApp.class, TestSecurityConfiguration.class})
public class UserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";

    private static final String DEFAULT_ID = User.PREFIX + DEFAULT_LOGIN;

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";

    private static final String DEFAULT_LASTNAME = "doe";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";

    private static final String DEFAULT_LANGKEY = "en";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WebTestClient webTestClient;

    private User user;

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    /**
     * Create a User.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static User createEntity() {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN);
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    @BeforeEach
    public void initTest() {
        userRepository.deleteAll().block();
        user = createEntity();
    }

    @Test
    public void getAllUsers() {
        // Initialize the database
        userRepository.save(user).block();

        // Get all the users
        UserDTO foundUser = webTestClient.get().uri("/api/users?sort=createdDate,DESC")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .returnResult(UserDTO.class).getResponseBody().blockFirst();

        assertThat(foundUser.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(foundUser.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(foundUser.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(foundUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(foundUser.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(foundUser.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
    }

    @Test
    public void getUser() {
        // Initialize the database
        userRepository.save(user).block();

        // Get the user
        webTestClient.get().uri("/api/users/{login}", user.getLogin())
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.login").isEqualTo(user.getLogin())
            .jsonPath("$.firstName").isEqualTo(DEFAULT_FIRSTNAME)
            .jsonPath("$.lastName").isEqualTo(DEFAULT_LASTNAME)
            .jsonPath("$.email").isEqualTo(DEFAULT_EMAIL)
            .jsonPath("$.imageUrl").isEqualTo(DEFAULT_IMAGEURL)
            .jsonPath("$.langKey").isEqualTo(DEFAULT_LANGKEY);

    }

    @Test
    public void getNonExistingUser() {
        webTestClient.get().uri("/api/users/unknown")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    public void testUserEquals() throws Exception {
        TestUtil.equalsVerifier(User.class);
        User user1 = new User();
        user1.setId("id1");
        User user2 = new User();
        user2.setId(user1.getId());
        assertThat(user1).isEqualTo(user2);
        user2.setId("id2");
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    public void testUserDTOtoUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setCreatedBy(DEFAULT_LOGIN);
        userDTO.setLastModifiedBy(DEFAULT_LOGIN);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.getActivated()).isEqualTo(true);
        assertThat(user.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(user.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getLastModifiedBy()).isNull();
        assertThat(user.getLastModifiedDate()).isNotNull();
        assertThat(user.getAuthorities()).containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    public void testUserToUserDTO() {
        user.setId(DEFAULT_ID);
        user.setCreatedBy(DEFAULT_LOGIN);
        user.setCreatedDate(Instant.now());
        user.setLastModifiedBy(DEFAULT_LOGIN);
        user.setLastModifiedDate(Instant.now());
        user.setAuthorities(Stream.of(AuthoritiesConstants.USER).collect(Collectors.toSet()));

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertThat(userDTO.getId()).isEqualTo(DEFAULT_ID);
        assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.isActivated()).isEqualTo(true);
        assertThat(userDTO.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userDTO.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userDTO.getCreatedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getCreatedDate()).isEqualTo(user.getCreatedDate());
        assertThat(userDTO.getLastModifiedBy()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate());
        assertThat(userDTO.getAuthorities()).containsExactly(AuthoritiesConstants.USER);
        assertThat(userDTO.toString()).isNotNull();
    }

    private void assertPersistedUsers(Consumer<List<User>> userAssertion) {
        // The security filter chain clears the security context after remote calls
        SecurityContextHolder.setContext(TestSecurityContextHolder.getContext());
        userAssertion.accept(userRepository.findAll().collectList().block());
    }
}
