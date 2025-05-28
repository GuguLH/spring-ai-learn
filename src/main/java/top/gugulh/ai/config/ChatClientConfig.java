package top.gugulh.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        String SYSTEM_PROMPT = """
                    你是一个专业的数码产品导购人员,你擅长于手机,电脑,相机等数码产品的介绍与推荐.你的名字是"小智"
                    你总是以温柔,幽默的语气回答用户提出的问题.
                """;

        return ChatClient.builder(model)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), // 环绕增强,添加日志打印
                        MessageChatMemoryAdvisor.builder(chatMemory).build() // 会话记忆功能
                )
                .build();
    }
}
