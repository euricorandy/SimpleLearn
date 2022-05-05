package umn.ac.simplelearn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import io.grpc.Context;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout2;
    NavigationView navigationView;
    Toolbar toolbar;

    private static final int GALLERY_INTENT_CODE = 1023;
    TextView fullName, email, nim;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button updateProfile;
    ImageView profilePicture;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout2 = findViewById(R.id.drawerLayout2);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.profileToolbar);

        fullName = findViewById(R.id.fullNameProfile);
        email = findViewById(R.id.emailProfile);
        nim = findViewById(R.id.nimProfile);

        profilePicture = findViewById(R.id.profilePicture);
        updateProfile = findViewById(R.id.updateProfile);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicture);
            }
        });

        userId = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                nim.setText(documentSnapshot.getString("nim"));
                fullName.setText(documentSnapshot.getString("namaMahasiswa"));
                email.setText(documentSnapshot.getString("email"));

            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open gallery
                Intent i = new Intent(view.getContext(), EditProfile.class);
                i.putExtra("fullName", fullName.getText().toString());
                i.putExtra("email", email.getText().toString());
                i.putExtra("nim", nim.getText().toString());
                startActivity(i);
            }
        });

        // toolbar
        setSupportActionBar(toolbar);

        // navigation drawer menu

        // hide and show logout and profile
        Menu menu = navigationView.getMenu();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {

            menu.findItem(R.id.login).setVisible(false);

        } else {

            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.profile_menu).setVisible(false);

        }

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout2,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout2.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.profile_menu);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout2.isDrawerOpen((GravityCompat.START))){
            drawerLayout2.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_menu:
                Intent intent = new Intent(Profile.this, Dashboard.class);
                startActivity(intent);
                finish();

            case R.id.profile_menu:
                break;

            case R.id.login:
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();

            case R.id.logout:
                FirebaseAuth.getInstance().signOut(); //logout
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
        }

        drawerLayout2.closeDrawer(GravityCompat.START);
        return true;
    }
}