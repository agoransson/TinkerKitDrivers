package se.wetcat.tinkerkit.tkrelay;

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
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.List;

/**
 * @author andreasgoransson0@gmail.com
 */
public class TKRelay implements AutoCloseable {

    private static final String TAG = TKRelay.class.getSimpleName();

    private final String mGpioName;

    private Gpio mGpio;

    private Connection mConnectionType;

    public enum Connection {
        NORMAL_OPEN, NORMAL_CLOSED
    }

    public TKRelay(String gpioName, Connection type) {
        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();

        this.mGpioName = gpioName;

        this.mConnectionType = type;

        List<String> gpioList = peripheralManagerService.getGpioList();

        if (!gpioList.contains(mGpioName)) {
            throw new RuntimeException("The GPIO pin was not found on this hardware");
        }

        try {
            mGpio = peripheralManagerService.openGpio(mGpioName);

            if (mConnectionType == Connection.NORMAL_OPEN) {
                mGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            } else if (mConnectionType == Connection.NORMAL_CLOSED) {
                mGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            }
        } catch (IOException e) {
            Log.d(TAG, "Failed to find the GPIO, even though it should exist.", e);
        }
    }

    public void toggleOn() {
        Log.e(TAG, "toggleOn()");

        try {
            if (mConnectionType == Connection.NORMAL_OPEN) {
                mGpio.setValue(true);
            } else if (mConnectionType == Connection.NORMAL_CLOSED) {
                mGpio.setValue(true);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to toggle relay to on", e);
        }
    }

    public void toggleOff() {
        Log.e(TAG, "toggleOff()");

        try {
            if (mConnectionType == Connection.NORMAL_OPEN) {
                mGpio.setValue(false);
            } else if (mConnectionType == Connection.NORMAL_CLOSED) {
                mGpio.setValue(false);
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to toggle relay to off", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (mGpio != null) {
            mGpio.close();
        }
    }

}
