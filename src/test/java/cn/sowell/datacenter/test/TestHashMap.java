package cn.sowell.datacenter.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bouncycastle.jcajce.provider.digest.GOST3411.HashMac;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.config.FastJsonConfig;


public class TestHashMap {
	

	public static void main(String[] args) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" {    \"id\": \"123\",")
		.append("  \"name\": \"张三\",")
		.append("  \"studentList\": [")
		.append("  {")
		.append("  \"id\": \"2222\",")
		.append(" \"name\": \"李四\",")
		.append(" \"age\": \"13\"")
		.append(" },")
		.append("  {")
		.append("   \"id\": \"33333\",")
		.append("  \"name\": \"王u\",")
		.append(" \"age\": \"18\"")
		.append(" }")
		.append(" ]")
		.append(" }");
		
		  JSONObject object = JSON.parseObject(sb.toString());  
		
		  Object object2 = object.get("id");
		  Object object3 = object.get("name");
		  Object object4 = object.get("studentList");
		  
		  Teacher teacher = JSON.parseObject(sb.toString(), Teacher.class);
		  
		  // 保存到数据库
		 // session.save(teacher);
		  
		  List<Student> stuList = teacher.getStudentList();
		  
		  for (Student student : stuList) {
			 // student.setId(teacher.getid);
		}
		  
		  
		  
		  System.out.println();
		
	}
}


