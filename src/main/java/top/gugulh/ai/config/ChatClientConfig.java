package top.gugulh.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static top.gugulh.ai.constants.SystemConstants.CHAT_SYSTEM_PROMPT;
import static top.gugulh.ai.constants.SystemConstants.GAME_SYSTEM_PROMPT;

@Configuration
public class ChatClientConfig {

    /**
     * 配置ChatMemory
     */
    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder().build();
    }

    /**
     * 配置客户端
     */
    @Bean
    public ChatClient chatClient(DeepSeekChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultSystem(CHAT_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 环绕增强,添加日志打印
                        MessageChatMemoryAdvisor.builder(chatMemory).build() // 会话记忆功能
                )
                .build();
    }

    @Bean
    public ChatClient gameChatClient(OpenAiChatModel model, ChatMemory chatMemory) {
        return ChatClient.builder(model)
                .defaultSystem(GAME_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 环绕增强,添加日志打印
                        MessageChatMemoryAdvisor.builder(chatMemory).build() // 会话记忆功能
                )
                .build();
    }
}
