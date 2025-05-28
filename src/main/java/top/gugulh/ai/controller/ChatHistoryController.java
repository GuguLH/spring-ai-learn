package top.gugulh.ai.controller;

import cn.hutool.core.collection.CollUtil;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.gugulh.ai.entity.vo.MessageVO;
import top.gugulh.ai.repository.IChatHistoryRepository;

import java.util.List;

@RestController
@RequestMapping("/ai/history")
public class ChatHistoryController {

    @Autowired
    private ChatMemory chatMemory;

    @Autowired
    private IChatHistoryRepository chatHistoryRepository;

    @GetMapping("/{type}")
    public List<String> getChatIds(@PathVariable(name = "type") String type) {
        return chatHistoryRepository.findChatIds(type);
    }

    @GetMapping("/{type}/{chatId}")
    public List<MessageVO> getChatHistory(
            @PathVariable(name = "type") String type,
            @PathVariable(name = "chatId") String chatId
    ) {
        List<Message> messages = chatMemory.get(chatId);
        if (CollUtil.isEmpty(messages)) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }

}
