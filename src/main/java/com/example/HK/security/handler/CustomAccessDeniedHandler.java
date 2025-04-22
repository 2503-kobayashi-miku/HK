package com.example.HK.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    HttpSession session;
    /*
     * 管理者権限フィルターのエラーメッセージ表示処理
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        session = request.getSession();
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("無効なアクセスです");
        session.setAttribute("errorMessages", errorMessages);
        // エラーメッセージをホーム画面に渡すクエリパラメータを追加してリダイレクト
        response.sendRedirect("/");
    }

}
