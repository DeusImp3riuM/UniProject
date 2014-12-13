package pythiumodyssey.android.aaronshort.pythiumodyssey;

        import java.lang.reflect.Array;
        import java.util.HashMap;
        import java.util.Hashtable;
        import java.util.Random;

        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.renderscript.Sampler;
        import android.util.Log;
        import android.view.Display;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.WindowManager;
        import android.graphics.*;
        import android.widget.Toast;
        import java.util.Arrays;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map.Entry;

public class GameView extends View {

    int mapSpeed = 3;
    Toast toast = Toast.makeText(getContext(),"Test",Toast.LENGTH_SHORT);
    HashMap<String,Spell> getSpell = new HashMap<String,Spell>();

    String[][] part1 = {
            {"Terrible","Weak","Flimsy"},
            {"Feeble","Light","Paper"},
            {"Lazy","Pathetic","Airy"},
            {"Sturdy","Random","Slow"},
            {"Tough","Standard","Fast"},
            {"Potent","Furious","Damaging"},
            {"Mashing","Controlling","Deadly"},
            {"Exterminating","Diamond","Soul Bound"},
            {"Monstrous","Destroying","Invincible"},
            {"Ascended","Dark Matter","Pythium"}
    };


    Random rand = new Random();
    String[][] WepTypes={
            {"LongSword","Club","Axe","Scythe"},
            {"Orb","Book","Staff","Rod"},
            {"LongBow","Crossbow","Sniper","Energy"}
    };
    String[][] ClassTypes = {
            {"Swordsman","Valkyrie","ScytheWielder","Paladin"},
            {"Mage","DarkMage","HolyMage","BloodMage"},
            {"Ranger","SharpShooter","Techmaturgist","CorruptedArcher"}
    };
    HashMap<String, String[]> GetClass = new HashMap<String,String[]>();

    Paint red = new Paint();
    Paint blue = new Paint();
    Paint black = new Paint();
    Paint cyan = new Paint();
    boolean load = false;
    boolean touch = false;
    int screenW,screenH;
    WindowManager wm;
    int xOffset,yOffset;
    int touchX,touchY;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    final static HashMap<String,Enemy> EnemyDatabase = new HashMap<String, Enemy>();

    public GameView(Context context) {
        super(context);
        red.setColor(Color.RED);
        blue.setColor(Color.BLUE);
        black.setColor(Color.BLACK);
        black.setTextSize(20);
        cyan.setColor(Color.CYAN);
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
        EnemyDatabase.put("Empty",new Enemy(0,0,"empty",0,0,0,new Rect(0,0,1,1),0));
        EnemyDatabase.put("debug1",new Enemy(10,5,"debug",5,5,5,new Rect(0,0,100,100),10));
        EnemyDatabase.put("debug2",new Enemy(10,5,"debug",5,5,5,new Rect(0,0,100,100),10));
        EnemyDatabase.put("test",new Enemy(10,5,"debug2",5,5,5,new Rect(0,0,200,200),1000));
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
    Map map = new Map();
    Controls controls = new Controls();
    Inventory inv = new Inventory();
    Skills skillTree = new Skills();
    Battle battle = null;
    PlayerCharacter player = new PlayerCharacter();
    PlayerCharacter player2 = new PlayerCharacter();
    PlayerCharacter player3 = new PlayerCharacter();
    Button settings = new Button(0,getScreenHeight()-(getScreenWidth()/7),"Settings","MainMenu",7,1,"yellow");
    Button back = new Button(0,getScreenHeight()-(getScreenWidth()/7),"Back","Map",7,1,"blue");
    Button backMenu = new Button(0,0,"Back","MainMenu",7,1,"yellow");
    Button inventory = new Button(getScreenWidth()/4,getScreenHeight()/10,"Inventory","Inventory",7,4,"cyan");
    Button equip = new Button(getScreenWidth()/4,(getScreenHeight()/10)*2,"Equip","Equiptment",7,4,"red");
    Button skills = new Button(getScreenWidth()/4,(getScreenHeight()/10)*3,"Skill","SkillTree",7,4,"#CB00FF");
    Button characters = new Button(getScreenWidth()/4,(getScreenHeight()/10)*4,"Characters","Characters",7,4,"#00A552");
    String State = "Map";
    String goingTo = "";
    boolean isTrans = false;
    boolean isTransIn = false;
    //Chest chest = new Chest(8,8,2,"Level2");
    Chest[] chest = new Chest[]{
            new Chest(8,8,2,"Level2"),
            new Chest(2,4,4,"Start")
    };
    MapWarp[] mapWarp = new MapWarp[]{
            new MapWarp(1,1,5,5,"Level2","Start")
    };
    Sprite[] sprite = new Sprite[]{
            new Sprite("Level2","test",4,4)
    };

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
		/*
		 *
		 * recieve = GetClass.get("Base");
		 * Print = Arrays.toString(recieve);
		 * canvas.drawText(Print,100,100,black)
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
        if(load==false){load=true;map.setCurrentMap("Level2");player.setClass("Range");player2.setClass("Melee");player3.setClass("Melee");}
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
                else if(skills.getPressed(touchX,touchY) == true){
                    goingTo = skills.getState();
                    isTrans = true;
                }
            }
            else if(isTransIn == true){transitionIn(15, canvas);}
            else if(isTrans == true){transitionOut(15, canvas, goingTo);}
        }
        else if(State == "Inventory"){
            inv.draw(canvas);
            inv.upDownButton();
            inv.slotPressed();
            backMenu.draw(canvas);
            inv.checkEquip(canvas);

            if(isTransIn == false && isTrans == false){

                if(backMenu.getPressed(touchX, touchY) == true){
                    goingTo = backMenu.getState();
                    isTrans = true;
                }
            }
            else if(isTransIn == true){transitionIn(15,canvas);}
            else if(isTrans == true){transitionOut(15,canvas,goingTo);}
        }
        else if(State == "Map"){
            canvas.translate(xOffset, yOffset);
            map.drawMap(canvas);
            player.draw(canvas);
            drawEntities(canvas);
            controls.drawButtons(canvas);
            settings.draw(canvas);
            if(isTrans == false && isTransIn == false){
                if(controls.triggerBattle()){goingTo = "Battle";battle = new Battle("debug1","test","debug2");isTrans = true;}
                if(touch == true){controls.getPress(touchX, touchY);}
                if(settings.getPressed(touchX, touchY) == true){
                    goingTo = settings.getState();
                    isTrans = true;
                }
            }
            else if(isTransIn == true){transitionIn(15,canvas);}
            else if(isTrans == true){transitionOut(15,canvas,goingTo);}
        }
        else if(State == "Battle"){
            battle.draw(canvas);
            if(isTrans == false && isTransIn == false){
                if(controls.triggerBattle()){goingTo = "Battle";isTrans = true;}
                if(touch == true){controls.getPress(touchX, touchY);}
                if(settings.getPressed(touchX, touchY) == true){
                    goingTo = settings.getState();
                    isTrans = true;
                }
            }
            else if(isTransIn == true){transitionIn(15,canvas);}
            else if(isTrans == true){transitionOut(15,canvas,goingTo);}
        }
        else if(State.equals("SkillTree")){
            skillTree.draw(canvas);
            if(isTransIn == false && isTrans == false){
                if(back.getPressed(touchX, touchY) == true){
                    goingTo = "MainMenu";
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
            touchX = getScreenWidth();
            touchY = getScreenHeight();
        }
    }
    public void transitionIn(float speed,Canvas canvas){
        Rect Cover = new Rect();
        Paint coverP = new Paint();
        Cover.set(0,0, getScreenWidth(), getScreenHeight());
        if(State == "Map"){Cover.offsetTo(-xOffset,-yOffset);}
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
    public void drawEntities(Canvas canvas){
        for(int w=0;w<sprite.length;w++){
            if(sprite[w].Level.equals(map.getCurrentMap())){
                sprite[w].draw(canvas);
                sprite[w].autoMove();
            }
        }
        for(int w=0;w<mapWarp.length;w++){
            if(mapWarp[w].Current.equals(map.getCurrentMap())){
                mapWarp[w].update();
            }
        }
        for(int w =0;w<chest.length;w++){
            if(chest[w].Level.equals(map.getCurrentMap())){
                chest[w].draw(canvas);
                chest[w].update();
            }
        }
    }
    public void toasty(String a){
        Toast toast = Toast.makeText(getContext(),a,Toast.LENGTH_SHORT);
        toast.show();
        if(!touch){toast.cancel();}
    }
    public boolean onTouchEvent(MotionEvent e){
        float xx = e.getX();
        float yy = e.getY();
        touch = true;
        if(e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_MOVE){
            if(e.getAction()==MotionEvent.ACTION_MOVE){skillTree.move=true;}
            touch = true;
            touchY = (int) yy;
            touchX = (int) xx;
        }
        else if(e.getAction() == MotionEvent.ACTION_UP){
            touchX = 0;
            touchY = 0;
            touch = false;
            skillTree.move=false;
            controls.dLock = 0;
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
                paint.setColor(Color.CYAN);
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
                MapTile.drawText(Integer.toString(tiles[i][j]),j*tileSize+(tileSize/2),i*tileSize+(tileSize/2),red);
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
        private Paint movePaint = new Paint();
        private Paint textPaint = new Paint();
        MoveBar(int a, int b, int c, int d, int e, int f,int g){
            Offset = a;
            Max = b;
            BarWidth = c;
            Speed = d;
            barX = e;
            barY = f;
            barPaint.setColor(Color.rgb(255, 0, 0));
            movePaint.setColor(Color.rgb(255, 157, 0));
            textPaint.setColor(Color.rgb(200, 200, 200));
            textPaint.setTextSize(g);
        }
        void Update(){
            if(Offset < Max){Offset+=Speed;}else{Offset=Max;}
        }
        void Draw(Canvas canvas){
            if(!canMove()){canvas.drawRect(barX, barY, barX+(Perc(Offset,Max))*(BarWidth/100), barY+(BarWidth/10), barPaint);}
            else{canvas.drawRect(barX, barY, barX+(Perc(Offset,Max))*(BarWidth/100), barY+(BarWidth/10), movePaint);}
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
        private Rect inter = new Rect();
        private Rect dinter = new Rect();
        private Rect Dialog = new Rect();
        private boolean dDisplay = false;
        private int dLock = 0;
        private int butScale = 9;
        private int sWidth, sHeight;
        private int chance = 0;
        private boolean canMove = true;
        private Rect playerHitBox = new Rect();
        private Point upP,downP,leftP,rightP,ulP,urP,dlP,drP,interP;
        private Rect test = new Rect();

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
            inter.set(ul.right,ul.bottom,dr.left,dr.top);
            dinter.set(ul.right,ul.bottom,dr.left,dr.top);

            upP = new Point(up.left,up.top);
            downP = new Point(down.left,down.top);
            leftP = new Point(left.left,left.top);
            rightP = new Point(right.left,right.top);
            ulP = new Point(ul.left,ul.top);
            urP = new Point(ur.left,ur.top);
            dlP = new Point(dl.left,dl.top);
            drP = new Point(dr.left,dr.top);
            interP = new Point(inter.left,inter.top);


            Dialog.set(10,10,getScreenWidth()-10,(getScreenHeight()/3)-10);

        }

        void drawButtons(Canvas canvas){
            dup.offsetTo(upP.x-xOffset, upP.y-yOffset);
            ddown.offsetTo(downP.x-xOffset, downP.y-yOffset);
            dleft.offsetTo(leftP.x-xOffset, leftP.y-yOffset);
            dright.offsetTo(rightP.x-xOffset, rightP.y-yOffset);
            dul.offsetTo(ulP.x-xOffset, ulP.y-yOffset);
            dur.offsetTo(urP.x-xOffset, urP.y-yOffset);
            ddl.offsetTo(dlP.x-xOffset, dlP.y-yOffset);
            ddr.offsetTo(drP.x-xOffset, drP.y-yOffset);
            dinter.offsetTo(interP.x-xOffset, interP.y-yOffset);
            canvas.drawRect(dup,red);
            canvas.drawRect(ddown, red);
            canvas.drawRect(dleft, red);
            canvas.drawRect(dright, red);
            canvas.drawRect(dul, blue);
            canvas.drawRect(dur, blue);
            canvas.drawRect(ddl, blue);
            canvas.drawRect(ddr, blue);
            canvas.drawRect(dinter, blue);
            canvas.drawRect(test,black);
            if(dDisplay){
                for(int w=0;w<sprite.length;w++){
                    if(sprite[w].Level.equals("Level2")){
                        if(Rect.intersects(sprite[w].sBox,player.drawPlayer)){
                            canvas.drawRect(Dialog,blue);
                            for(int t=0;t<sprite[w].dialogText[0].length;t++){
                                canvas.drawText(sprite[w].getText(0,t),Dialog.left+20,Dialog.top+40+(40*t),black);
                            }
                        }
                    }
                }
            }
        }
        private void offsetButtons(int x,int y){
            dup.offsetTo(upP.x-xOffset, upP.y-yOffset);
            ddown.offsetTo(downP.x-xOffset, downP.y-yOffset);
            dleft.offsetTo(leftP.x-xOffset, leftP.y-yOffset);
            dright.offsetTo(rightP.x-xOffset, rightP.y-yOffset);
            dul.offsetTo(ulP.x-xOffset, ulP.y-yOffset);
            dur.offsetTo(urP.x-xOffset, urP.y-yOffset);
            ddl.offsetTo(dlP.x-xOffset, dlP.y-yOffset);
            ddr.offsetTo(drP.x-xOffset, drP.y-yOffset);
            dinter.offsetTo(interP.x-xOffset, interP.y-yOffset);
            Dialog.offset(x, y);
        }
        boolean triggerBattle(){
            if(touch && map.getTag() == "Wild"){
                if(up.contains(touchX,touchY) || down.contains(touchX,touchY) || left.contains(touchX,touchY) || right.contains(touchX,touchY) || ul.contains(touchX,touchY) || ur.contains(touchX,touchY)|| dl.contains(touchX,touchY) || dr.contains(touchX,touchY)){
                    if (rand.nextInt(100)+1 <= 50){
                        return true;
                    }
                    else{return false;}
                }
            }
            return false;
        }
        void getPress(int x, int y){
            int [][] cMap = map.getCurrentMapArray();
            if(inter.contains(x, y) && dLock == 0){
                for(int w=0;w<sprite.length;w++){
                    if(sprite[w].Level.equals(map.getCurrentMap())){
                        if(Rect.intersects(sprite[w].sBox,player.drawPlayer)){
                            dDisplay = !dDisplay;
                            if(!dDisplay){
                                sprite[w].Speech();
                            }
                            dLock++;
                        }
                    }
                }
            }
            else if(dDisplay == false){
                canMove = true;
                if(up.contains(x,y)){playerHitBox.set(player.drawPlayer.left+5,player.drawPlayer.top,player.drawPlayer.right-5,player.drawPlayer.top+5);}
                else if(down.contains(x,y)){playerHitBox.set(player.drawPlayer.left+5,player.drawPlayer.bottom+5,player.drawPlayer.right-5,player.drawPlayer.bottom);}
                else if(right.contains(x,y)){playerHitBox.set(player.drawPlayer.right-5,player.drawPlayer.top+5,player.drawPlayer.right,player.drawPlayer.bottom-5);}
                else if(left.contains(x,y)){playerHitBox.set(player.drawPlayer.left,player.drawPlayer.top+5,player.drawPlayer.left+5,player.drawPlayer.bottom-5);}
                else if(ur.contains(x,y)){playerHitBox.set(player.drawPlayer);playerHitBox.left+=5;playerHitBox.bottom+=5;}
                else if(ul.contains(x,y)){playerHitBox.set(player.drawPlayer);playerHitBox.right+=5;playerHitBox.bottom+=5;}
                else if(dr.contains(x,y)){playerHitBox.set(player.drawPlayer);playerHitBox.left+=5;playerHitBox.top+=5;}
                else if(dl.contains(x,y)){playerHitBox.set(player.drawPlayer);playerHitBox.right+=5;playerHitBox.top+=5;}


                for(int w=0;w<sprite.length;w++){
                    if(sprite[w].Level.equals(map.getCurrentMap())){
                        if(Rect.intersects(sprite[w].sBox,playerHitBox)){
                            canMove = false;
                        }
                    }
                }
                for(int w=0;w<chest.length;w++){
                    if(chest[w].Level.equals(map.getCurrentMap())){
                        if(Rect.intersects(chest[w].sprite,playerHitBox)){
                            canMove = false;
                        }
                    }
                }
                if(canMove){
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
        }
    }
    class Map{
        HashMap<String,Bitmap> MapList = new HashMap<String,Bitmap>();
        HashMap<String,int[][]> MapListArray = new HashMap<String,int[][]>();
        HashMap<String,String> MapTag = new HashMap<String,String>();
        private String currentMap;
        public int tileSize;
        private int mapScale = 10;
        Map(){
            tileSize = getScreenWidth()/mapScale;
            int[][] setMap;
            setMap = new int[][]{
                    {1,1,1,1,1,1,1,1,1,1},
                    {1,1,1,1,1,1,1,1,1,1},
                    {1,1,1,1,0,1,1,1,1,1},
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
            MapTag.put("Start","Wild");
            setMap = new int[][]{
                    {1,2,1,2,1,2,1,2,1,2},
                    {2,1,2,1,2,1,2,1,2,1},
                    {1,2,0,0,1,0,0,2,1,2},
                    {2,1,2,1,2,1,2,1,2,1},
                    {1,2,1,2,1,2,1,2,1,2},
                    {2,1,2,1,2,1,2,1,2,1},
                    {1,2,1,2,1,2,1,2,1,2},
                    {2,0,2,1,2,1,2,1,2,1},
                    {1,2,1,2,1,2,1,2,1,2},
                    {2,1,2,1,2,1,2,1,2,1}
            };
            MapList.put("Level2",combineTiles(setMap,tileSize));
            MapListArray.put("Level2", setMap);
            MapTag.put("Level2","Wild");
        }
        void setCurrentMap(String nextMap){
            currentMap = nextMap;
        }
        String getCurrentMap(){
            return currentMap;
        }
        String getTag(){
            return MapTag.get(getCurrentMap());
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
        private String Name,Type,Holder,MainType;
        private int Rarity;
        private int Dmg,Spellp;
        private int Speed, Attsp;
        private int Def,MDef;
        int r1, r2;
        private String Eff1,Eff2,Eff3,Eff4;
        Weapon(String name, String type,String holder,int rarity,int dmg,int spellp,int speed, int attsp,int def, int mdef, String eff1, String eff2, String eff3,String eff4){
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
                int[] nd = {4,4,4};
                Type = WepTypes[RandClass][rand.nextInt(nd[RandClass])];
            }
            if(Arrays.asList(WepTypes[0]).contains(Type) || Arrays.asList(WepTypes[2]).contains(Type)){
                MainType = "AD";
            }
            else{MainType = "SP";}
            if(Dmg == 0){
                r1 = rand.nextInt(10)+1;
                r2 = rand.nextInt(10)+1;
                Dmg = (int) (Rarity*(Rarity*40)+((Rarity*r1)*(Rarity+(r2*0.4)* 0.7)));
                if(MainType.equals("SP")){Dmg=Dmg/3;}
            }
            if(Spellp == 0){
                r1 = rand.nextInt(10)+1;
                r2 = rand.nextInt(10)+1;
                Spellp = (int) (Rarity*(Rarity*40)+((Rarity*r1)*(Rarity+(r2*0.4)* 0.7)));
                if(MainType.equals("AD")){Spellp=Spellp/3;}
            }
            if(Attsp == 0){
                r1 = rand.nextInt(10)+1;
                r2 = rand.nextInt(10)+1;
                Attsp = (int) (Rarity*(Rarity*5)+((Rarity*r1)*(Rarity+(r2*0.4)* 0.3)));
            }
            if(Def == 0){
                r1 = rand.nextInt(10)+1;
                r2 = rand.nextInt(10)+1;
                Def = (int) (Rarity*(Rarity*0.1)+((Rarity*r1)*(Rarity+(r2*0.4)* 0.3)));
            }
            if(MDef == 0){
                r1 = rand.nextInt(10)+1;
                r2 = rand.nextInt(10)+1;
                MDef = (int) (Rarity*(Rarity*0.1)+((Rarity*r1)*(Rarity+(r2*0.4)* 0.3)));
            }
            if(Speed == 0){
                r1 = rand.nextInt(10)+1;
                r2 = rand.nextInt(10)+1;
                Speed = (int) (Rarity*(Rarity*2)+((Rarity*(r1/10))*(Rarity+((r2/10)*0.4)* 0.3)));
            }
            if(Name.equals("rand")){
                Name = part1[Rarity-1][rand.nextInt(3)] + " " + Type;
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
    class Armour{
        private String Name,Type,Holder,MainType;
        private int Rarity;
        private int Dmg,Spellp;
        private int Speed, Attsp;
        private float Def,MDef;
        private String Eff1,Eff2,Eff3,Eff4;
        Armour(String name, String type,String holder,int rarity,int dmg,int spellp,int speed, int attsp,float def, float mdef, String eff1, String eff2, String eff3,String eff4){
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
                int[] nd = {4,4,4};
                Type = WepTypes[RandClass][rand.nextInt(nd[RandClass])];
            }
            if(Arrays.asList(WepTypes[0]).contains("Type") || Arrays.asList(WepTypes[2]).contains("Type")){
                MainType = "AD";
            }
            else{MainType = "SP";}
            if(Dmg == 0){
                int r1 = rand.nextInt(10)+1;
                int r2 = rand.nextInt(10)+1;
                Dmg = (int) (Rarity*(Rarity*40)+((Rarity*r1)*(Rarity+(r2*0.4)* 0.7)));
                if(MainType.equals("SP")){Dmg=Dmg/3;}
            }
            if(Spellp == 0){
                int r1 = rand.nextInt(10)+1;
                int r2 = rand.nextInt(10)+1;
                Spellp = (int) (Rarity*(Rarity*40)+((Rarity*r1)*(Rarity+(r2*0.4)* 0.7)));
                if(MainType.equals("AD")){Spellp=Spellp/3;}
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
        private Weapon personalWep = null;
        private Armour personalArm = null;
        private int Experience = 0;
        private int nextLevel = 20;
        private int Level = 1;
        private String Class;
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
            canvas.drawText(""+controls.chance, 0, 0, black);
            canvas.drawRect(drawPlayer, playerpaint);
        }
        int playerCenterX(){return drawPlayer.centerX()/playerSize;}
        int playerCenterY(){return drawPlayer.centerY()/playerSize;}
        int playerL(){return drawPlayer.left/playerSize;}
        int playerR(){return drawPlayer.right/playerSize;}
        int playerT(){return drawPlayer.top/playerSize;}
        int playerB(){return drawPlayer.bottom/playerSize;}
        int playerLn(){return (drawPlayer.left-mapSpeed)/playerSize;}
        int playerRn(){return (drawPlayer.right+mapSpeed)/playerSize;}
        int playerTn(){return (drawPlayer.top-mapSpeed)/playerSize;}
        int playerBn(){return (drawPlayer.bottom+mapSpeed)/playerSize;}
        void unequipWeapon(){personalWep = null;}
        void equipWeapon(Weapon weapon){personalWep = weapon;}
        void unequipArmour(){personalArm = null;}
        void equipArmour(Armour armour){personalArm = armour;}
        void earnExp(int i){Experience += i;}
        void setClass(String s){
            Class = s;
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
            return touch && !inv.getDsbOpen() && press.contains(x2,y2);
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
        private Rect equipt = new Rect();
        private Rect unequipt = new Rect();
        HashMap<String,Weapon> slot = new HashMap<String, Weapon>();
        HashMap<String,Armour> slotA = new HashMap<String, Armour>();
        HashMap<String,Rect> allSlots = new HashMap<String, Rect>();
        private Rect slotSize = new Rect();
        private int nextSlot = 1;
        private int slotHeight;
        private Paint slotText = new Paint();
        private int slotOffset;
        private int slotCount;
        private int selected;
        private Paint selectedCol = new Paint();
        private int infoW,infoH,rowSpacing,colSpacing;
        private String[] tempArray = {"e",null,null,null,null,null,null,null,null,null};
        private DialogSelectionBox dsb = new DialogSelectionBox("test",tempArray,"none","none");
        private String Displaying = "Weapon";

        Inventory(){
            colSpacing = 25;
            rowSpacing = 12;
            selectedCol.setARGB(100,0,0,0);
            slotOffset = 0;
            selected = -1;
            slotText.setColor(Color.BLACK);
            slotText.setTextSize(getScreenWidth()/25);
            space.set((int)(getScreenWidth()/33.33), (int)(getScreenHeight()/10.21), (int)(getScreenWidth()/1.15),(int)(getScreenHeight()/1.99));
            top.set(0,0,getScreenWidth(),space.top);
            down.set(0,space.bottom,getScreenWidth(),getScreenHeight());
            left.set(0, space.top, space.left, space.bottom);
            right.set(space.right, space.top, getScreenWidth(), space.bottom);
            info.set((int)(getScreenWidth()/33.33),(int)(getScreenHeight()/1.82),(int)(getScreenWidth()/1.15),(int)(getScreenHeight()/1.04));
            infoW = info.width();
            infoH = info.height();
            mup.set((int)(getScreenWidth()/1.14),(int)(getScreenHeight()/8.48),(int)(getScreenWidth()/1.01),(int)(getScreenHeight()/5.04));
            mdown.set(mup);
            mdown.offsetTo(mup.left,(int)(getScreenHeight()/2.52));
            //int butSize = (int)(getScreenWidth()/1.14) - (int)(getScreenWidth()/1.01);
            equipt.set((int)(getScreenWidth()/1.14),(int)(getScreenHeight()/1.5),(int)(getScreenWidth()/1.01),info.top+(info.height()/2-10));
            unequipt.set(equipt);
            unequipt.offset(0,equipt.height()+10);
            wep.set((getScreenWidth()/4)+(offset*counter),(int)(getScreenHeight()/100.41),(getScreenWidth()/4)+(offset*counter)+tabSize,(int)(getScreenHeight()/100.41)+tabSize);
            counter++;
            arm.set((getScreenWidth()/4)+(offset*counter),(int)(getScreenHeight()/100.41),(getScreenWidth()/4)+(offset*counter)+tabSize,(int)(getScreenHeight()/100.41)+tabSize);
            counter++;
            acc.set((getScreenWidth()/4)+(offset*counter),(int)(getScreenHeight()/100.41),(getScreenWidth()/4)+(offset*counter)+tabSize,(int)(getScreenHeight()/100.41)+tabSize);
            slot.put("0", new Weapon("Hands", " ", " ", 1, 1, 1, 1, 1, 1, 1, "None", "None", "None", "None"));

            slotA.put("0", new Armour("Chest Plate of Fire", "rand", "rand", 10, 0, 50, 10, 40, 40, 32, "None", "None", "None", "None"));
            slotA.put("1", new Armour("Chest Plate of Ice", "rand", "rand", 10, 0, 50, 10, 40, 40, 32, "None", "None", "None", "None"));

            slot.put("-1", null);
            slotA.put("-1", null);
            slotSize.set(space.left, space.top, space.right, getScreenHeight() / 6);
            slotHeight = getScreenHeight()/6;
            slotHeight -= space.top;
        }
        void draw(Canvas canvas){
            canvas.drawRect(space,blue);
            Rect slotTemp = new Rect();
            slotTemp.setEmpty();
            int f;
            if(Displaying.equals("Weapon")) {
                for (f = 0; slot.get(Integer.toString(f)) != null; f++) {
                    int top = (space.top) + (f * slotHeight);
                    slotTemp.set(space.left, top + slotOffset, space.right, top + slotHeight + slotOffset);
                    canvas.drawRect(slotTemp, blue);
                    if (f == selected) {
                        canvas.drawRect(slotTemp, selectedCol);
                    }
                    Weapon temp = slot.get(Integer.toString(f));
                    canvas.drawText(temp.getName(), slotTemp.left + getScreenWidth() / 80, (top + slotHeight / 2) + slotOffset, slotText);
                    Rect rtemp = new Rect();
                    rtemp.set(slotTemp);
                    slotCount = f;
                    allSlots.put("" + f, rtemp);
                    if(temp == player.personalWep){canvas.drawRect(slotTemp.right-slotTemp.height(),slotTemp.top,slotTemp.right,slotTemp.bottom,black);}
                }
            } else if (Displaying.equals("Armour")) {
                for (f = 0; slotA.get(Integer.toString(f)) != null; f++) {
                    int top = (space.top) + (f * slotHeight);
                    slotTemp.set(space.left, top + slotOffset, space.right, top + slotHeight + slotOffset);
                    canvas.drawRect(slotTemp, blue);
                    if (f == selected) {
                        canvas.drawRect(slotTemp, selectedCol);
                    }
                    Armour temp = slotA.get(Integer.toString(f));
                    canvas.drawText(temp.getName(), slotTemp.left + getScreenWidth() / 80, (top + slotHeight / 2) + slotOffset, slotText);
                    Rect rtemp = new Rect();
                    rtemp.set(slotTemp);
                    slotCount = f;
                    allSlots.put("" + f, rtemp);
                    if(temp == player.personalArm){canvas.drawRect(slotTemp.right-slotTemp.height(),slotTemp.top,slotTemp.right,slotTemp.bottom,black);}
                }
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
            canvas.drawRect(equipt,blue);
            canvas.drawRect(unequipt,blue);
            if(selected != -1){
                if(Displaying.equals("Weapon")) {
                    Weapon temp = slot.get(Integer.toString(selected));
                    canvas.drawText("Name: " + temp.getName(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing)), slotText);
                    canvas.drawText("Class: " + temp.getHolder(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 3), slotText);
                    canvas.drawText("Type: " + temp.getType(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 4), slotText);
                    canvas.drawText("Rarity: " + temp.getRarity(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 5), slotText);
                    canvas.drawText("Damage: " + temp.getDamage(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 6), slotText);
                    canvas.drawText("Defence: " + temp.getDefence(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 7), slotText);
                    canvas.drawText("Spell Power: " + temp.getSpellPower(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 8), slotText);
                    canvas.drawText("Magic Defence: " + temp.getMagicDefence(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 9), slotText);
                    canvas.drawText("Speed: " + temp.getSpeed(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 10), slotText);
                    canvas.drawText("Attack Speed: " + temp.getAttackSpeed(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 11), slotText);

                    canvas.drawText("Effect 1: " + temp.getEffect1(), info.left + ((infoW / colSpacing)) + (info.width() / 2), info.top + ((infoH / rowSpacing) * 3), slotText);
                    canvas.drawText("Effect 2: " + temp.getEffect2(), info.left + ((infoW / colSpacing)) + (info.width() / 2), info.top + ((infoH / rowSpacing) * 4), slotText);
                    canvas.drawText("Effect 3: " + temp.getEffect3(), info.left + ((infoW / colSpacing) + (info.width() / 2)), info.top + ((infoH / rowSpacing) * 5), slotText);
                    canvas.drawText("Effect 4: " + temp.getEffect4(), info.left + ((infoW / colSpacing)) + (info.width() / 2), info.top + ((infoH / rowSpacing) * 6), slotText);
                }
                else if(Displaying.equals("Armour")) {
                    Armour temp = slotA.get(Integer.toString(selected));
                    canvas.drawText("Name: " + temp.getName(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing)), slotText);
                    canvas.drawText("Class: " + temp.getHolder(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 3), slotText);
                    canvas.drawText("Type: " + temp.getType(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 4), slotText);
                    canvas.drawText("Rarity: " + temp.getRarity(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 5), slotText);
                    canvas.drawText("Damage: " + temp.getDamage(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 6), slotText);
                    canvas.drawText("Defence: " + temp.getDefence(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 7), slotText);
                    canvas.drawText("Spell Power: " + temp.getSpellPower(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 8), slotText);
                    canvas.drawText("Magic Defence: " + temp.getMagicDefence(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 9), slotText);
                    canvas.drawText("Speed: " + temp.getSpeed(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 10), slotText);
                    canvas.drawText("Attack Speed: " + temp.getAttackSpeed(), info.left + ((infoW / colSpacing)), info.top + ((infoH / rowSpacing) * 11), slotText);

                    canvas.drawText("Effect 1: " + temp.getEffect1(), info.left + ((infoW / colSpacing)) + (info.width() / 2), info.top + ((infoH / rowSpacing) * 3), slotText);
                    canvas.drawText("Effect 2: " + temp.getEffect2(), info.left + ((infoW / colSpacing)) + (info.width() / 2), info.top + ((infoH / rowSpacing) * 4), slotText);
                    canvas.drawText("Effect 3: " + temp.getEffect3(), info.left + ((infoW / colSpacing) + (info.width() / 2)), info.top + ((infoH / rowSpacing) * 5), slotText);
                    canvas.drawText("Effect 4: " + temp.getEffect4(), info.left + ((infoW / colSpacing)) + (info.width() / 2), info.top + ((infoH / rowSpacing) * 6), slotText);
                }
            }
        }
        void addWeapon(Weapon w){
            slot.put(Integer.toString(nextSlot),w);
            nextSlot++;
        }
        void upDownButton(){
            if(mdown.contains(touchX,touchY) && !dsb.open){
                if(allSlots.get(Integer.toString(slotCount)).bottom > space.bottom) {
                    slotOffset -= 10;
                }
            }
            else if(mup.contains(touchX,touchY) && !dsb.open){
                if(allSlots.get("0").top < space.top) {
                    slotOffset += 10;
                }
            }
            else if(wep.contains(touchX,touchY) && !dsb.open){
                Displaying = "Weapon";
            }
            else if(arm.contains(touchX,touchY) && !dsb.open){
                Displaying = "Armour";
            }
        }
        void checkEquip(Canvas canvas){
            if(equipt.contains(touchX,touchY) && touch && selected != -1 || dsb.open){
                String[] s = new String[10];
                s[0] = "slot 1";
                s[1] = "slot 2";
                s[2] = "slot 3";
                s[3] = "slot 4";
                if(!dsb.open){dsb = new DialogSelectionBox("Select a Character",s,Displaying,"Inv");dsb.open = true;}
                dsb.draw(canvas);
                dsb.buttonPress();
            }
        }
        void slotPressed(){
            if(!dsb.open) {
                for (int r = 0; r < slotCount + 1; r++) {
                    if (allSlots.get(Integer.toString(r)).contains(touchX, touchY) && space.contains(touchX, touchY)) {
                        selected = r;
                    }
                }
            }
        }
        Weapon getSelectedWeapon(){
            return slot.get(Integer.toString(selected));
        }
        Armour getSelectedArmour(){
            return slotA.get(Integer.toString(selected));
        }
        boolean getDsbOpen(){
            return dsb.open;
        }
    }
    class DialogSelectionBox{
        private Rect title = new Rect();
        private Rect body = new Rect();
        private Rect bottom = new Rect();
        private Rect cancel = new Rect();
        private Rect confirm = new Rect();
        private Rect selHighlight = new Rect();
        private Rect checkRect = new Rect();

        private String titleText,Function,Type;
        private String[] selections = new String[5];
        private Paint white = new Paint();
        private Paint purple = new Paint();
        private Paint red = new Paint();
        private Paint green = new Paint();
        private Paint font = new Paint();
        private Paint selColor = new Paint();
        private Paint titleFont = new Paint();
        public boolean open;
        private int count = 0;
        int selectionMade = 0;

        DialogSelectionBox(String t, String[] s, String type,String function){
            Type = type;
            Function = function;
            white.setColor(Color.WHITE);
            purple.setColor(Color.rgb(255,0,255));
            red.setColor(Color.RED);
            green.setColor(Color.GREEN);
            selColor.setColor(Color.DKGRAY);
            open = false;
            titleText = t;
            selections = s;
            for(int y=0;selections[y]!=null;y++){count=y;}
            count++;
            body.set(0, 0, getScreenWidth() / 2, count * (getScreenHeight() / 16));
            body.offsetTo((getScreenWidth() / 2) - (body.width() / 2), (getScreenHeight() / 2) - (body.height() / 2));
            font.setTextSize(((body.height() / count) / 3) * 2);
            font.setColor(Color.RED);
            titleFont.setTextSize(((body.height()/count)/3)*2);
            titleFont.setColor(Color.RED);
            title.set(body.left,body.top-(body.height()/count),body.right,body.top);
            bottom.set(body.left,body.bottom,body.right,body.bottom+title.height());
            cancel.set(bottom.right-(bottom.width()/3),bottom.top,bottom.right,bottom.bottom);
            confirm.set(bottom.left,bottom.top,bottom.left+(bottom.width()/3),bottom.bottom);
            selHighlight.set(0,0,title.width(),title.height());
            selHighlight.offsetTo(body.left,body.top);
            checkRect.set(0,0,title.width(),title.height());
            checkRect.offsetTo(body.left, body.top);
        }
        void draw(Canvas canvas){
            slotPress();
            canvas.drawRect(body,white);
            canvas.drawRect(title,black);
            canvas.drawRect(bottom,purple);
            canvas.drawRect(cancel,red);
            canvas.drawRect(confirm,green);
            canvas.drawRect(selHighlight,selColor);

            for(int t =0;t<count;t++){
                canvas.drawText(selections[t],body.left,(body.top+((body.height()/count)*(t+1)))-(font.getTextSize()/3),font);
            }
            if(titleFont.measureText(titleText,0,titleText.length()) > title.width()){
                titleFont.setTextSize(title.width()/(titleText.length()/2));
            }
            canvas.drawText(titleText,title.left+(titleFont.getTextSize()/2),title.bottom-(titleFont.getTextSize()/2),titleFont);
        }
        void buttonPress(){
            if(cancel.contains(touchX,touchY)){
                open = false;
                if(battle.display.equals("attack")){battle.display = "none";}
            }
            else if(confirm.contains(touchX,touchY)){
                if(Function.equals("Inv")) {
                    switch (selectionMade) {
                        case 0:
                            if (Type.equals("Weapon")) {
                                player.equipWeapon(inv.getSelectedWeapon());
                                Toast toast = Toast.makeText(getContext(), inv.getSelectedWeapon().getName(), Toast.LENGTH_SHORT);
                                toast.show();
                            } else if (Type.equals("Armour")) {
                                player.equipArmour(inv.getSelectedArmour());
                                Toast toast = Toast.makeText(getContext(), inv.getSelectedArmour().getName(), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            break;
                        case 1:
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                    }
                    open = false;
                }
                else if(Function.equals("Battle")){
                    switch (selectionMade){
                        case 0:
                            if(battle.slot1.isAlive()) {
                                if (battle.P1.canMove()) {
                                    battle.castSpell = new CastSpell(battle.Player1Spells[battle.getSlot()], getSpell.get(battle.Player1Spells[battle.getSlot()]).getDuring(), new Point(battle.battlePlayer1.centerX(), battle.battlePlayer1.centerY()), new Point(battle.battleEnemy1.centerX(), battle.battleEnemy1.centerY()), battle.slot1);
                                    battle.P1.Reset();
                                } else if (battle.P2.canMove()) {
                                    battle.castSpell = new CastSpell(battle.Player1Spells[battle.getSlot()], getSpell.get(battle.Player1Spells[battle.getSlot()]).getDuring(), new Point(battle.battlePlayer2.centerX(), battle.battlePlayer2.centerY()), new Point(battle.battleEnemy1.centerX(), battle.battleEnemy1.centerY()), battle.slot1);
                                    battle.P2.Reset();
                                } else if (battle.P3.canMove()) {
                                    battle.castSpell = new CastSpell(battle.Player1Spells[battle.getSlot()], getSpell.get(battle.Player1Spells[battle.getSlot()]).getDuring(), new Point(battle.battlePlayer3.centerX(), battle.battlePlayer3.centerY()), new Point(battle.battleEnemy1.centerX(), battle.battleEnemy1.centerY()), battle.slot1);
                                    battle.P3.Reset();
                                }
                            }
                            break;
                        case 1:
                            if(battle.slot2.isAlive()) {
                                if (battle.P1.canMove()) {
                                    battle.castSpell = new CastSpell(battle.Player1Spells[battle.getSlot()], getSpell.get(battle.Player1Spells[battle.getSlot()]).getDuring(), new Point(battle.battlePlayer1.centerX(), battle.battlePlayer1.centerY()), new Point(battle.battleEnemy2.centerX(), battle.battleEnemy2.centerY()), battle.slot2);
                                    battle.P1.Reset();
                                } else if (battle.P2.canMove()) {
                                    battle.castSpell = new CastSpell(battle.Player1Spells[battle.getSlot()], getSpell.get(battle.Player1Spells[battle.getSlot()]).getDuring(), new Point(battle.battlePlayer2.centerX(), battle.battlePlayer2.centerY()), new Point(battle.battleEnemy2.centerX(), battle.battleEnemy2.centerY()), battle.slot2);
                                    battle.P2.Reset();
                                } else if (battle.P3.canMove()) {
                                    battle.castSpell = new CastSpell(battle.Player1Spells[battle.getSlot()], getSpell.get(battle.Player1Spells[battle.getSlot()]).getDuring(), new Point(battle.battlePlayer3.centerX(), battle.battlePlayer3.centerY()), new Point(battle.battleEnemy2.centerX(), battle.battleEnemy2.centerY()), battle.slot2);
                                    battle.P3.Reset();
                                }
                            }
                            break;
                        case 2:
                            if(battle.slot3.isAlive()) {
                                if (battle.P1.canMove()) {
                                    battle.castSpell = new CastSpell(battle.Player1Spells[battle.getSlot()], getSpell.get(battle.Player1Spells[battle.getSlot()]).getDuring(), new Point(battle.battlePlayer1.centerX(), battle.battlePlayer1.centerY()), new Point(battle.battleEnemy3.centerX(), battle.battleEnemy3.centerY()), battle.slot3);
                                    battle.P1.Reset();
                                } else if (battle.P2.canMove()) {
                                    battle.castSpell = new CastSpell(battle.Player1Spells[battle.getSlot()], getSpell.get(battle.Player1Spells[battle.getSlot()]).getDuring(), new Point(battle.battlePlayer2.centerX(), battle.battlePlayer2.centerY()), new Point(battle.battleEnemy3.centerX(), battle.battleEnemy3.centerY()), battle.slot3);
                                    battle.P2.Reset();
                                } else if (battle.P3.canMove()) {
                                    battle.castSpell = new CastSpell(battle.Player1Spells[battle.getSlot()], getSpell.get(battle.Player1Spells[battle.getSlot()]).getDuring(), new Point(battle.battlePlayer3.centerX(), battle.battlePlayer3.centerY()), new Point(battle.battleEnemy3.centerX(), battle.battleEnemy3.centerY()), battle.slot3);
                                    battle.P3.Reset();
                                }
                            }
                            break;
                    }
                    open = false;
                }
                else if(Function.equals("Attack")){
                    switch(selectionMade){
                        case 0:
                            if(battle.slot1.isAlive()){
                                if(battle.P1.canMove()){
                                    battle.canAttack = true;
                                    battle.Attacker = battle.battlePlayer1;
                                    battle.AttackerName = "player";
                                    battle.Attacking = "slot1";
                                    battle.AttackingClass = player.Class;
                                    battle.battleSequence.put("Attacker",new Rect(battle.battlePlayer1));
                                    battle.battleSequence.put("Attacking",new Rect(battle.slot1.Space));
                                    battle.battleSequencePoints.put("Attacker",new Point(battle.battlePlayer1.left,battle.battlePlayer1.top));
                                    battle.battleSequencePoints.put("Attacking",new Point(battle.slot1.Space.left-(int)(battle.battlePlayer1.width()),battle.slot1.Space.centerY()-(battle.battlePlayer1.height()/2)));
                                    battle.P1.Reset();
                                }else if(battle.P2.canMove()){
                                    battle.canAttack = true;
                                    battle.Attacker = battle.battlePlayer2;
                                    battle.AttackerName = "player2";
                                    battle.Attacking =  "slot1";
                                    battle.AttackingClass = player2.Class;
                                    battle.battleSequence.put("Attacker",new Rect(battle.battlePlayer2));
                                    battle.battleSequence.put("Attacking",new Rect(battle.slot1.Space));
                                    battle.battleSequencePoints.put("Attacker",new Point(battle.battlePlayer2.left,battle.battlePlayer2.top));
                                    battle.battleSequencePoints.put("Attacking",new Point(battle.slot1.Space.left-(int)(battle.battlePlayer2.width()),battle.slot1.Space.centerY()-(battle.battlePlayer1.height()/2)));
                                    battle.P2.Reset();
                                }else if(battle.P3.canMove()){
                                    battle.canAttack = true;
                                    battle.Attacker = battle.battlePlayer3;
                                    battle.AttackerName = "player3";
                                    battle.Attacking =  "slot1";
                                    battle.AttackingClass = player3.Class;
                                    battle.battleSequence.put("Attacker",new Rect(battle.battlePlayer3));
                                    battle.battleSequence.put("Attacking",new Rect(battle.slot1.Space));
                                    battle.battleSequencePoints.put("Attacker",new Point(battle.battlePlayer3.left,battle.battlePlayer3.top));
                                    battle.battleSequencePoints.put("Attacking",new Point(battle.slot1.Space.left-(int)(battle.battlePlayer3.width()),battle.slot1.Space.centerY()-(battle.battlePlayer1.height()/2)));
                                    battle.P3.Reset();
                                }
                                open = false;
                            }
                            break;
                        case 1:
                            if(battle.slot2.isAlive()){
                                if(battle.P1.canMove()){
                                    battle.canAttack = true;
                                    battle.Attacker = battle.battlePlayer1;
                                    battle.AttackerName = "player";
                                    battle.Attacking =  "slot2";
                                    battle.AttackingClass = player.Class;
                                    battle.battleSequence.put("Attacker",new Rect(battle.battlePlayer1));
                                    battle.battleSequence.put("Attacking",new Rect(battle.slot2.Space));
                                    battle.battleSequencePoints.put("Attacker",new Point(battle.battlePlayer1.left,battle.battlePlayer1.top));
                                    battle.battleSequencePoints.put("Attacking",new Point(battle.slot2.Space.left-(int)(battle.battlePlayer1.width()),battle.slot2.Space.centerY()-(battle.battlePlayer1.height()/2)));
                                    battle.P1.Reset();
                                }else if(battle.P2.canMove()){
                                    battle.canAttack = true;
                                    battle.Attacker = battle.battlePlayer2;
                                    battle.AttackerName = "player2";
                                    battle.Attacking = "slot2";
                                    battle.AttackingClass = player2.Class;
                                    battle.battleSequence.put("Attacker",new Rect(battle.battlePlayer2));
                                    battle.battleSequence.put("Attacking",new Rect(battle.slot2.Space));
                                    battle.battleSequencePoints.put("Attacker",new Point(battle.battlePlayer2.left,battle.battlePlayer2.top));
                                    battle.battleSequencePoints.put("Attacking",new Point(battle.slot2.Space.left-(int)(battle.battlePlayer2.width()),battle.slot2.Space.centerY()-(battle.battlePlayer1.height()/2)));
                                    battle.P2.Reset();
                                }else if(battle.P3.canMove()){
                                    battle.canAttack = true;
                                    battle.Attacker = battle.battlePlayer3;
                                    battle.AttackerName = "player3";
                                    battle.Attacking = "slot2";
                                    battle.AttackingClass = player3.Class;
                                    battle.battleSequence.put("Attacker",new Rect(battle.battlePlayer3));
                                    battle.battleSequence.put("Attacking",new Rect(battle.slot2.Space));
                                    battle.battleSequencePoints.put("Attacker",new Point(battle.battlePlayer3.left,battle.battlePlayer3.top));
                                    battle.battleSequencePoints.put("Attacking",new Point(battle.slot2.Space.left-(int)(battle.battlePlayer3.width()),battle.slot2.Space.centerY()-(battle.battlePlayer1.height()/2)));
                                    battle.P3.Reset();
                                }
                                open = false;
                            }
                            break;
                        case 2:
                            if(battle.slot3.isAlive()){
                                if(battle.P1.canMove()){
                                    battle.canAttack = true;
                                    battle.Attacker = battle.battlePlayer1;
                                    battle.AttackerName = "player";
                                    battle.Attacking = "slot3";
                                    battle.AttackingClass = player.Class;
                                    battle.battleSequence.put("Attacker",new Rect(battle.battlePlayer1));
                                    battle.battleSequence.put("Attacking",new Rect(battle.slot3.Space));
                                    battle.battleSequencePoints.put("Attacker",new Point(battle.battlePlayer1.left,battle.battlePlayer1.top));
                                    battle.battleSequencePoints.put("Attacking",new Point(battle.slot3.Space.left-(int)(battle.battlePlayer1.width()),battle.slot3.Space.centerY()-(battle.battlePlayer1.height()/2)));
                                    battle.P1.Reset();
                                }else if(battle.P2.canMove()){
                                    battle.canAttack = true;
                                    battle.Attacker = battle.battlePlayer2;
                                    battle.AttackerName = "player2";
                                    battle.Attacking = "slot3";
                                    battle.AttackingClass = player2.Class;
                                    battle.battleSequence.put("Attacker",new Rect(battle.battlePlayer2));
                                    battle.battleSequence.put("Attacking",new Rect(battle.slot3.Space));
                                    battle.battleSequencePoints.put("Attacker",new Point(battle.battlePlayer2.left,battle.battlePlayer2.top));
                                    battle.battleSequencePoints.put("Attacking",new Point(battle.slot3.Space.left-(int)(battle.battlePlayer2.width()),battle.slot3.Space.centerY()-(battle.battlePlayer1.height()/2)));
                                    battle.P2.Reset();
                                }else if(battle.P3.canMove()){
                                    battle.canAttack = true;
                                    battle.Attacker = battle.battlePlayer3;
                                    battle.AttackerName = "player3";
                                    battle.Attacking = "slot3";
                                    battle.AttackingClass = player3.Class;
                                    battle.battleSequence.put("Attacker",new Rect(battle.battlePlayer3));
                                    battle.battleSequence.put("Attacking",new Rect(battle.slot3.Space));
                                    battle.battleSequencePoints.put("Attacker",new Point(battle.battlePlayer3.left,battle.battlePlayer3.top));
                                    battle.battleSequencePoints.put("Attacking",new Point(battle.slot3.Space.left-(int)(battle.battlePlayer3.width()),battle.slot3.Space.centerY()-(battle.battlePlayer1.height()/2)));
                                    battle.P3.Reset();
                                }
                                open = false;
                            }
                            break;
                    }
                    battle.isAttacking = true;
                }
            }
        }
        void slotPress(){
            checkRect.offsetTo(body.left,body.top);
            for(int s=0;s<count;s++){
                checkRect.offsetTo(body.left,body.top+(title.height()*s));
                if(checkRect.contains(touchX,touchY)){
                    selHighlight.offsetTo(body.left,body.top+(title.height()*(s)));
                    selectionMade=s;
                }
            }
        }
    }
    class Battle{
        private Rect bg = new Rect(), menuButton = new Rect(),attack = new Rect(),spells = new Rect();
        private Rect classSpells = new Rect(),items = new Rect(),flee = new Rect(),subMenu = new Rect();
        private Rect subMenuButton = new Rect(),up = new Rect(),down = new Rect();
        private Paint back = new Paint(),subBack = new Paint(),buttonFont = new Paint(),fade = new Paint();
        private String display = "none";
        public Rect battlePlayer1 = new Rect(),battlePlayer2 = new Rect(),battlePlayer3 = new Rect();
        private Rect battleEnemy1 = new Rect(),battleEnemy2 = new Rect(),battleEnemy3 = new Rect();
        private boolean drawTooltip = false;
        private int subtouchX,subtouchY;
        private Tooltip spellTip = new Tooltip();
        private CastSpell castSpell = new CastSpell("none",1,new Point(0,0),new Point(0,0),null);
        private DialogSelectionBox dsb = new DialogSelectionBox("Select Target",new String[]{"1","2",null},"none","Battle");
        public boolean Standby = false;
        private int slotSelected;
        private Enemy slot1;
        private Enemy slot2;
        private Enemy slot3;
        private boolean send;
        private boolean giveExp;
        private boolean giveExpDisplay;
        private int P1ep,P2ep,P3ep;
        private Paint expSlot = new Paint();
        private Rect ExpBox = new Rect(getScreenWidth()/10,getScreenWidth()/10,getScreenWidth()-(getScreenWidth()/10),getScreenHeight()/2);
        private Rect ExpBox1 = new Rect(ExpBox.left,ExpBox.top,ExpBox.right,ExpBox.top+(ExpBox.height()/3));
        private Rect ExpBox3 = new Rect(ExpBox.left,ExpBox.top+(ExpBox.height()/3)*2,ExpBox.right,ExpBox.bottom);
        private Rect ExpBox2 = new Rect(ExpBox.left,ExpBox1.bottom,ExpBox.right,ExpBox3.top);
        private int t;
        private int totalExp;
        private int totalExpD;
        //               MoveBar(Offset,Max,     Width,       Speed,  X,                 Y,  TextSize)
        public MoveBar P1,P2,P3,E1,E2,E3;
        private String[] Player1Spells = {"Fire","Fire"};
        private String[] Player2Spells = {"Fire","Fire"};
        private String[] Player3Spells = {"Fire","Fire"};

        public boolean canAttack = false;
        public Rect Attacker = new Rect();
        public String AttackerName = "";
        String Attacking = "";
        public String AttackingClass = "";
        private int Phase = 0;
        private Point battlePlayer1Start = new Point();
        private Point battlePlayer2Start = new Point();
        private Point battlePlayer3Start = new Point();
        private int currentStep = 0;
        private Point moveController = new Point();
        private HashMap<String,Rect> battleSequence = new HashMap<String,Rect>();
        private HashMap<String,Point> battleSequencePoints = new HashMap<String,Point>();
        public boolean isAttacking = false;

        Battle(String a, String b, String c){
            Phase = 0;
            currentStep = 0;
            t=0;
            P1ep = player.Experience;
            giveExp = true;
            giveExpDisplay = true;
            totalExpD = 0;
            expSlot.setColor(Color.rgb(255,140,0));
            bg.set(0,(getScreenHeight()/2),getScreenWidth(),getScreenHeight());
            subMenu.set((getScreenWidth()/2)-(bg.right/16),bg.top+(bg.right/32),bg.right-(bg.right/16),bg.bottom-((bg.right/12)));
            menuButton.set(0,0,(int)(getScreenWidth()/3.5),(getScreenWidth()/4)/3);
            menuButton.offset(menuButton.height() / 2, bg.top + (menuButton.height() / 2));
            attack.set(menuButton);
            menuButton.offset(0, (int) (menuButton.height() * 1.5));
            spells.set(menuButton);
            menuButton.offset(0, (int) (menuButton.height() * 1.5));
            classSpells.set(menuButton);
            menuButton.offset(0, (int) (menuButton.height() * 1.5));
            items.set(menuButton);
            menuButton.offset(0, (int) (menuButton.height() * 1.5));
            flee.set(menuButton);
            subMenuButton.set(subMenu.left,subMenu.top,subMenu.left+subMenu.width(),subMenu.top+(subMenu.height()/8));

            battlePlayer1.set(50,100,150,200);
            battlePlayer2.set(battlePlayer1);
            battlePlayer2.offset(0, getScreenHeight()/6);
            battlePlayer3.set(battlePlayer2);
            battlePlayer3.offset(0, getScreenHeight()/6);
            battleEnemy1.set(getScreenWidth() - 150, 100, getScreenWidth() - 50, getScreenHeight()/6);
            battleEnemy2.set(battleEnemy1);
            battleEnemy2.offset(0, getScreenHeight()/6);
            battleEnemy3.set(battleEnemy2);
            battleEnemy3.offset(0, getScreenHeight()/6);

            battlePlayer1Start = new Point(battlePlayer1.left,battlePlayer1.top);
            battlePlayer2Start = new Point(battlePlayer2.left,battlePlayer2.top);
            battlePlayer3Start = new Point(battlePlayer3.left,battlePlayer3.top);

            slot1 = EnemyDatabase.get(a).get();
            slot1.setRect(new Point(battleEnemy1.centerX(), battleEnemy1.centerY()));

            slot2 = EnemyDatabase.get(b).get();
            slot2.setRect(new Point(battleEnemy2.centerX(),battleEnemy2.centerY()));

            slot3 = EnemyDatabase.get(c).get();
            slot3.setRect(new Point(battleEnemy3.centerX(),battleEnemy3.centerY()));
            totalExp = slot1.Exp+slot2.Exp+slot3.Exp;
            buttonFont.setColor(Color.BLACK);
            buttonFont.setTextSize((menuButton.height()/4)*3);
            buttonFont.setTextAlign(Paint.Align.CENTER);
            fade.setColor(Color.DKGRAY);
            fade.setAlpha(150);
            back.setColor(Color.DKGRAY);
            subBack.setColor(Color.LTGRAY);
            P1 = new MoveBar(0,1000,getScreenWidth()/4,2,battlePlayer1.left,battlePlayer1.top-(getScreenHeight()/24),40);
            P2 = new MoveBar(0,1000,getScreenWidth()/4,4,battlePlayer2.left,battlePlayer2.top-(getScreenHeight()/24),40);
            P3 = new MoveBar(0,1000,getScreenWidth()/4,50,battlePlayer3.left,battlePlayer3.top-(getScreenHeight()/24),40);
            E1 = new MoveBar(0,1000,getScreenWidth()/4,6,battleEnemy1.left-(battleEnemy1.width()),battleEnemy1.top-(getScreenHeight()/24),40);
            E2 = new MoveBar(0,1000,getScreenWidth()/4,10,battleEnemy2.left-(battleEnemy2.width()),battleEnemy2.top-(getScreenHeight()/24),40);
            E3 = new MoveBar(0,1000,getScreenWidth()/4,1,battleEnemy3.left-(battleEnemy3.width()),battleEnemy3.top-(getScreenHeight()/24),40);
            send = false;
        }
        void draw(Canvas canvas){
            canvas.drawRect(bg,back);
            canvas.drawRect(subMenu,subBack);
            canvas.drawRect(attack,red);
            canvas.drawText("attack",attack.centerX(),attack.centerY()+(buttonFont.getTextSize()/2),buttonFont);
            canvas.drawRect(spells,blue);
            canvas.drawText("spells",spells.centerX(),spells.centerY()+(buttonFont.getTextSize()/2),buttonFont);
            canvas.drawRect(classSpells,red);
            canvas.drawText("class",classSpells.centerX(),classSpells.centerY()+(buttonFont.getTextSize()/2),buttonFont);
            canvas.drawRect(items,blue);
            canvas.drawText("items",items.centerX(),items.centerY()+(buttonFont.getTextSize()/2),buttonFont);
            canvas.drawRect(flee,red);
            canvas.drawText("flee",flee.centerX(),flee.centerY()+(buttonFont.getTextSize()/2),buttonFont);
            canvas.drawRect(battlePlayer1, red);
            canvas.drawRect(battlePlayer2, red);
            canvas.drawRect(battlePlayer3, red);
            slot1.draw(canvas);
            slot2.draw(canvas);
            slot3.draw(canvas);
            P1.Draw(canvas);
            P2.Draw(canvas);
            P3.Draw(canvas);
            if(slot1.isAlive()){E1.Draw(canvas);}
            if(slot2.isAlive()){E2.Draw(canvas);}
            if(slot3.isAlive()){E3.Draw(canvas);}
            if(!Standby){
                P1.Update();
                P2.Update();
                P3.Update();
                if(slot1.isAlive()){E1.Update();}
                if(slot2.isAlive()){E2.Update();}
                if(slot3.isAlive()){E3.Update();}
            }
            else{getPress(canvas);}

            if(P1.canMove()||P2.canMove()||P3.canMove()||E1.canMove()||E2.canMove()||E3.canMove()){Standby = true;Phase=0;}

            if(E1.canMove()){E1.Reset();Standby=false;}
            else if(E2.canMove()){E2.Reset();Standby=false;}
            else if(E3.canMove()){E3.Reset();Standby=false;}

            if(display.equals("spells")){
                subMenuButton.offsetTo(subMenu.left,subMenu.top);
                drawTooltip = false;
                if(castSpell.Name.equals("none")){castSpell.spellComplete = true;}
                String[] PlayerSpells = Player1Spells;
                if(P2.canMove()){
                    PlayerSpells = Player2Spells;
                }
                else if(P3.canMove()){
                    PlayerSpells = Player3Spells;
                }
                for(int u=0;u<PlayerSpells.length;u++){
                    canvas.drawRect(subMenuButton,blue);
                    canvas.drawText(Player1Spells[u],subMenuButton.centerX(),subMenuButton.bottom,buttonFont);
                    if(touch){
                        subtouchX = touchX;
                        subtouchY = touchY;
                    }
                    if(P1.canMove()||P2.canMove()||P3.canMove()) {
                        if (subMenuButton.contains(subtouchX, subtouchY) && !touch && castSpell.completed()) {
                            subtouchX = 0;
                            subtouchY = 0;
                            if(!dsb.open){dsb = new DialogSelectionBox("Select a Target",new String[]{"Enemy1","Enemy2","Enemy3",null},"none","Battle");dsb.open = true;}
                            slotSelected = u;
                        }
                        if (subMenuButton.contains(touchX, touchY) && touch && !dsb.open) {
                            drawTooltip = true;
                        }
                    }
                    subMenuButton.offset(0,subMenuButton.height());
                }
                if(dsb.open){
                    dsb.draw(canvas);
                    dsb.buttonPress();
                }
                if(drawTooltip){spellTip.draw(canvas);}

            }
            else if(display.equals("attack")){
                if(!canAttack){
                    if(!dsb.open){dsb = new DialogSelectionBox("Select a Target",new String[]{"Enemy1","Enemy2","Enemy3",null},"none","Attack");dsb.open = true;}
                    if(dsb.open){
                        dsb.draw(canvas);
                        dsb.buttonPress();
                    }
                }
                else{
                }

            }
            //-------------------
            if(!castSpell.completed() && !castSpell.Name.equals("none")){
                castSpell.draw(canvas);
            }
            if(canAttack && isAttacking){
                if(AttackingClass.equals("Melee")){
                    switch(Phase){
                        case 0:
                            moveController = getLocation(battleSequencePoints.get("Attacker").x,battleSequencePoints.get("Attacker").y,battleSequencePoints.get("Attacking").x,battleSequencePoints.get("Attacking").y,24,currentStep);
                            battleSequence.get("Attacker").offsetTo(moveController.x,moveController.y);
                            canvas.drawRect(battleSequence.get("Attacker"),blue);
                            if(currentStep == 25){Phase++;currentStep=0;}
                            currentStep++;
                            break;
                        case 1:
                            canvas.drawRect(battleSequence.get("Attacker"),blue);
                            if(currentStep == 15){
                                if(Attacking.equals("slot1")){slot1.applyDamage(5);}
                                else if(Attacking.equals("slot2")){slot2.applyDamage(5);}
                                else if(Attacking.equals("slot3")){slot3.applyDamage(5);}
                            }
                            if(currentStep == 25){Phase++;currentStep = -1;}
                            currentStep++;
                            break;
                        case 2:
                            if(currentStep == 0){
                                battleSequencePoints.put("Attacker",new Point(battleSequence.get("Attacker").left,battleSequence.get("Attacker").top));
                                battleSequencePoints.put("Attacking",new Point(Attacker.left,Attacker.top));
                            }
                            moveController = getLocation(battleSequencePoints.get("Attacker").x,battleSequencePoints.get("Attacker").y,battleSequencePoints.get("Attacking").x,battleSequencePoints.get("Attacking").y,25,currentStep);
                            battleSequence.get("Attacker").offsetTo(moveController.x,moveController.y);
                            canvas.drawRect(battleSequence.get("Attacker"),blue);
                            currentStep++;
                            if(currentStep == 25){Phase++;currentStep=0;canAttack=true;Standby=false;isAttacking=false;canAttack=false;}
                            break;
                    }
                }
                else{
                    switch(Phase){
                        case 0:
                            moveController = getLocation(battleSequencePoints.get("Attacker").x,battleSequencePoints.get("Attacker").y,battleSequencePoints.get("Attacking").x,battleSequencePoints.get("Attacking").y,24,currentStep);
                            battleSequence.get("Attacker").offsetTo(moveController.x+battleSequence.get("Attacker").width(),moveController.y-(int)(battlePlayer1.height()/2));
                            canvas.drawRect(battleSequence.get("Attacker"),blue);
                            if(currentStep == 25){Phase++;currentStep=0;}
                            currentStep++;
                            break;
                        case 1:
                            canvas.drawRect(battleSequence.get("Attacker"),blue);
                            if(currentStep == 15){}
                            if(currentStep == 25){Phase++;currentStep = -1;}
                            currentStep++;
                            break;
                        case 2:
                            break;
                    }
                }
            }
            if(!P1.canMove()&&!P2.canMove()&&!P3.canMove()){
                display = "none";
                canvas.drawRect(bg,fade);
            }
            if(!slot1.isAlive()&&!slot2.isAlive()&&!slot3.isAlive()){
                Standby=true;
                battleEnd(1,canvas);
            }
        }
        Point getLocation(int startX, int startY,int endX,int endY,int totalSteps, int currentStep){
            int xInterval = (endX-startX)/totalSteps;
            int yInterval = (endY-startY)/totalSteps;
            Point returning = new Point(startX+(xInterval*currentStep),startY+(yInterval*currentStep));
            return returning;
        }
        int getSlot(){
            return slotSelected;
        }
        void getPress(Canvas canvas){
            if(Standby && !canAttack){
                if(flee.contains(touchX,touchY)){
                    battleEnd(0,canvas);
                }
                else if(attack.contains(touchX,touchY)){
                    display = "attack";
                }
                else if(spells.contains(touchX,touchY)){
                    display = "spells";
                }
                else if(classSpells.contains(touchX,touchY)){
                    display = "class";
                }
                else if(items.contains(touchX,touchY)){
                    display = "items";
                }
            }
        }
        void battleEnd(int Condition, Canvas canvas){
            Rect cont = new Rect(0,0,getScreenWidth()/4,getScreenWidth()/8);
            cont.offsetTo(getScreenWidth()-(getScreenWidth()/4)-10,getScreenHeight()-(getScreenWidth()/8)-10);
            switch(Condition){
                case 0:
                    break;
                case 1:
                    canvas.drawRect(0,0,getScreenWidth(),getScreenHeight(),blue);
                    canvas.drawRect(ExpBox,red);
                    canvas.drawRect(cont,red);
                    //canvas.drawRect(ExpBox1,expSlot);
                    canvas.drawRect(ExpBox2,expSlot);
                    //canvas.drawRect(ExpBox3,expSlot);

                    if(giveExp){
                        giveExp = false;

                    }
                    if(giveExpDisplay){
                        if(t<totalExp){
                            player.earnExp(1);
                            player2.earnExp(1);
                            player3.earnExp(1);
                            if(t == totalExp){
                                giveExpDisplay = false;
                            }
                            t++;

                        }
                    }
                    canvas.drawText("Player 1 - " +player.Experience + "/" + player.nextLevel + "..." + player.Level,ExpBox1.left+20,ExpBox1.top+20,black);
                    canvas.drawText("Player 2 - " +player2.Experience + "/" + player2.nextLevel + "..." + player2.Level,ExpBox2.left+20,ExpBox2.top+20,black);
                    canvas.drawText("Player 3 - " +player3.Experience + "/" + player3.nextLevel + "..." + player3.Level,ExpBox3.left+20,ExpBox3.top+20,black);
                    checkLevelUp();

                    if(cont.contains(touchX,touchY)&&!send&& t==totalExp){
                        send = true;
                    }
                    if(send){
                        transitionOut(15,canvas,"Map");
                    }
                    break;
                case 2:
                    break;
            }
        }
        void checkLevelUp(){
            if(player.Experience == player.nextLevel){
                player.nextLevel = (player.Level)*(5) + player.nextLevel;
                player.Experience = 0;
                player.Level++;
            }
            if(player2.Experience == player2.nextLevel){
                player2.nextLevel = (player2.Level)*(5) + player2.nextLevel;
                player2.Experience = 0;
                player2.Level++;
            }
            if(player3.Experience == player3.nextLevel){
                player3.nextLevel = (player3.Level)*(5) + player3.nextLevel;
                player3.Experience = 0;
                player3.Level++;
            }
        }
    }
    class Enemy{
        private int HP;
        private int Damage;
        private String Name;
        private int SpellP;
        private int Def;
        private int mDef;
        private int Exp;
        private Rect Space;
        private boolean Alive = true;
        Enemy(int a,int b, String c,int d, int e, int f,Rect g,int h){
            HP=a;
            Damage=b;
            Name=c;
            SpellP=d;
            Def=e;
            mDef=f;
            Space = g;
            Alive = true;
            Exp=h;
        }
        void draw(Canvas canvas){
            if(Alive){
                canvas.drawRect(Space,red);
                canvas.drawText(Integer.toString(HP),Space.left,Space.top,black);
            }

        }
        boolean isAlive(){
            return Alive;
        }
        void applyDamage(int Receive){
            HP=HP-Receive;
            if(HP <= 0){
                Alive=false;
            }
        }
        void setRect(Point pos){
            Space.offsetTo(pos.x,pos.y);
            Space.offset((Space.width()/2)*-1,(Space.height()/2)*-1);
        }
        Enemy get(){
            return new Enemy(HP,Damage,Name,SpellP,Def,mDef,Space,Exp);
        }
    }
    class CastSpell{
        private String Name;
        private Spell spell;
        private Rect spellBox = new Rect();
        private int Total,current;
        private Point From,To;
        public boolean spellComplete = false;
        private int[] spellLoc = new int[2];
        private int Phase;
        private int pre,during,post,preC,postC;
        private Enemy Target = null;

        CastSpell(String name,int total,Point from,Point to,Enemy target){
            setSpells();
            Phase = 0;
            From = from;
            To = to;
            spellComplete=false;
            Name = name;
            Total = total;
            current = 0;
            spell = getSpell.get(Name);
            //spell = new Spell("none",100,100,10);
            spellBox.set(0,0,spell.getWidth(),spell.getHeight());
            pre = spell.getPre();
            during = spell.getDuring();
            post = spell.getPost();
            preC = 0;
            postC = 0;
            Target = target;
        }
        boolean completed(){
            return spellComplete;
        }

        void draw(Canvas canvas){
            if(Name.equals("none")){spellComplete=true;}
            if(!Name.equals("none")) {
                if(spell.Type.equals("Projectile")){
                    switch(Phase){
                        case 0:
                            if(preC == pre) {
                                Phase++;
                            }
                            else {
                                preC++;
                            }
                            break;
                        case 1:
                            spellLoc = getLocation(From.x, From.y, To.x, To.y, during, current);
                            spellBox.offsetTo(spellLoc[0]-(spellBox.width()/2), spellLoc[1]-(spellBox.height()/2));
                            canvas.drawRect(spellBox, red);
                            current++;
                            break;
                        case 2:
                            if(postC == post) {
                                Phase++;
                                if(Target == battle.slot1){
                                    battle.slot1.applyDamage(spell.getDmg());
                                }
                                else if(Target == battle.slot2){
                                    battle.slot2.applyDamage(spell.getDmg());
                                }
                                else if(Target == battle.slot3){
                                    battle.slot3.applyDamage(spell.getDmg());
                                }
                            }
                            else {
                                postC++;

                            }
                            break;
                        case 3:
                            spellComplete=true;
                            spell = getSpell.get("none");
                            battle.Standby=false;
                            break;
                    }
                }
                else if(spell.Type.equals("Beam")){
                /*
                *   public static Bitmap RotateBitmap(Bitmap source, float angle)
                    {
                        Matrix matrix = new Matrix();
                        matrix.postRotate(angle);
                        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
                    } Rotates an image and returns it

                    public static double GetAngleOfLineBetweenTwoPoints(Point.Double p1, Point.Double p2) {
                        double xDiff = p2.x - p1.x;
                        double yDiff = p2.y - p1.y;
                        return Math.toDegrees(Math.atan2(yDiff, xDiff));
                    } Gets the angle between two points
                    */
                }
            }
        }
        void setSpells(){
            getSpell.put("Fire", new Spell("Fire", 100, 100, 50,"Projectile",25,25,25));
            getSpell.put("none",new Spell("none",100,100,10,"Blank",0,0,0));
        }
        int[] getLocation(int startX, int startY,int endX,int endY,int totalSteps, int currentStep){
            int xInterval = (endX-startX)/totalSteps;
            int yInterval = (endY-startY)/totalSteps;
            int[] returning = {
                    startX+(xInterval*currentStep),
                    startY+(yInterval*currentStep)
            };
            if(xInterval > 0 &&returning[0] >= endX){Phase++;}
            if(xInterval < 0 &&returning[0] <= endX){Phase++;}
            return returning;
        }

    }
    class Spell{
        private int Dmg;
        private int W,H;
        private String Name;
        private String Type;
        private int Pre, During ,Post;
        Spell(String name, int w, int h, int dmg, String t, int pre, int during, int post){
            Name = name;
            Type = t;
            //Image=
            W = w;
            H=h;
            Dmg=dmg;
            Pre = pre;
            During = during;
            Post = post;
        }
        int getWidth(){
            return W;
        }
        int getHeight(){
            return H;
        }

        public int getPre() {
            return Pre;
        }

        public int getDuring() {
            return During;
        }

        public int getPost() {
            return Post;
        }

        public int getDmg() {
            return Dmg;
        }
    }
    class Tooltip{
        private Rect box = new Rect();
        public boolean boxShow = false;
        Tooltip(){
            box.set(touchX,touchY,touchX+(getScreenWidth()/3),touchY+(getScreenWidth()/3));
            //box.set(0,0,100,100);
        }
        void draw(Canvas canvas){
            box.offsetTo(touchX,touchY);
            box.offset(0-box.width(),0-box.height());
            canvas.drawRect(box,red);
        }
    }
    class Sprite{
        private int mapX;
        private int mapY;
        private int sWidth;
        private int sHeight;
        private int worldX;
        private int worldY;
        private String Name;
        private String Level;
        private Bitmap Image;
        private String[] dialogLib;
        private Rect sBox;
        private int moveC;
        private int sSpeed = 2;
        private boolean sMoving;
        private int autoC;
        private String[] direc = {"up","down","left","right"};
        private int direcInt;
        private Rect sCol = new Rect();
        private String[] route = {"left","left","down","down","right","right","up","up"};
        private int cRoute = 0;
        private String[][] dialogText = {
                {"first line","second line"},
                {"new set of lines",":D"}
        };
        private int index = 0;
        Sprite(String level, String name,int x,int y){
            Name =  name;
            Level = level;
            cRoute = 0;
            sMoving = false;
            autoC = 0;
            moveC = 0;
            mapX = x;
            mapY = y;
            sWidth = map.tileSize;
            sHeight = map.tileSize;
            worldX = mapX*map.tileSize;
            worldY = mapY*map.tileSize;
            sBox = new Rect(worldX,worldY,worldX+sWidth,worldY+sHeight);
        }
        String getText(int i, int j){
            return dialogText[index][j];
        }
        void draw(Canvas canvas){
            canvas.drawRect(sBox,black);
        }
        void MoveReset(){
            moveC = 0;
            autoC = 0;
            cRoute++;
            if(cRoute == route.length){cRoute=0;}
        }
        int getMoveC(){
            return moveC;
        }
        void Speech(){
            index++;
            if(index == dialogText.length){index=0;};
        }
        void autoMove(){
            if(sMoving == false){
                if(autoC == 5){
                    sMoving = true;
                    direcInt = rand.nextInt(4);
                }
                autoC++;
            }
            else{
                //Move(direc[direcInt]);
                Move(route[cRoute]);
            }
        }
        boolean checkMapCollision(int x,int y){
            String[] array = {"1","2"};
            List<String> walkable = Arrays.asList(array);
            if(walkable.contains(Integer.toString(map.getCurrentMapArray()[y][x]))){
                return true;
            }
            else{
                return false;
            }
        }
        boolean checkEntityCollision(String e){
            if(e.equals("left")){
                sCol.set(worldX,worldY+5,worldX+5,worldY+sHeight-5);
                if(sCol.intersect(player.drawPlayer)){
                    return false;
                }
                else{
                    return true;
                }
            }
            else if(e.equals("right")){
                sCol.set(worldX+sWidth-5,worldY+5,worldX+sWidth,worldY+sHeight-5);
                if(sCol.intersect(player.drawPlayer)){
                    return false;
                }
                else{
                    return true;
                }
            }
            else if(e.equals("up")){
                sCol.set(worldX+5,worldY,worldX+sWidth-5,worldY+5);
                if(sCol.intersect(player.drawPlayer)){
                    return false;
                }
                else{
                    return true;
                }
            }
            else if(e.equals("down")){
                sCol.set(worldX+5,worldY+sHeight-5,worldX+sWidth-5,worldY+sHeight);
                if(sCol.intersect(player.drawPlayer)){
                    return false;
                }
                else{
                    return true;
                }
            }
            else{
                return false;
            }
        }
        void Move(String direction){
            if(moveC == 0){
                sMoving = true;
                if(direction.equals("left")&& mapX-1 != -1 && checkMapCollision(mapX-1,mapY)){
                    mapX--;
                }
                else if(direction.equals("right") && mapX+1 != map.MapListArray.get(map.getCurrentMap())[mapY].length && checkMapCollision(mapX+1,mapY)){
                    mapX++;
                }
                else if(direction.equals("up")&& mapY-1 != -1 && checkMapCollision(mapX,mapY-1)){
                    mapY--;
                }
                else if(direction.equals("down") && mapY+1 != map.MapListArray.get(map.getCurrentMap()).length && checkMapCollision(mapX,mapY+1)){
                    mapY++;
                }
                moveC=1;
            }
            else if(moveC == 1 && controls.canMove){
                if(direction.equals("left") && checkEntityCollision("left")){
                    if(worldX > mapX*map.tileSize){
                        worldX -= sSpeed;
                        sBox.offsetTo(worldX,worldY);
                    }
                    else if(worldX == mapX*map.tileSize){
                        moveC=2;
                    }
                }
                else if(direction.equals("right") && checkEntityCollision("right")){
                    if(worldX < mapX*map.tileSize){
                        worldX += sSpeed;
                        sBox.offsetTo(worldX,worldY);
                    }
                    else if(worldX == mapX*map.tileSize){
                        moveC=2;
                    }
                }
                else if(direction.equals("up") && checkEntityCollision("up")){
                    if(worldY > mapY*map.tileSize){
                        worldY -= sSpeed;
                        sBox.offsetTo(worldX,worldY);
                    }
                    else if(worldY == mapY*map.tileSize){
                        moveC=2;
                    }
                }
                else if(direction.equals("down") && checkEntityCollision("down")){
                    if(worldY < mapY*map.tileSize){
                        worldY += sSpeed;
                        sBox.offsetTo(worldX,worldY);
                    }
                    else if(worldY == mapY*map.tileSize){
                        moveC=2;
                    }
                }
            }
            else if(moveC == 2){
                sMoving = false;
                MoveReset();
            }
        }
    }
    class MapWarp{
        private String To,Current;
        private int mapX,mapY,toX,toY;
        private Rect HitBox = new Rect();
        private Rect HitBoxCenter = new Rect();
        MapWarp(int x, int y, int q, int w, String c, String to){
            To = to;
            Current = c;
            mapX = x;
            mapY = y;
            toX = q;
            toY = w;
            HitBox.set(mapX*map.tileSize,mapY*map.tileSize,(mapX*map.tileSize)+map.tileSize,(mapY*map.tileSize)+map.tileSize);
            HitBoxCenter.set(HitBox.centerX()-3,HitBox.centerY()-3,HitBox.centerX()+3,HitBox.centerY()+3);
        }
        void update(){
            if(map.getCurrentMap().equals(Current)){
                if(Rect.intersects(HitBoxCenter,player.drawPlayer)){
                    map.setCurrentMap(To);
                    xOffset = (getScreenWidth()/2-(map.tileSize/2))-(toX-1)*map.tileSize;
                    yOffset = (getScreenHeight()/2-(map.tileSize/2))-(toY-1)*map.tileSize;
                }
            }
        }
    }
    class Chest{
        private Rect sprite = new Rect();
        private int Rarity = 1;
        private String Level;
        private boolean Opened = false;
        private Weapon Reward = null;
        Chest(int x, int y,int r,String level){
            sprite.set((x-1)*map.tileSize,(y-1)*map.tileSize,x*map.tileSize,y*map.tileSize);
            Rarity = r;
            Level = level;
            Reward = new Weapon("rand", "rand", "rand", Rarity, 0, 0, 0, 0, 0, 0, "None", "None", "None", "None");
        }
        void draw(Canvas canvas){
            canvas.drawRect(sprite,red);
        }
        void update(){
            if(controls.inter.contains(touchX,touchY) && Rect.intersects(sprite,player.drawPlayer) && !Opened){
                Opened = true;
                inv.addWeapon(Reward);
            }
        }
    }
    class Skills{
        private Rect player1Skills = new Rect(0,0,getScreenWidth()/3,(getScreenWidth()/3)/3);
        private int spacer =  (int)(player1Skills.width()/7.6);
        private Rect player3Skills = new Rect(getScreenWidth()-(getScreenWidth()/3),0,getScreenWidth(),(getScreenWidth()/3)/3);
        private Rect player2Skills = new Rect(player1Skills.right,0,player3Skills.left,player1Skills.bottom);
        private Rect bottomBar = new Rect(0,getScreenHeight()-(int)(getScreenHeight()/2.48),getScreenWidth(),getScreenHeight());
        private Rect leftBar = new Rect(0,player1Skills.bottom,spacer,bottomBar.top);
        private Rect rightBar = new Rect(getScreenWidth()-spacer,player3Skills.bottom,getScreenWidth(),bottomBar.top);
        private Rect skillsArea = new Rect(leftBar.right,player2Skills.bottom+spacer,rightBar.left,bottomBar.top);
        private Rect infoArea = new Rect(leftBar.right,bottomBar.top+spacer,rightBar.left,getScreenHeight()-(spacer*4));
        private Rect Learn = new Rect(getScreenWidth()-spacer-player1Skills.width(),getScreenHeight()-spacer-player1Skills.height(),getScreenWidth()-spacer,getScreenHeight()-spacer);
        private int t = 0;
        private int offsetX = 0,offsetY = 0;
        private int offsetXD = 0,offsetYD = 0;
        public boolean move = false;
        public boolean setPos = false;
        private Point before = new Point();
        private Point present = new Point();
        Skills(){

        }
        int getXOffset(){
            return present.x-before.x;
        }
        int getYOffset(){
            return present.y-before.y;
        }
        void draw(Canvas canvas){
            canvas.save();
            if(skillsArea.contains(touchX,touchY)){
                if(setPos&&touch){
                    before.x = touchX;
                    before.y = touchY;
                    setPos=false;
                }
                else if(!setPos&&!touch){
                    setPos = true;
                }
                if(touch){
                    present.x = touchX;
                    present.y = touchY;
                    offsetXD=offsetXD+getXOffset();
                    offsetYD=offsetYD+getYOffset();
                }
                canvas.translate(offsetX+offsetXD, offsetY+offsetYD);
                before.x = present.x;
                before.y = present.y;
            }
            canvas.drawRect(100,100,200,200,blue);

            canvas.restore();
            canvas.drawRect(player1Skills,red);
            canvas.drawRect(player2Skills,red);
            canvas.drawRect(player3Skills,red);
            canvas.drawRect(leftBar,black);
            canvas.drawRect(rightBar,black);
            canvas.drawRect(bottomBar,black);
            canvas.drawRect(infoArea,blue);
            canvas.drawRect(Learn,blue);
            back.draw(canvas);
        }
    }
}