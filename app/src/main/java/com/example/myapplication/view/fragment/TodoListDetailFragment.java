package com.example.myapplication.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.transition.TransitionInflater;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentTodoListDetailBinding;
import com.example.myapplication.model.entities.TodoItem;
import com.example.myapplication.ui.adapter.TodoItemsAdapter;
import com.example.myapplication.viewmodel.TodoItemViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TodoListDetailFragment extends Fragment implements TodoItemsAdapter.OnDeleteItemClickListener
        , TodoItemsAdapter.OnMarkItemCompletedClickListener, TodoItemsAdapter.OnItemClickListener {
    private OnFragmentInteractionListener mListener;
    private TodoItemViewModel viewModel;
    private FragmentTodoListDetailBinding binding;
    private TodoItem todoItem;

    public TodoListDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.todolist_items_menu, menu);

        if (getContext() != null) {
            SearchView searchView = ((SearchView) menu.findItem(R.id.search).getActionView());
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    viewModel.setSearchKeyword(newText);
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            if (getActivity() != null) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(TodoListDetailFragmentDirections.itemsListToFilters());
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_todo_list_detail, container, false);
        View view = binding.getRoot();
        binding.setClickHandler(new TodoListDetailEventHandler());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(TodoItemViewModel.class);

        if (getArguments() != null) {
            viewModel.setTodoListId(TodoListDetailFragmentArgs.fromBundle(getArguments()).getTodoListId());
        }

        getActivity().setTitle(TodoListDetailFragmentArgs.fromBundle(getArguments()).getTodoListName());

        TodoItemsAdapter adapter = new TodoItemsAdapter();

        binding.rvTodoItems.setLayoutManager(new GridLayoutManager(getContext(), 2));

        binding.rvTodoItems.setHasFixedSize(true);
        binding.rvTodoItems.setAdapter(adapter);
        viewModel.onListUpdated().observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(((ArrayList<TodoItem>) items));
            binding.setNoDataVisibility(
                    items.size() > 0 ? View.GONE : View.VISIBLE
            );
        });

        viewModel.onItemTempDeleted().observe(getViewLifecycleOwner(), isTempDeleted -> showUndoSnackBar());
        adapter.setDeleteItemClickListener(this);
        adapter.setMarkItemCompletedClickListener(this);
        adapter.setItemClickListener(this);

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

    private void showUndoSnackBar() {
        Snackbar snackbar = Snackbar.make(binding.coordinatorLayout, R.string.TODOLIST_DELETED,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.UNDO_DELETE, v -> viewModel.tempDelete(todoItem, false));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event != DISMISS_EVENT_ACTION) {
                    viewModel.delete(todoItem);
                }
            }
        });
        snackbar.show();
    }

    @Override
    public void onDeleteItemClicked(TodoItem todoItem) {
        this.todoItem = todoItem;
        viewModel.tempDelete(todoItem, true);
    }

    @Override
    public void onMarkItemCompletedClicked(TodoItem todoItem, boolean isComplete) {
        viewModel.updateStatus(todoItem, isComplete);
    }

    @Override
    public void onItemClicked(TodoItem todoItem) {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(
                TodoListDetailFragmentDirections.itemsListToItemForm(todoItem.getName()).setTodoItemId(todoItem.getId()));
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class TodoListDetailEventHandler {

        public void onAddFabClicked(View view) {
            if (getActivity() != null)
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(
                        TodoListDetailFragmentDirections.itemsListToItemForm(todoItem.getName()).setTodoListId(
                                TodoListDetailFragmentArgs.fromBundle(getArguments()).getTodoListId()));
        }

    }
}
