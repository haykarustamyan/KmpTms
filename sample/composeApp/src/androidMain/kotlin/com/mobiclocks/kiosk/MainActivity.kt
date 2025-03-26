package com.mobiclocks.kiosk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.painterResource
import kiosk.sharedresources.SharedRes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
//            Image(
//                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.3f),
//                painter = painterResource(SharedRes.images.moko_logo),
//                contentDescription = null,
//            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}


@Composable
fun stringResource(id: StringResource, vararg args: Any): String {
    return Strings(LocalContext.current).get(id, args.toList())
}
