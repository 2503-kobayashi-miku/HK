package com.example.HK.service;

import com.example.HK.controller.form.MessageForm;
import com.example.HK.dto.UserMessage;
import com.example.HK.repository.MessageRepository;
import com.example.HK.repository.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    /*
     * レコード全件取得処理
     */
    public List<UserMessage> findAllMessageWithUser() {
        List<Object[]> results = messageRepository.findAllWithUser();
        List<UserMessage> messages = setUserMessage(results);
        return messages;
    }

    /*
     * DBから取得したデータをDtoに設定
     */
    private List<UserMessage> setUserMessage(List<Object[]> results) {
        List<UserMessage> messages = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            UserMessage message = new UserMessage();
            Object[] result = results.get(i);
            message.setId((Integer) result[0]);
            message.setTitle((String) result[1]);
            message.setText((String) result[2]);
            message.setCategory((String) result[3]);
            message.setUserId((Integer) result[4]);
            message.setAccount((String) result[5]);
            message.setName((String) result[6]);
            message.setBranchId((Integer) result[7]);
            message.setDepartmentId((Integer) result[8]);
            message.setCreatedDate((LocalDateTime) result[9]);
            message.setUpdatedDate((LocalDateTime) result[10]);
            messages.add(message);
        }
        return messages;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<MessageForm> setMessageForm(List<Message> results) {
        List<MessageForm> messages = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            MessageForm message = new MessageForm();
            Message result = results.get(i);
            message.setId(result.getId());
            message.setTitle(result.getTitle());
            message.setText(result.getText());
            message.setCategory(result.getCategory());
            message.setUserId(result.getUserId());
            message.setCreatedDate(result.getCreatedDate());
            message.setUpdatedDate(result.getUpdatedDate());
            messages.add(message);
        }
        return messages;
    }
}
