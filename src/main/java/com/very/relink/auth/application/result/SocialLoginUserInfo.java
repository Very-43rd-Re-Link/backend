package com.very.relink.auth.application.result;

import com.very.relink.auth.domain.value.OAuth2Provider;

public record SocialLoginUserInfo(
        OAuth2Provider oAuth2Provider,
        String providerId,
        String email,
        String name,
        String imageUrl
) {
}
