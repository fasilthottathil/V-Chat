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
import com.vchat.presentation.profile.ProfileViewModel
import com.vchat.presentation.profile.ViewProfile
import com.vchat.presentation.profile.ViewProfileViewModel

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
                editPost = { navHostController.navigate(NavRoute.AddEditPost.route.replace("{postID}", it.id)) },
                addPost = { navHostController.navigate(NavRoute.AddEditPost.route) },
                viewProfile = { navHostController.navigate(NavRoute.ViewProfile.route.replace("{userId}",it)) }
            )
        }
        composable(BottomNavRoute.Profile.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            profileViewModel.getUser()
            profileViewModel.getPosts()
            Profile(
                error = profileViewModel.error,
                loading = profileViewModel.loading,
                resetError = { profileViewModel.resetError() },
                pagingItems = profileViewModel.posts.collectAsLazyPagingItems(),
                userID = profileViewModel.userID,
                user = profileViewModel.user.collectAsStateWithLifecycle(),
                deletePost = { profileViewModel.deletePost(it) },
                editPost = { navHostController.navigate(NavRoute.AddEditPost.route.replace("{postID}", it.id)) },
                editProfile = {  }
            )
        }
        composable(
            NavRoute.AddEditPost.route,
            arguments = listOf(navArgument("postID") { type = NavType.StringType })
        ) {
            val addEditPostViewModel: AddEditPostViewModel = hiltViewModel()
            addEditPostViewModel.getPostById(it.arguments?.getString("postID"))
            AddEditPost(
                post = addEditPostViewModel.post.collectAsStateWithLifecycle(),
                error = addEditPostViewModel.error,
                loading = addEditPostViewModel.loading,
                resetError = { addEditPostViewModel.resetError() },
                onProductAddOrUpdated = addEditPostViewModel.onProductAddOrUpdated,
                onClickDone = { postEntity, uri ->
                    addEditPostViewModel.addOrEditPost(postEntity, uri)
                }
            ) { navHostController.popBackStack() }
        }
        composable(
            NavRoute.ViewProfile.route,
            arguments = listOf(navArgument("userId"){ type = NavType.StringType })
        ) { backStackEntry ->
            val viewProfileViewModel: ViewProfileViewModel = hiltViewModel()
            viewProfileViewModel.getUser(backStackEntry.arguments?.getString("userId"))
            viewProfileViewModel.getPosts(backStackEntry.arguments?.getString("userId"))
            ViewProfile(
                error = viewProfileViewModel.error,
                loading = viewProfileViewModel.loading,
                resetError = { viewProfileViewModel.resetError() },
                pagingItems = viewProfileViewModel.posts.collectAsLazyPagingItems(),
                userID = viewProfileViewModel.userID,
                user = viewProfileViewModel.user.collectAsStateWithLifecycle(),
                deletePost = { viewProfileViewModel.deletePost(it) },
                editPost = { navHostController.navigate(NavRoute.AddEditPost.route.replace("{postID}", it.id)) },
                startChat = {  },
                editProfile = { }
            )
        }
    }
}
