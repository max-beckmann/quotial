package com.max.quotial.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
    isVoting: Boolean,
    onVote: (VoteType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(post.score.toString())

        IconButton(
            onClick = {
                onVote(if (userVote == VoteType.UPVOTE) VoteType.NONE else VoteType.UPVOTE)
            },
            enabled = !isVoting,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Upvote",
                tint = if (userVote == VoteType.UPVOTE) Color.Green else MaterialTheme.colorScheme.surface
            )
        }

        IconButton(
            onClick = {
                onVote(if (userVote == VoteType.DOWNVOTE) VoteType.NONE else VoteType.DOWNVOTE)
            },
            enabled = !isVoting,
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Downvote",
                tint = if (userVote == VoteType.DOWNVOTE) Color.Red else MaterialTheme.colorScheme.surface
            )
        }
    }

}