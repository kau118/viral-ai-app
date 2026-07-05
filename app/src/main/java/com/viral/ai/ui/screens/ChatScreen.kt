package com.viral.ai.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.viral.ai.model.ChatMessage
import com.viral.ai.ui.components.*
import com.viral.ai.ui.theme.*
import com.viral.ai.util.BitmapUtil
import com.viral.ai.util.ShareHelper
import com.viral.ai.util.VoiceHelper
import com.viral.ai.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, viewModel: ChatViewModel = viewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val listState = rememberLazyListState()
    
    var inputText by remember { mutableStateOf("") }
    val messages by viewModel.messages.collectAsState()
    val chats by viewModel.chats.collectAsState()
    val followUps by viewModel.followUps.collectAsState()
    val isAiLoading = viewModel.isAiLoading
    val streamingText = viewModel.streamingText
    
    val voiceHelper = remember { VoiceHelper(context) }
    val clipboardManager = LocalClipboardManager.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            BitmapUtil.uriToBitmap(context, it)?.let { bitmap ->
                viewModel.addSelectedBitmap(bitmap)
            }
        }
    }

    LaunchedEffect(messages.size, streamingText) {
        if (messages.isNotEmpty() || streamingText.isNotEmpty()) {
            listState.animateScrollToItem(
                if (streamingText.isNotEmpty()) messages.size else (messages.size - 1).coerceAtLeast(0)
            )
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = SurfaceDark,
                drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
                modifier = Modifier.width(320.dp)
            ) {
                ChatHistoryDrawerContent(
                    chats = chats,
                    currentChatId = viewModel.currentChatId,
                    onChatSelect = { id ->
                        viewModel.selectChat(id)
                        scope.launch { drawerState.close() }
                    },
                    onNewChat = {
                        viewModel.createNewChat()
                        scope.launch { drawerState.close() }
                    },
                    onDeleteChat = { viewModel.deleteChat(it) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                ChatTopBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNewChat = { viewModel.createNewChat() },
                    isThinking = isAiLoading && streamingText.isEmpty()
                )
            },
            containerColor = BackgroundDark
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                
                // Main Chat Area
                Box(modifier = Modifier.weight(1f)) {
                    if (messages.isEmpty() && !isAiLoading && streamingText.isEmpty()) {
                        EmptyChatView()
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            contentPadding = PaddingValues(vertical = 24.dp)
                        ) {
                            items(messages) { message ->
                                PremiumChatBubble(
                                    message = message,
                                    onCopy = { clipboardManager.setText(AnnotatedString(it)) },
                                    onShare = { ShareHelper.shareText(context, it) },
                                    onSpeak = { voiceHelper.speak(it) }
                                )
                            }
                            
                            if (streamingText.isNotEmpty()) {
                                item {
                                    PremiumChatBubble(
                                        message = ChatMessage(text = streamingText, isUser = false),
                                        onCopy = { clipboardManager.setText(AnnotatedString(it)) },
                                        onShare = { ShareHelper.shareText(context, it) },
                                        onSpeak = { voiceHelper.speak(it) }
                                    )
                                }
                            }
                            
                            if (isAiLoading && streamingText.isEmpty()) {
                                item {
                                    ThinkingIndicatorBox()
                                }
                            }
                        }
                    }
                }

                // Interaction Area
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, BackgroundDark.copy(alpha = 0.8f), BackgroundDark)
                            )
                        )
                ) {
                    // Vision Selected Images
                    AnimatedVisibility(visible = viewModel.selectedBitmaps.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(viewModel.selectedBitmaps) { bitmap ->
                                Box {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    IconButton(
                                        onClick = { viewModel.removeSelectedBitmap(bitmap) },
                                        modifier = Modifier.size(24.dp).align(Alignment.TopEnd).background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    ) {
                                        Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Smart Suggestions
                    AnimatedVisibility(
                        visible = followUps.isNotEmpty() && !isAiLoading,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(followUps) { text ->
                                SuggestionChip(text = text) {
                                    viewModel.sendMessage(text)
                                }
                            }
                        }
                    }

                    // Input Box
                    ChatInputArea(
                        text = inputText,
                        onTextChange = { inputText = it },
                        onSend = {
                            if (inputText.isNotBlank() || viewModel.selectedBitmaps.isNotEmpty()) {
                                viewModel.sendMessage(inputText)
                                inputText = ""
                            }
                        },
                        onVoiceClick = {
                            voiceHelper.startListening { voiceText ->
                                viewModel.sendMessage(voiceText)
                            }
                        },
                        onImageClick = {
                            launcher.launch("image/*")
                        },
                        isGenerating = isAiLoading,
                        onStop = { viewModel.stopGeneration() },
                        canSend = inputText.isNotBlank() || viewModel.selectedBitmaps.isNotEmpty()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    onMenuClick: () -> Unit,
    onNewChat: () -> Unit,
    isThinking: Boolean
) {
    TopAppBar(
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Paavani AI",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                )
                if (isThinking) {
                    Text(
                        "Analyzing...",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryPurple
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, "History", tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = onNewChat) {
                Icon(Icons.Default.AddCircleOutline, "New Chat", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundDark,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun ChatHistoryDrawerContent(
    chats: List<com.viral.ai.model.Chat>,
    currentChatId: String?,
    onChatSelect: (String) -> Unit,
    onNewChat: () -> Unit,
    onDeleteChat: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(
            "Recent Intelligence",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
            color = Color.White
        )
        
        Spacer(Modifier.height(24.dp))
        
        GradientButton(
            text = "New Session",
            icon = Icons.Default.Add,
            onClick = onNewChat
        )
        
        Spacer(Modifier.height(24.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(chats) { chat ->
                val isSelected = chat.id == currentChatId
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) PrimaryPurple.copy(alpha = 0.15f) else Color.Transparent)
                        .clickable { onChatSelect(chat.id) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (chat.isPinned) Icons.Default.PushPin else Icons.AutoMirrored.Filled.Chat,
                        null,
                        tint = if (isSelected) PrimaryPurple else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(
                        chat.title,
                        modifier = Modifier.weight(1f),
                        color = if (isSelected) Color.White else TextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1
                    )
                    if (isSelected) {
                        IconButton(onClick = { onDeleteChat(chat.id) }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.DeleteOutline, null, tint = ErrorRed, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumChatBubble(
    message: ChatMessage,
    onCopy: (String) -> Unit,
    onShare: (String) -> Unit,
    onSpeak: (String) -> Unit
) {
    val isUser = message.isUser
    val alignment = if (isUser) Alignment.End else Alignment.Start
    
    val bubbleColor = if (isUser) PrimaryPurple else CardBackground
    val shape = if (isUser) {
        RoundedCornerShape(24.dp, 24.dp, 4.dp, 24.dp)
    } else {
        RoundedCornerShape(24.dp, 24.dp, 24.dp, 4.dp)
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        if (!isUser) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)) {
                Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Brush.linearGradient(PremiumGradient)))
                Spacer(Modifier.width(8.dp))
                Text("PAAVANI", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp), color = PrimaryPurple)
            }
        }

        Surface(
            color = bubbleColor,
            shape = shape,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (isUser) {
                    Text(message.text, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                } else {
                    MarkdownText(text = message.text)
                }
                
                if (!isUser && message.text.length > 2) {
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(
                            Icons.Default.ContentCopy, null, 
                            tint = TextMuted, 
                            modifier = Modifier.size(16.dp).clickable { onCopy(message.text) }
                        )
                        Icon(
                            Icons.Default.VolumeUp, null, 
                            tint = TextMuted, 
                            modifier = Modifier.size(16.dp).clickable { onSpeak(message.text) }
                        )
                        Icon(
                            Icons.Default.Share, null, 
                            tint = TextMuted, 
                            modifier = Modifier.size(16.dp).clickable { onShare(message.text) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatInputArea(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onVoiceClick: () -> Unit,
    onImageClick: () -> Unit,
    isGenerating: Boolean,
    onStop: () -> Unit,
    canSend: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(SurfaceDark, RoundedCornerShape(28.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onImageClick) {
                Icon(Icons.Default.AddPhotoAlternate, null, tint = PrimaryPurple)
            }

            IconButton(onClick = onVoiceClick) {
                Icon(Icons.Default.Mic, null, tint = PrimaryPurple)
            }
            
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("Consult with Paavani...", color = TextMuted) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = PrimaryPurple,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                maxLines = 5
            )

            if (isGenerating) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { onStop() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Stop, null, tint = Color.Black, modifier = Modifier.size(20.dp))
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (canSend) PrimaryPurple else TextMuted)
                        .clickable(enabled = canSend) { onSend() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun EmptyChatView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(PrimaryPurple.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AutoAwesome, null, tint = PrimaryPurple, modifier = Modifier.size(48.dp))
        }
        Spacer(Modifier.height(32.dp))
        Text(
            "Paavani AI",
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Black),
            color = Color.White
        )
        Text(
            "Your Personal Growth Partner",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )
        
        Spacer(Modifier.height(48.dp))
        
        Text("Try asking about:", color = TextMuted, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(16.dp))
        
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SuggestionChip("Scale my clothing brand?") {}
            SuggestionChip("Plan a 3-day trip to Goa") {}
            SuggestionChip("Fix my Kotlin coroutine bug") {}
        }
    }
}

@Composable
fun ThinkingIndicatorBox() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
        Surface(
            color = CardBackground,
            shape = RoundedCornerShape(24.dp, 24.dp, 24.dp, 4.dp)
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                ThinkingIndicatorDots()
            }
        }
    }
}

@Composable
fun SuggestionChip(text: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = CardBackground,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
