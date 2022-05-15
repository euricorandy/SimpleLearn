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
import android.widget.RelativeLayout;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Inggris extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    List<ProductIng> productInggrisList;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inggris);

        recyclerView = (RecyclerView) findViewById(R.id.ingRecyclerView);

        toolbar = findViewById(R.id.ingToolbar);
        drawerLayout = findViewById(R.id.inggrisDL);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);

        Menu menu = navigationView.getMenu();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            menu.findItem(R.id.login).setVisible(false);

        } else {

            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.profile_menu).setVisible(false);

        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        productInggrisList = new ArrayList<>();

        productInggrisList.add(new ProductIng(1,
                "Pronouns, Contractions, and Prepositions", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%202-Non%20PPT-%20Pronouns%2C%20Contractions%20and%20Prepositions.pdf?alt=media&token=bc4ad4cd-4919-45fa-8cfa-6221533b64cc"));

        productInggrisList.add(new ProductIng(2,
                "Tenses Overview", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%203-Non%20PPT-12%20tenses-Overview.pdf?alt=media&token=fcfe2bec-a70d-4475-ab3c-a06f599eb904"));

        productInggrisList.add(new ProductIng(3,
                "The Passive", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%204-Non%20PPT-The%20Passive.pdf?alt=media&token=03a3fa01-a126-4875-8bb6-f2f82fa156a9"));

        productInggrisList.add(new ProductIng(4,
                "Noun Clauses", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%205-Non%20PPT-Noun%20Clauses.pdf?alt=media&token=9b1104ca-8186-488f-b373-4e80f0cd5764"));

        productInggrisList.add(new ProductIng(5,
                "Subject-Verb Agreement", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%2014-PPT-S-V%20Agreement.pdf?alt=media&token=3eb36e97-bf22-4343-945c-8f4366458bbf"));

        ProductIngAdapter adapter = new ProductIngAdapter(this,productInggrisList);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_about_card_show);
        RelativeLayout relativeLayout = findViewById(R.id.inggrisRL);
        recyclerView.setAnimation(animation);
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
                Intent intentHome = new Intent(Inggris.this, Dashboard.class);
                startActivity(intentHome);
                finish();
                break;

            case R.id.profile_menu:
                Intent intentProfile = new Intent(Inggris.this, Profile.class);
                startActivity(intentProfile);
                break;

            case R.id.login:
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            case R.id.logout:
                FirebaseAuth.getInstance().signOut(); //logout
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}