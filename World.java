package mainClass;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import mainClass.Player;

import java.util.ArrayList;


//FINAL PROJECT: WORLD/////////////////////////////////////////////////////////////////////////
/*
 * This is where the magic happens. This file controls everything that happens within the game, scans a file, 
 * creates an instance of player, etc. 
 */

public class World 
{
	protected char[][] map; //this is the map... which is formed after we scan the map.txt file
	protected char[][] playerLocation; // this is where the player is. it will correspond with the map file
	private int playerLocationVert = 0; //playerlocations help us determine where the player is
	private int playerLocationHori = 0;
	
	private ArrayList<Creature> creatures = new ArrayList<Creature>(); //why an array list? So I can add whenever I want and don't have to 
	//deal with array constraint.
	
	public static final File DUNGEON = new File("map.txt"); //the file we read
	//it will always read from a file called "map.txt", so as far as I'm concerned-- it's a constant. 
	
	private static final char PLAYER = 'P'; //symbols on the map describing different things
	private static final char POTION = 'H';
	private static final char ENEMY = '!';
	private static final char EMPTY_FLOOR = '.';
	private static final char WALL = '#';
	private static final char ILLEGAL_LOCATION = ' ';
	
	//buttons we press to determine where the player moves. 
	protected static final String LEFT = "a";
	protected static final String RIGHT = "d";
	protected static final String UP = "w";
	protected static final String DOWN = "s";
	protected static final String QUIT = "q";
	
	//both of these classes are declared with the purpose of giving the map a player and creature. 
	//these, luckily, don't change, so it's fine.
	public Player player = new Player(20, 1, 5, 10);
	//we create an instance of player here. This means the player is dependent on World... which is fine. 
	
	
	
	public World(File file) throws FileNotFoundException
	{
		int creatureCount = 0;
		int lineCount = 0;
		Scanner scanner = new Scanner(file);
		
		//The world file doesn't get unintentionally edited here. If we wanted to add the capability for a 
		//'load game' option, we totally could, but we aren't, so I won't. 
		
		
		while(scanner.hasNextLine())
		{
			lineCount++; // counts the lines to give us a row value 
			if(scanner.nextLine() == "")
			{
				break; // stops when the map is blank.
			}
			
		}
		
		scanner.close(); //close the scanner, because we're done with it
		
		Scanner newScanner = new Scanner(file); //now we need to scan the file again, from the top. 
		
		int maxWidth = 0; //this will account for our the LONGEST length of any given line, to give us a column value
		for(int i = 0; i < lineCount; i++)
		{
			int nextWidth = newScanner.nextLine().length(); 
			
			if(nextWidth > maxWidth)
			{
				maxWidth = nextWidth; //the max width will only be the longest line 
			}
		}
		newScanner.close(); //close this scanner, because I'm done with it. 
		
		map = new char[lineCount][maxWidth]; // sets our map array equal to the height * largest width. 
		Scanner finalScanner = new Scanner(file); //one last scanner. 
		
		for(int i = 0; i < lineCount; i++)
		{
			
			//this entire code looks very convoluted, but it's actually my proudest accomplishment in this game,
			//because it accounts for UNEVEN arrays. The basic principle of the read code is to find an array and
			//bring it to life. The big issue is that rarely are game maps equidistant. 
			//this code 'fills in the gaps' for the game map, meaning that, while the end result is not a traditional
			// ragged array, i can simulate it. Any extra space (the area which otherwise would have have been an 
			// outofbound exception), will be filled with an illegal space. 
			char[] temp = finalScanner.nextLine().toCharArray();
			
			if(temp.length < maxWidth)
			{
				char[] newtemp = new char[maxWidth];
				
				for(int j = 0; j < newtemp.length; j++)
				{
					if(j < temp.length)
					newtemp[j] = temp[j];
					
					else
						newtemp[j] = ILLEGAL_LOCATION; // we fill it with a blank space ' '. 
					
				}
				
				temp = newtemp;
			}
		
			for(int j = 0; j < maxWidth; j++) 
			{
				map[i][j] = temp[j];
				
			}
		}
		finalScanner.close(); //done with this now, which means no more scanning needed. 
		
		playerLocation = new char[lineCount][maxWidth]; //the player location is a separate array. We can account for
		//the player's location while on the map. 
		//possible creature's location(s) on the map. 
		
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				if(map[i][j] == PLAYER)
				{
					playerLocation[i][j] = map[i][j];
					playerLocationHori = j;
					playerLocationVert = i;
					
					
				}
				else if(map[i][j] == ENEMY)
				{
					int creatureMaxHealth = Creature.statRoll(10, 35); // we give the creatures random stats
					int creatureArmor = Creature.statRoll(1, 4); //this makes encounters much less predictable
					int creatureDamage = Creature.statRoll(5, 10); //also makes getting potions necessary. 
					
					
					creatures.add(new Creature(creatureMaxHealth, creatureMaxHealth, creatureDamage, creatureArmor, true, i, j, creatureCount));
				}
			}
			
			
		}
		
	}
		
	
	static void quitEvent(int exitStatus) //activates when player presses Q.... will terminate the game. 
	{
		if(exitStatus == 0)
			System.exit(exitStatus); //terminates the JVM. Is it safe? probably???
		else
			{
				System.out.println("you shouldn't be seeing this");
				// this should never activate. this was a test to make sure the 
				//method worked. 
			}
		
		
	}
	
	//this entire code is repetetive, so all you need to know is:
	//the method reads a string input, and then, if it's a constant, moves the player accordingly...
	//if the player moves into the wall, then they'll not move at all and a message will display
	// if they hit a potion, the potion is 'consumed' (not really, though)
	//and the player gets 20 HP.
	// if the player presses a wrong button, then nothing happens at all, and they
	// can continue to play and press more buttons
	//if a player hits a '!', then a combatEvent ensues. 
	//if the player hits 'q', then the game ends. 
	public void playerMovementEvent(String input)
	{
		if(input.equalsIgnoreCase(LEFT)) 
		{
			
			if(map[playerLocationVert][playerLocationHori - 1] == WALL)
			{
				System.out.println("You can't move into a wall!");
				
			}
			
			else if(map[playerLocationVert][playerLocationHori - 1] == POTION)
			{
				player.healEvent(player);
				map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
				playerLocationHori -=1;
				map[playerLocationVert][playerLocationHori] = PLAYER;
				updateMapEvent(LEFT);
			}
			else if(map[playerLocationVert][playerLocationHori - 1] == ENEMY)
			{
				for(int i = 0; i < creatures.size(); i++)
				{
					if(creatures.get(i).row == playerLocationVert && creatures.get(i).col == playerLocationHori - 1)
					{
						combatEvent(player, creatures.get(i));
					
					if(creatures.get(i).health <= 0)
					{
						map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
						playerLocationHori -=1;
						map[playerLocationVert][playerLocationHori] = PLAYER;
						updateMapEvent(LEFT);
						
						//YOU HAVE NO IDEA HOW HARD IT WAS FOR ME TO MAKE THE CREATURE CODE WORK RIGHT
					}
					}
				}
				
			}
			else 
			{
			map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
			playerLocationHori -=1;
			map[playerLocationVert][playerLocationHori] = PLAYER;
			updateMapEvent(LEFT);
			}
		}
		else if(input.equalsIgnoreCase(RIGHT))
		{
			if(map[playerLocationVert][playerLocationHori+1] == WALL)
			{
				System.out.println("You can't move into a wall!");
				
			}
			
			else if(map[playerLocationVert][playerLocationHori + 1] == POTION)
			{
				player.healEvent(player);
				map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
				playerLocationHori +=1;
				map[playerLocationVert][playerLocationHori] = PLAYER;
				updateMapEvent(RIGHT);
			}
		
			
			else if(map[playerLocationVert][playerLocationHori + 1] == ENEMY)
			{
				for(int i = 0; i < creatures.size(); i++)
				{
					if(creatures.get(i).row == playerLocationVert && creatures.get(i).col == playerLocationHori + 1)
					{
						combatEvent(player, creatures.get(i));
					
					if(creatures.get(i).health <= 0)
					{
						map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
						playerLocationHori +=1;
						map[playerLocationVert][playerLocationHori] = PLAYER;
						updateMapEvent(RIGHT);
						
						//YOU HAVE NO IDEA HOW HARD IT WAS FOR ME TO MAKE THE CREATURE CODE WORK RIGHT
					}
					}
				}
				
			}
			else 
			{
			map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
			playerLocationHori +=1;
			map[playerLocationVert][playerLocationHori] = PLAYER;
			updateMapEvent(RIGHT);
			}
			}
			
		
		
		else if(input.equalsIgnoreCase(UP))
		{
			if(map[playerLocationVert - 1][playerLocationHori] == WALL)
			{
				System.out.println("You can't move into a wall!");
			}
			else if(map[playerLocationVert -1][playerLocationHori] == POTION)
			{
				player.healEvent(player);
				map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
				playerLocationVert -=1;
				map[playerLocationVert][playerLocationHori] = PLAYER;
				updateMapEvent(UP);
			}
		
			
			else if(map[playerLocationVert - 1 ][playerLocationHori] == ENEMY)
			{
				for(int i = 0; i < creatures.size(); i++)
				{
					if(creatures.get(i).row == playerLocationVert - 1 && creatures.get(i).col == playerLocationHori)
					{
						combatEvent(player, creatures.get(i));
					
					if(creatures.get(i).health <= 0)
					{
						map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
						playerLocationVert -= 1;
						map[playerLocationVert][playerLocationHori] = PLAYER;
						updateMapEvent(UP);
						
						//YOU HAVE NO IDEA HOW HARD IT WAS FOR ME TO MAKE THE CREATURE CODE WORK RIGHT
					}
					}
				}
				
			}
			
			
			else 
			{
			map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
			playerLocationVert -= 1;
			map[playerLocationVert][playerLocationHori] = PLAYER;
			updateMapEvent(UP);
			}
		}
		
		else if(input.equalsIgnoreCase(DOWN))
		{
			if(map[playerLocationVert+1][playerLocationHori] == WALL)
			{
				System.out.println("You can't move into a wall!");
				
			}
			
			else if(map[playerLocationVert + 1][playerLocationHori] == POTION)
			{
				player.healEvent(player);
				map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
				playerLocationVert +=1;
				map[playerLocationVert][playerLocationHori] = PLAYER;
				updateMapEvent(DOWN);
			}
		
			
			else if(map[playerLocationVert + 1 ][playerLocationHori] == ENEMY)
			{
				for(int i = 0; i < creatures.size(); i++)
				{
					if(creatures.get(i).row == playerLocationVert + 1 && creatures.get(i).col == playerLocationHori)
					{
						combatEvent(player, creatures.get(i));
					
					if(creatures.get(i).health <= 0)
					{
						map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
						playerLocationVert += 1;
						map[playerLocationVert][playerLocationHori] = PLAYER;
						updateMapEvent(DOWN);
						
						//YOU HAVE NO IDEA HOW HARD IT WAS FOR ME TO MAKE THE CREATURE CODE WORK RIGHT
					}
					}
				}
				
			}
			
			else 
			{
			map[playerLocationVert][playerLocationHori] = EMPTY_FLOOR;
			playerLocationVert +=1;
			map[playerLocationVert][playerLocationHori] = PLAYER;
			updateMapEvent(DOWN);
			}
		}
		
		else if(input.equalsIgnoreCase(QUIT))
		{
			System.out.println("Quitting Game...");
			quitEvent(0);
		}
		}
		
	
	public void updateMapEvent(String direction)
	{
		for(int i = 0; i < playerLocation.length; i++)
		{
			for(int j = 0; j < playerLocation[i].length; j++)
			{
			}
			
			// this method simply prints the map after a movement occurs. 
			//we actually don't NEED to put in a direction for an argument, but It was a way to keep things organized for me. 
			
			System.out.println(map[i]);
		}
		multSpace(); 
	}
	
	//a function that prints 5 lines. SO USEFUL. 
	public static void multSpace()
	{
		for(int i = 0; i < 5; i++)
		{
			System.out.println();
		}
	}
	
	
	public void combatEvent(Player player, Creature creature) //what to do if a fight breaks out
	{
		int InflictedDamage = 0; //damage player does
		int TakenDamage = 0; // damage player takes
		
		TakenDamage = creature.damage - player.armor; //damage player takes is creature damage - armor
		
		if(TakenDamage < 0)
		{
			TakenDamage = 0; //if damage is somehow less less than armor. Keeps from player 'healing'. 
		}
		
		
		
		InflictedDamage =  player.damage - creature.armor;
		player.health -= TakenDamage;
		
		creature.health -= InflictedDamage;
		
		System.out.println("A fight ensues! You take " + TakenDamage + " damage and deal " +  InflictedDamage + " damage");
		System.out.println("Your Health: " + player.health);
		System.out.println("Enemy Health: " + creature.health);
		
		if(player.health <= 0)
		{
			System.out.println("You've Died...");
			World.quitEvent(0);
			//if the player's health runs out, then they die and the game ends. 
		}
	
	}
}


