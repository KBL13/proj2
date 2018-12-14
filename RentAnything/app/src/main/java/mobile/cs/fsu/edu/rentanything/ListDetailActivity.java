package mobile.cs.fsu.edu.rentanything;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ListDetailActivity extends AppCompatActivity {
    private static final String TAG = "ListDetailActivity";
    TextView detailtitle;
    TextView detailphone;
    TextView detailrate;
    TextView detailuser;
    TextView detailstatus;
    TextView detaildescription;
    Button messagebutton;
    Button deletebutton;
    String user1;
    String user2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);
        mAuth = FirebaseAuth.getInstance();

        detailtitle = (TextView)findViewById(R.id._detailtitle);
        detailphone = (TextView)findViewById(R.id._detailphone);
        detailrate = (TextView)findViewById(R.id._detailrate);
        detailuser = (TextView)findViewById(R.id._detailuser);
        detailstatus = (TextView)findViewById(R.id._detailstatus);
        detaildescription = (TextView)findViewById(R.id._detaildescription);
        messagebutton = (Button)findViewById(R.id._detailmessage);
        deletebutton = (Button)findViewById(R.id._detaildelete);

        deletebutton.setVisibility(View.INVISIBLE);

        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        final FirebaseUser user = mAuth.getCurrentUser();

        final String ListID = getIntent().getStringExtra("ListID");

        Log.e("ID CLICKED:",ListID);

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                database.collection("Items").document(ListID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getBaseContext(), "Deleted Item", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                finish();
                Intent intent = new Intent(getApplicationContext(), MyListingActivity.class);
                startActivity(intent);
            }
        });

        messagebutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                user1 = user.getUid();
                Intent intent_test = new Intent(ListDetailActivity.this,CreateMessageActivity.class);
                intent_test.putExtra("sender",user1);
                intent_test.putExtra("receiver",user2);
                startActivity(intent_test);
            }
        });






        database.collection("Items")
                .whereEqualTo("ListingID", ListID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("TestClick","TESTCLICKED");

                               String listedby = document.getString("ListedBy");
                               String currentuser = user.getUid();

                               if(currentuser.equals(listedby)){
                                   deletebutton.setVisibility(View.VISIBLE);
                               }

                                String title = document.getString("Title");
                                String phone = document.getString("Phone");
                                float rate = document.getLong("Rate").floatValue();
                                String description  = document.getString("Description");
                                String listid = document.getString("ListingID");
                                String status = document.getString("RentedBy");
                                String username = document.getString("User");
                                user2 = document.getString("ListedBy");


                                detailtitle.setText(title);
                                detailphone.setText(phone);
                                detailrate.setText("$" + Float.toString(rate) + "/hr");

                             if(status == null) {
                                 detailstatus.setText("Available");
                                 detailstatus.setTextColor(Color.GREEN);
                             }
                             else{
                                 detailstatus.setText("Unavailable");
                                 detailstatus.setTextColor(Color.RED);
                             }
                             detaildescription.setText(description);
                             detailuser.setText(username);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
}
