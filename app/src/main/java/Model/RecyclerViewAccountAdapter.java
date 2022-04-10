package Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lasalle.communitybank.R;

import java.util.ArrayList;


public class RecyclerViewAccountAdapter extends RecyclerView.Adapter<RecyclerViewAccountAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Account>accountArrayList;

    public RecyclerViewAccountAdapter(Context context, ArrayList<Account> accountArrayList) {
        this.context = context;
        this.accountArrayList = accountArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAccountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_details,parent,false);
        return new RecyclerViewAccountAdapter.MyViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAccountAdapter.MyViewHolder holder, int position) {
        holder.txtAccountType.setText("Account Type: "+ accountArrayList.get(position).getAccountType().toString());
        holder.txtAccountBalance.setText("Balance: " + accountArrayList.get(position).getBalance());
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txtAccountType,txtAccountBalance;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAccountType = itemView.findViewById(R.id.txtAccountType);
            txtAccountBalance = itemView.findViewById(R.id.txtAccountBalance);
        }
    }
}