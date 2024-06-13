import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.guetteteskilometres.data.repository.EventRepository
import com.example.guetteteskilometres.ui.navigation.NewEvent
import com.example.guetteteskilometres.ui.screens.newevent.NewEventNavigations
import com.example.guetteteskilometres.ui.screens.newevent.NewEventScreen
import com.example.guetteteskilometres.ui.screens.newevent.NewEventViewModel

fun NavGraphBuilder.newEvent(
    navigations: NewEventNavigations,
    eventRepository: EventRepository
) {
    composable<NewEvent> {
        val viewModel = NewEventViewModel(
            eventRepository = eventRepository
        )
        NewEventScreen(
            navigations = navigations,
            viewModel = viewModel
        )
    }
}