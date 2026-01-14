package com.aicommunity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 贡献者列表VO
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Data
@ApiModel(description = "贡献者列表")
public class ContributorListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "贡献者列表")
    private List<ContributorVO> list;
}
