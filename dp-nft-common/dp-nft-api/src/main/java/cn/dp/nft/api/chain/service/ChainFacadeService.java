package cn.dp.nft.api.chain.service;

import cn.dp.nft.api.chain.request.ChainProcessRequest;
import cn.dp.nft.api.chain.response.ChainProcessResponse;
import cn.dp.nft.api.chain.response.data.ChainCreateData;
import cn.dp.nft.api.chain.response.data.ChainOperationData;

/**
 * @author yebahe
 */
public interface ChainFacadeService {

    /**
     * 创建交易链地址
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest request);

    /**
     * 上链藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> chain(ChainProcessRequest request);

    /**
     * 铸造藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> mint(ChainProcessRequest request);

    /**
     * 交易藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> transfer(ChainProcessRequest request);

    /**
     * 销毁藏品
     *
     * @param request
     * @return
     */
    ChainProcessResponse<ChainOperationData> destroy(ChainProcessRequest request);
}
