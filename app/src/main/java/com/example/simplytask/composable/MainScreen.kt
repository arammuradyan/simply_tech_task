package com.example.simplytask.composable

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simplytask.MainViewModel
import com.example.simplytask.R
import com.example.simplytask.UiState
import com.example.simplytask.data.ActionItem
import com.example.simplytask.data.ActionType
import com.example.simplytask.ui.theme.Black
import com.example.simplytask.ui.theme.Blue
import com.example.simplytask.ui.theme.Gray1
import com.example.simplytask.ui.theme.SimplyTaskTheme

private const val FADE_ANIM_DUR = 500

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainScreen()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() = SimplyTaskTheme {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            content = {
                Content()
            },
            bottomBar = {
                HomeBottomBar()
            }
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun Content(
    mainViewModel: MainViewModel = viewModel()
) {
    val uiState = mainViewModel.uiState.collectAsState()
    val needToShowDialog = remember {
        derivedStateOf {
            uiState.value is UiState.ShowDialog
        }
    }
    val needToShowToast =
        remember { derivedStateOf { uiState.value is UiState.UnlockInProgress || uiState.value is UiState.UnlockSuccess } }

    if (needToShowDialog.value)
        UnlockDialog(
            onNegativeClicked = { mainViewModel.negativeClicked() },
            onPositiveClicked = { mainViewModel.positiveClicked() }
        )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            MainScreenBackground(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )
            TopBar(modifier = Modifier.align(Alignment.TopStart))
            Info()
        }
        ActionBar(
            onActionClick = { actionType -> mainViewModel.actionItemClicked(actionType)},
            actionItems = mainViewModel.actionItems,
            uiState = uiState
        )
        Spacer(modifier = Modifier.weight(1f))
        Toast(uiState.value, needToShowToast.value)
    }
}

@Composable
private fun MainScreenBackground(modifier: Modifier) {
    Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(336.dp),
        contentScale = ContentScale.FillBounds,
        painter = painterResource(id = R.drawable.morning),
        contentDescription = ""
    )
    Image(
        painter = painterResource(id = R.drawable.vehicles),
        contentDescription = "",
        modifier = modifier
            .padding(top = 153.dp)
            .width(218.dp)
            .height(159.dp),
    )
}

@Composable
private fun TopBar(modifier: Modifier) =
    Row(
        modifier = modifier
            .padding(top = 40.dp)
            .fillMaxWidth()
            .height(36.dp),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_live_assist),
            contentDescription = "",
            modifier = Modifier
                .padding(top = 6.dp, start = 28.dp)
                .width(28.dp)
                .height(28.dp),
        )
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .wrapContentWidth()
                .height(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_outline_lock),
                contentDescription = "",
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .align(
                        Alignment.CenterVertically
                    ),
            )
            Text(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentSize(),
                text = stringResource(id = R.string.my_nissan),
                color = Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 16.sp,
                )
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_notification),
            contentDescription = "",
            modifier = Modifier
                .padding(end = 28.dp)
                .width(24.dp)
                .height(24.dp)
                .align(Alignment.CenterVertically)
        )
    }

@Composable
private fun Info() = Column(
    Modifier
        .padding(top = 91.dp, start = 27.dp)
        .wrapContentSize()
) {
    Text(
        modifier = Modifier
            .wrapContentSize(),
        text = stringResource(id = R.string.est),
        color = Gray1,
        textAlign = TextAlign.Center,
        style = TextStyle(
            fontSize = 14.sp,
        )
    )
    Row(
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            modifier = Modifier
                .wrapContentSize(),
            text = stringResource(id = R.string.distance),
            color = Black,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                fontSize = 40.sp,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                ),
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Bottom,
                    trim = LineHeightStyle.Trim.Both
                )
            )
        )
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 2.dp, bottom = 6.dp),
            text = stringResource(id = R.string.ml),
            color = Black,
            style = TextStyle(
                fontSize = 18.sp
            )
        )
    }
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cloud),
            contentDescription = "",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .align(Alignment.CenterVertically),
        )
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(start = 2.dp),
            text = stringResource(id = R.string.weather),
            color = Black,
            style = TextStyle(
                fontSize = 16.sp
            )
        )
    }
}

@Composable
private fun UnlockDialog(
    onPositiveClicked: () -> Unit,
    onNegativeClicked: () -> Unit
) = Dialog(onDismissRequest = {
    onNegativeClicked()
}) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .height(176.dp),
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 18.sp,
                ),
                color = Black,
                fontFamily = FontFamily.Serif,
                text = stringResource(id = R.string.dialog_title)
            )
            Text(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 14.sp,
                ),
                color = Black,
                fontFamily = FontFamily.Serif,
                text = stringResource(id = R.string.dialog_content)
            )
            Row(
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                TextButton(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .background(Color.White)
                        .wrapContentSize(),
                    contentPadding = PaddingValues(vertical = 4.dp, horizontal = 12.dp),
                    onClick = { onNegativeClicked() }
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize(), textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp,
                        ),
                        color = Blue,
                        text = stringResource(id = R.string.negative_button)
                    )
                }
                TextButton(
                    modifier = Modifier
                        .wrapContentSize(),
                    shape = RoundedCornerShape(3.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = colorResource(id = R.color.white)
                    ),
                    onClick = { onPositiveClicked() }
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp,
                        ),
                        color = colorResource(id = R.color.white),
                        text = stringResource(id = R.string.confirm_button)
                    )
                }
            }
        }
    }
}

@Composable
private fun Toast(
    uiState: UiState,
    isVisible: Boolean
) = AnimatedVisibility(
    visible = isVisible,
    enter = fadeIn(
        initialAlpha = 0.0f,
        animationSpec = tween(durationMillis = FADE_ANIM_DUR)
    ),
    exit = fadeOut(
        targetAlpha = 0.0f,
        animationSpec = tween(durationMillis = FADE_ANIM_DUR)
    )
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .padding(bottom = 80.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(Black),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .wrapContentSize(),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 15.sp,
            ),
            color = colorResource(id = R.color.white),
            text = if (uiState is UiState.UnlockInProgress) stringResource(id = R.string.unlocking_message) else stringResource(
                id = R.string.unlocked_message
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        if (uiState is UiState.UnlockSuccess)
            Image(
                painter = painterResource(id = R.drawable.ic_done),
                contentDescription = "",
                modifier = Modifier
                    .padding(end = 16.dp)
                    .width(20.dp)
                    .height(20.dp)
                    .align(Alignment.CenterVertically)
            )
    }
}

@Composable
private fun ActionBar(
    uiState: State<UiState>,
    actionItems: List<ActionItem>,
    onActionClick: (ActionType) -> Unit
) = LazyRow(
    modifier = Modifier
        .height(100.dp)
        .fillMaxSize(),
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically,
) {
    items(items = actionItems) { actionItem ->
        Column(
            modifier = Modifier
                .width(60.dp)
                .height(76.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val needToShowProgress = remember {
                derivedStateOf {
                    uiState.value is UiState.UnlockInProgress && actionItem.actionType == ActionType.UNLOCK
                }
            }
            val iconId = remember { derivedStateOf { getIconId(uiState.value, actionItem) } }
            val isEnabled =
                remember { derivedStateOf { getIsActionItemEnabled(uiState.value, actionItem) } }

            Box {
                IconButton(
                    onClick = { onActionClick(actionItem.actionType) },
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center),
                    enabled = isEnabled.value,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Black,
                        disabledContentColor = Color.Gray
                    )
                ) {
                    Icon(
                        painterResource(iconId.value),
                        stringResource(id = actionItem.descriptionId)
                    )
                }
                if (needToShowProgress.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(52.dp)
                            .height(52.dp)
                            .align(Alignment.Center),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeWidth = 3.dp,
                    )
                }
            }
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(60.dp)
                    .wrapContentHeight(),
                text = stringResource(id = actionItem.descriptionId),
                color = Black,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineBreak = LineBreak.Simple
                )
            )
        }
    }
}

private fun getIconId(uiState: UiState, actionItem: ActionItem) =
    if (uiState == UiState.UnlockInProgress && actionItem.actionType == ActionType.UNLOCK) R.drawable.ic_unlocking else actionItem.icon

private fun getIsActionItemEnabled(uiState: UiState, actionItem: ActionItem) =
    when (actionItem.actionType) {
        ActionType.UNSPECIFIED -> true
        ActionType.UNLOCK -> uiState !is UiState.Unlocked && uiState !is UiState.UnlockSuccess
        ActionType.LOCK -> !(uiState is UiState.Locked || uiState is UiState.UnlockInProgress)
    }

@Composable
private fun HomeBottomBar(
    mainViewModel: MainViewModel = viewModel()
) = LazyRow(
    modifier = Modifier
        .height(76.dp)
        .fillMaxWidth()
        .background(Color.White),
    horizontalArrangement = Arrangement.SpaceEvenly,
) {
    items(items = mainViewModel.navBarItems) { destination ->
        Column(
            modifier = Modifier.width(48.dp),
        ) {
            Box(
                modifier = Modifier
                    .height(54.dp)
            ) {
                if (destination.isSelected) {
                    Divider(
                        color = Blue,
                        thickness = 2.dp,
                    )
                }
                Image(
                    painter = painterResource(destination.icon),
                    contentDescription = stringResource(id = destination.descriptionId),
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                )
                Text(
                    modifier = Modifier
                        .width(56.dp)
                        .wrapContentHeight()
                        .align(Alignment.BottomCenter),
                    text = stringResource(id = destination.descriptionId),
                    textAlign = TextAlign.Center,
                    color = if (destination.isSelected) Blue else Black,
                    style = TextStyle(
                        fontSize = 12.sp,
                        lineBreak = LineBreak.Simple
                    )
                )
            }
        }
    }
}
