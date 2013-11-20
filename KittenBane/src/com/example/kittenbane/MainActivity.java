package com.example.kittenbane;

import java.util.Random;
import orbotix.robot.base.CollisionDetectedAsyncData;
import orbotix.robot.base.DeviceAsyncData;
import orbotix.robot.base.DeviceMessenger;
import orbotix.robot.base.DeviceMessenger.AsyncDataListener;
import orbotix.robot.base.RGBLEDOutputCommand;
import orbotix.robot.base.Robot;
import orbotix.robot.base.RobotProvider;
import orbotix.robot.base.RollCommand;
import orbotix.view.connection.SpheroConnectionView;
import orbotix.view.connection.SpheroConnectionView.OnRobotConnectionEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Robot firstRobot;
	private Handler colDetHandler = new Handler();// dunno if can be used for
													// timer
	private Handler timerHandler = new Handler();
	private TextView timerText;
	private TextView scoreText;
	private RadioButton easyButton;
	long startTime;
	long elapsedTime;
	int score;

	private SpheroConnectionView spheroConnectView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// check for problems

		timerText = (TextView) findViewById(R.id.timerText);
		scoreText = (TextView) findViewById(R.id.scoreText);
		easyButton = (RadioButton) findViewById(R.id.easyRadButton);
		easyButton.setChecked(true); //Not Tested Could Cause Problems
		spheroConnectView = (SpheroConnectionView) findViewById(R.id.sphero_connection_view);
		spheroConnectView
				.setOnRobotConnectionEventListener(new OnRobotConnectionEventListener() {
					@Override
					public void onRobotConnectionFailed(Robot robot) {
					}

					@Override
					public void onNonePaired() {
					}

					@Override
					public void onRobotConnected(Robot robot) {
						firstRobot = robot;
						// Remove if adding a second Sphero
						spheroConnectView.setVisibility(View.GONE);
						// ADD Collision Detection
						colDetHandler.postDelayed(new Runnable() {
							public void run() {
								DeviceMessenger.getInstance()
										.addAsyncDataListener(firstRobot,
												fColListener);
							}
						}, 1000);
					}

					@Override
					public void onBluetoothNotEnabled() {
						// Change this in future versions
						Toast.makeText(MainActivity.this,
								"Bluetooth not enabled", Toast.LENGTH_LONG)
								.show();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	long timeOfFirstCollision = 0;
	boolean secondCollisionFlag = false;
	
	private final AsyncDataListener fColListener = new AsyncDataListener() {
		public void onDataReceived(DeviceAsyncData arg0) {
			if (arg0 instanceof CollisionDetectedAsyncData) {
				// this needs to be set up to leave the heading that the
				// collision hit at

				float newHeading = (float) (((int)RollCommand.getCurrentHeading()
						+ rng.nextInt(90) + 45) % 360);
				RollCommand.sendCommand(firstRobot, newHeading, speed);
				//Below was added as the shake to stop feature and is untested.
				if (timeOfFirstCollision == 0) {
					timeOfFirstCollision = SystemClock.uptimeMillis();
				} else if (SystemClock.uptimeMillis() - timeOfFirstCollision <= 500 && secondCollisionFlag == false) {
					secondCollisionFlag = true;
				} else if (SystemClock.uptimeMillis() - timeOfFirstCollision > 500 && secondCollisionFlag == false) {
					timeOfFirstCollision = 0;
				} else if (SystemClock.uptimeMillis() - timeOfFirstCollision <= 1000 && secondCollisionFlag == true) {
					stopTimer();
					if (firstRobot != null)
						RollCommand.sendCommand(firstRobot, 0f, 0f);
				} else if (SystemClock.uptimeMillis() - timeOfFirstCollision > 1000 && secondCollisionFlag == true) {
					timeOfFirstCollision = 0;
					secondCollisionFlag = false;
				}
				
			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		spheroConnectView.showSpheros();
	}

	@Override
	public void onPause() {
		super.onPause();
		RobotProvider.getDefaultProvider().disconnectControlledRobots();
	}

	public void stopSphero(View view) {

		stopTimer();
		if (firstRobot != null)
			RollCommand.sendCommand(firstRobot, 0f, 0f);

	}

	final float easySpeed = .3f;
	final int easyLowerTimeLimit = 3000;
	final float medSpeed = .6f;
	final int medLowerTimeLimit = 1500;
	final float hardSpeed = 1f;
	final int hardLowerTimeLimit = 500;
	final int randomTimeRange = 1500;
	float speed = 0;
	int lowerTimeLimit = 0;

	public void onDiffButtonClicked(View view) {
		boolean isChecked = ((RadioButton) view).isChecked();

		switch (view.getId()) {
		case R.id.easyRadButton:
			if (isChecked) {
				speed = easySpeed;
				lowerTimeLimit = easyLowerTimeLimit;
			}
			break;
		case R.id.medRadButton:
			if (isChecked) {
				speed = medSpeed;
				lowerTimeLimit = medLowerTimeLimit;
			}
			break;
		case R.id.hardRadButton:
			if (isChecked) {
				speed = hardSpeed;
				lowerTimeLimit = hardLowerTimeLimit;
			}
			break;
		}
	}

	public void startSphero(View view) {
		startTime = SystemClock.uptimeMillis();
		score = 1000000;
		timeToNextTurn = (lowerTimeLimit + rng.nextInt(randomTimeRange));
		timeOfLastTurn = SystemClock.uptimeMillis();
		timerHandler.postDelayed(updateTimerText, 0);
		float heading = (float) rng.nextInt() % 360;
		RollCommand.sendCommand(firstRobot, heading, speed);
	}

	Random rng = new Random();
	long timeOfLastTurn;
	int timeToNextTurn;

	public void driveSphero() {
		if (SystemClock.uptimeMillis() > timeOfLastTurn + timeToNextTurn) {
			float heading = (float) rng.nextInt() % 360;
			RollCommand.sendCommand(firstRobot, heading, speed);
			timeToNextTurn = (lowerTimeLimit + rng.nextInt(randomTimeRange));
			timeOfLastTurn = SystemClock.uptimeMillis();
		}
	}

	private Runnable updateTimerText = new Runnable() {
		public void run() {
			updateTimeAndScore();
			driveSphero();
			timerHandler.postDelayed(this, 0);
		}
	};

	private void updateTimeAndScore() {
		elapsedTime = SystemClock.uptimeMillis() - startTime;
		int secs = (int) elapsedTime / 1000;
		int mins = (int) (secs / 60);
		secs = (int) (secs % 60);
		timerText.setText("" + mins + ":" + secs);
		if (score > 0)
		score = score - (int) elapsedTime / 100;
		if (score <= 0)
			score = 0;
		scoreText.setText("" + score);
		if (score > 700000)
			RGBLEDOutputCommand.sendCommand(firstRobot,255,0,0);
		else if (score > 400000)
			RGBLEDOutputCommand.sendCommand(firstRobot,0,255,0);
		else if (score > 100000)
			RGBLEDOutputCommand.sendCommand(firstRobot,0,0,255);
		else if (score > 0)
			RGBLEDOutputCommand.sendCommand(firstRobot,156,156,156);
		else
			RGBLEDOutputCommand.sendCommand(firstRobot,255,255,255);
	}

	private void stopTimer() {
		updateTimeAndScore();
		timerHandler.removeCallbacks(updateTimerText);
	}

}
