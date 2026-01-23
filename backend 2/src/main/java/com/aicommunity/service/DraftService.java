package com.aicommunity.service;

import com.aicommunity.vo.DraftSaveResponseVO;
import com.aicommunity.vo.DraftVO;

/**
 * 草稿服务接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
public interface DraftService {

    /**
     * 保存草稿
     *
     * @param draft 草稿信息
     * @param userId 用户ID
     * @return 保存响应
     */
    DraftSaveResponseVO saveDraft(DraftVO draft, String userId);

    /**
     * 获取草稿
     *
     * @param userId 用户ID
     * @return 草稿信息
     */
    DraftVO getDraft(String userId);

    /**
     * 删除草稿
     *
     * @param userId 用户ID
     */
    void deleteDraft(String userId);
}
