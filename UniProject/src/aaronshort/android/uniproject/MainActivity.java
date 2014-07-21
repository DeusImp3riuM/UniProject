package aaronshort.android.uniproject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
	GameView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        view = new GameView(this);
        setContentView(view);
    }
    protected void onPause(){
    	super.onStop();
    	Toast toast = Toast.makeText(this, "test", Toast.LENGTH_SHORT);
    	toast.show();
    }
}
