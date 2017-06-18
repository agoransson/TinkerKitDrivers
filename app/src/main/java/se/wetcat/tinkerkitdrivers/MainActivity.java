package se.wetcat.tinkerkitdrivers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import se.wetcat.tinkerkit.tkbutton.TKButtonDriver;
import se.wetcat.tinkerkit.tkled.TKLed;
import se.wetcat.tinkerkit.tkrelay.TKRelay;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TKButtonDriver tkButtonDriver;

    private TKRelay tkRelay;

    private TKLed tkLed;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mLightRef = mRootRef.child("light");

    private boolean mLightState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tkButtonDriver = new TKButtonDriver("BCM23", KeyEvent.KEYCODE_A);
        tkButtonDriver.register();

        tkRelay = new TKRelay("BCM24", TKRelay.Connection.NORMAL_CLOSED);

        tkLed = new TKLed("BCM21");
    }

    @Override
    protected void onStart() {
        super.onStart();

        mLightRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    mLightState = dataSnapshot.getValue(Boolean.class);

                    if (mLightState) {
                        tkRelay.toggleOn();
                        tkLed.toggleOn();
                    } else {
                        tkRelay.toggleOff();
                        tkLed.toggleOff();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyDown(" + keyCode + ", event)");

        if (keyCode == KeyEvent.KEYCODE_A) {
            mLightRef.setValue(!mLightState);
        }

        return super.onKeyDown(keyCode, event);
    }
}
