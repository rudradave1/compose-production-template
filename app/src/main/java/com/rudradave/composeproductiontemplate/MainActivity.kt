package com.rudradave.composeproductiontemplate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rudradave.composeproductiontemplate.ui.theme.ComposeProductionTemplateTheme
import com.rudradave.features.samplefeature.navigation.SampleFeatureDestination
import com.rudradave.features.samplefeature.navigation.sampleFeatureGraph

/**
 * Single-activity host for app navigation.
 */
@dagger.hilt.android.AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeProductionTemplateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}

/**
 * Root compose navigation graph.
 */
@Composable
private fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SampleFeatureDestination.route
    ) {
        sampleFeatureGraph()
    }
}
