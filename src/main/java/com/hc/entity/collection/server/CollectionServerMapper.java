package com.hc.entity.collection.server;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CollectionServerMapper {
	CollectionServer selectCollectionServer(@Param("csid")String csid);
	
	List<CollectionServer> selectCollectionServers(
			@Param("isUsedCs")Integer isUsedCs, @Param("type")Short type);
	
	CollectionServer selectCollectionServerByCtid(
			@Param("ctid")String ctid, @Param("csid")String csid);
	
	int insertCollectionServer(CollectionServer cs);
	
	int updateCollectionServerSelective(CollectionServer cs);
	
	int upsert(CollectionServer cs);
	
	int insertBatch(@Param("servers")List<CollectionServer> servers);

	int updateBatch(@Param("servers")List<CollectionServer> servers);

	int deleteBatch(@Param("servers")List<CollectionServer> servers);

}