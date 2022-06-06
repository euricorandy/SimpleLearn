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

public class Indonesia extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<ProductIndo> productIndoList;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    Button addIndo;
    String status;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indonesia);

        recyclerView = (RecyclerView) findViewById(R.id.indoRecyclerView);
        addIndo = (Button) findViewById(R.id.btnAddIndo);

        toolbar = findViewById(R.id.indoToolbar);
        drawerLayout = findViewById(R.id.indonesiaDL);
        navigationView = findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);

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
            }
        });

        if (status == "admin") {
            addIndo.setVisibility(View.VISIBLE);
        } else {
            addIndo.setVisibility(View.GONE);
        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        productIndoList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("materiInterpersonal");

        productIndoList.add(new ProductIndo("Pesan Verbal dan Pesan Non-Verbal",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%208%20-%20Pesan.pdf?alt=media&token=8e895e1f-368d-4225-b081-24f4fa4a2e38"));

        productIndoList.add(new ProductIndo("Tahapan, Teori, dan Hubungan Antarpribadi",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%209%20-%20Interpersonal%20Relationship%20Stages%2C%20Theories%2C%20and%20Communication.pdf?alt=media&token=28cc7645-ee4f-4a65-864d-97301a932802"));

        productIndoList.add(new ProductIndo("Jenis-jenis Hubungan Antarpribadi",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%2010%20-%20Types%20of%20Interpersonal%20Relationship.pdf?alt=media&token=6c486a96-1a2d-4916-ad4e-0389769f097b"));

        productIndoList.add(new ProductIndo("Kekuasaan dan Pengaruh Antarpribadi",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%2012%20-%20Interpersonal%20Power%20and%20Influence.pdf?alt=media&token=ff4d1942-e102-4028-b5c2-b45ab19c7a53"));

        productIndoList.add(new ProductIndo("Wawancara dan CMC",
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%2013%20-%20Wawancara%20dan%20CMC.pdf?alt=media&token=132f2067-be33-46b5-89c9-dbf13f8f0953"));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ProductIndo productIndo = postSnapshot.getValue(ProductIndo.class);
                    productIndoList.add(productIndo);
                }

                ProductIndoAdapter adapter = new ProductIndoAdapter(Indonesia.this, productIndoList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Indonesia.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_about_card_show);
        RelativeLayout relativeLayout = findViewById(R.id.indonesiaRL);
        recyclerView.setAnimation(animation);

        addIndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(Indonesia.this, AddIndo.class);
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
                Intent intentHome = new Intent(Indonesia.this, Dashboard.class);
                startActivity(intentHome);
                finish();
                break;

            case R.id.profile_menu:
                Intent intentProfile = new Intent(Indonesia.this, Profile.class);
                startActivity(intentProfile);
                break;

            case R.id.wallet_menu:
                Intent intentWallet = new Intent(Indonesia.this, WalletActivity.class);
                startActivity(intentWallet);
                break;

            case R.id.rank_menu:
                Intent intentRank = new Intent(Indonesia.this, LeaderboardActivity.class);
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