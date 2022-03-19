package com.hepengju.mockdata.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 简易数据库工具
 * 
 * <li>功能: 取得数据库连接/释放数据库连接等
 * <li>场景: 开发阶段测试数据库连接,为BOUtil提供的方法,根据表名获得字段名/字段类型/字段注释等
 * 
 * <br>备注: 关于conn,pstmt,rst的释放: 工具内部生成的由工具释放，外面作为参数传入的内部不处理
 * 
 *	@author he_pe 2018-01-11
 */
@Slf4j
public class DBUtil {

	public static final BigDecimal JS_NUMBER_MAX_VALUE = new BigDecimal("9007199254740992");

    /**
     * 执行SQL语句
     */
    public static void execSql(String sqls, Properties prop, Connection conn) throws SQLException {
        sqls = StringUtil.replaceVar(sqls, prop);
        List<String> sqlList = SqlUtil.splitSQL(sqls);
        for (String sql : sqlList) {
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.execute();
            }
        }
    }

	/**
	 * 释放数据库连接
	 */
	public static void releaseConnection(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				log.error("释放连接异常", e);
			}
		}
	}
	
	/**
	 * 回滚数据库连接
	 */
	public static void rollbackConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error("回滚异常", e);
			}
		}
	}
	
	/**
	 * 关闭结果集
	 */
	public static void closeResultSet(ResultSet rst) {
		if (rst != null) {
			try {
				if (!rst.isClosed())
					rst.close();
			} catch (SQLException e) {
				log.error("关闭结果集异常", e);
			}
		}
	}
	
	/**
	 * java.sql.Types转换为报表查询所需的类型
	 */
	public static String sqlTypeToReportType(int sqlType) {
		String javaType = "string";
		switch (sqlType) {
		case Types.BIT:
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
		case Types.BIGINT:
			javaType="integer";
			break;
		case Types.FLOAT:
		case Types.REAL:	
		case Types.DOUBLE:	
		case Types.NUMERIC:	
		case Types.DECIMAL:	
			javaType="double";
			break;
		case Types.DATE:	
		case Types.TIME:	
		case Types.TIMESTAMP:		
			javaType="date";
		default:
			break;
		}
		return javaType;
	}


	/**
	 * Spring的JdbcTemplate的QueryForList返回值的特殊处理
	 */
	public static List<Map<String,Object>> handleJdbcType(List<Map<String,Object>> list){
		return list.stream()
				   .map(map -> {
							 map.forEach((k,v) -> map.put(k,handleJdbcType(v)));
			                 return map;})
				   .collect(Collectors.toList());
	}

	/**
	 * 默认不处理double的精度
	 */
	public static Object handleJdbcType(Object obj) {
		return handleJdbcType(obj,false);
	}

	/**
	 * JDBC类型的特殊处理: 针对Web界面的SQL查询器
	 * 
	 * <br>分析:此时对于Double等小数位不用格式化处理, 由 handleJdbcTypeDouble 额外处理
	 * <br>JDBC的所有类型参见: @java.sql.Types
	 * <br>Mybatis的类型处理参见: http://www.mybatis.org/mybatis-3/zh/configuration.html#typeHandlers
	 */
	public static Object handleJdbcType(Object obj, boolean doubleTwoScale) {
		//日期/时间的格式化处理
		if (obj instanceof Time) {
			return DateFormatUtils.format((Time) obj, "yyyy-MM-dd HH:mm:ss");
		} else if (obj instanceof Date) {
			return DateFormatUtils.format((Date) obj, "yyyy-MM-dd");
		} else if (obj instanceof Timestamp) {
			return DateFormatUtils.format((Timestamp) obj, "yyyy-MM-dd HH:mm:ss");
		} else if (obj instanceof java.util.Date) {
			return DateFormatUtils.format((java.util.Date) obj, "yyyy-MM-dd HH:mm:ss");

			//Clob,Blob处理	20190214 情人节 何鹏举 处理Clob到字符串
		} else if (obj instanceof Blob) {
			return "(blob)";
		} else if (obj instanceof Clob) {
			Clob clob = (Clob) obj;
			try (Reader reader = clob.getCharacterStream()) {
				char[] buf = new char[(int) clob.length()];
				reader.read(buf);
				return new String(buf);
			} catch (SQLException | IOException e) {
				return "(clob)";
			}

			//空值处理
		} else if (obj == null) {
			return "";

		} else {
			if (doubleTwoScale) {
				if (obj instanceof Float) {
					return Double.parseDouble(String.format("%.2f", obj));
				} else if (obj instanceof Double) {
					return Double.parseDouble(String.format("%.2f", obj));
				} else if (obj instanceof BigDecimal) {
					return ((BigDecimal) obj).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
			}

			//20190219 王正英/何鹏举 对于BigDecimal当大于js的最大值: 2^53时, 返回字符串
			if (obj instanceof BigDecimal) {
				BigDecimal dbValue = (BigDecimal) obj;
				if (dbValue.compareTo(JS_NUMBER_MAX_VALUE) > 0) {
					return dbValue.toString();
				}
			}
			return obj;
		}
	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Sql查询器相关
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * 获取Sql语句的类型
	 *
	 * 20190620 追加 Merge和Call
	 */
	public static String getSqlType(String sql){
		String newsql = sql.trim().toLowerCase();
		if(newsql.startsWith("select")) return "Select";
		if(newsql.startsWith("update")) return "Update";
		if(newsql.startsWith("delete")) return "Delete";
		if(newsql.startsWith("insert")) return "Insert";
		if(newsql.startsWith("merge"))  return "Merge";
		if(newsql.startsWith("call"))   return "Call";

		return "other";
	}
	
	/**
	 * 处理增删改语句
	 */
	public static int handleUpdate(Connection conn, String sql) {
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			int rows = pstmt.executeUpdate();
			return rows;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


}
