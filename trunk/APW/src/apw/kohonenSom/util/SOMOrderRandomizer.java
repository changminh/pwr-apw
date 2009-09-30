package apw.kohonenSom.util;

import java.util.ArrayList;
import java.util.Random;

public class SOMOrderRandomizer {
	
	private Random rand;
	
	public SOMOrderRandomizer()
	{
		rand = new Random();
	}
	
	public ArrayList<Integer> randomizeOrder(int l)
	{
		ArrayList<Integer> order = new ArrayList<Integer>();
		
		for(int i=0; i<l; i++)
		{
			int num;
			do{
				num = rand.nextInt(l);
			}while (order.contains(num));
			order.add(num);
		}
		
		return order;
	}
}
