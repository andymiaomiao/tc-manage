package com.weiqisen.tc.exception;

import com.weiqisen.tc.model.ResultBody;
import com.weiqisen.tc.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义访问拒绝
 * @author weiqisen
 */
@Slf4j
public class BaseAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        ResultBody resultBody = GlobalExceptionHandler.resolveException(exception,request.getRequestURI());
        response.setStatus(resultBody.getHttpStatus());
        WebUtil.writeJson(response, resultBody);
    }
}
