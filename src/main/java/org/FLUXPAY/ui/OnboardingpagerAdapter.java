package main.java.org.FLUXPAY.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fluxpay.R;

import java.util.List;

public class OnboardingPagerAdapter extends RecyclerView.Adapter<OnboardingPagerAdapter.OnboardingViewHolder> {

    private Context context;
    private List<OnboardingPage> pages;
    
    public OnboardingPagerAdapter(Context context, List<OnboardingPage> pages) {
        this.context = context;
        this.pages = pages;
    }
    
    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_onboarding_page, parent, false);
        return new OnboardingViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        OnboardingPage page = pages.get(position);
        holder.imageView.setImageResource(page.getImageResId());
        holder.titleTextView.setText(page.getTitle());
        holder.descriptionTextView.setText(page.getDescription());
    }
    
    @Override
    public int getItemCount() {
        return pages.size();
    }
    
    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;
        
        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_onboarding);
            titleTextView = itemView.findViewById(R.id.tv_title);
            descriptionTextView = itemView.findViewById(R.id.tv_description);
        }
    }
}