package com.example.link.ui.start.login

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.link.R
import com.example.link.databinding.FragmentLoginIdBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.start.StartSharedViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.link.util.lifecycle.SystemUIType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginIdFragment : BaseFragment<FragmentLoginIdBinding, LoginIdViewModel>() {

    override val viewModel: LoginIdViewModel by viewModels()

    @Inject
    lateinit var sharedViewModel: StartSharedViewModel



    override fun getViewBinding(): FragmentLoginIdBinding =
        FragmentLoginIdBinding.inflate(layoutInflater)

    override fun initViews() {
        setData()
    }


    override fun bindViews() {
        binding.nextButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.clickNext()
        }


        binding.idEditText.addTextChangedListener {
            viewModel.setId(binding.idEditText.text.toString())
        }

        binding.passwordEditText.addTextChangedListener {
            viewModel.setPassword(binding.passwordEditText.text.toString())
        }

        binding.passwordCheckEditText.addTextChangedListener {
            viewModel.setCheckPassword(binding.passwordCheckEditText.text.toString())
        }
    }


    override fun observeData() {
        viewModel.passwordSameEvent.observe(viewLifecycleOwner) { isSame ->
            isSame?.let {
                binding.passwordErrorTextView.isVisible = it.not()
            }
        }

        viewModel.nextClickEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_loginIdFragment_to_loginPatFragment)
        }

        viewModel.signInSuccessEvent.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.saveUserId()
            } ?: kotlin.run {
                Toast.makeText(requireContext(), getString(R.string.logIn_fail_check_your_email), Toast.LENGTH_SHORT).show()
                binding.progressBar.isGone = true
            }

        }

    }


    private fun setData() {
        viewModel.id.value?.let { binding.idEditText.setText(it) }
        viewModel.password.value?.let { binding.passwordEditText.setText(it) }
        viewModel.passwordCheck.value?.let { binding.passwordCheckEditText.setText(it) }
    }
}
