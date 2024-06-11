package com.example.hethonglora

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hethonglora.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener {
            val user = binding.signupEmail.text.toString().trim()
            val pass = binding.signupPassword.text.toString().trim()

            if (user.isEmpty()) {
                binding.signupEmail.error = "Không để email trống"
                return@setOnClickListener
            }
            if (pass.isEmpty()) {
                binding.signupPassword.error = "Không để mật khẩu trống"
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(this@MainActivity, "Đăng ký thất bại" + task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.loginRedirectText.setOnClickListener {
            startActivity(Intent(this@MainActivity, dangnhap::class.java))
        }
    }
}