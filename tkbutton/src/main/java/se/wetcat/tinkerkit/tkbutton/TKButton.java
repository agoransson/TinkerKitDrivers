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

import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.List;

/**
 * @author andreasgoransson0@gmail.com
 */
public class TKButton implements AutoCloseable {

    private static final String TAG = TKButton.class.getSimpleName();

    private final String mGpioName;

    private Gpio mGpio;

    private TKButtonListener mListener;

    private Object mListenerLock = new Object();

    protected void setListener(TKButtonListener listener) {
        mListener = listener;
    }

    protected interface TKButtonListener {
        void onButtonStateChanged(boolean newState);
    }

    protected TKButton(String gpioName) {
        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();

        this.mGpioName = gpioName;

        List<String> gpioList = peripheralManagerService.getGpioList();

        if (!gpioList.contains(mGpioName)) {
            throw new RuntimeException("The GPIO pin was not found on this hardware");
        }

        try {
            mGpio = peripheralManagerService.openGpio(mGpioName);

            mGpio.setDirection(Gpio.DIRECTION_IN);

            mGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);

            mGpio.registerGpioCallback(mGpioCallback);
        } catch (IOException e) {
            Log.d(TAG, "Failed to find the GPIO, even though it should exist.", e);
        }
    }

    private void emitValue(boolean state) {
        if (mListener == null) {
            return;
        }

        synchronized (mListenerLock) {
            mListener.onButtonStateChanged(state);
        }
    }

    private GpioCallback mGpioCallback = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {

            try {
                boolean state = gpio.getValue();
                emitValue(state);
            } catch (IOException e) {
                Log.e(TAG, "Failed to read GPIO value", e);
            }

            return true;
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
            super.onGpioError(gpio, error);
        }
    };

    @Override
    public void close() throws Exception {
        if (mGpio != null) {
            mGpio.close();
        }
    }

}
