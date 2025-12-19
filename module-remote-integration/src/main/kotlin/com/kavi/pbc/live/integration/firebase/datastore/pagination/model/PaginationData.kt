package com.kavi.pbc.live.integration.firebase.datastore.pagination.model

import com.google.cloud.firestore.QueryDocumentSnapshot

data class PaginationData (
    val previousPageLastDocKey: String,
    val lastDocumentSnapshot: QueryDocumentSnapshot,
    val createdTime: Long
)