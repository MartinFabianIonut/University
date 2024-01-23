package com.example.messages.todo.data.remote

import com.example.messages.todo.data.Message
data class MessageEvent(val type: String, val payload: Message)