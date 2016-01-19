package com.nateglims.beantemp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.util.ArrayList;
import java.util.List;


public class TempActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        BeanManager.getInstance().startDiscovery(discoveryListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_temp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    final List<Bean> beans = new ArrayList<>();

    BeanDiscoveryListener discoveryListener = new BeanDiscoveryListener() {
        @Override
        public void onBeanDiscovered(Bean bean, int rssi) {
            beans.add(bean);
        }

        @Override
        public void onDiscoveryComplete() {
            for (Bean bean : beans) {
                System.out.println(bean.getDevice().getName());   // "Bean"              (example)
                System.out.println(bean.getDevice().getAddress());    // "B4:99:4C:1E:BC:75" (example)
                bean.connect(getApplicationContext(), listener);
            }

        }
    };

    BeanListener listener = new BeanListener() {
        @Override
        public void onConnected() {
            System.out.println("Connected!");

            beans.get(0).readDeviceInfo(new Callback<DeviceInfo>() {
                @Override
                public void onResult(DeviceInfo deviceInfo) {
                    System.out.println(deviceInfo.hardwareVersion());
                    System.out.println(deviceInfo.firmwareVersion());
                    System.out.println(deviceInfo.softwareVersion());
                }
            });

            beans.get(0).readTemperature(new Callback<Integer>() {
                @Override
                public void onResult(Integer temp) {
                    System.out.println("Current temp: " + temp + "°C");
//                    TextView textView = addView();
//                    textView.setText("Current temp: " + temp + "°C");
//                    RelativeLayout layout = (RelativeLayout) findViewById(R.id.tempcontent);
//                    layout.addView(textView);

                }
            });

        }

        @Override
        public void onConnectionFailed() {

        }

        @Override
        public void onDisconnected() {
            System.out.println("Disconnected!");
        }

        @Override
        public void onSerialMessageReceived(byte[] bytes) {

        }

        @Override
        public void onScratchValueChanged(ScratchBank scratchBank, byte[] bytes) {

        }

        @Override
        public void onError(BeanError beanError) {

        }
    };

    public TextView addView() {
        TextView textView = new TextView(this);
        return textView;
    }
}
