package cn.sowell.datacenter.test;

import java.util.HashMap;

import org.bouncycastle.jcajce.provider.digest.GOST3411.HashMac;

public class TestHashMap {

	public static void main(String[] args) {
		
		HashMap<String, String> map  = new HashMap<String, String>();
		
		String put = map.put("1", "55555");
		String put2 = map.put("1", "666");
		
		System.out.println(map);
		
		
	}
}
