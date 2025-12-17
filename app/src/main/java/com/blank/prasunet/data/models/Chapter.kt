package com.blank.prasunet.data.models

data class Chapter(
    val id: String,
    val title: String,
    val content: String,
    val image_url: String? = null,
    val video_url: String? = null,
    val sequence_order: Int,
    val is_completed: Boolean = false,
    val is_unlocked: Boolean = false
)

data class CreateChapterRequest(
    val title: String,
    val content: String,
    val image_url: String? = null,
    val video_url: String? = null,
    val sequence_order: Int
)

data class ChapterCreatedResponse(
    val message: String,
    val chapter: Chapter
)

