package com.hepengju.mockdata.core;


import com.hepengju.mockdata.generator.Generator;

import java.util.List;

/**
 * 
 * 数据产生器的选择器
 * 
 * @author hepengju
 *
 */
@FunctionalInterface
public interface Selector {
	
	/**
	 * @param metaColumn  列元数据
	 * @param sampleData  样例数据
	 */
	Generator<?> select(ColumnMeta metaColumn, List<Object> sampleData);
}
