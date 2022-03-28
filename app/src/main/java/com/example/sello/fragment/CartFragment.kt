package com.example.sello.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sello.R
import com.example.sello.adapter.CartAdapter
import com.example.sello.databinding.FragmentCartBinding
import com.example.sello.entity.Person
import com.example.sello.viewmodel.CartViewModel
import com.example.sello.viewmodel.ProductViewModel


class CartFragment : Fragment(R.layout.fragment_cart), CartAdapter.OnClickDelete,
    CartAdapter.OnClickPlus, CartAdapter.OnClickMinus,
    CartAdapter.OnClickCheckOut {
    var adapter: CartAdapter? = null

    var person: Person? = null
    var check: Boolean? = null

    private val cartViewModel: CartViewModel by viewModels()

    private val productViewModel: ProductViewModel by viewModels()

    private var _binding: FragmentCartBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        person = arguments?.getParcelable<Person>("person") as Person
        check = arguments?.getBoolean("check")
        initView()
        initAction()
    }

    private fun initView() {
        adapter = CartAdapter(Application(), this, this, this, this)

        binding.rvCart.layoutManager = LinearLayoutManager(context)
        binding.rvCart.adapter = adapter
        if (person != null) {
            cartViewModel.getAllCart(person!!.personId).observe(viewLifecycleOwner, Observer {
                adapter!!.setProducts(it)
            })
            var totalPrice = 0.toLong()
            val listCheckoutCart = cartViewModel.searchCartCheckOut(person!!.personId)
            for (item in listCheckoutCart) {
                val product = productViewModel.findProductByID(item.productID)
                totalPrice += (product.price.toDouble() * (1.0 - product.discount)).toLong() * item.quantity
                binding.tvCartTotal.text = totalPrice.toString()
            }
            if (listCheckoutCart.isNullOrEmpty()) {
                binding.tvCartTotal.text = "0"
            }
        }
    }

    override fun onClickDelete(personID: String, productID: String) {
        cartViewModel.deleteCart(personID, productID)
        var totalPrice = 0.toLong()

        val listCheckoutCart = cartViewModel.searchCartCheckOut(personID)

        for (item in listCheckoutCart) {
            val product = productViewModel.findProductByID(item.productID)
            totalPrice += (product.price.toDouble() * (1.0 - product.discount)).toLong() * item.quantity
            binding.tvCartTotal.text = totalPrice.toString()
        }
        if (listCheckoutCart.isNullOrEmpty()) {
            binding.tvCartTotal.text = "0"
        }
    }

    override fun onClickPlus(quantity: Int, personID: String, productID: String) {
        cartViewModel.updateCartNumber(quantity, personID, productID)
        updateTotalPrice(personID)
    }


    override fun onClickMinus(quantity: Int, personID: String, productID: String) {
        cartViewModel.updateCartNumber(quantity, personID, productID)
        updateTotalPrice(personID)

    }

    override fun onClickCheckOut(personID: String, productID: String) {
        cartViewModel.updateCartCheckout(personID, productID)
        updateTotalPrice(personID)

    }

    private fun initAction() {

        binding.tvCartPayment.setOnClickListener {
            val person = arguments?.getParcelable<Person>("person")
            if (person != null && binding.tvCartTotal.text.toString().toLong() != 0.toLong()) {
                val action =
                    CartFragmentDirections.actionCartFragmentToOrderFragment(
                        person = person
                    )
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, "Nothing to order", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvTitleCart.setOnClickListener{
            val action = CartFragmentDirections.actionCartFragmentToHomeFragment(person)
            findNavController().navigate(action)
        }
    }

    private fun updateTotalPrice(personID: String) {
        var totalPrice = 0.toLong()

        val listCheckoutCart = cartViewModel.searchCartCheckOut(personID)
        for (item in listCheckoutCart) {
            val product = productViewModel.findProductByID(item.productID)
            totalPrice += (product.price.toDouble() * (1.0 - product.discount)).toLong() * item.quantity
            binding.tvCartTotal.text = totalPrice.toString()
        }
        if (listCheckoutCart.isNullOrEmpty()) {
            binding.tvCartTotal.text = "0"
        }
    }
}


