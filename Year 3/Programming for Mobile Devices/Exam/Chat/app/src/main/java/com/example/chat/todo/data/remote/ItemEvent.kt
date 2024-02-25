package com.example.chat.todo.data.remote

import com.example.chat.todo.data.Item
data class ItemEvent(val type: String, val payload: Item)