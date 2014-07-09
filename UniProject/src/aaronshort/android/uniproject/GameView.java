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

public class GameView extends View {
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
	Paint black = new Paint();
	boolean load = false;
	boolean touch = false;
	int screenW,screenH;
	WindowManager wm;
	int xOffset,yOffset;
	
	public GameView(Context context) {
		super(context);
		red.setColor(Color.RED);
		black.setColor(Color.BLACK);
		black.setTextSize(20);
		wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenW = display.getWidth();
		screenH = display.getHeight();
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
		
		canvas.drawText(""+testWep.getName(), 100, 100, black);
		canvas.drawText(""+testWep.getHolder(), 100, 130, black);
		canvas.drawText(""+testWep.getType(), 100, 160, black);
		canvas.drawText(""+testWep.getRarity(), 100, 190, black);
		canvas.drawText(""+testWep.getDamage(), 100, 210, black);
		canvas.drawText(""+testWep.getSpellPower(), 100, 240, black);
		canvas.drawText(""+testWep.getSpeed(), 100, 270, black);
		canvas.drawText(""+testWep.getAttackSpeed(), 100, 300, black);
		canvas.drawText(""+testWep.getDefence(), 100, 330, black);
		canvas.drawText(""+testWep.getMagicDefence(), 100, 360, black);
		
		invalidate();
	}
	public boolean onTouchEvent(MotionEvent e){
		float xx = e.getX();
		float yy = e.getY();
		int x,y;
		touch = true;
		switch(e.getAction()){
			case MotionEvent.ACTION_DOWN:
				touch = true;
				y = (int) yy;
				x = (int) xx;
			case MotionEvent.ACTION_UP:
				touch = false;
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

}
