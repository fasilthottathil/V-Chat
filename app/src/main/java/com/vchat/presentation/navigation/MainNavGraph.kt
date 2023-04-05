package com.vchat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.vchat.presentation.chats.Chats
import com.vchat.presentation.chats.ChatsViewModel
import com.vchat.presentation.explore.AddEditPost
import com.vchat.presentation.explore.AddEditPostViewModel
import com.vchat.presentation.explore.Explore
import com.vchat.presentation.explore.ExploreViewModel
import com.vchat.presentation.profile.Profile

/**
 * Created by Fasil on 19/03/23.
 */
@Composable
fun MainNavGraph(navHostController: NavHostController, modifier: Modifier) {
    NavHost(navController = navHostController, startDestination = BottomNavRoute.Chats.route) {
        composable(BottomNavRoute.Chats.route) {
            val viewModel: ChatsViewModel = hiltViewModel()
            Chats(
                modifier,
                viewModel.user.collectAsStateWithLifecycle(),
                viewModel.chats.collectAsStateWithLifecycle()
            )
        }
        composable(BottomNavRoute.Explore.route) {
            val exploreViewModel: ExploreViewModel = hiltViewModel()
            Explore(
                modifier = modifier,
                error = exploreViewModel.error,
                loading = exploreViewModel.loading,
                resetError = { exploreViewModel.resetError() },
                pagingItems = exploreViewModel.postPaginated.collectAsLazyPagingItems(),
                userID = exploreViewModel.userID,
                searchPost = { exploreViewModel.search(it) },
                deletePost = { exploreViewModel.deletePost(it) },
                addPost = { navHostController.navigate(NavRoute.AddEditPost.route) }
            )
        }
        composable(BottomNavRoute.Profile.route) {
            Profile()
        }
        composable(
            NavRoute.AddEditPost.route,
            arguments = listOf(navArgument("postID") { type = NavType.StringType })
        ) {
            val addEditPostViewModel: AddEditPostViewModel = hiltViewModel()
            addEditPostViewModel.getPostById(it.arguments?.getString("postID"))
            AddEditPost(
                post = addEditPostViewModel.post.collectAsStateWithLifecycle()
            ) { navHostController.popBackStack() }
        }
    }
}
