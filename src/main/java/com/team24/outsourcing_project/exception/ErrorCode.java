package com.team24.outsourcing_project.exception;

public enum ErrorCode {

    NOT_FOUND_TOKEN(400,"토큰을 찾지 못했습니다."),
    EMPTY_TOKEN(400, "빈 토큰입니다."),
    INVALID_REVIEW_SCORE_RANGE(400, "유효하지 않은 범위의 별점입니다."),
    INVALID_ORDER_STATUS(400, "유효하지 않는 주문 상태입니다."),
    REVIEW_MUST_BE_MY_ORDER(400, "자신의 주문에만 리뷰를 담길 수 있습니다"),
    ROLE_INVALID(400, "존재하지 않는 권한입니다."),
    EMAIL_NULL(400, "잘못된 이메일입니다."),
    ALREADY_DELETED(400, "이미 탈퇴한 회원입니다."),
    MIN_ORDER_PRICE(400, "최소 주문 금액을 확인해 주세요."),
    STORE_TIME(400, "매장 운영 시간을 확인해 주세요."),
    OWNER_ROLE(400, "OWNER 권한이 없습니다."),
    PENDING_STATUS(400, "주문 접수 상태가 아닙니다."),
    STORE_STATUS_CHANGE_FAILED(400, "스토어 상태 변경에 실패했습니다."),


    PASSWORD_MISMATCH(401, "비밀번호가 일치하지 않습니다."),
    TOKEN_EXPIRED(401, "만료된 토큰입니다."),
    UNAUTHORIZED(401, "인증에 실패했습니다."),
    UNAUTHORIZED_DELETE_USER(401,"삭제 권한이 없습니다."),
    NOT_OWNER(401, "사장이 아닙니다."),
    INVAILD_MINORDERPRICE(400, "최소 주문 금액에 음수가 들어갈 수 없습니다."),
    EQUALS_OPEN_CLOSE_TIME(400, "오픈 시간과 마감 시간이 똑같을 수 없습니다"),
    OUT_STORE(400, "폐업한 가게입니다."),
    NOT_USER(401,"손님이 아닙니다."),


    STORE_MAX_OUT(403,"이미 유저의 가게가 3개 이상입니다."),

    USER_NOT_FOUND(404, "존재하지 않는 유저입니다."),
    STORE_NOT_FOUND(404, "존재하지 않는 가게입니다."),
    MENU_NOT_FOUND(404, "존재하지 않는 메뉴입니다."),
    ORDER_NOT_FOUND(404, "존재하지 않는 주문입니다."),

    EMAIL_DUPLICATE(409, "이미 존재하는 이메일입니다."),
    STORE_DUPLICATE(409, "이미 가게가 존재합니다.");

    private final int statusCode;
    private final String message;

    ErrorCode(final int statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int statusCode() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
