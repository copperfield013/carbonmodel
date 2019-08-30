package cn.sowell.datacenter.test;

import org.junit.Test;

import cho.carbon.meta.extface.dto.AggregateExpressionDTO;
import cho.carbon.meta.model.IAggregateExpressionDAO;

public class CommonTest {
	
	public class AAggregateExpressionDAO implements IAggregateExpressionDAO{

		@Override
		public AggregateExpressionDTO query(Integer id) {
			
			
			return new AggregateExpressionDTO();
		}
		
	}
	
	@Test
	public void fun() {
		System.out.println( System.currentTimeMillis());
	}

}
