package cn.dp.nft.chain.infrastructure.mapper;

import cn.dp.nft.chain.domain.entity.ChainOperateInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 链操作 Mapper 接口
 * </p>
 *
 * @author wswyb001
 * @since 2024-01-19
 */
@Mapper
public interface ChainOperateInfoMapper extends BaseMapper<ChainOperateInfo> {
    /**
     * 扫描所有
     *
     * @return
     */
    List<ChainOperateInfo> scanAll();

}
