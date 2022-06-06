package umn.ac.simplelearn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import umn.ac.simplelearn.databinding.ActivityMainBinding;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuQuiz extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    RecyclerView recyclerView;
    FirebaseFirestore database;
    FirebaseAuth firebaseAuth;

    ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_quiz);

        recyclerView = (RecyclerView) findViewById(R.id.categoryList);

        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        ArrayList<CategoryModel> categories = new ArrayList<>();

        CategoryAdapter adapter = new CategoryAdapter(this, categories);

        registration = database.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            CategoryModel categoryModel = snapshot.toObject(CategoryModel.class);
                            categoryModel.setCategoryId(snapshot.getId());
                            categories.add(categoryModel);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);

        toolbar = findViewById(R.id.quizToolbar);
        drawerLayout = findViewById(R.id.quizDL);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);

        Menu menu = navigationView.getMenu();
        if(firebaseAuth.getCurrentUser() != null) {

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
                Intent intentHome = new Intent(MenuQuiz.this, Dashboard.class);
                startActivity(intentHome);
                finish();
                break;

            case R.id.profile_menu:
                Intent intentProfile = new Intent(MenuQuiz.this, Profile.class);
                startActivity(intentProfile);
                break;

            case R.id.wallet_menu:
                Intent intentWallet = new Intent(MenuQuiz.this, WalletActivity.class);
                startActivity(intentWallet);
                break;

            case R.id.rank_menu:
                Intent intentRank = new Intent(MenuQuiz.this, LeaderboardActivity.class);
                startActivity(intentRank);
                break;

            case R.id.login:
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            case R.id.logout:
                registration.remove();
                firebaseAuth.signOut(); //logout
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}