package com.cuncisboss.eudekafirebaseexample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cuncisboss.eudekafirebaseexample.R;
import com.cuncisboss.eudekafirebaseexample.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private List<User> userList;
    private Context context;
    private ClickMenuListener clickMenuListener;

    public UserAdapter(List<User> userList, Context context, ClickMenuListener clickMenuListener) {
        this.userList = userList;
        this.context = context;
        this.clickMenuListener = clickMenuListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, final int position) {
        holder.tvName.setText(userList.get(position).getName());
        holder.tvEmail.setText(userList.get(position).getEmail());
        holder.tvPhone.setText(userList.get(position).getPhone());
        holder.btnSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMenuListener.onClickMenu(holder.btnSelection, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone;
        ImageButton btnSelection;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            btnSelection = itemView.findViewById(R.id.btn_selection);
        }
    }

    public interface ClickMenuListener {
        void onClickMenu(ImageButton btnSelection, int position);
    }

}
