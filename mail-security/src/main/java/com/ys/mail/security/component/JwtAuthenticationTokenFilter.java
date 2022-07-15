package com.ys.mail.security.component;

import cn.hutool.core.util.EnumUtil;
import com.alibaba.fastjson.JSONObject;
import com.ys.mail.constant.RequestMethod;
import com.ys.mail.security.util.JwtTokenUtil;
import com.ys.mail.service.UmsUserBlacklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT登录授权过滤器
 *
 * @author ghdhj
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UmsUserBlacklistService blacklistService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 请求日志
        String requestMethod = request.getMethod();
        String requestUri = request.getRequestURI();
        if (EnumUtil.contains(RequestMethod.class, requestMethod) && !requestUri.contains(".")) {
            LOGGER.info("{} [{}], parameters={}", requestMethod, requestUri, JSONObject.toJSONString(request.getParameterMap()));
        }

        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            // TODO The part after "Bearer" 从redis取出token是否存在,不存在则是退出登录,注册和登录封装起来不走这个方法
            String authToken = authHeader.substring(this.tokenHead.length());
            // TODO 白名单请求直接放行,在白名单里面继续走方法,拦截以外的路由,如果是在路由以内不需判断全部放行,在路由以外需判断
            String username = jwtTokenUtil.getUserNameFromToken(authToken);
            LOGGER.info("checking username:{}", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 黑名单检测
                blacklistService.checkPhone(username);

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOGGER.info("authenticated user:{}", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        }
        chain.doFilter(request, response);
    }
}
