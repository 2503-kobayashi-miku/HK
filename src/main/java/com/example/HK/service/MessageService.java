package com.example.HK.service;

import com.example.HK.controller.form.MessageForm;
import com.example.HK.dto.UserMessage;
import com.example.HK.repository.MessageRepository;
import com.example.HK.repository.entity.Message;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    /*
     * レコード絞り込み取得処理
     */
    public List<UserMessage> findMessageWithUserByOrder(LocalDate start, LocalDate end, String category) {
        final int LIMIT_NUM = 1000;
        LocalDateTime startDateTime;
        LocalDateTime endDateTime;

        if (start != null) {
            startDateTime = start.atStartOfDay();
        } else {
            startDateTime = LocalDate.of(2020,1,1).atStartOfDay();
        }

        if (end != null) {
            endDateTime = end.plusDays(1).atStartOfDay().minusSeconds(1);
        } else {
            endDateTime = LocalDate.of(2101,1,1).atStartOfDay().minusSeconds(1);;
        }

        if (!StringUtils.isBlank(category)) {
            List<Object[]> results = messageRepository.findByCreatedDateBetweenAndCategoryOrderByCreatedDateAsc(startDateTime, endDateTime, category, LIMIT_NUM);
            List<UserMessage> messages = setUserMessage(results);
            return messages;
        } else {
            List<Object[]> results = messageRepository.findByCreatedDateBetweenOrderByCreatedDateAsc(startDateTime, endDateTime, LIMIT_NUM);
            List<UserMessage> messages = setUserMessage(results);
            return messages;
        }
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
     * レコード追加
     */
    public void saveMessage(MessageForm reqMessage) {
        Message saveMessage = setMessageEntity(reqMessage);
        messageRepository.save(saveMessage);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Message setMessageEntity(MessageForm reqMessage) {
        Message message = new Message();
        message.setId(reqMessage.getId());
        message.setTitle(reqMessage.getTitle());
        message.setText(reqMessage.getText());
        message.setCategory(reqMessage.getCategory());
        message.setUserId(reqMessage.getUserId());
        return message;
    }

    /*
     * レコード削除
     */
    public void deleteMessage(Integer id) {
        messageRepository.deleteById(id);
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
