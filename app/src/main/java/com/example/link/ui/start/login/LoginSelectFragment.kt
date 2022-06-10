package com.example.link.ui.start.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.link.R
import com.example.link.databinding.FragmentLoginSelectBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainActivity
import com.example.link.ui.start.StartSharedViewModel
import com.example.link.util.lifecycle.SingleLiveEvent
import com.example.link.util.lifecycle.SystemUIType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginSelectFragment : BaseFragment<FragmentLoginSelectBinding, LoginSelectViewModel>() {

    @Inject
    lateinit var sharedViewModel: StartSharedViewModel

    @Inject
    lateinit var systemUIEvent: SingleLiveEvent<SystemUIType>


    override val viewModel: LoginSelectViewModel by viewModels()

    override fun getViewBinding(): FragmentLoginSelectBinding =
        FragmentLoginSelectBinding.inflate(layoutInflater)

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy { GoogleSignIn.getClient(requireContext(), gso) }


    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account ->
                        Log.e("Login", "firebaseAuthWithGoogle: ${account.id} , ${account.idToken}")
                        viewModel.setUserIdAndToken(account.id, account.idToken)
                    } ?: throw Exception()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


    override fun initViews() {
        systemUIEvent.value = SystemUIType.NORMAL
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
    }


    override fun bindViews() {
        binding.googleLoginButton.setOnClickListener {
            viewModel.clickGoogleLogin()
            sharedViewModel.onNavChange()
        }

        binding.facebookLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavChange()
        }

        binding.twitterLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavChange()
        }

        binding.appleLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSelectFragment_to_loginIdFragment)
            sharedViewModel.onNavChange()
        }
    }


    override fun observeData() {
        viewModel.googleLoginClickEvent.observe(viewLifecycleOwner) {
            signInGoogle()
        }


        viewModel.loginSuccess.observe(viewLifecycleOwner) { result ->
            result?.let {
                if (it) viewModel.saveUserData() else Toast.makeText(
                    requireContext(),
                    getString(R.string.login_fail),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.saveUserData.observe(viewLifecycleOwner) { isExist ->
            if (isExist!!) {
                startActivity(MainActivity.newIntent(requireContext()))
                requireActivity().finish()
            } else {
                findNavController().navigate(R.id.action_loginSelectFragment_to_loginPatFragment)
            }

        }
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }
}