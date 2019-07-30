package cho.carbon.imodel.model.modelitem.dao.impl;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;

import cho.carbon.imodel.model.modelitem.dao.MiTableDBDao;


@Repository
public class MiTableDBDaoImpl implements MiTableDBDao {
	
	
	@Resource
	SessionFactory sFactory;
	
	@Override
	public List queryCreTab(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT	a.code,")
		.append(" concat( \"create table \", a.belong_table, \"( `id`  bigint(20) NOT NULL AUTO_INCREMENT, `ABP0001`  varchar(32) DEFAULT ")
		.append(" NULL ,PRIMARY KEY (`id`),KEY `abp_index` (`ABP0001`) USING BTREE)\" ) valstr ")
		.append(" FROM")
		.append(" ( SELECT code, belong_table FROM t_cc_mi_value  WHERE belong_table IS NOT NULL GROUP BY belong_table ) a")
		.append(" LEFT JOIN ")
		.append(" ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b")
		.append(" ON a.belong_table = b.table_name ")
		.append(" WHERE 	b.table_name IS NULL");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryNewAddCol(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT	a.code,")
		.append(" CONCAT(	'alter table ',	a.belong_table,	' add ',	a.code,	' ', ")
		.append(" CASE	a.data_type ")
		.append(" WHEN '101' THEN	concat( 'int(', IF ( a.data_length IS NULL, \"11\", a.data_length ), ')' ) ")
		.append(" WHEN '102' THEN	concat( 'double(', IF ( a.data_length IS NULL, \"10,2\", a.data_length ), ')' ) ")
		.append(" WHEN '103' THEN	concat( 'bigint(', IF ( a.data_length IS NULL, \"20\", a.data_length ), ')' ) ")
		.append(" WHEN '104' THEN	concat( 'decimal(', IF ( a.data_length IS NULL, \"10,2\", a.data_length ), ')' ) ")
		.append(" WHEN '105' THEN	concat( 'varchar(', IF ( a.data_length IS NULL, \"32\", a.data_length), ')')")
		.append(" WHEN '106' THEN	'date' ")
		.append(" WHEN '107' THEN	'datetime(3)' ")
		.append(" 	END,	'  default NULL;' 	) ")
		.append(" FROM")
		.append(" ( SELECT * FROM t_cc_mi_value ) a")
		.append(" LEFT JOIN")
		.append(" ( SELECT col.column_name FROM information_schema.COLUMNS col WHERE table_schema = '"+dataBaseName+"' ) b ")
		.append(" ON a.code = b.column_name")
		.append(" WHERE	b.column_name IS NULL 	AND a.belong_table IS NOT NULL ");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryEditCol(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT	c.code,")
		.append(" 	CONCAT( 'alter table ', c.belong_table, ' CHANGE COLUMN ', c.code, ' ', c.code, ' ', c.mType, '  default NULL;' ) ")
		.append(" FROM	(	SELECT		a.code,		CONCAT(")
		.append("	CASE a.data_type ")
		.append(" WHEN '101' THEN	concat( 'int(', IF ( a.data_length IS NULL, \"11\", a.data_length ), ')' ) ")
		.append(" 	WHEN '102' THEN	concat( 'double(', IF ( a.data_length IS NULL, \"10,2\", a.data_length ), ')' ) ")
		.append(" WHEN '103' THEN	concat( 'bigint(', IF ( a.data_length IS NULL, \"20\", a.data_length ), ')' ) ")
		.append(" WHEN '104' THEN	concat( 'decimal(', IF ( a.data_length IS NULL, \"10,2\", a.data_length ), ')' ) ")
		.append(" WHEN '105' THEN	concat( 'varchar(', IF ( a.data_length IS NULL, \"32\", a.data_length), ')')")
		.append(" WHEN '106' THEN	'date' ")
		.append(" WHEN '107' THEN	'datetime(3)' 		END")
		.append(" ) mType, b.column_name, b.COLUMN_TYPE, a.belong_table")
		.append(" FROM	( SELECT * FROM t_cc_mi_value ) a")
		.append(" inner JOIN ")
		.append(" ( SELECT col.column_name, col.COLUMN_TYPE FROM information_schema.COLUMNS col WHERE table_schema = '"+dataBaseName+"' ) b ")
		.append(" ON a.code = b.column_name ")
		.append(" 	WHERE		 a.belong_table IS NOT NULL ) c")
		.append(" WHERE c.mType<>c.COLUMN_TYPE");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreRelaTabFun(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" concat( ' CREATE FUNCTION GetALLRelated', code, '(pCodes VARCHAR(4000),pRelationType  VARCHAR(20),pDepth int)  RETURNS text CHARSET utf8  BEGIN\r\n" + 
				"  DECLARE vResult text;")
		.append(" DECLARE vDepth int; DECLARE vTemp text;  SET vTemp = pCodes; SET vResult = \"\"; set vDepth=0;  if pDepth<0  then  set vdepth=-1;  end if; WHILE vTemp is not ")
		.append(" null and vdepth<=pDepth DO  SELECT GROUP_CONCAT( distinct  ABC0913) INTO vTemp  FROM ', tablename, ' WHERE FIND_IN_SET(ABP0001,vTemp) and not ")
		.append(" FIND_IN_SET(ABC0913,vResult) and  ABC0914=pRelationType;  if vTemp is not null then   SET vResult = concat(vResult,\",\", vTemp);   end if; if pDepth>=0 ")
		.append(" then   set vdepth=vdepth+1;  end if; END WHILE; RETURN substr(vResult,2); END' ) ")
		.append(" FROM	( SELECT")
		.append(" concat( 't_', code, '_r1' ) tablename,	code,	concat( 'GetALLRelated', code ) funName ")
		.append(" FROM	t_cc_model_item WHERE 	type in('1', '101') 	) a")
		.append(" LEFT JOIN ( SELECT SPECIFIC_NAME FROM information_schema.ROUTINES t WHERE t.ROUTINE_SCHEMA = '"+dataBaseName+"' ) b ON a.funName = b.SPECIFIC_NAME ")
		.append(" WHERE	b.SPECIFIC_NAME IS NULL");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreRelaTab(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" concat( \"create table \", a.tablename, \"( `id`  bigint(20) NOT NULL AUTO_INCREMENT, ")
		.append(" `ABP0001`  varchar(32) Not NULL ,")
		.append(" `ABC0913`  varchar(32) DEFAULT NULL ,")
		.append(" `ABC0914`  varchar(32) DEFAULT NULL,PRIMARY KEY (`id`))\" ) ")
		.append(" FROM")
		.append(" ( SELECT concat( 't_', code, '_r1' ) tablename FROM t_cc_model_item WHERE type  in('1', '101') ) a")
		.append(" 	LEFT JOIN ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b ")
		.append(" ON a.tablename = b.table_name ")
		.append(" WHERE	b.table_name IS NULL");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}


	@Override
	public List queryCreRepeatTabIndex(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" CONCAT( \" ALTER TABLE \", a.tablename, \" ADD INDEX IF NOT EXISTS index_p(\", codein, \");\" ) ")
		.append(" FROM")
		.append(" ( SELECT CONCAT('t_', belong_model, '_', code) tablename, CONCAT(code,'_P') codein FROM t_cc_model_item WHERE type = '6' ) a")
		.append(" 	left JOIN ")
		.append(" ( SELECT table_name, COLUMN_NAME, INDEX_NAME FROM information_schema.statistics t ")
		.append(" WHERE t.table_schema = '"+dataBaseName+"' 	AND INDEX_NAME = 'index_p') b")
		.append(" ON a.tablename = b.table_name and a.codein=b.COLUMN_NAME")
		.append(" WHERE	b.table_name IS NULL ");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreEntityFileTbaF1(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" concat(	\"CREATE TABLE \",	a.tablename,	\"(\",	\" `\",	a.code,	\"_FP` varchar(32) NOT NULL COMMENT 'code',\",	\"`\",")
		.append(" a.code,	\"_F2` datetime(3) DEFAULT NULL COMMENT 'insert_time',\",	\"`\",	a.code,	\"_F3` mediumblob DEFAULT NULL COMMENT 'content',\",	\"PRIMARY KEY (`\",")
		.append(" 	a.code,	\"_FP`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC\" 	) ")
		.append(" FROM")
		.append(" ( SELECT concat( 't_', code, '_f1' ) tablename, code FROM t_cc_model_item WHERE type in('1', '101') ) a")
		.append(" LEFT JOIN ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b ON a.tablename = b.table_name ")
		.append(" WHERE	b.table_name IS NULL");
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreEntityFileTbaF2(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" concat(	\"CREATE TABLE \",	a.tablename,	\"(`id` bigint(20) NOT NULL AUTO_INCREMENT,\",	")
		.append(" \"`ABP0001` varchar(32) NOT NULL COMMENT 'record_code',\",")
		.append(" \"`ABP0002` varchar(32) DEFAULT NULL COMMENT 'leaf_Code',\",	\"`\",	a.code,	\"_FP` varchar(32) NOT NULL COMMENT 'bytes_code',\",")
		.append(" \"`\",	a.code,	\"_F4` varchar(32) NOT NULL COMMENT 'abcattr',\",	\"`\",	a.code,	\"_F5` datetime(3) NOT NULL COMMENT 'insert_time',\",")
		.append(" \"PRIMARY KEY (`id`, `\",	a.code,	\"_F5`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC\" 	) ")
		.append(" FROM")
		.append(" ( SELECT concat( 't_', code, '_f2' ) tablename, code FROM t_cc_model_item WHERE type  in('1', '101') ) a")
		.append(" LEFT JOIN ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b ON a.tablename = b.table_name ")
		.append(" WHERE	b.table_name IS NULL");

		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreEntityFileTbaF3(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT	concat(	\"CREATE TABLE \",	a.tablename,	\"(`id` bigint(20) NOT NULL AUTO_INCREMENT,\",	\"`\",")
		.append(" a.code,	\"_HP` varchar(32) NOT NULL COMMENT 'history_code',\",	\"`ABP0001` varchar(32) NOT NULL,\",")
		.append(" \"`ABP0002` varchar(32) DEFAULT NULL,\",	\"`\",	a.code,	\"_FP` varchar(32) COMMENT 'bytes_code',\",")
		.append(" \"`\",	a.code,	\"_F4` varchar(32) NOT NULL COMMENT 'abcattr',\",	\"`\",")
		.append(" a.code,	\"_F5` datetime(3) NOT NULL COMMENT 'insert_time',\",")
		.append(" \"PRIMARY KEY (`id`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC\" 	) ")
		.append(" FROM")
		.append(" ( SELECT concat( 't_', code, '_f3' ) tablename, code FROM t_cc_model_item WHERE type  in('1', '101') ) a")
		.append(" LEFT JOIN ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b ON a.tablename = b.table_name ")
		.append(" WHERE	b.table_name IS NULL");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreEntityTabH1(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" concat(	\"CREATE TABLE \",	a.tablename,	\"(\",	\" `\",	a.code,	\"_HP` varchar(32) NOT NULL,\",")
		.append(" \"`ABP0001` varchar(32) DEFAULT NULL,\",	\"`\",	a.code,	\"_H1` datetime(3) DEFAULT NULL,\",")
		.append(" \"`\",	a.code,	\"_H2` varchar(32) DEFAULT NULL,\",	\"`\",	a.code,	\"_H3` varchar(320) DEFAULT NULL,\",")
		.append(" 	\"`\",	a.code,	\"_H4` int(2) DEFAULT 2 COMMENT '1 total,2 increment',\",")
		.append(" 	\"`\",	a.code,	\"_H5` int(2) DEFAULT 0,\",	\"`\",	a.code,	\"_H6` blob NOT NULL,\",")
		.append(" \"PRIMARY KEY (`\",	a.code,	\"_HP`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC\" 	) ")
		.append(" FROM")
		.append(" ( SELECT concat( 't_', code, '_h1' ) tablename, code FROM t_cc_model_item WHERE type  in('1', '101') ) a")
		.append(" LEFT JOIN ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b ON a.tablename = b.table_name ")
		.append(" WHERE	b.table_name IS NULL");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreEntityTabH2(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" 	concat(	\"CREATE TABLE \",	a.tablename,	\"(\",	\" `\",	a.code,	\"_HP` varchar(32) NOT NULL,\",")
		.append(" \"`ABP0001` varchar(32) DEFAULT NULL,\",	\"`\",	a.code,	\"_H1` datetime(3) NOT NULL,\",")
		.append(" \"`\",	a.code,	\"_H2` varchar(32) DEFAULT NULL COMMENT 'usergroup_id',\",")
		.append(" \"`\",	a.code,	\"_H3` varchar(320) DEFAULT NULL  COMMENT 'descripation',\",")
		.append(" \"`\",	a.code,	\"_H4` int(2) DEFAULT 2 COMMENT '1 total,2 increment',\",")
		.append(" \"`\",	a.code,	\"_H5` int(2) DEFAULT 0  COMMENT 'source',\",")
		.append(" \"`\",	a.code,	\"_H6` blob NOT NULL  COMMENT 'content',\",")
		.append(" \"PRIMARY KEY (`\",	a.code,	\"_HP`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC\" 	) ")
		.append(" FROM")
		.append(" ( SELECT concat( 't_', code, '_h2' ) tablename, code FROM t_cc_model_item WHERE type  in('1', '101') ) a")
		.append(" LEFT JOIN ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b ON a.tablename = b.table_name  ")
		.append(" WHERE	b.table_name IS NULL ");

		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreEntityTabD1(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" concat(	\"CREATE TABLE \",	a.tablename,	\"(\",	\" `ABP0001` varchar(32) NOT NULL COMMENT 'code',\",")
		.append(" 	\"`\",	a.code,	\"_DT` datetime(3) DEFAULT NULL COMMENT 'insert_time',\",	\"`\",")
		.append(" 	a.code,	\"_D1` varchar(32) DEFAULT NULL COMMENT 'userid',\",	\"`\",")
		.append(" a.code,	\"_D2` varchar(255) DEFAULT NULL COMMENT 'reason',\",")
		.append(" \"PRIMARY KEY (`ABP0001`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC\" 	) ")
		.append(" FROM")
		.append(" ( SELECT concat( 't_', code, '_d1' ) tablename, code FROM t_cc_model_item WHERE type in('1', '101') ) a")
		.append(" LEFT JOIN ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b ON a.tablename = b.table_name ")
		.append(" WHERE	b.table_name IS NULL");

		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreEntityTabc1(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" concat(	\"CREATE TABLE \",	a.tablename,	\"(\",	\"`ABP0001` varchar(32) NOT NULL,\",")
		.append(" \"`\",	a.code,	\"_CT` datetime(3) NOT NULL,\",")
		.append(" \"`\",	a.code,	\"_C1` mediumblob NOT NULL,\",")
		.append(" \"PRIMARY KEY (`ABP0001`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC\" 	) ")
		.append(" FROM")
		.append(" ( SELECT concat( 't_', code, '_c1' ) tablename, code FROM t_cc_model_item WHERE type in('1', '101') ) a")
		.append(" LEFT JOIN ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b ON a.tablename = b.table_name ")
		.append(" WHERE	b.table_name IS NULL");

		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}

	@Override
	public List queryCreEntityTabc2(String dataBaseName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT")
		.append(" concat(	\"CREATE TABLE \",	a.tablename,	\"(\",	\"`ABP0001` varchar(32) NOT NULL,\",")
		.append(" \"`\",	a.code,	\"_CT` datetime(3) NOT NULL,\",	\"`\",	")
		.append(" a.code,	\"_C1` mediumblob NOT NULL,\",")
		.append(" \"PRIMARY KEY (`ABP0001`) USING BTREE) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC\" 	) ")
		.append(" FROM")
		.append(" 	( SELECT concat( 't_', code, '_c2' ) tablename, code FROM t_cc_model_item WHERE type  in('1', '101') ) a")
		.append(" LEFT JOIN ( SELECT table_name FROM information_schema.TABLES t WHERE t.table_schema = '"+dataBaseName+"' ) b ON a.tablename = b.table_name ")
		.append(" WHERE	b.table_name IS NULL");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
	}
	
	
}
