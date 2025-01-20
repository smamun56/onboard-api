package com.hr.onboard.interceptor;

import com.hr.onboard.controller.constraint.checker.ControllerAuthConstraintChecker;
import com.hr.onboard.controller.constraint.checker.ControllerRateConstraintChecker;
import com.hr.onboard.exception.ControllerConstraintViolation;
import com.hr.onboard.utils.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class ControllerConstraintInterceptor implements HandlerInterceptor {
    /*
  ada 2 intercep yang dipake, auth dan rate try
   */
    @Autowired
    ControllerAuthConstraintChecker authConstraintChecker;
    @Autowired
    ControllerRateConstraintChecker rateConstraintChecker;

    private static final Logger logger = LoggerFactory.getLogger(ControllerConstraintInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        IPUtils.setRequestIp(request.getRemoteAddr());
        try {
            if (handler instanceof HandlerMethod) {
                authConstraintChecker.checkWithMethod(((HandlerMethod) handler).getMethod());
                rateConstraintChecker.checkWithMethod(((HandlerMethod) handler).getMethod());
            }
        } catch (ControllerConstraintViolation ex) {
            setJsonResponse(response, ex.getRejectStatus(), ex.getRejectMessage());
            return false;
        }catch (Exception e) {
            logger.error(e.getMessage());
            setJsonResponse(response, 500,"");
            return false;
        }
        return true;
    }

    private void setJsonResponse(HttpServletResponse response, int status, String message) {
        if(message != null && !message.isEmpty()) {
            try {
                response.getWriter().write("{ \"message\": \"" + message + "\"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
    }
}
