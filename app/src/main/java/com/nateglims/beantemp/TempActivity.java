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
import java.util.Arrays;
import java.util.List;


public class TempActivity extends ActionBarActivity {

    Bean myBean;
    String myBeanName = "destroyerBean";

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


    BeanDiscoveryListener discoveryListener = new BeanDiscoveryListener() {
        @Override
        public void onBeanDiscovered(Bean bean, int rssi) {
            if (bean.getDevice().getName().equals(myBeanName)) {
                System.out.println("Found bean: " + bean.getDevice().getName());
                myBean = bean;
            }
        }

        @Override
        public void onDiscoveryComplete() {
                myBean.connect(getApplicationContext(), listener);
        }
    };

    BeanListener listener = new BeanListener() {
        @Override
        public void onConnected() {
            System.out.println("Connected!");
            updateTemp();
        }

        @Override
        public void onConnectionFailed() {
            System.out.println("Failed to connect!");
        }

        @Override
        public void onDisconnected() {
            System.out.println("Disconnected!");
        }

        @Override
        public void onSerialMessageReceived(byte[] bytes) {
            System.out.println("Got serial message: " + new String(bytes));
        }

        @Override
        public void onScratchValueChanged(ScratchBank scratchBank, byte[] bytes) {
            System.out.println("Scratch value changed.");
        }

        @Override
        public void onError(BeanError beanError) {
            System.out.println("Bean error.");
        }
    };

    public void updateTemp() {
        myBean.readTemperature(new Callback<Integer>() {
            @Override
            public void onResult(Integer result) {
                System.out.println("Got temperature of " + result.toString());
                TextView textView = (TextView) findViewById(R.id.tempTextView);
                textView.setText(result.toString() + "°C");
            }
        });
    }
}
