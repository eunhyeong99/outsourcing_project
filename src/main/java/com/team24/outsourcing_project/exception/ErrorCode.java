package com.team24.outsourcing_project.exception;

public enum ErrorCode {

    EMPTY_TOKEN(400, "빈 토큰입니다."),

    PASSWORD_MISMATCH(401, "비밀번호가 일치하지 않습니다."),
    TOKEN_EXPIRED(401, "만료된 토큰입니다."),
    UNAUTHORIZED(401, "인증에 실패했습니다."),

    USER_NOT_FOUND(404, "존재하지 않는 유저입니다."),
    STORE_NOT_FOUND(404, "존재하지 않는 가게입니다."),
    MENU_NOT_FOUND(404, "존재하지 않는 메뉴입니다."),
    ORDER_NOT_FOUND(404, "존재하지 않는 주문입니다."),

    EMAIL_DUPLICATE(409, "이미 존재하는 이메일입니다.");

    private final int statusCodee;
    private final String message;

    ErrorCode(final int statusCodee, final String message) {
        this.statusCodee = statusCodee;
        this.message = message;
    }

    public int statusCode() {
        return statusCodee;
    }

    public String message() {
        return message;
    }
}