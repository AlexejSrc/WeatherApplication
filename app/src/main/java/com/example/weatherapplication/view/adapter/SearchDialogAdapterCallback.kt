package com.example.weatherapplication.view.adapter

import com.example.weatherapplication.model.SearchAdapterItem

interface SearchDialogAdapterCallback {
    fun onDeleteItem(item: SearchAdapterItem)
    fun onAddItem(item: SearchAdapterItem)
    fun onOpenItem(item: SearchAdapterItem)
}