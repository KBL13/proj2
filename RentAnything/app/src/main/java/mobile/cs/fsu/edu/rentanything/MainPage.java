package mobile.cs.fsu.edu.rentanything;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import butterknife.BindView;

public class MainPage extends AppCompatActivity {

    private static final String TAG = "MainPage";
    private FirebaseAuth mAuth;
    private BroadcastReceiver broadcastReceiver;
    private ItemArrayAdapter mAdapter;
    String user_token;

    ListView lv;
    TextView title;

    Button listingbutton;
    Button rentalbutton;
    Button searchbutton;
    EditText search_item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                user_token = SharedPrefManager.getInstance(MainPage.this).getToken();
            }
        };

        if(SharedPrefManager.getInstance(this).getToken()!= null)
        {
            Log.d("myfcmtokenshared", SharedPrefManager.getInstance(this).getToken());
        }
        registerReceiver(broadcastReceiver, new IntentFilter(MyFirebaseInstanceIdService.TOKEN_BROADCAST));


        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();


        lv = (ListView)findViewById(R.id._itemlist);
        title = (TextView)findViewById(R.id.titletext);
        listingbutton = (Button)findViewById(R.id._listingbutton) ;
        rentalbutton = (Button)findViewById(R.id._rentalbutton) ;
        search_item = (EditText) findViewById(R.id.editText);
        searchbutton = (Button)findViewById(R.id._searchbutton);



        FirebaseUser user = mAuth.getCurrentUser();

        DocumentReference docRef = database.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String titletext = document.getString("Location");
                        SharedPrefManager.getInstance(getApplicationContext()).storeLocation(titletext);
                        title.setText("Listings for " + titletext + ": ");

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        String this_location = SharedPrefManager.getInstance(MainPage.this).getLocation();
        title.setText("Listings for " + this_location + ": ");
        mAdapter = new ItemArrayAdapter(MainPage.this,R.layout.row_object);
        lv.setAdapter(mAdapter);


        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext = search_item.getText().toString();
                Intent intent = new Intent(MainPage.this, SearchActivity.class);
                intent.putExtra("Search",searchtext);
                startActivity(intent);
            }
        });

         listingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPage.this, MyListingActivity.class);
                startActivity(intent);
            }
        });

        rentalbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainPage.this, MyRentalActivity.class);
                startActivity(intent);
            }
        });

        populateList();

    }

    public void populateList(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference myRef = database.collection("Items");
        Log.d("TEST","TEST");
        database.collection("Items")
                .whereEqualTo("Location", "Tallahassee")
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

                                mAdapter.add(item);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
