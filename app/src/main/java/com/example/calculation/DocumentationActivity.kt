package com.example.calculation

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class DocumentationActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_documentation)

        webView = findViewById(R.id.webView)
        val closeButton = findViewById<ImageButton>(R.id.closeButton)
        
        closeButton.setOnClickListener {
            finish()
        }

        webView.settings.javaScriptEnabled = true
        loadDocumentation()
    }

    private fun loadDocumentation() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL("https://raw.githubusercontent.com/dasheca/calculator-docs/32795881eaa360dc90506c4985546ae50ed343e5/calculator_docs.md")
                val markdownContent = url.readText()

                withContext(Dispatchers.Main) {
                    val styledHtml = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <style>
                                body {
                                    font-family: 'Roboto', sans-serif;
                                    line-height: 1.6;
                                    color: #333;
                                    padding: 20px;
                                    background-color: #f5f5f5;
                                }
                                
                                .container {
                                    max-width: 800px;
                                    margin: 0 auto;
                                    background: white;
                                    padding: 20px;
                                    border-radius: 8px;
                                    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                                }
                                
                                h1 {
                                    color:rgb(243, 33, 138);
                                    border-bottom: 2px solid #2196F3;
                                    padding-bottom: 10px;
                                    margin-bottom: 30px;
                                }
                                
                                h2 {
                                    color: #1976D2;
                                    margin-top: 30px;
                                    border-bottom: 1px solid #1976D2;
                                    padding-bottom: 5px;
                                }
                                
                                h3 {
                                    color: #1565C0;
                                    margin-top: 20px;
                                }
                                
                                h4 {
                                    color: #0D47A1;
                                    margin: 10px 0;
                                }
                                
                                .function, .instruction-block, .function-block, .features-block, .tips-block, .support-block {
                                    background: #F5F5F5;
                                    padding: 15px 20px;
                                    border-radius: 8px;
                                    margin: 15px 0;
                                    border: 1px solid #E0E0E0;
                                }
                                
                                ul {
                                    padding-left: 20px;
                                    list-style-type: disc;
                                    margin: 10px 0;
                                }
                                
                                li {
                                    margin: 8px 0;
                                }
                                
                                strong {
                                    color: #1565C0;
                                }
                                
                                p {
                                    margin: 10px 0;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                $markdownContent
                            </div>
                        </body>
                        </html>

                    """.trimIndent()

                    webView.loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@DocumentationActivity,
                        "Ошибка загрузки документации: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}