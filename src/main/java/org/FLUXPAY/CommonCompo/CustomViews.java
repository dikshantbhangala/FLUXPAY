package main.java.org.FLUXPAY.CommonCompo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.fluxpay.android.R;
import com.fluxpay.android.databinding.ViewCustomHeaderBinding;

public class CustomViews {

    /**
     * Custom header view used throughout the app
     */
    public static class HeaderView extends FrameLayout {
        private ViewCustomHeaderBinding binding;
        
        public HeaderView(@NonNull Context context) {
            super(context);
            init(context, null);
        }
        
        public HeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context, attrs);
        }
        
        public HeaderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context, attrs);
        }
        
        private void init(Context context, AttributeSet attrs) {
            binding = ViewCustomHeaderBinding.inflate(LayoutInflater.from(context), this, true);
            
            if (attrs != null) {
                TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);
                
                String title = typedArray.getString(R.styleable.HeaderView_headerTitle);
                boolean showBackButton = typedArray.getBoolean(R.styleable.HeaderView_showBackButton, true);
                boolean showActionButton = typedArray.getBoolean(R.styleable.HeaderView_showActionButton, false);
                Drawable actionIcon = typedArray.getDrawable(R.styleable.HeaderView_actionIcon);
                
                setTitle(title);
                setShowBackButton(showBackButton);
                setShowActionButton(showActionButton);
                
                if (actionIcon != null) {
                    setActionIcon(actionIcon);
                }
                
                typedArray.recycle();
            }
        }
        
        public void setTitle(String title) {
            binding.tvHeaderTitle.setText(title);
        }
        
        public void setShowBackButton(boolean show) {
            binding.ivHeaderBack.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        
        public void setBackButtonClickListener(OnClickListener listener) {
            binding.ivHeaderBack.setOnClickListener(listener);
        }
        
        public void setShowActionButton(boolean show) {
            binding.ivHeaderAction.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        
        public void setActionIcon(Drawable icon) {
            binding.ivHeaderAction.setImageDrawable(icon);
        }
        
        public void setActionIcon(@DrawableRes int iconResId) {
            binding.ivHeaderAction.setImageResource(iconResId);
        }
        
        public void setActionButtonClickListener(OnClickListener listener) {
            binding.ivHeaderAction.setOnClickListener(listener);
        }
    }
    
    /**
     * Currency item view used in currency selection
     */
    public static class CurrencyItemView extends FrameLayout {
        private ImageView ivFlag;
        private TextView tvCode;
        private TextView tvName;
        
        public CurrencyItemView(@NonNull Context context) {
            super(context);
            init(context);
        }
        
        public CurrencyItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }
        
        public CurrencyItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }
        
        private void init(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_currency_item, this, true);
            ivFlag = view.findViewById(R.id.iv_currency_flag);
            tvCode = view.findViewById(R.id.tv_currency_code);
            tvName = view.findViewById(R.id.tv_currency_name);
        }
        
        public void setCurrencyCode(String code) {
            tvCode.setText(code);
        }
        
        public void setCurrencyName(String name) {
            tvName.setText(name);
        }
        
        public void setCurrencyFlag(int flagResId) {
            ivFlag.setImageResource(flagResId);
        }
        
        public void setCurrencyFlag(Drawable flagDrawable) {
            ivFlag.setImageDrawable(flagDrawable);
        }
    }
    
    /**
     * Transaction item view used in transaction lists
     */
    public static class TransactionItemView extends FrameLayout {
        private ImageView ivTransactionType;
        private TextView tvAmount;
        private TextView tvCurrency;
        private TextView tvDate;
        private TextView tvStatus;
        
        public TransactionItemView(@NonNull Context context) {
            super(context);
            init(context);
        }
        
        public TransactionItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }
        
        public TransactionItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }
        
        private void init(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_transaction_item, this, true);
            ivTransactionType = view.findViewById(R.id.iv_transaction_type);
            tvAmount = view.findViewById(R.id.tv_transaction_amount);
            tvCurrency = view.findViewById(R.id.tv_transaction_currency);
            tvDate = view.findViewById(R.id.tv_transaction_date);
            tvStatus = view.findViewById(R.id.tv_transaction_status);
        }
        
        public void setTransactionType(boolean isSent) {
            int iconResId = isSent ? 
                R.drawable.ic_transaction_sent : R.drawable.ic_transaction_received;
            ivTransactionType.setImageResource(iconResId);
        }
        
        public void setAmount(String amount) {
            tvAmount.setText(amount);
        }
        
        public void setCurrency(String currency) {
            tvCurrency.setText(currency);
        }
        
        public void setDate(String date) {
            tvDate.setText(date);
        }
        
        public void setStatus(String status) {
            tvStatus.setText(status);
            
            int colorResId;
            switch (status) {
                case "COMPLETED":
                    colorResId = R.color.transaction_status_completed;
                    break;
                case "PENDING":
                    colorResId = R.color.transaction_status_pending;
                    break;
                case "FAILED":
                    colorResId = R.color.transaction_status_failed;
                    break;
                default:
                    colorResId = R.color.transaction_status_processing;
                    break;
            }
            
            tvStatus.setTextColor(ContextCompat.getColor(getContext(), colorResId));
        }
    }
}