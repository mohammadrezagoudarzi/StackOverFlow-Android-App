package com.example.retrofit_test.View.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.retrofit_test.Model.Networking.ModelObject.Question;
import com.example.retrofit_test.R;
import com.example.retrofit_test.View.Adapter.RecStackQuestionAdapter;
import com.example.retrofit_test.ViewModel.HomeFragmentViewModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecStackQuestionAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private HomeFragmentViewModel viewModel;
    private ProgressBar progressBar;
    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        init();

        viewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);

        progressBar.setVisibility(View.VISIBLE);

        viewModel.getQuestions().observe(getViewLifecycleOwner(),questions -> {
            adapter.setQuestions((ArrayList<Question>) questions);
            progressBar.setVisibility(View.GONE);
        });

        refreshLayout.setOnRefreshListener(onRefreshListener);

        return view;
    }

    void init() {
        progressBar = view.findViewById(R.id.progress_main);
        refreshLayout = view.findViewById(R.id.swipe_refresh_main);
        RecyclerView recyclerView = view.findViewById(R.id.rec_main_questions);
        ArrayList<Question> questions = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecStackQuestionAdapter(questions);
        recyclerView.setAdapter(adapter);
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (viewModel == null)
            return;
        if (viewModel.fetchData() != null)
            adapter.setQuestions((ArrayList<Question>) viewModel.fetchData());
        refreshLayout.setRefreshing(false);

    };

}