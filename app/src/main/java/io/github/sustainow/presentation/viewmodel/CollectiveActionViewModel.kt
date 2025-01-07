package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.CollectiveAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate

val mocks = listOf(
    CollectiveAction(
        id=1,
        images = listOf(),
        name = "Limpeza da Praia",
        description = "Junte-se a nós para limpar a praia.",
        author = "João Silva",
        startDate = LocalDate(2023, 6, 1),
        endDate = LocalDate(2023, 6, 1),
        status = "Finalizada"
    ),
    CollectiveAction(
        id=2,
        images = listOf(),
        name = "Plantio de Árvores",
        description = "Ajude-nos a plantar árvores no parque.",
        author = "Maria Souza",
        startDate = LocalDate(2023, 7, 15),
        endDate = LocalDate(2023, 7, 15),
        status = "Em andamento"
    ),
    CollectiveAction(
        id=3,
        images = listOf(),
        name = "Campanha de Reciclagem",
        description = "Traga seus recicláveis para nossa campanha.",
        author = "Ana Pereira",
        startDate = LocalDate(2023, 8, 10),
        endDate = LocalDate(2023, 8, 10),
        status = "Finalizada"
    ),
    CollectiveAction(
        id=4,
        images = listOf(),
        name = "Horta Comunitária",
        description = "Ajude-nos a manter a horta comunitária.",
        author = "Carlos Lima",
        startDate = LocalDate(2023, 9, 5),
        endDate = LocalDate(2023, 9, 5),
        status = "Em andamento"
    ),
    CollectiveAction(
        id=5,
        images = listOf(),
        name = "Limpeza do Rio",
        description = "Junte-se a nós para limpar o rio.",
        author = "Fernanda Costa",
        startDate = LocalDate(2023, 10, 20),
        endDate = LocalDate(2023, 10, 20),
        status = "Finalizada"
    ),
    CollectiveAction(
        id=6,
        images = listOf(),
        name = "Restauração do Parque",
        description = "Ajude-nos a restaurar o parque local.",
        author = "Lucas Almeida",
        startDate = LocalDate(2023, 11, 12),
        endDate = LocalDate(2023, 11, 12),
        status = "Em andamento"
    ),
    CollectiveAction(
        id=7,
        images = listOf(),
        name = "Conservação da Vida Selvagem",
        description = "Junte-se a nós na conservação da vida selvagem.",
        author = "Mariana Fernandes",
        startDate = LocalDate(2023, 12, 1),
        endDate = LocalDate(2023, 12, 1),
        status = "Finalizada"
    ),
    CollectiveAction(
        id=8,
        images = listOf(),
        name = "Agricultura Urbana",
        description = "Ajude-nos com projetos de agricultura urbana.",
        author = "Pedro Gomes",
        startDate = LocalDate(2024, 1, 15),
        endDate = LocalDate(2024, 1, 15),
        status = "Em andamento"
    ),
    CollectiveAction(
        id=9,
        images = listOf(),
        name = "Conservação de Energia",
        description = "Aprenda sobre conservação de energia.",
        author = "Rafaela Santos",
        startDate = LocalDate(2024, 2, 10),
        endDate = LocalDate(2024, 2, 10),
        status = "Finalizada"
    ),
    CollectiveAction(
        id=10,
        images = listOf(),
        name = "Conservação da Água",
        description = "Junte-se a nós na conservação da água.",
        author = "Thiago Oliveira",
        startDate = LocalDate(2024, 3, 5),
        endDate = LocalDate(2024, 3, 5),
        status = "Em andamento"
    )
)



@HiltViewModel(assistedFactory = CollectiveActionViewModel.Factory::class)
class CollectiveActionViewModel @AssistedInject constructor(@Assisted("id") id:Int) : ViewModel(){
    private val _action = MutableStateFlow<CollectiveAction?>(null)
    val action = _action.asStateFlow()

    init {
        _action.value = mocks.find(){ it.id == id }
    }

    @AssistedFactory
        interface Factory {
            fun create(
                @Assisted("id") id: Int,
            ): CollectiveActionViewModel
        }
}