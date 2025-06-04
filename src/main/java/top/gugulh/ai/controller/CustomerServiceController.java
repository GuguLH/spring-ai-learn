package top.gugulh.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import top.gugulh.ai.repository.IChatHistoryRepository;

@RestController
@RequestMapping("/ai")
public class CustomerServiceController {

    @Autowired
    private ChatClient serviceChatClient;

    @Autowired
    private IChatHistoryRepository chatHistoryRepository;

    @GetMapping(value = "/service", produces = "text/html;charset=utf-8")
    public Flux<String> service(
            @RequestParam(name = "prompt", required = true) String prompt,
            @RequestParam(name = "chatId", required = true) String chatId
    ) {
        chatHistoryRepository.save("service", chatId);
        return serviceChatClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId)) // 添加会话ID到Advisor上下文中
                .stream() // 流式响应
                .content();
    }
}
