package com.example.myapplication.view.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentFormTodoItemBinding;
import com.example.myapplication.model.entities.TodoItem;
import com.example.myapplication.viewmodel.TodoItemViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This fragment is responsible for creating and updating a todoItem.
 */
public class TodoItemFormFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private TodoItem todoItem = new TodoItem();
    private FragmentFormTodoItemBinding binding;
    private TodoItemViewModel viewModel;
    private AwesomeValidation awesomeValidation;

    public TodoItemFormFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_form_todo_item, container, false);
        View view = binding.getRoot();
        binding.setClickHandler(new TodoItemFormEventHandler());
        binding.setTodoItem(todoItem);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        awesomeValidation = new AwesomeValidation(ValidationStyle.UNDERLABEL);
        awesomeValidation.setContext(getContext());
        awesomeValidation.addValidation(binding.etTitle, RegexTemplate.NOT_EMPTY, getString(R.string.EMPTY_FIELD));
        awesomeValidation.addValidation(binding.etDescription, RegexTemplate.NOT_EMPTY, getString(R.string.EMPTY_FIELD));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(TodoItemFormFragmentArgs.fromBundle(getArguments()).getTodoItemName() != null){
            getActivity().setTitle(TodoItemFormFragmentArgs.fromBundle(getArguments()).getTodoItemName());
        }
        viewModel = ViewModelProviders.of(this).get(TodoItemViewModel.class);
        viewModel.findById(TodoItemFormFragmentArgs.fromBundle(getArguments()).getTodoItemId());
        viewModel.onItemCreated().observe(getViewLifecycleOwner(), aLong ->
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack());
        viewModel.onTodoItemFound().observe(getViewLifecycleOwner(), todoItem -> {
            if (todoItem != null) {
                this.todoItem = todoItem;
                binding.setTodoItem(this.todoItem);
                if (todoItem.getDeadline() != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(todoItem.getDeadline());
                    int month = c.get(Calendar.MONTH) + 1;
                    String dateStr = c.get(Calendar.DAY_OF_MONTH) + "/" + month + "/" + c.get(Calendar.YEAR);
                    binding.tvDateString.setText(dateStr);
                }
            }
        });

        viewModel.onItemUpdated().observe(getViewLifecycleOwner(), integer -> {
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
        });
        viewModel.onItemCreated().observe(getViewLifecycleOwner(), aLong -> {
            if (getView() != null)
                Snackbar.make(getView(), R.string.NEW_ITEM_CREATED, BaseTransientBottomBar.LENGTH_SHORT);
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class TodoItemFormEventHandler {

        public void onAddBtnClicked(View view) {
            if (awesomeValidation.validate()) {
                if (TodoItemFormFragmentArgs.fromBundle(getArguments()).getTodoItemId() != -1) {
                    viewModel.update(todoItem, todoItem.isDeleted());
                } else {
                    todoItem.setTodoListId(TodoItemFormFragmentArgs.fromBundle(getArguments()).getTodoListId());
                    viewModel.create(todoItem);
                }
            }
        }

        public void onSelectDateClicked(View view) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        todoItem.setDeadline(new Date(
                                new GregorianCalendar(year, monthOfYear, dayOfMonth).getTimeInMillis())
                        );
                        monthOfYear += 1;
                        binding.tvDateString.setText(dayOfMonth + "/" + monthOfYear + "/" + year);

                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
            datePickerDialog.show();
        }

    }
}
