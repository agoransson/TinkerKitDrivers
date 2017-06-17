package se.wetcat.tinkerkit.tkbutton;

/*
    Copyright 2017 Andreas Göransson

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import android.view.InputDevice;
import android.view.KeyEvent;

import com.google.android.things.userdriver.InputDriver;
import com.google.android.things.userdriver.UserDriverManager;

import static se.wetcat.tinkerkit.tkbutton.BuildConfig.DRIVER_NAME;
import static se.wetcat.tinkerkit.tkbutton.BuildConfig.DRIVER_VERSION;

/**
 * @author andreasgoransson0@gmail.com
 */
public class TKButtonDriver implements AutoCloseable {

    private static final String TAG = TKButtonDriver.class.getSimpleName();

    private TKButton mDevice;

    private InputDriver mDriver;

    private int mKeycode;

    public TKButtonDriver(String gpio, int keycode) {
        mDevice = new TKButton(gpio);
        mKeycode = keycode;
    }

    public void register() {
        if (mDevice == null) {
            throw new IllegalStateException("cannot registered closed driver");
        }
        if (mDriver == null) {
            mDriver = build(mDevice, mKeycode);
            UserDriverManager.getManager().registerInputDriver(mDriver);
        }
    }

    public void unregister() {
        if (mDriver != null) {
            UserDriverManager.getManager().unregisterInputDriver(mDriver);
            mDriver = null;
        }
    }

    @Override
    public void close() throws Exception {
        unregister();

        if (mDevice != null) {
            try {
                mDevice.close();
            } finally {
                mDevice = null;
            }
        }
    }

    static InputDriver build(TKButton tkButton, final int keyCode) {
        final InputDriver inputDriver = new InputDriver.Builder(InputDevice.SOURCE_CLASS_BUTTON)
                .setName(DRIVER_NAME)
                .setVersion(DRIVER_VERSION)
                .setKeys(new int[]{keyCode})
                .build();

        tkButton.setListener(new TKButton.TKButtonListener() {
            @Override
            public void onButtonStateChanged(boolean newState) {
                if (newState) {
                    inputDriver.emit(new KeyEvent[]{
                            new KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
                    });
                } else {
                    inputDriver.emit(new KeyEvent[]{
                            new KeyEvent(KeyEvent.ACTION_UP, keyCode)
                    });
                }
            }
        });

        return inputDriver;
    }

}
