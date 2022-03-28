package com.example.sello.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.sello.R
import com.example.sello.adapter.ProductAdapter
import com.example.sello.adapter.SliderAdapter
import com.example.sello.databinding.FragmentHomeBinding
import com.example.sello.entity.Person
import com.example.sello.entity.Product
import com.example.sello.viewmodel.ProductViewModel

class HomeFragment : Fragment(R.layout.fragment_home), ProductAdapter.OnClickImg {


    private val productViewModel: ProductViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
        initView()
        sliderItems()
    }

    private fun sliderItems() {
        val sliderAdapter = SliderAdapter(this)
        binding.vpHotProduct.adapter = sliderAdapter
        productViewModel.getAllProduct().observe(viewLifecycleOwner, Observer {
            sliderAdapter.setItemSlider(it)
        })

    }

    private fun initView() {
        productViewModel.getProductFromFireStore()
        val adapter = ProductAdapter(this)
        binding.rvProduct.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(activity?.applicationContext, 2, LinearLayoutManager.VERTICAL, false)

        binding.rvProduct.layoutManager = gridLayoutManager

        binding.rvProduct.adapter = adapter
        productViewModel.getAllProduct().observe(viewLifecycleOwner, Observer {
            adapter.setProducts(it)
        })
    }

    override fun onClickImg(product: Product) {
        val person = arguments?.getParcelable<Person>("person")
        val check = arguments?.getBoolean("check")
        if (person != null && check != true) {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                product = product,
                person = person
            )
            findNavController().navigate(action)
        } else {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragmentNotLogin(
                product = product,
                check = true
            )
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initAction() {
        val person = arguments?.getParcelable<Person>("person")
        val check = arguments?.getBoolean("check")

        val bottomNavigationView =
            view?.findViewById<BottomNavigationView>(R.id.bottomNavigationViewHome)

        bottomNavigationView?.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.personDetailFragment -> {
                    if (person == null || check == true) {
                        val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                        findNavController().navigate(action)
                    } else {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToPersonDetailFragment(person = person)
                        findNavController().navigate(action)
                    }
                    return@setOnItemSelectedListener true

                }
                R.id.cartFragment -> {
                    if (check != true && person != null) {
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToCartFragment(person = person)
                        findNavController().navigate(action)

                    } else {
                        Toast.makeText(context, "You need to Login first!", Toast.LENGTH_SHORT)
                            .show()
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.homeFragment -> {
                    return@setOnItemSelectedListener true
                }
                R.id.search -> {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                    findNavController().navigate(action)

                    return@setOnItemSelectedListener true
                }
                else -> {
                    return@setOnItemSelectedListener false
                }
            }
        }


        binding.tvMale.setOnClickListener {
            val adapter = ProductAdapter(this)
            binding.rvProduct.setHasFixedSize(true)

            val gridLayoutManager = GridLayoutManager(activity?.applicationContext, 2, LinearLayoutManager.VERTICAL, false)
            binding.rvProduct.layoutManager = gridLayoutManager

            binding.rvProduct.adapter = adapter
            productViewModel.getProductsByType("Quần Áo Nam").observe(viewLifecycleOwner, Observer {
                adapter.setProducts(it)
            })
        }

        binding.tvAll.setOnClickListener {
            val adapter = ProductAdapter(this)
            binding.rvProduct.setHasFixedSize(true)

            val gridLayoutManager = GridLayoutManager(activity?.applicationContext, 2, LinearLayoutManager.VERTICAL, false)
            binding.rvProduct.layoutManager = gridLayoutManager

            binding.rvProduct.adapter = adapter
            productViewModel.getAllProduct().observe(viewLifecycleOwner, Observer {
                adapter.setProducts(it)
            })
        }

        binding.tvFemale.setOnClickListener {
            val adapter = ProductAdapter(this)
            binding.rvProduct.setHasFixedSize(true)

            val gridLayoutManager = GridLayoutManager(activity?.applicationContext, 2, LinearLayoutManager.VERTICAL, false)
            binding.rvProduct.layoutManager = gridLayoutManager

            binding.rvProduct.adapter = adapter
            productViewModel.getProductsByType("Quần Áo Nữ").observe(viewLifecycleOwner, Observer {
                adapter.setProducts(it)
            })
        }

        binding.tvShoe.setOnClickListener {
            val adapter = ProductAdapter(this)
            binding.rvProduct.setHasFixedSize(true)

            val gridLayoutManager = GridLayoutManager(activity?.applicationContext, 2, LinearLayoutManager.VERTICAL, false)
            binding.rvProduct.layoutManager = gridLayoutManager

            binding.rvProduct.adapter = adapter
            productViewModel.getProductsByType("Giày Dép").observe(viewLifecycleOwner, Observer {
                adapter.setProducts(it)
            })
        }
    }

}


