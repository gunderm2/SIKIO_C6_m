/*
 * Copyright 2015 Michael Gunderson. All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ARSHAN POURSOHI OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied.
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