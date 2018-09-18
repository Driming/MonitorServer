package com.hc.typehandler;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hc.entity.collection.history.CollectionHistory;
import com.hc.util.decompression.InflaterDecompress;

@SuppressWarnings("rawtypes")
@MappedJdbcTypes(JdbcType.LONGVARCHAR)
@MappedTypes(List.class)
public class ListTextHandler extends BaseTypeHandler{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int index, Object parameter, JdbcType jdbcType)
			throws SQLException {
		if(parameter == null)
			ps.setString(index, null);
		@SuppressWarnings("unchecked")
		List<CollectionHistory.Error> errors = (List<CollectionHistory.Error>) parameter;
		for(CollectionHistory.Error error : errors){
			ps.setString(index, error.getException());
		}
	}

	@Override
	public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String err =rs.getString(columnName);
		return parseErrors(err);
	}

	@Override
	public List<CollectionHistory.Error> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String err = rs.getString(columnIndex);
		return parseErrors(err);
	}

	@Override
	public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<CollectionHistory.Error> parseErrors(String err){
		List<CollectionHistory.Error> errors = new LinkedList<CollectionHistory.Error>();
		
		ObjectMapper mapper = new ObjectMapper();
		if(err != null && !err.trim().equals("")){
			try {
				byte[] bytes = mapper.writeValueAsBytes(err);
				bytes = InflaterDecompress.compress("[{\"exception\":\"无法读取文件\",\"filename\":\"input.txt\"}]");
				String decompress = InflaterDecompress.decompress(bytes);
				errors = mapper.readValue(decompress, new TypeReference<List<CollectionHistory.Error>>() {});
			} catch (DataFormatException | IOException e) {
				e.printStackTrace();
			}
			
		}
		return errors;
	}
	
}
