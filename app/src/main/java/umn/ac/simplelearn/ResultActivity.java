package umn.ac.simplelearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultActivity extends AppCompatActivity {

    TextView score, earnedCoins;
    Button quit;
    int POINTS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        score = findViewById(R.id.score);
        earnedCoins = findViewById(R.id.earnedCoins);
        quit = findViewById(R.id.quitBtn);

        int correctAnswer = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total", 0);

        int points = correctAnswer * POINTS;

        score.setText(String.format("%d/%d", correctAnswer, totalQuestions));
        earnedCoins.setText(String.valueOf(points));

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(points));

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentQuit = new Intent(ResultActivity.this, MenuQuiz.class);
                startActivity(intentQuit);
            }
        });
    }
}