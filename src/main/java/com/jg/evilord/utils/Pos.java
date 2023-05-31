package com.jg.evilord.utils;

import java.util.Arrays;

import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;

public class Pos {

	public int[] pos;
	
	public Pos() {
		this.pos = new int[] {};
	}
	
	public Pos(int[] pos) {
		this.pos = pos;
	}
	
	public boolean isEmpty() {
		return pos.length == 0;
	}
	
	public BlockPos toBlockPos() {
		return new BlockPos(pos[0], pos[1], pos[2]);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pos) {
			LogUtils.getLogger().info("Other: " + Arrays.toString(((Pos)obj).pos) +
					" This: " + Arrays.toString(pos));
			Pos other = (Pos)obj;
			int[] a = other.pos;
			return pos[0] == a[0] && pos[1] == a[1] && pos[2] == a[2];
		}
		return super.equals(obj);
	}
	
}
