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

    private static RecyclerViewInterfaceAccount recyclerViewInterfaceAccount;
    private Context context;
    private ArrayList<Account>accountArrayList;

    public RecyclerViewAccountAdapter(Context context, ArrayList<Account> accountArrayList,RecyclerViewInterfaceAccount recyclerViewInterfaceAccount) {
        this.context = context;
        this.accountArrayList = accountArrayList;
        this.recyclerViewInterfaceAccount = recyclerViewInterfaceAccount;
    }

    @NonNull
    @Override
    public RecyclerViewAccountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_details,parent,false);
        return new RecyclerViewAccountAdapter.MyViewHolder(view,recyclerViewInterfaceAccount);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAccountAdapter.MyViewHolder holder, int position) {
        holder.txtAccountType.setText(accountArrayList.get(position).getAccountType().toString());
        holder.txtAccountBalance.setText("$"+ accountArrayList.get(position).getBalance());
        holder.txtAccountNumber.setText(accountArrayList.get(position).getAccountNumber());
    }

    @Override
    public int getItemCount() {
        return accountArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtAccountType;
        private final TextView txtAccountBalance;
        private final TextView txtAccountNumber;

        public MyViewHolder(@NonNull View itemView,RecyclerViewInterfaceAccount recyclerViewInterfaceAccount) {
            super(itemView);
            txtAccountType = itemView.findViewById(R.id.txtAccountType);
            txtAccountBalance = itemView.findViewById(R.id.txtAccountBalance);
            txtAccountNumber = itemView.findViewById(R.id.txtAccountNumber);

            itemView.setOnClickListener(view -> {
                if (recyclerViewInterfaceAccount != null){
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION){
                        recyclerViewInterfaceAccount.onItemClick(pos);
                    }
                }
            });
        }
    }
}