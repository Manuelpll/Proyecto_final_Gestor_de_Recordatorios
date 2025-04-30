package com.example.proyecto_gestion_de_recordatorios.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.proyecto_gestion_de_recordatorios.register.RegisterScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavegationWrapper(navControler: NavHostController, auth: FirebaseAuth) {
    val navController = navControler
    NavHost(navController = navController, startDestination = InitialScreen) {

        composable<InitialScreen> {
        InitialScreen(
                navegateToRegister={navController.navigate(RegisterScreen)},
                navegateToLogin= { navController.navigate(LoginScreen)}
            )
        }

        composable<RegisterScreen>{
         RegisterScreen(
             navegateToLogin= {navController.navigate(LoginScreen)},
             auth
         )
        }
        composable<LoginScreen>{
         val loginViewModel = remember { LoginViewModel(auth) }

         LoginScreen(
             navegateToHome={navController.navigate(HomeScreen)},
             navegateToRegister={navController.navigate(RegisterScreen)},
             viewModel = loginViewModel
         )
        }
        composable<HomeScreen>{
         HomeScreen(
                navegateToProfile= {navController.navigate(ProfileScreen)},
                navegateToReminder={navController.navigate(ReminderScreen)},
                navegateToFriends= {navController.navigate(FriendScreen)},
                navegateToCategory= {navController.navigate(CategoryScreen)},
                navegateToSelectedReminder= {navController.navigate(SelectedReminderScreen)}
            )
        }
        composable<ProfileScreen>{
            ProfileScreen(
                navegateToInitial= {navController.navigate(InitialScreen)}
            )
        }
        composable<ReminderScreen>{
        ReminderScreen(
            navegateToNewReminder={navController.navigate(NewReminderScreen)},
            navegateToSelectedReminder= {navController.navigate(SelectedReminderScreen)},
            navegateToProfile= {navController.navigate(ProfileScreen)}
         )
        }

        composable<SelectedReminderScreen>{
            SelectedReminderScreen(
                navegateToProfile={navController.navigate(ProfileScreen)},
                navegateToCategory={navController.navigate(CategoryScreen)},
                navegateToFriend={navController.navigate(FriendScreen)}
            )
        }
        composable<NewReminderScreen>{
            NewReminderScreen (
            navegateToReminder={navController.navigate(ReminderScreen)}
            )
        }
        composable<FriendScreen>{
            FriendScreen(
                navegateToNewFriend={navController.navigate(NewFriendScreen)},
                navegateToProfile= {navController.navigate(ProfileScreen)},
                navegateToReminder={navController.navigate(ReminderScreen)}
            )
        }
        composable<CategoryScreen>{
            CategoryScreen(
                navegateToNewCategory={navController.navigate(NewCategoryScreen)},
                navegateToProfile= {navController.navigate(ProfileScreen)},
                navegateToCategory={navController.navigate(CategoryScreen)},
                navegateToReminder={navController.navigate(ReminderScreen)}
            )
        }

    }
}