package mobile.cs.fsu.edu.rentanything;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "ListingActivity";
    ListView list;
    private ItemArrayAdapter mAdapter;
    private FirebaseAuth mAuth;
    String searchID;
    String lowersearchID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        list = (ListView)findViewById(R.id._resultlistings);
        mAdapter = new ItemArrayAdapter(SearchActivity.this,R.layout.row_object);
        list.setAdapter(mAdapter);

        searchID = getIntent().getStringExtra("Search");

       lowersearchID = searchID.toLowerCase();

        Log.e("SEARCH:",searchID);

        populateList();
    }


    public void populateList(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        database.collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String description = document.getString("Description");

                                String[] wordvals = description.split(" ");

                                String[] lowerwordvals = new String[wordvals.length];

                                for(int i = 0; i < wordvals.length;i++ ){
                                    lowerwordvals[i] = wordvals[i].toLowerCase();
                                    Log.e("STRINGVAL:",lowerwordvals[i]);
                                }

                                for(int i = 0; i < lowerwordvals.length;i++ ){
                                   if(lowerwordvals[i].equals(lowersearchID)){
                                       String title = document.getString("Title");
                                       String phone = document.getString("Phone");
                                       float rate = document.getLong("Rate").floatValue();
                                       String newdescription  = document.getString("Description");

                                       Item item = new Item();
                                       item.setTitle(title);
                                       item.setPhone(phone);
                                       item.setRate(rate);
                                       item.setDescription(newdescription);

                                       mAdapter.add(item);
                                       break;

                                   }

                                }




                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }






}
