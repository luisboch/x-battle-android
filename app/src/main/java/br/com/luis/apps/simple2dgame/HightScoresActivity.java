package br.com.luis.apps.simple2dgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Comparator;

import br.com.luis.apps.simple2dgame.util.AppData;
import br.com.luis.apps.simple2dgame.util.AssetUtil;

public class HightScoresActivity extends BasicActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AssetUtil.create(getAssets(), getApplicationContext());
        AppData.load();

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();

        AppData data = AppData.getInstance();

        setContentView(R.layout.activity_hight_scores);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        Collections.sort(data.getScores(), new Comparator<AppData.Score>() {
            @Override
            public int compare(AppData.Score o1, AppData.Score o2) {
                return ((Long) o2.getPoints()).compareTo(o1.getPoints());
            }
        });

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp(5), dp(5), dp(5), dp(5));

        for (AppData.Score sc : data.getScores()) {
            LinearLayout lyt = new LinearLayout(getApplicationContext());
            lyt.setLayoutParams(params);
            lyt.setOrientation(LinearLayout.HORIZONTAL);
            lyt.setPadding(dp(10), dp(10), dp(15), dp(15));

            TextView textName = new TextView(getApplicationContext());
            textName.setLayoutParams(params);
            textName.setTextSize(dp(15));
            textName.setText(sc.getName());
            textName.setTextColor(Color.WHITE);
            lyt.addView(textName);

            TextView textPoints = new TextView(getApplicationContext());

            textPoints.setLayoutParams(params);
            textPoints.setTextSize(dp(15));
            textPoints.setText(sc.getPoints() + "");
            textPoints.setTextColor(Color.WHITE);
            lyt.addView(textPoints);

            TextView lblPoints = new TextView(getApplicationContext());
            lblPoints.setLayoutParams(params);
            lblPoints.setTextSize(dp(15));
            lblPoints.setText(R.string.higthScores_pts);
            lblPoints.setTextColor(Color.WHITE);
            lyt.addView(lblPoints);

            container.addView(lyt);
        }

    }

    public void goToMenu(View view) {
        final Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("showResult", true);

        startActivity(intent);
    }

    public void goToGame(View view) {
        final Intent intent = new Intent(this, GameActivity.class);
        //intent.putExtra("showResult", true);

        startActivity(intent);
    }

    public int dp(int px) {
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        float fPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, metrics);
        return Math.round(fPixels);
    }
}
