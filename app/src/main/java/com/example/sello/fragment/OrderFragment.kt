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
import com.example.sello.adapter.OrderAdapter
import com.example.sello.databinding.FragmentOrderBinding
import com.example.sello.entity.*
import com.example.sello.viewmodel.CartViewModel
import com.example.sello.viewmodel.OrderViewModel
import com.example.sello.viewmodel.ProductViewModel
import java.text.SimpleDateFormat
import java.util.*

class OrderFragment : Fragment(R.layout.fragment_order) {

    private var _binding: FragmentOrderBinding? = null
    private val binding
        get() = _binding!!

    private val orderViewModel: OrderViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()

    var person : Person? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
    }

    private fun initView() {
        val person = arguments?.getParcelable<Person>("person")
        if (person != null) {
            val adapter = OrderAdapter(Application())
            binding.rvOrder.layoutManager = LinearLayoutManager(context)
            binding.rvOrder.adapter = adapter
            cartViewModel.searchListCartCheckout(person.personId)
                .observe(viewLifecycleOwner, Observer {
                    adapter.setOrders(it)
                })
            binding.edtOrderName.setText(person.name)
            binding.edtOrderAddress.setText(person.address)
            binding.edtOrderPhone.setText(person.phone)
            var totalPrice = 0.toLong()

            val listCheckoutCart = cartViewModel.searchCartCheckOut(person.personId)
            for (item in listCheckoutCart) {
                val product = productViewModel.findProductByID(item.productID)
                totalPrice += (product.price.toDouble() * (1.0 - product.discount)).toLong() * item.quantity
                binding.tvOrderTotal.text = totalPrice.toString()
            }
        }
    }

    private fun initAction() {
        //mua hang han
        binding.tvOrderPayment.setOnClickListener {
            person = arguments?.getParcelable<Person>("person")

            if(!_binding!!.rbDirectly.isChecked && !_binding!!.rbAtm.isChecked) {
                Toast.makeText(context, "Please choose payment method", Toast.LENGTH_SHORT).show()
            } else if (person != null && binding.tvOrderTotal.text.toString().toLong() != 0.toLong()) {
                val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val date = Date()
                var timeOrder = person!!.phone + " | " + sdf.format(date)


                val order = Order(person!!.personId, timeOrder, OrderStatus.Pending)
                orderViewModel.insertOrder(order)

                val listCheckoutCart = cartViewModel.searchCartCheckOut(person!!.personId)

                for (item in listCheckoutCart) {
                    val product = productViewModel.findProductByID(item.productID)

                    val personID = item.personID
                    val productID = item.productID
                    val quantity = item.quantity
                    val totalPrice = product.price * quantity
                    val newStock = product.stock - quantity
                    val orderItem =
                        OrderItem(0, timeOrder, productID, quantity, totalPrice, product.discount)

                    orderViewModel.insertOrderItem(orderItem)
                    orderViewModel.addOrderItemToFireStore(orderItem, personID)

                    cartViewModel.checkoutCart(personID)

                    productViewModel.updateStock(newStock, item.productID)
                    productViewModel.updateProductFireStore(product, newStock)

                }
                binding.tvOrderTotal.text = "0"
                Toast.makeText(context, "Order success", Toast.LENGTH_SHORT).show()
                val action = OrderFragmentDirections.actionOrderFragmentToHomeFragment(person)
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, "Nothing to order", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvTitleOrder.setOnClickListener{
            val action = OrderFragmentDirections.actionOrderFragmentToHomeFragment(person = person)
            findNavController().navigate(action)
        }
    }

}