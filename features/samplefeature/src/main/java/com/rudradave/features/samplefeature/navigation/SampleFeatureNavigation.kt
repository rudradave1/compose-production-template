package com.rudradave.features.samplefeature.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rudradave.features.samplefeature.presentation.SampleFeatureRoute

/**
 * Navigation route names for sample feature graph.
 */
object SampleFeatureDestination {
    const val route: String = "sample_feature"
}

/**
 * Registers sample feature destination in nav graph.
 */
fun NavGraphBuilder.sampleFeatureGraph() {
    composable(route = SampleFeatureDestination.route) {
        SampleFeatureRoute()
    }
}
