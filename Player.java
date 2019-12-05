//FINAL PROJECT: A ROGUELIKE RPG////////////////////////////
//DEVIN FOSTER

public class Player 
{
	
	protected int health; //health
	private int maxHealth; // total health. Can't go over this
	protected int exp; 
	protected int damage; //our base damage 
	protected int armor; // our armor. How much damage can we absorb. 
	private static final int HEAL_AMOUNT = 20;
	boolean isAlive = true; //this determines the game loop. 
	
	public Player(int health, int exp, int damage, int armor)
	{
		
		this.maxHealth = 1000; //ultimate range of 1000. 
		
		if(health > maxHealth)
		{
			this.health = maxHealth; 
		}
		
		else if (health <= 0)
		{
			this.health = 1; //can't be zero, because we die if it is. 
		}
		else
		{
			this.health = health; 
		}
		
		if(damage < 0)
		{
			this.damage  = 1; //if neither value works, then damage is just 1
		}
		else if(damage > 100)
		{
			this.damage = 100; //if it's above 100, then we turn it to 100. 
		}
		else
		{
			this.damage = damage;
		}
		//that's a LOTTA DAMAGE. 
		//this is the 'strength' variable from the directions. 
		
		if(armor < 0)
		{
			this.armor = 0; //makes armor at least 0. 
		}
		
		else if(armor > 100)
		{
			this.armor = 100; // makes armor 100 if it's above 100 at any time. 
		}
		
		else 
		{
			this.armor = armor;
		}
		
		
		if(exp < 0)
		{
			this.exp = 0;  //we can't go below 0. 
		}
		
		else if(exp > 1000)
		{
			this.exp = 1000; // we can't go above 1000. 
		}
		

		
	}
	
	public void healEvent(Player player) //this heals player with potion. 
	{
		if(player.health <= player.maxHealth - HEAL_AMOUNT)
		player.health += HEAL_AMOUNT;
		
		else if(player.health + HEAL_AMOUNT > player.maxHealth)
		{
			player.health = player.maxHealth;
		}
		
		System.out.println("You've been Healed! Current Health: " + health);
	
	
}
	
	public void dealDamage(Player player, int damage) //deals damage to test health. kills player if too much damage occurs. 
	{
		health = this.health - damage; 
		if(health <= 0)
		{
			isAlive = false; 
		}
	}
	
	public void gainExp(Player player, int xpToGain) //keeps the player from gaining too much exp. 
	{
		exp = this.exp + xpToGain; 
		if(exp > 1000)
		{
			exp = 1000;
		}
		if(exp < 0)
		{
			exp = 0; 
		}
	}
	
	public void getSomeArmor(Player player, int armorToGain) //keeps the player from gaining TOO MUCH armor
	{
		armor = this.armor + armorToGain; 
		if(armor > 100)
			armor = 100; 
		if(armor < 0)
			armor = 0; 
	}
	
	
}
