package ir.ehsan.asmrcubicpager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.ehsan.asmrcubicpager.ui.theme.AsmrCubicPagerTheme
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsmrCubicPagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val pictures = listOf(
                        R.drawable.pic1,
                        R.drawable.pic2,
                        R.drawable.pic3,
                        R.drawable.pic4,
                        R.drawable.pic5,
                        R.drawable.pic1,
                        R.drawable.pic2,
                        R.drawable.pic3,
                        R.drawable.pic4,
                        R.drawable.pic5,
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val horizontalPagerState = rememberPagerState(pageCount = {
                            pictures.count()
                        })
                        val verticalPagerState = rememberPagerState(pageCount = {
                            pictures.count()
                        })
                        HorizontalPager(state = horizontalPagerState,modifier=Modifier.size(250.dp)) {page->
                            Box(
                                modifier = Modifier
                                    .size(250.dp)
                                    .applyCubic(horizontalPagerState, page)
                            ){
                                Image(
                                    painter = painterResource(id = pictures[page]),
                                    contentScale = ContentScale.Crop,
                                    modifier=Modifier.fillMaxSize(),
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        VerticalPager(state = verticalPagerState,modifier=Modifier.size(250.dp)) {page->
                            Box(
                                modifier = Modifier
                                    .size(250.dp)
                                    .applyCubic(verticalPagerState, page,horizontal = false)
                            ){
                                Image(
                                    painter = painterResource(id = pictures[page]),
                                    contentScale = ContentScale.Crop,
                                    modifier=Modifier.fillMaxSize(),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
fun Modifier.applyCubic(state:PagerState, page:Int,horizontal:Boolean = true):Modifier{
    return graphicsLayer {
        val pageOffset = state.offsetForPage(page)
        val offScreenRight = pageOffset < 0f
        val def = if (horizontal){
            105f
        }else{
            -90f
        }
        val interpolated = FastOutLinearInEasing.transform(pageOffset.absoluteValue)
        val rotation = (interpolated*if (offScreenRight) def else -def).coerceAtMost(90f)
        if (horizontal){
            rotationY = rotation
        }else{
            rotationX = rotation
        }

        transformOrigin = if (horizontal){
            TransformOrigin(
                pivotFractionX = if (offScreenRight) 0f else 1f,
                pivotFractionY =  .5f
            )
        }else{
            TransformOrigin(
                pivotFractionY = if (offScreenRight) 0f else 1f,
                pivotFractionX =  .5f
            )
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page:Int) = (currentPage-page)+currentPageOffsetFraction

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.startOffsetForPage(page: Int) = offsetForPage(page).coerceAtLeast(0f)

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.endOffsetForPage(page: Int) = offsetForPage(page).coerceAtMost(0f)

