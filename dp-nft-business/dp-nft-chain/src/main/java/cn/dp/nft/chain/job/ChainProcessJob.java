package cn.dp.nft.chain.job;

import cn.dp.nft.chain.domain.constant.ChainOperateStateEnum;
import cn.dp.nft.chain.domain.constant.ChainStateEnum;
import cn.dp.nft.chain.domain.service.ChainOperateInfoService;
import cn.dp.nft.chain.domain.service.ChainService;
import cn.dp.nft.chain.domain.service.ChainServiceFactory;
import cn.dp.nft.chain.infrastructure.exception.ChainErrorCode;
import cn.dp.nft.chain.infrastructure.exception.ChainException;
import cn.dp.nft.api.chain.constant.ChainType;
import cn.dp.nft.api.chain.request.ChainQueryRequest;
import cn.dp.nft.api.chain.response.ChainProcessResponse;
import cn.dp.nft.api.chain.response.data.ChainResultData;
import cn.dp.nft.base.exception.BizException;
import cn.dp.nft.base.exception.RepoErrorCode;
import cn.dp.nft.base.exception.SystemException;
import cn.dp.nft.chain.domain.entity.ChainOperateInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 链上处理任务
 *
 * @author yebahe
 */
@Component
public class ChainProcessJob {

    @Autowired
    private ChainOperateInfoService chainOperateInfoService;

    @Autowired
    private ChainServiceFactory chainServiceFactory;

    private static final int PAGE_SIZE = 100;

    private static final Logger LOG = LoggerFactory.getLogger(ChainProcessJob.class);

    @XxlJob("unFinishOperateExecute")
    public ReturnT<String> execute() {

        int currentPage = 1;
        //fixme 这里可能存在部分任务失败导致任务一直死循环的问题，需要增加游标修复，其他的类似 job 也有。 @Hollis
        Page<ChainOperateInfo> page = chainOperateInfoService.pageQueryOperateInfoByState(
            ChainOperateStateEnum.PROCESSING.name(), currentPage, PAGE_SIZE);

        page.getRecords().forEach(this::executeSingle);

        while (page.hasNext()) {
            currentPage++;
            page = chainOperateInfoService.pageQueryOperateInfoByState(ChainOperateStateEnum.PROCESSING.name(),
                currentPage, PAGE_SIZE);
            page.getRecords().forEach(this::executeSingle);
        }

        return ReturnT.SUCCESS;
    }

    private void executeSingle(ChainOperateInfo chainOperateInfo) {

        LOG.info("start to execute unfinish operate , id is {}", chainOperateInfo.getId());
        try {
            ChainService chainService = chainServiceFactory.get(ChainType.valueOf(chainOperateInfo.getChainType()));
            ChainQueryRequest query = new ChainQueryRequest();
            query.setOperationId(chainOperateInfo.getOutBizId());
            ChainProcessResponse<ChainResultData> chainProcessResponse = chainService.queryChainResult(query);
            if (!chainProcessResponse.getSuccess()) {
                throw new ChainException(ChainErrorCode.CHAIN_QUERY_FAIL);
            }
            ChainResultData chainResultData = chainProcessResponse.getData();
            //异常情况判断
            if (null == chainResultData) {
                throw new ChainException(ChainErrorCode.CHAIN_QUERY_FAIL);
            }
            if (!StringUtils.equals(chainResultData.getState(), ChainStateEnum.SUCCEED.name())) {
                throw new BizException(ChainErrorCode.CHAIN_PROCESS_STATE_ERROR);
            }
            //成功情况处理
            if (StringUtils.equals(chainResultData.getState(), ChainStateEnum.SUCCEED.name())) {
                //发送消息
                chainService.sendMsg(chainOperateInfo, chainResultData);
                //更新操作表状态
                //需要做核对，如果操作表状态成功，相应业务表状态处理中，需要核对出来
                boolean updateResult = chainOperateInfoService.updateResult(chainOperateInfo.getId(),
                        ChainOperateStateEnum.SUCCEED.name(), null);
                if (!updateResult) {
                    throw new SystemException(RepoErrorCode.UPDATE_FAILED);
                }
            }
        } catch (Exception e) {
            LOG.error("start to execute unfinish operate error, id is {}, error is {}", chainOperateInfo.getId(), e);
        }
    }


}
