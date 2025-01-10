package com.isodev.wumpusworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.isodev.wumpusworld.model.*
import com.isodev.wumpusworld.ui.theme.WumpusWorldTheme
import com.isodev.wumpusworld.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WumpusWorldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = gameState.message,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        GameGrid(
            gameState = gameState,
            onCellClick = { position ->
                viewModel.movePlayer(position)
            },
            onLongClick = { position ->
                if (gameState.hasArrow) {
                    viewModel.shootArrow(position)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.restartGame() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Tekrar Başlat")
        }

        if (gameState.hasArrow) {
            Text(
                text = "Hücreye uzun basarsan okla wumpusu vurabilirsin",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        Text(
            text = "Nasıl Oynanır:\n" +
                   "• Altını (G) bulmaya çalış\n" +
                   "• Wumpus (S) veya çukurdan (B) kaçın\n" +
                   "• Komşu hücrelerdeki tehlikeleri gösteren işaretler:\n" +
                   "  B: Çukur yakında\n" +
                   "  S: Wumpus yakında\n" +
                   "  G: Altın yakında\n" +
                   "• Sadece yan hücrelere hareket edebilirsin",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun GameGrid(
    gameState: GameState,
    onCellClick: (Position) -> Unit,
    onLongClick: (Position) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
    ) {
        for (i in 3 downTo 0) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                for (j in 0..3) {
                    GameCell(
                        cell = gameState.grid[i][j],
                        position = Position(i, j),
                        isPlayerHere = gameState.playerPosition == Position(i, j),
                        onCellClick = onCellClick,
                        onLongClick = onLongClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameCell(
    cell: Cell,
    position: Position,
    isPlayerHere: Boolean,
    onCellClick: (Position) -> Unit,
    onLongClick: (Position) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(2.dp)
            .aspectRatio(1f)
            .border(1.dp, Color.Black)
            .background(getCellColor(cell))
            .combinedClickable(
                onClick = { onCellClick(position) },
                onLongClick = { onLongClick(position) }
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            isPlayerHere -> Text("P")
            cell.isVisited -> Text("")
            cell.state != CellState.HIDDEN -> {
                when (cell.state) {
                    CellState.BREEZE -> Text("B")
                    CellState.STENCH -> Text("S")
                    CellState.BOTH_WARNINGS -> Text("B,S")
                    CellState.GLITTER -> Text("G")
                    else -> Text("")
                }
            }
        }
    }
}

private fun getCellColor(cell: Cell): Color {
    return when {
        !cell.isVisited && cell.state == CellState.HIDDEN -> Color.Gray
        cell.state == CellState.BREEZE -> Color.Cyan
        cell.state == CellState.STENCH -> Color.Yellow
        cell.state == CellState.BOTH_WARNINGS -> Color.Magenta
        cell.state == CellState.GLITTER -> Color(0xFFFFD700)
        cell.isVisited -> Color.White
        else -> Color.LightGray
    }
}