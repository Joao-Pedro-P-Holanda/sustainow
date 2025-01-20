import kotlinx.serialization.Serializable

@Serializable
object ColetiveActions

@Serializable
object CreateCollectiveAction

@Serializable
data class UpdateCollectiveAction(
    val id: Int?,
)

@Serializable
object CollectiveActions

@Serializable
object SearchCollectiveActions

@Serializable
data class ViewCollectiveAction(
    val id: Int?,
)
