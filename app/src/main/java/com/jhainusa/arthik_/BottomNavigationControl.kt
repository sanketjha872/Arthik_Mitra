package com.jhainusa.arthik_

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.jhainusa.arthik_.BackendFiles.AppDatabase
import com.jhainusa.arthik_.BottomNavPages.ChatBotScreen
import com.jhainusa.arthik_.BottomNavPages.DebtManagementScreen
import com.jhainusa.arthik_.BottomNavPages.ExpenseTrackerScreen
import com.jhainusa.arthik_.BottomNavPages.budgethindi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun BottomNavBarApp(db: AppDatabase,Mainnav:NavController) {
    val navController = rememberNavController()
    Scaffold (
        contentWindowInsets = WindowInsets(top=0.dp),
        bottomBar = {  BottomNavBar(navController) }
        ) { imepadding ->
        AnimatedNavHost(
            navController = navController,
            startDestination = BottomNavItem.Activity.route,
            modifier = Modifier.background(Color.White).padding(imepadding)
        ) {
            composable(BottomNavItem.Activity.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }) { budgethindi(mainNav = Mainnav)  }
            composable(BottomNavItem.Budget.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }) { ExpenseTrackerScreen() }
            composable(BottomNavItem.Transaction.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }) { DebtManagementScreen() }
            composable(BottomNavItem.Account.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }) { ChatBotScreen() }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Activity,
        BottomNavItem.Budget,
        BottomNavItem.Transaction,
        BottomNavItem.Account
    )
    val currentRoute = navController.currentBackStackEntryAsState()?.value?.destination?.route

    NavigationBar(

            containerColor = Color.White ,
                modifier = Modifier.padding(0.dp).fillMaxWidth(1f)
            .shadow(10.dp, RoundedCornerShape(30.dp))
            .background(Color.White, RoundedCornerShape(20.dp))
    ) {
        items.forEach { item ->
            NavigationBarItem(
                colors = NavigationBarItemColors(
                    selectedIconColor = Color.DarkGray,
                    selectedTextColor = Color.DarkGray,
                    selectedIndicatorColor = Color(0xFFE5E3E3),
                    unselectedIconColor = Color.DarkGray,
                    unselectedTextColor = Color.Gray,
                    disabledIconColor = Color.Gray,
                    disabledTextColor = Color.White),
                icon = { Icon(imageVector = ImageVector.vectorResource(id = item.icon), contentDescription = item.title) },
                label = { Text(item.title,
                    fontFamily = intern) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ScreenContent(title: String) {
    Text(text = title, style = MaterialTheme.typography.headlineMedium)
}

data class BottomNavItem(
    val title: String,
    val icon:Int,
    val route: String
) {
    companion object {
        val Activity = BottomNavItem("गतिविधि",R.drawable.vector__1_,"activity")
        val Budget = BottomNavItem("बजट", R.drawable.arcticons_budgetbakers_wallet__1_, "budget")
        val Transaction = BottomNavItem("ऋण", R.drawable.baseline_payment_24, "transaction")
        val Account = BottomNavItem("हिसाबAI", R.drawable.chatbot, "account")
    }
}
