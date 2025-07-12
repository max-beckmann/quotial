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
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.max.quotial.data.model.Post
import com.max.quotial.data.model.VoteType
import com.max.quotial.util.toDateTimeString

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PostCard(
    post: Post,
    userVote: VoteType,
    isVoting: Boolean,
    onVote: (VoteType) -> Unit,
) {
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

            Text(
                text = "in ${post.groupName ?: "General"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

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

            VoteSection(
                post,
                userVote,
                isVoting,
                onVote,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}