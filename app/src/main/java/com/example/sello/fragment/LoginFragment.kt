package com.example.sello.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sello.databinding.FragmentLoginBinding
import com.example.sello.viewmodel.PersonViewModel


class LoginFragment : Fragment() {


    private val personViewModel: PersonViewModel by viewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initAction()
    }

    private fun initData() {
        personViewModel.getDataFromFireStore()
    }

    private fun initAction() {
        binding.btnLogin.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            val pass = binding.etPassword.text.toString()

            if (phone.isBlank() || pass.isBlank()) {
                Toast.makeText(context, "Phone or Password must not empty!", Toast.LENGTH_SHORT)
                    .show()
            } else if (!personViewModel.checkPhonePass(phone, pass)) {
                Toast.makeText(context, "Invalid User", Toast.LENGTH_SHORT).show()
            } else {
                val person = personViewModel.findPersonByPhone(phone)
                val action =
                    LoginFragmentDirections.actionLoginFragmentToHomeFragment(person = person)
                findNavController().navigate(action)
                Toast.makeText(context, "Login Successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment2()
            findNavController().navigate(action)
        }
    }
}