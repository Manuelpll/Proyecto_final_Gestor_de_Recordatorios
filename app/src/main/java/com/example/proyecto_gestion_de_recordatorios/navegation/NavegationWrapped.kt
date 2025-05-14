package com.example.proyecto_gestion_de_recordatorios.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyecto_gestion_de_recordatorios.home.HomeScreen
import com.example.proyecto_gestion_de_recordatorios.initial.InitialScreen
import com.example.proyecto_gestion_de_recordatorios.login.LoginScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.category.CategoryScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.friend.FriendScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.newCategory.NewCategoryScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.newFriend.NewFriendScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.newReminder.NewReminderScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.reminder.ReminderScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.selectedReminder.SelectedReminderScreen
import com.example.proyecto_gestion_de_recordatorios.profile.ProfileScreen
import com.example.proyecto_gestion_de_recordatorios.register.RegisterScreen

@Composable
fun NavigationWrapper(navController: NavHostController) {
    NavHost(navController = navController, startDestination = InitialScreen) {

        composable<InitialScreen> {
            InitialScreen(
                navegateToRegister = { navController.navigate(RegisterScreen) },
                navegateToLogin = { navController.navigate(LoginScreen) }
            )
        }

        composable<RegisterScreen> {
            RegisterScreen(
                navigateToLogin = { navController.navigate(LoginScreen) }
            )
        }

        composable<LoginScreen> {
            LoginScreen(
                navegateToHome = { navController.navigate(HomeScreen) },
                navegateToRegister = { navController.navigate(RegisterScreen) }
            )
        }

        composable<HomeScreen> {
            HomeScreen(
                navegateToProfile = { navController.navigate(ProfileScreen) },
                navegateToReminder = { navController.navigate(ReminderScreen) },
                navegateToFriends = { navController.navigate(FriendScreen) },
                navegateToCategory = { navController.navigate(CategoryScreen) },
                navegateToSelectedReminder = { id -> navController.navigate(SelectedReminderScreen(id)) }
            )
        }

        composable<ProfileScreen> {
            ProfileScreen(
                navigateToInitial = { navController.navigate(InitialScreen) },
                navigateToBack= {navController.navigateUp()}
            )
        }

        composable<ReminderScreen> {
            ReminderScreen(
                navegateToNewReminder = { navController.navigate(NewReminderScreen) },
                navegateToSelectedReminder = { id-> navController.navigate(SelectedReminderScreen(id)) },
                navegateToProfile = { navController.navigate(ProfileScreen) },
                navegateToCategory = { navController.navigate(CategoryScreen) },
                navegateToFriend = { navController.navigate(FriendScreen) },
                navegateToBack= {navController.navigateUp()}
            )
        }

        composable<SelectedReminderScreen> { backStrackEntry->
            val id:SelectedReminderScreen= backStrackEntry.toRoute()
            SelectedReminderScreen(
                id_recordatorio = id.id_recordatorio,
                navegateToProfile = { navController.navigate(ProfileScreen) },
                navegateToCategory = { navController.navigate(CategoryScreen) },
                navegateToFriend = { navController.navigate(FriendScreen) },
                navegateToHome = { navController.navigate(HomeScreen) },
                navegateToBack= {navController.navigateUp()}
            )
        }

        composable<NewReminderScreen> {
            NewReminderScreen(
                navegateToReminder = { navController.navigate(ReminderScreen) }
            )
        }

        composable<FriendScreen> {
            FriendScreen(
                navegateToNewFriend = { navController.navigate(NewFriendScreen) },
                navegateToProfile = { navController.navigate(ProfileScreen) },
                navegateToReminder = { navController.navigate(ReminderScreen) },
                navegateToBack= {navController.navigateUp()}
            )
        }

        composable<NewFriendScreen>{
            NewFriendScreen(
                navegateToFriend={navController.navigate(FriendScreen)}
            )
        }

        composable<CategoryScreen> {
            CategoryScreen(
                navegateToNewCategory = { navController.navigate(NewCategoryScreen) },
                navegateToProfile = { navController.navigate(ProfileScreen) },
                navegateToFriend = { navController.navigate(FriendScreen) },
                navegateToReminder = { navController.navigate(ReminderScreen) },
                navegateToBack= {navController.navigateUp()}
            )
        }
        composable<NewCategoryScreen>{
            NewCategoryScreen(
                navigateToCategory= navController.navigate(CategoryScreen)
            )
        }
    }
}