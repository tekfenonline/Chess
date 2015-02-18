package OYMACI;

import java.util.Scanner;


public class ChessTest
{
	public static void main(String[] args)
	{
		new Frame();
	}
	
	
	void test()
	{
		Chess game = new Chess();
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
		String color = "Black";

		/*
		 * System.out.println("Choose a color(W/B)> "); color=input.next();
		 */
		while(true)
		{
			for (int i = 0; i < 30; i++)
				System.out.println();
			System.out.println("Turn:" + color);
			game.displayOnConsole();
			System.out.println("Enter command(row,col): ");
			int sourceX=input.nextInt()-1;
			int sourceY=input.nextInt()-1;
			int aimX=input.nextInt()-1;
			int aimY=input.nextInt()-1;
			if(sourceX<0 || sourceX>=8 || sourceY<0 || sourceY>=8 || aimX<0 || aimX>=8 || aimY<0 || aimY>=8)
			{
				System.out.println("Out of range!");
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			if(color.contentEquals("White"))
			{
				sourceX=7-sourceX;
				aimX=7-aimX;
				game.reverseBoard();
			}
			if(game.play(color,sourceX,sourceY,aimX,aimY)==null)
			{
				if(color.contentEquals("White"))
					game.reverseBoard();
				continue;
			}
			if(color.contentEquals("Black"))
				color="White";
			else
			{
				color="Black";
				game.reverseBoard();
			}
		}
	}
}
