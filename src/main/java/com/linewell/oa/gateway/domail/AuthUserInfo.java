package com.linewell.oa.gateway.domail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Blysin
 * @since 2018-11-08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserInfo {
    private boolean hasBindUser = true;
    private String dingdingUserId;
    private String userunid;
    private String username;
    private String deptunid;
    private String deptname;
    private String message;

    public static AuthUserInfo bindError(){
        AuthUserInfo authUserInfo = new AuthUserInfo();
        authUserInfo.setHasBindUser(false);
        return authUserInfo;
    }
}
