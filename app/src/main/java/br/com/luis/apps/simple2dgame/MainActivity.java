package br.com.luis.apps.simple2dgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import br.com.luis.apps.simple2dgame.util.AssetUtil;
import br.com.luis.apps.simple2dgame.util.SoundManager;

/**
 * Created by luis on 04/06/17.
 */

public class MainActivity extends BasicActivity {
    SoundManager soundManager = new SoundManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showToast("MainActivity");
        setContentView(R.layout.activity_main);

        AssetUtil.create(getAssets(), getApplicationContext());

        soundManager.play("menu-bg.wav", true);
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param view
     */
    public void goToHightScores(View view) {
        final Intent intent = new Intent(this, HightScoresActivity.class);
        //intent.putExtra("showResult", true);

        startActivity(intent);
    }

    public void goToGame(View view) {
        final Intent intent = new Intent(this, GameActivity.class);
        //intent.putExtra("showResult", true);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isFinishing()){
            soundManager.stop();
        } else {
            soundManager.pause();
        }
    }
}
