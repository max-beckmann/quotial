package com.max.quotial.ui.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.max.quotial.data.model.Post
import com.max.quotial.ui.viewmodel.PostViewModel
import com.max.quotial.util.toDateTimeString

enum class VotingState {
    UPVOTED,
    DOWNVOTED,
    NOT_VOTED
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostCard(post: Post, postViewModel: PostViewModel) {
    var rating by rememberSaveable { mutableIntStateOf(0) }
    var votingState by rememberSaveable { mutableStateOf(VotingState.NOT_VOTED) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.username,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = post.timestamp.toDateTimeString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = buildAnnotatedString {
                    append("\"")
                    append(post.quote.content)
                    append("\"")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.outline)) {
                        append(" - ")
                        append(post.quote.source)
                    }
                },
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(rating.toString())

                OutlinedButton(
                    onClick = {
                        when (votingState) {
                            VotingState.UPVOTED -> {
                                postViewModel.vote(post.id, 0)
                                votingState = VotingState.NOT_VOTED
                            }

                            else -> {
                                postViewModel.vote(post.id, 1)
                                votingState = VotingState.UPVOTED
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (votingState == VotingState.UPVOTED) Color.Green else Color.Transparent
                    )
                ) {
                    Text("Upvote")
                }

                OutlinedButton(
                    onClick = {
                        when (votingState) {
                            VotingState.DOWNVOTED -> {
                                postViewModel.vote(post.id, 0)
                                votingState = VotingState.NOT_VOTED
                            }

                            else -> {
                                postViewModel.vote(post.id, -1)
                                votingState = VotingState.DOWNVOTED
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (votingState == VotingState.DOWNVOTED) Color.Red else Color.Transparent
                    )
                ) {
                    Text("Downvote")
                }
            }
        }
    }
}