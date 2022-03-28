package com.example.sello.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sello.R
import com.example.sello.adapter.ProductAdapter
import com.example.sello.databinding.FragmentSearchBinding
import com.example.sello.entity.Person
import com.example.sello.entity.Product
import com.example.sello.viewmodel.ProductViewModel

class SearchFragment : Fragment(R.layout.fragment_search), ProductAdapter.OnClickImg {

    private val productViewModel: ProductViewModel by viewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        productViewModel.getProductFromFireStore()

        val adapter = ProductAdapter(this)

        binding.rvSearch.setHasFixedSize(true)
        binding.rvSearch.layoutManager = LinearLayoutManager(context)
        binding.rvSearch.adapter = adapter

        binding.tvSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.tvSearchView.clearFocus()
                if (!query.isNullOrEmpty()) {
                    productViewModel.findProductByName(query)
                        .observe(viewLifecycleOwner, Observer {
                            Log.d(TAG, "onQueryTextSubmit xxxx : ${it.size} va $query")
                            adapter.setProducts(it)

                        })
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    productViewModel.findProductByName(newText)
                        .observe(viewLifecycleOwner, Observer {
                            adapter.setProducts(it)
                        })
                }
                return false
            }


        })

    }

    override fun onClickImg(product: Product) {
        val person = arguments?.getParcelable<Person>("person")
        val check = arguments?.getBoolean("check")
        if (person != null && check != true) {
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(
                product = product,
                person = person
            )
            findNavController().navigate(action)
        } else {
            val action = SearchFragmentDirections.actionSearchFragmentToDetailFragmentNotLogin(
                product = product,
                check = true
            )
            findNavController().navigate(action)
        }
    }

}


