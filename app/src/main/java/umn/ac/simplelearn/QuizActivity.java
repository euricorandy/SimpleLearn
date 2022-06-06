package umn.ac.simplelearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ArrayList<Question> questions;
    CountDownTimer countDownTimer;
    TextView timer;
    TextView qCounter;
    TextView question;
    TextView option1;
    TextView option2;
    TextView option3;
    TextView option4;
    Button nextBtn;
    ImageView fiftyBtn, stopTime;

    Question questionObj;
    FirebaseFirestore database;

    int index = 0;
    int correctAnswers = 0;
    boolean counterF = true;
    boolean counterT = true;
    int exp = 10;
    int totalExp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        timer = findViewById(R.id.timer);
        qCounter = findViewById(R.id.questionCounter);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option_1);
        option2 = findViewById(R.id.option_2);
        option3 = findViewById(R.id.option_3);
        option4 = findViewById(R.id.option_4);

        nextBtn = findViewById(R.id.nextBtn);
        fiftyBtn = findViewById(R.id.fifty);
        stopTime = findViewById(R.id.clock);

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String todayString = year + "" + month + "" + day;

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        boolean currentDay = preferences.getBoolean(todayString, false);

        if (!currentDay) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(todayString, true);
            editor.apply();
            counterF = true;
            counterT = true;
        } else {
            counterF = false;
            counterT = false;
        }

        final String categoryId = getIntent().getStringExtra("categoryId");
        Random random = new Random();
        final int rand = random.nextInt(20);

        database.collection("categories")
                .document(categoryId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index", rand)
                .orderBy("index").limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.getDocuments().size() < 10) {
                    database.collection("categories")
                            .document(categoryId)
                            .collection("questions")
                            .whereLessThanOrEqualTo("index", rand)
                            .orderBy("index").limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                            setNextQuestion();
                        }
                    });
                } else {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        Question question = snapshot.toObject(Question.class);
                        questions.add(question);
                    }
                    setNextQuestion();
                }
            }
        });

        fiftyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counterF == true) {
                    fifty_fifty();
                    counterF = false;
                } else {
                    Toast.makeText(QuizActivity.this, "Only can be used once per day", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counterT == true) {
                    countDownTimer.cancel();
                    counterT = false;
                } else {
                    Toast.makeText(QuizActivity.this, "Only can be used once per day", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetTimer();
    }

    void resetTimer() {
        countDownTimer = new CountDownTimer(20000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                reset();
                if (index < questions.size() - 1) {
                    index++;
                    setNextQuestion();
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    startActivity(intent);
                }
            }
        };
    }

    void showAnswer() {
        if (questionObj.getAnswer().equals(option1.getText().toString()))
            option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (questionObj.getAnswer().equals(option2.getText().toString()))
            option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (questionObj.getAnswer().equals(option3.getText().toString()))
            option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if (questionObj.getAnswer().equals(option4.getText().toString()))
            option4.setBackground(getResources().getDrawable(R.drawable.option_right));
    }

    void setNextQuestion() {
        if (countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer.start();
        displayButtons();
        if (index < questions.size()) {
            qCounter.setText(String.format("%d/%d", (index+1), questions.size()));
            questionObj = questions.get(index);
            question.setText(questionObj.getQuestion());
            option1.setText(questionObj.getOption1());
            option2.setText(questionObj.getOption2());
            option3.setText(questionObj.getOption3());
            option4.setText(questionObj.getOption4());
        }
    }

    void checkAnswer(TextView textView) {
        String selectedAnswer = textView.getText().toString();
        if (selectedAnswer.equals(questionObj.getAnswer())) {
            correctAnswers++;
            totalExp += exp;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
            Toast.makeText(QuizActivity.this, "Yay! you get 10 EXP.", Toast.LENGTH_SHORT).show();
        } else {
            showAnswer();
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            Toast.makeText(QuizActivity.this, "You can do it better next time.", Toast.LENGTH_SHORT).show();
        }
    }

    void reset() {
        option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if (countDownTimer != null)
                    countDownTimer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);
                break;

            case R.id.nextBtn:
                reset();
                if (index < questions.size() - 1) {
                    index++;
                    setNextQuestion();
                } else {
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("correct", correctAnswers);
                    intent.putExtra("total", questions.size());
                    intent.putExtra("exp", totalExp);
                    startActivity(intent);
                }
                break;
        }
    }

    void fifty_fifty() {
        questionObj = questions.get(index);
        if (questionObj.getAnswer().equals(option4.getText().toString())) {
            option1.setVisibility(View.INVISIBLE);
            option2.setVisibility(View.INVISIBLE);
            option3.setVisibility(View.VISIBLE);
        }

        if (questionObj.getAnswer().equals(option3.getText().toString())) {
            option1.setVisibility(View.INVISIBLE);
            option4.setVisibility(View.INVISIBLE);
            option2.setVisibility(View.VISIBLE);
        }

        if (questionObj.getAnswer().equals(option2.getText().toString())) {
            option3.setVisibility(View.INVISIBLE);
            option4.setVisibility(View.INVISIBLE);
            option1.setVisibility(View.VISIBLE);
        }

        if (questionObj.getAnswer().equals(option1.getText().toString())) {
            option3.setVisibility(View.INVISIBLE);
            option2.setVisibility(View.INVISIBLE);
            option4.setVisibility(View.VISIBLE);
        }
    }

    private void displayButtons() {
        option1.setVisibility(View.VISIBLE);
        option2.setVisibility(View.VISIBLE);
        option3.setVisibility(View.VISIBLE);
        option4.setVisibility(View.VISIBLE);
    }
}