package com.atguigu.interceptor;

import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.WebUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获session域中的UserInfo对象
        UserInfo userInfo= (UserInfo) request.getSession().getAttribute("user");
        if(null==userInfo)
        {
            //证明还没登录
            Result<String> result = Result.build("还没有登录", ResultCodeEnum.LOGIN_AUTH);
            //响应result对象到前端
            WebUtil.writeJSON(response,result);
            return false;
        }
        return true;

    }
}
