package com.ra.order.util;

import java.util.UUID;

public class Constants {
    public static final String SUCCESS_MSG = "success";
    public static final String CREATED_MSG = "created";
    public static final String ERROR_MSG = "error";
    public static final String UPDATED_MSG = "updated";
    public static final String DELETED_MSG = "deleted";
    public static final String NOT_FOUND_MSG = "not found";
    public static final String INVOICE_ENDPOINT = "/api/invoice";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

    /*
     * Random Identifier result :
     * [0] -> time_low
     * [1] -> time_mid
     * [2] -> time_hi_and_version
     * [3] -> clock_seq_hi_and_res
     * [4] -> node
     */
    public static String[] randomIdentifier(String s) {
        byte[] b = s.getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(b);
        return uuid.toString().split("-");
    }
}
