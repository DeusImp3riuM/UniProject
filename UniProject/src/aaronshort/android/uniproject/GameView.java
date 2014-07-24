package aaronshort.android.uniproject;

import java.util.HashMap;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
		screenW = getScreenWidth();
		screenH = getScreenHeight();
		xOffset = 0;
		yOffset = 0;
		GetClass.put("Base", SetMap("Swordsman","Ranger","Mage"));
		GetClass.put("Swordsman", SetMap("Light","Warrior","Sentinal"));
		GetClass.put("Ranger", SetMap("Marksman","Dark","Light"));
		GetClass.put("Mage", SetMap("Light","Dark","Grey"));
		GetClass.put("Light", SetMap("","",""));
		GetClass.put("Warrior", SetMap("","",""));
		GetClass.put("Sentinal", SetMap("","",""));


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

	Map map = new Map();
	Controls controls = new Controls();
	Inventory inv = new Inventory();
	PlayerCharacter player = new PlayerCharacter();
	Button settings = new Button(0,getScreenHeight()-(getScreenWidth()/7),"Settings","MainMenu",7,1,"yellow");
	Button back = new Button(0,getScreenHeight()-(getScreenWidth()/7),"Back","Map",7,1,"blue");
	Button inventory = new Button(getScreenWidth()/4,getScreenHeight()/10,"Inventory","Inventory",7,4,"cyan");
	Button equip = new Button(getScreenWidth()/4,(getScreenHeight()/10)*2,"Equip","Equiptment",7,4,"red");
	Button skills = new Button(getScreenWidth()/4,(getScreenHeight()/10)*3,"Skill","SkillTree",7,4,"#CB00FF");
	Button characters = new Button(getScreenWidth()/4,(getScreenHeight()/10)*4,"Characters","Characters",7,4,"#00A552");
	String State = "Map";
	String goingTo = "";
	boolean isTrans = false;
	boolean isTransIn = false;
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		/*if(load==false){load=true;}
		 *
		 * recieve = GetClass.get("Base");
		 * Print = Arrays.toString(recieve);
		 * canvas.drawText(Print,100,100,black)
		 *
		 * move1.Draw(canvas);
		 * move1.Update();
		 *
		 * canvas.drawText(""+testWep.getName(), 100, 100, black);
		 * canvas.drawText(""+testWep.getHolder(), 100, 130, black);
		 * canvas.drawText(""+testWep.getType(), 100, 160, black);
		 * canvas.drawText(""+testWep.getRarity(), 100, 190, black);
		 * canvas.drawText(""+testWep.getDamage(), 100, 210, black);
		 * canvas.drawText(""+testWep.getSpellPower(), 100, 240, black);
		 * canvas.drawText(""+testWep.getSpeed(), 100, 270, black);
		 * canvas.drawText(""+testWep.getAttackSpeed(), 100, 300, black);
		 * canvas.drawText(""+testWep.getDefence(), 100, 330, black);
		 * canvas.drawText(""+testWep.getMagicDefence(), 100, 360, black);*/
		if(State == "MainMenu"){
			back.draw(canvas);
			inventory.draw(canvas);
			equip.draw(canvas);
			skills.draw(canvas);
			characters.draw(canvas);
			if(isTransIn == false && isTrans == false){

				if(back.getPressed(touchX, touchY) == true){
					goingTo = back.getState();
					isTrans = true;
				}
				else if(inventory.getPressed(touchX, touchY) == true){
					goingTo = inventory.getState();
					isTrans = true;
				}
			}
			else if(isTransIn == true){transitionIn(15,canvas);}
			else if(isTrans == true){transitionOut(15,canvas,goingTo);}
		}
		else if(State == "Inventory"){
			inv.draw(canvas);
		}
		else if(State == "Map"){
			canvas.translate(xOffset, yOffset);
			map.setCurrentMap("Level2");
			map.drawMap(canvas);
			controls.drawButtons(canvas);
			player.draw(canvas);
			settings.draw(canvas);
			if(isTrans == false && isTransIn == false){
				if(touch == true){controls.getPress(touchX, touchY);}
				if(settings.getPressed(touchX, touchY) == true){
					goingTo = settings.getState();
					isTrans = true;
				}
			}
			else if(isTransIn == true){transitionIn(15,canvas);}
			else if(isTrans == true){transitionOut(15,canvas,goingTo);}
		}
		invalidate();
	}
	float tCounter = 0;
	public void transitionOut(float speed,Canvas canvas,String To){
		int Max = 255;
		Rect Cover = new Rect();
		Paint coverP = new Paint();
		Cover.set(0, 0, getScreenWidth(), getScreenHeight());
		coverP.setColor(Color.argb((int)tCounter,0,0,0));
		if(State == "Map"){Cover.offsetTo(-xOffset,-yOffset);}		
		canvas.drawRect(Cover, coverP);
		if(tCounter < Max){
			tCounter += speed;
		}
		else{
			State = To;
			isTrans = false;
			isTransIn = true;
			touchX = 0;
			touchY = 0;
		}
	}
	public void transitionIn(float speed,Canvas canvas){
		Rect Cover = new Rect();
		Paint coverP = new Paint();
		Cover.set(0,0, getScreenWidth(), getScreenHeight());
		if(tCounter > 0){
			tCounter -= speed;
		}
		else{
			isTrans = false;
			isTransIn = false;
			touchX = 0;
			touchY = 0;
		}
		coverP.setColor(Color.argb((int)tCounter,0,0,0));
		canvas.drawRect(Cover, coverP);
		
	}
	public boolean onTouchEvent(MotionEvent e){
		float xx = e.getX();
		float yy = e.getY();
		touch = true;
		if(e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_MOVE){
			touch = true;
			touchY = (int) yy;
			touchX = (int) xx;
		}
		else if(e.getAction() == MotionEvent.ACTION_UP){
			touch = false;
		}
		else{}
		return true;
	}
	public int getScreenWidth(){
		int screenWidth;
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if(Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13){
			screenWidth = display.getWidth();
		}
		else{
			Point size = new Point();
			display.getSize(size);
			screenWidth = size.x;
		}
		return screenWidth;
	}
	public int getScreenHeight(){
		int screenHeight;
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		if(Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 13){
			screenHeight = display.getHeight();
		}
		else{
			Point size = new Point();
			display.getSize(size);
			screenHeight = size.y;
		}
		return screenHeight;
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
			case 1:
				paint.setColor(Color.rgb(50,150,50));
				break;
			case 2:
				paint.setColor(Color.rgb(100,100,100));
				break;
			case 3:
				paint.setColor(Color.rgb(0,255,0));
				break;
			case 4:
				paint.setColor(Color.rgb(255,0,0));
				break;
			default:
				paint.setColor(Color.rgb(0,0,0));
				break;
		}
		return paint;
	}
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
	public boolean insideArea(int x,int y,int h,int w){
		Rect Area = new Rect();
		Area.set(x, y, x+w, y+h);
		return Area.contains(touchX, touchX);
	}
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
		private int sWidth, sHeight;
		Controls(){
			buttonSize = getScreenWidth()/butScale;
			sWidth = getScreenWidth();
			sHeight = getScreenHeight();
			
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
			int [][] cMap = map.getCurrentMapArray();
			if(up.contains(x, y)){
				if(cMap[player.playerTn()][player.playerL()] == 0 ||cMap[player.playerTn()][player.playerCenterX()] == 0 || cMap[player.playerTn()][player.playerR()] == 0){}
				else{yOffset += mapSpeed;offsetButtons(0,-mapSpeed);}}
			else if(down.contains(x, y)){
				if(cMap[player.playerBn()][player.playerL()] == 0 ||cMap[player.playerBn()][player.playerCenterX()] == 0 || cMap[player.playerBn()][player.playerR()] == 0){}
				else{yOffset -=mapSpeed;offsetButtons(0,mapSpeed);}}
			else if(right.contains(x, y)){
				if(cMap[player.playerT()][player.playerRn()] == 0 ||cMap[player.playerCenterY()][player.playerRn()] == 0 || cMap[player.playerB()][player.playerRn()] == 0){}
				else{xOffset -= mapSpeed;offsetButtons(mapSpeed,0);}}
			else if(left.contains(x, y)){
				if(cMap[player.playerT()][player.playerLn()] == 0 ||cMap[player.playerCenterY()][player.playerLn()] == 0 || cMap[player.playerB()][player.playerLn()] == 0){}
				else{xOffset += mapSpeed;offsetButtons(-mapSpeed,0);}}
			else if(ul.contains(x, y)){
				if(cMap[player.playerTn()][player.playerL()] == 0 ||cMap[player.playerTn()][player.playerCenterX()] == 0 || cMap[player.playerTn()][player.playerR()] == 0){}
				else{yOffset += mapSpeed;offsetButtons(0,-mapSpeed);}
				if(cMap[player.playerT()][player.playerLn()] == 0 ||cMap[player.playerCenterY()][player.playerLn()] == 0 || cMap[player.playerB()][player.playerLn()] == 0){}
				else{xOffset += mapSpeed;offsetButtons(-mapSpeed,0);}}
			else if(ur.contains(x, y)){
				if(cMap[player.playerTn()][player.playerL()] == 0 ||cMap[player.playerTn()][player.playerCenterX()] == 0 || cMap[player.playerTn()][player.playerR()] == 0){}
				else{yOffset += mapSpeed;offsetButtons(0,-mapSpeed);}
				if(cMap[player.playerT()][player.playerRn()] == 0 ||cMap[player.playerCenterY()][player.playerRn()] == 0 || cMap[player.playerB()][player.playerRn()] == 0){}
				else{xOffset -= mapSpeed;offsetButtons(mapSpeed,0);}}
			else if(dl.contains(x, y)){
				if(cMap[player.playerBn()][player.playerL()] == 0 ||cMap[player.playerBn()][player.playerCenterX()] == 0 || cMap[player.playerBn()][player.playerR()] == 0){}
				else{yOffset -=mapSpeed;offsetButtons(0,mapSpeed);}
				if(cMap[player.playerT()][player.playerLn()] == 0 ||cMap[player.playerCenterY()][player.playerLn()] == 0 || cMap[player.playerB()][player.playerLn()] == 0){}
				else{xOffset += mapSpeed;offsetButtons(-mapSpeed,0);}}
			else if(dr.contains(x, y)){
				if(cMap[player.playerBn()][player.playerL()] == 0 ||cMap[player.playerBn()][player.playerCenterX()] == 0 || cMap[player.playerBn()][player.playerR()] == 0){}
				else{yOffset -=mapSpeed;offsetButtons(0,mapSpeed);}
				if(cMap[player.playerT()][player.playerRn()] == 0 ||cMap[player.playerCenterY()][player.playerRn()] == 0 || cMap[player.playerB()][player.playerRn()] == 0){}
				else{xOffset -= mapSpeed;offsetButtons(mapSpeed,0);}}
			else{}
		}
	}
	class Map{
		HashMap<String,Bitmap> MapList = new HashMap<String,Bitmap>();
		HashMap<String,int[][]> MapListArray = new HashMap<String,int[][]>();
		private String currentMap;
		private int tileSize;
		private int mapScale = 10;
		Map(){
			tileSize = getScreenWidth()/mapScale;
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
			MapListArray.put("Start", setMap);
			setMap = new int[][]{
				{1,2,1,2,1,2,1,2,1,2},
				{2,1,2,1,2,1,2,1,2,1},
				{1,2,0,0,1,0,0,2,1,2},
				{2,1,2,1,2,1,2,1,2,1},
				{1,2,1,2,1,2,1,2,1,2},
				{2,1,2,1,2,1,2,1,2,1},
				{1,2,1,2,1,2,1,2,1,2},
				{2,1,2,1,2,1,2,1,2,1},
				{1,2,1,2,1,2,1,2,1,2},
				{2,1,2,1,2,1,2,1,2,1}
			};
			MapList.put("Level2",combineTiles(setMap,tileSize));
			MapListArray.put("Level2", setMap);
		}
        void setCurrentMap(String nextMap){
			currentMap = nextMap;
		}
		String getCurrentMap(){
			return currentMap;
		}
		int[][] getCurrentMapArray(){
			return MapListArray.get(currentMap);
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
			if(Holder.equals("rand")){
				RandClass = rand.nextInt(3);
				int w = rand.nextInt(ClassTypes[RandClass].length);
				Holder = ClassTypes[RandClass][w];
			}
			if(Type.equals("rand")){
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

		private Rect drawPlayer = new Rect();
		private Paint playerpaint = new Paint();
		private int sWidth, sHeight;
		private int playerSize;
		PlayerCharacter(){
			sWidth = getScreenWidth();
			sHeight = getScreenHeight();
			playerSize = sWidth/10;
			drawPlayer.set(0, 0, playerSize, playerSize);
			drawPlayer.offsetTo(playerSize-drawPlayer.width()/2, (sHeight/2)-drawPlayer.height()/2);
			playerpaint.setColor(Color.RED);
		}
		void draw(Canvas canvas){
			drawPlayer.offsetTo(((sWidth/2)-drawPlayer.width()/2)-xOffset, ((sHeight/2)-drawPlayer.height()/2)-yOffset);
			canvas.drawText(""+getScreenWidth()+".."+getScreenHeight(), 0, 0, black);
			
			canvas.drawRect(drawPlayer, playerpaint);
		}
		int playerSize(){
			return playerSize;
		}
		int playerCenterX(){
			return drawPlayer.centerX()/playerSize;
		}
		int playerCenterY(){
			return drawPlayer.centerY()/playerSize;
		}
		int playerL(){
			return drawPlayer.left/playerSize;
		}
		int playerR(){
			return drawPlayer.right/playerSize;
		}
		int playerT(){
			return drawPlayer.top/playerSize;
		}
		int playerB(){
			return drawPlayer.bottom/playerSize;
		}
		int playerLn(){
			return (drawPlayer.left-mapSpeed)/playerSize;
		}
		int playerRn(){
			return (drawPlayer.right+mapSpeed)/playerSize;
		}
		int playerTn(){
			return (drawPlayer.top-mapSpeed)/playerSize;
		}
		int playerBn(){
			return (drawPlayer.bottom+mapSpeed)/playerSize;
		}
	}
	class Button{
		private Rect button = new Rect();
		private Rect press = new Rect();
		private int x,y,width,height;
		private int screenW = getScreenWidth();
		private int screenH = getScreenHeight();
		private String image,to;
		private Paint test = new Paint();
		Button(int a, int b, String c, String d,int e, int f,String color){
			x = a;
			y = b;
			image = c;
			to = d;
			height = screenW/e;
			width = height*f;
			button.set(0, 0, width, height);
			button.offsetTo(x, y);
			press.set(button);
			test.setColor(Color.parseColor(color));
		}
		void draw(Canvas canvas){
			if(State.equals("Map")){button.offsetTo(x-xOffset, y-yOffset);}
			canvas.drawRect(button, test);
		}
		boolean getPressed(int x2, int y2){
			return press.contains(x2, y2);
		}
		String getState(){
			return to;
		}
	}
	class Inventory{
		private Rect top = new Rect();
		private Rect down = new Rect();
		private Rect left = new Rect();
		private Rect right = new Rect();
		private Rect space = new Rect();
		private Rect info = new Rect();
        private Rect mdown = new Rect();
        private Rect mup = new Rect();
		private int tabSize = (int)(getScreenWidth()/8.6);
		private int offset = (int)(getScreenWidth()/8.33);
		private int counter = 0;
		private Rect wep = new Rect(); //Weapons
		private Rect arm = new Rect(); //Armour
		private Rect acc = new Rect(); //Accessories
        HashMap<String,Weapon> slot = new HashMap<String, Weapon>();
        //private Weapon[] slot;
        private Rect slotSize = new Rect();
        private Rect slotTemp = new Rect();
        private int slotHeight;
        private Paint slotText = new Paint();

		Inventory(){
            slotText.setColor(Color.BLACK);
            slotText.setTextSize(getScreenWidth()/25);
			space.set((int)(getScreenWidth()/19.5), (int)(getScreenHeight()/10.57), (int)(getScreenWidth()/1.85),(int)(getScreenHeight()/1.16));
			top.set(0,0,getScreenWidth(),space.top);
			down.set(0,space.bottom,getScreenWidth(),getScreenHeight());
			left.set(0, space.top, space.left, space.bottom);
			right.set(space.right, space.top, getScreenWidth(), space.bottom);
			info.set((int)(getScreenWidth()/1.56),(int)(getScreenHeight()/9.34),(int)(getScreenWidth()/1.045),(int)(getScreenHeight()/1.5));
            mup.set((int)(getScreenWidth()/1.81),(int)(getScreenHeight()/10.75),(int)(getScreenWidth()/1.64),(int)(getScreenHeight()/2.68));
            mdown.set(mup);
            mdown.offsetTo(mup.left,getScreenHeight()/2);
			wep.set((int)(getScreenWidth()/18.6)+(offset*counter),(int)(getScreenHeight()/100.41),(int)(getScreenWidth()/18.6)+(offset*counter)+tabSize,(int)(getScreenHeight()/100.41)+tabSize);
			counter++;
			arm.set((int)(getScreenWidth()/18.6)+(offset*counter),(int)(getScreenHeight()/100.41),(int)(getScreenWidth()/18.6)+(offset*counter)+tabSize,(int)(getScreenHeight()/100.41)+tabSize);
			counter++;
			acc.set((int)(getScreenWidth()/18.6)+(offset*counter),(int)(getScreenHeight()/100.41),(int)(getScreenWidth()/18.6)+(offset*counter)+tabSize,(int)(getScreenHeight()/100.41)+tabSize);
            slot.put("0", new Weapon("Scythe of Holy Destiny", "rand", "rand", 10, 0, 50, 10, 40, 40, 32, "None", "None", "None", "None"));
            slot.put("1", new Weapon("Long Sword of the Fallen", "rand", "rand", 10, 0, 50, 10, 40, 40, 32, "None", "None", "None", "None"));
            slot.put("-1", null);
            slotSize.set(space.left, space.top, space.right, getScreenHeight() / 6);
            slotHeight = getScreenHeight()/6;
            slotHeight -= space.top;
            slotTemp = slotSize;
        }
		void draw(Canvas canvas){
            for(int f=0;slot.get(Integer.toString(f))!= slot.get("-1");f++){
                int top = (space.top)+(f*slotHeight);
                slotTemp.set(space.left,top,space.bottom,top+slotHeight);
                Weapon temp = slot.get(Integer.toString(f));
                canvas.drawRect(slotTemp,blue);
                canvas.drawText(temp.getName(), slotTemp.left+getScreenWidth()/80, top+slotHeight/2, slotText);
            }

			canvas.drawRect(top,red);
			canvas.drawRect(down,red);
			canvas.drawRect(left,red);
			canvas.drawRect(right,red);
			canvas.drawRect(info, blue);
			canvas.drawRect(wep, blue);
			canvas.drawRect(arm, blue);
			canvas.drawRect(acc, blue);
            canvas.drawRect(mup,blue);
            canvas.drawRect(mdown,blue);
		}
	}
}