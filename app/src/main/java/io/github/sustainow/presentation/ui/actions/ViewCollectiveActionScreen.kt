package io.github.sustainow.presentation.ui.actions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import io.github.sustainow.R
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.components.ActivityDescription
import io.github.sustainow.presentation.ui.components.LoadingModal
import io.github.sustainow.presentation.ui.components.UserProfileCard
import io.github.sustainow.presentation.viewmodel.CollectiveActionViewModel
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import kotlin.uuid.ExperimentalUuidApi


@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class, ExperimentalLayoutApi::class)
@Composable
fun CollectiveActionScreen(userState: UserState, viewModel:CollectiveActionViewModel, modifier: Modifier = Modifier, navigateEditCallback: ()->Unit, returnCallback: ()->Unit) {
    val action by viewModel.action.collectAsState()
    val activities by viewModel.activities.collectAsState()
    val comment by viewModel.comment.collectAsState()
    val loading by viewModel.loading.collectAsState()

    var showConfirmDelete by remember { mutableStateOf(false) }

    val commentMaxLength= 180

    if(loading){
        LoadingModal()
    }
    else {
        if (action != null) {
                Column(
                    modifier = modifier.fillMaxSize().padding(24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = action!!.images.firstOrNull() ?: R.drawable.image_not_found,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(0.dp, 300.dp)
                            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)),
                        contentScale = ContentScale.Crop,
                        loading = { LoadingModal() }
                    )
                    Row(modifier = modifier.fillMaxWidth()) {
                        Text(
                            text = action!!.name,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = modifier.padding(8.dp).fillMaxWidth(0.8f)
                        )
                        if (userState is UserState.Logged) {
                            if (action!!.authorId.toString() == userState.user.uid) {
                                IconButton(
                                    onClick = { navigateEditCallback() },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = null)
                                }
                            }
                        }
                    }

                    Text(
                        text = "${stringResource(R.string.view_collective_action_author_text)} ${action!!.authorName}",
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(8.dp).align(Alignment.Start),
                        style = MaterialTheme.typography.labelLarge
                    )
                    val totalMembers = action!!.members.size + 1
                    val memberDescription = if(totalMembers > 1) "$totalMembers Membros" else "$totalMembers Membro"
                    Text(
                        text = memberDescription,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(8.dp).align(Alignment.Start),
                        style = MaterialTheme.typography.labelLarge
                    )
                    HorizontalDivider(
                        modifier.fillMaxWidth(),
                    )


                    Row(
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AssistChip(
                            onClick = {},
                            leadingIcon = { Icon(Icons.Default.Today, contentDescription = null) },
                            label = {
                                Text(
                                    text = action!!.startDate.toJavaLocalDate()
                                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        )
                        AssistChip(
                            onClick = {},
                            leadingIcon = { Icon(Icons.Default.Today, contentDescription = null) },
                            label = {
                                Text(
                                    text = action!!.endDate.toJavaLocalDate()
                                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            })
                    }

                    Row(
                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
                    ) {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = action!!.status,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)
                        )
                    }

                    Column(
                        modifier = modifier.fillMaxWidth().clip(
                            RoundedCornerShape(16.dp)
                        ).background(MaterialTheme.colorScheme.surfaceContainerHigh).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.view_collective_action_description_header),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(8.dp)
                        )
                        HorizontalDivider(
                            modifier.fillMaxWidth(),
                        )
                        Text(
                            text = action!!.description,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Column(
                        modifier = modifier.fillMaxWidth().clip(
                            RoundedCornerShape(16.dp)
                        ).background(MaterialTheme.colorScheme.surfaceContainer).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.view_collective_action_activity_title),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(8.dp)
                        )
                        HorizontalDivider(
                            modifier.fillMaxWidth(),
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (activities?.isNotEmpty() == true) {
                                activities?.forEach {
                                    ActivityDescription(it)
                                }
                            } else {
                                Text("Nenhuma atividade ainda")
                            }
                        }
                        if (userState is UserState.Logged){
                            if(action!!.members.any {it.id.toString() == userState.user.uid} || action!!.authorId.toString() == userState.user.uid){
                                Row(modifier=modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                                    OutlinedTextField(value = comment,
                                        onValueChange = {viewModel.setComment(it)},
                                        label= { Text("Adicione um comentário") },
                                        supportingText = { Text("${comment.length}/$commentMaxLength",style=MaterialTheme.typography.labelSmall) },
                                        modifier= modifier.fillMaxWidth(0.9f)

                                    )
                                    IconButton(
                                        onClick = {viewModel.sendComment()},
                                        enabled = comment.isNotEmpty(),
                                        modifier = modifier.size(48.dp)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                                    }
                                }
                            }
                        }
                    }

                    if (userState is UserState.Logged) {
                        if (action!!.authorId.toString() == userState.user.uid) {
                            Button(
                                onClick = { showConfirmDelete = true },
                                colors = ButtonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError,
                                    disabledContainerColor = MaterialTheme.colorScheme.errorContainer,
                                    disabledContentColor = MaterialTheme.colorScheme.onErrorContainer,
                                )
                            ) {
                                Text("Remover")
                            }
                            if (showConfirmDelete) {
                                BasicAlertDialog(
                                    onDismissRequest = { showConfirmDelete = false },
                                    content = {
                                        Card(modifier = modifier.requiredSize(300.dp, 100.dp)) {
                                            Column(
                                                modifier = modifier.fillMaxSize()
                                                    .padding(8.dp, 16.dp),
                                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                            ) {
                                                Text("Deseja mesmo excluir essa ação?")
                                                Row(
                                                    modifier = modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceAround
                                                ) {
                                                    Button(onClick = {
                                                        showConfirmDelete = false
                                                    }) {
                                                        Text("Cancelar")
                                                    }
                                                    Button(onClick = { viewModel.delete() }) {
                                                        Text("Confirmar")
                                                    }
                                                }
                                            }
                                        }
                                    })
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.view_collective_action_not_found_text))
                    Button(onClick = returnCallback) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        Text(stringResource(R.string.view_collective_action_back_text))
                    }
                }
            }
        }
    }
