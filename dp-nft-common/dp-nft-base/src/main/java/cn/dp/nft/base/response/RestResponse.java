package cn.dp.nft.base.response;

import com.alibaba.fastjson2.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * @author yebahe
 */
@Setter
@Getter
public class RestResponse extends BaseResponse {

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
