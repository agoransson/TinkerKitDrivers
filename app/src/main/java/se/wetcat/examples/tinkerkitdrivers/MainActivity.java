package se.wetcat.examples.tinkerkitdrivers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import se.wetcat.tinkerkit.tkbutton.TKButtonDriver;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
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
