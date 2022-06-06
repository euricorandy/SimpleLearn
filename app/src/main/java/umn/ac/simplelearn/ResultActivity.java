package umn.ac.simplelearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        score = findViewById(R.id.score);
        earnedCoins = findViewById(R.id.earnedCoins);
        quit = findViewById(R.id.quitBtn);

        int correctAnswer = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total", 0);
        int totalExp = getIntent().getIntExtra("exp", 0);

        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("users")
                .document(fAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                level = documentSnapshot.getString("level");
            }
        });

        if (level == "Mahasiswa") {
            bonus = 0;
        } else if (level == "Ketua Kelas") {
            bonus = 50;
        } else if (level == "Sarjana") {
            bonus = 100;
        }

        int points = correctAnswer * POINTS + bonus;

        score.setText(String.format("%d/%d", correctAnswer, totalQuestions));
        earnedCoins.setText(String.valueOf(points));
        Toast.makeText(ResultActivity.this, "You got total " + totalExp + " EXP from this quiz.", Toast.LENGTH_SHORT).show();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(points));
        database.collection("users").document(FirebaseAuth.getInstance().getUid())
                .update("exp", FieldValue.increment(totalExp));

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentQuit = new Intent(ResultActivity.this, MenuQuiz.class);
                startActivity(intentQuit);
            }
        });
    }
}