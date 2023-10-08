package com.camihruiz24.woof_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.twotone.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.camihruiz24.woof_app.data.Dog
import com.camihruiz24.woof_app.data.dogs
import com.camihruiz24.woof_app.ui.theme.WoofAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WoofAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WoofApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Row(modifier = Modifier, verticalAlignment = Alignment.Top) {
                Image(
                    painter = painterResource(id = R.drawable.ic_woof_logo),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.image_size))
                        .padding(end = dimensionResource(id = R.dimen.padding_medium))
                )
                Text(
                    stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.displayLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.menu)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        }
    )
}

/**
 * Composable that displays an app bar and a list of dogs.
 */
@Composable
fun WoofApp() {
    Scaffold(
        modifier = Modifier,
        topBar = { WoofTopAppBar() },
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            //        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
            //        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            items(dogs, key = { dog -> dog.name }) {
                DogItem(dog = it)
            }
        }
    }
}

/**
 * Composable that displays a list item containing a dog icon and their information.
 *
 * @param dog contains the data that populates the list item
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogItem(
    dog: Dog,
    modifier: Modifier = Modifier
) {

    var expanded: Boolean by remember { mutableStateOf(false) }
    val itemColor by animateColorAsState(
        targetValue = when (expanded) {
            true -> MaterialTheme.colorScheme.tertiaryContainer
            false -> MaterialTheme.colorScheme.primaryContainer
        },
        label = stringResource(R.string.card_s_color_animation)
    )

    Card(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small)),
        shape = MaterialTheme.shapes.medium, // Esto es innecesario porque Card lo toma por defecto
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow,
                    )
                )
                .background(color = itemColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                DogIcon(dog.imageResourceId)
                DogInformation(dog.name, dog.age)
                Spacer(modifier = Modifier.weight(1f))
                MoreDogInfoButton(expanded = expanded, onClick = { expanded = !expanded })
            }
            if (expanded)
                DogHobby(
                    dogHobbies = dog.hobbies,
                    Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_medium)
                        )
                        .padding(
                            top = dimensionResource(id = R.dimen.padding_small),
                            bottom = dimensionResource(id = R.dimen.padding_medium)
                        )
                )
        }
    }
}

@Composable
fun MoreDogInfoButton(expanded: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = when (expanded) {
                true -> Icons.Filled.ExpandMore
                false -> Icons.TwoTone.KeyboardArrowDown
            },
            contentDescription = stringResource(id = R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Composable that displays a photo of a dog.
 *
 * @param dogIcon is the resource ID for the image of the dog
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogIcon(
    @DrawableRes dogIcon: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(dogIcon),

        contentScale = ContentScale.Crop,

        // Content Description is not needed here - image is decorative, and setting a null content
        // description allows accessibility services to skip this element during navigation.

        contentDescription = null,

        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small)

    )
}

/**
 * Composable that displays a dog's name and age.
 *
 * @param dogName is the resource ID for the string of the dog's name
 * @param dogAge is the Int that represents the dog's age
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogInformation(
    @StringRes dogName: Int,
    dogAge: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // displayMedium as the style for the dogName because it is a short, important piece of information.
        Text(
            text = stringResource(dogName),
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
            style = MaterialTheme.typography.displayMedium
        )
        // bodyLarge as the style for the dogAge because it works well with smaller text sizes
        Text(
            text = stringResource(R.string.years_old, dogAge),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
fun DogHobby(
    @StringRes dogHobbies: Int,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Text(
            text = stringResource(id = R.string.about),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(id = dogHobbies),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

/**
 * Composable that displays what the UI of the app looks like in light theme in the design tab.
 */
@Preview(name = "Light theme", showBackground = true)
@Composable
fun WoofLightThemePreview() {
    WoofAppTheme(darkTheme = false) {
        WoofApp()
    }
}

@Preview(name = "Dark theme", showBackground = true)
@Composable
fun WoofDarkThemePreview() {
    WoofAppTheme(darkTheme = true) {
        WoofApp()
    }
}
