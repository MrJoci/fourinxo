package jocit.software;

import java.util.Arrays;

public class StartNewFiveInRow extends StartNewGame{
	int jatekMod = 5;

	public int gyoztes(){
		int jatekMod = 5;
		int em = 0;
		for (int r=0; r<nyomva.length; r++) {
		    for (int c=0; c<nyomva[r].length; c++) {
		    	int sor[] = new int[jatekMod];
		    	
		    	if (nyomva[r].length > c+jatekMod-1) {
		    		for (int i = 0; i < jatekMod; i++) {
						sor[i] = nyomva[r][c+i];
					}
		    		Arrays.sort(sor);
		    		if(sor[0] == sor[sor.length-1] && sor[0] != 0) return sor[0];
		    	}

		    	if (nyomva.length > r+jatekMod-1) {
		    		for (int i = 0; i < jatekMod; i++) {
						sor[i] = nyomva[r+i][c];
					}
		    		Arrays.sort(sor);
		    		if(sor[0] == sor[sor.length-1] && sor[0] != 0) return sor[0];
		    	}

		    	if (nyomva.length > r+jatekMod-1 && nyomva[r].length > c+jatekMod-1) {
		    		for (int i = 0; i < jatekMod; i++) {
						sor[i] = nyomva[r+i][c+i];
					}
		    		Arrays.sort(sor);
		    		if(sor[0] == sor[sor.length-1] && sor[0] != 0) return sor[0];
		    	}	

		    	if (nyomva.length > r+jatekMod-1 && c-jatekMod+1 > -1) {
		    		for (int i = 0; i < jatekMod; i++) {
						sor[i] = nyomva[r+i][c-i];
					}
		    		Arrays.sort(sor);
		    		if(sor[0] == sor[sor.length-1] && sor[0] != 0) return sor[0];
				}
		    }
		}
		return 0;
	}

	public String getJatekMod(){
		return "Five in row";
	}	

	
}
