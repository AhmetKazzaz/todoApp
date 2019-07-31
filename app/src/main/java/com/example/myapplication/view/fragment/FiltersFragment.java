package com.example.myapplication.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentFiltersBinding;
import com.example.myapplication.model.repository.TodoItemSearchCriteria;
import com.example.myapplication.view.OptionsSelectorView;
import com.example.myapplication.viewmodel.TodoItemViewModel;

import static com.example.myapplication.model.repository.TodoItemSearchCriteria.CompletionState.Complete;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.CompletionState.Incomplete;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.CompletionState.NotSpecified;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.ExpiryState.Expired;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.ExpiryState.NotExpired;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.OrderBy.CreateDate;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.OrderBy.Deadline;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.OrderBy.Name;
import static com.example.myapplication.model.repository.TodoItemSearchCriteria.OrderBy.Status;

/**
 * Filters fragment is responsible for filtering todoItems and ordering them
 * Uses binding with a custom view
 */
public class FiltersFragment extends Fragment {

    private TodoItemViewModel viewModel;
    private FragmentFiltersBinding binding;

    public FiltersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //not working yet
    // weird :v
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_filters, container, false);
        View view = binding.getRoot();
        binding.setHandler(new EventHandler());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(TodoItemViewModel.class);

        binding.orderSelector.setSelectedIndex(viewModel.getOrderBy());
        binding.statusSelector.setSelectedIndex(viewModel.getStatus());
        binding.expirySelector.setSelectedIndex(viewModel.getExpiryState());
    }

    public class EventHandler {

        public void onChanged(OptionsSelectorView selectorView, int selectedOptionIndex) {
            switch (selectorView.getId()) {
                case R.id.orderSelector:
                    viewModel.setOrderBy(selectedOptionIndex);
                    break;

                case R.id.statusSelector:
                    viewModel.setStatus(selectedOptionIndex);
                    break;

                case R.id.expirySelector:
                    viewModel.setExpiryState(selectedOptionIndex);
                    break;
            }
        }
    }
}
