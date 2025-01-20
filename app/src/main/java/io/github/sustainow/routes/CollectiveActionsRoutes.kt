import kotlinx.serialization.Serializable

@Serializable
object CollectiveActions

@Serializable
object SearchCollectiveActions

@Serializable
data class ViewCollectiveAction(
    val id: Int?,
)
