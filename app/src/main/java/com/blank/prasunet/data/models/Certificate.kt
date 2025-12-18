package com.blank.prasunet.data.models

data class CertificateDetail(
    val id: String,
    val student: CertificateUser,
    val course: CertificateCourse,
    val file_url: String,
    val issued_at: String
)

data class CertificateUser(
    val id: String,
    val name: String,
    val email: String
)

data class CertificateCourse(
    val id: String,
    val title: String
)

