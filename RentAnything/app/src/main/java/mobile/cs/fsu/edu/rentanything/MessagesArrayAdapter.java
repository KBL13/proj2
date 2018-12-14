package mobile.cs.fsu.edu.rentanything;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MessagesArrayAdapter extends ArrayAdapter<Message> {
    private ArrayList<Message> mObjects;
    private Context mContext;

    public MessagesArrayAdapter(Context context, int mObjects) {
        super(context, 0, mObjects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Message test1 = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_messages, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.titleText);
        TextView tvMsg = (TextView) convertView.findViewById(R.id.emailText);
        // Populate the data into the template view using the data object
        tvName.setText(test1.User);
        tvMsg.setText(test1.mMessage);
        // Return the completed view to render on screen
        return convertView;
    }
}
