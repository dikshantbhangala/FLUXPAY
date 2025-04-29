package main.java.org.FLUXPAY.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fluxpay.R;
import com.fluxpay.data.model.Transaction;
import com.fluxpay.data.model.User;
import com.fluxpay.ui.payment.PaymentActivity;
import com.fluxpay.ui.transactions.TransactionAdapter;
import com.fluxpay.ui.transactions.TransactionDetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment implements TransactionAdapter.OnTransactionClickListener {

    private HomeViewModel viewModel;
    private TextView tvUserName;
    private TextView tvBalance;
    private TextView tvBalanceSubtitle;
    private RecyclerView rvRecentTransactions;
    private TransactionAdapter transactionAdapter;
    private CardView cardExchangeRates;
    private CardView cardStudentDiscount;
    private CardView cardBusinessTools;
    private FloatingActionButton fabSendMoney;
    private View loadingView;
    private View contentView;
    private View errorView;
    private Button btnRetry;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // Initialize views
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvBalance = view.findViewById(R.id.tv_balance);
        tvBalanceSubtitle = view.findViewById(R.id.tv_balance_subtitle);
        rvRecentTransactions = view.findViewById(R.id.rv_recent_transactions);
        cardExchangeRates = view.findViewById(R.id.card_exchange_rates);
        cardStudentDiscount = view.findViewById(R.id.card_student_discount);
        cardBusinessTools = view.findViewById(R.id.card_business_tools);
        fabSendMoney = view.findViewById(R.id.fab_send_money);
        loadingView = view.findViewById(R.id.loading_view);
        contentView = view.findViewById(R.id.content_view);
        errorView = view.findViewById(R.id.error_view);
        btnRetry = view.findViewById(R.id.btn_retry);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        
        // Setup RecyclerView
        transactionAdapter = new TransactionAdapter(getContext(), this);
        rvRecentTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecentTransactions.setAdapter(transactionAdapter);
        
        // Setup click listeners
        fabSendMoney.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        
        cardExchangeRates.setOnClickListener(v -> {
            // Navigate to exchange rates screen
        });
        
        cardStudentDiscount.setOnClickListener(v -> {
            // Navigate to student discounts screen
        });
        
        cardBusinessTools.setOnClickListener(v -> {
            // Navigate to business tools screen
        });
        
        btnRetry.setOnClickListener(v -> {
            loadData();
        });
        
        // Observe data
        observeData();
        
        // Load data
        loadData();
    }
    
    private void observeData() {
        viewModel.getUser().observe(getViewLifecycleOwner(), this::updateUserUI);
        viewModel.getRecentTransactions().observe(getViewLifecycleOwner(), this::updateTransactionsUI);
        viewModel.getLoadingState().observe(getViewLifecycleOwner(), this::updateLoadingState);
    }
    
    private void loadData() {
        viewModel.loadUserData();
        viewModel.loadRecentTransactions();
    }
    
    private void updateUserUI(User user) {
        if (user != null) {
            tvUserName.setText(getString(R.string.welcome_user, user.getName()));
            tvBalance.setText(getString(R.string.balance_format, user.getBalance()));
            
            // Show/hide features based on user type
            if ("Student".equalsIgnoreCase(user.getUserType())) {
                cardStudentDiscount.setVisibility(View.VISIBLE);
                cardBusinessTools.setVisibility(View.GONE);
                tvBalanceSubtitle.setText(R.string.student_balance_subtitle);
            } else if ("Exporter".equalsIgnoreCase(user.getUserType()) || 
                       "Importer".equalsIgnoreCase(user.getUserType())) {
                cardStudentDiscount.setVisibility(View.GONE);
                cardBusinessTools.setVisibility(View.VISIBLE);
                tvBalanceSubtitle.setText(R.string.business_balance_subtitle);
            } else {
                cardStudentDiscount.setVisibility(View.GONE);
                cardBusinessTools.setVisibility(View.GONE);
                tvBalanceSubtitle.setText(R.string.personal_balance_subtitle);
            }
        }
    }
    
    private void updateTransactionsUI(List<Transaction> transactions) {
        if (transactions != null && !transactions.isEmpty()) {
            transactionAdapter.setTransactions(transactions);
            transactionAdapter.notifyDataSetChanged();
        }
    }
    
    private void updateLoadingState(HomeViewModel.LoadingState state) {
        switch (state) {
            case LOADING:
                loadingView.setVisibility(View.VISIBLE);
                contentView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                break;
            case SUCCESS:
                loadingView.setVisibility(View.GONE);
                contentView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                break;
            case ERROR:
                loadingView.setVisibility(View.GONE);
                contentView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onTransactionClick(Transaction transaction) {
        Intent intent = new Intent(getActivity(), TransactionDetailActivity.class);
        intent.putExtra(TransactionDetailActivity.EXTRA_TRANSACTION_ID, transaction.getId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}