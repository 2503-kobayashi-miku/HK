package com.example.HK.security.handler;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    HttpSession session;
    /*
     * 未ログイン時のエラーメッセージ表示処理
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        session = request.getSession();
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("ログインしてください");
        session.setAttribute("errorMessages", errorMessages);
        // エラーメッセージをログイン画面に渡すクエリパラメータを追加してリダイレクト
        response.sendRedirect("/toLogin");
    }
}