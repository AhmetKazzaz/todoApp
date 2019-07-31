package com.example.myapplication.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentLoginBinding;
import com.example.myapplication.model.entities.User;
import com.example.myapplication.viewmodel.LoginViewModel;

import static com.basgeekball.awesomevalidation.ValidationStyle.UNDERLABEL;

public class LoginFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private User user = new User();
    private LoginViewModel viewModel;
    private AwesomeValidation mAwesomeValidation;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFieldsValidation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentLoginBinding fragmentLoginBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_login, container, false);
        View view = fragmentLoginBinding.getRoot();
        fragmentLoginBinding.setClickHandler(new LoginFragmentEventHandler());
        fragmentLoginBinding.setUser(user);
        return view;
    }

    /**
     * Setting validation rules for field
     */
    private void initFieldsValidation() {
        mAwesomeValidation = new AwesomeValidation(UNDERLABEL);
        mAwesomeValidation.setContext(getContext());
        mAwesomeValidation.addValidation(getActivity(), R.id.etUserName, RegexTemplate.NOT_EMPTY, R.string.EMPTY_FIELD);
        mAwesomeValidation.addValidation(getActivity(), R.id.etPassword, RegexTemplate.NOT_EMPTY, R.string.EMPTY_FIELD);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        viewModel.onError().observe(this, error -> {
            String message = error.getString(getContext());
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

    /**
     * Event Handler class to bind events with views
     */
    public class LoginFragmentEventHandler {

        public void onSignInClicked(View view) {
            if (mAwesomeValidation.validate())
                viewModel.signIn(user);
        }

        public void onRegisterClicked(View view) {
            Navigation.findNavController(view).navigate(LoginFragmentDirections.loginToRegister());
        }

    }
}
