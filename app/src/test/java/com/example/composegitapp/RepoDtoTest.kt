package com.example.composegitapp

import com.example.composegitapp.clean_arch_comp.data.dto.RepoDto
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Test

class RepoDtoTest {

    private val gson = Gson()

    @Test
    fun deserializeRepoDtoFromJSON() {

        val json = """
            {
                "incomplete_results": false,
                "items": [
                    {
                        "id": 1,
                        "name": "TestRepo"
                    }
                ],
                "total_count": 42
            }
        """.trimIndent()

        val repoDto = gson.fromJson(json, RepoDto::class.java)

        assertEquals(false, repoDto.isIncompleteResults)
        assertEquals(42, repoDto.totalCount)
        assertEquals(1, repoDto.items?.size)
        assertEquals("TestRepo", repoDto.items?.get(0)?.name)
    }
}
