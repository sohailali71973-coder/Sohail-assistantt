package com.sohailboss.assistant

import android.content.Context
import android.content.Intent
import android.widget.Toast

class AssistantCommandRouter(
    private val context: Context,
    private val speak: (String) -> Unit
) {
    private val identity = "Mujhe Sohail Boss ne banaya hai. Main Sohail Boss ka personal assistant hoon."

    fun handle(raw: String) {
        val text = raw.trim().lowercase()

        if (asksCreator(text)) {
            speak(identity)
            return
        }

        when {
            text.contains("instagram") && (text.contains("open") || text.contains("khol")) ->
                open("com.instagram.android", "Instagram")

            text.contains("whatsapp") && (text.contains("open") || text.contains("khol")) ->
                open("com.whatsapp", "WhatsApp")

            text.contains("play store") || text.contains("playstore") ->
                open("com.android.vending", "Play Store")

            else ->
                speak("Maine suna: $raw. Is task ke liye AI action planner connect karna baaki hai.")
        }
    }

    private fun asksCreator(t: String): Boolean {
        val creatorWords = listOf(
            "who made you", "who created you", "who built you", "developer",
            "kisne banaya", "tumhe kisne banaya", "banaya kisne", "creator",
            "உன்னை யார் உருவாக்கியது", "யார் உன்னை உருவாக்கினார்",
            "谁创建了你", "誰創造了你"
        )
        return creatorWords.any { t.contains(it) }
    }

    private fun open(pkg: String, name: String) {
        val intent = context.packageManager.getLaunchIntentForPackage(pkg)
        if (intent != null) {
            context.startActivity(intent)
            speak("$name khol rahi hoon.")
        } else {
            Toast.makeText(context, "$name installed nahi hai.", Toast.LENGTH_SHORT).show()
            speak("$name installed nahi hai.")
        }
    }
}
