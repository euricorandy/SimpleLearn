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

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Indonesia extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    List<ProductIndo> productIndoList;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    Button addIndo;
    DatabaseReference databaseReference;
    ListView putPDFListView;
    List<putPDF> putPDFS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indonesia);

        recyclerView = (RecyclerView) findViewById(R.id.indoRecyclerView);
        addIndo = (Button) findViewById(R.id.btnAddIndo);
        putPDFListView = (ListView) findViewById(R.id.indoListView);

        toolbar = findViewById(R.id.indoToolbar);
        drawerLayout = findViewById(R.id.indonesiaDL);
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

        productIndoList = new ArrayList<>();
        putPDFS = new ArrayList<>();

        productIndoList.add(new ProductIndo(1,
                "Pesan Verbal dan Pesan Non-Verbal", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%208%20-%20Pesan.pdf?alt=media&token=8e895e1f-368d-4225-b081-24f4fa4a2e38"));

        productIndoList.add(new ProductIndo(2,
                "Tahapan, Teori, dan Hubungan Antarpribadi", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%209%20-%20Interpersonal%20Relationship%20Stages%2C%20Theories%2C%20and%20Communication.pdf?alt=media&token=28cc7645-ee4f-4a65-864d-97301a932802"));

        productIndoList.add(new ProductIndo(3,
                "Jenis-jenis Hubungan Antarpribadi", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%2010%20-%20Types%20of%20Interpersonal%20Relationship.pdf?alt=media&token=6c486a96-1a2d-4916-ad4e-0389769f097b"));

        productIndoList.add(new ProductIndo(4,
                "Kekuasaan dan Pengaruh Antarpribadi", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%2012%20-%20Interpersonal%20Power%20and%20Influence.pdf?alt=media&token=ff4d1942-e102-4028-b5c2-b45ab19c7a53"));

        productIndoList.add(new ProductIndo(5,
                "Wawancara dan CMC", R.drawable.ic_materi,
                "https://firebasestorage.googleapis.com/v0/b/simplelearn-5c94c.appspot.com/o/materiInterpersonal%2FKAP%202021%20TI%2013%20-%20Wawancara%20dan%20CMC.pdf?alt=media&token=132f2067-be33-46b5-89c9-dbf13f8f0953"));

        ProductIndoAdapter adapter = new ProductIndoAdapter(this,productIndoList);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_about_card_show);
        RelativeLayout relativeLayout = findViewById(R.id.indonesiaRL);
        recyclerView.setAnimation(animation);

        viewAllFilesUploaded();

        putPDFListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                putPDF putPDF = putPDFS.get(i);

                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(putPDF.getUrl()));
                startActivity(intent);
            }
        });

        addIndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdd = new Intent(Indonesia.this, AddIndo.class);
                startActivity(intentAdd);
            }
        });
    }

    private void viewAllFilesUploaded() {

        databaseReference = FirebaseDatabase.getInstance().getReference("materiInterpersonal");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    putPDF putPDF = postSnapshot.getValue(umn.ac.simplelearn.putPDF.class);
                    putPDFS.add(putPDF);
                }

                String[] uploads = new String[putPDFS.size()];

                for (int i = 0; i<uploads.length; i++){
                    uploads[i] = putPDFS.get(i).getName();
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, uploads){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView indoText = (TextView) view.findViewById(android.R.id.text1);
                        indoText.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                putPDFListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Indonesia.this, "File error", Toast.LENGTH_SHORT).show();
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