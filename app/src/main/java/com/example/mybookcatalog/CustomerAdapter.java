package com.example.mybookcatalog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<Customer> customerList;

    public CustomerAdapter(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.textViewName.setText(customer.getName());
        holder.textViewType.setText(customer.getType());
        holder.textViewContact.setText(customer.getContactInfo());
        holder.textViewBalance.setText(String.format(Locale.US, "Balance: $%.2f", customer.getOutstandingBalance()));
        holder.textViewCredit.setText(String.format(Locale.US, "Credit Limit: $%.2f", customer.getCreditLimit()));

        if (customer.getOutstandingBalance() > 0) {
            holder.textViewBalance.setTextColor(0xFFD32F2F); // Red
        } else {
            holder.textViewBalance.setTextColor(0xFF2E7D32); // Green
        }
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewType, textViewContact, textViewBalance, textViewCredit;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewCustomerName);
            textViewType = itemView.findViewById(R.id.textViewCustomerType);
            textViewContact = itemView.findViewById(R.id.textViewCustomerContact);
            textViewBalance = itemView.findViewById(R.id.textViewOutstandingBalance);
            textViewCredit = itemView.findViewById(R.id.textViewCreditLimit);
        }
    }
}
