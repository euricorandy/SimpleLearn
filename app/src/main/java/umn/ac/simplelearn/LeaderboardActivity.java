package umn.ac.simplelearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        toolbar = findViewById(R.id.rankToolbar);
        drawerLayout = findViewById(R.id.rankDL);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);

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
        navigationView.setCheckedItem(R.id.rank_menu);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_about_card_show);
        recyclerView.setAnimation(animation);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        ArrayList<User> users = new ArrayList<>();
        LeaderboardsAdapter adapter = new LeaderboardsAdapter(this, users);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database.collection("users").orderBy("coins", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                    User user = snapshot.toObject(User.class);
                    users.add(user);
                }
                adapter.notifyDataSetChanged();
            }
        });
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
                Intent intentHome = new Intent(LeaderboardActivity.this, Dashboard.class);
                startActivity(intentHome);
                finish();
                break;

            case R.id.profile_menu:
                Intent intentProfile = new Intent(LeaderboardActivity.this, Profile.class);
                startActivity(intentProfile);
                break;

            case R.id.wallet_menu:
                Intent intentWallet = new Intent(LeaderboardActivity.this, WalletActivity.class);
                startActivity(intentWallet);
                break;

            case R.id.rank_menu:
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