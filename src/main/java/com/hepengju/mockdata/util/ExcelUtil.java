package com.hepengju.mockdata.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.PropertyPlaceholderHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Excel工具类
 * 
 * <pre>
 *   参考： 
 *    1.Apache的POI组件：http://poi.apache.org/spreadsheet/index.html
 *      - 注释： POI为"Poor Obfuscation Implementation"的首字母缩写，意为"可怜的模糊实现"
 *    2.没有找到优雅简约的第三方更高级封装,因此自己进行封装
 *      - 阿里的easyExcel对BO类的影响较大,因此未采用
 * </pre>
 * 
 * <pre>
 * 
 *  Excel文件的导出:
 *    1.格式: 统一采用xlsx(Excel2007及之后版本),统一使用性能较好的POI的SXSSFWorkbook
 *    2.导出方式:
 *      * {@link ExcelUtil#exportFromList}: 给定标题List和数据List,输出到指定流
 *
 *  Excel文件的导入: @ExcelService
 *    1.格式: 支持xls和xlsx格式
 *    2.导入步骤:
 *      a.下载模板: 数据库表配置,指定导入的表名,Excel每列(中文)对应的数据库表的列(英文)及类型(日期/数字/字符串)
 *      b.填写数据: 用户填写
 *      c.上传文件: 用户上传
 *      d.数据校验: Service层校验(比如日期解析校验)
 *      e.批量导入: batch方法插入数据库表
 * 
 *  日期: 20190311
 *  问题: 在浦发银行测试"元数据导入", 对于xlsx格式, 开发机6M读取还行, 超过10M基本就要报内存溢出
 *  解决: 
 *      * 第一种: POI的SAX解析方式
 *          @see <a href="http://poi.apache.org/components/spreadsheet">POI官网解析模式对比图</a>
 *          @see <a href="http://poi.apache.org/components/spreadsheet/limitations.html">POI官网限制及解决说明</a>
 *          @see <a href="https://www.cnblogs.com/swordfall/p/8298386.html">POI读写大数据量excel，解决超过几万行而导致内存溢出的问题</a>
 *          @see <a href-"https://blog.csdn.net/daiyutage/article/details/53023020">POI解决读入Excel内存溢出</a>
 *      * 第二种: 阿里的easyExcel
 *          @see <a href="https://github.com/alibaba/easyexcel">easyexcel</a>
 *          @see <a href="https://github.com/alibaba/easyexcel/blob/master/abouteasyexcel.md">easyexcel要去解决的问题</a>
 *          @see <a href="https://github.com/alibaba/easyexcel/blob/master/quickstart.md">easyexcel核心功能</a>
 *      * 分析: 由于easyExcel中的说明,POI即使使用SAX模式解析一定程度解决内存溢出问题,但还有缺陷....因此选择阿里的方案
 *
 * 日期: 20190715
 * 问题: 导入1, 取得1.0的问题
 * 解决: 对于所有的Double进行格式化处理
 * </pre>
 * 
 * @author he_pe 2018-03-07
 */
public class ExcelUtil {

	private static final DecimalFormat decimalFormat = new DecimalFormat("####################.###########"); // 整数20位, 小数11位
	private static final int    MAX_ROW   = 100_0000; //一百万
	private static final String FONT_NAME = "等线";

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 输出Excel
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * 给定数据库连接和SQL语句,输出到指定流
	 * 给定标题List和数据List,输出到指定流; 支持多Sheet导出
	 */
	public static void exportFromDB(Connection conn, String sql, OutputStream os) {
		// 1.创建工作薄,工作表
		Workbook wb = new SXSSFWorkbook();
		Sheet sheet = wb.createSheet();

		// 20200111 hepengju 补充一个Sheet, 记录SQL语句
		Sheet sqlSheet = wb.createSheet("sql");
		Row sqlRow = sqlSheet.createRow(0);
		Cell sqlCell = sqlRow.createCell(0);
		sqlCell.setCellValue(sql);

		try (PreparedStatement pstmt = conn.prepareStatement(sql);
			 ResultSet rst = pstmt.executeQuery();) {
			ResultSetMetaData metaData = rst.getMetaData();
			int rowNum = 0, count = metaData.getColumnCount();

			// 20200113 hepengju WPS中默认为宋体, 比较难看
			Font titleFont = wb.createFont();
			titleFont.setBold(true);
			titleFont.setFontName(FONT_NAME);

			Font commonFont = wb.createFont();
			commonFont.setFontName(FONT_NAME);

			// 2.创建标题行
			Row titleRow = sheet.createRow(rowNum++);
			CellStyle titleCellStyle = buildTitleCellStyle(wb, titleFont);
			for (int i = 0; i < count; i++) {
				String label = metaData.getColumnLabel(i + 1); // 列标签
				Cell cell = titleRow.createCell(i);
				cell.setCellStyle(titleCellStyle);
				cell.setCellValue(label);
			}

			// 冻结首行
			sheet.createFreezePane(0, 1);

			// 3.写入结果集
			CellStyle commonCellStyle = buildCommonCellStyle(wb, commonFont);
			while (rst.next()) {
				Row row = sheet.createRow(rowNum++);
				for (int i = 0; i < count; i++) {
					Object oldValue = rst.getObject(i + 1);
					Object newValue = DBUtil.handleJdbcType(oldValue); // JDBC类型处理
					Cell cell = row.createCell(i);
					cell.setCellStyle(commonCellStyle);
					fillCellByType(cell, newValue);
				}
			}

			// 4.工作薄输出到流
			wb.write(os);
			wb.close();
		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		} finally {
			DBUtil.releaseConnection(conn);
		}
	}
	public static void exportFromList(List<String> title, List<? extends List<? extends Object>> data, OutputStream os) {
		exportFromList(title, data, os, null);
	}
	public static void exportFromList(List<String> title, List<? extends List<? extends Object>> data, OutputStream os, String sheetName) {
		try (Workbook wb = new SXSSFWorkbook();) {
			writeSheet(wb, sheetName, title, data);
			wb.write(os);
			wb.setActiveSheet(0); //激活首个Sheet页
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void exportFromList(Map<String, List<String>> titleMap, Map<String, List<List<Object>>> dataMap, OutputStream os) {
		try (Workbook wb = new SXSSFWorkbook();) {
			titleMap.forEach((name, title) -> {
				List<List<Object>> data = dataMap.get(name);
				writeSheet(wb, name, title, data);
			});

			wb.write(os);
			wb.setActiveSheet(0); //激活首个Sheet页
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	


	/**
	 * 取得工作薄中的所有工作表名称
	 */
	public static List<String> getSheetNameList(Workbook wb) {
		List<String> sheetNameList = new ArrayList<>();
		Iterator<Sheet> iterator = wb.sheetIterator();
		while (iterator.hasNext()) {
			Sheet sheet = iterator.next();
			sheetNameList.add(sheet.getSheetName());
		}
		return sheetNameList;
	}
	public static List<String> getTitleList(Sheet sheet) { return getTitleList(sheet, 0); }
	public static List<String> getTitleList(Sheet sheet, String columnEnName) {
		return getTitleList(sheet, getColumnIndexByEnName(columnEnName));
	}
	public static List<String> getTitleList(Sheet sheet, int lastColumnIndex) {
		List<String> titleList = new ArrayList<>();
		Row titleRow = sheet.getRow(0);
		if (titleRow == null) throw new RuntimeException("excel.titleIsNull");
		for (int i = 0; i < (lastColumnIndex == 0 ? titleRow.getLastCellNum() : lastColumnIndex); i++) {
			Cell cell = titleRow.getCell(i);
			titleList.add(cell == null ? "" : excelTrim(cell.getStringCellValue()));
		}
		return titleList;
	}

	public static List<List<Object>> getDataList(Sheet sheet) { return getDataList(sheet, 0);}
	public static List<List<Object>> getDataList(Sheet sheet, String columnEnName) { return getDataList(sheet, getColumnIndexByEnName(columnEnName)); }
	public static List<List<Object>> getDataList(Sheet sheet, int lastColumnIndex) { return getDataList(sheet, lastColumnIndex, 2); }
	public static List<List<Object>> getDataList(Sheet sheet, int lastColumnIndex, int dataBeginRow) {
		List<List<Object>> data = new ArrayList<>();
		//Row firstRow = sheet.getRow(1);
		//if(firstRow == null) return data;
		for (int i = dataBeginRow - 1; i <= sheet.getLastRowNum(); i++) {
			Row curRow = sheet.getRow(i);
			if (!isRowBlank(curRow)) {
				List<Object> list = new ArrayList<>();
				for (int j = 0; j < (lastColumnIndex == 0 ? curRow.getLastCellNum() : lastColumnIndex); j++) {
					Cell cell = curRow.getCell(j);
					list.add(getCell(cell));
				}
				data.add(list);
			}
		}
		return data;
	}
	
	public static List<List<String>> getAllData(Sheet sheet) {
		return getAllData(sheet, 0);
	}
	public static List<List<String>> getAllData(Sheet sheet, String columnEnName) {
		return getAllData(sheet, getColumnIndexByEnName(columnEnName));
	}
	public static List<List<String>> getAllData(Sheet sheet, int lastColumnIndex) {
		List<List<String>> data = new ArrayList<>();
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row curRow = sheet.getRow(i);
			//if(curRow != null) {
			if (!isRowBlank(curRow)) {
				List<String> list = new ArrayList<>();
				for (int j = 0; j < (lastColumnIndex == 0 ? curRow.getLastCellNum() : lastColumnIndex); j++) {
					Cell cell = curRow.getCell(j);
					list.add(cell == null ? "" : cell.getStringCellValue());
				}
				data.add(list);
			}
		}
		return data;
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 替换值: 采用Spring的PropertyPlaceholderHelper简化操作,支持默认值
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	private static PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}",":",true);

	/**
	 * 替换整个Sheet的变量
	 */
	public static void replaceSheet(Sheet sheet, Map<String, Object> map) { replaceSheet(sheet, transferToProperties(map)); }
	public static void replaceSheet(Sheet sheet, Map<String, Object> map, int lastRow, int lastCol) { replaceSheet(sheet, transferToProperties(map), lastRow, lastCol); }
	public static void replaceSheet(Sheet sheet, Map<String, Object> map, String range) { replaceRange(sheet, transferToProperties(map), range); }

	/**
	 * 读取单元格的值(Object)
	 *
	 * <br> http://poi.apache.org/components/spreadsheet/quick-guide.html
	 */
	public static Object getCell(Cell cell) {
		if (cell == null) return null;

		Object obj = null;
		//switch (cell.getCellTypeEnum()) {
		switch (cell.getCellType()) {
			case STRING:
				obj = excelTrim(cell.getRichStringCellValue().getString());
				break;
			case NUMERIC:
				if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
					obj = cell.getDateCellValue();

					// 20190312
					if (obj != null) {
						Date date = (Date) obj;
						obj = new Date(date.getTime());
					}

				} else {
					// 20190715 解决Excel中输入1 --> 读取到1.0的问题
					obj = decimalFormat.format(cell.getNumericCellValue());
				}
				break;
			case BOOLEAN:
				obj = cell.getBooleanCellValue();
				break;
			case FORMULA:
				//公式的取公式的值
				try {
					FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
					CellValue cellValue = evaluator.evaluate(cell);
					switch (cellValue.getCellType()) {
						case BOOLEAN:
							obj = cell.getBooleanCellValue();
							break;
						case NUMERIC:
							if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
								obj = cell.getDateCellValue();
								// 20190312
								if (obj != null) {
									Date date = (Date) obj;
									obj = new Date(date.getTime());
								}
							} else {
								// 20190715 解决Excel中输入1 --> 读取到1.0的问题
								obj = decimalFormat.format(cell.getNumericCellValue());
							}
							break;
						case STRING:
							obj = excelTrim(cell.getStringCellValue());
							break;
						case BLANK:
						case ERROR:
						default:
							obj = null;
					}
				} catch (Exception e) {
					obj = cell.getCellFormula();
				}
				break;
			default:
				obj = null;
		}

		return obj;
	}

	/**
	 * Excel中的空格是160, 仅仅用Java的String的trim方法无法去除空格, 因此编写此方法
	 *
	 * @see String#trim()
	 */
	public static String excelTrim(String str) {
		if (str == null) return null;

		char[] value = str.toCharArray();
		int len = value.length;
		int st = 0;
		char[] val = value;    /* avoid getfield opcode */

		while ((st < len) && (val[st] <= ' ' || val[st] == (char) 160)) {
			st++;
		}
		while ((st < len) && (val[len - 1] <= ' ' || val[len - 1] == (char) 160)) {
			len--;
		}

		// 20190703 由于在数据校验的时候, 每个非空校验都要做null和""的判断, 麻烦, 此处将空白单元格直接处理为null返回
		String result = ((st > 0) || (len < value.length)) ? str.substring(st, len) : str;
		return "".equals(result) ? null : result;
	}


	/**
	 * 读取工作表的单元格的字符串值
	 * 
	 * <br> 1.POI的行列是以0为起始值
	 * <br> 2.此方法的行列以1为起始值(原因: 与VBA的cells(rowIndex,columnIndex)保持一致
	 */
	public static String getCellString(Sheet sheet, int rowIndex, int columnIndex) {
		Row row = sheet.getRow(rowIndex - 1);
		Cell cell = row.getCell(columnIndex - 1);
		return excelTrim(cell.getStringCellValue());
	}
	public static String getRangeString(Sheet sheet, String range) {
		int numPosition = getRangeNumPosition(range);
		int rowIndex = Integer.parseInt(range.substring(numPosition));
		int columnIndex = getColumnIndexByEnName(range.substring(0, numPosition));
		return getCellString(sheet, rowIndex, columnIndex);
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 设置值
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	/**
	 * 给工作表的单元格设置值
	 * 
	 * <br> 1.POI的行列是以0为起始值
	 * <br> 2.此方法的行列以1为起始值(原因: 与VBA的cells(rowIndex,columnIndex)保持一致
	 */
	public static void setCell(Sheet sheet, int rowIndex, int columnIndex, Object obj) {
		Row row = sheet.getRow(rowIndex - 1);
		Cell cell = row.getCell(columnIndex - 1);
		if (obj instanceof String) {
			cell.setCellValue((String) obj);
		} else {
			fillCellByType(cell, DBUtil.handleJdbcType(obj, true));
		}
	}
	
	/**
	 * 给工作表的单元格设置值
	 * 
	 * @param sheet  工作表
	 * @param range  单元格: D5/C10
	 * @param obj    填入对象
	 * @param rowOff 行偏移
	 * @param colOff 列偏移
	 */
	public static void setRange(Sheet sheet, String range, Object obj, int rowOff, int colOff) {
		int numPosition = getRangeNumPosition(range);
		int rowIndex = Integer.parseInt(range.substring(numPosition)) + rowOff;
		int columnIndex = getColumnIndexByEnName(range.substring(0, numPosition)) + colOff;
		setCell(sheet, rowIndex, columnIndex, obj);
	}
	
	/**
	 * 重载的不带行列偏移的方法
	 */
	public static void setRange(Sheet sheet,String range,Object obj, int rowOff) { setRange(sheet, range, obj, rowOff, 0); }
	public static void setRange(Sheet sheet,String range,Object obj) { setRange(sheet, range, obj, 0, 0); }

	/**
	 * 给定Map数据, 逐个设置进去
	 *
	 * <br> Map的key为单元格
	 */
	public static void setRangeBatch(Sheet sheet, Map<String, Object> rangeDataMap) {
		rangeDataMap.forEach((range, obj) -> setRange(sheet, range, obj));
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 复制单元格,复制行,复制列
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	/**
	 * 插入行
	 */
	@SuppressWarnings("deprecation")
	public static void insertRow(Sheet sheet, int rowIndex, int rowCount) {

		//20180727 移动行为零时,直接返回;避免shiftRows方法报错
		if (rowCount == 0) return;

		//移动下方的行
		int lastRowNum = sheet.getLastRowNum() - 1;
		sheet.shiftRows(rowIndex - 1, lastRowNum, rowCount, true, false);

		//新的空白行和上方的行格式一致
		Row oldRow = sheet.getRow(rowIndex - 2);
		for (int i = 0; i < rowCount; i++) {
			Row newRow = sheet.createRow(rowIndex + i - 1);
			newRow.setRowStyle(oldRow.getRowStyle());
			newRow.setHeight(oldRow.getHeight());
			for (int j = 0; j < oldRow.getLastCellNum(); j++) {
				Cell oldCell = oldRow.getCell(j);
				Cell newCell = newRow.createCell(j);
				newCell.setCellStyle(oldCell.getCellStyle());
				newCell.setCellType(oldCell.getCellType());
			}
		}
	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 内部辅助功能
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * 替换某个单元格的变量
	 */
	private static void replaceRange(Sheet sheet, Properties properties, String range) {
		int numPosition = getRangeNumPosition(range);
		int rowIndex = Integer.parseInt(range.substring(numPosition));
		int columnIndex = getColumnIndexByEnName(range.substring(0, numPosition));
		String cellString = getCellString(sheet, rowIndex, columnIndex);
		replacePlaceholders(sheet, properties, columnIndex, rowIndex, cellString);
	}

	/**
	 * 替换整个Sheet的变量
	 */
	private static void replaceSheet(Sheet sheet, Properties properties) {
		for (Row row : sheet) {
			for (Cell cell : row) {
				String cellString = cell.getStringCellValue();
				replacePlaceholders(sheet, properties, cell.getRowIndex() + 1, cell.getColumnIndex() + 1, cellString);
			}
		}
	}

	/**
	 * 替换整个Sheet的变量(指定最后一行和最后一列)
	 */
	private static void replaceSheet(Sheet sheet, Properties properties, int lastRow, int lastCol) {
		for (int row = 1; row <= lastRow; row++) {
			for (int col = 1; col < lastCol; col++) {
				String cellString = getCellString(sheet, row, col);
				replacePlaceholders(sheet, properties, row, col, cellString);
			}
		}
	}

	private static void replacePlaceholders(Sheet sheet, Properties properties, int rowIndex, int columnIndex, String cellString) {
		if (cellString != null && cellString.contains("${")) {
			String newStr = helper.replacePlaceholders(cellString, properties);
			setCell(sheet, rowIndex, columnIndex, newStr);
		}
	}

	private static Properties transferToProperties(Map<String, Object> map) {
		Properties prop = new Properties();
		//注意: Spring的replacePlaceholders对于属性要求必须为字符串,否则不替换!!
		map.forEach((k, v) -> {
			String value = String.valueOf(DBUtil.handleJdbcType(v, true));
			if (StringUtils.isNotBlank(value)) { //非空的加入（这样才方便使用默认值）
				prop.put(k, value);
			}
		});
		return prop;
	}

	/**
	 * 计算数字所在位置
	 */
	private static int getRangeNumPosition(String range) {
		int numPosition = 0;
		char[] charArray = range.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (String.valueOf(charArray[i]).matches("\\d")) {
				numPosition = i;
				break;
			}
		}
		return numPosition;
	}

	/**
	 * 计算英文列名对应的列序号(从1开始)
	 */
	public static int getColumnIndexByEnName(String colEn) {
		colEn = colEn.toUpperCase();
		int colnum = 0;
		switch (colEn.length()) {
			case 1:
				colnum = colEn.toCharArray()[0] - 'A' + 1;
				break;
			case 2:
				colnum = (colEn.toCharArray()[0] - 'A' + 1) * 26
						+ (colEn.toCharArray()[1] - 'A' + 1);
				break;
			case 3:
				colnum = (colEn.toCharArray()[0] - 'A' + 1) * 26 * 26
						+ (colEn.toCharArray()[1] - 'A' + 1) * 26
						+ (colEn.toCharArray()[2] - 'A' + 1);
				break;
		}
		return colnum;
	}

	/**
	 * 根据列中文名称 和 标题集合获取对应的列序号(从0开始)
	 *
	 * @param titleList 标题集合
	 * @return MAP key->title val->序号（从0开始）
	 */
	public static Map<String, Integer> getTitleIndexMap(List<String> titleList) {
		Map<String, Integer> map = new LinkedHashMap<>();
		for (int i = 0; i < titleList.size(); i++) {
			map.put(titleList.get(i), i);
		}
		return map;
	}
	/**
	 * 根据列中文名称 和 标题集合获取对应的列序号(从1开始)
	 *
	 * @param titleList 标题集合
	 * @param colCn     列中文名称
	 * @return 列中文名称对应的列序号(从1开始)
	 */
	public static int getColumnIndexByCnName(List<String> titleList, String colCn) {
		return getTitleIndexMap(titleList).get(colCn) + 1;
	}

	/**
	 * 根据值类型的不同,使用不同的cell.setCellValue(类型)方法
	 */
	private static void fillCellByType(Cell cell, Object newValue) {
		if (newValue instanceof Number) {
			cell.setCellValue(Double.parseDouble(String.valueOf(newValue)));
		} else {
			//20190228 何鹏举 org.apache.poi.xssf.streaming.SXSSFCell.setCellValue(String)
			//单元格长度最大为32767, 在浦发的视图SQL语句中发现此情况, 此时导出: (exception: 异常信息)
			try {
				cell.setCellValue(String.valueOf(newValue));
			} catch (Exception e) {
				cell.setCellValue("(exception: " + e.getMessage() + ")");
			}
		}
	}

	/**
	 * 普通单元格样式, 标题单元格样式
	 * <br> 参考: 阿里的easyExcel的com.alibaba.excel.write.context.GenerateContextImpl.buildDefaultCellStyle()
	 */

	private static CellStyle buildTitleCellStyle(Workbook workbook, Font font) {
		CellStyle newCellStyle = buildCommonCellStyle(workbook, font);

		// 20190701 何鹏举 由于标题需要加粗, 因此创建了字体, 默认为Calibri, 比较难看, 而buildCommonCellStyle不设置加粗, 默认字体是等线, 因此修改此处
		// 20200113 字体调整到外面设置, 避免new很多个

		//水平垂直居中
		newCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		newCellStyle.setAlignment(HorizontalAlignment.CENTER);

		//填充颜色
		newCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		newCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

		return newCellStyle;
	}
	private static CellStyle buildCommonCellStyle(Workbook workbook, Font font) {
		CellStyle newCellStyle = workbook.createCellStyle();

		//四周边框
		newCellStyle.setBorderTop(BorderStyle.THIN);
		newCellStyle.setBorderBottom(BorderStyle.THIN);
		newCellStyle.setBorderLeft(BorderStyle.THIN);
		newCellStyle.setBorderRight(BorderStyle.THIN);

		// 字体
		newCellStyle.setFont(font);
		return newCellStyle;
	}

	/**
	 * 判断某行是否全部为空
	 */
	private static boolean isRowBlank(Row row) {
		if (row == null) return true;

		for (int i = 0; i < row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			Object obj = getCell(cell);
			if (obj != null && StringUtils.isNotBlank(String.valueOf(obj))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 写入工作表: 内部包含数据量的判断
	 */
	private static void writeSheet(Workbook wb, String name, List<String> title, List<? extends List<? extends Object>> data) {
		name = StringUtils.isBlank(name) ? "Sheet" : name;

		int count = data.size() / MAX_ROW; //整除
		if (count == 0) {
			writeSingleSheet(wb, name, title, data);
		} else {
			for (int i = 0; i <= count; i++) {
				String newName = name + "_" + i; //从Sheet0开始
				int fromIndex = MAX_ROW * i;
				int toIndex = Math.min(data.size(), MAX_ROW * (i + 1));
				List<? extends List<? extends Object>> newData = data.subList(fromIndex, toIndex);
				writeSingleSheet(wb, newName, title, newData);
			}
		}
	}

	/**
	 * 写入单个Sheet
	 */
	private static void writeSingleSheet(Workbook wb, String name, List<String> title, List<? extends List<? extends Object>> data) {
		//1.创建工作表(不存在才创建,举例: 读取模板,写入一些数据时sheet已经存在)
		Sheet sheet = wb.getSheet(name);
		if (sheet == null) {
			sheet = wb.createSheet(name);
		}

		// 20200113 hepengju WPS中默认为宋体, 比较难看
		Font titleFont = wb.createFont();
		titleFont.setBold(true);
		titleFont.setFontName(FONT_NAME);

		Font commonFont = wb.createFont();
		commonFont.setFontName(FONT_NAME);

		// 2.创建标题行
		int rowNum = 0;
		Row titleRow = sheet.createRow(rowNum++);
		CellStyle titleCellStyle = buildTitleCellStyle(wb, titleFont);
		if (title != null && title.size() > 0) {
			for (int i = 0; i < title.size(); i++) {
				Cell cell = titleRow.createCell(i);
				cell.setCellStyle(titleCellStyle);
				cell.setCellValue(title.get(i));
			}
		}

		// 冻结首行
		sheet.createFreezePane(0, 1);

		// 3.写入结果集
		CellStyle commonCellStyle = buildCommonCellStyle(wb, commonFont);
		commonCellStyle.setFont(commonFont);
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				Row row = sheet.createRow(rowNum++);
				List<? extends Object> rowData = data.get(i);
				for (int j = 0; j < rowData.size(); j++) {
					Cell cell = row.createCell(j);
					cell.setCellStyle(commonCellStyle);
					Object oldValue = rowData.get(j);
					Object newValue = DBUtil.handleJdbcType(oldValue); // JDBC类型处理
					fillCellByType(cell, newValue);
				}
			}
		}

	}

}
