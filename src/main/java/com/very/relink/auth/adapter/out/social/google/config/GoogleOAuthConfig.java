package com.very.relink.auth.adapter.out.social.google.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class GoogleOAuthConfig {

    @Value("${social.google.client-ids.web}")
    private String web;

    @Value("${social.google.client-ids.android}")
    private String android;

    @Value("${social.google.client-ids.ios}")
    private String ios;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        List<String> allowedClientIds = Stream.of(web, android, ios)
                .filter(StringUtils::hasText)
                .toList();

        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance()
        )
                .setAudience(allowedClientIds)
                .build();
    }
}
