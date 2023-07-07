package com.blindstick.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blindstick.Models.ModelLocation;
import com.blindstick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminHome extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    List<String> victimItems = new ArrayList<>();
    List<ModelLocation> victimItems2 = new ArrayList<>();
    ProgressDialog progressDialog;
    private ListView list_view;
    private ArrayAdapter<String> mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView txtnoitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);

        list_view = (ListView) findViewById(R.id.list_view);
        txtnoitem = (TextView) findViewById(R.id.txtnoitem);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ModelLocation fire = victimItems2.get(position);
                Toast.makeText(AdminHome.this, "uid = "+fire.getPhone(), Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(AdminHome.this)
                        .setTitle(getResources().getString(R.string.app_name))
                        .setIcon(R.drawable.ic_stick)
                        .setMessage("Show on map ?")
                        .setNegativeButton("no",null)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("http://maps.google.com/maps?daddr="+ fire.getLat() +","+ fire.getLng()));
                                startActivity(intent);
                            }
                        })
                        .show();
//                Intent intent = new Intent(getApplicationContext(),TouristPlaceDetails.class);
//                intent.putExtra("fileurl",fire.getFileurl());
//                intent.putExtra("pname",fire.getPname());
//                intent.putExtra("desc",fire.getDesc());
//                startActivity(intent);
            }
        });


        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadJSON();
                    }
                }
        );
    }
    private void loadJSON(){
        swipeRefreshLayout.setRefreshing(true);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        myRef = FirebaseDatabase.getInstance().getReference("locations");
        Query query = myRef.orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                victimItems = new ArrayList<String>();
                victimItems2 = new ArrayList<ModelLocation>();
                System.out.println("\n\n\nDataSnap Snap"+dataSnapshot);
                for (DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    System.out.println("\n\n\nItem Snap"+itemSnapshot.getKey());
                    ModelLocation fire = new ModelLocation();
                    fire.setPhone((String) itemSnapshot.getKey());
                    fire.setName((String) itemSnapshot.child("name").getValue());
                    fire.setLat((String) itemSnapshot.child("lat").getValue());
                    fire.setLng((String) itemSnapshot.child("lng").getValue());
                    victimItems.add("Name : "+fire.getName()+"\nPhone : "+fire.getPhone());
                    victimItems2.add(fire);

                    swipeRefreshLayout.setRefreshing(false);
                    progressDialog.dismiss();

                }
                System.out.println("\n\n\n"+victimItems);
                //Toast.makeText(AdminHome.this, ""+victimItems, Toast.LENGTH_SHORT).show();

                if (victimItems.size() == 0){
                    list_view.setVisibility(View.GONE);
                    txtnoitem.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }else {
                    mAdapter = new ArrayAdapter<String>(AdminHome.this, android.R.layout.simple_list_item_1, victimItems);
                    list_view.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException(); // don't ignore errors
            }
        });

        progressDialog.dismiss();


    }

    @Override
    public void onRefresh() {
        loadJSON();
    }
}