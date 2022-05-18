package umn.ac.simplelearn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class AddIng extends AppCompatActivity {

    private EditText editPDFName;
    private Button chooseIng;
    private Button uploadIng;
    private ProgressBar mProgressBar;

    private Uri mPDFUri;

    StorageReference storageReference;
    DatabaseReference databaseReference;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ing);

        editPDFName = (EditText) findViewById(R.id.addIngET);
        chooseIng = (Button) findViewById(R.id.chooseIng);
        uploadIng = (Button) findViewById(R.id.uploadIng);
        mProgressBar = findViewById(R.id.progressBar3);

        storageReference = FirebaseStorage.getInstance().getReference("materiInggris");
        databaseReference = FirebaseDatabase.getInstance().getReference("materiInggris");

        chooseIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPDF();
            }
        });

        uploadIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(AddIng.this, "Upload is in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadPDF();
                }
            }
        });
    }

    private void selectPDF(){

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            mPDFUri = data.getData();

        }
    }

    private void uploadPDF() {
        if (mPDFUri != null) {
            StorageReference reference = storageReference.child("upload"+System.currentTimeMillis() + ".pdf");

            mUploadTask = reference.putFile(mPDFUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(0);
                        }
                    }, 5000);

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri url = uriTask.getResult();

                            ProductIng productIng = new ProductIng(editPDFName.getText().toString(),url.toString());
                            databaseReference.child(databaseReference.push().getKey()).setValue(productIng);

                            Toast.makeText(AddIng.this, "File uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddIng.this, "File error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddIng.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    mProgressBar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}