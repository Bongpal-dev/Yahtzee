package com.code.bongpaldev.yahtzeee.ui.game

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.code.bongpaldev.yahtzeee.model.LOWER
import com.code.bongpaldev.yahtzeee.R
import com.code.bongpaldev.yahtzeee.RESULT_SCREEN
import com.code.bongpaldev.yahtzeee.Score
import com.code.bongpaldev.yahtzeee.model.ScoreType
import com.code.bongpaldev.yahtzeee.model.UPPER
import com.code.bongpaldev.yahtzeee.ui.theme.ButtonDisable
import com.code.bongpaldev.yahtzeee.ui.theme.Active
import com.code.bongpaldev.yahtzeee.ui.theme.PickAble
import com.code.bongpaldev.yahtzeee.ui.theme.Typography

const val TAG = "Yahtzeeeeeeee"

@Composable
fun GameScreen(
    navController: NavController,
    paddingValues: PaddingValues = PaddingValues()
) {
    val viewModel = remember { GameViewModel() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val isGameOver by viewModel.isGameOver.observeAsState()
        if (isGameOver!!) {
            navController.navigate(RESULT_SCREEN)
        }

        PointArea(modifier = Modifier.weight(2.3f), viewModel)
        PointDisplay(modifier = Modifier.weight(0.4f), viewModel)
        DiceArea(Modifier.weight(1f), viewModel)
        ButtonArea(modifier = Modifier.weight(0.5f), viewModel)
    }
}

@Composable
fun PointArea(
    modifier: Modifier,
    viewModel: GameViewModel
) {
    val scores = viewModel.scoreSheet.observeAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        PointSection(
            modifier = Modifier.weight(0.6f),
            section = scores.value?.filter { it.scoreType.section == UPPER } ?: emptyList(),
            viewModel = viewModel
        )

        PointSection(
            modifier = Modifier.weight(1f),
            section = scores.value?.filter { it.scoreType.section == LOWER } ?: emptyList(),
            viewModel = viewModel

        )
    }
}

@Composable
fun PointSection(
    modifier: Modifier,
    section: List<Score>,
    viewModel: GameViewModel
) {

    Column(
        modifier = modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        section.forEach { score ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val newTurn by viewModel.isNewTurn.observeAsState()

                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clickable {
                            if (!score.isPick && newTurn!!.not()) viewModel.selectScore(
                                score.scoreType
                            )
                        },
                    painter = getScoreImage(type = score.scoreType), contentDescription = null
                )

                Box(
                    modifier = Modifier
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = when {
                            score.isPick -> score.score.toString()
                            score.score == 0 && score.isSelected.not() -> ""
                            else -> score.score.toString()
                        },
                        color = when {
                            score.isPick -> Color.Black
                            score.isSelected -> Active
                            else -> Color.LightGray
                        },
                        style = Typography.labelLarge,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun PointDisplay(modifier: Modifier, viewModel: GameViewModel) {
    val totalScore = viewModel.totalScore.observeAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.DarkGray)
            .padding(horizontal = 32.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(0.5f)
        ) {
            Text(text = "12", fontSize = 24.sp)
        }

        Box(
            modifier = Modifier.weight(0.5f)
        ) {
            Text(text = "12", fontSize = 24.sp)
        }

        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Total: ", fontSize = 16.sp)
            Text(text = "${totalScore.value}점", fontSize = 24.sp)
        }

    }
}

@Composable
fun DiceArea(modifier: Modifier, viewModel: GameViewModel) {
    val dices by viewModel.dices.observeAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        dices?.forEachIndexed { index, dice ->
            DiceItem(
                index = index,
                number = dice.value,
                isActive = dice.isActive,
                viewModel = viewModel,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            )
        }
    }
}

@Composable
fun DiceItem(
    index: Int,
    number: Int,
    isActive: Boolean,
    modifier: Modifier,
    viewModel: GameViewModel
) {
    val localDensity = LocalDensity.current
    var holdPosition by remember { mutableStateOf(0.dp) }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .onGloballyPositioned {
                with(localDensity) {
                    holdPosition = it.size.height.toDp() - it.size.width.toDp()
                }
            }
    ) {
        val state by animateDpAsState(
            targetValue = if (isActive) 0.dp else holdPosition,
            label = "",
            animationSpec = tween(durationMillis = 200)
        )
        val isNewTurn = viewModel.isNewTurn.observeAsState()

        Box(
            modifier = modifier
                .alpha(if (isNewTurn.value!!) 0f else 1f)
                .offset(y = state)
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .clickable {
                    if (isNewTurn.value!!.not()) viewModel.holdDice(index)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(painter = getDiceImage(num = number), contentDescription = null)
        }
    }
}

@Composable
private fun getDiceImage(num: Int): Painter {
    val painter = painterResource(
        when (num) {
            1 -> R.drawable.img_dice_1
            2 -> R.drawable.img_dice_2
            3 -> R.drawable.img_dice_3
            4 -> R.drawable.img_dice_4
            5 -> R.drawable.img_dice_5
            6 -> R.drawable.img_dice_6
            else -> throw Exception("Dice Number Error")
        }
    )

    return painter
}

@Composable
private fun getScoreImage(type: ScoreType): Painter {
    return painterResource(
        when (type) {
            ScoreType.Aces -> R.drawable.img_dice_1
            ScoreType.Twos -> R.drawable.img_dice_2
            ScoreType.Threes -> R.drawable.img_dice_3
            ScoreType.Fours -> R.drawable.img_dice_4
            ScoreType.Fives -> R.drawable.img_dice_5
            ScoreType.Sixes -> R.drawable.img_dice_6
            ScoreType.ThreeOfAKind -> R.drawable.pair_1
            ScoreType.FourOfAKind -> R.drawable.pair_2
            ScoreType.FullHouse -> R.drawable.pair_3
            ScoreType.SmallStraight -> R.drawable.pair_4
            ScoreType.LargeStraight -> R.drawable.pair_5
            ScoreType.Yahtzee -> R.drawable.pair_7
            ScoreType.Chance -> R.drawable.pair_6
            else -> throw IllegalArgumentException("Score Type Error")
        }
    )
}

@Composable
fun ButtonArea(
    modifier: Modifier,
    viewModel: GameViewModel
) {
    val canRoll = viewModel.canRoll.observeAsState()
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            enabled = canRoll.value!!,
            modifier = modifier
                .fillMaxHeight()
                .weight(1f),
            colors = RollButtonColor(),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                viewModel.rollDices();
            },
            content = { Text(text = "Roll", style = Typography.displayLarge) }
        )

        val isSelected = viewModel.currentSelected.observeAsState().value != null

        Button(
            modifier = modifier
                .fillMaxHeight()
                .weight(1f),
            enabled = isSelected,
            colors = ButtonDefaults.buttonColors(
                containerColor = PickAble,
                disabledContainerColor = ButtonDisable
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                viewModel.pickScore()
            },
            content = {
                Text(
                    text = "Pick",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        )
    }
}

@Composable
fun RollButtonColor(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = Active,
        disabledContainerColor = ButtonDisable
    )
}