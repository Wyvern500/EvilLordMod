package com.jg.evilord.client.helpers;

import com.jg.evilord.client.handler.ClientHandler;

public class Timer {

	int id;
	float time;
	float maxTime;
	boolean start;
	OnTimerEnd onEnd;
	
	public Timer(float maxTime, OnTimerEnd end) {
		this(-1, maxTime, end);
	}
	
	public Timer(int id, float maxTime, OnTimerEnd end) {
		this.id = id;
		this.time = 0;
		this.maxTime = maxTime;
		this.onEnd = end;
	}
	
	public void tick() {
		if(start) {
			if(time < maxTime) {
				time += ClientHandler.partialTicks;
				if(time > maxTime) {
					time = maxTime;
				}
			} else {
				onEnd.timerEnded(id, this);
				time = 0;
				start = false;
			}
		}
	}
	
	public Timer start() {
		time = 0;
		start = true;
		return this;
	}
	
	public void stop() {
		start = false;
	}
	
	public int getName() {
		return id;
	}

	public void setName(int id) {
		this.id = id;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public float getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(float maxTime) {
		this.maxTime = maxTime;
	}

	public interface OnTimerEnd {
		public void timerEnded(int id, Timer timer);
	}
	
}
