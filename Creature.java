package mainClass;

import java.util.Random;

//CREATURE CLASS////////////////////////////////////////////////////////////////
//Ignore this for grading. This was a fun little 'side quest' to see if I could make enemies. They only stand still, but they do damage. 

public class Creature {
	
	protected int health; //health
	private int maxHealth; // total health. Can't go over this. is redundant. 
	protected int damage; //our base damage 
	protected int armor; // our armor. How much damage can we absorb. 
	private boolean isAlive; //is useless for now. 
	protected int row; //the creature's row
	protected int col; //the creature's column. 
	protected int identification; //ah yes. THIS value. It gets determined when we loop through creatures
	//which means every "identification" is different. It was for testing purposes. 
	
	public Creature(int health, int maxHealth,int damage, int armor, boolean isAlive, int row, int col, int identification)
	{
		this.health = health;
		this.maxHealth = maxHealth;
		this.damage = damage;
		this.armor = armor;
		this.isAlive = isAlive;
		this.row = row;
		this.col = col;
		this.identification = identification;
		
	}
	
	public static int statRoll(int min, int max)
	//static method to determine Creature Stats based on a min and max value. 
	//utilizes Java's util.Random method. 
	
	{
		Random r = new Random();
		int result = r.nextInt(max-min) + min;
		return result; 
	}
	
	
	
}