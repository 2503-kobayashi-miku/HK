package com.example.HK.controller;

import com.example.HK.service.CommentService;
import com.example.HK.service.MessageService;
import com.example.HK.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HKController {
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    CommentService commentService;
    @Autowired
    HttpSession session;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        // 画面遷移先を指定
        mav.setViewName("/top");
        return mav;
    }

    /*
     * ログイン画面表示処理
     */
    @GetMapping("/toLogin")
    public ModelAndView loginView() {
        ModelAndView mav = new ModelAndView();
        // 画面遷移先を指定
        mav.setViewName("/login");
        return mav;
    }

    // ログアウト処理用のハンドラをDI注入する
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    // ログアウトコントローラー
    @GetMapping("/logout")
    public String logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        // ログアウト処理(セッション破棄など)
        this.logoutHandler.logout(request, response, authentication);
        // ログアウト後はログインページにリダイレクトする
        return "redirect:/login";
    }
}
