package com.aicommunity.service.impl;

import com.aicommunity.common.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.dto.ToolOwnerResponse;
import com.aicommunity.entity.Tool;
import com.aicommunity.mapper.ToolMapper;
import com.aicommunity.mapper.UserRoleMapper;
import com.aicommunity.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 工具服务实现类
 *
 * @author AI Community Team
 */
@Service
public class ToolServiceImpl implements ToolService {

    @Autowired
    private ToolMapper toolMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<Tool> getTools(Boolean featured) {
        return toolMapper.selectAll(featured);
    }

    @Override
    public Tool getToolDetail(Long id) {
        Tool tool = toolMapper.selectById(id);
        if (tool == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND, "工具不存在");
        }
        return tool;
    }

    @Override
    public ToolOwnerResponse checkToolOwner(Long id) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED);
        }

        boolean isOwner = userRoleMapper.existsByUserAndToolAndRole(currentUserId, id, "owner");
        
        ToolOwnerResponse response = new ToolOwnerResponse();
        response.setIsOwner(isOwner);
        response.setToolId(id);
        
        if (isOwner) {
            Tool tool = toolMapper.selectById(id);
            if (tool != null) {
                response.setToolName(tool.getName());
            }
        }
        
        return response;
    }

    private Long getCurrentUserId() {
        return com.aicommunity.util.UserContext.getUserId();
    }
}
