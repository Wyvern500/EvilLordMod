package com.jg.evilord.client.handler;

import java.util.HashMap;
import java.util.Map;

public class SoulLinkHandler {

	Map<int[], Integer> data;
	
	public SoulLinkHandler() {
		data = new HashMap<>();
	}
	
	public void next(int[] pos) {
		data.putIfAbsent(pos, 0);
		data.put(pos, data.get(pos) + 1);
	}
	
	public void back(int[] pos) {
		data.putIfAbsent(pos, 0);
		data.put(pos, data.get(pos) - 1);
	}

	public Map<int[], Integer> getData() {
		return data;
	}
	
}
