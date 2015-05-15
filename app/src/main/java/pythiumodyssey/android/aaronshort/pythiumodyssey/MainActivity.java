package pythiumodyssey.android.aaronshort.pythiumodyssey;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
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
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Quit Game")
                .setMessage("Are you sure you want to quit the game?This will NOT save progress.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which){
                        view.global.release();
                        finish();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }
}
