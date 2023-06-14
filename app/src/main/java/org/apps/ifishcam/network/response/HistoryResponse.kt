package org.apps.ifishcam.network.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class HistoryResponse(
	val uid: String? = null,
	val histories: List<HistoriesItem?>? = null
)

@Parcelize
data class HistoriesItem(
	val photoUrl: String? = null,
	val createdAt: String? = null,
	val historyId: String? = null,
	val fishId: String? = null,
	val name: String? = null,
	val predictionAccuracy: String? = null,
	val userId: String? = null,
	val predictionValue: String? = null
): Parcelable

