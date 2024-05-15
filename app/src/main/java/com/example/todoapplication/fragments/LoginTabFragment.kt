package com.example.todoapplication.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.todoapplication.R
import com.example.todoapplication.activity.TodoActivity

class LoginTabFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_tab, container, false)

        emailEditText = view.findViewById(R.id.login_email)
        passwordEditText = view.findViewById(R.id.login_password)
        loginButton = view.findViewById(R.id.login_button)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateCredentials(email, password)) {
                // Credentials are valid, proceed with login
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, TodoActivity::class.java)
                startActivity(intent)
            } else {
                // Credentials are invalid, show an error message
                Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        // TODO: Implement your own validation logic here
        // For now, we'll just check that the fields are not empty
        return email.isNotEmpty() && password.isNotEmpty()
    }
}