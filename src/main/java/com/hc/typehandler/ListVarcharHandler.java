package com.hc.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@SuppressWarnings("rawtypes")
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class ListVarcharHandler extends BaseTypeHandler{

	@Override
	public void setNonNullParameter(PreparedStatement ps, int index, Object parameter, JdbcType jdbcType)
			throws SQLException {
	}

	@Override
	public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String element =rs.getString(columnName);
		return parseErrors(element);
	}

	@Override
	public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		String element = rs.getString(columnIndex);
		return parseErrors(element);
	}

	@Override
	public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<String> parseErrors(String element){
		List<String> elements = new LinkedList<String>();
		
		if(element == null)
			return elements;
		element = element.substring(1, element.length()-1);
		String[] eles = element.split(",");
		for(int i=0; i<eles.length; i++)
			eles[i] = eles[i].trim();
		return Arrays.asList(eles);
	}
	
}
