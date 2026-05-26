package com.very.relink.auth.adapter.out.social.google;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.very.relink.auth.application.command.SocialLoginCommand;
import com.very.relink.auth.application.port.out.SocialUserResolver;
import com.very.relink.auth.application.result.SocialLoginUserInfo;
import com.very.relink.auth.domain.value.OAuth2Provider;
import com.very.relink.auth.exception.AuthErrorCode;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleUserResolver implements SocialUserResolver {

    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    @Override
    public OAuth2Provider supports() {
        return OAuth2Provider.GOOGLE;
    }

    @Override
    public SocialLoginUserInfo resolve(SocialLoginCommand socialLoginCommand) {

        if(socialLoginCommand.idToken() == null || socialLoginCommand.idToken().isBlank()) {
            throw AuthErrorCode.NOT_FOUND_GOOGLE_IDTOKEN.toException();
        }

        try {
            GoogleIdToken idToken = googleIdTokenVerifier.verify(socialLoginCommand.idToken());

            if (idToken == null) {
                throw AuthErrorCode.OAUTH2_LOGIN_FAILED.toException();
            }

            Payload payload = idToken.getPayload();


            return new SocialLoginUserInfo(
                    OAuth2Provider.GOOGLE,
                    payload.getSubject(),
                    payload.getEmail(),
                    (String) payload.get("name"),
                    (String) payload.get("picture")
            );

        } catch (GeneralSecurityException | IOException e) {
            log.error("Google idToken 파싱 에러 - {}", e.getMessage());
            throw AuthErrorCode.OAUTH2_LOGIN_FAILED.toException();
        }
    }
}
