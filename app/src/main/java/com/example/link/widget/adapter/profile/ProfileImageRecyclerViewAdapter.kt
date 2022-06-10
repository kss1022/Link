package com.example.link.widget.adapter.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.link.databinding.ViewholderProfileImageBinding

class ProfileImageRecyclerViewAdapter(
    private val profiles : List<Int>,
    private val listener : ProfileImageClickListener
) : RecyclerView.Adapter<ProfileImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileImageViewHolder =
        ProfileImageViewHolder(
            ViewholderProfileImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ProfileImageViewHolder, position: Int) {
        holder.bindData( profiles[position] )
        holder.bindViews( profiles[position] , position, listener)
    }

    override fun getItemCount(): Int{
        return  profiles.size
    }
}