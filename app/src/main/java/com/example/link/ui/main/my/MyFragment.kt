package com.example.link.ui.main.my

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.link.R
import com.example.link.databinding.FragmentMyBinding
import com.example.link.model.PetModel
import com.example.link.model.User
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.MainToolbarViewModel
import com.example.link.util.ext.toReadableDateString
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MyFragment : BaseFragment<FragmentMyBinding, MyViewModel>() {

    @Inject
    lateinit var sharedViewModel : MainSharedViewModel

    @Inject
    lateinit var toolbarViewModel: MainToolbarViewModel

    override val viewModel: MyViewModel by viewModels()

    override fun getViewBinding(): FragmentMyBinding =
        FragmentMyBinding.inflate(layoutInflater)

    override fun initViews() {
        initActionBar()
    }

    override fun bindViews() {
    }

    override fun observeData() {
        viewModel.profileImage.observe(viewLifecycleOwner){
            Glide.with(binding.profileImageView)
                .load(it)
                .circleCrop()
                .into(binding.profileImageView)
        }

        sharedViewModel.userModel.observe(viewLifecycleOwner){
            setUserData(it)
        }

        sharedViewModel.petModel.observe(viewLifecycleOwner) {
            setPetData(it)
        }
    }




    fun initActionBar() {
        toolbarViewModel.setTitle(getString(R.string.app_name))
            .setNavIconRes(null)
            .onChange()

        toolbarViewModel.onChangeBottomNavigation(true)
    }


    /**
     * Set Data
     */

    private fun setUserData(it: User) {
        binding.userNameTextView.text = it.name
        binding.userEmailTextView.text = it.email
    }


    @SuppressLint("SetTextI18n")
    private fun setPetData(pet: PetModel) = with(binding) {
        petNameTextView.text = pet.name
        petYearTextView.text = getYear(pet.birthDay)
        petTypeTextView.text = pet.type
        petMailTextView.text =
            if (pet.isMail) getString(R.string.character_mail) else getString(R.string.character_femail)
        petWeightTextView.text = "${pet.weight}kg"
    }


    private fun getYear(date: List<Int>): String {
        val today = Date(System.currentTimeMillis()).toReadableDateString().split(".")

        var year = today[0].toInt() - date[0]
        var month = today[1].toInt() - date[1]
        val day = today[2].toInt() - date[2]


        if (day < 0) {
            month--
        }
        if (month < 0) {
            year--
            month += 12
        }

        return getString(R.string.pet_age, year, month)
    }


    companion object{
        fun newInstance()  = MyFragment()
        const val TAG = "MyFragment"
    }
}