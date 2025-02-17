package io.github.sustainow.presentation.ui.components.formulary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.sustainow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReuseAnswersDialog(active:Boolean, onDismissRequest: () -> Unit,onAcceptRequest: () -> Unit) {
    if(active) {
        BasicAlertDialog(
            onDismissRequest = { onDismissRequest() }
        ) {
            Surface(
                modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        stringResource(R.string.already_answered_form_message),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier=Modifier.padding(16.dp).fillMaxWidth()
                    ) {

                        Button(
                            onClick = { onDismissRequest() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        ) {
                            Text(
                                stringResource(R.string.no),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                        Button(
                            onClick = { onAcceptRequest()
                                        onDismissRequest()
                                      },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                stringResource(R.string.yes),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
            }
        }
    }
}