package jocit.software;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;


public class StartNewGame extends Activity implements OnTouchListener{
	FastRenderView renderNezet;
	boolean nyom = false;
	float x = 0;
	float y = 0;
	float szm = 50;
	int szegX = 8;
	int szegY = 12;
	int eltolas = 87; //eltolasY
	int eltolasX = 8;
	int jatekos = 1;
	int[] jatekosSzin = {0x00000000, 0xAA00FF00, 0xAA0000FF};
	int jatekosElsoSzin = 0xAA00FF00;
	int jatekosMasodSzin = 0xAA0000FF;
	int[][] nyomva = new int[8][13];
	int nyert = 0;
	int[] lastRakas = new int[3];
	
	float width_multiplier = 1f;
	float height_multiplier = 1f;	
	
	float screen_width = 0;
	float screen_height = 0;	
	
	public String getJatekMod(){
		return "Four in row";
	}

	
	public void onCreate(Bundle mentettAllapot){
		super.onCreate(mentettAllapot);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		renderNezet = new FastRenderView(this);
		renderNezet.setOnTouchListener(this);
		tombStart();
		setContentView(renderNezet);
	}
	
	public void tombStart(){
		for (int i = 0; i < szegX; i++) {
			for (int j = 0; j < szegY; j++) {
				nyomva[i][j] = 0;
			}
		}
	}
	
	protected void onResume() {
		super.onResume();
		renderNezet.resume();
	}
	
	protected void onPause() {
		super.onPause();
		renderNezet.pause();
	}
	
	class FastRenderView extends SurfaceView implements Runnable{
		Thread renderSzal = null;
		SurfaceHolder feluletTarto;
		Context kontext;
		volatile boolean fut = false;
		
		public FastRenderView(Context kon){
			super(kon);
			kontext = kon;
			feluletTarto = getHolder();
		}
		
		public Canvas halo(Canvas kanvas, Paint paint){
			paint.setColor(0xFF888888);
			for (float i = 0; i < szegX+1; i++) {
				kanvas.drawLine(i*szm, 0+eltolas, i*szm, szegY*szm+eltolas, paint);
			}
			kanvas.drawLine(479, 0+eltolas, 479, szegY*szm+eltolas, paint);
			for (float j = 0; j < szegY+1; j++) {
				kanvas.drawLine(0, j*szm+eltolas, szegX*szm, j*szm+eltolas, paint);				
			}
			return kanvas;
		}
				
		public Canvas negyzet(Canvas kanvas, Paint paint){
			for (float i = 0; i < szegX; i++) {
				for (float j = 0; j < szegY; j++) {
					if (nyomva[(int)i][(int)j]>0) {
						paint.setColor(jatekosSzin[ nyomva[(int)i][(int)j] ]);
						kanvas.drawRect(i*szm+2, j*szm+eltolas+2, i*szm+szm-1, j*szm+szm+eltolas-1, paint);								
					}
				}
			}					
			return kanvas;
		}
		
		public void negyzetKep(Canvas kanvas, Paint paint, Bitmap korJel[], Bitmap xJel[]){
			float kepszelx = 45f*width_multiplier + 15f*width_multiplier;
			float kepszely = 45f*height_multiplier + 14.5f*height_multiplier;
			float ikonsizeX = 50f*width_multiplier;
			float ikonsizeY = 50f*height_multiplier;
			
			float relEltolasX = (float)eltolasX;//*width_multiplier;
			float relEltolasY = (float)eltolas;//*height_multiplier;
			
			
			for (float i = 0; i < szegX; i++) {
				for (float j = 0; j < szegY; j++) {
					
//					float xcord = i*xm+7;
//					float ycord = j*szm+eltolas+7;
//					Rect csempe = new Rect();
//					csempe.set((int)xcord, (int)ycord, (int)(xcord+kepszelx), (int)(ycord+kepszely));
//					
//					if (nyomva[(int)i][(int)j]>0) {
//						if (nyomva[(int)i][(int)j]==1) {
//							//kanvas.drawBitmap(xJel[1], i*xm+7, j*szm+eltolas+7, paint);
//							kanvas.drawBitmap(xJel[1], null, csempe, null);		
//						}
//						else {
//							//kanvas.drawBitmap(korJel[1], i*xm+7, j*szm+eltolas+7, paint);	
//							kanvas.drawBitmap(korJel[1], null, csempe, null);	
//						}
//					}
					float xcord = (float)i*kepszelx+relEltolasX;
					float ycord = (float)j*kepszely+relEltolasY;
					Rect csempe = new Rect();
					csempe.set((int)xcord, (int)ycord, (int)(xcord+ikonsizeX), (int)(ycord+ikonsizeY));
					
					if (nyomva[(int)i][(int)j]>0) {
						if (nyomva[(int)i][(int)j]==1) {
							//kanvas.drawBitmap(xJel[1], i*xm+7, j*szm+eltolas+7, paint);
							kanvas.drawBitmap(xJel[1], null, csempe, null);		
						}
						else {
							//kanvas.drawBitmap(korJel[1], i*xm+7, j*szm+eltolas+7, paint);	
							kanvas.drawBitmap(korJel[1], null, csempe, null);	
						}
					}					
					
					
				}
			}
			if (nyomva.length > 0){
				float xcord = (float)lastRakas[0]*kepszelx+relEltolasX;
				float ycord = (float)lastRakas[1]*kepszely+relEltolasY;
				Rect csempe = new Rect();
				csempe.set((int)xcord, (int)ycord, (int)(xcord+ikonsizeX), (int)(ycord+ikonsizeY));
				if(lastRakas[2] == 1){
					kanvas.drawBitmap(xJel[0], null, csempe, null);					
				}
				else{	
					kanvas.drawBitmap(korJel[0], null, csempe, null);		
				}			
			}
//			float xcord = lastRakas[0]*szm+7;
//			float ycord = lastRakas[1]*szm+eltolas+7;
//			Rect csempe = new Rect();
//			csempe.set((int)xcord, (int)ycord, (int)(xcord+kepszelx), (int)(ycord+kepszely));
//			if(lastRakas[2] == 1){
//				kanvas.drawBitmap(xJel[0], null, csempe, null);					
//			}
//			else{
//				//kanvas.drawBitmap(korJel[0], xcord, ycord, paint);	
//				kanvas.drawBitmap(korJel[0], null, csempe, null);		
//			}
		}
		
		
		public void run() {
			while(fut) {
				if (!feluletTarto.getSurface().isValid()) continue;
				
				try {
					Bitmap lap;
					int jelekSzama = 2;
					Bitmap korJel[] = new Bitmap[jelekSzama];
					Bitmap xJel[] = new Bitmap[jelekSzama];
					Canvas kanvas = feluletTarto.lockCanvas();
					
					if (screen_width==0){
						screen_width = kanvas.getWidth();
						screen_height = kanvas.getHeight();
						
						width_multiplier = screen_width/480f;
						height_multiplier = screen_height/800f;
						
						eltolasX = (int)((float)eltolasX * width_multiplier);
						eltolas  = (int)((float)eltolas  * height_multiplier);	
						
					}	
					
					int textSize = (int)(40f * height_multiplier);
					int szovegHely = (int)(60f * height_multiplier);
					
					int kijonX = (int)(180f * height_multiplier);
					int kijonY = (int)(23f * width_multiplier);					
					
					AssetManager kepTolto = kontext.getAssets();
					InputStream inputStream = kepTolto.open("lap.jpg");
					lap = BitmapFactory.decodeStream(inputStream);
					inputStream.close();

					for (int i = 0; i < jelekSzama; i++) {
						InputStream inputX = kepTolto.open("x/x_" + Integer.toString(i) + ".jpg");
						InputStream inputKor = kepTolto.open("k/k_" + Integer.toString(i) + ".jpg");

						xJel[i] = BitmapFactory.decodeStream(inputX);
						korJel[i] = BitmapFactory.decodeStream(inputKor);

						inputX.close();
						inputKor.close();
					}
					
					kanvas.drawRGB(255, 255, 255);
					Paint paint = new Paint();
					Rect dst = new Rect();
					dst.set(0, 3, (int)screen_width, (int)screen_height-3);
//					kanvas.drawBitmap(lap, 0, 3, paint);
					kanvas.drawBitmap(lap, null, dst, null);
//					kanvas = halo(kanvas, paint);
//					kanvas = negyzet(kanvas, paint);
					negyzetKep(kanvas, paint, korJel, xJel);
	
					paint.setColor(0xFF000000);
					paint.setTextSize(textSize);
					paint.setTextAlign(Paint.Align.LEFT);
					kanvas.drawText(getJatekMod(), 240, szovegHely, paint);
	
					paint.setTextSize(textSize);
					paint.setTextAlign(Paint.Align.LEFT);
					kanvas.drawText("Player:", 10, szovegHely, paint);
					
					
					float ikonsizeX = 50f*width_multiplier;
					float ikonsizeY = 50f*height_multiplier;					
					Rect csempe = new Rect();
					csempe.set(kijonX, kijonY, kijonX+(int)ikonsizeX, kijonY+(int)ikonsizeY);
					
					if (jatekos==1) {
						kanvas.drawBitmap(xJel[1], null, csempe, null);	
					}
					else {
						kanvas.drawBitmap(korJel[1], null, csempe, null);							
					}
					
					if(nyert>0){
						paint.setColor(0xFF000000);
						kanvas.drawRect(0, 0, (int)(480f * width_multiplier), (int)(80f  * height_multiplier), paint);								
	
						paint.setColor(0xFFFFFFFF);
						paint.setTextSize(textSize);
						paint.setTextAlign(Paint.Align.LEFT);
						kanvas.drawText("Winner:", 10, szovegHely, paint);					
						
						if (nyert==1) {
							kanvas.drawBitmap(xJel[1], null, csempe, null);	
						}
						else {
							kanvas.drawBitmap(korJel[1], null, csempe, null);							
						}					
						
					}
					
					feluletTarto.unlockCanvasAndPost(kanvas);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void resume() {
			fut = true;
			renderSzal = new Thread(this);
			renderSzal.start();
		}
		
		public void pause() {
			fut = false;
			while (renderSzal.isAlive()) {
				try {
					Log.d("szaljoin", "proba");
					renderSzal.join();
				} catch (InterruptedException e) {
					Log.d("szaljoin", "nem megy");
				}
				
			}
		}
	}
	public boolean onTouch(View v, MotionEvent muvelet){
		if(nyert>0) return true;
		switch (muvelet.getAction()){
		case MotionEvent.ACTION_DOWN:
			nyom = true;
			x = muvelet.getX();
			y = muvelet.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_UP:
			nyom = false;
			break;
		}
		setNegyzet();
		nyert = gyoztes();
		return true;
	}

	
	public void setNegyzet(){
		float xCount = 8;
		float yCount = 12;
		
		float CellWidth = screen_width/xCount;
		float CellHeight = (screen_height-(float)eltolas)/yCount;
		
		for (float i = 0; i < szegX; i++) {
			for (float j = 0; j < szegY; j++) {
				if (i*CellWidth < x && x < (i+1f)*CellWidth) {
					if (j*CellHeight+eltolas < y && y < (j+1)*CellHeight+eltolas) {
						if(nyomva[(int)i][(int)j]>0) continue;
						nyomva[(int)i][(int)j] = jatekos;
						lastRakas[0]=(int)i;
						lastRakas[1]=(int)j;
						lastRakas[2]=jatekos;
						if(jatekos==1){
							jatekos=2;
						}
						else{
							jatekos=1;
						}
					}						
				}
			}
		}
	}
	
	
	public int gyoztes(){
		int jatekMod = 4;
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
	
	
}
