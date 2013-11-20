package com.example.kittenbane;

import java.io.BufferedReader;
import java.util.StringTokenizer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class ChooseUserActivity extends Activity {

	final String[][] userKeyArray = 
			{{"fN", "fET","fMT", "fHT", "fS"},
			{"sN", "sET","sMT", "sHT", "sS"},
			{"tN", "tET","tMT", "tHT", "tS"},
			{"frN", "frET","frMT", "frHT", "frS"},
			{"ffN", "ffET","ffMT", "ffHT", "ffS"}};
	final int firstUser = 0, secondUser = 1, thirdUser = 2, forthUser = 3, fifthUser = 4;
	final int userName = 0, easyTime = 1, medTime = 2, hardTime = 3, bestScore = 4;
	User[] userArray;
	String fileName = "kittenBaneSave";
	int currentUser = -1;
	private int topOfArray = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_user);
		// Show the Up button in the action bar.
		try {

		} catch (Exception e) { // this could cause a save problem

		}
		setupActionBar();

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	private void buildUserArray(BufferedReader br) {
		topOfArray = 0;
		while (!eof) { // add the stuff here
			StringTokenizer st = new StringTokenizer(file.nextLine());
			if (st.countTokens() == 5) {
				userArray[i] = new User(st.nextToken(),
						Long.parseLong(st.nextToken()),
						Long.parseLong(st.nextToken()),
						Long.parseLong(st.nextToken()),
						Integer.parseInt(st.nextToken()));
				userNameArray[i] = userArray[i].getName();
				i++;
			}
		}
	}

	private void saveUserArray() {
		
		
		for (int i = 0; i < 10; i++) {
			//file .println(userArray[i].getSaveString());
		}
		//close file
	}
*/
}
