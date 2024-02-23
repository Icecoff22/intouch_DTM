package com.intouchDTM.hospitalityMinistry.OAuth2;

import com.intouchDTM.hospitalityMinistry.User.AuthProvider;
import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider authProvider, Map<String, Object> attributes) {
        return switch (authProvider) {
            //case GOOGLE: return new GoogleOAuth2User(attributes);
            case NAVER -> new NaverOAuth2User(attributes);
            case KAKAO -> new KakaoOAuth2User(attributes);
            default -> throw new IllegalArgumentException("Invalid Provider Type.");
        };
    }
}
