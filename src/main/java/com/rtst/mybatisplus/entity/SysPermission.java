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
@ApiModel(value="SysPermission对象", description="")
public class SysPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限ID")
    @TableId(value = "permissionId", type = IdType.AUTO)
    private Integer permissionId;

    @ApiModelProperty(value = "页面父ID")
    @TableField("parentId")
    private Integer parentId;

    @ApiModelProperty(value = "权限")
    private String permission;

    @ApiModelProperty(value = "权限名")
    @TableField("permissionName")
    private String permissionName;

    @ApiModelProperty(value = "路径url")
    private String url;

    @ApiModelProperty(value = "图标路径")
    @TableField("iconPath")
    private String iconPath;

    @ApiModelProperty(value = "资源类型")
    @TableField("resourceType")
    private String resourceType;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private Integer deleted;

    @ApiModelProperty(value = "乐观锁")
    @Version
    private Integer version;


}
