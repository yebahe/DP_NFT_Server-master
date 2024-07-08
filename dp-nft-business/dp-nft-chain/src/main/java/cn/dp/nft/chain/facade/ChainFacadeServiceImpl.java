package cn.dp.nft.chain.facade;

import cn.dp.nft.chain.domain.service.ChainService;
import cn.dp.nft.chain.domain.service.ChainServiceFactory;
import cn.dp.nft.api.chain.constant.ChainType;
import cn.dp.nft.api.chain.request.ChainProcessRequest;
import cn.dp.nft.api.chain.response.ChainProcessResponse;
import cn.dp.nft.api.chain.response.data.ChainCreateData;
import cn.dp.nft.api.chain.response.data.ChainOperationData;
import cn.dp.nft.api.chain.service.ChainFacadeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static cn.dp.nft.base.constant.ProfileConstant.PROFILE_DEV;

/**
 * @author yebahe
 */
@DubboService(version = "1.0.0")
public class ChainFacadeServiceImpl implements ChainFacadeService {

    @Value("${nft.turbo.chain.type:MOCK}")
    private String chainType;

    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    private ChainServiceFactory chainServiceFactory;

    @Override
    public ChainProcessResponse<ChainCreateData> createAddr(ChainProcessRequest request) {
        return getChainService().createAddr(request);
    }

    @Override
    public ChainProcessResponse<ChainOperationData> chain(ChainProcessRequest request) {
        return getChainService().chain(request);
    }

    @Override
    public ChainProcessResponse<ChainOperationData> mint(ChainProcessRequest request) {
        return getChainService().mint(request);
    }

    @Override
    public ChainProcessResponse<ChainOperationData> transfer(ChainProcessRequest request) {
        return getChainService().transfer(request);
    }

    @Override
    public ChainProcessResponse<ChainOperationData> destroy(ChainProcessRequest request) {
        return getChainService().destroy(request);
    }

    private ChainService getChainService() {
        if (PROFILE_DEV.equals(profile)) {
            return chainServiceFactory.get(ChainType.MOCK);
        }

        ChainService chainService = chainServiceFactory.get(ChainType.valueOf(chainType));
        return chainService;
    }
}
