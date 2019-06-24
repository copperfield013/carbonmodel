package cn.sowell.datacenter.test;

import com.abc.extface.dto.AggregateExpressionDTO;
import com.abc.model.IAggregateExpressionDAO;

public class CommonTest {
	
	public class AAggregateExpressionDAO implements IAggregateExpressionDAO{

		@Override
		public AggregateExpressionDTO query(Integer id) {
			
			
			return new AggregateExpressionDTO();
		}
		
	}

}
