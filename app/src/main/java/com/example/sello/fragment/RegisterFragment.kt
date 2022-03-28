package com.example.sello.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sello.R
import com.example.sello.databinding.FragmentRegisterBinding
import com.example.sello.entity.Person
import com.example.sello.validate.Validate
import com.example.sello.viewmodel.PersonViewModel

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val personViewModel: PersonViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction() {

        binding.btnRegister.setOnClickListener {
            addPersonOnclick()
        }
    }

    private fun addPersonOnclick() {

        val valid = Validate()
        val name = binding.etFullname.text.toString()
        val email = binding.etEmailRegister.text.toString()
        val phone = binding.etPhoneRegister.text.toString()
        val address = binding.etAddressRegister.text.toString()
        val pass = binding.etPasswordRegister.text.toString()
        val repass = binding.etConfirmPassword.text.toString()
        val gender = if (binding.rbMale.isChecked) "1" else "0"

        fun checkExist() : Boolean{
            val listPersonsRegistered = personViewModel.getPerson()
            for (item in listPersonsRegistered){
                if(phone == item.phone){
                    return true
                }
            }
            return  false
        }

        if (!valid.checkPass(pass)) {
            binding.etPasswordRegister.text = null
            Toast.makeText(context, "Invalid Password!", Toast.LENGTH_SHORT).show()

        } else if (pass != repass) {
            binding.etPasswordRegister.text = null
            binding.etConfirmPassword.text = null
            Toast.makeText(context, "RePassword is not same Password!", Toast.LENGTH_SHORT)
                .show()
        } else if (!valid.isValidEmail(email)) {
            Toast.makeText(context, "Invalid Email!", Toast.LENGTH_SHORT).show()

        } else if (!valid.validCellPhone(phone)) {
            Toast.makeText(context, "Invalid Phone!", Toast.LENGTH_SHORT).show()

        }
        else if(checkExist()) {
            Toast.makeText(context, "Phone have been registered!", Toast.LENGTH_SHORT).show()
            return
        } else {
            val person = Person(phone, name, email, phone, pass, address, gender, 0)
            personViewModel.addFireStore(person)
            personViewModel.insertPerson(person)
            Toast.makeText(activity, "Add account successful", Toast.LENGTH_SHORT).show()
            val action = RegisterFragmentDirections.actionRegisterFragment2ToLoginFragment()
            findNavController().navigate(action)
        }
    }
}
