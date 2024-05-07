package com.example.intesavincente.repository.user;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.example.intesavincente.model.AuthenticationResponse;

public interface IUserRepository {
        MutableLiveData<AuthenticationResponse> signInWithEmail(String email, String password);
        MutableLiveData<AuthenticationResponse> createUserWithGoogle(Intent data);
        MutableLiveData<AuthenticationResponse> createUserWithEmail(String email, String password);
}
