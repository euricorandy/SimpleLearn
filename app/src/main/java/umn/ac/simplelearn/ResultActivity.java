package umn.ac.simplelearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ResultActivity extends AppCompatActivity {

    TextView score, earnedCoins;
    String level;
    Button quit;
    int POINTS = 10;
    int bonus;

    User user;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser userF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userF = fAuth.getCurrentUser();

        score = findViewById(R.id.score);
        earnedCoins = findViewById(R.id.earnedCoins);
        quit = findViewById(R.id.quitBtn);

        int correctAnswer = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total", 0);
        int totalExp = getIntent().getIntExtra("exp", 0);

        DocumentReference documentReference = fStore.collection("users")
                .document(userF.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                long expNum = user.getExp();
                if (expNum < 500) {
                    fStore.collection("users").document(userF.getUid())
                            .update("level", "Mahasiswa");
                }
                else if (expNum >= 500 && expNum < 1000) {
                    fStore.collection("users").document(userF.getUid())
                            .update("level", "Ketua Kelas");
                } else if (expNum >= 1000) {
                    fStore.collection("users").document(userF.getUid())
                            .update("level", "Sarjana");
                }

                level = documentSnapshot.getString("level");

                Log.i("LOGGER", "LEVEL = " + level);

                if (level.equals("Mahasiswa")) {
                    bonus = 0;
                } else if (level.equals("Ketua Kelas")) {
                    bonus = 50;
                } else if (level.equals("Sarjana")) {
                    bonus = 100;
                }

                int points = correctAnswer * POINTS + bonus;

                score.setText(String.format("%d/%d", correctAnswer, totalQuestions));
                earnedCoins.setText(String.valueOf(points));
                Toast.makeText(ResultActivity.this, "You got total " + totalExp + " EXP from this quiz.", Toast.LENGTH_SHORT).show();

                fStore.collection("users").document(fAuth.getUid())
                        .update("coins", FieldValue.increment(points));
            }
        });

        fStore.collection("users").document(userF.getUid())
                .update("exp", FieldValue.increment(totalExp));

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MenuQuiz.class));
                finish();
            }
        });
    }
}