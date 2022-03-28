package com.example.sello.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sello.databinding.FragmentPersonDetailBinding
import com.example.sello.entity.Person
import com.example.sello.viewmodel.PersonViewModel


class PersonDetailFragment : Fragment() {

    private val personViewModel: PersonViewModel by viewModels()


    private var _binding: FragmentPersonDetailBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        val person = arguments?.getParcelable<Person>("person")
        binding.tvFullname.setText(person?.name)
        binding.tvEmail.setText(person?.email)
        binding.tvAddress.setText(person?.address)
        if (person?.gender == "1") {
            binding.rbMale.isChecked = true
            binding.rbFemale.isChecked = false
        } else {
            binding.rbMale.isChecked = false
            binding.rbFemale.isChecked = true
        }
    }

    private fun initAction() {
        val person = arguments?.getParcelable<Person>("person")

        //logout
        binding.btnLogOut.setOnClickListener {
            val action = PersonDetailFragmentDirections.actionPersonDetailFragmentToHomeFragment(
                check = true,
                person = person!!
            )
            findNavController().navigate(action)
        }

        //update
        binding.btnUpdate.setOnClickListener {
            val pass = person?.password
            val phone = person?.phone
            val gender = if (binding.rbMale.isChecked) "1" else "0"
            val user = Person(
                phone.toString(),
                binding.tvFullname.text.toString(),
                binding.tvEmail.text.toString(),
                phone.toString(),
                pass!!,
                binding.tvAddress.text.toString(),
                gender,
                0
            )
            personViewModel.updatePersonToFireStore(user)
            personViewModel.updatePerson(user)

            val action = PersonDetailFragmentDirections.actionPersonDetailFragmentToHomeFragment(
                check = false,
                person = user
            )
            findNavController().navigate(action)
            Toast.makeText(context, "Update Information Successfully!", Toast.LENGTH_SHORT).show()
        }

        //delete
        binding.btnDelete.setOnClickListener {
            if (person != null) {
                personViewModel.deletePersonFromFireStore(person)
                personViewModel.deletePerson(person.phone)
                val action =
                    PersonDetailFragmentDirections.actionPersonDetailFragmentToHomeFragment(
                        check = true,
                        person = person
                    )
                findNavController().navigate(action)
            }
            Toast.makeText(context, "Update Information Successfully!", Toast.LENGTH_SHORT).show()
        }
    }


}