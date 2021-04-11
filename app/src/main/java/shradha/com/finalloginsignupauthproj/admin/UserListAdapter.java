package shradha.com.finalloginsignupauthproj.admin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import shradha.com.finalloginsignupauthproj.util.Constants;
import shradha.com.finalloginsignupauthproj.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private List<DocumentSnapshot> documents = new ArrayList<>();

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
       holder.name.setText(String.format("%s", documents.get(position).getData().get(Constants.KEY_NAME)));
        holder.age.setText(String.format("Age %s", documents.get(position).getData().get(Constants.KEY_AGE)));
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);

        }
    }

    public void setList(List<DocumentSnapshot> documentSnapshots) {
        this.documents = documentSnapshots;
        notifyDataSetChanged();
    }
}
