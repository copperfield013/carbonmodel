package cn.sowell.datacenter.test;

public class TestJson {
	
	public static void main(String[] args)  {
		
		Class<? extends Teacher> clazz = new Teacher().getClass();
		
		ClassLoader classLoader = clazz.getClassLoader();
		System.out.println(classLoader);
		
		
		clazz = null;
		System.gc();
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Teacher teacher = new Teacher();
			
			System.out.println(teacher);
			
//			System.out.println(newInstance);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
	}
	
	
}
