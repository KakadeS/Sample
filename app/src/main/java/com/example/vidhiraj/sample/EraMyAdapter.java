package com.example.vidhiraj.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by vidhiraj on 10-08-2016.
 */
public class EraMyAdapter extends RecyclerView.Adapter<EraMyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    private static final int TYPE_ITEM = 1;
    private String mNavTitles[]; // String Array to store the passed titles Value from MainActivity.java
    private int mIcons[];       // Int Array to store the passed icons resource value from MainActivity.java
    private String email;
    String url_icon;
    //String Resource for header view email

    static Context context;
    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
      //  ImageView imageView;
        ImageView profile;
        TextView email;
        // private Context context = null;

        public ViewHolder(View itemView, int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
        //        imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        Intent intent1;
                        if (pos == 1) {
                            textView.setTextColor(context.getResources().getColor(R.color.bb_darkBackgroundColor));
                            intent1 = new Intent(context, ClassActivity.class);
                            context.startActivity(intent1);
                        } else if (pos == 2) {
                            textView.setTextColor(context.getResources().getColor(R.color.bb_darkBackgroundColor));
                            intent1 = new Intent(context, DailyCatalogActivity.class);
                            context.startActivity(intent1);
                        } else if (pos == 3) {
                            textView.setTextColor(context.getResources().getColor(R.color.bb_darkBackgroundColor));
                            intent1 = new Intent(context, StudentListActivity.class);
                            context.startActivity(intent1);
                        } else if (pos == 4) {
                            textView.setTextColor(context.getResources().getColor(R.color.bb_darkBackgroundColor));
                            intent1 = new Intent(context, AndroidSpinnerExampleActivity.class);
                            context.startActivity(intent1);
                        }
                    }
                });
                // setting holder id as 1 as the object being populated are of type item row
            } else {

                // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }

        }


    }


    EraMyAdapter(Context context, String Titles[], int Icons[], String Email, String url) { // MyAdapter Constructor with titles and icons parameter
        // titles, icons, name, email, profile pic are passed from the main activity as we
        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        email = Email;
        url_icon = url;
        this.context = context;
        //here we assign those passed values to the values we declared here
        //in adapter


    }


    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    @Override
    public EraMyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eracord_items, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eracord_header, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;

    }

    @Override
    public void onBindViewHolder(EraMyAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
          //  holder.imageView.setImageResource(mIcons[position - 1]);// Settimg the image with array of our icons
        } else {


                        Glide.with(context).load(url_icon)
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(holder.profile);
            holder.email.setText(email);
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}

