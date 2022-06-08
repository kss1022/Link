package com.example.link.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.link.databinding.FragmentLoginProfileBinding
import com.example.link.ui.start.StartSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginProfileFragment : Fragment(){

    @Inject
    lateinit var sharedViewModel : StartSharedViewModel


    private var _binding  : FragmentLoginProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViews()
    }

    private fun initViews() {

    }

    private fun bindViews() = with(binding){
        nextButton.setOnClickListener {
            startActivity(MainActivity.newIntent(requireContext()))
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
