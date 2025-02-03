package io.github.sustainow.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.sustainow.repository.actions.CollectiveActionRepository
import io.github.sustainow.repository.actions.CollectiveActionRepositorySupabaseImp
import io.github.sustainow.repository.formulary.FormularyRepository
import io.github.sustainow.repository.formulary.FormularyRepositorySupabaseImp
import io.github.sustainow.service.calculation.CalculationService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideFormularyRepository(calculationService: CalculationService,supabaseClient: SupabaseClient): FormularyRepository {
        return FormularyRepositorySupabaseImp(
            supabaseClient,
            calculationService = calculationService,
            formularyTableName = "form",
            answerTableName = "form_answer",
            questionTableName = "question",
            questionDependencyTableName = "dependent_question",
            alternativeTableName = "question_alternative",
        )
    }
    @Provides
    @Singleton
    fun provideCollectiveActionRepository(context: Context, supabaseClient: SupabaseClient): CollectiveActionRepository{
        return CollectiveActionRepositorySupabaseImp(
            supabaseClient,
            actionTableName = "action",
            invitationTableName = "action_invitation",
            memberTableName = "action_member",
            memberActivityTableName = "action_member_activity",
            usernameTableName = "user_name",
            context
        )
    }
}
