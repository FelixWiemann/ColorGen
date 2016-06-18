package com.nepumuk.colorgen;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class ColorGen extends ActionBarActivity {

	private SeekBar seekBarRed;
	private SeekBar seekBarGreen;
	private SeekBar seekBarBlue;
	private TextView textViewRed;
	private TextView textViewGreen;
	private TextView textViewBlue;
	private TextView textViewRedDis;
	private TextView textViewGreenDis;
	private TextView textViewBlueDis;
	private TextView hexVal;

	private LinearLayout lv;
	private Camera camera;

	private int[] hexVals = new int[] {255,255,255};

	private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new  SeekBar.OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			switch (seekBar.getId()){
				case R.id.seekBar:
					textViewRed.setText(String.valueOf(progress));
					hexVals[0] = progress;
					break;
				case R.id.seekBar2:
					textViewGreen.setText(String.valueOf(progress));
					hexVals[1] = progress;
					break;
				case R.id.seekBar3:
					textViewBlue.setText(String.valueOf(progress));
					hexVals[2] = progress;
					break;
			}
			SetBackGroundColor(hexVals);

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_gen);
		textViewRed= (TextView) findViewById(R.id.textView);
		textViewGreen= (TextView) findViewById(R.id.textView2);
		textViewBlue= (TextView) findViewById(R.id.textView3);
		seekBarRed = (SeekBar) findViewById(R.id.seekBar);
		seekBarGreen= (SeekBar) findViewById(R.id.seekBar2);
		seekBarBlue= (SeekBar) findViewById(R.id.seekBar3);
		lv = (LinearLayout) findViewById(R.id.LinLay);
		seekBarBlue.setOnSeekBarChangeListener(seekBarChangeListener);
		seekBarRed.setOnSeekBarChangeListener(seekBarChangeListener);
		seekBarGreen.setOnSeekBarChangeListener(seekBarChangeListener);
		 textViewRedDis=(TextView) findViewById(R.id.textView4);
		 textViewGreenDis=(TextView) findViewById(R.id.textView5);
		 textViewBlueDis=(TextView) findViewById(R.id.textView6);
		hexVal = (TextView) findViewById(R.id.HexVal);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_color_gen, menu);
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

		if (id == R.id.cameraDetectColor) {
			startActivity(new Intent(this,CameraPreviewActivity.class));
			//checkCameraHardware(this);
			return true;
		}if (id == R.id.ReturnColorCode) {
			Intent intent = new Intent();
			intent.putExtra("DATA",Color.rgb(hexVals[0],hexVals[1],hexVals[2]));
			setResult(ActionBarActivity.RESULT_OK,intent);
			finish();
			//checkCameraHardware(this);
			return true;
		}



		return super.onOptionsItemSelected(item);
	}


	public void SetBackGroundColor(int[] rgb){
		lv.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
		int textViewC = Color.rgb(255-rgb[0],255-rgb[1],255-rgb[2]);
		String[] sHexVals = {Integer.toHexString(rgb[0]),Integer.toHexString(rgb[1]),Integer.toHexString(rgb[2])};
		for (int i = 0;i<3;i++){
			if (sHexVals[i].length()==2){

			}
			else{
				sHexVals[i] = "0"+sHexVals[i];
			}
		}
		hexVal.setText("#"+sHexVals[0]+sHexVals[1]+sHexVals[2]);
		if((rgb[0]+rgb[1]+rgb[2])<100){
			textViewGreen.setTextColor(textViewC);
			textViewRed.setTextColor(textViewC);
			textViewBlue.setTextColor(textViewC);
			textViewGreenDis.setTextColor(textViewC);
			textViewRedDis.setTextColor(textViewC);
			textViewBlueDis.setTextColor(textViewC);

		}
		else{
			textViewGreen.setTextColor(textViewC);
			textViewRed.setTextColor(textViewC);
			textViewBlue.setTextColor(textViewC);
			textViewGreenDis.setTextColor(textViewC);
			textViewRedDis.setTextColor(textViewC);
			textViewBlueDis.setTextColor(textViewC);
		}
	}



}
