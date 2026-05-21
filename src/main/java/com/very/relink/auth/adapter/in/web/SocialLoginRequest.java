package com.very.relink.auth.adapter.in.web;

import com.very.relink.auth.domain.value.OAuth2Provider;
import jakarta.validation.constraints.NotNull;

public record SocialLoginRequest(
        @NotNull
        OAuth2Provider provider,

        String idToken,

        String accessToken,

        String name
) {
}