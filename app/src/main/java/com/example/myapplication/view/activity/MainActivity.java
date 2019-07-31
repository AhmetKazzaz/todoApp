package com.example.myapplication.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.R;
import com.example.myapplication.view.fragment.LoginFragment;
import com.example.myapplication.view.fragment.RegisterFragment;
import com.example.myapplication.view.fragment.SplashFragment;
import com.example.myapplication.view.fragment.TodoItemFormFragment;
import com.example.myapplication.view.fragment.TodoListDetailFragment;
import com.example.myapplication.view.fragment.TodoListsFragment;
import com.example.myapplication.viewmodel.HomeViewModel;

/**
 * MainActivity that is the base of navigation
 */
public class MainActivity extends AppCompatActivity implements
        SplashFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener, TodoListsFragment.OnFragmentInteractionListener,
        TodoListDetailFragment.OnFragmentInteractionListener, TodoItemFormFragment.OnFragmentInteractionListener,
        NavController.OnDestinationChangedListener {

    private Toolbar toolbar;
    private HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupNavigation();

        viewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        viewModel.onAuthStatusChanged().observe(this, user -> {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

            int fragment = user == null ? R.id.loginFragment : R.id.todoListsFragment;
            navController.navigate(fragment, null,
                    new NavOptions.Builder()
                            .setLaunchSingleTop(true)
                            .setPopUpTo(R.id.auth_navigation_graph, true)
                            .build()
            );
        });
    }

    /**
     * Setting up navigation with toolbar and top level fragments
     */
    private void setupNavigation() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.splashFragment, R.id.loginFragment, R.id.todoListsFragment
        ).build();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navController.addOnDestinationChangedListener(this);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        //Hides toolbar when login,register,splash fragments are displayed.
        if (destination.getId() == R.id.splashFragment || destination.getId() == R.id.loginFragment
                || destination.getId() == R.id.registerFragment) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }
    }
}
