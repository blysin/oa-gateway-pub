package com.linewell.oa.gateway.domail;

import lombok.Data;

/**
 * @author Blysin
 * @since 2019-01-16
 */
@Data
public class Token {
    private String token;
    private long createTimeMillis;
}
