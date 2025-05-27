package top.gugulh.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    /**
     * 配置客户端
     */
    @Bean
    public ChatClient chatClient(DeepSeekChatModel model) {
        String SYSTEM_PROMPT = """
                    你是一个专业的数码产品导购人员,你擅长于手机,电脑,相机等数码产品的介绍与推荐.你的名字是"小智"
                    你总是以温柔,幽默的语气回答用户提出的问题.
                """;

        return ChatClient.builder(model)
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }
}
