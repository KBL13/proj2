package mobile.cs.fsu.edu.rentanything;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;

public class ViewMessagesActivity extends AppCompatActivity {
    private static final String TAG = "ListingActivity";
    ListView list;

    private MessagesArrayAdapter mAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_messages);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore database = FirebaseFirestore.getInstance();


        list = (ListView)findViewById(R.id._listings);

        mAdapter = new MessagesArrayAdapter(ViewMessagesActivity.this,R.layout.row_messages);
        list.setAdapter(mAdapter);


        populateList();

    }

    public void populateList(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        final List<String> transaction_list = new ArrayList<>();

        final String sub = user.getUid();

        database.collection("chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        String compare= document.getId();
                        if(compare.contains(sub)) {
                            transaction_list.add(document.getId());
                        }
                    }
                }
            }
        });

        CollectionReference myRef = database.collection("Items");
        Log.d("TEST","TEST");
        for (int i = 0; i < transaction_list.size();i++) {

            database.collection("Chat")
                    .document(transaction_list.get(i)).collection("messages").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String other_person = document.getString("sentby");
                                    String msg = document.getString("message");

                                    mobile.cs.fsu.edu.rentanything.Message temp1 = new mobile.cs.fsu.edu.rentanything.Message();
                                    temp1.setPerson(other_person);
                                    temp1.setmMessage(msg);

                                    mAdapter.add(temp1);
                                    //mAdapter.add(mMessage);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }

    }



}
