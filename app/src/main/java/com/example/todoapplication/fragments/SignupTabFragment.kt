package com.example.todoapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.todoapplication.R

class SignupTabFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signupButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup_tab, container, false)

        emailEditText = view.findViewById(R.id.signup_email)
        passwordEditText = view.findViewById(R.id.signup_password)
        confirmPasswordEditText = view.findViewById(R.id.signup_confirm)
        signupButton = view.findViewById(R.id.signup_button)

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (validateCredentials(email, password, confirmPassword)) {
                // Credentials are valid, proceed with signup
                Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show()
                // TODO: Save the credentials securely (e.g., in your database)
            } else {
                // Credentials are invalid, show an error message
                Toast.makeText(context, "Invalid email or passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun validateCredentials(email: String, password: String, confirmPassword: String): Boolean {
        // TODO: Implement your own validation logic here
        // For now, we'll just check that the fields are not empty and passwords match
        return email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword
    }
}