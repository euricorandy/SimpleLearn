package umn.ac.simplelearn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderboardsAdapter extends RecyclerView.Adapter<LeaderboardsAdapter.LeaderboardViewHolder> {

    Context context;
    ArrayList<User> users;
    public LeaderboardsAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_leaderboards, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        User user = users.get(position);

        holder.name.setText(user.getNamaMahasiswa());
        holder.coins.setText(String.valueOf(user.getCoins()));
        holder.no_rank.setText(String.format("#%d", position+1));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder {

        TextView no_rank, name, coins;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            no_rank = itemView.findViewById(R.id.no_rank);
            name = itemView.findViewById(R.id.name);
            coins = itemView.findViewById(R.id.coins);
        }

    }

}
