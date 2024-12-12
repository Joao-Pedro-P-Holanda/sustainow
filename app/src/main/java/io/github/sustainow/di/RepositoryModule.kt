package io.github.sustainow.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.sustainow.repository.formulary.FormularyRepository
import io.github.sustainow.repository.formulary.FormularyRepositorySupabaseImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideFormularyRepository(supabaseClient: SupabaseClient): FormularyRepository {
        return FormularyRepositorySupabaseImp(
            supabaseClient,
            formularyTableName = "form",
            answerTableName = "form_answer",
            questionTableName = "question",
            questionDependencyTableName = "dependent_question",
            alternativeTableName = "question_alternative",
        )
    }
}
