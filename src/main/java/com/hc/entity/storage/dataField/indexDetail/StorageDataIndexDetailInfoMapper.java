package com.hc.entity.storage.dataField.indexDetail;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.hc.vo.StorageDataIndexDetailInfoVo;

public interface StorageDataIndexDetailInfoMapper {
    int deleteById(@Param("id")String id);

    int insertBatch(@Param("infos")List<StorageDataIndexDetailInfo> infos);

    Page<StorageDataIndexDetailInfoVo> selectAll(
    		@Param("id")String id, @Param("collectName")String collectName,
    		@Param("field")String field, @Param("fieldTypes")List<Short> fieldTypes,
    		@Param("fieldsItem")String fieldsItem);
   
    int insertSelective(StorageDataIndexDetailInfo record);

    StorageDataIndexDetailInfo selectByPrimaryKey(StorageDataIndexDetailInfoKey key);

    int updateByPrimaryKeySelective(StorageDataIndexDetailInfo record);

    int updateByPrimaryKey(StorageDataIndexDetailInfo record);

}