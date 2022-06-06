package umn.ac.simplelearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth fAuth;

    ImageView indoMenu, ingMenu, quizMenu, spinMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        indoMenu = findViewById(R.id.bhsIndo);
        ingMenu = findViewById(R.id.bhsIng);
        quizMenu = findViewById(R.id.funQuiz);
        spinMenu = findViewById(R.id.spinWheel);

        fAuth = FirebaseAuth.getInstance();

        // toolbar
        setSupportActionBar(toolbar);

        // navigation drawer menu

        // hide and show logout and profile
        Menu menu = navigationView.getMenu();
        if(fAuth.getCurrentUser() != null) {

            menu.findItem(R.id.login).setVisible(false);
            indoMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),Indonesia.class));
                }
            });

            ingMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),Inggris.class));
                }
            });

            quizMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),MenuQuiz.class));
                }
            });

            spinMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(),SpinnerActivity.class));
                }
            });

        } else {

            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.wallet_menu).setVisible(false);
            menu.findItem(R.id.rank_menu).setVisible(false);
            menu.findItem(R.id.profile_menu).setVisible(false);
            indoMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Dashboard.this, "Please login first.", Toast.LENGTH_SHORT).show();
                }
            });

            ingMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Dashboard.this, "Please login first.", Toast.LENGTH_SHORT).show();
                }
            });

            quizMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Dashboard.this, "Please login first.", Toast.LENGTH_SHORT).show();
                }
            });

            spinMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Dashboard.this, "Please login first.", Toast.LENGTH_SHORT).show();
                }
            });

        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.home_menu);
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
                break;

            case R.id.profile_menu:
                Intent intent = new Intent(Dashboard.this, Profile.class);
                startActivity(intent);
                break;

            case R.id.wallet_menu:
                Intent intentWallet = new Intent(Dashboard.this, WalletActivity.class);
                startActivity(intentWallet);
                break;

            case R.id.rank_menu:
                Intent intentRank = new Intent(Dashboard.this, LeaderboardActivity.class);
                startActivity(intentRank);
                break;

            case R.id.login:
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            case R.id.logout:
                fAuth.signOut(); //logout
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}