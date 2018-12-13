package mobile.cs.fsu.edu.rentanything;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MyRentalActivity extends AppCompatActivity {
    private static final String TAG = "ListingActivity";
    ListView list;
    private ItemArrayAdapter mAdapter1;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rental);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();


        list = (ListView)findViewById(R.id._rentals);

        mAdapter1 = new ItemArrayAdapter(MyRentalActivity.this,R.layout.row_object);
        list.setAdapter(mAdapter1);




        populateList();

    }

    public void populateList(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();



        CollectionReference myRef = database.collection("Items");
        Log.d("TEST","TEST");
        database.collection("Items")
                .whereEqualTo("RentedBy", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("Title");
                                String phone = document.getString("Phone");
                                float rate = document.getLong("Rate").floatValue();
                                String description  = document.getString("Description");

                                Item item = new Item();
                                item.setTitle(title);
                                item.setPhone(phone);
                                item.setRate(rate);
                                item.setDescription(description);

                                mAdapter1.add(item);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }



}
