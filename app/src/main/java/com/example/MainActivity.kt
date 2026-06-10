package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    MainNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    var isAuthenticated by remember { mutableStateOf(false) }
    
    AnimatedContent(
        targetState = isAuthenticated,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(400))
        },
        label = "nav_transition"
    ) { authenticated ->
        if (!authenticated) {
            PasscodeLockScreen(onCorrectPassword = { isAuthenticated = true })
        } else {
            SensitivityDashboard(modifier = modifier)
        }
    }
}

@Composable
fun PasscodeLockScreen(onCorrectPassword: () -> Unit) {
    var passwordInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Visual Palette aligned with "High Density" tactile theme
    val bgColor = Color(0xFF1C1B1F)
    val textColor = Color(0xFFE6E1E5)
    val lightPurple = Color(0xFFD0BCFF)
    val cardSurface = Color(0xFF2B2930)
    val borderOutline = Color(0xFF49454F)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Stylized Tactical Header Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(cardSurface)
                    .border(1.dp, borderOutline, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Lock Icon",
                    tint = lightPurple,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PAVIN CHEATS 90%",
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            
            Text(
                text = "Enter tactical decryption passcode",
                color = textColor.copy(alpha = 0.6f),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Password Card Area
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderOutline, RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = {
                            passwordInput = it
                            errorMessage = "" // clear error
                        },
                        label = { Text("Access Password", color = lightPurple) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = lightPurple,
                            unfocusedBorderColor = borderOutline,
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            cursorColor = lightPurple
                        ),
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Lock else Icons.Default.Lock, // fallback standard Icon
                                    contentDescription = "Toggle visibility",
                                    tint = textColor.copy(alpha = 0.7f)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_input_field")
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (passwordInput.equals("pavin", ignoreCase = true)) {
                                onCorrectPassword()
                            } else {
                                errorMessage = "Decryption failed: Incorrect Password."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = lightPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("login_submit_button")
                    ) {
                        Text(
                            text = "UNLOCK WORKSPACE",
                            color = Color(0xFF21005D),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Hint info for the preview session
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(cardSurface.copy(alpha = 0.5f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Hint: Passcode is pavin",
                    color = lightPurple.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

// Phone Support Profile Data structures for "all phone support" feature
data class PhoneProfile(
    val brand: String,
    val model: String,
    val recommendedGeneral: Float,
    val recommendedRedDot: Float,
    val recommended2x: Float,
    val recommended4x: Float,
    val recommendedSniper: Float,
    val recommendedFreeLook: Float,
    val dpiConfig: Int
)

@Composable
fun SensitivityDashboard(modifier: Modifier = Modifier) {
    // List of pre-configured calibration values for "all phone support"
    val phoneProfiles = listOf(
        PhoneProfile("iPhone", "15 Pro Max / 14 Pro", 95f, 92f, 88f, 84f, 75f, 90f, 120),
        PhoneProfile("Samsung", "S24 Ultra / Note Series", 98f, 95f, 90f, 88f, 82f, 95f, 411),
        PhoneProfile("ASUS", "ROG Phone 8 / 7 Ultimate", 100f, 98f, 95f, 92f, 85f, 100f, 480),
        PhoneProfile("Xiaomi/POCO", "F6 Pro / X6 Neo", 92f, 88f, 85f, 82f, 70f, 85f, 392),
        PhoneProfile("OnePlus", "12 / 11R Pro", 94f, 90f, 86f, 84f, 78f, 88f, 420),
        PhoneProfile("Realme/OPPO", "GT Club / Find X", 91f, 86f, 82f, 80f, 68f, 80f, 380),
        PhoneProfile("Infinix/Tecno", "Pova 6 PRO / Camon", 89f, 85f, 80f, 78f, 65f, 75f, 360)
    )

    var selectedProfileIndex by remember { mutableStateOf(0) }
    val activeProfile = phoneProfiles[selectedProfileIndex]

    // Sliders customized values
    var sensitivityGeneral by remember { mutableFloatStateOf(95f) }
    var sensitivityRedDot by remember { mutableFloatStateOf(92f) }
    var sensitivity2x by remember { mutableFloatStateOf(88f) }
    var sensitivity4x by remember { mutableFloatStateOf(84f) }
    var sensitivitySniper by remember { mutableFloatStateOf(75f) }
    var sensitivityFreeLook by remember { mutableFloatStateOf(90f) }

    // Whenever phone profile is changed, update values with recommended profiles to assist users immediately!
    LaunchedEffect(selectedProfileIndex) {
        val current = phoneProfiles[selectedProfileIndex]
        sensitivityGeneral = current.recommendedGeneral
        sensitivityRedDot = current.recommendedRedDot
        sensitivity2x = current.recommended2x
        sensitivity4x = current.recommended4x
        sensitivitySniper = current.recommendedSniper
        sensitivityFreeLook = current.recommendedFreeLook
    }

    var noRecoilSelected by remember { mutableStateOf(false) }
    var autoScopeSync by remember { mutableStateOf(true) }

    // Simulation states
    var isCalibrating by remember { mutableStateOf(false) }
    var calibrationProgress by remember { mutableFloatStateOf(0f) }
    var currentSimulationLog by remember { mutableStateOf("System Idle...") }
    val simulationLogs = remember { mutableStateListOf<String>() }

    // Radar scanning rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "RadarTransition")
    val sweepRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RadarSweeper"
    )

    // Log calculation sequences
    if (isCalibrating) {
        LaunchedEffect(isCalibrating) {
            simulationLogs.clear()
            simulationLogs.add("[SYS] Reading Hardware profile configuration for ${activeProfile.brand} ${activeProfile.model}")
            currentSimulationLog = "Fetching physical device matrix..."
            calibrationProgress = 10f
            delay(1000)

            simulationLogs.add("[DPI] Virtual Screen density bounds matched at: ${activeProfile.dpiConfig} DPI")
            simulationLogs.add("[CALIBRATE] Interpolating General Sensitivity: ${sensitivityGeneral.toInt()}% ratio scale")
            currentSimulationLog = "Aligning General & ADS 2.0D matrices..."
            calibrationProgress = 35f
            delay(1200)

            simulationLogs.add("[ADS] Injecting compensation profiles for scope optics (2x: ${sensitivity2x.toInt()}%, 4x: ${sensitivity4x.toInt()}%)")
            currentSimulationLog = "Dampening viewport velocity scaling..."
            calibrationProgress = 60f
            delay(1100)

            if (noRecoilSelected) {
                simulationLogs.add("[MOD] Loaded dynamic 90% drift alignment coefficient: ENABLED.")
            } else {
                simulationLogs.add("[MOD] Smooth recoil stabilizer offset applied.")
            }
            simulationLogs.add("[OK] System parameters successfully formulated for all phones sport matrix.")
            currentSimulationLog = "Applying checksum validations..."
            calibrationProgress = 85f
            delay(900)

            simulationLogs.add("[SUCCESS] Device calibration parameters deployed securely.")
            currentSimulationLog = "Formulations deployed successfully to local cache."
            calibrationProgress = 100f
        }
    }

    // Interactive Theme Colors (High Density Tactical Dark Vibe)
    val bgColor = Color(0xFF1C1B1F)
    val textColor = Color(0xFFE6E1E5)
    val lightPurple = Color(0xFFD0BCFF)
    val darkPurpleBg = Color(0xFFEADDFF)
    val darkPurpleText = Color(0xFF21005D)
    val cardSurface = Color(0xFF2B2930)
    val borderOutline = Color(0xFF49454F)
    val successColor = Color(0xFF81C784)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Status bar spacer
            Spacer(modifier = Modifier.statusBarsPadding())

            // Top Header App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(borderOutline),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "90",
                            color = lightPurple,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "pavin cheats 90%",
                            color = textColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Status: Device Config Loaded",
                            color = lightPurple,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                // Top status dot indicator
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(successColor.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(successColor)
                        )
                        Text(
                            text = "ACTIVE",
                            color = successColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Scrollable Config Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                // ALL PHONE SPORT Brand / Model configuration swipe Selector
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = darkPurpleBg),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        Text(
                            text = "SUPPORTED DEVICE BRAND (ALL PHONE SPORT)",
                            color = darkPurpleText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        // Row list of supported brands
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            phoneProfiles.forEachIndexed { idx, pf ->
                                val active = idx == selectedProfileIndex
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (active) darkPurpleText else Color.White.copy(alpha = 0.4f))
                                        .clickable { selectedProfileIndex = idx }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = pf.brand,
                                        color = if (active) Color.White else darkPurpleText,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Model: ${activeProfile.model}",
                                    color = darkPurpleText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Base hardware layout reference: ${activeProfile.dpiConfig} DPI scale",
                                    color = darkPurpleText.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(darkPurpleText)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "LOADED",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                // RADAR LIVE SCOPE TARGET PREVIEW CANVAS
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardSurface),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val centerX = size.width / 2
                            val centerY = size.height / 2
                            val center = Offset(centerX, centerY)

                            // Target Outer Radar rings
                            drawCircle(
                                color = lightPurple.copy(alpha = 0.15f),
                                radius = 60.dp.toPx(),
                                style = Stroke(width = 1.dp.toPx())
                            )
                            drawCircle(
                                color = lightPurple.copy(alpha = 0.35f),
                                radius = 40.dp.toPx(),
                                style = Stroke(width = 1.dp.toPx())
                            )
                            drawCircle(
                                color = lightPurple,
                                radius = 15.dp.toPx(),
                                style = Stroke(width = 1.5.dp.toPx())
                            )

                            // Sweep lines from rotating angle state
                            val currentRads = Math.toRadians(sweepRotation.toDouble())
                            val endX = centerX + (60.dp.toPx() * cos(currentRads)).toFloat()
                            val endY = centerY + (60.dp.toPx() * sin(currentRads)).toFloat()

                            drawLine(
                                brush = Brush.linearGradient(
                                    colors = listOf(lightPurple, Color.Transparent),
                                    start = center,
                                    end = Offset(endX, endY)
                                ),
                                start = center,
                                end = Offset(endX, endY),
                                strokeWidth = 2.dp.toPx()
                            )

                            // Crosshair Center Lines
                            drawLine(
                                color = lightPurple.copy(alpha = 0.4f),
                                start = Offset(centerX - 70.dp.toPx(), centerY),
                                end = Offset(centerX - 20.dp.toPx(), centerY),
                                strokeWidth = 1.dp.toPx()
                            )
                            drawLine(
                                color = lightPurple.copy(alpha = 0.4f),
                                start = Offset(centerX + 20.dp.toPx(), centerY),
                                end = Offset(centerX + 70.dp.toPx(), centerY),
                                strokeWidth = 1.dp.toPx()
                            )
                            drawLine(
                                color = lightPurple.copy(alpha = 0.4f),
                                start = Offset(centerX, centerY - 70.dp.toPx()),
                                end = Offset(centerX, centerY - 20.dp.toPx()),
                                strokeWidth = 1.dp.toPx()
                            )
                            drawLine(
                                color = lightPurple.copy(alpha = 0.4f),
                                start = Offset(centerX, centerY + 20.dp.toPx()),
                                end = Offset(centerX, centerY + 70.dp.toPx()),
                                strokeWidth = 1.dp.toPx()
                            )

                            // Live target tracker coordinate offset matching general/scope adjustments
                            val generalOffset = (100f - sensitivityGeneral) / 10f
                            val redDotOffset = (100f - sensitivityRedDot) / 10f
                            drawCircle(
                                color = Color.Red.copy(alpha = 0.8f),
                                radius = 4.dp.toPx(),
                                center = Offset(centerX + generalOffset.dp.toPx(), centerY - redDotOffset.dp.toPx())
                            )
                        }

                        // HUD Labels Overlay
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Text(
                                text = "CALIBRATION SCOPE SCREEN",
                                color = textColor.copy(alpha = 0.4f),
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Text(
                                text = "90% LOCK STABLE",
                                color = successColor.copy(alpha = 0.8f),
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // TACTICAL SENSITIVITY CONFIG SLIDERS CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardSurface),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, borderOutline)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "CUSTOM SENSITIVITY ALIGNMENTS",
                            color = lightPurple,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        // 1. General Sensitivity
                        SensitivitySliderItem(
                            label = "General Sensitivity",
                            value = sensitivityGeneral,
                            onValueChange = { sensitivityGeneral = it },
                            testTagPrefix = "general"
                        )

                        // 2. Red Dot / ADS Sensitivity
                        SensitivitySliderItem(
                            label = "Red Dot / ADS",
                            value = sensitivityRedDot,
                            onValueChange = { sensitivityRedDot = it },
                            testTagPrefix = "red_dot"
                        )

                        // 3. 2x Scope Sensitivity
                        SensitivitySliderItem(
                            label = "2X Scope",
                            value = sensitivity2x,
                            onValueChange = { sensitivity2x = it },
                            testTagPrefix = "scope_2x"
                        )

                        // 4. 4X Scope Sensitivity
                        SensitivitySliderItem(
                            label = "4X Scope",
                            value = sensitivity4x,
                            onValueChange = { sensitivity4x = it },
                            testTagPrefix = "scope_4x"
                        )

                        // 5. Sniper Scope Sensitivity
                        SensitivitySliderItem(
                            label = "Sniper Scope",
                            value = sensitivitySniper,
                            onValueChange = { sensitivitySniper = it },
                            testTagPrefix = "sniper"
                        )

                        // 6. Free Look Sensitivity
                        SensitivitySliderItem(
                            label = "Free Look",
                            value = sensitivityFreeLook,
                            onValueChange = { sensitivityFreeLook = it },
                            testTagPrefix = "free_look"
                        )
                    }
                }

                // ADDITIONAL TOGGLE CHOICES CARD
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardSurface),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, borderOutline)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "OPTIONS & FILTERS",
                            color = lightPurple,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { noRecoilSelected = !noRecoilSelected }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Anti-Stick Recoil Mod", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text(text = "Interpolates steady physical coordinate inputs", color = textColor.copy(alpha = 0.5f), fontSize = 11.sp)
                            }
                            Switch(
                                checked = noRecoilSelected,
                                onCheckedChange = { noRecoilSelected = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = lightPurple,
                                    checkedTrackColor = borderOutline
                                )
                            )
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(borderOutline))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { autoScopeSync = !autoScopeSync }
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Synchronize Scope Alignments", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Text(text = "Maintains proportional zoom ratio calibration", color = textColor.copy(alpha = 0.5f), fontSize = 11.sp)
                            }
                            Switch(
                                checked = autoScopeSync,
                                onCheckedChange = { autoScopeSync = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = lightPurple,
                                    checkedTrackColor = borderOutline
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Bottom Calibration Core Button Action
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bgColor)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { isCalibrating = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("apply_cheats_calc_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = lightPurple)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Optimize settings",
                            tint = darkPurpleText,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "CALIBRATE & INJECT CONFIG",
                            color = darkPurpleText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(textColor.copy(alpha = 0.2f))
                )
            }
        }

        // BENIGN SYSTEM CALIBRATION SIMULATOR DIALOG OVERLAY
        AnimatedVisibility(
            visible = isCalibrating,
            enter = fadeIn(animationSpec = tween(400)) + scaleIn(initialScale = 0.92f),
            exit = fadeOut(animationSpec = tween(400)) + scaleOut(targetScale = 0.92f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor.copy(alpha = 0.98f))
                    .padding(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header Status
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "TACTICAL VECTOR CALIBRATION",
                                    color = lightPurple,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "Simulating math matrices safely on local heap",
                                    color = textColor.copy(alpha = 0.5f),
                                    fontSize = 11.sp
                                )
                            }
                            IconButton(onClick = {
                                isCalibrating = false
                                calibrationProgress = 0f
                                simulationLogs.clear()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close overlay",
                                    tint = textColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress representation
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(cardSurface)
                                .border(1.dp, borderOutline, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (calibrationProgress < 100f) "CALCULATING RATIO ALIGNMENTS..." else "CHECK COMPLETE: ALL PHONES MATCHED",
                                color = if (calibrationProgress < 100f) Color.Yellow else successColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Live system processing logs representation
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(alpha = 0.8f))
                            .border(1.dp, borderOutline, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "CALIBRATION_FORMULATION_CONSOLE //",
                            color = textColor.copy(alpha = 0.4f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            simulationLogs.forEach { rowLog ->
                                Text(
                                    text = rowLog,
                                    color = if (rowLog.startsWith("[SUCCESS]")) successColor else if (rowLog.startsWith("[SYS]")) lightPurple else textColor,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    // Lower control progress
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentSimulationLog,
                                color = textColor.copy(alpha = 0.7f),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "${calibrationProgress.toInt()}%",
                                color = lightPurple,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // Gradient bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(borderOutline)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(calibrationProgress / 100f)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(lightPurple, Color(0xFF6750A4))
                                        )
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                isCalibrating = false
                                calibrationProgress = 0f
                                simulationLogs.clear()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (calibrationProgress >= 100f) successColor else borderOutline,
                                contentColor = if (calibrationProgress >= 100f) Color.Black else textColor
                            )
                        ) {
                            Text(
                                text = if (calibrationProgress >= 100f) "APPLY & DISMISS LOCKOUT" else "MINIMIZE SIMULATION",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SensitivitySliderItem(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    testTagPrefix: String
) {
    val textColor = Color(0xFFE6E1E5)
    val lightPurple = Color(0xFFD0BCFF)
    val borderOutline = Color(0xFF49454F)

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = label,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${value.toInt()}%",
                color = lightPurple,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..100f,
            modifier = Modifier
                .height(34.dp)
                .testTag("${testTagPrefix}_slider"),
            colors = SliderDefaults.colors(
                thumbColor = lightPurple,
                activeTrackColor = lightPurple,
                inactiveTrackColor = borderOutline
            )
        )
    }
}
