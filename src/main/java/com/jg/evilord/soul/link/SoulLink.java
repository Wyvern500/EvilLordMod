package com.jg.evilord.soul.link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jg.evilord.utils.Pos;
import com.mojang.logging.LogUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class SoulLink implements ISoulLink {

	List<int[]> links;
	List<Pos> outputs;
	List<Pos> inputs;
	
	public SoulLink(int inputSlots, int outputSlots) {
		links = new ArrayList<>(inputSlots);
		links.add(new int[] {});
		links.add(new int[] {});
		outputs = new ArrayList<>(outputSlots);
		inputs = new ArrayList<>(inputSlots);
		for(int i = 0; i < inputSlots; i++) {
			inputs.add(new Pos());
		}
		for(int i = 0; i < outputSlots; i++) {
			outputs.add(new Pos());
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		nbt.putIntArray("input", links.get(0));
		nbt.putIntArray("out", links.get(1));
		for(int i = 0; i < inputs.size(); i++) {
			nbt.putIntArray("input" + i, inputs.get(i).pos);
		}
		for(int i = 0; i < outputs.size(); i++) {
			nbt.putIntArray("out" + i, outputs.get(i).pos);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		links.set(0, nbt.getIntArray("input"));
		links.set(1, nbt.getIntArray("output"));
		for(int i = 0; i < inputs.size(); i++) {
			inputs.set(i, new Pos(nbt.getIntArray("input" + i)));
		}
		for(int i = 0; i < outputs.size(); i++) {
			outputs.set(i, new Pos(nbt.getIntArray("output" + i)));
		}
	}
	
	public void set(boolean output, BlockPos pos) {
		set(output, new int[] { pos.getX(), pos.getY(), pos.getZ() });
	}
	
	@Override
	public void set(boolean output, int[] arr) {
		if(output) {
			links.set(1, arr);
			bindToFirstFreePlace(true, arr);
		} else {
			links.set(0, arr);
			bindToFirstFreePlace(false, arr);
		}
		LogUtils.getLogger().info((output ? "output: " : "input: ") + 
				Arrays.toString(arr));
	}

	public void unbind(boolean output, BlockPos pos) {
		unbind(output, new int[] { pos.getX(), pos.getY(), pos.getZ() });
	}
	
	@Override
	public void unbind(boolean output, int[] pos) {
		if(output) {
			links.set(1, new int[] {});
			removeLink(true, new Pos(pos));
		} else {
			links.set(0, new int[] {});
			removeLink(false, new Pos(pos));
		}
	}
	
	public void bindToFirstFreePlace(boolean output, int[] pos) {
		if(output) {
			for(int i = 0; i < outputs.size(); i++) {
				if(outputs.get(i).pos.length == 0) {
					outputs.set(i, new Pos(pos));
					break;
				}
			}
		} else {
			for(int i = 0; i < inputs.size(); i++) {
				if(inputs.get(i).pos.length == 0) {
					inputs.set(i, new Pos(pos));
					break;
				}
			}
		}
	}
	
	public void removeLink(boolean output, Pos pos) {
		if(output) {
			//int i = outputs.indexOf(pos);
			int oi = -1;
			for(int j = 0; j < outputs.size(); j++) {
				if(outputs.get(j).equals(pos)) {
					oi = j;
					LogUtils.getLogger().info("Index: " + oi);
					break;
				}
			}
			String v = "{ ";
			for(Pos a : outputs) {
				v += Arrays.toString(a.pos) + ", ";
			}
			v += "}";
			LogUtils.getLogger().info("pos: " + Arrays.toString(pos.pos) 
				+ " outputs: " + v);
			if(oi != -1) {
				outputs.set(oi, new Pos());
				LogUtils.getLogger().info("Removing output");
			}
		} else {
			//int i = inputs.indexOf(pos);
			int ii = -1;
			for(int j = 0; j < inputs.size(); j++) {
				if(inputs.get(j).equals(pos)) {
					ii = j;
					break;
				}
			}
			if(ii != -1) {
				inputs.set(ii, new Pos());
			}
			LogUtils.getLogger().info("Input");
		}
	}
	
	public int getRemainingSlots(boolean output) {
		if(output) {
			int free = 0;
			for(Pos pos : outputs) {
				if(pos.isEmpty()) {
					free++;
				}
			}
			return free;
		} else {
			int free = 0;
			for(Pos pos : inputs) {
				if(pos.isEmpty()) {
					free++;
				}
			}
			return free;
		}
	}
	
	public int[] getInput() {
		return links.get(0);
	}
	
	public int[] getOutput() {
		return links.get(1);
	}
	
	public List<Pos> getInputs(){
		return inputs;
	}
	
	public List<Pos> getOutputs(){
		return outputs;
	}
	
	public List<int[]> getLinks(){
		return links;
	}
	
	@Override
	public String toString() {
		String input = "";
		for(Pos pos : inputs) {
			input += Arrays.toString(pos.pos) + ", ";
		}
		String output = "";
		for(Pos pos : outputs) {
			output += Arrays.toString(pos.pos) + ", ";
		}
		return "link: input: " + input + " output: " + output;
	}
	
}
