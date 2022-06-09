package com.example.link.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.link.R
import com.example.link.databinding.FragmentLoginSelectBinding
import com.example.link.ui.start.StartSharedViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.link.util.lifecycle.SystemUIType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginSelectFragment : Fragment() {

    @Inject
    lateinit var sharedViewModel: StartSharedViewModel

    @Inject
    lateinit var systemUIEvent: SingleLiveEvent<SystemUIType>

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
        systemUIEvent.value = SystemUIType.NORMAL
        bindViews()
    }

    private fun bindViews() = with(binding) {
        googleLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavChange()
        }
        facebookLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavChange()
        }
        twitterLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavChange()
        }
        appleLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavChange()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}