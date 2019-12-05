package mainClass;

///////////////////GAME RUNNER. USED TO EXECUTE CLASS////////////////////////

import java.io.FileNotFoundException;
import java.util.Scanner;

public class GameLoop {
	
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(System.in);
		World dungeon = new World(World.DUNGEON);
		while(dungeon.player.isAlive) //runs so long as player is alive (or until we press 'Q')
		{
		String input = scanner.nextLine();
		dungeon.playerMovementEvent(input.toLowerCase());
		System.out.println();
		}
		
		scanner.close(); //closes scanner. 
	

}
}
