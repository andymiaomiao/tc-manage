package com.opencloud.device.exception;

import com.opencloud.device.model.ResultBody;
import com.opencloud.device.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义oauth2异常提示
 * @author liuyadu
 */
@Slf4j
public class BaseOauth2WebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        HttpServletRequest request = WebUtil.getHttpServletRequest();
        ResultBody resultBody = GlobalExceptionHandler.resolveException(e,request.getRequestURI());
        return ResponseEntity.status(resultBody.getHttpStatus()).body(resultBody);
    }
}
