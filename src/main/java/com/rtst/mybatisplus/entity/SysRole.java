package com.rtst.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author White Liu
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysRole对象", description="")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID")
    @TableId(value = "roleId", type = IdType.AUTO)
    private Integer roleId;

    @ApiModelProperty(value = "角色名")
    @TableField("roleName")
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "角色状态是否启用 0 启用，1未启用")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;


}
