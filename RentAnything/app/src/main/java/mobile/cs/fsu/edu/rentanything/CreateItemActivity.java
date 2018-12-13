package mobile.cs.fsu.edu.rentanything;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateItemActivity extends AppCompatActivity {
    private static final String TAG = "CreateItemActivity";
    private FirebaseAuth mAuth;

    String title;
    float rate;
    String description;
    String User;
    String City;
    String Phone;
    String Userid;
    String listedBy;
    String rentedBy;

    @BindView(R.id.input_title) EditText _titleText;
    @BindView(R.id.input_rate) EditText _rateText;
    @BindView(R.id.input_description) EditText _descriptionText;
    @BindView(R.id.btn_list) Button _listButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        ButterKnife.bind(this);


        mAuth = FirebaseAuth.getInstance();
        _listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createItem();
            }
        });
    }


    public void createItem() {

        Log.d(TAG, "Signup");

        /*if (!validate()) {
            onSignupFailed();
            return;
        }*/

        _listButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(CreateItemActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Item...");
        progressDialog.show();

        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        listedBy = user.getUid();
        final DocumentReference docRef = database.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        User = document.getString("name");
                       Phone = document.getString("mobile");

                        title = _titleText.getText().toString();
                        rate = Float.valueOf(_rateText.getText().toString());
                        description = _descriptionText.getText().toString();

                        final Map<String, Object> list_item = new HashMap<>();
                        list_item.put("Description", description);
                        list_item.put("Phone", Phone);
                        list_item.put("Rate", rate);
                        list_item.put("Title", title);
                        list_item.put("User", User);
                        list_item.put("ListedBy", listedBy);
                        list_item.put("ListingID", "");
                        list_item.put("RentedBy", null);
                        FirebaseFirestore database1 = FirebaseFirestore.getInstance();
                        database1.collection("Items")
                                .add(list_item)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(final DocumentReference documentReference) {
                                        final FirebaseFirestore database2 = FirebaseFirestore.getInstance();
                                        final DocumentReference docRef2 = database2.collection("Items").document(documentReference.getId());
                                        final String listingid = documentReference.getId();

                                        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        if (document.exists()) {
                                                                                            FirebaseFirestore database3 = FirebaseFirestore.getInstance();
                                                                                            database3.collection("Items").document(documentReference.getId())
                                                                                                    .update("ListingID", listingid);


                                                                                        }

                                                                                    }
                                                                                }
                                                                            });

                                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document", e);
                                    }
                                });






                        finish();
                        Intent intent = new Intent(CreateItemActivity.this, MainPage.class);
                        startActivity(intent);
                            return;

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _listButton.setEnabled(true);
    }

    public void onSignupSuccess() {
        _listButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String name = _titleText.getText().toString();
        String rate = _rateText.getText().toString();
        String description = _descriptionText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _titleText.setError("at least 3 characters");
            valid = false;
        } else {
            _titleText.setError(null);
        }

        if (rate.isEmpty()) {
            _rateText.setError("Enter Valid Address");
            valid = false;
        } else {
            _rateText.setError(null);
        }

        if (description.isEmpty()){
            _descriptionText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _descriptionText.setError(null);
        }

        return valid;
    }

    public void showAlert(String errMessage){
        AlertDialog.Builder formNotComplete = new AlertDialog.Builder(CreateItemActivity.this);
        formNotComplete.setMessage(errMessage).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = formNotComplete.create();
        alert.setTitle("Alert!");
        alert.show();
    }

}
