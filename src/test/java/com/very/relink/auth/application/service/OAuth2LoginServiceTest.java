package com.very.relink.auth.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.very.relink.auth.application.command.OAuth2LoginCommand;
import com.very.relink.auth.application.port.out.TokenIssuePort;
import com.very.relink.auth.application.result.OAuth2LoginResult;
import com.very.relink.auth.domain.token.AuthTokens;
import com.very.relink.auth.domain.value.OAuth2Provider;
import com.very.relink.member.application.port.out.LoadMemberPort;
import com.very.relink.member.application.port.out.SaveMemberPort;
import com.very.relink.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OAuth2LoginServiceTest {

    @Test
    @DisplayName("returns saved member id for new kakao member")
    void loginReturnsSavedMemberId() {
        LoadMemberPort loadMemberPort = new FakeLoadMemberPort(Optional.empty());
        SaveMemberPort saveMemberPort = member -> Member.builder()
                .id(1L)
                .email(member.getEmail())
                .name(member.getName())
                .imageUrl(member.getImageUrl())
                .provider(member.getProvider())
                .providerId(member.getProviderId())
                .build();
        TokenIssuePort tokenIssuePort = member -> new AuthTokens("access-token", "Bearer", 3600L);
        OAuth2LoginService service = new OAuth2LoginService(
                loadMemberPort,
                saveMemberPort,
                tokenIssuePort
        );

        OAuth2LoginResult result = service.login(new OAuth2LoginCommand(
                OAuth2Provider.KAKAO,
                "123456789",
                "kakao@example.com",
                "kakao-user",
                "https://example.com/profile.png"
        ));

        assertThat(result.memberId()).isEqualTo(1L);
        assertThat(result.authTokens().accessToken()).isEqualTo("access-token");
    }

    @Test
    @DisplayName("returns existing member id for existing kakao member")
    void loginReturnsExistingMemberId() {
        Member existingMember = Member.builder()
                .id(2L)
                .email("kakao@example.com")
                .name("kakao-user")
                .imageUrl("https://example.com/profile.png")
                .provider(OAuth2Provider.KAKAO)
                .providerId("123456789")
                .build();
        LoadMemberPort loadMemberPort = new FakeLoadMemberPort(Optional.of(existingMember));
        SaveMemberPort saveMemberPort = member -> {
            throw new IllegalStateException("Existing member should not be saved again.");
        };
        TokenIssuePort tokenIssuePort = member -> new AuthTokens("access-token", "Bearer", 3600L);
        OAuth2LoginService service = new OAuth2LoginService(
                loadMemberPort,
                saveMemberPort,
                tokenIssuePort
        );

        OAuth2LoginResult result = service.login(new OAuth2LoginCommand(
                OAuth2Provider.KAKAO,
                "123456789",
                "kakao@example.com",
                "kakao-user",
                "https://example.com/profile.png"
        ));

        assertThat(result.memberId()).isEqualTo(2L);
        assertThat(result.authTokens().accessToken()).isEqualTo("access-token");
    }

    private record FakeLoadMemberPort(Optional<Member> member) implements LoadMemberPort {

        @Override
        public Optional<Member> findByEmail(String email) {
            return Optional.empty();
        }

        @Override
        public Optional<Member> findByProviderAndProviderId(
                OAuth2Provider provider,
                String providerId
        ) {
            return member.filter(value ->
                    value.getProvider() == provider && value.getProviderId().equals(providerId)
            );
        }
    }
}
