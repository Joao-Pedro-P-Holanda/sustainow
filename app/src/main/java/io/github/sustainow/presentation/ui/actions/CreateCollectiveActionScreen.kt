package io.github.sustainow.presentation.ui.actions

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import io.github.sustainow.R
import io.github.sustainow.presentation.ui.components.LoadingModal
import io.github.sustainow.presentation.ui.components.UserProfileCard
import io.github.sustainow.presentation.ui.utils.toLocalDate
import io.github.sustainow.presentation.viewmodel.CollectiveActionViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.uuid.ExperimentalUuidApi

enum class SubmitAction {
    CREATE,
    UPDATE
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalUuidApi::class)
@Composable
fun FormCollectiveActionScreen(viewModel: CollectiveActionViewModel, submitAction: SubmitAction, modifier: Modifier = Modifier) {
    val users by viewModel.users.collectAsState()
    val action by viewModel.action.collectAsState()
    val invitations by viewModel.invitations.collectAsState()
    val usersToInvite by viewModel.usersToInvite.collectAsState()
    var imageUri by remember(action) { mutableStateOf(action?.images?.firstOrNull()) }
    var name by remember(action) { mutableStateOf(action?.name ?: "") }
    var description by remember(action) { mutableStateOf(action?.description ?: "") }
    var status by remember(action) { mutableStateOf(action?.status ?: "") }
    var startDate by remember(action) { mutableStateOf(action?.startDate) }
    var endDate by remember(action) { mutableStateOf(action?.endDate) }

    var showDatePicker by remember { mutableStateOf(false) }
    val dateRangePickerState = remember(action) {
        DateRangePickerState(
            locale= Locale.getDefault(),
            initialSelectedStartDateMillis = startDate?.atStartOfDayIn(
                TimeZone.currentSystemDefault()
            )
                ?.toEpochMilliseconds(),
            initialSelectedEndDateMillis = endDate?.atStartOfDayIn(
                TimeZone.currentSystemDefault()
            )
                ?.toEpochMilliseconds(),
        )
    }
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    val filteredUsers = remember(users, usersToInvite, action, invitations) {
        derivedStateOf {
            users?.filter {
                        (action == null  || !action!!.members.contains(it)) &&
                        (invitations == null || !invitations!!.any { invite -> invite.invitedUser == it }) &&
                        !usersToInvite.contains(it)
            }
        }
    }

    val selectPictureLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
            } else {
                Log.e("GalleryFragment", "Failed to get image from gallery")
            }
        }

    fun selectPicture() {
        selectPictureLauncher.launch("image/*")
    }

    if(loading) {
        LoadingModal()
    }
    else {
        if (error != null) {
            BasicAlertDialog(
                onDismissRequest =
                { viewModel.resetError() },
            ) {
                Surface(
                    modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = AlertDialogDefaults.TonalElevation,
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(error!!)
                    }
                }
            }
        } else {
            Column(
                modifier = modifier.fillMaxSize().padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (submitAction == SubmitAction.CREATE) stringResource(id = R.string.create_collective_action_title) else "Editar ação coletiva",
                    style = MaterialTheme.typography.displaySmall
                )
                if (imageUri != null) {
                    Box(
                        modifier = modifier.heightIn(0.dp, 300.dp)
                            .wrapContentSize(align = Alignment.TopStart),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        SubcomposeAsyncImage(
                            model = imageUri ?: R.drawable.image_not_found,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(0.dp, 300.dp)
                                .border(
                                    BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.outlineVariant
                                    )
                                ),
                            contentScale = ContentScale.Crop,
                            loading = { LoadingModal() }
                        )
                        IconButton(
                            onClick = { imageUri = null },
                            modifier = modifier.align(Alignment.TopEnd).clip(
                                RoundedCornerShape(
                                    topStart = 0.dp,
                                    topEnd = 0.dp,
                                    bottomEnd = 0.dp,
                                    bottomStart = 12.dp
                                )
                            ).background(MaterialTheme.colorScheme.error),
                            colors = IconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onError,
                                containerColor = Color.Transparent,
                                disabledContentColor = MaterialTheme.colorScheme.onErrorContainer,
                                disabledContainerColor = Color.Transparent
                            )
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = null)
                        }
                    }
                }
                Button(onClick = { selectPicture() }) {
                    if (imageUri == null) {
                        Text(stringResource(id = R.string.create_collective_action_image_button_add))
                    } else {
                        Text(stringResource(id = R.string.create_collective_action_image_button_update))
                    }
                }


                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.create_collective_action_field_name)) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    maxLines = 4,
                    label = { Text(stringResource(R.string.create_collective_action_field_description)) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = status,
                    onValueChange = { status = it },
                    label = { Text(stringResource(R.string.create_collective_action_field_status)) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    FilterChip(
                        selected = startDate != null || endDate != null,
                        onClick = { showDatePicker = !showDatePicker },
                        leadingIcon = {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null
                            )
                        },
                        label = {
                            if (startDate != null && endDate != null) {
                                Text(
                                    "${
                                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                            .format(startDate?.toJavaLocalDate())
                                    } - ${
                                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                            .format(endDate?.toJavaLocalDate())
                                    }"
                                )
                            } else if (startDate != null) {
                                Text(
                                    "A partir de ${
                                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                            .format(startDate?.toJavaLocalDate())
                                    }"
                                )
                            } else if (endDate != null) {
                                Text(
                                    "Até ${
                                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                            .format(endDate?.toJavaLocalDate())
                                    }"
                                )
                            } else {
                                Text(stringResource(R.string.create_collective_action_date_message))
                            }
                        }
                    )
                    if(error != null && startDate == null) {
                        Text("Data de início é obrigatória", color = MaterialTheme.colorScheme.error)
                    }
                    if(error != null && endDate == null) {
                        Text("Data de término é obrigatória", color = MaterialTheme.colorScheme.error)
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)){
                    Column{
                        Text(stringResource(R.string.create_collective_action_invitation_title),style=MaterialTheme.typography.titleLarge)
                        Text(stringResource(R.string.create_collective_action_invitation_description),style=MaterialTheme.typography.bodyMedium)
                        HorizontalDivider(modifier=Modifier.fillMaxWidth())
                    }

                    FlowRow {
                        usersToInvite.forEach { user->
                            InputChip(label = { Text(user.fullName, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                selected=true,
                                onClick={viewModel.removeUserFromInvites(user)},
                                modifier=Modifier.widthIn(40.dp,80.dp))
                        }
                    }
                    Column {
                        // show only users that are not already members, don't have any pending invitations
                        // and are not already in the list of users to invite
                        filteredUsers.value?.forEach { user ->
                            key(user.id) {
                                UserProfileCard(user, onClick = {
                                    viewModel.addUserToInvites(user)
                                })
                            }
                        }
                    }
                }

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    dateRangePickerState.selectedStartDateMillis?.let {
                                        startDate = it.toLocalDate()
                                    }
                                    dateRangePickerState.selectedEndDateMillis?.let {
                                        endDate = it.toLocalDate()
                                    }
                                    showDatePicker = false
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DateRangePicker(dateRangePickerState, showModeToggle = true)
                    }
                }

                when (submitAction) {
                    SubmitAction.CREATE ->
                        Button(
                            enabled=name.isNotEmpty() && status.isNotEmpty() && startDate != null && endDate != null,
                            onClick = {
                                if(startDate== null){
                                    viewModel.setError("Data de início é obrigatória")
                                } else if (endDate == null){
                                    viewModel.setError("Data de término é obrigatória")
                                }
                                else {
                                    viewModel.create(
                                        name = name,
                                        images = listOfNotNull(imageUri),
                                        description = description,
                                        status = status,
                                        startDate = startDate!!,
                                        endDate = endDate!!,
                                        usersToInvite
                                    )
                                }

                            }, modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Text(stringResource(R.string.create_collective_action_submit_text))
                        }

                    SubmitAction.UPDATE ->
                        Button(
                            onClick = {
                                viewModel.update(
                                    name = name,
                                    images = listOfNotNull(imageUri),
                                    description = description,
                                    status = status,
                                    startDate = startDate!!,
                                    endDate = endDate!!,
                                    usersToInvite=usersToInvite
                                )
                            }, modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Text("Atualizar")
                        }
                }
            }
        }
    }
}