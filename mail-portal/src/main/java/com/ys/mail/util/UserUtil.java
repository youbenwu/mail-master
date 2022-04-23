package com.ys.mail.util;

import com.ys.mail.bo.UmsUserDetails;
import com.ys.mail.entity.UmsUser;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.CommonResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @author： DT
 * @date： 2021-09-15 09:41
 */
@Component
@RequiredArgsConstructor
public class UserUtil {

    public static UmsUser getCurrentUser() {
        UmsUser user;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UmsUserDetails userDetails = (UmsUserDetails) principal;
            user = userDetails.getUmsUser();
        } else {
            throw new ApiException(CommonResultCode.ERR_LOGIN_FAILURE);
        }
        return user;
    }
}
