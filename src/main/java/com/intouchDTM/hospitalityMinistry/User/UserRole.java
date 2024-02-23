package com.intouchDTM.hospitalityMinistry.User;

import lombok.Getter;

@Getter
public enum UserRole {
    //간사님, 목사님, 기타 관리자
    ROLE_ADMIN("관리자"),

    //리더
    ROLE_LEADER("리더"),

    //셀원
    ROLE_MEMBER("셀원");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }
}
