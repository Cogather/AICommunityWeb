package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.entity.Draft;
import com.aicommunity.mapper.DraftMapper;
import com.aicommunity.service.DraftService;
import com.aicommunity.vo.DraftSaveResponseVO;
import com.aicommunity.vo.DraftVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 草稿服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class DraftServiceImpl implements DraftService {

    @Autowired
    private DraftMapper draftMapper;

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DraftSaveResponseVO saveDraft(DraftVO draftVO, String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED.getCode(), "请先登录");
        }

        // 查询是否已有草稿
        Draft existingDraft = draftMapper.selectByUserId(userId);

        Date now = new Date();
        String draftId = "draft_" + System.currentTimeMillis();

        if (existingDraft != null) {
            // 更新草稿
            Draft draft = new Draft();
            draft.setDraftId(existingDraft.getDraftId());
            draft.setUserId(userId);
            draft.setZone(draftVO.getZone());
            draft.setToolId(draftVO.getToolId());
            draft.setTitle(draftVO.getTitle());
            draft.setSummary(draftVO.getSummary());
            draft.setContent(draftVO.getContent());
            draft.setCover(draftVO.getCover());
            draft.setTags(convertTagsToString(draftVO.getTags()));
            draft.setSavedAt(parseDate(draftVO.getSavedAt()));
            draft.setUpdateTime(now);

            int result = draftMapper.updateDraft(draft);
            if (result <= 0) {
                throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "保存草稿失败");
            }
            draftId = existingDraft.getDraftId();
        } else {
            // 创建新草稿
            Draft draft = new Draft();
            draft.setDraftId(draftId);
            draft.setUserId(userId);
            draft.setZone(draftVO.getZone());
            draft.setToolId(draftVO.getToolId());
            draft.setTitle(draftVO.getTitle());
            draft.setSummary(draftVO.getSummary());
            draft.setContent(draftVO.getContent());
            draft.setCover(draftVO.getCover());
            draft.setTags(convertTagsToString(draftVO.getTags()));
            draft.setSavedAt(parseDate(draftVO.getSavedAt()));
            draft.setCreateTime(now);
            draft.setUpdateTime(now);

            int result = draftMapper.insertDraft(draft);
            if (result <= 0) {
                throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "保存草稿失败");
            }
        }

        DraftSaveResponseVO response = new DraftSaveResponseVO();
        response.setDraftId(draftId);
        response.setSavedAt(formatDate(now));
        return response;
    }

    @Override
    public DraftVO getDraft(String userId) {
        Draft draft = draftMapper.selectByUserId(userId);
        if (draft == null) {
            return null;
        }

        DraftVO vo = new DraftVO();
        vo.setDraftId(draft.getDraftId());
        vo.setZone(draft.getZone());
        vo.setToolId(draft.getToolId());
        vo.setTitle(draft.getTitle());
        vo.setSummary(draft.getSummary());
        vo.setContent(draft.getContent());
        vo.setCover(draft.getCover());
        vo.setTags(convertStringToTags(draft.getTags()));
        vo.setSavedAt(formatDate(draft.getSavedAt()));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDraft(String userId) {
        draftMapper.deleteDraft(userId);
    }

    /**
     * 转换标签列表为JSON字符串
     */
    private String convertTagsToString(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(tags);
        } catch (Exception e) {
            log.error("转换标签列表失败", e);
            return null;
        }
    }

    /**
     * 转换JSON字符串为标签列表
     */
    private List<String> convertStringToTags(String tagsStr) {
        if (!StringUtils.hasText(tagsStr)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(tagsStr, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.error("解析标签列表失败", e);
            return null;
        }
    }

    /**
     * 格式化日期
     */
    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return ISO_DATE_FORMAT.format(date);
    }

    /**
     * 解析日期
     */
    private Date parseDate(String dateStr) {
        if (!StringUtils.hasText(dateStr)) {
            return new Date();
        }
        try {
            return ISO_DATE_FORMAT.parse(dateStr);
        } catch (Exception e) {
            log.error("解析日期失败", e);
            return new Date();
        }
    }
}
