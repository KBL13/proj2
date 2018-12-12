package mobile.cs.fsu.edu.rentanything;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> mObjects;
    private Context mContext;

    public ItemArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
        mObjects = new ArrayList<>();
    }

    // Create a class to represent the View of MyObject
    private static class MyObjectHolder {
        TextView titletext;
        TextView ratetext;
        TextView phonetext;
        TextView descriptiontext;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get mObjects[position]
        final Item item = getItem(position);
        MyObjectHolder viewHolder;
        if (convertView == null) {
            // If row has not been created, inflate from row_object.xml
            //   and assign viewHolder to tag
            viewHolder = new MyObjectHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_object, parent, false);
            viewHolder.titletext = convertView.findViewById(R.id.titletext);
            viewHolder.ratetext = convertView.findViewById(R.id.ratetext);
            viewHolder.phonetext = convertView.findViewById(R.id.phonetext);
            viewHolder.descriptiontext = convertView.findViewById(R.id.descriptiontext);
            convertView.setTag(viewHolder);
        } else {
            // If row has been created, get viewHolder from tag
            viewHolder = (MyObjectHolder) convertView.getTag();
        }

        // Update viewHolder textView
        viewHolder.titletext.setText(item.getTitle());

        String ratetext = "$" + Float.toString(item.getRate()) + "/hr";
        String phonetext = "#" + item.getPhone();
        viewHolder.ratetext.setText(ratetext);
        viewHolder.phonetext.setText(phonetext);
        viewHolder.descriptiontext.setText(item.getDescription());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change textView color when row is clicked
                // Let ArrayAdapter now that mObjects list has changed,
                //  forces listview to update rows
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Nullable
    @Override
    public Item getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public int getPosition(@Nullable Item item) {
        // Find if item is already in mObjects by checking text
        for (int i = 0; i < mObjects.size(); i++) {
            if (TextUtils.equals(item.getTitle(), mObjects.get(i).getTitle())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void add(@Nullable Item object) {
        int idx = getPosition(object);
        if (idx >= 0) {
            mObjects.set(idx, object);
        } else {
            mObjects.add(object);
        }
        notifyDataSetChanged();
    }
}
