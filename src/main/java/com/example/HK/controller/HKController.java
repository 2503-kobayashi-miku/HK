package com.example.HK.controller;

import com.example.HK.controller.form.*;
import com.example.HK.dto.UserBranchDepartment;
import com.example.HK.dto.UserComment;
import com.example.HK.dto.UserMessage;
import com.example.HK.security.details.LoginUserDetails;
import com.example.HK.service.*;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

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
    BranchService branchService;
    @Autowired
    DepartmentService departmentService;


    /*
     * ホーム画面表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(name = "start", required = false) LocalDate start,
                            @RequestParam(name = "end", required = false) LocalDate end,
                            @RequestParam(name = "category", required = false) String category,
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
    @PostMapping("/message/add")
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
    @DeleteMapping("/message/delete/{id}")
    public ModelAndView deleteMessage(@PathVariable Integer id) {
        // テーブルから投稿を削除
        messageService.deleteMessage(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 新規コメント処理
     */
    @PostMapping("/comment/add")
    public ModelAndView addText(@ModelAttribute("formModel") @Validated CommentForm commentForm,
                                BindingResult result,
                                @AuthenticationPrincipal LoginUserDetails loginUser,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("formMessageId", commentForm.getMessageId());
            redirectAttributes.addFlashAttribute("commentErrorMessages", errorMessages);
            return new ModelAndView("redirect:/");
        }
        // ログインユーザーIDをコメントに格納
        commentForm.setUserId(loginUser.getUserId());
        // コメントをテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント削除処理
     */
    @DeleteMapping("/comment/delete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        // テーブルからコメントを削除
        commentService.deleteComment(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * ログイン画面表示処理
     */
    @GetMapping("/toLogin")
    public ModelAndView loginView(RedirectAttributes redirectAttributes) {

        if (session.getAttribute("errorMessages") != null) {
            redirectAttributes.addFlashAttribute("errorMessages", session.getAttribute("errorMessages"));
            //セッションを削除
            session.invalidate();
            return new ModelAndView("redirect:/toLogin");
        }

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

    /*
     * ユーザー復活・停止処理
     */
    @PutMapping("/update-isStopped/{id}")
    public ModelAndView updateStatus(@PathVariable Integer id,
                                     @ModelAttribute("isStopped") short isStopped) {
        userService.saveIsStopped(id, isStopped);
        return new ModelAndView("redirect:/admin");
    }

    /*
     * ユーザー登録画面表示処理
     */
    @GetMapping("/signup")
    public ModelAndView signupView(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        UserForm userForm;
        if(RequestContextUtils.getInputFlashMap(request) != null) {
            userForm = (UserForm) RequestContextUtils.getInputFlashMap(request).get("formModel");
        } else {
            // form用の空のentityを準備
            userForm = new UserForm();
        }
        // 支社情報を全件取得
        List<BranchForm> branchDate = branchService.findAllBranch();
        // 部署情報を全件取得
        List<DepartmentForm> departmentDate = departmentService.findAllDepartment();
        // 画面遷移先を指定
        mav.setViewName("/signup");
        //  支社データオブジェクトを保管
        mav.addObject("branches", branchDate);
        //  部署データオブジェクトを保管
        mav.addObject("departments", departmentDate);
        // 準備したFormを保管
        mav.addObject("formModel", userForm);
        return mav;
    }

    /*
     * ユーザ登録処理
     */
    @PostMapping("/user/add")
    public ModelAndView addUser(@ModelAttribute("formModel") @Validated UserForm userForm,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        List<String> errorMessages = new ArrayList<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        // パスワードと確認用パスワードの一致チェック
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            errorMessages.add("パスワードと確認用パスワードが一致しません");
        }
        // アカウント重複チェック
        if (userService.existsUserByAccount(userForm.getAccount())) {
            errorMessages.add("アカウントが重複しています");
        }
        if(!errorMessages.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            redirectAttributes.addFlashAttribute("formModel", userForm);
            return new ModelAndView("redirect:/signup");
        }
        // ユーザをテーブルに格納
        userService.saveUser(userForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/admin");
    }

    /*
     * ユーザ編集画面表示処理
     */
    @GetMapping({"/user/edit/", "/user/edit/{id}"})
    public ModelAndView editUser(@PathVariable(required = false) String id,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        UserForm userForm = null;
        if(RequestContextUtils.getInputFlashMap(request) != null) {
            userForm = (UserForm) RequestContextUtils.getInputFlashMap(request).get("formModel");
        } else {
            //URLチェック
            if (!StringUtils.isBlank(id) && id.matches("^[0-9]*$")) {
                int intId = Integer.parseInt(id);
                // 編集するユーザを取得
                userForm = userService.editUser(intId);
            }
        }

        if (userForm == null) {
            redirectAttributes.addFlashAttribute("errorMessages","不正なパラメータが入力されました");
            return new ModelAndView("redirect:/admin");
        }
        // 支社情報を全件取得
        List<BranchForm> branchDate = branchService.findAllBranch();
        // 部署情報を全件取得
        List<DepartmentForm> departmentDate = departmentService.findAllDepartment();
        // 画面遷移先を指定
        mav.setViewName("/edit");
        //  支社データオブジェクトを保管
        mav.addObject("branches", branchDate);
        //  部署データオブジェクトを保管
        mav.addObject("departments", departmentDate);
        // 編集するユーザをセット
        mav.addObject("formModel", userForm);
        return mav;
    }

    /*
     * ユーザ編集処理
     */
    @PutMapping("/user/update/{id}")
    public ModelAndView updateUser(@PathVariable Integer id,
                                   @ModelAttribute("formModel") @Validated UserForm userForm,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        // UrlParameterのidを更新するentityにセット
        userForm.setId(id);
        List<String> errorMessages = new ArrayList<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        // パスワードと確認用パスワードの一致チェック
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            errorMessages.add("パスワードと確認用パスワードが一致しません");
        }
        // アカウント重複チェック
        if (userService.existsUserByAccountAndIdNot(userForm.getAccount(),userForm.getId())) {
            errorMessages.add("アカウントが重複しています");
        }
        if(!errorMessages.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessages", errorMessages);
            redirectAttributes.addFlashAttribute("formModel", userForm);
            return new ModelAndView("redirect:/user/edit/" + id);
        }
        // 編集したユーザを更新
        userService.saveUser(userForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/admin");
    }
}