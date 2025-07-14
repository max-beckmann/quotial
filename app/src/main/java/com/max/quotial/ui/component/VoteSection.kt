package com.max.quotial.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
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
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(post.score.toString())

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        onVote(if (userVote == VoteType.UPVOTE) VoteType.NONE else VoteType.UPVOTE)
                    },
                    enabled = !isVoting,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Upvote",
                        tint = if (userVote == VoteType.UPVOTE) Color.Green else Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = {
                        onVote(if (userVote == VoteType.DOWNVOTE) VoteType.NONE else VoteType.DOWNVOTE)
                    },
                    enabled = !isVoting,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Downvote",
                        tint = if (userVote == VoteType.DOWNVOTE) Color.Red else Color.DarkGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }


}