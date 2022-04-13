package Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.project.lasalle.communitybank.R;

import java.util.ArrayList;

public class RecyclerViewTransactions extends RecyclerView.Adapter<RecyclerViewTransactions.MyViewHolder> {

    private Context context;
    private ArrayList<Transaction> transactionArrayList;

    public RecyclerViewTransactions(Context context, ArrayList<Transaction> transactionArrayList) {
        this.context = context;
        this.transactionArrayList = transactionArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewTransactions.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_details,parent,false);
        return new RecyclerViewTransactions.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewTransactions.MyViewHolder holder, int position) {
        holder.txtDate.setText(transactionArrayList.get(position).getDate().toString());
        holder.txtAmount.setText(String.valueOf(transactionArrayList.get(position).getAmount()));
        holder.txtTransId.setText(transactionArrayList.get(position).getTransactionType().toString());

        if (transactionArrayList.get(position).getTransactionType().toString().equals("Withdraw")){
            holder.imgTrans.setImageResource(R.drawable.ic_withdraw);
        }else{
            holder.imgTrans.setImageResource(R.drawable.ic_deposit);
        }
    }

    @Override
    public int getItemCount() {
        return transactionArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTransId, txtAmount,txtDate;
        private final ImageView imgTrans;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTransId = itemView.findViewById(R.id.txtTransId);
            txtAmount = itemView.findViewById(R.id.txtTransAmount);
            txtDate = itemView.findViewById(R.id.txtDate);
            imgTrans = itemView.findViewById(R.id.imageTransaction);

        }
    }
}
