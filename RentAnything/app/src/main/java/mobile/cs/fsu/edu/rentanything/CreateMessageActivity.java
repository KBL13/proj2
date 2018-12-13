package mobile.cs.fsu.edu.rentanything;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateMessageActivity extends AppCompatActivity{
    EditText desc;
    Button create_mess;

    String sender;
    String receiver;

    String transaction;
    String mMessage;

    boolean temp = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_message);

        desc = (EditText) findViewById(R.id.input_description);
        create_mess = (Button) findViewById(R.id.send_message);

        Bundle extras = getIntent().getExtras();
        sender = extras.getString("sender");
        receiver = extras.getString("receiver");
        transaction = sender + "&" + receiver;
        final String time = Long.toString(System.currentTimeMillis());


        create_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessage = desc.getText().toString();
                final Map<String, Object> letter = new HashMap<>();
                letter.put("sentby", sender);
                letter.put("message", mMessage);

                final FirebaseFirestore database = FirebaseFirestore.getInstance();

                FirebaseFirestore.getInstance().collection("Chat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String find_dup;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                find_dup = document.getId();
                                if(find_dup.contains(sender) && find_dup.contains(receiver))
                                {
                                    //If there has already been a conversation between the two just but its flipped
                                    //example receiver&sender then just add that document id to be written to
                                    temp = false;
                                    transaction = find_dup;
                                }
                            }
                            //Log.d(TAG, list.toString());
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

                database.collection("Chat").document(transaction).collection("messages").document(time)
                        .set(letter)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                    finish();
                    Intent intent = new Intent(getApplicationContext(), MyListingActivity.class);
                    startActivity(intent);
            }
        });
    }

}
