package top.gugulh.ai.repository;

import java.util.List;

public interface IChatHistoryRepository {

    /**
     * 保存会话记录
     *
     * @param chatId 会话id
     * @param bsType 业务类型
     */
    public void save(String chatId, String bsType);

    /**
     * 查询会话id列表
     *
     * @param bsType 业务类型
     * @return 该业务的会话列表
     */
    public List<String> findChatIds(String bsType);

}
