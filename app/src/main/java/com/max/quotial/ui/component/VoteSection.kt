package com.max.quotial.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.max.quotial.data.model.Post
import com.max.quotial.data.model.VoteType
import com.max.quotial.data.model.score

@Composable
fun VoteSection(
    post: Post,
    userVote: VoteType,
    onVote: (VoteType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(post.score.toString())

        OutlinedButton(
            onClick = {
                onVote(if (userVote == VoteType.UPVOTE) VoteType.NONE else VoteType.UPVOTE)
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (userVote == VoteType.UPVOTE) Color.Green else Color.Transparent
            )
        ) {
            Text("Upvote")
        }

        OutlinedButton(
            onClick = {
                onVote(if (userVote == VoteType.DOWNVOTE) VoteType.NONE else VoteType.DOWNVOTE)
            },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (userVote == VoteType.DOWNVOTE) Color.Red else Color.Transparent
            )
        ) {
            Text("Downvote")
        }
    }

}