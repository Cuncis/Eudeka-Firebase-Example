package com.cuncisboss.eudekafirebaseexample;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.cuncisboss.eudekafirebaseexample.adapter.UserAdapter;
import com.cuncisboss.eudekafirebaseexample.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UserAdapter.ClickMenuListener {

    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;
    private View dialogView;

    private RecyclerView recyclerView;
    private FloatingActionButton fabAddUser;

    private UserAdapter adapter;
    private List<User> userList;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = new ArrayList<>();


        initView();
        initAdapter();

        firebaseDatabase = FirebaseDatabase.getInstance();

        // reference to node users
        databaseReference = firebaseDatabase.getReference("users");

        // save reference node app title
        firebaseDatabase.getReference("app_title").setValue("Eudeka Real Database");

        addUserEventListener();
    }

    private void createUser(String name, String email, String phone) {
        User user = new User(name, email, phone);
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(user);
    }

    private void addUserEventListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();

                if (!userList.isEmpty()) {
                    userList.clear();
                }

                while (iterator.hasNext()) {
                    User user = iterator.next().getValue(User.class);
                    userList.add(user);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("_logMain", "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    private void initAdapter() {
        adapter = new UserAdapter(userList, this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        fabAddUser = findViewById(R.id.fab_add);
        fabAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddUser();
            }
        });
    }

    private void showDialogAddUser() {
        layoutInflater = LayoutInflater.from(this);
        dialogView = layoutInflater.inflate(R.layout.add_data_user, null);

        builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final EditText etName = dialogView.findViewById(R.id.et_name);
        final EditText etEmail = dialogView.findViewById(R.id.et_email);
        final EditText etPhone = dialogView.findViewById(R.id.et_phone);
        Button btnSave = dialogView.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill the field :(", Toast.LENGTH_SHORT).show();
                } else {
                    createUser(name, email, phone);
                    etName.setText("");
                    etEmail.setText("");
                    etPhone.setText("");
                    Toast.makeText(MainActivity.this, "Success...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.create().show();
    }

    @Override
    public void onClickMenu(final ImageButton btnSelection, final int position) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, btnSelection);
        popupMenu.inflate(R.menu.menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.update:
                        Toast.makeText(MainActivity.this, "Update clicked " + position, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.delete:
                        Toast.makeText(MainActivity.this, "Delete clicked " + position, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        popupMenu.show();
    }
}































