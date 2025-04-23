package com.example.HK.security.handler;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    HttpSession session;
    /*
     * ログイン失敗時のエラーメッセージ表示処理
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        List<String> errorMessages = new ArrayList<>();

        if(StringUtils.isBlank(request.getParameter("account"))){
            errorMessages.add("アカウントを入力してください");
        }
        if(StringUtils.isBlank(request.getParameter("password"))){
            errorMessages.add("パスワードを入力してください");
        }
        if(CollectionUtils.isEmpty(errorMessages)){
            errorMessages.add("ログインに失敗しました");
        }
        session.setAttribute("errorMessages", errorMessages);
        // エラーメッセージをログイン画面に渡すクエリパラメータを追加してリダイレクト
        response.sendRedirect(request.getContextPath() + "/toLogin");
    }
}