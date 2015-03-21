/*
*The MIT License (MIT)
*
*Copyright (c) 2015 Michael Gunderson
*
*Permission is hereby granted, free of charge, to any person obtaining a copy
*of this software and associated documentation files (the "Software"), to deal
*in the Software without restriction, including without limitation the rights
*to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
*copies of the Software, and to permit persons to whom the Software is
*furnished to do so, subject to the following conditions:
*
*The above copyright notice and this permission notice shall be included in
*all copies or substantial portions of the Software.
*
*THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
*THE SOFTWARE.
 */
package ioio.examples.simple;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class IOIOSimpleApp extends IOIOActivity {
	private TextView analogValue;
	private TextView logValue;
	private SeekBar seekBar;
	private Button startButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		analogValue = (TextView) findViewById(R.id.analogValue);
		logValue = (TextView) findViewById(R.id.logValue);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setMax(100);
		seekBar.setProgress(0);
		startButton = (Button) findViewById(R.id.startButton);
		
		
		startButton.setOnClickListener(new View.OnClickListener() {	
            public void onClick(View view) {
            	//start loging here           	
            }
        });
	}

	class Looper extends BaseIOIOLooper {
		AnalogInput photoresistor;

		// Variables to store analog and digital values
		float photoresistorVal; // Our analog values range between 0 and 1

		@Override
		public void setup() throws ConnectionLostException {
			// Opening the input pins.
			photoresistor = ioio_.openAnalogInput(39);
		}

		@Override
		public void loop() throws ConnectionLostException, InterruptedException {
			try
			  {
			    // While we're running, read our photoresistor.
				photoresistorVal = (photoresistor.read()) * 100;
				setAnalogValue(photoresistorVal);
				seekBar.setProgress((int) photoresistorVal);
			    // Don't call this loop again for 100 milliseconds
			    Thread.sleep(100);
			  } 
			  catch (InterruptedException e) 
			  {
			  }
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() {
		return new Looper();
	}
	
	private void setAnalogValue(final Float value) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				analogValue.setText(value.toString());
			}
		});
	}
}