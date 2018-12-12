package mobile.cs.fsu.edu.rentanything;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import butterknife.BindView;

public class MainPage extends AppCompatActivity {

    private static final String TAG = "MainPage";
    private FirebaseAuth mAuth;

    private ItemArrayAdapter mAdapter;

    ListView lv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        Button _listbutton = findViewById(R.id.listbutton);

        lv = (ListView)findViewById(R.id._itemlist);

        mAdapter = new ItemArrayAdapter(MainPage.this,R.layout.row_object);
        lv.setAdapter(mAdapter);

        _listbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), CreateItemActivity.class);
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
