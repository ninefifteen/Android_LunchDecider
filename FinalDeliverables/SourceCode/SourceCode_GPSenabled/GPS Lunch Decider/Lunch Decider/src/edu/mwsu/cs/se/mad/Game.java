package edu.mwsu.cs.se.mad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import edu.mwsu.cs.se.mad.common.Restaurant;
import edu.mwsu.cs.se.mad.common.RestaurantSearchManager;
import edu.mwsu.cs.se.mad.common.SequenceList;
import edu.mwsu.cs.se.mad.common.SequenceList.SingleSquence;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Game extends Activity {

	private int winnerCol;
	private List<Restaurant> rstlist;
	private List<TextView> candidateRestList = new ArrayList<TextView>();
	private List<Integer> textViewIds = new ArrayList<Integer>();
	private ImageView animationImageView;
	private SequenceList animationSequenceList;
	private AnimationDrawable gameAnimation;
	private final int FARAME_DURATION = 100;
	private AnimationControl animationControl;

	// private final int frameDuration=100;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game);

		intTextViewIds();
		initViews();
		
		
		
		Random r = new Random();
		winnerCol = r.nextInt(4);
		rstlist = RestaurantSearchManager.getRestaurantList();
		assignRestaurantName(winnerCol);

		animationImageView = (ImageView) findViewById(R.id.background_img);
		animationImageView.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.background));
		animationSequenceList = new SequenceList();
		animationSequenceList.setSequence();
		gameAnimation = new AnimationDrawable();
		setFrames(winnerCol);

	}

	private void setFrames(int winnerCol) {
		SequenceList.SingleSquence animationSequenceIds = animationSequenceList
				.getSquences().get(winnerCol);
		for (Iterator<Integer> seq = animationSequenceIds
				.getSingleAnimSquence().iterator(); seq.hasNext();)
			gameAnimation.addFrame(
					getResources().getDrawable(seq.next().intValue()),
					FARAME_DURATION);
		gameAnimation.setOneShot(true);

	}

	private void assignRestaurantName(int winnerCol) {

		candidateRestList.get(winnerCol).setText(rstlist.get(0).getTitle());

		for (int i = 0, j = 1; i < rstlist.size(); i++, j++) {
			if (i != winnerCol)
				candidateRestList.get(i).setText(rstlist.get(j).getTitle());
			else
				j--;

		}
	}

	private void intTextViewIds() {
		textViewIds.add(R.id.rest_one);
		textViewIds.add(R.id.rest_two);
		textViewIds.add(R.id.rest_three);
		textViewIds.add(R.id.rest_four);

	}

	private void initViews() {
		for (Iterator<Integer> viewIds = textViewIds.iterator(); viewIds
				.hasNext();) {
			TextView _textview = (TextView) getItemById(viewIds.next()
					.intValue());
			candidateRestList.add(_textview);
		}

	}

	private View getItemById(int itemId) {

		return findViewById(itemId);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Game.this.finish();
	}

	private static void startNewActivity(Context currentContext,
			Class<?> activityURI) {
		Intent intent = new Intent(currentContext, activityURI);
		currentContext.startActivity(intent);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			animationControl = new AnimationControl();
			animationControl.execute();
		}
		return super.onTouchEvent(event);
	}

	private class AnimationControl extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			animationImageView.setBackgroundDrawable(gameAnimation);
			gameAnimation.start();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			while (gameAnimation.isRunning()) {
				try {
					Thread.sleep(6000);
					gameAnimation.stop();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					gameAnimation.stop();
				}

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			startNewActivity(Game.this, Result.class);
		}

	}

	private class GameExitListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (animationControl != null) {
				while (!animationControl.isCancelled())
					animationControl.cancel(false);
			}
			Game.this.finish();
		}

	}

	
	private class GamePauseListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton checkedButton,
				boolean checked) {
if (checked) {
				if (gameAnimation != null && gameAnimation.isRunning())
					while(checked){
//						gameAnimation.invalidateDrawable(gameAnimation.getCurrent().);
					}
					gameAnimation.stop();
			} else {

				gameAnimation.start();

			}

		}

	}

}
