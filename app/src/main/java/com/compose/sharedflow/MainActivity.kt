package com.compose.sharedflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compose.sharedflow.ui.theme.SharedFlowTheme
import kotlinx.coroutines.flow.SharedFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SharedFlowTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenSetup()
                }
            }
        }
    }
}

@Composable
fun ScreenSetup(viewModel: DemoViewModel = viewModel()) {
    MainScreen(
        viewModel.sharedFlow
    )
}

@Composable
fun MainScreen(sharedFlow: SharedFlow<Int>) {

    val messages = remember {
        mutableStateListOf<Int>()
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sharedFlow.collect {
                println("Collecting $it")
                messages.add(it)
            }
        }
    }

    LazyColumn {
        items(messages) {
            Text(
                "Collected Value = $it",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(5.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DemoPreview() {
    SharedFlowTheme {
        ScreenSetup(viewModel())
    }
}