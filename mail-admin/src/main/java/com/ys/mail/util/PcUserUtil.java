package com.ys.mail.util;

import com.ys.mail.entity.PcUser;
import com.ys.mail.exception.ApiException;
import com.ys.mail.exception.code.CommonResultCode;
import com.ys.mail.security.PcUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author DT
 * @version 1.0
 * @date 2021-10-27 10:02
 */

public class PcUserUtil {

    public static PcUser getCurrentUser(){
        PcUser user ;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails){
            PcUserDetails userDetails = (PcUserDetails) principal;
            user = userDetails.getUser();
        }else{
            throw new ApiException(CommonResultCode.ERR_LOGIN_FAILURE);
        }
        return user;
    }
}
