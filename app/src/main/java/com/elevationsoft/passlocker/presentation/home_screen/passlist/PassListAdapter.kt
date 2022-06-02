package com.elevationsoft.passlocker.presentation.home_screen.passlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.elevationsoft.passlocker.databinding.LayoutPasslistItemBinding
import com.elevationsoft.passlocker.domain.models.Credential

class PassListAdapter(
    private val callback: PassListItemCallBacks
) : PagingDataAdapter<Credential, PassListAdapter.ViewMaker>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewMaker {
        val binding =
            LayoutPasslistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewMaker(binding)
    }

    override fun onBindViewHolder(holder: ViewMaker, position: Int) {
        holder.bind()
    }

    inner class ViewMaker(private val binding: LayoutPasslistItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }

        fun bind() {

        }
    }


    class Diff : DiffUtil.ItemCallback<Credential>() {
        override fun areItemsTheSame(oldItem: Credential, newItem: Credential): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Credential, newItem: Credential): Boolean {
            return oldItem == newItem
        }
    }

    interface PassListItemCallBacks {
        fun onItemClicked(credential: Credential)

        fun onDeleteClicked(credential: Credential)

        fun onFavouriteClicked(credential: Credential)
    }

}