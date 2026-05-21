package com.very.relink.auth.application.port.out;

import com.very.relink.auth.application.command.SocialLoginCommand;
import com.very.relink.auth.application.result.SocialLoginUserInfo;
import com.very.relink.auth.domain.value.OAuth2Provider;

public interface SocialUserResolver {

    OAuth2Provider supports();

    SocialLoginUserInfo resolve(SocialLoginCommand socialLoginCommand);

}
