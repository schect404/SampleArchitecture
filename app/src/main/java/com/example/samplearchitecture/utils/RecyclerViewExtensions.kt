package com.example.samplearchitecture.utils

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.attachAdapter(listAdapter: RecyclerView.Adapter<*>) {
    adapter ?: run { adapter = listAdapter }
}