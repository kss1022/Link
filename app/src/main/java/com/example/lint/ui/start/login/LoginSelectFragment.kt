package com.example.lint.ui.start.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lint.databinding.FragmentLoginSelectBinding
import com.example.lint.ui.start.StartSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginSelectFragment : Fragment() {

    @Inject
    lateinit var sharedViewModel : StartSharedViewModel

    private var _binding  : FragmentLoginSelectBinding? = null
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

    private fun bindViews() = with(binding){
        googleLoginButton.setOnClickListener {  }
        facebookLoginButton.setOnClickListener {  }
        twitterLoginButton.setOnClickListener {  }
        appleLoginButton.setOnClickListener {  }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}