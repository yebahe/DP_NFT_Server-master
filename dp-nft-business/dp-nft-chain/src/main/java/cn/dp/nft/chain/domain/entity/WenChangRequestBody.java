package cn.dp.nft.chain.domain.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Setter
@Getter
public class WenChangRequestBody implements RequestBody{

    @JSONField(name = "operation_id")
    private String operationId;


}
