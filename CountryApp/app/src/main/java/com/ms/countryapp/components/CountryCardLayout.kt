package com.ms.countryapp.components

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.ms.countryapp.CountryApp.Companion.APP_TAG
import com.ms.countryapp.data.Country
import com.ms.countryapp.viewModel.CountryViewModel
import kotlin.plus

@Composable
fun CountryCardLayout(country: Country,
                      showDeleteDialog: MutableState<Boolean>,
                      showUpdateDialog: MutableState<Boolean>,
                      viewModel: CountryViewModel) {

    ConstraintLayout(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(5.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        Log.i(APP_TAG,"Double tap detected")
                        showUpdateDialog.value = true
                        viewModel.selectedCountryForUpdation.value = country
                    },

//                    onTap = {
//                        Log.i(APP_TAG,"Tap detected")
//                    },
//
//                    onPress = {
//                        Log.i(APP_TAG,"onPress detected")
//                    },

                    onLongPress = {
                        Log.i(APP_TAG,"Long press detected")
                        showDeleteDialog.value = true
                        viewModel.selectedCountryForDeletion.value = country
                    }
                )
            }
    ) {
        val (flag, name, capital, officialName, region, subregion, currencySymbol, currencyName, mobileCode, tld) = createRefs()
        country.let {
            AsyncImage(model = it.flags?.png,
                contentScale = ContentScale.Crop,
                contentDescription = it.flag,
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(70.dp)
                    .padding(2.dp)
                    .constrainAs(flag) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                )
        }

        country.name?.common?.let {
            Text(
                text = it,
                modifier = Modifier
                    .padding(top = 14.dp, start = 2.dp, end = 2.dp)
                    .fillMaxWidth(0.3f)
                    .constrainAs(name) {
                        top.linkTo(flag.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(flag.end)
                    },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        country.capital?.firstOrNull()?.let {
            Text(text = it,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp, start = 2.dp, end = 2.dp)
                    .constrainAs(capital) {
                        top.linkTo(name.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(flag.end)
                }
            )
        }

        country.name?.official?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(officialName) {
                        top.linkTo(parent.top)
                        start.linkTo(flag.end)
                        end.linkTo(parent.end)
                    }
                    .padding(2.dp)
                    .fillMaxWidth(0.65f)
            )
        }

        country.region?.let {
            Text(it,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(region) {
                        start.linkTo(flag.end)
                        end.linkTo(parent.end)
                        top.linkTo(officialName.bottom)
                    }
                    .padding(2.dp)
                    .fillMaxWidth(0.8f)
                )
        }

        country.subregion?.let {
            Text(text = it,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(subregion) {
                        start.linkTo(officialName.start)
                        top.linkTo(region.bottom)
                        end.linkTo(officialName.end)
                    }
                    .padding(2.dp)
                    .fillMaxWidth(0.8f))
        }

        country.currencies?.entries?.firstOrNull()?.let{
            CircularText(text = it.value.symbol.toString(),
                modifier = Modifier
                    .padding(bottom = 10.dp)
                    .constrainAs(currencySymbol) {
                        start.linkTo(flag.end, margin = 30.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                    })
        }

        country.currencies?.entries?.firstOrNull()?.let{
            Text(text = it.value.name.toString(),
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp)
                    .constrainAs(currencyName) {
                        top.linkTo(subregion.bottom)
                        start.linkTo(currencySymbol.end, margin = 12.dp)
                        bottom.linkTo(parent.bottom, margin = 5.dp)
                        end.linkTo(mobileCode.start)
                    }
                    .width(100.dp),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        country.idd?.let {
            Text(
                text = it.root+""+it.suffixes?.get(0),
                modifier = Modifier
                    .constrainAs(mobileCode) {
                        top.linkTo(subregion.bottom)
                        end.linkTo(parent.end)
                    }
                    .width(50.dp)
            )
        }

        country.tld?.firstOrNull()?.let {
            Text(
                text = it,
                modifier = Modifier
                    .constrainAs(tld) {
                        top.linkTo(mobileCode.bottom)
                        end.linkTo(parent.end)
                    }
                    .width(50.dp)
            )
        }
    }

}