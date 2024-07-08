package cn.dp.nft.chain.domain.response;

import cn.dp.nft.base.response.BaseResponse;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yebahe
 */
@Setter
@Getter
@ToString
public class ChainResponse extends BaseResponse {

    private JSONObject data;

    private JSONObject error;

    @Override
    public Boolean getSuccess() {
        return data != null;
    }

    @Override
    public String getResponseMessage() {
        if (this.error != null) {
            return error.getString("message");
        }
        return null;
    }

    @Override
    public String getResponseCode() {
        if (this.error != null) {
            return error.getString("code");
        }
        return null;
    }
}
