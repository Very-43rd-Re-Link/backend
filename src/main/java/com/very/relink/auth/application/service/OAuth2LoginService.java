package com.very.relink.auth.application.service;

import com.very.relink.auth.application.command.OAuth2LoginCommand;
import com.very.relink.auth.application.port.in.OAuth2LoginUseCase;
import com.very.relink.auth.application.port.out.TokenIssuePort;
import com.very.relink.auth.application.result.OAuth2LoginResult;
import com.very.relink.auth.domain.token.AuthTokens;
import com.very.relink.auth.exception.AuthErrorCode;
import com.very.relink.member.application.port.out.LoadMemberPort;
import com.very.relink.member.application.port.out.SaveMemberPort;
import com.very.relink.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService implements OAuth2LoginUseCase {

    private final LoadMemberPort loadMemberPort;
    private final SaveMemberPort saveMemberPort;
    private final TokenIssuePort tokenIssuePort;

    @Override
    @Transactional
    public OAuth2LoginResult login(OAuth2LoginCommand oAuth2LoginCommand) {
        String providerId = oAuth2LoginCommand.providerId();
        if (providerId == null || providerId.isEmpty()) {
            throw AuthErrorCode.OAUTH2_LOGIN_FAILED.toException();
        }

        Member member = loadMemberPort.findByProviderAndProviderId(
                        oAuth2LoginCommand.provider(),
                        providerId
                )
                .orElseGet(() -> saveMemberPort.save(
                        Member.create(
                                oAuth2LoginCommand.email(),
                                oAuth2LoginCommand.name(),
                                oAuth2LoginCommand.imageUrl(),
                                oAuth2LoginCommand.provider(),
                                providerId
                        )
                ));

        AuthTokens authTokens = tokenIssuePort.issue(member);
        return new OAuth2LoginResult(member.getId(), authTokens);
    }
}
