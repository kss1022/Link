package com.example.link.ui.start.login.education

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.link.databinding.FragmentEducationThirdBinding
import com.example.link.ui.main.MainActivity
import com.example.link.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EducationThirdFragment : Fragment() {

    private var _binding: FragmentEducationThirdBinding? = null
    private val binding get() = _binding!!

    private val nextClick = SingleLiveEvent<Unit>(100)
    private val skippClick = SingleLiveEvent<Unit>(100)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEducationThirdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        observeData()
    }


    private fun bindViews() = with(binding) {
        nextButton.setOnClickListener {
            nextClick.call()
        }

        skippTextView.setOnClickListener {
            skippClick.call()
        }
    }

    private fun observeData() {
        nextClick.observe(viewLifecycleOwner){
            startActivity(MainActivity.newIntent(requireContext()))
            requireActivity().finish()
        }

        skippClick.observe(viewLifecycleOwner){
            startActivity(MainActivity.newIntent(requireContext()))
            requireActivity().finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}