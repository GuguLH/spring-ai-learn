package top.gugulh.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @GetMapping(value = "/chat-stream", produces = "text/html;charset=utf-8")
    public Flux<String> chatStream(@RequestParam(name = "prompt", required = true) String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .stream() // 流式响应
                .content();
    }
}
