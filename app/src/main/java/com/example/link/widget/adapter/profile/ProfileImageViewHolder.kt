package com.example.link.widget.adapter.profile

import androidx.recyclerview.widget.RecyclerView
import com.example.link.databinding.ViewholderProfileImageBinding
import com.example.link.util.ext.clear
import com.example.link.util.ext.load

class ProfileImageViewHolder(
    private val binding: ViewholderProfileImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {


    fun bindData(id: Int) {
        binding.profileImageView.clear()
        binding.profileImageView.load(id)
    }

    fun bindViews(id: Int, position: Int, listener: ProfileImageClickListener) {
        binding.root.setOnClickListener {
            listener.clickProfile(id, position)
        }
    }
}