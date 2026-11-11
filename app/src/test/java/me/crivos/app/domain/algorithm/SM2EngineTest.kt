package me.crivos.app.domain.algorithm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SM2EngineTest {

    @Test
    fun `primeira resposta correta rep 1 e intervalo 1 dia`() {
        val res = SM2Engine.nextSM2(SM2State(n = 0, ef = 2.5, iv = 0), quality = 4)
        assertEquals(1, res.n)
        assertEquals(1, res.iv)
        assertEquals(2.5, res.ef, 0.001)
    }

    @Test
    fun `segunda resposta correta consecutiva intervalo 6 dias`() {
        val res = SM2Engine.nextSM2(SM2State(n = 1, ef = 2.5, iv = 1), quality = 4)
        assertEquals(2, res.n)
        assertEquals(6, res.iv)
    }

    @Test
    fun `terceira resposta intervalo round de intervalo vezes ease`() {
        val res = SM2Engine.nextSM2(SM2State(n = 2, ef = 2.5, iv = 6), quality = 4)
        assertEquals(3, res.n)
        assertEquals(15, res.iv)
    }

    @Test
    fun `erro qualidade menor que 3 reseta rep para 0 e intervalo para 1`() {
        val res = SM2Engine.nextSM2(SM2State(n = 5, ef = 2.5, iv = 30), quality = 1)
        assertEquals(0, res.n)
        assertEquals(1, res.iv)
        assertTrue(res.ef < 2.5)
        assertTrue(res.ef >= 1.3)
    }

    @Test
    fun `ease nunca cai abaixo de 1_3`() {
        var card = SM2State(n = 0, ef = 1.4, iv = 0)
        for (i in 0 until 5) {
            val r = SM2Engine.nextSM2(card, quality = 0)
            card = SM2State(n = r.n, ef = r.ef, iv = r.iv)
        }
        assertEquals(1.3, card.ef, 0.001)
    }

    @Test
    fun `qualidade eh limitada ao intervalo 0 a 5`() {
        assertEquals(5, SM2Engine.nextSM2(SM2State(n = 0, ef = 2.5, iv = 0), 99).quality)
        assertEquals(0, SM2Engine.nextSM2(SM2State(n = 0, ef = 2.5, iv = 0), -3).quality)
    }

    @Test
    fun `defaultState devolve padroes corretos`() {
        val def = SM2Engine.defaultState(null)
        assertEquals(2.5, def.ef, 0.001)
        assertEquals(0, def.n)
        assertEquals(0, def.iv)
        assertEquals("", def.due)
    }
}
