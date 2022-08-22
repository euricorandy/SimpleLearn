package umn.ac.simplelearn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class Inggris extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<ProductIng> productInggrisList;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    Button addIng;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inggris);

        recyclerView = (RecyclerView) findViewById(R.id.ingRecyclerView);
        addIng = (Button) findViewById(R.id.btnAddIng);

        toolbar = findViewById(R.id.ingToolbar);
        drawerLayout = findViewById(R.id.inggrisDL);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);

        FirebaseAuth fAuth;
        FirebaseFirestore fStore;

        fAuth = FirebaseAuth.getInstance();

        Menu menu = navigationView.getMenu();
        if(fAuth.getCurrentUser() != null) {

            menu.findItem(R.id.login).setVisible(false);

        } else {

            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.wallet_menu).setVisible(false);
            menu.findItem(R.id.rank_menu).setVisible(false);
            menu.findItem(R.id.profile_menu).setVisible(false);

        }

        fStore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = fStore.collection("users")
                .document(fAuth.getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                status = documentSnapshot.getString("profile");
                Log.i("LOGGER", "STATUS = " + status);
                if (status.equals("admin")) {
                    addIng.setVisibility(View.VISIBLE);
                } else {
                    addIng.setVisibility(View.GONE);
                }
            }
        });

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        productInggrisList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("materiInggris");

        productInggrisList.add(new ProductIng("Pronouns, Contractions, and Prepositions",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%202-Non%20PPT-%20Pronouns%2C%20Contractions%20and%20Prepositions.pdf?alt=media&token=bc4ad4cd-4919-45fa-8cfa-6221533b64cc"));

        productInggrisList.add(new ProductIng("Tenses Overview",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%203-Non%20PPT-12%20tenses-Overview.pdf?alt=media&token=fcfe2bec-a70d-4475-ab3c-a06f599eb904"));

        productInggrisList.add(new ProductIng("The Passive",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%204-Non%20PPT-The%20Passive.pdf?alt=media&token=03a3fa01-a126-4875-8bb6-f2f82fa156a9"));

        productInggrisList.add(new ProductIng("Noun Clauses",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%205-Non%20PPT-Noun%20Clauses.pdf?alt=media&token=9b1104ca-8186-488f-b373-4e80f0cd5764"));

        productInggrisList.add(new ProductIng("Subject-Verb Agreement",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInggris%2FWeek%2014-PPT-S-V%20Agreement.pdf?alt=media&token=3eb36e97-bf22-4343-945c-8f4366458bbf"));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ProductIng productIng = postSnapshot.getValue(ProductIng.class);
                    productInggrisList.add(productIng);
                }

                ProductIngAdapter adapter = new ProductIngAdapter(Inggris.this, productInggrisList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Inggris.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_about_card_show);
        RelativeLayout relativeLayout = findViewById(R.id.inggrisRL);
        recyclerView.setAnimation(animation);

        addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(Inggris.this, AddIng.class);
                startActivity(intentAdd);
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
                Intent intentHome = new Intent(Inggris.this, Dashboard.class);
                startActivity(intentHome);
                finish();
                break;

            case R.id.profile_menu:
                Intent intentProfile = new Intent(Inggris.this, Profile.class);
                startActivity(intentProfile);
                break;

            case R.id.wallet_menu:
                Intent intentWallet = new Intent(Inggris.this, WalletActivity.class);
                startActivity(intentWallet);
                break;

            case R.id.rank_menu:
                Intent intentRank = new Intent(Inggris.this, LeaderboardActivity.class);
                startActivity(intentRank);
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