package co.kezzi.cst

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log

interface UtteranceListener {
    fun onInitialized()
    fun onStart()
    fun onCompleted()
    fun onError()
}

class Speaker(context: Context, listener: UtteranceListener?) {
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                listener?.onInitialized()
            }
        }

        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String) {
                listener?.onStart()
            }

            override fun onDone(utteranceId: String) {
                listener?.onCompleted()
            }

            override fun onError(utteranceId: String) {
                listener?.onError()
            }
        })
    }

    fun isInitialized(): Boolean {
        return isInitialized
    }

    fun synthesizeUtterance(text: String) {
        if (isInitialized) {
            textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
        }
    }

    fun release() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

}




object Voice : UtteranceListener {
    lateinit var speaker: Speaker

    private val tag = "LEE"

    private val queue = ArrayDeque<String>()

    override fun onInitialized() {
        Log.d(tag, "Initialized")
// Check if the synthesizer is initialized
        if (speaker.isInitialized()) {
            // Synthesize the utterance
            speaker.synthesizeUtterance("Text to speech initialized")

        }
    }

    override fun onStart() {
        Log.d(tag, "onStart")
    }

    override fun onCompleted() {
        Log.d(tag, "onCompleted")

        val next = queue.removeFirstOrNull()

        if(next != null) {
            speaker.synthesizeUtterance(next)
        }

    }

    override fun onError() {
        Log.d(tag, "onError")
    }

    fun speak(text:String) {
        if (queue.isEmpty()) {
            speaker.synthesizeUtterance(text)
        } else if(queue.count() < 4) {
            queue.add(text)
        }
    }
}
//textToSpeech.release()
