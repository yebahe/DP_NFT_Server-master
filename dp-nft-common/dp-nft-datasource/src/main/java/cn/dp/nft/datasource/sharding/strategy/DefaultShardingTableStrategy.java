package cn.dp.nft.datasource.sharding.strategy;

/**
 * @author yebahe
 */
public class DefaultShardingTableStrategy implements ShardingTableStrategy {

    public DefaultShardingTableStrategy() {
    }

    @Override
    public int getTable(String externalId,int tableCount) {
        int hashCode = externalId.hashCode();
        return (int) Math.abs((long) hashCode) % tableCount;
    }
}
