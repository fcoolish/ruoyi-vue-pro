package cn.iocoder.yudao.adminserver.modules.bpm.controller.model.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ApiModel("流程模型分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ModelPageReqVO extends PageParam {

    @ApiModelProperty(value = "标识", example = "process1641042089407", notes = "精准匹配")
    private String key;

    @ApiModelProperty(value = "名字", example = "芋道", notes = "模糊匹配")
    private String name;

    @ApiModelProperty(value = "流程分类", notes = "参见 bpm_model_category 数据字典", example = "1")
    private String category;

}
