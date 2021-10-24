package com.lsoehadak.galleryapp.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lsoehadak.galleryapp.R
import com.lsoehadak.galleryapp.data.ImageEntity
import com.lsoehadak.galleryapp.databinding.ItemImageBinding

class ImageAdapter(
    private val items: ArrayList<ImageEntity?>,
    private val onItemSelectedListener: ((ImageEntity) -> Unit)? = null
) : RecyclerView.Adapter<ImageAdapter.CustomViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_PROGRESS = 1

    open inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ProgressViewHolder(itemView: View) : CustomViewHolder(itemView)

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        CustomViewHolder(binding.root) {
        fun bind(image: ImageEntity) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(image.url)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    ).into(ivImage)
                tvTitle.text = image.title
            }

            itemView.setOnClickListener {
                onItemSelectedListener?.invoke(image)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] != null) {
            VIEW_TYPE_ITEM
        } else {
            VIEW_TYPE_PROGRESS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return if (viewType == VIEW_TYPE_PROGRESS) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
            ProgressViewHolder(view)
        } else {
            val binding =
                ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ImageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bind(items[position]!!)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addNullData() {
        items.add(null)
        notifyItemInserted(itemCount - 1)
    }

    fun removeNullData() {
        if (itemCount - 1 >= 0) {
            items.removeAt(itemCount - 1)
            notifyItemRemoved(itemCount)
        }
    }

    fun clearData() {
        val size = items.size
        this.items.clear()
        notifyItemRangeRemoved(0, size);
    }

    fun addNewData(items: List<ImageEntity>) {
        val size = this.items.size
        this.items.addAll(items)
        val newSize = this.items.size
        notifyItemRangeChanged(size, newSize)
    }
}