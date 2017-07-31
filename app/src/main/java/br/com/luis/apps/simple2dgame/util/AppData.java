package br.com.luis.apps.simple2dgame.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by luis on 11/06/17.
 */

public class AppData {

    private final List<Score> scores;

    private static AppData instance;

    private Score currentScore;

    private AppData() {
        scores = new ArrayList<>();
    }

    public AppData addScore(String name, long points) {
        scores.add(new Score(name, points));
        Collections.sort(scores);
        return this;
    }

    /**
     * return the unique instance.
     *
     * @return
     */
    public static AppData getInstance() {
        return instance;
    }

    public Score getCurrentScore() {
        return currentScore == null ? currentScore = new Score() : currentScore;
    }

    public void registerCurrent() {
        this.scores.add(currentScore);
        currentScore = new Score();
        save();
    }

    public List<Score> getScores() {
        return scores;
    }

    public void save(){
        AssetUtil.getInstance().writeText(new GsonBuilder().create().toJson(this, AppData.class), "app.data");
    }


    public static void load(){
        String data = AssetUtil.getInstance().getText("app.data");
        if(data == null){
            instance = new AppData();
        } else {
            Gson gson = new Gson();
            instance = gson.fromJson(data, AppData.class);
        }
    }
    public static class Score implements Comparable<Score> {

        private final String name;

        private long points;

        private Score() {
            this.name = new SimpleDateFormat("dd/MM/yy HH:mm").format(new Date());
            points = 0l;
        }

        private Score(String name, long points) {
            this.name = name;
            this.points = points;
        }

        public long getPoints() {
            return points;
        }

        public String getName() {
            return name;
        }

        public void increase() {
            this.points++;
        }

        @Override
        public int compareTo(@NonNull Score o) {
            return this.points > o.points ? 1 : (this.points == o.points ? 0 : -1);
        }
    }

}
