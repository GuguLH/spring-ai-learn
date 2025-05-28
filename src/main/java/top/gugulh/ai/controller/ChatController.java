package top.gugulh.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private ChatClient chatClient;

    /**
     * 阻塞式调用
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompt", required = true) String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call() // 阻塞式调用
                .content();
    }

    /**
     * 使用WebFlux完成流式响应,前端类似打字机效果
     */
    @PostMapping(value = "/chat-stream", produces = "text/html;charset=utf-8")
    public Flux<String> chatStream(
            @RequestParam(name = "prompt", required = true) String prompt,
            @RequestParam(name = "chatId", required = true) String chatId
    ) {
        return chatClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId)) // 添加会话ID到Advisor上下文中
                .stream() // 流式响应
                .content();
    }
}
