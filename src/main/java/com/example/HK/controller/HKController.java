package com.example.HK.controller;

import com.example.HK.controller.form.CommentForm;
import com.example.HK.controller.form.MessageForm;
import com.example.HK.dto.UserMessage;
import com.example.HK.service.CommentService;
import com.example.HK.service.MessageService;
import com.example.HK.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HKController {
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;
    @Autowired
    CommentService commentService;

    /*
     * ホーム画面表示処理
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        // commentForm用の空のentityを準備
        CommentForm commentForm = new CommentForm();
        // 投稿を全件取得(投稿者情報)
        List<UserMessage> messageData = messageService.findAllMessageWithUser();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("messages", messageData);
        // 準備した空のcommentFormを保管
        mav.addObject("formModel", commentForm);
        return mav;
    }
}
