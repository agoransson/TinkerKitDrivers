package se.wetcat.examples.tinkerkitdrivers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import se.wetcat.tinkerkit.tkbutton.TKButtonDriver;

public class MainActivity extends Activity {

    private TKButtonDriver tkButtonDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tkButtonDriver = new TKButtonDriver("BCM23", KeyEvent.KEYCODE_CALL);
        tkButtonDriver.register();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("ANTE", "onKeydown(" + keyCode + ", " + event.getCharacters() + ")");
        return super.onKeyDown(keyCode, event);
    }
}
