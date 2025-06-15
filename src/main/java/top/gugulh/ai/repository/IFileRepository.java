package top.gugulh.ai.repository;

import org.springframework.core.io.Resource;

public interface IFileRepository {

    /**
     * 保存文件,记录chatId和文件的映射关系
     *
     * @param chatId   会话id
     * @param resource 文件
     * @return 上传成功, 返回true;否则false
     */
    boolean save(String chatId, Resource resource);

    /**
     * 根据chatId获取文件
     *
     * @param chatId 会话id
     * @return 找到的文件
     */
    Resource load(String chatId);
}
