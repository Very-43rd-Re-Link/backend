package com.very.relink.auth.application.port.in;

import com.very.relink.auth.application.command.SocialLoginCommand;
import com.very.relink.auth.application.result.OAuth2LoginResult;

public interface SocialLoginUseCase {

    OAuth2LoginResult login(SocialLoginCommand socialLoginCommand);
}
