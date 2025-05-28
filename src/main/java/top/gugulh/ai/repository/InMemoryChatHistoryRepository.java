package top.gugulh.ai.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryChatHistoryRepository implements IChatHistoryRepository {

    private final Map<String, List<String>> chatHistory = new ConcurrentHashMap<>();

    @Override
    public void save(String chatId, String bsType) {
        chatHistory.computeIfAbsent(bsType, k -> new ArrayList<>()).add(chatId);
    }

    @Override
    public List<String> findChatIds(String bsType) {
        return chatHistory.getOrDefault(bsType, List.of());
    }
}
