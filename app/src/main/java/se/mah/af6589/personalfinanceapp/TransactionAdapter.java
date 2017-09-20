package se.mah.af6589.personalfinanceapp;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private ArrayList<Transaction> transactions;
    private Controller controller;
    private int lastPosition = -1;

    public TransactionAdapter(ArrayList<Transaction> transactions, Controller controller) {
        this.transactions = transactions;
        this.controller = controller;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.tvTitle.setText(transaction.getTitle());
        holder.tvId.setText("#" + transaction.getId());
        holder.tvDate.setText(transaction.getDate().toString());
        holder.tvAmount.setText(String.valueOf(transaction.getAmount()) + " kr");
        holder._id = transaction.getId();
        Drawable drawable = null;
        switch (transaction.getCategory()) {
            case Food:
                drawable = controller.getFood();
                break;
            case Accomodation:
                drawable = controller.getAccomodation();
                break;
            case Travel:
                drawable = controller.getTravel();
                break;
            case Leisure:
                drawable = controller.getLeisure();
                break;
            case Salary:
                drawable = controller.getSalary();
                break;
            case Other:
                drawable = controller.getOther();
                break;
        }
        holder.ivIcon.setImageDrawable(drawable);
        setAnimation(holder.parent, position);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation((AppCompatActivity) controller.getActivity(), android.R.anim.slide_in_left);
            animation.setDuration(400);
            animation.setStartOffset(position * 50);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void updateData(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle, tvId, tvDate, tvAmount;
        public ImageView ivIcon;
        public View parent;
        public int _id;

        public ViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvAmount = (TextView) itemView.findViewById(R.id.tv_amount);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
        }
    }
}
