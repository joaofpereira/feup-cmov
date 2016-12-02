package com.example.joao.cafeteria_client_app.Cafeteria;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joao.cafeteria_client_app.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class PastTransactionsAdapter extends RecyclerView.Adapter<PastTransactionsAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    private final List<Transaction> transactions;
    private final OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView transaction_id, transaction_date, transaction_total_price;

        public Transaction transaction;

        public MyViewHolder(View view) {
            super(view);
            transaction_id = (TextView) view.findViewById(R.id.transaction_id);
            transaction_date = (TextView) view.findViewById(R.id.transaction_date);
            transaction_total_price = (TextView) view.findViewById(R.id.transaction_total_price);
        }

        public void bind(final Transaction transaction, final OnItemClickListener listener) {
            transaction_id.setText("Transaction nº. " + transaction.getId());
            transaction_date.setText("at " + new SimpleDateFormat("HH:mm:ss MM-dd-yyyy").format(transaction.getDate()));
            transaction_total_price.setText("Value: " + String.format(java.util.Locale.US,"%.2f", transaction.getTotalPrice()) + " €");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(transaction);
                }
            });
        }

    }

    public PastTransactionsAdapter(List<Transaction> transactions, OnItemClickListener listener) {
        this.transactions = transactions;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.past_transactions_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(transactions.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }
}