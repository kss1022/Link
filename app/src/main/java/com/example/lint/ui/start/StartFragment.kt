package com.example.lint.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lint.R
import com.example.lint.databinding.FragmentStartBinding

class StartFragment : Fragment(){

    private var _binding  : FragmentStartBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    private fun bindViews() {
        binding.titleTextView.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_loginFragment)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}