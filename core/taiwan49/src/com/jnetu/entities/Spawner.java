package com.jnetu.entities;

import java.util.Random;

import com.jnetu.main.Game;

public class Spawner {
	public static Random random;
	public int frames, maxFrames = 60;

	public long spawnTimeEnemy1 = 1000;
	public long waitTimeEnemy1;
	public boolean canSpawnEnemy1 = true;

	public long spawnTimeEnemy2 = 6400;
	public long waitTimeEnemy2;
	public boolean canSpawnEnemy2 = false;

	public long spawnTimeEnemy3 = 11000;
	public long waitTimeEnemy3;
	public boolean canSpawnEnemy3 = false;

	public boolean startSpawn1 = false;
	public boolean startSpawn2 = false;
	public boolean startSpawn3 = false;
	
	public long startTime;

	public Spawner() {
		random = new Random();
		

	}

	public void tick() {
		
		
		if(startTime == 0) {
			startTime = System.currentTimeMillis();
		}
		
		
		if (System.currentTimeMillis() - startTime >= spawnTimeEnemy1) {
			startSpawn1 = true;
			
		}
		if (System.currentTimeMillis() - startTime >= spawnTimeEnemy2) {
			startSpawn2 = true;
		}
		if (System.currentTimeMillis() - startTime >= spawnTimeEnemy3) {
			startSpawn3 = true;
		}
		
		
		if (startSpawn1) {
			spawnEnemy1();
		}
		if (startSpawn2) {
			spawnEnemy2();
		}
		if (startSpawn3) {
			spawnEnemy3();
		}

	}

	private void spawnEnemy3() {
		if (canSpawnEnemy3) {
			Enemy en;
			en = new Enemy3(random.nextInt(Game.WIDTH - 30), -20, 16, 16, null);
			Game.enemies.add(en);
			waitTimeEnemy3 = System.currentTimeMillis();
			canSpawnEnemy3 = false;
		} else {
			if (System.currentTimeMillis() - waitTimeEnemy3 >= spawnTimeEnemy3) {
				canSpawnEnemy3 = true;
			}
		}

	}

	private void spawnEnemy2() {
		if (canSpawnEnemy2) {
			Enemy en;
			en = new Enemy2(random.nextInt(Game.WIDTH - 30), -20, 16, 16, null);
			Game.enemies.add(en);
			waitTimeEnemy2 = System.currentTimeMillis();
			canSpawnEnemy2 = false;
		} else {
			if (System.currentTimeMillis() - waitTimeEnemy2 >= spawnTimeEnemy2) {
				canSpawnEnemy2 = true;
			}
		}

	}

	private void spawnEnemy1() {
		if (canSpawnEnemy1) {
			Enemy en;
			en = new Enemy(random.nextInt(Game.WIDTH - 30), -20, 16, 16, null);
			Game.enemies.add(en);
			waitTimeEnemy1 = System.currentTimeMillis();
			canSpawnEnemy1 = false;
		} else {
			if (System.currentTimeMillis() - waitTimeEnemy1 >= spawnTimeEnemy1) {
				canSpawnEnemy1 = true;
			}
		}

	}

}
