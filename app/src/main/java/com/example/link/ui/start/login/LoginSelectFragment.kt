package com.example.link.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.link.R
import com.example.link.databinding.FragmentLoginSelectBinding
import com.example.link.ui.start.StartSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginSelectFragment : Fragment() {

    @Inject
    lateinit var sharedViewModel: StartSharedViewModel


    private var _binding: FragmentLoginSelectBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginSelectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
    }

    //todo  로그인을 제대로 구현해야되는지 다시 물어보기
    private fun bindViews() = with(binding) {
        googleLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavClick()
        }
        facebookLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavClick()
        }
        twitterLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavClick()
        }
        appleLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavClick()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}