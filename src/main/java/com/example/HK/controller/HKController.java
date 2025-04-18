package com.example.HK.controller;

import com.example.HK.controller.form.CommentForm;
import com.example.HK.controller.form.MessageForm;
import com.example.HK.dto.UserBranchDepartment;
import com.example.HK.dto.UserComment;
import com.example.HK.dto.UserMessage;
import com.example.HK.repository.entity.LoginUserDetails;
import com.example.HK.service.CommentService;
import com.example.HK.service.MessageService;
import com.example.HK.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
     * ホーム画面表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(name = "start", required = false) LocalDate start,
                            @RequestParam (name = "end", required = false) LocalDate end,
                            @RequestParam (name = "category", required = false) String category,
                            @AuthenticationPrincipal LoginUserDetails loginUser) {
        ModelAndView mav = new ModelAndView();
        // commentForm用の空のentityを準備
        CommentForm commentForm = new CommentForm();
        // 投稿を絞り込み取得(投稿者情報)
        List<UserMessage> messageData = messageService.findMessageWithUserByOrder(start, end, category);
        // コメントを全件取得(コメント者情報)
        List<UserComment> commentData = commentService.findAllCommentWithUser();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // ログインユーザーデータオブジェクトを保管
        mav.addObject("loginUserId", loginUser.getUserId());
        // 投稿データオブジェクトを保管
        mav.addObject("messages", messageData);
        // コメントデータオブジェクトを保管
        mav.addObject("comments", commentData);
        // 準備した空のcommentFormを保管
        mav.addObject("formModel", commentForm);
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newMessage() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        MessageForm messageForm = new MessageForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", messageForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addMessage(@ModelAttribute("formModel") @Validated MessageForm messageForm,
                                   BindingResult result,
                                   @AuthenticationPrincipal LoginUserDetails loginUser,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/new");
        }
        // ログインユーザーIDを投稿に格納
        messageForm.setUserId(loginUser.getUserId());
        // 投稿をテーブルに格納
        messageService.saveMessage(messageForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/message-delete/{id}")
    public ModelAndView deleteMessage(@PathVariable Integer id) {
        // テーブルから投稿を削除
        messageService.deleteMessage(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
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

    /*
     * ユーザー管理画面表示処理
     */
    @GetMapping("/admin")
    public ModelAndView adminView() {
        ModelAndView mav = new ModelAndView();
        // 画面遷移先を指定
        mav.setViewName("/admin");
        List<UserBranchDepartment> userDate = userService.findUserWithBranchWithDepartment();
        mav.addObject("users", userDate);
        return mav;
    }
}
