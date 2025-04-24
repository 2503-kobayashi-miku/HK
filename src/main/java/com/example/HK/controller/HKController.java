package com.example.HK.controller;

import com.example.HK.controller.form.*;
import com.example.HK.dto.*;
import com.example.HK.security.details.LoginUserDetails;
import com.example.HK.service.*;
import com.example.HK.validation.ValidationGroups.Edit;
import com.example.HK.validation.ValidationGroups.Signup;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    HttpSession session;


    /*
     * ホーム画面表示処理
     */
    @GetMapping("/")
    public ModelAndView top(@RequestParam(name = "start", required = false) LocalDate start,
                            @RequestParam(name = "end", required = false) LocalDate end,
                            @RequestParam(name = "category", required = false) String category,
                            @AuthenticationPrincipal LoginUserDetails loginUser,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {
        if (session.getAttribute("errorMessages") != null) {
            redirectAttributes.addFlashAttribute("errorMessages", session.getAttribute("errorMessages"));
            //セッションを削除
            session.removeAttribute("errorMessages");
            // form用の空のentityを準備
            CommentForm commentForm = new CommentForm();
            // コメントデータオブジェクトを保管
            redirectAttributes.addFlashAttribute("formModel", commentForm);
            return new ModelAndView("redirect:/");
        }
        ModelAndView mav = new ModelAndView();
        CommentForm commentForm;
        if(RequestContextUtils.getInputFlashMap(request) != null) {
            commentForm = (CommentForm) RequestContextUtils.getInputFlashMap(request).get("formModel");
        } else {
            // form用の空のentityを準備
            commentForm = new CommentForm();
        }
        // 投稿を絞り込み取得(投稿者情報)
        List<UserMessage> messageData = messageService.findMessageWithUserByOrder(start, end, category);
        // コメントを全件取得(コメント者情報)
        List<UserComment> commentData = commentService.findAllCommentWithUser();
        // 画面遷移先を指定
        mav.setViewName("top");
        // ログインユーザーデータオブジェクトを保管
        mav.addObject("loginUserId", loginUser.getUserId());
        // ログインユーザーの部署IDを保管
        mav.addObject("loginDepartmentId", loginUser.getDepartmentId());
        // ログインユーザーの支社IDを保管
        mav.addObject("loginBranchId", loginUser.getBranchId());
        // 投稿データオブジェクトを保管
        mav.addObject("messages", messageData);
        // コメントデータオブジェクトを保管
        mav.addObject("comments", commentData);
        // 準備した空のcommentFormを保管
        mav.addObject("formModel", commentForm);
        mav.addObject("start", start);
        mav.addObject("end", end);
        mav.addObject("category", category);
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newMessage(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        MessageForm messageForm;
        if(RequestContextUtils.getInputFlashMap(request) != null) {
            messageForm = (MessageForm) RequestContextUtils.getInputFlashMap(request).get("formModel");
        } else {
            // form用の空のentityを準備
            messageForm = new MessageForm();
        }
        // 画面遷移先を指定
        mav.setViewName("new");
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
            redirectAttributes.addFlashAttribute("formModel", messageForm);
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
            redirectAttributes.addFlashAttribute("formModel", commentForm);
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
            redirectAttributes.addFlashAttribute("account", session.getAttribute("account"));
            //セッションを削除
            session.invalidate();
            return new ModelAndView("redirect:/toLogin");
        }

        ModelAndView mav = new ModelAndView();
        // 画面遷移先を指定
        mav.setViewName("login");
        return mav;
    }

    /*
     * ユーザー管理画面表示処理
     */
    @GetMapping("/admin")
    public ModelAndView adminView(@AuthenticationPrincipal LoginUserDetails loginUser) {
        ModelAndView mav = new ModelAndView();
        // 画面遷移先を指定
        mav.setViewName("admin");
        List<UserBranchDepartment> userDate = userService.findUserWithBranchWithDepartment();
        mav.addObject("users", userDate);
        // ログインユーザーデータオブジェクトを保管
        mav.addObject("loginUserId", loginUser.getUserId());
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
        mav.setViewName("signup");
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
    public ModelAndView addUser(@ModelAttribute("formModel") @Validated(Signup.class) UserForm userForm,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        List<String> errorMessages = new ArrayList<>();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
        }
        if(!userForm.getAccount().isBlank()) {
            // アカウントの半角英数字かつ文字数チェック
            if(!userForm.getAccount().matches("^[a-zA-Z0-9]{6,20}$")) {
                errorMessages.add("アカウントは半角英数字かつ6文字以上20文字以下で入力してください");
            }
        }
        if(!userForm.getPassword().isBlank()) {
            // パスワードの半角文字かつ文字数チェック
            if(!userForm.getPassword().matches("^[!-~]{6,20}$")) {
                errorMessages.add("パスワードは半角文字かつ6文字以上20文字以下で入力してください");
            }
        }
        // パスワードと確認用パスワードの一致チェック
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            errorMessages.add("パスワードと確認用パスワードが一致しません");
        }
        // 支社IDと部署IDの妥当性チェック
        if (userForm.getBranchId() != null && userForm.getDepartmentId() != null) {
            if (userForm.getBranchId() == 1) {
                if (userForm.getDepartmentId() != 1 && userForm.getDepartmentId() != 2) {
                    errorMessages.add("支社と部署の組み合わせが不正です");
                }
            } else {
                if (userForm.getDepartmentId() != 3 && userForm.getDepartmentId() != 4) {
                    errorMessages.add("支社と部署の組み合わせが不正です");
                }
            }
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
                                 @AuthenticationPrincipal LoginUserDetails loginUser,
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
        mav.setViewName("edit");
        // ログインユーザーデータオブジェクトを保管
        mav.addObject("loginUserId", loginUser.getUserId());
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
                                   @ModelAttribute("formModel") @Validated(Edit.class) UserForm userForm,
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
        if(!userForm.getAccount().isBlank()) {
            // アカウントの半角英数字かつ文字数チェック
            if(!userForm.getAccount().matches("^[a-zA-Z0-9]{6,20}$")) {
                errorMessages.add("アカウントは半角英数字かつ6文字以上20文字以下で入力してください");
            }
        }
        if(!userForm.getPassword().isBlank()) {
            // パスワードの半角文字かつ文字数チェック
            if(!userForm.getPassword().matches("^[!-~]{6,20}$")) {
            errorMessages.add("パスワードは半角文字かつ6文字以上20文字以下で入力してください");
            }
        }
        // パスワードと確認用パスワードの一致チェック
        if (!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            errorMessages.add("パスワードと確認用パスワードが一致しません");
        }
        // 支社IDと部署IDの妥当性チェック
        if (userForm.getBranchId() != null && userForm.getDepartmentId() != null) {
            if (userForm.getBranchId() == 1) {
                if (userForm.getDepartmentId() != 1 && userForm.getDepartmentId() != 2) {
                    errorMessages.add("支社と部署の組み合わせが不正です");
                }
            } else {
                if (userForm.getDepartmentId() != 3 && userForm.getDepartmentId() != 4) {
                    errorMessages.add("支社と部署の組み合わせが不正です");
                }
            }
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

    /*
     * サマリー画面表示処理
     */
    @GetMapping("/summary")
    public ModelAndView summary() {
        ModelAndView mav = new ModelAndView();
        // ユーザー毎の投稿数取得(投稿者情報)
        List<UserMessageCount> userMessageCount = userService.findUserMessageCount();
        // ユーザー毎の投稿数取得(コメント者情報)
        List<UserCommentCount> userCommentCount = userService.findUserCommentCount();
        // ユーザー毎の投稿数取得(投稿者情報)
        List<BranchMessageCount> branchMessageCount = branchService.findBranchMessageCount();
        // ユーザー毎の投稿数取得(コメント者情報)
        List<BranchCommentCount> branchCommentCount = branchService.findBranchCommentCount();
        // 画面遷移先を指定
        mav.setViewName("summary");
        // ユーザー毎の投稿数データオブジェクトを保管
        mav.addObject("userMessageCount", userMessageCount);
        // ユーザー毎のコメント数データオブジェクトを保管
        mav.addObject("userCommentCount", userCommentCount);
        // ユーザー毎の投稿数データオブジェクトを保管
        mav.addObject("branchMessageCount", branchMessageCount);
        // ユーザー毎のコメント数データオブジェクトを保管
        mav.addObject("branchCommentCount", branchCommentCount);
        return mav;
    }
}