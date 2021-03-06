package sit374_team17.propertyinspector.Walkthrough;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sit374_team17.propertyinspector.R;

public class Adapter_Loan extends RecyclerView.Adapter<Adapter_Loan.ViewHolder> {
    private List<Loan> mList;
    private Context mContext;
    private Listener_Walkthrough mListener;

    public Adapter_Loan(Listener_Walkthrough listener, Context context) {
        mList = new ArrayList<>();
        mListener = listener;
        mContext = context;
    }

    public void setList(ArrayList<Loan> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Adapter_Loan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_walkthrough, parent, false);
        return new Adapter_Loan.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(final Adapter_Loan.ViewHolder holder, int position) {
        holder.mLogo.setImageDrawable(mList.get(position).getLogo());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Loan loan = mList.get(holder.getAdapterPosition());
                mListener.goTo_2_loan_details(loan);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mName;
        private ImageView mLogo;
        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mName = (TextView) itemView.findViewById(R.id.textView_name);
            mLogo = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}