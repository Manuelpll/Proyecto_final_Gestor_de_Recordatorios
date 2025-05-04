package com.example.proyecto_gestion_de_recordatorios.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyecto_gestion_de_recordatorios.home.HomeScreen
import com.example.proyecto_gestion_de_recordatorios.initial.InitialScreen
import com.example.proyecto_gestion_de_recordatorios.login.LoginScreen
import com.example.proyecto_gestion_de_recordatorios.login.LoginViewModel
import com.example.proyecto_gestion_de_recordatorios.otherScreens.category.CategoryScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.friend.FriendScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.newReminder.NewReminderScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.reminder.ReminderScreen
import com.example.proyecto_gestion_de_recordatorios.otherScreens.selectedReminder.SelectedReminderScreen
import com.example.proyecto_gestion_de_recordatorios.profile.ProfileScreen
import com.example.proyecto_gestion_de_recordatorios.profile.ProfileViewModel
import com.example.proyecto_gestion_de_recordatorios.register.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavegationWrapper(navControler: NavHostController, auth: FirebaseAuth) {
    NavHost(navController = navControler, startDestination = InitialScreen) {

        composable<InitialScreen> {
            InitialScreen(
                navegateToRegister = { navControler.navigate(RegisterScreen) },
                navegateToLogin = { navControler.navigate(LoginScreen) }
            )
        }

        composable<RegisterScreen> {
            RegisterScreen(
                navegateToLogin = { navControler.navigate(LoginScreen) },
                auth
            )
        }
        composable<LoginScreen> {
            val loginViewModel = remember { LoginViewModel(auth) }

            LoginScreen(
                navegateToHome = { navControler.navigate(HomeScreen) },
                navegateToRegister = { navControler.navigate(RegisterScreen) },
                viewModel = loginViewModel
            )
        }
        composable<HomeScreen> {
            HomeScreen(
                navegateToProfile = { navControler.navigate(ProfileScreen) },
                navegateToReminder = { navControler.navigate(ReminderScreen) },
                navegateToFriends = { navControler.navigate(FriendScreen) },
                navegateToCategory = { navControler.navigate(CategoryScreen) },
                navegateToSelectedReminder = { navControler.navigate(SelectedReminderScreen) }
            )
        }
        composable<ProfileScreen> {
            val profileViewModel = remember { ProfileViewModel(auth) }
            ProfileScreen(
                viewModel = profileViewModel,
                navigateToInitial = {navControler.navigate(InitialScreen)}
            )
        }
        composable<ReminderScreen> {
            ReminderScreen(
                navegateToNewReminder = { navControler.navigate(NewReminderScreen) },
                navegateToSelectedReminder = { navControler.navigate(SelectedReminderScreen) },
                navegateToProfile = { navControler.navigate(ProfileScreen) },
                navegateToCategory = { navControler.navigate(CategoryScreen) },
                navegateToFriend = { navControler.navigate(FriendScreen) }
            )
        }

        composable<SelectedReminderScreen> {
            SelectedReminderScreen(
                navegateToProfile = { navControler.navigate(ProfileScreen) },
                navegateToCategory = { navControler.navigate(CategoryScreen) },
                navegateToFriend = { navControler.navigate(FriendScreen) },
                navegateToHome={navControler.navigate(HomeScreen)}
            )
        }
        composable<NewReminderScreen> {
            NewReminderScreen(
                navegateToReminder = { navControler.navigate(ReminderScreen) }
            )
        }
        composable<FriendScreen> {
            FriendScreen(
                navegateToNewFriend = { navControler.navigate(NewFriendScreen) },
                navegateToProfile = { navControler.navigate(ProfileScreen) },
                navegateToReminder = { navControler.navigate(ReminderScreen) }
            )
        }
        composable<CategoryScreen> {
            CategoryScreen(
                navegateToNewCategory = { navControler.navigate(NewCategoryScreen) },
                navegateToProfile = { navControler.navigate(ProfileScreen) },
                navegateToCategory = { navControler.navigate(CategoryScreen) },
                navegateToReminder = { navControler.navigate(ReminderScreen) }
            )
        }

    }
}