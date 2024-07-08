package cn.dp.nft.api.chain.response.data;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wwswyb001
 * @date 2024/01/18
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChainCreateData implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 操作编号
     */
    private String operationId;

    /**
     * 链账户地址
     */
    private String account;

    /**
     * 链账户名称
     */
    private String name;

    /**
     * 平台名称
     */
    private String platform;

}
