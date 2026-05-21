package com.very.relink.auth.adapter.in.web;

import com.very.relink.auth.domain.token.AuthTokens;

public record SocialLoginResponse(
        Long memberId,
        String accessToken,
        Long accessTokenExpiresIn
) {

    public static SocialLoginResponse from(Long memberId, AuthTokens authTokens) {
        return new SocialLoginResponse(
                memberId,
                authTokens.accessToken(),
                authTokens.expiresIn()
        );
    }
}
