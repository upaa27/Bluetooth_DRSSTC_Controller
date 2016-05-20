package phoenixkang.teslacontroller;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.os.AsyncTask;
import android.bluetooth.BluetoothDevice;
import android.widget.TextView;


import java.io.IOException;
import java.util.UUID;

public class Interrupter extends AppCompatActivity {
    boolean burstCheck = false;
    Button oneShot;
    Switch onOff;
    SeekBar onTime, offTime, burstLength, burstDelay;
    CheckBox burstEn;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    String address = null;
    TextView onTimeVal, offTimeVal;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        address = newint.getStringExtra(ListDevices.EXTRA_ADDRESS);
        setContentView(R.layout.activity_interrupter);

        oneShot = (Button) findViewById(R.id.oneShot);

        onTime = (SeekBar) findViewById(R.id.seekBar);
        onTime.setOnSeekBarChangeListener(onTimeListener);

        offTime = (SeekBar) findViewById(R.id.seekBar2);
        offTime.setOnSeekBarChangeListener(offTimeListener);

        burstDelay = (SeekBar) findViewById(R.id.seekBar3);
        burstDelay.setOnSeekBarChangeListener(burstDelayListener);
        burstDelay.setEnabled(false);

        burstLength = (SeekBar) findViewById(R.id.seekBar4);
        burstLength.setOnSeekBarChangeListener(burstLengthListener);
        burstLength.setEnabled(false);

        onOff = (Switch) findViewById(R.id.enSwitch);
        onOff.setOnCheckedChangeListener(onOffListener);

        burstEn = (CheckBox) findViewById(R.id.burstEn);
        burstEn.setOnCheckedChangeListener(burstEnListener);

        onTimeVal = (TextView) findViewById(R.id.onTimeVal);

        offTimeVal = (TextView) findViewById(R.id.offTimeVal);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        try {
            btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        } catch (IOException e) {
            e.printStackTrace();
        }
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        try {
            btSocket.connect();//start connection
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Switch.OnCheckedChangeListener onOffListener =
            new Switch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        onTime.setProgress(0);
                        onTime.setEnabled(false);
                        offTime.setEnabled(false);
                    }
                    else {
                        onTime.setEnabled(true);
                        offTime.setEnabled(true);
                    }
                }
            };

    private CheckBox.OnCheckedChangeListener burstEnListener =
            new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        burstDelay.setEnabled(true);
                        burstLength.setEnabled(true);
                    }
                    else {
                        burstDelay.setEnabled(false);
                        burstLength.setEnabled(false);
                    }
                }
            };

    private SeekBar.OnSeekBarChangeListener onTimeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    try {
                        onTimeVal.setText((int)(progress*2.3) + 1 + " μs");
                        btSocket.getOutputStream().write(progress);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

    //Sets a listener for progress changes for the off time seekBar and sends them to the reciever
    private SeekBar.OnSeekBarChangeListener offTimeListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    try {
                        offTimeVal.setText((progress/10)+4 + " ms");
                        btSocket.getOutputStream().write(progress+101);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

    private SeekBar.OnSeekBarChangeListener burstLengthListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    try {
                        btSocket.getOutputStream().write(progress + 202);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };

    private SeekBar.OnSeekBarChangeListener burstDelayListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    try {
                        btSocket.getOutputStream().write((progress + 303));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };
}

