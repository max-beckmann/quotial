package com.max.quotial.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.max.quotial.ui.component.PostList
import com.max.quotial.ui.viewmodel.PostViewModel
import com.max.quotial.ui.viewmodel.QuoteViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainFeedScreen(
    onGroupClick: (String) -> Unit,
    postViewModel: PostViewModel = hiltViewModel(),
    quoteViewModel: QuoteViewModel = hiltViewModel(),
) {
    val posts by postViewModel.postsLiveData.observeAsState(initial = emptyList())
    val userVotes by postViewModel.userVotesLiveData.observeAsState(initial = emptyMap())
    val activeVotePostIds by postViewModel.activeVotePostIds.observeAsState(initial = emptySet())
    val quoteOfTheDay = quoteViewModel.quoteOfTheDay

    LaunchedEffect(Unit) {
        quoteViewModel.loadQuoteOfTheDay()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (quoteOfTheDay != null) {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Quote of the day",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = buildAnnotatedString {
                            append("\"")
                            append(quoteOfTheDay.q)
                            append("\"")
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.outline)) {
                                append(" - ")
                                append(quoteOfTheDay.a)
                            }
                        },
                    )
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        PostList(
            posts,
            userVotes,
            activeVotePostIds,
            onVote = { postId, voteType ->
                postViewModel.vote(postId, voteType)
            },
            onGroupClick,
            onDelete = { id ->
                postViewModel.deletePost(id)
            },
        )
    }
}