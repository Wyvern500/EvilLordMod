package com.jg.evilord.animation;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jg.evilord.animation.model.JgModelPart;

public class Keyframe {

	public int dur;
	public int startTick;
	public int startVisualTick;
	public String easing;
	public int type = 0;
	public Map<JgModelPart, float[]> translations;
	public Map<JgModelPart, float[]> rotations;
	
	public Keyframe(int dur) {
		this.dur = dur;
		translations = new HashMap<>();
		rotations = new HashMap<>();
		this.easing = "empty";
	}
	
	public Keyframe(int dur, String easing) {
		this.dur = dur;
		translations = new HashMap<>();
		rotations = new HashMap<>();
		this.easing = easing;
	}
	
	public void copyTransformFrom(Keyframe kf) {
		translations = copyMap(kf.translations);
		rotations = copyMap(kf.rotations);
	}
	
	public Keyframe copy() {
		Keyframe kf = new Keyframe(dur);
		kf.startTick = startTick;
		kf.startVisualTick = startVisualTick;
		kf.translations = copyMap(translations);
		kf.rotations = copyMap(rotations);
		kf.type = type;
		return kf;
	}

	protected Map<JgModelPart, float[]> copyMap(Map<JgModelPart, float[]> other){
		Map<JgModelPart, float[]> map = new HashMap<>();
		for(Entry<JgModelPart, float[]> entry : other.entrySet()) {
			float[] temp = entry.getValue();
			map.put(entry.getKey(), new float[] { temp[0], temp[1], temp[2] });
		}
		return map;
	}
	
	@Override
	public String toString() {
		return "Duration: " + dur + " startTick: " + startTick + " startVisualTick: " 
				+ startVisualTick + " pos size: " 
				+ translations.size() + " rot size: " + rotations.size();
	}
	
}
