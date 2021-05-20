package io.github.raeperd.realworld.application.user;

import io.github.raeperd.realworld.IntegrationTestUtils;
import io.github.raeperd.realworld.application.security.WithMockJWTUser;
import io.github.raeperd.realworld.domain.jwt.JWTDeserializer;
import io.github.raeperd.realworld.domain.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileRestController.class)
class ProfileRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProfileService profileService;
    @MockBean
    private JWTDeserializer jwtDeserializer;

    @Test
    void when_get_profile_with_not_exists_username_expect_notFound_status() throws Exception {
        when(profileService.viewProfileWithUserName(any(UserName.class))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/profiles/{username}", "user-name-not-exists"))
                .andExpect(status().isNotFound());
    }

    @Test
    void when_get_profile_with_username_expect_valid_ProfileModel() throws Exception {
        when(profileService.viewProfileWithUserName(new UserName("sample-user-name"))).thenReturn(sampleProfile());

        mockMvc.perform(get("/profiles/{username}", "sample-user-name"))
                .andExpect(status().isOk())
                .andExpect(IntegrationTestUtils.validProfileModel());
    }

    @WithMockJWTUser
    @Test
    void when_get_profile_with_auth_and_not_exists_username_expect_notFound_status() throws Exception {
        when(profileService.viewProfileFromUser(anyLong(), any(UserName.class))).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/profiles/{username}", "user-name-not-exists"))
                .andExpect(status().isNotFound());
    }

    @WithMockJWTUser
    @Test
    void when_get_profile_with_username_and_auth_expect_valid_ProfileModel() throws Exception {
        when(profileService.viewProfileFromUser(anyLong(), eq(new UserName("sample-user-name")))).thenReturn(sampleProfile());

        mockMvc.perform(get("/profiles/{username}", "sample-user-name"))
                .andExpect(status().isOk())
                .andExpect(IntegrationTestUtils.validProfileModel());
    }

    private Profile sampleProfile() {
        return ProfileTestUtils.profileOf(new UserName("sample-user-name"),
                "sample-bio",
                new Image("sample-image"),
                false);
    }

}
