package com.nepumuk.colorgen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


public class ColorGen extends ActionBarActivity {

	private SeekBar aSeekBarRed;
	private SeekBar aSeekBarGreen;
	private SeekBar aSeekBarBlue;
	private TextView aTextViewRed;
	private TextView aTextViewGreen;
	private TextView aTextViewBlue;
	private TextView aTextViewRedDis;
	private TextView aTextViewGreenDis;
	private TextView aTextViewBlueDis;
	private TextView aTextViewHexValue;

	private LinearLayout aLinearLayout;

	private int[] aHexValues = new int[] {255,255,255};

	private SeekBar.OnSeekBarChangeListener aSeekBarChangeListener = new  SeekBar.OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			switch (seekBar.getId()){
				case R.id.seekBar_RED:
					aTextViewRed.setText(String.valueOf(progress));
					aHexValues[0] = progress;
					break;
				case R.id.seekBar_GREEN:
					aTextViewGreen.setText(String.valueOf(progress));
					aHexValues[1] = progress;
					break;
				case R.id.seekBar_BLUE:
					aTextViewBlue.setText(String.valueOf(progress));
					aHexValues[2] = progress;
					break;
			}
			SetBackGroundColor(aHexValues);

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
		aTextViewRed = (TextView) findViewById(R.id.textView);
		aTextViewGreen = (TextView) findViewById(R.id.textView2);
		aTextViewBlue = (TextView) findViewById(R.id.textView3);
		aSeekBarRed = (SeekBar) findViewById(R.id.seekBar_RED);
		aSeekBarGreen = (SeekBar) findViewById(R.id.seekBar_GREEN);
		aSeekBarBlue = (SeekBar) findViewById(R.id.seekBar_BLUE);
		aLinearLayout = (LinearLayout) findViewById(R.id.LinLay);
		aSeekBarBlue.setOnSeekBarChangeListener(aSeekBarChangeListener);
		aSeekBarRed.setOnSeekBarChangeListener(aSeekBarChangeListener);
		aSeekBarGreen.setOnSeekBarChangeListener(aSeekBarChangeListener);
		aTextViewRedDis =(TextView) findViewById(R.id.textView4);
		aTextViewGreenDis =(TextView) findViewById(R.id.textView5);
		aTextViewBlueDis =(TextView) findViewById(R.id.textView6);
		aTextViewHexValue = (TextView) findViewById(R.id.HexVal);
		SetBackGroundColor(aHexValues);

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
			intent.putExtra("DATA",Color.rgb(aHexValues[0], aHexValues[1], aHexValues[2]));
			setResult(ActionBarActivity.RESULT_OK,intent);
			finish();
			//checkCameraHardware(this);
			return true;
		}



		return super.onOptionsItemSelected(item);
	}


	public void SetBackGroundColor(int[] rgb){
		aLinearLayout.setBackgroundColor(Color.rgb(rgb[0],rgb[1],rgb[2]));
		int textViewC = Color.rgb(255-rgb[0],255-rgb[1],255-rgb[2]);
		String[] sHexVals = {Integer.toHexString(rgb[0]),Integer.toHexString(rgb[1]),Integer.toHexString(rgb[2])};
		for (int i = 0;i<3;i++){
			if (sHexVals[i].length()==2){}
			else{
				sHexVals[i] = "0"+sHexVals[i];
			}
		}
		aTextViewHexValue.setText("#"+sHexVals[0]+sHexVals[1]+sHexVals[2]);
		if((rgb[0]+rgb[1]+rgb[2])<100){
			aTextViewGreen.setTextColor(textViewC);
			aTextViewRed.setTextColor(textViewC);
			aTextViewBlue.setTextColor(textViewC);
			aTextViewGreenDis.setTextColor(textViewC);
			aTextViewRedDis.setTextColor(textViewC);
			aTextViewBlueDis.setTextColor(textViewC);

		}
		else{
			aTextViewGreen.setTextColor(textViewC);
			aTextViewRed.setTextColor(textViewC);
			aTextViewBlue.setTextColor(textViewC);
			aTextViewGreenDis.setTextColor(textViewC);
			aTextViewRedDis.setTextColor(textViewC);
			aTextViewBlueDis.setTextColor(textViewC);
		}
	}



}
