package com.example.link.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.link.R
import com.example.link.databinding.FragmentLoginIdBinding
import com.example.link.ui.start.StartSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginIdFragment : Fragment() {

    @Inject
    lateinit var sharedViewModel: StartSharedViewModel

    private var _binding: FragmentLoginIdBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginIdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        bindViews()
    }


    private fun initViews() {
    }

    private fun bindViews() = with(binding) {
        nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginIdFragment_to_loginPatFragment)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
