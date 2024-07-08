package cn.dp.nft.collection.domain.service.impl.db;

import cn.dp.nft.collection.domain.service.impl.BaseCollectionService;
import cn.dp.nft.base.response.PageResponse;
import cn.dp.nft.collection.domain.entity.Collection;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 藏品服务-数据库
 *
 * @author yebahe
 */
@Service
@ConditionalOnProperty(name = "spring.elasticsearch.enable", havingValue = "false", matchIfMissing = true)
public class CollectionDbService extends BaseCollectionService {

    @Override
    public PageResponse<Collection> pageQueryByState(String keyWord, String state, int currentPage, int pageSize) {
        Page<Collection> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Collection> wrapper = new QueryWrapper<>();
        wrapper.eq("state", state);

        if (keyWord != null) {
            wrapper.like("name", keyWord);
        }
        wrapper.orderBy(true, true, "gmt_create");

        Page<Collection> collectionPage = this.page(page, wrapper);

        return PageResponse.of(collectionPage.getRecords(), (int) collectionPage.getTotal(), pageSize, currentPage);
    }
}
