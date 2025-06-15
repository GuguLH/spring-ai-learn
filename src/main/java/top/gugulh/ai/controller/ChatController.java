package top.gugulh.ai.controller;

import cn.hutool.core.collection.CollUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import top.gugulh.ai.repository.IChatHistoryRepository;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private IChatHistoryRepository chatHistoryRepository;

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
    /*
    @PostMapping(value = "/chat-stream", produces = "text/html;charset=utf-8")
    public Flux<String> chatStream(
            @RequestParam(name = "prompt", required = true) String prompt,
            @RequestParam(name = "chatId", required = true) String chatId
    ) {
        // 1 保存会话id
        chatHistoryRepository.save("chat", chatId);
        // 2 发起请求
        return chatClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId)) // 添加会话ID到Advisor上下文中
                .stream() // 流式响应
                .content();
    }
     */
    @PostMapping(value = "/chat-stream", produces = "text/html;charset=utf-8")
    public Flux<String> chatStream(
            @RequestParam(name = "prompt", required = true) String prompt,
            @RequestParam(name = "chatId", required = true) String chatId,
            @RequestParam(name = "files", required = false) List<MultipartFile> files
    ) {
        // 1 保存会话id
        chatHistoryRepository.save("chat", chatId);
        // 2 发起请求
        if (CollUtil.isEmpty(files)) {
            // 纯文本
            return textChat(prompt, chatId);
        } else {
            // 多模态
            return multiModalChat(prompt, chatId, files);
        }

    }

    private Flux<String> multiModalChat(String prompt, String chatId, List<MultipartFile> files) {
        // 1 解析多媒体
        List<Media> medias = files.stream()
                .map(file -> new Media(
                        MimeType.valueOf(Objects.requireNonNull(file.getContentType())),
                        file.getResource())
                )
                .toList();
        // 2 请求模型
        return chatClient.prompt()
                .user(p -> p.text(prompt).media(medias.toArray(Media[]::new)))
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId)) // 添加会话ID到Advisor上下文中
                .stream() // 流式响应
                .content();
    }

    private Flux<String> textChat(String prompt, String chatId) {
        return chatClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId)) // 添加会话ID到Advisor上下文中
                .stream() // 流式响应
                .content();
    }
}
