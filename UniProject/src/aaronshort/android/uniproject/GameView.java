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
	HashMap<String, String[]> GetClass = new HashMap<String,String[]>();
	Paint red = new Paint();
	Paint white = new Paint();
	boolean load = false;
	boolean touch = false;
	int screenW,screenH;
	WindowManager wm;
	int xOffset,yOffset;
	
	public GameView(Context context) {
		super(context);
		red.setColor(Color.RED);
		white.setColor(Color.WHITE);
		white.setTextSize(20);
		wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenW = display.getWidth();
		screenH = display.getHeight();
		xOffset = 0;
		yOffset = 0;
		GetClass.put("Base", SetMap("Swordsman","Mage","Ranger"));
		GetClass.put("Swordsman",SetMap("LightSword","Warrior","Sentinal"));
		GetClass.put("Mage",SetMap("LightMage","DarkMage","GreyMage"));
		GetClass.put("Ranger",SetMap("Marksman","LightArcher","DarkArcher"));
		GetClass.put("LightSword",SetMap("Assassin","Duelist","Thief"));
		GetClass.put("Warrior",SetMap("Beserker","Valkyrie","ScytheWielder"));
		GetClass.put("Sentinal",SetMap("Mutant","Paladin","Brute"));
		GetClass.put("LightMage",SetMap("Healer","HolyMage","Sorcerer"));
		GetClass.put("DarkMage",SetMap("CursedMage","BloodMage","Necromancer"));
		GetClass.put("GreyMage",SetMap("Geomancer","WarriorMage","BioMorphMage"));
		GetClass.put("Marksman",SetMap("PiercingArcher","SharpShooter","Techmaturgist"));
		GetClass.put("LightArcher",SetMap("Hunter","Frost","DaggerThrower"));
		GetClass.put("DarkArcher",SetMap("CorruptedArcher","NightArcher","StealthArcher"));
		
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
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		canvas.translate(xOffset, yOffset);
		if(load==false){load=true;}
		/*
		 * recieve = GetClass.get("Base");
		 * Print = Arrays.toString(recieve);
		 * canvas.drawText(Print,100,100,white)
		 */
		move1.Draw(canvas);
		move1.Update();
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
			if(Rarity == 0){}Rarity = rand.nextInt(10)+1;
			if(Holder == "rand"){}
			if(Type == "rand"){
				int nd = rand.nextInt(3);
				int[] nd2 = {9,6,7};
				Type = WepTypes[nd][rand.nextInt(nd2[nd])];
			}
			if(Dmg == 0){
				int r1 = rand.nextInt(10)+1;
				int r2 = rand.nextInt(10)+1;
				Dmg = (int) (Rarity*(Rarity*40)+((Rarity*r1)*(Rarity+(r2*0.4)* 0.7)));
			}
		}
	}

}
