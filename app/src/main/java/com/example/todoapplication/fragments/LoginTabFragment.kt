package com.example.todoapplication.fragments

import android.content.Context
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
import com.example.todoapplication.database.UserDataBaseHandler

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

            if (validateCredentials(email, password) != null) {
                // Credentials are valid, proceed with login
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, TodoActivity::class.java)

                val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return@setOnClickListener
                with (sharedPref.edit()) {
                    putInt("userId", validateCredentials(email, password)!!)
                    apply()
                }

                startActivity(intent)
            } else {
                // Credentials are invalid, show an error message
                Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun validateCredentials(email: String, password: String): Int? {
        for (user in UserDataBaseHandler(requireContext()).readUserData()) {
            if (user.email == email && user.password == password) {
                return user.id
            }
        }
        return null
    }
}