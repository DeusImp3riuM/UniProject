package aaronshort.android.uniproject;

import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.graphics.*;

public class GameView extends View {
	
	int mapSpeed = 3;
	
	
	Random rand = new Random();
	String[][] WepTypes={
			{"LongSword","Rapier","TwoHanded","Dagger","Club","Axe","TwoHandedAxe","Scythe","DualScythe"},
			{"Orb","Book","Staff","Rod","Sword","Scythe"},
			{"LongBow","ShortBow","Crossbow","Sniper","Pistol","Energy","Throwing"}
	};
	String[][] ClassTypes = {
			{"Swordsman","LightSword","Warrior","Sentinal","Assassin","Duelist","Thief","Beserker","Valkyrie","ScytheWielder","Mutant","Paladin","Brute"},
			{"Mage","LightMage","DarkMage","GreyMage","Healer","HolyMage","Sorcerer","CursedMage","BloodMage","Necromancer","Geomancer","WarriorMage","BioMorphMage"},
			{"Ranger","Marksman","LightArcher","DarkArcher","PiercingArcher","SharpShooter","Techmaturgist","Hunter","Frost","DaggerThrower","CorruptedArcher","NightArcher","StealthArcher"}
			};
	HashMap<String, String[]> GetClass = new HashMap<String,String[]>();
	Paint red = new Paint();
	Paint blue = new Paint();
	Paint black = new Paint();
	boolean load = false;
	boolean touch = false;
	int screenW,screenH;
	WindowManager wm;
	int xOffset,yOffset;
	int touchX,touchY;
	final int sdk = android.os.Build.VERSION.SDK_INT;
	
	public GameView(Context context) {
		super(context);
		red.setColor(Color.RED);
		blue.setColor(Color.BLUE);
		black.setColor(Color.BLACK);
		black.setTextSize(20);
		wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if(Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13){
			screenW = display.getWidth();
			screenH = display.getHeight();
		}
		else{
			Point size = new Point();
			display.getSize(size);
			screenW = size.x;
			screenH = size.y;
		}
		xOffset = 0;
		yOffset = 0;
		
		
	}
	public String[] SetMap(String one, String two, String three){
		String[] Temp = new String[3];
		Temp[0] = one;
		Temp[1] = two;
		Temp[2] = three;
		return Temp;
	}
	
	String Print;
	String[] recieve = new String[3];
	MoveBar move1 = new MoveBar(0,1000,300,10,100,200,40);
	Weapon testWep = new Weapon("TestWeapon", "rand", "rand",10, 0, 50, 10, 40, 40, 32,"None", "None", "None", "None");
	Map map = new Map();
	Controls controls = new Controls();
	PlayerCharacter player = new PlayerCharacter();
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.translate(xOffset, yOffset);
		if(load==false){load=true;}
		/*
		 * recieve = GetClass.get("Base");
		 * Print = Arrays.toString(recieve);
		 * canvas.drawText(Print,100,100,black)
		 */
		/*move1.Draw(canvas);
		move1.Update();*/
		
		/*canvas.drawText(""+testWep.getName(), 100, 100, black);
		canvas.drawText(""+testWep.getHolder(), 100, 130, black);
		canvas.drawText(""+testWep.getType(), 100, 160, black);
		canvas.drawText(""+testWep.getRarity(), 100, 190, black);
		canvas.drawText(""+testWep.getDamage(), 100, 210, black);
		canvas.drawText(""+testWep.getSpellPower(), 100, 240, black);
		canvas.drawText(""+testWep.getSpeed(), 100, 270, black);
		canvas.drawText(""+testWep.getAttackSpeed(), 100, 300, black);
		canvas.drawText(""+testWep.getDefence(), 100, 330, black);
		canvas.drawText(""+testWep.getMagicDefence(), 100, 360, black);*/
		map.setCurrentMap("Level2");
		map.drawMap(canvas);
		controls.drawButtons(canvas);
		player.draw(canvas);
		if(touch == true){controls.getPress(touchX, touchY);}
		invalidate();
	}
	public boolean onTouchEvent(MotionEvent e){
		float xx = e.getX();
		float yy = e.getY();
		touch = true;
		switch(e.getAction()){
			case MotionEvent.ACTION_DOWN:
				touch = true;
				touchY = (int) yy;
				touchX = (int) xx;
			case MotionEvent.ACTION_UP:
				touch = false;
				break;
			case MotionEvent.ACTION_MOVE:
				touch = true;
				touchY = (int) yy;
				touchX = (int) xx;
				break;
			default:
				break;
		}
		return true;
	}
	public int Perc(int current,int max){
		float Value = 0;
		Value = (float)current/(float)max;
		Value = Value*100;
		return (int)Value;
	}
	public Paint getPaint(int tile){
		Paint paint = new Paint();
		switch(tile){
			case 0:
				paint.setColor(Color.rgb(255,0,0));
				break;
			case 1:
				paint.setColor(Color.rgb(100,100,100));
				break;
			case 2:
				paint.setColor(Color.rgb(0,255,0));
				break;
			case 3:
				paint.setColor(Color.rgb(0,0,255));
				break;
			default:
				paint.setColor(Color.rgb(0,0,0));
				break;
		}
		return paint;
	}
	/*-------------------------------------------*/
	public Bitmap combineTiles(int[][] tiles, int tileSize){
		Bitmap bit = null;
		int height = tiles.length;
		int width = 0;
		for(int y=0;y<height;y++){
			if(tiles[y].length > width){
				width = tiles[y].length;
			}
		}
		int cWidth = width*tileSize;
		int cHeight = height*tileSize;
		bit = Bitmap.createBitmap(cWidth, cHeight, Bitmap.Config.ARGB_8888);
		Canvas MapTile = new Canvas(bit);
		for(int i=0;i<height;i++){
			width = tiles[i].length;
			for(int j=0;j<width;j++){
				MapTile.drawRect(j*tileSize,i*tileSize,(j+1)*tileSize,(i+1)*tileSize,getPaint(tiles[i][j]));
			}
		}
		return bit;
	}
	/*-------------------------------------------*/
	class MoveBar{
		private int Offset,Max,BarWidth,Speed,barX,barY;
		private Paint barPaint = new Paint();
		private Paint textPaint = new Paint();
		MoveBar(int a, int b, int c, int d, int e, int f,int g){
			Offset = a;
			Max = b;
			BarWidth = c;
			Speed = d;
			barX = e;
			barY = f;
			barPaint.setColor(Color.rgb(255, 0, 0));
			textPaint.setColor(Color.rgb(200, 200, 200));
			textPaint.setTextSize(g);
		}
		void Update(){
			if(Offset < Max){Offset+=Speed;}else{Offset=Max;}
		}
		void Draw(Canvas canvas){
			canvas.drawRect(barX, barY, barX+(Perc(Offset,Max))*(BarWidth/100), barY+50, barPaint);
			canvas.drawText(""+Perc(Offset,Max), barX, barY, textPaint);
		}
		boolean canMove(){if(Offset>= Max){return true;}else{return false;}}
		void Reset(){Offset = 0;}
	}
	class Controls{
		private int buttonSize;
		private Rect up = new Rect();
		private Rect left = new Rect();
		private Rect down = new Rect();
		private Rect right = new Rect();
		private Rect dup = new Rect();
		private Rect dleft = new Rect();
		private Rect ddown = new Rect();
		private Rect dright = new Rect();
		private Rect ul = new Rect();
		private Rect dul = new Rect();
		private Rect ur = new Rect();
		private Rect dur = new Rect();
		private Rect dl = new Rect();
		private Rect ddl = new Rect();
		private Rect dr = new Rect();
		private Rect ddr = new Rect();
		private int butScale = 9;
		Controls(){
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int sWidth, sHeight;
			if(Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13){
				buttonSize = display.getWidth()/butScale;
				sWidth = display.getWidth();
				sHeight = display.getHeight();
			}
			else{
				Point size = new Point();
				display.getSize(size);
				buttonSize = size.x/butScale;
				sWidth = size.x;
				sHeight = size.y;
			}
			up.set(sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*3)-buttonSize/2,sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2);
			dup.set(sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*3)-buttonSize/2,sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2);
			down.set(sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2,sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*0)-buttonSize/2);
			ddown.set(sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2,sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*0)-buttonSize/2);
			right.set(sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2,sWidth-(buttonSize*0)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2);
			dright.set(sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2,sWidth-(buttonSize*0)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2);
			left.set(sWidth-(buttonSize*3)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2,sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2);
			dleft.set(sWidth-(buttonSize*3)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2,sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2);
			ul.set(sWidth-(buttonSize*3)-buttonSize/2,sHeight-(buttonSize*3)-buttonSize/2,sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2);
		   dul.set(sWidth-(buttonSize*3)-buttonSize/2,sHeight-(buttonSize*3)-buttonSize/2,sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2);
		    ur.set(sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*3)-buttonSize/2,sWidth-(buttonSize*0)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2);
		   dur.set(sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*3)-buttonSize/2,sWidth-(buttonSize*0)-buttonSize/2,sHeight-(buttonSize*2)-buttonSize/2);
		    dl.set(sWidth-(buttonSize*3)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2,sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*0)-buttonSize/2);
		   ddl.set(sWidth-(buttonSize*3)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2,sWidth-(buttonSize*2)-buttonSize/2,sHeight-(buttonSize*0)-buttonSize/2);
		    dr.set(sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2,sWidth-(buttonSize*0)-buttonSize/2,sHeight-(buttonSize*0)-buttonSize/2);
		   ddr.set(sWidth-(buttonSize*1)-buttonSize/2,sHeight-(buttonSize*1)-buttonSize/2,sWidth-(buttonSize*0)-buttonSize/2,sHeight-(buttonSize*0)-buttonSize/2);
			
		}
		void drawButtons(Canvas canvas){
			canvas.drawRect(dup,red);
			canvas.drawRect(ddown, red);
			canvas.drawRect(dleft, red);
			canvas.drawRect(dright, red);
			canvas.drawRect(dul, blue);
			canvas.drawRect(dur, blue);
			canvas.drawRect(ddl, blue);
			canvas.drawRect(ddr, blue);
		}
		private void offsetButtons(int x,int y){
			dup.offset(x, y);
			ddown.offset(x, y);
			dleft.offset(x, y);
			dright.offset(x, y);
			dul.offset(x, y);
			dur.offset(x, y);
			ddl.offset(x, y);
			ddr.offset(x, y);
		}
		void getPress(int x, int y){
			if(up.contains(x, y)){yOffset += mapSpeed;offsetButtons(0,-mapSpeed);}
			else if(down.contains(x, y)){yOffset -=mapSpeed;offsetButtons(0,mapSpeed);}
			else if(right.contains(x, y)){xOffset -= mapSpeed;offsetButtons(mapSpeed,0);}
			else if(left.contains(x, y)){xOffset += mapSpeed;offsetButtons(-mapSpeed,0);}
			else if(ul.contains(x, y)){xOffset += mapSpeed;yOffset += mapSpeed;offsetButtons(-mapSpeed,-mapSpeed);}
			else if(ur.contains(x, y)){xOffset -= mapSpeed;yOffset += mapSpeed;offsetButtons(mapSpeed,-mapSpeed);}
			else if(dl.contains(x, y)){xOffset += mapSpeed;yOffset -= mapSpeed;offsetButtons(-mapSpeed,mapSpeed);}
			else if(dr.contains(x, y)){xOffset -= mapSpeed;yOffset -= mapSpeed;offsetButtons(mapSpeed,mapSpeed);}
			else{}
		}
	}
	class Map{
		HashMap<String,Bitmap> MapList = new HashMap<String,Bitmap>();
		private String currentMap;
		private int tileSize;
		private int mapScale = 10;
		Map(){
			wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			if(Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13){
				tileSize = display.getWidth()/mapScale;
			}
			else{
				Point size = new Point();
				display.getSize(size);
				tileSize = size.x/mapScale;
			}
			int[][] setMap;
			setMap = new int[][]{
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1},
				{1,1,1,1,1,1,1,1,1,1}
			};
			MapList.put("Start",combineTiles(setMap,tileSize));
			setMap = new int[][]{
				{1,2,1,2,1,2,1,2,1,2},
				{2,1,2,1,2,1,2,1,2,1},
				{1,2,1,2,1,2,1,2,1,2},
				{2,1,2,1,2,1,2,1,2,1},
				{1,2,1,2,1,2,1,2,1,2},
				{2,1,2,1,2,1,2,1,2,1},
				{1,2,1,2,1,2,1,2,1,2},
				{2,1,2,1,2,1,2,1,2,1},
				{1,2,1,2,1,2,1,2,1,2},
				{2,1,2,1,2,1,2,1,2,1}
			};
			MapList.put("Level2",combineTiles(setMap,tileSize));
		}
		void setCurrentMap(String nextMap){
			currentMap = nextMap;
		}
		String getCurrentMap(){
			return currentMap;
		}
		void drawMap(Canvas canvas){
			Bitmap map = MapList.get(currentMap);
			canvas.drawBitmap(map, 0, 0, null);
			
		}
		/*int getTile(int x, int y){
			int[][] cMap = MapList.get(currentMap);
			int tile = cMap[x][y];
			return tile;
		}*/
	}
	class Weapon{
		private String Name,Type,Holder;
		private int Rarity;
		private int Dmg,Spellp;
		private int Speed, Attsp;
		private float Def,MDef;
		private String Eff1,Eff2,Eff3,Eff4;
		Weapon(String name, String type,String holder,int rarity,int dmg,int spellp,int speed, int attsp,float def, float mdef, String eff1, String eff2, String eff3,String eff4){
			Name = name;
			Type = type;
			Holder = holder;
			Rarity = rarity;
			Dmg = dmg;
			Spellp = spellp;
			Speed = speed;
			Attsp = attsp;
			Def = def;
			MDef = mdef;
			Eff1 = eff1;
			Eff2 = eff2;
			Eff3 = eff3;
			Eff4 = eff4;
			if(Rarity == 0){Rarity = rand.nextInt(10)+1;}
			int RandClass = 0;
			if(Holder == "rand"){
				RandClass = rand.nextInt(3);
				int w = rand.nextInt(ClassTypes[RandClass].length);
				Holder = ClassTypes[RandClass][w];
			}
			if(Type == "rand"){
				int[] nd = {9,6,7};
				Type = WepTypes[RandClass][rand.nextInt(nd[RandClass])];
			}
			if(Dmg == 0){
				int r1 = rand.nextInt(10)+1;
				int r2 = rand.nextInt(10)+1;
				Dmg = (int) (Rarity*(Rarity*40)+((Rarity*r1)*(Rarity+(r2*0.4)* 0.7)));
			}
		}
		
		String getName(){return Name;}
		String getType(){return Type;}
		String getHolder(){return Holder;}
		int getRarity(){return Rarity;}
		int getDamage(){return Dmg;}
		int getSpellPower(){return Spellp;}
		int getSpeed(){return Speed;}
		int getAttackSpeed(){return Attsp;}
		float getDefence(){return Def;}
		float getMagicDefence(){return MDef;}
		String getEffect1(){return Eff1;}
		String getEffect2(){return Eff2;}
		String getEffect3(){return Eff3;}
		String getEffect4(){return Eff4;}
	}
	class PlayerCharacter{
		private Rect playerPos = new Rect();
		private Rect drawPlayer = new Rect();
		private Paint playerpaint = new Paint();
		private int sWidth, sHeight;
		PlayerCharacter(){
			wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			if(Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13){
				sWidth = display.getWidth();
				sHeight = display.getHeight();
			}
			else{
				Point size = new Point();
				display.getSize(size);
				sWidth = size.x;
				sHeight = size.y;
			}
			drawPlayer.set(0, 0, sWidth/10, sWidth/10);
			drawPlayer.offsetTo((sWidth/2)-drawPlayer.width()/2, (sHeight/2)-drawPlayer.height()/2);
			playerPos.set(0, 0, sWidth/10, sWidth/10);
			playerPos.offsetTo((sWidth/2)-drawPlayer.width()/2, (sHeight/2)-drawPlayer.height()/2);
			playerpaint.setColor(Color.RED);
		}
		void draw(Canvas canvas){
			drawPlayer.offsetTo(((sWidth/2)-drawPlayer.width()/2)-xOffset, ((sHeight/2)-drawPlayer.height()/2)-yOffset);
			canvas.drawRect(drawPlayer, playerpaint);
		}
	}
}
