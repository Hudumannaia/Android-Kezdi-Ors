package android.kezdi.ors.Adapter

import android.kezdi.ors.Interface.ILoadMore
import android.kezdi.ors.MainActivity
import android.kezdi.ors.Models.Restaurant
import android.kezdi.ors.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(recyclerView: RecyclerView, private val activity: MainActivity, var items: MutableList<Restaurant?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    internal class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rPic: ImageView = itemView.findViewById(R.id.rPic)
        val rTitle: TextView = itemView.findViewById(R.id.rTitle)
        val rAddress: TextView = itemView.findViewById(R.id.rAddress)
        val rPrice: TextView = itemView.findViewById(R.id.rPrice)
        val myFavourite: ImageView = itemView.findViewById(R.id.myFavourite)
        val view: View = itemView
    }

    private val VIEWTYPEITEM: Int = 0
    private val VIEWTYPELOADING: Int = 1

    private var loadMore: ILoadMore? = null
    private var isLoading: Boolean = false
    private val visibleThreshold: Int = 5

    init {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!isLoading && linearLayoutManager.itemCount <= linearLayoutManager.findLastVisibleItemPosition() + visibleThreshold){
                    loadMore?.onLoadMore()
                    isLoading = true    //huncut kis sorocska (a tutorialok alapjan)
                }
            }
        })
    }

    override fun getItemViewType(position: Int) = when(items[position]) {
        is Restaurant -> VIEWTYPEITEM
        else -> VIEWTYPELOADING
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType){
        VIEWTYPEITEM -> ItemViewHolder( LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout, parent, false) )
        else -> LoadingViewHolder( LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loading, parent, false) )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder){ when(this){
            is ItemViewHolder -> {
                val item = items[position]!!
                if (item.image_url.isNotEmpty()) Glide
                        .with(activity)
                        .load(item.image_url)
                        .centerCrop()
                        .into(rPic)
                rPic.setOnClickListener { activity.clickFavourite(item, myFavourite) }
                activity.setFavouriteIndicator(item.id, myFavourite)
                rTitle.text = item.name
                rAddress.text = item.address
                rPrice.text = "$".repeat(item.price)
                view.setOnClickListener { activity.showDetails(item.id) }
            }
            is LoadingViewHolder -> {
                progressBar.isIndeterminate = true
            }
        }}
    }

    override fun getItemCount() = items.size
    fun setLoaded(){ isLoading = false }
    fun setLoadMore(loadMore: ILoadMore){ this.loadMore = loadMore }
}