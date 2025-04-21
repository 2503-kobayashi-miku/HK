package com.example.HK.service;

import com.example.HK.controller.form.CommentForm;
import com.example.HK.dto.UserComment;
import com.example.HK.repository.CommentRepository;
import com.example.HK.repository.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    /*
     * レコード全件取得処理
     */
    public List<UserComment> findAllCommentWithUser() {
        final int LIMIT_NUM = 1000;
        List<Object[]> results = commentRepository.findAllWithUserByCreatedDateAsc(LIMIT_NUM);
        List<UserComment> comments = setUserComment(results);
        return comments;
    }

    /*
     * DBから取得したデータをDtoに設定
     */
    private List<UserComment> setUserComment(List<Object[]> results) {
        List<UserComment> comments = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            UserComment comment = new UserComment();
            Object[] result = results.get(i);
            comment.setId((Integer) result[0]);
            comment.setText((String) result[1]);
            comment.setUserId((Integer) result[2]);
            comment.setMessageId((Integer) result[3]);
            comment.setAccount((String) result[4]);
            comment.setName((String) result[5]);
            comment.setBranchId((Integer) result[6]);
            comment.setDepartmentId((Integer) result[7]);
            comment.setCreatedDate((LocalDateTime) result[8]);
            comment.setUpdatedDate((LocalDateTime) result[9]);
            comments.add(comment);
        }
        return comments;
    }

    /*
     * レコード追加
     */
    public void saveComment(CommentForm reqComment) {
        Comment saveComment = setCommentEntity(reqComment);
        commentRepository.save(saveComment);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Comment setCommentEntity(CommentForm reqComment) {
        Comment comment = new Comment();
        comment.setId(reqComment.getId());
        comment.setText(reqComment.getText());
        comment.setUserId(reqComment.getUserId());
        comment.setMessageId(reqComment.getMessageId());
        return comment;
    }

    /*
     * レコード削除
     */
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<CommentForm> setCommentForm(List<Comment> results) {
        List<CommentForm> comments = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            CommentForm comment = new CommentForm();
            Comment result = results.get(i);
            comment.setId(result.getId());
            comment.setText(result.getText());
            comment.setUserId(result.getUserId());
            comment.setMessageId(result.getMessageId());
            comment.setCreatedDate(result.getCreatedDate());
            comment.setUpdatedDate(result.getUpdatedDate());
            comments.add(comment);
        }
        return comments;
    }
}
