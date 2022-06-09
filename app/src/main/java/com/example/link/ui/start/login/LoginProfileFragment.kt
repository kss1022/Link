package com.example.link.ui.start.login

import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.link.R
import com.example.link.databinding.FragmentLoginProfileBinding
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.start.StartSharedViewModel
import com.example.link.util.ext.load
import com.example.widget.adapter.profile.ProfileImageClickListener
import com.example.widget.adapter.profile.ProfileImageRecyclerViewAdapter
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginProfileFragment : BaseFragment<FragmentLoginProfileBinding, LoginProfileViewModel>(){

    @Inject
    lateinit var sharedViewModel : StartSharedViewModel

    override val viewModel: LoginProfileViewModel by viewModels()

    override fun getViewBinding(): FragmentLoginProfileBinding =FragmentLoginProfileBinding.inflate(layoutInflater)

    private val args :LoginProfileFragmentArgs by navArgs()

    private val profileAdapter by lazy {
        ProfileImageRecyclerViewAdapter(
            ProfileCategory.values().toList().map { it.imageId },
            object  : ProfileImageClickListener{
                override fun clickProfile(id: Int, position: Int) {
                    viewModel.setProfile(id , position)
                }
            }
        )

    }

    override fun initViews() {
        binding.patNameTextView.text = args.patName
        initRecyclerView()
    }



    override fun bindViews() = with(binding){
        nextButton.setOnClickListener {
            viewModel.clickNext()
        }


        userNameEditButton.setOnClickListener { viewModel.changeUserName() }
        userNameTextView.setOnClickListener { viewModel.changeUserName() }
    }


    override fun observeData() {
        viewModel.nextClickEvent.observe(viewLifecycleOwner){ goNext->
            goNext?.let {
                if(it)  findNavController().navigate(R.id.action_loginProfileFragment_to_educationFirstFragment)
            }
        }

        viewModel.profile.observe(viewLifecycleOwner){
            binding.mainProfileImageView.load(it)
        }

        viewModel.userName.observe(viewLifecycleOwner){
            binding.userNameTextView.text = it
        }

        viewModel.changeUseName.observe(viewLifecycleOwner){
            showGetUserNameAlertDialog()
        }
    }

    private fun initRecyclerView() =with(binding){
        recycleView.apply {
            adapter = profileAdapter
            layoutManager = GridLayoutManager(context, 5)
        }
    }


    /**
     * AlertDialog
     */

    private fun showGetUserNameAlertDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val getUserNameLayout = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialog_get_user_name, null)

        val titleTextView = getUserNameLayout.findViewById<TextInputEditText>(R.id.userNameEditText)

        getUserNameLayout.findViewById<TextView>(R.id.negativeButton).setOnClickListener {
            dialog.dismiss()
        }

        getUserNameLayout.findViewById<TextView>(R.id.positiveButton).setOnClickListener {
            viewModel.setUserName(titleTextView.text.toString())
            dialog.dismiss()
        }

        dialog.apply {
            setView(getUserNameLayout)
        }.show()
    }
}
