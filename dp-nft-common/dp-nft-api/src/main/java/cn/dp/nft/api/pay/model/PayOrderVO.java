package cn.dp.nft.api.pay.model;

import cn.dp.nft.api.pay.constant.PayOrderState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author yebahe
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class PayOrderVO implements Serializable {

    private String payOrderId;

    private String payUrl;

    private PayOrderState orderState;

    private static final long serialVersionUID = 1L;
}
