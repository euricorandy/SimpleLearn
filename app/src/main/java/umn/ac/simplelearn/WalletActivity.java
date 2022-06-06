package umn.ac.simplelearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalletActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseFirestore database;

    TextView coins;
    EditText emailGopay;
    Button sendRequest;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        drawerLayout = findViewById(R.id.walletDL);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.walletToolbar);

        coins = findViewById(R.id.currentCoins);
        emailGopay = findViewById(R.id.gopayEmail);
        sendRequest = findViewById(R.id.sendRequest);

        setSupportActionBar(toolbar);

        database = FirebaseFirestore.getInstance();
        database.collection("users").document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                coins.setText(String.valueOf(user.getCoins()));
            }
        });

        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getCoins() > 50000) {
                    String uid = FirebaseAuth.getInstance().getUid();
                    String gopay = emailGopay.getText().toString();
                    if (gopay == user.getEmail()) {
                        WithdrawRequest request = new WithdrawRequest(uid, gopay, user.getNamaMahasiswa());
                        database.collection("withdraws").document(uid).set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(WalletActivity.this, "Request sent successfully.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(WalletActivity.this, "Enter the same email as the one in the profile.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(WalletActivity.this, "You need more coins to get withdraw.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Menu menu = navigationView.getMenu();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            menu.findItem(R.id.login).setVisible(false);

        } else {

            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.wallet_menu).setVisible(false);
            menu.findItem(R.id.rank_menu).setVisible(false);
            menu.findItem(R.id.profile_menu).setVisible(false);

        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.wallet_menu);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen((GravityCompat.START))){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_menu:
                Intent intentHome = new Intent(WalletActivity.this, Dashboard.class);
                startActivity(intentHome);
                finish();
                break;

            case R.id.wallet_menu:
                break;

            case R.id.rank_menu:
                Intent intentRank = new Intent(WalletActivity.this, LeaderboardActivity.class);
                startActivity(intentRank);
                break;

            case R.id.profile_menu:
                Intent intentProfile = new Intent(WalletActivity.this, Profile.class);
                startActivity(intentProfile);
                break;

            case R.id.login:
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            case R.id.logout:
                FirebaseAuth.getInstance().signOut(); //logout
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}