package com.example.citymet.ui.feature

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.citymet.R
import com.example.citymet.data.model.WeatherInfo
import com.example.citymet.extensions.withoutAccent
import com.example.citymet.ui.theme.BlueDay
import com.example.citymet.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherRoute(
    viewModel: WeatherViewModel = viewModel()
) {
    val weatherUiState by viewModel.weatherUiState.collectAsStateWithLifecycle()

    PermissionRequester(
        onGranted = {
            viewModel.loadInitialData()
        }
    )

    WeatherScreen(
        weatherUiState = weatherUiState,
        onCitySelected = viewModel::getLatLongCity
    )
}

@Composable
fun WeatherScreen(
    weatherUiState: WeatherUiState?,
    onCitySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (weatherUiState == null) {
        WeatherLoadingState(modifier = modifier)
        return
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = if (weatherUiState.weatherInfo?.isDay == true) BlueDay else Color.DarkGray
    ) {
        with(weatherUiState) {
            when {
                isLoading -> WeatherLoadingState()
                error != null -> WeatherErrorState(error.ifEmpty { stringResource(R.string.error) })
                weatherInfo != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        WeatherSearchBar(
                            cities = cities,
                            onCitySelected = onCitySelected,
                            selectedCity = selectedCity,
                            modifier = Modifier.padding(
                                top = 48.dp,
                                start = 16.dp,
                                end = 16.dp
                            )
                        )

                        WeatherContent(
                            weatherInfo = weatherInfo,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherSearchBar(
    cities: List<String>,
    onCitySelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedCity: String
) {
    DropdownSearch(
        items = cities,
        onItemSelected = onCitySelected,
        selectedCity = selectedCity,
        label = "Pesquise pela cidade",
        placeholder = "Digite para buscar...",
        modifier = modifier
    )
}

@Composable
private fun WeatherContent(
    weatherInfo: WeatherInfo,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherLocationInfo(
            locationName = weatherInfo.locationName,
            dayOfWeek = weatherInfo.dayOffWeek
        )

        Spacer(modifier = Modifier.height(32.dp))

        WeatherConditionInfo(
            conditionIcon = weatherInfo.conditionIcon,
            condition = weatherInfo.condition
        )

        Spacer(modifier = Modifier.height(32.dp))

        WeatherTemperature(
            temperature = weatherInfo.temperature
        )
    }
}

@Composable
private fun WeatherLocationInfo(
    locationName: String,
    dayOfWeek: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = locationName,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

@Composable
private fun WeatherConditionInfo(
    conditionIcon: String,
    condition: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherIcon(iconName = conditionIcon)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = condition,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}

@Composable
private fun WeatherIcon(
    iconName: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val iconDrawableResId = remember(iconName) {
        context.resources.getIdentifier(
            "weather_$iconName",
            "drawable",
            context.packageName
        )
    }

    if (iconDrawableResId != 0) {
        Image(
            painter = painterResource(id = iconDrawableResId),
            contentDescription = iconName,
            modifier = modifier
        )
    } else {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = iconName,
            tint = Color.White,
            modifier = modifier.size(64.dp)
        )
    }
}

@Composable
private fun WeatherTemperature(
    temperature: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "$temperature°",
        style = MaterialTheme.typography.displayLarge,
        color = Color.White,
        modifier = modifier
    )
}

@Composable
private fun WeatherLoadingState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun DropdownSearch(
    items: List<String>,
    onItemSelected: (String) -> Unit,
    selectedCity: String,
    modifier: Modifier = Modifier,
    label: String = stringResource(R.string.select),
    placeholder: String = stringResource(R.string.type_to_search)
) {
    var inputText by rememberSaveable { mutableStateOf(selectedCity) }

    var expanded by remember { mutableStateOf(false) }

    val filteredItems by remember(items, inputText) {
        derivedStateOf {
            if (inputText.isEmpty()) {
                items
            } else {
                items.filter {
                    it.withoutAccent().contains(
                        inputText.withoutAccent(),
                        ignoreCase = true
                    )
                }
            }
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { text ->
                inputText = text
                expanded = text.isNotEmpty()
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            trailingIcon = {
                TrailingIconButton(
                    expanded = expanded,
                    hasText = inputText.isNotEmpty(),
                    onClick = {
                        if (inputText.isEmpty()) {
                            expanded = !expanded
                        } else {
                            inputText = ""
                            expanded = false
                            focusManager.clearFocus()
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused && inputText.isEmpty()) {
                        expanded = true
                    }
                },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }
            )
        )

        AnimatedVisibility(
            visible = expanded && filteredItems.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            DropdownList(
                items = filteredItems,
                onItemClick = { item ->
                    inputText = item
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onItemSelected(item)
                    expanded = false
                }
            )
        }
    }
}

@Composable
private fun TrailingIconButton(
    expanded: Boolean,
    hasText: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = when {
                hasText -> Icons.Default.Close
                expanded -> Icons.Default.KeyboardArrowUp
                else -> Icons.Default.KeyboardArrowDown
            },
            contentDescription = when {
                hasText -> stringResource(R.string.clean)
                expanded -> stringResource(R.string.close)
                else -> stringResource(R.string.open)
            }
        )
    }
}

@Composable
private fun DropdownList(
    items: List<String>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
    ) {
        LazyColumn {
            items(
                items = items,
                key = { it }
            ) { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = { onItemClick(item) },
                    modifier = Modifier.fillMaxWidth()
                )

                if (item != items.last()) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun WeatherErrorState(
    error: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "Erro ao carregar dados",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun PermissionRequester(onGranted: () -> Unit) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        onGranted()
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            onGranted()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WeatherScreenPreview() {
    MaterialTheme {
        WeatherScreen(
            weatherUiState = WeatherUiState(
                weatherInfo = WeatherInfo(
                    locationName = "São Paulo",
                    dayOffWeek = "Segunda-feira",
                    conditionIcon = "sunny",
                    condition = "Ensolarado",
                    temperature = 25
                )
            ),
            onCitySelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AutocompleteDropdownPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DropdownSearch(
                items = listOf(
                    "São Paulo",
                    "Rio de Janeiro",
                    "Belo Horizonte",
                    "Salvador",
                    "Brasília"
                ),
                onItemSelected = {},
                selectedCity = "",
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}