package me.crivos.app.domain.algorithm

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Estado atual do flashcard para repetição espaçada SM-2.
 * Equivalente fiel ao formato utilizado no app web Crivos (`cst` e `nextSM2`).
 */
data class SM2State(
    val ef: Double = 2.5,
    val n: Int = 0,
    val iv: Int = 0,
    val due: String = ""
)

data class SM2Result(
    val ef: Double,
    val n: Int,
    val iv: Int,
    val quality: Int
)

object SM2Engine {
    /**
     * Garante o estado inicial do card.
     */
    fun defaultState(state: SM2State? = null): SM2State {
        return state ?: SM2State(ef = 2.5, n = 0, iv = 0, due = "")
    }

    /**
     * Calcula o próximo estado SM-2 baseado no estado atual e na qualidade da resposta (0 a 5).
     * 0 - Errei feio / Esqueci completamente
     * 1 - Errei mas lembrei após ver a resposta
     * 2 - Acertei com muita dificuldade
     * 3 - Acertei com esforço razoável
     * 4 - Acertei bem
     * 5 - Perfeito / Fácil demais
     */
    fun nextSM2(card: SM2State, quality: Int): SM2Result {
        val safeQuality = max(0, min(5, quality))

        var repetitions = max(0, card.n)
        var interval = max(0, card.iv)
        var ease = if (card.ef > 1.3) card.ef else 2.5

        if (safeQuality < 3) {
            repetitions = 0
            interval = 1
        } else {
            interval = when (repetitions) {
                0 -> 1
                1 -> 6
                else -> max(1, (interval * ease).roundToInt())
            }
            repetitions += 1
        }

        ease = max(1.3, ease + 0.1 - (5 - safeQuality) * (0.08 + (5 - safeQuality) * 0.02))
        val roundedEase = BigDecimal(ease).setScale(3, RoundingMode.HALF_UP).toDouble()

        return SM2Result(
            ef = roundedEase,
            n = repetitions,
            iv = interval,
            quality = safeQuality
        )
    }
}
